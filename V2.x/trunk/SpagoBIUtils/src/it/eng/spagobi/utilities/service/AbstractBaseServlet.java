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
package it.eng.spagobi.utilities.service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractBaseServlet extends HttpServlet {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	

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
