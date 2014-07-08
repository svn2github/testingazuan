/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.NewQueryFileWizard;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.generate.CreateQueryCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author cortella
 *
 */
public class CreateQueryAction extends AbstractSpagoBIModelAction {

	/**
	 * @param commandClass
	 * @param workbenchPart
	 * @param selection
	 */
	public CreateQueryAction(IWorkbenchPart workbenchPart,
			ISelection selection) {
		super(CreateQueryCommand.class, workbenchPart, selection);
	
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			BusinessModel businessModel = (BusinessModel)((BusinessRootItemProvider)owner).getParentObject();
			//CreateQueryWizard wizard = new CreateQueryWizard(businessModel, editingDomain, (AbstractSpagoBIModelCommand)command );
	    	NewQueryFileWizard wizard = new NewQueryFileWizard(businessModel, editingDomain, (ISpagoBIModelCommand)command );
	    	wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
	    	WizardDialog dialog = new WizardDialog(wizard.getShell(), wizard);
			dialog.create();
	    	dialog.open();

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

}
