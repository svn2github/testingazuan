/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.model;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
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
public class SortBusinessModelTablesCommand extends AbstractSpagoBIModelEditCommand {
	
	BusinessColumnSet businessColumnSet;
	BusinessModel businessModel;
	int oldIndex;
	int newIndex;
	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public SortBusinessModelTablesCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.edit.table.sorttables.label", "model.business.commands.edit.table.sorttables.description", "model.business.commands.edit.table.sort", domain, parameter);
	}
	
	public SortBusinessModelTablesCommand(EditingDomain domain){
		this(domain,null);
	}
	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessColumnSet){
			businessModel = (BusinessModel)parameter.getOwner();
			newIndex = (Integer)parameter.getFeature();
			businessColumnSet= (BusinessColumnSet)parameter.getValue();
			oldIndex = businessModel.getTables().indexOf(businessColumnSet);
			businessModel.getTables().move(newIndex, businessColumnSet);

			this.executed = true;
		}
			
	}
	
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessModel != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessModel);
		}
		return affectedObjects;
	}
	
	@Override
	public void undo() {
		businessModel.getTables().move(oldIndex, businessColumnSet);
	}
	
	@Override
	public void redo() {
		businessModel.getTables().move(newIndex, businessColumnSet);
	}

}
