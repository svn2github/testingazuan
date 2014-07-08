/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.multi.wizards;

import it.eng.spagobi.commons.utils.SpagoBIMetaConstants;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class NewQueryFileProjectExplorerWizard extends Wizard implements
INewWizard {
	private IFile file;
	private IStructuredSelection selection;
	private IWorkbench workbench;
	private NewQueryFileProjectExplorerWizardPage newFileWizardPage;
	private static Logger logger = LoggerFactory.getLogger(NewQueryFileProjectExplorerWizard.class);
	protected IPath containerFullPath = null;

	public NewQueryFileProjectExplorerWizard(){
		setWindowTitle("Create Query File");
		this.setHelpAvailable(false);	
	}

	@Override
	public void addPages() {
		newFileWizardPage = new NewQueryFileProjectExplorerWizardPage(selection);
		if(containerFullPath != null)newFileWizardPage.setContainerFullPath(containerFullPath);
		addPage(newFileWizardPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		if(selection != null && !selection.toList().isEmpty()){
			Object objSel = selection.toList().get(0);
			if(objSel instanceof IFile){
				logger.debug("set default dataset path");
				IProject project = ((IFile)objSel).getProject();
				IPath pathProj = project.getFullPath();
				containerFullPath = pathProj.append(SpagoBIMetaConstants.FOLDER_DATASET);
			}
		}
	}


	@Override
	public boolean performFinish() {
		file = newFileWizardPage.createNewFile();

		if (file != null){
			EmfXmiSerializer emfXmiSerializer = new EmfXmiSerializer();
			// go on only if you selected a folder
			if(selection != null && !selection.toList().isEmpty()){
				Object objSel = selection.toList().get(0);
				if(objSel instanceof IFile){
					File fileSel = null;		
					fileSel=(File)objSel;
					try{
						// force to avoid failing cause of missing syncronization
						Model root = emfXmiSerializer.deserialize(fileSel.getContents(true));						
						logger.debug("Model root is [{}] ",root );
						BusinessModel businessModel = root.getBusinessModels().get(0);
						logger.debug("link to model "+businessModel.getName());
						logger.debug("file "+file.getName());
						file.setPersistentProperty(SpagoBIMetaConstants.MODEL_NAME, businessModel.getName());
						
						logger.debug("model file is name "+fileSel.getName()+" witrh relative path "+fileSel.getProjectRelativePath().toOSString()+" and full path "+fileSel.getFullPath());
						file.setPersistentProperty(SpagoBIMetaConstants.MODEL_FILE_NAME, fileSel.getName());				
						file.setPersistentProperty(SpagoBIMetaConstants.MODEL_FILE_NAME_FULL_PATH, fileSel.getFullPath().toOSString());				
						file.setPersistentProperty(SpagoBIMetaConstants.MODEL_FILE_NAME_REL_PATH, fileSel.getProjectRelativePath().toOSString());				
						
						logger.debug("Set file metadata with model name "+businessModel.getName()+" and file name "+fileSel.getName());
					}
					catch (Exception e) {
						logger.error("could not link to model metadata", e);		
					}
				}
			}

			try {
				file.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				logger.error("Refresh Local workspace error");
				e.printStackTrace();
			}

			return true;
		}
		else
			return false;
	}

	/**
	 * @return the file
	 */
	public IFile getFile() {
		return file;
	}

	public void setContainerFullPath(IPath containerFullPath) {
		this.containerFullPath = containerFullPath;
	}

}
