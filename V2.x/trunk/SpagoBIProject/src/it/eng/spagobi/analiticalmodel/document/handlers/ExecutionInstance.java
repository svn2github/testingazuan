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
package it.eng.spagobi.analiticalmodel.document.handlers;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.util.JavaScript;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.validation.SpagoBIValidationImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * This class represents a document execution instance.
 * This contains the following attributes:
 * 1. execution flow id: it is the id of an execution flow (execution in cross navigation mode share the same flow id)
 * 2. execution id: single execution id, it is unique for a single execution
 * 3. the BIObject being executed
 * 4. the execution role
 * 4. the execution modality
 * 
 * @author zerbetto
 *
 */
public class ExecutionInstance {
	
	static private Logger logger = Logger.getLogger(ExecutionInstance.class);
	
	private String flowId = null;
	private String executionId = null;
	private BIObject object = null;
	private String executionRole = null;
	private String executionModality = null;
	private IEngUserProfile userProfile = null;
	private boolean displayToolbar = true;
	private boolean displaySliders = true;
	private Calendar calendar = null;
	
	
	/**
     * Instantiates a new execution instance.
     * 
     * @param flowId the flow id
     * @param executionId the execution id
     * @param obj the obj
     * @param executionRole the execution role
	 * @throws Exception 
     */
    public ExecutionInstance (IEngUserProfile userProfile, String flowId, String executionId, Integer biobjectId, String executionRole, String executionModality) throws Exception {
    	logger.debug("IN: input parameters: userProfile = [" + userProfile + "]; flowId = [" + flowId + "]; executionId = [" + executionId + "]; " +
    			"biobjectId" + biobjectId + "]; executionRole = [" + executionRole + "]");
    	if (userProfile == null || flowId == null || executionId == null || biobjectId == null) {
    		throw new Exception("Invalid arguments.");
    	}
    	this.userProfile = userProfile;
		this.flowId = flowId;
		this.executionId = executionId;
		this.object = DAOFactory.getBIObjectDAO().loadBIObjectForExecutionByIdAndRole(biobjectId, executionRole);
		this.calendar = new GregorianCalendar();
		this.executionRole = executionRole;
		this.executionModality = (executionModality == null) ? SpagoBIConstants.NORMAL_EXECUTION_MODALITY : executionModality;
		initBIParameters();
    }
	
    public ExecutionInstance (IEngUserProfile userProfile, String flowId, String executionId, Integer biobjectId, 
    		String executionRole, String executionModality, boolean displayToolbar) throws Exception {
    	this(userProfile, flowId, executionId, biobjectId, executionRole, executionModality);
    	this.displayToolbar = displayToolbar;
    }
    
    public ExecutionInstance (IEngUserProfile userProfile, String flowId, String executionId, Integer biobjectId, 
    		String executionRole, String executionModality, boolean displayToolbar, boolean displaySliders) throws Exception {
    	this(userProfile, flowId, executionId, biobjectId, executionRole, executionModality, displayToolbar);
    	this.displaySliders = displaySliders;
    }
    
    public void changeExecutionRole(String newRole) throws Exception {
    	logger.debug("IN");
    	List correctExecutionRoles = loadCorrectRolesForExecution();
    	if (!correctExecutionRoles.contains(newRole)) {
    		throw new Exception("The role [" + newRole + "] is not a valid role for executing document [" + object.getLabel() + "].");
    	}
    	// reload the biobject
    	this.object = DAOFactory.getBIObjectDAO().loadBIObjectForExecutionByIdAndRole(object.getId(), newRole);
		// generates a new execution id
    	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuidObj = uuidGen.generateTimeBasedUUID();
		String executionId = uuidObj.toString();
		this.executionId = executionId.replaceAll("-", "");
		this.calendar = new GregorianCalendar();
		initBIParameters();
    	logger.debug("OUT");
    }
    
	private void initBIParameters() {
		logger.debug("IN");
		List tmpBIObjectParameters = object.getBiObjectParameters();
		Iterator it = tmpBIObjectParameters.iterator();
		BIObjectParameter aBIObjectParameter = null;
		while (it.hasNext()){
			aBIObjectParameter = (BIObjectParameter) it.next();
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
			    			lovResult = lovDetail.getLovResult(this.userProfile);
			    			LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
			    			aBIObjectParameter.setLovResult(lovResult);
			    			// if the lov is single value and the parameter value is not set, the parameter value 
			    			// is the lov result
			    			if(lovResultHandler.isSingleValue() && aBIObjectParameter.getParameterValues() == null) {
			    				aBIObjectParameter.setParameterValues(lovResultHandler.getValues(lovDetail.getValueColumnName()));
			    				aBIObjectParameter.setHasValidValues(true);
			    				aBIObjectParameter.setTransientParmeters(true);
			    			}
			        	}        	       
		        	} catch (Exception e) {
		        		logger.error(e);
						continue;
					}
				}
			}
		}
		logger.debug("OUT");
	}
	
	private List loadCorrectRolesForExecution() throws EMFInternalError, EMFUserError {
		logger.debug("IN");
		List correctRoles = null;
		if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)
				|| userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_USER)
				|| userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN))
			correctRoles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(object.getId(), userProfile);
		else
			correctRoles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(object.getId());
		logger.debug("OUT");
		return correctRoles;
	}
	
	public boolean isDirectExecution() {
		logger.debug("IN");
		if (object == null) {
			logger.error("No object is set into this ExecutionInstance!!");
			return false;
		}
		List biParameters = object.getBiObjectParameters();
        if (biParameters == null) {
        	logger.error("BIParameters list cannot be null!!!");
        	return false;
        }
        if (biParameters.size() == 0) {
        	logger.debug("BIParameters list is empty.");
        	return true;
        }
        int countHidePar = 0;
        Iterator iterPars = biParameters.iterator();
        BIObjectParameter biParameter = null;
        while (iterPars.hasNext()){
			biParameter = (BIObjectParameter)iterPars.next();
            Parameter par = biParameter.getParameter();
        	if (biParameter.isTransientParmeters()) {
        		countHidePar ++;
            	continue;
        	}
        	if (biParameter.hasValidValues()) {
        		countHidePar ++;
            	continue;
        	}
        	if (par == null) {
				logger.error("The biparameter with label = ['" + biParameter.getLabel() + "'] and url name = ['" + biParameter.getParameterUrlName() + "'] has no parameter associated. ");
        		continue;
        	}
        	if (biParameter.getLovResult() == null) continue;
        	LovResultHandler lovResultHandler;
			try {
				lovResultHandler = new LovResultHandler(biParameter.getLovResult());
				if(lovResultHandler.isSingleValue()) countHidePar ++;
			} catch (SourceBeanException e) {
				continue;
			}
		}
        if (countHidePar == biParameters.size())
        	return true;
        else return false;
	}

	public void setParameterValues(String userProvidedParametersStr, boolean transientMode) {
		logger.debug("IN");
		if (userProvidedParametersStr != null) {
			List biparameters = object.getBiObjectParameters();
			if (biparameters == null) {
				logger.error("BIParameters list cannot be null!!!");
				return;
			}
			userProvidedParametersStr = JavaScript.unescape(userProvidedParametersStr);
			String[] userProvidedParameters = userProvidedParametersStr.split("&");
			for(int i = 0; i < userProvidedParameters.length; i++) {
				String[] chunks = userProvidedParameters[i].split("=");
				if (chunks == null || chunks.length > 2) {
					logger.warn("User provided parameter [" + userProvidedParameters[i] + "] cannot be splitted in " +
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
					logger.warn("No BIObjectParameter with url name = ['" + parUrlName + "'] was found.");
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
					String[] values = parValue.split(";");
					for (int m = 0; m < values.length; m++) {
						parameterValues.add(values[m]);
					}
					biparameter.setParameterValues(parameterValues);
				}
				biparameter.setTransientParmeters(transientMode);
			}
		}
		logger.debug("OUT");
	}
	
	public void refreshParametersValues(SourceBean request, boolean transientMode) throws Exception {
		logger.debug("IN");
		String pendingDelete = (String) request.getAttribute("PENDING_DELETE");
		List biparams = object.getBiObjectParameters();
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			if (pendingDelete != null && !pendingDelete.trim().equals("")) {
				if (isSingleValue(biparam))
					continue;
				biparam.setParameterValues(null);
			} else {
				refreshParameter(biparam, request, transientMode);
			}
		}
		logger.debug("OUT");
	}
	
	private void refreshParameter(BIObjectParameter biparam, SourceBean request, boolean transientMode) {
		logger.debug("IN");
		String nameUrl = biparam.getParameterUrlName();
		List paramAttrsList = request.getAttributeAsList(nameUrl);
		ArrayList paramvalues = new ArrayList();
		if (paramAttrsList.size() == 0)
			return;
		Iterator iterParAttr = paramAttrsList.iterator();
		while (iterParAttr.hasNext()) {
			String values = (String) iterParAttr.next();
			String[] value = values.split(";");
			for (int i = 0; i < value.length; i++) {
				if (!value[i].trim().equalsIgnoreCase(""))
					paramvalues.add(value[i]);
			}
		}
		if (paramvalues.size() == 0)
			biparam.setParameterValues(null);
		else
			biparam.setParameterValues(paramvalues);
		biparam.setTransientParmeters(transientMode);
		logger.debug("OUT");
	}
	
	/**
	 * Checks if is single value.
	 * 
	 * @param biparam the biparam
	 * 
	 * @return true, if is single value
	 */
	private boolean isSingleValue(BIObjectParameter biparam) {
		logger.debug("IN");
		boolean isSingleValue = false;
		try {
			LovResultHandler lovResultHandler = new LovResultHandler(biparam.getLovResult());
			if (lovResultHandler.isSingleValue())
				isSingleValue = true;
		} catch (SourceBeanException e) {
			logger.error("SourceBeanException", e);
		}
		logger.debug("OUT");
		return isSingleValue;
	}
	
	public List getParametersErrors() throws Exception {
		logger.debug("IN");
		List toReturn = new ArrayList();
		List biparams = object.getBiObjectParameters();
		if (biparams.size() == 0)
			return toReturn;
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			List errorsOnChecks = getValidationErrorsOnChecks(biparam);
			toReturn.addAll(errorsOnChecks);
			List errorsOnValues = getValidationErrorsOnValues(biparam);
			toReturn.addAll(errorsOnValues);
			List values = biparam.getParameterValues();
			boolean hasValidValues = false;
			// if parameter has values and there are no errors, the parameter has valid values
			if (values != null && values.size() > 0 && errorsOnChecks.isEmpty() && errorsOnValues.isEmpty()) {
				hasValidValues = true;
			}
			biparam.setHasValidValues(hasValidValues);
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	private List getValidationErrorsOnChecks(BIObjectParameter biparameter) throws Exception {
		logger.debug("OUT");
		List toReturn = new ArrayList();
		List checks = biparameter.getParameter().getChecks();
		String urlName = biparameter.getParameterUrlName();
		String label = biparameter.getLabel();
		List values = biparameter.getParameterValues();
		if (checks == null || checks.size() == 0) {
			logger.debug("No checks associated for biparameter [" + label + "].");
			return toReturn;
		} else {
			Iterator it = checks.iterator();
			Check check = null;
			while (it.hasNext()) {
				check = (Check) it.next();
				if (check.getValueTypeCd().equalsIgnoreCase("MANDATORY")) {
					if (values == null || values.isEmpty()) {
						EMFValidationError error = SpagoBIValidationImpl.validateField(urlName, label, null, "MANDATORY", null, null, null);
						toReturn.add(error);
					} else {
						Iterator valuesIt = values.iterator();
						boolean hasAtLeastOneValue = false;
						while (valuesIt.hasNext()) {
							String aValue = (String) valuesIt.next();
							if (aValue != null && !aValue.trim().equals("")) {
								hasAtLeastOneValue = true;
								break;
							}
						}
						if (!hasAtLeastOneValue) {
							EMFValidationError error = SpagoBIValidationImpl.validateField(urlName, label, null, "MANDATORY", null, null, null);
							toReturn.add(error);
						}
					}
				} else {
					if (values != null && !values.isEmpty()) {
						Iterator valuesIt = values.iterator();
						while (valuesIt.hasNext()) {
							String aValue = (String) valuesIt.next();
							EMFValidationError error = null;
							if (check.getValueTypeCd().equalsIgnoreCase("LETTERSTRING")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "LETTERSTRING", null, null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("ALFANUMERIC")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "ALFANUMERIC", null, null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("NUMERIC")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "NUMERIC", null, null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("EMAIL")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "EMAIL", null, null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("FISCALCODE")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "FISCALCODE", null, null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("INTERNET ADDRESS")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "URL", null, null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("DECIMALS")) {
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "DECIMALS", check.getFirstValue(), check.getSecondValue(), null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("RANGE")) {
								if (biparameter.getParameter().getType().equalsIgnoreCase("DATE")){
									// In a Parameter where parameterType == DATE the mask represent the date format
									error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "DATERANGE", check.getFirstValue(), check.getSecondValue(), biparameter.getParameter().getMask());
								}else if (biparameter.getParameter().getType().equalsIgnoreCase("NUM")){
									// In a Parameter where parameterType == NUM the mask represent the decimal format
									error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "NUMERICRANGE", check.getFirstValue(), check.getSecondValue(), biparameter.getParameter().getMask());
								}else if (biparameter.getParameter().getType().equalsIgnoreCase("STRING")){
									error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "STRINGRANGE", check.getFirstValue(), check.getSecondValue(), null);
								}
							} else if (check.getValueTypeCd().equalsIgnoreCase("MAXLENGTH")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "MAXLENGTH", check.getFirstValue(), null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("MINLENGTH")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "MINLENGTH", check.getFirstValue(), null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("REGEXP")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "REGEXP", check.getFirstValue(), null, null);
							} else if (check.getValueTypeCd().equalsIgnoreCase("DATE")){
								error = SpagoBIValidationImpl.validateField(urlName, label, aValue, "DATE", check.getFirstValue(), null, null);
							}
							toReturn.add(error);
						}
					}
				}
		}
		logger.debug("OUT");
		return toReturn;
		}	
	}
	
	private List getValidationErrorsOnValues(BIObjectParameter biparam) throws Exception {
		List toReturn = new ArrayList();
		// get lov
		ModalitiesValue lov = biparam.getParameter().getModalityValue();
		if (lov.getITypeCd().equals("MAN_IN")) {
			return toReturn;
		}
		
		String parameterValuesDescription = "";
		// get the lov provider detail
		String lovProvider = lov.getLovProvider();
		ILovDetail lovProvDet = LovDetailFactory.getLovFromXML(lovProvider);
		// get lov result
		String lovResult = biparam.getLovResult();
		// get lov result handler
		LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
		List values = biparam.getParameterValues();
		if (values != null && values.size()>0) {
			for (int i = 0; i < values.size(); i++) {
				String value = values.get(i).toString();
				if (!value.equals("") && !lovResultHandler.containsValue(value, lovProvDet
						.getValueColumnName())) {
					logger.error("Parameter '" + biparam.getLabel() + "' cannot assume value '" + value + "'" +
        					" for user '" + this.userProfile.getUserUniqueIdentifier().toString() 
        					+ "' with role '" + this.executionRole + "'.");
					List l = new ArrayList();
					l.add(biparam.getLabel());
					l.add(value);
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 1077, l);
					toReturn.add(userError);
				} else {
					parameterValuesDescription += lovResultHandler.getValueDescription(value, 
							lovProvDet.getValueColumnName(), lovProvDet.getDescriptionColumnName());
					if (i < values.size() - 1) parameterValuesDescription += "; ";
				}
			}
		}
		biparam.setParameterValuesDescription(parameterValuesDescription);
		return toReturn;
	}

	public void eraseParametersValues() {
		logger.debug("OUT");
		List biparams = object.getBiObjectParameters();
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			biparam.setParameterValues(new ArrayList());
			biparam.setParameterValuesDescription("");
			biparam.setHasValidValues(false);
			List values = biparam.getParameterValues();
			if ((values == null) || (values.size() == 0)) {
				ArrayList paramvalues = new ArrayList();
				paramvalues.add("");
				biparam.setParameterValues(paramvalues);
			}
		}
		logger.debug("IN");
	}
	
	/**
     * Gets the execution id.
     * 
     * @return the execution id
     */
    public String getExecutionId() {
		return executionId;
	}

	/**
	 * Gets the flow id.
	 * 
	 * @return the flow id
	 */
	public String getFlowId() {
		return flowId;
	}
	
	/**
	 * Gets the bI object.
	 * 
	 * @return the bI object
	 */
	public BIObject getBIObject() {
		return object;
	}
	
	/**
	 * Gets the calendar.
	 * 
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}
	
	/**
	 * Gets the current execution role.
	 * 
	 * @return the execution role
	 */
	public String getExecutionRole() {
		return executionRole;
	}
	
	/**
	 * Gets the execution modality.
	 * 
	 * @return the execution modality
	 */
	public String getExecutionModality() {
		return executionModality;
	}
	
	public boolean displayToolbar() {
		return displayToolbar;
	}

	public void setDisplayToolbar(boolean displayToolbar) {
		this.displayToolbar = displayToolbar;
	}
	
	public boolean displaySliders() {
		return displaySliders;
	}

	public void setDisplaySliders(boolean displaySliders) {
		this.displaySliders = displaySliders;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object another) {
		if (another instanceof ExecutionInstance) {;
			ExecutionInstance anInstance = (ExecutionInstance) another;
			return this.executionId.equals(anInstance.executionId);
		} else 
			return false;
	}

}
