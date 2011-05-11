package it.eng.spagobi.meta.editor.business.actions;



import it.eng.spagobi.meta.editor.multi.wizards.SpagoBIModelEditorWizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class NewSpagoBIModelAction  implements IWorkbenchWindowActionDelegate {

	ISelection selection;
	private static Logger logger = LoggerFactory.getLogger(NewSpagoBIModelAction.class);

	public NewSpagoBIModelAction() {
	}


	public void run(IAction action) {
		logger.debug("IN");
		SpagoBIModelEditorWizard sbindw = new SpagoBIModelEditorWizard();
		IStructuredSelection sel=(IStructuredSelection)selection;
		IContainer containerSelected = null;
		Object objSel = sel.getFirstElement();
		// selection is limited to folder
		if(objSel instanceof IFolder){
			containerSelected = (IFolder)objSel;
		}
		else if(objSel instanceof IFile){
			containerSelected = ((IFile)objSel).getParent();
		}
		else if(objSel instanceof IProject){
			containerSelected = (IProject)objSel;
		}

		sbindw.init(PlatformUI.getWorkbench(), sel);

		if(objSel != null){
			IPath pathSelected = containerSelected.getFullPath();
			sbindw.setContainerFullPath(pathSelected);

		}

		// init wizard
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


	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


	@Override
	public void init(IWorkbenchWindow window) {


	}

}