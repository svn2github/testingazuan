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
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerAccess;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BaseTagSupport extends BodyTagSupport {
	
	public BaseTagSupport() {
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
		
		if(pageContext.getAttribute("requestContainer") == null) {
			if ( QbeEngineConf.getInstance().isWebModalityActive() ) {
				pageContext.setAttribute("requestContainer", RequestContainerAccess.getRequestContainer( getRequest() ) );
			} else {
				pageContext.setAttribute("requestContainer", RequestContainerPortletAccess.getRequestContainer( getRequest() ) );
			}
		}		
		return (RequestContainer)pageContext.getAttribute("requestContainer");
	}
	
	protected ResponseContainer getResponseContainer() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("responseContainer") == null) {
			if ( QbeEngineConf.getInstance().isWebModalityActive() ) {
				pageContext.setAttribute("responseContainer", ResponseContainerAccess.getResponseContainer( getRequest() ) );
			} else {
				pageContext.setAttribute("responseContainer", ResponseContainerPortletAccess.getResponseContainer( getRequest() ) );
			}
		}		
		return (ResponseContainer)pageContext.getAttribute("responseContainer");
	}
	
	protected SessionContainer getSessionContainer() {
		if(pageContext == null) {
			return null;
		}
		
		if(pageContext.getAttribute("sessionContainer") == null) {
			pageContext.setAttribute("sessionContainer", getRequestContainer().getSessionContainer() );
		}
		return (SessionContainer)pageContext.getAttribute("sessionContainer");
	}
}
