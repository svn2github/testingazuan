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

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.initializer.descriptor.BusinessTableDescriptor;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author cortella
 *
 */
public class AddBusinessTableWizard extends AbstractSpagoBIModelWizard {

	private AddBusinessTableWizardPagePhysicalTableSelection pageOne;
	private AddBusinessTableWizardPageColumnSelection pageTwo;
	private AddBusinessTableWizardPropertiesPage pageThree;
	private TableItem[] columnsToImport;
	private PhysicalTable physicalTable;
	private BusinessModel owner;
	private String businessTableName,businessTableDescription;
	private BusinessTableDescriptor businessTableDescriptor;
	
	public AddBusinessTableWizard(BusinessModel owner, PhysicalTable physicalTable, EditingDomain editingDomain, AbstractSpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle("Create a new Business Table");
		this.setHelpAvailable(false);
		this.physicalTable = physicalTable;
		this.owner = owner;
		businessTableDescriptor = new BusinessTableDescriptor();
	}
	
	@Override
	public void addPages() {
		if (physicalTable == null){
			pageOne = new AddBusinessTableWizardPagePhysicalTableSelection("Create Business Table step one", owner, physicalTable);
			addPage(pageOne);
		}
		pageTwo = new AddBusinessTableWizardPageColumnSelection("Create Business Table step two", owner, pageOne, physicalTable);
		addPage(pageTwo);
		if (pageOne != null){
			pageOne.setColumnSelectionPage(pageTwo);
		}
		pageThree = new AddBusinessTableWizardPropertiesPage("Create Business Table step three", owner, pageOne, physicalTable);
		addPage(pageThree);
		if (pageOne != null){
			pageOne.setPropertiesPage(pageThree);
		}
	}

	@Override
	public CommandParameter getCommandInputParameter() {
		if (pageThree.isPageComplete()){
			if (pageTwo.isPageComplete() && pageTwo.isColumnSelected()){
				//columns to import
				columnsToImport = pageTwo.getColumnsToImport();
				int numCol = columnsToImport.length;
				List<PhysicalColumn> colList = new ArrayList<PhysicalColumn>();
				for (int i=0; i<numCol; i++){
					PhysicalColumn pc = ((PhysicalColumn)columnsToImport[i].getData());
					colList.add(pc);
				}
				
				//properties to import if specified
				businessTableName = pageThree.getName();
				businessTableDescription = pageThree.getDescription();
				if ((businessTableName != null) && (businessTableName.length() > 0)){
					businessTableDescriptor.setBusinessTableName(businessTableName);
				}
				if ((businessTableDescription != null) && (businessTableDescription.length() > 0)){
					businessTableDescriptor.setBusinessTableDescription(businessTableDescription);
				}
				businessTableDescriptor.setPhysicalColumns(colList);
				
				return new CommandParameter(owner, null, businessTableDescriptor, new ArrayList<Object>());
			}
		}
		if (pageOne != null){
			if (pageOne.isPageComplete()){
				String tableName = pageOne.getTableSelected();
				//Create Business Table from a Physical Table
				return new CommandParameter(owner, null, tableName, new ArrayList<Object>());
			}
		}
		return null;
	}


	@Override
	public boolean isWizardComplete() {
		if (pageThree.isPageComplete()){
			if (pageTwo.isPageComplete() && pageTwo.isColumnSelected()){
				return true;
			}
		}
		if (pageOne != null){
			if (pageOne.isPageComplete()){
				return true;
			}
		}
		return false;
	}

}
