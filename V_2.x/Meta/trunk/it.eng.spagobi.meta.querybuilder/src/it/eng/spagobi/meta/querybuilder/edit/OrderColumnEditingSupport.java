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
package it.eng.spagobi.meta.querybuilder.edit;

import it.eng.qbe.query.DataMartSelectField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.Query;
import it.eng.spagobi.meta.querybuilder.model.QueryProvider;
import it.eng.spagobi.meta.querybuilder.model.SelectField;
import it.eng.spagobi.meta.querybuilder.model.SelectFieldModelProvider;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class OrderColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	/**
	 * @param viewer
	 */
	public OrderColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] order = new String[3];
		order[0] = "NONE";
		order[1] = "ASCENDING";
		order[2] = "DESCENDING";

		return new ComboBoxCellEditor(viewer.getTable(), order);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
//		SelectField selectField = (SelectField) element;
		DataMartSelectField selectField = ((DataMartSelectField) element);
		if (selectField.getOrderType().equals("NONE")) {
			return 0;
		} else if (selectField.getOrderType().equals("ASCENDING")){
			return 1;
		} else if (selectField.getOrderType().equals("DESCENDING")){
			return 2;
		} 
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
//		SelectField selectField = (SelectField) element;
		DataMartSelectField selectField = ((DataMartSelectField) element);
		if (((Integer) value) == 0) {
			selectField.setOrderType("NONE");
		} else if (((Integer) value) == 1) {
			selectField.setOrderType("ASC");
		} else if (((Integer) value) == 2) {
			selectField.setOrderType("DESC");
		} 
		
		viewer.refresh();
		
		//Update the Query object for execution
//		int selectFieldIndex = SelectFieldModelProvider.INSTANCE.getSelectFieldIndex(selectField);
//		Query query = QueryProvider.getQuery();
//		ISelectField querySelectField = query.getSelectFieldByIndex(selectFieldIndex);
//		if (querySelectField instanceof DataMartSelectField){
//			if ((selectField.getOrder()!= null) && (!selectField.getOrder().equals("NONE"))){
//				if (selectField.getOrder().equals("ASCENDING")){
//					((DataMartSelectField)querySelectField).setOrderType("ASC");
//				} else if (selectField.getOrder().equals("DESCENDING")){
//					((DataMartSelectField)querySelectField).setOrderType("DESC");
//				}
//			}	
//			System.out.println(((DataMartSelectField) querySelectField).getUniqueName()+" is order: "+((DataMartSelectField)querySelectField).getOrderType());
//		}
		//**********************

	}

}
