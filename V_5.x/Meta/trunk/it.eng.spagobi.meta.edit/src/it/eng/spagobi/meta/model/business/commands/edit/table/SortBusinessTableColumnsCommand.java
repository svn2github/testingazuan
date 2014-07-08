/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
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
