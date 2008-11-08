/**
 *
 *	LICENSE: see COPYING file
 *
**/

package it.eng.spagobi.geo.configuration;

public class ConfigurationException extends Exception {
    
	/**
     * Builds a <code>GeoConfigurationException</code> 
     * @param message Text of the exception
     */
    public ConfigurationException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>GeoConfigurationException</code> 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public ConfigurationException(String message, Throwable ex) {
    	super(message, ex);
    }

}

