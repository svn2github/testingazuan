package it.eng.spagobi.rest.objects.serDeser;

import java.util.Locale;
import java.util.ResourceBundle;

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
		
//		JSONObject detail = (JSONObject)o.opt("details");
//		if(detail != null){
//			
//			String URL = detail.optString("URL");
//			String authType = detail.optString("authType");
//			String httpMethod = detail.optString("httpMethod");
//			JSONObject queryParameters = (JSONObject)detail.opt("queryParameters");
//			if(queryParameters != null){
//				String page =queryParameters.optString("PAGE");
//				String newSession =queryParameters.optString("NEW_SESSION");
//				String userID =queryParameters.optString("userID");
//				String password =queryParameters.optString("password");
//				String label =queryParameters.optString("OBJECT_LABEL");				
//			}
//		}	
		
		document.setLabel(id);
		document.setName(name);

		
		return document;
	}

	@Override
	public JSONObject serialize(Object o, JSONArray objects)
			throws JSONException, SourceBeanException {
		//per scrittura
		JSONObject datasrcJson = new JSONObject();
		if(o == null){
			return null;
		}
		ResourceBundle rb = ResourceBundle.getBundle("restclient", Locale.ITALY);
		
		BIObject obj = (BIObject)o;
		datasrcJson.put("id", obj.getLabel());
		datasrcJson.put("description", obj.getDescription());
		datasrcJson.put("type", SerDeserFactory.TYPE_DASHBOARD);

		JSONObject details = new JSONObject();
		String url = rb.getString("rest.spagobi.protocol")+rb.getString("rest.spagobi.host")+":"+rb.getString("rest.spagobi.port")+rb.getString("rest.spagobi.uri");
		details.put("URL", url);
		details.put("authType", "noauth");
		details.put("httpMethod", "GET");
		JSONObject queryParameters = new JSONObject();
		
		
		queryParameters.put("PAGE", rb.getString("rest.spagobi.page"));
		queryParameters.put("NEW_SESSION", "TRUE");
		queryParameters.put("userID", rb.getString("rest.spagobi.user"));
		queryParameters.put("password", rb.getString("rest.spagobi.pwd"));
		queryParameters.put("OBJECT_LABEL", obj.getLabel());
		
		details.put("queryParameters", queryParameters);
		datasrcJson.put("details", details);
		
		return datasrcJson;
	}

}
