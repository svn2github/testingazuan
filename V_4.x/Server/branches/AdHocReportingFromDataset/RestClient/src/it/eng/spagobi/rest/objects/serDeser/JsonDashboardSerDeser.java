package it.eng.spagobi.rest.objects.serDeser;

import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class JsonDashboardSerDeser implements ObjectsSerDeser {

	@Override
	public Object deserialize(JSONObject o) throws SerializationException {
		if(o == null){
			return null;
		}
		BIObject document = new BIObject();
		
		String id = (String)o.opt("id");
		String name = (String)o.opt("name");
		String description = (String)o.opt("description");
//			
//			Integer creationDateD = (Integer)o.opt("creationDate");
//			Integer lastModifiedDateD = (Integer)o.opt("lastModifiedDate");
//			String userD = (String)o.opt("user");
//			String ownerD = (String)o.opt("owner");
//			String userObjectStatusD = (String)o.opt("userObjectStatus");
//			String ownerObjectStatusD = (String)o.opt("ownerObjectStatus");

		document.setDescription(description);
		
		JSONObject detail = (JSONObject)o.opt("details");


//			String subT = detail.optString("subtype");
//			String URL = detail.optString("URL");
//			String username = detail.optString("username");
//			String password = detail.optString("password");
//			String resourceType = detail.optString("resourceType");
//			String resource = detail.getString("resource");
//			String label =detail.optString("biId");
		
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
