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
package it.eng.qbe.locale;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.RequestContainer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.portlet.PortletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class LocaleUtils.
 * 
 * @author Andrea Zoppello
 * 
 * A Class that contains utilities for i18n
 */
public class LocaleUtils {
	
	/**
	 * Gets the locale.
	 * 
	 * @param requestConatiner the request conatiner
	 * @param qbeMode the qbe mode
	 * 
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
		
			PortletRequest portletRequest = PortletAccess.getPortletRequest();
			Locale portalLocale = (Locale)PortletAccess.getPortalLocale();
			retLocale =  portalLocale;
		
		}
		
		return retLocale;
	
	}
	
	
	/**
	 * Return the label.properties file inf datamart JarFile as a Properties object
	 * 
	 * @param jf the jf
	 * 
	 * @return the Properties reperesenting label.properties file inf datamart JarFile jf
	 * 
	 * @deprecated use DatamartLabelsDAOF
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
	 * 
	 * @param jf the jf
	 * @param loc the loc
	 * 
	 * @return the Properties reperesenting label.properties file inf datamart JarFile jf
	 * the Properties reperesenting label_<LANG>.properties file inf datamart JarFile jf
	 * 
	 * @deprecated use DatamartLabelsDAOF
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
