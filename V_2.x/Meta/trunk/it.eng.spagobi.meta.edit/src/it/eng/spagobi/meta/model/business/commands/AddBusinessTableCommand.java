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
package it.eng.spagobi.meta.model.business.commands;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.initializer.descriptor.BusinessTableDescriptor;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddBusinessTableCommand extends AbstractSpagoBIModelCommand {

	private BusinessTable addedBusinessTable;
	
	private static Logger logger = LoggerFactory.getLogger(AddBusinessTableCommand.class);
	
	
	public AddBusinessTableCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.addbtable.label"
			 , "model.business.commands.addbtable.description"
			 , "model.business.commands.addbtable"
			 , domain, parameter);
	}
	
	public AddBusinessTableCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessTableDescriptor){
			//parameter is a collection of selected column list
			
			//Create Business Table from a Physical Table with column filter
			BusinessModel businessModel = (BusinessModel)parameter.getOwner();
			BusinessTableDescriptor businessTableDescriptor = (BusinessTableDescriptor)parameter.getValue();
			Collection<PhysicalColumn> selectedColumns = businessTableDescriptor.getPhysicalColumns();
			//getting PhysicalTable reference
			PhysicalColumn physicalColum =((PhysicalColumn)(selectedColumns.toArray()[0]));
			PhysicalTable physicalTable = physicalColum.getTable();
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			//getting Properties (Name,Description)
			String businessTableName = businessTableDescriptor.getBusinessTableName();
			String businessTableDescription = businessTableDescriptor.getBusinessTableDescription();
			addedBusinessTable = initializer.addTable(physicalTable, new PhysicalColumnFilter(selectedColumns), businessTableName, businessTableDescription, businessModel, true);	
						
			this.executed = true;			
			logger.debug("Command [{}] executed succesfully", AddBusinessTableCommand.class.getName());
			
		} else if (parameter.getValue() instanceof String){
			//parameter is a String with Physical Table name to import as Business Table
			
			String tableName = (String)parameter.getValue();
			BusinessModel businessModel = (BusinessModel)parameter.getOwner();
			PhysicalModel physicalModel = businessModel.getPhysicalModel();
			PhysicalTable physicalTable = physicalModel.getTable(tableName);
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			addedBusinessTable = initializer.addTable(physicalTable, businessModel, true);

			this.executed = true;
			logger.debug("Command [{}] executed succesfully", AddBusinessTableCommand.class.getName());	
		}

	}
	
	
	@Override
	public void undo() {
		BusinessModel businessModel = (BusinessModel)parameter.getOwner();
		businessModel.getTables().remove(addedBusinessTable);
	}

	@Override
	public void redo() {
		BusinessModel businessModel = (BusinessModel)parameter.getOwner();
		businessModel.getTables().add(addedBusinessTable);
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
