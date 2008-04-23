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
package it.eng.spagobi.analiticalmodel.document.handlers;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.util.JavaScript;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.bo.ScriptDetail;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ExecutionController {

	static private Logger logger = Logger.getLogger(ExecutionController.class);
	
	private BIObject biObject = null;
	private Map lovResultMap = new HashMap();
	
	
	
	public boolean directExecution() {
		
		if(biObject == null) return false;
	    
		List biParameters = biObject.getBiObjectParameters();
        if(biParameters == null) return false;
        if(biParameters.size() == 0)return true;
        
        int countHidePar = 0;
        Iterator iterPars = biParameters.iterator();
		
        BIObjectParameter biParameter = null;
        ModalitiesValue paruse= null;
        String typeparuse = null;
        String lovprov = null;
        ScriptDetail scriptDet = null;
        
        while (iterPars.hasNext()){
			biParameter = (BIObjectParameter)iterPars.next();
            Parameter par = biParameter.getParameter();
            
        	if(biParameter.isTransientParmeters()) {
        		countHidePar ++;
            	continue;
        	}
        	
        	if(biParameter.hasValidValues()) {
        		countHidePar ++;
            	continue;
        	}
        	
        	if (par == null) {
				SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
		 				"ExecuteBIObjectMOdule", 
		 				"directExecution", 
		 				"The biparameter with label = ['" + biParameter.getLabel() + "'] and url name = ['" + biParameter.getParameterUrlName() + "'] has no parameter associated. ");
        		continue;
        	}

        	if(biParameter.getLovResult() == null) continue;
        	LovResultHandler lovResultHandler;
			try {
				lovResultHandler = new LovResultHandler(biParameter.getLovResult());
				if(lovResultHandler.isSingleValue()) countHidePar ++;
			} catch (SourceBeanException e) {
				continue;
			}
        	
		}
		
        if(countHidePar==biParameters.size())
        	return true;
        else return false;
	}
	
		
	public void refreshParameters(BIObject obj, String userProvidedParametersStr){
		if(userProvidedParametersStr != null) {
			List biparameters = obj.getBiObjectParameters();
			if(biparameters == null) {
				try{
					IBIObjectParameterDAO pardao = DAOFactory.getBIObjectParameterDAO();
				    biparameters = pardao.loadBIObjectParametersById(obj.getId());
				} catch(Exception e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
							            "refreshParameters", "Error while loading biparameters of the biobject with id " + obj.getId());
					return;
				}
			}
			userProvidedParametersStr = JavaScript.unescape(userProvidedParametersStr);
			String[] userProvidedParameters = userProvidedParametersStr.split("&");
			for(int i = 0; i < userProvidedParameters.length; i++) {
				String[] chunks = userProvidedParameters[i].split("=");
				if (chunks == null || chunks.length > 2) {
					SpagoBITracer.warning(ObjectsTreeConstants.NAME_MODULE, 
			 				this.getClass().getName(), 
			 				"refreshParameters", 
			 				"User provided parameter [" + userProvidedParameters[i] + "] cannot be splitted in " +
			 						"[parameter url name=parameter value] by '=' characters.");
					continue;
				}
				String parUrlName = chunks[0];
				if (parUrlName == null || parUrlName.trim().equals("")) continue;
				BIObjectParameter biparameter = null;
				Iterator it = biparameters.iterator();
				while (it.hasNext()) {
					BIObjectParameter temp = (BIObjectParameter) it.next();
					if (temp.getParameterUrlName().equals(parUrlName)) {
						biparameter = temp;
						break;
					}
				}
				if (biparameter == null) {
					SpagoBITracer.info(ObjectsTreeConstants.NAME_MODULE, this.getClass().getName(), 
	 				                   "refreshParameters", "No BIObjectParameter with url name = ['" + parUrlName + "'] was found.");
					continue;
				}
				// if the user specified the parameter value it is considered, elsewhere an empty String is considered
				String parValue = "";
				if (chunks.length == 2) {
					parValue = chunks[1];
				}
				if (parValue != null && parValue.equalsIgnoreCase("NULL")) {
					biparameter.setParameterValues(null);
				} else {
					List parameterValues = new ArrayList();
					parameterValues.add(parValue);
					biparameter.setParameterValues(parameterValues);
				}
				biparameter.setTransientParmeters(true);
			}
			obj.setBiObjectParameters(biparameters);
		}
	}
	
	
	/**
	 * @param aSessionContainer
	 * @param aPath
	 * @param aRoleName
	 * @throws EMFUserError
	 */
	public BIObject prepareBIObjectInSession(SessionContainer aSessionContainer, Integer id, 
					String aRoleName, String userProvidedParametersStr) throws EMFUserError {
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForExecutionByIdAndRole(id, aRoleName);
		IEngUserProfile profile = (IEngUserProfile)aSessionContainer.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		
		refreshParameters(obj, userProvidedParametersStr);
		aSessionContainer.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
		SessionContainer permanentSession = aSessionContainer.getPermanentContainer();
		String serviceName = "VALIDATEEXECUTEBIOBJECTPAGE";
		String validationRulesName = "VALIDATE_PAGE_" + serviceName;
		List tmpBIObjectParameters = obj.getBiObjectParameters();
		SourceBean internalValidationSourceBean = null;
		SourceBean fieldsContainerSourceBean  = null;
		SourceBean fieldSourceBean = null;
		SourceBean dynValidations = null;
		Iterator it = tmpBIObjectParameters.iterator();
		try{
			dynValidations = new SourceBean("DYN_VALIDATIONS");
			internalValidationSourceBean = new SourceBean("VALIDATION");
			internalValidationSourceBean.setAttribute("blocking","false");
			String conditionsSrt = "<CONDITIONS>";
			conditionsSrt += "	<PARAMETER name=\"LOOKUP_OBJ_PAR_ID\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
			conditionsSrt += "	<PARAMETER name=\"LOOKUP_PARAMETER_NAME\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
			conditionsSrt += "	<PARAMETER name=\"MESSAGE\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
			conditionsSrt += "	<PARAMETER name=\"valueFilter\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
			conditionsSrt += "	<PARAMETER name=\"REFRESH_CORRELATION\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
			conditionsSrt += "</CONDITIONS>";
			SourceBean conditions = SourceBean.fromXMLString(conditionsSrt);
			if(conditions!=null) {
				internalValidationSourceBean.setAttribute(conditions);
			} else {
				internalValidationSourceBean.setAttribute(new SourceBean("CONDITIONS"));
			}
			fieldsContainerSourceBean = new SourceBean("FIELDS");
			BIObjectParameter aBIObjectParameter = null;
			while (it.hasNext()){
				aBIObjectParameter = (BIObjectParameter)it.next();
				// check if the script return an unique value and preload it
				Parameter par = aBIObjectParameter.getParameter();
				if(par != null) {
					ModalitiesValue paruse = par.getModalityValue();
					if (!paruse.getITypeCd().equals("MAN_IN")) {					
						try {
				        	String lovResult = aBIObjectParameter.getLovResult();
				        	if(lovResult == null) {
				        		String lovprov = paruse.getLovProvider();
				            	ILovDetail lovDetail = LovDetailFactory.getLovFromXML(lovprov);
				    			lovResult = lovDetail.getLovResult(profile);
				    			LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
				    			aBIObjectParameter.setLovResult(lovResult);
				    			// if the lov is single value and the parameter value is not set, the parameter value 
				    			// is the lov result
				    			if(lovResultHandler.isSingleValue() && aBIObjectParameter.getParameterValues() == null)
				    				aBIObjectParameter.setParameterValues(lovResultHandler.getValues(lovDetail.getValueColumnName()));
				        	}        	       
			        	} catch (Exception e1) {
			        		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			        				            "prepareBIObjectInSession", "Error while loading lov values", e1);
							continue;
						}
					}
				}
				fieldSourceBean = createValidableFieldSourceBean(aBIObjectParameter);
				if (fieldSourceBean == null){
					SpagoBITracer.info(ObjectsTreeConstants.NAME_MODULE, 
			 				"ExecuteBIObjectMOdule", 
			 				"execute", 
			 				"No Checks associated with Parameter" + aBIObjectParameter.getParameterUrlName());
				}else{
					fieldsContainerSourceBean.setAttribute(fieldSourceBean);
				}
			}
			internalValidationSourceBean.setAttribute(fieldsContainerSourceBean);
			dynValidations.setAttribute(internalValidationSourceBean);
			aSessionContainer.setAttribute(validationRulesName,dynValidations);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	
	/**
	 * Creates the XML dinamic validator, according to checks
	 * 
	 * 
	 * @param aBIObjectParameter The input BI object parameter
	 * @return The output Source Bean Containing XML
	 * @throws SourceBeanException If any Exception occurred
	 */
	public SourceBean createValidableFieldSourceBean(BIObjectParameter aBIObjectParameter) throws SourceBeanException {
		
		//if(aBIObjectParameter.isTransientParmeters()) return null;
		List checks = aBIObjectParameter.getParameter().getChecks();
		if (checks == null || checks.size() == 0){
			return null;
		}else{
			Iterator it = checks.iterator();
			SourceBean sb = new SourceBean("FIELD");
			sb.setAttribute("name", aBIObjectParameter.getParameterUrlName());
			sb.setAttribute("label", aBIObjectParameter.getLabel());
			
			
			if(aBIObjectParameter.getParameter().getModalityValue().isMultivalue()){
				sb.setAttribute("multivalues", "true");	
				sb.setAttribute("separator", ";");	
			}
			else {
				sb.setAttribute("multivalues", "false");	
			}
			
			Check check = null;
			SourceBean validatorSourceBean = null;
			while (it.hasNext()){
			check = (Check)it.next();
			validatorSourceBean = new SourceBean("VALIDATOR");
			if (check.getValueTypeCd().equalsIgnoreCase("MANDATORY")){
				validatorSourceBean.setAttribute("validatorName", "MANDATORY");
			} else if (check.getValueTypeCd().equalsIgnoreCase("LETTERSTRING")){
				validatorSourceBean.setAttribute("validatorName", "LETTERSTRING");
			} else if (check.getValueTypeCd().equalsIgnoreCase("ALFANUMERIC")){
				validatorSourceBean.setAttribute("validatorName", "ALFANUMERIC");
			} else if (check.getValueTypeCd().equalsIgnoreCase("NUMERIC")){
				validatorSourceBean.setAttribute("validatorName", "NUMERIC");
			} else if (check.getValueTypeCd().equalsIgnoreCase("EMAIL")){
				validatorSourceBean.setAttribute("validatorName", "EMAIL");
			} else if (check.getValueTypeCd().equalsIgnoreCase("FISCALCODE")){
				validatorSourceBean.setAttribute("validatorName", "FISCALCODE");
			} else if (check.getValueTypeCd().equalsIgnoreCase("INTERNET ADDRESS")){
				validatorSourceBean.setAttribute("validatorName", "URL");
			} else if (check.getValueTypeCd().equalsIgnoreCase("DECIMALS")){
				validatorSourceBean.setAttribute("arg0", check.getFirstValue());
				validatorSourceBean.setAttribute("arg1", check.getSecondValue());
				validatorSourceBean.setAttribute("validatorName", "DECIMALS");
			} else if (check.getValueTypeCd().equalsIgnoreCase("RANGE")){
				validatorSourceBean.setAttribute("arg0", check.getFirstValue());
				validatorSourceBean.setAttribute("arg1", check.getSecondValue());
				
				if (aBIObjectParameter.getParameter().getType().equalsIgnoreCase("DATE")){
					// In a Parameter where parameterType == DATE the mask represent the date format
					validatorSourceBean.setAttribute("arg2", aBIObjectParameter.getParameter().getMask());
					validatorSourceBean.setAttribute("validatorName", "DATERANGE");
				}else if (aBIObjectParameter.getParameter().getType().equalsIgnoreCase("NUM")){
					// In a Parameter where parameterType == NUM the mask represent the decimal format
					validatorSourceBean.setAttribute("arg2", aBIObjectParameter.getParameter().getMask());
					validatorSourceBean.setAttribute("validatorName", "NUMERICRANGE");
				}else if (aBIObjectParameter.getParameter().getType().equalsIgnoreCase("STRING")){
					validatorSourceBean.setAttribute("validatorName", "STRINGRANGE");
				}
			} else if (check.getValueTypeCd().equalsIgnoreCase("MAXLENGTH")){
				validatorSourceBean.setAttribute("arg0", check.getFirstValue());
				validatorSourceBean.setAttribute("validatorName", "MAXLENGTH");
			} else if (check.getValueTypeCd().equalsIgnoreCase("MINLENGTH")){
				validatorSourceBean.setAttribute("arg0", check.getFirstValue());
				validatorSourceBean.setAttribute("validatorName", "MINLENGTH"); 
			} else if (check.getValueTypeCd().equalsIgnoreCase("REGEXP")){
				validatorSourceBean.setAttribute("arg0", check.getFirstValue());
				validatorSourceBean.setAttribute("validatorName", "REGEXP");
			} else if (check.getValueTypeCd().equalsIgnoreCase("DATE")){
				validatorSourceBean.setAttribute("arg0", check.getFirstValue());
				validatorSourceBean.setAttribute("validatorName", "DATE");	
			}
									
			sb.setAttribute(validatorSourceBean);			
		}
		return sb;
		}	
	}
	
	
	
	public BIObject getBiObject() {
		return biObject;
	}

	
	public void setBiObject(BIObject biObject) {
		this.biObject = biObject;
	}


	public void refreshParameters(BIObject biobj, Map confPars) throws Exception {
		logger.debug("IN");
		try {
		    // load the list of parameter of the biobject
		    IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
		    List params = biobjpardao.loadBIObjectParametersById(biobj.getId());
		    logger.debug("biobject parameter list " + params);
		    // for each parameter set the configured value
		    Iterator iterParams = params.iterator();
		    while (iterParams.hasNext()) {
				BIObjectParameter par = (BIObjectParameter) iterParams.next();
				String parUrlName = par.getParameterUrlName();
				logger.debug("processing biparameter with url name " + parUrlName);
				String value = (String) confPars.get(parUrlName);
				logger.debug("usign " + value + " as value for the parameter");
				if (value != null) {
				    List values = new ArrayList();
				    values.add(value);
				    par.setParameterValues(values);
				    logger.debug("parameter value set");
				}
		    }
		    // set the parameters into the biobject
		    biobj.setBiObjectParameters(params);
		} finally {
			logger.debug("OUT");
		}
	}
	
	
}
