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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import javax.servlet.http.HttpServletResponse;
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
    
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
        super.init(config);
    } 
	

	// REQUEST utility methods
	
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
	
	/**
	 * Gets the attribute.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute
	 */
	public Object getAttribute(String attrName) {
		return request.getAttribute(attrName);
	}
	
	/**
	 * Gets the attribute as string.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute as string
	 */
	public String getAttributeAsString(String attrName) {
		if ( requestContainsAttribute(attrName) ) {
			return getAttribute(attrName).toString();
		}
		
		return null;
	}
	
	/**
	 * Gets the attribute as boolean.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute as boolean
	 */
	public boolean getAttributeAsBoolean(String attrName) {
		return getAttributeAsBoolean(attrName, false);
	}

	/**
	 * Gets the attribute as boolean.
	 * 
	 * @param attrName the attr name
	 * @param defaultValue the default value
	 * 
	 * @return the attribute as boolean
	 */
	public boolean getAttributeAsBoolean(String attrName, boolean defaultValue) {
		if( getAttribute(attrName) == null ) return defaultValue;
		return getAttributeAsString(attrName).equalsIgnoreCase(TRUE);
	}
	
	/**
	 * Gets the attribute as integer.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute as integer
	 */
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
	
	/**
	 * Del attribute.
	 * 
	 * @param attrName the attr name
	 */
	public void delAttribute(String attrName) {
		if( requestContainsAttribute(attrName) ) {
			try {
				request.delAttribute(attrName);
			} catch (SourceBeanException e) {
				logger.warn("Impossible to delete parameter " + attrName + " from request", e);
			}
		}
	}
	
	/**
	 * Request contains attribute.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return true, if successful
	 */
	public boolean requestContainsAttribute(String attrName) {
		return (getAttribute(attrName) != null);
	}
	
	/**
	 * Request contains attribute.
	 * 
	 * @param attrName the attr name
	 * @param attrValue the attr value
	 * 
	 * @return true, if successful
	 */
	public boolean requestContainsAttribute(String attrName, String attrValue) {
		return ( requestContainsAttribute(attrName) && getAttribute(attrName).toString().equalsIgnoreCase(attrValue) );
	}
	
	
	// RESPONSE utility methods
	
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
	
	public void writeBackToClient(IServiceResponse response) throws IOException {
		writeBackToClient(response.getContent(),
				response.isInline(),
				response.getName(),
				response.getType());
	}
	
	public void writeBackToClient(String content, boolean inline, String contentName, String contentType) throws IOException {
		freezeHttpResponse();
		
		// setup response header
		getHttpResponse().setHeader("Content-Disposition", (inline?"inline":"attachment") + "; filename=\"" + contentName + "\";");
		getHttpResponse().setContentType( contentType );
		getHttpResponse().setContentLength( content.length() );
		
		getHttpResponse().getWriter().print(content);
	}
	
	public void writeBackToClient(File file, boolean inline, String contentName, String contentType) throws IOException {
		BufferedInputStream bis = null;
		
		bis = new BufferedInputStream( new FileInputStream(file) );
		try {
			writeBackToClient(bis, inline, contentName, contentType);
		} finally {
			bis.close();
		}		
	}
	
	public void writeBackToClient(InputStream in, boolean inline, String contentName, String contentType) throws IOException {		
		int contentLength = 0;
		int b = -1;
		
		freezeHttpResponse();
		
		// setup response header
		getHttpResponse().setHeader("Content-Disposition", (inline?"inline":"attachment") + "; filename=\"" + contentName + "\";");
		getHttpResponse().setContentType( contentType );
		
		while((b = in.read()) != -1) {
			getHttpResponse().getOutputStream().write(b);
			contentLength++;
		}		
		getHttpResponse().getOutputStream().flush();
		
		getHttpResponse().setContentLength( contentLength );
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
		return getSession().getAttribute(attrName);
	}
	
	/**
	 * Gets the attribute from session as string.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute from session as string
	 */
	public String getAttributeFromSessionAsString(String attrName) {
		if(sessionContainsAttribute(attrName)) {
			return getAttributeFromSession(attrName).toString();
		}
		
		return null;
	}
	
	/**
	 * Gets the attribute from session as boolean.
	 * 
	 * @param key the key
	 * 
	 * @return the attribute from session as boolean
	 */
	public boolean getAttributeFromSessionAsBoolean(String key) {
		return getAttributeFromSessionAsBoolean(key, false);
	}

	/**
	 * Gets the attribute from session as boolean.
	 * 
	 * @param key the key
	 * @param defaultValue the default value
	 * 
	 * @return the attribute from session as boolean
	 */
	public boolean getAttributeFromSessionAsBoolean(String key, boolean defaultValue) {
		if( getAttributeFromSession(key) == null ) return defaultValue;
		return getAttributeFromSessionAsString(key).equalsIgnoreCase(TRUE);
	}
	
	/**
	 * Del attribute from session.
	 * 
	 * @param attrName the attr name
	 */
	public void delAttributeFromSession(String attrName) {
		if(getAttributeFromSession(attrName) != null) {
			getSession().delAttribute(attrName);
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
		getSession().setAttribute(attrName, attrValue);
	}	
	
	/**
	 * Session contains attribute.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return true, if successful
	 */
	public boolean sessionContainsAttribute(String attrName) {
		return getAttributeFromSession(attrName) != null;
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
	
	/**
	 * Gets the attribute from http session.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute from http session
	 */
	public Object getAttributeFromHttpSession(String attrName) {
		return getHttpSession().getAttribute(attrName);
	}
	
	/**
	 * Gets the attribute from http session as string.
	 * 
	 * @param attrName the attr name
	 * 
	 * @return the attribute from http session as string
	 */
	public String getAttributeFromHttpSessionAsString(String attrName) {
		return (String)getAttributeFromHttpSession(attrName);
	}
	
	/**
	 * Gets the attribute from http session as boolean.
	 * 
	 * @param key the key
	 * 
	 * @return the attribute from http session as boolean
	 */
	public boolean getAttributeFromHttpSessionAsBoolean(String key) {
		return getAttributeFromHttpSessionAsBoolean(key, false);
	}

	/**
	 * Gets the attribute from http session as boolean.
	 * 
	 * @param key the key
	 * @param defaultValue the default value
	 * 
	 * @return the attribute from http session as boolean
	 */
	public boolean getAttributeFromHttpSessionAsBoolean(String key, boolean defaultValue) {
		if( getAttributeFromSession(key) == null ) return defaultValue;
		return getAttributeFromSessionAsString(key).equalsIgnoreCase(TRUE);
	}
	
	/**
	 * Del attribute from http session.
	 * 
	 * @param attrName the attr name
	 */
	public void delAttributeFromHttpSession(String attrName) {
		if(getAttributeFromHttpSession(attrName) != null) {
			getHttpSession().removeAttribute(attrName);
		}
	}
		
	/**
	 * Sets the attribute in http session.
	 * 
	 * @param attrName the attr name
	 * @param attrValue the attr value
	 */
	public void setAttributeInHttpSession(String attrName, Object attrValue) {
		delAttributeFromHttpSession(attrName);
		getHttpSession().setAttribute(attrName, attrValue);
	}	

}
