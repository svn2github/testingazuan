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


import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDragListener;
import it.eng.spagobi.meta.querybuilder.ui.editor.SpagoBIDataSetEditor;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables.QueryFiltersComponents;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree.ModelTreeViewer;
import it.eng.spagobi.meta.querybuilder.ui.shared.result.ResultTableViewer;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.MultiPageEditorPart;



/**
 * @author cortella
 *
 */
public class QueryBuilder {
	
	protected ViewModelStructure datamartStructure;

	public QueryBuilder(ViewModelStructure datamartStructure){
		this.datamartStructure = datamartStructure;
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
		return new SpagoBIDataSetEditor(this);
	}
	
	/*
	 * Create UI components for Query Edit
	 * @return the composite populated with widgets
	 */
	public Composite createEditComponents(Composite parent){
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
	public void createEditBusinessModelTree(Composite composite){
		Composite compositeTree = new Composite(composite, SWT.NONE);
		GridData gd_compositeTree = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_compositeTree.widthHint = 180;
		compositeTree.setLayoutData(gd_compositeTree);
		compositeTree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group groupBusinessModelTree = new Group(compositeTree, SWT.NONE);
		groupBusinessModelTree.setText("Business Model");
		groupBusinessModelTree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		//*******************************************
		// Business Model Tree Viewer 
		//*******************************************
		ModelTreeViewer businessModelTreeViewer = new ModelTreeViewer(groupBusinessModelTree, datamartStructure);
		Transfer[] transferTypes = new Transfer[]{ TextTransfer.getInstance(),LocalSelectionTransfer.getTransfer()  };
		businessModelTreeViewer.addDragSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDragListener(businessModelTreeViewer));
	}

	/*
	 * Create UI for Query Edit - Query Filters (Select, Where, Having)
	 */	
	public QueryFiltersComponents createEditFilters(Composite composite){
		QueryFiltersComponents compositeFilters;
		compositeFilters = new QueryFiltersComponents(composite, datamartStructure);
		return compositeFilters;
	}

	/*
	 *  Create UI components for Query Results
	 *  @return the composite populated with widgets
	 */
	public Composite createResultsComponents(Composite parent){		
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
		createResultsTableViewer(groupQueryResult);
		
		return container;
	}
	
	/*
	 *  Create Table widget for Query Results
	 */
	public ResultTableViewer createResultsTableViewer(Group groupQueryResult){
		ResultTableViewer tableViewer ;
		tableViewer = new ResultTableViewer(groupQueryResult, datamartStructure);
		return tableViewer;
	}
	

}
