package br.ufrj.cos.prisma.miner.popup.dialogs;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import miner.Process;

public class ProcessMiningDialog extends Dialog {
	protected Shell shell;
	protected Process process;
	protected ComposedAdapterFactory composedAdapterFactory;
	protected Resource resource;

	protected Label frameworkNameLabel;
	protected Text frameworkName;

	public ProcessMiningDialog(Shell parentShell) {
		super(parentShell);
	}

	public void loadModel(IFile model) {
		try {
			AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(
					getAdapterFactory(), new BasicCommandStack());
			resource = editingDomain.createResource(model.getFullPath().toString());
			resource.load(null);

			EObject eObject = resource.getContents().get(0);
			setProcess((Process) eObject);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Process getProcess() {
		return process;
	}
	
	protected void setProcess(Process p) {
		this.process = p;
	}

	protected void save() {
		try {
			resource.save(null);
			super.buttonPressed(OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite root = (Composite) super.createDialogArea(parent);
		root.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite labelComposite = new Composite(root, SWT.None);
		labelComposite.setLayout(new GridLayout(2, false));
		
		Label text = new Label(labelComposite, SWT.None);
		text.setText("Mining Framework Reuse Actions");
		frameworkNameLabel = new Label(labelComposite, SWT.None);
		
		root.layout();
		parent.pack();
		return parent;
	}
	
	/**
	 * Return an ComposedAdapterFactory for all registered models
	 * 
	 * @return a ComposedAdapterFactory
	 */
	protected AdapterFactory getAdapterFactory() {
		if (composedAdapterFactory == null) {
			composedAdapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		}
		return composedAdapterFactory;
	}
	
	@Override
	protected void okPressed() {
		super.okPressed();
	}
}
