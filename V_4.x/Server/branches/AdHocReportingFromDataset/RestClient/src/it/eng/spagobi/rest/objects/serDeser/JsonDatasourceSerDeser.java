/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.rest.objects.serDeser;

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
		
			String idD = (String)datasource.get("id");
			String nameD = (String)datasource.get("name");
			String descriptionD = (String)datasource.get("description");
//			
//			Integer creationDateD = (Integer)datasource.get("creationDate");
//			Integer lastModifiedDateD = (Integer)datasource.get("lastModifiedDate");
//			String userD = (String)datasource.get("user");
//			String ownerD = (String)datasource.get("owner");
//			String userObjectStatusD = (String)datasource.get("userObjectStatus");
//			String ownerObjectStatusD = (String)datasource.get("ownerObjectStatus");

			dataSourceSpagoBI.setDescr(descriptionD);
			
			JSONObject detail = (JSONObject)datasource.get("details");


			String subT = detail.getString("subtype");
			String URL = detail.getString("URL");
			String username = detail.getString("username");
			String password = detail.getString("password");
			String resourceType = detail.getString("resourceType");
			String resource = detail.getString("resource");
			String label =detail.getString("biId");
			
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
		datasrcJson.put("type", SerDeserFactory.TYPE_DATAOURCE);
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
