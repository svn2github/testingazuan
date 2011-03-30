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
