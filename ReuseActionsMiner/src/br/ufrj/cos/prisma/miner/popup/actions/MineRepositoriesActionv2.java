package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.List;

import minerv1.Commit;
import minerv1.Event;
import minerv1.FrameworkApplication;
import minerv1.Minerv1Factory;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.FrameworkMiningHelper;
import br.ufrj.cos.prisma.helpers.GitRepositoryHelper;
import br.ufrj.cos.prisma.helpers.LogHelper;

public class MineRepositoriesActionv2 extends BaseExtractionAction {
	int currentIndex;
	boolean wait;

	public MineRepositoriesActionv2() {
		super();
		wait = true;
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		mineReuseActionsFromRepositories();
		save();
	}

	private void mineReuseActionsFromRepositories() {
		for (FrameworkApplication app : process.getApplications()) {
			currentIndex = 0;
			if (!app.isMine()) {
				continue;
			}
			final GitRepositoryHelper gitHelper = GitRepositoryHelper
					.getInstanceForApplication(app);

			List<String> commits = gitHelper.getCommitsHistoryFromMaster();

			LogHelper.log(String.format("%d commits found for application %s",
					commits.size(), app.getName()));

			FrameworkMiningHelper miningHelper = new FrameworkMiningHelper(
					gitHelper.getRepoFile().getAbsolutePath(), this.process);

			while (currentIndex < commits.size()) {
				LogHelper.log(String.format("Commit %d out of %d",
						currentIndex + 1, commits.size()));

				String currentCommitId = commits.get(currentIndex);
				this.currentCommit = createCommit(currentCommitId);

				cloneCurrentCommit(gitHelper, currentCommitId);

				List<Event> reuseActionsEvents = miningHelper.extractApplicationReuseActions();
				this.currentCommit.getEvents().clear();
				this.currentCommit.getEvents().addAll(reuseActionsEvents);
				app.getCommits().add(this.currentCommit);
				
				gitHelper.deleteRepo();

				this.currentIndex++;
			}

			gitHelper.deleteParentFolder();
			LogHelper.log("Finishing FrameworkApplication "
					+ app.getName());
		}
	}

	private void cloneCurrentCommit(GitRepositoryHelper gitHelper,
			String commitId) {
		LogHelper.log("Cloning commit: " + commitId);
		gitHelper.cloneFromCommit(commitId);
	}

	private Commit createCommit(String id) {
		Commit commit = Minerv1Factory.eINSTANCE.createCommit();
		commit.setName(id);
		commit.setId(id);
		return commit;
	}

}