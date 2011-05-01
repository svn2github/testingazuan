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
package it.eng.spagobi.meta.model.business.commands.edit.identifier;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DeleteIdentifierCommand extends AbstractSpagoBIModelEditCommand {

	BusinessIdentifier businessIdentifier;
	BusinessModel model;
	
	private static Logger logger = LoggerFactory.getLogger(DeleteIdentifierCommand.class);
	
	
	public DeleteIdentifierCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.edit.identifier.delete.label"
			 , "model.business.commands.edit.identifier.delete.description"
			 , "model.business.commands.edit.identifier.delete"
			 , domain, parameter);
	}
	
	public DeleteIdentifierCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		businessIdentifier = (BusinessIdentifier)parameter.getOwner();
		model = businessIdentifier.getModel();
		model.getIdentifiers().remove(businessIdentifier);
		executed = true;
		logger.debug("Command [{}] executed succesfully", DeleteIdentifierCommand.class.getName());
	}
	
	
	@Override
	public void undo() {
		model.getIdentifiers().add(businessIdentifier);
	}

	@Override
	public void redo() {
		model.getIdentifiers().remove(businessIdentifier);
	}
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessIdentifier != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessIdentifier.getTable());
		}
		return affectedObjects;
	}
	
}
