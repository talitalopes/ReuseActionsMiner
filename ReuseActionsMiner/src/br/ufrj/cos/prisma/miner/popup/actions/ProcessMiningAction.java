package br.ufrj.cos.prisma.miner.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ProcessMiningAction implements IObjectActionDelegate {
	protected Shell shell;
	protected IWorkbenchPart part;
	
	public ProcessMiningAction() {
		super();
	}
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		part = targetPart;
	}
	
	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

}
