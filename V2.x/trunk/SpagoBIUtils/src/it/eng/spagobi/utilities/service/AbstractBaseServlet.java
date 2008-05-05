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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractBaseServlet extends HttpServlet {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);	
    }
	
	
    ///////////////////////////////////////////////////////////
    // REQUEST
    ///////////////////////////////////////////////////////////
	protected HttpServletRequest getRequest() {
		return request;
	}
	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	protected Object getParameter(String parName) {
		return getRequest().getParameter( parName );
	}
	
	protected String getParameterAsString(String parName) {
		if(requestContainsParameter(parName)) {
			return getRequest().getParameter( parName ).toString();
		}
		return null;
	}
	
	protected boolean requestContainsParameter(String parName) {
		return getParameter(parName) != null;
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
	
	
	
	
	///////////////////////////////////////////////////////////
    // SESSION
    ///////////////////////////////////////////////////////////	
	protected HttpSession getSession() {
		return getRequest().getSession();
	}
	
}
