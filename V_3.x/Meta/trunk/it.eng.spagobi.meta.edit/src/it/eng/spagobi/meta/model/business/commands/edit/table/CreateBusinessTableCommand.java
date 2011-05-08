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
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessTableDescriptor;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CreateBusinessTableCommand extends AbstractSpagoBIModelEditCommand {

	BusinessModelInitializer initializer;
	
	// input objects
	BusinessModel businessModel;
	Object tableToAdd;
	
	// cached objects
	BusinessTable addedBusinessTable;
	BusinessIdentifier addedBusinessIdentifier;
	List<BusinessRelationship> addedBusinessRelationships;
	
	private static Logger logger = LoggerFactory.getLogger(CreateBusinessTableCommand.class);
	
	
	public CreateBusinessTableCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.create.label"
			 , "model.business.commands.edit.table.create.description"
			 , "model.business.commands.edit.table.create"
			 , domain, parameter);
		
		initializer = new BusinessModelInitializer();
	}
	
	public CreateBusinessTableCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected void clearCachedObjects() {
		addedBusinessTable = null;
		addedBusinessIdentifier = null;
		addedBusinessRelationships = new ArrayList<BusinessRelationship>() ;
	}
	
	@Override
	protected boolean prepare() {
		return true;
	}
	
	@Override
	public void execute() {
		
		clearCachedObjects();
		
		// read input
		businessModel = (BusinessModel)parameter.getOwner();
		tableToAdd = parameter.getValue();
		
		// add table
		if (tableToAdd instanceof BusinessTableDescriptor){
			executeCreateTable( (BusinessTableDescriptor)tableToAdd );
		} else if (tableToAdd instanceof String){
			executeCreateTable( (String)tableToAdd );
		}
		
		// added identifier
		addedBusinessIdentifier = addedBusinessTable.getIdentifier();
		
		// add outcome relationship
		List <PhysicalForeignKey> physicalForeignKeys = addedBusinessTable.getPhysicalTable().getForeignKeys();
		for(PhysicalForeignKey foreignKey : physicalForeignKeys) {
			for(BusinessTable businessTable: businessModel.getBusinessTables()) {
				BusinessRelationship relationship = initializer.addRelationship(addedBusinessTable , businessTable, foreignKey);
				if(relationship != null) {
					addedBusinessRelationships.add(relationship);
				}
			}
		}
		
		
//		List <PhysicalForeignKey> physicalForeignKeys = addedBusinessTable.getPhysicalTable().getForeignKeys();
//		if(!physicalForeignKeys.isEmpty()) {
//			for(PhysicalForeignKey physicalForeignKey : physicalForeignKeys) {
//				BusinessRelationship relationship = initializer.addRelationship(physicalForeignKey, businessModel);
//				if(relationship != null) {
//					addedBusinessRelationships.add(relationship);
//				}
//			}
//		}
		
		// add income relationship
		for(BusinessTable businessTable: businessModel.getBusinessTables()) {
			if(businessTable == addedBusinessTable) continue;
			List<PhysicalForeignKey> foreignKeys = businessTable.getPhysicalTable().getForeignKeys();
			for(PhysicalForeignKey foreignKey: foreignKeys) {
				BusinessRelationship relationship = initializer.addRelationship(businessTable, addedBusinessTable, foreignKey);
				if(relationship != null) {
					addedBusinessRelationships.add(relationship);
				}
			}
		}
	}
	

	
	@Override
	public void undo() {
		// undo add outcome relationship
		if(!addedBusinessRelationships.isEmpty()) {
			businessModel.getRelationships().removeAll(addedBusinessRelationships);
		}
		
		// undo add identifier
		if(addedBusinessIdentifier != null) {
			businessModel.getIdentifiers().remove(addedBusinessIdentifier);
		}
		
		// undo add table		
		businessModel.getTables().remove(addedBusinessTable);
		
		
	}
	
	public void executeCreateTable(BusinessTableDescriptor tableDescriptor) {
		Collection<PhysicalColumn> selectedColumns = tableDescriptor.getPhysicalColumns();
		
		//getting PhysicalTable reference
		PhysicalColumn physicalColum =((PhysicalColumn)(selectedColumns.toArray()[0]));
		PhysicalTable physicalTable = physicalColum.getTable();
		
		//getting Properties (Name,Description)
		String businessTableName = tableDescriptor.getBusinessTableName();
		String businessTableDescription = tableDescriptor.getBusinessTableDescription();
		addedBusinessTable = initializer.addTable(physicalTable, new PhysicalColumnFilter(selectedColumns), businessTableName, businessTableDescription, businessModel, true);	
					
		this.executed = true;			
		logger.debug("Command [{}] executed succesfully", CreateBusinessTableCommand.class.getName());
	}
	
	public void executeCreateTable(String tableName) {
		PhysicalModel physicalModel = businessModel.getPhysicalModel();
		PhysicalTable physicalTable = physicalModel.getTable(tableName);
		
		addedBusinessTable = initializer.addTable(physicalTable, businessModel, true);

		this.executed = true;
		logger.debug("Command [{}] executed succesfully", CreateBusinessTableCommand.class.getName());	
	}
	
	
	

	@Override
	public void redo() {
		execute();
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
	
	//-------------------------------------------------------------------------
	//	Inner Classes
	//-------------------------------------------------------------------------
		
	/*
	 * Inner class that implements IModelObjectFilter
	 */
	private class PhysicalColumnFilter implements IModelObjectFilter{

		Collection<PhysicalColumn> columnsTrue;
		public PhysicalColumnFilter(Collection<PhysicalColumn> columnsToMantain){
			columnsTrue = columnsToMantain;
		}
		@Override
		public boolean filter(ModelObject o) {
			if (columnsTrue.contains((PhysicalColumn)o))
				return false;
			else
				return true;
		}		
	}
	//-------------------------------------------------------------------------

}
