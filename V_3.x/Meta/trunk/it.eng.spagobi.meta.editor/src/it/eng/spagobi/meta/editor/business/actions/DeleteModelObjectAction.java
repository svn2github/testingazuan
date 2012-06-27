/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.table.DeleteBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.RemoveColumnsFromBusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.view.DeleteBusinessViewCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.DeleteBusinessViewPhysicalTableCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessViewPhysicalTableItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.DeleteAction;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DeleteModelObjectAction extends DeleteAction {
	
	
	public DeleteModelObjectAction(EditingDomain domain, boolean removeAllReferences) {
		super(domain, removeAllReferences);
	}

	public DeleteModelObjectAction(EditingDomain domain) {
	    super(domain);
	}

	public DeleteModelObjectAction(boolean removeAllReferences) {
		super(removeAllReferences);
	}

	public DeleteModelObjectAction() {
		super();
	}

	@Override
	public void run() {
	    domain.getCommandStack().execute(command);
	}
	  
	Command removeCommand;
	
	@Override
	public Command createCommand(Collection<?> selection) {
		
		Command command = null;
		
		List<Command> removeTableCommands = new ArrayList<Command>();
		List<Command> removeColumnCommands = new ArrayList<Command>();
	
		if(selection != null && !selection.isEmpty()) {
			Iterator it = selection.iterator();
			while(it.hasNext()) {
				Object o = it.next();
				if(o instanceof BusinessTable) {
					CommandParameter parameter = new CommandParameter(o);
					removeCommand = new DeleteBusinessTableCommand(domain, parameter);
					removeTableCommands.add( removeCommand );
				} else if(o instanceof BusinessView) {
					CommandParameter parameter = new CommandParameter(o);
					removeCommand = new DeleteBusinessViewCommand(domain, parameter);
					removeTableCommands.add( removeCommand );
				} else if(o instanceof SimpleBusinessColumn) {
					SimpleBusinessColumn businessColumn = (SimpleBusinessColumn)o;
					BusinessColumnSet businessColumnSet = businessColumn.getTable();
					//Collection<PhysicalColumn> physicalColumns = extractPhysicalColumns(businessColumnSet);
					//physicalColumns.remove(businessColumn.getPhysicalColumn());
					Collection<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
					physicalColumns.add(businessColumn.getPhysicalColumn());
					CommandParameter parameter = new CommandParameter(businessColumnSet, null, physicalColumns, null);
					removeCommand = new RemoveColumnsFromBusinessTable(domain, parameter);
					removeColumnCommands.add( removeCommand );
				} else if (o instanceof BusinessViewPhysicalTableItemProvider){
					PhysicalTable physicalTable = ((BusinessViewPhysicalTableItemProvider)o).getPhysicalTable();
					BusinessView businessView = (BusinessView)((BusinessViewPhysicalTableItemProvider)o).getParentObject();
					CommandParameter parameter = new CommandParameter(businessView, null, physicalTable, null);
					removeCommand = new DeleteBusinessViewPhysicalTableCommand(domain, parameter);
					removeTableCommands.add( removeCommand );
				}
			}
			
			Command removeTablesCommand = null;
			if(!removeTableCommands.isEmpty()) {
				removeTablesCommand = new CompoundCommand(removeTableCommands.size()-1, "Delete business classes", "Delete business classes", removeTableCommands);
			}
			
			Command removeColumnsCommand = null;
			if(!removeColumnCommands.isEmpty()) {
				removeColumnsCommand = new CompoundCommand(removeColumnCommands.size()-1, "Delete attributes", "Delete attributes", removeColumnCommands);
			}
			
			removeCommand = null;
			if(removeTablesCommand != null && removeColumnsCommand != null) {
				List<Command> removeCommands = new ArrayList<Command>();
				removeCommands.add(removeColumnsCommand);
				removeCommands.add(removeTablesCommand);
				removeCommand = new CompoundCommand(removeCommands.size()-1, "Delete", "Delete", removeCommands);				
			} else if(removeTablesCommand != null && removeColumnsCommand == null) {
				removeCommand = removeTablesCommand;
			} else if(removeTablesCommand == null && removeColumnsCommand != null) {
				removeCommand = removeColumnsCommand;
			}
		} 
		
		command = removeCommand;
		
		
		
		if(command == null) {
			command = super.createCommand(selection);
		}
		
	    return command;
	}
	
	private Collection<PhysicalColumn> extractPhysicalColumns(BusinessColumnSet businessColumnSet) {
		List<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
		for(SimpleBusinessColumn businessColumn: businessColumnSet.getSimpleBusinessColumns()) {
			physicalColumns.add(businessColumn.getPhysicalColumn());
		}
		return physicalColumns;
	} 
}
