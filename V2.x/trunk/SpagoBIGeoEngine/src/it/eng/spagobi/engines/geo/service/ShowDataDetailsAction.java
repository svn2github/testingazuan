/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ShowDataDetailsAction extends AbstractGeoEngineAction {
	
	/**
     * Request parameters
     */
	public static final String SELECTED_FEATURE_ID = "featureValue";
	
	/**
     * Session parameters
     */
	public static final String RESULT_SET = "RESULT_SET";
	public static final String SELECTED_FEATURE_DESC = "FEATURE_DESC";
	
	/**
     * Default serial version number (just to keep eclipse happy)
     */
	private static final long serialVersionUID = 1L;

	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(ShowDataDetailsAction.class);
	
    
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException {
		
		String featureValue = null;
		SourceBean resultSB = null;
		
		logger.debug("IN");
		
		super.service(serviceRequest, serviceResponse);
		
		featureValue = getAttributeAsString(SELECTED_FEATURE_ID);
		logger.debug("Selected feature: " + featureValue);
		resultSB = getGeoEngineInstance().getDatasetProvider().getDataDetails(featureValue);
		logger.debug("ResultSet: \n" + resultSB);
				
		setAttributeInSession(RESULT_SET, resultSB);
		setAttributeInSession(SELECTED_FEATURE_DESC, featureValue);
		
		logger.debug("OUT");
	}
}