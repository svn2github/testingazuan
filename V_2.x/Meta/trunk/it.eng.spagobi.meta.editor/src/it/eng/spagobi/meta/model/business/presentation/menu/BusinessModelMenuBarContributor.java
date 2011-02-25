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
package it.eng.spagobi.meta.model.business.presentation.menu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;

import it.eng.spagobi.meta.model.business.actions.RefreshViewerAction;
import it.eng.spagobi.meta.model.business.actions.ShowPropertiesViewAction;
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelMenuBarContributor {
	
	protected IMenuManager toolbarMainMenuManager;
	protected Map<String, InlineMenuContributor> toolbarSubMenuManagers;
	
	protected RefreshViewerAction refreshViewerAction;
	protected ShowPropertiesViewAction showPropertiesViewAction;
	
	public Map<String, InlineMenuContributor> getSubmenuManagers() {
		return toolbarSubMenuManagers;
	}

	public BusinessModelMenuBarContributor(IMenuManager parentMenuManager, boolean insertAfter, String anchorMenuItemId) {
		
		toolbarMainMenuManager = new MenuManager(SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_BusinessModelEditor_menu"), "it.eng.spagobi.meta.model.businessMenuID");
		toolbarSubMenuManagers = new HashMap();
		
		refreshViewerAction = new RefreshViewerAction();
		showPropertiesViewAction = new ShowPropertiesViewAction();
		
		// add the custom toolbar menu in the right place
		if(insertAfter == true) {
			parentMenuManager.insertAfter(anchorMenuItemId, toolbarMainMenuManager);
		} else {
			parentMenuManager.insertBefore(anchorMenuItemId, toolbarMainMenuManager);
		}
		
		// divide the custom menu into parts
		toolbarMainMenuManager.add(new Separator("settings"));
		toolbarMainMenuManager.add(new Separator("actions"));
		toolbarMainMenuManager.add(new Separator("additions"));
		toolbarMainMenuManager.add(new Separator("additions-end"));
		
		// Force an update because Eclipse hides empty menus now.
		toolbarMainMenuManager.addMenuListener
			(new IMenuListener() {
				 public void menuAboutToShow(IMenuManager menuManager) {
					 menuManager.updateAll(true);
				 }
			 });
		
		// these actions are not sensitive to selection
		addGlobalActions();
	
	}
	
	protected void addGlobalActions() {
		toolbarMainMenuManager.insertAfter("additions-end", new Separator("ui-actions"));
		toolbarMainMenuManager.insertAfter("ui-actions", showPropertiesViewAction);

		refreshViewerAction.setEnabled(refreshViewerAction.isEnabled());		
		toolbarMainMenuManager.insertAfter("ui-actions", refreshViewerAction);
	}
	
	public void setActiveEditor(IEditorPart activeEditorPart) {
		refreshViewerAction.setActiveEditorPart(activeEditorPart);
		showPropertiesViewAction.setActiveEditorPart(activeEditorPart);
	}
	
	public void selectionChanged(IEditorPart activeEditorPart, SelectionChangedEvent event) {
		
		Collection<?> newChildDescriptors = null;
		Collection<?> newSiblingDescriptors = null;
		
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object object = ((IStructuredSelection)selection).getFirstElement();

			EditingDomain domain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();

			newChildDescriptors = domain.getNewChildDescriptors(object, null);
			newSiblingDescriptors = domain.getNewChildDescriptors(null, object);
		}
		
		depopulateAllSubMenus();
		
		Map<String, Collection<IAction>> actions = BusinessModelMenuActionFactory.getActions(activeEditorPart, newChildDescriptors, selection);
		
		Iterator<String> it = actions.keySet().iterator();
		while(it.hasNext()) {
			String submenuName = it.next();
			InlineMenuContributor contextualMenuManager = toolbarSubMenuManagers.get(submenuName);
			if(contextualMenuManager == null) {
				contextualMenuManager = new InlineMenuContributor(toolbarMainMenuManager, submenuName);
				toolbarSubMenuManagers.put(submenuName, contextualMenuManager);
			}
			
			contextualMenuManager.setActions(actions.get(submenuName));
		}
	}
	
	protected void depopulateAllSubMenus() {
		Iterator<String> it = toolbarSubMenuManagers.keySet().iterator();
		while(it.hasNext()) {
			String submenuName = it.next();
			InlineMenuContributor contextualMenuManager = toolbarSubMenuManagers.get(submenuName);
			contextualMenuManager.depopulateManager();
		}
		
	}
}
