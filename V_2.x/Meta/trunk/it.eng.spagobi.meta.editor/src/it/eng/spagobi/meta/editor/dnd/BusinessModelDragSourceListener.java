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
		if (selection.getFirstElement() instanceof BusinessColumnSet){
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
