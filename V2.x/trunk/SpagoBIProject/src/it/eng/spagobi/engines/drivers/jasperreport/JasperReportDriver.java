/**
 * 
 * LICENSE: see 'LICENSE.sbi.drivers.jasperreports.txt' file
 * 
 */
package it.eng.spagobi.engines.drivers.jasperreport;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubreportDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ParameterValuesEncoder;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.drivers.EngineURL;
import it.eng.spagobi.engines.drivers.IEngineDriver;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Encoder;



/**
 * Driver Implementation (IEngineDriver Interface) for Jasper Report Engine. 
 */
public class JasperReportDriver implements IEngineDriver {

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
			map = getMap(biobj);
		} catch (ClassCastException cce) {
			SpagoBITracer.major("ENGINES",
					this.getClass().getName(),
					"getParameterMap(Object)",
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
		return getParameterMap(object, profile, roleName);
	}
	
	
	/**
	 * Applys changes for security reason if necessary
	 * @param pars The map of parameters
	 * @return the map of parameters to send to the engine 
	 */
	protected Map applySecurity(Map pars) {
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
		try{
			ObjTemplate objtemplate = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(biobj.getId());
			if(objtemplate==null) throw new Exception("Active Template null");
			byte[] template = DAOFactory.getBinContentDAO().getBinContent(objtemplate.getBinId());
			if(template==null) throw new Exception("Content of the Active template null");
			BASE64Encoder bASE64Encoder = new BASE64Encoder();
			pars.put("template", bASE64Encoder.encode(template));
			pars.put("templatePath",biobj.getPath() + "/template");
	        pars.put("spagobiurl", GeneralUtilities.getSpagoBiContentRepositoryServlet());
	        String flgTemplateStandard = "true";
	        if (objtemplate.getName().indexOf(".zip") > -1){        	
	        		flgTemplateStandard = "false";
	        }
	        pars.put("flgTemplateStandard", flgTemplateStandard);
	        pars = addBISubreports(biobj, pars);
	        pars = addBIParameters(biobj, pars);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "getMap", "Error while recovering execution parameter map: \n" + e);
		}
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
				ObjTemplate srobjtemplate = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(subrptbiobj.getId());
				if(srobjtemplate==null) throw new Exception("SubObject "+subrptbiobj.getLabel()  +" Active Template null");
				String flgTemplateStandard = "true";
		        if (srobjtemplate.getName().indexOf(".zip") > -1){        	
		        		flgTemplateStandard = "false";
		        }
		        SpagoBITracer.debug("JasperReportDriver", "JasperReportDriver","addBISubreports", " flgTemplateStandard: " + flgTemplateStandard);
		        pars.put("subrpt." + (i+1) + ".flgTempStd", flgTemplateStandard);
				String path = subrptbiobj.getPath();				
				SpagoBITracer.debug("JasperReportDriver", "JasperReportDriver","addBISubreports", " PATH: " + path);
				pars.put("subrpt." + (i+1) + ".path", path);
			}
			pars.put("srptnum", "" + subreportList.size());
		} catch (Exception e) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					  			 "addBISubreports", "Error while reading subreports", e);
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
		
		ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
		if(biobj.getBiObjectParameters() != null){
			BIObjectParameter biobjPar = null;
			for(Iterator it = biobj.getBiObjectParameters().iterator(); it.hasNext();){
				try {
					biobjPar = (BIObjectParameter)it.next();
					
					
					/*
					String value = "";
					for(int i = 0; i < biobjPar.getParameterValues().size(); i++)
						value += (i>0?",":"") + (String)biobjPar.getParameterValues().get(i);
					 */
					String value = parValuesEncoder.encode(biobjPar);
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
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject The BIOBject to edit
	 * @throws InvalidOperationRequest
	 */
	public EngineURL getEditDocumentTemplateBuildUrl(Object biobject) throws InvalidOperationRequest {
		SpagoBITracer.major("ENGINES",
				  this.getClass().getName(),
				  "getEditDocumentTemplateBuildUrl",
				  "Function not implemented");
		throw new InvalidOperationRequest();
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param biobject The BIOBject to edit
	 * @throws InvalidOperationRequest
	 */
	public EngineURL getNewDocumentTemplateBuildUrl(Object biobject) throws InvalidOperationRequest {
		SpagoBITracer.major("ENGINES",
				  this.getClass().getName(),
				  "getNewDocumentTemplateBuildUrl",
				  "Function not implemented");
		throw new InvalidOperationRequest();
	}
	
}

