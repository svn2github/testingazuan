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
package it.eng.spagobi.meta.model.business.wizards;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author cortella
 *
 */
public class CreateQueryWizard extends AbstractSpagoBIModelWizard {
	
	BusinessModel businessModel;
	
	/**
	 * @param editingDomain
	 * @param command
	 */
	public CreateQueryWizard(BusinessModel businessModel, EditingDomain editingDomain,
			AbstractSpagoBIModelCommand command) {
		super(editingDomain, command);
		this.setWindowTitle("Create Query");
		this.setHelpAvailable(false);	
		this.businessModel = businessModel;
	}

	@Override
	public void addPages() {
		IWizardPage pageOne = new CreateQueryWizardDirectorySelectionPage("Directory Selection");
		addPage( pageOne );
	}
	
	@Override
	public CommandParameter getCommandInputParameter() {
		CreateQueryWizardDirectorySelectionPage wizardPage = (CreateQueryWizardDirectorySelectionPage)this.getStartingPage();
		String directory = wizardPage.getSelectedDirectory();
		
		return new CommandParameter(businessModel, null, directory, new ArrayList<Object>());
	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
