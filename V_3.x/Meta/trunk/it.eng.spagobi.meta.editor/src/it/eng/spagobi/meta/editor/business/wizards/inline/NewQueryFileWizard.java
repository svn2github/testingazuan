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
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.Wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class NewQueryFileWizard extends AbstractSpagoBIModelWizard implements INewWizard {

    private IStructuredSelection selection;
    private NewQueryFileWizardPage newFileWizardPage;
    private IWorkbench workbench;
    private IFile file;
    private BusinessModel businessModel;
    
	private static Logger logger = LoggerFactory.getLogger(NewQueryFileWizard.class);


    public NewQueryFileWizard(BusinessModel businessModel, EditingDomain editingDomain,
			AbstractSpagoBIModelCommand command) {
		super(editingDomain, command);
		this.setWindowTitle("Create Query");
		this.setHelpAvailable(false);	
		this.businessModel = businessModel;

    } 

    @Override
    public void addPages() {

        newFileWizardPage = new NewQueryFileWizardPage(selection);
        addPage(newFileWizardPage);
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
        	super.performFinish();
            return true;
        }
        else
            return false;
    }
    
	@Override
	public CommandParameter getCommandInputParameter() {
		String queryFilePath = file.getRawLocation().toOSString();
		logger.debug("Query file path is [{}]",queryFilePath);
		return new CommandParameter(businessModel, null, queryFilePath, new ArrayList<Object>());
	}

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard#isWizardComplete()
	 */
	@Override
	public boolean isWizardComplete() {
		// TODO Auto-generated method stub
		return true;
	}

}
