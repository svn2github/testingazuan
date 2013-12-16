/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business;

import it.eng.spagobi.meta.editor.business.actions.DeleteModelObjectAction;
import it.eng.spagobi.meta.editor.business.menu.BusinessModelMenuBarContributor;
import it.eng.spagobi.meta.editor.business.menu.BusinessModelPopupMenuContributor;

import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;

/**
 * This is the action bar contributor for the BusinessModel model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BusinessModelEditorWorkbenchContributor
	extends EditingDomainActionBarContributor
	implements ISelectionChangedListener {
	
	/**
	 * This keeps track of the active editor.
	 * @generated
	 */
	protected IEditorPart activeEditorPart;

	/**
	 * This keeps track of the current selection provider.
	 * @generated
	 */
	protected ISelectionProvider selectionProvider;

	// added
	protected BusinessModelMenuBarContributor toolbarMenu;
	protected BusinessModelPopupMenuContributor popupMenu;
	
	
	/**
	 * This creates an instance of the contributor.
	 */
	public BusinessModelEditorWorkbenchContributor() {
		super(ADDITIONS_LAST_STYLE);
	}

	/**
	 * This adds Separators for editor additions to the tool bar.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(new Separator("businessmodel-settings"));
		toolBarManager.add(new Separator("businessmodel-additions"));
	}

	/**
	 * This adds to the menu bar a menu and some separators for editor additions,
	 * as well as the sub-menus for object creation items.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);		
		toolbarMenu = new BusinessModelMenuBarContributor(menuManager, true, "additions");
		popupMenu = new BusinessModelPopupMenuContributor();
	}
	


	/**
	 * When the active editor changes, this remembers the change and registers with it as a selection provider.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		activeEditorPart = part;

		// Switch to the new selection provider.
		//
		if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(this);
		}
		if (part == null) {
			selectionProvider = null;
		}
		else {
			selectionProvider = part.getSite().getSelectionProvider();
			selectionProvider.addSelectionChangedListener(this);

			// Fake a selection changed event to update the menus.
			//
			if (selectionProvider.getSelection() != null) {
				selectionChanged(new SelectionChangedEvent(selectionProvider, selectionProvider.getSelection()));
			}
		}
		
		this.toolbarMenu.setActiveEditor(activeEditorPart);
		this.popupMenu.setActiveEditor(activeEditorPart);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionChangedListener},
	 * handling {@link org.eclipse.jface.viewers.SelectionChangedEvent}s by querying for the children and siblings
	 * that can be added to the selected object and updating the menus accordingly.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		toolbarMenu.selectionChanged(activeEditorPart, event);
		popupMenu.selectionChanged(activeEditorPart, event);
	}
	
	
	
	@Override
	protected DeleteAction createDeleteAction() {
	    return new DeleteModelObjectAction(removeAllReferencesOnDelete());
	}
	
	
	/**
	 * This populates the pop-up menu before it appears.
	 */
	@Override
	public void menuAboutToShow(IMenuManager menuManager) {
		cutAction.setEnabled(false);
		copyAction.setEnabled(false);
		pasteAction.setEnabled(false);
		//deleteAction.setEnabled(false);
		
		super.menuAboutToShow(menuManager);
		popupMenu.menuAboutToShow(menuManager);
		
	}

	@Override
	protected boolean removeAllReferencesOnDelete() {
		return true;
	}

}