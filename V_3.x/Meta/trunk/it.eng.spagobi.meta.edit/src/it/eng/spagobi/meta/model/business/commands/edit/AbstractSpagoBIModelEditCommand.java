/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit;

import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelSimpleCommand;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * All commands that modify the model structure must extends this abstract class
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractSpagoBIModelEditCommand extends AbstractSpagoBIModelSimpleCommand {

	public AbstractSpagoBIModelEditCommand(String commandLabel,
			String commandDescription, String commandImage,
			EditingDomain domain, CommandParameter parameter) {
		super(commandLabel, commandDescription, commandImage, domain, parameter);
	}

}
