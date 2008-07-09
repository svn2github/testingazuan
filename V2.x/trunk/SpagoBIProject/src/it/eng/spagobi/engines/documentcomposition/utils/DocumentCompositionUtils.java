/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubreportDAO;
import it.eng.spagobi.analiticalmodel.document.dao.IViewpointDAO;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration.Document;
import it.eng.spagobi.engines.drivers.IEngineDriver;

import java.util.ArrayList;
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
	 * 
	 * @param objLabel the logical label of the document (gets from the template file)
	 * @param sessionContainer session object
	 * @param requestSB request object
	 * 
	 * @return String the complete url. It use this format: <code_error>|<url>. If there is an error during the execution <code_error> is valorized and url is null, else it is null and the url is complete.
	 */
	public static String getEngineUrl(String objLabel, SessionContainer sessionContainer, SourceBean requestSB) {
		String urlReturn = "";
		BIObject obj = null;
		Engine engine = null;
		
		if (objLabel == null || objLabel.equals("")){
			logger.error("Object Label is null: cannot get engine's url.");
			return "1008|";
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
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1005", l, messageBundle);
			}
			engine = obj.getEngine();
		
			// get the initial url of the engine
		    urlReturn = engine.getUrl()+"?userId="+profile.getUserUniqueIdentifier()+"&amp;"+SpagoBIConstants.SBI_CONTEXT+"="+GeneralUtilities.getSpagoBiContext()+"&amp;"+SpagoBIConstants.SBI_BACK_END_HOST+"="+GeneralUtilities.getSpagoBiHostBackEnd()+"&amp;"+SpagoBIConstants.SBI_HOST+"="+GeneralUtilities.getSpagoBiHost();
		}
		catch (Exception ex) {		
			logger.error("Cannot obtain engine url. Document with label " + objLabel +" doesn't exist into database." , ex);		
			return "1005|";
		}	
		
		// GET THE TYPE OF ENGINE (INTERNAL / EXTERNAL) AND THE SUITABLE BIOBJECT TYPES
		Domain engineType = null;
		Domain compatibleBiobjType = null;
		try {
			engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
			compatibleBiobjType = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		} catch (EMFUserError error) {
			 logger.error("Error retrieving document's engine information", error);
			 return "1009|";
		} catch (Exception error) {
			 logger.error("Error retrieving document's engine information", error);
			 return "1009|";
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
			return "2002|";
		}
		
		// IF USER CAN'T EXECUTE THE OBJECT RETURN
		if (!canExecute(profile, obj)) return "1010|"; 
		
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
			return "1002|"; 
		}
			
	    
		// IF THE ENGINE IS EXTERNAL
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			try {
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			    // get the map of the parameters
				Map mapPars = null;
				
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
							String name = param.substring(0, param.indexOf("%3D"));
							String value = param.substring(param.indexOf("%3D")+3);
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
				urlReturn = engine.getUrl()+"?"+SpagoBIConstants.SBI_CONTEXT+"="+GeneralUtilities.getSpagoBiContext()+"&amp;"+SpagoBIConstants.SBI_BACK_END_HOST+"="+GeneralUtilities.getSpagoBiHostBackEnd()+"&amp;"+SpagoBIConstants.SBI_HOST+"="+GeneralUtilities.getSpagoBiHost();
				
			    Set parKeys = mapPars.keySet();
				Iterator parKeysIter = parKeys.iterator();
				while(parKeysIter.hasNext()) {
		     	   	String parkey = parKeysIter.next().toString();
		     	   	String parvalue = mapPars.get(parkey).toString();
		     	   	if(parkey.equalsIgnoreCase("template"))
		     	   		continue;
		     	
		     	   	urlReturn += "&amp;"+parkey+"="+parvalue;
				}
		     	
		     	urlReturn += getParametersUrl(obj, document, requestSB, false);
		     	urlReturn += "&amp;DOCUMENT_LABEL="+document.getLabel();
				
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
				return "2001|";
			} catch (Exception e) {
				logger.error("Error while instantiating class " + className, e);
				return "100|";
			}
			
			logger.debug("Class " + className + " instantiated successfully. Now engine's execution starts.");
			
			
			urlReturn = GeneralUtilities.getSpagoBiHost() + GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?USERNAME="+(String)profile.getUserUniqueIdentifier()+
						"&amp;PAGE=DirectExecutionPage&amp;DOCUMENT_LABEL="+obj.getLabel()+"&amp;DOCUMENT_PARAMETERS=";
			
			urlReturn += getParametersUrl(obj, document, requestSB, true);
			
			
		}
		
		//if into url therisn't yet document id, adds it
		if (urlReturn.indexOf("document=") < 0){
			if (urlReturn.endsWith("DOCUMENT_PARAMETERS="))
				urlReturn += "%26document%3D" + obj.getId();
			else
				urlReturn += "&amp;document=" + obj.getId();
		}
		//set EXECUTION_CONTEXT (only for documentcomposition docs)
		urlReturn += "&amp;" + SpagoBIConstants.EXECUTION_CONTEXT + "=" + SpagoBIConstants.DOCUMENT_COMPOSITION +"&amp;";
	
		//prepares and sets parameters value into session
		HashMap parValueDoc = getAllParamsValue(urlReturn);
		sessionContainer.setAttribute(document.getLabel(), parValueDoc);
		//adds '|' char for management error into jsp if is necessary.
		return "|"+urlReturn;
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

	/**
	 * Return a string representative an url with all parameters set with a request value (if it is present) or
	 * with the default's value.
	 * @param doc the document object that is managed
	 * @param document the document configurator
	 * @param requestSB the request object
	 * @return a string with the url completed
	 */
	private static String getParametersUrl(BIObject doc, Document document, SourceBean requestSB, boolean forInternalEngine){
		String paramUrl = "";
		
		//set others parameters value
		Properties lstParams = document.getParams();
		String key = "";
		String value = "";
		int cont = 0;
		//while(enParams.hasMoreElements()) {
		for (int i=0; i<lstParams.size(); i++) {
			String typeParam =  lstParams.getProperty("type_par_"+document.getNumOrder()+"_"+cont);
			//only for parameter in input to the document managed (type equal 'IN')
			if (typeParam != null && typeParam.indexOf("IN")>=0) {
		    	String tmpKey = "sbi_par_label_param_"+document.getNumOrder()+"_"+cont;
	    		key = lstParams.getProperty(tmpKey);
	    		if (key == null) break;
	    		value = (String)requestSB.getAttribute(key);
		    	//if value isn't defined, gets the default value
			    if(value == null || value.equals("")){
				    value = lstParams.getProperty(("default_value_param_"+document.getNumOrder()+"_"+cont));
		    	}
			    if (value.equals("%")) value = "%25";
			    
			    if (forInternalEngine && value != null && !value.equals(""))
			    	paramUrl += "%26" + key + "%3D" + value;
			    else if (!forInternalEngine && value != null && !value.equals(""))
			    	paramUrl += "&amp;" + key + "=" + value;
			    cont++;
			}
	    }
		/*
		if (forInternalEngine)
			paramUrl = paramUrl.substring(0, paramUrl.length()-3); 
		else
			paramUrl = paramUrl.substring(0, paramUrl.length()-5); 
		*/
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
	 * Return an hashmap of all parameters for the document managed
	 * @param urlReturn String with url and parameters
	 * @return HashMap 
	 */
	private static HashMap getAllParamsValue(String urlReturn){
		HashMap retHM = new HashMap();
		String tmpStr = urlReturn.substring(urlReturn.indexOf("?")+1);
		String tmpStrDirect = "";
		if (urlReturn.indexOf("DirectExecutionPage")>0){
			String[] tmpArr = tmpStr.split("&amp;");
			for (int i=0; i<tmpArr.length; i++){
				String strPar = (String)tmpArr[i];
				String key = strPar.substring(0,strPar.indexOf("="));
				String value = strPar.substring(strPar.indexOf("=")+1);
				if (key.equalsIgnoreCase("DOCUMENT_PARAMETERS"))
					tmpStrDirect = value.substring(3);
				else
					retHM.put(key, value);
				tmpStr = tmpStr.substring(tmpStr.indexOf((String)tmpArr[i])+((String)tmpArr[i]).length()+5);
			}
			
			String[] tmpArrDirect = tmpStrDirect.split("%26");
			for (int i=0; i<tmpArrDirect.length; i++){
				String strPar = (String)tmpArrDirect[i];
				int pos = strPar.indexOf("%3D");
				String key = strPar.substring(0,strPar.indexOf("%3D"));
				String value = strPar.substring(strPar.indexOf("%3D")+3);
				retHM.put(key, value);
			}
		}
		else{
			String[] tmpArr = tmpStr.split("&amp;");
			for (int i=0; i<tmpArr.length; i++){
				String strPar = (String)tmpArr[i];
				String key = strPar.substring(0,strPar.indexOf("="));
				String value = strPar.substring(strPar.indexOf("=")+1);
				retHM.put(key, value);
			}
		}
		
		return retHM;
	}
}
