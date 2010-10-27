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
package it.eng.spagobi.meta.editor.actions;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.views.BusinessModelView;
import it.eng.spagobi.meta.editor.wizards.AddBusinessColumnWizard;
import it.eng.spagobi.meta.model.business.BusinessTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddBusinessColumnAction extends BusinessModelAction {

	public AddBusinessColumnAction(BusinessModelView businessModelView) {
		super(businessModelView);
		setText("Edit Business Columns");
		setToolTipText("Edit Business Columns");
		setImageDescriptor(Activator.getImageDescriptor("add.png"));
	}

	public void run() {
		AddBusinessColumnWizard wizard;
		
		BusinessTable businessTable = ((BusinessTable)getTargetObject());
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		
		//Launch wizard
		wizard = new AddBusinessColumnWizard(businessTable);
    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.create();
    	dialog.open();
	}
}
