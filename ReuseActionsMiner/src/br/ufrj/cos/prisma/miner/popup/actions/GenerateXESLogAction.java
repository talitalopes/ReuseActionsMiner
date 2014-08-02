package br.ufrj.cos.prisma.miner.popup.actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.text.DateFormatter;

import minerv1.Event;
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
			List<Event> events = app.getOrderedListOfEvents();
			System.out.println("Events: " + events.size());
		}
		
		XESLogGenerator xesGen = new XESLogGenerator(true);
		xesGen.getXESRepresentationFromProcess(process);
		xesGen.serialize(generateFilename(process.getKeyword()));
	}
	
	public static void generateLog(FrameworkProcess process, boolean classesOnly) {
		LogHelper.log(String.format("Log will be generated: %s", generateFilename("test")));
		
		XESLogGenerator xesGen = new XESLogGenerator(classesOnly);
		xesGen.getXESRepresentationFromProcess(process);
		xesGen.serialize(generateFilename("test-graphiti"));

	}
	
	private static String generateFilename(String prefix) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd_HH:mm");
		String date = dateFormat.format(Calendar.getInstance().getTime());
		
		return String.format("%s-%s.xes",
				prefix,
				date);
	}
}
