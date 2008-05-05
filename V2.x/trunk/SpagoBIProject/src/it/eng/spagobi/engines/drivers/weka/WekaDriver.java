/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.drivers.weka;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.engines.drivers.EngineURL;
import it.eng.spagobi.engines.drivers.IEngineDriver;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * Driver Implementation (IEngineDriver Interface) for Jasper Report Engine. 
 */
public class WekaDriver implements IEngineDriver {

	
    static private Logger logger = Logger.getLogger(WekaDriver.class);
	
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
			map = getMap(biobj, profile);
		} catch (ClassCastException cce) {
			logger.error("The parameter is not a BIObject type",cce);
		} 
		map = applySecurity(map, profile);
		logger.debug("OUT");
		return map;
	}


    /**
     * Returns a map of parameters which will be send in the request to the
     * engine application.
     * 
     * @param subObject SubObject to execute
     * @param profile Profile of the user
     * @param roleName the name of the execution role
     * @param object the object
     * 
     * @return Map The map of the execution call parameters
     */
	public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile, String roleName) {
		logger.debug("IN");
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj, profile);
		} catch (ClassCastException e) {
			logger.error("The parameter is not a BIObject type",e);
		} 
		map = applySecurity(map, profile);
		logger.debug("OUT");
		return map;
	}
	
	
	
	/**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	private Map getMap(BIObject biobj, IEngUserProfile profile) {
		logger.debug("IN");
		Map pars = new Hashtable();

		 
		pars.put("document", biobj.getId().toString());
    	pars.put("processActivatedMsg", GeneralUtilities.replaceInternationalizedMessages("${weka.execution.processActivatedMsg}"));
    	pars.put("processNotActivatedMsg", GeneralUtilities.replaceInternationalizedMessages("${weka.execution.processNotActivatedMsg}"));
        Map biobjParameters = findBIParameters(biobj);
        pars.putAll(biobjParameters);
        logger.debug("OUT");
        return pars;
	} 
 
	
	
    /**
     * Applys changes for security reason if necessary
     * 
     * @param pars  The map of parameters
     * @return      The map of parameters to send to the engine
     */
    protected Map applySecurity(Map pars, IEngUserProfile profile) {
	logger.debug("IN");
	pars.put("userId", profile.getUserUniqueIdentifier());
	logger.debug("Add parameter: userId/"+profile.getUserUniqueIdentifier());
	logger.debug("OUT");
	return pars;
    }
	
    /**
     * Returns the BIObject parameters map
     * @param biobj BIOBject to be executed
     * @return Map The map of the BIObject parameters
     */
	private Map findBIParameters(BIObject biobj) {
		logger.debug("IN");
		Map pars = new HashMap();
		if (biobj==null) {
			logger.warn("BIObject null");
			return pars;
		}
		if (biobj.getBiObjectParameters() != null){
			BIObjectParameter biobjPar = null;
			String value = null;
			ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
			for (Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();) {
				try {
					biobjPar = (BIObjectParameter)it.next();
					
					value = parValuesEncoder.encode(biobjPar);
					pars.put(biobjPar.getParameterUrlName(), value);
					
					/*
					value = (String)biobjPar.getParameterValues().get(0);
					pars.put(biobjPar.getParameterUrlName(), value);
					*/
				} catch (Exception e) {
					logger.warn("Error while processing a BIParameter",e);
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
	public EngineURL getEditDocumentTemplateBuildUrl(Object biobject, IEngUserProfile profile) throws InvalidOperationRequest {
        logger.error("Function not implemented");
		throw new InvalidOperationRequest();
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject The BIOBject to edit
	 * @param profile the profile
	 * 
	 * @return the new document template build url
	 * 
	 * @throws InvalidOperationRequest the invalid operation request
	 */
	public EngineURL getNewDocumentTemplateBuildUrl(Object biobject, IEngUserProfile profile) throws InvalidOperationRequest {
        logger.error("Function not implemented");
		throw new InvalidOperationRequest();
	}

}

