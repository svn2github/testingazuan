package it.eng.spagobi.studio.jasper.editors;

import it.eng.spagobi.studio.core.preferences.PreferenceConstants;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.PlatformUI;

public final class JasperEditor implements IEditorLauncher {

	/**
	 *  Editor for Jasper document: opens ireport editor 
	 */

	public void open(IPath fileToEditIPath) {

		try{
			// Catch the file to call path
			File fileT=fileToEditIPath.toFile();
			String fileToEditPath=fileT.getPath();
			String fileToEditDirectoryPath=fileT.getParent();
			IPath fileToEditDirectoryIPath=new Path(fileToEditDirectoryPath);
			// Launch I report, get from preferences the IReport Path
			IPreferenceStore store = it.eng.spagobi.studio.jasper.Activator.getDefault().getPreferenceStore();

			String iReportPathString = store.getString(PreferenceConstants.IREPORT_EXEC_FILE);

			if(iReportPathString==null || iReportPathString.equalsIgnoreCase("")){
				MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Warning", "You must define IReport path in preferences");
			}
			else{
				// get the directory Path
				Path iReportPath=new Path(iReportPathString);
				File iReportExec=iReportPath.toFile();
				File iReportDirectory=iReportExec.getParentFile();
				String command=iReportPath+" "+fileToEditPath;

				Runtime rt = Runtime.getRuntime();
				Process proc  = rt.exec(command, null, iReportDirectory);		
				int returnValue=proc.waitFor();
				if(returnValue!=0){
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
							"Error", "Generic error after closing iReport: code "+Integer.valueOf(returnValue).toString());			
				}
								
				IFile fileToEditIFile = ResourcesPlugin.getWorkspace().getRoot().getFile(fileToEditIPath);
				IFile fileToEditDirectory = ResourcesPlugin.getWorkspace().getRoot().getFile(fileToEditDirectoryIPath);
				
				boolean isSync=fileToEditIFile.isSynchronized(2);				
				fileToEditIFile.refreshLocal(IResource.DEPTH_INFINITE, null);
				boolean isSync2=fileToEditDirectory.isSynchronized(2);				
				fileToEditDirectory.refreshLocal(IResource.DEPTH_INFINITE, null);

			}
		}
		catch(Exception e)
		{
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Could not start iReport; check you selected the right execution file in preferences");			
		}

	}



}
