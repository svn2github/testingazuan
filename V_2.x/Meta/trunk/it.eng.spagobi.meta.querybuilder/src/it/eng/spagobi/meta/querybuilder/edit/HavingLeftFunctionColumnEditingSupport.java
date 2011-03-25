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

import it.eng.qbe.query.HavingField;
import it.eng.qbe.query.HavingField.Operand;
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
	/**
	 * @param viewer
	 */
	public HavingLeftFunctionColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
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

	}

}
