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
package it.eng.spagobi.meta.model.business.commands.edit.view;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author cortella
 *
 */
public class RemovePhysicalTableFromBusinessViewCommand extends AbstractSpagoBIModelEditCommand {

	BusinessView businessView;
	PhysicalTable physicalTable;
	BusinessViewInnerJoinRelationship innerJoinRelationship;
	BusinessModel businessModel;
	BusinessTable businessTable ;
	
	private static Logger logger = LoggerFactory.getLogger(RemovePhysicalTableFromBusinessViewCommand.class);
	
	
	public RemovePhysicalTableFromBusinessViewCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.view.removetable.label"
			 , "model.business.commands.edit.view.removetable.description"
			 , "model.business.commands.edit.view.removetable"
			 , domain, parameter);
	}

	public RemovePhysicalTableFromBusinessViewCommand(EditingDomain domain){
		this(domain, null);
	}
	
	@Override
	public void execute() {
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		businessView = (BusinessView)parameter.getOwner();
		physicalTable = (PhysicalTable)parameter.getValue();
		businessModel = businessView.getModel();
		innerJoinRelationship = initializer.removePhysicalTableToBusinessView(businessView, physicalTable);
		
		if (innerJoinRelationship != null){
			this.executed = true;
			logger.debug("Command [{}] executed succesfully", RemovePhysicalTableFromBusinessViewCommand.class.getName());
		} else {
			this.executed = false;
			logger.debug("Command [{}] not executed succesfully", RemovePhysicalTableFromBusinessViewCommand.class.getName());
			showInformation("Warning","Cannot delete this physical table because is used in a join relationship as a source table.\nPlease remove first the other tables.");
		}

	}
	
	@Override
	public void undo() {
		//check if businessView still exists
		if (businessModel.getTables().contains(businessView)){
			businessView.getJoinRelationships().add(innerJoinRelationship);
		} else {
		//undo the downgrade
			businessTable = (BusinessTable)businessModel.getTable(businessView.getName());
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(
					innerJoinRelationship.getSourceTable(),
					innerJoinRelationship.getDestinationTable(),
					innerJoinRelationship.getSourceColumns(),
					innerJoinRelationship.getDestinationColumns(),
					1,
					innerJoinRelationship.getName());
			businessView = initializer.upgradeBusinessTableToBusinessView(businessTable, innerJoinRelationshipDescriptor);
		}
	}
	
	@Override
	public void redo() {
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		
		//check if redo the downgrade
		if (businessTable != null ){
			initializer.downgradeBusinessViewToBusinessTable(businessView);
		} else {
			initializer.removePhysicalTableToBusinessView(businessView, physicalTable);
		}
	}
	
	/**
	 * Show an information dialog box.
	 */
	public void showInformation(final String title, final String message) {
	  Display.getDefault().asyncExec(new Runnable() {
	    @Override
	    public void run() {
	      MessageDialog.openInformation(null, title, message);
	    }
	  });
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessView != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessView);
		}
		return affectedObjects;
	}

}
