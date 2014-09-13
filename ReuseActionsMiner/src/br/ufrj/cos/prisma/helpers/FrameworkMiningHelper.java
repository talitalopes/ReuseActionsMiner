package br.ufrj.cos.prisma.helpers;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minerv1.Activity;
import minerv1.ActivityType;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkProcess;
import minerv1.Minerv1Factory;

public class FrameworkMiningHelper {

	public enum MiningType {
		FRAMEWORK, APPLICATION
	}

	String frameworkPath;
	FrameworkProcess process;
	int methodsCount;

	public FrameworkMiningHelper(String frameworkPath) {
		this.frameworkPath = frameworkPath;
	}

	public FrameworkMiningHelper(String frameworkPath, FrameworkProcess process) {
		this.frameworkPath = frameworkPath;
		this.process = process;
	}

	/**
	 * This method extracts all reuse actions from a framework. Only class
	 * extension methods were implemented.
	 * 
	 * **/
	public List<Activity> extractFrameworkReuseActions() {
		Filewalker walker = new Filewalker();
		walker.walk(this.frameworkPath, MiningType.FRAMEWORK);
		System.out.println("Methods count: " + walker.getMethods().size());
		return walker.getActivities();
	}

	/**
	 * This method extracts all reuse actions from a framework application. Only
	 * class extension methods were implemented.
	 * 
	 * **/
	public List<Event> extractApplicationReuseActions() {
		LogHelper.log("Mining reuse actions from application");
		Filewalker walker = new Filewalker(process);
		walker.walk(this.frameworkPath, MiningType.APPLICATION);

		return walker.getEvents();
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	@SuppressWarnings("rawtypes")
	protected static class MethodVisitor extends VoidVisitorAdapter {
		Set<MethodDeclaration> methods;

		public MethodVisitor() {
			methods = new HashSet<MethodDeclaration>();
		}

		@Override
		public void visit(MethodDeclaration n, Object arg) {
			methods.add(n);
		}

		public Set<MethodDeclaration> getAllMethods() {
			return methods;
		}
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	@SuppressWarnings("rawtypes")
	private static class ClassVisitor extends VoidVisitorAdapter {
		ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
		PackageDeclaration packageDeclaration;
		List<ImportDeclaration> imports;

		@Override
		public void visit(ClassOrInterfaceDeclaration c, Object arg) {
			classOrInterfaceDeclaration = c;
		}

		public ClassOrInterfaceDeclaration getClassOrInterfaceName() {
			return this.classOrInterfaceDeclaration;
		}

		public void setPackage(PackageDeclaration _package) {
			this.packageDeclaration = _package;
		}

		public String getPackage() {
			return this.packageDeclaration.getName().toString();
		}
		
		public void setImports(List<ImportDeclaration> imports) {
			this.imports = imports;
		}
		
		public List<ImportDeclaration> getImports() {
			return this.imports;
		}

	}
	
	public static class Filewalker {
		List<MethodDeclaration> methods;
		List<Activity> activities;
		Map<String, Event> events;
		FrameworkProcess process;

		public Filewalker() {
			this.activities = new ArrayList<Activity>();
			this.methods = new ArrayList<MethodDeclaration>();
		}

		public List<Event> getEvents() {
			for (Event e: this.events.values()) {
				
				List<EventDependency> dependenciesToRemove = new ArrayList<EventDependency>();
				for (EventDependency dep: e.getDependencies()) {
					
					if (events.containsKey(dep.getId())) {
						dep.setEvent(events.get(dep.getId()));
					
					} else {
						dependenciesToRemove.add(dep);
					}
					
				}
				
				e.getDependencies().removeAll(dependenciesToRemove);
			}
			
			return new ArrayList<Event>(this.events.values());
		}

		public Filewalker(FrameworkProcess process) {
			this.events = new HashMap<String, Event>();
			this.methods = new ArrayList<MethodDeclaration>();
			this.process = process;
		}

		public List<Activity> getActivities() {
			return this.activities;
		}

		public List<MethodDeclaration> getMethods() {
			return this.methods;
		}

		public void walk(String path, MiningType type) {
			File root = new File(path);
			File[] list = root.listFiles();

			if (list == null)
				return;

			for (File f : list) {
				if (f.isDirectory()) {
					walk(f.getAbsolutePath(), type);
				} else {
					try {
						if (type.equals(MiningType.FRAMEWORK)) {
							visitClassAndMethods(f.getAbsolutePath());
						} else {
							findReuseActions(f.getAbsolutePath());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}

		public void findReuseActions(String filePath) throws Exception {
			if (this.process == null) {
				LogHelper.log("No process found");
				return;
			}

			ClassVisitor classVisitor = getClassVisitor(filePath);
			if (classVisitor == null) {
				return;
			}

			ClassOrInterfaceDeclaration classDeclaration = classVisitor
					.getClassOrInterfaceName();
			if (classDeclaration == null) {
				return;
			}
			
			List<ClassOrInterfaceType> classExtensions = classDeclaration
					.getExtends();
			if (classExtensions == null) {
				return;
			}

			for (ClassOrInterfaceType type : classExtensions) {

				if (process.hasActivity(type.getName())) {
					// Event id = class complete name (package + name)
					String eventId = String.format("%s.%s",
							classVisitor.getPackage(), classDeclaration.getName());
					
					Event e = Minerv1Factory.eINSTANCE.createEvent();
					int index = process.getActivitiesMap()
							.get(type.getName());
					
					e.setActivity(process.getActivities().get(index));
					
					for (ImportDeclaration imp: classVisitor.getImports()) {
						String importName = imp.getName().toString();
						System.out.println(importName);
						
						EventDependency dep = Minerv1Factory.eINSTANCE.createEventDependency();
						dep.setId(importName);
						e.getDependencies().add(dep);
					}
					
					System.out.println("Event activity: " + process.getActivities().get(index));
					e.setId(eventId);
					events.put(eventId, e);
				}
			}

		}

		public void visitClassAndMethods(String filePath) throws Exception {
			// Visit class
			ClassVisitor classVisitor = getClassVisitor(filePath);
			if (classVisitor == null) {
				return;
			}

			// Add class to framework
			Activity activity = (Activity) Minerv1Factory.eINSTANCE
					.createActivity();

			activity.setId(classVisitor.getClassOrInterfaceName().getName());
			activity.setType(ActivityType.CLASS_EXTENSION);
			String activityName = classVisitor.getClassOrInterfaceName().getName();
			activity.setName(activityName);
			activity.setPackageName(classVisitor.getPackage());
			this.activities.add(activity);

			MethodVisitor methodVisitor = getMethodVisitor(filePath);
			if (methodVisitor == null) {
				return;
			}
			this.methods.addAll(methodVisitor.getAllMethods());

			// FrameworkClass fwClass = new FrameworkClass(
			// classVisitor.getClassOrInterfaceName());
			// MethodVisitor methodVisitor = new MethodVisitor();
			// methodVisitor.visit(cu, null);
			//
			// fwClass.addFrameworkMethods(methodVisitor.getAllMethods());
			// framework.addFrameworkClass(fwClass);
		}

		@SuppressWarnings("unchecked")
		public ClassVisitor getClassVisitor(String filePath) throws Exception {
			if (!filePath.contains(".java")) {
				return null;
			}

			FileInputStream in = new FileInputStream(filePath);
			CompilationUnit cu = null;
			try {
				// parse the file
				cu = JavaParser.parse(in);

			} catch (ParseException e) {
				System.out.println("ERROR: couldn't parse file: " + filePath);
			} finally {
				in.close();
			}

			if (cu == null) {
				return null;
			}

			// Visit class
			ClassVisitor classVisitor = new ClassVisitor();
			classVisitor.visit(cu, null);
			classVisitor.setPackage(cu.getPackage());
			classVisitor.setImports(cu.getImports());
			return classVisitor;
		}

		@SuppressWarnings("unchecked")
		public MethodVisitor getMethodVisitor(String filePath) throws Exception {
			if (!filePath.contains(".java")) {
				return null;
			}

			FileInputStream in = new FileInputStream(filePath);
			CompilationUnit cu = null;
			try {
				// parse the file
				cu = JavaParser.parse(in);
			} catch (ParseException e) {
				System.out.println("ERROR: couldn't parse file: " + filePath);
			} finally {
				in.close();
			}

			if (cu == null) {
				return null;
			}

			// Visit class
			MethodVisitor methodVisitor = new MethodVisitor();
			methodVisitor.visit(cu, null);
			return methodVisitor;
		}
	}

}
