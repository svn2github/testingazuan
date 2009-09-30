package it.eng.spagobi.studio.chart.wizards;

import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.chart.Activator;
import it.eng.spagobi.studio.chart.editors.ChartEditorUtils;
import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel;
import it.eng.spagobi.studio.chart.utils.GeneralUtils;
import it.eng.spagobi.studio.chart.wizards.pages.NewChartWizardPage;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.Bundle;

public class SpagoBINewChartWizard extends Wizard implements INewWizard {

	// dashboard creation page
	private NewChartWizardPage newChartWizardPage;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	// the workbench instance
	protected IWorkbench workbench;



	public boolean performFinish() {
		// get the name of the dashboard from the form
		SpagoBILogger.infoLog("Starting chart wizard");
		String chartFileName = newChartWizardPage.getChartNameText().getText();
		if (chartFileName == null || chartFileName.trim().equals("")) {
			SpagoBILogger.errorLog("ChartNameEmpty", null);
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "Chart name empty");
			return false;
		}
		// Get the selected Type
		String typeSelected=newChartWizardPage.getSelectedType();		

		// get the folder selected:  
		Object objSel = selection.toList().get(0);
		Folder folderSel = null;		
		try{
			// FolderSel is the folder in wich to insert the new template
			folderSel=(Folder)objSel;
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("no selected folder", e);			
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "You must select a folder in wich to insert the chart");		
		}
		// get the project
		String projectName = folderSel.getProject().getName();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// get the folder where to insert the chart document
		IProject project = root.getProject(projectName);

		// generate the byte array input stream used to fill the file
		ByteArrayInputStream bais = null;
		Bundle b = Platform.getBundle(Activator.PLUGIN_ID);
		
//		String chartTemplatePath = null;
//		try {
//			chartTemplatePath = ChartEditorUtils.getChartTemplatePath(typeSelected,null);
//		} catch (Exception e) {
//			SpagoBILogger.errorLog("Error", e);			
//			MessageDialog.openInformation(getShell(), "Error", e.getMessage());
//			return true;
//		}
//		if (chartTemplatePath == null || chartTemplatePath.trim().equals("")) {
//			SpagoBILogger.errorLog("Missing template path for dashboard " + typeSelected, null);
//			MessageDialog.openInformation(getShell(), 
//					"Error", "Missing template path for dashboard " + typeSelected);
//			return true;
//		}
//		URL res = b.getResource(chartTemplatePath);;
//		InputStream is = null;
//		try {
//			// The new file is initialised 
//			is = res.openStream();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			GeneralUtils.flushFromInputStreamToOutputStream(is, baos, true);
//			byte[] resbytes = baos.toByteArray();
//			bais = new ByteArrayInputStream(resbytes);
//		} catch (Exception e) {
//			SpagoBILogger.errorLog("Error while creating file", e);
//			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
//					"Error", "Error while creating file");
//		} finally {
//			try {
//				if(is!=null) is.close();
//			} catch (Exception e) {
//				SpagoBILogger.errorLog("Error while closing stream", e);
//				SpagoBILogger.errorLog("Error while creating file", e);
//			}
//		}


		// generate the file	       
		IPath pathFolder = folderSel.getProjectRelativePath();
		IPath pathNewFile = pathFolder.append(chartFileName + ".sbichart");
		IFile newFile = project.getFile(pathNewFile);
		/*try {
			newFile.create(bais, true, null);
		} catch (CoreException e) {
			SpagoBILogger.errorLog("Error while creating file", e);
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "Error while creating file; name alreay present");
		}*/

		try {
		String toWrite="<"+typeSelected.toUpperCase()+" name='"+chartFileName+"' />";
		byte[] bytes=toWrite.getBytes();
		InputStream inputStream=new ByteArrayInputStream(bytes);
			newFile.create(inputStream, true, null);
		} catch (CoreException e1) {
			SpagoBILogger.errorLog("Error while creating file", e1);
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "Error while creating file; name alreay present");
			e1.printStackTrace();
		}

		     
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorRegistry er = wb.getEditorRegistry();
		IEditorDescriptor editordesc =  er.getDefaultEditor(newFile.getName());

		try {
			page.openEditor(new FileEditorInput(newFile), editordesc.getId());
		} catch (PartInitException e) {
			SpagoBILogger.errorLog("Error while opening editor", e);
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"Error", "Error while opening editor");
		}
		SpagoBILogger.infoLog("Open the chart wizard");
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New Chart template creation");
		this.workbench = workbench;
		this.selection = selection;
	}

	public void addPages() {
		super.addPages();
		newChartWizardPage = new NewChartWizardPage("New Dashboard");
		addPage(newChartWizardPage);
	}

}
