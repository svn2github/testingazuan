package it.eng.spagobi.meta.editor.multi.wizards.deployDatasetWizard;

import it.eng.spagobi.studio.utils.bo.Dataset;
import it.eng.spagobi.studio.utils.bo.Template;
import it.eng.spagobi.studio.utils.exceptions.NoActiveServerException;
import it.eng.spagobi.studio.utils.exceptions.NoDocumentException;
import it.eng.spagobi.studio.utils.services.SpagoBIServerObjectsFactory;
import it.eng.spagobi.studio.utils.util.SpagoBIStudioConstants;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeployDatasetService {

	ISelection selection; 
	private static Logger logger = LoggerFactory.getLogger(DeployDatasetService.class);
	String projectname = null;
	SpagoBIDeployDatasetWizard sbdw = null;

	public DeployDatasetService(ISelection _selection, SpagoBIDeployDatasetWizard _sbdw) {
		selection = _selection;	
		sbdw = _sbdw;
	}


	/** if document has meadata associated do the automated deploy
	 * 
	 * @return if automated eply has been done
	 */
	public boolean doAutomaticDeploy(){
		logger.debug("IN");

		IStructuredSelection sel=(IStructuredSelection)selection;

		// go on only if ysou selected a document
		Object objSel = sel.toList().get(0);
		org.eclipse.core.internal.resources.File fileSel = null;		
		fileSel=(org.eclipse.core.internal.resources.File)objSel;
		projectname = fileSel.getProject().getName();

		//if file has document metadata associated upload it, else call wizard

		String datasetId=null;
		String datasetLabel=null;
		try {
			datasetId=fileSel.getPersistentProperty(SpagoBIStudioConstants.DATASET_ID);			
			datasetLabel=fileSel.getPersistentProperty(SpagoBIStudioConstants.DATASET_LABEL);
		} catch (CoreException e) {
			logger.error("Error in retrieving dataset Label", e);		
		}

		// IF File selected has already an id of datasetassociated do the upload wiyhout asking further informations
		boolean automatic = false;
		if(datasetId!=null){
			logger.debug("Query already associated to dataset"+datasetId+" - "+datasetLabel);	
			final Integer idInteger=Integer.valueOf(datasetId);
			final String label2=datasetLabel;
			final org.eclipse.core.internal.resources.File fileSel2=fileSel;
			final NoDocumentException datasetException=new NoDocumentException();
			final NoActiveServerException noActiveServerException=new NoActiveServerException();

			IRunnableWithProgress op = new IRunnableWithProgress() {			
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					monitor.beginTask("Deploying to dataset "+label2, IProgressMonitor.UNKNOWN);

					if(projectname == null){
						projectname = fileSel2.getProject().getName();
					}

					try{
						logger.debug("dataset associated, upload the query to dataset "+label2);

						SpagoBIServerObjectsFactory spagoBIServerObjects = new SpagoBIServerObjectsFactory(projectname);


						URI uri=fileSel2.getLocationURI();

						File fileJava=new File(uri.getPath()); 
						FileDataSource fileDataSource=new FileDataSource(fileJava);
						DataHandler dataHandler=new DataHandler(fileDataSource);			
					// TO DO
						Template template=new Template();
						template.setFileName(fileSel2.getName());
						template.setContent(dataHandler);

						// check ataset still exists
						Dataset ds=spagoBIServerObjects.getServerDatasets().getDataSet(idInteger);
						if(ds==null){
							datasetException.setNoDocument(true);
							logger.warn("Dataset no more present on server: with id "+idInteger);					
						}
						else{
							datasetException.setNoDocument(false);
							spagoBIServerObjects.getServerDocuments().uploadTemplate(idInteger, template);
						}
					}

					catch (NoActiveServerException e1) {
						// no active server found 
						noActiveServerException.setNoServer(true);
					}
					catch (RemoteException e) {
						logger.error("Error comunicating with server", e);		
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
								"Error comunicating with server", "Error while uploading the template: missing comunication with server");	
					}

					monitor.done();
					if (monitor.isCanceled())
						logger.error("Operation not ended",new InterruptedException("The long running operation was cancelled"));
				}
			};


			ProgressMonitorDialog dialog=new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());		
			try {
				dialog.run(true, true, op);
			} 
			catch (InvocationTargetException e1) {
				logger.error("Error comunicating with server", e1);			
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Error", "Missing comunication with server; check server definition and if service is avalaible");	
				dialog.close();
				return false;
			} catch (InterruptedException e1) {
				logger.error("Error comunicating with server", e1);			
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Error", "Missing comunication with server; check server definition and if service is avalaible");	
				dialog.close();
				return false;
			} 
			if(datasetException.isNoDocument()){
				logger.error("Document no more present");			
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Error upload", "Dataset is no more present on server; you can do a new deploy");	
				sbdw.setNewDeployFromOld(true);
				return false;
			}
			if(noActiveServerException.isNoServer()){
				logger.error("No server is defined active");			
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Error", "No server is defined active");	
				return false;
			}

			dialog.close();
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),"Deploy succesfull", "Deployed to the associated dataset "+datasetLabel+" succesfull");		
			logger.debug("Deployed to the associated document "+datasetLabel+" succesfull");		
			automatic = true;
		}
		else{
			automatic = false;
		}

		if(!automatic)
		{
			logger.debug("deploy a new Dataset: start wizard");		
			// init wizard
			sbdw.init(PlatformUI.getWorkbench(), sel);
			// Create the wizard dialog
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbdw);

			// Open the wizard dialog
			dialog.open();	
		}


		logger.debug("OUT");
		return automatic;



	}


}
