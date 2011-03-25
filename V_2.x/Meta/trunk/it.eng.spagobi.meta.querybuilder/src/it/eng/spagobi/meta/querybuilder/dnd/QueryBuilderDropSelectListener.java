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

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.Query;
import it.eng.spagobi.meta.querybuilder.model.QueryProvider;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables.SelectFieldTable;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
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
	
	private static Logger logger = LoggerFactory.getLogger(QueryBuilderDropSelectListener.class);
	
	public QueryBuilderDropSelectListener(SelectFieldTable selectFieldTable) {
		super(selectFieldTable.getViewer());
		this.selectTable = selectFieldTable;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
	 */
	@Override
	public boolean performDrop(Object data) {
		TreeSelection selection = (TreeSelection)data;
		Object selectionData = selection.getFirstElement();

		System.out.println("SelectionData: "+selectionData.getClass().getName());
		if (selectionData instanceof IModelEntity){
   			System.out.println("DataMartEntity");
   			IModelEntity dataMartEntity = (IModelEntity)selectionData;
			List<IModelField> dataMartFields = dataMartEntity.getAllFields();
			for (IModelField dataMartField : dataMartFields){
				addTableRow(selectTable.getViewer(), dataMartField);
			}        	
        } else if(selectionData instanceof IModelField){
        	addTableRow(selectTable.getViewer(),(IModelField)selectionData);
        }
        
			
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
	

	
	public void addTableRow(TableViewer tableViewer, IModelField dataMartField) {
		Query query;
		query = QueryProvider.getQuery();
		query.addSelectFiled(dataMartField.getUniqueName(), "NONE", dataMartField.getName(), true, true, false, null, dataMartField.getPropertyAsString("format"));
		tableViewer.setInput(query.getSelectFields(false));
		tableViewer.refresh();
	}

}
