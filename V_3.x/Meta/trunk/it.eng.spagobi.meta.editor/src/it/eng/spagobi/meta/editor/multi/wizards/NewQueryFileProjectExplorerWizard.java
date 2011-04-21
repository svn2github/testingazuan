/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.editor.multi.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
   
    
	public NewQueryFileProjectExplorerWizard(){
		setWindowTitle("Create Query File");
		this.setHelpAvailable(false);	
	}

    @Override
    public void addPages() {
        newFileWizardPage = new NewQueryFileProjectExplorerWizardPage(selection);
        addPage(newFileWizardPage);
    }

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
	}


	@Override
	public boolean performFinish() {
        file = newFileWizardPage.createNewFile();
        try {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (CoreException e) {
			logger.error("Refresh Local workspace error");
			e.printStackTrace();
		}
        if (file != null){
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

}
