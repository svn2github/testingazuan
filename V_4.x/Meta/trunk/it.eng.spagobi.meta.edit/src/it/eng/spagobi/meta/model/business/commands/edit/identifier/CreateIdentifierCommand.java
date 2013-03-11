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
public class CreateIdentifierCommand extends AbstractSpagoBIModelEditCommand {

	BusinessIdentifier addedBusinessIdentifier;
	Collection<BusinessColumn> selectedColumns;
	Collection<BusinessColumn> oldColumns = new ArrayList<BusinessColumn>(); //may be empty
	
	private static Logger logger = LoggerFactory.getLogger(CreateIdentifierCommand.class);
	
	
	public CreateIdentifierCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.identifier.create.label"
			 , "model.business.commands.edit.identifier.create.description"
			 , "model.business.commands.edit.identifier.create"
			 , domain, parameter);
	}
	
	public CreateIdentifierCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		
		//Checking if a Business Identifier for this Business Table/View already exists
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		BusinessModel businessModel = businessColumnSet.getModel();
		selectedColumns = (Collection)parameter.getValue();
		
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessColumnSet);
		
		if (businessIdentifier != null){
			String identifierName = businessIdentifier.getName();
			oldColumns = businessIdentifier.getColumns();
			//Business Identifier already exists, substitution
			businessModel.getIdentifiers().remove(businessIdentifier);
			addedBusinessIdentifier = initializer.addIdentifier(identifierName, businessColumnSet, selectedColumns);			
		}
		else {
			//Business Identifier doesn't exists, create
			addedBusinessIdentifier = initializer.addIdentifier(businessColumnSet.getName(), businessColumnSet, selectedColumns);
		}
		
		this.executed = true;
		logger.debug("Command [{}] executed succesfully", CreateIdentifierCommand.class.getName());
	}
	
	
	@Override
	public void undo() {
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		BusinessModel businessModel = businessColumnSet.getModel();
		//get existing businessIdentifier
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessColumnSet);
		//reset the previous BusinessIdentifier's columns
		businessIdentifier.getColumns().clear();
		businessIdentifier.getColumns().addAll(oldColumns);
		//remove the BusinessIdentifier if is empty
		if (businessIdentifier.getColumns().isEmpty()){
			businessModel.getIdentifiers().remove(addedBusinessIdentifier);			
		}
	}

	@Override
	public void redo() {
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		BusinessModel businessModel = businessColumnSet.getModel();
		//check if exists a businessIdentifier
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessColumnSet);
		if (businessIdentifier != null){
			//add only columns to existing BusinessIdentifier
			businessIdentifier.getColumns().clear();
			businessIdentifier.getColumns().addAll(selectedColumns);
		}
		else {
			//add the complete BusinessIdentifier
			businessModel.getIdentifiers().add(addedBusinessIdentifier);	
		}			
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(addedBusinessIdentifier != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(addedBusinessIdentifier.getTable());
		}
		return affectedObjects;
	}
	
}
