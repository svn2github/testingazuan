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

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.presentation.treehtmlgenerators.ITreeHtmlGenerator;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Creates the tag for tree objects
 * 
 * @author sulis
 */
public class TreeObjectsTag extends TagSupport {

	private String moduleName = null;
	private String htmlGeneratorClass = null;
	HttpServletRequest httpRequest = null;
	
	/**
	 * Starting tag
	 */
	public int doStartTag() throws JspException {
		httpRequest = (HttpServletRequest) pageContext.getRequest();
		RequestContainer requestContainer = ChannelUtilities.getRequestContainer(httpRequest);
		ResponseContainer responseContainer = ChannelUtilities.getResponseContainer(httpRequest);
		SourceBean serviceRequest = requestContainer.getServiceRequest();
		SourceBean serviceResponse = responseContainer.getServiceResponse();
		SourceBean moduleResponse = (SourceBean)serviceResponse.getAttribute(moduleName);
		List functionalitiesList = (List) moduleResponse.getAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST);
		String initialPath = (String) moduleResponse.getAttribute(TreeObjectsModule.PATH_SUBTREE);
        ITreeHtmlGenerator gen = null;
        try{
        	gen = (ITreeHtmlGenerator)Class.forName(htmlGeneratorClass).newInstance();
        } catch(Exception e) {
        	return -1;
        }
        StringBuffer htmlStream = gen.makeTree(functionalitiesList, httpRequest, initialPath);
		try {
			pageContext.getOut().print(htmlStream);
		} catch(IOException ioe) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "doStartTag", "cannot start object tree tag: IOexception occurred",ioe);
		}
		return SKIP_BODY;
	}
	

	/**
	 * ending tag
	 */
	public int doEndTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.INFORMATION, "TitleTag::doEndTag:: invocato");
		return super.doEndTag();
	}

	/**
	 * @return the module name
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName the module name to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * @return the html generator class
	 */
	public String getHtmlGeneratorClass() {
		return htmlGeneratorClass;
	}
	/**
	 * @param htmlGeneratorClass the html generator class to set
	 */
	public void setHtmlGeneratorClass(String htmlGeneratorClass) {
		this.htmlGeneratorClass = htmlGeneratorClass;
	}
		
	
}
