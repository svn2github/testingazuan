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

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author cortella
 *
 */
public class SortBusinessTableColumnsCommand extends AbstractSpagoBIModelEditCommand {
	
	BusinessColumnSet businessColumnSet;
	BusinessColumn businessColumn;
	int oldIndex;
	int newIndex;
	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public SortBusinessTableColumnsCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.edit.table.sortcolumns.label", "model.business.commands.edit.table.sortcolumns.description", "model.business.commands.edit.table.sortcolumns", domain, parameter);
	}
	
	public SortBusinessTableColumnsCommand(EditingDomain domain){
		this(domain,null);
	}
	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessColumn){
			businessColumnSet = (BusinessColumnSet)parameter.getOwner();
			newIndex = (Integer)parameter.getFeature();
			businessColumn= (BusinessColumn)parameter.getValue();
			oldIndex = businessColumnSet.getColumns().indexOf(businessColumn);
			businessColumnSet.getColumns().move(newIndex, businessColumn);

			this.executed = true;
		}
			
	}
	
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessColumnSet != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessColumnSet);
		}
		return affectedObjects;
	}
	
	@Override
	public void undo() {
		businessColumnSet.getColumns().move(oldIndex, businessColumn);
	}
	
	@Override
	public void redo() {
		businessColumnSet.getColumns().move(newIndex, businessColumn);
	}

}
