/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.dnd;



import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * @author cortella
 *
 */
public class QueryBuilderDragListener extends DragSourceAdapter  {
	private Viewer viewer;
	
	public QueryBuilderDragListener(Viewer viewer){
		this.viewer = viewer;
	}
	
	public void dragStart(DragSourceEvent event) {
		StructuredSelection selection = (StructuredSelection)viewer.getSelection();
		if (selection.getFirstElement()!=null){
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

}
