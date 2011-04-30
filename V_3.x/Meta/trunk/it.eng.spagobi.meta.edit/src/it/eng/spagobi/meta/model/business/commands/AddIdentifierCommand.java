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
package it.eng.spagobi.meta.model.business.commands;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;

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
public class AddIdentifierCommand extends AbstractSpagoBIModelEditCommand {

	BusinessIdentifier addedBusinessIdentifier;
	Collection<BusinessColumn> selectedColumns;
	Collection<BusinessColumn> oldColumns = new ArrayList<BusinessColumn>(); //may be empty
	
	private static Logger logger = LoggerFactory.getLogger(AddIdentifierCommand.class);
	
	
	public AddIdentifierCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.addidentifier.label"
			 , "model.business.commands.addidentifier.description"
			 , "model.business.commands.addidentifier"
			 , domain, parameter);
	}
	
	public AddIdentifierCommand(EditingDomain domain) {
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
		logger.debug("Command [{}] executed succesfully", AddIdentifierCommand.class.getName());
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
