/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.edit;

import it.eng.qbe.query.HavingField;
import it.eng.qbe.query.HavingField.Operand;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.tools.dataset.common.query.AggregationFunctions;
import it.eng.spagobi.tools.dataset.common.query.IAggregationFunction;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class HavingLeftFunctionColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public HavingLeftFunctionColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] functions = new String[7];
		functions[0] = "NONE";
		functions[1] = "SUM";
		functions[2] = "AVERAGE";
		functions[3] = "MAXIMUM";
		functions[4] = "MINIMUM";
		functions[5] = "COUNT";
		functions[6] = "COUNT DISTINCT";
		
		return new ComboBoxCellEditor(viewer.getTable(), functions);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		HavingField havingClause = (HavingField) element;
		if(havingClause.getLeftOperand()==null || havingClause.getLeftOperand().function==null){
			return 0;
		}
		IAggregationFunction function = havingClause.getLeftOperand().function;
		if (function.equals("NONE")) {
			return 0;
		} else if (function.equals("SUM")){
			return 1;
		} else if (function.equals("AVERAGE")){
			return 2;
		} else if (function.equals("MAXIMUM")){
			return 3;
		} else if (function.equals("MINIMUM")){
			return 4;
		} else if (function.equals("COUNT")){
			return 5;
		} else if (function.equals("COUNT DISTINCT")){
			return 6;
		}
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
		HavingField havingClause = (HavingField) element;
		if(havingClause.getLeftOperand()!=null){
			Operand operand = havingClause.getLeftOperand();
			if (((Integer) value) == 0) {
				operand.function = AggregationFunctions.NONE_FUNCTION;
			} else if (((Integer) value) == 1) {
				operand.function = AggregationFunctions.SUM_FUNCTION;
			} else if (((Integer) value) == 2) {
				operand.function = AggregationFunctions.AVG_FUNCTION;
			} else if (((Integer) value) == 3) {
				operand.function = AggregationFunctions.MAX_FUNCTION;
			} else if (((Integer) value) == 4) {
				operand.function = AggregationFunctions.MIN_FUNCTION;
			} else if (((Integer) value) == 5) {
				operand.function = AggregationFunctions.COUNT_FUNCTION;
			} else if (((Integer) value) == 6) {
				operand.function = AggregationFunctions.COUNT_DISTINCT_FUNCTION;
			}
		}
		viewer.refresh();
		
		queryBuilder.setDirtyEditor();
	}

}
