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
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class EditBusinessColumnsCommand extends AbstractSpagoBIModelCommand {

	// cache edited columns (added and removed) for undo e redo
	List<BusinessColumn> removedColumns;
	List<BusinessColumn> addedColumns;

	public EditBusinessColumnsCommand(EditingDomain domain, CommandParameter parameter) {
		super("Business Columns", "Add/Remove business columns ", domain, parameter);
	}
	
	public EditBusinessColumnsCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		BusinessModelInitializer initializer;
		BusinessColumnSet businessColumnSet;
		BusinessView businessView;
		businessColumnSet = (BusinessColumnSet)parameter.getOwner();

		Collection<PhysicalColumn> selectedColumns = (Collection)parameter.getValue();
		List<PhysicalColumn> columns;
		
		removedColumns = new ArrayList();
		addedColumns = new ArrayList();
		
		initializer = new BusinessModelInitializer();	
		
		columns = getColumnsToRemove(businessColumnSet, selectedColumns);
		for(PhysicalColumn column: columns) {
			BusinessColumn c = businessColumnSet.getColumn(column);
			businessColumnSet.getColumns().remove(c);
			removedColumns.add(c);
			//System.err.println("Removed column [" + column.getName() + "]");
		}
				
		columns = getColumnsToAdd(businessColumnSet, selectedColumns);
		for(PhysicalColumn column: columns) {
			initializer.addColumn(column, businessColumnSet);
			addedColumns.add( businessColumnSet.getColumn(column) );
			//System.err.println("Added column [" + businessTable.getColumn(column).getName() + "]");
		}
		
		
		System.err.println("COMMAND [AddBusinessRelationshipCommand] SUCCESFULLY EXECUTED");
		
		this.executed = true;
	}
	
	private List<PhysicalColumn> getColumnsToRemove(BusinessColumnSet businessColumnSet, Collection<PhysicalColumn> newColumnSet) {
		
		List columnsToRemove;
		
		columnsToRemove = new ArrayList();
		
		for(BusinessColumn businessColumn : businessColumnSet.getColumns()) {
			boolean deleteColumn = true;
			for(PhysicalColumn column : newColumnSet) {
				if(businessColumn.equals(column)) {
					deleteColumn = false;
				}
			}
			if(deleteColumn) {
				columnsToRemove.add(businessColumn.getPhysicalColumn());
			}
		}
		
		return columnsToRemove;
	}
	
	private List<PhysicalColumn> getColumnsToAdd(BusinessColumnSet businessColumnSet, Collection<PhysicalColumn> newColumnSet) {
		
		List<PhysicalColumn> columnsToAdd;
		
		columnsToAdd = new ArrayList();
		
		for(PhysicalColumn column : newColumnSet) {
			if(businessColumnSet.getColumn(column) == null) {
				columnsToAdd.add(column);
			}
		}
		
		return columnsToAdd;
	}

	
	@Override
	public void undo() {
		BusinessColumnSet businessTable = (BusinessColumnSet)parameter.getOwner();
		for(BusinessColumn column: removedColumns) {
			businessTable.getColumns().add(column);
		}	
		
		for(BusinessColumn column: addedColumns) {
			businessTable.getColumns().remove(column);
		}	
	}

	@Override
	public void redo() {
		BusinessColumnSet businessTable = (BusinessColumnSet)parameter.getOwner();
		for(BusinessColumn column: removedColumns) {
			businessTable.getColumns().remove(column);
		}	
		
		for(BusinessColumn column: addedColumns) {
			businessTable.getColumns().add(column);
		}			
	}
	
	@Override
	public Object getImage() {
		return SpagoBIMetalModelEditPlugin.INSTANCE.getImage("full/obj16/BusinessColumn");
	}

	

}
