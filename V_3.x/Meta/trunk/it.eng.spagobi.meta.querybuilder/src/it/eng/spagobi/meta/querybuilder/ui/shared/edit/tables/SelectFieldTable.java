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
import it.eng.qbe.query.InLineCalculatedSelectField;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.SimpleSelectField;
import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.querybuilder.SpagoBIMetaQueryBuilderPlugin;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropSelectListener;
import it.eng.spagobi.meta.querybuilder.edit.AliasColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.FunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.GroupColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.IncludeColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.OrderColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.SelectHavingFilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.SelectWhereFilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.VisibleColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @authors
 * Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SelectFieldTable extends AbstractQueryEditTable {
	
	private TableViewer viewer;
	private QueryBuilder queryBuilder;
	
	private static final IResourceLocator RL = SpagoBIMetaQueryBuilderPlugin.getInstance().getResourceLocator();
	private static final Image CHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.checked") ).createImage();
	private static final Image UNCHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.unchecked") ).createImage();
	private static final Image FILTER = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.filter") ).createImage();
	
	private static Logger logger = LoggerFactory.getLogger(SelectFieldTable.class);


	public SelectFieldTable(Composite container, QueryBuilder queryBuilder) {
		super(container, SWT.NONE);
		this.queryBuilder = queryBuilder;
		
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label selectFieldLabel = new Label(this, SWT.NONE);
		selectFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectFieldLabel.setText("Select Fields");
		
		//clear buttons
		Composite buttons = new Composite(this, SWT.NONE);
		selectFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buttons.setLayout(new GridLayout(2, false));
		Button cleanButton = new Button(buttons, SWT.PUSH);
		cleanButton.setLayoutData(new GridData(GridData.END  , GridData.FILL  , false, false, 1, 1));
		cleanButton.setText("Clean");
		Button cleanAllButton = new Button(buttons, SWT.PUSH);
		cleanAllButton.setLayoutData(new GridData(GridData.END  , GridData.FILL  , false, false, 1, 1));
		cleanAllButton.setText("Clean All");
		
		viewer  = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		viewer.setColumnProperties(new String[] { "Entity", "Field", "Alias", "Function", "Order", "Group", "Include", "Visible", "Filter", "Having" });
		
		Table table = viewer.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gdTable.heightHint = 100;
		table.setLayoutData(gdTable);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		
		viewer.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		viewer.setInput(query.getSelectFields(false));	
		
		cleanButton.addListener(SWT.Selection, new SelectClearListener());
		cleanAllButton.addListener(SWT.Selection, new SelectClearAllListener());
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		viewer.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropSelectListener(this, queryBuilder));		
		
		//Delete via Keyboard
		viewer.getTable().addKeyListener(new SelectTableKeyAdapter(queryBuilder) ); 
	}
	
	public void createEditSelectColumns(TableViewer tableViewerWhere, TableViewer tableViewerHaving){
		String[] columnsTitles = { "Entity", "Field", "Alias", "Function", "Order", "Group", "Include", "Visible", "Filter", "Having" };
		int[] columnsBounds = { 100, 100, 50, 50, 50, 50, 50, 50, 50, 50 };	
		//Entity Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					IModelField modelField = queryBuilder.getBaseModelStructure().getField(field.getUniqueName());
					return modelField.getParent().getName();
				}
				else if (element instanceof InLineCalculatedSelectField){
					return "";
				}
				return "";

			}
		});		
		
		//Field Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getField();
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					IModelField modelField = queryBuilder.getBaseModelStructure().getField(field.getUniqueName());
					return modelField.getName();					
				} else if (element instanceof InLineCalculatedSelectField){
					return "";
				}
				return "";

			}
		});		
		
		//Alias Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getAlias();
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					return field.getAlias();					
				} else if (element instanceof InLineCalculatedSelectField){
					InLineCalculatedSelectField field = (InLineCalculatedSelectField) element;
					return field.getAlias();	
				}
				return "";

			}
		});		
		col.setEditingSupport(new AliasColumnEditingSupport(viewer,queryBuilder));
		
		//Function Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getFunction();
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					return field.getFunction().getName();					
				} else if (element instanceof InLineCalculatedSelectField){
					InLineCalculatedSelectField field = (InLineCalculatedSelectField) element;
					return field.getFunction().getName();		
				}
				return "";

			}
		});		
		col.setEditingSupport(new FunctionColumnEditingSupport(viewer,queryBuilder));
		
		
		//Order Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getOrder();
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					return field.getOrderType();	
				} else if (element instanceof InLineCalculatedSelectField){
					InLineCalculatedSelectField field = (InLineCalculatedSelectField) element;
					return field.getOrderType();		
				}
				return "";

			}
		});	
		col.setEditingSupport(new OrderColumnEditingSupport(viewer,queryBuilder));
		
		//Group Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
//				if (((SelectField) element).isGroup()) {
//					return CHECKED;
//				} else {
//					return UNCHECKED;
//				}
				boolean isGroupBy = false;
				
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					isGroupBy = field.isGroupByField();
				} else if (element instanceof InLineCalculatedSelectField){
					InLineCalculatedSelectField field = (InLineCalculatedSelectField) element;
					isGroupBy = field.isGroupByField();
				}
				
				if (isGroupBy){
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});	
		col.setEditingSupport(new GroupColumnEditingSupport(viewer,queryBuilder));
		
		//Include Column
		col = createTableViewerColumn(columnsTitles[6], columnsBounds[6], 6, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
//				if (((SelectField) element).isInclude()) {
//					return CHECKED;
//				} else {
//					return UNCHECKED;
//				}
				boolean isIncluded = false;
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					isIncluded = field.isIncluded();					
				} else if (element instanceof InLineCalculatedSelectField){
					InLineCalculatedSelectField field = (InLineCalculatedSelectField) element;
					isIncluded = field.isIncluded();	
				}

				if (isIncluded){
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});	
		col.setEditingSupport(new IncludeColumnEditingSupport(viewer,queryBuilder));
		
		//Visible Column
		col = createTableViewerColumn(columnsTitles[7], columnsBounds[7], 7, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
//				if (((SelectField) element).isVisible()) {
//					return CHECKED;
//				} else {
//					return UNCHECKED;
//				}
				boolean isVisible = false;
				if (element instanceof SimpleSelectField){
					SimpleSelectField field = (SimpleSelectField) element;
					isVisible = field.isVisible();
				} else if (element instanceof InLineCalculatedSelectField){
					InLineCalculatedSelectField field = (InLineCalculatedSelectField) element;
					isVisible = field.isVisible();	
				}

				if (isVisible){
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}

		});	
		col.setEditingSupport(new VisibleColumnEditingSupport(viewer,queryBuilder));

		
		//Filter Column
		col = createTableViewerColumn(columnsTitles[8], columnsBounds[8], 8, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				return FILTER;
			}
		});	
		col.setEditingSupport(new SelectWhereFilterColumnEditingSupport(tableViewerWhere, queryBuilder));
		
		//Having Column
		col = createTableViewerColumn(columnsTitles[9], columnsBounds[9], 9, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				return FILTER;
			}
		});	
		col.setEditingSupport(new SelectHavingFilterColumnEditingSupport(tableViewerHaving, queryBuilder));
	}

	
	public TableViewer getViewer() {
		return viewer;
	}
	

	
	public class SelectTableKeyAdapter extends KeyAdapter {
		QueryBuilder queryBuilder;
		
		public SelectTableKeyAdapter(QueryBuilder queryBuilder) {
			this.queryBuilder = queryBuilder;
		}
		
		public void keyPressed(KeyEvent e)	{
			logger.trace("IN");
				
			if (e.keyCode == SWT.DEL)
			{
				logger.debug("Delete pressed");
				int[] selectionIndices = viewer.getTable().getSelectionIndices();
				logger.debug("Number of selected elements is equals to [{}]",selectionIndices.length);
				int indexLength = selectionIndices.length;
				Query query = queryBuilder.getQuery();
				int selectFieldsNumber = query.getSelectFields(false).size();
				if (indexLength > 0)
				{
					logger.debug("Number of selection field in query is equal to [{}]", query.getSelectFields(false).size());
					for (int i = indexLength-1; i >=0; i--){		
						query.removeSelectField(selectionIndices[i]);
						logger.debug("Successfully removed query selection field at index equal to [{}]", selectionIndices[i]);
					}
					logger.debug("Number of selection field in query is equal to [{}]", query.getSelectFields(false).size());
					//Assert.assertTrue("Unable to delete alla select fields from query", selectFieldsNumber - query.getSelectFields(false).size() == indexLength);
					viewer.setInput(query.getSelectFields(false));
					viewer.refresh();
					
					queryBuilder.setDirtyEditor();
				}
			}
			logger.trace("OUT");
		}
	}
	
	/**
	 * Listener for the clear button
	 * @author Alberto Ghedin (alberto.ghedin@eng.it)
	 *
	 */
	private class SelectClearListener implements Listener{
		
		/**
		 * remove the selected fields from the query
		 * and update the table
		 */
		public void handleEvent(Event event) {
			queryBuilder.getQuery().clearSelectedFields();
			if(viewer!=null){
				viewer.setInput(new ArrayList<Object>());
				viewer.refresh();
				
				queryBuilder.setDirtyEditor();
			}
		}
	}
	
	/**
	 * Listener for the clear all button
	 * @author Alberto Ghedin (alberto.ghedin@eng.it)
	 *
	 */
	private class SelectClearAllListener implements Listener{
		
		/**
		 * remove all the fields (select, where)
		 * and update the table
		 */
		public void handleEvent(Event event) {
			queryBuilder.getQuery().clearSelectedFields();
			queryBuilder.getQuery().clearWhereFields();
			queryBuilder.getQuery().clearHavingFields();
			queryBuilder.refreshQueryEditGroup();
			
			queryBuilder.setDirtyEditor();
		}
	}
	
	
}
