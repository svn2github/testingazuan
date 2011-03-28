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

import it.eng.qbe.query.HavingField;
import it.eng.qbe.query.HavingField.Operand;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.WhereField;
import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.querybuilder.SpagoBIMetaQueryBuilderPlugin;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropHavingListener;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropWhereListener;
import it.eng.spagobi.meta.querybuilder.edit.BooleanConnectorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.FilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.FilterRightOperandColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingBooleanConnectorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingFilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingIsForPromptColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingLeftFunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingOperatorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingRightFunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.IsForPromptColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.OperatorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class QueryEditGroup extends Composite {
	
	
	private static final IResourceLocator RL = SpagoBIMetaQueryBuilderPlugin.getInstance().getResourceLocator();
	private static final Image CHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.checked") ).createImage();
	private static final Image UNCHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.unchecked") ).createImage();
	
	private QueryBuilder queryBuilder;

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
		
		this.queryBuilder = queryBuilder;
		
		createEditSelect(grpQueryEditor);
		createEditWhere(grpQueryEditor);
		createEditHaving(grpQueryEditor);
		
	}
	
	/*
	 * Create UI for Query Edit - Select Filter 
	 */	
	private void createEditSelect(Group queryEditorGroup){
		new SelectFieldTable(queryEditorGroup, queryBuilder);
	}	
	
	

	/*
	 * Create UI for Query Edit - Where Filter 
	 */	
	private void createEditWhere(Group grpQueryEditor){
		Label lblWhereClauses = new Label(grpQueryEditor, SWT.NONE);
		lblWhereClauses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblWhereClauses.setText("Where Clause");
		
		TableViewer tableViewerWhere  = new TableViewer(grpQueryEditor, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerWhere.setColumnProperties(new String[] { "Filter Name", "Left Operand", "Operator","Right Operand", "Is for prompt", "Bol. connector" });
		
		Table table = tableViewerWhere.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		createEditWhereColumns(grpQueryEditor, tableViewerWhere);
		
		tableViewerWhere.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		tableViewerWhere.setInput(query.getWhereFields());		

		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerWhere.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropWhereListener(tableViewerWhere, queryBuilder));
	}	
	
	public void createEditWhereColumns(final Composite parent, final TableViewer viewer){
		String[] columnsTitles = { "Filter Name", "Left Operand", "Operator", "Right Operand", "Is for prompt", "Bol. connector" };
		int[] columnsBounds = { 100, 100, 100, 100, 50, 50 };	

		//Filter Name Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField field = (WhereField) element;
				return field.getName();
			}
		});	
		col.setEditingSupport(new FilterColumnEditingSupport(viewer, queryBuilder));

		
		//Left Operand Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				String leftOperand = (whereClause.getLeftOperand()!=null)?whereClause.getLeftOperand().description:"";
				return leftOperand;
			}
		});	
		
		//Operator Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				return whereClause.getOperator();
			}
		});		
		col.setEditingSupport(new OperatorColumnEditingSupport(viewer));

		
		//Right Operand Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				String rightOperand = (whereClause.getRightOperand()!=null)?whereClause.getRightOperand().description:"";
				return rightOperand;
			}
		});	
		col.setEditingSupport(new FilterRightOperandColumnEditingSupport(viewer));
		
		//Is for Prompt Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				return ""+whereClause.isPromptable();
			}
			
			@Override
			public Image getImage(Object element) {
				if (((WhereField) element).isPromptable()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});		
		col.setEditingSupport(new IsForPromptColumnEditingSupport(viewer));

		
		//Bol. Connector Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				return whereClause.getBooleanConnector();
			}
		});		
		col.setEditingSupport(new BooleanConnectorColumnEditingSupport(viewer, queryBuilder));
	}	

	/*
	 * Create UI for Query Edit - Having Filter 
	 */	
	private void createEditHaving(Group grpQueryEditor){
		Label lblHavingClause = new Label(grpQueryEditor, SWT.NONE);
		lblHavingClause.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblHavingClause.setText("Having Clause");
		
		TableViewer tableViewerHaving  = new TableViewer(grpQueryEditor, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerHaving.setColumnProperties(new String[] { "Filter Name","Function", "Left Operand", "Operator", "Function","Right Operand", "Is for prompt", "Bol. connector" });
		
		Table table = tableViewerHaving.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		createEditHavingColumns(grpQueryEditor, tableViewerHaving);
		
		tableViewerHaving.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		tableViewerHaving.setInput(query.getHavingFields());		
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerHaving.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropHavingListener(tableViewerHaving, queryBuilder));
	}
	
	public void createEditHavingColumns(final Composite parent, final TableViewer viewer){
		String[] columnsTitles = { "Filter Name","Function", "Left Operand", "Operator","Function", "Right Operand", "Is for prompt", "Bol. connector" };
		int[] columnsBounds = { 100, 100, 100, 100, 100, 100, 50, 50 };	
		
		//Filter Name Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getName();
			}
		});	
		col.setEditingSupport(new HavingFilterColumnEditingSupport(viewer));

		
		//Left Function Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				Operand leftOperand = havingClause.getLeftOperand();
				if(leftOperand!=null && leftOperand.function!=null && leftOperand.function.getName()!=null){
					return leftOperand.function.getName();
				}
				return "";
			}
		});	
		col.setEditingSupport(new HavingLeftFunctionColumnEditingSupport(viewer));

		
		//Left Operand Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				Operand leftOperand = havingClause.getLeftOperand();
				if(leftOperand!=null && leftOperand.description!=null){
					return leftOperand.description;
				}
				return "";
			}
		});	
		
		//Operator Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getOperator();
			}
		});	
		col.setEditingSupport(new HavingOperatorColumnEditingSupport(viewer));

		
		//Right Function Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				Operand rightOperand = havingClause.getLeftOperand();
				if(rightOperand!=null && rightOperand.function!=null && rightOperand.function.getName()!=null){
					return rightOperand.function.getName();
				}
				return "";
			}
		});	
		col.setEditingSupport(new HavingRightFunctionColumnEditingSupport(viewer));

		
		//Right Operand Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				Operand rightOperand = havingClause.getLeftOperand();
				if(rightOperand!=null && rightOperand.description!=null){
					return rightOperand.description;
				}
				return "";
			}
		});	
		
		//Is for Prompt Column
		col = createTableViewerColumn(columnsTitles[6], columnsBounds[6], 6, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (((HavingField) element).isPromptable()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}
		});	
		col.setEditingSupport(new HavingIsForPromptColumnEditingSupport(viewer));

		
		//Bol. Connector Column
		col = createTableViewerColumn(columnsTitles[7], columnsBounds[7], 7, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getBooleanConnector();
			}
		});	
		col.setEditingSupport(new HavingBooleanConnectorColumnEditingSupport(viewer));

	}
	
	/*
	 * Create Columns for a Table Viewer
	 * 
	 * @deprecated 
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber, TableViewer viewer) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;

	}	

}
