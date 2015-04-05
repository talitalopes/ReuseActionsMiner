package br.ufrj.cos.prisma.miner.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minerv1.Activity;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkProcess;
import minerv1.Minerv1Factory;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ApplicationFileWalker extends BaseFileWalker {

	FrameworkProcess process;
	Map<String, Event> eventsMap;
	List<Event> applicationReuseActions;
	
	public ApplicationFileWalker(FrameworkProcess process) {
		super();
		this.process = process;
		this.eventsMap = new HashMap<String, Event>();
		this.applicationReuseActions = new ArrayList<Event>();
	}

	public List<Event> getReuseActions() {
		// update dependencies events
		
		for (int i = 0; i < this.applicationReuseActions.size(); i++) {
			Event e = this.applicationReuseActions.get(i);
			
			List<EventDependency> eventsToRemove = new ArrayList<EventDependency>();
			for (int j = 0; j < e.getDependencies().size(); j++) {
				EventDependency d = e.getDependencies().get(j);
				Event depEvent = this.eventsMap.get(d.getId());
				if (depEvent == null) {
					eventsToRemove.add(d);
				} else if (depEvent.getId().equals(e.getId())) {
					eventsToRemove.add(d);
				} else {
					d.setEvent(depEvent);
					if (!e.getDependencies().contains(d)) {
						e.getDependencies().set(j, d);
					}
				}
			}
			
			e.getDependencies().removeAll(eventsToRemove);
			Set<EventDependency> removeDuplicates = new HashSet<EventDependency>();
			removeDuplicates.addAll(e.getDependencies());
			e.getDependencies().clear();
			e.getDependencies().addAll(removeDuplicates);
		}
		
		return this.applicationReuseActions;
	}

	public void walk(String path) {
//		if (this.process == null) {
//			LogHelper.log("No process found");
//			return;
//		}
//		if (this.process.getActivitiesMap() == null) {
//			LogHelper.log("Map is null");
//			return;
//		}

		super.walk(path);
	}

	@Override
	protected void getClassInfo(String fileContent) {
		parser.setSource(fileContent.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit compilationUnit = (CompilationUnit) parser
				.createAST(null);
		compilationUnit.accept(new ReuseMinerASTVisitor());
	}
	
	class ReuseMinerMethodsVisitor extends ASTVisitor {
		
	}
	
	class ReuseMinerASTVisitor extends ASTVisitor {
		String packageName;
		String appClassName;

		Event reuseClassOrInterfaceEvent;
		Set<String> imports;
	
		public ReuseMinerASTVisitor() {
			this.reuseClassOrInterfaceEvent = Minerv1Factory.eINSTANCE.createEvent();
			this.imports = new HashSet<String>();
		}
		
		public String getPackage() {
			return this.packageName;
		}

		public void updateReuseClassOrInterfaceEvent(String eventId, Activity activity) {
			reuseClassOrInterfaceEvent.setId(eventId);
    		reuseClassOrInterfaceEvent.setActivity(activity);
    		eventsMap.put(appClassName, reuseClassOrInterfaceEvent);
    		applicationReuseActions.add(reuseClassOrInterfaceEvent);	
		}
		
		public boolean visit(PackageDeclaration node) {
			this.packageName = node.getName().getFullyQualifiedName();
			return true;
		}
		
		@SuppressWarnings("unchecked")
		public boolean checkInterfaceImplementations(String eventId, TypeDeclaration node) {
			List<SimpleType> types = (List<SimpleType>) node.superInterfaceTypes();
			
	        for (int typesIndex = 0; typesIndex < types.size(); typesIndex++) {
	        	String interfaceName = types.get(typesIndex).getName().toString();
	        	
	        	Activity activity = findSuperClassOrInterface(interfaceName);
	        	if (activity != null) {
	        		this.updateReuseClassOrInterfaceEvent(eventId, activity);
	        	}
	        }
	        
			return true;
		}

		public boolean visit(TypeDeclaration node) {
			appClassName = node.getName().getFullyQualifiedName();
			System.out.println("---------\nClass name: " + appClassName);
			
			String eventId = String.format("%s.%s", this.getPackage(),
					appClassName);
			
			Type superclass = node.getSuperclassType();
			if (superclass == null) {
				// Check if node implements any interface
				return this.checkInterfaceImplementations(eventId, node);
			}
			
			Activity activity = findSuperClassOrInterface(superclass.toString());
			if (activity == null) {
				return true;
			}
			this.updateReuseClassOrInterfaceEvent(eventId, activity);
			return true;
		}
		
		public Activity findSuperClassOrInterface(String typeName) {
			for (String importName : imports) {
				if (importName.contains(typeName)) {
					if (process.getActivitiesMap().get(
							typeName) != null) {
						int activityIndex = process.getActivitiesMap().get(
								typeName);
						return process.getActivities().get(activityIndex);
					}
				}
			}
			return null;
		}

		public boolean visit(ImportDeclaration importDeclaration) {
			String importName = importDeclaration.getName()
					.getFullyQualifiedName();
			imports.add(importName);
			return true;
		}

		public boolean visit(MethodDeclaration node) {
			if (node.getBody() != null) {

				if (parseMethods) {
//						 Event reuseMethod =
//						 Minerv1Factory.eINSTANCE.createEvent();
//						 reuseMethod.setId(node.getName().getFullyQualifiedName());
//						 appClass.addMethod(reuseMethod);
				}

				Block block = node.getBody();
				block.accept(new ASTVisitor() {
					
					public boolean visit(ClassInstanceCreation node) {
						EventDependency dep = Minerv1Factory.eINSTANCE
								.createEventDependency();
						dep.setId(node.getType().toString());
											
						if (dep.getId() == null || dep.getId().equals(appClassName)) {
							System.out.println("Dep: " + dep.getId());
							System.out.println("reuseClassOrInterfaceEvent" + appClassName);
							return true;
						}
						
						if (!reuseClassOrInterfaceEvent.getDependencies().contains(dep)) {
							reuseClassOrInterfaceEvent.getDependencies().add(dep);
						}
						return true;
					}

//						public boolean visit(MethodInvocation node) {
//
//							for (int i = 0; i < node.arguments().size(); i++) {
//								Expression expressionArg = (Expression) node
//										.arguments().get(i);
//								expressionArg.accept(new ASTVisitor() {
//
//									public boolean visit(
//											ClassInstanceCreation node) {
//										EventDependency dep = Minerv1Factory.eINSTANCE
//												.createEventDependency();
//										dep.setId(node.getType().toString());
//										reuseClassEvent.getDependencies().add(
//												dep);
//										return true;
//									}
//								});
//
//							}
//
//							return true;
//						}
				});
			}

			return true;
		}
	}
}
