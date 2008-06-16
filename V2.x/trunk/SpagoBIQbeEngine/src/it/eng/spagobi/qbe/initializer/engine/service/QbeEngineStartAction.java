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
import it.eng.spagobi.utilities.engines.EngineConstants;

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


// TODO: Auto-generated Javadoc
/**
 * The Class QbeEngineStartAction.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineStartAction extends AbstractEngineStartAction {	
	
	// INPUT PARAMETERS
	
	
	// SESSION PARAMETRES	
	public static final String USER_ID = EngineConstants.USER_ID;
	public static final String DOCUMENT_ID = EngineConstants.DOCUMENT_ID;	
	public static final String AUDIT_ID = EngineConstants.AUDIT_ID;
	public static final String ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
	
	
	
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineStartAction.class);
	
	
    public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws QbeEngineException {
    	
    	logger.debug("IN");
       
    	try {
			super.service(serviceRequest, serviceResponse);
			
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Template: " + getTemplate());
			
			// AUDIT UPDATE
			updateAudit(getAuditId(), getUserId());	
			
						
			Map env = new HashMap();
			copyRequestParametersIntoEnv(env, serviceRequest);
			env.put(EngineConstants.ENV_DATASOURCE, getDataSource());
			env.put(EngineConstants.ENV_DOCUMENT_ID, getDocumentId());
			env.put(EngineConstants.ENV_CONTENT_SERVICE_PROXY, new ContentServiceProxy(getUserId(), getHttpSession()) );
			env.put(EngineConstants.ENV_LOCALE, getLocale()); 
			
			QbeEngineInstance qbeEngineInstance = QbeEngine.createInstance( getTemplate(), env );
			qbeEngineInstance.setStandaloneMode( false );
			
			qbeEngineInstance.setAnalysisMetadata( getAnalysisMetadata() );
			if( getAnalysisStateRowData() != null ) {
				QbeEngineAnalysisState analysisState = new QbeEngineAnalysisState( qbeEngineInstance.getDatamartModel() );
				analysisState.load( getAnalysisStateRowData() );
				qbeEngineInstance.setAnalysisState( analysisState );
			}
						
			
			setAttributeInSession( USER_ID, getUserId() );
			setAttributeInSession( AUDIT_ID, getAuditId() );
			setAttributeInSession( ENGINE_INSTANCE, qbeEngineInstance);	
			setAttribute("query", QueryEncoder.encode(qbeEngineInstance.getQuery(), qbeEngineInstance.getDatamartModel()));
			
		} catch (Exception e) {
			if(e instanceof QbeEngineException) throw (QbeEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			throw new QbeEngineException("Service error", description, hints, e);
		}
		
		

		logger.debug("OUT");
	}
	
    
    
    
    
    /**
	 * Copy request parameters into env.
	 * 
	 * @param env the env
	 * @param serviceRequest the service request
	 */
	public void copyRequestParametersIntoEnv(Map env, SourceBean serviceRequest) {
		Set parameterStopList = null;
		List requestParameters = null;
		
		logger.debug("IN");
		
		parameterStopList = new HashSet();
		parameterStopList.add("template");
		parameterStopList.add("ACTION_NAME");
		parameterStopList.add("NEW_SESSION");
		parameterStopList.add("document");
		parameterStopList.add("spagobicontext");
		parameterStopList.add("BACK_END_SPAGOBI_CONTEXT");
		parameterStopList.add("userId");
		parameterStopList.add("auditId");
		
		
		requestParameters = serviceRequest.getContainedAttributes();
		for(int i = 0; i < requestParameters.size(); i++) {
			SourceBeanAttribute attrSB = (SourceBeanAttribute)requestParameters.get(i);
			logger.debug("Parameter [" + attrSB.getKey() + "] has been read from request");
			logger.debug("Parameter [" + attrSB.getKey() + "] is of type  " + attrSB.getClass().getName());
			logger.debug("Parameter [" + attrSB.getKey() + "] is equal to " + attrSB.getValue().toString());
			
			if(parameterStopList.contains(attrSB.getKey())) {
				logger.debug("Parameter [" + attrSB.getKey() + "] copyed into environment parameters list: FALSE");
				continue;
			}
			
			env.put(attrSB.getKey(), decodeParameterValue(attrSB.getValue().toString()) );
			logger.debug("Parameter [" + attrSB.getKey() + "] copyed into environment parameters list: TRUE");
		}

		logger.debug("OUT");
	}
	
	/**
	 * Decode parameter value.
	 * 
	 * @param parValue the par value
	 * 
	 * @return the string
	 */
	private String decodeParameterValue(String parValue) {
		String newParValue;
			
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
		} else {
			newParValue = parValue;
		}
			
		return newParValue;
	}
    
    
    /**
     * Update audit.
     * 
     * @param auditId the audit id
     * @param userId the user id
     */
    private void updateAudit(String auditId, String userId) {
    	HttpSession session = getHttpRequest().getSession();
		AuditAccessUtils auditAccessUtils = (AuditAccessUtils) session.getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditId != null && auditAccessUtils != null) {
			auditAccessUtils.updateAudit(session, getUserId(), auditId, new Long(System
				    .currentTimeMillis()), null, "EXECUTION_STARTED", null, null);
		}
    }
    
}
