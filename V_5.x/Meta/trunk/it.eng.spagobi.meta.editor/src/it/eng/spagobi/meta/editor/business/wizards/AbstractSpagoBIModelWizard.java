/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
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
