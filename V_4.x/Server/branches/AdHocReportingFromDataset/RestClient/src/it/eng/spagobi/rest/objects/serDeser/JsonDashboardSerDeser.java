package it.eng.spagobi.rest.objects.serDeser;

import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class JsonDashboardSerDeser implements ObjectsSerDeser {
/*
    "details" : {
    "URL" : " http://localhost:8080/SpagoBI/servlet/AdapterHTTP",
    “authType” : “noauth”,            // es. “noauth”, “OAuth2”, ecc.
    “httpMethod” : "GET"
    “queryParameters” : {
          PAGE="LoginPage"
          , NEW_SESSION="TRUE"
          , userID="biadmin"
          , password="biadmin"
          , OBJECT_LABEL: "DASH1"

     }
    “header” : {},
    “body” : “”,                        // Sempre vuoto per il PoC
}
*/
	@Override
	public Object deserialize(JSONObject o) throws SerializationException {
		//per lettura
		if(o == null){
			return null;
		}
		BIObject document = new BIObject();
		
		String id = (String)o.opt("id");
		String name = (String)o.opt("name");
		String description = (String)o.opt("description");

		document.setDescription(description);
		
		JSONObject detail = (JSONObject)o.opt("details");

			String URL = detail.optString("URL");
			String authType = detail.optString("authType");
			String httpMethod = detail.optString("httpMethod");
			JSONObject queryParameters = (JSONObject)detail.opt("queryParameters");
			if(queryParameters != null){
				String page =queryParameters.optString("PAGE");
				String newSession =queryParameters.optString("NEW_SESSION");
				String userID =queryParameters.optString("userID");
				String password =queryParameters.optString("password");
				String label =queryParameters.optString("OBJECT_LABEL");				
			}
			
		
		document.setLabel(id);
		document.setName(name);

		
		return document;
	}

	@Override
	public JSONObject serialize(Object o, JSONArray objects)
			throws JSONException, SourceBeanException {
		
		JSONObject datasrcJson = new JSONObject();
		if(o == null){
			return null;
		}
		
		BIObject obj = (BIObject)o;
		datasrcJson.put("biId", obj.getLabel());
		datasrcJson.put("description", obj.getDescription());
		datasrcJson.put("type", SerDeserFactory.TYPE_DASHBOARD);
		//empty detail
		JSONObject details = new JSONObject();

		
		datasrcJson.put("details", details);
		
		return datasrcJson;
	}

}
