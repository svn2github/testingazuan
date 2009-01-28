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
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.container.ContextManager;
import it.eng.spagobi.container.SpagoBISessionContainer;
import it.eng.spagobi.container.strategy.LightNavigatorContextRetrieverStrategy;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
	public static String getExecutionUrl(String objLabel, SessionContainer sessionContainer, SourceBean requestSB) {
		logger.debug("IN");
		 // get the user profile from session
		SessionContainer permSession = sessionContainer.getPermanentContainer();
	    IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	    String urlReturn = GeneralUtilities.getSpagoBIProfileBaseUrl(((UserProfile)profile).getUserId().toString()) + 
		//String urlReturn = GeneralUtilities.getSpagoBIProfileBaseUrl(((UserProfile)profile).getUserUniqueIdentifier().toString()) + 
				"&PAGE=ExecuteBIObjectPage&" + SpagoBIConstants.IGNORE_SUBOBJECTS_VIEWPOINTS_SNAPSHOTS + "=true&"
				+ ObjectsTreeConstants.OBJECT_LABEL + "=" + objLabel + "&" 
				+ ObjectsTreeConstants.MODALITY + "=" + SpagoBIConstants.DOCUMENT_COMPOSITION;
	    // get the execution role
	    ContextManager contextManager = new ContextManager(new SpagoBISessionContainer(sessionContainer), 
				new LightNavigatorContextRetrieverStrategy(requestSB));
	    ExecutionInstance instance = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
		String executionRole = instance.getExecutionRole();
		urlReturn += "&" + SpagoBIConstants.ROLE + "=" + executionRole;
		// identity string for context
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateRandomBasedUUID();
	    urlReturn += "&" + LightNavigationManager.LIGHT_NAVIGATOR_ID + "=" + uuid.toString();

		//get object configuration
		DocumentCompositionConfiguration docConfig = null;
	    docConfig = (DocumentCompositionConfiguration) contextManager.get("docConfig");
	    
	    Document document = null;
		//get correct document configuration
		List lstDoc = docConfig.getLabelsArray();
		boolean foundDoc = false;
		for (int i = 0; i < lstDoc.size(); i++){
			document = (Document)docConfig.getDocument((String)lstDoc.get(i)); 
			if (document != null){
				if (!objLabel.equalsIgnoreCase(document.getSbiObjLabel()))
					continue;
				else{
					foundDoc = true;
					break;
				}
			}
		}
		if (!foundDoc){
			List l = new ArrayList();
			l.add(objLabel);
			EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 1079, l);
			logger.error("The object with label " + objLabel + " hasn't got a document into template" );
			return "1002|"; 
		}
		urlReturn += getParametersUrl(document, requestSB, instance);
		//adds '|' char for management error into jsp if is necessary.
		logger.debug("urlReturn: " + "|"+urlReturn);
		logger.debug("OUT");
		return "|"+urlReturn;
	}

	/**
	 * Return a string representative an url with all parameters set with a request value (if it is present) or
	 * with the default's value.
	 * @param doc the document object that is managed
	 * @param document the document configurator
	 * @param requestSB the request object
	 * @return a string with the url completed
	 */
	private static String getParametersUrl(Document document, SourceBean requestSB, ExecutionInstance instance){
		logger.debug("IN");
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
	    		//if value isn't defined, check if there is a value into the instance(there is when a document is called from a refresh o viewpoint mode) 
	    		if((value == null || value.equals(""))){
	    			value = getInstanceValue(key, instance);
	    		}
	    		//if value isn't defined, gets the default value from the template
			    if(value == null || value.equals("")){
				    value = lstParams.getProperty(("default_value_param_"+document.getNumOrder()+"_"+cont));
		    	}
			    if (value.equals("%")) value = "%25";
			    else if (value.equals(";%")) value = ";%25";
			    if (value != null && !value.equals(""))
			    	paramUrl += "&" + key + "=" + value;
			    cont++;
			}
	    }
		/*
		if (forInternalEngine)
			paramUrl = paramUrl.substring(0, paramUrl.length()-3); 
		else
			paramUrl = paramUrl.substring(0, paramUrl.length()-5); 
		*/
		logger.debug("paramUrl: " + paramUrl);
		logger.debug("OUT");
		return paramUrl;
	}

	/**
	 * Return an hashmap of all parameters for the document managed
	 * @param urlReturn String with url and parameters
	 * @return HashMap 
	 */
	private static HashMap getAllParamsValue(String urlReturn){
		HashMap retHM = new HashMap();
		String tmpStr = urlReturn.substring(urlReturn.indexOf("?")+1);
		String[] tmpArr = tmpStr.split("&");
		for (int i=0; i<tmpArr.length; i++){
			String strPar = (String)tmpArr[i];
			String key = strPar.substring(0,strPar.indexOf("="));
			String value = strPar.substring(strPar.indexOf("=")+1);
			retHM.put(key, value);
		}
		return retHM;
	}
	
	private static String getInstanceValue(String key, ExecutionInstance instance){
		String retVal = "";
		BIObject obj = instance.getBIObject();
		List objPars = obj.getBiObjectParameters();
		
		for (int i=0; i < objPars.size(); i++){
			BIObjectParameter objPar = (BIObjectParameter)objPars.get(i);
			if (objPar.getParameterUrlName().equalsIgnoreCase(key)){
				retVal = (objPar.getParameterValues()==null)?"":(String)objPar.getParameterValues().get(0);
				break;
			}
		}
		return retVal;
		
	}
	
}
