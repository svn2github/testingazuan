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

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCompoundCommand;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class AddCalculatedFieldCommand extends
		AbstractSpagoBIModelEditCompoundCommand {

	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	
	private static Logger logger = LoggerFactory.getLogger(AddCalculatedFieldCommand.class);
	
	public AddCalculatedFieldCommand(EditingDomain domain, CommandParameter parameter)  {
		super( "model.business.commands.edit.table.addcalculatedfield.label"
				 , "model.business.commands.edit.table.addcalculatedfield.description"
				 , "model.business.commands.edit.table.addcalculatedfield"
				 , domain, parameter);
	}
	
	public AddCalculatedFieldCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	protected boolean prepare() {
		return true;
	}
	
	public void execute() {
		
	}

}
