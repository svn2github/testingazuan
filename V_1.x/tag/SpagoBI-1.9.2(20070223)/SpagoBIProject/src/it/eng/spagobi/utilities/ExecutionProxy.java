package it.eng.spagobi.utilities;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.IEngineDriver;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class ExecutionProxy {

	private BIObject biObject = null;
	
	public BIObject getBiObject() {
		return biObject;
	}
	
	public void setBiObject(BIObject biObject) {
		this.biObject = biObject;
	}
	
	public byte[] exec(IEngUserProfile profile) {
		byte[] response = new byte[0];
		try{
			if(biObject==null)
				return response;
			// get the engine of the bionject
			Engine eng = biObject.getEngine();
			// if engine is not an external it's not possible to call it using url
			if(!EngineUtilities.isExternal(eng))
				return response;
			// get driver class 
			String driverClassName = eng.getDriverName();
			// get the url of the engine
			String urlEngine = eng.getUrl();
			// build an instance of the driver
			IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			// get the map of parameter to send to the engine
			Map mapPars = aEngineDriver.getParameterMap(biObject, profile, "");
			// built the request to sent to the engine
			Iterator iterMapPar = mapPars.keySet().iterator();
			HttpClient client = new HttpClient();
		    PostMethod httppost = new PostMethod(urlEngine);
		    while(iterMapPar.hasNext()){
		    	String parurlname = (String)iterMapPar.next();
		    	String parvalue = (String)mapPars.get(parurlname);
		    	httppost.addParameter(parurlname, parvalue);
		    }
		    // sent request to the engine
		    int statusCode = client.executeMethod(httppost);
		    response = httppost.getResponseBody();
		    httppost.releaseConnection();
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "execute", "Error while executing object ", e);
		}
		return response;
	}
	
}
