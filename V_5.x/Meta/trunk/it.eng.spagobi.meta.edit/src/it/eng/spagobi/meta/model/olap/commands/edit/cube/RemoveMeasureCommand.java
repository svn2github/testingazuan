/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.olap.commands.edit.cube;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.CreateEmptyBusinessTableCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Measure;
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
public class RemoveMeasureCommand extends AbstractSpagoBIModelEditCommand {
	
	OlapModelInitializer olapModelInitializer;
	
	BusinessColumn businessColumn;
	Cube cube;
	Measure removedMeasure;

	private String originalTableType;
	private static Logger logger = LoggerFactory.getLogger(RemoveMeasureCommand.class);

	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public RemoveMeasureCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.olap.commands.edit.cube.removemeasure.label", "model.olap.commands.edit.cube.removemeasure.description", "model.olap.commands.edit.cube.removemeasure", domain, parameter);
		olapModelInitializer = new OlapModelInitializer();
	}
	
	public RemoveMeasureCommand(EditingDomain domain){
		this(domain,null);
	}
	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessColumn){
			cube = (Cube)parameter.getOwner();
			businessColumn= (BusinessColumn)parameter.getValue();
			//get the original Column Type Value for undo
			if (parameter.getFeature() != null){
				//original type from the property view
				originalTableType = (String)parameter.getFeature();
			}
			else{
				originalTableType = businessColumn.getProperties().get("structural.columntype").getValue();
			}
			
			removedMeasure = olapModelInitializer.removeCorrespondingOlapObject(businessColumn,cube);
			//Set property columntype = measure
			businessColumn.getProperties().get("structural.columntype").setValue("attribute");

			this.executed = true;
			
			logger.debug("Command [{}] executed succesfully", RemoveMeasureCommand.class.getName());	

		}
			
	}
	
	
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(cube.getModel() != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(cube.getModel());
		}
		return affectedObjects;
	}
	
	
	@Override
	public void undo() {		
		cube.getMeasures().add(removedMeasure);
		businessColumn.getProperties().get("structural.columntype").setValue(originalTableType);
	}
	
	@Override
	public void redo() {
		execute();
	}

}
