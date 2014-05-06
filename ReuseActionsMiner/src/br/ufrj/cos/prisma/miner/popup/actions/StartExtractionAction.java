package br.ufrj.cos.prisma.miner.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

import br.ufrj.cos.prisma.miner.popup.dialogs.MiningInitialConfigurationDialog;

public class StartExtractionAction extends ProcessMiningAction {

	public StartExtractionAction() {
		super();
	}

	@Override
	public void run(IAction arg0) {
		MiningInitialConfigurationDialog dialog = new MiningInitialConfigurationDialog(
				shell);
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
