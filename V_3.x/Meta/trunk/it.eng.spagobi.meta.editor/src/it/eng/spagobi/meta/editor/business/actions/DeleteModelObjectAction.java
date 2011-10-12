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
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.table.DeleteBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.ModifyBusinessTableColumnsCommand;
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
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.jface.viewers.IStructuredSelection;

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
