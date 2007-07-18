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
package it.eng.spagobi.bo.dao.audit;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IAuditDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiAudit;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class AuditManager {
	
	public static final String MODULE_NAME 		= "AuditService";
	
	public static final String AUDIT_ID 		= "SPAGOBI_AUDIT_ID";
	public static final String AUDIT_SERVLET 	= "SPAGOBI_AUDIT_SERVLET";
	public static final String EXECUTION_START 	= "SPAGOBI_AUDIT_EXECUTION_START";
	public static final String EXECUTION_END 	= "SPAGOBI_AUDIT_EXECUTION_END";
	public static final String EXECUTION_STATE 	= "SPAGOBI_AUDIT_EXECUTION_STATE";
	public static final String ERROR_MESSAGE 	= "SPAGOBI_AUDIT_ERROR_MESSAGE";
	public static final String ERROR_CODE 		= "SPAGOBI_AUDIT_ERROR_CODE";
	
    private static AuditManager _instance = null;
    
    private static boolean _disabled = false;
    private static String _documentState = "ALL";
    private static IAuditDAO _auditDAO = null;

    private AuditManager() {
    	SpagoBITracer.debug(MODULE_NAME, AuditManager.class.getName(), "<init>", "Begin istantiation of AuditManager");
    	SourceBean config = (SourceBean) ConfigSingleton.getInstance().getAttribute("AUDIT.CONFIG");
    	SpagoBITracer.debug(MODULE_NAME, AuditManager.class.getName(), "<init>", "Audit configuration found: \n" + config.toString());
    	String disable = (String) config.getAttribute("disable");
    	if (disable != null && disable.toLowerCase().trim().equals("true")) {
    		_disabled = true;
    	}
    	
    	if (!_disabled) {
    		/*
    		 * loads the document state and try to find it in the SBI_DOMAINS table;
    		 * if it does not exist, the default value is considered
    		 */
	    	String documentState = (String) config.getAttribute("document_state");
	    	if (documentState != null) {
	    		documentState = documentState.toUpperCase().trim();
	    		if (!documentState.toUpperCase().trim().equals("ALL")) {
	    			List availableStates = new ArrayList();
					try {
						availableStates = DAOFactory.getDomainDAO().loadListDomainsByType("STATE");
					} catch (EMFUserError e) {
						SpagoBITracer.critical(MODULE_NAME, AuditManager.class.getName(), "<init>", 
			    				"Error while getting available document states from db", e);
					}
	    			boolean stateFound = false;
	    			Iterator it = availableStates.iterator();
	    			while (it.hasNext()) {
	    				Domain aDomain = (Domain) it.next();
	    				if (aDomain.getValueCd().equalsIgnoreCase(documentState)) {
	    					stateFound = true;
	    					break;
	    				}
	    			}
	    			if (stateFound) {
	    				_documentState = documentState;
	    			}
	    		}
	    	}
	    	
    		/*
    		 * instantiates the persistence class; if some errors occur, the audit log is disabled
    		 */
	    	String persistenceClassName = (String) config.getAttribute("persistenceClass");
	    	try {
	    		Class persistenceClass = Class.forName(persistenceClassName);
	    		_auditDAO = (IAuditDAO) persistenceClass.newInstance();
	    	} catch (Exception e) {
	    		SpagoBITracer.critical(MODULE_NAME, AuditManager.class.getName(), "<init>", 
	    				"Error while instantiating persistence class. Audit log will be disabled", e);
	    		_disabled = true;
	    	}
    	}	
		SpagoBITracer.debug(MODULE_NAME, AuditManager.class.getName(), "<init>", "AuditManager instatiation end");
    }

    public static AuditManager getInstance() {
        if (_instance == null) {
        	_instance = new AuditManager();
        }
        return _instance;
    }
	
    public SbiAudit loadAudit(Integer id) throws EMFUserError {
    	SbiAudit aSbiAudit = _auditDAO.loadAuditByID(id);
    	return aSbiAudit;
    }
    
    public void insertAudit(SbiAudit aSbiAudit) throws EMFUserError {
    	if (canBeRegistered(aSbiAudit)) _auditDAO.insertAudit(aSbiAudit);
    }
    
    public void modifyAudit(SbiAudit aSbiAudit) throws EMFUserError {
    	if (canBeRegistered(aSbiAudit)) _auditDAO.modifyAudit(aSbiAudit);
    }
    
    private boolean canBeRegistered(SbiAudit aSbiAudit) {
    	if (!_disabled) {
    		if (_documentState.equalsIgnoreCase("ALL") || 
    				_documentState.equalsIgnoreCase(aSbiAudit.getDocumentState())) {
    			return true;
    		} else {
        		SpagoBITracer.debug(MODULE_NAME, AuditManager.class.getName(), "insertAudit", 
					"AuditManager is disabled for documents with state " + aSbiAudit.getDocumentState());
        		return false;
    		}
    	} else {
    		SpagoBITracer.debug(MODULE_NAME, AuditManager.class.getName(), "modifyAudit", 
					"AuditManager is disabled, so no records can be modified");
    		return false;
    	}
    }
    
	/**
	 * Inserts a record on the audit log
	 * @param obj The BIObject being executed
	 * @param profile The user profile
	 * @param role The execution role
	 * @param modality The execution modality
	 * @return The Integer representing the execution id
	 */
	public Integer insertAudit(BIObject obj, IEngUserProfile profile, String role, String modality) {
		SbiAudit audit = new SbiAudit();
		audit.setUserName(profile.getUserUniqueIdentifier().toString());
		audit.setUserGroup(role);
		audit.setDocumentId(obj.getId());
		audit.setDocumentLabel(obj.getLabel());
		audit.setDocumentName(obj.getName());
		audit.setDocumentType(obj.getBiObjectTypeCode());
		audit.setDocumentState(obj.getStateCode());
		String documentParameters = "";
		List parameters = obj.getBiObjectParameters();
		if (parameters != null && parameters.size() > 0) {
			for (int i = 0; i < parameters.size(); i++) {
				BIObjectParameter parameter = (BIObjectParameter) parameters.get(i);
				documentParameters += parameter.getParameterUrlName() + "=";
				List values = parameter.getParameterValues();
				if (values != null) 
					documentParameters += values.toString();
				else 
					documentParameters += "NULL";
				if (i < parameters.size() - 1) documentParameters += "&";
			}
		}
		audit.setDocumentParameters(documentParameters);
		Engine engine = obj.getEngine();
		audit.setEngineId(engine.getId());
		audit.setEngineLabel(engine.getLabel());
		audit.setEngineName(engine.getName());
		Domain engineType = null;
		try {
			engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
		} catch (EMFUserError e) {
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "auditLog", 
		 				"Error retrieving document's engine information", e);
		}
		audit.setEngineType(engineType != null ? engineType.getValueCd() : null);
		if (engineType != null) {
			if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
				audit.setEngineUrl(engine.getUrl());
				audit.setEngineDriver(engine.getDriverName());
			} else {
				audit.setEngineClass(engine.getClassName());
			}
		}
		audit.setRequestTime(new Timestamp(System.currentTimeMillis()));
		audit.setExecutionModality(modality);
		audit.setExecutionState("EXECUTION_REQUESTED");
		
		try {
			insertAudit(audit);
		} catch (EMFUserError e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "auditLog", 
		 				"Error doing audit insertion", e);
			return null;
		}
		return audit.getId();
	}
    
	public void updateAudit(Integer auditId, Long startTime, Long endTime, String executionState, 
			String errorMessage, String errorCode) {
		if (auditId == null) {
			SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "updateAudit:", 
					"Audit record id not specified, no updating is possible.");
			return;
		}
		
		SbiAudit audit;
		try {
			audit = loadAudit(auditId);
		} catch (EMFUserError e) {
			SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "updateAudit:", 
					"Error loading audit record with id = [" + auditId.toString() + "]", e);
			return;
		}
		
		if (audit.getExecutionStartTime() != null && audit.getExecutionEndTime() != null) {
			SpagoBITracer.warning(AuditManager.MODULE_NAME, getClass().getName(), "updateAudit:", 
					"Audit record with id = [" + auditId.toString() + "] has already a start time and an " +
							"end time. This record will not be modified.");
			return;
		}
		
		if (startTime != null) {
			Date executionStartTime = new Date(startTime.longValue());
			audit.setExecutionStartTime(executionStartTime);
//			if (audit.getExecutionStartTime() == null) {
//				audit.setExecutionStartTime(executionStartTime);
//			} else {
//				SpagoBITracer.warning(AuditManager.MODULE_NAME, getClass().getName(), "updateAudit:", 
//						"Audit record with id = [" + auditId.toString() + "] has already a start time! " +
//								"It will be NOT overwritten");
//			}
		}
		if (endTime != null) {
			Date executionEndTime = new Date(endTime.longValue());
			audit.setExecutionEndTime(executionEndTime);
//			if (audit.getExecutionEndTime() == null) {
//				audit.setExecutionEndTime(executionEndTime);
//			} else {
//				SpagoBITracer.warning(AuditManager.MODULE_NAME, getClass().getName(), "updateAudit:", 
//						"Audit record with id = [" + auditId.toString() + "] has already an end time! " +
//								"It will be overwritten");
//				audit.setExecutionEndTime(executionEndTime);
//			}
			Date executionStartTime = audit.getExecutionStartTime();
			if (executionStartTime != null) {
				// calculates exectuion time as a difference in ms
				long executionTimeLongMSec = endTime.longValue() - executionStartTime.getTime();
				// calculates exectuion time as a difference in s
				int executionTimeIntSec = Math.round(executionTimeLongMSec / 1000);
				Integer executionTime = new Integer(executionTimeIntSec);
				audit.setExecutionTime(executionTime);
			}
		}
		if (executionState != null && !executionState.trim().equals("")) {
			audit.setExecutionState(executionState);
		}
		if (errorMessage != null && !errorMessage.trim().equals("")) {
			audit.setErrorMessage(errorMessage);
			audit.setError(new Short((short)1));
		} else {
			audit.setError(new Short((short)0));
		}
		if (errorCode != null && !errorCode.trim().equals("")) {
			audit.setErrorCode(errorCode);
		}
		
		try {
			modifyAudit(audit);
		} catch (EMFUserError e) {
			SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "updateAudit:", 
					"Error updating audit record with id = [" + auditId.toString() + "]", e);
			return;
		}
	}
	
}
