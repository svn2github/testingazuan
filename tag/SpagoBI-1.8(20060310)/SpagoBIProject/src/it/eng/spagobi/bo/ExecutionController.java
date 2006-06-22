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
            if(par==null)
            	continue;
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
	
	
	
	
	/**
	 * @param aSessionContainer
	 * @param aPath
	 * @param aRoleName
	 * @throws EMFUserError
	 */
	public BIObject prepareBIObjectInSession(SessionContainer aSessionContainer, String aPath, 
					String aRoleName) throws EMFUserError {
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForExecutionByPathAndRole(aPath, aRoleName);
		aSessionContainer.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
		SessionContainer permanentSession = aSessionContainer.getPermanentContainer();
		String serviceName = "ValidateExecuteBIObjectPage";
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
			conditionsSrt += "	<PARAMETER name=\"LOOKUP_PARAMETER_ID\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
			conditionsSrt += "	<PARAMETER name=\"LOOKUP_VALUE\" scope=\"SERVICE_REQUEST\" value=\"AF_NOT_DEFINED\" />";
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
		
		List checks = aBIObjectParameter.getParameter().getChecks();
		if (checks == null || checks.size() == 0){
			return null;
		}else{
			Iterator it = checks.iterator();
			SourceBean sb = new SourceBean("FIELD");
			sb.setAttribute("name", aBIObjectParameter.getParameterUrlName());
			sb.setAttribute("label", aBIObjectParameter.getLabel());
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
