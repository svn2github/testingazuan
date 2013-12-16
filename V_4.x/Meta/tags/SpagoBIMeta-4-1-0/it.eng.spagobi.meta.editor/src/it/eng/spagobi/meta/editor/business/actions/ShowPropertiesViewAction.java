/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ShowPropertiesViewAction extends Action {
	
	IEditorPart activeEditorPart;
	
	private static IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();
	

	public ShowPropertiesViewAction() {
		super( RL.getString("business.action.show.properties.label") );
	}

	public void setActiveEditorPart(IEditorPart activeEditorPart) {
		this.activeEditorPart = activeEditorPart;
	}

	@Override
	public boolean isEnabled() {
		return activeEditorPart != null;
	}

	
	@Override
	public void run() {
		try {
			IViewReference[] views = activeEditorPart.getSite().getPage().getViewReferences();
			IViewReference view = null;
			for(int i = 0; i < views.length; i++) {
				if(views[i].getId().equalsIgnoreCase("org.eclipse.ui.views.PropertySheet")) {
					view = views[i];
					break;
				}
			}
			if(view != null) activeEditorPart.getSite().getPage().hideView(view);
			IViewPart viewp = activeEditorPart.getSite().getPage().showView("org.eclipse.ui.views.PropertySheet");
		}
		catch (PartInitException e) {
		    IStatus status = new Status(IStatus.ERROR, SpagoBIMetaEditorPlugin.PLUGIN_ID, IStatus.OK, "Impossible to open Property Sheet View", e);
		    StatusManager.getManager().handle(status, StatusManager.LOG|StatusManager.SHOW);
		}
	}
}
