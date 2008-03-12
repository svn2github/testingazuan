/**
 *
 *	LICENSE: see COPYING file
 *
**/

package it.eng.spagobi.utilities.engines;

public class EngineException extends Exception {
    
	/**
     * Builds a <code>EngineException</code> 
     * @param message Text of the exception
     */
    public EngineException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>EngineException</code> 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public EngineException(String message, Throwable ex) {
    	super(message, ex);
    }

}

