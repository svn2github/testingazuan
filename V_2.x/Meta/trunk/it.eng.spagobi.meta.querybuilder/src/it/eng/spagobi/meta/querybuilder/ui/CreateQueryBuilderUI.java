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

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.MultiPageEditorPart;


/**
 * @author cortella
 *
 */
public class CreateQueryBuilderUI {
	
	public CreateQueryBuilderUI(){
		
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
		return new CreateQueryBuilderEditor(this);
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
		
		TreeViewer businessModelTreeViewer = new TreeViewer(groupBusinessModelTree, SWT.BORDER);
				
		//only for test, use a fake model and content provider
		businessModelTreeViewer.setLabelProvider(new LabelProvider());
		businessModelTreeViewer.setContentProvider(new MyContentProvider());
		businessModelTreeViewer.setInput(createModel());
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
		
		List listSelect = new List(grpQueryEditor, SWT.BORDER);
		listSelect.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));		
	}
	
	/*
	 * Create UI for Query Edit - Where Filter 
	 */	
	private void createEditWhere(Group grpQueryEditor){
		Label lblWhereClauses = new Label(grpQueryEditor, SWT.NONE);
		lblWhereClauses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblWhereClauses.setText("Where Clause");
		
		List listWhere = new List(grpQueryEditor, SWT.BORDER);
		listWhere.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));		
	}	
	
	/*
	 * Create UI for Query Edit - Having Filter 
	 */	
	private void createEditHaving(Group grpQueryEditor){
		Label lblHavingClause = new Label(grpQueryEditor, SWT.NONE);
		lblHavingClause.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblHavingClause.setText("Having Clause");
		
		List listHaving = new List(grpQueryEditor, SWT.BORDER);
		listHaving.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}	
	
	/*
	 *  Create UI components for Query Results
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
		/*
		Table tableQueryResults;
		tableQueryResults = new Table(groupQueryResult, SWT.BORDER | SWT.FULL_SELECTION);
		tableQueryResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableQueryResults.setHeaderVisible(true);
		tableQueryResults.setLinesVisible(true);
		
		TableColumn tblclmnColumnHeader = new TableColumn(tableQueryResults, SWT.LEFT);
		tblclmnColumnHeader.setMoveable(true);
		tblclmnColumnHeader.setWidth(100);
		tblclmnColumnHeader.setText("Column Header");
		
		TableItem tableItem_1 = new TableItem(tableQueryResults, SWT.NONE);
		tableItem_1.setText("New TableItem");
		
		TableItem tableItem = new TableItem(tableQueryResults, SWT.NONE);
		tableItem.setText("New TableItem");
		
		TableColumn tableColumn = new TableColumn(tableQueryResults, SWT.NONE);
		tableColumn.setMoveable(true);
		tableColumn.setWidth(100);
		tableColumn.setText("New Column");
		*/
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
