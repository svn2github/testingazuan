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

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhysicalObjectDragListener implements DragSourceListener {

	private final Viewer physicalModelTree;
	private String textToTransfer;
	
	public static final String HEADER_FOR_COLUMNS_DATA = "C";
	public static final String HEADER_FOR_TABLES_DATA = "T";
	public static final String DATA_CHUNKS_SEPARATOR = "$$";
	
	
	private static Logger logger = LoggerFactory.getLogger(PhysicalObjectDragListener.class);
	

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
				textToTransfer = HEADER_FOR_TABLES_DATA + DATA_CHUNKS_SEPARATOR + EcoreUtil.getURI(physicalTable).toString();
				logger.debug("Dragged physical table [{}]", physicalTable);
				return true;
			}			
			else if (selection.getFirstElement() instanceof PhysicalColumn){
				PhysicalColumn physicalColumn = (PhysicalColumn) selection.getFirstElement();
				textToTransfer = HEADER_FOR_COLUMNS_DATA + DATA_CHUNKS_SEPARATOR + EcoreUtil.getURI(physicalColumn).toString();
				logger.debug("Dragged physical column [{}]", physicalColumn);
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
						logger.debug("Dragged physical table [{}]", physicalTable);
						if (firstElement){
							textToTransfer = HEADER_FOR_TABLES_DATA + DATA_CHUNKS_SEPARATOR + EcoreUtil.getURI(physicalTable).toString();
							firstElement = false;
						}
						else {
							textToTransfer = textToTransfer + DATA_CHUNKS_SEPARATOR + EcoreUtil.getURI(physicalTable).toString();
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
						logger.debug("Dragged physical column [{}]", physicalColumn);
						if (firstElement){
							textToTransfer = HEADER_FOR_COLUMNS_DATA + DATA_CHUNKS_SEPARATOR + EcoreUtil.getURI(physicalColumn).toString();
							firstElement = false;
						}
						else {
							textToTransfer = textToTransfer + DATA_CHUNKS_SEPARATOR + EcoreUtil.getURI(physicalColumn).toString();
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
