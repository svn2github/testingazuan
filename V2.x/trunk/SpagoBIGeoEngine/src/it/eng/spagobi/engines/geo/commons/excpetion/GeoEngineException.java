/**
 *
 *	LICENSE: see COPYING file
 *
**/

package it.eng.spagobi.engines.geo.commons.excpetion;

import it.eng.spagobi.utilities.engines.EngineException;

public class GeoEngineException extends EngineException {
    
	/**
     * Builds a <code>GeoEngineException</code> 
     * @param message Text of the exception
     */
    public GeoEngineException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>GeoEngineException</code> 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public GeoEngineException(String message, Throwable ex) {
    	super(message, ex);
    }

}

