/*
 * DragSourceListener for the TreeViewer inside the DBStructureView
 */
package it.eng.spagobi.meta.editor.dnd;

import java.util.List;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.ecore.util.EcoreUtil;
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
			//----------------- multiple selection
			//for multiple PhysicalTable drag
			if (selection.getFirstElement() instanceof PhysicalTable){
				List<PhysicalTable> selectionList = selection.toList();
				boolean firstElement = true;
				for (PhysicalTable physicalTable : selectionList){
					if (firstElement){
						textToTransfer = EcoreUtil.getURI(physicalTable).toString();
						firstElement = false;
					}
					else {
						textToTransfer = textToTransfer+"$$"+EcoreUtil.getURI(physicalTable).toString();
					}					
				}
			} 
			//for multiple PhysicalColumn drag
			else if (selection.getFirstElement() instanceof PhysicalColumn){
				List<PhysicalColumn> selectionList = selection.toList();
				boolean firstElement = true;
				for (PhysicalColumn physicalColumn : selectionList){
					if (firstElement){
						textToTransfer = EcoreUtil.getURI(physicalColumn).toString();
						firstElement = false;
					}
					else {
						textToTransfer = textToTransfer+"$$"+EcoreUtil.getURI(physicalColumn).toString();
					}					
				}
			}

		} else if (selection.size() == 1){
			//----------------- single selection
			if (selection.getFirstElement() instanceof PhysicalTable){
				PhysicalTable physicalTable = (PhysicalTable) selection.getFirstElement();
				textToTransfer = EcoreUtil.getURI(physicalTable).toString();				
			}
			if (selection.getFirstElement() instanceof PhysicalColumn){
				PhysicalColumn physicalColumn = (PhysicalColumn) selection.getFirstElement();
				textToTransfer = EcoreUtil.getURI(physicalColumn).toString();
			}
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
		if (selection.getFirstElement() instanceof PhysicalTable == false){
			if (selection.getFirstElement() instanceof PhysicalColumn == false)
				event.doit = false;	
		}
	
	}

}
