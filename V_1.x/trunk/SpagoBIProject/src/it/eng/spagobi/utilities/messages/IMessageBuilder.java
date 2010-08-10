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
package it.eng.spagobi.utilities.messages;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the interface for classes that implements logic
 * to retrieve localized messages to be used in JSP pages 
 */
public interface IMessageBuilder {

	/**
	 * @param aRequestContainer: Spago Request Container
	 * @param code: the message code
	 * @return: the message associated with code in the default bundle
	 */
	//public String getMessage(RequestContainer aRequestContainer, String code);
	
	/**
	 * @param aRequestContainer: Spago Request Container
	 * @param code: the message code
	 * @param bundle: the bundle to use
	 * @return  the message associated with code in the given bundle
	 */
	//public String getMessage(RequestContainer aRequestContainer, String code, String bundle);


	/**
	 * Returns the message associated with code in the default bundle
	 * @param code: the message code
	 * @return: the message associated with code in the default bundle
	 */
	public String getMessage(String code);
	
	/**
	 * If the specified locale is valid, returns the message associated with code in the default bundle with the specified locale; 
	 * otherwise returns the message associated with code in the default bundle with default locale
	 * @param code: the message code
	 * @param locale: the specified locale
	 * @return: if the specified locale is valid, returns the message associated with code in the default bundle with the specified locale; 
	 * otherwise returns the message associated with code in the default bundle with default locale
	 */
	public String getMessage(String code, Locale locale);
	
	/**
	 * Returns the message associated with code in the given bundle
	 * @param code: the message code
	 * @param bundle: the bundle to use
	 * @return  the message associated with code in the given bundle
	 */
	public String getMessage(String code, String bundle);
	public String getMessage(String code, String bundle, Locale locale);
	
	/**
	 * Returns the message associated with code in the default bundle
	 * @param code: the message code
	 * @param request: the http request
	 * @return: the message associated with code in the default bundle
	 */
	public String getMessage(String code, HttpServletRequest request);
	public String getMessage(String code, HttpServletRequest request, Locale locale);
	
	/**
	 * Returns the message associated with code in the given bundle
	 * @param code: the message code
	 * @param bundle: the bundle to use
	 * @param request: the http request
	 * @return  the message associated with code in the given bundle
	 */
	public String getMessage(String code, String bundle, HttpServletRequest request);
	public String getMessage(String code, String bundle, HttpServletRequest request, Locale locale);
	
	/**
	 * Get the localized text contained into the resource
	 * @param resourceName The complete name of the resource
	 * @return the localized text contained into the resource
	 */
	public String getMessageTextFromResource(String resourceName);
	public String getMessageTextFromResource(String resourceName, Locale locale);
	
}
