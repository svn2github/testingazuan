package it.eng.spagobi.studio.core.actions;

import java.io.File;
import java.net.URI;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import it.eng.spagobi.sdk.documents.bo.SDKTemplate;
import it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;
import it.eng.spagobi.studio.core.wizards.deployWizard.SpagoBIDeployWizard;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.runtime.CoreException;
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

public class DeployDocumentAction implements IViewActionDelegate {

	private IViewPart view = null;

	public DeployDocumentAction() {
	}


	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		SpagoBIDeployWizard sbindw = new SpagoBIDeployWizard();
		CommonViewer commViewer=((CommonNavigator) view).getCommonViewer();
		IStructuredSelection sel=(IStructuredSelection)commViewer.getSelection();

		// go on only if you selected a document
		Object objSel = sel.toList().get(0);
		org.eclipse.core.internal.resources.File fileSel = null;		
		try{
			fileSel=(org.eclipse.core.internal.resources.File)objSel;
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No file selected", e);			
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Not a file", "You must select a file to deploy");		
			return;
		}

		//if file has document metadata associated upload it, else call wizard

		String document_idString=null;
		String document_label=null;
		try {
			document_idString=fileSel.getPersistentProperty(PropertyPage.DOCUMENT_ID);			
			document_label=fileSel.getPersistentProperty(PropertyPage.DOCUMENT_LABEL);
		} catch (CoreException e) {
			SpagoBILogger.errorLog("Error in retrieving document Label", e);
		}

		if(document_idString!=null){
			// document associated, upload the template
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			DocumentsServiceProxy docServiceProxy=proxyFactory.getDocumentsServiceProxy();
			
			Integer id=Integer.valueOf(document_idString);
			URI uri=fileSel.getLocationURI();

			File fileJava=new File(uri.getPath()); 
			FileDataSource fileDataSource=new FileDataSource(fileJava);
			DataHandler dataHandler=new DataHandler(fileDataSource);			
			SDKTemplate sdkTemplate=new SDKTemplate();
			sdkTemplate.setFileName(fileSel.getName());
			sdkTemplate.setContent(dataHandler);
			
			try {
				docServiceProxy.uploadTemplate(id, sdkTemplate);
			} catch (NotAllowedOperationException e) {
				SpagoBILogger.errorLog("Not Allowed Operation", e);			
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Error upload", "Error while uploading the template: not allowed operation");	
			} catch (RemoteException e) {
				SpagoBILogger.errorLog("Error comunicating with server", e);			
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Error comunicating with server", "Error while uploading the template: missing comunication with server");	
			}
			
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),"Deploy succesfull", "Deployed to the associated document succesfull");		
		}
		else{
			// init wizard
			sbindw.init(PlatformUI.getWorkbench(), sel);
			// Create the wizard dialog
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbindw);
			// Open the wizard dialog
			dialog.open();	
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}