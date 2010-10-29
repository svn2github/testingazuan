/*
 * DragSourceListener for the TreeViewer inside the DBStructureView
 */
package it.eng.spagobi.meta.editor.dnd;

import java.util.List;

import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

public class PhysicalObjectDragListener implements DragSourceListener {

	private final TreeViewer physicalModelTree;
	

	public PhysicalObjectDragListener(TreeViewer viewer) {
		this.physicalModelTree = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		//Check if the selection is of the appropriate type and set the data dragged
		ITreeSelection selection = (ITreeSelection) physicalModelTree.getSelection();
		String textToTransfer = "";
		
		if (selection.size() > 1){
			//multiple selection
			List<PhysicalTable> selectionList = selection.toList();
			boolean firstElement = true;
			for (PhysicalTable physicalTable : selectionList){
				if (firstElement){
					textToTransfer = physicalTable.getName();
					firstElement = false;
				}
				else {
					textToTransfer = textToTransfer+"##"+physicalTable.getName();
				}
					
			}
		} else if (selection.size() == 1){
			//single selection
			PhysicalTable physicalTable = (PhysicalTable) selection.getFirstElement();
			textToTransfer = physicalTable.getName(); 
		}

		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			//data to transport via the drag
			event.data = textToTransfer;
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) physicalModelTree.getSelection();
		//if selected element is not of the appropriate type don't start the drag
		if (selection.getFirstElement() instanceof PhysicalTable == false)
			event.doit = false;
	}

}
