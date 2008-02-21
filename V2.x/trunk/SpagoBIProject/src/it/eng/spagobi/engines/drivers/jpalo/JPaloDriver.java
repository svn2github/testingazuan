/**
 * 
 * LICENSE: see 'LICENSE.sbi.drivers.jasperreports.txt' file
 * 
 */
package it.eng.spagobi.engines.drivers.jpalo;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubreportDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IBinContentDAO;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.engines.drivers.EngineURL;
import it.eng.spagobi.engines.drivers.IEngineDriver;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Driver Implementation (IEngineDriver Interface) for Jasper Report Engine.
 */
public class JPaloDriver implements IEngineDriver {

    static private Logger logger = Logger.getLogger(JPaloDriver.class);
    
    /**
     * Returns a map of parameters which will be send in the request to the
     * engine application.
     * 
     * @param biObject  Object to execute
     * @param profile   Profile of the user
     * @param roleName  the name of the execution role
     * @return Map The map of the execution call parameters
     */
    public Map getParameterMap(Object biobject, IEngUserProfile profile,String roleName) {
		logger.debug("IN");
		Map map = new Hashtable();
		try {
		    BIObject biobj = (BIObject) biobject;
		    map = getMap(biobj);
		} catch (ClassCastException cce) {
		    logger.error("The parameter is not a BIObject type", cce);
		}
		map = applySecurity(map,profile);
		logger.debug("OUT");
		return map;
    }

    /**
     * Returns a map of parameters which will be send in the request to the
     * engine application.
     * 
     * @param biObject  Object container of the subObject
     * @param subObject SubObject to execute
     * @param profile   Profile of the user
     * @param roleName  the name of the execution role
     * @return Map The map of the execution call parameters
     */
    public Map getParameterMap(Object object, Object subObject,
	    IEngUserProfile profile, String roleName) {
    	return getParameterMap(object, profile, roleName);
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
     * Starting from a BIObject extracts from it the map of the paramaeters for
     * the execution call
     * 
     * @param biobj
     *                BIObject to execute
     * @return Map The map of the execution call parameters
     */
    private Map getMap(BIObject biobj) {
		logger.debug("IN");
		Map pars = new Hashtable();
	
		String documentId=biobj.getId().toString();
		pars.put("document", documentId);
		logger.debug("Add document parameter:"+documentId);
		pars = addBIParameters(biobj, pars);
	  
		logger.debug("OUT");
		return pars;
    }

    

    /**
     * Add into the parameters map the BIObject's BIParameter names and values
     * @param biobj  BIOBject to execute
     * @param pars   Map of the parameters for the execution call
     * @return Map The map of the execution call parameters
     */
    private Map addBIParameters(BIObject biobj, Map pars) {
		logger.debug("IN");
		if (biobj == null) {
		    logger.warn("BIObject parameter null");	    
		    return pars;
		}
	
		ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
		if (biobj.getBiObjectParameters() != null) {
		    BIObjectParameter biobjPar = null;
		    for (Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();) {
			try {
			    biobjPar = (BIObjectParameter) it.next();
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

}
