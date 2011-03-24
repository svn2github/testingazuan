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

import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.query.DataMartSelectField;
import it.eng.qbe.query.Query;
import it.eng.spagobi.meta.querybuilder.ResourceRegistry;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropHavingListener;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropSelectListener;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropWhereListener;
import it.eng.spagobi.meta.querybuilder.edit.AliasColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.BooleanConnectorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.FilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.FunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.GroupColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingBooleanConnectorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingFilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingIsForPromptColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingLeftFunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingOperatorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingRightFunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.IncludeColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.IsForPromptColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.OperatorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.OrderColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.VisibleColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.model.HavingClause;
import it.eng.spagobi.meta.querybuilder.model.HavingClauseModelProvider;
import it.eng.spagobi.meta.querybuilder.model.QueryProvider;
import it.eng.spagobi.meta.querybuilder.model.SelectField;
import it.eng.spagobi.meta.querybuilder.model.WhereClause;
import it.eng.spagobi.meta.querybuilder.model.WhereClauseModelProvider;

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

/**
 * @author cortella
 *
 */
public class QueryEditGroup extends Composite {
	
	private static final Image CHECKED = ResourceRegistry.getImage("ui.shared.edit.tables.button.checked");
	// Activator.getImageDescriptor("icons/checked.png").createImage();
	private static final Image UNCHECKED = ResourceRegistry.getImage("ui.shared.edit.tables.button.unchecked");
	// Activator.getImageDescriptor("icons/unchecked.png").createImage();
	private ViewModelStructure datamartStructure;

	/*
	 * Create UI for Query Edit - Query Filters (Select, Where, Having)
	 */	
	public QueryEditGroup(Composite composite, ViewModelStructure datamartStructure) {
		super(composite, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQueryEditor = new Group(this, SWT.NONE);
		grpQueryEditor.setText("Query Editor");
		grpQueryEditor.setLayout(new GridLayout(1, false));
		
		this.datamartStructure = datamartStructure;
		
		//create Select panel
		createEditSelect(grpQueryEditor);
		//create Where panel
		createEditWhere(grpQueryEditor);
		//create Having panel
		createEditHaving(grpQueryEditor);
		
	}
	
	/*
	 * Create UI for Query Edit - Select Filter 
	 */	
	private void createEditSelect(Group grpQueryEditor){
		Label lblSelectFields = new Label(grpQueryEditor, SWT.NONE);
		lblSelectFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblSelectFields.setText("Select Fields");
		
		TableViewer tableViewerSelect  = new TableViewer(grpQueryEditor, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerSelect.setColumnProperties(new String[] { "Entity", "Field", "Alias", "Function", "Order", "Group", "Include", "Visible", "Filter", "Having" });
		
		Table table = tableViewerSelect.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createEditSelectColumns(grpQueryEditor, tableViewerSelect);
		
		tableViewerSelect.setContentProvider(new ArrayContentProvider());
//		tableViewerSelect.setInput(SelectFieldModelProvider.INSTANCE.getSelectFields());
		Query query = QueryProvider.getQuery();
		tableViewerSelect.setInput(query.getSelectFields(false));		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerSelect.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropSelectListener(tableViewerSelect));
	}	
	
	private void createEditSelectColumns(final Composite parent, final TableViewer viewer){
		String[] columnsTitles = { "Entity", "Field", "Alias", "Function", "Order", "Group", "Include", "Visible", "Filter", "Having" };
		int[] columnsBounds = { 100, 100, 50, 50, 50, 50, 50, 50, 50, 50 };	

		//Entity Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				
//				SelectField field = (SelectField) element;
//				return field.getEntity();

				DataMartSelectField field = (DataMartSelectField) element;
				IModelField modelField = datamartStructure.getDataSource().getModelStructure().getField(field.getUniqueName());
				return modelField.getParent().getName();
			}
		});		
		
		//Field Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getField();
				DataMartSelectField field = (DataMartSelectField) element;
				IModelField modelField = datamartStructure.getDataSource().getModelStructure().getField(field.getUniqueName());
				return modelField.getName();
			}
		});		
		
		//Alias Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getAlias();
				DataMartSelectField field = (DataMartSelectField) element;
				return field.getAlias();
			}
		});		
		col.setEditingSupport(new AliasColumnEditingSupport(viewer));
		
		//Function Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getFunction();
				DataMartSelectField field = (DataMartSelectField) element;
				IModelField modelField = datamartStructure.getDataSource().getModelStructure().getField(field.getUniqueName());
				return field.getFunction().getName();
			}
		});		
		col.setEditingSupport(new FunctionColumnEditingSupport(viewer));
		
		
		//Order Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SelectField field = (SelectField) element;
				return field.getOrder();
			}
		});	
		col.setEditingSupport(new OrderColumnEditingSupport(viewer));
		
		//Group Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (((SelectField) element).isGroup()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});	
		col.setEditingSupport(new GroupColumnEditingSupport(viewer));
		
		//Include Column
		col = createTableViewerColumn(columnsTitles[6], columnsBounds[6], 6, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (((SelectField) element).isInclude()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});	
		col.setEditingSupport(new IncludeColumnEditingSupport(viewer));
		
		//Visible Column
		col = createTableViewerColumn(columnsTitles[7], columnsBounds[7], 7, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (((SelectField) element).isVisible()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});	
		col.setEditingSupport(new VisibleColumnEditingSupport(viewer));

		
		//Filter Column
		col = createTableViewerColumn(columnsTitles[8], columnsBounds[8], 8, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SelectField field = (SelectField) element;
				if(field.isFilter())
					return "true";
				else 
					return "false";
			}
		});	
		
		//Having Column
		col = createTableViewerColumn(columnsTitles[9], columnsBounds[9], 9, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SelectField field = (SelectField) element;
				if(field.isHaving())
					return "true";
				else 
					return "false";
			}
		});	
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
		tableViewerWhere.setInput(WhereClauseModelProvider.INSTANCE.getWhereClauses());
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerWhere.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropWhereListener(tableViewerWhere));
	}	
	
	public void createEditWhereColumns(final Composite parent, final TableViewer viewer){
		String[] columnsTitles = { "Filter Name", "Left Operand", "Operator", "Right Operand", "Is for prompt", "Bol. connector" };
		int[] columnsBounds = { 100, 100, 100, 100, 50, 50 };	

		//Filter Name Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereClause whereClause = (WhereClause) element;
				return whereClause.getFilterName();
			}
		});	
		col.setEditingSupport(new FilterColumnEditingSupport(viewer));

		
		//Left Operand Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereClause whereClause = (WhereClause) element;
				return whereClause.getLeftOperand();
			}
		});	
		
		//Operator Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereClause whereClause = (WhereClause) element;
				return whereClause.getOperator();
			}
		});		
		col.setEditingSupport(new OperatorColumnEditingSupport(viewer));

		
		//Right Operand Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereClause whereClause = (WhereClause) element;
				return whereClause.getRightOperand();
			}
		});		
		
		//Is for Prompt Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (((WhereClause) element).isForPrompt()) {
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
				WhereClause whereClause = (WhereClause) element;
				return whereClause.getBooleanConnector();
			}
		});		
		col.setEditingSupport(new BooleanConnectorColumnEditingSupport(viewer));
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
		tableViewerHaving.setInput(HavingClauseModelProvider.INSTANCE.getHavingClauses());
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerHaving.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropHavingListener(tableViewerHaving));
	}
	
	public void createEditHavingColumns(final Composite parent, final TableViewer viewer){
		String[] columnsTitles = { "Filter Name","Function", "Left Operand", "Operator","Function", "Right Operand", "Is for prompt", "Bol. connector" };
		int[] columnsBounds = { 100, 100, 100, 100, 100, 100, 50, 50 };	
		
		//Filter Name Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getFilterName();
			}
		});	
		col.setEditingSupport(new HavingFilterColumnEditingSupport(viewer));

		
		//Left Function Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getLeftFunction();
			}
		});	
		col.setEditingSupport(new HavingLeftFunctionColumnEditingSupport(viewer));

		
		//Left Operand Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getLeftOperand();
			}
		});	
		
		//Operator Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getOperator();
			}
		});	
		col.setEditingSupport(new HavingOperatorColumnEditingSupport(viewer));

		
		//Right Function Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getRightFunction();
			}
		});	
		col.setEditingSupport(new HavingRightFunctionColumnEditingSupport(viewer));

		
		//Right Operand Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getRightOperand();
			}
		});	
		
		//Is for Prompt Column
		col = createTableViewerColumn(columnsTitles[6], columnsBounds[6], 6, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingClause havingClause = (HavingClause) element;
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (((HavingClause) element).isForPrompt()) {
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
				HavingClause havingClause = (HavingClause) element;
				return havingClause.getBooleanConnector();
			}
		});	
		col.setEditingSupport(new HavingBooleanConnectorColumnEditingSupport(viewer));

	}
	
	/*
	 * Create Columns for a Table Viewer
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
