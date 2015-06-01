package br.ufrj.cos.prisma.miner.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minerv1.Activity;
import minerv1.Commit;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkProcess;
import minerv1.Minerv1Factory;

import org.eclipse.jdt.core.Flags;
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

import br.ufrj.cos.prisma.miner.util.ReuseMinerClassASTVisitor.ApplicationClass;

public class ApplicationFileWalker extends BaseFileWalker {

	FrameworkProcess process;
	Map<String, Event> eventsMap;
	List<Event> applicationReuseActions;
	List<Event> applicationEvents;
	Map<String, String> superclassesMap;

	Map<String, ApplicationClass> applicationClasses;
	Commit currentCommit;

	public ApplicationFileWalker(FrameworkProcess process, Commit commit) {
		super();
		this.process = process;
		this.eventsMap = new HashMap<String, Event>();
		this.applicationReuseActions = new ArrayList<Event>();
		this.applicationEvents = new ArrayList<Event>();
		this.superclassesMap = new HashMap<String, String>();

		this.applicationClasses = new HashMap<String, ApplicationClass>();
		this.currentCommit = commit;
	}

	public List<Event> getReuseActions() {
		this.createMainEvents();
		this.createDependencies();
		return this.applicationReuseActions;
	}

	public void walk(String path) {
		super.walk(path);
	}

	@Override
	protected void getClassInfo(String className, String fileContent) {
		parser.setSource(fileContent.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit compilationUnit = (CompilationUnit) parser
				.createAST(null);
		ReuseMinerClassASTVisitor visitor = new ReuseMinerClassASTVisitor(
				className, this);
		compilationUnit.accept(visitor);
	}

	public Activity getFrameworkActivity(String typeName) {
		if (process.getActivitiesMap().get(typeName) != null) {
			int activityIndex = process.getActivitiesMap().get(typeName);
			return process.getActivities().get(activityIndex);
		}
		return null;
	}

	public String getBaseClass(ApplicationClass appClass) {
		String superclassname = appClass.superClassName;

		while (this.applicationClasses.containsKey(superclassname)) {
			appClass = this.applicationClasses.get(superclassname);
			superclassname = appClass.superClassName;
		}

		if (appClass.superClassName == null) {
			return null;
		}

		String[] superClassNameParts = appClass.superClassName.split("\\.");
		String superclasskey = appClass.superClassName;
		if (superClassNameParts.length > 1) {
			superclasskey = superClassNameParts[superClassNameParts.length - 1];
		}
		return superclasskey;
	}

	public void createMainEvents() {
		for (ApplicationClass appClass : this.applicationClasses.values()) {

			if (appClass.superClassName != null) {
				String baseClass = this.getBaseClass(appClass);
				Event reuseActionEvent = this.getEventForClass(baseClass,
						appClass);
				if (reuseActionEvent != null && !appClass.isAbstract) {
					this.applicationReuseActions.add(reuseActionEvent);
				} else {
					System.out.println(String.format("NF Event for superclass:: %s extends %s",
							appClass.appClassName, baseClass));
				}
								
			} else {
				if (appClass.interfacesDependencies.size() == 1) {
					String interfaceName = appClass.interfacesDependencies.iterator().next();
					Event reuseActionEvent = this.getEventForClass(interfaceName,
							appClass);
					if (reuseActionEvent != null && !appClass.isAbstract) {
						this.applicationReuseActions.add(reuseActionEvent);
					} else {
//						System.out.println(String.format("NF:: %s extends %s",
//								appClass.appClassName, interfaceName));
					}
				} else {
					// TODO: deal with multiple interfaces
				}
			}
			
			for (String methodId: appClass.methodsDependencies) {
				Activity fwActivity = this.getFrameworkActivity(methodId);
				if (fwActivity == null) {
					continue;
				}
				
				System.out.println("Found activity for method");
				Event reuseActionEvent = Minerv1Factory.eINSTANCE.createEvent();
				reuseActionEvent.setId(methodId);
				reuseActionEvent.setActivity(fwActivity);
				this.currentCommit.getEvents().add(reuseActionEvent);	
				this.eventsMap.put(methodId, reuseActionEvent);
			}
		}
	}

	private Event getEventForClass(String name, ApplicationClass appClass) {
		if (appClass == null) {
			return null;
		}

		if (this.eventsMap.containsKey(name)) {
			return this.eventsMap.get(name);
		}

		Activity fwActivity = this.getFrameworkActivity(name);
		if (fwActivity != null) {
			Event reuseActionEvent = Minerv1Factory.eINSTANCE.createEvent();
			reuseActionEvent.setId(appClass.getCompleteAppName());
			reuseActionEvent.setActivity(fwActivity);
			this.eventsMap.put(appClass.appClassName, reuseActionEvent);
			return reuseActionEvent;
		}

		return null;
	}

	private void createDependencies() {
		for (ApplicationClass appClass : this.applicationClasses.values()) {
			
			List<String> dependencies = new ArrayList<String>();
			dependencies.addAll(appClass.classDependencies);	
			
			if (!this.eventsMap.containsKey(appClass.appClassName)) {
				continue;
			}
			
			Event appClassEvent = this.eventsMap.get(appClass.appClassName);
			Set<String> superClassAndInterfaces = new HashSet<String>();
			superClassAndInterfaces.add(appClass.appClassName);
			superClassAndInterfaces.addAll(appClass.interfacesDependencies);
			
			for (String methodDependencyId : appClass.methodsDependencies) {
		
				if (process.getActivitiesMap().containsKey(methodDependencyId)) {
					
					 Event e = this.eventsMap.get(methodDependencyId);
					 if (e != null) {
						 
						 if (!e.getActivity().getParent().equals(appClass.superClassName)) {
							 System.out.println("Method Difference: " + e.getActivity().getParent() + " != " + appClass.superClassName);
							 continue;
						 } else {
							 System.out.println("Method found: " + e.getCompleteName());
						 }
						 
						 EventDependency dep = Minerv1Factory.eINSTANCE
									.createEventDependency();
						dep.setEvent(e);
						dep.setId(methodDependencyId);
	
						String eventName = e.getId();
						if (!eventName.equals(appClass.appClassName)) {
							appClassEvent.getDependencies().add(dep);
							System.out.println("Event found: " + e.getCompleteName());
						}
					 } else {
						 System.out.println("Method not found: " + methodDependencyId);
					 }
				 }
			}
			
			for (String interfaceDependency : appClass.interfacesDependencies) {
				if (process.getActivitiesMap().containsKey(interfaceDependency)) {
					 Event e = this.eventsMap.get(interfaceDependency);
					 if (e != null) {
						 
						 if (!e.getActivity().getParent().equals(appClass.superClassName)) {
							 System.out.println("Interface Difference: " + e.getActivity().getParent() + " != " + appClass.superClassName);
							 continue;
						 } else {
							 System.out.println("Interface found: " + e.getCompleteName());
						 }
						 
						 
						 EventDependency dep = Minerv1Factory.eINSTANCE
									.createEventDependency();
						dep.setEvent(e);
						dep.setId(interfaceDependency);
	
						String eventName = e.getId();
						if (!eventName.equals(appClass.appClassName)) {
							appClassEvent.getDependencies().add(dep);
							System.out.println("Event found: " + e.getCompleteName());
						}
					 }
				 }
			}
			
			for (String dependencyClass : dependencies) {
				
					Event e = this.eventsMap.get(appClass.appClassName);
					ApplicationClass depClass = this.applicationClasses
							.get(dependencyClass);
					Event dependencyEvent = this.getEventForClass(
							dependencyClass, depClass);

					EventDependency dep = Minerv1Factory.eINSTANCE
							.createEventDependency();
					dep.setEvent(dependencyEvent);
					dep.setId(dependencyClass);

					String[] nameParts = e.getId().split("\\.");
					String eventName = nameParts[nameParts.length - 1];

					if (!eventName.equals(dependencyClass)) {
						e.getDependencies().add(dep);
					}

//					System.out.println(String.format("DEP: " + eventName
//							+ " : " + dependencyClass));
			}
		}
	}

	public void printDependencies(FrameworkProcess process) {
		for (ApplicationClass appClass : this.applicationClasses.values()) {

			if (appClass.classDependencies.size() == 0) {
				continue;
			}

			if (appClass.superClassName != null) {
				String baseClass = this.getBaseClass(appClass);
				Activity fwActivity = this.getFrameworkActivity(baseClass);
				if (fwActivity != null) {
					Event reuseActionEvent = Minerv1Factory.eINSTANCE
							.createEvent();
					reuseActionEvent.setId(appClass.getCompleteAppName());
					reuseActionEvent.setActivity(fwActivity);
					System.out.println(String.format("%s extends %s",
							appClass.appClassName, baseClass));
					// this.applicationReuseActions.add(reuseActionEvent);
					// this.eventsMap.put(baseClass, reuseActionEvent);

				} else {
					System.out.println(String.format("NF:: %s extends %s",
							appClass.appClassName, baseClass));
				}
			}

			for (String dependencyClass : appClass.classDependencies) {
				if (process.getActivitiesMap().containsKey(dependencyClass)) {
					if (this.eventsMap.containsKey(dependencyClass)) {
						Event e = this.eventsMap.get(dependencyClass);

						EventDependency dep = Minerv1Factory.eINSTANCE
								.createEventDependency();
						dep.setEvent(e);
						dep.setId(dependencyClass);

						e.getDependencies().add(dep);

					} else {
						System.out.println("\t NF FW: " + dependencyClass);
					}
					continue;
				}

				// Get base class
				if (!this.applicationClasses.containsKey(dependencyClass)) {
					continue;
				}

				ApplicationClass depAppClass = this.applicationClasses
						.get(dependencyClass);
				String superclassname = depAppClass.superClassName;
				while (this.applicationClasses.containsKey(superclassname)) {
					depAppClass = this.applicationClasses.get(superclassname);
					superclassname = depAppClass.superClassName;
				}

				if (depAppClass.superClassName == null) {
					continue;
				}

				String[] superClassNameParts = depAppClass.superClassName
						.split("\\.");
				String superclasskey = depAppClass.superClassName;
				if (superClassNameParts.length > 1) {
					superclasskey = superClassNameParts[superClassNameParts.length - 1];
				}

				if (process.getActivitiesMap().containsKey(superclasskey)) {
					// System.out.println("\t FW: " + depAppClass.appClassName);
					continue;
				} else {
					// System.out.println("\t NF: " + superclasskey);
				}

			}

			for (String dependencyInterface : appClass.interfacesDependencies) {
				System.out.println("\t I: " + dependencyInterface);
			}

		}
	}

	// class ReuseMinerMethodsASTVisitor extends ASTVisitor {
	//
	// public boolean visit(ClassInstanceCreation node) {
	// EventDependency dep = Minerv1Factory.eINSTANCE
	// .createEventDependency();
	// dep.setId(node.getType().toString());
	//
	// if (dep.getId() == null || dep.getId().equals(appClassName)) {
	// System.out.println("Dep: " + dep.getId());
	// System.out.println("reuseClassOrInterfaceEvent" + appClassName);
	// return true;
	// }
	//
	// if (!reuseClassOrInterfaceEvent.getDependencies().contains(dep)) {
	// reuseClassOrInterfaceEvent.getDependencies().add(dep);
	// }
	// return true;
	// }
	// }

	class ReuseMinerASTVisitor extends ASTVisitor {
		String packageName;
		String appClassName;

		Event reuseClassOrInterfaceEvent;
		Set<String> imports;

		public ReuseMinerASTVisitor() {
			this.imports = new HashSet<String>();
		}

		public String getPackage() {
			return this.packageName;
		}

		public boolean visit(PackageDeclaration node) {
			this.packageName = node.getName().getFullyQualifiedName();
			return true;
		}

		@SuppressWarnings("unchecked")
		public boolean checkInterfaceImplementations(String eventId,
				TypeDeclaration node) {
			List<SimpleType> types = (List<SimpleType>) node
					.superInterfaceTypes();

			for (int typesIndex = 0; typesIndex < types.size(); typesIndex++) {
				String interfaceName = types.get(typesIndex).getName()
						.toString();

				Activity activity = findSuperClassOrInterface(interfaceName);
				if (activity != null) {
					boolean isAbstract = Flags.isAbstract(node.getModifiers());
					this.updateReuseClassOrInterfaceEvent(eventId, activity,
							isAbstract);
				}
			}

			return true;
		}

		public boolean visit(TypeDeclaration node) {
			this.appClassName = node.getName().getFullyQualifiedName();
			this.reuseClassOrInterfaceEvent = Minerv1Factory.eINSTANCE
					.createEvent();

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
				// did not find framework reuse action. It can be a subclass of
				// a
				// framework application that extends a framework class
				reuseClassOrInterfaceEvent.setId(eventId + "+"
						+ superclass.toString() + "+" + appClassName);

				applicationEvents.add(reuseClassOrInterfaceEvent);
				return true;
			}
			boolean isAbstract = Flags.isAbstract(node.getModifiers());
			this.updateReuseClassOrInterfaceEvent(eventId, activity, isAbstract);
			return true;
		}

		public void updateReuseClassOrInterfaceEvent(String eventId,
				Activity activity, boolean isAbstract) {
			reuseClassOrInterfaceEvent.setId(eventId);
			reuseClassOrInterfaceEvent.setActivity(activity);
			eventsMap.put(appClassName, reuseClassOrInterfaceEvent);

			if (!isAbstract) {
				applicationReuseActions.add(reuseClassOrInterfaceEvent);
			}
		}

		public Activity findSuperClassOrInterface(String typeName) {
			for (String importName : imports) {
				if (importName.contains(typeName)) {
					if (process.getActivitiesMap().get(typeName) != null) {
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
					// Event reuseMethod =
					// Minerv1Factory.eINSTANCE.createEvent();
					// reuseMethod.setId(node.getName().getFullyQualifiedName());
					// appClass.addMethod(reuseMethod);
				}

				Block block = node.getBody();
				block.accept(new ASTVisitor() {

					public boolean visit(ClassInstanceCreation node) {
						EventDependency dep = Minerv1Factory.eINSTANCE
								.createEventDependency();
						dep.setId(node.getType().toString());

						if (dep.getId() == null
								|| dep.getId().equals(appClassName)) {
							System.out.println("Dep: " + dep.getId());
							System.out.println("reuseClassOrInterfaceEvent"
									+ appClassName);
							return true;
						}

						if (!reuseClassOrInterfaceEvent.getDependencies()
								.contains(dep)) {
							reuseClassOrInterfaceEvent.getDependencies().add(
									dep);
						}
						return true;
					}

					// public boolean visit(MethodInvocation node) {
					//
					// for (int i = 0; i < node.arguments().size(); i++) {
					// Expression expressionArg = (Expression) node
					// .arguments().get(i);
					// expressionArg.accept(new ASTVisitor() {
					//
					// public boolean visit(
					// ClassInstanceCreation node) {
					// EventDependency dep = Minerv1Factory.eINSTANCE
					// .createEventDependency();
					// dep.setId(node.getType().toString());
					// reuseClassEvent.getDependencies().add(
					// dep);
					// return true;
					// }
					// });
					//
					// }
					//
					// return true;
					// }
				});
			}

			return true;
		}
	}
}
