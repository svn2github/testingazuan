/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.edit;

import it.eng.qbe.query.HavingField;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author cortella
 *
 */
public class HavingBooleanConnectorColumnEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public HavingBooleanConnectorColumnEditingSupport(TableViewer viewer, QueryBuilder queryBuilder) {
		super(viewer);
		this.viewer = viewer;
		this.queryBuilder = queryBuilder;
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
		HavingField havingClause = (HavingField) element;
		if (havingClause.getBooleanConnector().equals("AND")) {
			return 0;
		} else if (havingClause.getBooleanConnector().equals("OR")){
			return 1;
		} 
		return 0;

	}

	@Override
	protected void setValue(Object element, Object value) {
		HavingField havingClause = (HavingField) element;
		if (((Integer) value) == 0) {
			havingClause.setBooleanConnector("AND");
		} else if (((Integer) value) == 1) {
			havingClause.setBooleanConnector("OR");
		} 

		viewer.refresh();
		
		queryBuilder.setDirtyEditor();
	}

}
