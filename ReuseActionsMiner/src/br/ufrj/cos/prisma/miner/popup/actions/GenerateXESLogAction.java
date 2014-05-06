package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.Calendar;
import java.util.List;

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
		xesGen.serialize(generateFilename("test"));
	}
	
	public static void generateLog(FrameworkProcess process, boolean classesOnly) {
		LogHelper.log(String.format("Log will be generated: %s", generateFilename("test")));
		
		XESLogGenerator xesGen = new XESLogGenerator(classesOnly);
		xesGen.getXESRepresentationFromProcess(process);
		xesGen.serialize(generateFilename("test-graphiti"));

	}
	
	private static String generateFilename(String prefix) {
		return String.format("%s-%d-%d-%d_%d-%d.xes",
				prefix,
				Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH + 1), 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
				Calendar.getInstance().get(Calendar.MINUTE));
	}
}
