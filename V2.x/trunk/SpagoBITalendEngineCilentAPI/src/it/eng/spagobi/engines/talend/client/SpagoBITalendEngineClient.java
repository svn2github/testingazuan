/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.talend.client;

import it.eng.spagobi.engines.talend.client.exception.AuthenticationFailedException;
import it.eng.spagobi.engines.talend.client.exception.EngineUnavailableException;
import it.eng.spagobi.engines.talend.client.exception.ServiceInvocationFailedException;
import it.eng.spagobi.engines.talend.client.exception.UnsupportedEngineVersionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngineClient implements ISpagoBITalendEngineClient {

	public static final int CLIENTAPI_MAJOR_VERSION_NUMBER = 0;
	public static final int CLIENTAPI_MINOR_VERSION_NUMBER = 5;
	public static final int CLIENTAPI_REVISION_VERSION_NUMBER = 0;
	public static final String CLIENTAPI_VERSION_NUMBER = CLIENTAPI_MAJOR_VERSION_NUMBER + "." 
														+ CLIENTAPI_MINOR_VERSION_NUMBER + "."
														+ CLIENTAPI_REVISION_VERSION_NUMBER;
	
	private ISpagoBITalendEngineClient client;
	
	public SpagoBITalendEngineClient(String usr, String pwd, String host,String port, String appContext) 
	throws EngineUnavailableException, ServiceInvocationFailedException, UnsupportedEngineVersionException  { 
		String url = "http://" + host + ":" + port + "/" + appContext + "/EngineInfoService";
		String complianceVersion = getEngineComplianceVersion(url);
		String[] versionChunks = complianceVersion.split("\\.");
		int major = Integer.parseInt(versionChunks[0]);
		int minor = Integer.parseInt(versionChunks[1]);
		if(major > CLIENTAPI_MAJOR_VERSION_NUMBER 
				|| (major == CLIENTAPI_MAJOR_VERSION_NUMBER && minor > CLIENTAPI_MINOR_VERSION_NUMBER)) {
			throw new UnsupportedEngineVersionException("Unsupported engine version", complianceVersion);
		}
		
		if (major == 0 && minor == 5) {
			client = new SpagoBITalendEngineClient_0_5_0(usr, pwd, host, port, appContext);
		}		
	}
		
	public boolean deployJob(JobDeploymentDescriptor jobDeploymentDescriptor, File executableJobFiles) 
	throws EngineUnavailableException, AuthenticationFailedException, ServiceInvocationFailedException {
		return client.deployJob(jobDeploymentDescriptor, executableJobFiles);
	}

	public String getEngineName() throws EngineUnavailableException, ServiceInvocationFailedException {
		return client.getEngineName();
	}

	public String getEngineVersion() throws EngineUnavailableException, ServiceInvocationFailedException {
		return client.getEngineVersion();
	}

	public boolean isEngineAvailible() throws EngineUnavailableException {
		return client.isEngineAvailible();
	}	

	public static String getEngineComplianceVersion(String url) 
	throws EngineUnavailableException, ServiceInvocationFailedException {
		String version;
		HttpClient client;
		PostMethod method;
		NameValuePair[] nameValuePairs;
		
		version = null;
		client = new HttpClient();
		method = new PostMethod(url);
		
		// Provide custom retry handler is necessary
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler(3, false));

	    nameValuePairs   = new NameValuePair[] {
	    		new NameValuePair("infoType", "complianceVersion")
	    };
       
        method.setRequestBody( nameValuePairs );
	    
	    try {
	      // Execute the method.
	      int statusCode = client.executeMethod(method);

	      if (statusCode != HttpStatus.SC_OK) {  
	        throw new ServiceInvocationFailedException("Service failed", 
	        		method.getStatusLine().toString(),
	        		method.getResponseBodyAsString());
	      } else {
	    	  version = method.getResponseBodyAsString();
	      }      

	    } catch (HttpException e) {
	    	throw new EngineUnavailableException("Fatal protocol violation: " + e.getMessage());	
	    } catch (IOException e) {
	    	throw new EngineUnavailableException("Fatal transport error: " + e.getMessage());
	    } finally {
	      // Release the connection.
	      method.releaseConnection();
	    }  	
		
		return version;
	}
}
