package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minerv1.Activity;
import minerv1.ActivityType;
import minerv1.Commit;
import minerv1.Event;
import minerv1.Minerv1Factory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.JDTHelper;
import br.ufrj.cos.prisma.helpers.LogHelper;
import br.ufrj.cos.prisma.miner.util.CustomSearchRequestor;

public class BaseExtractionAction extends BaseAction {
	protected Commit currentCommit;
	protected JDTHelper jdtHelper;
	protected Map<String, Activity> processActivities;
	protected Set<String> commitEvents;

	public BaseExtractionAction() {
		super();
		this.processActivities = new HashMap<String, Activity>();
		this.commitEvents = new HashSet<String>();
	}

	@Override
	public void run(IAction arg0) {
		super.run(arg0);
		this.jdtHelper = new JDTHelper(process.getName());
	}

	protected void exploreProject(IProject project) throws JavaModelException {
		IJavaProject javaProject = JDTHelper.openProject(project);
		if (javaProject == null) {
			return;
		}

		System.out.println("Explore Project: " + javaProject.getElementName());
		jdtHelper.setCurrentFrameworkApplicationProject(javaProject);
		IPackageFragment[] packages = null;
		packages = javaProject.getPackageFragments();

		if (packages == null) {
			return;
		}

		for (IPackageFragment mPackage : packages) {
			explorePackage(mPackage);
		}

		JDTHelper.closeProject(project);
	}

	private void explorePackage(IPackageFragment p) {

		try {
			p.open(null);
			if (p.getKind() != IPackageFragmentRoot.K_SOURCE) {
				return;
			}
		} catch (JavaModelException e) {
			LogHelper.log("JavaModelException (explorePackage) ",
					e.getMessage());
			return;
		}

		try {
			ICompilationUnit[] units = p.getCompilationUnits();
			for (ICompilationUnit unit : units) {
				for (IType type : unit.getAllTypes()) {
					if (!type.isClass()) {
						continue;
					}
					extractClassesAndMethods(type);
				}
				unit.close();
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		try {
			p.close();
		} catch (JavaModelException e) {
			LogHelper.log("JavaModelException (closing package)",
					e.getMessage());
		}
	}

	private void extractClassesAndMethods(IType type) {
		// Check is superclass belongs to framework.
		IType superClassFW;
		try {
			superClassFW = JDTHelper.isFrameworkClass(
					this.jdtHelper.getFrameworkProject(), type);
			if (superClassFW == null) {
				return;
			}

			Activity classActivity = getActivityForSuperClass(superClassFW);
			String activityEventKey = String
					.format("%s+%s", superClassFW.getFullyQualifiedName(),
							type.getElementName());

			if (!this.commitEvents.contains(activityEventKey)) {
				this.commitEvents.add(activityEventKey);
				Event e = Minerv1Factory.eINSTANCE.createEvent();
				e.setActivity(classActivity);
				this.currentCommit.getEvents().add(e);
			}

			SearchMatch mMatch = callHierarchy(
					jdtHelper.getCurrentFrameworkApplicationProject(),
					type.getElementName());
			if (mMatch != null) {
				IType dependentIType = jdtHelper
						.getCurrentFrameworkApplicationProject().findType(
								mMatch.getResource().getName());
				extractClassesAndMethods(dependentIType);
			}

		} catch (JavaModelException e) {
			LogHelper.log("JavaModelException (extractClassesAndMethods) ",
					e.getMessage());
		} catch (CoreException e) {
			LogHelper.log("CoreException (extractClassesAndMethods) ",
					e.getMessage());
		}

	}

	private Activity getActivityForSuperClass(IType superClassFW) {
		String key = superClassFW.getFullyQualifiedName();
		if (this.processActivities.containsKey(key)) {
			return this.processActivities.get(key);
		}

		Activity a = Minerv1Factory.eINSTANCE.createActivity();
		a.setName(superClassFW.getFullyQualifiedName());
		a.setType(ActivityType.CLASS_EXTENSION);
		this.process.getActivities().add(a);
		this.processActivities.put(key, a);

		return a;
	}

	private static SearchMatch callHierarchy(IJavaProject javaProject,
			String searchKeyword) throws CoreException {
		SearchPattern pattern = SearchPattern.createPattern(searchKeyword,
				IJavaSearchConstants.CLASS, IJavaSearchConstants.REFERENCES,
				SearchPattern.R_CASE_SENSITIVE);

		if (pattern == null) {
			return null;
		}

		boolean includeReferencedProjects = false;
		IJavaElement[] javaProjects = new IJavaElement[] { javaProject };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(
				javaProjects, includeReferencedProjects); // <--- ????

		SearchRequestor requestor = CustomSearchRequestor.getInstance();

		SearchEngine searchEngine = new SearchEngine();
		SearchParticipant[] searchParticipant = new SearchParticipant[] { SearchEngine
				.getDefaultSearchParticipant() };

		try {
			searchEngine.search(pattern, searchParticipant, scope, requestor,
					null);
		} catch (Exception e) {
			return null;
		}

		return ((CustomSearchRequestor) requestor).getMatch();
	}

	protected void deleteProjectsFromWorkspace(List<IProject> javaProjects) {
		while (javaProjects.size() > 0) {
			IProject project = javaProjects.remove(0);

			if (!project.exists()) {
				return;
			}

			try {
				if (project.getName().toLowerCase().contains("miner")
						|| project.getName().toLowerCase().contains("gef")) {
					continue;
				}

				project.delete(true, null);
				project = null;

			} catch (CoreException e) {
				LogHelper.log("CoreException (deleteProjectsFromWorkspace) ",
						e.getMessage());
			}
		}
		javaProjects = null;
	}

}
