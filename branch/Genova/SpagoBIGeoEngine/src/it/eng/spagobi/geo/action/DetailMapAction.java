/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spagobi.geo.bo.SbiGeoFeatures;
import it.eng.spagobi.geo.bo.SbiGeoMapFeatures;
import it.eng.spagobi.geo.bo.SbiGeoMapFeaturesId;
import it.eng.spagobi.geo.bo.SbiGeoMaps;
import it.eng.spagobi.geo.bo.dao.DAOFactory;
import it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;

/**
 * Spago Action which executes the map producing request  
 */
public class DetailMapAction extends AbstractAction {
	public static final String ACTION_PAGE = "ListMapsPage";
	private String modalita = "";
	
	
	/**
	 * Method called automatically by Spago framework when the action is invoked.
	 * The method search into the request two parameters
	 * <ul>
	 * <li>message: a message which contains the type of the request</li>
	 * </ul>
	 * 
	 * @param serviceRequest the Spago request SourceBean 
	 * @param serviceResponse the Spago response SourceBean 
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {		
		// AUDIT UPDATE
		String auditId = (String)serviceRequest.getAttribute("SPAGOBI_AUDIT_ID");
		AuditAccessUtils auditAccessUtils = 
			(AuditAccessUtils) serviceRequest.getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
				"EXECUTION_STARTED", null, null);

		String message = (String) serviceRequest.getAttribute("MESSAGEDET");
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,  "begin of detail Map modify/visualization service with message =" + message);		

		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);				
				TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.DEBUG,  "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(Constants.DETAIL_SELECT)) {
				String id = (String) serviceRequest.getAttribute("MAP_ID");
				getDetailMap(id, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_MOD)) {
				modDetailMap(serviceRequest, Constants.DETAIL_MOD, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_NEW)) {
				newDetailMap(serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_INS)) {
				modDetailMap(serviceRequest, Constants.DETAIL_INS, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_DEL)) {
				delDetailMap(serviceRequest, Constants.DETAIL_DEL, serviceResponse);
			}

		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	
		// AUDIT UPDATE		
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
				"EXECUTION_PERFORMED", null, null);
		
				
	}
	
	
	/**
	 * sends an error message to the client
	 * @param out The servlet output stream
	 */
	private void sendError(ServletOutputStream out)  {
		try{
			out.write("<html>".getBytes());
			out.write("<body>".getBytes());
			out.write("<br/><br/><center><h2><span style=\"color:red;\">Unable to produce map</span></h2></center>".getBytes());
			out.write("</body>".getBytes());
			out.write("</html>".getBytes());
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
								"GeoAction :: sendError : " +
								"Unable to write into output stream ", e);
		}
	}
	

/**
 * Gets the detail of an map choosed by the user from the 
 * maps list. It reaches the key from the request and asks to the DB all detail
 * map information, by calling the method <code>loadMapByID</code>.
 *   
 * @param key The choosed map id key
 * @param response The response Source Bean
 * @throws EMFUserError If an exception occurs
 */
private void getDetailMap(String key, SourceBean response) throws EMFUserError {
	try {
		this.modalita = Constants.DETAIL_MOD;
		response.setAttribute("modality", modalita);
		SbiGeoMaps map = DAOFactory.getSbiGeoMapsDAO().loadMapByID(new Integer(key));
		response.setAttribute("mapObj", map);
	} catch (Exception ex) {
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());	
		HashMap params = new HashMap();
		//params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
		throw new EMFUserError(EMFErrorSeverity.ERROR, 1014, new Vector(), params);
	}
}
 /**
 * Inserts/Modifies the detail of an map according to the user request. 
 * When a map is modified, the <code>modifyMap</code> method is called; when a new
 * map is added, the <code>insertMap</code>method is called. These two cases are 
 * differentiated by the <code>mod</code> String input value .
 * 
 * @param request The request information contained in a SourceBean Object
 * @param mod A request string used to differentiate insert/modify operations
 * @param response The response SourceBean 
 * @throws EMFUserError If an exception occurs
 * @throws SourceBeanException If a SourceBean exception occurs
 */
private void modDetailMap(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
	
	try {
		SbiGeoMaps map = recoverMapDetails(request);
		SbiGeoFeatures feature = null;
		SbiGeoMapFeatures mapFeatures = null;
		EMFErrorHandler errorHandler = getErrorHandler();
		
		// if there are some validation errors into the errorHandler does not write into DB
		Collection errors = errorHandler.getErrors();
		if (errors != null && errors.size() > 0) {
			Iterator iterator = errors.iterator();
			while (iterator.hasNext()) {
				Object error = iterator.next();
				if (error instanceof EMFValidationError) {
					response.setAttribute("mapObj", map);
					response.setAttribute("modality", mod);
					return;
				}
			}
		}
		
		if (mod.equalsIgnoreCase(Constants.DETAIL_INS)) {
			//the activity INSERT consists in:
			//inserts a map (SBI_GEO_MAPS)
			DAOFactory.getSbiGeoMapsDAO().insertMap(map);
			//through the url of a map, gets and opens the svg file and insert a feature for every tag <g>
			List lstFeatures = DAOFactory.getSbiGeoMapsDAO().getFeaturesFromSVG(map.getUrl());			
			for (int i=0; i < lstFeatures.size(); i++){				
				HashMap hFeature = (HashMap)lstFeatures.get(i);
				feature = new SbiGeoFeatures();
				feature.setName((String)hFeature.get("id"));
				feature.setDescr((String)hFeature.get("descr"));
				feature.setType((String)hFeature.get("type"));
				DAOFactory.getSbiGeoFeaturesDAO().insertFeature(feature);
//				for every map/feature inserts a row in join table (SBI_GEO_MAP_FEATURES)
				SbiGeoMapFeatures hibMapFeatures = new SbiGeoMapFeatures();
				SbiGeoMapFeaturesId hibMapFeatureId = new SbiGeoMapFeaturesId();
				hibMapFeatureId.setMapId(((SbiGeoMaps)DAOFactory.getSbiGeoMapsDAO().loadMapByName(map.getName())).getMapId());
				hibMapFeatureId.setFeatureId(((SbiGeoFeatures)DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByName(feature.getName())).getFeatureId());
				hibMapFeatures.setId(hibMapFeatureId);  
				hibMapFeatures.setSvgGroup(null);
				hibMapFeatures.setVisibleFlag(null);
				DAOFactory.getSbiGeoMapFeaturesDAO().insertMapFeatures(hibMapFeatures);			
			}
		} else {
			DAOFactory.getSbiGeoMapsDAO().modifyMap(map);
		}
		//test anto
		response.setAttribute("mapObj", map);
        
	} catch (EMFUserError e){
		HashMap params = new HashMap();
		params.put(Constants.ACTION, this.ACTION_PAGE);
		throw new EMFUserError(EMFErrorSeverity.ERROR, 1012, new Vector(), params);
		
	}
	
	catch (Exception ex) {		
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());		
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	response.setAttribute("loopback", "true");
	
}

/**
 * Deletes a map choosed by user from the maps list.
 * 
 * @param request	The request SourceBean
 * @param mod	A request string used to differentiate delete operation
 * @param response	The response SourceBean
 * @throws EMFUserError	If an Exception occurs
 * @throws SourceBeanException If a SourceBean Exception occurs
 */
private void delDetailMap(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
	
	try {
		String id = (String) request.getAttribute("MAP_ID");
        ISbiGeoMapsDAO mapdao = DAOFactory.getSbiGeoMapsDAO();
        SbiGeoMaps map = mapdao.loadMapByID(new Integer(id));
//		controls if the map is in use by any BIFeautures
		boolean isAss = mapdao.hasFeaturesAssociated(id);
		if (isAss){
			HashMap params = new HashMap();
			//params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1030, new Vector(), params );
			getErrorHandler().addError(error);
			return;
			
		}
		mapdao.eraseMap(map);
	}   catch (EMFUserError e){
		  HashMap params = new HashMap();
		  params.put(Constants.ACTION, this.ACTION_PAGE);
		  throw new EMFUserError(EMFErrorSeverity.ERROR, 1013, new Vector(), params);
			
		}
	    catch (Exception ex) {		
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, "Cannot fill response container" + ex.getLocalizedMessage());
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	response.setAttribute("loopback", "true");
	
}


/**
 * Instantiates a new <code>map<code> object when a new map insertion is required, in order
 * to prepare the page for the insertion.
 * 
 * @param response The response SourceBean
 * @throws EMFUserError If an Exception occurred
 */

private void newDetailMap(SourceBean response) throws EMFUserError {
	
	try {
		
		SbiGeoMaps map = null;
		this.modalita = Constants.DETAIL_INS;
		response.setAttribute("modality", modalita);
		map = new SbiGeoMaps();
		map.setMapId(new Integer(-1));
		map.setDescr("");
		map.setName("");
		map.setUrl("");
		response.setAttribute("mapObj", map);
	} catch (Exception ex) {
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, "Cannot prepare page for the insertion" + ex.getLocalizedMessage());		
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	
}


private SbiGeoMaps recoverMapDetails (SourceBean request) throws EMFUserError {
	
	String idStr = (String)request.getAttribute("MAP_ID");
	Integer id = new Integer(idStr);
	String description = (String)request.getAttribute("DESCR");	
	String name = (String)request.getAttribute("NAME");
	String url = (String)request.getAttribute("URL");
		
	SbiGeoMaps map  = new SbiGeoMaps();
	map.setMapId(id);
	map.setName(name);
	map.setDescr(description);
	map.setUrl(url);
	
   /* List mapsList = DAOFactory.getSbiGeoMapsDAO().loadAllMaps();
	Iterator i = mapsList.listIterator();
	while (i.hasNext()) {
		SbiGeoMaps ma = (SbiGeoMaps) i.next();
		if (ma.getName().equals(name) && !String.valueOf(ma.getMapId()).equals(id)) {
			HashMap params = new HashMap();
			params.put(Constants.ACTION, this.ACTION_PAGE);
			EMFValidationError error = new EMFValidationError(EMFErrorSeverity.ERROR, "label", 
					"1011", new Vector(), params);
			getErrorHandler().addError(error);
		}
	}
    */
	return map;
	}
		
}