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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.views.BusinessModelView;
import it.eng.spagobi.meta.editor.wizards.AddBusinessColumnWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AddBusinessColumnToBusinessIdentifierAction extends BusinessModelAction {

	public AddBusinessColumnToBusinessIdentifierAction(BusinessModelView businessModelView) {
		super(businessModelView);
		setText("Add column to table identifier");
		setToolTipText("Add column to table identifier");
		setImageDescriptor(Activator.getImageDescriptor("key.png"));
	}

	public void run() {
		BusinessColumn businessColumn = (BusinessColumn)getTargetObject();
		BusinessColumnSet businessTable = businessColumn.getTable();
		BusinessIdentifier businessIdentifier = businessTable.getIdentifier();
		if (businessIdentifier != null){
			businessIdentifier.getColumns().add(businessColumn);
		} else {
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			List<BusinessColumn> businessColumns = new ArrayList<BusinessColumn>();
			businessColumns.add(businessColumn);
			initializer.addIdentifier("BI_" + businessTable.getName(), businessTable, businessColumns);
		}
		getBusinessModelView().getBusinessModelTreeViewer().update(businessColumn, null);
		MessageDialog.openInformation(
				null, 
				"Business Model Editor", 
				"Column setted as Business Identifier"
		);
	}
}
