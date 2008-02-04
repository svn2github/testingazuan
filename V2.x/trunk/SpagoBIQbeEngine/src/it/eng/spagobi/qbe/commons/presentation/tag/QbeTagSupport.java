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
import it.eng.qbe.query.IQuery;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerAccess;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spagobi.qbe.commons.constants.QbeConstants;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.PortletQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.WebQbeUrlGenerator;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTagSupport extends BaseTagSupport {
	
	protected Locale getLocale() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("locale") == null) {
			pageContext.setAttribute("locale", getSessionContainer().getAttribute(QbeConstants.LOCALE) );
		}
		return (Locale)pageContext.getAttribute("locale");
	}
	
	
	
	protected DataMartModel getDatamartModel() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("datamartModel") == null) {
			pageContext.setAttribute("datamartModel", getSessionContainer().getAttribute(QbeConstants.DATAMART_MODEL) );
		}
		return (DataMartModel)pageContext.getAttribute("datamartModel");   
	}
	
	protected ISingleDataMartWizardObject getDatamartWizard() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("datamartWizard") == null) {
			pageContext.setAttribute("datamartWizard", getSessionContainer().getAttribute(QbeConstants.DATAMART_WIZARD) );
		}
		return (ISingleDataMartWizardObject)pageContext.getAttribute("datamartWizard");
	}
	
	protected IQuery getQuery() {
		if(pageContext == null) {
			return null;
		}
		
		ISingleDataMartWizardObject datamartWizard = null;
		
		if(pageContext.getAttribute("query") == null) {
			datamartWizard =  (ISingleDataMartWizardObject)getSessionContainer().getAttribute(QbeConstants.DATAMART_WIZARD);
			if(datamartWizard !=  null) {
				pageContext.setAttribute("query", datamartWizard.getQuery() );
			}
		}
		return (IQuery)pageContext.getAttribute("query");
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
	
	protected boolean isWebModality() {
		if(pageContext == null) {
			return false;
		}
		
		if(pageContext.getAttribute("isWebModality") == null) {
			pageContext.setAttribute("isWebModality", QbeEngineConf.getInstance().isWebModalityActive() );
		}
		return ((Boolean)pageContext.getAttribute("isWebModality")).booleanValue();
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
		return getQbeUrlGenerator().getResourceUrl(getRequest() , url);
	}
	
	protected String getActionUrl(Map parameters) {
		return getQbeUrlGenerator().getActionUrl(getRequest() , parameters);
	}
}

