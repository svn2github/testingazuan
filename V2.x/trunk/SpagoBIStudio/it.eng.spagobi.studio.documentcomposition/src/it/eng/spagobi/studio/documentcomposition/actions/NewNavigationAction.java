package it.eng.spagobi.studio.documentcomposition.actions;

import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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

		Object objSel = sel.toList().get(0);
		Button newButtonSel = null;	
		try{

			newButtonSel=(Button)objSel;
			final boolean[] result = new boolean[1];
			// Add Button Listener
			Listener addListener = new Listener() {
				public void handleEvent(Event event) {
			        switch (event.type) {
			        case SWT.Selection:
					//parte wizard
			        	result[0] = true;
			        	System.out.println("cliccato pulsante new");
			        }

				}
			};
			newButtonSel.addListener(SWT.Selection, addListener);
			Listener[] listeners = newButtonSel.getListeners(SWT.Selection);
			
			if(result[0]){
			
				sbindw.init(PlatformUI.getWorkbench(), sel);
				// Create the wizard dialog
				WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbindw);
				// Open the wizard dialog
				dialog.open();
			}

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
