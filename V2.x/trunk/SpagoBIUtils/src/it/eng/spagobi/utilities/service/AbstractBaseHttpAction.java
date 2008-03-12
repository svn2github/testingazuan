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

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbstractBaseHttpAction extends AbstractHttpAction {
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
	

	// REQUEST & RESPONSE utility methods
	
	protected SourceBean getRequest() {
		return request;
	}

	protected void setRequest(SourceBean request) {
		this.request = request;
	}

	protected SourceBean getResponse() {
		return response;
	}

	protected void setResponse(SourceBean response) {
		this.response = response;
	}
	
	public Object getAttribute(String attrName) {
		return request.getAttribute(attrName);
	}
	
	public String getAttributeAsString(String attrName) {
		if ( requestContainsAttribute(attrName) ) {
			return getAttribute(attrName).toString();
		}
		
		return null;
	}
	
	public boolean getAttributeAsBoolean(String attrName) {
		return getAttributeAsBoolean(attrName, false);
	}

	public boolean getAttributeAsBoolean(String attrName, boolean defaultValue) {
		if( getAttribute(attrName) == null ) return defaultValue;
		return getAttributeAsString(attrName).equalsIgnoreCase(TRUE);
	}
	
	public Integer getAttributeAsInteger(String attrName) {
		Integer attrValue;
		
		if( !requestContainsAttribute(attrName) ) return null;
		try {
			attrValue = new Integer( getAttributeAsString(attrName) );
		} catch(NumberFormatException e) {
			logger.warn("Impossible to convert request parameter " + attrName 
					+ " whose value is " + getAttributeAsString(attrName)
					+ " to an integer", e);
			
			attrValue = null;
		}
		
		return attrValue;
	}
	
	public void delAttribute(String attrName) {
		if( requestContainsAttribute(attrName) ) {
			try {
				request.delAttribute(attrName);
			} catch (SourceBeanException e) {
				logger.warn("Impossible to delete parameter " + attrName + " from request", e);
			}
		}
	}
	
	public boolean requestContainsAttribute(String attrName) {
		return (getAttribute(attrName) != null);
	}
	
	public boolean requestContainsAttribute(String attrName, String attrValue) {
		return ( requestContainsAttribute(attrName) && getAttribute(attrName).toString().equalsIgnoreCase(attrValue) );
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
		return getSession().getAttribute(attrName);
	}
	
	public String getAttributeFromSessionAsString(String attrName) {
		if(sessionContainsAttribute(attrName)) {
			return getAttributeFromSession(attrName).toString();
		}
		
		return null;
	}
	
	public boolean getAttributeFromSessionAsBoolean(String key) {
		return getAttributeFromSessionAsBoolean(key, false);
	}

	public boolean getAttributeFromSessionAsBoolean(String key, boolean defaultValue) {
		if( getAttributeFromSession(key) == null ) return defaultValue;
		return getAttributeFromSessionAsString(key).equalsIgnoreCase(TRUE);
	}
	
	public void delAttributeFromSession(String attrName) {
		if(getAttributeFromSession(attrName) != null) {
			getSession().delAttribute(attrName);
		}
	}
		
	public void setAttributeInSession(String attrName, Object attrValue) {
		delAttributeFromSession(attrName);
		getSession().setAttribute(attrName, attrValue);
	}	
	
	public boolean sessionContainsAttribute(String attrName) {
		return getAttributeFromSession(attrName) != null;
	}
	
	

	
	
	// HTTP-SESSION utility methods	
	
	public HttpSession getHttpSession() {
		return getHttpRequest().getSession();
	}
	
	public Object getAttributeFromHttpSession(String attrName) {
		return getHttpSession().getAttribute(attrName);
	}
	
	public String getAttributeFromHttpSessionAsString(String attrName) {
		return (String)getAttributeFromHttpSession(attrName);
	}
	
	public boolean getAttributeFromHttpSessionAsBoolean(String key) {
		return getAttributeFromHttpSessionAsBoolean(key, false);
	}

	public boolean getAttributeFromHttpSessionAsBoolean(String key, boolean defaultValue) {
		if( getAttributeFromSession(key) == null ) return defaultValue;
		return getAttributeFromSessionAsString(key).equalsIgnoreCase(TRUE);
	}
	
	public void delAttributeFromHttpSession(String attrName) {
		if(getAttributeFromHttpSession(attrName) != null) {
			getHttpSession().removeAttribute(attrName);
		}
	}
		
	public void setAttributeInHttpSession(String attrName, Object attrValue) {
		delAttributeFromHttpSession(attrName);
		getHttpSession().setAttribute(attrName, attrValue);
	}	

}
