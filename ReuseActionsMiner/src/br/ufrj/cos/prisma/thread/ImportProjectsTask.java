package br.ufrj.cos.prisma.thread;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;

public class ImportProjectsTask extends NotificationThread {

	String projectPath;

	public ImportProjectsTask(String projectPath) {
		this.projectPath = projectPath;
		this.projects = new ArrayList<IProject>();
	}

	@Override
	public void doWork() {
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			IPath projectDotProjectFile = new Path(projectPath);
			IProjectDescription projectDescription = workspace
					.loadProjectDescription(projectDotProjectFile);
			IProject project = workspace.getRoot().getProject(
					projectDescription.getName());
			JavaCapabilityConfigurationPage.createProject(project,
					projectDescription.getLocationURI(), null);

			projects.add(project);
			System.out.println("javaProjects: " + projects.size());

		} catch (CoreException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Couldn't import project: " + projectPath);
		}
	}

	public void deleteProjectsFromWorkspace() {
		for (IProject project : projects) {
			if (!project.exists()) {
				return;
			}

			try {
				System.out.println("deleting: " + project);
				if (project.getName().toLowerCase().contains("miner")
						|| project.getName().toLowerCase().contains("gef")) {
					continue;
				}

				project.delete(true, null);

			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		projects.clear();
		projects = null;
	}

}
