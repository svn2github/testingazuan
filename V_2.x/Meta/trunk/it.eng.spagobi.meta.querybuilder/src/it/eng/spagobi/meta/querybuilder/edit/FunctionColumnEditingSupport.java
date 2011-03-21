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

import it.eng.spagobi.meta.querybuilder.model.SelectField;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class FunctionColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	/**
	 * @param viewer
	 */
	public FunctionColumnEditingSupport(TableViewer viewer) {
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
		SelectField selectField = (SelectField) element;
		if (selectField.getFunction().equals("NONE")) {
			return 0;
		} else if (selectField.getFunction().equals("SUM")){
			return 1;
		} else if (selectField.getFunction().equals("AVERAGE")){
			return 2;
		} else if (selectField.getFunction().equals("MAXIMUM")){
			return 3;
		} else if (selectField.getFunction().equals("MINIMUM")){
			return 4;
		} else if (selectField.getFunction().equals("COUNT")){
			return 5;
		} else if (selectField.getFunction().equals("COUNT DISTINCT")){
			return 6;
		}
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
		SelectField selectField = (SelectField) element;
		if (((Integer) value) == 0) {
			selectField.setFunction("NONE");
		} else if (((Integer) value) == 1) {
			selectField.setFunction("SUM");
		} else if (((Integer) value) == 2) {
			selectField.setFunction("AVERAGE");
		} else if (((Integer) value) == 3) {
			selectField.setFunction("MAXIMUM");
		} else if (((Integer) value) == 4) {
			selectField.setFunction("MINIMUM");
		} else if (((Integer) value) == 5) {
			selectField.setFunction("COUNT");
		} else if (((Integer) value) == 6) {
			selectField.setFunction("COUNT DISTINCT");
		}

		viewer.refresh();

	}

}
