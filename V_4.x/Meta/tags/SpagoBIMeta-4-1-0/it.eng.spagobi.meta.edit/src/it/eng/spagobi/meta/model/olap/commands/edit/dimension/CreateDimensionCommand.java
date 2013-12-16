/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.olap.commands.edit.dimension;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.CreateEmptyBusinessTableCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.OlapModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class CreateDimensionCommand extends AbstractSpagoBIModelEditCommand {
	
	OlapModelInitializer olapModelInitializer;
	
	BusinessColumnSet businessColumnSet;
	OlapModel olapModel;
	Dimension addedDimension;
	String originalTableType;
	Object removedPreviousObject;


	private static Logger logger = LoggerFactory.getLogger(CreateDimensionCommand.class);

	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public CreateDimensionCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.olap.commands.edit.dimension.create.label", "model.olap.commands.edit.dimension.create.description", "model.olap.commands.edit.dimension.create", domain, parameter);
		olapModelInitializer = new OlapModelInitializer();
	}
	
	public CreateDimensionCommand(EditingDomain domain){
		this(domain,null);
	}
	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessColumnSet){
			olapModel = (OlapModel)parameter.getOwner();
			businessColumnSet= (BusinessColumnSet)parameter.getValue();
			//get the original Table Type Value for undo
			if (parameter.getFeature() != null){
				//original type from the property view
				originalTableType = (String)parameter.getFeature();
			}
			else{
				originalTableType = businessColumnSet.getProperties().get("structural.tabletype").getValue();			
			}
			//remove previous objects
			removedPreviousObject = olapModelInitializer.removeCorrespondingOlapObject(businessColumnSet);			
			
			addedDimension = olapModelInitializer.addDimension(olapModel, businessColumnSet);
			
			//Set property tabletype = dimension
			businessColumnSet.getProperties().get("structural.tabletype").setValue("dimension");

			this.executed = true;
			logger.debug("Command [{}] executed succesfully", CreateDimensionCommand.class.getName());	

		}
			
	}
	
	
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(olapModel != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(olapModel);
		}
		return affectedObjects;
	}
	
	
	@Override
	public void undo() {		
		if ((removedPreviousObject != null) && (removedPreviousObject instanceof Cube)){
			olapModelInitializer.addCube(olapModel, (Cube)removedPreviousObject);
		}
		olapModelInitializer.removeDimension(olapModel,addedDimension);
		businessColumnSet.getProperties().get("structural.tabletype").setValue(originalTableType);
	}
	
	@Override
	public void redo() {
		execute();
	}

}
