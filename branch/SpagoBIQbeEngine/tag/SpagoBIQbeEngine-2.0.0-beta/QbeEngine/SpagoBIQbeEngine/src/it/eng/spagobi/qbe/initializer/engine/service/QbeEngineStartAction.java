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

import it.eng.qbe.conf.QbeTemplate;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.qbe.QbeEngine;
import it.eng.spagobi.qbe.QbeEngineInstance;
import it.eng.spagobi.qbe.commons.constants.QbeConstants;
import it.eng.spagobi.qbe.commons.datasource.QbeDataSourceManager;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.QbeEngineAnalysisState;
import it.eng.spagobi.qbe.core.service.QueryEncoder;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.AuditServiceProxy;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


/**
 * The Class QbeEngineStartAction.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineStartAction extends AbstractEngineStartAction {	
	
	// INPUT PARAMETERS
	
	
	// SESSION PARAMETRES	
	public static final String ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
	
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineStartAction.class);
	
	
    public void service(SourceBean serviceRequest, SourceBean serviceResponse) {
    	QbeEngineInstance qbeEngineInstance = null;
    	
    	logger.debug("IN");
       
    	try {
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
				
			setAttributeInSession( ENGINE_INSTANCE, qbeEngineInstance);	
			setAttribute("query", QueryEncoder.encode(qbeEngineInstance.getQuery(), qbeEngineInstance.getDatamartModel()));
			
		} catch (Throwable e) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), qbeEngineInstance, e);
		} finally {
			// no resources need to be released
		}		

		logger.debug("OUT");
	}    
}
