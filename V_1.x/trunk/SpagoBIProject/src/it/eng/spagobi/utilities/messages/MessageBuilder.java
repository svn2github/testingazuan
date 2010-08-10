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


import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.message.MessageBundle;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
/**
 * The implementation of IMessageBuilder   
 */
public class MessageBuilder implements IMessageBuilder {


	 /**
	 * Gets a localized information text given the resource name which contains the text 
	 * information. 
	 * @param resourceName	The complete name of the resource. 
	 * The resource will be searched into the classpath of the application
	 * @return	A string containing the text
	 */
	public String getMessageTextFromResource(String resourceName) {
		Locale locale = this.getLocale(null);
		return getMessageTextFromResource(resourceName, locale);
	}
	
	public String getMessageTextFromResource(String resourceName, Locale locale) {
		if (!isValidLocale(locale)) {
			SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getMessageTextFromResource", "Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		String message = "";
		try{
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			// get mode of execution
			String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
			String resourceNameLoc = resourceName + "_" + locale.getLanguage() + "_" + locale.getCountry();
		 	ClassLoader classLoad = this.getClass().getClassLoader();
		 	InputStream resIs = classLoad.getResourceAsStream(resourceNameLoc);
		 	if(resIs==null) {
		    	SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                                      "getMessageTextFromResource", "Cannot find resource " + resourceName);
		 		resIs = classLoad.getResourceAsStream(resourceName);
		 	}
		 	byte[] resBytes = GeneralUtilities.getByteArrayFromInputStream(resIs);
		 	message = new String(resBytes);
		} catch (Exception e) {
			message = "";
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
		                        "getMessageTextFromResource", "Error while recovering text of the resource name " + resourceName, e);
		}
	 	return message;
	}
	
	public String getMessage(String code) {
		Locale locale = getLocale(null);
		return getMessageInternal(code, "messages", locale);
	}
	public String getMessage(String code, Locale locale) {
		if (!isValidLocale(locale)) {
			SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getMessageTextFromResource", "Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, "messages", locale);
	}
	
	public String getMessage(String code, String bundle) {
		Locale locale = getLocale(null);
		return getMessageInternal(code, bundle, locale);
	}
	public String getMessage(String code, String bundle, Locale locale) {
		if (!isValidLocale(locale)) {
			SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getMessageTextFromResource", "Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, bundle, locale);
	}
	
	public String getMessage(String code, HttpServletRequest request) {
		Locale locale = getLocale(request);
		return getMessageInternal(code, "messages", locale);
	}
	public String getMessage(String code, HttpServletRequest request, Locale locale) {
		if (!isValidLocale(locale)) {
			SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getMessageTextFromResource", "Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, "messages", locale);
	}
	
	public String getMessage(String code, String bundle, HttpServletRequest request) {
		Locale locale = getLocale(request);
		return getMessageInternal(code, bundle, locale);
	}
	public String getMessage(String code, String bundle, HttpServletRequest request, Locale locale) {
		if (!isValidLocale(locale)) {
			SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getMessageTextFromResource", "Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, bundle, locale);
	}
	
	
	private String getMessageInternal(String code, String bundle, Locale locale) {
		String message = "";
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			message = MessageBundle.getMessage(code, bundle, locale);
			if((message==null) || message.trim().equals("")) {
				message = code;
			}
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			message = PortletUtilities.getMessage(code, bundle);
		}
		return message;
	}
	
	
	
	private Locale getBrowserLocaleFromSpago() {
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
		return browserLocale;
	}
	
	
	private Locale getBrowserLocale(HttpServletRequest request) {
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
		return browserLocale;
	}
	
	public Locale getLocale(HttpServletRequest request) {
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get locale
		Locale locale = null;
		if (sbiMode.equalsIgnoreCase("WEB")) {
			if (request == null) {
				locale = getBrowserLocaleFromSpago();
			} else {
				locale = getBrowserLocale(request);
			}
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			locale = PortletUtilities.getPortalLocale();
		}
		if (!isValidLocale(locale)) {
			SpagoBITracer.minor(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getLocale", "Request locale " + locale + " not valid since it is not configured.");
			locale = getDefaultLocale();
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    "getLocale", "Using default locale " + locale + ".");
		}
		return locale;
	}
	
	
	private Locale getDefaultLocale() {
	 	// get the configuration sourceBean/language code/country code of the default language
	 	SourceBean defaultLangSB = (SourceBean)ConfigSingleton.getInstance()
	 	                           .getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
	 	                           		                           "default", "true");
	 	String defaultLang = (String)defaultLangSB.getAttribute("language");
	 	String defaultCountry = (String)defaultLangSB.getAttribute("country");
	 	// create the locale
 		Locale locale = new Locale(defaultLang, defaultCountry);
	 	return locale;
	}
	
	private boolean isValidLocale(Locale locale) {
		if (locale == null) return false;
	 	// check if the Locale at input is configured
		String language = locale.getLanguage();
		Object o = ConfigSingleton.getInstance().getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
				"language", language);
		if (o != null) {
			if (o instanceof SourceBean) {
				SourceBean langSB = (SourceBean) o;
				String country = (String) langSB.getAttribute("country");
				if (locale.getCountry().equalsIgnoreCase(country)) return true;
				else return false;
			} else if (o instanceof List) {
				List list = (List) o;
				Iterator it = list.iterator();
				while (it.hasNext()) {
					SourceBean langSB = (SourceBean) it.next();
					String country = (String) langSB.getAttribute("country");
					if (locale.getCountry().equalsIgnoreCase(country)) return true;
				}
				return false;
			} else {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	                    "getLocale", "Invalid configuration.");
				return false;
			}
		} else return false;
	}
	
}
