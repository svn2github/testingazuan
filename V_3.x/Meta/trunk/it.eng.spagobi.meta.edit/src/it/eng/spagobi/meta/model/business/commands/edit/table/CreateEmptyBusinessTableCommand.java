/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.table;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class CreateEmptyBusinessTableCommand extends AbstractSpagoBIModelEditCommand {

	BusinessModelInitializer initializer;
	
	// input objects
	BusinessModel businessModel;
	
	// cached objects
	BusinessTable addedBusinessTable;

	
	private static Logger logger = LoggerFactory.getLogger(CreateEmptyBusinessTableCommand.class);
	
	
	public CreateEmptyBusinessTableCommand(EditingDomain domain, CommandParameter parameter) {
		super( "model.business.commands.edit.table.empty.create.label"
			 , "model.business.commands.edit.table.empty.create.description"
			 , "model.business.commands.edit.table.create"
			 , domain, parameter);
		
		initializer = new BusinessModelInitializer();
	}
	
	public CreateEmptyBusinessTableCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected void clearCachedObjects() {
		addedBusinessTable = null;
	}
	
	@Override
	protected boolean prepare() {
		return true;
	}
	
	@Override
	public void execute() {
		
		clearCachedObjects();
		Object owner = parameter.getOwner();
		
		if (owner instanceof BusinessRootItemProvider){
			businessModel = (BusinessModel)((BusinessRootItemProvider)owner).getParentObject();
		}
		String businessTableName = (String)parameter.getValue();
		
		executeCreateTable(businessTableName);
	}
	

	
	@Override
	public void undo() {
		// undo add table		
		businessModel.getTables().remove(addedBusinessTable);
	
	}
	
	
	public void executeCreateTable(String tableName) {
		addedBusinessTable = initializer.addEmptyTable(businessModel, tableName);

		this.executed = true;
		logger.debug("Command [{}] executed succesfully", CreateEmptyBusinessTableCommand.class.getName());	
	}
	
	
	

	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessModel != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessModel);
		}
		return affectedObjects;
	}
	

}
