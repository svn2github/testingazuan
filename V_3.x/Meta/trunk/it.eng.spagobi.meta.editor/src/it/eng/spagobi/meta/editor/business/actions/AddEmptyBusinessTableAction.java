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
