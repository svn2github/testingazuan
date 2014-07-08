/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.dnd;


import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author cortella
 *
 */
public class CalculatedFieldDragSourceListener implements DragSourceListener {

	private Tree viewer;
	private TreeItem dragSourceItem;
	public CalculatedFieldDragSourceListener(Tree viewer){
		this.viewer = viewer;
	}
	@Override
	public void dragStart(DragSourceEvent event) {
		TreeItem[] selection = viewer.getSelection();
        if (selection.length > 0 && selection[0].getItemCount() == 0) {
        	dragSourceItem = selection[0];
        	event.doit = true;
		} else {
			event.doit = false;
		}
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = dragSourceItem.getText();
		//LocalSelectionTransfer.getTransfer().setSelection(selection);
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		//do nothing
	}

}
