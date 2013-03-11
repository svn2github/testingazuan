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
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class EditBusinessViewInnerJoinWizard extends AbstractSpagoBIModelWizard {

	/**
	 * @param editingDomain
	 * @param command
	 */
	BusinessColumnSet businessColumnSet;
	EditBusinessViewInnerJoinPage editJoinPathPage;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	
	public EditBusinessViewInnerJoinWizard(BusinessColumnSet businessColumnSet, EditingDomain editingDomain,
			ISpagoBIModelCommand command) {
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.editjoinpaths.title"));
		this.setHelpAvailable(false);	
		this.businessColumnSet = businessColumnSet;
	}
	
	@Override
	public void addPages() {
		editJoinPathPage = new EditBusinessViewInnerJoinPage("EditJoinPathPage",businessColumnSet);
		addPage(editJoinPathPage);
	}

	@Override
	public CommandParameter getCommandInputParameter() {
		List<BusinessViewInnerJoinRelationshipDescriptor> innerJoinsDescriptors = editJoinPathPage.getJoinRelationshipsDescriptors();
		return new CommandParameter(businessColumnSet, null, innerJoinsDescriptors, new ArrayList<Object>());

	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
