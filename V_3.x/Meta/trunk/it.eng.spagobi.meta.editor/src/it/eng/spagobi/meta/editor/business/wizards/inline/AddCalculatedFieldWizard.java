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
