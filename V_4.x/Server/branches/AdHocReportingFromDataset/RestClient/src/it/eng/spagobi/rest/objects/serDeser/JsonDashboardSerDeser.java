package it.eng.spagobi.rest.objects.serDeser;

import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import java.io.IOException;
import java.util.Properties;

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
    
	public final static String CONFIG_FILE = "restclient.properties";
	
	private static String user;
    private static String password;
    private static String page;
	
    static {
    	Properties properties = new Properties();
    	try {
			properties.load(JsonDashboardSerDeser.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			throw new RuntimeException("Cannot read " + CONFIG_FILE + " file", e);
		}
    	user = properties.getProperty("rest.spagobi.user");
    	password = properties.getProperty("rest.spagobi.pwd");
    	page = properties.getProperty("rest.spagobi.page");
    }
	
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
		
		BIObject obj = (BIObject)o;
		//datasrcJson.put("id", obj.getLabel());
		datasrcJson.put("description", obj.getDescription());
		datasrcJson.put("type", SerDeserFactory.TYPE_DASHBOARD);

		JSONObject details = new JSONObject();
		details.put("URL", GeneralUtilities.getSpagoBiHost() + GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl());
		details.put("authType", "noauth");
		details.put("httpMethod", "GET");
		JSONObject queryParameters = new JSONObject();
		
		
		queryParameters.put("PAGE", page);
		queryParameters.put("NEW_SESSION", "TRUE");
		queryParameters.put("userID", user);
		queryParameters.put("password", password);
		queryParameters.put("OBJECT_LABEL", obj.getLabel());
		
		details.put("queryParameters", queryParameters);
		datasrcJson.put("details", details);
		
		return datasrcJson;
	}

}
