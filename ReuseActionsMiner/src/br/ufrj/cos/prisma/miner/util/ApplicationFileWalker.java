package br.ufrj.cos.prisma.miner.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkProcess;
import minerv1.Minerv1Factory;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ApplicationFileWalker extends BaseFileWalker {

	FrameworkProcess process;
	List<Event> applicationReuseActions;
	Set<String> visited;
	
	public ApplicationFileWalker(FrameworkProcess process) {
		super();
		this.process = process;
		this.applicationReuseActions = new ArrayList<Event>();
		this.visited = new HashSet<String>();
	}

	public List<Event> getReuseActions() {
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
		compilationUnit.accept(new ASTVisitor() {
			Event reuseClassEvent = Minerv1Factory.eINSTANCE.createEvent();
			Set<String> imports = new HashSet<String>();

			String packageName;
			String appClassName;

			public boolean visit(PackageDeclaration node) {
				packageName = node.getName().getFullyQualifiedName();
				return false;
			}
			
			public boolean visit(TypeDeclaration node) {
				appClassName = node.getName().getFullyQualifiedName();
				String eventId = String.format("%s.%s", packageName,
						appClassName);
				
				Type superclass = node.getSuperclassType(); 
				
				if (superclass == null) {
					// Check if node implements any interface
					List<SimpleType> types = (List<SimpleType>) node.superInterfaceTypes();
					
			        for (int bIndex = 0; bIndex < types.size(); bIndex++) {
			        	String interfaceName = types.get(bIndex).getName().toString();
			        	int index = findSuperClassOrInterface(interfaceName);
			        	if (index != -1) {
			        		reuseClassEvent.setId(eventId);
			        		reuseClassEvent.setActivity(process.getActivities()
								.get(index));
			        		applicationReuseActions.add(reuseClassEvent);
			        	}
			        }
			        
					visited.add(node.getName().getIdentifier());
					return false;
					
				} else {
					int index = findSuperClassOrInterface(superclass.toString());
					if (index == -1) {
						visited.add(node.getName().getIdentifier());
						return false;
					}
					reuseClassEvent.setId(eventId);
					reuseClassEvent.setActivity(process.getActivities()
							.get(index));
					applicationReuseActions.add(reuseClassEvent);
				}
				
				// Mark as visited
				visited.add(node.getName().getIdentifier());

				return true;
			}
			
			public int findSuperClassOrInterface(String typeName) {
				for (String importName : imports) {
					if (importName.contains(typeName)) {
						System.out.println("Search superclass: " + typeName);
						if (process.getActivitiesMap().get(
								typeName) != null) {
							
							System.out.println("Found Superclass: " + typeName);
							
							int index = process.getActivitiesMap().get(
									typeName);
							return index;
						}
					}
				}
				return -1;
			}

			public boolean visit(ImportDeclaration importDeclaration) {
				String importName = importDeclaration.getName()
						.getFullyQualifiedName();
				imports.add(importName);
				return false;
			}

			public boolean visit(MethodDeclaration node) {
				if (visited.contains(node.getName().getIdentifier())) {
					return false;
				}

				if (node.getBody() != null) {

					if (parseMethods) {
						// Event reuseMethod =
						// Minerv1Factory.eINSTANCE.createEvent();
						// method.setName(node.getName().getFullyQualifiedName());
						// appClass.addMethod(method);
					}

					// Mark as visited
					visited.add(node.getName().getIdentifier());

					Block block = node.getBody();
					block.accept(new ASTVisitor() {

						public boolean visit(ClassInstanceCreation node) {
							EventDependency dep = Minerv1Factory.eINSTANCE
									.createEventDependency();
							dep.setId(node.getType().toString());
							reuseClassEvent.getDependencies().add(dep);
							return true;
						}

						public boolean visit(MethodInvocation node) {

							for (int i = 0; i < node.arguments().size(); i++) {
								Expression expressionArg = (Expression) node
										.arguments().get(i);
								expressionArg.accept(new ASTVisitor() {

									public boolean visit(
											ClassInstanceCreation node) {
										EventDependency dep = Minerv1Factory.eINSTANCE
												.createEventDependency();
										dep.setId(node.getType().toString());
										reuseClassEvent.getDependencies().add(
												dep);
										return true;
									}
								});

							}

							return true;
						}
					});
				}

				return true;
			}

		});
	}
}
