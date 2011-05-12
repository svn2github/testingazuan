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
import it.eng.spagobi.meta.initializer.descriptor.BusinessTableDescriptor;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;
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
