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
