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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.GeoEngine;
import it.eng.spagobi.engines.geo.GeoEngineInstance;
import it.eng.spagobi.engines.geo.commons.constants.GeoEngineConstants;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.presentation.DynamicPublisher;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineConstants;


/**
 * Geo entry point action.
 */
public class GeoEngineStartAction extends AbstractEngineStartAction {
	
	private MapCatalogueAccessUtils mapCatalogueServiceProxy;
	private String standardHierarchy;
	
	
	// request
	/** The Constant EXECUTION_CONTEXT. */
	public static final String EXECUTION_CONTEXT = "EXECUTION_CONTEXT";
	public static final String EXECUTION_ID = "EXECUTION_ID";
	public static final String DOCUMENT_LABEL = "DOCUMENT_LABEL";
	public static final String OUTPUT_TYPE = "outputType";
	
	//response 
	/** The Constant IS_DOC_COMPOSITION_MODE_ACTIVE. */
	public static final String IS_DOC_COMPOSITION_MODE_ACTIVE =  "isDocumentCompositionModeActive";
	
	// session
	/** The Constant GEO_ENGINE_INSTANCE. */
	public static final String GEO_ENGINE_INSTANCE = EngineConstants.ENGINE_INSTANCE;
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GeoEngineStartAction.class);
	

	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws GeoEngineException {
		
		GeoEngineInstance geoEngineInstance;
		Map env;
		byte[] analysisStateRowData;
		GeoEngineAnalysisState analysisState = null;
		String executionContext;
		String executionId;
		String documentLabel;
		String outputType;
		
		Monitor hitsPrimary = null;
        Monitor hitsByDate = null;
        Monitor hitsByUserId = null;
        Monitor hitsByDocumentId = null;
        Monitor hitsByExecutionContext = null;
		
		
		logger.debug("IN");		
		
		try {
			super.service(serviceRequest, serviceResponse);
						
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Template: " + getTemplate());	
			
			hitsPrimary = MonitorFactory.startPrimary("GeoEngine.requestHits");
	        hitsByDate = MonitorFactory.start("GeoEngine.requestHits." + DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));
	        hitsByUserId = MonitorFactory.start("GeoEngine.requestHits." + getUserId());
	        hitsByDocumentId = MonitorFactory.start("GeoEngine.requestHits." + getDocumentId());
			
			
			executionContext = getAttributeAsString( EXECUTION_CONTEXT ); 
			logger.debug("Parameter [" + EXECUTION_CONTEXT + "] is equal to [" + executionContext + "]");
			
			executionId = getAttributeAsString( EXECUTION_ID );
			logger.debug("Parameter [" + EXECUTION_ID + "] is equal to [" + executionId + "]");
			
			documentLabel = getAttributeAsString( DOCUMENT_LABEL );
			logger.debug("Parameter [" + DOCUMENT_LABEL + "] is equal to [" + documentLabel + "]");
			
			outputType = getAttributeAsString(OUTPUT_TYPE);
			logger.debug("Parameter [" + OUTPUT_TYPE + "] is equal to [" + outputType + "]");
			
			logger.debug("Execution context: " + executionContext);
			String isDocumentCompositionModeActive = (executionContext != null && executionContext.equalsIgnoreCase("DOCUMENT_COMPOSITION") )? "TRUE": "FALSE";
			logger.debug("Document composition mode active: " + isDocumentCompositionModeActive);
			
			hitsByExecutionContext = MonitorFactory.start("GeoEngine.requestHits." + (isDocumentCompositionModeActive.equalsIgnoreCase("TRUE")?"compositeDocument": "singleDocument"));
			
			
			env = getEnv("TRUE".equalsIgnoreCase(isDocumentCompositionModeActive), documentLabel, executionId);
			if( outputType != null ) {
				env.put(GeoEngineConstants.ENV_OUTPUT_TYPE, outputType);
			}			
			
			geoEngineInstance = GeoEngine.createInstance(getTemplate(), env);
			geoEngineInstance.setAnalysisMetadata( getAnalysisMetadata() );
			
			analysisStateRowData = getAnalysisStateRowData();
			if(analysisStateRowData != null) {
				logger.debug("AnalysisStateRowData: " + new String(analysisStateRowData));
				analysisState = new GeoEngineAnalysisState( );
				analysisState.load( analysisStateRowData );
				logger.debug("AnalysisState: " + analysisState.toString());
			} else {
				logger.debug("AnalysisStateRowData: NULL");
			}
			if(analysisState != null) {
				geoEngineInstance.setAnalysisState( analysisState );
			}
			
			if("TRUE".equalsIgnoreCase(isDocumentCompositionModeActive)){
				setAttribute(DynamicPublisher.PUBLISHER_NAME, "SIMPLE_UI_PUBLISHER");
			} else {
				setAttribute(DynamicPublisher.PUBLISHER_NAME, "AJAX_UI_PUBLISHER");
			}
			
			String id = getAttributeAsString("SBI_EXECUTION_ID");
			setAttributeInSession(GEO_ENGINE_INSTANCE, geoEngineInstance);					
		} catch (Exception e) {
			if(e instanceof GeoEngineException) throw (GeoEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			throw new GeoEngineException("Service error", description, hints, e);
		} finally {
			if(hitsByExecutionContext != null) hitsByExecutionContext.stop();
			if(hitsByDocumentId != null) hitsByDocumentId.stop();
			if(hitsByUserId != null) hitsByUserId.stop();
			if(hitsByDate != null) hitsByDate.stop();
			if(hitsPrimary != null) hitsPrimary.stop();
			
		}
		
		logger.debug("OUT");
	}
	
	private MapCatalogueAccessUtils getMapCatalogueProxy() {
		if(mapCatalogueServiceProxy == null) {
			mapCatalogueServiceProxy = new MapCatalogueAccessUtils( getHttpSession(), getUserIdentifier() );
		}
		
		return mapCatalogueServiceProxy;
	}
	
	private String getStandardHierarchy() {
		if(standardHierarchy == null) {
			try {
				standardHierarchy = getMapCatalogueProxy().getStandardHierarchy( );
				logger.debug("Standard hierarchy: " + standardHierarchy);
			} catch (Exception e) {
				logger.warn("Impossible to get standard Hierarchy configuration settings from map catalogue");
			}	
		}
		
		return standardHierarchy;
	}
	
	private String getContextUrl() {
		String contextUrl = null;
		
		contextUrl = getHttpRequest().getContextPath();	
		logger.debug("Context path: " + contextUrl);
		
		return contextUrl;
	}
	
	private String getAbsoluteContextUrl() {
		String contextUrl = null;
		
		contextUrl = getHttpRequest().getScheme() + "://" 
					+ getHttpRequest().getServerName() + ":" 
					+ getHttpRequest().getServerPort() + "/" 
					+ getContextUrl();
		logger.debug("Context path: " + contextUrl);
		
		return contextUrl;
	}
	
	public Map getEnv(boolean isDocumentCompositionModeActive, String documentLabel, String executionId) {
		Map env = null;
		
		env = super.getEnv();
		
		IDataSource dataSource = getDataSource();
		IDataSet dataset = getDataSet();
		if( dataset != null ) {
			dataset.setUserProfile( getUserProfile() );
			dataset.setParamsMap( env );
		}
		
		env.put(EngineConstants.ENV_DATASOURCE, dataSource);
		env.put(EngineConstants.ENV_DATASET, dataset);
		
		logger.debug("DataSource: " + dataSource.toString());
		
		env.put(GeoEngineConstants.ENV_CONTEXT_URL, getContextUrl());
		env.put(GeoEngineConstants.ENV_ABSOLUTE_CONTEXT_URL, getAbsoluteContextUrl());
		
		env.put(GeoEngineConstants.ENV_MAPCATALOGUE_SERVICE_PROXY, getMapCatalogueProxy());
		
		if(isDocumentCompositionModeActive) {
			env.put(GeoEngineConstants.ENV_IS_DAFAULT_DRILL_NAV, "FALSE");
			env.put(GeoEngineConstants.ENV_IS_WINDOWS_ACTIVE, "FALSE");
			env.put(GeoEngineConstants.ENV_EXEC_IFRAME_ID, "iframe_" + documentLabel);
		} else {
			env.put(GeoEngineConstants.ENV_IS_WINDOWS_ACTIVE, "TRUE");
			env.put(GeoEngineConstants.ENV_EXEC_IFRAME_ID, "iframeexec" + executionId);
		}
		
		if(getStandardHierarchy() != null) {
			env.put(GeoEngineConstants.ENV_STD_HIERARCHY, getStandardHierarchy());
		}		
		
		return env;
	}
}