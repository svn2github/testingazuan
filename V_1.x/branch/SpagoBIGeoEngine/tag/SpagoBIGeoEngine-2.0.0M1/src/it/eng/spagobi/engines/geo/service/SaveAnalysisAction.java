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
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveAnalysisAction.
 * 
 * @author Andrea Gioia
 */
public class SaveAnalysisAction extends AbstractGeoEngineAction {	
	
	/** Request parameters. */
	private static final String ANALYSYS_NAME = "name";
	
	/** The Constant ANALYSYS_DESCRIPTION. */
	private static final String ANALYSYS_DESCRIPTION = "description";
	
	/** The Constant ANALYSYS_SCOPE. */
	private static final String ANALYSYS_SCOPE = "scope";
	
	/** Default serial version number (just to keep eclipse happy). */
	private static final long serialVersionUID = 1L;
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(SaveAnalysisAction.class);
    
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) {
		
		logger.debug("IN");
		
		try {
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

					
			analysisMetadata = getGeoEngineInstance().getAnalysisMetadata();
			analysisState = (GeoEngineAnalysisState)getGeoEngineInstance().getAnalysisState();		
			
			analysisMetadata.setName(name);
			analysisMetadata.setDescription(description);
			analysisMetadata.setScope(scope);
			
		    try {
		    	String result = saveAnalysisState();
		    	getHttpSession().setAttribute("saveSubObjectMessage", result);
		    } catch (Exception gse) {		
		    	logger.error("Error while saving analysis.", gse);
		    	getHttpSession().setAttribute("saveSubObjectMessage", "KO - " + gse.getMessage());
		    }  
		}  catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			// no resources need to be released
		}	
		
		logger.debug("OUT");
		
	}
}