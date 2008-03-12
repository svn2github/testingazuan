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
 * Spago Action which executes the map producing request  
 */
public class SaveAnalysisAction extends AbstractGeoEngineAction {	
	
	// request parameters
	private static final String ANALYSYS_NAME = "name";
	private static final String ANALYSYS_DESCRIPTION = "description";
	private static final String ANALYSYS_SCOPE = "scope";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(SaveAnalysisAction.class);
    
    
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException  {
		
		super.service(serviceRequest, serviceResponse);
		
		String name = getAttributeAsString( ANALYSYS_NAME );
		String description  = getAttributeAsString( ANALYSYS_DESCRIPTION );
		String scope = getAttributeAsString( ANALYSYS_SCOPE );

		EngineAnalysisMetadata analysisMetadata = getAnalysisMetadata();
		GeoEngineAnalysisState analysisState = (GeoEngineAnalysisState)getAnalysisState();		
		
		analysisMetadata.setName(name);
		analysisMetadata.setDescription(description);
		analysisMetadata.setScope(scope);
		
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