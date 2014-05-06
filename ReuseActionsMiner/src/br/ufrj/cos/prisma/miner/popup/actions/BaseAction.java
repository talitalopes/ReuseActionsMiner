package br.ufrj.cos.prisma.miner.popup.actions;

import java.io.IOException;

import minerv1.FrameworkProcess;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class BaseAction implements IObjectActionDelegate {
	protected Shell shell;
	protected IWorkbenchPart part;
	protected FrameworkProcess process;
	protected Resource resource;
	protected ComposedAdapterFactory composedAdapterFactory;

	public BaseAction() {
		super();
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		part = targetPart;
	}

	@Override
	public void run(IAction action) {
		ISelection sel = part.getSite().getSelectionProvider().getSelection();

		if (sel instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) sel;
			Object firstElement = treeSelection.getFirstElement();

			if (firstElement instanceof IFile) {
				System.out.println("Loading process");
				loadModel((IFile) firstElement);
			}
		}

	}

	public void loadModel(IFile model) {
		try {
			AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(
					getAdapterFactory(), new BasicCommandStack());
			resource = editingDomain.createResource(model.getFullPath()
					.toString());
			resource.load(null);

			EObject eObject = resource.getContents().get(0);
			this.process = (FrameworkProcess) eObject;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

}