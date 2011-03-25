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

import java.util.List;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.WhereField.Operand;
import it.eng.qbe.statement.AbstractStatement;
import it.eng.spagobi.meta.querybuilder.model.QueryProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;


/**
 * @author cortella
 *
 */
public class QueryBuilderDropWhereListener extends ViewerDropAdapter {
	private static int counter = 1;
	
	private Viewer viewer;
	/**
	 * @param viewer
	 */
	public QueryBuilderDropWhereListener(Viewer viewer) {
		super(viewer);
		this.viewer = viewer;

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
				addTableRow((TableViewer)viewer,dataMartField);
			}        	
        } else if(selectionData instanceof IModelField){
        	addTableRow((TableViewer)viewer,(IModelField)selectionData);
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
	
	public void addTableRow(TableViewer tableViewer, IModelField dataMartField){
		Query query;
                
        String[] nullStringArray = new String[1];
        nullStringArray[0] = "null";
        
        String[] values = new String[1];
        nullStringArray[0] = dataMartField.getUniqueName();
        
        Operand leftOperand = new Operand(values,dataMartField.getParent().getName()+" : "+dataMartField.getName(), AbstractStatement.OPERAND_TYPE_FIELD, nullStringArray, nullStringArray);
        query = QueryProvider.getQuery();
        query.addWhereField("Filter"+counter, "Filter"+counter, true, leftOperand, "NONE", null, "AND");
        tableViewer.setInput(query.getWhereFields());
        tableViewer.refresh();
        counter++;
	}

}
