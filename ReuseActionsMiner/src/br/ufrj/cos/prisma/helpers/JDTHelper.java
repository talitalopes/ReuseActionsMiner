package br.ufrj.cos.prisma.helpers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import br.ufrj.cos.prisma.miner.util.Constants;

public class JDTHelper {

	IJavaProject frameworkProject;
	IJavaProject currentFrameworkApplicationProject;

	public JDTHelper(String projectName) {
		this.frameworkProject = getFrameworkProject(projectName);
		System.out.println(projectName + " " + this.frameworkProject);
	}

	public boolean frameworkProjectExists() {
		return frameworkProject != null;
	}

	public IJavaProject getFrameworkProject() {
		return this.frameworkProject;
	}

	public IJavaProject getCurrentFrameworkApplicationProject() {
		return currentFrameworkApplicationProject;
	}

	public void setCurrentFrameworkApplicationProject(
			IJavaProject currentFrameworkApplicationProject) {
		this.currentFrameworkApplicationProject = currentFrameworkApplicationProject;
	}

	/**
	 * Get all projects in the workspace For mining purposes each project
	 * corresponds to a commit in the repository.
	 * 
	 * **/
	public IProject[] getAllProjectsInWorkspace() {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		return projects;
	}

	private static IJavaProject getFrameworkProject(String name) {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IJavaProject frameworkProject = null;

		try {
			IProject fProject = root.getProject(name);
			if (fProject.exists()) {
				fProject.open(null);
				frameworkProject = JavaCore.create(root.getProject(name));
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return frameworkProject;
	}

	public static IType isFrameworkClass(IJavaProject framework, IType type)
			throws JavaModelException {
		if (framework == null) {
			System.out.println("Error: framework null");
			return null;
		}

		if (type == null) {
			System.out.println("Error: type null");
			return null;
		}
		
		if (type.getSuperclassName() == null) {
			System.out.println("Error: superclass null " + type);
			return null;
		}
		
		IType superClass = null;
		if (type.getCompilationUnit() == null) {
			System.out.println("Error: type compilation unit null");
			return null;
		}

		IImportDeclaration[] imports = type.getCompilationUnit().getImports();
		for (int i = 0; i < imports.length; i++) {
			if (imports[i].getElementName() == null) {
				continue;
			}

			// TODO: generalize this code!!!
			System.out.println(">>>> Imports: " + imports[i]);
			if (imports[i].getElementName().contains(type.getSuperclassName())
					&& imports[i].getElementName().contains(framework.getElementName())) {
				superClass = framework.findType(imports[i].getElementName());
				System.out.println(">>> not null: " + superClass);
				return superClass;
			}
		}

		return null;
	}

	public static boolean isIProjectValid(IJavaProject frameworkProject, IProject project) throws CoreException {
		if (!project.isNatureEnabled(Constants.JAVA_NATURE)
				|| (project.getName().equals(frameworkProject
						.getElementName()))) {
			return false;
		}
		return true;
	}

	public static boolean isJavaProjectValid(IJavaProject javaProject) {
		if (javaProject.getElementName().split("\\.").length == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the name of the application that corresponds to a given java
	 * project.
	 * 
	 * **/
	public static String getApplicationNameFromProjectName(
			String projectName) {
		String[] keys = projectName.split("\\.");
		String name = "";
		
		if (keys.length > 2) {
			for (int i = 0; i < keys.length - 2; i++) {
				name = name + "." + keys[i];
			}			
		}
		
		String applicationName = name.replaceFirst("\\.", "");
		System.out.println("Application name: " + applicationName);
		return applicationName;
	}
	
	public static IJavaProject openProject(IProject project) {
		try {
			project.open(null);
		} catch (CoreException e) {
			return null;
		}

		IJavaProject javaProject = JavaCore.create(project);
		if (!JDTHelper.isJavaProjectValid(javaProject)) {
			
			try {
				javaProject.close();
				closeProject(project);
			} catch (JavaModelException e) {
				LogHelper.log("Couldn't close java project");
			}
			
			return null;
		}
//		closeProject(project);
		return javaProject;
	}
	
	public static void closeProject(IProject project) {
		try {
			project.close(null);
		} catch (JavaModelException e) {
			LogHelper.log("Couldn't close project");
		} catch (CoreException e) {
			LogHelper.log("Couldn't close project");
		}
	}
	
//	public static void explorePackage(IPackageFragment p) {
//		System.out.println("Exploring package: " + p.getElementName());
//		
//		try {
//			if (p.getKind() != IPackageFragmentRoot.K_SOURCE) {
//				return;
//			}
//		} catch (JavaModelException e1) {
//			e1.printStackTrace();
//			return;
//		}
//
//		ICompilationUnit[] units = null;
//		try {
//			units = p.getCompilationUnits();
//		} catch (JavaModelException e) {
//			e.printStackTrace();
//			return;
//		}
//
//		try {
//			for (ICompilationUnit unit : units) {
//				for (IType type : unit.getAllTypes()) {
//					if (type.isClass()) {
//						extractClassesAndMethods(type);
//					} else if (type.isInterface()) {
//						continue; // TODO: get interfaces
//					}
//				}
//
//			}
//		} catch (JavaModelException e) {
//			e.printStackTrace();
//		}
//	}
		
}
