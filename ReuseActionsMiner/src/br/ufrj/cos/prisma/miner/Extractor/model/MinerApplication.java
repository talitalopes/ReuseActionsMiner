package br.ufrj.cos.prisma.miner.Extractor.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import miner.MinerFactory;
import miner.MinerPackage;
import miner.ProcessInstance;

import org.eclipse.core.resources.IProject;

public class MinerApplication {
	ProcessInstance application;
	List<IProject> workspaceProjects;
	
	List<MinerCommit> commits;
	Set<MinerEvent> eventsOrder;
	
	public MinerApplication(String applicationName) {
		this.application = MinerFactory.eINSTANCE.createProcessInstance();
		this.application.eSet(MinerPackage.eINSTANCE.getProcess_Name(),
				applicationName);
		this.workspaceProjects = new ArrayList<IProject>();
		
		this.commits = new ArrayList<MinerCommit>();
		this.eventsOrder = new HashSet<MinerEvent>();
	}
	
	public List<IProject> getWorkspaceProjects() {
		return workspaceProjects;
	}

	public void setWorkspaceProjects(List<IProject> workspaceProjects) {
		this.workspaceProjects = workspaceProjects;
	}

	public void addWorkspaceProject(IProject project) {
		this.workspaceProjects.add(project);
	}

	public ProcessInstance getApplication() {
		return this.application;
	}
	
	public List<MinerCommit> getAllCommits() {
		return this.commits;
	}
	
	public void addCommit(MinerCommit mc) {
		this.application.getCommits().add(mc.getCommit());
	}
	
	public void addMinerCommit(MinerCommit mc) {
		this.commits.add(mc);
		this.application.getCommits().add(mc.getCommit());
	}
}
