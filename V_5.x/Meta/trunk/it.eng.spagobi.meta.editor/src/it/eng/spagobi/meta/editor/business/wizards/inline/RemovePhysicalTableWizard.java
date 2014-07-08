/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author cortella
 *
 */
public class RemovePhysicalTableWizard extends AbstractSpagoBIModelWizard {

	private RemovePhysicalTableWizardSelectionPage pageOne;
	private BusinessColumnSet owner;
	/**
	 * @param editingDomain
	 * @param command
	 */
	public RemovePhysicalTableWizard(BusinessColumnSet businessColumnSet, EditingDomain editingDomain,
			ISpagoBIModelCommand command) {
		super(editingDomain, command);
		this.owner = businessColumnSet;
	}
	
	public void addPages() {
		pageOne = new RemovePhysicalTableWizardSelectionPage("Remove Physical Table", owner);
		addPage(pageOne);
	}

	@Override
	public CommandParameter getCommandInputParameter() {
		String tableName = pageOne.getTableSelected();
		if (owner instanceof BusinessView){
		/*
			java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
			for (PhysicalTable physicalTable:physicalTables ){
				if (physicalTable.getName().equals(tableName))
				{
					return new CommandParameter(owner, null, physicalTable, new ArrayList<Object>());
				}
			}
		*/	
			return new CommandParameter(owner, null, tableName, new ArrayList<Object>());
		}
		return null;	
	}


	@Override
	public boolean isWizardComplete() {
		if (pageOne.isPageComplete()){
			return true;
		}
		return false;
	}

}
