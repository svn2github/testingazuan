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

import it.eng.spagobi.meta.edit.SpagoBIMetaEditPlugin;

import java.util.ArrayList;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractSpagoBIModelSimpleCommand extends AbstractCommand implements ISpagoBIModelCommand {

	EditingDomain domain;
	protected CommandParameter parameter;
	protected boolean executed;
	
	Object image;
	
	

	public AbstractSpagoBIModelSimpleCommand(String commandLabel, String commandDescription, String commandImage, EditingDomain domain, CommandParameter parameter) {
		
		
		super( SpagoBIMetaEditPlugin.getInstance().getResourceLocator().getString(commandLabel)
			 , SpagoBIMetaEditPlugin.getInstance().getResourceLocator().getString(commandDescription) );
		
		this.domain = domain;
		this.parameter = parameter;
		this.executed = false;
		this.image = SpagoBIMetaEditPlugin.getInstance().getResourceLocator().getImage(commandImage);
	}

	
	public CommandParameter getParameter() {
		if(parameter == null) return null;
		return new CommandParameter(parameter.getOwner(), parameter.feature, parameter.value, new ArrayList<Object>(parameter.getCollection()));
	}

	public void setParameter(CommandParameter parameter) {
		if(!executed) {
			this.parameter = parameter;
		}
	}
	
	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	public void execute() {
		executed = true;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}
	
	
	// Implementation of CommandActionDelegate
	
	
	@Override
	public Object getImage() {
		return image;
	}

	@Override
	public String getText() {
		return getLabel();
	}

	@Override
	public String getToolTipText() {
		return getDescription();
	}

}
