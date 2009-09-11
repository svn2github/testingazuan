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
package it.eng.qbe.query.serializer;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.cache.QbeCacheManager;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.ExpressionNode;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.SelectField;
import it.eng.qbe.query.WhereField;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.assertion.Assert;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QueryJSONSerializer implements QuerySerializer {

		
	public Object serialize(Query query, DataMartModel datamartModel, Locale locale) throws SerializationException {
		JSONObject  result = null;
		
		JSONArray recordsJOSN;
		JSONArray filtersJSON;
		JSONObject filterExpJOSN;
		boolean distinctClauseEnabled = false;		
		JSONArray subqueriesJSON;
		JSONObject subqueryJSON;
		Iterator subqueriesIterator;
		Query subquery;
		
		Assert.assertNotNull(query, "Query cannot be null");
		Assert.assertNotNull(query.getId(), "Query id cannot be null");
		Assert.assertNotNull(datamartModel, "DataMartModel cannot be null");
		
		try {
			
			
			recordsJOSN = serializeFields(query, datamartModel, locale);			
			filtersJSON = serializeFilters(query, datamartModel, locale);
			filterExpJOSN = encodeFilterExp( query.getWhereClauseStructure() );
			
			subqueriesJSON = new JSONArray();
			subqueriesIterator = query.getSubqueryIds().iterator();		
			while(subqueriesIterator.hasNext()) {
				String id = (String)subqueriesIterator.next();
				subquery = query.getSubquery(id);
				subqueryJSON = (JSONObject)serialize(subquery, datamartModel, locale);
				subqueriesJSON.put(subqueryJSON);
			} 
			
			
			result = new JSONObject();
			result.put(SerializationConstants.ID, query.getId());
			result.put(SerializationConstants.NAME, query.getName());
			result.put(SerializationConstants.DESCRIPTION, query.getDescription());
			result.put(SerializationConstants.DISTINCT, query.isDistinctClauseEnabled());
			result.put(SerializationConstants.IS_NESTED_EXPRESSION, query.isNestedExpression());
			
			result.put(SerializationConstants.FIELDS, recordsJOSN);
			
			result.put(SerializationConstants.FILTERS, filtersJSON);
			result.put(SerializationConstants.EXPRESSION, filterExpJOSN);
			
			result.put(SerializationConstants.SUBQUERIES, subqueriesJSON);
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + query, t);
		} finally {
			
		}
		
		return result;
	}
	
	
	/*
	 { 
	  "id" : "it.eng.spagobi.ProductClass:productClassId",
	  "entity" : "ProductClass",
	  "field"  : "productClassId",
	  "alias"  : "",
	  "group"  : "undefined",
	  "order"  : "",
	  "funct"  : "",
	  "visible" : "si"
	 }
	 */
	private JSONArray serializeFields(Query query, DataMartModel datamartModel, Locale locale) throws SerializationException {
		JSONArray result;
		
		List fields;
		SelectField field;
		String fieldUniqueName;
		DataMartField datamartField;
		JSONObject fieldJSON;
		Iterator it;
		DatamartLabels datamartLabels;
		
		datamartLabels = null;
		if(locale != null) {
			datamartLabels =  QbeCacheManager.getInstance().getLabels( datamartModel , locale );
		}
		
		fields = query.getSelectFields();
		Assert.assertNotNull(fields, "Fields cannot be null");
		
		result = new JSONArray();
		it = fields.iterator();
		while( it.hasNext() ) {
			field = (SelectField)it.next();
			fieldUniqueName = field.getUniqueName();
			datamartField = datamartModel.getDataMartModelStructure().getField( fieldUniqueName );
			Assert.assertNotNull(datamartField, "A filed named [" + fieldUniqueName + "] does not exist in the datamart model");
						
			
			
			fieldJSON = new JSONObject();
			try {
				fieldJSON.put(SerializationConstants.FIELD_ID, datamartField.getUniqueName());
				
				
				String label;
				label = null;
				if(datamartLabels != null) {
					label = datamartLabels.getLabel( datamartField.getParent() );
				}
				label = StringUtilities.isEmpty(label)? datamartField.getParent().getName(): label;
				fieldJSON.put(SerializationConstants.FIELD_ENTITY, label);
				
				label = null;
				if(datamartLabels != null) {
					label = datamartLabels.getLabel( datamartField );
				}
				label = StringUtilities.isEmpty(label)? datamartField.getName(): label;
				fieldJSON.put(SerializationConstants.FIELD_NAME, label);
				
				fieldJSON.put(SerializationConstants.FIELD_ALIAS, field.getAlias());
				if( field.isGroupByField() ) {
					fieldJSON.put(SerializationConstants.FIELD_GROUP, "true");
				} else {
					fieldJSON.put(SerializationConstants.FIELD_GROUP, "");
				}
				fieldJSON.put(SerializationConstants.FIELD_ORDER, field.getOrderType());
				fieldJSON.put(SerializationConstants.FIELD_AGGREGATION_FUNCTION, field.getFunction().getName());
				fieldJSON.put(SerializationConstants.FIELD_VISIBLE, field.isVisible());
				fieldJSON.put(SerializationConstants.FIELD_INCLUDE, field.isIncluded());
			} catch(JSONException e) {
				throw new SerializationException("An error occurred while serializing field: " + fieldUniqueName, e);
			}
			
			result.put(fieldJSON);
		}
		
		return result;
	}	
	
	/*
	 
				Iterator it = query.getSelectFields().iterator();
				while( it.hasNext() ) {
					SelectField selectField = (SelectField)it.next();
					DataMartField datamartField = getDatamartModel().getDataMartModelStructure().getField(selectField.getUniqueName());
					String label;
					label = datamartLabels.getLabel(datamartField);
					label =  StringUtilities.isEmpty(label)? datamartField.getName(): label;
				} 
	 */
	
	/*
	{
	  "id" : "it.eng.spagobi.ProductClass:productClassId",
	  "entity" : "ProductClass",
	  "field"  : "productClassId",
	  //"alias"  : "",
	  "operator"  : "GREATER THAN",
	  "value"  : "5",
	  "type"  : "Static Value"
	  }
	 */
	private JSONArray serializeFilters(Query query, DataMartModel datamartModel, Locale locale) throws SerializationException {
		JSONArray filtersJOSN = new JSONArray();
		
		List filters;
		WhereField filter;
		WhereField.Operand operand;
		JSONObject filterJSON;
		DataMartField datamartFilter;
		String fieldUniqueName;
		Iterator it;
		DatamartLabels datamartLabels;
		DataMartField datamartField;
		
		filters = query.getWhereFields();
		Assert.assertNotNull(filters, "Filters cannot be null");
		
		datamartLabels = null;
		if(locale != null) {
			datamartLabels =  QbeCacheManager.getInstance().getLabels( datamartModel , locale );
		}
		
		it = filters.iterator();
		while( it.hasNext() ) {
			filter = (WhereField)it.next();
			
			filterJSON = new JSONObject();
			try {
				filterJSON.put(SerializationConstants.FILTER_ID, filter.getName());
				filterJSON.put(SerializationConstants.FILTER_DESCRIPTION, filter.getDescription());
				filterJSON.put(SerializationConstants.FILTER_PROMPTABLE, filter.isPromptable());
				
				operand = filter.getLeftOperand();
				filterJSON.put(SerializationConstants.FILTER_LO_VALUE, operand.value);
				if(operand.type.equalsIgnoreCase("Field Content")) {
					datamartField = datamartModel.getDataMartModelStructure().getField( operand.value );
					
					String labelF, labelE;
					labelE = null;
					if(datamartLabels != null) {
						labelE = datamartLabels.getLabel( datamartField.getParent() );
					}
					labelE = StringUtilities.isEmpty(labelE)? datamartField.getParent().getName(): labelE;
					
					
					labelF = null;
					if(datamartLabels != null) {
						labelF = datamartLabels.getLabel( datamartField );
					}
					labelF = StringUtilities.isEmpty(labelF)? datamartField.getName(): labelF;
					
					filterJSON.put(SerializationConstants.FILTER_LO_DESCRIPTION, labelE  + " : " + labelF );
				} else {
					filterJSON.put(SerializationConstants.FILTER_LO_DESCRIPTION, operand.description);
				}
				
				
				
				filterJSON.put(SerializationConstants.FILTER_LO_TYPE, operand.type);
				filterJSON.put(SerializationConstants.FILTER_LO_DEFAULT_VALUE, operand.defaulttValue);
				filterJSON.put(SerializationConstants.FILTER_LO_LAST_VALUE, operand.lastValue);
				
				filterJSON.put(SerializationConstants.FILTER_OPEARTOR, filter.getOperator());
				
				operand = filter.getRightOperand();
				filterJSON.put(SerializationConstants.FILTER_RO_VALUE, operand.value);
				if(operand.type.equalsIgnoreCase("Field Content")) {
					datamartField = datamartModel.getDataMartModelStructure().getField( operand.value );
					
					String labelF, labelE;
					labelE = null;
					if(datamartLabels != null) {
						labelE = datamartLabels.getLabel( datamartField.getParent() );
					}
					labelE = StringUtilities.isEmpty(labelE)? datamartField.getParent().getName(): labelE;
					
					
					labelF = null;
					if(datamartLabels != null) {
						labelF = datamartLabels.getLabel( datamartField );
					}
					labelF = StringUtilities.isEmpty(labelF)? datamartField.getName(): labelF;
					
					filterJSON.put(SerializationConstants.FILTER_RO_DESCRIPTION, labelE  + " : " + labelF );
				} else {
					filterJSON.put(SerializationConstants.FILTER_RO_DESCRIPTION, operand.description);
				}
				filterJSON.put(SerializationConstants.FILTER_RO_TYPE, operand.type);
				filterJSON.put(SerializationConstants.FILTER_RO_DEFAULT_VALUE, operand.defaulttValue);
				filterJSON.put(SerializationConstants.FILTER_RO_LAST_VALUE, operand.lastValue);
				
				filterJSON.put(SerializationConstants.FILTER_BOOLEAN_CONNETOR, filter.getBooleanConnector());
				
			} catch(JSONException e) {
				throw new SerializationException("An error occurred while serializing filter: " + filter.getName(), e);
			}
			filtersJOSN.put(filterJSON);
		}
		
		return filtersJOSN;
	}
	
		
	private JSONObject encodeFilterExp(ExpressionNode filterExp) throws SerializationException {
		JSONObject exp = new JSONObject();
		JSONArray childsJSON = new JSONArray();
		
		if(filterExp == null) return exp;
		
		try {
			exp.put(SerializationConstants.EXPRESSION_TYPE, filterExp.getType()) ;
			exp.put(SerializationConstants.EXPRESSION_VALUE, filterExp.getValue());
			
			for(int i = 0; i < filterExp.getChildNodes().size(); i++) {
				ExpressionNode child = (ExpressionNode)filterExp.getChildNodes().get(i);
				JSONObject childJSON = encodeFilterExp(child);
				childsJSON.put(childJSON);
			}		
			
			exp.put(SerializationConstants.EXPRESSION_CHILDREN, childsJSON);
		} catch(JSONException e) {
			throw new SerializationException("An error occurred while serializing filter expression", e);
		}		
		 
		return exp;
	}	

}
