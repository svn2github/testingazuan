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
package it.eng.spagobi.drivers.birt;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SecurityUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BirtReportDriverTokenSign implements IEngineDriver {
    
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @return Map The map of the execution call parameters
  	*/
	public Map getParameterMap(Object biobject){
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			map = getMap(biobj);
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object)",
					"The parameter is not a BIObject type",
					cce);
		} 
		return map;
	}			
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @param profile Profile of the user 
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object object, IEngUserProfile profile){
		return getParameterMap(object);
	}
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @return Map The map of the execution call parameters
  	 */
	public Map getParameterMap(Object object, Object subObject){
		return getParameterMap(object);
	}
    /**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user 
	 * @return Map The map of the execution call parameters
  	 */
    public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile){
		return getParameterMap(object);
	}

    
        
        
    /**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	private Map getMap(BIObject biobj) {
   		Map pars = new Hashtable();
		ConfigSingleton config = ConfigSingleton.getInstance();
//		SourceBean biobjectsPathSB = (SourceBean) config.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
//		String biobjectsPath = (String) biobjectsPathSB.getAttribute("path");
//		String path = biobjectsPath + "/" + biobj.getUuid() + "/template";
		String path = biobj.getPath() + "/template";
		pars.put("templatePath", path);
        pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
		// retrieving the date format
	    SourceBean formatSB = (SourceBean) config.getAttribute("DATA-ACCESS.DATE-FORMAT");
	    String format = (String) formatSB.getAttribute("format");
	    pars.put("dateformat", format);
        pars = addBIParameters(biobj, pars);
        pars = addSecurityToken(pars);
        return pars;
	} 
 
         
    /**
     * Add into the parameters map the BIObject's BIParameter names and values
     * @param biobj BIOBject to execute
     * @param pars Map of the parameters for the execution call  
     * @return Map The map of the execution call parameters
     */
	private Map addBIParameters(BIObject biobj, Map pars) {
		if(biobj==null) {
			SpagoBITracer.warning("ENGINES",
								  this.getClass().getName(),
								  "addBIParameters",
								  "BIObject parameter null");
			return pars;
		}
		if(biobj.getBiObjectParameters() != null){
			BIObjectParameter biobjPar = null;
			String value = null;
			for(Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();){
				try {
					biobjPar = (BIObjectParameter)it.next();
					value = (String)biobjPar.getParameterValues().get(0);
					pars.put(biobjPar.getParameterUrlName(), value);
				} catch (Exception e) {
					SpagoBITracer.warning("ENGINES",
										  this.getClass().getName(),
										  "addBIParameters",
										  "Error while processing a BIParameter",
										  e);
				}
			}
		}
  		return pars;
	}

	
	/**
	 * Add to the map parameters the authethication token
	 * @param pars Map of the parameters
	 * @return Map of the parameters containing the security information
	 */
	private Map addSecurityToken(Map pars) {
		SecurityUtilities secUt = new SecurityUtilities();
        String queryString = "";
        Set keys = pars.keySet();
		Iterator iterKeys = keys.iterator();
        while(iterKeys.hasNext()) {
        	String key = (String)iterKeys.next();
        	String value = (String)pars.get(key);
            queryString = queryString + key + "=" + value + "&";
        }
        byte[] challenge = queryString.getBytes();
        //String template = (String)pars.get("templatePath");
        //byte[] challenge = template.getBytes();
		//byte[] challenge = secUt.generateRandomChallenge();
		//if(challenge==null) {
		//	SpagoBITracer.warning("ENGINES",
		//						  this.getClass().getName(),
		//						  "addSecurityToken",
		//						  "challenge byte array null");
		//	return pars;
		//}
		//String challenge64 = secUt.encodeBase64(challenge);
        byte[] encoded = secUt.signDSA(challenge);
        if(encoded==null) {
			SpagoBITracer.warning("ENGINES",
								  this.getClass().getName(),
								  "addSecurityToken",
								  "encoded challenge byte array null");
			return pars;
		}
        String encoded64 = secUt.encodeBase64(encoded);
	    pars.put("TOKEN_CLEAR", queryString);
	    pars.put("TOKEN_SIGN", encoded64);
	    // generate identity
	    int parhash = pars.hashCode();
	    Date date = new Date();
	    long time = date.getTime();
	    String identity = "" + parhash + "|" + time;
	    pars.put("IDENTITY", identity);
	    return pars;
	}

}
