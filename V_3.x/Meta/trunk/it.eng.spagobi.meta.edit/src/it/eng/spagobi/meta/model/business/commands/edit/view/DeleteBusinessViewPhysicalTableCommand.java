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

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

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
public class DeleteBusinessViewPhysicalTableCommand extends AbstractSpagoBIModelEditCommand {

	BusinessModel model;
	BusinessView businessView;
	PhysicalTable physicalTable;
	BusinessTable businessTable;
	
	BusinessIdentifier removedIdentifier;
	List<BusinessRelationship> removedRelationships;
	List<BusinessColumn> removedBusinessColumns;
	List<BusinessColumn> removedBusinessColumnsIdentifier;
	List<BusinessViewInnerJoinRelationship> removedJoinRelationships;
	PhysicalTable removedPhysicalTable;
	boolean downgraded = false;
	
	private static Logger logger = LoggerFactory.getLogger(DeleteBusinessViewPhysicalTableCommand.class);
	
	public DeleteBusinessViewPhysicalTableCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.view.delete.label"
			 , "model.business.commands.edit.view.delete.description"
			 , "model.business.commands.edit.view.delete"
			 , domain, parameter);
	}
	
	public DeleteBusinessViewPhysicalTableCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected BusinessView getBusinessView() {
		if(businessView == null) businessView = (BusinessView)parameter.getOwner();
		return businessView;
	}
	
	protected PhysicalTable getPhysicalTable(){
		if(physicalTable == null) physicalTable = (PhysicalTable)parameter.getValue();
		return physicalTable;
	}
	
	@Override
	public void execute() {
		try {
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			model = getBusinessView().getModel();
			removedBusinessColumns = new ArrayList<BusinessColumn>();
			removedPhysicalTable = getPhysicalTable();
			removedBusinessColumnsIdentifier = new ArrayList<BusinessColumn>();
			removedJoinRelationships = new ArrayList<BusinessViewInnerJoinRelationship>();
			removedRelationships = new ArrayList<BusinessRelationship>();
			
			List<SimpleBusinessColumn> businessColumns = getBusinessView().getSimpleBusinessColumns();
			//search columns to remove
			for (SimpleBusinessColumn businessColumn : businessColumns){
				if (businessColumn.getPhysicalColumn().getTable().equals(removedPhysicalTable)){
					removedBusinessColumns.add(businessColumn);
					if ( (businessColumn.isIdentifier()) || (businessColumn.isPartOfCompositeIdentifier()) ){
						removedBusinessColumnsIdentifier.add(businessColumn);
					}
				}						
			}
			//remove columns from business view
			getBusinessView().getColumns().removeAll(removedBusinessColumns);
			
			//remove columns from identifier
			if (getBusinessView().getIdentifier() != null) {
				getBusinessView().getIdentifier().getColumns().removeAll(removedBusinessColumnsIdentifier);
				//remove identifier if empty
				if (getBusinessView().getIdentifier().getColumns().isEmpty()){
					removedIdentifier = getBusinessView().getIdentifier();
					model.getIdentifiers().remove(removedIdentifier);
				}
			}
			
			
			List<BusinessViewInnerJoinRelationship> joinRelationships = getBusinessView().getJoinRelationships();
			//search Inner Join Relationships to remove
			for (BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
				if(  (joinRelationship.getSourceTable()== removedPhysicalTable) || 
					 (joinRelationship.getDestinationTable()== removedPhysicalTable) ){
					removedJoinRelationships.add(joinRelationship);
				}
			}
			//remove Inner Join Relationships from view
			getBusinessView().getJoinRelationships().removeAll(removedJoinRelationships);
			
			
			List<BusinessRelationship> businessRelationships = getBusinessView().getRelationships();
			//search Business Relationships to remove
			for (BusinessRelationship businessRelationship : businessRelationships){
				List<BusinessColumn> sourceColumns = businessRelationship.getSourceColumns();
				List<BusinessColumn> destinationColumns = businessRelationship.getDestinationColumns();
				//search removed columns if used in business relationships
				for (BusinessColumn removedColumn : removedBusinessColumns){
					if ((sourceColumns.contains(removedColumn)) || (destinationColumns.contains(removedColumn)) ){
						removedRelationships.add(businessRelationship);
						break;
					}
				}
			}
			//remove Business Relationships from view and model
			getBusinessView().getRelationships().removeAll(removedRelationships);
			model.getRelationships().removeAll(removedRelationships);
			
			
			//Downgrade the BusinessView to Business Table
			if (getBusinessView().getPhysicalTables().size() == 1){
				businessTable = initializer.downgradeBusinessViewToBusinessTable(businessView);
				downgraded = true;
			}
			executed = true;
		} catch(Throwable t) {
			t.printStackTrace();
			if(t instanceof RuntimeException) throw (RuntimeException)t;
			
		}
	}
	
	
	@Override
	public void undo() {		
		if (downgraded){
			//upgrade to BusinessView
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			businessView = initializer.upgradeBusinessTableToBusinessView(businessTable);	
			downgraded = false;
		} 
		//re-add columns
		businessView.getColumns().addAll(removedBusinessColumns);
		//re-add identifier
		if(removedIdentifier != null) {
			model.getIdentifiers().add(removedIdentifier);
		}
		getBusinessView().getIdentifier().getColumns().addAll(removedBusinessColumnsIdentifier);
		//re-add join relationships
		getBusinessView().getJoinRelationships().addAll(removedJoinRelationships);
		//re-add relationships
		model.getRelationships().addAll(removedRelationships);
		getBusinessView().getRelationships().addAll(removedRelationships);

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
