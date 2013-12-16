/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.OlapModel;
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
public class DeleteOlapRelationshipCommand extends AbstractSpagoBIModelEditCommand {

	OlapModel model;
	BusinessRelationship businessRelationship;
	Cube cube;
	Dimension dimension;
	
	
	private static Logger logger = LoggerFactory.getLogger(DeleteOlapRelationshipCommand.class);
	
	public DeleteOlapRelationshipCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.olap.commands.edit.relationship.delete.label"
			 , "model.olap.commands.edit.relationship.delete.description"
			 , "model.business.commands.edit.table.delete"
			 , domain, parameter);
	}
	
	public DeleteOlapRelationshipCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected BusinessRelationship getBusinessRelationship() {
		if(businessRelationship == null) businessRelationship = (BusinessRelationship)parameter.getOwner();
		return businessRelationship;
	}
	
	@Override
	public void execute() {
		model = getBusinessRelationship().getModel().getParentModel().getOlapModels().get(0);
		OlapModelInitializer olapModelInitializer = new OlapModelInitializer();
		//Check if Source Table is a Cube and Destination Table is a Dimension
		cube = olapModelInitializer.getCube(getBusinessRelationship().getSourceTable());
		dimension = olapModelInitializer.getDimension(getBusinessRelationship().getDestinationTable());
		
		if ((cube != null) && (dimension != null)){
			//remove relationship from the cube
			cube.getDimensions().remove(dimension);
		}
		
		executed = true;
	}
	

	
	@Override
	public void undo() {
		
		
		if ((cube != null) && (dimension != null)){
			//add relationship from the cube to the dimension
			cube.getDimensions().add(dimension);
		}

	}

	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(getBusinessRelationship().getModel() != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(getBusinessRelationship().getModel());
		}
		return affectedObjects;
	}
	


}
