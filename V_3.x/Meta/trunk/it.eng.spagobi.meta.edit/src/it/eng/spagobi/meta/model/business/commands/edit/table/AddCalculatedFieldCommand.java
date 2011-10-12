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
import it.eng.spagobi.meta.initializer.descriptor.CalculatedFieldDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCompoundCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class AddCalculatedFieldCommand extends
		AbstractSpagoBIModelEditCompoundCommand {

	BusinessModelInitializer initializer;

	// input objects
	BusinessColumnSet businessColumnSet;
	CalculatedFieldDescriptor calculatedColumnDesc;
	String calculatedColumnName;
	
	// cache edited columns (added and removed) for undo e redo
	List<CalculatedBusinessColumn> addedColumns  = new ArrayList<CalculatedBusinessColumn>();
	
	//check if Business Table has empty Physical Table reference
	boolean isEmptyBusinessTable = false;
	
	private static Logger logger = LoggerFactory.getLogger(AddCalculatedFieldCommand.class);
	
	public AddCalculatedFieldCommand(EditingDomain domain, CommandParameter parameter)  {
		super( "model.business.commands.edit.table.addcalculatedfield.label"
				 , "model.business.commands.edit.table.addcalculatedfield.description"
				 , "model.business.commands.edit.table.addcalculatedfield"
				 , domain, parameter);
		initializer = new BusinessModelInitializer();
	}
	
	public AddCalculatedFieldCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected void clearCachedObjects() {
		addedColumns = new ArrayList<CalculatedBusinessColumn>();
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		// read input
		businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		calculatedColumnDesc = (CalculatedFieldDescriptor)parameter.getValue();
		calculatedColumnName = calculatedColumnDesc.getName();
		
		//check if BusinessTable has null Physical Table reference
		if (businessColumnSet instanceof BusinessTable){
			BusinessTable bTable = (BusinessTable)businessColumnSet;
			PhysicalTable physicalTable = bTable.getPhysicalTable();
			if (physicalTable == null){
				isEmptyBusinessTable = true;
			}
			else{
				isEmptyBusinessTable = false;
			}				
		}
		
		if (businessColumnSet.getCalculatedBusinessColumn(calculatedColumnName) == null){// avoid columns duplication
			if (!isEmptyBusinessTable){ //no calculated column for empty business tables
				initializer.addCalculatedColumn(calculatedColumnDesc);
				addedColumns.add( businessColumnSet.getCalculatedBusinessColumn(calculatedColumnName) );
				
				//Print for test, TO REMOVE
				CalculatedBusinessColumn calculatedColumn= businessColumnSet.getCalculatedBusinessColumn(calculatedColumnName);
				List<SimpleBusinessColumn> referencedColumns = calculatedColumn.getReferencedColumns();
				if ( referencedColumns != null ){
					for (SimpleBusinessColumn referencedColumn: referencedColumns){
						System.out.println("Referenced column: "+referencedColumn.getName() + "\n");
					}
				}
			}
		}

		
		
		
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessColumnSet != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessColumnSet);
		}
		return affectedObjects;
	}
	
	@Override
	public void undo() {		
		for(BusinessColumn column: addedColumns) {
			businessColumnSet.getColumns().remove(column);
		}	
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public Collection<?> getResult() {
		// TODO the result here should change with operation type (execute = columns; undo = table)
		return getAffectedObjects();
	}

}
