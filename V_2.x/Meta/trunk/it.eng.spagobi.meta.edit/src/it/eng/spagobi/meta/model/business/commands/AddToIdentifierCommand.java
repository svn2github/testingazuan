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
import it.eng.spagobi.meta.initializer.descriptor.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddToIdentifierCommand extends AbstractSpagoBIModelCommand {

	BusinessColumn businessColumn;
	public AddToIdentifierCommand(EditingDomain domain, CommandParameter parameter) {
		super("Identifier", "Add identifier ", domain, parameter);
	}
	
	public AddToIdentifierCommand(EditingDomain domain) {
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
		
		System.err.println("COMMAND [AddToIdentifierCommand] SUCCESFULLY EXECUTED: ");
		
		this.executed = true;
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
	public Object getImage() {
		return SpagoBIMetaModelEditPlugin.INSTANCE.getImage("full/obj16/BusinessIdentifier");
	}

	

}
