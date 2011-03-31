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
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ShowPropertiesViewAction extends Action {
	
	IEditorPart activeEditorPart;


	public ShowPropertiesViewAction() {
		super( SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item") );
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
			activeEditorPart.getSite().getPage().showView("org.eclipse.ui.views.PropertySheet");
		}
		catch (PartInitException exception) {
			SpagoBIMetaModelEditorPlugin.INSTANCE.log(exception);
		}
	}
}
