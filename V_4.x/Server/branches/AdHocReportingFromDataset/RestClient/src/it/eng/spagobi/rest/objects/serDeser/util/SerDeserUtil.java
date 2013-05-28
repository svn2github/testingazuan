/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.rest.objects.serDeser.util;

import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SerDeserUtil {
	/*'columns' : [{	
					'name' : 'user_id',
					'dataType' : {
						'name' : 'integer'
					},
					'biType' : 'attribute'
					},
					{'name' : 'msisdn',
					'dataType' : {
						'name' : 'string',
						'length' : 40
					},
					'biType' : 'attribute'
					},
					{'name' : 'name',
					'dataType' : {
						'name' : 'string ',
						'length' : 40
					},
					'biType' : 'attribute'
					},
					{'name' : 'registration_date',
						'dataType' : {
							'name' : 'date ',
							'dateFormat' : 'YYYYMMDD:HH24:mi:ss'
					},
					'biType' : 'attribute'
					}]*/
	
	/*
	 "<?xml version="1.0" encoding="ISO-8859-1"?>
<COLUMNLIST>
  <COLUMN fieldType="ATTRIBUTE" name="timestamp_cdr" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="lac_cella" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="ci_cella" type="java.lang.String"/>
  <COLUMN fieldType="MEASURE" name="durata_chiamata" type="java.lang.Long"/>
  <COLUMN fieldType="ATTRIBUTE" name="tipo_chiamante" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="tipo_chiamato" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="country_code" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="area_code" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="hash" type="java.lang.String"/>
  <COLUMN fieldType="ATTRIBUTE" name="data_cdr" type="java.lang.String"/>
</COLUMNLIST>
"
	 */
	public static String deserMetadata(JSONArray columns) throws JSONException, SourceBeanException{
		String meta = "";
		SourceBean metadata = new SourceBean("COLUMNLIST");
		
		for(int i=0; i< columns.length(); i++){
			JSONObject col = (JSONObject)columns.get(i);
			
			String name = (String)col.getString("name");
			JSONObject dataType = (JSONObject)col.opt("dataType");
			String typeName = dataType.optString("name");
			String fieldType = (String)col.optString("biType");
			
			SourceBean column = new SourceBean("COLUMN");
			column.setAttribute("name", name);
			String type= "java.lang.String";
			if(typeName != null ){
				if(typeName.equalsIgnoreCase("date")){
					type= "java.util.Date";
				}else if(typeName.equalsIgnoreCase("integer")){
					type= "java.lang.Integer";
				}else if(typeName.equalsIgnoreCase("decimal")){
					type= "java.lang.Long";
				}else if(typeName.equalsIgnoreCase("boolean")){
					type= "java.lang.Boolean";
				}
			}
			
			column.setAttribute("type", type);
			column.setAttribute("fieldType", fieldType.toUpperCase());
			metadata.setAttribute(column);
		}
		meta = metadata.toString();
		return meta;
	}
	public static JSONArray serializeMetada(String meta) throws JSONException, SourceBeanException{
		JSONArray metaListJSON = new JSONArray();

		if(meta!=null && !meta.equals("")){
			SourceBean source = SourceBean.fromXMLString(meta);
			if(source!=null){
				if(source.getName().equals("COLUMNLIST")){
					List<SourceBean> rows = source.getAttributeAsList("COLUMN");
					for(int i=0; i< rows.size(); i++){
						SourceBean row = rows.get(i);
						String name = (String)row.getAttribute("name");
						String typeName = (String)row.getAttribute("TYPE");
						String fieldType = (String)row.getAttribute("fieldType");
						JSONObject jsonMeta = new JSONObject();
						jsonMeta.put("name", name);
						JSONObject dataType = new JSONObject();
						String type= "string";
						if(typeName != null ){
							if(typeName.equalsIgnoreCase("java.util.Date")){
								type= "date";
							}else if(typeName.equalsIgnoreCase("java.lang.Integer")){
								type= "integer";
							}else if(typeName.equalsIgnoreCase("java.lang.Long")){
								type= "decimal";
							}else if(typeName.equalsIgnoreCase("java.lang.Boolean")){
								type= "boolean";
							}
						}
						dataType.put("name", type);
						
						jsonMeta.put("dataType", dataType);
						jsonMeta.put("biType", fieldType.toLowerCase());
						metaListJSON.put(jsonMeta);
					}				
				}else if(source.getName().equals("METADATALIST")){
					List<SourceBean> rows = source.getAttributeAsList("ROWS.ROW");
					for(int i=0; i< rows.size(); i++){
						SourceBean row = rows.get(i);
						String name = (String)row.getAttribute("NAME");
						String typeName = (String)row.getAttribute("TYPE");
						JSONObject jsonMeta = new JSONObject();
						
						jsonMeta.put("name", name);
						
						JSONObject dataType = new JSONObject();
						String type= "string";
						if(typeName != null ){
							if(typeName.equalsIgnoreCase("java.util.Date")){
								type= "date";
							}else if(typeName.equalsIgnoreCase("java.lang.Integer")){
								type= "integer";
							}else if(typeName.equalsIgnoreCase("java.lang.Long")){
								type= "decimal";
							}else if(typeName.equalsIgnoreCase("java.lang.Boolean")){
								type= "boolean";
							}
						}
						dataType.put("name", type);
						
						jsonMeta.put("dataType", dataType);

						metaListJSON.put(jsonMeta);
					}				
				}
			}
		}
		return metaListJSON;
	}
}
