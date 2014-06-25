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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ParserAction implements IObjectActionDelegate {

	@Override
	public void run(IAction arg0) {
		String filePath = "/Users/talitalopes/Documents/Projetos/UFRJ/Mestrado/frameworks/gef/org.eclipse.gef";
		Filewalker walker = new Filewalker();
		walker.walk(filePath);
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
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

		@Override
		public void visit(ClassOrInterfaceDeclaration c, Object arg) {
			classOrInterfaceDeclaration = c;
		}

		public String getClassOrInterfaceName() {
			return this.classOrInterfaceDeclaration.getName();
		}
	}

	public static class Filewalker {
		public Filewalker() {

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

			ClassVisitor classVisitor = new ClassVisitor();
			classVisitor.visit(cu, null);
			System.out.println(classVisitor.getClassOrInterfaceName());

			// prints the resulting compilation unit to default system output
			MethodVisitor methodVisitor = new MethodVisitor();
			methodVisitor.visit(cu, null);

			for (MethodDeclaration method : methodVisitor.getAllMethods()) {
				System.out.println(method.getName());
			}
		}
	}
}
