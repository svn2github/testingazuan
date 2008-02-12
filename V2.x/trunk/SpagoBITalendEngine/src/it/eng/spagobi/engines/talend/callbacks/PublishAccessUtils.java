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
package it.eng.spagobi.engines.talend.callbacks;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author Andrea Gioia
 *
 */
public class PublishAccessUtils {
	
	private static String serviceName = "PublishService";
	
	
	public static void publish(String url,
							   String user, String password,
							   String label, String name, String description, 
							   boolean encrypted, boolean visble, 
							   String type, String state,
							   String functionalityCode, String template) throws Exception {
		
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(url + "/" + serviceName);
	    NameValuePair[] parameters = {  new NameValuePair("OPERATION", "PUBLISH"), 
	    								new NameValuePair("usr", user),				
	    								new NameValuePair("pwd", password),
	    								new NameValuePair("label", label),
	    								new NameValuePair("name", name),
	    								new NameValuePair("description", description),
	    								new NameValuePair("encrypted", "" + encrypted),
	    								new NameValuePair("visible", "" + visble),
	    								new NameValuePair("type", type),
	    								new NameValuePair("state", state),
	    								new NameValuePair("functionalityCode", functionalityCode),
	    								new NameValuePair("template", template),
	    							  };
	    
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
            System.err.println("Failed to get response body.");
            e.printStackTrace();
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        if (responseBody == null) throw new Exception("Failed to register event");
        String idStr = new String(responseBody);
	}
}
