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
import it.eng.spagobi.meta.model.business.commands.edit.table.DeleteBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.ModifyBusinessTableColumnsCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
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
	public Command createCommand(Collection<?> selection) {
		Command command = null;
		if(selection != null && !selection.isEmpty()) {
			Object o = selection.iterator().next();
			if(o instanceof BusinessTable) {
				CommandParameter parameter = new CommandParameter(o);
				command = new DeleteBusinessTableCommand(domain, parameter);
			} else if(o instanceof BusinessColumn) {
				BusinessColumn businessColumn = (BusinessColumn)o;
				BusinessColumnSet businessColumnSet = businessColumn.getTable();
				Collection<PhysicalColumn> physicalColumns = extractPhysicalColumns(businessColumnSet);
				physicalColumns.remove(businessColumn.getPhysicalColumn());
				CommandParameter parameter = new CommandParameter(businessColumnSet, null, physicalColumns, null);
				command = new ModifyBusinessTableColumnsCommand(domain, parameter);
			}
		}
		
		
		if(command == null) {
			command = super.createCommand(selection);
		}
	    return command;
	}
	
	private Collection<PhysicalColumn> extractPhysicalColumns(BusinessColumnSet businessColumnSet) {
		List<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
		for(BusinessColumn businessColumn: businessColumnSet.getColumns()) {
			physicalColumns.add(businessColumn.getPhysicalColumn());
		}
		return physicalColumns;
	} 
}
