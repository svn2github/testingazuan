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
package it.eng.spagobi.meta.model.business.actions;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.AddBusinessRelationshipCommand;
import it.eng.spagobi.meta.model.business.wizards.AddBusinessRelationshipWizard;
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
			
			AddBusinessRelationshipWizard wizard = new AddBusinessRelationshipWizard( model, sourceTable, destinationTable,  editingDomain, (AbstractSpagoBIModelCommand)command );
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			dialog.create();
	    	dialog.open();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
}
