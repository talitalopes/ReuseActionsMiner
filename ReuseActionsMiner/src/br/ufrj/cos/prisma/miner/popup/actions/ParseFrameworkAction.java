package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.List;

import minerv1.Activity;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.miner.util.FrameworkFileWalker;

public class ParseFrameworkAction extends BaseAction {

	@Override
	public void run(IAction arg0) {
		super.run(arg0);

		String filePath = this.process.getDir();
		System.out.println("File path: " + filePath);
		
		FrameworkFileWalker fileWalker = new FrameworkFileWalker();
		fileWalker.walk(filePath);
		List<Activity> activities = fileWalker.getReuseActions();
		
		int index = 0;
		this.process.getActivities().clear();
		for (Activity a : activities) {
			if (this.process.getActivitiesMap().containsKey(a.getName())) {
				continue;
			}
			
			this.process.getActivitiesMap().put(a.getName(), index);
			index++;
			this.process.getActivities().add(a);
			System.out.println("Package Name: " + a.getPackageName());
		}
		
		System.out.println("Activities count: " + this.process.getActivities().size());
		save();
	}

}
