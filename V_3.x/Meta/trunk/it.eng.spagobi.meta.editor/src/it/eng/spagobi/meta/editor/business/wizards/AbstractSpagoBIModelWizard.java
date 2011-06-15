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
package it.eng.spagobi.meta.editor.business.wizards;

import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.Wizard;

public abstract class AbstractSpagoBIModelWizard extends Wizard {

	ISpagoBIModelCommand performFinishCommand;
	EditingDomain editingDomain;
	
	public AbstractSpagoBIModelWizard(EditingDomain editingDomain, ISpagoBIModelCommand command){
		super();
		this.editingDomain = editingDomain;
		this.performFinishCommand = command;
	}
	
	@Override
	public boolean performFinish() {
		if (isWizardComplete()){
			
			performFinishCommand.setParameter( getCommandInputParameter() );
			
			
			// this guard is for extra security, but should not be necessary
		    if (editingDomain != null && performFinishCommand != null) {
		    	// use up the command
		    	editingDomain.getCommandStack().execute(performFinishCommand);
		    }
	
			return true;
		} else {
			return false;
		}
	}
	
	public abstract CommandParameter getCommandInputParameter();
	public abstract boolean isWizardComplete();

	

	
}
