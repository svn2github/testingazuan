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
import it.eng.qbe.statement.AbstractStatement;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.tools.dataset.common.query.AggregationFunctions;

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class HavingRightOperandColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public HavingRightOperandColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		HavingField whereField = ((HavingField) element);
		if(whereField!=null && whereField.getRightOperand()!=null && whereField.getRightOperand().description!=null){
			return whereField.getRightOperand().description;
		}
		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {	
		String stringValue = (String)value;
		String[] values = getValues(stringValue);
		Operand rightOperand = new Operand(values, stringValue,AbstractStatement.OPERAND_TYPE_STATIC,values, getValues(stringValue), AggregationFunctions.NONE_FUNCTION);
		((HavingField) element).setRightOperand(rightOperand);
		viewer.refresh();
		
		queryBuilder.setDirtyEditor();
	}
	
	private String[] getValues(String values){
		StringTokenizer stk = new StringTokenizer(values,",");
		int tockensNumber = stk.countTokens();
		String[] valuesArray = new String[tockensNumber];
		int i=0;
		while(stk.hasMoreTokens()){
			valuesArray[i]=stk.nextToken();
			i++;
		}
		return valuesArray;
	}
}

