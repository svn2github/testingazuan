/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.dnd;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * @author cortella
 *
 */
public class BusinessModelDragSourceListener implements DragSourceListener {

	private Viewer viewer;
	private EObject model;
	public BusinessModelDragSourceListener(Viewer viewer,EObject model){
		this.viewer = viewer;
		this.model = model;
	}
	@Override
	public void dragStart(DragSourceEvent event) {
		StructuredSelection selection = (StructuredSelection)viewer.getSelection();
		if (selection.getFirstElement() instanceof BusinessColumn || selection.getFirstElement() instanceof BusinessColumnSet){
			event.doit = true;
		} else {
			event.doit = false;
		}
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		StructuredSelection selection = (StructuredSelection)viewer.getSelection();
		event.data = selection.getFirstElement();
		LocalSelectionTransfer.getTransfer().setSelection(selection);
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		//do nothing
	}

}
