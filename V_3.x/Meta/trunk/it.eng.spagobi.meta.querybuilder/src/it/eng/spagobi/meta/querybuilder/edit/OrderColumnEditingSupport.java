/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.edit;

import it.eng.qbe.query.InLineCalculatedSelectField;
import it.eng.qbe.query.SimpleSelectField;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class OrderColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public OrderColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] order = new String[3];
		order[0] = "NONE";
		order[1] = "ASC";
		order[2] = "DESC";

		return new ComboBoxCellEditor(viewer.getTable(), order);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof SimpleSelectField) {
			SimpleSelectField selectField = ((SimpleSelectField) element);	
			if (selectField.getOrderType().equals("NONE")) {
				return 0;
			} else if (selectField.getOrderType().equals("ASC")){
				return 1;
			} else if (selectField.getOrderType().equals("DESC")){
				return 2;
			} 
		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);			
			if (selectField.getOrderType().equals("NONE")) {
				return 0;
			} else if (selectField.getOrderType().equals("ASC")){
				return 1;
			} else if (selectField.getOrderType().equals("DESC")){
				return 2;
			} 
		}

		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof SimpleSelectField){
			SimpleSelectField selectField = ((SimpleSelectField) element);
			if (((Integer) value) == 0) {
				selectField.setOrderType("NONE");
			} else if (((Integer) value) == 1) {
				selectField.setOrderType("ASC");
			} else if (((Integer) value) == 2) {
				selectField.setOrderType("DESC");
			} 			
		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);
			if (((Integer) value) == 0) {
				selectField.setOrderType("NONE");
			} else if (((Integer) value) == 1) {
				selectField.setOrderType("ASC");
			} else if (((Integer) value) == 2) {
				selectField.setOrderType("DESC");
			} 			
		}

		
		viewer.refresh();
		
		queryBuilder.setDirtyEditor();		
	}

}
