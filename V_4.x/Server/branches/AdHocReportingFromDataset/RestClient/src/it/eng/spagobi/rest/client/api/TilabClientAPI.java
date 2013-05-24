package it.eng.spagobi.rest.client.api;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.rest.client.TilabClient;
import it.eng.spagobi.rest.objects.serDeser.ObjectsSerDeser;
import it.eng.spagobi.rest.objects.serDeser.SerDeserFactory;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class TilabClientAPI {
	
	private static Logger logger = Logger.getLogger(TilabClientAPI.class);
	
	public final static String CONFIG_FILE = "restclient.properties";
	
    private static String user;
    private static String auth;

    static {
    	Properties properties = new Properties();
    	try {
			properties.load(TilabClientAPI.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			logger.error("Cannot read " + CONFIG_FILE + " file", e);
			throw new RuntimeException("Cannot read " + CONFIG_FILE + " file", e);
		}
    	user = properties.getProperty("rest.server.user");
    	logger.debug("rest.server.user = " + user);
    	auth = properties.getProperty("rest.server.auth");
    	logger.debug("rest.server.auth = " + auth);
    }
	
	public GuiGenericDataSet getDatasetDetail(String id) {
		try {

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
					ObjectsSerDeser des = SerDeserFactory.getSerDeser(type);
					result = (GuiGenericDataSet)des.deserialize(object);

				}
			}
			return result;
			
		} catch (Exception e) {
			logger.error("Error while getting detail for dataset with id = [" + id + "]", e);
			throw new RuntimeException("Error while getting detail for dataset with id = [" + id + "]", e);
		}

	}
	
	public List<GuiGenericDataSet> getDatasetList(String offset, String limit) {
		try {
			
			List<GuiGenericDataSet> result = new ArrayList<GuiGenericDataSet>();

			//http://<host>:<port>/<path>/objects?type=dataset&offset=0&limit=10
			String jsonObjStr = TilabClient.v1().objects().getAsJson(offset, limit, SerDeserFactory.TYPE_DATASET, user, auth, String.class);
			JSONObject jsonObj = new JSONObject(jsonObjStr);
			
			//get objects
			JSONArray objects = jsonObj.getJSONArray("objects");

			//instantiate proper deserializer
			ObjectsSerDeser des = SerDeserFactory.getSerDeser(SerDeserFactory.TYPE_DATASET);
			for(int i= 0; i<objects.length(); i++){
				JSONObject dataset = objects.getJSONObject(i);
				GuiGenericDataSet deserializedObj = (GuiGenericDataSet)des.deserialize(dataset);
				result.add(deserializedObj);
			}
			return result;
			
		} catch (Exception e) {
			logger.error("Error while getting datasets list using offset [" + offset + "] and limit [" + limit + "]", e);
			throw new RuntimeException("Error while getting datasets list using offset [" + offset + "] and limit [" + limit + "]", e);
		}

	}
	
	public List<GuiGenericDataSet> getDatasetList() {
		try {

			List<GuiGenericDataSet> result = new ArrayList<GuiGenericDataSet>();

			int offset = 0;
			int limit = 30;
			boolean otherData = true;
			
			while (otherData) {
				String jsonObjStr = TilabClient.v1().objects().getAsJson(
						new Integer(offset).toString(),
						new Integer(limit).toString(),
						SerDeserFactory.TYPE_DATASET, user, auth, String.class);
				JSONObject jsonObj = new JSONObject(jsonObjStr);
				
				//get objects
				JSONArray objects = jsonObj.getJSONArray("objects");

				// instantiate proper deserializer
				ObjectsSerDeser des = SerDeserFactory
						.getSerDeser(SerDeserFactory.TYPE_DATASET);
				for (int i = 0; i < objects.length(); i++) {
					JSONObject dataset = objects.getJSONObject(i);
					GuiGenericDataSet deserializedObj = (GuiGenericDataSet) des
							.deserialize(dataset);
					result.add(deserializedObj);
				}
				
				otherData = jsonObj.getBoolean("otherData");
				
				offset += limit;
			}
			return result;
			
		} catch (Exception e) {
			logger.error("Error while getting datasets' list", e);
			throw new RuntimeException("Error while getting datasets' list", e);
		}

	}
	
	
	public List<Object> getObjectsList(String offset, String limit, String type) {
		try {
			
			List<Object> result = new ArrayList<Object>();

			//http://<host>:<port>/<path>/objects?type=dataset&offset=0&limit=10
			String jsonObjStr = TilabClient.v1().objects().getAsJson(offset, limit, type, user, auth, String.class);
			JSONObject jsonObj = new JSONObject(jsonObjStr);
			
			//get objects
			JSONArray objects = jsonObj.getJSONArray("objects");

			//instantiate proper deserializer
			ObjectsSerDeser des = SerDeserFactory.getSerDeser(type);
			for(int i= 0; i<objects.length(); i++){
				JSONObject dataset = objects.getJSONObject(i);
				Object deserializedObj = des.deserialize(dataset);
				result.add(deserializedObj);
			}
			return result;	
			
		} catch (Exception e) {
			logger.error("Error while getting objects' list using offset ["
					+ offset + "] limit [" + limit + "] and type [" + type
					+ "]", e);
			throw new RuntimeException(
					"Error while getting objects' list using offset [" + offset
							+ "] limit [" + limit + "] and type [" + type + "]", e);
		}
		
	}
	
	public Object getObjectDetail(String id, String type) {
		try {
		
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
					ObjectsSerDeser des = SerDeserFactory.getSerDeser(type);
					result = (Object)des.deserialize(object);

				}
			}
			return result;
			
		} catch (Exception e) {
			logger.error("Error while getting object detail using id [" + id + "] and type [" + type + "]", e);
			throw new RuntimeException("Error while getting object detail using id [" + id + "] and type [" + type + "]", e);
		}
		
	}
	
	public String writeDataset(DataSource dSRC, GuiGenericDataSet dSet) {	
		try {
			//get table name from datset and put it in datasource objects field
			//then the serializer will use it
			Set<GuiGenericDataSet> objectsGGU = new HashSet<GuiGenericDataSet>();
			objectsGGU.add(dSet);
			dSRC.setObjects(objectsGGU);
			
			
			String datasetLabel = null;
			
			ObjectsSerDeser des1 = SerDeserFactory.getSerDeser(SerDeserFactory.TYPE_DATASOURCE);
			JSONObject inputDataSource = des1.serialize(dSRC, null);
			
			//http://<host>:<port>/<path>/objects
			String responseDatasourceStr = TilabClient.v1().objects().postJson(inputDataSource, user, auth, String.class);
			JSONObject responseDatasource = new JSONObject(responseDatasourceStr);
			//put in array
			JSONArray objects = new JSONArray();
			objects.put(responseDatasource);
			String responseCode = responseDatasource.getString("responseCode");
			String responseDesc = responseDatasource.getString("responseDesc");
			if (responseCode == null) {
				logger.error("Response code is missing when saving datasource");
				throw new RuntimeException("Response code is missing when saving datasource");
			}
			if (!responseCode.equals("200")) {
				logger.error("Error while writing datasource into Tilab console: response code is ["
						+ responseCode
						+ "], response desc is ["
						+ responseDesc + "]");
				throw new RuntimeException(
						"Error while writing datasource into Tilab console: response code is ["
								+ responseCode + "], response desc is ["
								+ responseDesc + "]");
			}
			
			//use datasource for dataset request
			ObjectsSerDeser des2 = SerDeserFactory.getSerDeser(SerDeserFactory.TYPE_DATASET);
			
			JSONObject inputDataSet = des2.serialize(dSet, objects);
			String responseDatasetStr = TilabClient.v1().objects().postJson(inputDataSet, "", "", String.class);
			JSONObject responseDataset = new JSONObject(responseDatasetStr);
			responseCode = responseDataset.getString("responseCode");
			responseDesc = responseDataset.getString("responseDesc");
			if (responseCode == null) {
				logger.error("Response code is missing when saving dataset");
				throw new RuntimeException("Response code is missing when saving dataset");
			}
			if (!responseCode.equals("200")) {
				logger.error("Error while writing dataset into Tilab console: response code is ["
						+ responseCode
						+ "], response desc is ["
						+ responseDesc + "]");
				throw new RuntimeException(
						"Error while writing dataset into Tilab console: response code is ["
								+ responseCode + "], response desc is ["
								+ responseDesc + "]");
			}
			
			datasetLabel = responseDataset.getJSONObject("object").getString("id");
			return datasetLabel;
			
		} catch (Exception e) {
			logger.error("Error while writing dataset [" + dSet + "]", e);
			throw new RuntimeException("Error while writing dataset [" + dSet + "]", e);
		}
		
	}

	public boolean writeObject(Object obj, String type) {	
		try {
		
			boolean result = false;
			
			ObjectsSerDeser des = SerDeserFactory.getSerDeser(type);
			JSONObject biObject = des.serialize(obj, null);
			
			//http://<host>:<port>/<path>/objects
			String responseStr = TilabClient.v1().objects().postJson(biObject, user, auth, String.class);
			JSONObject response = new JSONObject(responseStr);

			if(response.getString("responseCode").equals("200")) {
				result = true;
			}
			return result;
			
		} catch (Exception e) {
			logger.error("Error while writing object [" + obj + "] with type [" + type + "]", e);
			throw new RuntimeException("Error while writing object [" + obj + "] with type [" + type + "]", e);
		}
		
	}
	
	public boolean writeDocument(BIObject object) {
		return this.writeObject(object, SerDeserFactory.TYPE_DASHBOARD);
	}

}
