import it.eng.spagobi.rest.client.TilabClient;
import it.eng.spagobi.rest.objects.serDeser.SerDeserFactory;
import it.eng.spagobi.rest.objects.serDeser.ObjectsSerDeser;
import it.eng.spagobi.tools.dataset.bo.GuiDataSetDetail;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;


public class TestDatasetDetail {

	/**
	 * @param args
	 * @throws JSONException 
	 * @throws SerializationException 
	 */
	public static void main(String[] args) throws SerializationException, JSONException {
		GuiDataSetDetail result = null;

		//http://<host>:<port>/<path>/objects/4d2a9ddf-2ba6-456d-b708-9b65a1bca811
		JSONObject jsonObj = TilabClient.v1().objectsId("idxxxxx").getAsJson("", "", JSONObject.class);
		//JSONObject jsonObj = new JSONObject(objs);
		String type = SerDeserFactory.TYPE_DATASET;
		
		if(jsonObj.getString("responseCode").equals("204")) {
			//get object type
			JSONObject object = jsonObj.getJSONObject("object");
			if(object != null){
				type = object.getString("type");
				//instantiate proper deserializer
				ObjectsSerDeser des = SerDeserFactory.getSerDeser(type);
				result = (GuiDataSetDetail)des.deserialize(object);

			}
		}
		//return result;
	}

}
