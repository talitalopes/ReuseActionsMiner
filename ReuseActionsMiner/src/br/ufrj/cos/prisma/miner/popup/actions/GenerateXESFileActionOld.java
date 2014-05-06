package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.Calendar;

import miner.Process;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

import br.ufrj.cos.prisma.miner.openxes.XESLogGenerator;
import br.ufrj.cos.prisma.miner.popup.dialogs.GenerateLogDialog;

public class GenerateXESFileActionOld  extends ProcessMiningAction {

	public GenerateXESFileActionOld() {
		super();
	}
	
	public static void generateLog(Process process, boolean classesOnly) {
		System.out.println(String.format("Log will be generated: %s", generateFilename("test")));
		
		XESLogGenerator xesGen = new XESLogGenerator(classesOnly);
		xesGen.getXESRepresentationFromProcess(process);
		xesGen.serialize(generateFilename("test"));

	}
	
	public static void generateLog(Process process) {
		generateLog(process, false);
	}

	public static void generateLogForClasses(Process process) {
		generateLog(process, true);
	}
	
	private static String generateFilename(String prefix) {
		return String.format("%s-%d-%d-%d_%d-%d.xes",
				prefix,
				Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
				Calendar.getInstance().get(Calendar.MINUTE));
	}

	@Override
	public void run(IAction arg0) {
		GenerateLogDialog dialog = new GenerateLogDialog(shell);
		ISelection sel = part.getSite().getSelectionProvider().getSelection();
		if (sel instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) sel;
			Object firstElement = treeSelection.getFirstElement();
			if (firstElement instanceof IFile) {
				System.out.println("instanceof IFile");
				dialog.loadModel((IFile) firstElement);
			}
		}
		dialog.open();
	}
	
}
