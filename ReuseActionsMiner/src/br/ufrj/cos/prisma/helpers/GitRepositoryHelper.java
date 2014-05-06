package br.ufrj.cos.prisma.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import minerv1.FrameworkApplication;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.revwalk.RevCommit;

import br.ufrj.cos.prisma.model.GithubRepository;

public class GitRepositoryHelper {

	String repoURL;
	File repoFile;

	public GitRepositoryHelper(GithubRepository repo) {
		this.repoURL = repo.getCloneUrl();
		this.repoFile = repo.getRepoFile();
	}

	public GitRepositoryHelper(String url, File repoFile) {
		this.repoURL = url;
		this.repoFile = repoFile;
	}
	
	public Git getRepo() {
		Git git = null;
		try {
			// Repository not found. A clone command will be called.
			if (!repoExists(repoFile)) {
				cloneGitRepo();
			}
			git = Git.open(repoFile);
			git.fetch().call();

		} catch (IOException e) {
			LogHelper.log("Error: IOException");
		} catch (InvalidRemoteException e) {
			LogHelper.log("InvalidRemoteException", e.getMessage());
		} catch (TransportException e) {
			LogHelper.log("TransportException", e.getMessage());
		} catch (GitAPIException e) {
			LogHelper.log("GitAPIException", e.getMessage());
		}

		return git;
	}

	public List<String> getCommitsHistoryFromMaster() {
		// we need to clone the repository for the first time to get the correct
		// commit history
		getRepo();

		List<String> commits = new ArrayList<String>();
		// Switch to master to get log history from that branch only
		try {
			Runtime.getRuntime().exec("git checkout master", null, repoFile);
			InputStream is = Runtime.getRuntime()
					.exec("git log --format=%H", null, repoFile)
					.getInputStream();
			String log = getStringFromInputStream(is);
			String[] logArray = log.split("\n");
			for (int i = 0; i < logArray.length; i++) {
				commits.add(0, logArray[i]);
			}

		} catch (IOException e) {
			LogHelper.log("IOException", e.getMessage());
		}

		return commits;
	}

	public List<RevCommit> getCompleteCommitsHistory() {
		List<RevCommit> commitsHistory = new ArrayList<RevCommit>();
		LogCommand logcommand;
		try {

			logcommand = getRepo().log().all();
			Iterable<RevCommit> commitsIterable = logcommand.call();

			for (RevCommit commit : commitsIterable) {
				commitsHistory.add(0, commit);
			}

		} catch (IOException e) {
			LogHelper.log("IOException", e.getMessage());
		} catch (NoHeadException e) {
			LogHelper.log("NoHeadException", e.getMessage());
		} catch (GitAPIException e) {
			LogHelper.log("GitAPIException", e.getMessage());
		}

		return commitsHistory;
	}

	public void discardChanges() {
		try {
			LogHelper.log("discarging changes");
			Runtime.getRuntime().exec("git checkout -- .", null, repoFile);
		} catch (IOException e) {
			LogHelper.log("Error: IOException");
		}
	}

	public void cloneFromCommitId(String id) {
		if (id == null)
			return;

		try {
			discardChanges();
			Git git = getRepo();
			if (git == null) {
				LogHelper.log("Git repo is null", "Error during repo clone");
				return;
			}
			
			git.checkout().setName(id).call();

		} catch (JGitInternalException e) {
			LogHelper.log("JGitInternalException", e.getMessage());
		} catch (RefAlreadyExistsException e) {
			LogHelper.log("RefAlreadyExistsException", e.getMessage());
		} catch (RefNotFoundException e) {
			LogHelper.log("RefNotFoundException", e.getMessage());
		} catch (InvalidRefNameException e) {
			LogHelper.log("InvalidRefNameException", e.getMessage());
		} catch (CheckoutConflictException e) {
			LogHelper.log("CheckoutConflictException", e.getMessage());
		} catch (GitAPIException e) {
			LogHelper.log("GitAPIException", e.getMessage());
		}
	}

	public void cloneFromCommit(String commitId) {
		if (commitId == null) {
			return;
		}
		cloneFromCommitId(commitId);
	}

	public void cloneFromCommit(RevCommit commit) {
		if (commit == null) {
			return;
		}
		cloneFromCommitId(commit.name());
	}

	public void cloneFromOldestCommit() {
		List<String> commits = getCommitsHistoryFromMaster();
		if (commits == null || commits.size() == 0) {
			LogHelper.log("No commits were made to this repository.");
			return;
		}

		String firstCommit = commits.get(0);
		cloneFromCommitId(firstCommit);
	}

	public File cloneGitRepo() {
		CloneCommand clone = new CloneCommand();
		clone.setURI(repoURL).setNoCheckout(false).setDirectory(repoFile);

		try {
			clone.call();

		} catch (InvalidRemoteException e) {
			LogHelper.log("InvalidRemoteException", e.getMessage());
		} catch (TransportException e) {
			LogHelper.log("TransportException", e.getMessage());
		} catch (GitAPIException e) {
			LogHelper.log("GitAPIException", e.getMessage());
		}

		return repoFile;
	}

	public boolean repoExists(File repoFile) {
		if (!repoFile.isDirectory()) {
			return false;
		}

		try {
			File[] fileNames = repoFile.listFiles();
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i].toString().contains(".git")) {
					return true;
				}
			}

		} catch (IllegalArgumentException e) {
			LogHelper.log("IllegalArgumentException", e.getMessage());
		}

		return false;
	}

	public void deleteRepo() {
		try {
			FileUtils.deleteDirectory(repoFile);
		} catch (IOException e) {
			LogHelper.log("Couldn't delete repository folder");
			e.printStackTrace();
		}
	}

	public File getRepoFile() {
		return this.repoFile;
	}

	public void deleteParentFolder() {
		if (repoFile == null) {
			return;
		}

		try {
			LogHelper.log("Deleting Parent file: " + repoFile.getParentFile());
			FileUtils.deleteDirectory(repoFile.getParentFile());
		} catch (IOException e) {
			LogHelper.log("Couldn't delete parent folder");
		}

	}

	// convert InputStream to String
	private String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}
	
	public static GitRepositoryHelper getInstanceForApplication(FrameworkApplication app) {
//		final String REPO_CLONE_LOCAL_DIR = "/users/talitalopes/Documents/Mestrado/github/";
		final String REPO_CLONE_LOCAL_DIR = "D:/Mestrado/Miner/github/";
		String repoLocalDir = String.format("%s%s", REPO_CLONE_LOCAL_DIR,
				app.getName());
		File repoFile = new File(repoLocalDir);
		GitRepositoryHelper helper = new GitRepositoryHelper(
				app.getRepositoryUrl(), repoFile);
		return helper;
	}

}
