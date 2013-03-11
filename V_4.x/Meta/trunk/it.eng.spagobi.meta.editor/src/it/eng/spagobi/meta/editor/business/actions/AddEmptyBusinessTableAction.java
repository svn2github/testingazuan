/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.CreateEmptyBusinessTableCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddEmptyBusinessTableAction extends AbstractSpagoBIModelAction {
	private ISpagoBIModelCommand performFinishCommand;

	public AddEmptyBusinessTableAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(CreateEmptyBusinessTableCommand.class, workbenchPart, selection);
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
				InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
						"New Empty Business Class", "Please enter Business Class name", "New Business Class", null);
				if (dlg.open() == Window.OK) {
					performFinishCommand.setParameter(new CommandParameter(owner, null, dlg.getValue(), new ArrayList<Object>()));
				}
				editingDomain.getCommandStack().execute(performFinishCommand);
			}

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
}
