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
import it.eng.qbe.query.DataMartSelectField;
import it.eng.qbe.query.Query;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label selectFieldLabel = new Label(this, SWT.NONE);
		selectFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectFieldLabel.setText("Select Fields");
		
		viewer  = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		viewer.setColumnProperties(new String[] { "Entity", "Field", "Alias", "Function", "Order", "Group", "Include", "Visible", "Filter", "Having" });
		
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		//createEditSelectColumns(this, viewer);
		
		viewer.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		viewer.setInput(query.getSelectFields(false));		
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		viewer.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropSelectListener(this));
		
		//Delete via Keyboard
		//TODO: modify the removeSelectField
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
				DataMartSelectField field = (DataMartSelectField) element;
				IModelField modelField = queryBuilder.getBaseModelStructure().getField(field.getUniqueName());
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
				IModelField modelField = queryBuilder.getBaseModelStructure().getField(field.getUniqueName());
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
				return field.getFunction().getName();
			}
		});		
		col.setEditingSupport(new FunctionColumnEditingSupport(viewer));
		
		
		//Order Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, viewer);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
//				SelectField field = (SelectField) element;
//				return field.getOrder();
				DataMartSelectField field = (DataMartSelectField) element;
				return field.getOrderType();
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
//				if (((SelectField) element).isGroup()) {
//					return CHECKED;
//				} else {
//					return UNCHECKED;
//				}
				DataMartSelectField field = (DataMartSelectField) element;
				boolean isGroupBy = field.isGroupByField();
				if (isGroupBy){
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
//				if (((SelectField) element).isInclude()) {
//					return CHECKED;
//				} else {
//					return UNCHECKED;
//				}
				DataMartSelectField field = (DataMartSelectField) element;
				boolean isIncluded = field.isIncluded();
				if (isIncluded){
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
//				if (((SelectField) element).isVisible()) {
//					return CHECKED;
//				} else {
//					return UNCHECKED;
//				}
				DataMartSelectField field = (DataMartSelectField) element;
				boolean isVisible = field.isVisible();
				if (isVisible){
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
	
	public void addField(IModelField dataMartField) {
		Query query;
		
		query = queryBuilder.getQuery();
		query.addSelectFiled(dataMartField.getUniqueName(), "NONE", dataMartField.getName(), true, true, false, null, dataMartField.getPropertyAsString("format"));
		
		viewer.setInput(query.getSelectFields(false));
		viewer.refresh();
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
				}
			}
			logger.trace("OUT");
		}
	}
	
	
	
	
	
}
