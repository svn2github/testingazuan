/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.identifier;

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
