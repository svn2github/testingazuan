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

import it.eng.spagobi.engines.talend.exception.AuthenticationFailedException;
import it.eng.spagobi.engines.talend.runtime.JobDeploymentDescriptor;
import it.eng.spagobi.engines.talend.services.JobUploadService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngineClient {
	String host;
	String port;
	String appContext;
	
	private static transient Logger logger = Logger.getLogger(SpagoBITalendEngineClient.class);
	
	private static final String JOB_UPLOAD_SERVICE = "JobUploadService";
	private static final String ENGINE_INFO_SERVICE = "EngineInfoService";
	
	private String getServiceUrl(String serviceName) {
		return ("http://" + host + ":" + port + "/" + appContext + "/" + serviceName);
	}
	
	
	public SpagoBITalendEngineClient(String usr, String pwd, String host,String port, String appContext)  
	throws AuthenticationFailedException { 
		this.host = host;
		this.port = port;
		this.appContext = appContext;
	} 

	public String getEngineVersion() {
		return getEngineInfo("version");
	}
	
	public String getEngineName() {
		return getEngineInfo("name");
	}
	
	public boolean isEngineAvailible() {
		return (getEngineInfo("version") != null);
	}
	
	private String getEngineInfo(String infoType) {
		String version;
		HttpClient client;
		PostMethod method;
		NameValuePair[] nameValuePairs;
		
		version = null;
		client = new HttpClient();
		method = new PostMethod(getServiceUrl(ENGINE_INFO_SERVICE));
		
		// Provide custom retry handler is necessary
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler(3, false));

	   nameValuePairs   = new NameValuePair[] {
	    		new NameValuePair("infoType", infoType)
	    };
       
        method.setRequestBody( nameValuePairs );
	    
	    try {
	      // Execute the method.
	      int statusCode = client.executeMethod(method);

	      if (statusCode != HttpStatus.SC_OK) {
	        System.err.println("Method failed: " + method.getStatusLine());
	        System.err.println("Response body: " + method.getResponseBodyAsString());
	      } else {
	    	  version = method.getResponseBodyAsString();
	      }      

	    } catch (HttpException e) {
	      System.err.println("Fatal protocol violation: " + e.getMessage());
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("Fatal transport error: " + e.getMessage());
	      e.printStackTrace();
	    } finally {
	      // Release the connection.
	      method.releaseConnection();
	    }  	
		
		return version;
	}
		
	public boolean deployJob(JobDeploymentDescriptor jobDeploymentDescriptor, File executableJobFiles)  {
		
		HttpClient client;
		PostMethod method;	
		File deploymentDescriptorFile;
		boolean result = false;
		
		client = new HttpClient();
		method = new PostMethod(getServiceUrl(JOB_UPLOAD_SERVICE));
		deploymentDescriptorFile = null;
		
		try {			
			deploymentDescriptorFile = File.createTempFile("deploymentDescriptor", ".xml");		
			FileWriter writer = new FileWriter(deploymentDescriptorFile);
			writer.write(jobDeploymentDescriptor.toXml());
			writer.flush();
			writer.close();
					
				        
			Part[] parts = {
					new FilePart(executableJobFiles.getName(), executableJobFiles),
	                new FilePart("deploymentDescriptor", deploymentDescriptorFile)
	        };
			
			method.setRequestEntity(
					new MultipartRequestEntity(parts, method.getParams())
	        );
			
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			
        	int status = client.executeMethod(method);
            if (status == HttpStatus.SC_OK) {
                if(method.getResponseBodyAsString().equalsIgnoreCase("OK")) result = true;
            } else {
            	System.out.println(
                    "Upload failed, response=" + HttpStatus.getStatusText(status)
                );
            }
        } catch (Exception ex) {
        	System.err.println("ERROR: " + ex.getClass().getName() + " "+ ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            method.releaseConnection();
            if(deploymentDescriptorFile != null) deploymentDescriptorFile.delete();
        }
        
        return result;
	}
	
	public static void main(String[] args) throws ZipException, IOException {
		
		
		try {		
			SpagoBITalendEngineClient client 
			= new SpagoBITalendEngineClient("biadmin", "biadmin", "localhost", "8080", "SpagoBITalendEngine");
			
			if(client.isEngineAvailible()) {
				System.out.println("Engine version: " + client.getEngineVersion());
				System.out.println("Engine fullname: " + client.getEngineName());
				
				JobDeploymentDescriptor jobDeploymentDescriptor = new JobDeploymentDescriptor("Test", "perl");
				File zipFile = new File("C:\\Prototipi\\TalendJob2.zip");
				
				boolean result = client.deployJob(jobDeploymentDescriptor, zipFile);
				if(result) System.out.println("Jobs deployed succesfully");
				else System.out.println("Jobs not deployed");
			}	
		} catch(AuthenticationFailedException e) {
			System.err.println("Authentication failed");
		}
	}
	
}
