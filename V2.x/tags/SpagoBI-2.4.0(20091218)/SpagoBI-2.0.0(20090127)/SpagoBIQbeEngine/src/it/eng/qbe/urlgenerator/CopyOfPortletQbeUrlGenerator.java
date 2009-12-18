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
package it.eng.qbe.urlgenerator;

import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;


// TODO: Auto-generated Javadoc
/**
 * The Class CopyOfPortletQbeUrlGenerator.
 * 
 * @author Andrea Zoppello
 * 
 * The implementation of IQbeUrlGenerator used when QBE is used as a PORTLET
 */
public class CopyOfPortletQbeUrlGenerator implements CopyOfIQbeUrlGenerator{

	
	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.CopyOfIQbeUrlGenerator#getUrl(javax.servlet.http.HttpServletRequest, java.util.Map)
	 */
	public String getUrl(HttpServletRequest aHttpServletRequest, Map parameters) {
		RenderResponse renderResponse =(RenderResponse)aHttpServletRequest.getAttribute("javax.portlet.response");
		
		PortletURL aPortletURL = renderResponse.createActionURL();
		
		if (parameters != null){
			Iterator keysIt = parameters.keySet().iterator();
			
			boolean isFirst = true;
			String paramName = null;
			Object paramValue = null;
			while (keysIt.hasNext()){
				
				paramName = (String)keysIt.next();
				paramValue = parameters.get(paramName); 
				aPortletURL.setParameter(paramName, paramValue.toString());
			}
		}
		
		return aPortletURL.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.urlgenerator.CopyOfIQbeUrlGenerator#conformStaticResourceLink(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public String conformStaticResourceLink(HttpServletRequest aHttpServletRequest, String originalUrl){
		RenderRequest renderRequest =(RenderRequest)aHttpServletRequest.getAttribute("javax.portlet.request");
		RenderResponse renderResponse =(RenderResponse)aHttpServletRequest.getAttribute("javax.portlet.response");
		
		String urlToConvert = null; 
		int idx = originalUrl.indexOf('/'); 
		if ( idx > - 1){
			urlToConvert = originalUrl.substring(idx);
			//Logger.debug(PortletQbeUrlGenerator.class,"URL TO CONVERT "+ urlToConvert);
		}
		String newUrl = renderResponse.encodeURL(renderRequest.getContextPath() + urlToConvert).toString();
		//Logger.debug(PortletQbeUrlGenerator.class,"New URL " + newUrl);
		return newUrl;
	}

}
