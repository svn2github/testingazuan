/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.generate;

import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelSimpleCommand;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * All commands that use the model as input to generate some other artifact must extends this class
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractSpagoBIModelGenerateCommand extends AbstractSpagoBIModelSimpleCommand {

	public AbstractSpagoBIModelGenerateCommand(String commandLabel,
			String commandDescription, String commandImage,
			EditingDomain domain, CommandParameter parameter) {
		super(commandLabel, commandDescription, commandImage, domain, parameter);
	}

}
