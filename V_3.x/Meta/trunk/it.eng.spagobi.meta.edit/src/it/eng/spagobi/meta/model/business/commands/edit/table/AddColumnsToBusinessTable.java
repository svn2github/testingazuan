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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddColumnsToBusinessTable extends AbstractSpagoBIModelEditCommand {

	BusinessModelInitializer initializer;
	
	// input values
	BusinessColumnSet businessTable;
	Collection<PhysicalColumn> columnsToAdd;
	
	// cache edited columns (added and removed) for undo e redo
	List<BusinessColumn> addedColumns;
	BusinessIdentifier addedIdentifier;
	
		
	private static Logger logger = LoggerFactory.getLogger(ModifyBusinessTableColumnsCommand.class);
	
	public AddColumnsToBusinessTable(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.addcolumns.label"
			 , "model.business.commands.edit.table.addcolumns.description"
			 , "model.business.commands.edit.table.addcolumns"
			 , domain, parameter);
		initializer = new BusinessModelInitializer();	
	}
	
	public AddColumnsToBusinessTable(EditingDomain domain) {
		this(domain, null);
	}
	
	protected void clearCachedObjects() {
		addedColumns = new ArrayList<BusinessColumn>();
		addedIdentifier = null;
	}
	
	@Override
	protected boolean prepare() {
		businessTable = (BusinessColumnSet)parameter.getOwner();
		columnsToAdd = (Collection)parameter.getValue();
		return (businessTable != null && columnsToAdd != null);
	}
	@Override
	public void execute() {
		
		clearCachedObjects();
		
		for(PhysicalColumn column: columnsToAdd) {
			initializer.addColumn(column, businessTable);
			addedColumns.add( businessTable.getColumn(column) );
		}
		
		addIdentifier();
		
		executed = true;
	}
	
	@Override
	public void undo() {
		undoAddIdentifier();

		for(BusinessColumn column: addedColumns) {
			businessTable.getColumns().remove(column);
		}	
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	private void addIdentifier() {
		if(businessTable instanceof BusinessTable) {
			BusinessTable table = (BusinessTable)businessTable;
			BusinessIdentifier identifier = table.getIdentifier();
			if(identifier == null) {
				addedIdentifier = initializer.addIdentifier(table, table.getModel());
			}
		}
	}
	
	private void undoAddIdentifier() {
		if(addedIdentifier != null) {
			businessTable.getModel().getIdentifiers().remove(addedIdentifier);
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
	
	@Override
	public Collection<?> getResult() {
		// TODO the result here should change with operation type (execute = columns; undo = table)
		return getAffectedObjects();
	}

}
