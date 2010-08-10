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
package it.eng.spagobi.bo;

import groovy.lang.Binding;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExecutionController {

	private BIObject biObject = null;

	
	
	
	public boolean directExecution() {
		
		if(biObject==null) 
			return false;
	    List biParameters = biObject.getBiObjectParameters();
        if(biParameters==null)
        	return false;
        if(biParameters.size()==0) 
        	return true;
        
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
        	if (par == null) {
				SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, 
		 				"ExecuteBIObjectMOdule", 
		 				"directExecution", 
		 				"The biparameter with label = ['" + biParameter.getLabel() + "'] and url name = ['" + biParameter.getParameterUrlName() + "'] has no parameter associated. ");
        		continue;
        	}
//            if(par==null){
//            	if(biParameter.isTransientParmeters())
//            		countHidePar ++;
//            	continue;
//            }

            paruse = par.getModalityValue();
            if(paruse==null)
            	continue;
            typeparuse = paruse.getITypeCd();
		    if(typeparuse.equalsIgnoreCase("SCRIPT")) {
		    	lovprov = paruse.getLovProvider();
		    	try {
		    		scriptDet = ScriptDetail.fromXML(lovprov);
		    	} catch (Exception e) {
		    		continue;
		    	}
		    	if(scriptDet.isSingleValue()) 
		    		countHidePar ++;
		    }
		}
		
        if(countHidePar==biParameters.size())
        	return true;
        else return false;
	}
	
	
	private void refreshParameters(BIObject obj, String userProvidedParametersStr){
		if(userProvidedParametersStr != null) {
			
			List biparameters = obj.getBiObjectParameters();
			String[] userProvidedParameters = userProvidedParametersStr.split("&");
			for(int i = 0; i < userProvidedParameters.length; i++) {
				String[] chunks = userProvidedParameters[i].split("=");
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
					SpagoBITracer.info(ObjectsTreeConstants.NAME_MODULE, 
	 				"ExecuteBIObjectMOdule", 
	 				"refreshParameters", 
	 				"No BIObjectParameter with url name = ['" + parUrlName + "'] was found.");
					continue;
				}
				String parValue = chunks[1];
				List parameterValues = new ArrayList();
				parameterValues.add(parValue);
				biparameter.setParameterValues(parameterValues);
				biparameter.setTransientParmeters(true);
			}
			
			
//			List parameterList = obj.getBiObjectParameters();
//			BIObjectParameter parameter = null;
//			Map paramMap = new HashMap();
//			for(int i = 0; i < parameterList.size(); i++) {
//				parameter = (BIObjectParameter)parameterList.get(i);
//				paramMap.put(parameter.getParameterUrlName(), parameter);
//				SpagoBITracer.info(ObjectsTreeConstants.NAME_MODULE, 
//		 				"ExecuteBIObjectMOdule", 
//		 				"refreshParameters", 
//		 				"Parameter [IN]: " + parameter);
//			}
//			
//			parameterList.clear();
//					
//			String[] userProvidedParameters = userProvidedParametersStr.split("&");
//			for(int i = 0; i < userProvidedParameters.length; i++) {
//				String[] chunks = userProvidedParameters[i].split("=");
//				parameter = new BIObjectParameter();
//				parameter.setParameterUrlName(chunks[0]);
//				List parameterValues = new ArrayList();
//				parameterValues.add(chunks[1]);
//				parameter.setParameterValues(parameterValues);
//				parameter.setTransientParmeters(true);
//				paramMap.put(parameter.getParameterUrlName(), parameter);
//				SpagoBITracer.info(ObjectsTreeConstants.NAME_MODULE, 
//		 				"ExecuteBIObjectMOdule", 
//		 				"refreshParameters", 
//		 				"Parameter [NEW]: " + parameter);
//			}
//			
//			Iterator it = paramMap.entrySet().iterator();
//			while(it.hasNext()){
//				parameter = (BIObjectParameter)((Map.Entry)it.next()).getValue();
//				parameterList.add(parameter);
//				SpagoBITracer.info(ObjectsTreeConstants.NAME_MODULE, 
//		 				"ExecuteBIObjectMOdule", 
//		 				"refreshParameters", 
//		 				"Parameter [FINISH]: " + parameter);
//			}
//			
//			obj.setBiObjectParameters(parameterList);
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
				// if the value of the parameter is retrived with a script
				// control if the script return an unique value and preload it
				Parameter par = aBIObjectParameter.getParameter();
				if(par != null) {
					ModalitiesValue paruse = par.getModalityValue();
					String type = paruse.getITypeCd();
					if(type.equalsIgnoreCase("SCRIPT")) {
						String lovProv = paruse.getLovProvider();
						ScriptDetail scriptdet = ScriptDetail.fromXML(lovProv);
						if(scriptdet.isSingleValue()) {
							String script = scriptdet.getScript();
							IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
							Binding bind = GeneralUtilities.fillBinding(profile);
							String value = GeneralUtilities.testScript(script, bind);
							List biparvals = new ArrayList();
							biparvals.add(value);
							aBIObjectParameter.setParameterValues(biparvals);
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
		
		if(aBIObjectParameter.isTransientParmeters()) return null;
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
	
	
}
