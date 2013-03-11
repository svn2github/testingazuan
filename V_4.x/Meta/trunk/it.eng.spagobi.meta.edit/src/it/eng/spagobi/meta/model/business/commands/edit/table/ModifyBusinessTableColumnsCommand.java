/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCompoundCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModifyBusinessTableColumnsCommand extends AbstractSpagoBIModelEditCompoundCommand {

	BusinessModelInitializer initializer;
	
	// input values
	BusinessColumnSet businessTable;
	Collection<PhysicalColumn> newColumnSet;
	Collection<PhysicalColumn> oldColumnSet;
	
	
	private static Logger logger = LoggerFactory.getLogger(ModifyBusinessTableColumnsCommand.class);
	
	
	public ModifyBusinessTableColumnsCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.modifycolumns.label"
			 , "model.business.commands.edit.table.modifycolumns.description"
			 , "model.business.commands.edit.table.modifycolumns"
			 , domain, parameter);
		initializer = new BusinessModelInitializer();	
	}
	
	public ModifyBusinessTableColumnsCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected BusinessColumnSet getBusinessTable() {
		if(businessTable == null) businessTable = (BusinessColumnSet)parameter.getOwner();
		return businessTable;
	}
	
	protected Collection<PhysicalColumn> getNewColumnSet() {
		if(newColumnSet == null) newColumnSet = (Collection)parameter.getValue();
		return newColumnSet;
	}
	
	protected Collection<PhysicalColumn> getOldColumnSet() {
		if(oldColumnSet == null) oldColumnSet = extractPhysicalColumns( getBusinessTable() );
		return oldColumnSet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.command.CompoundCommand#prepare()
	 * 
	 * TODO to make command executable. Otherwise it will be not executable because the commandList
	 * of compundCommand is empty at this stage (commands are pushed on the stack by the execute method).
	 */
	@Override
	protected boolean prepare() {
		return true;
	}
	
	@Override
	public void execute() {
		
		appendAndExecute(new RemoveColumnsFromBusinessTable(domain, new CommandParameter(getBusinessTable(), null, getColumnsToRemove(), null)));
		appendAndExecute(new AddColumnsToBusinessTable(domain, new CommandParameter(getBusinessTable(), null,  getColumnsToAdd(), null)));
				
		executed = true;		
		logger.debug("Command [{}] executed succesfully", ModifyBusinessTableColumnsCommand.class.getName());
	}
	
	private Collection<PhysicalColumn> extractPhysicalColumns(BusinessColumnSet businessColumnSet) {
		List<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
		for(SimpleBusinessColumn businessColumn: businessColumnSet.getSimpleBusinessColumns()) {
			physicalColumns.add(businessColumn.getPhysicalColumn());
		}
		return physicalColumns;
	} 

	private List<PhysicalColumn> getColumnsToRemove() {
		
		List<PhysicalColumn> columnsToRemove = new ArrayList<PhysicalColumn>();
		
		for(PhysicalColumn oldSelectionColumn : getOldColumnSet()) {
			if(isColumnPartOfTheNewSelection(oldSelectionColumn, getNewColumnSet()) == false) {
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
	
	private List<PhysicalColumn> getColumnsToAdd() {
		
		List<PhysicalColumn> columnsToAdd;
		
		columnsToAdd = new ArrayList<PhysicalColumn>();
		
		for(PhysicalColumn newSelectionColumn : getNewColumnSet()) {
			if(isColumnPartOfTheOldSelection(newSelectionColumn, getOldColumnSet()) == false) {
				columnsToAdd.add(newSelectionColumn);
			}
		}
		
		return columnsToAdd;
	}

	private boolean isColumnPartOfTheOldSelection(PhysicalColumn newSelectionColumn, Collection<PhysicalColumn> oldSelectionColumns) {
		boolean isPart = false;
		
		for(PhysicalColumn oldSelectionColumn : oldSelectionColumns) {
			if(oldSelectionColumn.equals(newSelectionColumn)) {
				isPart = true;
			}
		}
		
		return isPart;
	}
}
