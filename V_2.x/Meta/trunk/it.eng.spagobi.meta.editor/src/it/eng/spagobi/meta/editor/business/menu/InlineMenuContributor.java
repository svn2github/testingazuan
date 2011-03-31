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
				}
				else {
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
				//
				IContributionItem contributionItem = items[i];
				while (contributionItem instanceof SubContributionItem) {
					contributionItem = ((SubContributionItem)contributionItem).getInnerItem();
				}

				// Delete the ActionContributionItems with matching action.
				//
				if (contributionItem instanceof ActionContributionItem) {
					IAction action = ((ActionContributionItem)contributionItem).getAction();
					if (actions.contains(action)) {
						menuManager.remove(contributionItem);
					}
				}
			}
		}
		
		// just to avoid to depopulate a manager alredy depopulated
		actions = null;
	}
}
