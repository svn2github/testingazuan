/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.EditBusinessColumnsWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.ModifyBusinessTableColumnsCommand;

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
		super(ModifyBusinessTableColumnsCommand.class, workbenchPart, selection);
	}
	
	
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)owner;
		EditBusinessColumnsWizard wizard = new EditBusinessColumnsWizard( businessColumnSet, editingDomain, (ISpagoBIModelCommand)command );
    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
    	dialog.open();
	}
	
}
