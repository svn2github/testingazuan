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



import it.eng.spagobi.meta.querybuilder.model.WhereClause;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class BooleanConnectorColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	/**
	 * @param viewer
	 */
	public BooleanConnectorColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] booleanConnectors = new String[2];
		booleanConnectors[0] = "AND";
		booleanConnectors[1] = "OR";


		return new ComboBoxCellEditor(viewer.getTable(), booleanConnectors);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		WhereClause whereClause = (WhereClause) element;
		if (whereClause.getBooleanConnector().equals("AND")) {
			return 0;
		} else if (whereClause.getBooleanConnector().equals("OR")){
			return 1;
		} 
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
		WhereClause whereClause = (WhereClause) element;
		if (((Integer) value) == 0) {
			whereClause.setBooleanConnector("AND");
		} else if (((Integer) value) == 1) {
			whereClause.setBooleanConnector("OR");
		} 

		viewer.refresh();

	}

}
