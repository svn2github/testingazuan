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
package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables;


import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

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
		
		SelectFieldTable selectField = new SelectFieldTable(grpQueryEditor, queryBuilder);
		WhereFieldTable whereField = new WhereFieldTable(grpQueryEditor, queryBuilder);
		TableViewer tableViewerWhere = whereField.getViewer();
		HavingFieldTable havingField = new HavingFieldTable(grpQueryEditor, queryBuilder);
		TableViewer tableViewerHaving = havingField.getViewer();
		selectField.createEditSelectColumns(tableViewerWhere, tableViewerHaving);

	}


}
