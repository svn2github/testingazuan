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
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;

public class AddBusinessRelationshipWizard extends AbstractSpagoBIModelWizard {

	BusinessModel model;
	BusinessColumnSet sourceTable;
	BusinessColumnSet destinationTable;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	
	public AddBusinessRelationshipWizard(BusinessModel model, BusinessColumnSet sourceTable, BusinessColumnSet destinationTable, EditingDomain editingDomain, ISpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.addbusinessrelationship.title"));
		this.setHelpAvailable(false);	
		this.model = model;
		this.sourceTable = sourceTable;
		this.destinationTable = destinationTable;
		
	}
	
	@Override
	public void addPages() {
		IWizardPage pageOne = new AddBusinessRelationshipWizardPage("Add business relationship", model, sourceTable, destinationTable);
		addPage( pageOne );
	}
	
	public CommandParameter getCommandInputParameter(){
		AddBusinessRelationshipWizardPage wizardPage = (AddBusinessRelationshipWizardPage)this.getStartingPage();

		return new CommandParameter(sourceTable, null, wizardPage.getRelationshipDescriptor(), new ArrayList<Object>());

	}
	
	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
