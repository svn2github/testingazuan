/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.dnd;


import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables.SelectFieldTable;

import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author cortella
 *
 */
public class QueryBuilderDropSelectListener extends ViewerDropAdapter {
	
	private SelectFieldTable selectTable;
	private QueryBuilder queryBuilder;
	
	private static Logger logger = LoggerFactory.getLogger(QueryBuilderDropSelectListener.class);
	
	public QueryBuilderDropSelectListener(SelectFieldTable selectFieldTable, QueryBuilder queryBuilder) {
		super(selectFieldTable.getViewer());
		this.queryBuilder = queryBuilder;
		this.selectTable = selectFieldTable;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
	 */
	@Override
	public boolean performDrop(Object data) {
		TreeSelection selection = (TreeSelection)data;
		Object selectionData = selection.getFirstElement();

		logger.debug("SelectionData: "+selectionData.getClass().getName());

		queryBuilder.addSelectFields(selectionData);
		queryBuilder.refreshSelectFields();
		queryBuilder.setDirtyEditor();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		// TODO Auto-generated method stub
		return true;
	}
	

	

}
