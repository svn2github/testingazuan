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
package it.eng.spagobi.meta.querybuilder.ui;



import it.eng.spagobi.meta.datamarttree.tree.DatamartTree;
import it.eng.spagobi.meta.querybuilder.Activator;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDragListener;
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
import it.eng.spagobi.meta.querybuilder.model.SelectField;
import it.eng.spagobi.meta.querybuilder.model.SelectFieldModelProvider;
import it.eng.spagobi.meta.querybuilder.model.WhereClause;
import it.eng.spagobi.meta.querybuilder.model.WhereClauseModelProvider;

import java.util.ArrayList;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
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
import org.eclipse.ui.part.MultiPageEditorPart;


/**
 * @author cortella
 *
 */
public class QueryBuilder {

	private static final Image CHECKED = Activator.getImageDescriptor(
			"icons/checked.png").createImage();
	private static final Image UNCHECKED = Activator.getImageDescriptor(
			"icons/unchecked.png").createImage();

	public QueryBuilder(){
		
	}
	
	/*
	 * Create a new Wizard for QueryBuilder with 
	 * Edit and Results pages
	 */
	public Wizard createWizard() {
		return null;
	}
	
	/*
	 * Create a new (MultiPage)Editor for QueryBuilder with Edit 
	 * and Results pages
	 */
	public MultiPageEditorPart createEditor() {
		return new QueryBuilderEditor(this);
	}
	
	/*
	 * Create UI components for Query Edit
	 * @return the composite populated with widgets
	 */
	public Composite createEditComponent(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		//Create main grid with two columns
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		//Create Business Model Tree 
		createEditBusinessModelTree(composite);
		
		//Create Query Filters
		createEditFilters(composite);

		return container;
	}
	
	/*
	 * Create UI for Query Edit - Business Model Tree
	 */
	private void createEditBusinessModelTree(Composite composite){
		Composite compositeTree = new Composite(composite, SWT.NONE);
		GridData gd_compositeTree = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_compositeTree.widthHint = 180;
		compositeTree.setLayoutData(gd_compositeTree);
		compositeTree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group groupBusinessModelTree = new Group(compositeTree, SWT.NONE);
		groupBusinessModelTree.setText("Business Model");
		groupBusinessModelTree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		//*******************************************
		// TODO: Business Model Tree Viewer Here!
		//*******************************************
		
		/*
		TreeViewer businessModelTreeViewer = new TreeViewer(groupBusinessModelTree, SWT.BORDER);
				
		//only for test, use a fake model and content provider
		businessModelTreeViewer.setLabelProvider(new LabelProvider());
		businessModelTreeViewer.setContentProvider(new MyContentProvider());
		businessModelTreeViewer.setInput(createModel());
		*/
		DatamartTree businessModelTreeViewer = new DatamartTree(groupBusinessModelTree);
		Transfer[] transferTypes = new Transfer[]{ TextTransfer.getInstance(),LocalSelectionTransfer.getTransfer()  };
		businessModelTreeViewer.addDragSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDragListener(businessModelTreeViewer));
	}

	/*
	 * Create UI for Query Edit - Query Filters (Select, Where, Having)
	 */	
	private void createEditFilters(Composite composite){
		Composite compositeFilters = new Composite(composite, SWT.NONE);
		compositeFilters.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeFilters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQueryEditor = new Group(compositeFilters, SWT.NONE);
		grpQueryEditor.setText("Query Editor");
		grpQueryEditor.setLayout(new GridLayout(1, false));
		
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
		tableViewerSelect.setInput(SelectFieldModelProvider.INSTANCE.getSelectFields());
		
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
				SelectField field = (SelectField) element;
				return field.getEntity();
			}
		});		
		
		//Field Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SelectField field = (SelectField) element;
				return field.getField();
			}
		});		
		
		//Alias Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SelectField field = (SelectField) element;
				return field.getAlias();
			}
		});		
		col.setEditingSupport(new AliasColumnEditingSupport(viewer));
		
		//Function Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SelectField field = (SelectField) element;
				return field.getFunction();
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
	 *  Create UI components for Query Results
	 *  @return the composite populated with widgets
	 */
	public Composite createResultsComponent(Composite parent){		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		FillLayout fl_composite = new FillLayout(SWT.HORIZONTAL);
		fl_composite.marginWidth = 2;
		fl_composite.marginHeight = 2;
		composite.setLayout(fl_composite);
		
		Group groupQueryResult = new Group(composite, SWT.NONE);
		groupQueryResult.setText("Query Result");
		GridLayout gl_groupQueryResult = new GridLayout(1, false);
		gl_groupQueryResult.marginRight = 1;
		gl_groupQueryResult.marginTop = 1;
		gl_groupQueryResult.marginLeft = 1;
		gl_groupQueryResult.marginBottom = 1;
		groupQueryResult.setLayout(gl_groupQueryResult);
		
		//Create Table widget to host results
		createResultsTable(groupQueryResult);
		
		return container;
	}
	
	/*
	 *  Create Table widget for Query Results
	 */
	private void createResultsTable(Group groupQueryResult){
		TableViewer tableViewer = new TableViewer(groupQueryResult, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//***** FAKE SETTINGS TO CHANGE ******
		tableViewer.setLabelProvider(new LabelProvider());
		tableViewer.setContentProvider(new MyContentProvider2());

		//populate Table widget with data
		populateResultsTable(tableViewer);
	}
	
	//Populate the Result Table widgets with data from the query
	private void populateResultsTable(TableViewer tableViewer){
		
		//Create columns with header
		tableViewer.setColumnProperties(new String[] { "Column 1", "Column 2" });
		TableColumn column = new TableColumn(tableViewer.getTable(),SWT.NONE);
		column.setWidth(100);
		column.setText("Column 1");
		
		column = new TableColumn(tableViewer.getTable(),SWT.NONE);
		column.setWidth(100);
		column.setText("Column 2");
		
		//****** THIS IS A FAKE MODEL ONLY FOR TEST - TO REMOVE ******
		MyModel2[] model2 = createModel2();
		tableViewer.setInput(model2);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		
	}
	

//*************************************************************
	
	//***********************************************
	// Private Class, only for test. Will be deleted.
	//***********************************************
	private class MyContentProvider implements ITreeContentProvider {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return ((MyModel)inputElement).child.toArray();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object parentElement) {
			return getElements(parentElement);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
			if( element == null) {
				return null;
			}

			return ((MyModel)element).parent;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) {
			return ((MyModel)element).child.size() > 0;
		}

	}

	public class MyModel {
		public MyModel parent;
		public ArrayList child = new ArrayList();
		public int counter;

		public MyModel(int counter, MyModel parent) {
			this.parent = parent;
			this.counter = counter;
		}

		public String toString() {
			String rv = "Item ";
			if( parent != null ) {
				rv = parent.toString() + ".";
			}

			rv += counter;

			return rv;
		}
	}


	private MyModel createModel() {

		MyModel root = new MyModel(0,null);
		root.counter = 0;

		MyModel tmp;
		for( int i = 1; i < 10; i++ ) {
			tmp = new MyModel(i, root);
			root.child.add(tmp);
			for( int j = 1; j < i; j++ ) {
				tmp.child.add(new MyModel(j,tmp));
			}
		}

		return root;
	}

	private class MyContentProvider2 implements IStructuredContentProvider {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return (MyModel2[])inputElement;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}

	public class MyModel2 {
		public int counter;

		public MyModel2(int counter) {
			this.counter = counter;
		}

		public String toString() {
			return "Item " + this.counter;
		}
	}	
	
	private MyModel2[] createModel2() {
		MyModel2[] elements = new MyModel2[10];
		
		for( int i = 0; i < 10; i++ ) {
			elements[i] = new MyModel2(i);
		}
		
		return elements;
	}
	
//****** end private classes and methods to delete
}
