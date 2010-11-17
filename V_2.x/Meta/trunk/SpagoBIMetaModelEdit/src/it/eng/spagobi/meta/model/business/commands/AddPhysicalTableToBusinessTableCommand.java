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
package it.eng.spagobi.meta.model.business.commands;

import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author cortella
 *
 */
public class AddPhysicalTableToBusinessTableCommand extends
		AbstractSpagoBIModelCommand {

	public AddPhysicalTableToBusinessTableCommand(EditingDomain domain, CommandParameter parameter) {
		super("Physical Table", "Add Physical Table ", domain, parameter);
	}
	
	public AddPhysicalTableToBusinessTableCommand(EditingDomain domain){
		this(domain, null);
	}
	
	@Override
	public void execute() {
		//do something
		System.err.println("COMMAND [AddPhysicalTableToBusinessTableCommand] SUCCESFULLY EXECUTED");
		
		this.executed = true;
	}
	@Override
	public void undo() {
		
	}
	
	@Override
	public void redo() {
		
	}
	@Override
	public Object getImage() {
		return SpagoBIMetalModelEditPlugin.INSTANCE.getImage("full/obj16/PhysicalTable");
	}
}
