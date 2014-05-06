package br.ufrj.cos.prisma.miner.popup.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import br.ufrj.cos.prisma.miner.popup.actions.GenerateXESFileActionOld;
import br.ufrj.cos.prisma.miner.util.Constants;

public class GenerateLogDialog extends ProcessMiningDialog {
	
	public GenerateLogDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, Constants.ID_GENERATE_LOG, Constants.LABEL_GENERATE_LOG, true);
		createButton(parent, Constants.ID_GENERATE_LOG_CLASSES_ONLY, Constants.LABEL_GENERATE_CLASSES_LOG, true);
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case Constants.ID_GENERATE_LOG:
			generateXESLog();
			break;
		case Constants.ID_GENERATE_LOG_CLASSES_ONLY:
			generateXESLogForClasses();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}
		
	protected void generateXESLog() {
		GenerateXESFileActionOld.generateLog(getProcess());
	}

	protected void generateXESLogForClasses() {
		GenerateXESFileActionOld.generateLogForClasses(getProcess());
	}

}