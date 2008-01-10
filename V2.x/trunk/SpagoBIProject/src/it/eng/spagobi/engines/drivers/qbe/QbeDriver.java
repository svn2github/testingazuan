/**
 * 
 * LICENSE: see 'LICENSE.sbi.drivers.qbe.txt' file
 * 
 */
package it.eng.spagobi.engines.drivers.qbe;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.commons.utilities.UploadedFile;
import it.eng.spagobi.engines.drivers.EngineURL;
import it.eng.spagobi.engines.drivers.IEngineDriver;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;



/**
 * Driver Implementation (IEngineDriver Interface) for Qbe External Engine. 
 */
public class QbeDriver implements IEngineDriver {
	
	static private Logger logger = Logger.getLogger(QbeDriver.class);
	 
		
	/**
	 * Returns a map of parameters which will be send in the request to the 
	 * engine application.
	 * @param biObject Object to execute
	 * @param profile Profile of the user 
	 * @param roleName the name of the execution role
	 * @return Map The map of the execution call parameters
	 */
	public Map getParameterMap(Object biobject, IEngUserProfile profile, String roleName) {
		logger.debug("IN");
		
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)biobject;
			map = getMap(biobj);
			map.put("query", "#");
			//map.put("user", profile.getUserUniqueIdentifier());
		} catch (ClassCastException cce) {
			logger.error("The parameter is not a BIObject type", cce);
		} 
		
		map = applySecurity(map, profile);
		map = applyLocale(map);
		
		logger.debug("OUT");
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
		logger.debug("IN");			
			
		if(subObject == null) {
			return getParameterMap(object, profile, roleName);
		}
		
		Map map = new Hashtable();
		try{
			BIObject biobj = (BIObject)object;
			map = getMap(biobj);
			SubObject subObjectDetail = (SubObject) subObject;
			map.put("query", subObjectDetail.getName());
			//map.put("user", profile.getUserUniqueIdentifier());
		} catch (ClassCastException cce) {
			logger.error("The parameter is not a BIObject type", cce);
		} 
		map = applySecurity(map, profile);
		map = applyLocale(map);
		
		logger.debug("OUT");
		return map;
	}
	
	
	
	     
    /**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	private Map getMap(BIObject biobj) {
		logger.debug("IN");
		
		Map pars = new Hashtable();
		String documentId=biobj.getId().toString();
		pars.put("document", documentId);
		logger.debug("Add document parameter:"+documentId);
		
		//pars.put("templatePath",biobj.getPath() + "/template");
        // pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
        // pars.put("mapCatalogueManagerUrl", GeneralUtilities.getMapCatalogueManagerServlet());
        
        
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
		
		if(biobj==null) {
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
     * @throws InvalidOperationRequest
     */
    public EngineURL getEditDocumentTemplateBuildUrl(Object biobject, IEngUserProfile profile)
	throws InvalidOperationRequest {
    	logger.warn("Function not implemented");
    	throw new InvalidOperationRequest();
    }

    /**
     * Function not implemented. Thid method should not be called
     * 
     * @param biobject  The BIOBject to edit
     * @throws InvalidOperationRequest
     */
    public EngineURL getNewDocumentTemplateBuildUrl(Object biobject, IEngUserProfile profile)
	throws InvalidOperationRequest {
    	logger.warn("Function not implemented");
    	throw new InvalidOperationRequest();
    }

    
        
    /**
	 * Applys changes for security reason if necessary
	 * @param pars The map of parameters
	 * @return the map of parameters to send to the engine 
	 */
	protected Map applySecurity(Map pars, IEngUserProfile profile) {
		logger.debug("IN");
		pars.put("userId", profile.getUserUniqueIdentifier());
		logger.debug("Add parameter: userId/"+ profile.getUserUniqueIdentifier());
		logger.debug("OUT");
		return pars;
	}
    
    private Map applyLocale(Map map) {
    	logger.debug("IN");
    	
		ConfigSingleton config = ConfigSingleton.getInstance();
		Locale portalLocale = null;
		try {
			portalLocale =  PortletUtilities.getPortalLocale();
		} catch (Exception e) {
			logger.warn("Impossible to get portal locale.");
			logger.warn("Default lacale will be used (country: US; language: en).");
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
			logger.debug("Added parameter: country/" + (String)languageSB.getAttribute("country"));
			logger.debug("Added parameter: language/" + (String)languageSB.getAttribute("language"));
		} else {
			logger.warn("Language " + portalLocale.getLanguage() + " is not supported by SpagoBI");
			logger.warn("Portal locale will be replaced with the default lacale (country: US; language: en).");
			map.put("country", "US");
			map.put("language", "en");
			logger.debug("Added parameter: country/US");
			logger.debug("Added parameter: language/en");
		}			
		
		logger.debug("OUT");
		return map;
	}
}

