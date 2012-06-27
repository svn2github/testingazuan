/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.menu;

import java.util.Collection;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class InlineMenuContributor {
	protected Collection<IAction> actions;
	
	public Collection<IAction> getActions() {
		return actions;
	}

	protected IMenuManager menuManager;
	
	public InlineMenuContributor(IMenuManager parentMenuManager, String subMenuLabel) {
		// Prepare for Generate item menu
		menuManager = new MenuManager(subMenuLabel);
		parentMenuManager.insertBefore("additions", menuManager);
	}
	
	public void setActions(Collection<IAction> newActions) {
		if (menuManager != null) {
			depopulateManager();
		}
		
		actions = newActions;
		
		if (menuManager != null) {
			populateManager();
			menuManager.update(true);
		}

	}
	
	protected void populateManager() {
		populateManager(null);
	}
	
	protected void populateManager(String contributionID) {
		if (actions != null) {
			for (IAction action : actions) {
				if (contributionID != null) {
					menuManager.insertBefore(contributionID, action);
				} else {
					menuManager.add(action);
				}
			}
		}
	}
	
	protected void depopulateManager() {
		
		if (actions != null) {
			IContributionItem[] items = menuManager.getItems();
			for (int i = 0; i < items.length; i++) {
				
				// Look into SubContributionItems
				IContributionItem contributionItem = items[i];
				while (contributionItem instanceof SubContributionItem) {
					contributionItem = ((SubContributionItem)contributionItem).getInnerItem();
				}

				// Delete the ActionContributionItems with matching action.
				if (contributionItem instanceof ActionContributionItem) {
					IAction action = ((ActionContributionItem)contributionItem).getAction();
					if (actions.contains(action)) {
						menuManager.remove(contributionItem);
					}
				}
			}
		}
		
		// just to avoid to de-populate a manager already de-populated
		actions = null;
	}
}
