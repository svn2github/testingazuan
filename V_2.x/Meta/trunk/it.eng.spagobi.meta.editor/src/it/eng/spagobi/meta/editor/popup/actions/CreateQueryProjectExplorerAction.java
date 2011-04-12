package it.eng.spagobi.meta.editor.popup.actions;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.editor.multi.wizards.NewQueryFileProjectExplorerWizard;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.business.BusinessModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateQueryProjectExplorerAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelectionProvider selectionProvider;
	private static Logger logger = LoggerFactory.getLogger(CreateQueryProjectExplorerAction.class);

	
	/**
	 * Constructor for Action.
	 */
	public CreateQueryProjectExplorerAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		selectionProvider = targetPart.getSite().getSelectionProvider();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		MessageDialog.openInformation(
			shell,
			"it.eng.spagobi.meta.editor",
			"New Query was executed.");
		
		//create Query File
		IFile queryFile = createQueryFile();
		
		//init Query File contents
		initQueryFileContents(queryFile);
		
		//open Query Editor
		openQueryEditor(queryFile);
	}
	
	public IFile createQueryFile(){
		//Create Folder Wizard
		NewQueryFileProjectExplorerWizard wizard = new NewQueryFileProjectExplorerWizard();
    	wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
    	WizardDialog dialog = new WizardDialog(wizard.getShell(), wizard);
		dialog.create();
    	dialog.open();
    	
    	IFile queryFile = wizard.getFile();
		logger.debug("New Query File is [{}]",queryFile.getRawLocation().toOSString());
		return queryFile;
	}
	
	public void initQueryFileContents(IFile queryFile){
		
		ISelection selection = selectionProvider.getSelection();
		logger.debug("Project Explorer Selection is [{}]",selection);
		logger.debug("Selection Class is [{}]",selection.getClass().getName());
		TreeSelection treeSelection = (TreeSelection)selection;
		IFile iFile = (IFile)treeSelection.getFirstElement();
		String modelPath = iFile.getRawLocation().toOSString();
		logger.debug("Model path [{}]",modelPath);

		String modelDirectory = new File(modelPath).getParent();
		XMIResourceImpl resource = new XMIResourceImpl();
		File source = new File(modelPath);
		ModelPackage libraryPackage = ModelPackage.eINSTANCE;
		try {
			resource.load( new FileInputStream(source), new HashMap<Object,Object>());
		} catch (FileNotFoundException e) {
			throw new SpagoBIPluginException("Create Query: File not found",e);
		} catch (IOException e) {
			throw new SpagoBIPluginException("Create Query: IO Error",e);
		}
		Model root = (Model) resource.getContents().get(0);
		logger.debug("Model root is [{}] ",root );

		BusinessModel businessModel;
		businessModel = root.getBusinessModels().get(0);
		logger.debug("Business Model name is [{}] ",businessModel.getName() );

		if(queryFile.exists()){

			JSONObject o;
			String queryContent ;
			try {
				o = new JSONObject(); 
				JSONObject queryMeta = new JSONObject(); 
				o.put("queryMeta", queryMeta);
				queryMeta.put("modelPath",modelPath);
				queryContent = o.toString(3);
			} catch (JSONException e) {
				throw new SpagoBIPluginException("Impossibile to create JSON Metadata ",e);
			}
			try {
				StringBufferInputStream inputStream = new StringBufferInputStream(queryContent);
				queryFile.setContents(inputStream, IResource.NONE,null);

			} catch (CoreException e) {
				e.printStackTrace();
			} 
		}
	}

	protected void openQueryEditor(IFile queryFile) {
		if (queryFile.exists() ) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorDescriptor desc = PlatformUI.getWorkbench().
	        getEditorRegistry().getDefaultEditor(queryFile.getName());
			try {
				page.openEditor(new FileEditorInput(queryFile), desc.getId());
			} catch (PartInitException e) {
				throw new SpagoBIPluginException("Impossibile to Open Query Editor ",e);			}
		}
	}	
	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
