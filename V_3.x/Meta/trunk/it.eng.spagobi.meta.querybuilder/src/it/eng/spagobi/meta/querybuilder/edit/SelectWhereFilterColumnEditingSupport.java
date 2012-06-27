/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.edit;

import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.query.InLineCalculatedSelectField;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.SimpleSelectField;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class SelectWhereFilterColumnEditingSupport extends EditingSupport {
	private final TableViewer whereViewer;
	private QueryBuilder queryBuilder;
	/**
	 * @param viewer
	 */
	public SelectWhereFilterColumnEditingSupport(TableViewer whereViewer, QueryBuilder queryBuilder) {
		super(whereViewer);
		this.whereViewer = whereViewer;
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
		return true;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof SimpleSelectField){
			SimpleSelectField selectField = ((SimpleSelectField) element);
			IModelField modelField = queryBuilder.getBaseModelStructure().getField(selectField.getUniqueName());
			String fieldName = modelField.getParent().getName()+" : "+modelField.getName();
			Query query = queryBuilder.addWhereField(selectField.getUniqueName(), fieldName, false,null);
			if(whereViewer!=null){
				whereViewer.setInput(query.getWhereFields());
				whereViewer.refresh();	
				queryBuilder.setDirtyEditor();
			}			
		} else if (element instanceof InLineCalculatedSelectField){
			InLineCalculatedSelectField selectField = ((InLineCalculatedSelectField) element);
			Query query = queryBuilder.addWhereField(null, selectField.getName(), true,selectField.getExpression());
			if(whereViewer!=null){
				whereViewer.setInput(query.getWhereFields());
				whereViewer.refresh();	
				queryBuilder.setDirtyEditor();
			}
		}
	}

}
