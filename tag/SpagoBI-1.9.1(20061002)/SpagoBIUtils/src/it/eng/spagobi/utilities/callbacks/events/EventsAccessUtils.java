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
package it.eng.spagobi.utilities.callbacks.events;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * A proxy class used by clients to remotly access the spagoBI EventHandler 
 * interface in a customized way.
 * 
 * @author Gioia
 */
public class EventsAccessUtils {
	
	private String eventsManagerServletUrl;
	
	public EventsAccessUtils(String eventsManagerServletUrl) {
		this.eventsManagerServletUrl = eventsManagerServletUrl;
	}
	
	public long registerEvent(String user) {
		return 0;
	}
	
	public long registerHandler(long eventId, Object handler) {
		return 0;
	}
	
	private String getParamsStr(Map params) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = params.keySet().iterator();
		boolean isFirstParameter = true;
		while(it.hasNext()) {
			String pname = (String)it.next();
			String pvalue = (String)params.get(pname);
			if(!isFirstParameter) buffer.append("&");
			else isFirstParameter = false;
			buffer.append(pname + "=" + pvalue);
		}
		return buffer.toString();
	}
	
	public Integer fireEvent(String user, String desc, Map params, String rolesHandlerClassName, String presentationHandler) throws Exception {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(eventsManagerServletUrl);
	    NameValuePair[] parameters = {  new NameValuePair("operation", "fireEvent"), 
	    								new NameValuePair("user", user),				
	    								new NameValuePair("desc", desc),
	    								new NameValuePair("rolesHandler", rolesHandlerClassName),
	    								new NameValuePair("presentationHandler", presentationHandler),
	    								new NameValuePair("parameters", getParamsStr(params))};
	    
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
            System.err.println("Failed to get response body.");
            e.printStackTrace();
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        if (responseBody == null) throw new Exception("Failed to register event");
        String idStr = new String(responseBody);
        Integer id = new Integer(idStr);
        return id;
	}
}
