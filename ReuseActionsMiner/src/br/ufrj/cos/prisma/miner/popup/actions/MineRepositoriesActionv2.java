package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minerv1.Commit;
import minerv1.Event;
import minerv1.FrameworkApplication;
import minerv1.Minerv1Factory;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.FrameworkMiningHelper;
import br.ufrj.cos.prisma.helpers.GitRepositoryHelper;
import br.ufrj.cos.prisma.helpers.LogHelper;

public class MineRepositoriesActionv2 extends BaseExtractionAction {
	boolean wait;
	Set<String> discoveredEvents;

	public MineRepositoriesActionv2() {
		super();
		wait = true;
		discoveredEvents = new HashSet<String>();
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		mineReuseActionsFromRepositories();
	}

	private void mineReuseActionsFromRepositories() {

		for (FrameworkApplication app : process.getApplications()) {
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

			List<Event> reuseActionsEvents = null;
			Commit currentCommit = null;
			for (int currentIndex = 0; currentIndex < commits.size(); currentIndex++) {
				LogHelper.log(String.format("Commit %d out of %d",
						currentIndex + 1, commits.size()));

				String currentCommitId = commits.get(currentIndex);
				System.out.println("Commit: " + currentCommitId);
				cloneCurrentCommit(gitHelper, currentCommitId);
				currentCommit = createCommit(currentCommitId);
				reuseActionsEvents = miningHelper
						.extractApplicationReuseActions();

				for (Event e : reuseActionsEvents) {
					if (!this.discoveredEvents.contains(e.getId())) {
						addEventToCommit(e, currentCommit);
						System.out.println("Added: " + e.getId());
						this.discoveredEvents.add(e.getId());
					} else {
						System.out.println("Event not added: " + e.getId());
					}
				}

				app.getCommits().add(currentCommit);

				gitHelper.deleteRepo();

				save();
			}

			app.setMine(false);
			gitHelper.deleteParentFolder();
			LogHelper.log("Finishing FrameworkApplication " + app.getName());
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

	private void addEventToCommit(Event currentEvent, Commit currentCommit) {
		int position = -1;

		for (int i = 0; i < currentCommit.getEvents().size(); i++) {
			Event e = currentCommit.getEvents().get(i);
			if (e.getActivity().getName()
					.equals(currentEvent.getActivity().getName())) {
				position = i;
				i = currentCommit.getEvents().size();
			}
		}

		if (position >= 0) {
			currentCommit.getEvents().add(position, currentEvent);
		} else {
			currentCommit.getEvents().add(currentEvent);
		}
	}

}