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
import it.eng.spagobi.meta.initializer.descriptor.CalculatedFieldDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class AddCalculatedFieldWizard extends AbstractSpagoBIModelWizard {
	BusinessModel model;
	BusinessColumnSet sourceTable;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	CalculatedBusinessColumn existingCalculatedField;

	/**
	 * @param editingDomain
	 * @param command
	 */
	public AddCalculatedFieldWizard(BusinessModel model, BusinessColumnSet sourceTable, EditingDomain editingDomain, ISpagoBIModelCommand command, CalculatedBusinessColumn existingCalculatedField) {
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.addcalculatedfield.title"));
		this.setHelpAvailable(false);	
		this.model = model;
		this.sourceTable = sourceTable;
		this.existingCalculatedField = existingCalculatedField;

	}
	
	@Override
	public void addPages() {
		IWizardPage pageOne = new AddCalculatedFieldWizardPage("Edit calculated field",sourceTable, existingCalculatedField);
		addPage( pageOne );
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard#getCommandInputParameter()
	 */
	@Override
	public CommandParameter getCommandInputParameter() {
		AddCalculatedFieldWizardPage wizardPage = (AddCalculatedFieldWizardPage)this.getStartingPage();
		CalculatedFieldDescriptor calculatedFieldDescriptor = new CalculatedFieldDescriptor
															(wizardPage.getTxtName(),wizardPage.getTextCalculatedField(), wizardPage.getDataType(), sourceTable );
		//Existing Calculated Field to modify
		if (existingCalculatedField != null){
			return new CommandParameter(sourceTable, existingCalculatedField, calculatedFieldDescriptor, new ArrayList<Object>());
		}
		
		return new CommandParameter(sourceTable, null, calculatedFieldDescriptor, new ArrayList<Object>());
	}


	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
