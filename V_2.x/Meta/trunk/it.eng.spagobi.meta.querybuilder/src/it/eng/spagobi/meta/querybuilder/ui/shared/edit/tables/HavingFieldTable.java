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
import it.eng.qbe.query.Query;
import it.eng.qbe.query.HavingField.Operand;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label selectFieldLabel = new Label(this, SWT.NONE);
		selectFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectFieldLabel.setText("Having Clause");

//		Composite bottonContainer = new Composite(container, SWT.NONE);
//		bottonContainer.setLayout(new RowLayout());
//		Label lblHavingClause = new Label(bottonContainer, SWT.NONE);
//		lblHavingClause.setText("Having Clause");
//		Button cleanButton = new Button(bottonContainer, SWT.PUSH);

		tableViewerHaving  = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerHaving.setColumnProperties(new String[] { "Filter Name","Function", "Left Operand", "Operator", "Function","Right Operand", "Is for prompt", "Bol. connector" });
		
		Table table = tableViewerHaving.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		createEditHavingColumns(container);
		
		tableViewerHaving.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		tableViewerHaving.setInput(query.getHavingFields());		
		
//		cleanButton.setText("Clean");
//		cleanButton.addListener(SWT.Selection, new HavingClearListener(tableViewerHaving));
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerHaving.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropHavingListener(tableViewerHaving, queryBuilder));
	}

	private class HavingClearListener implements Listener{
		
		TableViewer tableViewerHaving;
		
		public HavingClearListener(TableViewer tableViewerHaving){
			this.tableViewerHaving = tableViewerHaving;
		}
		
		public void handleEvent(Event event) {
			queryBuilder.getQuery().getHavingFields().clear();
			if(tableViewerHaving!=null){
				tableViewerHaving.refresh();
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
		col.setEditingSupport(new HavingFilterColumnEditingSupport(tableViewerHaving));

		
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
		col.setEditingSupport(new HavingLeftFunctionColumnEditingSupport(tableViewerHaving));

		
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
		col.setEditingSupport(new HavingOperatorColumnEditingSupport(tableViewerHaving));

		
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
		col.setEditingSupport(new HavingRightFunctionColumnEditingSupport(tableViewerHaving));

		
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
		col.setEditingSupport(new HavingRightOperandColumnEditingSupport(tableViewerHaving));
		
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
		col.setEditingSupport(new HavingIsForPromptColumnEditingSupport(tableViewerHaving));

		
		//Bol. Connector Column
		col = createTableViewerColumn(columnsTitles[7], columnsBounds[7], 7, tableViewerHaving);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				HavingField havingClause = (HavingField) element;
				return havingClause.getBooleanConnector();
			}
		});	
		col.setEditingSupport(new HavingBooleanConnectorColumnEditingSupport(tableViewerHaving));

	}
	
	
	public TableViewer getViewer() {
		return tableViewerHaving;
	}
	
}
