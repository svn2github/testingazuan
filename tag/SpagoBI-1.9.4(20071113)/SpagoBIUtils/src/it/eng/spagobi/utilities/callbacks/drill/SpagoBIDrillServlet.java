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
package it.eng.spagobi.utilities.callbacks.drill;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SpagoBIDrillServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String spagobiContextUrl = (String) session.getAttribute("spagobicontext");
		//String spagobiExecutionId = (String) session.getAttribute("spagobi_flow_id");
		String url = spagobiContextUrl + "/servlet/AdapterHTTP?";
		//url += "spagobi_flow_id=" + spagobiExecutionId;
		//url += "&USERNAME=" + username;
		url += "USERNAME=" + username;
		url += "&NEW_SESSION=TRUE";
		url += "&PAGE=DirectExecutionPage";
		url += "&DOCUMENT_LABEL=" + request.getParameter("DOCUMENT_LABEL");
	    String documentParameters = "";
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
	    	String parurlname = (String) parameterNames.nextElement();
	    	if (parurlname.equalsIgnoreCase("DOCUMENT_LABEL")) 
	    		continue;
	    	String parvalue = request.getParameter(parurlname);
	    	documentParameters += "%26" + parurlname + "%3D" + parvalue;
	    }
	    url += "&DOCUMENT_PARAMETERS=" + documentParameters;
		
		try {
			response.sendRedirect(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/*
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagobiContextUrl + "/servlet/AdapterHTTP");
	    httppost.addParameter("USERNAME", username);
	    httppost.addParameter("NEW_SESSION", "TRUE");
	    httppost.addParameter("PAGE", "DirectExecutionPage");
	    httppost.addParameter("DOCUMENT_LABEL", request.getParameter("DOCUMENT_LABEL"));
	    String documentParameters = "";
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
	    	String parurlname = (String) parameterNames.nextElement();
	    	if (parurlname.equalsIgnoreCase("DOCUMENT_LABEL")) 
	    		continue;
	    	String parvalue = request.getParameter(parurlname);
	    	documentParameters += "&" + parurlname + "=" + parvalue;
	    }
	    httppost.addParameter("DOCUMENT_PARAMETERS", documentParameters);
	    // sends request to SpagoBI
	    try {
			int statusCode = client.executeMethod(httppost);
			byte[] responseBytes = httppost.getResponseBody();
		    httppost.releaseConnection();
			response.getOutputStream().write(responseBytes);
			response.getOutputStream().flush();	
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
	}
	
}
