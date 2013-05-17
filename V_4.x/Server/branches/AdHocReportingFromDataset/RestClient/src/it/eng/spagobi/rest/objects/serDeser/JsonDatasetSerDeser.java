/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.rest.objects.serDeser;

import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.rest.objects.serDeser.util.SerDeserUtil;
import it.eng.spagobi.tools.dataset.bo.GuiDataSetDetail;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSetDetail;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class JsonDatasetSerDeser  implements ObjectsSerDeser{
	public static final String JDBC_DS_TYPE = "Query";
	static private Logger logger = Logger.getLogger(JsonDatasetSerDeser.class);

	@Override
	public Object deserialize(JSONObject o) throws SerializationException {
		if(o == null){
			return null;
		}

		GuiGenericDataSet guiGenericDataSet = new GuiGenericDataSet();
		GuiDataSetDetail guiDataSetDetail = new GuiDataSetDetail();
		
		try {

			String id = (String)o.optString("id");
			String name = (String)o.optString("name");
			String description = (String)o.optString("description");
			Integer creationDate = (Integer)o.opt("creationDate");	
			String owner = (String)o.optString("owner");
			//String userObjectStatus = (String)o.optString("userObjectStatus");
			
			guiGenericDataSet.setLabel(id);
			guiGenericDataSet.setName(name);
			guiGenericDataSet.setDescription(description);			
			guiGenericDataSet.setTimeIn(new Date(creationDate));
			guiGenericDataSet.setUserIn(owner);
			//detects whether it's list or detail
			if(!o.has("details")){
				return guiGenericDataSet;
			}

			
			String ownerObjectStatus = (String)o.optString("ownerObjectStatus");
			String user = (String)o.optString("user");
			String lastModifiedDate = (String)o.opt("lastModifiedDate");
			JSONObject details = (JSONObject)o.opt("details");
			JSONObject structure = (JSONObject)details.opt("structure");
			JSONArray columns = (JSONArray)structure.opt("columns");	
			String meta =SerDeserUtil.deserMetadata(columns);
			
			JSONArray datasources = (JSONArray)o.opt("objects");
			if(datasources != null && datasources.length() != 0){
				QueryDataSetDetail queryDatasetDetail = new QueryDataSetDetail();
				
				JSONObject datasource = (JSONObject) datasources.get(0);
				
				ObjectsSerDeser des = SerDeserFactory.getSerDeser(SerDeserFactory.TYPE_DATASOURCE);
				DataSource dataSourceObj = (DataSource)des.deserialize(datasource);
				
				queryDatasetDetail.setDataSourceLabel(dataSourceObj.getLabel());//datasource label
				String resource = ((JSONObject)datasource.opt("details")).optString("resource");
				queryDatasetDetail.setQuery("select * from "+resource);//query
				
				guiDataSetDetail.setDsType(JDBC_DS_TYPE);
				guiDataSetDetail =(GuiDataSetDetail)queryDatasetDetail;
			}		
			
			guiGenericDataSet.setTimeUp(new Date(Long.valueOf(lastModifiedDate)));
			guiGenericDataSet.setUserUp(user);
			guiDataSetDetail.setDsMetadata(meta);
			
			guiGenericDataSet.setActiveDetail(guiDataSetDetail);

		}catch(Exception ex){
			logger.error(ex);
		}
		return guiGenericDataSet;
	}

	@Override
	public JSONObject serialize(Object o, JSONArray objects ) throws JSONException, SourceBeanException {
		JSONObject objJson = new JSONObject();
		JSONObject datasetJson = new JSONObject();
		if(o == null){
			return null;
		}
		/*
		{
			   "object" : {
				"name" : "<object name (40 char)>",
				"description" : "<object description (string 1024 char)>",
				"type" : "<object type (string 40 char)>",
				"details" : {
					"readOnly" : <boolean>,	// true if read only, false if reading/writing permitted
					"structure" : {
						"columns" : [{
								"name" : "<column name (string 30 char)>",
								"dataType" : {
									"name" : "<column data type [integer | decimal | string | date | boolean]>",
									"length" : <max length in char (int)>,	  // solo in caso di "string"
									"dateFormat" : "<date format (string 40 char)>" // solo in caso di "date"
								},
								"biType" : "<spagoBI type [attribute | measure]>"
								}
							…
						],
					…
					}
				},
				"objects" : [objectMetadata1, objectMetadata2...]
			    }
			}
			*/
		GuiGenericDataSet ds = (GuiGenericDataSet)o;
		GuiDataSetDetail dsDetail =  ds.getActiveDetail();

		datasetJson.put("name", ds.getName());
		datasetJson.put("description", ds.getLabel());
		datasetJson.put("creationDate", ds.getLabel());
		datasetJson.put("type", SerDeserFactory.TYPE_DATASET);
		
		JSONObject detail = new JSONObject();
		JSONObject structure = new JSONObject();
		detail.put("structure", structure);
		
		String metadataStr = dsDetail.getDsMetadata();
		JSONArray columns = SerDeserUtil.serializeMetada(metadataStr);

		detail.put("columns", columns);
		
		datasetJson.put("details", detail);
		
		datasetJson.put("objects", objects);

		objJson.put("object", datasetJson);
		return datasetJson;
	}
	
}
