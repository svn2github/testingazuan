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
package it.eng.spagobi.security.init;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

public class UPRSSecurityProviderInit implements InitializerIFace {
	private SourceBean _config = null;
	private String configFileName = "it/eng/spagobi/security/conf/uprssecurity.cfg.xml";
	
	public SourceBean getConfig() {
		return _config;
	}
 
	public void init(SourceBean config) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
		        "start method with configuration:\n" + config);
		_config = config;
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
                "Security configuration file: " + configFileName);
		SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
		SourceBean uprsPortalSecuritySB = (SourceBean) configSingleton.getAttribute("EXO_PORTAL_SECURITY");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
                "UPRS_PORTAL_SECURITY attribute in ConfigSingleton:\n" + uprsPortalSecuritySB);
		if (uprsPortalSecuritySB == null) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
	                "UPRS_PORTAL_SECURITY attribute is not present in ConfigSingleton: putting it into ConfigSingleton");
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
			try {
				if (is == null) {
					SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
			                "Security configuration file '" + configFileName + "' not found!!!");
				} else {
					InputSource stream = new InputSource(is);
					SourceBean securityConfig;
					try {
						securityConfig = SourceBean.fromXMLStream(stream);
						configSingleton.setAttribute(securityConfig);
					} catch (SourceBeanException e) {
						SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
					            "Error while loading security configuration file to ConfigSingleton", e);
					}
				}
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {
						SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
					            "Error while closing input stream", e);
					}
			}
		} else {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
            "UPRS_PORTAL_SECURITY attribute is already present in ConfigSingleton:\n" + uprsPortalSecuritySB.toXML());
		}
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),"init()", 
		        "end method.");
	}


}
