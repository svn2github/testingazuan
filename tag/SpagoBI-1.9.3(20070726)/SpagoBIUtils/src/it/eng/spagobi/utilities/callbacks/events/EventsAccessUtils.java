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
	
	public static final String START_EVENT_ID = "startEventId";
	public static final String BIOBJECT_ID = "biobjectId";
	public static final String USER = "user";
	public static final String EVENTS_MANAGER_URL = "events_manager_url";
	public static final String EVENT_TYPE = "event-type";
	public static final String DOCUMENT_EXECUTION_START = "biobj-start-execution";
	public static final String DOCUMENT_EXECUTION_END = "biobj-end-execution";
	
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
