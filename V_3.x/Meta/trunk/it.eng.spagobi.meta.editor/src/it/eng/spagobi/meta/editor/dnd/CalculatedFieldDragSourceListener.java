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
