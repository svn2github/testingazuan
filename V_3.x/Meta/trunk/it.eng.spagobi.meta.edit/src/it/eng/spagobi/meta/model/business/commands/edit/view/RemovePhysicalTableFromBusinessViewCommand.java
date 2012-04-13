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
package it.eng.spagobi.meta.model.business.commands.edit.view;


import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author cortella
 *
 */
public class RemovePhysicalTableFromBusinessViewCommand extends AbstractSpagoBIModelEditCommand {

	BusinessModelInitializer initializer;
	
	BusinessView businessView;
	PhysicalTable physicalTable;
	BusinessViewInnerJoinRelationship innerJoinRelationship;
	BusinessModel businessModel;
	BusinessTable businessTable;
	List<BusinessColumn> removedBusinessColumns;
	
	private static Logger logger = LoggerFactory.getLogger(RemovePhysicalTableFromBusinessViewCommand.class);
	
	
	public RemovePhysicalTableFromBusinessViewCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.view.removetable.label"
			 , "model.business.commands.edit.view.removetable.description"
			 , "model.business.commands.edit.view.removetable"
			 , domain, parameter);
		initializer = new BusinessModelInitializer();
	}

	public RemovePhysicalTableFromBusinessViewCommand(EditingDomain domain){
		this(domain, null);
	}
	
	@Override
	public void execute() {
		
		businessView = (BusinessView)parameter.getOwner();
		//physicalTable = (PhysicalTable)parameter.getValue();
		String tableName = (String)parameter.getValue();
		int occurrenceNumber = 0;
		if (tableName.contains("#")){
			//special case: duplicate physical table
			occurrenceNumber = Integer.parseInt(tableName.substring(tableName.indexOf("#")+1));
			tableName = tableName.substring(0, tableName.indexOf("#"));
		} else {
			//normal case
			List<PhysicalTable> physicalTables = businessView.getPhysicalTablesOccurrences();
			for (PhysicalTable table:physicalTables){
				if (table.getName().equals(tableName))
					physicalTable = table;
				break;
			}
		}
			
		businessModel = businessView.getModel();
		removedBusinessColumns = new ArrayList<BusinessColumn>();
		if (physicalTable != null){
			//normal case
			innerJoinRelationship = removePhysicalTableToBusinessView(businessView, physicalTable);
		}
		else {
			//special case
			List<PhysicalTable> physicalTables = businessView.getPhysicalTablesOccurrences();
			for (PhysicalTable table:physicalTables){
				if (table.getName().equals(tableName))
					physicalTable = table;
				break;
			}
			innerJoinRelationship = removePhysicalTableToBusinessView(businessView, physicalTable, occurrenceNumber);
		}
		
		if (innerJoinRelationship != null){
			this.executed = true;
			logger.debug("Command [{}] executed succesfully", RemovePhysicalTableFromBusinessViewCommand.class.getName());
		} else {
			this.executed = false;
			logger.debug("Command [{}] not executed succesfully", RemovePhysicalTableFromBusinessViewCommand.class.getName());
			showInformation("Warning","Cannot delete this physical table because it is used in a join relationship as a source table. Remove first the other tables.");
		}

	}
	
	public BusinessViewInnerJoinRelationship removePhysicalTableToBusinessView(BusinessView businessView, PhysicalTable physicalTable){
		BusinessViewInnerJoinRelationship innerJoinRelationship = null;

		try {
			if (isSourceTable(businessView,physicalTable)){
				//if the physicalTable to remove is used as a SourceTable in a joinRelationship
				//then DO NOT remove the physical table	
				return null;
			}
			else {
				//check if the removed physicalTable was in join with another PhysicalTable in this BusinessView
				EList<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
				for(BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
					if (joinRelationship.getDestinationTable() == physicalTable){
						innerJoinRelationship = joinRelationship;
						if (businessView.getJoinRelationships().size() == 1){
							//downgrade to BusinessTable
							
							for(SimpleBusinessColumn bc: businessView.getSimpleBusinessColumns()) {
								if(bc.getPhysicalColumn().getTable().equals(physicalTable)) {
									removedBusinessColumns.add( bc );
								}				
							}
							initializer.downgradeBusinessViewToBusinessTable(businessView);
						}
						else {
							//remove the inner join relationship between two physical table
							businessView.getJoinRelationships().remove(joinRelationship);
							//remove he inner join relationship from BusinessModel
							businessModel.getJoinRelationships().remove(joinRelationship);
							
							//remove physical table's columns
							List<SimpleBusinessColumn> businessColumns = businessView.getSimpleBusinessColumns();
							for (SimpleBusinessColumn businessColumn : businessColumns){
								if (businessColumn.getPhysicalColumn().getTable() == physicalTable){
									businessView.getColumns().remove(businessColumn);
								}
							}
						}
						break;
					}
				}
			}

			
		} 
		catch(Throwable t) {
			throw new RuntimeException("Impossible to remove physical table to business view", t);
		}
		return innerJoinRelationship;	
	}
	
	public BusinessViewInnerJoinRelationship removePhysicalTableToBusinessView(BusinessView businessView, PhysicalTable physicalTable, int occurenceNumber){
		BusinessViewInnerJoinRelationship innerJoinRelationship = null;

		try {
			EList<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
			innerJoinRelationship = businessView.getBusinessViewInnerJoinRelationshipAtOccurrenceNumber(physicalTable, occurenceNumber);
				//check if the removed physicalTable was in join with another PhysicalTable in this BusinessView
						
						
						if (businessView.getJoinRelationships().size() == 1){
							//downgrade to BusinessTable
							
//							for(BusinessColumn bc: businessView.getColumns()) {
//								if(bc.getPhysicalColumn().getTable().equals(physicalTable)) {
//									removedBusinessColumns.add( bc );
//								}				
//							}
							initializer.downgradeBusinessViewToBusinessTable(businessView);
						}
						else {
							//remove the inner join relationship between two physical table
							businessView.getJoinRelationships().remove(innerJoinRelationship);
							//remove he inner join relationship from BusinessModel
							businessModel.getJoinRelationships().remove(innerJoinRelationship);
							
							//remove physical table's columns
//							EList<BusinessColumn> businessColumns = businessView.getColumns();
//							for (BusinessColumn businessColumn : businessColumns){
//								if (businessColumn.getPhysicalColumn().getTable() == physicalTable){
//									businessView.getColumns().remove(businessColumn);
//								}
//							}
						}

		} 
		catch(Throwable t) {
			throw new RuntimeException("Impossible to remove physical table to business view", t);
		}
		return innerJoinRelationship;	
	}
	
	//check if the PhysicalTable is used as a SourceTable in the inner join relationship for BusinessView
	public boolean isSourceTable(BusinessView businessView, PhysicalTable physicalTable){
		EList<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
		for(BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
			if (joinRelationship.getSourceTable() == physicalTable){
				return true;
			} 
		}
		return false;
	}
	
	@Override
	public void undo() {
		//check if businessView still exists
		if (businessModel.getTables().contains(businessView)){
			businessView.getJoinRelationships().add(innerJoinRelationship);
		} else {
		//undo the downgrade
			businessTable = (BusinessTable)businessModel.getTableByUniqueName(businessView.getUniqueName());
			BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(
					innerJoinRelationship.getSourceTable(),
					innerJoinRelationship.getDestinationTable(),
					innerJoinRelationship.getSourceColumns(),
					innerJoinRelationship.getDestinationColumns(),
					1,
					innerJoinRelationship.getName());
			businessView = initializer.upgradeBusinessTableToBusinessView(businessTable, innerJoinRelationshipDescriptor);
			for(BusinessColumn bc : removedBusinessColumns) {
				businessView.getColumns().add(bc);
			}
			
			
		
		}
	}
	
	@Override
	public void redo() {
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		
		//check if redo the downgrade
		if (businessTable != null ){
			initializer.downgradeBusinessViewToBusinessTable(businessView);
		} else {
			removePhysicalTableToBusinessView(businessView, physicalTable);
		}
	}
	
	/**
	 * Show an information dialog box.
	 */
	public void showInformation(final String title, final String message) {
	  Display.getDefault().asyncExec(new Runnable() {
	    @Override
	    public void run() {
	      MessageDialog.openInformation(null, title, message);
	    }
	  });
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessModel != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessModel);
		}
		return affectedObjects;
	}

}
