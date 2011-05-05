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
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO preserve column order upon undo
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModifyBusinessTableColumnsCommand extends AbstractSpagoBIModelEditCommand {

	BusinessModelInitializer initializer;
	
	// input values
	BusinessColumnSet businessTable;
	Collection<PhysicalColumn> newColumnSet;
	Collection<PhysicalColumn> oldColumnSet;
	
	// cache edited columns (added and removed) for undo e redo
	List<BusinessColumn> removedColumns;
	List<BusinessColumn> addedColumns;
	
	BusinessIdentifier removedIdentifier;
	BusinessIdentifier addedIdentifier;
	List<BusinessColumn> columnsRemovedFromIdentifier;
	
	List<BusinessRelationship> removedRelationships;
	List<RelationshipModification> relationshipModifications;
	
	private class RelationshipModification  {
		BusinessRelationship relationship;
		public int index;
		public BusinessColumn sourceColumn;
		public BusinessColumn destinationColumn;
		
		public RelationshipModification(BusinessRelationship r, int i, BusinessColumn s, BusinessColumn d){relationship = r; index = i; sourceColumn = s; destinationColumn = d;}
	}
	
	private static Logger logger = LoggerFactory.getLogger(ModifyBusinessTableColumnsCommand.class);
	
	
	public ModifyBusinessTableColumnsCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.modifycolumns.label"
			 , "model.business.commands.edit.table.modifycolumns.description"
			 , "model.business.commands.edit.table.modifycolumns"
			 , domain, parameter);
		initializer = new BusinessModelInitializer();	
	}
	
	public ModifyBusinessTableColumnsCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected BusinessColumnSet getBusinessTable() {
		if(businessTable == null) businessTable = (BusinessColumnSet)parameter.getOwner();
		return businessTable;
	}
	
	protected Collection<PhysicalColumn> getNewColumnSet() {
		if(newColumnSet == null) newColumnSet = (Collection)parameter.getValue();
		return newColumnSet;
	}
	
	protected Collection<PhysicalColumn> getOldColumnSet() {
		if(oldColumnSet == null) oldColumnSet = extractPhysicalColumns( getBusinessTable() );
		return oldColumnSet;
	}
	
	protected void clearCachedObjects() {
		removedColumns = new ArrayList<BusinessColumn>();
		columnsRemovedFromIdentifier = new ArrayList<BusinessColumn>();
		removedIdentifier = null;
		relationshipModifications = new ArrayList<RelationshipModification>();
		removedRelationships = new ArrayList<BusinessRelationship>();
		addedColumns = new ArrayList<BusinessColumn>();
		addedIdentifier = null;
	}
	
	@Override
	public void execute() {
		
		clearCachedObjects();
		
		removeColumns();		
		addColumns();			
		addIdentifier();
		
		executed = true;
		
		logger.debug("Command [{}] executed succesfully", ModifyBusinessTableColumnsCommand.class.getName());
	}
	
	@Override
	public void undo() {
		undoAddIdentifier();
		undoAddColumns();
		undoRemoveColumns();
	}
	
	private Collection<PhysicalColumn> extractPhysicalColumns(BusinessColumnSet businessColumnSet) {
		List<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
		for(BusinessColumn businessColumn: businessColumnSet.getColumns()) {
			physicalColumns.add(businessColumn.getPhysicalColumn());
		}
		return physicalColumns;
	} 

	private List<PhysicalColumn> getColumnsToRemove() {
		
		List<PhysicalColumn> columnsToRemove = new ArrayList<PhysicalColumn>();
		
		for(PhysicalColumn oldSelectionColumn : getOldColumnSet()) {
			if(isColumnPartOfTheNewSelection(oldSelectionColumn, getNewColumnSet()) == false) {
				columnsToRemove.add(oldSelectionColumn);
			}
		}
		
		return columnsToRemove;
	}
	
	public boolean isColumnPartOfTheNewSelection(PhysicalColumn oldSelectionColumn, Collection<PhysicalColumn> newSelectionColumns) {
		boolean isPart = false;
		for(PhysicalColumn column : newSelectionColumns) {
			if(oldSelectionColumn.equals(column)) {
				isPart = true;
			}
		}
		return isPart;
	}
	
	private List<PhysicalColumn> getColumnsToAdd() {
		
		List<PhysicalColumn> columnsToAdd;
		
		columnsToAdd = new ArrayList<PhysicalColumn>();
		
		for(PhysicalColumn newSelectionColumn : getNewColumnSet()) {
			if(isColumnPartOfTheOldSelection(newSelectionColumn, getOldColumnSet()) == false) {
				columnsToAdd.add(newSelectionColumn);
			}
		}
		
		return columnsToAdd;
	}

	public boolean isColumnPartOfTheOldSelection(PhysicalColumn newSelectionColumn, Collection<PhysicalColumn> oldSelectionColumns) {
		boolean isPart = false;
		
		for(PhysicalColumn oldSelectionColumn : oldSelectionColumns) {
			if(oldSelectionColumn.equals(newSelectionColumn)) {
				isPart = true;
			}
		}
		
		return isPart;
	}
	
	public List<BusinessColumn> removeColumns() {
		List<PhysicalColumn> columns;
		
		columns = getColumnsToRemove();
		
		for(PhysicalColumn column: columns) {
			BusinessColumn c = getBusinessTable().getColumn(column);
			updateIdentifier(c);			
			updateRelationships(c);
			
			// remove
			getBusinessTable().getColumns().remove(c);
			removedColumns.add(c);
		}
		
		return removedColumns;
	}
	
	public void undoRemoveColumns() {
		for(BusinessColumn column: removedColumns) {			
			businessTable.getColumns().add(column);			
		}	
		undoUpdateRelationships();
		undoUpdateIdentifier();
	}

	public List<BusinessColumn> addColumns() {
		List<PhysicalColumn> columns;
		
		columns = getColumnsToAdd();
		
		
		for(PhysicalColumn column: columns) {
			initializer.addColumn(column, getBusinessTable());
			addedColumns.add( getBusinessTable().getColumn(column) );
		}
		return addedColumns;
	}
	
	public void undoAddColumns() {
		for(BusinessColumn column: addedColumns) {
			businessTable.getColumns().remove(column);
		}	
	}
	
	private void updateRelationships(BusinessColumn businessColumn) {
		
		List<BusinessRelationship> businessRelationships;
		List<BusinessRelationship> removedRelationshipsAfterColumnDeletion = new ArrayList<BusinessRelationship>();
		
		businessRelationships = getBusinessTable().getRelationships();
		
		
		for (BusinessRelationship businessRelationship : businessRelationships) {	
			List<BusinessColumn> sourceColumns = businessRelationship.getSourceColumns();
			List<BusinessColumn> destinationColumns = businessRelationship.getDestinationColumns();
			
		
			if (sourceColumns.contains(businessColumn)){
				int index = businessRelationship.getSourceColumns().indexOf(businessColumn);
				businessRelationship.getSourceColumns().remove(businessColumn);
				//remove other part 
				BusinessColumn destinationColumn = businessRelationship.getDestinationColumns().get(index);
				businessRelationship.getDestinationColumns().remove(destinationColumn);
				relationshipModifications.add(0, new RelationshipModification(businessRelationship, index, businessColumn, destinationColumn));
			}
			else if (destinationColumns.contains(businessColumn)){
				int index = businessRelationship.getDestinationColumns().indexOf(businessColumn);
				businessRelationship.getDestinationColumns().remove(businessColumn);
				//remove other part 
				BusinessColumn sourceColumn = businessRelationship.getSourceColumns().get(index);
				businessRelationship.getSourceColumns().remove(sourceColumn);
				relationshipModifications.add(0, new RelationshipModification(businessRelationship, index, sourceColumn, businessColumn));
			}
			
			if (businessRelationship.getSourceColumns().isEmpty() || businessRelationship.getDestinationColumns().isEmpty()){
				removedRelationshipsAfterColumnDeletion.add(businessRelationship);
			}
		}
		//remove inconsistent relationships
		removedRelationships.addAll( removedRelationshipsAfterColumnDeletion );
		getBusinessTable().getModel().getRelationships().removeAll(removedRelationshipsAfterColumnDeletion);
	}
	
	private void undoUpdateRelationships() {
		getBusinessTable().getModel().getRelationships().addAll(removedRelationships);
		
		for(RelationshipModification modification : relationshipModifications) {
			modification.relationship.getSourceColumns().add( modification.index, modification.sourceColumn );
			modification.relationship.getDestinationColumns().add( modification.index, modification.destinationColumn );
		}
	}

	private void updateIdentifier(BusinessColumn businessColumn) {
		
		if(businessColumn.isIdentifier()) {
			BusinessIdentifier identifier = getBusinessTable().getIdentifier();
			identifier.getColumns().remove(businessColumn);
			columnsRemovedFromIdentifier.add(businessColumn);
			
			if(identifier.getColumns().size() == 0) {
				getBusinessTable().getModel().getIdentifiers().remove(identifier);
				removedIdentifier = identifier;
			}
		}
	}
	
	private void undoUpdateIdentifier() {
		if(removedIdentifier != null) {
			getBusinessTable().getModel().getIdentifiers().add(removedIdentifier);
		}
		
		if(columnsRemovedFromIdentifier.size() > 0) {
			getBusinessTable().getIdentifier().getColumns().addAll(columnsRemovedFromIdentifier);
		}
		
	}
	
	private void addIdentifier() {
		if(getBusinessTable() instanceof BusinessTable) {
			BusinessTable businessTable = (BusinessTable)getBusinessTable();
			BusinessIdentifier identifier = getBusinessTable().getIdentifier();
			if(identifier == null) {
				addedIdentifier = initializer.addIdentifier(businessTable, getBusinessTable().getModel());
			}
		}
	}
	
	private void undoAddIdentifier() {
		if(addedIdentifier != null) {
			businessTable.getModel().getIdentifiers().remove(addedIdentifier);
		}
	}
	
	
	

	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		BusinessColumnSet businessTable = (BusinessColumnSet)parameter.getOwner();
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessTable != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessTable);
		}
		return affectedObjects;
	}
	

}
