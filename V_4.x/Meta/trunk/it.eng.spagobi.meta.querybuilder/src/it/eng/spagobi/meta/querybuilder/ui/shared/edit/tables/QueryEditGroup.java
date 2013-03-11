/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables;


import it.eng.qbe.query.Query;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class QueryEditGroup extends Composite {
	

	private SelectFieldTable selectField;
	private WhereFieldTable whereField;
	private HavingFieldTable havingField;
	
	private static Logger logger = LoggerFactory.getLogger(QueryEditGroup.class);

	
	
	/*
	 * Create UI for query edit tables (Select, Where, Having)
	 */	
	public QueryEditGroup(Composite composite, QueryBuilder queryBuilder) {
		super(composite, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQueryEditor = new Group(this, SWT.NONE);
		grpQueryEditor.setText("Query Editor");
		grpQueryEditor.setLayout(new GridLayout(1, false));
		
		selectField = new SelectFieldTable(grpQueryEditor, queryBuilder);
		whereField = new WhereFieldTable(grpQueryEditor, queryBuilder);
		TableViewer tableViewerWhere = whereField.getViewer();
		havingField = new HavingFieldTable(grpQueryEditor, queryBuilder);
		TableViewer tableViewerHaving = havingField.getViewer();
		selectField.createEditSelectColumns(tableViewerWhere, tableViewerHaving);

	}
	
	public void refreshSelectTable(List selectFields){
		selectField.getViewer().setInput(selectFields);
		selectField.getViewer().refresh();
	}
	
	public void refresh(Query query){
		if(selectField!=null){
			selectField.getViewer().setInput(query.getSelectFields(true));
			selectField.getViewer().refresh();
		}
		if(havingField!=null){
			havingField.getViewer().setInput(query.getHavingFields());
			havingField.getViewer().refresh();
		}
		if(whereField!=null){
			whereField.getViewer().setInput(query.getWhereFields());
			whereField.getViewer().refresh();
		}
	}


}
