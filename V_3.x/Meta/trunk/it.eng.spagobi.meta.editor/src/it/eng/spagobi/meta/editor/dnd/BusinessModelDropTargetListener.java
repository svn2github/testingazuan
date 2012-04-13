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

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;


/**
 * @author cortella
 *
 */
public class BusinessModelDropTargetListener extends ViewerDropAdapter {

	BusinessModelDropFromBusinessModelHandler bmHandler;
	BusinessModelDropFromPhysicalModelHandler pmHandler;
		
	public BusinessModelDropTargetListener(Viewer viewer, EObject model, AdapterFactoryEditingDomain editingDomain) {
		super(viewer);
			
		this.setScrollEnabled(true);
		this.setExpandEnabled(true);
		this.setSelectionFeedbackEnabled(true);
		this.setFeedbackEnabled(true);
		bmHandler = new BusinessModelDropFromBusinessModelHandler(model, editingDomain);
		pmHandler = new BusinessModelDropFromPhysicalModelHandler(model, editingDomain);

	}

	@Override
	public boolean performDrop(Object data) {
		boolean isLocationBeoforeOrAfterTarget = getCurrentLocation() == LOCATION_BEFORE || getCurrentLocation() == LOCATION_AFTER;
		
		Class dataType = getDataType();
		
		if (getDragSource() == DragSource.BUSINESS_MODEL_EDITOR){
			bmHandler.setNextTo(getCurrentTarget());
			return bmHandler.performDrop(data, getCurrentTarget(), isLocationBeoforeOrAfterTarget);
			//return performBusinessModelObjectDrop(data,currentTarget);
		} else if (getDragSource() == DragSource.PHYSICAL_MODEL_EDITOR){
			String strData = (String)data;
			strData = strData.substring(3);

			return pmHandler.performDrop(strData, getCurrentTarget(), isLocationBeoforeOrAfterTarget);
			//return performPhysicalModelObjectDrop(data, currentTarget, currentLocation, initializer);
		} else {
			return false;
		}

	}
	
	public enum DragSource{UNKNOWN_SOURCE, PHYSICAL_MODEL_EDITOR, BUSINESS_MODEL_EDITOR};
	
	public DragSource getDragSource() {
		DragSource source = DragSource.UNKNOWN_SOURCE;
		if(this.getCurrentEvent().data instanceof TreeSelection) {
			source = DragSource.BUSINESS_MODEL_EDITOR;
		} else {
			source = DragSource.PHYSICAL_MODEL_EDITOR;
		}
		return source;
	}
	
	
	public Class getDataType() {
		Class c = null;
		
		if (getDragSource() == DragSource.BUSINESS_MODEL_EDITOR){
			TreeSelection selection = (TreeSelection)getCurrentEvent().data;
			c = selection.getFirstElement().getClass();
		} else if (getDragSource() == DragSource.PHYSICAL_MODEL_EDITOR){
			String text = (String)getCurrentEvent().data;
			if(text.startsWith(PhysicalObjectDragListener.HEADER_FOR_COLUMNS_DATA)) {
				c = PhysicalColumn.class;
			} else if(text.startsWith(PhysicalObjectDragListener.HEADER_FOR_TABLES_DATA)) {
				c = PhysicalTable.class;
			}
		} 
		
		return c;
	}
	
	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		boolean isLocationBeoforeOrAfterTarget = getCurrentLocation() == LOCATION_BEFORE || getCurrentLocation() == LOCATION_AFTER;
		
		/*
		boolean isSupportedType = TextTransfer.getInstance().isSupportedType(transferType) || LocalSelectionTransfer.getTransfer().isSupportedType(transferType);
		if(isSupportedType == false) return false;
	
		Class dataType = getDataType();
		
		
		if (getDragSource() == DragSource.BUSINESS_MODEL_EDITOR){
			bmHandler.setNextTo(nextTo);
			return bmHandler.validateDrop(dataType, target, isLocationBeoforeOrAfterTarget);
		} else if (getDragSource() == DragSource.PHYSICAL_MODEL_EDITOR){
			return pmHandler.validateDrop(dataType, target, isLocationBeoforeOrAfterTarget);
		} else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}
		*/
		
		if((target instanceof BusinessColumnSet)) {
			LocalSelectionTransfer.getTransfer();
		}
		
		if ( (target instanceof BusinessRootItemProvider) || ( (target instanceof BusinessColumnSet) && isLocationBeoforeOrAfterTarget) ) {
			return TextTransfer.getInstance().isSupportedType(transferType)|| LocalSelectionTransfer.getTransfer().isSupportedType(transferType);
		}
		else if ( (target instanceof BusinessColumnSet) || ( (target instanceof BusinessColumn) && isLocationBeoforeOrAfterTarget) ){
			return TextTransfer.getInstance().isSupportedType(transferType) || LocalSelectionTransfer.getTransfer().isSupportedType(transferType);
		}
		else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}	
		
	}
}
