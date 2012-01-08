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
