/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service.initializer;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.constants.GeoEngineConstants;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Geo entry point action
 */
public class GeoEngineStartAction extends AbstractEngineStartAction {
	
	// request
	public static final String EXECUTION_CONTEXT = "EXECUTION_CONTEXT";
	
	//response 
	public static final String BASE_URL = "baseUrl";
	public static final String IS_DOC_COMPOSITION_MODE_ACTIVE =  "isDocumentCompositionModeActive";
	
	// session
	public static final String MAP_CONFIGURATION = GeoEngineConstants.MAP_CONFIGURATION;
	public static final String ANALYSIS_METADATA = GeoEngineConstants.ANALYSIS_METADATA;
	public static final String ANALYSIS_STATE = GeoEngineConstants.ANALYSIS_STATE;
	
	// ???
	public static final String SPAGOBI_TEMPLATE = "template";
	public static final String SPAGOBI_DATASOURCE = "SPAGOBI_DATASOURCE";
	
	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(GeoEngineStartAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException {
		logger.debug("Starting service method...");		
		super.service(serviceRequest, serviceResponse);
		
		String userId;
		String auditId;
		String documentId;
		SourceBean template;
		SpagoBiDataSource dataSource;
		EngineAnalysisMetadata analysisMetadata;
		byte[] analysisStateRowData;
		GeoEngineAnalysisState analysisState;
		MapConfiguration mapConfiguration;
		String baseUrl;
		String executionContext;
		
		userId = getUserId();
		documentId = getDocumentId();
		auditId = getAuditId();
		template = getTemplate();
		dataSource = getDataSource();
		analysisMetadata = getAnalysisMetadata();
		analysisStateRowData = getAnalysisStateRowData();
		
		executionContext = this.getAttributeAsString( EXECUTION_CONTEXT ); 
		String isDocumentCompositionModeActive = (executionContext != null && executionContext.equalsIgnoreCase("DOCUMENT_COMPOSITION") )? "TRUE": "FALSE";
		
		
		baseUrl = null;		
		mapConfiguration = null;
		

		MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils( getHttpSession(), getUserId() );
		MapConfiguration.setMapCatalogueAccessUtils( mapCatalogueAccessUtils );		
		
	
		try{
			
			
			String standardHierarchy = mapCatalogueAccessUtils.getStandardHierarchy( );
			mapConfiguration = new MapConfiguration(getTemplate().toString().getBytes(), serviceRequest, 
					standardHierarchy, getDataSource());
			
			baseUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + getHttpRequest().getContextPath();			
			mapConfiguration.getMapRendererConfiguration().setContextPath(baseUrl);
			mapConfiguration.getMapRendererConfiguration().setWindowsActive(isDocumentCompositionModeActive.equalsIgnoreCase("FALSE"));
		} catch (Exception e) {
			logger.error("Error while reading map configuration", e);
		}
				
		analysisState = new GeoEngineAnalysisState( analysisStateRowData );				
		mapConfiguration.setAnalysisState( analysisState );
		
		
		Properties props = new Properties();
		Enumeration enumer = getHttpRequest().getParameterNames();
		String parName = null;
		String parValue = null;
		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();
			parValue = getHttpRequest().getParameter(parName);
			if(parName.equalsIgnoreCase("NEW_SESSION")) continue;
			if(parName.equalsIgnoreCase("ACTION_NAME")) continue;
			parValue = decodeParameterValue(parValue);
			setAttribute(parName, parValue);
			props.setProperty(parName, parValue);			
		}		
		mapConfiguration.getDatamartProviderConfiguration().setParameters(props);	
		
		
		if(analysisState.getSelectedLayers() != null) {
			setAttribute("selectedLayers", analysisState.getSelectedLayers().split(","));
		}
		
		setAttribute(IS_DOC_COMPOSITION_MODE_ACTIVE, isDocumentCompositionModeActive);
		//setAttribute(IS_DOC_COMPOSITION_MODE_ACTIVE, "TRUE");
		
		
		setAttributeInSession(EngineConstants.USER_ID, userId );
		setAttributeInSession(EngineConstants.DOCUMENT_ID, documentId );
		setAttributeInSession(EngineConstants.AUDIT_ID, auditId );
		setAttributeInSession(ANALYSIS_METADATA, analysisMetadata );
		setAttributeInSession(ANALYSIS_STATE, analysisState );
		setAttributeInSession(MAP_CONFIGURATION, mapConfiguration);
		
		setAttributeInSession(SPAGOBI_TEMPLATE, template );
		setAttributeInSession(SPAGOBI_DATASOURCE, dataSource );		
		
		
		
		
		logger.debug("End service method");
	}
	
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