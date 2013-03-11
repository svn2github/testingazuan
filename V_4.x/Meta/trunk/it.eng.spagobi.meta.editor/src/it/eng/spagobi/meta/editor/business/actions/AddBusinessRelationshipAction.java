/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.AddBusinessRelationshipWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.relationship.AddBusinessRelationshipCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddBusinessRelationshipAction extends AbstractSpagoBIModelAction {
	
	private boolean isOutbound;
	
	public AddBusinessRelationshipAction(IWorkbenchPart workbenchPart, ISelection selection) {
		this(workbenchPart, selection, true);
	}
	
	public AddBusinessRelationshipAction(IWorkbenchPart workbenchPart, ISelection selection, boolean isOutbound) {
		super(AddBusinessRelationshipCommand.class, workbenchPart, selection);
		this.isOutbound = isOutbound;
	}
	
	
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			BusinessModel model;
			BusinessColumnSet sourceTable;
			BusinessColumnSet destinationTable;
			
			if(owner instanceof BusinessRootItemProvider) {
				BusinessRootItemProvider root = (BusinessRootItemProvider)owner;
				sourceTable = null;
				destinationTable = null;
				model = (BusinessModel)root.getParentObject();
			} else if(isOutbound) {
				sourceTable = (BusinessColumnSet)owner;
				destinationTable = null;
				model = sourceTable.getModel();
			} else {
				sourceTable = null;
				destinationTable = (BusinessColumnSet)owner;
				model = destinationTable.getModel();
			}
			
			AddBusinessRelationshipWizard wizard = new AddBusinessRelationshipWizard( model, sourceTable, destinationTable,  editingDomain, (ISpagoBIModelCommand)command );
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			dialog.create();
	    	dialog.open();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
}
