package br.ufrj.cos.prisma.miner.popup.actions;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import minerv1.Minerv1Factory;
import minerv1.Activity;
import minerv1.ActivityType;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.model.FrameworkMethod;

public class ParseFrameworkAction extends BaseAction {

	@Override
	public void run(IAction arg0) {
		super.run(arg0);

		String filePath = this.process.getDir();
		Filewalker walker = new Filewalker();
		walker.walk(filePath);

		this.process.getActivities().addAll(walker.getActivities());
		save();
//		for (FrameworkClass c : this.framework.getFrameworkClasses()) {
//			if (c.getClassDeclaration().getExtends() != null) {
//				System.out.println(c.getClassDeclaration().getName()
//						+ " extends "
//						+ c.getClassDeclaration().getExtends().get(0));
//			} else {
//				System.out.println(c.getClassDeclaration().getName());
//			}
//
//			for (FrameworkMethod m : c.getFrameworkMethods()) {
//				System.out.println("\t" + m.getMethod().getName());
//			}
//
//			if (c.getFrameworkMethods().size() == 0) {
//				System.out.println("No methods");
//			}
//		}
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	@SuppressWarnings("rawtypes")
	protected static class MethodVisitor extends VoidVisitorAdapter {
		Set<FrameworkMethod> methods;

		public MethodVisitor() {
			methods = new HashSet<FrameworkMethod>();
		}

		@Override
		public void visit(MethodDeclaration n, Object arg) {
			methods.add(new FrameworkMethod(n));
		}

		public Set<FrameworkMethod> getAllMethods() {
			return methods;
		}
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	@SuppressWarnings("rawtypes")
	private static class ClassVisitor extends VoidVisitorAdapter {
		ClassOrInterfaceDeclaration classOrInterfaceDeclaration;

		@Override
		public void visit(ClassOrInterfaceDeclaration c, Object arg) {
			classOrInterfaceDeclaration = c;
		}

		public ClassOrInterfaceDeclaration getClassOrInterfaceName() {
			return this.classOrInterfaceDeclaration;
		}
	}

	public static class Filewalker {
		Set<Activity> activities;

		public Filewalker() {
			this.activities = new HashSet<Activity>();
		}

		public Set<Activity> getActivities() {
			return this.activities;
		}

		public void walk(String path) {
			File root = new File(path);
			File[] list = root.listFiles();

			if (list == null)
				return;

			for (File f : list) {
				if (f.isDirectory()) {
					walk(f.getAbsolutePath());
				} else {
					try {
						visitClassAndMethods(f.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		public void visitClassAndMethods(String filePath) throws Exception {
			if (!filePath.contains(".java")) {
				return;
			}

			FileInputStream in = new FileInputStream(filePath);
			CompilationUnit cu;
			try {
				// parse the file
				cu = JavaParser.parse(in);
			} finally {
				in.close();
			}

			// Visit class
			ClassVisitor classVisitor = new ClassVisitor();
			classVisitor.visit(cu, null);
			
			// Add class to framework
			Activity activity = (Activity) Minerv1Factory.eINSTANCE.createActivity();
			activity.setId("id");
			activity.setType(ActivityType.CLASS_EXTENSION);
			activity.setName(classVisitor.getClassOrInterfaceName().getName());
			this.activities.add(activity);
			
//			FrameworkClass fwClass = new FrameworkClass(
//					classVisitor.getClassOrInterfaceName());
//			 MethodVisitor methodVisitor = new MethodVisitor();
//			 methodVisitor.visit(cu, null);
//			
//			 fwClass.addFrameworkMethods(methodVisitor.getAllMethods());
//			framework.addFrameworkClass(fwClass);
		}
	}
}
