/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
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

**/
package it.eng.spagobi.utilities.service;

import java.io.IOException;

import it.eng.spagobi.utilities.container.HttpServletRequestContainer;
import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbstractBaseServlet extends HttpServlet {
	private HttpServletRequestContainer requestContainer;
	private HttpServletResponse response;
	
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	
	 /**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractBaseServlet.class);
    
	

    public void init(ServletConfig config) throws ServletException {
    	super.init(config);	
    }
    
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	this.setRequest(request);
    	this.setResponse(response);
    	
    	try {
    		String str = this.getClass().getName();
			this.doService();
		} catch (Throwable t) {
			handleException(t);
		}
    }
    
    public abstract void doService() throws SpagoBIEngineException;
    
    public abstract void handleException(Throwable t);
	
	
    ///////////////////////////////////////////////////////////
    // REQUEST
    ///////////////////////////////////////////////////////////
	protected HttpServletRequest getRequest() {
		return requestContainer.getRequest();
	}
	protected void setRequest(HttpServletRequest request) {
		if(requestContainer == null) {
			requestContainer = new HttpServletRequestContainer(request);
		} else {
			this.requestContainer.setRequest(request);
		}
		
	}
	
	protected Object getParameter(String parName) {
		return requestContainer.getProperty(parName);
	}
	
	protected String getParameterAsString(String parName) {
		return requestContainer.getPropertyAsString(parName);
	}
	
	protected boolean requestContainsParameter(String parName) {
		return requestContainer.containsProperty( parName );
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////
    // RESPONSE
    ///////////////////////////////////////////////////////////
	protected HttpServletResponse getResponse() {
		return response;
	}
	protected void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public boolean tryToWriteBackToClient(String message) {
		try {
			writeBackToClient(message);
		} catch (IOException e) {
			logger.error("Impossible to write back to the client the message: [" + message + "]", e);
			return false;
		}
		
		return true;
	}
	
	public void writeBackToClient(String message) throws IOException {
		writeBackToClient(200, message,
				true,
				"service-response",
				"text/plain");
	}
	
	public void writeBackToClient(int statusCode, String content, boolean inline, String fileName, String contentType) throws IOException {
		
		// setup response header
		getResponse().setHeader("Content-Disposition", (inline?"inline":"attachment") + "; filename=\"" + fileName + "\";");
		getResponse().setContentType( contentType );
		getResponse().setContentLength( content.length() );
		getResponse().setStatus(statusCode);
		
		getResponse().getWriter().print(content);
		getResponse().getWriter().flush();
	}
	
	
	
	
	///////////////////////////////////////////////////////////
    // SESSION
    ///////////////////////////////////////////////////////////	
	protected HttpSession getHttpSession() {
		return getRequest().getSession();
	}
	
	protected Object getAttributeFromHttpSession(String attrName) {
		return getHttpSession().getAttribute( attrName );
	}
	
	public String getAttributeFromHttpSessionAsString(String attrName) {
		return (String)getAttributeFromHttpSession(attrName);
	}
	
}
