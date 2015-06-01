package br.ufrj.cos.prisma.miner.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

class ReuseMinerClassASTVisitor extends ASTVisitor {

	public class ApplicationClass extends Object {
		String packageName;
		String appClassName;
		String superClassName;
		boolean isAbstract;
		Set<String> classDependencies;
		Set<String> interfacesDependencies;
		Set<String> methodsDependencies;

		public ApplicationClass(String packageName, String appClassName,
				String superClassName, boolean isAbstract) {
			this.packageName = packageName;
			this.appClassName = appClassName;
			this.superClassName = superClassName;
			this.classDependencies = new HashSet<String>();
			this.interfacesDependencies = new HashSet<String>();
			this.methodsDependencies = new HashSet<String>();
			this.isAbstract = isAbstract;
		}

		@Override
		public boolean equals(Object obj) {
			return ((ApplicationClass) obj).appClassName
					.equals(this.appClassName);
		}

		@Override
		public int hashCode() {
			return this.appClassName.hashCode();
		}

		public void addClassDependency(String dependency) {
			this.classDependencies.add(dependency);
		}

		public void addInterfaceDependency(String dependency) {
			this.interfacesDependencies.add(dependency);
		}

		public void addMethodDependency(String dependency) {
			this.methodsDependencies.add(dependency);
		}
		
		public String getCompleteAppName() {
			String completeName = String.format("%s.%s", this.packageName,
					appClassName);
			return completeName;
		}
		
	}

	String packageName;
//	String currentAppClassName;
	String mainClassName;
	ApplicationFileWalker walker;
	
	Map<String, String> classCompleteNames;
	Map<String, Set<String>> methodsDependencies;

	public ReuseMinerClassASTVisitor(String mainClassName, ApplicationFileWalker walker) {
		this.mainClassName = mainClassName;
		this.methodsDependencies = new HashMap<String, Set<String>>();
		this.classCompleteNames = new HashMap<String, String>();
		this.walker = walker;
	}

	public boolean visit(PackageDeclaration node) {
		this.packageName = node.getName().getFullyQualifiedName();
		return true;
	}

	public boolean visit(TypeDeclaration node) {
		String currentAppClassName = node.getName().getFullyQualifiedName();

		boolean isAbstract = Flags.isAbstract(node.getModifiers());
		Type superclass = node.getSuperclassType();
		String superclassName = null;
		if (superclass != null) {
			superclassName = superclass.toString();
			// superclassesMap.put(currentAppClassName, superclassName);
		}

		ApplicationClass appClass = new ApplicationClass(this.packageName,
				currentAppClassName, superclassName, isAbstract);

		@SuppressWarnings("unchecked")
		List<SimpleType> types = (List<SimpleType>) node.superInterfaceTypes();

		for (int typesIndex = 0; typesIndex < types.size(); typesIndex++) {
			String interfaceName = types.get(typesIndex).getName().toString();
			appClass.addInterfaceDependency(interfaceName);
		}

		if (!currentAppClassName.equals(this.mainClassName)) {
			ApplicationClass mainClass = this.walker.applicationClasses
					.get(mainClassName);
			if (mainClass != null) {
				mainClass.addClassDependency(currentAppClassName);
			}
		}

		this.walker.applicationClasses.put(currentAppClassName, appClass);
		return true;
	}

	public String getMainClassName() {
		String name = String.format("%s.%s", this.packageName,
				this.mainClassName);
		return name;
	}

	public boolean visit(ClassInstanceCreation node) {
		int type = node.getParent().getNodeType();
		ASTNode parentClass = node.getParent();
		while (type != ASTNode.TYPE_DECLARATION) {
			parentClass = parentClass.getParent();
			type = parentClass.getNodeType();
		}

		TypeDeclaration t = (TypeDeclaration) parentClass;
		ApplicationClass parent = this.walker.applicationClasses.get(t.getName().toString());
		if (parent != null) {
			parent.addClassDependency(node.getType().toString());
		}
		return true;
	}
	
	public boolean visit(MethodDeclaration node) {
		int type = node.getParent().getNodeType();
		ASTNode parentClass = node.getParent();
		while (type != ASTNode.TYPE_DECLARATION) {
			parentClass = parentClass.getParent();
			type = parentClass.getNodeType();
		}

		TypeDeclaration t = (TypeDeclaration) parentClass;
		ApplicationClass parent = this.walker.applicationClasses.get(t.getName().toString());
		if (parent != null) {
			String methodId = String.format("%s.%s", parent.superClassName, node.getName().toString());
			parent.addMethodDependency(methodId);
		}
		
		return true;
	}

}
