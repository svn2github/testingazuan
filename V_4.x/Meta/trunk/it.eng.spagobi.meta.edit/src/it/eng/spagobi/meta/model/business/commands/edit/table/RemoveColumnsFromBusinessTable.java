/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.Hierarchy;
import it.eng.spagobi.meta.model.olap.Level;
import it.eng.spagobi.meta.model.olap.Measure;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	List<Measure> removedMeasures;
	List<Hierarchy> hierarchiesToRemove;
	Cube cube;
	Dimension dimension;
	List<HierarchyLevel> hierarchyLevelsToRemove;
	List<ModifiedLevel>  modifiedLevels;
	
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
		
		removedMeasures = new ArrayList<Measure>();
		hierarchyLevelsToRemove = new ArrayList<HierarchyLevel>();
		hierarchiesToRemove = new ArrayList<Hierarchy>();
		modifiedLevels = new ArrayList<ModifiedLevel>();
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
			
			updateOlapModel(businessColumnToRemove);
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
		undoUpdateOlapModel();

	}
	
	@Override
	public void redo() {
		execute();
	}
	
	
	private void  updateOlapModel(SimpleBusinessColumn businessColumnToRemove){
		OlapModelInitializer olapModelInitializer =  new OlapModelInitializer();
			
		BusinessColumnSet businessColumnSet = businessColumnToRemove.getTable();
		
		
		cube = olapModelInitializer.getCube(businessColumnSet);
		if (cube != null){
			//Remove a Measure corresponding to the businessColumn
			Measure removedMeasure = olapModelInitializer.removeCorrespondingOlapObject(businessColumnToRemove,cube);
			if (removedMeasure != null){
				removedMeasures.add(removedMeasure);
			}
		} else {
			//Remove Hierarchy Level and references to the businessColumn
			dimension = olapModelInitializer.getDimension(businessColumnSet);
			if (dimension != null){
				for(Hierarchy hierarchy : dimension.getHierarchies()){
					//Level removedLevel = olapModelInitializer.removeHierarchyLevel(dimension, hierarchy, businessColumnToRemove);
					LevelIndex levelIndex = removeHierarchyLevel(dimension, hierarchy, businessColumnToRemove);
					List<ModifiedLevel> modLevels = modifyHierarchyLevel(dimension, hierarchy, businessColumnToRemove);
					if (modLevels != null){
						modifiedLevels.addAll(modLevels);
					}
					if (levelIndex != null){
						HierarchyLevel hierarchyLevel =  new HierarchyLevel(hierarchy,levelIndex);
						hierarchyLevelsToRemove.add(0,hierarchyLevel);
					}
					//If the hierarchy has no levels, remove the hierarchy
					if(hierarchy.getLevels().isEmpty()){
						hierarchiesToRemove.add(hierarchy);
					}
				}
				//remove empty hierarchies
				dimension.getHierarchies().removeAll(hierarchiesToRemove);

			}
		}
		
	}
	
	//Remove a (Hierarchy) Level that point to the passed BusinessColumn
	public LevelIndex removeHierarchyLevel(Dimension dimension, Hierarchy hierarchy, BusinessColumn businessColumn){
		
		List<Level> levels = hierarchy.getLevels();
		Level levelToRemove = null;
		
		for (Level level : levels){
			if (level.getColumn().equals(businessColumn)){
				levelToRemove = level;
			}
		}
		
		if (levelToRemove != null){
			int index = levels.lastIndexOf(levelToRemove);
			levels.remove(levelToRemove);
			return new LevelIndex(index,levelToRemove);
		}

		return null;	
		
	}
	
	//Set to null references to the deleted BusinessColumn inside a (OlapModel) Level object
	public List<ModifiedLevel> modifyHierarchyLevel(Dimension dimension, Hierarchy hierarchy, BusinessColumn businessColumn){
		
		List<Level> levels = hierarchy.getLevels();
		List<ModifiedLevel> modifiedLevels = new ArrayList<ModifiedLevel>();

		for (Level level : levels){
			boolean modifiedNameColumn = false;
			boolean modifiedOrdinalColumn = false;
			boolean modifiedCaptionColumn = false;
			
			if((level.getNameColumn() != null) && (level.getNameColumn().equals(businessColumn))){
				level.setNameColumn(null);
				modifiedNameColumn = true;
			}
			if((level.getOrdinalColumn() != null) && (level.getOrdinalColumn().equals(businessColumn))){
				level.setOrdinalColumn(null);
				modifiedOrdinalColumn = true;
			}
			if((level.getCaptionColumn() != null) && (level.getCaptionColumn().equals(businessColumn))){
				level.setCaptionColumn(null);
				modifiedCaptionColumn = true;
			}
			
			if((modifiedNameColumn) || (modifiedOrdinalColumn) || (modifiedCaptionColumn)){
				ModifiedLevel modifiedLevel= new ModifiedLevel(level, hierarchy, businessColumn,modifiedNameColumn,modifiedOrdinalColumn,modifiedCaptionColumn );
				modifiedLevels.add(modifiedLevel);
			}			
		}
		
		if (!modifiedLevels.isEmpty()){	
			return modifiedLevels;
		}
		return null;	
		
	}
	 
	
	private void undoUpdateOlapModel(){
		OlapModelInitializer olapModelInitializer =  new OlapModelInitializer();

		//Re-add Measures
		if ((cube != null) && (!removedMeasures.isEmpty())){
			for (Measure measure : removedMeasures){
				cube.getMeasures().add(measure);
			}
		} else if (dimension != null){
			if (!hierarchiesToRemove.isEmpty()){
				//re-add empty hierarchies
				dimension.getHierarchies().addAll(hierarchiesToRemove);
			}
			if (!hierarchyLevelsToRemove.isEmpty()){
				//re-add levels to corresponding hierarchies			
				for (HierarchyLevel hierarchyLevel : hierarchyLevelsToRemove){
					Level level = hierarchyLevel.getLevelIndex().getLevel();
					int index = hierarchyLevel.getLevelIndex().getIndex();
					Hierarchy hierarchy = hierarchyLevel.getHierarchy();
					hierarchy.getLevels().add(index, level);
					//level.setHierarchy(hierarchyLevel.getHierarchy());
				}
			}
			//re-set references to (un)deleted BusinessColumn in modified levels
			if (!modifiedLevels.isEmpty()){
				for (ModifiedLevel modifiedLevel : modifiedLevels){
					Level level = modifiedLevel.getLevel();
					BusinessColumn businessColumn = modifiedLevel.getBusinessColumn();
					if (modifiedLevel.isModifiedCaptionColumn()){
						level.setCaptionColumn(businessColumn);
					}
					if (modifiedLevel.isModifiedNameColumn()){
						level.setNameColumn(businessColumn);
					}
					if (modifiedLevel.isModifiedOrdinalColumn()){
						level.setOrdinalColumn(businessColumn);
					}					
				}
			}
			
		}
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
	
	//*********** Inner class *************
	private class HierarchyLevel{
		private Hierarchy hierarchy;
		private LevelIndex levelIndex;
		
		public HierarchyLevel(Hierarchy hierarchy, LevelIndex levelIndex){
			this.hierarchy = hierarchy;
			this.levelIndex = levelIndex;
		}

		/**
		 * @return the hierarchy
		 */
		public Hierarchy getHierarchy() {
			return hierarchy;
		}

		/**
		 * @param hierarchy the hierarchy to set
		 */
		public void setHierarchy(Hierarchy hierarchy) {
			this.hierarchy = hierarchy;
		}

		/**
		 * @return the level
		 */
		public LevelIndex getLevelIndex() {
			return levelIndex;
		}

		/**
		 * @param level the level to set
		 */
		public void setLevel(LevelIndex levelIndex) {
			this.levelIndex = levelIndex;
		}
	}
	//******************************************
	
	private class LevelIndex{
		private int index;
		private Level level;
		
		public LevelIndex(int index, Level level){
			this.index = index;
			this.level = level;
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @param index the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * @return the level
		 */
		public Level getLevel() {
			return level;
		}

		/**
		 * @param level the level to set
		 */
		public void setLevel(Level level) {
			this.level = level;
		}
		
		
	}
	//******************************************
	
	private class ModifiedLevel{
		Level level;
		Hierarchy hierarchy;
		BusinessColumn businessColumn;
		
		boolean modifiedNameColumn;
		boolean modifiedOrdinalColumn;
		boolean modifiedCaptionColumn;

		public ModifiedLevel(Level level, Hierarchy hierarchy, BusinessColumn businessColumn,boolean modifiedNameColumn,boolean modifiedOrdinalColumn,boolean modifiedCaptionColumn){
			this.level = level;
			this.hierarchy = hierarchy;
			this.businessColumn = businessColumn;
			this.modifiedCaptionColumn = modifiedCaptionColumn;
			this.modifiedNameColumn = modifiedNameColumn;
			this.modifiedOrdinalColumn = modifiedOrdinalColumn;
		}

		/**
		 * @return the level
		 */
		public Level getLevel() {
			return level;
		}

		/**
		 * @param level the level to set
		 */
		public void setLevel(Level level) {
			this.level = level;
		}

		/**
		 * @return the hierarchy
		 */
		public Hierarchy getHierarchy() {
			return hierarchy;
		}

		/**
		 * @param hierarchy the hierarchy to set
		 */
		public void setHierarchy(Hierarchy hierarchy) {
			this.hierarchy = hierarchy;
		}

		/**
		 * @return the businessColumn
		 */
		public BusinessColumn getBusinessColumn() {
			return businessColumn;
		}

		/**
		 * @param businessColumn the businessColumn to set
		 */
		public void setBusinessColumn(BusinessColumn businessColumn) {
			this.businessColumn = businessColumn;
		}

		/**
		 * @return the modifiedNameColumn
		 */
		public boolean isModifiedNameColumn() {
			return modifiedNameColumn;
		}

		/**
		 * @param modifiedNameColumn the modifiedNameColumn to set
		 */
		public void setModifiedNameColumn(boolean modifiedNameColumn) {
			this.modifiedNameColumn = modifiedNameColumn;
		}

		/**
		 * @return the modifiedOrdinalColumn
		 */
		public boolean isModifiedOrdinalColumn() {
			return modifiedOrdinalColumn;
		}

		/**
		 * @param modifiedOrdinalColumn the modifiedOrdinalColumn to set
		 */
		public void setModifiedOrdinalColumn(boolean modifiedOrdinalColumn) {
			this.modifiedOrdinalColumn = modifiedOrdinalColumn;
		}

		/**
		 * @return the modifiedCaptionColumn
		 */
		public boolean isModifiedCaptionColumn() {
			return modifiedCaptionColumn;
		}

		/**
		 * @param modifiedCaptionColumn the modifiedCaptionColumn to set
		 */
		public void setModifiedCaptionColumn(boolean modifiedCaptionColumn) {
			this.modifiedCaptionColumn = modifiedCaptionColumn;
		}
		
		

	}
	
	//******************************************
}
