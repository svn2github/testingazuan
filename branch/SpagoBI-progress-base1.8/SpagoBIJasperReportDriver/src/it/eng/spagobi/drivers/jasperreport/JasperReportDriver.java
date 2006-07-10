/**
 * 
 * LICENSE: see 'LICENSE.sbi.drivers.jasperreports.txt' file
 * 
 */
package it.eng.spagobi.drivers.jasperreport;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * Driver Implementation (IEngineDriver Interface) for Jasper Report Engine. 
 */
public class JasperReportDriver implements IEngineDriver {

	
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
		pars.put("templatePath",biobj.getPath() + "/template");
        pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
        pars = addBISubreports(biobj, pars);
        pars = addBIParameters(biobj, pars);
        return pars;
	} 
 
	// TODO check all the subreport's hierarchy recursively and not only the first level
	private Map addBISubreports(BIObject biobj, Map pars) {
		Integer masterReportId = biobj.getId();
				 
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			IBIObjectDAO biobjectdao = DAOFactory.getBIObjectDAO();
			
			List subreportList =  subrptdao.loadSubreportsByMasterRptId(masterReportId);
			for(int i = 0; i < subreportList.size(); i++) {
				Subreport subreport = (Subreport)subreportList.get(i);
				BIObject subrptbiobj = biobjectdao.loadBIObjectForDetail(subreport.getSub_rpt_id());
								
				
				String path = subrptbiobj.getPath();
				SpagoBITracer.debug("JasperReportDriver", "JasperReportDriver","addBISubreports", " PATH: " + path);
				
				pars.put("subrpt." + (i+1) + ".path", path);
			}
			pars.put("srptnum", "" + subreportList.size());
			
		} catch (EMFUserError e) {
			SpagoBITracer.warning("ENGINES",
					  this.getClass().getName(),
					  "addBISubreports",
					  "Error while reading subreports",
					  e);
		}		
		
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

