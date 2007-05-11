package it.eng.spagobi.utilities;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.IEngineDriver;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class ExecutionProxy {

	private BIObject biObject = null;
	
	private String returnedContentType = null;
	
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
			
		    // AUDIT
			AuditManager auditManager = AuditManager.getInstance();
			Integer executionId = auditManager.insertAudit(biObject, profile, "", "SCHEDULATION");
			// adding parameters for AUDIT updating
			if (executionId != null) {
				mapPars.put(AuditManager.AUDIT_ID, executionId.toString());
				mapPars.put(AuditManager.AUDIT_SERVLET, GeneralUtilities.getSpagoBiAuditManagerServlet());
			}
			
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
		    
		    Header headContetType =  httppost.getResponseHeader("Content-Type");
		    if(headContetType!=null) {
		    	returnedContentType = headContetType.getValue();
		    } else {
		    	returnedContentType = "application/octet-stream";
		    }
		    
		    httppost.releaseConnection();
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "execute", "Error while executing object ", e);
		}
		return response;
	}

	public String getReturnedContentType() {
		return returnedContentType;
	}

	public void setReturnedContentType(String returnedContentType) {
		this.returnedContentType = returnedContentType;
	}
	
	public String getFileExtensionFromContType(String contentType) {
		String extension = "";
		if(contentType.equalsIgnoreCase("text/html")) {
			extension = ".html";
		} else if (contentType.equalsIgnoreCase("text/xml")) {
			extension = ".xml";
		} else if (contentType.equalsIgnoreCase("text/plain")) {
			extension = ".txt";
		} else if (contentType.equalsIgnoreCase("text/csv")) {
			extension = ".csv";
		} else if (contentType.equalsIgnoreCase("application/pdf")) {
			extension = ".pdf";
		} else if (contentType.equalsIgnoreCase("application/rtf")) {
			extension = ".pdf";
		} else if (contentType.equalsIgnoreCase("application/vnd.ms-excel")) {
			extension = ".xls";
		} else if (contentType.equalsIgnoreCase("application/msword")) {
			extension = ".word";
		} else if (contentType.equalsIgnoreCase("image/jpeg")) {
			extension = ".jpg";
		} else if (contentType.equalsIgnoreCase("application/powerpoint")) {
			extension = ".ppt";
		} else if (contentType.equalsIgnoreCase("application/vnd.ms-powerpoint")) {
			extension = ".ppt";
		} else if (contentType.equalsIgnoreCase("application/x-mspowerpoint")) {
			extension = ".ppt";
		} else if (contentType.equalsIgnoreCase("image/svg+xml")) {
			extension = ".svg";
		} 
		
		
		// TODO complete list
		return extension;
	}
	
}
