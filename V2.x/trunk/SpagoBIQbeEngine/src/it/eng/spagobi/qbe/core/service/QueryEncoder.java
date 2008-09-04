/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.qbe.core.service;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.newquery.SelectField;
import it.eng.qbe.newquery.WhereField;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QueryEncoder {
	
	public static Query decode(String query, DataMartModel datamartModel) throws JSONException {
		JSONObject recordJSON = new JSONObject(query);
		return decode(recordJSON.getJSONArray("fields").toString(),
				recordJSON.getJSONArray("filters").toString(),
				datamartModel);
	}
	
	public static Query decode(String queryFields, String queryFilters, DataMartModel datamartModel) throws JSONException {
		Query query = null;
		
		query = new Query();		

		JSONArray recordsJOSN = new JSONArray( queryFields );
		for(int i = 0; i < recordsJOSN.length(); i++) {
			JSONObject recordJSON = recordsJOSN.getJSONObject(i);
			String fieldUniqueName = recordJSON.getString("id");
			String alias = recordJSON.getString("alias") != null 
						   && !recordJSON.getString("alias").trim().equalsIgnoreCase("")
						   ? recordJSON.getString("alias")
						   : "Column_" + (i+1);
			String fieldA = recordJSON.getString("field");
			String group = recordJSON.getString("group");
			String order = recordJSON.getString("order");
			String funct = recordJSON.getString("funct");
			boolean visible = recordJSON.getBoolean("visible");
												
			if(fieldUniqueName != null) {
				// add field
				DataMartField field = datamartModel.getDataMartModelStructure().getField(fieldUniqueName);
				Assert.assertNotNull(field, "Inpossible to retrive from datamart-structure a fild named " + fieldUniqueName + ". Please check select clause in  query: " + queryFields);
				query.addSelectFiled(field.getUniqueName(), funct, alias, visible, group.equalsIgnoreCase("true"), order);
				if(group.equalsIgnoreCase("true")) {
					query.addGroupByField(field.getUniqueName());
				}
				if(order != null && (order.equalsIgnoreCase("ASC") || order.equalsIgnoreCase("DESC")) ) {
					query.addOrderByField(fieldUniqueName, order.equalsIgnoreCase("ASC"));
				}
			}	
		}
		
		JSONArray filtersJOSN = new JSONArray( queryFilters );
		for(int i = 0; i < filtersJOSN.length(); i++) {
			JSONObject filterJSON = filtersJOSN.getJSONObject(i);
			String fieldUniqueName = filterJSON.getString("id");
			//String alias = filterJSON.getString("alias");
			String operator = filterJSON.getString("operator");
			String value = filterJSON.getString("value");
			String type = filterJSON.getString("type");
			
			if(operator == null || operator.trim().equalsIgnoreCase("") || operator.equalsIgnoreCase("NONE")) continue;
			
			DataMartField field = datamartModel.getDataMartModelStructure().getField(fieldUniqueName);
			query.addWhereFiled(field.getUniqueName(), operator, value);
		}
		
		return query;
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
	public static String encode(Query query, DataMartModel datamartModel) throws JSONException {
		List selectedFields = query.getSelectFields();
		Iterator selectedFieldsIt = selectedFields.iterator();
		JSONArray recordsJOSN = new JSONArray();
		while( selectedFieldsIt.hasNext() ) {
			SelectField selectField = (SelectField)selectedFieldsIt.next();
			DataMartField datamartField = datamartModel.getDataMartModelStructure().getField( selectField.getUniqueName() );
			JSONObject recordJSON = new JSONObject();
			recordJSON.put("id", datamartField.getUniqueName());
			recordJSON.put("entity", datamartField.getParent().getName());
			recordJSON.put("field", datamartField.getName());
			recordJSON.put("alias", selectField.getAlias());
			if( selectField.isGroupByField() ) {
				recordJSON.put("group", "true");
			} else {
				recordJSON.put("group", "");
			}
			recordJSON.put("order", selectField.getOrderType());
			recordJSON.put("funct", selectField.getFunction().getName());
			recordJSON.put("visible", selectField.isVisible());
			
			recordsJOSN.put(recordJSON);
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
		List whereFields = query.getWhereFields();
		Iterator filterFieldsIt = whereFields.iterator();
		JSONArray filtersJOSN = new JSONArray();
		while( filterFieldsIt.hasNext() ) {
			WhereField filterField = (WhereField)filterFieldsIt.next();
			DataMartField datamartField = datamartModel.getDataMartModelStructure().getField( filterField.getUniqueName() );
			JSONObject filterJSON = new JSONObject();
			filterJSON.put("id", datamartField.getUniqueName());
			filterJSON.put("entity", datamartField.getParent().getName());
			filterJSON.put("field", datamartField.getName());
			//filterJSON.put("alias", "");
			filterJSON.put("operator", filterField.getOperator().getName());
			filterJSON.put("value", filterField.getRightHandValue().toString());
			filterJSON.put("type", "Static Value");
			
			filtersJOSN.put(filterJSON);
		}
		
		JSONObject queryJSON = new JSONObject();
		queryJSON.put("fields", recordsJOSN);
		queryJSON.put("filters", filtersJOSN);
		
		return queryJSON.toString();
	}

}
