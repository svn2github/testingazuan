/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.edit;


import it.eng.qbe.query.WhereField;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class OperatorColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public OperatorColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] operators = new String[19];
		operators[0] = "NONE";
		operators[1] = "EQUALS TO";
		operators[2] = "NOT EQUALS TO";
		operators[3] = "GREATER THAN";
		operators[4] = "EQUALS OR GREATER THAN";
		operators[5] = "LESS THAN";
		operators[6] = "EQUALS OR LESS THAN";
		operators[7] = "STARTS WITH";
		operators[8] = "NOT STARTS WITH";
		operators[9] = "ENDS WITH";
		operators[10] = "NOT ENDS WITH";
		operators[11] = "CONTAINS";
		operators[12] = "NOT CONTAINS";
		operators[13] = "BETWEEN";
		operators[14] = "NOT BETWEEN";
		operators[15] = "IN";
		operators[16] = "NOT IN";
		operators[17] = "NOT NULL";
		operators[18] = "IS NULL";
		
		return new ComboBoxCellEditor(viewer.getTable(), operators);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		WhereField whereClause = (WhereField) element;
		if (whereClause.getOperator().equals("NONE")) {
			return 0;
		} else if (whereClause.getOperator().equals("EQUALS TO")){
			return 1;
		} else if (whereClause.getOperator().equals("NOT EQUALS TO")){
			return 2;
		} else if (whereClause.getOperator().equals("GREATER THAN")){
			return 3;
		} else if (whereClause.getOperator().equals("EQUALS OR GREATER THAN")){
			return 4;
		} else if (whereClause.getOperator().equals("LESS THAN")){
			return 5;
		} else if (whereClause.getOperator().equals("EQUALS OR LESS THAN")){
			return 6;
		} else if (whereClause.getOperator().equals("STARTS WITH")){
			return 7;
		} else if (whereClause.getOperator().equals("NOT STARTS WITH")){
			return 8;
		} else if (whereClause.getOperator().equals("ENDS WITH")){
			return 9;
		} else if (whereClause.getOperator().equals("NOT ENDS WITH")){
			return 10;
		} else if (whereClause.getOperator().equals("CONTAINS")){
			return 11;
		} else if (whereClause.getOperator().equals("NOT CONTAINS")){
			return 12;
		} else if (whereClause.getOperator().equals("BETWEEN")){
			return 13;
		} else if (whereClause.getOperator().equals("NOT BETWEEN")){
			return 14;
		} else if (whereClause.getOperator().equals("IN")){
			return 15;
		} else if (whereClause.getOperator().equals("NOT IN")){
			return 16;
		} else if (whereClause.getOperator().equals("NOT NULL")){
			return 17;
		} else if (whereClause.getOperator().equals("IS NULL")){
			return 18;
		} 
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
		WhereField whereClause = (WhereField) element;
		if (((Integer) value) == 0) {
			whereClause.setOperator("NONE");
		} else if (((Integer) value) == 1) {
			whereClause.setOperator("EQUALS TO");
		} else if (((Integer) value) == 2) {
			whereClause.setOperator("NOT EQUALS TO");
		} else if (((Integer) value) == 3) {
			whereClause.setOperator("GREATER THAN");
		} else if (((Integer) value) == 4) {
			whereClause.setOperator("EQUALS OR GREATER THAN");
		} else if (((Integer) value) == 5) {
			whereClause.setOperator("LESS THAN");
		} else if (((Integer) value) == 6) {
			whereClause.setOperator("EQUALS OR LESS THAN");
		} else if (((Integer) value) == 7) {
			whereClause.setOperator("STARTS WITH");
		} else if (((Integer) value) == 8) {
			whereClause.setOperator("NOT STARTS WITH");
		} else if (((Integer) value) == 9) {
			whereClause.setOperator("ENDS WITH");
		} else if (((Integer) value) == 10) {
			whereClause.setOperator("NOT ENDS WITH");
		} else if (((Integer) value) == 11) {
			whereClause.setOperator("CONTAINS");
		} else if (((Integer) value) == 12) {
			whereClause.setOperator("NOT CONTAINS");
		} else if (((Integer) value) == 13) {
			whereClause.setOperator("BETWEEN");
		} else if (((Integer) value) == 14) {
			whereClause.setOperator("NOT BETWEEN");
		} else if (((Integer) value) == 15) {
			whereClause.setOperator("IN");
		} else if (((Integer) value) == 16) {
			whereClause.setOperator("NOT IN");
		} else if (((Integer) value) == 17) {
			whereClause.setOperator("NOT NULL");
		} else if (((Integer) value) == 18) {
			whereClause.setOperator("IS NULL");
		}

		viewer.refresh();
		
		queryBuilder.setDirtyEditor();
	}

}
