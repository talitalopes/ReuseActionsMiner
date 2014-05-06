package br.ufrj.cos.prisma.miner.popup.actions;

import minerv1.Commit;
import minerv1.FrameworkApplication;
import minerv1.Minerv1Factory;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.JDTHelper;
import br.ufrj.cos.prisma.helpers.LogHelper;

public class MineWorkspaceAction extends BaseExtractionAction {

	public MineWorkspaceAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		mineReuseActionsFromWorkspace();
	}
	
	private void mineReuseActionsFromWorkspace() {
		JDTHelper jdtHelper = new JDTHelper(process.getName());
		IProject[] projects = jdtHelper.getAllProjectsInWorkspace();
		System.out.println("Projects count: " + projects.length);
		
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].getName().toLowerCase().equals("miner")
					|| projects[i].getName().toLowerCase().equals(this.process.getName().toLowerCase())) {
				continue;
			}

			FrameworkApplication app = Minerv1Factory.eINSTANCE.createFrameworkApplication();
			app.setName(projects[i].getName().toLowerCase());
			
			Commit c = Minerv1Factory.eINSTANCE.createCommit();
			String id = String.format("commit%s", i);
			c.setId(id);
			c.setName(id);
			this.currentCommit = c;
			
			LogHelper.log(">>>>>Project:  " + projects[i].getName());
			try {
				exploreProject(projects[i]);
			} catch (JavaModelException e) {
				LogHelper.log("Error: JavaModelException");
			}
			
			app.getCommits().add(c);
			process.getApplications().add(app);

			LogHelper.log("Deleting projects from workspace");
//			deleteApplicationProjectsFromWorkspace();			
		}	
	}
	
	
	
}
