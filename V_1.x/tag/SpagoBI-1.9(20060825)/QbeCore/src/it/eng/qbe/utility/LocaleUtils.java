package it.eng.qbe.utility;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.RequestContainer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.portlet.PortletRequest;

/**
 * @author Andrea Zoppello
 * 
 * A Class that contains utilities for i18n
 *
 */
public class LocaleUtils {
	
	/**
	 * @param requestConatiner: Spago Request Container
	 * @param qbeMode: a parameter to determine if we're using QBE like a WebApp or like a Portlet
	 * @return a Locale Object
	 */
	public static Locale getLocale(RequestContainer requestConatiner,String qbeMode){
		
		Locale retLocale = null;
		
		if (qbeMode.equalsIgnoreCase("WEB")){
			
			String afLanguage = (String)requestConatiner.getSessionContainer().getPermanentContainer().getAttribute("AF_LANGUAGE");
			
			if (afLanguage == null){
				afLanguage = "en";
			}
			String afCountry = (String)requestConatiner.getSessionContainer().getPermanentContainer().getAttribute("AF_COUNTRY");
			
			
			retLocale = new Locale(afLanguage);
		
		}else if (qbeMode.equalsIgnoreCase("PORTLET")) {
		
			//PortletRequest portletRequest = (PortletRequest)requestConatiner.getAttribute("PORTLET_REQUEST");
			//Locale portalLocale = (Locale)portletRequest.getPortletSession().getAttribute("LOCALE");
			PortletRequest portletRequest = PortletAccess.getPortletRequest();
			Locale portalLocale = (Locale)PortletAccess.getPortalLocale();
			retLocale =  portalLocale;
		
		}
		
		return retLocale;
	
	}
	
	
	/**
	 * Return the label.properties file inf datamart JarFile as a Properties object
	 * @param jf: The JarFile of the datamart
	 * @return the Properties reperesenting label.properties file inf datamart JarFile jf
	 */
	public static Properties getLabelProperties(JarFile jf){
		Properties prop = new Properties();
		
		try{
			ZipEntry ze = jf.getEntry("label.properties");
			if (ze != null){
				prop = new Properties();
				prop.load(jf.getInputStream(ze));
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * Return the label_<LANG>.properties file inf datamart JarFile as a Properties object
	 * @param jf: The JarFile of the datamart
	 * @return the Properties reperesenting label.properties file inf datamart JarFile jf
	 * @param loc: A locale
	 * @return the Properties reperesenting label_<LANG>.properties file inf datamart JarFile jf
	 */
	public static Properties getLabelProperties(JarFile jf, Locale loc){
		Properties prop = new Properties();
		try{
			
		
			Logger.debug(LocaleUtils.class, "Seacrhing in Jar File for label file Properties [label_"+loc.getLanguage()+".properties");
			ZipEntry ze = jf.getEntry("label_"+loc.getLanguage()+".properties");
			
			Enumeration e = jf.entries();
			/*
			while (e.hasMoreElements()){
				ze = (ZipEntry)e.nextElement();
				Logger.debug(LocaleUtils.class,"-->" + ze.getName());
			}
			*/
			if (ze != null){
				prop = new Properties();
				prop.load(jf.getInputStream(ze));
			}
				
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return prop;
	}

}
