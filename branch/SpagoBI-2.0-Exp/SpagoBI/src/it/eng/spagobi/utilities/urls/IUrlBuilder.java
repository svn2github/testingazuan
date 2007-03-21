package it.eng.spagobi.utilities.urls;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the interface for classes that implements logic
 * to generate URLS 
 * This is because we want reuse JSP in Standalone Web applications and Portlet Enviroments
 */
public interface IUrlBuilder {

	/**
	 * @param aHttpServletRequest the http servlet request
	 * @param parameters an HashMap of parameters
	 * @return a URL given the Map parameters
	 */
	public String getUrl(HttpServletRequest aHttpServletRequest, Map parameters);
	
	/**
	 * @param aHttpServletRequest the http servlet request
	 * @param originalUrl a String representic a link to static resource img, css, js and so on
	 * @return
	 */
	public String getResourceLink(HttpServletRequest aHttpServletRequest, String originalUrl);
}
