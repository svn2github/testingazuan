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

	private static IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();
	

	public RefreshViewerAction() {
		super( RL.getString("business.action.refresh.label") );
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
