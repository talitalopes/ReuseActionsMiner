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

	public ApplicationFileWalker(FrameworkProcess process) {
		super();
		this.process = process;
		this.eventsMap = new HashMap<String, Event>();
		this.applicationReuseActions = new ArrayList<Event>();
		this.applicationEvents = new ArrayList<Event>();
		this.superclassesMap = new HashMap<String, String>();

		this.applicationClasses = new HashMap<String, ApplicationClass>();
	}

	public List<Event> getReuseActions() {
		this.createMainEvents();
		this.createDependencies();
		 
		// update dependencies events

		// for (Event e: this.applicationEvents) {
		// String[] idParts = e.getId().split("\\+");
		// if (idParts.length <= 1) {
		// continue;
		// }
		//
		// if (this.eventsMap.get(idParts[1]) != null) {
		// e.setId(idParts[0]);
		// e.setActivity(this.eventsMap.get(idParts[1]).getActivity());
		// this.eventsMap.put(idParts[2], e);
		// this.applicationReuseActions.add(e);
		// }
		// }
		//
		// for (int i = 0; i < this.applicationReuseActions.size(); i++) {
		// Event e = this.applicationReuseActions.get(i);
		//
		// List<EventDependency> eventsToRemove = new
		// ArrayList<EventDependency>();
		// for (int j = 0; j < e.getDependencies().size(); j++) {
		// EventDependency d = e.getDependencies().get(j);
		// Event depEvent = this.eventsMap.get(d.getId());
		// if (depEvent == null) {
		// eventsToRemove.add(d);
		// } else if (depEvent.getId().equals(e.getId())) {
		// eventsToRemove.add(d);
		// } else {
		// d.setEvent(depEvent);
		// if (!e.getDependencies().contains(d)) {
		// e.getDependencies().set(j, d);
		// }
		// }
		// }
		//
		// e.getDependencies().removeAll(eventsToRemove);
		// Set<EventDependency> removeDuplicates = new
		// HashSet<EventDependency>();
		// removeDuplicates.addAll(e.getDependencies());
		// e.getDependencies().clear();
		// e.getDependencies().addAll(removeDuplicates);
		// }

		return this.applicationReuseActions;
	}

	public void walk(String path) {
		// if (this.process == null) {
		// LogHelper.log("No process found");
		// return;
		// }
		// if (this.process.getActivitiesMap() == null) {
		// LogHelper.log("Map is null");
		// return;
		// }

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

			// if (appClass.classDependencies.size() == 0) {
			// continue;
			// }

			if (appClass.superClassName != null) {
				String baseClass = this.getBaseClass(appClass);
				Event reuseActionEvent = this.getEventForClass(baseClass,
						appClass);
				if (reuseActionEvent != null) {
					this.applicationReuseActions.add(reuseActionEvent);
				} else {
					System.out.println(String.format("NF:: %s extends %s",
							appClass.appClassName, baseClass));
				}
				// Activity fwActivity = this.getFrameworkActivity(baseClass);
				// if (fwActivity != null) {
				// Event reuseActionEvent =
				// Minerv1Factory.eINSTANCE.createEvent();
				// reuseActionEvent.setId(appClass.getCompleteAppName());
				// reuseActionEvent.setActivity(fwActivity);
				// System.out.println(String.format("%s extends %s",
				// appClass.appClassName, baseClass));
				//
				//
				// this.eventsMap.put(appClass.appClassName, reuseActionEvent);
				//
				// } else {
				// System.out.println(String.format("NF:: %s extends %s",
				// appClass.appClassName, baseClass));
				// }
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

			for (String dependencyClass : appClass.classDependencies) {
				// if (process.getActivitiesMap().containsKey(dependencyClass))
				// {
				if (this.eventsMap.containsKey(appClass.appClassName)) {
					Event e = this.eventsMap.get(appClass.appClassName);
					ApplicationClass depClass = this.applicationClasses
							.get(dependencyClass);
					Event dependencyEvent = this.getEventForClass(
							dependencyClass, depClass);

					if (dependencyEvent == null) {
						if (process.getActivitiesMap().get(dependencyClass) != null) {
							int classIndex = process.getActivitiesMap().get(dependencyClass);
							Activity fwActivity = process.getActivities().get(classIndex);
							
							dependencyEvent = Minerv1Factory.eINSTANCE.createEvent();
							dependencyEvent.setId(fwActivity.getId());
							dependencyEvent.setActivity(fwActivity);
							
							this.applicationReuseActions.add(dependencyEvent);
							
							System.out.println("Can't find but fw class: " + appClass + " : " + dependencyClass);
						} else {
							System.out.println("Cant find class: "
								+ dependencyClass);
							continue;
						}
						
						// this.applicationClasses.get(dependencyClass).getCompleteAppName();
					}

					EventDependency dep = Minerv1Factory.eINSTANCE
							.createEventDependency();
					dep.setEvent(dependencyEvent);
					dep.setId(dependencyClass);

					String[] nameParts = e.getId().split("\\.");
					String eventName = nameParts[nameParts.length - 1];

					if (!eventName.equals(dependencyClass)) {
						e.getDependencies().add(dep);
					}

					System.out.println(String.format("DEP: " + eventName
							+ " : " + dependencyClass));

				} else {
					System.out.println("\t NF FW: " + dependencyClass);
				}
				continue;
			}
			// }
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
