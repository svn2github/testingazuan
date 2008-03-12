/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.documentcomposition.utils;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubreportDAO;
import it.eng.spagobi.analiticalmodel.document.dao.IViewpointDAO;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration.Document;
import it.eng.spagobi.engines.drivers.IEngineDriver;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 * Utility Class for document composition
 */

public class DocumentCompositionUtils {
	private static transient Logger logger=Logger.getLogger(DocumentCompositionUtils.class);
	public static final String messageBundle = "component_spagobidocumentcompositionIE_messages";
	
	
	/**
	 * Returns an url for execute the document with the engine associated.
	 * It calls relative driver.
	 * @param objLabel the logical label of the document (gets from the template file)
	 * @param sessionContainer session object
	 * @param requestSB request object
	 * @return String the complete url 
	 */
	public static String getEngineUrl(String objLabel, SessionContainer sessionContainer, SourceBean requestSB) {
		String urlReturn = "";
		BIObject obj = null;
		Engine engine = null;
		
		if (objLabel == null || objLabel.equals("")){
			logger.error("Object Label is null: cannot get engine's url.");
			return null;
		}
		
		 // get the user profile from session
		SessionContainer permSession = sessionContainer.getPermanentContainer();
	    IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	    
	    // get the execution role
		String executionRole = (String)sessionContainer.getAttribute(SpagoBIConstants.ROLE);
		
		 // identity string for object execution
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    String executionId = uuid.toString();
	    executionId = executionId.replaceAll("-", "");

		// GET ENGINE ASSOCIATED TO THE BIOBJECT
		try{
			obj = (BIObject)DAOFactory.getBIObjectDAO().loadBIObjectByLabel(objLabel);
			if (obj == null){
				logger.error("Cannot obtain engine url. Document with label " + objLabel +" doesn't exist into database.");		
				
				List l = new ArrayList();
				l.add(objLabel);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1005, l);
			}
			engine = obj.getEngine();

			// get the initial url of the engine
		    urlReturn = engine.getUrl()+"?userId="+profile.getUserUniqueIdentifier()+"&amp;"+SpagoBIConstants.SBICONTEXTURL+"="+GeneralUtilities.getSpagoBiContextAddress()+"&amp;"+SpagoBIConstants.BACK_END_SBICONTEXTURL+"="+GeneralUtilities.getBackEndSpagoBiContextAddress();
		}
		catch (Exception ex) {		
			logger.error("Cannot obtain engine url " , ex);		
		}	
		
		// GET THE TYPE OF ENGINE (INTERNAL / EXTERNAL) AND THE SUITABLE BIOBJECT TYPES
		Domain engineType = null;
		Domain compatibleBiobjType = null;
		try {
			engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
			compatibleBiobjType = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		} catch (EMFUserError error) {
			 logger.error("Error retrieving document's engine information", error);
			// errorHandler.addError(error);
			 return null;
		}
		String compatibleBiobjTypeCd = compatibleBiobjType.getValueCd();
		String biobjTypeCd = obj.getBiObjectTypeCode();
		
		// CHECK IF THE BIOBJECT IS COMPATIBLE WITH THE TYPES SUITABLE FOR THE ENGINE
		if (!compatibleBiobjTypeCd.equalsIgnoreCase(biobjTypeCd)) {
			// the engine document type and the biobject type are not compatible
			 logger.error("Engine cannot execute input document type: " +
		 				"the engine " + engine.getName() + " can execute '" + compatibleBiobjTypeCd + "' type documents " +
		 						"while the input document is a '" + biobjTypeCd + "'.");
			Vector params = new Vector();
			params.add(engine.getName());
			params.add(compatibleBiobjTypeCd);
			params.add(biobjTypeCd);
			//errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2002, params));
			return null;
		}
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		logger.debug("List subobject loaded: " + subObjects);
		// put in response the list of subobject
		//response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
        
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		logger.debug("List snapshot loaded: " + snapshots);
		// put in response the list of snapshot 
		//response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		
		// get the list of viewpoints
		List viewpoints = getViewpointList(obj, (String)profile.getUserUniqueIdentifier());
		logger.debug("List viewpoint loaded: " + viewpoints);
		// put in response the list of viewpoint 
		//response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);
		
		// IF USER CAN'T EXECUTE THE OBJECT RETURN
		if (!canExecute(profile, obj)) return null; 
		
		//get object configuration
		DocumentCompositionConfiguration docConfig = null;
	    docConfig = (DocumentCompositionConfiguration)sessionContainer.getAttribute("docConfig");
	    
	    Document document = null;
		//get correct document configuration
		List lstDoc = docConfig.getLabelsArray();
		boolean foundDoc = false;
		for (int i = 0; i < lstDoc.size(); i++){
			document = (Document)docConfig.getDocument((String)lstDoc.get(i)); 
			if (document != null){
				if (!obj.getLabel().equalsIgnoreCase(document.getSbiObjLabel()))
					continue;
				else{
					foundDoc = true;
					break;
				}
			}
		}
		if (!foundDoc){
			List l = new ArrayList();
			l.add(obj.getLabel());
			EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 1079, l);
			logger.error("The object with label " + obj.getLabel() + " hasn't got a document into template" );
			return ""; //testare caso per visualizzare msg errore all'interno del framte interessato
		}
			
	    
		// IF THE ENGINE IS EXTERNAL
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			try {
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			    // get the map of the parameters
				Map mapPars = null;
				
				// subObj is not null if specified on preferences
				String subObjectName = (String) sessionContainer.getAttribute("LABEL_SUB_OBJECT");
				SubObject subObj = getRequiredSubObject(obj, subObjects, subObjectName, (String)profile.getUserUniqueIdentifier());
				if(subObj!=null) 
					mapPars = aEngineDriver.getParameterMap(obj, subObj, profile, executionRole);
				else 
					mapPars = aEngineDriver.getParameterMap(obj, profile, executionRole);
				
				// adding or substituting parameters for viewpoint
				String id = (String) requestSB.getAttribute("vpId");
				if (id != null){
					IViewpointDAO VPDAO = DAOFactory.getViewpointDAO();		
					Viewpoint vp =  VPDAO.loadViewpointByID(new Integer(id));
					String[] vpParameters = vp.getVpValueParams().split("%26");
					if (vpParameters != null){
						for (int i=0; i< vpParameters.length; i++){
							String param = (String)vpParameters[i];
							String name = param.substring(0, param.indexOf("="));
							String value = param.substring(param.indexOf("=")+1);
							if (mapPars.get(name) != null){
								mapPars.remove(name);
								mapPars.put(name, value);
							}
							else
								mapPars.put(name, value);
						}
					}
				}

				// complete the url of the external engine
			    urlReturn = engine.getUrl()+"?"+SpagoBIConstants.SBICONTEXTURL+"="+GeneralUtilities.getSpagoBiContextAddress()+"&amp;"+SpagoBIConstants.BACK_END_SBICONTEXTURL+"="+GeneralUtilities.getBackEndSpagoBiContextAddress();
				
			    Set parKeys = mapPars.keySet();
				Iterator parKeysIter = parKeys.iterator();
				while(parKeysIter.hasNext()) {
		     	   	String parkey = parKeysIter.next().toString();
		     	   	String parvalue = mapPars.get(parkey).toString();
		     	   	if(parkey.equalsIgnoreCase("template"))
		     	   		continue;
		     	
		     	   	urlReturn += "&amp;"+parkey+"="+parvalue;
				}
		     	
		     	urlReturn += getParametersUrl(obj, document, requestSB);
				
			} catch (Exception e) {
				 logger.error("Error During object execution", e);
			}	
			
		// IF THE ENGINE IS INTERNAL	
		} else {
			
			String className = engine.getClassName();
			logger.debug("Try instantiating class " + className + " for internal engine " + engine.getName() + "...");
			InternalEngineIFace internalEngine = null;
			// tries to instantiate the class for the internal engine
			try {
				if (className == null && className.trim().equals("")) throw new ClassNotFoundException();
				internalEngine = (InternalEngineIFace) Class.forName(className).newInstance();
			} catch (ClassNotFoundException cnfe) {
				logger.error("The class ['" + className + "'] for internal engine " + engine.getName() + " was not found.", cnfe);
				Vector params = new Vector();
				params.add(className);
				params.add(engine.getName());
				//errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2001, params));
				return null;
			} catch (Exception e) {
				logger.error("Error while instantiating class " + className, e);
				//errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
				return null;
			}
			
			logger.debug("Class " + className + " instantiated successfully. Now engine's execution starts.");
			
			
			urlReturn = GeneralUtilities.getSpagoBiContextAddress() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?USERNAME="+(String)profile.getUserUniqueIdentifier()+
						"&amp;PAGE=DirectExecutionPage&amp;DOCUMENT_LABEL="+obj.getLabel()+"&amp;DOCUMENT_PARAMETERS=";
			
			urlReturn += getParametersUrl(obj, document, requestSB);
			
			
		}
		
		//if into url therisn't yet document id, adds it
		if (urlReturn.indexOf("document=") < 0){
			if (urlReturn.endsWith("DOCUMENT_PARAMETERS="))
				urlReturn += "document=" + obj.getId();
			else
				urlReturn += "&amp;document=" + obj.getId();
		}
		
		//TEST
		//urlReturn += "&amp;";
		urlReturn += "&amp;" + SpagoBIConstants.EXECUTION_CONTEXT + "=" + SpagoBIConstants.DOCUMENT_COMPOSITION +"&amp;";
	
		//prepares and sets parameters value into session
		HashMap parValueDoc = getAllParamsValue(urlReturn);
		sessionContainer.setAttribute(document.getLabel(), parValueDoc);

		return urlReturn;
	}
	
	private static boolean canExecute(IEngUserProfile profile, BIObject biobj) {
		
		Integer masterReportId = biobj.getId();
		String masterReportStatus = biobj.getStateCode();
		
		
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			IBIObjectDAO biobjectdao = DAOFactory.getBIObjectDAO();
			
			List subreportList =  subrptdao.loadSubreportsByMasterRptId(masterReportId);
			for(int i = 0; i < subreportList.size(); i++) {
				Subreport subreport = (Subreport)subreportList.get(i);
				BIObject subrptbiobj = biobjectdao.loadBIObjectForDetail(subreport.getSub_rpt_id());
				if(!isSubRptStatusAdmissible(masterReportStatus, subrptbiobj.getStateCode())) {
					//errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1062));
					return false;							
				}
				if(!isSubRptExecutableByUser(profile, subrptbiobj)) {
					//errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1063)); 
					return false;							
				}
			}			
		} catch (EMFUserError e) {
			logger.warn("Error while reading subreports",
					  e);
			return false;
		}	
		
		return true;
	}

	
	private static String getParametersUrl(BIObject doc, Document document, SourceBean requestSB){
		String paramUrl = "";
		
		
		//set others parameters value
		Properties lstParams = document.getParams();
		Enumeration enParams =lstParams.keys();
		String key = "";
		String value = "";
		int cont = 0;
		while(enParams.hasMoreElements()) {
	    	String tmpKey = "sbi_par_label_param_"+document.getNumOrder()+"_"+cont;
    		key = lstParams.getProperty(tmpKey);
    		if (key == null) break;
	    	//value = lstParams.getProperty(key);
    		value = (String)requestSB.getAttribute(key);
	    	//if value isn't defined, gets the default value
		    if(value == null || value.equals("")){
			    value = lstParams.getProperty(("default_value_param_"+document.getNumOrder()+"_"+cont));
	    	}
		    if (value != null && !value.equals(""))
	    		paramUrl += "&amp;"+ key + "=" + value;
	    	
		    cont++;
	    }

		return paramUrl;
	}

	private static boolean isSubRptStatusAdmissible(String masterRptStatus, String subRptStatus) {
		if(masterRptStatus.equalsIgnoreCase("DEV")) {
			if(subRptStatus.equalsIgnoreCase("DEV") ||
			   subRptStatus.equalsIgnoreCase("REL")) return true;
			else return false;
		}
		else if(masterRptStatus.equalsIgnoreCase("TEST")) {
			if(subRptStatus.equalsIgnoreCase("TEST") ||
			   subRptStatus.equalsIgnoreCase("REL")) return true;
			else return false;
		}
		else if(masterRptStatus.equalsIgnoreCase("REL")) {
			if(subRptStatus.equalsIgnoreCase("REL")) return true;
			else return false;
		}
		return false;
	}
	
	private static boolean isSubRptExecutableByUser(IEngUserProfile profile, BIObject subrptbiobj) {
		String subrptbiobjStatus = subrptbiobj.getStateCode();
		List functionalities = subrptbiobj.getFunctionalities();
		Iterator functionalitiesIt = functionalities.iterator();
		boolean isExecutableByUser = false;
		while (functionalitiesIt.hasNext()) {
			Integer functionalityId = (Integer) functionalitiesIt.next();
			if (ObjectsAccessVerifier.canDev(subrptbiobjStatus, functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
			if (ObjectsAccessVerifier.canTest(subrptbiobjStatus, functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
			if (ObjectsAccessVerifier.canExec(subrptbiobjStatus, functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
		}
		return isExecutableByUser;
	}
	
	/**
	 * Get the list of subObjects of a BIObject for the current user
	 * @param obj BIObject container of the subObjects 
	 * @param profile profile of the user
	 * @return the List of the BIObject's subobjects visible to the current user
	 */
	private static List getSubObjectsList(BIObject obj, IEngUserProfile profile) {
		List subObjects = new ArrayList();
		try {
			ISubObjectDAO subobjdao = DAOFactory.getSubObjectDAO();
			subObjects =  subobjdao.getAccessibleSubObjects(obj.getId(), profile);
		} catch (Exception e) {
			logger.error("Error retriving the subObject list", e);
		}
		return subObjects;
	}
	
	
	
	/**
	 * Get the list of BIObject snapshots 
	 * @param obj BIObject container of the snapshot 
	 * @return the List of the BIObject snapshots 
	 */
	private static List getSnapshotList(BIObject obj) {
		List snapshots = new ArrayList();
		try {
			ISnapshotDAO snapdao = DAOFactory.getSnapshotDAO();
			snapshots =  snapdao.getSnapshots(obj.getId());
		} catch (Exception e) {
			logger.error("Error retriving the snapshot list", e);
		}
		return snapshots;
	}
		
	/**
	 * Get the list of viewpoints 
	 * @param obj BIObject container of the viewpoint
	 * @return the List of the viewpoints 
	 */
	private static List getViewpointList(BIObject obj, String userName) {
		List viewpoints = new ArrayList();
		try {
			IViewpointDAO biVPDAO = DAOFactory.getViewpointDAO();
			viewpoints =  biVPDAO.loadAllViewpointsByObjID(obj.getId());
			//if scope is 'public' or scope is 'private' and user is the owner, then the viewpoint is visualized
			for (int i=0; i<viewpoints.size(); i++){
				Viewpoint vp =(Viewpoint)viewpoints.get(i);
				if (vp.getVpScope().equals(PortletUtilities.getMessage("SBIDev.docConf.viewPoint.scopePrivate", "messages")) &&
				   !vp.getVpOwner().equals(userName))
				viewpoints.remove(i);
			}
		} catch (Exception e) {
			logger.error("Error retriving the viewpoint list", e);
		}
		return viewpoints;
	}
	
	/**
	 * Find the subobject with the name specified by the attribute "LABEL_SUB_OBJECT" on 
	 * SessionContainer. If such a subobject exists and the user can execute it, then it is returned;
	 * if it doesn't exist, null is returned; if it exists but the user is not able to execute it, 
	 * an error is added into the error handler and null is returned.
	 * 
	 * 
	 * @param obj The BIObject being executed
	 * @param subObjects The list of all the document suobjects
	 * @param profile The user profile
	 * @return the subobject to be executed if it exists and the user can execute it
	 */
	private static SubObject getRequiredSubObject(BIObject obj, List subObjects,String subObjectName, String userName) {
		SubObject subObj = null;
		if (subObjects.size() > 0) {
			//String subObjectName = (String) session.getAttribute("LABEL_SUB_OBJECT");
			if (subObjectName != null ) {
				Iterator iterSubs = subObjects.iterator();
				while (iterSubs.hasNext() && subObj == null) {
					SubObject sd = (SubObject) iterSubs.next();
					if (sd.getName().equals(subObjectName.trim())) {
						subObj = sd;
					}
				}
				// TODO - Error case if not found?
			}
		}
		if (subObj != null) {
			if (!subObj.getIsPublic().booleanValue() && !subObj.getOwner().equals(userName)) {
				List l = new ArrayList();
				l.add(subObj.getName());
				l.add(obj.getName());
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 1079, l);
				//errorHandler.addError(userError);
				return null;
			}
		}
		return subObj;
	}
	
	/**
	 * Return an hashmap of all parameters for the document managed
	 * @param urlReturn String with url and parameters
	 * @return HashMap 
	 */
	private static HashMap getAllParamsValue(String urlReturn){
		HashMap retHM = new HashMap();
		String tmpStr = urlReturn.substring(urlReturn.indexOf("?")+1);
		String[] tmpArr = tmpStr.split("&amp;");
		for (int i=0; i<tmpArr.length; i++){
			String strPar = (String)tmpArr[i];
			String key = strPar.substring(0,strPar.indexOf("="));
			String value = strPar.substring(strPar.indexOf("=")+1);
			retHM.put(key, value);
		}
		
		return retHM;
	}
}
