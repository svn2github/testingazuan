/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.commons.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractBaseHttpAction.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class AbstractBaseHttpAction extends AbstractHttpAction {
	
	/** The request. */
	private SourceBean request;
	
	/** The response. */
	private SourceBean response;
	
	/** The Constant TRUE. */
	public static final String TRUE = "TRUE";
	
	/** The Constant FALSE. */
	public static final String FALSE = "FALSE";
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(AbstractBaseHttpAction.class);
    
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		setRequest( request );
		setResponse( response );
	}

	// REQUEST & RESPONSE utility methods
	
	/**
	 * Gets the request.
	 * 
	 * @return the request
	 */
	protected SourceBean getRequest() {
		return request;
	}

	/**
	 * Sets the request.
	 * 
	 * @param request the new request
	 */
	private void setRequest(SourceBean request) {
		this.request = request;
	}

	/**
	 * Gets the response.
	 * 
	 * @return the response
	 */
	protected SourceBean getResponse() {
		return response;
	}

	/**
	 * Sets the response.
	 * 
	 * @param response the new response
	 */
	private void setResponse(SourceBean response) {
		this.response = response;
	}
	
	
	/**
	 * Gets the attribute.
	 * 
	 * @param key the key
	 * 
	 * @return the attribute
	 */
	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}
	
	/**
	 * Gets the attribute as string.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute as string
	 */
	public String getAttributeAsString(String attrName) {
		return (String)getAttribute(attrName);
	}
	
	/**
	 * Gets the attribute as boolean.
	 * 
	 * @param key the key
	 * 
	 * @return the attribute as boolean
	 */
	public boolean getAttributeAsBoolean(String key) {
		return getAttributeAsBoolean(key, false);
	}

	/**
	 * Gets the attribute as boolean.
	 * 
	 * @param key the key
	 * @param defaultValue the default value
	 * 
	 * @return the attribute as boolean
	 */
	public boolean getAttributeAsBoolean(String key, boolean defaultValue) {
		if( getAttribute(key) == null ) return defaultValue;
		return getAttributeAsString(key).equalsIgnoreCase(TRUE);
	}
	
	/**
	 * Sets the attribute.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public void setAttribute(String key, Object value) {
		try {
			response.setAttribute(key, value);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
	}
	
	
	// SESSION utility methods	
	
	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	public SessionContainer getSession() {
		return getRequestContainer().getSessionContainer();
	}
	
	/**
	 * Gets the attribute from session.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute from session
	 */
	public Object getAttributeFromSession(String attrName) {
		return getRequestContainer().getSessionContainer().getAttribute(attrName);
	}
	
	/**
	 * Del attribute from session.
	 * 
	 * @param attrName the attr name
	 */
	public void delAttributeFromSession(String attrName) {
		if(getAttributeFromSession(attrName) != null) {
			getRequestContainer().getSessionContainer().delAttribute(attrName);
		}
	}
		
	/**
	 * Sets the attribute in session.
	 * 
	 * @param attrName the attr name
	 * @param attrValue the attr value
	 */
	public void setAttributeInSession(String attrName, Object attrValue) {
		delAttributeFromSession(attrName);
		getRequestContainer().getSessionContainer().setAttribute(attrName, attrValue);
	}	

	
	
	// HTTP-SESSION utility methods	
	
	/**
	 * Gets the http session.
	 * 
	 * @return the http session
	 */
	public HttpSession getHttpSession() {
		return getHttpRequest().getSession();
	}
}
