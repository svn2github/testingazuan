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

import java.util.Collection;
import java.util.Iterator;

import it.eng.spago.base.Constants;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.error.EMFAbstractError;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.util.JavaScript;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * It is used when any errors occurs during the execution of a tag. 
 * It handles errors throwing exceptions.
 * 
 * @author sulis
 */
public class ErrorTag extends TagSupport  {
	/**
	 * @see it.eng.spagobi.presentation.tags.ListTag#doStartTag()
	 * 
	 */
	public int doStartTag() throws JspException {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "ErrorTag::doStartTag:: invocato");
        if (pageContext == null) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                "ErrorTag::doStartTag:: pageContext nullo");
            throw new JspException("pageContext nullo");
        } // if (_httpRequest == null)
        HttpServletRequest httpRequest = null;
        try {
            httpRequest = (HttpServletRequest)pageContext.getRequest();
        } // try
        catch (Exception ex) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "ErrorTag::doStartTag::", ex);
            throw new JspException(ex.getMessage());
        } // catch (Exception ex)
        if (httpRequest == null) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                "ErrorTag::doStartTag:: httpRequest nullo");
            throw new JspException("httpRequest nullo");
        } // if (_httpRequest == null)
        ResponseContainer responseContainer = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
        if (responseContainer == null) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                "ErrorTag::doStartTag:: responseContainer nullo");
            throw new JspException("responseContainer nullo");
        } // if (responseContainer == null)
        StringBuffer output = new StringBuffer();
        EMFErrorHandler engErrorHandler = responseContainer.getErrorHandler();

    	Collection errors = engErrorHandler.getErrors();
    	
    	
    	if (!errors.isEmpty()) {
	    	output.append("<div class='div_detail_errors'>\n");
	    	output.append("	<div class='portlet-section-header'>\n");
	    	output.append("			ERRORS:\n");
	    	output.append("	</div>\n");
	    	output.append("	<div class='portlet-msg-error'>\n");
	    	output.append("		<ul class='ul_detail_error'>\n");
	    	EMFAbstractError error = null;
	    	String description = "";
	    	Iterator iter = errors.iterator();
	    	while(iter.hasNext()) {
	    		error = (EMFAbstractError)iter.next();
	    	 	description = error.getDescription();
	    	 	output.append("		<li>"+description+"</li>\n");
	    	}
	    	output.append("		</ul>\n");
	    	output.append("	</div>\n");
	    	output.append("</div>\n");
    	}
    	
        
        try {
            pageContext.getOut().print(output.toString());
        } // try
        catch (Exception ex) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "ErrorTag::doStartTag::", ex);
            throw new JspException(ex.getMessage());
        } // catch (Exception ex)
        return SKIP_BODY;
    } // public int doStartTag() throws JspException

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "ErrorTag::doEndTag:: invocato");
        return super.doEndTag();
    } // public int doEndTag() throws JspException
} // public class ErrorTag extends TagSupport
