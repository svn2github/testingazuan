/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

 */

/*
 * Created on 3-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.drivers.jpivot;

import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.SecurityUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Driver Implementation (IEngineDriver Interface) for JPivot Engine. 
 */
public class JPivotDriverSignSec extends JPivotDriver implements IEngineDriver {

	
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
