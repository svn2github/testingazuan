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
import java.util.Locale;
/**
 * The implementation of IMessageBuilder   
 */
public class MessageBuilder implements IMessageBuilder {

	public String getMessage(RequestContainer aRequestContainer, String code){
		return getMessage(code, "messages");
	}
	
	public String getMessage(RequestContainer aRequestContainer, String code, String bundle){
		return getMessage(code, bundle);
	}

	 /**
	 * Gets a localized information text given the resource name which contains the text 
	 * information. 
	 * @param resourceName	The complete name of the resource. 
	 * The resource will be searched into the classpath of the application
	 * @return	A string containing the text
	 */
	public String getMessageTextFromResource(String resourceName) {
		String message = "";
		try{
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			// get mode of execution
			String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
			// based on mode get locale
			Locale locale = null;
			if (sbiMode.equalsIgnoreCase("WEB")) {
				locale = getDefaultLocale();
			} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
				locale = PortletUtilities.getPortalLocale();
			}
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
	
	
	
	
	
	private String getMessage(String code, String bundle) {
		String message = "";
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			Locale locale = getDefaultLocale();
			message = MessageBundle.getMessage(code, bundle, locale);
			if((message==null) || message.trim().equals("")) {
				message = code;
			}
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			message = PortletUtilities.getMessage(code, bundle);
		}
		return message;
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
	
}
