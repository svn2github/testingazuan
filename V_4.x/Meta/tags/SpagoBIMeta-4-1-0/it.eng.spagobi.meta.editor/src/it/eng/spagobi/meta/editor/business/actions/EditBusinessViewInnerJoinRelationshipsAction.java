/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.EditBusinessViewInnerJoinWizard;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.EditBusinessViewInnerJoinRelationshipsCommand;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class EditBusinessViewInnerJoinRelationshipsAction extends
		AbstractSpagoBIModelAction {
	
	public EditBusinessViewInnerJoinRelationshipsAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(EditBusinessViewInnerJoinRelationshipsCommand.class, workbenchPart, selection);
		
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {	
			BusinessView businessColumnSet = (BusinessView)owner;

			EditBusinessViewInnerJoinWizard wizard = new EditBusinessViewInnerJoinWizard(businessColumnSet,editingDomain, (ISpagoBIModelCommand)command);
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			dialog.create();
	    	dialog.open();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

}
