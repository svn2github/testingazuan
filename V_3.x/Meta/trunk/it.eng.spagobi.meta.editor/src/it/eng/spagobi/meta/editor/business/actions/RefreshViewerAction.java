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

import java.util.Collection;

import it.eng.spagobi.meta.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.phantom.provider.BusinessColumnFolderItemProvider;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class RefreshViewerAction extends Action {
	
	IEditorPart activeEditorPart;


	public RefreshViewerAction() {
		super( SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_RefreshViewer_menu_item") );
	}

	public void setActiveEditorPart(IEditorPart activeEditorPart) {
		this.activeEditorPart = activeEditorPart;
	}
	
	@Override
	public boolean isEnabled() {
		return activeEditorPart instanceof IViewerProvider;
	}

	@Override
	public void run() {
		if (activeEditorPart instanceof IViewerProvider) {
			Viewer viewer = ((IViewerProvider)activeEditorPart).getViewer();
			if (viewer != null) {
				if(viewer instanceof TreeViewer) {
					TreeViewer treeViewer = (TreeViewer)viewer;
									
					Object[] elements = treeViewer.getExpandedElements();
//					for(int i = 0; i < elements.length; i++) {
//						treeViewer.refresh(elements[i]);
//						if(elements[i] instanceof BusinessColumnFolderItemProvider) {
//							BusinessColumnFolderItemProvider bcfip = (BusinessColumnFolderItemProvider)elements[i];
//							treeViewer.setExpandedState(elements[i], true);							
//						} else {
//							treeViewer.setExpandedState(elements[i], true);
//						}
//					}
//					
					treeViewer.refresh();
					treeViewer.setExpandedElements(elements);
				} else {
					viewer.refresh();
				}
			}
		}
	}
}
