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
import it.eng.qbe.newquery.ExpressionNode;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.newquery.SelectField;
import it.eng.qbe.newquery.WhereField;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.strings.StringUtils;

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
				recordJSON.getJSONObject("expression").toString(),
				datamartModel);
	}
	
	public static Query decode(String queryFields, String queryFilters, String queryFilterExp, DataMartModel datamartModel) throws JSONException {
		Query query = null;
		
		query = new Query();		
		
		
		// ----------------------------------------------------
		// DECODE fields
		// ----------------------------------------------------
		decodeFields(query, queryFields, datamartModel);
		
		// ----------------------------------------------------
		// DECODE filters
		// ----------------------------------------------------
		decodeFilters(query, queryFilters, datamartModel);
		
		// ----------------------------------------------------
		// DECODE expression
		// ----------------------------------------------------
		decodeFilterExp(query, queryFilterExp, datamartModel);
		
		return query;
	}
	
	private static void decodeFields(Query query, String queryFields, DataMartModel datamartModel) throws JSONException {
		JSONArray recordsJOSN = new JSONArray( queryFields );
		for(int i = 0; i < recordsJOSN.length(); i++) {
			JSONObject recordJSON = recordsJOSN.getJSONObject(i);
			String fieldUniqueName = recordJSON.getString("id");
			String alias = recordJSON.getString("alias") != null 
						   && !recordJSON.getString("alias").trim().equalsIgnoreCase("")
						   ? recordJSON.getString("alias")
						   : "Column_" + (i+1);
			String group = recordJSON.getString("group");
			String order = recordJSON.getString("order");
			String funct = recordJSON.getString("funct");
			boolean visible = recordJSON.getBoolean("visible");
												
			if(fieldUniqueName != null) {
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
	}
	
	private static void decodeFilters(Query query, String queryFilters, DataMartModel datamartModel) throws JSONException {
		JSONArray filtersJOSN = new JSONArray( queryFilters );
		for(int i = 0; i < filtersJOSN.length(); i++) {
			JSONObject filterJSON = filtersJOSN.getJSONObject(i);
			String fname = filterJSON.getString("fname");
			String fdesc =  filterJSON.getString("fname");
			String fieldUniqueName = filterJSON.getString("id");
			String operator = filterJSON.getString("operator");
			String operand = filterJSON.getString("operand");
			String operandDesc = filterJSON.getString("odesc");
			String operandType = filterJSON.getString("otype");
			String boperator = filterJSON.getString("boperator");
			
			
			
			Assert.assertTrue(!StringUtils.isEmpty(operator), "Undefined operator for filter: " + filterJSON.toString());
			Assert.assertTrue(!"NONE".equalsIgnoreCase(operator), "Undefined operator NONE for filter: " + filterJSON.toString());
			
			
		    DataMartField field = datamartModel.getDataMartModelStructure().getField(fieldUniqueName);
			query.addWhereFiled(fname, fdesc,field.getUniqueName(), operator, operand, operandType, operandDesc, boperator);
		}	
	}
	
	private static void decodeFilterExp(Query query, String queryFilterExp, DataMartModel datamartModel) throws JSONException {
		JSONObject filterExpJOSN = new JSONObject( queryFilterExp );
		ExpressionNode filterExp = getFilterExpTree(filterExpJOSN);
		query.setWhereClauseStructure( filterExp );
		if(filterExp != null) {
			System.out.println(">>>>>>> " + getFilterExpAsString(filterExp));
		}
		
	}
	
	private static String getFilterExpAsString(ExpressionNode filterExp) {
		String str = "";
		
		String type = filterExp.getType();
		if("NODE_OP".equalsIgnoreCase( type )) {
			for(int i = 0; i < filterExp.getChildNodes().size(); i++) {
				ExpressionNode child = (ExpressionNode)filterExp.getChildNodes().get(i);
				String childStr = getFilterExpAsString(child);
				if("NODE_OP".equalsIgnoreCase( child.getType() )) {
					childStr = "(" + childStr + ")";
				}
				str += (i==0?"": " " + filterExp.getValue());
				str += " " + childStr;
			}
		} else {
			str += filterExp.getValue();
		}
		
		return str;
	}
	
	private static ExpressionNode getFilterExpTree(JSONObject nodeJSON) throws JSONException {
		ExpressionNode node = null;
		String nodeType;
		String nodeValue;
		JSONArray childNodesJSON;
		
		if(nodeJSON.has("type") && nodeJSON.has("value")) {		
			nodeType = nodeJSON.getString("type");
			nodeValue = nodeJSON.getString("value");			
			node = new ExpressionNode(nodeType, nodeValue);
			
			childNodesJSON = nodeJSON.getJSONArray("childNodes");
			for(int i = 0; i < childNodesJSON.length(); i++) {
				JSONObject childNodeJSON = childNodesJSON.getJSONObject(i);
				node.addChild( getFilterExpTree(childNodeJSON) );
			}
		}
		return node;
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
			filterJSON.put("fname", filterField.getFname());
			filterJSON.put("fdesc", filterField.getFdesc());
			filterJSON.put("id", datamartField.getUniqueName());
			filterJSON.put("entity", datamartField.getParent().getName());
			filterJSON.put("field", datamartField.getName());
			filterJSON.put("operator", filterField.getOperator());
			filterJSON.put("operand", filterField.getOperand().toString());
			filterJSON.put("odesc", filterField.getOperandDesc());			
			filterJSON.put("otype", filterField.getOperandType());
			filterJSON.put("boperator", filterField.getBoperator());
			
			filtersJOSN.put(filterJSON);
		}
		
		
		JSONObject filterExpJOSN = encodeFilterExp( query.getWhereClauseStructure() );
		
		
		JSONObject queryJSON = new JSONObject();
		queryJSON.put("fields", recordsJOSN);
		queryJSON.put("filters", filtersJOSN);
		queryJSON.put("expression", filterExpJOSN);
		
		return queryJSON.toString();
	}
	
	private static JSONObject encodeFilterExp(ExpressionNode filterExp) throws JSONException {
		JSONObject exp = new JSONObject();
		JSONArray childsJSON = new JSONArray();
		
		if(filterExp == null) return exp;
		
		exp.put("type", filterExp.getType()) ;
		exp.put("value", filterExp.getValue());
				
		for(int i = 0; i < filterExp.getChildNodes().size(); i++) {
			ExpressionNode child = (ExpressionNode)filterExp.getChildNodes().get(i);
			JSONObject childJSON = encodeFilterExp(child);
			childsJSON.put(childJSON);
		}		
		
		exp.put("childNodes", childsJSON);
		 
		return exp;
	}	
	

}
