import it.eng.spagobi.rest.client.api.TilabClientAPI;
import it.eng.spagobi.tools.dataset.bo.GuiGenericDataSet;

import java.util.List;

import org.json.JSONException;


public class Test {


	/**
	 * @param args
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
//		TilabClient tc = new TilabClient();
//		Objects objs = tc.v1().objects();
//		//String json = objs.getAsJson(1, 20, type, user, authorization, returnType);
		String json = "{"+
			"'responseCode': '200',"+
			"'responseDesc': 'Operation succeeded',"+
			"'object' : {"+
				"'id' : '4d2a9ddf-2ba6-456d-b708-9b65a1bca811',"+
				"'name' : 'Utenti adsl',"+
				"'description' : 'Utenti ADSL residenziali Telecom Italia',"+
				"'type' : 'dataset',"+
		      	"'creationDate': 1234567890,"+
		    	"'lastModifiedDate': '123457777',"+
		      	"'user':'enelenergia',"+
		      	"'owner':'telecomitalia',"+
		      	"'userObjectStatus': 'Buyed', "+
		      	"'ownerObjectStatus': 'Published',"+
				"'details' : {"+
					"'readOnly' : true,"+
					"'structure' : {"+
						"'columns' : [{	"+
							"'name' : 'user_id',"+
							"'dataType' : {"+
								"'name' : 'integer'"+
							"},"+
							"'biType' : 'attribute'"+
						"},"+
						"{'name' : 'msisdn',"+
						"'dataType' : {"+
							"'name' : 'string',"+
							"'length' : 40"+
						"},"+
						"'biType' : 'attribute'"+
						"},"+
						"{'name' : 'name',"+
						"'dataType' : {"+
							"'name' : 'string ',"+
							"'length' : 40"+
						"},"+
						"'biType' : 'attribute'"+
						"},"+
						"{'name' : 'registration_date',"+
							"'dataType' : {"+
								"'name' : 'date ',"+
								"'dateFormat' : 'YYYYMMDD:HH24:mi:ss'"+
						"},"+
						"'biType' : 'attribute'"+
						"}]"+
					"}"+
				"},"+
				"'objects' :  [{"+
					"'id' : '84889cb8-fade-4121-a48e-90da9f277114',"+
					"'name' : 'Oracle utenti adsl',"+
					"'description' : 'Datasource Oracle utenti ADSL residenziali Telecom Italia',"+
					"'type' : 'datasource',"+
					"'creationDate': 1234567890,"+
					"'lastModifiedDate': '1234567788',"+
					"'user':'enelenergia',"+
					"'owner':'telecomitalia',"+
					"'userObjectStatus': 'Buyed',"+
					"'ownerObjectStatus': 'Published',"+
					"'details' : {"+
						"'subtype' : 'RDBMS',"+
						"'URL' : 'jdbc:oracle:thin:...',"+
						"'username' : 'xxxxxxxx',"+
						"'password' : 'yyyyyyyy',"+
						"'resourceType' : 'table',"+
						"'resource' : 'user_anag',"+
						"'biId' : 'labelxx'"+
						
					"},"+
					"'objects' : []"+
				"}]"+
			"}	"+
		"}";
		
		//use simulator http://localhost:8080/RestServerSimul/
		TilabClientAPI tca = new TilabClientAPI();
		//GuiGenericDataSet gdd = tca.getDatasetDetail("", "", ""); ok
		
		List<GuiGenericDataSet> gdd1 = tca.getDatasetList("0", "10", "", "");
		
	}
	
}
