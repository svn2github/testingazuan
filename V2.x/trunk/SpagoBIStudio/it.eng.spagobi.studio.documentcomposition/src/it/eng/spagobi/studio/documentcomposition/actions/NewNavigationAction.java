package it.eng.spagobi.studio.documentcomposition.actions;

import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

public class NewNavigationAction implements IViewActionDelegate{
	
	private IViewPart view = null;

	public void init(IViewPart view) {
		this.view = view;
		
	}

	public void run(IAction action) {
		SpagoBINavigationWizard sbindw = new SpagoBINavigationWizard();
		CommonViewer commViewer=((CommonNavigator) view).getCommonViewer();
		IStructuredSelection sel=(IStructuredSelection)commViewer.getSelection();

		//Object objSel = sel.toList().get(0);
		//Table tableSel = null;		
		try{
			// table in wich to insert the new navigation
			//tableSel=(Table)objSel;

			sbindw.init(PlatformUI.getWorkbench(), sel);
			// Create the wizard dialog
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbindw);
			// Open the wizard dialog
			dialog.open();

		}
		catch (Exception e) {
			//			SpagoBILogger.errorLog("no selected folder", e);			
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Unable to start wizard");		
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
