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




import it.eng.spagobi.meta.querybuilder.model.HavingClause;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class HavingOperatorColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	/**
	 * @param viewer
	 */
	public HavingOperatorColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
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
		HavingClause havingClause = (HavingClause) element;
		if (havingClause.getOperator().equals("NONE")) {
			return 0;
		} else if (havingClause.getOperator().equals("EQUALS TO")){
			return 1;
		} else if (havingClause.getOperator().equals("NOT EQUALS TO")){
			return 2;
		} else if (havingClause.getOperator().equals("GREATER THAN")){
			return 3;
		} else if (havingClause.getOperator().equals("EQUALS OR GREATER THAN")){
			return 4;
		} else if (havingClause.getOperator().equals("LESS THAN")){
			return 5;
		} else if (havingClause.getOperator().equals("EQUALS OR LESS THAN")){
			return 6;
		} else if (havingClause.getOperator().equals("STARTS WITH")){
			return 7;
		} else if (havingClause.getOperator().equals("NOT STARTS WITH")){
			return 8;
		} else if (havingClause.getOperator().equals("ENDS WITH")){
			return 9;
		} else if (havingClause.getOperator().equals("NOT ENDS WITH")){
			return 10;
		} else if (havingClause.getOperator().equals("CONTAINS")){
			return 11;
		} else if (havingClause.getOperator().equals("NOT CONTAINS")){
			return 12;
		} else if (havingClause.getOperator().equals("BETWEEN")){
			return 13;
		} else if (havingClause.getOperator().equals("NOT BETWEEN")){
			return 14;
		} else if (havingClause.getOperator().equals("IN")){
			return 15;
		} else if (havingClause.getOperator().equals("NOT IN")){
			return 16;
		} else if (havingClause.getOperator().equals("NOT NULL")){
			return 17;
		} else if (havingClause.getOperator().equals("IS NULL")){
			return 18;
		} 
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
		HavingClause havingClause = (HavingClause) element;
		if (((Integer) value) == 0) {
			havingClause.setOperator("NONE");
		} else if (((Integer) value) == 1) {
			havingClause.setOperator("EQUALS TO");
		} else if (((Integer) value) == 2) {
			havingClause.setOperator("NOT EQUALS TO");
		} else if (((Integer) value) == 3) {
			havingClause.setOperator("GREATER THAN");
		} else if (((Integer) value) == 4) {
			havingClause.setOperator("EQUALS OR GREATER THAN");
		} else if (((Integer) value) == 5) {
			havingClause.setOperator("LESS THAN");
		} else if (((Integer) value) == 6) {
			havingClause.setOperator("EQUALS OR LESS THAN");
		} else if (((Integer) value) == 7) {
			havingClause.setOperator("STARTS WITH");
		} else if (((Integer) value) == 8) {
			havingClause.setOperator("NOT STARTS WITH");
		} else if (((Integer) value) == 9) {
			havingClause.setOperator("ENDS WITH");
		} else if (((Integer) value) == 10) {
			havingClause.setOperator("NOT ENDS WITH");
		} else if (((Integer) value) == 11) {
			havingClause.setOperator("CONTAINS");
		} else if (((Integer) value) == 12) {
			havingClause.setOperator("NOT CONTAINS");
		} else if (((Integer) value) == 13) {
			havingClause.setOperator("BETWEEN");
		} else if (((Integer) value) == 14) {
			havingClause.setOperator("NOT BETWEEN");
		} else if (((Integer) value) == 15) {
			havingClause.setOperator("IN");
		} else if (((Integer) value) == 16) {
			havingClause.setOperator("NOT IN");
		} else if (((Integer) value) == 17) {
			havingClause.setOperator("NOT NULL");
		} else if (((Integer) value) == 18) {
			havingClause.setOperator("IS NULL");
		}

		viewer.refresh();

	}

}
