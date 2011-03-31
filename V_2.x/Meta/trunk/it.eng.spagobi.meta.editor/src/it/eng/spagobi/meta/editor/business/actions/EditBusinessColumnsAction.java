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

import it.eng.spagobi.meta.editor.business.wizards.inline.EditBusinessColumnsWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.EditBusinessColumnsCommand;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class EditBusinessColumnsAction extends AbstractSpagoBIModelAction {
	
	public EditBusinessColumnsAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(EditBusinessColumnsCommand.class, workbenchPart, selection);
	}
	
	
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)owner;
		EditBusinessColumnsWizard wizard = new EditBusinessColumnsWizard( businessColumnSet, editingDomain, (AbstractSpagoBIModelCommand)command );
    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
    	dialog.open();
	}
	
}
