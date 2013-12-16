/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.AddCalculatedFieldWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.AddCalculatedFieldCommand;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class AddCalculatedFieldAction extends AbstractSpagoBIModelAction {

	Object target;
	public AddCalculatedFieldAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(AddCalculatedFieldCommand.class, workbenchPart, selection);
		
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		BusinessColumnSet businessColumnSet;
		AddCalculatedFieldWizard wizard;
		CalculatedBusinessColumn calculatedBusinessColumn;
		BusinessModel businessModel;

		if (owner instanceof CalculatedBusinessColumn){
			calculatedBusinessColumn = (CalculatedBusinessColumn)owner;
			businessColumnSet = calculatedBusinessColumn.getTable();
			businessModel = businessColumnSet.getModel();

		} else  {
			businessColumnSet = (BusinessColumnSet)owner;
			businessModel = businessColumnSet.getModel();
		}
			

		if (owner instanceof CalculatedBusinessColumn){
			wizard = new AddCalculatedFieldWizard( businessModel, businessColumnSet, editingDomain, (ISpagoBIModelCommand)command, (CalculatedBusinessColumn)owner );
		} else{
			wizard = new AddCalculatedFieldWizard( businessModel, businessColumnSet, editingDomain, (ISpagoBIModelCommand)command, null );
		}
			
    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
    	dialog.open();
	}
}
