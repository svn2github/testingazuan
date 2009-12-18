package it.eng.spagobi.studio.core.actions;

import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.wizards.downloadWizard.SpagoBIDownloadWizard;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

/** DownloadDocumentAction calls the SpagoBI Download Wizard
 * 
 */

public class DownloadDocumentAction implements IViewActionDelegate {

	private IViewPart view = null;

	public DownloadDocumentAction() {
	}

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		SpagoBIDownloadWizard sbindw = new SpagoBIDownloadWizard();
		CommonViewer commViewer=((CommonNavigator) view).getCommonViewer();
		IStructuredSelection sel=(IStructuredSelection)commViewer.getSelection();

		// go on only if you selected a folder
		Object objSel = sel.toList().get(0);
		Folder folderSel = null;		
		try{
			folderSel=(Folder)objSel;
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No folder selected", e);			
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Not a folder", "You must select a folder in wich to download the template");		
			return;
		}

		// init wizard
		sbindw.init(PlatformUI.getWorkbench(), sel);
		// Create the wizard dialog
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbindw);
		// Open the wizard dialog
		dialog.open();	

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
