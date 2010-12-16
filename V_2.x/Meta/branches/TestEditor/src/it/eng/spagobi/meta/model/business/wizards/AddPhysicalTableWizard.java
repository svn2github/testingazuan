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

import java.util.ArrayList;

import it.eng.spagobi.meta.initializer.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;

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
	
	/**
	 * @param editingDomain
	 * @param command
	 */
	public AddPhysicalTableWizard(BusinessColumnSet owner, EditingDomain editingDomain,
			AbstractSpagoBIModelCommand command, boolean isBusinessView, String physicalTableName) {
		super(editingDomain, command);
		this.setWindowTitle("Add a Physical Table");
		this.setHelpAvailable(false);	
		this.owner = owner;
		this.isBusinessView = isBusinessView;
		this.selectedPhysicalTableName = physicalTableName; // may be null
	}

	@Override
	public void addPages() {
		pageOne = new AddPhysicalTableSelectionPage("Add Physical Table to Business Table", owner, isBusinessView, selectedPhysicalTableName);
		addPage(pageOne);
		pageTwo = new AddPhysicalTableSourceSelectionPage("Select Source Physical Table to Join",owner);
		addPage(pageTwo);
		pageThree = new AddBusinessViewInnerJoinPage("Select join relationship",owner, selectedPhysicalTableName);
		addPage(pageThree);
		pageOne.setPageThreeRef(pageThree);
		pageTwo.setPageThreeRef(pageThree);
	}	
	
	@Override
	public CommandParameter getCommandInputParameter() {
		//String physicalTableName = pageOne.getTableSelected();
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
