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
package it.eng.spagobi.qbe.commons.presentation.tag;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.urlgenerator.IQbeUrlGenerator;
import it.eng.qbe.urlgenerator.PortletQbeUrlGenerator;
import it.eng.qbe.urlgenerator.WebQbeUrlGenerator;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerAccess;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SessionContainer;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Andrea Gioia
 *
 */
public class QbeTagSupport extends BodyTagSupport {
	
	public QbeTagSupport() {
		super();
	}
	
	
	
	protected HttpServletRequest getRequest() {
		if(pageContext == null) {
			return null;
		}
		return (HttpServletRequest) pageContext.getRequest();
	}
	
	protected RequestContainer getRequestContainer() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("RequestContainer") == null) {
			if ( QbeEngineConf.getInstance().isWebModalityActive() ) {
				pageContext.setAttribute("RequestContainer", RequestContainerAccess.getRequestContainer( getRequest() ) );
			} else {
				pageContext.setAttribute("RequestContainer", RequestContainerPortletAccess.getRequestContainer( getRequest() ) );
			}
		}		
		return (RequestContainer)pageContext.getAttribute("RequestContainer");
	}
	
	protected ResponseContainer getResponseContainer() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("ResponseContainer") == null) {
			if ( QbeEngineConf.getInstance().isWebModalityActive() ) {
				pageContext.setAttribute("ResponseContainer", ResponseContainerAccess.getResponseContainer( getRequest() ) );
			} else {
				pageContext.setAttribute("ResponseContainer", ResponseContainerPortletAccess.getResponseContainer( getRequest() ) );
			}
		}		
		return (ResponseContainer)pageContext.getAttribute("ResponseContainer");
	}
	
	protected SessionContainer getSessionContainer() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("SessionContainer") == null) {
			pageContext.setAttribute("SessionContainer", getRequestContainer().getSessionContainer() );
		}
		return (SessionContainer)pageContext.getAttribute("SessionContainer");
	}
	
	protected IQbeUrlGenerator getQbeUrlGenerator() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("UrlGenerator") == null) {
			IQbeUrlGenerator urlGenerator;
			if ( QbeEngineConf.getInstance().isWebModalityActive() ) {
				urlGenerator = new WebQbeUrlGenerator();
			} else {
				urlGenerator = new PortletQbeUrlGenerator();
			}
			pageContext.setAttribute("UrlGenerator", urlGenerator  );
		}
		return (IQbeUrlGenerator)pageContext.getAttribute("UrlGenerator");
	}
	
	protected String getResourceUrl(String url) {
		return getQbeUrlGenerator().conformStaticResourceLink(getRequest() , url);
	}
	
	protected String getActionUrl(Map parameters) {
		return getQbeUrlGenerator().getUrl(getRequest() , parameters);
	}
	
	protected Locale getLocale() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("Locale") == null) {
			pageContext.setAttribute("Locale", getSessionContainer().getAttribute("QBE_ENGINE_LOCALE") );
		}
		return (Locale)pageContext.getAttribute("Locale");
	}
	
	
	
	protected DataMartModel getDatamartModel() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("datamartModel") == null) {
			pageContext.setAttribute("datamartModel", getSessionContainer().getAttribute("dataMartModel") );
		}
		return (DataMartModel)pageContext.getAttribute("datamartModel");   
	}
	
	protected ISingleDataMartWizardObject getQuery() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("query") == null) {
			pageContext.setAttribute("query", Utils.getWizardObject( getSessionContainer() ) );
		}
		return (ISingleDataMartWizardObject)pageContext.getAttribute("query");
	}
	
	protected boolean isStandaloneModality() {
		if(pageContext == null) {
			return false;
		}
		
		if(pageContext.getAttribute("isStandaloneModality") == null) {
			boolean isStandaloneModality = (getSessionContainer().getAttribute("spagobi") == null); 
			pageContext.setAttribute("isStandaloneModality", new Boolean( isStandaloneModality ) );
		}
		return ((Boolean)pageContext.getAttribute("isStandaloneModality")).booleanValue();
	}
}

