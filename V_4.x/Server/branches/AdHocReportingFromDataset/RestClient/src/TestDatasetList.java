/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import it.eng.spagobi.rest.client.TilabClient;
import it.eng.spagobi.rest.objects.serDeser.SerDeserFactory;
import it.eng.spagobi.rest.objects.serDeser.ObjectsSerDeser;
import it.eng.spagobi.tools.dataset.bo.GuiDataSetDetail;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TestDatasetList {

	/**
	 * @param args
	 * @throws JSONException 
	 */
	
	
	public static void main(String[] args) throws JSONException {
		List<GuiGenericDataSet> result = new ArrayList<GuiGenericDataSet>();

		//http://<host>:<port>/<path>/objects?type=dataset&offset=0&limit=10
		JSONObject jsonObj = TilabClient.v1().objects().getAsJson("0", "10", SerDeserFactory.TYPE_DATASET, "", "", JSONObject.class);
		//JSONObject jsonObj = new JSONObject(objs);
		
		//get objects
		JSONArray objects = jsonObj.getJSONArray("objects");

		//instantiate proper deserializer
		ObjectsSerDeser des = SerDeserFactory.getDeserializer(SerDeserFactory.TYPE_DATASET);
		for(int i= 0; i<objects.length(); i++){
			JSONObject dataset = objects.getJSONObject(i);
			GuiGenericDataSet deserializedObj = (GuiGenericDataSet)des.deserialize(dataset);
			result.add(deserializedObj);
		}
		//return result;
	}

}
