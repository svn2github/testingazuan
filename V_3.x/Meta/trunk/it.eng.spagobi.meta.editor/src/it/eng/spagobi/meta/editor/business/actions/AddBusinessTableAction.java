/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.AddBusinessTableWizard;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.CreateBusinessTableCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddBusinessTableAction extends AbstractSpagoBIModelAction {
	PhysicalTable physicalTable; //used for drag&drop
	public AddBusinessTableAction(IWorkbenchPart workbenchPart, ISelection selection, PhysicalTable physicalTable) {
		super(CreateBusinessTableCommand.class, workbenchPart, selection);
		this.physicalTable = physicalTable;
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			BusinessModel businessModel = (BusinessModel)((BusinessRootItemProvider)owner).getParentObject();
			AddBusinessTableWizard wizard = new AddBusinessTableWizard(businessModel, physicalTable, editingDomain, (ISpagoBIModelCommand)command );
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			dialog.create();
	    	dialog.open();
	    	
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
}
