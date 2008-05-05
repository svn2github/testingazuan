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
package it.eng.spagobi.engines.geo.service.initializer;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.engines.geo.GeoEngine;
import it.eng.spagobi.engines.geo.GeoEngineInstance;
import it.eng.spagobi.engines.geo.commons.constants.GeoEngineConstants;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.engines.geo.datasource.DataSource;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.EngineConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * Geo entry point action.
 */
public class GeoEngineStartAction extends AbstractEngineStartAction {
	
	// request
	/** The Constant EXECUTION_CONTEXT. */
	public static final String EXECUTION_CONTEXT = "EXECUTION_CONTEXT";
	
	//response 
	/** The Constant IS_DOC_COMPOSITION_MODE_ACTIVE. */
	public static final String IS_DOC_COMPOSITION_MODE_ACTIVE =  "isDocumentCompositionModeActive";
	
	// session
	/** The Constant GEO_ENGINE_INSTANCE. */
	public static final String GEO_ENGINE_INSTANCE = GeoEngineConstants.GEO_ENGINE_INSTANCE;
	
	/** The Constant ANALYSIS_METADATA. */
	public static final String ANALYSIS_METADATA = GeoEngineConstants.ANALYSIS_METADATA;
	
	/** The Constant ANALYSIS_STATE. */
	public static final String ANALYSIS_STATE = GeoEngineConstants.ANALYSIS_STATE;

	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GeoEngineStartAction.class);
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineStartAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws GeoEngineException {
		
		logger.debug("IN");		
		
		try {
			super.service(serviceRequest, serviceResponse);
				
			GeoEngineInstance geoEngineInstance;
			DataSource dataSource;
			MapCatalogueAccessUtils mapCatalogueServiceProxy;
			String standardHierarchy;
			EngineAnalysisMetadata analysisMetadata;
			byte[] analysisStateRowData;
			GeoEngineAnalysisState analysisState = null;
			String contextUrl;
			String executionContext;
			
			
			if(false) {
				throw new RuntimeException("Eccezione di prova");
			}
			
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Template: " + getTemplate());
			
			dataSource = new DataSource( getDataSource() );
			logger.debug("DataSource: " + dataSource.toString());
			
			mapCatalogueServiceProxy = new MapCatalogueAccessUtils( getHttpSession(), getUserId() );
			logger.debug("MapCatalogueServiceProxy created successfully");
			standardHierarchy = null;
			try {
				standardHierarchy = mapCatalogueServiceProxy.getStandardHierarchy( );
				logger.debug("Standard hierarchy: " + standardHierarchy);
			} catch (Exception e) {
				logger.warn("Impossible to get standard Hierarchy configuration settings from map catalogue");
			}
			
			
			analysisMetadata = getAnalysisMetadata();
			logger.debug("AnalysisMetadata: " + analysisMetadata.toString());
			analysisStateRowData = getAnalysisStateRowData();
			if(analysisStateRowData != null) {
				logger.debug("AnalysisStateRowData: " + new String(analysisStateRowData));
				analysisState = new GeoEngineAnalysisState( analysisStateRowData );		
				logger.debug("AnalysisState: " + analysisState.toString());
			} else {
				logger.debug("AnalysisStateRowData: NULL");
			}
			
			
			executionContext = getAttributeAsString( EXECUTION_CONTEXT ); 
			//executionContext = "DOCUMENT_COMPOSITION";
			logger.debug("Execution context: " + executionContext);
			String isDocumentCompositionModeActive = (executionContext != null && executionContext.equalsIgnoreCase("DOCUMENT_COMPOSITION") )? "TRUE": "FALSE";
			logger.debug("Document composition mode active: " + isDocumentCompositionModeActive);
			
			
			contextUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + getHttpRequest().getContextPath();	
			logger.debug("Context path: " + contextUrl);
	
			Map env = new HashMap();
			copyRequestParametersIntoEnv(env, serviceRequest);
			env.put(Constants.ENV_CONTEXT_URL, contextUrl);
			env.put(Constants.ENV_DATASOURCE, dataSource);
			env.put(Constants.ENV_MAPCATALOGUE_SERVICE_PROXY, mapCatalogueServiceProxy);
			if("TRUE".equalsIgnoreCase(isDocumentCompositionModeActive)) {
				env.put(Constants.ENV_IS_DAFAULT_DRILL_NAV, "FALSE");
				env.put(Constants.ENV_IS_WINDOWS_ACTIVE, "FALSE");
			} else {
				env.put(Constants.ENV_IS_DAFAULT_DRILL_NAV, "TRUE");
				env.put(Constants.ENV_IS_WINDOWS_ACTIVE, "TRUE");
			}
			
			if(standardHierarchy != null) {
				env.put(Constants.ENV_STD_HIERARCHY, standardHierarchy);
			}				
			
			geoEngineInstance = GeoEngine.createInstance(getTemplate(), env);
			if(analysisState != null) {
				geoEngineInstance.setAnalysisState( analysisState );
			}
			
			setAttribute(IS_DOC_COMPOSITION_MODE_ACTIVE, isDocumentCompositionModeActive);
					
			setAttributeInSession(EngineConstants.USER_ID, getUserId() );
			setAttributeInSession(EngineConstants.DOCUMENT_ID, getDocumentId() );
			setAttributeInSession(EngineConstants.AUDIT_ID, getAuditId() );
			setAttributeInSession(ANALYSIS_METADATA, analysisMetadata );
			setAttributeInSession(ANALYSIS_STATE, geoEngineInstance.getAnalysisState() );
			setAttributeInSession(GEO_ENGINE_INSTANCE, geoEngineInstance);		
		
		} catch (Exception e) {
			//EMFInternalError error = new EMFInternalError(EMFErrorSeverity.ERROR, "Errore orrore", e, "error object description");
			//getErrorHandler().addError( error );
			if(e instanceof GeoEngineException) throw (GeoEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			throw new GeoEngineException("Service error", description, hints, e);
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
}