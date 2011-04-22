package it.eng.spagobi.meta.editor.business.actions;



import it.eng.spagobi.meta.editor.multi.wizards.SpagoBIModelEditorWizard;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jface.wizard.WizardDialog;



public class NewSpagoBIModelAction  implements IObjectActionDelegate {

	ISelection selection;
	private static Logger logger = LoggerFactory.getLogger(NewSpagoBIModelAction.class);

	public NewSpagoBIModelAction() {
	}


	public void run(IAction action) {
		logger.debug("IN");
		SpagoBIModelEditorWizard sbindw = new SpagoBIModelEditorWizard();
		IStructuredSelection sel=(IStructuredSelection)selection;

		// init wizard
		sbindw.init(PlatformUI.getWorkbench(), sel);
		// Create the wizard dialog
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbindw);
		// Open the wizard dialog
		dialog.open();	
		logger.debug("OUT");


	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;		
	}

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub

	}

}