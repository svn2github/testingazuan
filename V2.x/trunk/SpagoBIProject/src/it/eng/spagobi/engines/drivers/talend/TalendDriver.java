/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
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
**/

package it.eng.spagobi.engines.drivers.talend;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.engines.drivers.EngineURL;
import it.eng.spagobi.engines.drivers.IEngineDriver;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * Driver Implementation (IEngineDriver Interface) for Talend External Engine. 
 */
public class TalendDriver implements IEngineDriver {

	static private Logger logger = Logger.getLogger(TalendDriver.class);

	private void addLocale(Map map) {
		logger.debug("IN");
		ConfigSingleton config = ConfigSingleton.getInstance();
		Locale portalLocale = null;
		try {
			portalLocale =  PortletUtilities.getPortalLocale();
		} catch (Exception e) {
			logger.warn("Error while getting portal locale: default en");
			portalLocale = new Locale("en", "US");
		}
		SourceBean languageSB = null;
		if(portalLocale != null && portalLocale.getLanguage() != null) {
			languageSB = (SourceBean)config.getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
					"language", portalLocale.getLanguage());
		}
		if(languageSB != null) {
			map.put("country", (String)languageSB.getAttribute("country"));
			map.put("language", (String)languageSB.getAttribute("language"));
		} else {
			map.put("country", "US");
			map.put("language", "en");
		}
		logger.debug("Locale set");
		logger.debug("OUT");
	}

	/**
	 * Returns a map of parameters which will be send in the request to the
	 * engine application.
	 * 
	 * @param profile Profile of the user
	 * @param roleName the name of the execution role
	 * @param biobject the biobject
	 * 
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object biobject, IEngUserProfile profile, String roleName) {
		logger.debug("IN");
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			map = getMap(biobj);
			//map.put("user", profile.getUserUniqueIdentifier());
		} catch (ClassCastException cce) {
			logger.error("The parameter is not a BIObject type", cce);
		} 
		map = applySecurity(map,profile);
		logger.debug("OUT");
		return map;
	}

	/**
	 * SpagoBITalendEngine does not manage subobejcts, so this method is equivalent to <code>getParameterMap(object, profile, roleName)</code>.
	 * 
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user
	 * @param roleName the name of the execution role
	 * @param object the object
	 * 
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile, String roleName) {
		return getParameterMap(object, profile, roleName);
	}


	/**
	 * Applys changes for security reason if necessary
	 * 
	 * @param pars  The map of parameters
	 * @return      The map of parameters to send to the engine
	 */
	protected Map applySecurity(Map pars,IEngUserProfile profile) {
		logger.debug("IN");
		pars.put("userId", profile.getUserUniqueIdentifier());
		logger.debug("Add parameter: userId/"+profile.getUserUniqueIdentifier());
		logger.debug("OUT");
		return pars;
	}

	/**
	 * Starting from a BIObject extracts from it the map of the paramaeters for the
	 * execution call
	 * @param biobj BIObject to execute
	 * @return Map The map of the execution call parameters
	 */    
	private Map getMap(BIObject biobj) {
		Map pars = new Hashtable();

		String documentId=biobj.getId().toString();
		pars.put("document", documentId);
		logger.debug("Add document parameter:"+documentId);
		addLocale(pars);
		pars = addBIParameters(biobj, pars);
		logger.debug("OUT");
		return pars;
	} 

	/**
	 * Add into the parameters map the BIObject's BIParameter names and values
	 * @param biobj BIOBject to execute
	 * @param pars Map of the parameters for the execution call  
	 * @return Map The map of the execution call parameters
	 */
	private Map addBIParameters(BIObject biobj, Map pars) {
		logger.debug("IN");
		if (biobj == null) {
			logger.warn("BIObject parameter null");	    
			return pars;
		}

		ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
		if(biobj.getBiObjectParameters() != null){
			BIObjectParameter biobjPar = null;
			for(Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();){
				try {
					biobjPar = (BIObjectParameter)it.next();
					String value = parValuesEncoder.encode(biobjPar);
					pars.put(biobjPar.getParameterUrlName(), value);
					logger.debug("Add parameter:"+biobjPar.getParameterUrlName()+"/"+value);
				} catch (Exception e) {
					logger.error("Error while processing a BIParameter",e);
				}
			}
		}
		logger.debug("OUT");
		return pars;
	}



	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject The BIOBject to edit
	 * @param profile the profile
	 * 
	 * @return the edits the document template build url
	 * 
	 * @throws InvalidOperationRequest the invalid operation request
	 */
	public EngineURL getEditDocumentTemplateBuildUrl(Object biobject,
			IEngUserProfile profile) throws InvalidOperationRequest {
		logger.warn("Function not implemented");
		throw new InvalidOperationRequest();
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject  The BIOBject to edit
	 * @param profile the profile
	 * 
	 * @return the new document template build url
	 * 
	 * @throws InvalidOperationRequest the invalid operation request
	 */
	public EngineURL getNewDocumentTemplateBuildUrl(Object biobject, IEngUserProfile profile)
	throws InvalidOperationRequest {
		logger.warn("Function not implemented");
		throw new InvalidOperationRequest();
	}

}

