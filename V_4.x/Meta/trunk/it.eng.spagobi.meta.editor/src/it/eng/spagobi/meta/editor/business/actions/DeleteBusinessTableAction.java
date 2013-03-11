/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.DeleteBusinessTableCommand;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DeleteBusinessTableAction extends AbstractSpagoBIModelAction {
	
	ISpagoBIModelCommand performFinishCommand; 
	
	public DeleteBusinessTableAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(DeleteBusinessTableCommand.class, workbenchPart, selection);
		if (command instanceof ISpagoBIModelCommand)
			this.performFinishCommand = (ISpagoBIModelCommand)command;
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			/*
			no wizard here because we do not need some extra input. we can execute command directly
	    	*/
			
			// this guard is for extra security, but should not be necessary
		    if (editingDomain != null && performFinishCommand != null) {
		    	// use up the command
		    	editingDomain.getCommandStack().execute(performFinishCommand);
		    }

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * @return the performFinishCommand
	 */
	public ISpagoBIModelCommand getPerformFinishCommand() {
		return performFinishCommand;
	}
	
}
