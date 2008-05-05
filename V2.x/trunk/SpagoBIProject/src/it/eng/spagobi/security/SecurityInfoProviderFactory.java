/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

**/

package it.eng.spagobi.security;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

/**
 * Defines portal security provider information and  has a methot to get them.
 * 
 * @author sulis
 *
 */
public class SecurityInfoProviderFactory {
	
	
		
		private static ISecurityInfoProvider aPortalSecurityProvider = null;
		private static final String DEFAULT_SECURITY_PROVIDER_CLASS = "it.eng.spagobi.security.ExoPortalSecurityProviderImpl";
		
		/**
		 * Reads the security provider class from the spagobi.xml file; if no class is
		 * found, the default provider class is selected.
		 * 
		 * @return the singleton instance of ISecurityInfoProvider
		 */
		public static synchronized ISecurityInfoProvider getPortalSecurityProvider(){
			
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
					aPortalSecurityProvider = (ISecurityInfoProvider)Class.forName(securityProviderClass).newInstance();
				}catch (Exception e) {
					SpagoBITracer.critical("UTILITIES", "PortalSecurityProviderFactory", "getPortalSecurityProvider()","Error istantiatin security provider [" + securityProviderClass + "]", e); 
				}//catch
			}//if 
			return aPortalSecurityProvider;
		}
	}

