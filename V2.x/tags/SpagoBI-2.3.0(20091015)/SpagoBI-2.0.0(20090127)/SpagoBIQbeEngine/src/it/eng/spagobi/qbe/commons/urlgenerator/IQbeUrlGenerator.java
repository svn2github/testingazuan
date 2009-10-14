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
package it.eng.spagobi.qbe.commons.urlgenerator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface IQbeUrlGenerator.
 * 
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements logic
 * to generate URLS
 * 
 * This is because we want reuse JSP in Standalone Web applications and Portlet Enviroments
 */
public interface IQbeUrlGenerator {

	/**
	 * Gets the action url.
	 * 
	 * @param parameters an HashMap of parameters
	 * @param httpServletRequest the http servlet request
	 * 
	 * @return a URL given the Map parameters
	 */
	public String getActionUrl(HttpServletRequest httpServletRequest, Map parameters);
	
	/**
	 * Gets the resource url.
	 * 
	 * @param originalUrl a String representic a link to static resource img, css, js and so on
	 * @param httpServletRequest the http servlet request
	 * 
	 * @return the resource url
	 */
	public String getResourceUrl(HttpServletRequest httpServletRequest, String originalUrl);
}
