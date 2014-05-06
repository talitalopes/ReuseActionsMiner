package br.ufrj.cos.prisma.miner.Extractor.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import miner.Commit;
import miner.MinerFactory;
import miner.MinerPackage;

import org.eclipse.core.resources.IProject;

public class MinerCommit {

	Commit c;
	IProject workspaceProject;

	public MinerCommit(String id, int commitTime) {
		this.c = MinerFactory.eINSTANCE.createCommit();
		c.eSet(MinerPackage.eINSTANCE.getCommit_Id(), id);

		Timestamp stamp = new Timestamp(commitTime);
		Date date = new Date(stamp.getTime());
		c.eSet(MinerPackage.eINSTANCE.getCommit_Date(), date);
	}

	public MinerCommit(String[] keys) {
		this.c = MinerFactory.eINSTANCE.createCommit();
		c.eSet(MinerPackage.eINSTANCE.getCommit_Id(), keys[keys.length - 1]);
		c.eSet(MinerPackage.eINSTANCE.getCommit_Date(),
				getCommitDate(keys[keys.length - 2]));
	}

	public MinerCommit(String[] keys, IProject project) {
		this.c = MinerFactory.eINSTANCE.createCommit();
		c.eSet(MinerPackage.eINSTANCE.getCommit_Id(), keys[keys.length - 1]);
		// c.eSet(MinerPackage.eINSTANCE.getCommit_Date(),
		// getCommitDate(keys[keys.length - 2]));
		this.workspaceProject = project;
	}

	private Date getCommitDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date commitDate = null;
		try {
			commitDate = formatter.parse(date);
		} catch (ParseException e) {
			commitDate = Calendar.getInstance().getTime();
		}
		return commitDate;
	}

	public void addEvent(MinerEvent e) {
		this.c.getEvents().add(e.getEvent());
	}

	public Commit getCommit() {
		return this.c;
	}

	public IProject getWorkspaceProject() {
		return workspaceProject;
	}

	public void setWorkspaceProject(IProject workspaceProject) {
		this.workspaceProject = workspaceProject;
	}

	public Date getDate() {
		return c.getDate();
	}

}
