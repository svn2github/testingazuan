/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.qbe.services.formviewer;

import it.eng.qbe.query.CriteriaConstants;
import it.eng.qbe.query.ExpressionNode;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.WhereField;
import it.eng.qbe.query.transformers.AbstractQbeQueryTransformer;
import it.eng.spagobi.engines.qbe.bo.FormViewerState;
import it.eng.spagobi.engines.qbe.template.QbeJSONTemplateParser;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Transforms the input query starting from document template and form viewer state 
 * 
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class FormViewerQueryTransformer extends AbstractQbeQueryTransformer {

	public static transient Logger logger = Logger.getLogger(FormViewerQueryTransformer.class);
	
	private JSONObject template;
	private JSONObject formState;
	private FormViewerState formViewerState = null;
	
	public JSONObject getFormState() {
		return formState;
	}

	public void setFormState(JSONObject formState) {
		this.formState = formState;
	}

	public JSONObject getTemplate() {
		return template;
	}

	public void setTemplate(JSONObject template) {
		this.template = template;
	}

	@Override
	public Query execTransformation(Query query) {
		logger.debug("IN");
		try {
			formViewerState = new FormViewerState(formState);
			applyStaticClosedFilters(query);
			applyStaticOpenFilters(query);
			applyDynamicFilters(query);
		} catch (Exception e) {
			logger.error(e);
			// TODO manage exception
		} finally {
			logger.debug("OUT");
		}
		return query;
	}

	private void applyStaticClosedFilters(Query query) throws Exception {
		logger.debug("IN");
		JSONArray staticClosedFilters = template.optJSONArray(QbeJSONTemplateParser.STATIC_CLOSED_FILTERS);
		if (staticClosedFilters != null && staticClosedFilters.length() > 0) {
			for (int i = 0; i < staticClosedFilters.length(); i++) {
				JSONObject aStaticClosedFilter = (JSONObject) staticClosedFilters.get(i);
				boolean isSingleSelection = aStaticClosedFilter.optBoolean(QbeJSONTemplateParser.STATIC_CLOSED_FILTER_SINGLE_SELECTION);
				if (isSingleSelection) {
					String id = aStaticClosedFilter.getString(QbeJSONTemplateParser.ID);
					String option = formViewerState.getXORFilterSelectedOption(id);
					if (option != null && !option.trim().equals("") && !option.equalsIgnoreCase(QbeJSONTemplateParser.STATIC_CLOSED_FILTER_NO_SELECTION)) {
						JSONObject filter = null;
						JSONArray filters = aStaticClosedFilter.getJSONArray(QbeJSONTemplateParser.FILTERS);
						for (int j = 0; j < filters.length(); j++) {
							JSONObject temp = filters.getJSONObject(j);
							if (temp.getString(QbeJSONTemplateParser.ID).equals(option)) {
								filter = temp;
								break;
							}
						}
						if (filter != null) {
							applyStaticClosedFilter(query, filter, id);
						}
					}
				} else {
					JSONArray filters = aStaticClosedFilter.getJSONArray(QbeJSONTemplateParser.FILTERS);
					for (int j = 0; j < filters.length(); j++) {
						JSONObject filter = filters.getJSONObject(j);
						boolean isActive = formViewerState.isOnOffFilterActive(filter.getString(QbeJSONTemplateParser.ID));
						if (isActive) {
							applyStaticClosedFilter(query, filter, filter.getString(QbeJSONTemplateParser.ID));
						}
					}
				}
			}
			
		}
		logger.debug("OUT");
	}
	
	
	private void applyStaticClosedFilter(Query query, JSONObject filter, String id) throws Exception {
		JSONArray expression = filter.optJSONArray("expression");
		ExpressionNode node = null;
		if (expression == null) {
			node = addWhereConditionFromClosedfilter(query, filter, id);
		} else {
			node = addWhereExpressionFromClosedfilter(query, expression);
		}
		updateWhereClauseStructure(query, node, "AND");
	}
	
	private ExpressionNode addWhereConditionFromClosedfilter(Query query, JSONObject filter, String id) throws Exception {
		WhereField.Operand leftOperand = new WhereField.Operand(filter.getString("leftOperandValue"), null, "Field Content", null, null);
		WhereField.Operand rightOperand = null;
		if (filter.optString("rightOperandValue") != null) {
			rightOperand = new WhereField.Operand(filter.getString("rightOperandValue"), null, "Static Value", null, null);
		}
		query.addWhereField(id, null, false, leftOperand, filter.getString("operator"), rightOperand, "AND");
		
		ExpressionNode newFilterNode = new ExpressionNode("NODE_CONST", "$F{" + id + "}");
		return newFilterNode;
	}
	
	
	private ExpressionNode addWhereExpressionFromClosedfilter(Query query, JSONArray expression) throws Exception {
		String operator = expression.getString(1);
		ExpressionNode node = new ExpressionNode("NODE_OP", operator);
		
		JSONObject filter1 = expression.getJSONObject(0);
		String id = UUID.randomUUID().toString();
		addWhereConditionFromClosedfilter(query, filter1, id);
		node.addChild(new ExpressionNode("NODE_CONST", "$F{" + id + "}"));
		
		JSONObject filter2 = expression.getJSONObject(2);
		id = UUID.randomUUID().toString();
		addWhereConditionFromClosedfilter(query, filter2, id);
		node.addChild(new ExpressionNode("NODE_CONST", "$F{" + id + "}"));
		
		return node;
	}
	
	private void updateWhereClauseStructure(Query query, String filterId,
			String booleanConnector) {
		ExpressionNode node = query.getWhereClauseStructure();
		ExpressionNode newFilterNode = new ExpressionNode("NODE_CONST", "$F{" + filterId + "}");
		if (node == null) {
			node = newFilterNode;
			query.setWhereClauseStructure(node);
		} else {
			if (node.getType() == "NODE_OP" && node.getValue().equals(booleanConnector)) {
				node.addChild(newFilterNode);
			} else {
				ExpressionNode newNode = new ExpressionNode("NODE_OP", booleanConnector);
				newNode.addChild(node);
				newNode.addChild(newFilterNode);
				query.setWhereClauseStructure(newNode);
			}
		}
	}

	private void updateWhereClauseStructure(Query query, ExpressionNode nodeToInsert,
			String booleanConnector) {
		ExpressionNode node = query.getWhereClauseStructure();
		if (node == null) {
			node = nodeToInsert;
			query.setWhereClauseStructure(node);
		} else {
			ExpressionNode newNode = new ExpressionNode("NODE_OP", booleanConnector);
			newNode.addChild(node);
			newNode.addChild(nodeToInsert);
			query.setWhereClauseStructure(newNode);
		}
	}
	
	private void applyStaticOpenFilters(Query query) throws Exception {
		logger.debug("IN");
		JSONArray staticOpenFilters = template.optJSONArray(QbeJSONTemplateParser.STATIC_OPEN_FILTERS);
		if (staticOpenFilters != null && staticOpenFilters.length() > 0) {
			for (int i = 0; i < staticOpenFilters.length(); i++) {
				JSONObject filter = (JSONObject) staticOpenFilters.get(i);
				String id = filter.getString(QbeJSONTemplateParser.ID);
				List<String> values = formViewerState.getOpenFilterValues(id);
				if (values.size() > 0) {
					StringBuffer buffer = new StringBuffer(values.get(0));
					if (values.size() > 1) {
						for (int c = 1; c < values.size(); c++) {
							buffer.append("," + values.get(c));
						}
					}
					String operator = filter.getString("operator");
					if (operator.equals(CriteriaConstants.EQUALS_TO)) {
						operator = CriteriaConstants.IN;
					}
					WhereField.Operand leftOperand = new WhereField.Operand(filter.getString("field"), null, "Field Content", null, null);
					WhereField.Operand rightOperand = new WhereField.Operand(buffer.toString(), null, "Static Value", null, null);
					query.addWhereField(id, null, false, leftOperand, operator, rightOperand, "AND");
					updateWhereClauseStructure(query, filter.getString(QbeJSONTemplateParser.ID), "AND");
				}
			}
		}
		logger.debug("OUT");
	}

	private void applyDynamicFilters(Query query) throws Exception {
		logger.debug("IN");
		JSONArray dynamicFilters = template.optJSONArray(QbeJSONTemplateParser.DYNAMIC_FILTERS);
		if (dynamicFilters != null && dynamicFilters.length() > 0) {
			for (int i = 0; i < dynamicFilters.length(); i++) {
				JSONObject filter = (JSONObject) dynamicFilters.get(i);
				String id = filter.getString(QbeJSONTemplateParser.ID);
				String field = formViewerState.getDynamicFilterField(id);
				if (field != null && !field.trim().equals("")) {
					WhereField.Operand leftOperand = new WhereField.Operand(field, null, "Field Content", null, null);
					String operator = filter.getString(QbeJSONTemplateParser.OPERATOR);
					WhereField.Operand rightOperand = null;
					if (operator.equalsIgnoreCase("BETWEEN")) {
						List<String> fromToValues = formViewerState.getDynamicFilterFromToValues(id);
						rightOperand = new WhereField.Operand(fromToValues.get(0) + "," + fromToValues.get(1), null, "Static Value", null, null);
					} else {
						String value = formViewerState.getDynamicFilterValue(id);
						rightOperand = new WhereField.Operand(value, null, "Static Value", null, null);
					}
					query.addWhereField(id, null, false, leftOperand, filter.getString("operator"), rightOperand, "AND");
					updateWhereClauseStructure(query, id, "AND");
				}
			}
		}
		logger.debug("OUT");
	}

}
