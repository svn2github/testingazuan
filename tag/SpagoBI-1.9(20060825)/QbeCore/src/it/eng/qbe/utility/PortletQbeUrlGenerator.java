package it.eng.qbe.utility;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;

import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Andrea Zoppello
 * 
 * The implementation of IQbeUrlGenerator used when QBE is used as a PORTLET
 */
public class PortletQbeUrlGenerator implements IQbeUrlGenerator{

	/**
	 * @see it.eng.qbe.utility.IQbeUrlGenerator#getUrl(javax.servlet.http.HttpServletRequest, java.util.Map)
	 */
	public String getUrl(HttpServletRequest aHttpServletRequest, Map parameters) {
		//Logger.debug(PortletQbeUrlGenerator.class, "getUrl::Start");
		RenderResponse renderResponse =(RenderResponse)aHttpServletRequest.getAttribute("javax.portlet.response");
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(aHttpServletRequest);
		
		//Logger.debug(PortletQbeUrlGenerator.class, "getUrl::Obatined Render Response and Request Container");
		
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
	
	/**
	 * @see it.eng.qbe.utility.IQbeUrlGenerator#conformStaticResourceLink(javax.servlet.http.HttpServletRequest, java.lang.String)
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
