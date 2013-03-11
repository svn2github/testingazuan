/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands;

import it.eng.spagobi.meta.edit.SpagoBIMetaEditPlugin;

import java.util.ArrayList;

import org.eclipse.emf.common.command.AbstractCommand;
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
