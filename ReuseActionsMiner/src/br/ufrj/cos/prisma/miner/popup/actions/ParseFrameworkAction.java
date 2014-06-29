package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.Set;

import minerv1.Activity;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.FrameworkMiningHelper;

public class ParseFrameworkAction extends BaseAction {

	@Override
	public void run(IAction arg0) {
		super.run(arg0);

		String filePath = this.process.getDir();
		FrameworkMiningHelper miningHelper = new FrameworkMiningHelper(filePath);
		Set<Activity> activities = miningHelper.extractReuseActions();
		
		for (Activity a : activities) {
			this.process.getActivitiesMap().put(a.getName(), a);
		}
		this.process.getActivities().clear();
		this.process.getActivities().addAll(
				this.process.getActivitiesMap().values());

		save();
	}

}
