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
package it.eng.spagobi.meta.editor.business.menu;

import it.eng.spagobi.meta.editor.business.actions.AbstractSpagoBIModelAction;
import it.eng.spagobi.meta.editor.business.actions.AddBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.business.actions.AddBusinessTableAction;
import it.eng.spagobi.meta.editor.business.actions.AddEmptyBusinessTableAction;
import it.eng.spagobi.meta.editor.business.actions.AddIdentifierAction;
import it.eng.spagobi.meta.editor.business.actions.AddIncomeBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.business.actions.AddOutcomeBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.business.actions.AddPhysicalTableToBusinessTableAction;
import it.eng.spagobi.meta.editor.business.actions.AddToIdentifierAction;
import it.eng.spagobi.meta.editor.business.actions.CreateQueryAction;
import it.eng.spagobi.meta.editor.business.actions.DeleteBusinessTableAction;
import it.eng.spagobi.meta.editor.business.actions.EditBusinessColumnsAction;
import it.eng.spagobi.meta.editor.business.actions.EditBusinessViewInnerJoinRelationshipsAction;
import it.eng.spagobi.meta.editor.business.actions.GenerateJPAMappingAction;
import it.eng.spagobi.meta.editor.business.actions.RemovePhysicalTableToBusinessViewAction;
import it.eng.spagobi.meta.editor.business.actions.RemoveFromIdentifierAction;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

/**
 * This factory generate actions used in contextual menu
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class BusinessModelMenuActionFactory {
	
	
	public static Map<String, Collection<IAction>> getActions(IEditorPart activeEditorPart, Collection<?> descriptors, ISelection selection) {
		Map actions = new HashMap();
		
		if(selection.isEmpty())return actions;
		
		IStructuredSelection sselection = (IStructuredSelection) selection;
		List<?> list = sselection.toList();
		Object target = list.get(0);
		    
		
		if(target instanceof BusinessTable) {
			 List editActions = new ArrayList();
			 if (((BusinessTable)target).getPhysicalTable() != null ){
				 editActions.add(new AddIdentifierAction(activeEditorPart, selection));
				 editActions.add(new EditBusinessColumnsAction(activeEditorPart, selection));
				 editActions.add(new AddOutcomeBusinessRelationshipAction(activeEditorPart, selection));
				 editActions.add(new AddIncomeBusinessRelationshipAction(activeEditorPart, selection));	
				 //editActions.add(new AddPhysicalTableToBusinessTableAction(activeEditorPart, selection));
			 }
			 else {
				 editActions.add(new EditBusinessColumnsAction(activeEditorPart, selection));
			 }
			 actions.put("Edit", editActions);
		} else if(target instanceof BusinessView) {
			 List editActions = new ArrayList();
			 editActions.add(new AddIdentifierAction(activeEditorPart, selection));
			 editActions.add(new EditBusinessColumnsAction(activeEditorPart, selection));
			 editActions.add(new AddOutcomeBusinessRelationshipAction(activeEditorPart, selection));
			 //editActions.add(new AddIncomeBusinessRelationshipAction(activeEditorPart, selection));	
			 //editActions.add(new AddPhysicalTableToBusinessTableAction(activeEditorPart, selection));
			 //editActions.add(new RemovePhysicalTableToBusinessViewAction(activeEditorPart, selection));
			 editActions.add(new EditBusinessViewInnerJoinRelationshipsAction(activeEditorPart, selection));
			 actions.put("Edit", editActions);
			 
//			 List removeActions = new ArrayList();
//			 removeActions.add(new RemovePhysicalTableToBusinessViewAction(activeEditorPart, selection));
//			 actions.put("Remove", removeActions);
		} else if(target instanceof BusinessColumn){
			List editActions = new ArrayList();
			List removeActions = new ArrayList();
			BusinessColumn businessColumn = (BusinessColumn)target;
			if ((businessColumn.isIdentifier()) || (businessColumn.isPartOfCompositeIdentifier())){
				RemoveFromIdentifierAction removeFromIdentifierAction = new RemoveFromIdentifierAction(activeEditorPart, selection);
				Command command = removeFromIdentifierAction.getPerformFinishCommand();
				//Check if Command is executable
				if (command instanceof ISpagoBIModelCommand){
					removeActions.add(removeFromIdentifierAction);
				}
				actions.put("Edit", removeActions);
			} else {
				AddToIdentifierAction addToIdentifierAction = new AddToIdentifierAction(activeEditorPart, selection);
				Command command = addToIdentifierAction.getPerformFinishCommand();
				//Check if Command is executable
				if (command instanceof ISpagoBIModelCommand){
					editActions.add(addToIdentifierAction);
				}
				actions.put("Edit", editActions);
			}
	    } else if(target instanceof BusinessRootItemProvider) {
	    	List editActions = new ArrayList();
	    	editActions.add(new AddBusinessTableAction(activeEditorPart, selection, null));
	    	editActions.add(new AddEmptyBusinessTableAction(activeEditorPart, selection));
	    	editActions.add(new AddBusinessRelationshipAction(activeEditorPart, selection));
	    	actions.put("Edit", editActions);
	    	
	    	List generateActions = new ArrayList();
	    	generateActions.add(new CreateQueryAction(activeEditorPart, selection));
	    	generateActions.add(new GenerateJPAMappingAction(activeEditorPart, selection));
	    	actions.put("Create", generateActions);
	    	
//	    	List queryActions = new ArrayList();
//	    	queryActions.add(new CreateQueryAction(activeEditorPart, selection));
//	    	actions.put("Query", queryActions);
	    } 
		
		return actions;
	}
}
