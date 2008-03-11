/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service.initializer;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.engines.geo.configuration.Constants;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.engines.geo.configuration.MapRendererConfiguration;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class GeoEngineStartAction extends AbstractEngineStartAction {
	
	//response 
	public static final String IS_SUBOBJECT = "isSubObject";
	public static final String BASE_URL = "baseUrl";
	
	// session
	public static final String MAP_CONFIGURATION = "CONFIGURATION";
	public static final String SPAGOBI_TEMPLATE = "template";
	public static final String SPAGOBI_DATASOURCE = "SPAGOBI_DATASOURCE";
	public static final String ANALYSIS_METADATA = "ANALYSIS_METADATA";
	
	
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
		
		userId = getUserId();
		documentId = getDocumentId();
		auditId = getAuditId();
		template = getTemplate();
		dataSource = getDataSource();
		analysisMetadata = getAnalysisMetadata();
		analysisStateRowData = getAnalysisStateRowData();
		
		baseUrl = null;		
		mapConfiguration = null;
		

		MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils( getHttpSession(), getUserId() );
		MapConfiguration.setMapCatalogueAccessUtils( mapCatalogueAccessUtils );		
		
	
		try{
			baseUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + getHttpRequest().getContextPath();			
			
			
			String standardHierarchy = mapCatalogueAccessUtils.getStandardHierarchy( );
			mapConfiguration = new MapConfiguration(baseUrl, getTemplate().toString().getBytes(), serviceRequest, 
					standardHierarchy, getDataSource());			
		} catch (Exception e) {
			logger.error("Error while reading map configuration", e);
		}
				
		analysisState = new GeoEngineAnalysisState( analysisStateRowData );				
		if( analysisState != null ) {			
			mapConfiguration.setAnalysisState( analysisState );
		}
		
		
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
		
		
		if(analysisState != null) setAttribute("selectedLayers", analysisState.getSelectedLayers().split(","));
		setAttribute(BASE_URL, baseUrl);
		
		setAttributeInSession(USER_ID, userId );
		setAttributeInSession(DOCUMENT_ID, documentId );
		setAttributeInSession(AUDIT_ID, auditId );
		setAttributeInSession(ANALYSIS_METADATA, analysisMetadata );
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