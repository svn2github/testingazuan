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
package it.eng.spagobi.utilities.callbacks.refresh;

import it.eng.spago.security.IEngUserProfile;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 * Servlet for management the refresh operation into document composition
 */

public class SpagoBIRefreshServlet extends HttpServlet {

    private static transient Logger logger = Logger
	    .getLogger(SpagoBIRefreshServlet.class);

    public void service(HttpServletRequest request, HttpServletResponse response) {
	HttpSession session = request.getSession();

	// read the configuration and set relative object into session
	//DocumentCompositionConfiguration docConf = new DocumentCompositionConfiguration(content);
	//session.setAttribute("docConfig", docConf);
	
	IEngUserProfile profile = (IEngUserProfile) session
		.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	if (profile == null) {
	    logger.error("IEngUserProfile is not in session!!!");
	} else {
	    String username = (String) profile.getUserUniqueIdentifier();
	    String documentLabel =  request.getParameter("DOCUMENT_LABEL");
	    
	    //test
	    String pippo = request.getParameter("lov_id");
	    String spagobiContextUrl = (String) session
		    .getAttribute("spagobicontext");
	    String url = spagobiContextUrl + "/servlet/AdapterHTTP?";
	    url += "USERNAME=" + username;
	    url += "&NEW_SESSION=TRUE";
	    url += "&PAGE=DirectExecutionPage";
	    url += "&DOCUMENT_LABEL=" + request.getParameter("DOCUMENT_LABEL");
	    String documentParameters = "";
	    /* Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
		String parurlname = (String) parameterNames.nextElement();
		if (parurlname.equalsIgnoreCase("DOCUMENT_LABEL"))
		    continue;
		String parvalue = request.getParameter(parurlname);
		documentParameters += "%26" + parurlname + "%3D" + parvalue;
	    }
	    */

	    HashMap tmpParameters = (HashMap)session.getAttribute(documentLabel);
	    if (tmpParameters != null){
	    	try{
		    	Object[] keyParameters = (Object[])tmpParameters.keySet().toArray();
		    	for (int i = 0; i < keyParameters.length; i++ ){
		    		String key = (String)keyParameters[i];
		    		if (key.equalsIgnoreCase("DOCUMENT_LABEL") || key.equalsIgnoreCase("REPORT_PARAMETERS_MAP") )
		    		    continue;
		    		Object value = (Object)tmpParameters.get(key);
		    		//check if the parameter has a new value into the request 
		    		if (request.getParameter(key) != null)
		    				value = (String)request.getParameter(key);
		    		
		    		documentParameters += "%26" + keyParameters[i] + "%3D" + value.toString();
		    	}
	    	}catch(Exception ex){
	    		logger.error("Error during creation url for refresh : " + ex.getMessage());
		    }
	    }
	    url += "&DOCUMENT_PARAMETERS=" + documentParameters;
	    
	    
	    //String url = (request.getParameter("docUrl")==null)?"":(String)request.getParameter("docUrl");
	    try {
		response.sendRedirect(url);
	    } catch (IOException e1) {
		logger.error("IOException during sendRedirect",e1);
	    }
	}

    }

}
