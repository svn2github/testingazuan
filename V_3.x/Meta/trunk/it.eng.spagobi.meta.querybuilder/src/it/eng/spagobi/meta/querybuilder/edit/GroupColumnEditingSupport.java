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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

/**
 * @author cortella
 *
 */
public class GroupColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public GroupColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof SimpleSelectField) {
			SimpleSelectField selectField = ((SimpleSelectField) element);
			return selectField.isGroupByField();
		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);
			return selectField.isGroupByField();			
		}
		return null;

	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof SimpleSelectField) {
			SimpleSelectField selectField = ((SimpleSelectField) element);
			selectField.setGroupByField((Boolean) value);
		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);
			selectField.setGroupByField((Boolean) value);
		}

		viewer.refresh();
		
		queryBuilder.setDirtyEditor();
	}

}
