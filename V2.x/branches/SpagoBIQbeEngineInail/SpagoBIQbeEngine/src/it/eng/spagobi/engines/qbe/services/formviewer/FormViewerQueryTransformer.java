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
							WhereField.Operand leftOperand = new WhereField.Operand(filter.getString("leftOperandValue"), null, "Field Content", null, null);
							WhereField.Operand rightOperand = new WhereField.Operand(filter.getString("rightOperandValue"), null, "Static Value", null, null);
							query.addWhereField(id, null, false, leftOperand, filter.getString("operator"), rightOperand, "AND");
							updateWhereClauseStructure(query, id, "AND");
						}
					}
				} else {
					JSONArray filters = aStaticClosedFilter.getJSONArray(QbeJSONTemplateParser.FILTERS);
					for (int j = 0; j < filters.length(); j++) {
						JSONObject filter = filters.getJSONObject(j);
						boolean isActive = formViewerState.isOnOffFilterActive(filter.getString(QbeJSONTemplateParser.ID));
						if (isActive) {
							WhereField.Operand leftOperand = new WhereField.Operand(filter.getString("leftOperandValue"), null, "Field Content", null, null);
							WhereField.Operand rightOperand = new WhereField.Operand(filter.getString("rightOperandValue"), null, "Static Value", null, null);
							query.addWhereField(filter.getString(QbeJSONTemplateParser.ID), null, false, leftOperand, filter.getString("operator"), rightOperand, "AND");
							updateWhereClauseStructure(query, filter.getString(QbeJSONTemplateParser.ID), "AND");
						}
					}
				}
			}
			
		}
		logger.debug("OUT");
	}
	
	private void updateWhereClauseStructure(Query query, String filterId,
			String booleanConnector) {
		ExpressionNode node = query.getWhereClauseStructure();
		if (node == null) {
			node = new ExpressionNode("NODE_OP", booleanConnector);
			ExpressionNode child = new ExpressionNode("NODE_CONST", "$F{" + filterId + "}");
			node.addChild(child);
			query.setWhereClauseStructure(node);
		} else {
			ExpressionNode child = new ExpressionNode("NODE_OP", "AND");
			ExpressionNode nephew = new ExpressionNode("NODE_CONST", "$F{" + filterId + "}");
			child.addChild(nephew);
			node.addChild(child);
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
