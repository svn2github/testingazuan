/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.rest.objects.serDeser;

import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class JsonDatasourceSerDeser  implements ObjectsSerDeser{
	/*
	"objects" :  [{
		"id" : "84889cb8-fade-4121-a48e-90da9f277114",
		"name" : "Oracle utenti adsl",
		"description" : "Datasource Oracle utenti ADSL residenziali Telecom Italia",
		"type" : "datasource",
		"creationDate": 1234567890,
		"lastModifiedDate": 1234567788",
   		"user":"enelenergia",
   		"owner":"telecomitalia",
  		"userObjectStatus": "Buyed",
   		"ownerObjectStatus": "Published",
		"details" : {
			"subtype" : "RDBMS",
			"URL" : "jdbc:oracle:thin:...",
			"username" : "xxxxxxxx",
			"password" : "yyyyyyyy",
			"resourceType" : "table",
			"resource" : "user_anag"
		},
		"objects" : []
		}]
		*/
	@Override
	public Object deserialize(JSONObject datasource) throws SerializationException {
		if(datasource == null){
			return null;
		}
		DataSource dataSourceSpagoBI = new DataSource();
		
		try {
		
			String idD = (String)datasource.opt("id");
			String nameD = (String)datasource.opt("name");
			String descriptionD = (String)datasource.opt("description");
//			
//			Integer creationDateD = (Integer)datasource.opt("creationDate");
//			Integer lastModifiedDateD = (Integer)datasource.opt("lastModifiedDate");
//			String userD = (String)datasource.opt("user");
//			String ownerD = (String)datasource.opt("owner");
//			String userObjectStatusD = (String)datasource.opt("userObjectStatus");
//			String ownerObjectStatusD = (String)datasource.opt("ownerObjectStatus");

			dataSourceSpagoBI.setDescr(descriptionD);
			
			JSONObject detail = (JSONObject)datasource.opt("details");


			String subT = detail.optString("subtype");
			String URL = detail.optString("URL");
			String username = detail.optString("username");
			String password = detail.optString("password");
			String resourceType = detail.optString("resourceType");
			String resource = detail.getString("resource");
			String label =detail.optString("biId");
			
			
			if(StringUtilities.isEmpty(label)) {
				throw new SerializationException("Attribute [biId] of datasource object is not defined");
			}
			
			dataSourceSpagoBI.setLabel(label);
			dataSourceSpagoBI.setUser(username);
			dataSourceSpagoBI.setPwd(password);
			dataSourceSpagoBI.setUrlConnection(URL);

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return dataSourceSpagoBI;
	}
/*{
			"id" : "84889cb8-fade-4121-a48e-90da9f277114",
			"name" : "Oracle utenti adsl",
			"description" : "Datasource Oracle utenti ADSL residenziali Telecom Italia",
			"type" : "datasource",
			"creationDate": 1234567890,
			"lastModifiedDate": 1234567788",
       		"user":"enelenergia",
       		"owner":"telecomitalia",
      		"userObjectStatus": "Buyed",
       		"ownerObjectStatus": "Published",
			"details" : {
				"subtype" : "RDBMS",
				"URL" : "jdbc:oracle:thin:...",
				"username" : "xxxxxxxx",
				"password" : "yyyyyyyy",
				"resourceType" : "table",
				"resource" : "user_anag"

			},
			"objects" : []
}*/
	@Override
	public JSONObject serialize(Object o, JSONArray objects )throws JSONException  {
		
		JSONObject datasrcJson = new JSONObject();
		if(o == null){
			return null;
		}
		
		DataSource ds = (DataSource)o;
		datasrcJson.put("biId", ds.getLabel());
		datasrcJson.put("description", ds.getDescr());
		datasrcJson.put("type", SerDeserFactory.TYPE_DATASOURCE);
		JSONObject details = new JSONObject();
		
		details.put("subType", "RDBMS");
		details.put("URL", ds.getUrlConnection());
		details.put("username", ds.getUser());
		details.put("password", ds.getPwd());
		details.put("resourceType", "table");
		details.put("resource", "RDBMS");
		
		datasrcJson.put("details", details);
		
		return datasrcJson;
	}
	


}
