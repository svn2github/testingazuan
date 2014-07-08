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
import it.eng.spagobi.meta.initializer.descriptor.BusinessTableDescriptor;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
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
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	public AddBusinessTableWizard(BusinessModel owner, PhysicalTable physicalTable, EditingDomain editingDomain, ISpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.addbusinessclass.title"));
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
