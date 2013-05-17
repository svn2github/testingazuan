package it.eng.spagobi.rest.client.api;

import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.rest.client.TilabClient;
import it.eng.spagobi.rest.objects.serDeser.ObjectsSerDeser;
import it.eng.spagobi.rest.objects.serDeser.SerDeserFactory;
import it.eng.spagobi.tools.dataset.bo.GuiDataSetDetail;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class TilabClientAPI {
	
	public GuiGenericDataSet getDatasetDetail(String id, String user, String auth) throws SerializationException, JSONException{
		GuiGenericDataSet result = null;

		//http://<host>:<port>/<path>/objects/4d2a9ddf-2ba6-456d-b708-9b65a1bca811
		//JSONObject jsonObj = TilabClient.v1().objectsId(id).getAsJson(user, auth, JSONObject.class);
		String jsonObjStr = TilabClient.v1().objectsId(id).getAsJson(user, auth, String.class);
		JSONObject jsonObj = new JSONObject(jsonObjStr);
		String type = SerDeserFactory.TYPE_DATASET;
		
		if(!jsonObj.getString("responseCode").equals("204")) {
			//get object type
			JSONObject object = jsonObj.getJSONObject("object");
			if(object != null){
				type = object.getString("type");
				//instantiate proper deserializer
				ObjectsSerDeser des = SerDeserFactory.getDeserializer(type);
				result = (GuiGenericDataSet)des.deserialize(object);

			}
		}
		return result;
	}
	
	public List<GuiGenericDataSet> getDatasetList(String offset, String limit, String user, String auth) throws JSONException{
		List<GuiGenericDataSet> result = new ArrayList<GuiGenericDataSet>();

		//http://<host>:<port>/<path>/objects?type=dataset&offset=0&limit=10
		String jsonObjStr = TilabClient.v1().objects().getAsJson(offset, limit, SerDeserFactory.TYPE_DATASET, user, auth, String.class);
		JSONObject jsonObj = new JSONObject(jsonObjStr);
		
		//get objects
		JSONArray objects = jsonObj.getJSONArray("objects");

		//instantiate proper deserializer
		ObjectsSerDeser des = SerDeserFactory.getDeserializer(SerDeserFactory.TYPE_DATASET);
		for(int i= 0; i<objects.length(); i++){
			JSONObject dataset = objects.getJSONObject(i);
			GuiGenericDataSet deserializedObj = (GuiGenericDataSet)des.deserialize(dataset);
			result.add(deserializedObj);
		}
		return result;
	}
	
	public List<Object> getObjectsList(String offset, String limit, String user, String auth, String type) throws JSONException{
		List<Object> result = new ArrayList<Object>();

		//http://<host>:<port>/<path>/objects?type=dataset&offset=0&limit=10
		String jsonObjStr = TilabClient.v1().objects().getAsJson(offset, limit, type, user, auth, String.class);
		JSONObject jsonObj = new JSONObject(jsonObjStr);
		
		//get objects
		JSONArray objects = jsonObj.getJSONArray("objects");

		//instantiate proper deserializer
		ObjectsSerDeser des = SerDeserFactory.getDeserializer(type);
		for(int i= 0; i<objects.length(); i++){
			JSONObject dataset = objects.getJSONObject(i);
			Object deserializedObj = des.deserialize(dataset);
			result.add(deserializedObj);
		}
		return result;
	}
	public Object getObjectDetail(String id, String user, String auth, String type) throws SerializationException, JSONException{
		Object result = null;

		//http://<host>:<port>/<path>/objects/4d2a9ddf-2ba6-456d-b708-9b65a1bca811
		//JSONObject jsonObj = TilabClient.v1().objectsId(id).getAsJson(user, auth, JSONObject.class);
		String jsonObjStr = TilabClient.v1().objectsId(id).getAsJson(user, auth, String.class);
		JSONObject jsonObj = new JSONObject(jsonObjStr);
		
		if(!jsonObj.getString("responseCode").equals("204")) {
			//get object type
			JSONObject object = jsonObj.getJSONObject("object");
			if(object != null){
				type = object.getString("type");
				//instantiate proper deserializer
				ObjectsSerDeser des = SerDeserFactory.getDeserializer(type);
				result = (Object)des.deserialize(object);

			}
		}
		return result;
	}
	public boolean writeDataset(DataSource dSRC, GuiGenericDataSet dSet, String user, String auth) throws JSONException, SourceBeanException{	
		
		boolean result = false;
		
		ObjectsSerDeser des1 = SerDeserFactory.getDeserializer(SerDeserFactory.TYPE_DATASOURCE);
		JSONObject inputDataSource = des1.serialize(dSRC, null);
		
		//http://<host>:<port>/<path>/objects
		JSONObject responseDatasource = TilabClient.v1().objects().postJson(inputDataSource, user, auth, JSONObject.class);
		//put in array
		JSONArray objects = new JSONArray();
		objects.put(responseDatasource);
		//use datasource for dataset request
		
		ObjectsSerDeser des2 = SerDeserFactory.getDeserializer(SerDeserFactory.TYPE_DATASET);
		
		JSONObject inputDataSet = des2.serialize(dSet, objects);
		JSONObject responseDataset = TilabClient.v1().objects().postJson(inputDataSet, "", "", JSONObject.class);
		
		if(responseDataset.getString("responseCode").equals("200")) {
			result = true;
		}
		return result;
	}

}
