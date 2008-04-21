/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.EngineException;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 * 
 */
public class SaveAnalysisAction extends AbstractGeoEngineAction {	
	
	/**
     * Request parameters
     */
	private static final String ANALYSYS_NAME = "name";
	private static final String ANALYSYS_DESCRIPTION = "description";
	private static final String ANALYSYS_SCOPE = "scope";
	
	/**
     * Default serial version number (just to keep eclipse happy)
     */
	private static final long serialVersionUID = 1L;
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(SaveAnalysisAction.class);
    
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException  {
		
		logger.debug("IN");
		
		String name = null;
		String description  = null;
		String scope = null;
		EngineAnalysisMetadata analysisMetadata = null;
		GeoEngineAnalysisState analysisState = null;
				
		super.service(serviceRequest, serviceResponse);
		
		name = getAttributeAsString( ANALYSYS_NAME );
		logger.debug("Analysy name: " + name);
		description  = getAttributeAsString( ANALYSYS_DESCRIPTION );
		logger.debug("Analysy description: " + description);
		scope = getAttributeAsString( ANALYSYS_SCOPE );
		logger.debug("Analysy scope: " + scope);

				
		analysisMetadata = getAnalysisMetadata();
		analysisState = (GeoEngineAnalysisState)getAnalysisState();		
		
		analysisMetadata.setName(name);
		analysisMetadata.setDescription(description);
		analysisMetadata.setScope(scope);
		analysisState.refreshRowData();
		
		ContentServiceProxy proxy = new ContentServiceProxy( getUserId(), getHttpSession() );
	    try {
	    	String data = new String(analysisState.getRowData());
	    	String result = proxy.saveSubObject( getDocumentId(), analysisMetadata.getName(),analysisMetadata.getDescription(), 
	    			analysisMetadata.getScope(), 
	    			analysisState.getRowData() == null?"": new String(analysisState.getRowData()));			
	    	getHttpSession().setAttribute("saveSubObjectMessage", result);
	    } catch (Exception gse) {		
	    	logger.error("Error while saving analysis.", gse);
	    	getHttpSession().setAttribute("saveSubObjectMessage", "KO - " + gse.getMessage());
	    }  
		
	}
}