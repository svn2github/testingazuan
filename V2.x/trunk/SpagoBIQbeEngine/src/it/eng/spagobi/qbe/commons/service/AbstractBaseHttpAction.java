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
package it.eng.spagobi.qbe.commons.service;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractBaseHttpAction extends AbstractHttpAction {
	private SourceBean request;
	private SourceBean response;
	
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractBaseHttpAction.class);
    
	
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) {
		setRequest( request );
		setResponse( response );
	}

	// REQUEST & RESPONSE utility methods
	
	protected SourceBean getRequest() {
		return request;
	}

	private void setRequest(SourceBean request) {
		this.request = request;
	}

	protected SourceBean getResponse() {
		return response;
	}

	private void setResponse(SourceBean response) {
		this.response = response;
	}
	
	
	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}
	
	public String getAttributeAsString(String attrName) {
		return (String)getAttribute(attrName);
	}
	
	public boolean getAttributeAsBoolean(String key) {
		return getAttributeAsBoolean(key, false);
	}

	public boolean getAttributeAsBoolean(String key, boolean defaultValue) {
		if( getAttribute(key) == null ) return false;
		return getAttributeAsString(key).equalsIgnoreCase(TRUE);
	}
	
	public void setAttribute(String key, Object value) {
		try {
			response.setAttribute(key, value);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
	}
	
	
	// SESSION utility methods	
	
	public SessionContainer getSession() {
		return getRequestContainer().getSessionContainer();
	}
	
	public Object getAttributeFromSession(String attrName) {
		return getRequestContainer().getSessionContainer().getAttribute(attrName);
	}
	
	public void delAttributeFromSession(String attrName) {
		if(getAttributeFromSession(attrName) != null) {
			getRequestContainer().getSessionContainer().delAttribute(attrName);
		}
	}
		
	public void setAttributeInSession(String attrName, Object attrValue) {
		delAttributeFromSession(attrName);
		getRequestContainer().getSessionContainer().setAttribute(attrName, attrValue);
	}	

	
	
	// HTTP-SESSION utility methods	
	
	public HttpSession getHttpSession() {
		return getHttpRequest().getSession();
	}
}
