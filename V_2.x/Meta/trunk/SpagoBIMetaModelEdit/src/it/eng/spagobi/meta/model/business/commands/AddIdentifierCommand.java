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
import java.util.Collection;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddIdentifierCommand extends AbstractSpagoBIModelCommand {

	
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
		Collection<BusinessColumn> selectedColumns = (Collection)parameter.getValue();
		
		//BusinessTable bizTable = businessModel.getTable(tableName);
		BusinessIdentifier businessIdentifier = businessModel.getIdentifier(businessTable);
		String identifierName = businessIdentifier.getName();
		if (businessIdentifier != null){
			//Business Identifier already exists, substitution
			businessModel.getIdentifiers().remove(businessIdentifier);
			initializer.addIdentifier(identifierName, businessTable, selectedColumns);			
		}
		else {
			//Business Identifier doesn't exists, create
			initializer.addIdentifier(identifierName, businessTable, selectedColumns);
			//initializer.addIdentifier(tableName, businessTable, selectedColumns);
		}
		
		System.err.println("COMMAND [AddIdentifierCommand] SUCCESFULLY EXECUTED: ");
		
		this.executed = true;
	}
	
	
	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
			
	}
	
	@Override
	public Object getImage() {
		return SpagoBIMetalModelEditPlugin.INSTANCE.getImage("full/obj16/BusinessIdentifier");
	}

	

}
