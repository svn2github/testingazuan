package it.eng.spagobi.meta.editor.business.actions;



import it.eng.spagobi.meta.editor.multi.wizards.SpagoBIModelEditorWizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class NewSpagoBIModelAction  implements IObjectActionDelegate {

	ISelection selection;
	private static Logger logger = LoggerFactory.getLogger(NewSpagoBIModelAction.class);

	public NewSpagoBIModelAction() {
	}


	public void run(IAction action) {
		logger.debug("IN");
		SpagoBIModelEditorWizard sbindw = new SpagoBIModelEditorWizard();
		IStructuredSelection sel=(IStructuredSelection)selection;
		IFolder folderSelected = null;
		Object objSel = sel.getFirstElement();
		// selection is limited to folder
		if(objSel instanceof IFolder){
			folderSelected = (IFolder)objSel;
		}
		else{
			logger.error(" Not a folder selected: this should not be allowed");
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Not a folder", "You must select a folder in wich to create the Meta Model");			
			return;
		}

		IPath pathSelected = folderSelected.getFullPath();
		// init wizard
		sbindw.init(PlatformUI.getWorkbench(), sel);
		sbindw.setContainerFullPath(pathSelected);
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