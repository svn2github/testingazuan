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
package it.eng.spagobi.meta.editor.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.actions.AddBusinessColumnAction;
import it.eng.spagobi.meta.editor.actions.AddBusinessColumnToBusinessIdentifierAction;
import it.eng.spagobi.meta.editor.actions.AddBusinessIdentifierAction;
import it.eng.spagobi.meta.editor.actions.AddBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.actions.AddBusinessTableAction;
import it.eng.spagobi.meta.editor.actions.RemoveBusinessColumnAction;
import it.eng.spagobi.meta.editor.actions.RemoveBusinessIdentifierAction;
import it.eng.spagobi.meta.editor.actions.RemoveBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.actions.RemoveBusinessTableAction;
import it.eng.spagobi.meta.editor.wizards.AddBusinessColumnWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessIdentifierWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessRelationshipWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelPopUpMenuListener implements IMenuListener{

	BusinessModelView businessModelView;
	
	private Action ADD_BUSINESS_TABLE_ACTION;
	private Action REMOVE_BUSINESS_TABLE_ACTION;
	private Action ADD_BUSINESS_COLUMN_ACTION;
	private Action REMOVE_BUSINESS_COLUMN_ACTION;
	private Action ADD_BUSINESS_IDENTIFIER_ACTION;
	
	private Action REMOVE_BUSINESS_IDENTIFIER_ACTION;
	private Action ADD_BUSINESS_RELATIONSHIP_ACTION;
	private Action REMOVE_BUSINESS_RELATIONSHIP_ACTION;
	private Action ADD_BUSINESS_COLUMN_TO_BUSINESS_IDENTIFIER_ACTION;
	
	BusinessModelPopUpMenuListener(BusinessModelView businessModelView) {
		this.businessModelView = businessModelView;
		
		ADD_BUSINESS_TABLE_ACTION = new AddBusinessTableAction( businessModelView );
		REMOVE_BUSINESS_TABLE_ACTION = new RemoveBusinessTableAction( businessModelView );
		
		ADD_BUSINESS_COLUMN_ACTION = new AddBusinessColumnAction( businessModelView );
		REMOVE_BUSINESS_COLUMN_ACTION = new RemoveBusinessColumnAction( businessModelView );
		ADD_BUSINESS_COLUMN_TO_BUSINESS_IDENTIFIER_ACTION = new AddBusinessColumnToBusinessIdentifierAction( businessModelView );
		
		ADD_BUSINESS_IDENTIFIER_ACTION = new AddBusinessIdentifierAction( businessModelView );
		REMOVE_BUSINESS_IDENTIFIER_ACTION = new RemoveBusinessIdentifierAction( businessModelView );
		
		ADD_BUSINESS_RELATIONSHIP_ACTION = new AddBusinessRelationshipAction( businessModelView );
		REMOVE_BUSINESS_RELATIONSHIP_ACTION = new RemoveBusinessRelationshipAction( businessModelView );
	}
	
	
	public void menuAboutToShow(IMenuManager manager) {
		//create context menu based on the current tree selection
		Object currentTreeSelection = businessModelView.getCurrentTreeSelection();
		if (currentTreeSelection instanceof BusinessRootItemProvider){
			manager.removeAll();
			manager.add(ADD_BUSINESS_TABLE_ACTION);
			//manager.add(ADD_BUSINESS_IDENTIFIER_ACTION);
			manager.add(ADD_BUSINESS_RELATIONSHIP_ACTION);
		} else if (currentTreeSelection instanceof BusinessTable){
			manager.removeAll();
			manager.add(REMOVE_BUSINESS_TABLE_ACTION);
			manager.add(ADD_BUSINESS_COLUMN_ACTION);
			manager.add(ADD_BUSINESS_IDENTIFIER_ACTION);
			manager.add(ADD_BUSINESS_RELATIONSHIP_ACTION);
		}  else if (currentTreeSelection instanceof BusinessColumn){
			BusinessColumn businessColumn = (BusinessColumn)currentTreeSelection;
			BusinessIdentifier businessIdentifier = businessColumn.getTable().getIdentifier();
			manager.removeAll();
			if ( (businessIdentifier == null) || (!businessIdentifier.getColumns().contains(businessColumn)) ){
				manager.add(ADD_BUSINESS_COLUMN_TO_BUSINESS_IDENTIFIER_ACTION);
			}
			manager.add(REMOVE_BUSINESS_COLUMN_ACTION);
			if(((BusinessColumn)currentTreeSelection).isIdentifier()){
				manager.add(REMOVE_BUSINESS_IDENTIFIER_ACTION);
			}
		} else if (currentTreeSelection instanceof BusinessIdentifier){
			manager.removeAll();
			manager.add(REMOVE_BUSINESS_IDENTIFIER_ACTION);
		} else if (currentTreeSelection instanceof BusinessRelationship){
			manager.removeAll();
			manager.add(REMOVE_BUSINESS_RELATIONSHIP_ACTION);
		}				
	}

}
