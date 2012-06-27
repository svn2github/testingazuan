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

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author cortella
 *
 */
public class AddPhysicalTableWizard extends AbstractSpagoBIModelWizard {

	private AddPhysicalTableSelectionPage pageOne;
	private AddPhysicalTableSourceSelectionPage pageTwo;
	private AddBusinessViewInnerJoinPage pageThree;
	private BusinessColumnSet owner;
	private boolean isBusinessView;
	private String selectedPhysicalTableName;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	
	/**
	 * @param editingDomain
	 * @param command
	 */
	public AddPhysicalTableWizard(BusinessColumnSet owner, EditingDomain editingDomain,
			ISpagoBIModelCommand command, boolean isBusinessView, String physicalTableName) {
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.addphysicaltable.title"));
		this.setHelpAvailable(false);	
		this.owner = owner;
		this.isBusinessView = isBusinessView;
		this.selectedPhysicalTableName = physicalTableName; // may be null
	}

	@Override
	public void addPages() {
		if(selectedPhysicalTableName == null){
			pageOne = new AddPhysicalTableSelectionPage("Add Physical Table to Business Class", owner, isBusinessView, selectedPhysicalTableName);
			addPage(pageOne);	
		}
		if (isBusinessView){
			pageTwo = new AddPhysicalTableSourceSelectionPage("Select Source Physical Table to Join",owner);
			addPage(pageTwo);
		}
		pageThree = new AddBusinessViewInnerJoinPage("Select join relationship",owner, selectedPhysicalTableName);
		addPage(pageThree);
		if (pageOne != null){
			pageOne.setPageThreeRef(pageThree);	
		}
		if (pageTwo != null){
			pageTwo.setPageThreeRef(pageThree);	
		}
	}	
	
	@Override
	public CommandParameter getCommandInputParameter() {
		BusinessViewInnerJoinRelationshipDescriptor joinRelationshipDescriptor = pageThree.getRelationshipDescriptor();
		return new CommandParameter(owner, null, joinRelationshipDescriptor, new ArrayList<Object>());
	}

	@Override
	public boolean isWizardComplete() {
		if (pageThree.isPageComplete()){
			return true;			
		}
		return false;	
	}

}
