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

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.views.BusinessModelView;
import it.eng.spagobi.meta.editor.wizards.AddBusinessColumnWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class RemoveBusinessIdentifierAction extends BusinessModelAction {

	public RemoveBusinessIdentifierAction(BusinessModelView businessModelView) {
		super(businessModelView);
		setText("Remove Business Relationship");
		setToolTipText("Remove Business Relationship");
		setImageDescriptor(Activator.getImageDescriptor("remove.png"));
	}

	public void run() {
		if (getTargetObject() instanceof BusinessColumn){
			BusinessColumn businessColumn = ((BusinessColumn)getTargetObject());
			BusinessTable businessTable = businessColumn.getTable();
			BusinessIdentifier businessIdentifier = businessTable.getModel().getIdentifier(businessTable);
			if ( businessIdentifier.getColumns().size() > 0){
				//remove only the column
				businessIdentifier.getColumns().remove(businessColumn);		
			}
			if ( businessIdentifier.getColumns().size() == 0){
				//remove the empty businessidentifier
				businessTable.getModel().getIdentifiers().remove(businessIdentifier);
			}
			getBusinessModelView().getBusinessModelTreeViewer().update(businessColumn, null);
			MessageDialog.openInformation(
					null, 
					"Business Model Editor", 
					"Business identifier removed"
			);
			
		} else if (getTargetObject() instanceof BusinessIdentifier){
			((BusinessIdentifier)getTargetObject()).getModel().getIdentifiers().remove(getTargetObject());
			MessageDialog.openInformation(
					null, 
					"Business Model Editor", 
					"Business identifier removed"
			);
		}

	}
}

