/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

 */

package it.eng.spagobi.utilities;



import java.io.IOException;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;



/**
 * This class has been created to provide SpagoBI Access Utils, in order to
 * customize operations with clients.
 * 
 * @author zoppello
 */
public class SpagoBIAccessUtils {
	
	/**
	 * Starting from the base url and the path as input, gets content
	 * information (response body).
	 * 
	 * @param spagoBIBaseUrl The SpagoBI input base URL
	 * @param path The input path
	 * @return The output response body
	 */
	public byte[] getContent(String spagoBIBaseUrl, String path){
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] parameters = { new NameValuePair("jcrPath", path),
	    		                       new NameValuePair("operation", "getContent") };
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
          } catch (IOException e) {
            System.err.println("Failed to download file.");
            e.printStackTrace();
          } finally {
            // Release the connection.
          	httppost.releaseConnection();
          }
          return responseBody;	    
	}
	
	
	/**
	 * Gets the content of a subObject of the principal object
	 * 
	 * @param spagoBIBaseUrl The SpagoBI service URL
	 * @param path The cms path
	 * @param nameSubObject the name of the subObject
	 * @return The output response body
	 */
	public byte[] getSubObjectContent(String spagoBIBaseUrl, String path, String nameSubObj, String user) {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] data = { new NameValuePair("jcrPath", path),
	    						 new NameValuePair("operation", "getSubObjectContent"),
	    						 new NameValuePair("nameSubObject", nameSubObj),
	    						 new NameValuePair("user", user)};
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(data);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
          } catch (IOException e) {
            System.err.println("Failed to download file.");
            e.printStackTrace();
          } finally {
            // Release the connection.
          	httppost.releaseConnection();
          }
          return responseBody;	    
	}
	
	
	
	
	/**
	 * Save the content of a subObject into SpagoBI Repository
	 * 
	 * @param spagoBIBaseUrl The SpagoBI service URL
	 * @param path The cms path
	 * @param nameSubObject the name of the subObject
	 * @param visibility if true the object is public, else if false is visible only to current user
	 * @param content byte array of the suobject content
	 * @return The output response body
	 */
	public byte[] saveSubObject(String spagoBIBaseUrl, String path, String nameSubObj, 
			                    String description, String user, boolean visibility, 
			                    String content) throws GenericSavingException {
		HttpClient client = new HttpClient();
		String visibilityStr = "false";
		if(visibility)
			visibilityStr = "true";
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] data = { new NameValuePair("jcrPath", path),
	    						 new NameValuePair("operation", "saveSubObject"),
	    						 new NameValuePair("nameSubObject", nameSubObj),
	    						 new NameValuePair("publicVisibility", visibilityStr),
	    						 new NameValuePair("content", content),
	    						 new NameValuePair("user", user),
	    						 new NameValuePair("description", description)};
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(data);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
              
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
       
        } catch (IOException e) {
            System.err.println("Failed to upload file.");
            e.printStackTrace();
            throw new GenericSavingException("Communication Exception");
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        return responseBody;	  
	}
	
	
}
