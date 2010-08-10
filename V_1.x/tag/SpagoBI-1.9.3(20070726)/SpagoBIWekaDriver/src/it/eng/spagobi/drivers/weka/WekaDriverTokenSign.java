/**
 *
 *	LICENSE: see 'LICENSE-sbi.drivers.weka.txt' file
 *
 */
package it.eng.spagobi.drivers.weka;

import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.SecurityUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;



/**
 * Driver Implementation (IEngineDriver Interface) for Jasper Report Engine. 
 */
public class WekaDriverTokenSign extends WekaDriver implements IEngineDriver {
	
	/**
	 * Applys changes for security reason if necessary
	 * @param pars The map of parameters
	 * @return the map of parameters to send to the engine 
	 */
	protected Map applySecurity(Map pars) {
		pars = cryptParams(pars);
        pars = addSecurityToken(pars);
        return pars;
	}
	
	
	/**
	 * Add to the map parameters the authethication token
	 * @param pars Map of the parameters
	 * @return Map of the parameters containing the security information
	 */
	private Map cryptParams(Map pars) {
		// TODO change the following code encrypting parameters with a secure alghoritm
		/*
		SecurityUtilities secUt = new SecurityUtilities();
        Set keys = pars.keySet();
		Iterator iterKeys = keys.iterator();
        while(iterKeys.hasNext()) {
        	String key = (String)iterKeys.next();
        	String value = (String)pars.get(key);
        	String encoded64 = secUt.encodeBase64(value.getBytes());
        	pars.put(key, encoded64);        	
        }
        */
	    return pars;
	}
	
        
	/**
	 * Add to the map parameters the authethication token
	 * @param pars Map of the parameters
	 * @return Map of the parameters containing the security information
	 */
	private Map addSecurityToken(Map pars) {
		String queryStr = buildQueryStr(pars);
		byte[] challenge = queryStr.getBytes();
		SecurityUtilities secUt = new SecurityUtilities();
        byte[] encoded = secUt.signDSA(challenge);
        if(encoded==null) {
			SpagoBITracer.warning("ENGINES",
								  this.getClass().getName(),
								  "addSecurityToken",
								  "encoded challenge byte array null");
			return pars;
		}
        String encoded64 = secUt.encodeBase64(encoded);
        pars.put("TOKEN_CLEAR", queryStr);
	    pars.put("TOKEN_SIGN", encoded64);
	    return pars;
	}
	
	
	/**
	 * Builds and returns the query string to send to the engine. The query string is composed 
	 * using the parameters map
	 * @param pars The map of parameters
	 * @return the query string to send to the engine
	 */
	private String buildQueryStr(Map pars) {
		SecurityUtilities secUt = new SecurityUtilities();
        String queryString = "";
        Set keys = pars.keySet();
		Iterator iterKeys = keys.iterator();
        while(iterKeys.hasNext()) {
        	String key = (String)iterKeys.next();
        	String value = (String)pars.get(key);
            queryString = queryString + key + "=" + value + "&";
        }
        return queryString;
	}

}

