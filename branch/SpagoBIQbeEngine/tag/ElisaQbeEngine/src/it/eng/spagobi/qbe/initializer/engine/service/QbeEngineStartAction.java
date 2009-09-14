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
package it.eng.spagobi.qbe.initializer.engine.service;

import java.util.Locale;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.QbeEngine;
import it.eng.spagobi.qbe.QbeEngineInstance;
import it.eng.spagobi.qbe.commons.service.QbeEngineAnalysisState;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineStartupException;


/**
 * The Class QbeEngineStartAction.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineStartAction extends AbstractEngineStartAction {	
	
	// INPUT PARAMETERS
	
	// OUTPUT PARAMETERS
	public static final String LANGUAGE = "LANGUAGE";
	public static final String COUNTRY = "COUNTRY";
	
	// SESSION PARAMETRES	
	public static final String ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
	
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineStartAction.class);
    
    public static final String ENGINE_NAME = "SpagoBIQbeEngine";
		
    public void service(SourceBean serviceRequest, SourceBean serviceResponse) {
    	QbeEngineInstance qbeEngineInstance = null;
    	Locale locale;
    	
    	logger.debug("IN");
       
    	try {
    		setEngineName(ENGINE_NAME);
			super.service(serviceRequest, serviceResponse);
			
			
			
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Template: " + getTemplate());
						
			getAuditServiceProxy().notifyServiceStartEvent();
					
			qbeEngineInstance = QbeEngine.createInstance( getTemplate(), getEnv() );
			qbeEngineInstance.setStandaloneMode( false );
			
			qbeEngineInstance.setAnalysisMetadata( getAnalysisMetadata() );
			if( getAnalysisStateRowData() != null ) {
				QbeEngineAnalysisState analysisState = new QbeEngineAnalysisState( qbeEngineInstance.getDatamartModel() );
				analysisState.load( getAnalysisStateRowData() );
				qbeEngineInstance.setAnalysisState( analysisState );
			}
			
			locale = (Locale)qbeEngineInstance.getEnv().get(EngineConstants.ENV_LOCALE);
			
			setAttributeInSession( ENGINE_INSTANCE, qbeEngineInstance);		
			setAttribute(ENGINE_INSTANCE, qbeEngineInstance);
			
			setAttribute(LANGUAGE, locale.getLanguage());
			setAttribute(COUNTRY, locale.getCountry());
			
			
		} catch (Throwable e) {
			SpagoBIEngineStartupException serviceException = null;
			String engineName = "SpagoBIQbeEngine";
			
			if(e instanceof SpagoBIEngineStartupException) {
				serviceException = (SpagoBIEngineStartupException)e;
			} else {
				Throwable rootException = e;
				while(rootException.getCause() != null) {
					rootException = rootException.getCause();
				}
				String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
				String message = "An unpredicted error occurred while executing " + engineName + " service."
								 + "\nThe root cause of the error is: " + str;
				
				serviceException = new SpagoBIEngineStartupException(engineName, message, e);
			}
			
			throw serviceException;
			
			//throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), qbeEngineInstance, e);
		} finally {
			logger.debug("OUT");
		}		

		
	}    
}
