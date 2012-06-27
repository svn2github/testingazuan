/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DeleteBusinessTableCommand extends AbstractSpagoBIModelEditCommand {

	BusinessModel model;
	BusinessTable businessTable;
	
	BusinessIdentifier removedIdentifier;
	List<BusinessRelationship> removedRelationships;
	
	private static Logger logger = LoggerFactory.getLogger(DeleteBusinessTableCommand.class);
	
	public DeleteBusinessTableCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.delete.label"
			 , "model.business.commands.edit.table.delete.description"
			 , "model.business.commands.edit.table.delete"
			 , domain, parameter);
	}
	
	public DeleteBusinessTableCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected BusinessTable getBusinessTable() {
		if(businessTable == null) businessTable = (BusinessTable)parameter.getOwner();
		return businessTable;
	}
	
	@Override
	public void execute() {
		model = getBusinessTable().getModel();
		
		removedIdentifier = getBusinessTable().getIdentifier();
		if(removedIdentifier != null) {
			model.getIdentifiers().remove(removedIdentifier);
		}
		
		//remove relationships of this business table
		removedRelationships = getBusinessTable().getRelationships();
		model.getRelationships().removeAll(removedRelationships);
		
		model.getTables().remove(getBusinessTable());
		
		executed = true;
	}
	
	
	@Override
	public void undo() {
		
		
		model.getTables().add( getBusinessTable() );
		model.getRelationships().addAll(removedRelationships);
		if(removedIdentifier != null) {
			model.getIdentifiers().add(removedIdentifier);
		}
	}

	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(model != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(model);
		}
		return affectedObjects;
	}
	
	//-------------------------------------------------------------------------
	//	Inner Classes
	//-------------------------------------------------------------------------
		
	/*
	 * Inner class that implements IModelObjectFilter
	 */
	private class PhysicalColumnFilter implements IModelObjectFilter{

		Collection<PhysicalColumn> columnsTrue;
		public PhysicalColumnFilter(Collection<PhysicalColumn> columnsToMantain){
			columnsTrue = columnsToMantain;
		}
		@Override
		public boolean filter(ModelObject o) {
			if (columnsTrue.contains((PhysicalColumn)o))
				return false;
			else
				return true;
		}		
	}
	//-------------------------------------------------------------------------

}
