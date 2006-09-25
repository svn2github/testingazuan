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
package it.eng.spagobi.security.init;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.security.RoleSynchronizer;

public class SecurityInitializer implements InitializerIFace {

	private SourceBean _config = null;
	
	public SourceBean getConfig() {
		return _config;
	}

	public void init(SourceBean config) {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
		        "SecurityInitializer::init: start method", config);
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
    			"SecurityInitializer::init: starting synchronizing roles...");
		RoleSynchronizer synch = new RoleSynchronizer();
		synch.synchronize();
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
				"SecurityInitializer::init: roles synchronization ended.");
		_config = config;
		SourceBean configSingleton = ConfigSingleton.getInstance();
		String portalSecurityInitClassName = ((SourceBean) configSingleton.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-INIT-CLASS")).getCharacters();
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        	"SecurityInitializer::init: Portal security initialization class name: '" + portalSecurityInitClassName + "'");
		if (portalSecurityInitClassName == null || portalSecurityInitClassName.trim().equals("")) return;
		portalSecurityInitClassName = portalSecurityInitClassName.trim();
		InitializerIFace portalSecurityInit = null;
		try {
			portalSecurityInit = (InitializerIFace)Class.forName(portalSecurityInitClassName).newInstance();
		} catch (Exception e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR, 
	        	"SecurityInitializer::init: error while instantiating portal security initialization class name: '" + portalSecurityInitClassName + "'", e);
			return;
		}
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
	        	"SecurityInitializer::init: invoking init method of the portal security initialization class name: '" + portalSecurityInitClassName + "'");
		portalSecurityInit.init(config);
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        		"SecurityInitializer::init: end method");
	}

}
