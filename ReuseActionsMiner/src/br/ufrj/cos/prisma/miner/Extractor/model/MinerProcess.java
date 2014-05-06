package br.ufrj.cos.prisma.miner.Extractor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miner.Process;
import miner.ProcessInstance;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import br.ufrj.cos.prisma.helpers.JDTHelper;

public class MinerProcess {

	Process process;

	HashMap<String, MinerActivity> processActivities;
	HashMap<String, MinerApplication> applicationsMap;

	public MinerProcess(Process process) {
		this.process = process;
		processActivities = new HashMap<String, MinerActivity>();
		applicationsMap = new HashMap<String, MinerApplication>();
	}

	public void populateApplicationsFromWorkspaceProjects(JDTHelper jdtHelper)
			throws CoreException {
		// Get all projects in the workspace
		IProject[] projects = jdtHelper.getAllProjectsInWorkspace();
		for (IProject project : projects) {
			if (project.getName().equals(
					jdtHelper.getFrameworkProject().getElementName())) {
				continue;
			}

			if (!JDTHelper.isIProjectValid(jdtHelper.getFrameworkProject(),
					project)) {
				continue;
			}

			String applicationName = JDTHelper
					.getApplicationNameFromProjectName(project.getName());
			MinerApplication app = addNewApplication(applicationName);
			MinerCommit projectCommit = new MinerCommit(project.getName()
					.split("\\."), project);
			app.addMinerCommit(projectCommit);
		}
	}

	public MinerApplication addNewApplication(String name) {
		if (this.applicationsMap.containsKey(name)) {
			return this.applicationsMap.get(name);
		}

		MinerApplication app = new MinerApplication(name);
		this.applicationsMap.put(name, app);
		return app;
	}

	public void addActivity(MinerActivity minerActivity) {
		this.process.getActivities().add(minerActivity.getActivity());
		this.processActivities.put(minerActivity.getActivity().getId(),
				minerActivity);
	}

	public MinerActivity getActivityById(String key) {
		return this.processActivities.get(key);
	}

	/**
	 * Returns an application with the given name. If no application with the
	 * provided name exists a new application is created.
	 * **/
	public MinerApplication getApplicationByName(String name) {
		if (applicationsMap.containsKey(name)) {
			return applicationsMap.get(name);
		}
		MinerApplication app = new MinerApplication(name);
		return app;
	}

	public void addApplication(MinerApplication application) {
		this.applicationsMap.put(application.getApplication().getName(),
				application);
	}

	public List<MinerApplication> getMinerApplications() {
		List<MinerApplication> apps = new ArrayList<MinerApplication>();
		apps.addAll(this.applicationsMap.values());
		return apps;
	}

	public List<ProcessInstance> getAllApplications() {
		List<ProcessInstance> applications = new ArrayList<ProcessInstance>();
		for (MinerApplication app : this.applicationsMap.values()) {
			applications.add(app.getApplication());
		}
		return applications;
	}
}
