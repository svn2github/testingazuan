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
package it.eng.spagobi.commons.utilities.messages;


import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.message.MessageBundle;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.PortletUtilities;

import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
/**
 * The implementation of IMessageBuilder   
 */
public class MessageBuilder implements IMessageBuilder {

    static private Logger logger = Logger.getLogger(MessageBuilder.class);
	 /**
	 * Gets a localized information text given the resource name which contains the text 
	 * information. 
	 * @param resourceName	The complete name of the resource. 
	 * The resource will be searched into the classpath of the application
	 * @return	A string containing the text
	 */
	public String getMessageTextFromResource(String resourceName) {
	    logger.debug("IN-resourceName:"+resourceName);
		String message = "";
		try{
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			// get mode of execution
			String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
			// based on mode get locale
			Locale locale = null;
			if (sbiMode.equalsIgnoreCase("WEB")) {
				locale = getBrowserLocaleFromSpago();
			} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
				locale = PortletUtilities.getPortalLocale();
			}
			String resourceNameLoc = resourceName + "_" + locale.getLanguage() + "_" + locale.getCountry();
			logger.debug("resourceNameLoc:"+resourceNameLoc);
			ClassLoader classLoad = this.getClass().getClassLoader();
		 	InputStream resIs = classLoad.getResourceAsStream(resourceNameLoc);
		 	if(resIs==null) {
		 	   logger.warn("Cannot find resource " + resourceName);
     	 		   resIs = classLoad.getResourceAsStream(resourceName);
		 	}
		 	byte[] resBytes = GeneralUtilities.getByteArrayFromInputStream(resIs);
		 	message = new String(resBytes);
		} catch (Exception e) {
			message = "";
			logger.error("Error while recovering text of the resource name " + resourceName,e);
		}
		logger.debug("OUT-message:"+message);
	 	return message;
	}
	
	
	public String getMessage(String code) {
		return getMessageInternal(code, "messages", null);
	}
	
	
	public String getMessage(String code, String bundle) {
		return getMessageInternal(code, bundle, null);
	}
	
	public String getMessage(String code, HttpServletRequest request) {
		return getMessageInternal(code, "messages", request);
	}
	
	
	public String getMessage(String code, String bundle, HttpServletRequest request) {
		return getMessageInternal(code, bundle, request);
	}
	
	
	
	private String getMessageInternal(String code, String bundle, HttpServletRequest request) {
	        logger.debug("IN-code:"+code);
	        logger.debug("bundle:"+bundle);
		String message = "";
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			Locale locale = null;
			if(request==null) {
				locale = getBrowserLocaleFromSpago();
			} else {
				locale = getBrowserLocale(request);
			}
			message = MessageBundle.getMessage(code,bundle, locale);
			if((message==null) || message.trim().equals("")) {
				message = code;
			}
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			message = PortletUtilities.getMessage(code,bundle);
		}
		logger.debug("OUT-message:"+message);
		return message;
	}
	
	
	
	private Locale getBrowserLocaleFromSpago() {
	    logger.debug("IN");
		Locale browserLocale = null;
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		if(reqCont!=null) {
			Object obj = reqCont.getInternalRequest();
			if(obj != null) {
				if(obj instanceof HttpServletRequest) {
					HttpServletRequest request = (HttpServletRequest)obj;
					Locale reqLocale = request.getLocale();
					String language = reqLocale.getLanguage();
					SourceBean langSB = (SourceBean)ConfigSingleton.getInstance()
                     					.getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
                     		                           					"language", language);
					if(langSB!=null) {
						String country = (String)langSB.getAttribute("country");
						browserLocale = new Locale(language, country);
					}
				}
			}
		}
		if(browserLocale==null) {
			browserLocale = getDefaultLocale();
		}
		logger.debug("OUT");
		return browserLocale;
	}
	
	
	
	
	private Locale getBrowserLocale(HttpServletRequest request) {
	    logger.debug("IN");
		Locale browserLocale = null;
		Locale reqLocale = request.getLocale();
		String language = reqLocale.getLanguage();
		SourceBean langSB = (SourceBean)ConfigSingleton.getInstance()
                    					.getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
                     		                           					"language", language);
		if(langSB!=null) {
			String country = (String)langSB.getAttribute("country");
			browserLocale = new Locale(language, country);
		}
		if(browserLocale==null) {
			browserLocale = getDefaultLocale();
		}
		logger.debug("OUT");
		return browserLocale;
	}
	
	
	
	
	private Locale getDefaultLocale() {
	    logger.debug("IN");
	 	// get the configuration sourceBean/language code/country code of the default language
	 	SourceBean defaultLangSB = (SourceBean)ConfigSingleton.getInstance()
	 	                           .getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
	 	                           		                           "default", "true");
	 	String defaultLang = (String)defaultLangSB.getAttribute("language");
	 	String defaultCountry = (String)defaultLangSB.getAttribute("country");
	 	// create the locale
 		Locale locale = new Locale(defaultLang, defaultCountry);
 		logger.debug("OUT");
	 	return locale;
	}
	
}
