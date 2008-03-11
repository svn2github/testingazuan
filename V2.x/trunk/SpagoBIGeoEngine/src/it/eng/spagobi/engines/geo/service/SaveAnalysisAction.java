/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.configuration.Constants;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.engines.geo.service.initializer.GeoEngineStartAction;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.GenericSavingException;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class SaveAnalysisAction extends AbstractGeoEngineAction {	
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(SaveAnalysisAction.class);
    
    
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException  {
		
		super.service(serviceRequest, serviceResponse);
		
		//GeoEngineStartAction.SubObjectDetails subObjectDetails = this.getSpagoBISubObjectDetails();
			
			
		String name = (String)serviceRequest.getAttribute("name");
		String description = (String)serviceRequest.getAttribute("description");
		String scope = (String)serviceRequest.getAttribute("scope");
		
		subObjectDetails.setName(name);
		subObjectDetails.setDescription(description);
		subObjectDetails.setScope(scope);
		
		ContentServiceProxy proxy = new ContentServiceProxy( getUserId(), getHttpSession() );
	    try {
	    	String data = new String(subObjectDetails.getData());
	    	String result = proxy.saveSubObject( getDocumentId(), subObjectDetails.getName(),subObjectDetails.getDescription(), 
	    			subObjectDetails.getScope(), 
	    			subObjectDetails.getData() == null?"": new String(subObjectDetails.getData()));			
	    	getHttpSession().setAttribute("saveSubObjectMessage", result);
	    } catch (Exception gse) {		
	    	logger.error("Error while saving analysis.", gse);
	    	getHttpSession().setAttribute("saveSubObjectMessage", "KO - " + gse.getMessage());
	    }  
		
	}
}