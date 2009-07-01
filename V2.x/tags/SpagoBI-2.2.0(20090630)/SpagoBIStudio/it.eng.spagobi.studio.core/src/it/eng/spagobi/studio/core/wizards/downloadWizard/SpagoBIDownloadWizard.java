package it.eng.spagobi.studio.core.wizards.downloadWizard;

import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKTemplate;
import it.eng.spagobi.sdk.engines.bo.SDKEngine;
import it.eng.spagobi.sdk.exceptions.NotAllowedOperationException;
import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.sdk.proxy.EnginesServiceProxy;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class SpagoBIDownloadWizard extends Wizard implements INewWizard {
	private SpagoBIDownloadWizardPage page;
	private IStructuredSelection selection;
	protected IWorkbench workbench;


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

		TreeItem[] selectedItems=page.getTree().getSelection();
		if(selectedItems==null || selectedItems.length!=1){
			SpagoBILogger.errorLog("Error; no item or multiple items selected", null);
		}
		else{	
			TreeItem selectedItem=selectedItems[0];
			Object docObject=selectedItem.getData();
			SDKDocument document=(SDKDocument)docObject;
			doFinish(document);
		}

		return true;
	}

	/** The worker method. Download the template and creates the file
	 * 
	 * @param document: the SdkDocument refderencing the BiObject
	 * @throws CoreException 
	 * 
	 */

	private void doFinish(SDKDocument document) {
		InputStream is=null;
		//try{
		Integer id=document.getId();
		SDKProxyFactory proxyFactory=new SDKProxyFactory();
		SDKTemplate template=null;

		try{
			DocumentsServiceProxy docServiceProxy=proxyFactory.getDocumentsServiceProxy(); 
			template=docServiceProxy.downloadTemplate(id);
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No comunication with SpagoBI server, could not retrieve template", e);
			MessageDialog.openError(getShell(), "Could not get template from server", "Could not get the template from server");	
			return;
		}			

		// get the extension

		Integer engineId=document.getEngineId();
		EnginesServiceProxy engineProxy=proxyFactory.getEnginesServiceProxy();

		SDKEngine sdkEngine=null;
		try{
			sdkEngine=engineProxy.getEngine(engineId);
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No comunication with SpagoBI server, could not get engine", e);
			MessageDialog.openError(getShell(), "Could not get template from server", "Could not get engine the template from server");	
			return;
		}		

		String type=document.getType();
		String engineName=sdkEngine!=null?sdkEngine.getLabel():"";
		String extension=getFileExtension(type, engineName);

		// create the file in the selected directory
		// get the folder selected 
		Object objSel = selection.toList().get(0);
		Folder folderSel = null;
		folderSel=(Folder)objSel;  
		String projectName = folderSel.getProject().getName();

		//Take workspace
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath workspacePath=root.getLocation();

		// get the folder where to insert the template document
		IProject project = root.getProject(projectName);
		IPath pathFolder = folderSel.getProjectRelativePath();

		String templateFileName=template.getFileName();

		// remove previous extensions
		int indexPoint=templateFileName.indexOf('.');
		if(indexPoint!=-1){
			templateFileName=templateFileName.substring(0, indexPoint);
		}

		IPath pathNewFile = pathFolder.append(templateFileName+extension); 
		IFile newFile = project.getFile(pathNewFile);
		DataHandler dh=template.getContent(); 
		try {
			is=dh.getInputStream();
		} catch (IOException e1) {
			SpagoBILogger.errorLog("Error in writing the file", e1);
			return;
		}



		// check if file exists, in case ask user if overwrite
		boolean write=true;
		if(newFile.exists()==true){
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "Dashboard type not set");
			write=MessageDialog.openQuestion(workbench.getActiveWorkbenchWindow().getShell(), "Overwrite?", "File "+newFile.getName()+" already exists, overwrite?"); 
		}

		if(write){

			try{
				newFile.create(is, true, null);
			}
			catch (CoreException e) {
				SpagoBILogger.errorLog("error while creating new file", e);	
				return;
			}

			
			//Set File Metadata	
			try{
				newFile=setFileMetaData(newFile,document);
			}
			catch (CoreException e) {
				SpagoBILogger.errorLog("Error while setting meta data", e);	
				return;
			}
			
			

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();
			IEditorRegistry er = wb.getEditorRegistry();
			IEditorDescriptor editordesc =  er.getDefaultEditor(newFile.getName());
			if(editordesc!=null){
				try {
					page.openEditor(new FileEditorInput(newFile), editordesc.getId());
				} catch (PartInitException e) {
					SpagoBILogger.errorLog("Error while opening editor", e);
					MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
							"Error", "Error while opening editor");
				}
				SpagoBILogger.infoLog("File "+newFile.getName()+" created");	
			}
			else{
				SpagoBILogger.infoLog("No editor avalaible for the selected Bi Object type: "+type+ " and engine "+engineName);	
				MessageDialog.openWarning(getShell(), "No editor avalaible", "No editor avalaible for the selected Bi Object type: "+type+ " and engine "+engineName);
			}
		}
		else // choose not to overwrite the file
		{
			SpagoBILogger.infoLog("Choose to not overwrite file "+newFile.getName());	
		}

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


	private String getFileExtension(String type, String engine){
		String extension=".xml";
		if(type.equalsIgnoreCase("DASH") && engine.equalsIgnoreCase("DashboardInternalEng") ){
			extension=".sbidash";
		}
		else if(type.equalsIgnoreCase("DASH") && engine.equalsIgnoreCase("ChartInternalEngine")){
			extension=".sbichart";
		}
		else if(type.equalsIgnoreCase("REPORT") && engine.equalsIgnoreCase("Birt")){
			extension=".xml";
		}
		else if(type.equalsIgnoreCase("REPORT") && engine.equalsIgnoreCase("JasperReport")){
			extension=".xml";
		}	
		else if(type.equalsIgnoreCase("OLAP") && engine.equalsIgnoreCase("JPIVOT")){
			extension=".xml";
		}
		else if(type.equalsIgnoreCase("MAP") && engine.equalsIgnoreCase("GeoEngine")){
			extension=".xml";
		}		
		else if(type.equalsIgnoreCase("OFFICE_DOC") && engine.equalsIgnoreCase("OfficeInternalEng")){
			extension=".xml";
		}
		else if(type.equalsIgnoreCase("ETL") && engine.equalsIgnoreCase("TALEND")){
			extension=".xml";
		}		
		else if(type.equalsIgnoreCase("Dossier") && engine.equalsIgnoreCase("Dossier")){
			extension=".xml";
		}
		else if(type.equalsIgnoreCase("Composite") && engine.equalsIgnoreCase("CompositeDocument")){
			extension=".xml";
		}
		else if(type.equalsIgnoreCase("DATA_MINING") && engine.equalsIgnoreCase("Weka")){
			extension=".xml";
		}
		else if(type.equalsIgnoreCase("DATAMART") && engine.equalsIgnoreCase("Qbe")){
			extension=".xml";
		}
		return extension;

	}


	private IFile setFileMetaData(IFile newFile, SDKDocument document) throws CoreException{
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_ID, document.getId().toString());
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_LABEL, document.getLabel());
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_NAME, document.getName());
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_DESCRIPTION, document.getDescription());
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_STATE, document.getState());
		newFile.setPersistentProperty(PropertyPage.DOCUMENT_TYPE, document.getType());
		newFile.setPersistentProperty(PropertyPage.DATA_SOURCE_ID, (document.getDataSourceId()!=null?document.getDataSourceId().toString(): ""));
		newFile.setPersistentProperty(PropertyPage.DATASET_ID, (document.getDataSetId()!=null?document.getDataSetId().toString(): ""));
		newFile.setPersistentProperty(PropertyPage.ENGINE_ID, (document.getEngineId()!=null?document.getEngineId().toString(): ""));
		return newFile;
	}

}


