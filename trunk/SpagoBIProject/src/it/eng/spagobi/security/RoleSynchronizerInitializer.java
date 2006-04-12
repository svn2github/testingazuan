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
 * Created on 20-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.security;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.tracing.TracerSingleton;

/**
 * Spago framework initializer for portal role synchronization 
 */
public class RoleSynchronizerInitializer implements InitializerIFace {
	private SourceBean _config = null;
	
	/**
	 * Synchronizes portal roles with SpagoBI roles
	 * @param config The configuration Source Bean
	 */
	public void init(SourceBean config) {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        "RoleSynchronizerInitializer::init: start method", config);
        _config = config;
        RoleSynchronizer synch = new RoleSynchronizer();
        synch.synchronize();
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        "RoleSynchronizerInitializer::init: end method", config);
    } 
	
	
    /**
     * gets the config sourcebean for list module
     */
	public SourceBean getConfig() {
        return _config;
    } 
    
    
}
