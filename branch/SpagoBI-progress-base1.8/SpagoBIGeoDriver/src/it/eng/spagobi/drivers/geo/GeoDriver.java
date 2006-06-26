/**
 * 
 * LICENSE: see 'LICENSE-sbi.drivers.geo.txt' file
 * 
 */
package it.eng.spagobi.drivers.geo;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import sun.misc.BASE64Encoder;


/**
 * Driver Implementation (IEngineDriver Interface) for Geo Engine. 
 */
public class GeoDriver implements IEngineDriver {

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
    	Map map = getParameterMap(object);
//		Map map = null;
//		map = getParameterMap(object);
//		String username = (String)profile.getUserUniqueIdentifier();
//		map.put("user", username);
		return map;
	}
	/**
	 * Return a map of parameters which will be sended in the request to the 
	 * engine application.
	 * @param biObject Object container of the subObject
	 * @param subObject SubObject to execute
	 * @return Map The map of the execution call parameters
  	 */
	public Map getParameterMap(Object object, Object subObject){
    	Map map = getParameterMap(object);
//		Map map = new Hashtable();
//		try{
//			SubObjectDetail subObj = (SubObjectDetail)subObject;
//			map = getParameterMap(object);
//			String nameSub = subObj.getName();
//			map.put("nameSubObject", nameSub);
//			String descrSub = subObj.getDescription();
//			map.put("descriptionSubObject", descrSub);
//			String visStr = "Private";
//			boolean visBool = subObj.isPublicVisible();
//		    if(visBool) 
//		    	visStr = "Public";
//			map.put("visibilitySubObject", visStr);
//		} catch (ClassCastException cce) {
//			SpagoBITracer.major("ENGINES",
//					this.getClass().getName(),
//					"getParameterMap(Object, Object)",
//					"The second parameter is not a SubObjectDetail type",
//					cce);
//		} 
		return map;
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
    	Map map = getParameterMap(object);
//    	Map map = new Hashtable();
//		try{
//			SubObjectDetail subObj = (SubObjectDetail)subObject;
//			map = getParameterMap(object, profile);
//			String nameSub = subObj.getName();
//			map.put("nameSubObject", nameSub);
//			String descrSub = subObj.getDescription();
//			map.put("descriptionSubObject", descrSub);
//			String visStr = "Private";
//			boolean visBool = subObj.isPublicVisible();
//		    if(visBool) 
//		    	visStr = "Public";
//			map.put("visibilitySubObject", visStr);
//		} catch (ClassCastException cce) {
//			SpagoBITracer.major("ENGINES",
//					this.getClass().getName(),
//					"getParameterMap(Object, Object, IEngUserProfile)",
//					"The second parameter is not a SubObjectDetail type",
//					cce);
//		} 
		return map;
	}

    
        
        
    /**
     * Starting from a BIObject extracts from it the map of the paramaeters for the
     * execution call
     * @param biobj BIObject to execute
     * @return Map The map of the execution call parameters
     */    
	private Map getMap(BIObject biobj) {
		Map pars = new Hashtable();
		biobj.loadTemplate();
		UploadedFile uploadedFile =  biobj.getTemplate();
		byte[] template = uploadedFile.getFileContent();
		BASE64Encoder bASE64Encoder = new BASE64Encoder();
		pars.put("template", bASE64Encoder.encode(template));
//		pars.put("templatePath",biobj.getPath() + "/template");
//        pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
//		pars.put("query", "dynamicOlap");
        pars = addBIParameters(biobj, pars);
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
	
}
