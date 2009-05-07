package it.eng.spagobi.studio.dashboard.actions;

import javax.net.ssl.SSLEngineResult.Status;

import it.eng.spagobi.studio.dashboard.Activator;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.dashboard.wizards.SpagoBINewDashboardWizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

public class NewDashboardAction implements IViewActionDelegate {

	private IViewPart view = null;
	
	public NewDashboardAction() {
		// TODO Auto-generated constructor stub
	}

	public void init(IViewPart view) {
		this.view = view;
		System.out.println("init");
		
	}

	public void run(IAction action) {
		System.out.println("run");
		SpagoBINewDashboardWizard sbindw = new SpagoBINewDashboardWizard();
		

		
		CommonViewer commViewer=((CommonNavigator) view).getCommonViewer();
		
		IStructuredSelection sel=(IStructuredSelection)commViewer.getSelection();

		SpagoBILogger.infoLog("Create  a new Dashboard in folder "+sel.toString());

		
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
