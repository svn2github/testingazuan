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
import it.eng.qbe.query.WhereField;
import it.eng.qbe.query.HavingField.Operand;
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
import it.eng.spagobi.meta.querybuilder.edit.HavingRightOperandColumnEditingSupport;
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
public class WhereFieldTable extends AbstractQueryEditTable {

	private TableViewer tableViewerWhere;
	private QueryBuilder queryBuilder;

	private static Logger logger = LoggerFactory.getLogger(HavingFieldTable.class);
	private static final IResourceLocator RL = SpagoBIMetaQueryBuilderPlugin.getInstance().getResourceLocator();
	private static final Image CHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.checked") ).createImage();
	private static final Image UNCHECKED = ImageDescriptor.createFromURL( (URL)RL.getImage("ui.shared.edit.tables.button.unchecked") ).createImage();



	public WhereFieldTable(Composite container, QueryBuilder queryBuilder) {
		super(container, SWT.NONE);
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label selectFieldLabel = new Label(this, SWT.NONE);
		selectFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		selectFieldLabel.setText("Where Clause");
		
		
//		Composite bottonContainer = new Composite(container, SWT.NONE);
//		bottonContainer.setLayout(new RowLayout());
//		Label lblWhereClauses = new Label(bottonContainer, SWT.CENTER);
//		lblWhereClauses.setText("Where Clause");
//		Button cleanButton = new Button(bottonContainer, SWT.PUSH);

		tableViewerWhere  = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerWhere.setColumnProperties(new String[] { "Filter Name", "Left Operand", "Operator","Right Operand", "Is for prompt", "Bol. connector" });
		
		Table table = tableViewerWhere.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		createEditWhereColumns(container);
		
		tableViewerWhere.setContentProvider(new ArrayContentProvider());
		Query query = queryBuilder.getQuery();
		tableViewerWhere.setInput(query.getWhereFields());		
		
//		cleanButton.setText("Clean");
//		cleanButton.addListener(SWT.Selection, new WhereClearListener(tableViewerWhere));
		
		//Drop support
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer()  };
		tableViewerWhere.addDropSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDropWhereListener(tableViewerWhere, queryBuilder));
	}

	private class WhereClearListener implements Listener{
		
		TableViewer tableViewerWhere;
		
		public WhereClearListener(TableViewer tableViewerHaving){
			this.tableViewerWhere = tableViewerHaving;
		}
		
		public void handleEvent(Event event) {
			queryBuilder.getQuery().setWhereClauseStructure(null);
			queryBuilder.getQuery().getWhereFields().clear();
			if(tableViewerWhere!=null){
				tableViewerWhere.refresh();
			}
		}
	}
	
	public void createEditWhereColumns(final Composite parent){
		String[] columnsTitles = { "Filter Name", "Left Operand", "Operator", "Right Operand", "Is for prompt", "Bol. connector" };
		int[] columnsBounds = { 100, 100, 100, 100, 50, 50 };	

		//Filter Name Column
		TableViewerColumn col = createTableViewerColumn(columnsTitles[0], columnsBounds[0], 0, tableViewerWhere);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField field = (WhereField) element;
				return field.getName();
			}
		});	
		col.setEditingSupport(new FilterColumnEditingSupport(tableViewerWhere, queryBuilder));

		
		//Left Operand Column
		col = createTableViewerColumn(columnsTitles[1], columnsBounds[1], 1, tableViewerWhere);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				String leftOperand = (whereClause.getLeftOperand()!=null)?whereClause.getLeftOperand().description:"";
				return leftOperand;
			}
		});	
		
		//Operator Column
		col = createTableViewerColumn(columnsTitles[2], columnsBounds[2], 2, tableViewerWhere);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				return whereClause.getOperator();
			}
		});		
		col.setEditingSupport(new OperatorColumnEditingSupport(tableViewerWhere));

		
		//Right Operand Column
		col = createTableViewerColumn(columnsTitles[3], columnsBounds[3], 3, tableViewerWhere);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				String rightOperand = (whereClause.getRightOperand()!=null)?whereClause.getRightOperand().description:"";
				return rightOperand;
			}
		});	
		col.setEditingSupport(new FilterRightOperandColumnEditingSupport(tableViewerWhere));
		
		//Is for Prompt Column
		col = createTableViewerColumn(columnsTitles[4], columnsBounds[4], 4, tableViewerWhere);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				return "";//+whereClause.isPromptable();
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
		col.setEditingSupport(new IsForPromptColumnEditingSupport(tableViewerWhere));

		
		//Bol. Connector Column
		col = createTableViewerColumn(columnsTitles[5], columnsBounds[5], 5, tableViewerWhere);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				WhereField whereClause = (WhereField) element;
				return whereClause.getBooleanConnector();
			}
		});		
		col.setEditingSupport(new BooleanConnectorColumnEditingSupport(tableViewerWhere, queryBuilder));
	}	
	
	
	public TableViewer getViewer() {
		return tableViewerWhere;
	}
	
}
