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
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.presentation.listobjectshtmlgenerators.IListObjectsHtmlGenerator;
import it.eng.spagobi.presentation.listobjectshtmlgenerators.IListObjectsTransformer;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * The tag used to build a list of objects.
 * 
 * @author sulis
 */
public class ListObjectsTag extends TagSupport {

	private String moduleName = null;
	private String htmlGeneratorClass = null;
	private String listTransformerClass = null;
	private String listPage = null;
	private String actor = null;
	HttpServletRequest httpRequest = null;

	/**
	 * @see it.eng.spagobi.presentation.tags.ListTag#doStartTag()
	 * 
	 */
	public int doStartTag() throws JspException {

		httpRequest = (HttpServletRequest) pageContext.getRequest();
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		SourceBean serviceRequest = requestContainer.getServiceRequest();
		ResponseContainer responseContainer = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		SourceBean serviceResponse = responseContainer.getServiceResponse();
		SourceBean moduleResponse = (SourceBean)serviceResponse.getAttribute(moduleName);
        
		ListIFace list = null;
		
		if(listTransformerClass!=null) {
			SourceBean data = (SourceBean)moduleResponse.getAttribute("SystemFunctionalities");
            IListObjectsTransformer trans = null;
            try{
            	trans = (IListObjectsTransformer)Class.forName(listTransformerClass).newInstance();
            } catch(Exception e) {
            	return -1;
            }
            if(actor == null) {
            	actor = SpagoBIConstants.ADMIN_ACTOR;
            }
            list = trans.transform(data);
		} else {
			list = (ListIFace)moduleResponse.getAttribute("SystemFunctionalities");
		}
			
        IListObjectsHtmlGenerator gen = null;
        try{
        	gen = (IListObjectsHtmlGenerator)Class.forName(htmlGeneratorClass).newInstance();
        } catch(Exception e) {
        	return -1;
        }
        StringBuffer htmlStream = gen.makeList(list, httpRequest, listPage);
		try {
			pageContext.getOut().print(htmlStream);
		} catch(IOException ioe) {
			// TODO trace and throw exception
			SpagoBITracer.major("", "ListObjectsTag", "doStartTag", "cannot start object tag: IOexception occurred",ioe);
		}
		return SKIP_BODY;
	}
	

	/**
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.INFORMATION, "TitleTag::doEndTag:: invocato");
		return super.doEndTag();
	}

	/**
	 * 
	 * @return the Module Name to return
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName The Module Name to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * @return The html generator Class
	 */
	public String getHtmlGeneratorClass() {
		return htmlGeneratorClass;
	}
	/**
	 * @param htmlGeneratorClass the html generator Class to set
	 */
	public void setHtmlGeneratorClass(String htmlGeneratorClass) {
		this.htmlGeneratorClass = htmlGeneratorClass;
	}
	/**
	 * @return the list transformer class
	 */
	public String getListTransformerClass() {
		return listTransformerClass;
	}
	/**
	 * @param listTransformerClass the list transformer class to set
	 */
	public void setListTransformerClass(String listTransformerClass) {
		this.listTransformerClass = listTransformerClass;
	}
	/**
	 * @return the list page 
	 */
	public String getListPage() {
		return listPage;
	}
	/**
	 * @param listPage the list page to set
	 */
	public void setListPage(String listPage) {
		this.listPage = listPage;
	}
	/**
	 * @return the actor
	 */
	public String getActor() {
		return actor;
	}
	/**
	 * @param actor the actor to set
	 */
	public void setActor(String actor) {
		this.actor = actor;
	}
}