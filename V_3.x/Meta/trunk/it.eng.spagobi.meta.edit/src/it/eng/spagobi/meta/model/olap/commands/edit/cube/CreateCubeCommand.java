/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.olap.commands.edit.cube;

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
public class CreateCubeCommand extends AbstractSpagoBIModelEditCommand {
	
	OlapModelInitializer olapModelInitializer;
	
	BusinessColumnSet businessColumnSet;
	OlapModel olapModel;
	Cube addedCube;
	String originalTableType;
	Object removedPreviousObject;
	private static Logger logger = LoggerFactory.getLogger(CreateCubeCommand.class);

	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public CreateCubeCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.olap.commands.edit.cube.create.label", "model.olap.commands.edit.cube.create.description", "model.olap.commands.edit.cube.create", domain, parameter);
		olapModelInitializer = new OlapModelInitializer();
	}
	
	public CreateCubeCommand(EditingDomain domain){
		this(domain,null);
	}
	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessColumnSet){
			olapModel = (OlapModel)parameter.getOwner();
			businessColumnSet= (BusinessColumnSet)parameter.getValue();
			//get the original Table Type Value for undo
			originalTableType = businessColumnSet.getProperties().get("structural.tabletype").getValue();

			//remove previous objects
			removedPreviousObject = olapModelInitializer.removeCorrespondingOlapObject(businessColumnSet);

			
			addedCube = olapModelInitializer.addCube(olapModel, businessColumnSet);
			
			//Set property tabletype = cube
			businessColumnSet.getProperties().get("structural.tabletype").setValue("cube");

			this.executed = true;
			logger.debug("Command [{}] executed succesfully", CreateCubeCommand.class.getName());	

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
		if ((removedPreviousObject != null) && (removedPreviousObject instanceof Dimension)){
			olapModelInitializer.addDimension(olapModel, (Dimension)removedPreviousObject);
		}
		olapModel.getCubes().remove(addedCube);
		businessColumnSet.getProperties().get("structural.tabletype").setValue(originalTableType);
	}
	
	@Override
	public void redo() {
		execute();
	}

}
