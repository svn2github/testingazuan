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
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
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
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class EditBusinessViewInnerJoinRelationshipsCommand extends
		AbstractSpagoBIModelEditCommand {

	
	BusinessColumnSet businessColumnSet;
	BusinessView businessView;
	BusinessModel businessModel;
	
	List<BusinessViewInnerJoinRelationship> joinRelationships;
	List<BusinessViewInnerJoinRelationshipDescriptor> joinRelationshipsDescriptors;
	List<BusinessViewInnerJoinRelationshipDescriptor> modifiedJoinRelationshipsDescriptors; 
	List<BusinessViewInnerJoinRelationshipDescriptor> removedJoinRelationshipsDescriptors; 
	
	List<BusinessViewInnerJoinRelationship> removedJoinRelationships;
	List<BusinessViewInnerJoinRelationship> addedJoinRelationships;
	List<BusinessViewInnerJoinRelationship> modifiedJoinRelationships;

	List<List<PhysicalColumn>> modifiedSourceColumns;
	List<List<PhysicalColumn>> modifiedDestinationColumns;
	
	private static Logger logger = LoggerFactory.getLogger(EditBusinessViewInnerJoinRelationshipsCommand.class);

	
	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public EditBusinessViewInnerJoinRelationshipsCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.edit.view.editjoins.label", "model.business.commands.edit.view.editjoins.label.description", "model.business.commands.edit.view.editjoins", domain, parameter);
		
	}

	public EditBusinessViewInnerJoinRelationshipsCommand(EditingDomain domain){
		this(domain, null);
	}
	
	
	@Override
	public void execute() {
		modifiedJoinRelationshipsDescriptors = new ArrayList<BusinessViewInnerJoinRelationshipDescriptor>();
		removedJoinRelationshipsDescriptors = new ArrayList<BusinessViewInnerJoinRelationshipDescriptor>();
		
		removedJoinRelationships = new ArrayList<BusinessViewInnerJoinRelationship>();
		addedJoinRelationships = new ArrayList<BusinessViewInnerJoinRelationship>();
		modifiedJoinRelationships = new ArrayList<BusinessViewInnerJoinRelationship>();

		modifiedSourceColumns = new ArrayList<List<PhysicalColumn>>();
		modifiedDestinationColumns = new ArrayList<List<PhysicalColumn>>();
		
		
		BusinessModelInitializer initializer = new BusinessModelInitializer();
		businessColumnSet = (BusinessColumnSet)parameter.getOwner();
		businessModel = businessColumnSet.getModel();
		joinRelationshipsDescriptors = (List<BusinessViewInnerJoinRelationshipDescriptor>)parameter.getValue();
		boolean foundJoinRelationship = false;
		if (businessColumnSet instanceof BusinessView){
			businessView = (BusinessView)businessColumnSet;
			joinRelationships = businessView.getJoinRelationships();
			
			//check if descriptors contains new join relationships, edited or deleted
			for (BusinessViewInnerJoinRelationship joinRelationship: joinRelationships){
				foundJoinRelationship = false;
				for (BusinessViewInnerJoinRelationshipDescriptor joinRelationshipsDescriptor: joinRelationshipsDescriptors){
					//same source and destination tables of descriptor
					if ((joinRelationshipsDescriptor.getSourceTable() == joinRelationship.getSourceTable()) && (joinRelationshipsDescriptor.getDestinationTable() == joinRelationship.getDestinationTable()) ){
						//check also if the source and destination columns are the same of the descriptor
						foundJoinRelationship = true;
						if ((joinRelationshipsDescriptor.getSourceColumns().equals(joinRelationship.getSourceColumns())) && (joinRelationshipsDescriptor.getDestinationColumns().equals(joinRelationship.getDestinationColumns()))){
							//we can ignore this descriptor because the join relationship is already present
							removedJoinRelationshipsDescriptors.add(joinRelationshipsDescriptor);
						} else {
							//the join relationships needs to be modified using this descriptor
							modifiedJoinRelationshipsDescriptors.add(joinRelationshipsDescriptor);
							removedJoinRelationshipsDescriptors.add(joinRelationshipsDescriptor);
						}
						
					}
				}
				
				if (!foundJoinRelationship){
					//the join relationships needs to be removed
					removedJoinRelationships.add(joinRelationship);
					foundJoinRelationship = false;
				}
				
				
			}
			//remove not useful descriptors from main collection
			joinRelationshipsDescriptors.removeAll(removedJoinRelationshipsDescriptors);
			
			//Adding new join Relationships
			//*************************************
			//remaining descriptor in the list are the new join relationship to add
			if (!joinRelationshipsDescriptors.isEmpty()){
				for (BusinessViewInnerJoinRelationshipDescriptor joinRelationshipDescriptor: joinRelationshipsDescriptors){
					BusinessViewInnerJoinRelationship businessViewInnerJoinRelationship = initializer.addBusinessViewInnerJoinRelationship(businessModel, joinRelationshipDescriptor);
					addedJoinRelationships.add(businessViewInnerJoinRelationship);
					businessView.getJoinRelationships().add(businessViewInnerJoinRelationship);
				}
			}
			
			//Removing join Relationships
			//*************************************
			if (!removedJoinRelationships.isEmpty()){
				for (BusinessViewInnerJoinRelationship joinRelationshipToRemove: removedJoinRelationships){
					//remove reference from Business View
					businessView.getJoinRelationships().remove(joinRelationshipToRemove);
					//remove reference from Business Model
					businessModel.getJoinRelationships().remove(joinRelationshipToRemove);
				}
			}
			
			//Edit existing join relationships
			//*************************************
			if (!modifiedJoinRelationshipsDescriptors.isEmpty()){
				for (BusinessViewInnerJoinRelationship joinRelationship: joinRelationships){
					for (BusinessViewInnerJoinRelationshipDescriptor modifiedJoinRelationshipsDescriptor: modifiedJoinRelationshipsDescriptors){
						if ((modifiedJoinRelationshipsDescriptor.getSourceTable().equals(joinRelationship.getSourceTable())) && (modifiedJoinRelationshipsDescriptor.getDestinationTable().equals(joinRelationship.getDestinationTable()))){
							
							//save original relationship columns for undo operation
							modifiedJoinRelationships.add(joinRelationship);
							List<PhysicalColumn> sourceColumns = new ArrayList<PhysicalColumn>();
							sourceColumns.addAll(joinRelationship.getSourceColumns());
							modifiedSourceColumns.add(sourceColumns);
							List<PhysicalColumn> destinationColumns = new ArrayList<PhysicalColumn>();
							destinationColumns.addAll(joinRelationship.getDestinationColumns());
							modifiedDestinationColumns.add(destinationColumns);
							
							
							//Editing columns of join relationship
							
							//Source Columns
							joinRelationship.getSourceColumns().clear();
							joinRelationship.getSourceColumns().addAll(modifiedJoinRelationshipsDescriptor.getSourceColumns());
							
							//Destination Columns
							joinRelationship.getDestinationColumns().clear();
							joinRelationship.getDestinationColumns().addAll(modifiedJoinRelationshipsDescriptor.getDestinationColumns());
						}
					}
				}
			}
			
			this.executed = true;
			logger.debug("Command [{}] executed succesfully", EditBusinessViewInnerJoinRelationshipsCommand.class.getName());
			
		}
		
		

		
	}
	
	@Override
	public void undo() {
		//Remove Added Join Relationships
		if (!businessView.getJoinRelationships().isEmpty()){
			businessView.getJoinRelationships().removeAll(addedJoinRelationships);
			businessModel.getJoinRelationships().removeAll(addedJoinRelationships);
		}
		
		//Add Removed Join Relationships
		businessView.getJoinRelationships().addAll(removedJoinRelationships);
		businessModel.getJoinRelationships().addAll(removedJoinRelationships);

		int i = 0;
		//Edited Join Relationships
		for (BusinessViewInnerJoinRelationship modifiedJoinRelationship: modifiedJoinRelationships){
			modifiedJoinRelationship.getSourceColumns().clear();
			modifiedJoinRelationship.getSourceColumns().addAll(modifiedSourceColumns.get(i));
			
			modifiedJoinRelationship.getDestinationColumns().clear();
			modifiedJoinRelationship.getDestinationColumns().addAll(modifiedDestinationColumns.get(i));
			i++;
		}
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessView != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessView);
		} else if (joinRelationships != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(joinRelationships);
		} 
		return affectedObjects;
	}
}
