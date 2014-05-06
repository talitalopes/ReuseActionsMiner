package br.ufrj.cos.prisma.miner.popup.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import br.ufrj.cos.prisma.miner.Extractor.ReuseActionsExtractor;
import br.ufrj.cos.prisma.miner.util.Constants;


public class MiningInitialConfigurationDialog extends ProcessMiningDialog {
		
	public MiningInitialConfigurationDialog(Shell parentShell) {
		super(parentShell);
		shell = parentShell;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, Constants.ID_START_MINING, Constants.LABEL_START_MINING, true);
		super.createButtonsForButtonBar(parent);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case Constants.ID_START_MINING:
			startMining();
			save();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}
	
	protected void startMining() {
		ReuseActionsExtractor.start(process, shell);
	}

}