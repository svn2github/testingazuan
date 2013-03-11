/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.identifier;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddColumnToIdentifierCommand extends AbstractSpagoBIModelEditCommand {

	BusinessColumn businessColumn;
	
	private static Logger logger = LoggerFactory.getLogger(AddColumnToIdentifierCommand.class);
	
	
	public AddColumnToIdentifierCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.edit.identifier.addcolumn.label"
			 , "model.business.commands.edit.identifier.addcolumn.description"
			 , "model.business.commands.edit.identifier.addcolumn"
			 , domain, parameter);
	}
	
	public AddColumnToIdentifierCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		businessColumn = (BusinessColumn)parameter.getOwner();
		BusinessColumnSet businessColumnSet = businessColumn.getTable();
		BusinessIdentifier businessIdentifier = businessColumnSet.getIdentifier();
		if (businessIdentifier != null){
			//update existing Business Identifier
			businessIdentifier.getColumns().add(businessColumn);
		} else {
			//create a new Business Identifier
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			Collection<BusinessColumn> businessColumns = new ArrayList<BusinessColumn>();
			businessColumns.add(businessColumn);
			initializer.addIdentifier(businessColumnSet.getName(), businessColumnSet, businessColumns);
		}
		
		this.executed = true;
		logger.debug("Command [{}] executed succesfully", AddColumnToIdentifierCommand.class.getName());
	}
	
	
	@Override
	public void undo() {
		BusinessColumnSet businessColumnSet = businessColumn.getTable();
		BusinessIdentifier businessIdentifier = businessColumnSet.getIdentifier();
		businessIdentifier.getColumns().remove(businessColumn);
		//check if the Business Identifier is empty after the remove
		if(businessIdentifier.getColumns().isEmpty()){
			BusinessModel businessModel = businessColumnSet.getModel();
			businessModel.getIdentifiers().remove(businessIdentifier);
		}
	}

	@Override
	public void redo() {
		BusinessColumnSet businessColumnSet = businessColumn.getTable();
		BusinessIdentifier businessIdentifier = businessColumnSet.getIdentifier();
		if (businessIdentifier != null){
			//update existing Business Identifier
			businessIdentifier.getColumns().add(businessColumn);
		} else {
			//create a new Business Identifier
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			Collection<BusinessColumn> businessColumns = new ArrayList<BusinessColumn>();
			businessColumns.add(businessColumn);
			initializer.addIdentifier(businessColumnSet.getName(), businessColumnSet, businessColumns);
		}			
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessColumn != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessColumn.getTable());
		}
		return affectedObjects;
	}
	
}
