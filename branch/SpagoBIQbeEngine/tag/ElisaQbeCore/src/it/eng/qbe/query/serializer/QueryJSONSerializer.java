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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.ExpressionNode;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.SelectField;
import it.eng.qbe.query.WhereField;
import it.eng.spagobi.utilities.assertion.Assert;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QueryJSONSerializer implements QuerySerializer {

		
	public Object serialize(Query query, DataMartModel datamartModel) throws SerializationException {
		JSONObject  result = null;
		
		JSONArray recordsJOSN;
		JSONArray filtersJSON;
		JSONObject filterExpJOSN;
		boolean distinctClauseEnabled = false;
		
		Assert.assertNotNull(query, "Query cannot be null");
		Assert.assertNotNull(query.getId(), "Query id cannot be null");
		Assert.assertNotNull(datamartModel, "DataMartModel cannot be null");
		
		try {
			
			
			recordsJOSN = serializeFields(query, datamartModel);			
			filtersJSON = serializeFilters(query, datamartModel);
			distinctClauseEnabled = query.isDistinctClauseEnabled();
			filterExpJOSN = encodeFilterExp( query.getWhereClauseStructure() );
			
			result = new JSONObject();
			result.put(SerializationConstants.ID, query.getId());
			result.put(SerializationConstants.FIELDS, recordsJOSN);
			result.put(SerializationConstants.DISTINCT, distinctClauseEnabled);
			result.put(SerializationConstants.FILTERS, filtersJSON);
			result.put(SerializationConstants.EXPRESSION, filterExpJOSN);
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
	private JSONArray serializeFields(Query query, DataMartModel datamartModel) throws SerializationException {
		JSONArray result;
		
		List fields;
		SelectField field;
		String fieldUniqueName;
		DataMartField datamartField;
		JSONObject fieldJSON;
		Iterator it;
		
		
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
				fieldJSON.put(SerializationConstants.FIELD_ENTITY, datamartField.getParent().getName());
				fieldJSON.put(SerializationConstants.FIELD_NAME, datamartField.getName());
				fieldJSON.put(SerializationConstants.FIELD_ALIAS, field.getAlias());
				if( field.isGroupByField() ) {
					fieldJSON.put(SerializationConstants.FIELD_GROUP, "true");
				} else {
					fieldJSON.put(SerializationConstants.FIELD_GROUP, "");
				}
				fieldJSON.put(SerializationConstants.FIELD_ORDER, field.getOrderType());
				fieldJSON.put(SerializationConstants.FIELD_AGGREGATION_FUNCTION, field.getFunction().getName());
				fieldJSON.put(SerializationConstants.FIELD_VISIBLE, field.isVisible());
			} catch(JSONException e) {
				throw new SerializationException("An error occurred while serializing field: " + fieldUniqueName, e);
			}
			
			result.put(fieldJSON);
		}
		
		return result;
	}	
	
	
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
	private JSONArray serializeFilters(Query query, DataMartModel datamartModel) throws SerializationException {
		JSONArray filtersJOSN = new JSONArray();
		
		List filters;
		WhereField filter;
		JSONObject filterJSON;
		DataMartField datamartFilter;
		String fieldUniqueName;
		Iterator it;
		
		filters = query.getWhereFields();
		Assert.assertNotNull(filters, "Filters cannot be null");
		
		
		it = filters.iterator();
		while( it.hasNext() ) {
			filter = (WhereField)it.next();
			fieldUniqueName = filter.getUniqueName();
			datamartFilter = datamartModel.getDataMartModelStructure().getField( fieldUniqueName );
			Assert.assertNotNull(datamartFilter, "A filed named [" + fieldUniqueName + "] does not exist in the datamart model");
			
			filterJSON = new JSONObject();
			try {
				filterJSON.put(SerializationConstants.FILTER_ID, fieldUniqueName);
				filterJSON.put(SerializationConstants.FILTER_NAME, filter.getFname());
				filterJSON.put(SerializationConstants.FILTER_DESCRIPTION, filter.getFdesc());
				filterJSON.put(SerializationConstants.FILTER_ENTITY, datamartFilter.getParent().getName());
				filterJSON.put(SerializationConstants.FILTER_FIELD, datamartFilter.getName());
				filterJSON.put(SerializationConstants.FILTER_OPEARTOR, filter.getOperator());
				filterJSON.put(SerializationConstants.FILTER_OPEARND, filter.getOperand().toString());
				filterJSON.put(SerializationConstants.FILTER_OPEARND_DESCRIPTION, filter.getOperandDesc());			
				filterJSON.put(SerializationConstants.FILTER_OPEARND_TYPE, filter.getOperandType());
				filterJSON.put(SerializationConstants.FILTER_BOOLEAN_CONNETOR, filter.getBoperator());
				filterJSON.put(SerializationConstants.FILTER_IS_FREE, filter.isFree());
			} catch(JSONException e) {
				throw new SerializationException("An error occurred while serializing filter on field: " + fieldUniqueName, e);
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
