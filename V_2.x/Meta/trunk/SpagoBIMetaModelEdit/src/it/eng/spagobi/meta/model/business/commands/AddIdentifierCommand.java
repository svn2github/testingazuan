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
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddIdentifierCommand extends AbstractSpagoBIModelCommand {

	BusinessIdentifier addedBusinessIdentifier;
	Collection<BusinessColumn> selectedColumns;
	Collection<BusinessColumn> oldColumns = new ArrayList<BusinessColumn>(); //may be empty
	
	public AddIdentifierCommand(EditingDomain domain, CommandParameter parameter) {
		super("Identifier", "Add identifier ", domain, parameter);
	}
	
	public AddIdentifierCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		
		//Checking if a Business Identifier for this Business Table already exists
		BusinessTable businessTable = (BusinessTable)parameter.getOwner();
		BusinessModel businessModel = businessTable.getModel();
		selectedColumns = (Collection)parameter.getValue();
		
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessTable);
		
		if (businessIdentifier != null){
			String identifierName = businessIdentifier.getName();
			oldColumns = businessIdentifier.getColumns();
			//Business Identifier already exists, substitution
			businessModel.getIdentifiers().remove(businessIdentifier);
			addedBusinessIdentifier = initializer.addIdentifier(identifierName, businessTable, selectedColumns);			
		}
		else {
			//Business Identifier doesn't exists, create
			addedBusinessIdentifier = initializer.addIdentifier(businessTable.getName(), businessTable, selectedColumns);
		}
		
		System.err.println("COMMAND [AddIdentifierCommand] SUCCESFULLY EXECUTED: ");
		
		this.executed = true;
	}
	
	
	@Override
	public void undo() {
		BusinessTable businessTable = (BusinessTable)parameter.getOwner();
		BusinessModel businessModel = businessTable.getModel();
		//get existing businessIdentifier
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessTable);
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
		BusinessTable businessTable = (BusinessTable)parameter.getOwner();
		BusinessModel businessModel = businessTable.getModel();
		//check if exists a businessIdentifier
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessTable);
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
	public Object getImage() {
		return SpagoBIMetalModelEditPlugin.INSTANCE.getImage("full/obj16/BusinessIdentifier");
	}

	

}
