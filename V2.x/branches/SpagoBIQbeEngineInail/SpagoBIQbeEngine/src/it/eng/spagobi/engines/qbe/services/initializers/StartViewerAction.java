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
package it.eng.spagobi.engines.qbe.services.initializers;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.qbe.QbeEngine;
import it.eng.spagobi.engines.qbe.QbeEngineInstance;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineConstants;

import org.apache.log4j.Logger;


/**
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 * TODO substitute this action with FormEngineStartAction
 */
public class StartViewerAction extends AbstractEngineStartAction {	
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(StartViewerAction.class);
    public static final String ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
		
    public void service(SourceBean serviceRequest, SourceBean serviceResponse) {
    	logger.debug("IN");
    	try {
    		SourceBean template = SourceBean.fromXMLString("<QBE><DATAMART 	name=\"foodmart1998\"/></QBE>");
			super.service(serviceRequest, serviceResponse);
			QbeEngineInstance qbeEngineInstance = QbeEngine.createInstance( template, getEnv() );
			setAttributeInSession( ENGINE_INSTANCE, qbeEngineInstance);		
			setAttribute(ENGINE_INSTANCE, qbeEngineInstance);
			
			setAttribute(LANGUAGE, "it");
			setAttribute(COUNTRY, "IT");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	logger.debug("OUT");
	}

}
