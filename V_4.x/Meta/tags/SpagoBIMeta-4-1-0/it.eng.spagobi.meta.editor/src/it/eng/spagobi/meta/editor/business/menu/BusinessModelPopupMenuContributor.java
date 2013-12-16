/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.menu;

import it.eng.spagobi.meta.editor.business.actions.RefreshViewerAction;
import it.eng.spagobi.meta.editor.business.actions.ShowPropertiesViewAction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelPopupMenuContributor {

	ISelection selection;
	Collection<?> newChildDescriptors;
	Collection<?> newSiblingDescriptors;
	IEditorPart activeEditorPart;
	
	RefreshViewerAction refreshViewerAction;
	ShowPropertiesViewAction showPropertiesViewAction;
	
	public BusinessModelPopupMenuContributor() {
		refreshViewerAction = new RefreshViewerAction();
		showPropertiesViewAction = new ShowPropertiesViewAction();
	}
	
	public void menuAboutToShow(IMenuManager menuManager) {
		
		Map<String, Collection<IAction>> actions;
		MenuManager submenuManager;
		
		actions = BusinessModelMenuActionFactory.getActions(activeEditorPart, newChildDescriptors, selection);
		Iterator<String> it = actions.keySet().iterator();
		while(it.hasNext()) {
			String menuName = it.next();
			submenuManager = new MenuManager(menuName);
			populateManager(submenuManager, actions.get(menuName), null);
			menuManager.insertBefore("edit", submenuManager);
		}
		
		menuManager.insertAfter("additions-end", new Separator("ui-actions"));
		menuManager.insertAfter("ui-actions", showPropertiesViewAction);
		menuManager.insertAfter("ui-actions", refreshViewerAction);
	}
	
	public void setActiveEditor(IEditorPart activeEditorPart) {
		refreshViewerAction.setActiveEditorPart(activeEditorPart);
		showPropertiesViewAction.setActiveEditorPart(activeEditorPart);
	}
	
	public void selectionChanged(IEditorPart activeEditorPart, SelectionChangedEvent event) {
	
		this.activeEditorPart = activeEditorPart;
		selection = event.getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object object = ((IStructuredSelection)selection).getFirstElement();

			EditingDomain domain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();

			newChildDescriptors = domain.getNewChildDescriptors(object, null);
			newSiblingDescriptors = domain.getNewChildDescriptors(null, object);
		}
	}
	
	/**
	 * This populates the specified <code>manager</code> with {@link org.eclipse.jface.action.ActionContributionItem}s
	 * based on the {@link org.eclipse.jface.action.IAction}s contained in the <code>actions</code> collection,
	 * by inserting them before the specified contribution item <code>contributionID</code>.
	 * If <code>contributionID</code> is <code>null</code>, they are simply added.
	 */
	protected void populateManager(IContributionManager manager, Collection<? extends IAction> actions, String contributionID) {
		if (actions != null) {
			for (IAction action : actions) {
				if (contributionID != null) {
					manager.insertBefore(contributionID, action);
				}
				else {
					manager.add(action);
				}
			}
		}
	}
}
