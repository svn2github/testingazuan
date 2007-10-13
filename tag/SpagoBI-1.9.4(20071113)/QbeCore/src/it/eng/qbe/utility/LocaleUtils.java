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
			
		
			
			ZipEntry ze = jf.getEntry("label_"+loc.getLanguage()+".properties");
			
			Enumeration e = jf.entries();
			
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
