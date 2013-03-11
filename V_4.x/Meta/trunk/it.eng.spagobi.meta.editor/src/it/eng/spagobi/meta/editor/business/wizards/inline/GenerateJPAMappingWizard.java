/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author cortella
 *
 */
public class GenerateJPAMappingWizard extends AbstractSpagoBIModelWizard {
	
	BusinessModel businessModel;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	/**
	 * @param editingDomain
	 * @param command
	 */
	public GenerateJPAMappingWizard(BusinessModel businessModel, EditingDomain editingDomain,
			ISpagoBIModelCommand command) {
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.generatemapping.title"));
		this.setHelpAvailable(false);	
		this.businessModel = businessModel;
	}

	@Override
	public void addPages() {
		IWizardPage pageOne = new GenerateJPAMappingWizardDirectorySelectionPage("Directory Selection");
		addPage( pageOne );
	}
	
	@Override
	public CommandParameter getCommandInputParameter() {
		GenerateJPAMappingWizardDirectorySelectionPage wizardPage = (GenerateJPAMappingWizardDirectorySelectionPage)this.getStartingPage();
		String directory = wizardPage.getSelectedDirectory();
		
		return new CommandParameter(businessModel, null, directory, new ArrayList<Object>());
	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
