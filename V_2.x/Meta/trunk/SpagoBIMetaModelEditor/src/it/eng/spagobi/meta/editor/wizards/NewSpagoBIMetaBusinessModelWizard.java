/*
 * This Wizard create a new SpagoBI Meta project
 */

package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;

import java.io.File;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;



public class NewSpagoBIMetaBusinessModelWizard extends Wizard implements INewWizard{

	private NewBusinessClassWizardPage1 one;
	
	public NewSpagoBIMetaBusinessModelWizard() {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Business Model");
		
	}

	@Override
	public void addPages() {
		one = new NewBusinessClassWizardPage1("New SpagoBI BM Project Page");
		addPage(one);
	}
	
	@Override
	public boolean performFinish() {		
        String path = one.getDirPath().toString()+"/"+one.getBmName();
        if (path != null){
             File dir = new File(path);
	   		 if (!dir.exists())
	   		 {
	   			//create directory with Business Model Name
	   			dir.mkdir();
	   			
	   			//Create CoreSingleton and initialize root Model
	   			CoreSingleton cs = CoreSingleton.getInstance();
	   			cs.initModel();
	   			cs.setBmName(one.getBmName());
	   			cs.setBmPath(one.getDirPath().toString()+"/"+one.getBmName());
	   			
	   			//launch perspective
	   			IWorkbench workbench = PlatformUI.getWorkbench();
	   			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	   			try {
					workbench.showPerspective("it.eng.spagobi.meta.editor.perspective", window);
				} catch (WorkbenchException e) {
					reportError(e);
				}

	   			return true;
	   		 }
        }
		return false;
		
	}

	private void reportError(Exception x) {
		ErrorDialog.openError(getShell(), "Error", "Error in New Business Model", makeStatus(x));
	}	

	public static IStatus makeStatus(Exception x){
	    return new Status(IStatus.ERROR, "", IStatus.ERROR, x.getMessage(), null);
	}	
	
	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {

	}
}
