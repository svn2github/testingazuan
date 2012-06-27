/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables;

import it.eng.qbe.query.HavingField;
import it.eng.qbe.query.HavingField.Operand;
import it.eng.qbe.query.Query;
import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.querybuilder.SpagoBIMetaQueryBuilderPlugin;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDropHavingListener;
import it.eng.spagobi.meta.querybuilder.edit.HavingBooleanConnectorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingFilterColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingIsForPromptColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingLeftFunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingOperatorColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingRightFunctionColumnEditingSupport;
import it.eng.spagobi.meta.querybuilder.edit.HavingRightOperandColumnEditingSupport;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class HavingFieldTable extends AbstractQueryEditTable {

	private TableViewer tableViewerHaving;
	private QueryBuilder queryBuilder;

	private static Logger logger = LoggerFactory.getLogger(HavingFieldTable.class);
	private static final IResourceLocator RL = SpagoBIMetaQueryBuilderPlugin.getInstance().getResourceLocator();
	private static final Image CHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.checked") ).createImage();
	private static final Image UNCHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.unchecked") ).createImage();

	public HavingFieldTable(Composite container, QueryBuilder queryBuilder) {
		super(container, SWT.NONE);
		this.queryBuilder = queryBuilder;
		
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(GridData.FILL , GridData.FILL, true, true, 1, 1));

		Label selectFieldLabel = new Label(this, SWT.NONE);
		selectFieldLabel.setLayoutData(new GridData(GridData.BEGINNING  , GridData.BEGINNING , false, false, 1, 1));
		selectFieldLabel.setText("Having Clause");
		
		Button cleanButton = new Button(this, SWT.PUSH);
		cleanButton.setLayoutData(new GridData(GridData.END  , GridData.FILL  , false, false, 1, 1));
		cleanButton.setText("Clean");
		
		tableViewerHaving  = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewerHaving.setColumnProperties(new String[] { "Filter Name","Function", "Left Operand", "Operator", "Function","Right Operand", "Is for prompt", "Bol. connector" });
		
		Table table = tableViewerHaving.getTable();
		GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gdTable.heightHint = 100;
		table.setLayoutData(gdTable);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		createEditHavingColumns(this);
		
		tableViewerHaving.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		tableViewerHaving.setInput(query.getHavingFields());		
		
		cleanButton.addListener(SWT.Selection, new HavingClearListener(tableViewerHaving));
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerHaving.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropHavingListener(tableViewerHaving, queryBuilder));
		tableViewerHaving.getTable().addKeyListener(new SelectTableKeyAdapter(queryBuilder) ); 
	}

	private class HavingClearListener implements Listener{
		
		TableViewer tableViewerHaving;
		
		public HavingClearListener(TableViewer tableViewerHaving){
			this.tableViewerHaving = tableViewerHaving;
		}
		
		public void handleEvent(Event event) {
			queryBuilder.getQuery().clearHavingFields();
			if(tableViewerHaving!=null){
				tableViewerHaving.refresh();
				queryBuilder.setDirtyEditor();
			}
		}
	}
	
	public void createEditHavingColumns(final Composite parent){
		String[] columnsTitles = { "Filter Name","Function", "Left Operand", "Operator","Function", "Right Operand", "Is for prompt", "Bol. connector" };
		int[] columnsBounds = { 100, 100, 100, 100, 100, 100, 50, 50 };	
		
		//Filter Name Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, tableViewerHaving);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getName();
			}
		});	
		col.setEditingSupport(new HavingFilterColumnEditingSupport(tableViewerHaving,queryBuilder));

		
		//Left Function Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, tableViewerHaving);
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
		col.setEditingSupport(new HavingLeftFunctionColumnEditingSupport(tableViewerHaving,queryBuilder));

		
		//Left Operand Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, tableViewerHaving);
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
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, tableViewerHaving);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getOperator();
			}
		});	
		col.setEditingSupport(new HavingOperatorColumnEditingSupport(tableViewerHaving,queryBuilder));

		
		//Right Function Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, tableViewerHaving);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				Operand rightOperand = havingClause.getRightOperand();
				if(rightOperand!=null && rightOperand.function!=null && rightOperand.function.getName()!=null){
					return rightOperand.function.getName();
				}
				return "";
			}
		});	
		col.setEditingSupport(new HavingRightFunctionColumnEditingSupport(tableViewerHaving,queryBuilder));

		
		//Right Operand Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, tableViewerHaving);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				Operand rightOperand = havingClause.getRightOperand();
				if(rightOperand!=null && rightOperand.description!=null){
					return rightOperand.description;
				}
				return "";
			}
		});	                    
		col.setEditingSupport(new HavingRightOperandColumnEditingSupport(tableViewerHaving,queryBuilder));
		
		//Is for Prompt Column
		col = createTableViewerColumn(columnsTitles[6], columnsBounds[6], 6, tableViewerHaving);
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
		col.setEditingSupport(new HavingIsForPromptColumnEditingSupport(tableViewerHaving, queryBuilder));

		
		//Bol. Connector Column
		col = createTableViewerColumn(columnsTitles[7], columnsBounds[7], 7, tableViewerHaving);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getBooleanConnector();
			}
		});	
		col.setEditingSupport(new HavingBooleanConnectorColumnEditingSupport(tableViewerHaving,queryBuilder));

	}
	
	private class SelectTableKeyAdapter extends KeyAdapter {
		QueryBuilder queryBuilder;
		
		public SelectTableKeyAdapter(QueryBuilder queryBuilder) {
			this.queryBuilder = queryBuilder;
		}
		
		public void keyPressed(KeyEvent e)	{
			logger.trace("IN");
				
			if (e.keyCode == SWT.DEL)
			{
				logger.debug("Delete pressed");
				int[] selectionIndices = tableViewerHaving.getTable().getSelectionIndices();
				logger.debug("Number of selected elements is equals to [{}]",selectionIndices.length);
				int indexLength = selectionIndices.length;
				Query query = queryBuilder.getQuery();
				if (indexLength > 0)
				{
					logger.debug("Number of selection field in query is equal to [{}]", query.getSelectFields(false).size());
					for (int i = indexLength-1; i >=0; i--){		
						query.removeHavingField(selectionIndices[i]);
						logger.debug("Successfully removed query selection field at index equal to [{}]", selectionIndices[i]);
					}
					logger.debug("Number of selection field in query is equal to [{}]", query.getHavingFields().size());
					//Assert.assertTrue("Unable to delete alla select fields from query", selectFieldsNumber - query.getSelectFields(false).size() == indexLength);
					tableViewerHaving.setInput(query.getHavingFields());
					tableViewerHaving.refresh();
					queryBuilder.setDirtyEditor();
				}

			}
			logger.trace("OUT");
		}
	}
	
	
	
	public TableViewer getViewer() {
		return tableViewerHaving;
	}
	
}
