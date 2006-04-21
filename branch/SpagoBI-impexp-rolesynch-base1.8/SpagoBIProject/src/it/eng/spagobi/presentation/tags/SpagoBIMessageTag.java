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
package it.eng.spagobi.presentation.tags;

import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Custom tag that retrieves message using spago facilities
 * 
 * @author zoppello
 */
public class SpagoBIMessageTag extends TagSupport {

	private static final String DEFAULT_BUNDLE = "messages";
	
	/**
     * The arguments.
     */
    protected String args = null;
    
    /**
     * The message key of the message to be retrieved.
     */
    protected String key = null;
    
    /**
     * The servlet context attribute key for our resources.
     */
    protected String bundle = null;
        
    /**
     * @return The arguments
     */
    public String getArgs() {
        return (this.args);
    }
    /**
     * 
     * @param args	The arguments to set
     */
    public void setArgs(String args) {
    	this.args = args;
    }
    /**
     * 
     * @return The servlet context attribute key
     */
    public String getBundle() {
        return (this.bundle);
    }
    /**
     * 
     * @param bundle	The servlet context attribute key to set
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
    /**
     * 
     * @return The reference key
     */
    public String getKey() {
        return (this.key);
    }
    /**
     * 
     * @param key	The key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        String key = this.key;
       
        // Construct the optional arguments array we will be using
        Object[] arguments = new Object[0];
        if(args!=null) {
        	arguments = args.split("\\|");
        }

        HttpServletRequest httpRequest = (HttpServletRequest)pageContext.getRequest();
    	PortletRequest renderRequest = (PortletRequest)httpRequest.getAttribute("javax.portlet.request");
        
        String message = null;        
        if (bundle != null)
        	message = PortletUtilities.getMessage(key, bundle);
        else
        	message = getMessage(renderRequest, key); // Use the default spago bundle
        
        for (int i=0; i<arguments.length; i++){
        	message = replace(message, i, arguments[i].toString());
        }
        
        StringBuffer htmlStream = new StringBuffer();
        htmlStream.append(message);        
        try {
            pageContext.getOut().print(htmlStream);
        } 
        catch (Exception ex) {
            SpagoBITracer.critical("Utilities", this.getClass().getName(), "doStartTag", "Impossible to elaborate pageContext stream");
            throw new JspException("Impossible to elaborate pageContext stream");
        } 
        return SKIP_BODY;
	}
	
	
    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        key = null;
    }
    /**
     * Substitutes the message value to the placeholders.
     * 
     * @param messageFormat The String representing the message format
     * @param iParameter	The numeric value defining the replacing string
     * @param value	Input object containing parsing information
     * @return	The parsed string
     */
    protected String replace(String messageFormat, int iParameter, Object value) {
		if (value != null) {
			String toParse = messageFormat;
			String replacing = "%" + iParameter;
			String replaced = value.toString();
			StringBuffer parsed = new StringBuffer();
			int parameterIndex = toParse.indexOf(replacing);
			while (parameterIndex != -1) {
				parsed.append(toParse.substring(0, parameterIndex));
				parsed.append(replaced);
				toParse = toParse.substring(
						parameterIndex + replacing.length(), toParse.length());
				parameterIndex = toParse.indexOf(replacing);
			} // while (parameterIndex != -1)
			parsed.append(toParse);
			return parsed.toString();
		} else {
			return messageFormat;
		}
	}
    
   /**
    * A methd useful to call messages directly into code.
    * 
    * @param portReq The portlet request object
    * @param code	The message code
    * @return	The message string matching to code
    */
    public String getMessage(PortletRequest portReq, String code) {
		return PortletUtilities.getMessage(code, DEFAULT_BUNDLE);
	}

    
	
}