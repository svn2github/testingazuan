import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.rest.client.TilabClient;
import it.eng.spagobi.rest.objects.serDeser.ObjectsSerDeser;
import it.eng.spagobi.rest.objects.serDeser.SerDeserFactory;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;
import it.eng.spagobi.tools.datasource.bo.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TestWriteDataset {

	/**
	 * @param args
	 * @throws SourceBeanException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws JSONException, SourceBeanException {
		
		//change: take them with SpagoBI API
		DataSource dSRC = new DataSource();
		GuiGenericDataSet dSet = new GuiGenericDataSet();
		
		
		ObjectsSerDeser des1 = SerDeserFactory.getDeserializer(SerDeserFactory.TYPE_DATAOURCE);
		JSONObject inputDataSource = des1.serialize(dSRC, null);
		
		//http://<host>:<port>/<path>/objects
		JSONObject responseDatasource = TilabClient.v1().objects().postJson(inputDataSource, "", "", JSONObject.class);
		//put in array
		JSONArray objects = new JSONArray();
		objects.put(responseDatasource);
		//use datasource for dataset request
		
		ObjectsSerDeser des2 = SerDeserFactory.getDeserializer(SerDeserFactory.TYPE_DATASET);
		
		JSONObject inputDataSet = des2.serialize(dSet, objects);
		JSONObject responseDataset = TilabClient.v1().objects().postJson(inputDataSet, "", "", JSONObject.class);
		
	}

}
