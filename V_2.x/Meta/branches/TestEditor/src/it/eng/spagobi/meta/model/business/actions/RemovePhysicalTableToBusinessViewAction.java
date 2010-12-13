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

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.RemovePhysicalTableToBusinessViewCommand;
import it.eng.spagobi.meta.model.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.wizards.EditBusinessColumnsWizard;
import it.eng.spagobi.meta.model.business.wizards.RemovePhysicalTableWizard;

/**
 * @author cortella
 *
 */
public class RemovePhysicalTableToBusinessViewAction extends
AbstractSpagoBIModelAction {

	/**
	 * @param editingDomain
	 * @param command
	 */
	public RemovePhysicalTableToBusinessViewAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(RemovePhysicalTableToBusinessViewCommand.class, workbenchPart, selection);
	}
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		BusinessColumnSet businessColumnSet = (BusinessColumnSet)owner;
		RemovePhysicalTableWizard wizard = new RemovePhysicalTableWizard( businessColumnSet, editingDomain, (AbstractSpagoBIModelCommand)command );
    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
    	dialog.open();
	}


}
