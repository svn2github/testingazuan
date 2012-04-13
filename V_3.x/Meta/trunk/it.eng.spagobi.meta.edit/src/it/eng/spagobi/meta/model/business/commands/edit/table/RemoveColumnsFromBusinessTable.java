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
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO preserve column order upon undo
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class RemoveColumnsFromBusinessTable extends AbstractSpagoBIModelEditCommand {
	
	BusinessModelInitializer initializer;
	
	// input values
	BusinessColumnSet businessTable;
	Collection<PhysicalColumn> columnsToRemove;
	List<BusinessColumn> columnsRemovedFromIdentifier;
	BusinessIdentifier removedIdentifier;
	
	List<BusinessRelationship> removedRelationships;
	List<RelationshipModification> relationshipModifications;
	
	private class RelationshipModification  {
		BusinessRelationship relationship;
		public int index;
		public BusinessColumn sourceColumn;
		public BusinessColumn destinationColumn;
		
		public RelationshipModification(BusinessRelationship r, int i, BusinessColumn s, BusinessColumn d){relationship = r; index = i; sourceColumn = s; destinationColumn = d;}
	}
	
	// cache edited columns (added and removed) for undo e redo
	List<BusinessColumn> removedColumns;
	
		
	private static Logger logger = LoggerFactory.getLogger(RemoveColumnsFromBusinessTable.class);
	
	public RemoveColumnsFromBusinessTable(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.removecolumns.label"
			 , "model.business.commands.edit.table.removecolumns.description"
			 , "model.business.commands.edit.table.removecolumns"
			 , domain, parameter);
		initializer = new BusinessModelInitializer();	
	}
	
	public RemoveColumnsFromBusinessTable(EditingDomain domain) {
		this(domain, null);
	}
	
	protected void clearCachedObjects() {
		removedColumns = new ArrayList<BusinessColumn>();
		columnsRemovedFromIdentifier = new ArrayList<BusinessColumn>();
		removedIdentifier = null;
		relationshipModifications = new ArrayList<RelationshipModification>();
		removedRelationships = new ArrayList<BusinessRelationship>();
	}
	
	@Override
	protected boolean prepare() {
		businessTable = (BusinessColumnSet)parameter.getOwner();
		columnsToRemove = (Collection)parameter.getValue();
		return (businessTable != null && columnsToRemove != null);
	}
	@Override
	public void execute() {
		
		clearCachedObjects();
		
		
		
		for(PhysicalColumn column: columnsToRemove) {
			SimpleBusinessColumn businessColumnToRemove = businessTable.getSimpleBusinessColumn(column);
			
			boolean canBeDeleted = true;
			if(businessTable instanceof BusinessView) {
				BusinessView businessView = (BusinessView)businessTable;
				List<BusinessViewInnerJoinRelationship> innerRelationships = businessView.getJoinRelationships();
				for(BusinessViewInnerJoinRelationship innerRelationship : innerRelationships) {
					if(innerRelationship.getSourceColumns().contains(businessColumnToRemove.getPhysicalColumn())
					|| innerRelationship.getDestinationColumns().contains(businessColumnToRemove.getPhysicalColumn())) {
						canBeDeleted = false;
						break;
					}
				}
			}
			if(canBeDeleted == false) {
				showInformation("Impossible to delete attribute", "Business attribute [" + businessColumnToRemove.getName() + "] cannot be deleted because it is used in join relationship. If you want to hide it in query editor set its visible property to false.");
				continue;
			}
			
			updateIdentifier(businessColumnToRemove);			
			updateRelationships(businessColumnToRemove);
			
			// remove
			businessTable.getColumns().remove(businessColumnToRemove);
			removedColumns.add(businessColumnToRemove);
		}
		
		executed = true;
	}
	
	@Override
	public void undo() {
		for(BusinessColumn column: removedColumns) {			
			businessTable.getColumns().add(column);			
		}	
		undoUpdateRelationships();
		undoUpdateIdentifier();
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	private void updateIdentifier(BusinessColumn businessColumn) {
		
		if(businessColumn.isIdentifier()) {
			BusinessIdentifier identifier = businessTable.getIdentifier();
			identifier.getColumns().remove(businessColumn);
			columnsRemovedFromIdentifier.add(businessColumn);
			
			if(identifier.getColumns().size() == 0) {
				businessTable.getModel().getIdentifiers().remove(identifier);
				removedIdentifier = identifier;
			}
		}
	}
	
	private void undoUpdateIdentifier() {
		if(removedIdentifier != null) {
			businessTable.getModel().getIdentifiers().add(removedIdentifier);
		}
		
		if(columnsRemovedFromIdentifier.size() > 0) {
			businessTable.getIdentifier().getColumns().addAll(columnsRemovedFromIdentifier);
		}
		
	}
	
	private void updateRelationships(BusinessColumn businessColumn) {
		
		List<BusinessRelationship> businessRelationships;
		List<BusinessRelationship> removedRelationshipsAfterColumnDeletion = new ArrayList<BusinessRelationship>();
		
		businessRelationships = businessTable.getRelationships();
		
		
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
		businessTable.getModel().getRelationships().removeAll(removedRelationshipsAfterColumnDeletion);
	}
	
	private void undoUpdateRelationships() {
		businessTable.getModel().getRelationships().addAll(removedRelationships);
		
		for(RelationshipModification modification : relationshipModifications) {
			modification.relationship.getSourceColumns().add( modification.index, modification.sourceColumn );
			modification.relationship.getDestinationColumns().add( modification.index, modification.destinationColumn );
		}
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
	
	@Override
	public Collection<?> getResult() {
		// TODO the result here should change with operation type (execute = columns; undo = table)
		return getAffectedObjects();
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

}
