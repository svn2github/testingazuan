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
/*
 * Created on 13-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.security;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * Defines portal security provider information and  has a methot to get them.
 * 
 * @author sulis
 *
 */
public class PortalSecurityProviderFactory {
	
	
		
		private static IPortalSecurityProvider aPortalSecurityProvider = null;
		private static final String DEFAULT_SECURITY_PROVIDER_CLASS = "it.eng.spagobi.security.ExoPortalSecurityProviderImpl";
		
		/**
		 * Reads the security provider class from the spagobi.xml file; if no class is
		 * found, the default provider class is selected.
		 * 
		 * @return the singleton instance of IPortalSecurityProvider
		 */
		public static synchronized IPortalSecurityProvider getPortalSecurityProvider(){
			
			// Read the securityProviderClass from configuration file spagobi.xml
			String securityProviderClass = (String)ConfigSingleton.getInstance().getAttribute("SPAGOBI.PORTAL_SECURITY_PROVIDER_CLASS");
			if(securityProviderClass!=null){
				securityProviderClass = securityProviderClass.trim();
			}
			
			//If we don't found class secuirtyProviderClass we set the default (EXO)
			if (securityProviderClass == null){
				SpagoBITracer.info("UTILITIES", "PortalSecurityProviderFactory", "getPortalSecurityProvider()", "SPAGOBI.PORTAL_SECURITY_PROVIDER_CLASS not found USE THE DEAULT " + DEFAULT_SECURITY_PROVIDER_CLASS);
				securityProviderClass = DEFAULT_SECURITY_PROVIDER_CLASS;
			}
			
			SpagoBITracer.info("UTILITIES", "PortalSecurityProviderFactory", "getPortalSecurityProvider()","securityProviderClass  is " + securityProviderClass);
			
			if (aPortalSecurityProvider == null){
				try{
					aPortalSecurityProvider = (IPortalSecurityProvider)Class.forName(securityProviderClass).newInstance();
				}catch (Exception e) {
					SpagoBITracer.critical("UTILITIES", "PortalSecurityProviderFactory", "getPortalSecurityProvider()","Error istantiatin security provider [" + securityProviderClass + "]", e); 
				}//catch
			}//if 
			return aPortalSecurityProvider;
		}
	}

