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


import java.util.List;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.model.structure.filter.IQbeTreeEntityFilter;
import it.eng.qbe.model.structure.filter.IQbeTreeFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.qbe.model.structure.filter.QbeTreeFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderEntityFilter;
import it.eng.qbe.model.structure.filter.QbeTreeOrderFieldFilter;
import it.eng.qbe.query.ExpressionNode;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.WhereField;
import it.eng.qbe.query.WhereField.Operand;
import it.eng.qbe.statement.AbstractStatement;
import it.eng.spagobi.meta.querybuilder.dnd.QueryBuilderDragListener;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables.QueryEditGroup;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree.ModelTreeViewer;
import it.eng.spagobi.meta.querybuilder.ui.shared.result.ResultTableViewer;
import it.eng.spagobi.tools.dataset.common.query.AggregationFunctions;
import it.eng.spagobi.tools.dataset.common.query.IAggregationFunction;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author cortella
 *
 */
public class QueryBuilder {

	protected IModelStructure modelView;
	protected Query query;
	protected IDataSource dataSource;
	private int whereFilterCount=1;
	private int havingFilterCount=1;
	private ResultTableViewer tableViewer = null;
	private QueryEditGroup compositeFilters = null;
	
	private static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
	
	
	public QueryBuilder(IDataSource dataSource){
		logger.debug("Creating QueryBuilder with DataSource [{}]",dataSource.getName());
		this.dataSource = dataSource;

		IQbeTreeEntityFilter entityFilter = null;
		IQbeTreeFieldFilter fieldFilter = null;
		entityFilter = new QbeTreeAccessModalityEntityFilter();
		entityFilter = new QbeTreeOrderEntityFilter(entityFilter);
		fieldFilter = new QbeTreeAccessModalityFieldFilter();
		fieldFilter = new QbeTreeOrderFieldFilter(fieldFilter);
		QbeTreeFilter filters =  new QbeTreeFilter(entityFilter, fieldFilter);
		IModelStructure iDatamartModelStructure = dataSource.getModelStructure();
		this.modelView =  new ViewModelStructure(iDatamartModelStructure, dataSource, filters);
		this.query = new Query();
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
		createEditGroup(composite);

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
		ModelTreeViewer businessModelTreeViewer = new ModelTreeViewer(groupBusinessModelTree, dataSource, modelView, this);
		Transfer[] transferTypes = new Transfer[]{ TextTransfer.getInstance(),LocalSelectionTransfer.getTransfer()  };
		businessModelTreeViewer.addDragSupport(DND.DROP_MOVE, transferTypes, new QueryBuilderDragListener(businessModelTreeViewer));
	}

	public QueryEditGroup createEditGroup(Composite composite){
		compositeFilters = new QueryEditGroup(composite, this);
		refreshQueryEditGroup();
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
		tableViewer = new ResultTableViewer(groupQueryResult, this);
		return tableViewer;
	}
	
	public void refreshQueryResultPage(){
		if(tableViewer!=null){
			tableViewer.updateTable();
		}
	}
	
	public void refreshQueryEditGroup(){
		if(compositeFilters!=null){
			compositeFilters.refresh(query);
		}
	}
	
	public void refreshSelectFields(){
		compositeFilters.refreshSelectTable(query.getSelectFields(true));
	}

	public Query addWhereField(IModelField dataMartField){
		String fieldName = dataMartField.getParent().getName()+" : "+dataMartField.getName();
		return addWhereField(dataMartField.getUniqueName(), fieldName);
	}

	public Query addWhereField(String uniqueName, String fieldName){

		String[] values = new String[1];
		values[0] = uniqueName;

		Operand leftOperand = new Operand(values,fieldName, AbstractStatement.OPERAND_TYPE_FIELD, values,values);
		query.addWhereField("Filter"+whereFilterCount, "Filter"+whereFilterCount, true, leftOperand, "NONE", null, "AND");
		ExpressionNode node = query.getWhereClauseStructure();
		if(node==null){
			node = new ExpressionNode("NO_NODE_OP","$F{Filter" +whereFilterCount+"}");
			query.setWhereClauseStructure(node);
		}else{
			//get the previous field
			WhereField previousAddedField = (WhereField)query.getWhereFields().get(query.getWhereFields().size()-2);
			ExpressionNode operationNode = new ExpressionNode("NODE_OP", previousAddedField.getBooleanConnector());
			ExpressionNode filterNode = new ExpressionNode("NO_NODE_OP","$F{Filter" +whereFilterCount+"}");
			operationNode.addChild(node);
			operationNode.addChild(filterNode);
			query.setWhereClauseStructure(operationNode);
		}
		whereFilterCount++;
		return query;
	}

	public Query addHavingField(IModelField dataMartField){	
		String fieldName = dataMartField.getParent().getName()+" : "+dataMartField.getName();
		return addHavingField(dataMartField.getUniqueName(),fieldName, AggregationFunctions.NONE_FUNCTION);
	}

	public Query addHavingField(String uniqueName, String fieldName,IAggregationFunction aggregation){

		String[] values = new String[1];
		values[0] = uniqueName;

		it.eng.qbe.query.HavingField.Operand leftOperand = new it.eng.qbe.query.HavingField.Operand(values, fieldName, AbstractStatement.OPERAND_TYPE_FIELD, values, values,aggregation);
		query = getQuery();
		query.addHavingField("Having"+havingFilterCount, "Having"+havingFilterCount, false, leftOperand, "NONE", null, "AND");

		havingFilterCount++;
		return query;

	}
	
	public Query addSelectFields(Object selectionData){
		logger.debug("SelectionData: "+selectionData.getClass().getName());
		if (selectionData instanceof IModelEntity){
   			logger.debug("Dragghed the entity [{}]",((IModelEntity)selectionData).getUniqueName());
   			IModelEntity dataMartEntity = (IModelEntity)selectionData;
			List<IModelField> dataMartFields = dataMartEntity.getAllFields();
			for (IModelField dataMartField : dataMartFields){
				addField(dataMartField);
			}        	
        } else if(selectionData instanceof IModelField){
        	addField((IModelField)selectionData);
        }
		return query;
	}
	
	public void addField(IModelField dataMartField) {
		query.addSelectFiled(dataMartField.getUniqueName(), "NONE", dataMartField.getName(), true, true, false, null, dataMartField.getPropertyAsString("format"));
	}
	
	public IDataSource getDataSource() {
		return dataSource;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public IModelStructure getModelView() {
		return modelView;
	}

	public IModelStructure getBaseModelStructure() {
		return modelView;
	}

}
