/*
 * DragSourceListener for the TreeViewer inside the DBStructureView
 */
package it.eng.spagobi.meta.editor.dnd;

import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

public class PhysicalTableDragListener implements DragSourceListener {

	private final TreeViewer connTree;

	public PhysicalTableDragListener(TreeViewer viewer) {
		this.connTree = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		//Check if the selection is of the appropriate type and set the data dragged
		IStructuredSelection selection = (IStructuredSelection) connTree.getSelection();
		PhysicalTable firstElement = (PhysicalTable) selection.getFirstElement();
		
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			//data to transport via the drag
			event.data = firstElement.getName(); 
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) connTree.getSelection();
		//if selected element is not of the appropriate type don't start the drag
		if (selection.getFirstElement() instanceof PhysicalTable == false)
			event.doit = false;
	}

}
