package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.List;

import minerv1.FrameworkApplication;
import minerv1.Minerv1Factory;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.LogHelper;
import br.ufrj.cos.prisma.helpers.RepositoriesHelper;
import br.ufrj.cos.prisma.miner.util.Constants;
import br.ufrj.cos.prisma.model.GithubRepository;

public class ListRepositoriesAction extends BaseAction {
	public ListRepositoriesAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		listRepositories();
	}

	private List<GithubRepository> listRepositories() {
		String searchKeywork = getSearchKeywork();
		LogHelper.log("Search for repositories using keyword: "
				+ searchKeywork);

		List<GithubRepository> repositories = RepositoriesHelper
				.listRepositories(searchKeywork); // "JJTV5_gef"

		// create applications for repositories
		int index = 1;
		for (GithubRepository repo : repositories) {
			FrameworkApplication app = Minerv1Factory.eINSTANCE
					.createFrameworkApplication();
			app.setName(repo.getName());
			app.setRepositoryUrl(repo.getCloneUrl());

			System.out.println(index + " - Add application " + app.getName()
					+ " to process " + process.getName());
			this.process.getApplications().add(app);
			index++;
		}
		save();

		return repositories;
	}

	private String getSearchKeywork() {
		
		if (this.process.getKeyword() != null) {
			return this.process.getKeyword();
		}
		return Constants.REPOSITORIES_SEARCH_KEYWORK;
	}

}
