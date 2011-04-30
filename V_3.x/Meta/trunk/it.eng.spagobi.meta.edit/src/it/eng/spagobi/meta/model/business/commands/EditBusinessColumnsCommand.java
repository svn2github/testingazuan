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
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

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
public class EditBusinessColumnsCommand extends AbstractSpagoBIModelEditCommand {

	// cache edited columns (added and removed) for undo e redo
	List<BusinessColumn> removedColumns;
	List<BusinessColumn> addedColumns;

	private static Logger logger = LoggerFactory.getLogger(EditBusinessColumnsCommand.class);
	
	
	public EditBusinessColumnsCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.editbcolumn.label"
			 , "model.business.commands.editbcolumn.description"
			 , "model.business.commands.editbcolumn"
			 , domain, parameter);
	}
	
	public EditBusinessColumnsCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		BusinessModelInitializer initializer;
		BusinessColumnSet businessColumnSet;
		businessColumnSet = (BusinessColumnSet)parameter.getOwner();

		Collection<PhysicalColumn> newSelectionColumns = (Collection)parameter.getValue();
		Collection<PhysicalColumn> oldSelectionColumns = extractPhysicalColumns(businessColumnSet);
		
		List<PhysicalColumn> columns;
		
		removedColumns = new ArrayList<BusinessColumn>();
		addedColumns = new ArrayList<BusinessColumn>();
		
		initializer = new BusinessModelInitializer();	
		
		columns = getColumnsToRemove(oldSelectionColumns, newSelectionColumns);
		for(PhysicalColumn column: columns) {
			BusinessColumn c = businessColumnSet.getColumn(column);
			businessColumnSet.getColumns().remove(c);
			removedColumns.add(c);
		}
				
		columns = getColumnsToAdd(oldSelectionColumns, newSelectionColumns);
		for(PhysicalColumn column: columns) {
			initializer.addColumn(column, businessColumnSet);
			addedColumns.add( businessColumnSet.getColumn(column) );
		}
		
		this.executed = true;
		logger.debug("Command [{}] executed succesfully", EditBusinessColumnsCommand.class.getName());
	}
	
	private Collection<PhysicalColumn> extractPhysicalColumns(BusinessColumnSet businessColumnSet) {
		List<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
		for(BusinessColumn businessColumn: businessColumnSet.getColumns()) {
			physicalColumns.add(businessColumn.getPhysicalColumn());
		}
		return physicalColumns;
	} 

	private List<PhysicalColumn> getColumnsToRemove(Collection<PhysicalColumn> oldSelectionColumns, Collection<PhysicalColumn> newSelectionColumns) {
		
		List<PhysicalColumn> columnsToRemove = new ArrayList<PhysicalColumn>();
		
		for(PhysicalColumn oldSelectionColumn : oldSelectionColumns) {
			if(isColumnPartOfTheNewSelection(oldSelectionColumn, newSelectionColumns) == false) {
				columnsToRemove.add(oldSelectionColumn);
			}
		}
		
		return columnsToRemove;
	}
	
	public boolean isColumnPartOfTheNewSelection(PhysicalColumn oldSelectionColumn, Collection<PhysicalColumn> newSelectionColumns) {
		boolean isPart = false;
		for(PhysicalColumn column : newSelectionColumns) {
			if(oldSelectionColumn.equals(column)) {
				isPart = true;
			}
		}
		return isPart;
	}
	
	private List<PhysicalColumn> getColumnsToAdd(Collection<PhysicalColumn> oldSelectionColumns, Collection<PhysicalColumn> newSelectionColumns) {
		
		List<PhysicalColumn> columnsToAdd;
		
		columnsToAdd = new ArrayList<PhysicalColumn>();
		
		for(PhysicalColumn newSelectionColumn : newSelectionColumns) {
			if(isColumnPartOfTheOldSelection(newSelectionColumn, oldSelectionColumns) == false) {
				columnsToAdd.add(newSelectionColumn);
			}
		}
		
		return columnsToAdd;
	}

	public boolean isColumnPartOfTheOldSelection(PhysicalColumn newSelectionColumn, Collection<PhysicalColumn> oldSelectionColumns) {
		boolean isPart = false;
		
		for(PhysicalColumn oldSelectionColumn : oldSelectionColumns) {
			if(oldSelectionColumn.equals(newSelectionColumn)) {
				isPart = true;
			}
		}
		
		return isPart;
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
	public Collection<?> getAffectedObjects() {
		BusinessColumnSet businessTable = (BusinessColumnSet)parameter.getOwner();
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessTable != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessTable);
		}
		return affectedObjects;
	}
	

}
