package br.ufrj.cos.prisma.miner.Extractor;

import java.util.ArrayList;
import java.util.List;

import miner.Commit;
import miner.Process;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.swt.widgets.Shell;

import br.ufrj.cos.prisma.helpers.JDTHelper;
import br.ufrj.cos.prisma.miner.Extractor.model.ClassExtensionActivity;
import br.ufrj.cos.prisma.miner.Extractor.model.MethodExtensionActivity;
import br.ufrj.cos.prisma.miner.Extractor.model.MinerActivity;
import br.ufrj.cos.prisma.miner.Extractor.model.MinerApplication;
import br.ufrj.cos.prisma.miner.Extractor.model.MinerCommit;
import br.ufrj.cos.prisma.miner.Extractor.model.MinerEvent;
import br.ufrj.cos.prisma.miner.Extractor.model.MinerProcess;
import br.ufrj.cos.prisma.miner.util.Constants;
import br.ufrj.cos.prisma.miner.util.CustomSearchRequestor;
import br.ufrj.cos.prisma.miner.util.Log;

/**
 * Reuse Actions Extractor. This class is responsible for extracting reuse
 * actions from applications code. All applications are related to a single
 * framework and the reuse actions corresponds to actions executed to build the
 * application from the framework.
 **/
public class ReuseActionsExtractor {
	private static MinerProcess mProcess;
	private static JDTHelper jdtHelper;
	private static List<String> eventsOrder;

	public static void start(Process process, Shell shell) {
		if (process == null) {
			Log.i(Constants.ERROR_KEY, Constants.PROCESS_NOT_EXISTS);
			return;
		}

		if (process.getName() == null) {
			Log.i(Constants.ERROR_KEY, Constants.ERROR_LOADING_FRAMEWORK);
			return;
		}

		System.out.println(String.format(
				"Extracting reuse actions related to %s framework",
				process.getName()));

//		manageFrameworkApplications(process);

		 mineReuseActions(process);
	}

	public static void mineReuseActionsFromCommit(MinerCommit commit) {
		eventsOrder = new ArrayList<String>();
		Log.i(String.format("%s: %s", Constants.FRAMEWORK_PROJECT_NAME_KEY,
				jdtHelper.getFrameworkProject().getElementName()));

		if (!jdtHelper.frameworkProjectExists()) {
			Log.i(Constants.ERROR_FRAMEWORK_NOT_EXISTS);
			return;
		}

		try {
			IProject[] projects = jdtHelper.getAllProjectsInWorkspace();

			for (int i = 0; i < projects.length; i++) {
				IJavaProject javaProject = openProject(projects[i]);
				if (javaProject == null) {
					continue;
				}

				for (IPackageFragment mPackage : javaProject
						.getPackageFragments()) {
					explorePackage(mPackage, commit);
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	private static void mineReuseActions(Process process) {
		jdtHelper = new JDTHelper(process.getName());
		if (!jdtHelper.frameworkProjectExists()) {
			Log.i(Constants.ERROR_FRAMEWORK_NOT_EXISTS);
			return;
		}

		mProcess = new MinerProcess(process);
		eventsOrder = new ArrayList<String>();
		Log.i(String.format("%s: %s", Constants.FRAMEWORK_PROJECT_NAME_KEY,
				jdtHelper.getFrameworkProject().getElementName()));

		try {
			// Add process instances to the process
			// Iterates in the workspace grouping projects into framework
			// applications
			mProcess.populateApplicationsFromWorkspaceProjects(jdtHelper);

			for (MinerApplication app : mProcess.getMinerApplications()) {
				Log.i(String
						.format("Application Name: %s - Projects - %d", app
								.getApplication().getName(), app
								.getAllCommits().size()));

				for (MinerCommit commit : app.getAllCommits()) {
					IJavaProject javaProject = openProject(commit
							.getWorkspaceProject());

					if (javaProject == null) {
						continue;
					}

					for (IPackageFragment mPackage : javaProject
							.getPackageFragments()) {
						explorePackage(mPackage, commit);
					}

				}
			}

			Log.i("Process applications: "
					+ mProcess.getAllApplications().size());
			process.getInstances().addAll(mProcess.getAllApplications());

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Explore projects to extract reuse actions
	 * 
	 * @deprecated
	 * **/
	public static void getFrameworkApplicationsCommits() throws CoreException {

		// Commit lastCommit = null;
		IProject[] projects = jdtHelper.getAllProjectsInWorkspace();
		// Loop over all projects
		for (IProject project : projects) {
			if (!JDTHelper.isIProjectValid(jdtHelper.getFrameworkProject(),
					project)) {
				continue;
			}

			IJavaProject javaProject = openProject(project);
			jdtHelper.setCurrentFrameworkApplicationProject(javaProject);
			String applicationName = JDTHelper
					.getApplicationNameFromProjectName(javaProject
							.getElementName());
			MinerApplication application = mProcess
					.getApplicationByName(applicationName);

			// The name of the project follows the pattern: name - commitid -
			// date TODO:confirm ordering
			String[] keys = javaProject.getElementName().split("\\.");
			MinerCommit minerCommit = new MinerCommit(keys);
			addExistingEventsToCommit(minerCommit.getCommit());
			application.addCommit(minerCommit);

			mProcess.addApplication(application);

			// Explore the files in this project
			boolean mine = true;
			for (IPackageFragment mPackage : javaProject.getPackageFragments()) {
				if (mine) {
					explorePackage(mPackage, minerCommit);
				}

				// if (lastCommit != null && lastCommit.equals(c)) {
				// System.out.println("Same events. Can stop mining: " + mine);
				// System.out.println("Last commit: " + lastCommit);
				// System.out.println("commit: " + c);
				// System.out.println("First: " + lastCommit != null);
				// System.out.println("Second: " + lastCommit.equals(c));
				// mine = false; // TODO: stop mining when reuse actions are the
				// same
				// }
				// lastCommit = c;
			}
		}
	}

	private static IJavaProject openProject(IProject project)
			throws CoreException {
		project.open(null);
		IJavaProject javaProject = JavaCore.create(project);
		if (!JDTHelper.isJavaProjectValid(javaProject)) {
			return null;
		}
		return javaProject;
	}

	private static void addExistingEventsToCommit(Commit c) {
		for (String eventName : eventsOrder) {
			String[] eventNameParts = eventName.split("\\+");
			String eventKey = "";
			if (eventNameParts.length == 2) {
				eventKey = eventNameParts[0];
			} else if (eventNameParts.length == 3) {
				eventKey = String.format("%s+%s", eventNameParts[0],
						eventNameParts[2]);
			}

			MinerActivity activity = mProcess.getActivityById(eventKey);
			if (activity == null) {
				continue;
			}

			MinerEvent event = new MinerEvent(activity, c.getDate());
			c.getEvents().add(event.getEvent());
		}
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

	public static void explorePackage(IPackageFragment p, MinerCommit mc)
			throws JavaModelException {
		if (p.getKind() != IPackageFragmentRoot.K_SOURCE) {
			return;
		}

		ICompilationUnit[] units = p.getCompilationUnits();

		for (ICompilationUnit unit : units) {
			for (IType type : unit.getAllTypes()) {
				if (type.isClass()) {
					extractClassAndMethods(type, mc);
				} else if (type.isInterface()) {
					continue; // TODO: get interfaces
				}
			}
		}
	}

	private static void extractClassAndMethods(IType type, MinerCommit c)
			throws JavaModelException {
		// Check is superclass belongs to framework.
		IType superClassFW = JDTHelper.isFrameworkClass(
				jdtHelper.getFrameworkProject(), type);
		if (superClassFW == null) {
			return;
		}

		try {
			SearchMatch mMatch = callHierarchy(
					jdtHelper.getCurrentFrameworkApplicationProject(),
					type.getElementName());
			if (mMatch != null) {
				IType dependentIType = jdtHelper
						.getCurrentFrameworkApplicationProject().findType(
								mMatch.getResource().getName());
				extractClassAndMethods(dependentIType, c);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		MinerActivity classExtensionActivity = getClassActivity(superClassFW,
				type);
		String activityNameKey = String.format("%s+%s",
				superClassFW.getFullyQualifiedName(), type.getElementName());

		if (eventsOrder.contains(activityNameKey)) {
			return;
		}

		MinerEvent minerEvent = new MinerEvent(classExtensionActivity,
				c.getDate());
		c.addEvent(minerEvent);
		// c.getEvents().add(minerEvent.getEvent());
		eventsOrder.add(activityNameKey);

		getClassMethods(superClassFW, type, c);

		// c.getReuseActions().add(classRA);
		// ArrayList<RAMethod> fwMethods = classRA.getFrameworkMethods();
		// for (int m = 0; m < fwMethods.size(); m++) {
		// c.getReuseActions().add(fwMethods.get(m));
		//
		// if (m == fwMethods.size() - 1)
		// nextPosition = fwMethods.get(m).getPosition();
		// }
		//
		// nextPosition += 1;

	}

	private static void getClassMethods(IType superClassFW, IType type,
			MinerCommit c) throws JavaModelException {
		IMethod[] methods = type.getMethods();

		for (IMethod method : methods) {
			boolean isExtension = superClassFW.findMethods(method) != null;
			if (!isExtension) {
				continue;
			}
			MinerActivity methodExtensionActivity = getMethodActivity(
					superClassFW, type, method);

			// Super class + method + class name
			String methodKey = String.format("%s+%s+%s",
					superClassFW.getElementName(), type.getElementName(),
					method.getElementName());

			if (eventsOrder.contains(methodKey)) {
				continue;
			}

			MinerEvent methodExtensionEvent = new MinerEvent(
					methodExtensionActivity, c.getDate());
			c.addEvent(methodExtensionEvent);

			eventsOrder.add(methodKey);
		}
	}

	private static MinerActivity getClassActivity(IType superClassFW, IType type) {
		String activityKey = MinerActivity.getKeyForClass(superClassFW);
		MinerActivity classExtensionActivity = mProcess
				.getActivityById(activityKey);
		if (classExtensionActivity != null) {
			return classExtensionActivity;
		}

		classExtensionActivity = new ClassExtensionActivity(superClassFW, type);
		mProcess.addActivity(classExtensionActivity);

		return classExtensionActivity;
	}

	private static MinerActivity getMethodActivity(IType superClassFW,
			IType type, IMethod method) throws JavaModelException {
		String methodKey = MinerActivity.getKeyForMethod(superClassFW, method);
		MinerActivity methodExtensionActivity = mProcess
				.getActivityById(methodKey);
		if (methodExtensionActivity != null) {
			return methodExtensionActivity;
		}

		methodExtensionActivity = new MethodExtensionActivity(superClassFW,
				type, method);
		mProcess.addActivity(methodExtensionActivity);

		return methodExtensionActivity;
	}

	/*
	 * private static void openDialog(String id, Shell shell) { // First see if
	 * this is a "new wizard". IWizardDescriptor descriptor =
	 * PlatformUI.getWorkbench().getImportWizardRegistry() .findWizard(id); //
	 * If not check if it is an "import wizard". if (descriptor == null) {
	 * descriptor = PlatformUI.getWorkbench().getImportWizardRegistry()
	 * .findWizard(id); } // Or maybe an export wizard if (descriptor == null) {
	 * descriptor = PlatformUI.getWorkbench().getExportWizardRegistry()
	 * .findWizard(id); } try { // Then if we have a wizard, open it. if
	 * (descriptor != null) { IWizard wizard = descriptor.createWizard();
	 * WizardDialog wd = new WizardDialog(shell, wizard);
	 * wd.setTitle(wizard.getWindowTitle()); wd.open(); } } catch (CoreException
	 * e) { e.printStackTrace(); } }
	 */

}