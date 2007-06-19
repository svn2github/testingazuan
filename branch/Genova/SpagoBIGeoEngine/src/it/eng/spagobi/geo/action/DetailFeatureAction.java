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
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.geo.bo.SbiGeoFeatures;
import it.eng.spagobi.geo.bo.dao.DAOFactory;
import it.eng.spagobi.geo.bo.dao.ISbiGeoFeaturesDAO;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spago Action which executes the map producing request  
 */
public class DetailFeatureAction extends AbstractAction {
	public static final String ACTION_PAGE = "ListFeaturesAction";
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
				String id = (String) serviceRequest.getAttribute("FEATURE_ID");
				getDetailFeature(id, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_MOD)) {
				modDetailFeature(serviceRequest, Constants.DETAIL_MOD, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_NEW)) {
				newDetailFeature(serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_INS)) {
				modDetailFeature(serviceRequest, Constants.DETAIL_INS, serviceResponse);
			} else if (message.trim().equalsIgnoreCase(Constants.DETAIL_DEL)) {
				delDetailFeature(serviceRequest, Constants.DETAIL_DEL, serviceResponse);
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
								"DetailFeatureAction :: sendError : " +
								"Unable to write into output stream ", e);
		}
	}
	

/**
 * Gets the detail of a feature choosed by the user from the 
 * features list. It reaches the key from the request and asks to the DB all detail
 * feature information, by calling the method <code>loadFeatureByID</code>.
 *   
 * @param key The choosed feature id key
 * @param response The response Source Bean
 * @throws EMFUserError If an exception occurs
 */
private void getDetailFeature(String key, SourceBean response) throws EMFUserError {
	try {
		this.modalita = Constants.DETAIL_MOD;
		response.setAttribute("modality", modalita);
		SbiGeoFeatures feature = DAOFactory.getSbiGeoFeaturesDAO().loadFeatureByID(new Integer(key));
		response.setAttribute("featureObj", feature);
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
private void modDetailFeature(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
	
	try {
		SbiGeoFeatures feature = recoverFeatureDetails(request);
		EMFErrorHandler errorHandler = getErrorHandler();
		
		// if there are some validation errors into the errorHandler does not write into DB
		Collection errors = errorHandler.getErrors();
		if (errors != null && errors.size() > 0) {
			Iterator iterator = errors.iterator();
			while (iterator.hasNext()) {
				Object error = iterator.next();
				if (error instanceof EMFValidationError) {
					response.setAttribute("featureObj", feature);
					response.setAttribute("modality", mod);
					return;
				}
			}
		}
		
		if (mod.equalsIgnoreCase(Constants.DETAIL_INS)) {
			DAOFactory.getSbiGeoFeaturesDAO().insertFeature(feature);
		} else {
			DAOFactory.getSbiGeoFeaturesDAO().modifyFeature(feature);
		}
		//test anto
		response.setAttribute("featureObj", feature);
        
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
private void delDetailFeature(SourceBean request, String mod, SourceBean response)
	throws EMFUserError, SourceBeanException {
	
	try {
		String id = (String) request.getAttribute("FEATURE_ID");
        ISbiGeoFeaturesDAO featuredao = DAOFactory.getSbiGeoFeaturesDAO();
        SbiGeoFeatures feature = featuredao.loadFeatureByID(new Integer(id));
//		controls if the map is in use by any BIFeautures
		boolean isAss = featuredao.hasMapsAssociated(id);
		if (isAss){
			HashMap params = new HashMap();
			//params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1030, new Vector(), params );
			getErrorHandler().addError(error);
			return;
			
		}
		featuredao.eraseFeature(feature);
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

private void newDetailFeature(SourceBean response) throws EMFUserError {
	
	try {
		
		SbiGeoFeatures feature = null;
		this.modalita = Constants.DETAIL_INS;
		response.setAttribute("modality", modalita);
		feature = new SbiGeoFeatures();
		feature.setFeatureId(new Integer(-1));
		feature.setDescr("");
		feature.setName("");
		feature.setType("");
		response.setAttribute("featureObj", feature);
	} catch (Exception ex) {
		TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, "Cannot prepare page for the insertion" + ex.getLocalizedMessage());		
		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	}
	
}


private SbiGeoFeatures recoverFeatureDetails (SourceBean request) throws EMFUserError {
	
	String idStr = (String)request.getAttribute("FEATURE_ID");
	Integer id = new Integer(idStr);
	String description = (String)request.getAttribute("DESCR");	
	String name = (String)request.getAttribute("NAME");
	String type = (String)request.getAttribute("TYPE");
		
	SbiGeoFeatures feature  = new SbiGeoFeatures();
	feature.setFeatureId(id);
	feature.setName(name);
	feature.setDescr(description);
	feature.setType(type);
	
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
	return feature;
	}
		
}