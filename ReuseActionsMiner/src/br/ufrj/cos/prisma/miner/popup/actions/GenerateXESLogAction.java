package br.ufrj.cos.prisma.miner.popup.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import minerv1.FrameworkApplication;
import minerv1.FrameworkProcess;

import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.LogHelper;
import br.ufrj.cos.prisma.miner.openxes.XESLogGenerator;

public class GenerateXESLogAction extends BaseAction {

	@Override
	public void run(IAction action) {
		super.run(action);
		
		for (FrameworkApplication app: this.process.getApplications()) {
			System.out.println("Events: " + app.getEventsCount());
		}
		
		XESLogGenerator xesGen = new XESLogGenerator(true);
		xesGen.getXESRepresentationFromProcess(process);
		
//		Testing log separated by commits
//		xesGen.getXESRepresentationForCommits(process);

		xesGen.serialize(generateFilename(process.getKeyword()));
	}
	
	private static String generateFilename(String prefix) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd_HH:mm");
		String date = dateFormat.format(Calendar.getInstance().getTime());
		
		return String.format("%s-%s.xes",
				prefix,
				date);
	}
}
