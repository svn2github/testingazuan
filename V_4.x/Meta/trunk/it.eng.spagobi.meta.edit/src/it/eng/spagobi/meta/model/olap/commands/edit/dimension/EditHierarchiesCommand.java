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
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
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

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author cortella
 *
 */
public class EditHierarchiesCommand extends AbstractSpagoBIModelEditCommand {
	
	OlapModelInitializer olapModelInitializer;
	
	BusinessColumnSet businessColumnSet;
	OlapModel olapModel;
	Dimension addedDimension;
	String originalTableType;
	Object removedPreviousObject;


	private static Logger logger = LoggerFactory.getLogger(EditHierarchiesCommand.class);

	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public EditHierarchiesCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.olap.commands.edit.hierarchies.label", "model.olap.commands.edit.hierarchies.description", "model.olap.commands.edit.dimension.edithierarchies", domain, parameter);
		olapModelInitializer = new OlapModelInitializer();
	}
	
	public EditHierarchiesCommand(EditingDomain domain){
		this(domain,null);
	}
	@Override
	public void execute() {
		
		//This is a fake command that doesn't perform anything
			
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
	
	}
	
	@Override
	public void redo() {
		
	}

}
