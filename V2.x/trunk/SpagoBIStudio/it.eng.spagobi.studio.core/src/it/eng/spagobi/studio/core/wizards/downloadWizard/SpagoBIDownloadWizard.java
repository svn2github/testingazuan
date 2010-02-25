package it.eng.spagobi.studio.core.wizards.downloadWizard;

import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.sdk.documents.bo.SDKTemplate;
import it.eng.spagobi.sdk.engines.bo.SDKEngine;
import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.sdk.proxy.EnginesServiceProxy;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;
import it.eng.spagobi.studio.core.util.BiObjectUtilities;
import it.eng.spagobi.studio.core.util.FileFinder;
import it.eng.spagobi.studio.core.wizards.deployWizard.SpagoBIDeployWizardTreePage;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class SpagoBIDownloadWizard extends Wizard implements INewWizard {
	private SpagoBIDownloadWizardPage page;
	private IStructuredSelection selection;
	protected IWorkbench workbench;

	private static transient Logger logger = Logger.getLogger(SpagoBIDownloadWizard.class);

	/**
	 * Constructor for SampleNewWizard.
	 */
	public SpagoBIDownloadWizard() {
		super();
		setNeedsProgressMonitor(true);
	}


	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new SpagoBIDownloadWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		logger.debug("IN");
		boolean toReturn;
		TreeItem[] selectedItems=page.getTree().getSelection();
		if(selectedItems==null){
			logger.warn("Error; no item or multiple items selected");
		}
		else{	
			for (int i = 0; i < selectedItems.length; i++) {
				TreeItem selectedItem=selectedItems[i];
				Object docObject=selectedItem.getData();	
				SDKDocument document=(SDKDocument)docObject;
				toReturn=downloadTemplate(document);
			}
			doFinish();
		}

		logger.debug("OUT");
		return true;
	}


	public boolean downloadTemplate(SDKDocument document){
		logger.debug("IN");
		InputStream is=null;
		//try{
		Integer id=document.getId();
		SDKTemplate template=null;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			DocumentsServiceProxy docServiceProxy=proxyFactory.getDocumentsServiceProxy(); 		
			template=docServiceProxy.downloadTemplate(id);
		}
		catch (NullPointerException e) {
			logger.error("No comunication with server, check SpagoBi Server definition in preferences page");
			MessageDialog.openError(getShell(), "Error", "No comunication with server, check SpagoBi Server definition in preferences page");	
			return false;
		}
		catch (Exception e) {
			logger.error("No comunication with SpagoBI server, could not retrieve template",e);
			MessageDialog.openError(getShell(), "Error", "Could not get the template from server");	
			return false;
		}			

		// Recover information field like dataSource, dataSet, engine names!


		//Get the parameters
		String[] roles;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			DocumentsServiceProxy docServiceProxy=proxyFactory.getDocumentsServiceProxy(); 		
			roles=docServiceProxy.getCorrectRolesForExecution(id);
		}
		catch (NullPointerException e) {
			logger.error("No comunication with server, check SpagoBi Server definition in preferences page",e);
			MessageDialog.openError(getShell(), "Error", "No comunication with server, check SpagoBi Server definition in preferences page");	
			return false;
		}		
		catch (Exception e) {
			logger.error("No comunication with SpagoBI server, could not retrieve roles for execution",e);
			MessageDialog.openError(getShell(), "Could not retrieve roles for execution", "Could not retrieve roles for execution");	
			return false;
		}			
		if(roles==null || roles.length==0){
			logger.error("No roles for execution found",null);
			MessageDialog.openError(getShell(), "No roles for execution found", "No roles for execution found");	
			return false;			
		}

		//SDKDocumentParameter[] parameters=null;

		SDKDocumentParameter[] parameters=null;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			DocumentsServiceProxy docServiceProxy=proxyFactory.getDocumentsServiceProxy(); 		
			parameters=docServiceProxy.getDocumentParameters(id, roles[0]);
		}
		catch (NullPointerException e) {
			logger.error("No comunication with server, check SpagoBi Server definition in preferences page",e);
			MessageDialog.openError(getShell(), "Error", "No comunication with server, check SpagoBi Server definition in preferences page");	
			return false;
		}		
		catch (Exception e) {
			logger.error("No comunication with SpagoBI server, could not retrieve document parameters",e);
			MessageDialog.openError(getShell(), "Could not retrieve document parameters for execution", "Could not retrieve roles for execution");	
			return false;
		}			




		// get the extension
		Integer engineId=document.getEngineId();
		SDKProxyFactory proxyFactory=new SDKProxyFactory();
		EnginesServiceProxy engineProxy=proxyFactory.getEnginesServiceProxy();

		SDKEngine sdkEngine=null;
		try{
			sdkEngine=engineProxy.getEngine(engineId);
		}
		catch (Exception e) {
			logger.error("No comunication with SpagoBI server, could not get engine", e);
			MessageDialog.openError(getShell(), "", "Could not get engine the template from server");	
			return false;
		}		

		String type=document.getType();
		String engineName=sdkEngine!=null?sdkEngine.getLabel(): null;
		String extension=BiObjectUtilities.getFileExtension(type, engineName);

		// create the file in the selected directory
		// get the folder selected 
		Object objSel = selection.toList().get(0);
		Folder folderSel = null;
		folderSel=(Folder)objSel;  
		String projectName = folderSel.getProject().getName();

		//Take workspace
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// get the folder where to insert the template document
		IProject project = root.getProject(projectName);
		IPath pathFolder = folderSel.getProjectRelativePath(); 

		String templateFileName=template.getFileName();

		// remove previous extensions only if a new Extension was found
		String fileName="";
		if(extension!=null){
			int indexPoint=templateFileName.indexOf('.');
			if(indexPoint!=-1){
				templateFileName=templateFileName.substring(0, indexPoint);
				fileName=templateFileName+extension;
			}
		}
		else{
			fileName=templateFileName;
		}

		IPath pathNewFile = pathFolder.append(fileName); 
		IFile newFile = project.getFile(pathNewFile);
		DataHandler dh=template.getContent(); 
		try {
			is=dh.getInputStream();
		} catch (IOException e1) {
			SpagoBILogger.errorLog("Error in writing the file", e1);
			return false;
		}

		IPath projectFolder=project.getLocation();
		// Check there is not another existing file with the same name inside project directory workspace!!!
		boolean alreadyFound=FileFinder.fileExistsInSubtree(fileName, projectFolder.toString());

		if(alreadyFound){
			MessageDialog.openWarning(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "File "+pathNewFile+" already exists in your project: to download it againg you must first delete the existing one");
			logger.warn("File "+pathNewFile+" already exists in your project: to download it againg you must first delete the existing one");
			return false;
			//write=MessageDialog.openQuestion(workbench.getActiveWorkbenchWindow().getShell(), "File exists: Overwrite?", "File "+newFile.getName()+" already exists, overwrite?"); 
		}

		if(true){

			try{
				newFile.create(is, true, null);
			}
			catch (CoreException e) {
				logger.error("error while creating new file", e);	
				return false;
			}


			//Set File Metadata	
			try{
				newFile=BiObjectUtilities.setFileMetaData(newFile,document);
				//newFile=BiObjectUtilities.setFileMetaData(newFile,document);
			}
			catch (CoreException e) {
				logger.error("Error while setting meta data", e);	
				return false;
			}

			//Set ParametersFile Metadata	
			if(parameters.length>0){
				try{
					newFile=BiObjectUtilities.setFileParametersMetaData(newFile,parameters);

				}
				catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while setting meta data", e);	
					return false;
				}			
			}

			try{			
				newFile=BiObjectUtilities.setFileLastRefreshDateMetaData(newFile);
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error("Error while setting last refresh date", e);	
				return false;
			}			
		}
		else // choose not to overwrite the file
		{
			logger.info("Choose to not overwrite file "+newFile.getName());	
		}

		return true;

	}


	/** The worker method. Download the template and creates the file
	 * 
	 * @param document: the SdkDocument refderencing the BiObject
	 * @throws CoreException 
	 * 
	 */

	private void doFinish() {
		logger.info("Documents downloaded");
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench _workbench, IStructuredSelection _selection) {
		this.selection = _selection;
		this.workbench=_workbench;

	}


}


