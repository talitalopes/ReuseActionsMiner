package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minerv1.Commit;
import minerv1.Event;
import minerv1.FrameworkApplication;
import minerv1.Minerv1Factory;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.GitRepositoryHelper;
import br.ufrj.cos.prisma.helpers.LogHelper;
import br.ufrj.cos.prisma.miner.util.ApplicationFileWalker;

public class MineRepositoriesActionv2 extends BaseExtractionAction {
	
	Set<String> discoveredEvents;
	int maxCommit = 10;
	boolean mineAllCommits = true;
	
	public MineRepositoriesActionv2() {
		super();
		discoveredEvents = new HashSet<String>();
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		mineReuseActionsFromRepositories();
	}

	private void mineReuseActionsFromRepositories() {
		LogHelper.log("Number of processes to mine: " + process.getApplicationsToMine().size());
		
		for (FrameworkApplication app : process.getApplicationsToMine()) {
			
			final GitRepositoryHelper gitHelper = GitRepositoryHelper
					.getInstanceForApplication(app);

			List<String> commits = gitHelper.getCommitsHistoryFromMaster();

			LogHelper.log("--- Starting FrameworkApplication " + app.getName());
			int limit = (commits.size() < maxCommit || mineAllCommits) ? commits.size() : maxCommit;
			LogHelper.log(String.format("%d commits found for application %s. %d commits will be mined.",
					commits.size(), app.getName(), limit));
			
			for (int currentIndex = 0; currentIndex < limit; currentIndex++) {
				LogHelper.log(String.format("Commit %d out of %d",
						currentIndex + 1, commits.size()));

				String currentCommitId = commits.get(currentIndex);
				gitHelper.cloneFromCommit(currentCommitId);
				
				LogHelper.log("Current commit Id: " + currentCommitId);
				Commit currentCommit = Minerv1Factory.eINSTANCE.createCommit(currentCommitId);
				
				ApplicationFileWalker walker = new ApplicationFileWalker(this.process);
				walker.walk(gitHelper.getRepoFile().getAbsolutePath());
				List<Event> reuseActionsEvents = walker.getReuseActions();
				LogHelper.log("Events found: " + reuseActionsEvents.size());
				
				for (Event e : reuseActionsEvents) {
//					if (!this.discoveredEvents.contains(e.getId())) {
//						System.out.println("Add: " + e.getId());
//						addEventToCommit(e, currentCommit);
//						this.discoveredEvents.add(e.getId());
//					} else {
//						System.out.println("Event not added: " + e.getId());
//					}
					addEventToCommit(e, currentCommit);
				}

				app.getCommits().add(currentCommit);
				save();
			}

			app.setMine(false);			
			save();
			gitHelper.deleteRepo();
			gitHelper.deleteParentFolder();
			LogHelper.log("--- Finishing FrameworkApplication " + app.getName());
		}
	}

	private void addEventToCommit(Event currentEvent, Commit currentCommit) {
		int position = -1;

		for (int i = 0; i < currentCommit.getEvents().size(); i++) {
			// Group activities
			Event e = currentCommit.getEvents().get(i);
			if (e.getActivity().getName()
					.equals(currentEvent.getActivity().getName())) {
				position = i;
				break;
//				i = currentCommit.getEvents().size();
			}
		}

		if (position >= 0) {
			if (!currentCommit.getEvents().contains(currentEvent)) {
				currentCommit.getEvents().add(position, currentEvent);
			} else {
				System.out.println("Error:: Duplicated: " + currentEvent.getId() + ": " + currentCommit.getId());
			}
		} else {
			currentCommit.getEvents().add(currentEvent);
		}
	}

}