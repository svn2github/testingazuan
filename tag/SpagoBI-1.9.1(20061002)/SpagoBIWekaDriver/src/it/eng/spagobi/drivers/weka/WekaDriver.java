/**
 *
 *	LICENSE: see 'LICENSE-sbi.drivers.weka.txt' file
 *
 */
package it.eng.spagobi.drivers.weka;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.ParameterValuesEncoder;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import sun.misc.BASE64Encoder;



/**
 * Driver Implementation (IEngineDriver Interface) for Jasper Report Engine. 
 */
public class WekaDriver implements IEngineDriver {

	/**
	 * Returns a map of parameters which will be send in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object biobject, IEngUserProfile profile, String roleName) {
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			map = getMap(biobj, profile);
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, IEngUserProfile, String)",
					"The parameter is not a BIObject type",
					cce);
		} 
		map = applySecurity(map);
		return map;
	}


    /**
	 * Returns a map of parameters which will be send in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
  	 */
	public Map getParameterMap(Object object, Object subObject, IEngUserProfile profile, String roleName) {
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj, profile);
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object, Object, IEngUserProfile, String)",
					"The parameter is not a BIObject type",
					cce);
		} 
		map = applySecurity(map);
		return map;
	}
	
	
	
	/**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	private Map getMap(BIObject biobj, IEngUserProfile profile) {
		Map pars = new Hashtable();
		
		biobj.loadTemplate();
		UploadedFile uploadedFile =  biobj.getTemplate();
		byte[] template = uploadedFile.getFileContent();
		BASE64Encoder bASE64Encoder = new BASE64Encoder();
		pars.put("template", bASE64Encoder.encode(template));
		//pars.put("templatePath",biobj.getPath() + "/template");
		pars.put("biobjectId", biobj.getId().toString());
		pars.put("cr_manager_url", GeneralUtilities.getSpagoBiContentRepositoryServlet());
    	pars.put("events_manager_url", GeneralUtilities.getSpagoBiEventsManagerServlet());
    	pars.put("processActivatedMsg", GeneralUtilities.replaceInternationalizedMessages("${weka.execution.processActivatedMsg}"));
    	pars.put("processNotActivatedMsg", GeneralUtilities.replaceInternationalizedMessages("${weka.execution.processNotActivatedMsg}"));
        Map biobjParameters = findBIParameters(biobj);
        pars.putAll(biobjParameters);
        // callback event id
        if (profile!=null) {
        	String user = (String) profile.getUserUniqueIdentifier();
        	pars.put("user", user);
        }
        return pars;
	} 
 
	
	
    /**
     * Returns the BIObject parameters map
     * @param biobj BIOBject to be executed
     * @return Map The map of the BIObject parameters
     */
	private Map findBIParameters(BIObject biobj) {
		Map pars = new HashMap();
		if (biobj==null) {
			SpagoBITracer.warning("ENGINES",
								  this.getClass().getName(),
								  "addBIParameters",
								  "BIObject null");
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
	 * Applys changes for security reason if necessary
	 * @param pars The map of parameters
	 * @return the map of parameters to send to the engine 
	 */
	protected Map applySecurity(Map pars) {
		return pars;
	}
       

}

