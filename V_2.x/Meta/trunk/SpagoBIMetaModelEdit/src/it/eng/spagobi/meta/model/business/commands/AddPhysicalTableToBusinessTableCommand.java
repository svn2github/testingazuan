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
import it.eng.spagobi.meta.initializer.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author cortella
 *
 */
public class AddPhysicalTableToBusinessTableCommand extends
		AbstractSpagoBIModelCommand {
	
	BusinessColumnSet businessColumnSet;
	BusinessTable businessTable;
	BusinessView addedBusinessView;
	BusinessView businessView;
	BusinessViewInnerJoinRelationshipDescriptor joinRelationshipDescriptor;
	
	public AddPhysicalTableToBusinessTableCommand(EditingDomain domain, CommandParameter parameter) {
		super("Physical Table", "Add Physical Table ", domain, parameter);
	}
	
	public AddPhysicalTableToBusinessTableCommand(EditingDomain domain){
		this(domain, null);
	}
	
	@Override
	public void execute() {
		//do something
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		joinRelationshipDescriptor = (BusinessViewInnerJoinRelationshipDescriptor)parameter.getValue();
		businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		
		//for BusinessTable upgrade to BusinessView
		if (businessColumnSet instanceof BusinessTable){
			businessTable = (BusinessTable)businessColumnSet;
			addedBusinessView = initializer.upgradeBusinessTableToBusinessView(businessTable, joinRelationshipDescriptor);
		}
		//for BusinessView, update the Physical Tables
		else if (businessColumnSet instanceof BusinessView){
			businessView = (BusinessView)businessColumnSet;
			businessView = initializer.addPhysicalTableToBusinessView(businessView, joinRelationshipDescriptor);
		}
		System.err.println("COMMAND [AddPhysicalTableToBusinessTableCommand] SUCCESFULLY EXECUTED");
		
		this.executed = true;
	}
	@Override
	public void undo() {
		//undo the upgrade of a BusinessTable to BusinessView
		if (businessTable != null){
			BusinessModel businessModel = addedBusinessView.getModel();
			//re-add the columns to the business table
			businessTable.getColumns().addAll(addedBusinessView.getColumns());
			businessModel.getTables().remove(addedBusinessView);
			businessModel.getTables().add(businessTable);		
		} 
		//undo the update of the BusinessView
		else if (businessView != null){
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			businessView = initializer.removePhysicalTableToBusinessView(businessView, joinRelationshipDescriptor);
		}
	}
	
	@Override
	public void redo() {
		//redo the upgrade of a BusinessTable to BusinessView
		if (businessTable != null){
			BusinessModel businessModel = businessTable.getModel();
			//add the columns to BusinessView
			addedBusinessView.getColumns().addAll(businessTable.getColumns());
			businessModel.getTables().add(addedBusinessView);
			businessModel.getTables().remove(businessTable);
		} else if (businessView != null){
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			initializer.addPhysicalTableToBusinessView(businessView, joinRelationshipDescriptor);
		}
	}
	@Override
	public Object getImage() {
		return SpagoBIMetalModelEditPlugin.INSTANCE.getImage("full/obj16/PhysicalTable");
	}
}
