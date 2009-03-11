/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.message.MessageBundle;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.PortletUtilities;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
/**
 * The implementation of IMessageBuilder   
 */
public class MessageBuilder implements IMessageBuilder {

	static private Logger logger = Logger.getLogger(MessageBuilder.class);

	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessageTextFromResource(java.lang.String, java.util.Locale)
	 */
	public String getMessageTextFromResource(String resourceName, Locale locale) {
		logger.debug("IN-resourceName:"+resourceName);
		logger.debug("IN-locale:"+ (locale != null ? locale.toString() : "null" ));
		if (!isValidLocale(locale)) {
			logger.warn("Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		String message = "";
		try{
			// get mode of execution
			String resourceNameLoc = resourceName + "_" + locale.getLanguage() + "_" + locale.getCountry();
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
			logger.error("Error while recovering text of the resource name " + resourceName, e);
		}
		logger.debug("OUT-message:"+message);
		return message;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String)
	 */
	public String getMessage(String code) {
		Locale locale = getLocale(null);
		return getMessageInternal(code, null, locale);
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.util.Locale)
	 */
	public String getMessage(String code, Locale locale) {
		if (!isValidLocale(locale)) {
			logger.warn("Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, null, locale);
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.lang.String)
	 */
	public String getMessage(String code, String bundle) {
		Locale locale = getLocale(null);
		return getMessageInternal(code, bundle, locale);
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.lang.String, java.util.Locale)
	 */
	public String getMessage(String code, String bundle, Locale locale) {
		if (!isValidLocale(locale)) {
			logger.warn("Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, bundle, locale);
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String getMessage(String code, HttpServletRequest request) {
		Locale locale = getLocale(request);
		return getMessageInternal(code, null, locale);
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, javax.servlet.http.HttpServletRequest, java.util.Locale)
	 */
	public String getMessage(String code, HttpServletRequest request, Locale locale) {
		if (!isValidLocale(locale)) {
			logger.warn("Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, null, locale);
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String getMessage(String code, String bundle, HttpServletRequest request) {
		Locale locale = getLocale(request);
		return getMessageInternal(code, bundle, locale);
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String getUserMessage(String code, String bundle, HttpServletRequest request) {
		Locale locale = getLocale(request);

		String toReturn=code;

		if(code.length()>4){
			String prefix=code.substring(0, 4);
			if(prefix.equalsIgnoreCase("cod_")){
				String newCode=code.substring(4);
				toReturn=getMessageInternal(newCode, bundle, locale);
			}
		}
		return toReturn;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.lang.String, Locale)
	 */
	public String getUserMessage(String code, String bundle, Locale locale) {

		String toReturn=code;

		if(code.length()>4){
			String prefix=code.substring(0, 4);
			if(prefix.equalsIgnoreCase("cod_")){
				String newCode=code.substring(4);
				toReturn=getMessageInternal(newCode, bundle, locale);
			}
		}
		return toReturn;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.utilities.messages.IMessageBuilder#getMessage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, java.util.Locale)
	 */
	public String getMessage(String code, String bundle, HttpServletRequest request, Locale locale) {
		if (!isValidLocale(locale)) {
			logger.warn("Request locale " + locale + " in input is not valid since it is null or not configured.");
			locale = getDefaultLocale();
		}
		return getMessageInternal(code, bundle, locale);
	}



	private String getMessageInternal(String code, String bundle, Locale locale) {
		logger.debug("IN-code:"+code);
		logger.debug("bundle:"+bundle);
		String message = null;
		if (bundle == null) {
			message = MessageBundle.getMessage(code, locale);
		} else {
			message = MessageBundle.getMessage(code, bundle, locale);
		}
		if (message == null || message.trim().equals("")) {
			message = code;
		}
//		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
//		// get mode of execution
//		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
//		// based on mode get spago object and url builder
//		if (sbiMode.equalsIgnoreCase("WEB")) {
//			if (bundle == null) {
//				message = MessageBundle.getMessage(code, locale);
//			} else {
//				message = MessageBundle.getMessage(code, bundle, locale);
//			}
//			if((message==null) || message.trim().equals("")) {
//				message = code;
//			}
//		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
//			message = PortletUtilities.getMessage(code, bundle);
//		}
		logger.debug("OUT-message:"+message);
		return message;
	}



	public static Locale getBrowserLocaleFromSpago() {
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




	public static Locale getDefaultLocale() {
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


	/**
	 * Gets the locale.
	 * 
	 * @param request the request
	 * 
	 * @return the locale
	 */
	public Locale getLocale(HttpServletRequest request) {
		logger.debug("IN");
		String sbiMode = getSpagoBIMode(request);
		// based on mode get locale
		Locale locale = null;
		if (sbiMode.equalsIgnoreCase("WEB")) {

			//if it is defined a special language in session use that one
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer sessCont = reqCont.getSessionContainer();
			SessionContainer permSess = sessCont.getPermanentContainer();
			String language=(String)permSess.getAttribute(SpagoBIConstants.AF_LANGUAGE);
			String country=(String)permSess.getAttribute(SpagoBIConstants.AF_COUNTRY);
			if(country==null)country="";

			if(language!=null){
				locale=new Locale(language,country,"");
			}
			else {
				if (request == null) {
					locale = getBrowserLocaleFromSpago();
				} else {
					locale = getBrowserLocale(request);
				}
			} 
		}
		else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			locale = PortletUtilities.getPortalLocale();
		}

		if (!isValidLocale(locale)) {
			logger.warn("Request locale " + locale + " not valid since it is not configured.");
			locale = getDefaultLocale();
			logger.debug("Using default locale " + locale + ".");
		}
		logger.debug("OUT-locale:" + (locale != null ? locale.toString() : "null"));
		return locale;
	}

	private boolean isValidLocale(Locale locale) {
		logger.debug("IN");
		try {
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
					logger.error("Invalid configuration.");
					return false;
				}
			} else return false;
		} finally {
			logger.debug("OUT");
		}
	}

	/**
	 * Returns 'WEB' in case the request is a http request or 'PORTLET' in case request is a portlet request.
	 * 
	 * @param request The HttpServletRequest to be examined
	 * 
	 * @return 'WEB' in case the request is a http request or 'PORTLET' in case request is a portlet request
	 */
	public String getSpagoBIMode(HttpServletRequest request) {
		logger.debug("IN");
		String sbiMode = null;
		if (request != null) {
			RequestContainer aRequestContainer = null;
			// case of portlet mode
			aRequestContainer = RequestContainerPortletAccess.getRequestContainer(request);
			if (aRequestContainer == null) {
				// case of web mode
				aRequestContainer = RequestContainerAccess.getRequestContainer(request);
			}
			String channelType = aRequestContainer.getChannelType();
			if ("PORTLET".equalsIgnoreCase(channelType)) sbiMode = "PORTLET";
			else sbiMode = "WEB";
		} else {
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			// get mode of execution
			sbiMode = (String) spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");  
		}
		logger.debug("OUT: sbiMode = " + sbiMode);
		return sbiMode;
	}

	/**
	 * Gets a localized information text given the resource name which contains the text
	 * information.
	 * The resource will be searched into the classpath of the application
	 * 
	 * @param resourceName The complete name of the resource.
	 * @param request The http request for locale retrieving
	 * 
	 * @return A string containing the text
	 */
	public String getMessageTextFromResource(String resourceName,
			HttpServletRequest request) {
		logger.debug("IN");
		Locale locale = this.getLocale(request);
		String message = getMessageTextFromResource(resourceName, locale);
		logger.debug("OUT");
		return message;
	}
}
