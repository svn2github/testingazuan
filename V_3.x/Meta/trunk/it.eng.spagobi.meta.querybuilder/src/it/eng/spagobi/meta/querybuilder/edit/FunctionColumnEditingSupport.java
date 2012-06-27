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
import it.eng.spagobi.tools.dataset.common.query.AggregationFunctions;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class FunctionColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public FunctionColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] functions = new String[7];
		functions[0] = "NONE";
		functions[1] = "SUM";
		functions[2] = "AVG";
		functions[3] = "MAX";
		functions[4] = "MIN";
		functions[5] = "COUNT";
		functions[6] = "COUNT_DISTINCT";
		
		return new ComboBoxCellEditor(viewer.getTable(), functions);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
//		SelectField selectField = (SelectField) element;
		String function = null;
		if (element instanceof SimpleSelectField){
			SimpleSelectField selectField = ((SimpleSelectField) element);		
			function = selectField.getFunction().getName();

		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);		
			function = selectField.getFunction().getName();
		}
		if (function.equals("NONE")) {
			return 0;
		} else if (function.equals("SUM")){
			return 1;
		} else if (function.equals("AVG")){
			return 2;
		} else if (function.equals("MAX")){
			return 3;
		} else if (function.equals("MIN")){
			return 4;
		} else if (function.equals("COUNT")){
			return 5;
		} else if (function.equals("COUNT_DISTINCT")){
			return 6;
		}
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
//		SelectField selectField = (SelectField) element;
		if (element instanceof SimpleSelectField){
			SimpleSelectField selectField = ((SimpleSelectField) element);
			if (((Integer) value) == 0) {
				selectField.setFunction(AggregationFunctions.NONE_FUNCTION);
			} else if (((Integer) value) == 1) {
				selectField.setFunction(AggregationFunctions.SUM_FUNCTION);
			} else if (((Integer) value) == 2) {
				selectField.setFunction(AggregationFunctions.AVG_FUNCTION);
			} else if (((Integer) value) == 3) {
				selectField.setFunction(AggregationFunctions.MAX_FUNCTION);
			} else if (((Integer) value) == 4) {
				selectField.setFunction(AggregationFunctions.MIN_FUNCTION);
			} else if (((Integer) value) == 5) {
				selectField.setFunction(AggregationFunctions.COUNT_FUNCTION);
			} else if (((Integer) value) == 6) {
				selectField.setFunction(AggregationFunctions.COUNT_DISTINCT_FUNCTION);
			}
		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);	
			if (((Integer) value) == 0) {
				selectField.setFunction(AggregationFunctions.NONE_FUNCTION);
			} else if (((Integer) value) == 1) {
				selectField.setFunction(AggregationFunctions.SUM_FUNCTION);
			} else if (((Integer) value) == 2) {
				selectField.setFunction(AggregationFunctions.AVG_FUNCTION);
			} else if (((Integer) value) == 3) {
				selectField.setFunction(AggregationFunctions.MAX_FUNCTION);
			} else if (((Integer) value) == 4) {
				selectField.setFunction(AggregationFunctions.MIN_FUNCTION);
			} else if (((Integer) value) == 5) {
				selectField.setFunction(AggregationFunctions.COUNT_FUNCTION);
			} else if (((Integer) value) == 6) {
				selectField.setFunction(AggregationFunctions.COUNT_DISTINCT_FUNCTION);
			}
		} 

		viewer.refresh();
		
		queryBuilder.setDirtyEditor();
	}

}
