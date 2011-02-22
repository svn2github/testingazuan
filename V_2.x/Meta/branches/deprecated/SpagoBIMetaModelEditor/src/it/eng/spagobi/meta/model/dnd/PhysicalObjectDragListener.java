/*
 * DragSourceListener for the PhysicalModelEditor
 */
package it.eng.spagobi.meta.model.dnd;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

public class PhysicalObjectDragListener implements DragSourceListener {

	private final Viewer physicalModelTree;
	private String textToTransfer;
	

	public PhysicalObjectDragListener(Viewer viewer) {
		this.physicalModelTree = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			//data to transport via the drag
			event.data = textToTransfer;
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) physicalModelTree.getSelection();
		//if selected element is not of the appropriate type don't start the drag
		if (!checkSelectionSameType(selection)){
			event.doit = false;	
		}		
	}
	
	/**
	 * Check if the selected elements are all of the same (supported) type
	 * @return true if selected elements are all of the same (supported) type
	 */
	public boolean checkSelectionSameType(IStructuredSelection selection){
		textToTransfer = "";
		int selectionSize = selection.size();
		
		//-------------------- Single Selection
		if (selectionSize == 1){
			if (selection.getFirstElement() instanceof PhysicalTable){
				PhysicalTable physicalTable = (PhysicalTable) selection.getFirstElement();
				textToTransfer = EcoreUtil.getURI(physicalTable).toString();
				System.err.println(">> Dragged physical table:"+physicalTable);
				return true;
			}			
			else if (selection.getFirstElement() instanceof PhysicalColumn){
				PhysicalColumn physicalColumn = (PhysicalColumn) selection.getFirstElement();
				textToTransfer = EcoreUtil.getURI(physicalColumn).toString();
				System.err.println(">> Dragged physical column:"+physicalColumn);
				return true;
			}			
			else
				return false;
		}
		//-------------------- Multiple Selection
		else if (selectionSize > 1){
			
			//check if the elements are all instance of PhysicalTable
			if (selection.getFirstElement() instanceof PhysicalTable){
				Object[] selectionArray = selection.toArray();
				boolean firstElement = true;		
				for (int i=0; i < selectionSize; i++){
					if (selectionArray[i] instanceof PhysicalTable){
						PhysicalTable physicalTable = (PhysicalTable)selectionArray[i];
						System.err.println(">> Dragged physical table:"+physicalTable);
						if (firstElement){
							textToTransfer = EcoreUtil.getURI(physicalTable).toString();
							firstElement = false;
						}
						else {
							textToTransfer = textToTransfer+"$$"+EcoreUtil.getURI(physicalTable).toString();
						}
					}
					else {
						//selection of mixed types
						return false;
					}
				}
				return true;
			} 
			//check if the elements are all instance of PhysicalColumn
			else if (selection.getFirstElement() instanceof PhysicalColumn){
				Object[] selectionArray = selection.toArray();
				boolean firstElement = true;			
				for (int i=0; i < selectionSize; i++){
					if (selectionArray[i] instanceof PhysicalColumn){
						PhysicalColumn physicalColumn = (PhysicalColumn)selectionArray[i];
						System.err.println(">> Dragged physical column:"+physicalColumn);
						if (firstElement){
							textToTransfer = EcoreUtil.getURI(physicalColumn).toString();
							firstElement = false;
						}
						else {
							textToTransfer = textToTransfer+"$$"+EcoreUtil.getURI(physicalColumn).toString();
						}
					}
					else {
						//selection of mixed types
						return false;
					}
				}
				return true;				
			}
			//no supported elements selected
			return false;
		}
		//no valid selection
		return false;
	}

}
