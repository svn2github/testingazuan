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

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ExecutionManager {

	//private static ExecutionManager _instance = null;
	// exections before 2 hours ago are deleted  
	//private static int hoursAgo = 2;
	private Map _flows = null;
	
	/**
	 * Instantiates a new execution manager.
	 */
	public ExecutionManager() {
		_flows = new HashMap(); 
	}
	
	/*
    public static ExecutionManager getInstance() {
        if (_instance == null) {
        	_instance = new ExecutionManager();
        } else {
        	// erases old executions
        	Calendar now = new GregorianCalendar();
        	Calendar someHoursAgo = new GregorianCalendar();
        	someHoursAgo.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) - hoursAgo);
        	Set keys = _instance._flows.keySet();
        	List keysToBeDeleted = new ArrayList();
        	Iterator keysIt = keys.iterator();
        	while (keysIt.hasNext()) {
        		String key = (String) keysIt.next();
        		Calendar lastExecutionCalendar = _instance.getLastExecutionInstance(key).calendar;
        		if (lastExecutionCalendar.before(someHoursAgo)) keysToBeDeleted.add(key);
        	}
        	Iterator keysToBeDeletedIt = keysToBeDeleted.iterator();
        	while (keysToBeDeletedIt.hasNext()) {
        		String key = (String) keysToBeDeletedIt.next();
        		_instance._flows.remove(key);
        	}
        }
        return _instance;
    }
    */
    
    /**
	 * Register execution.
	 * 
	 * @param flowId the flow id
	 * @param executionId the execution id
	 * @param obj the obj
	 * @param executionRole the execution role
	 */
	public void registerExecution(String flowId, String executionId, BIObject obj, String executionRole) {
    	ExecutionInstance newInstance = new ExecutionInstance(flowId, executionId, obj, executionRole);
    	if (_flows.containsKey(flowId)) {
    		List instances = (List) _flows.get(flowId);
    		if (!instances.contains(newInstance)) 
    			instances.add(newInstance);
    	} else {
    		List list = new ArrayList();
    		list.add(newInstance);
    		_flows.put(flowId, list);
    	}
    }
    
    /**
     * Gets the execution.
     * 
     * @param executionId the execution id
     * 
     * @return the execution
     */
    public ExecutionInstance getExecution(String executionId) {
    	Set keys = _flows.keySet();
    	Iterator it = keys.iterator();
    	while (it.hasNext()) {
    		ExecutionInstance toReturn = null;
    		String key = (String) it.next();
    		List instances = (List) _flows.get(key);
    		int i = 0;
    		for (; i < instances.size(); i++) {
    			ExecutionInstance instance = (ExecutionInstance) instances.get(i);
    			if (instance.executionId.equals(executionId)) {
    				toReturn = instance;
    				break;
    			}
    		}
    		if (toReturn != null) {
        		return toReturn;
    		}
    	}
    	return null;
    }
    
    /**
     * Recover execution.
     * 
     * @param flowId the flow id
     * @param executionId the execution id
     * 
     * @return the execution instance
     */
    public ExecutionInstance recoverExecution(String flowId, String executionId) {
    	if (_flows.containsKey(flowId)) {
    		ExecutionInstance toReturn = null;
    		List instances = (List) _flows.get(flowId);
    		int i = 0;
    		for (; i < instances.size(); i++) {
    			ExecutionInstance instance = (ExecutionInstance) instances.get(i);
    			if (instance.executionId.equals(executionId)) {
    				toReturn = instance;
    				break;
    			}
    		}
    		// removes execution instances starting from the requested one
    		int initialLength = instances.size();
    		for (int k = 0; k < initialLength - i; k++) {
    			instances.remove(i);
    		}
    		return toReturn;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Checks if is being reexecuteing.
     * 
     * @param flowId the flow id
     * @param obj the obj
     * 
     * @return true, if is being reexecuteing
     */
    public boolean isBeingReexecuteing (String flowId, BIObject obj) {
    	if (!_flows.containsKey(flowId)) return false;
    	else {
    		BIObject lastObj = getLastExecutionObject(flowId);
    		if (lastObj.equals(obj)) return true;
    		else return false;
    	}
    }
    
    /**
     * Gets the last execution object.
     * 
     * @param flowId the flow id
     * 
     * @return the last execution object
     */
    public BIObject getLastExecutionObject(String flowId) {
    	ExecutionInstance executionInstance = getLastExecutionInstance(flowId);
    	if (executionInstance != null) {
    		return executionInstance.object;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Gets the last execution id.
     * 
     * @param flowId the flow id
     * 
     * @return the last execution id
     */
    public String getLastExecutionId(String flowId) {
    	ExecutionInstance executionInstance = getLastExecutionInstance(flowId);
    	if (executionInstance != null) {
    		return executionInstance.executionId;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Gets the last execution instance.
     * 
     * @param flowId the flow id
     * 
     * @return the last execution instance
     */
    public ExecutionInstance getLastExecutionInstance(String flowId) {
    	if (_flows.containsKey(flowId)) {
    		List instances = (List) _flows.get(flowId);
    		ExecutionInstance toReturn = (ExecutionInstance) instances.get(instances.size() - 1); 
    		return toReturn;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Gets the bI objects execution flow.
     * 
     * @param flowId the flow id
     * 
     * @return the bI objects execution flow
     */
    public List getBIObjectsExecutionFlow(String flowId) {
    	List instances = new ArrayList();
    	if (_flows.containsKey(flowId)) {
    		instances = (List) _flows.get(flowId);
    	}
    	return instances;
    }
    
    /**
     * Stores execution information for a single document execution
     * 
     * @author zerbetto
     *
     */
    public class ExecutionInstance {
    	
    	private String flowId = null;
    	private String executionId = null;
    	private BIObject object = null;
    	private String executionRole = null;
    	private Calendar calendar = null; 
    	
    	/**
	     * Instantiates a new execution instance.
	     * 
	     * @param flowId the flow id
	     * @param executionId the execution id
	     * @param obj the obj
	     * @param executionRole the execution role
	     */
	    public ExecutionInstance (String flowId, String executionId, BIObject obj, String executionRole) {
    		this.flowId = flowId;
    		this.executionId = executionId;
    		this.object = obj;
    		this.calendar = new GregorianCalendar();
    		this.executionRole = executionRole;
    	}
    	
    	/*
    	private BIObject cloneBIObject (BIObject obj) {
    		// clones the BIObject information
    		BIObject toReturn = new BIObject();
    		toReturn.setId(obj.getId());
    		toReturn.setLabel(obj.getLabel());
    		toReturn.setName(obj.getName());
    		toReturn.setDescription(obj.getDescription());
    		toReturn.setBiObjectTypeCode(obj.getBiObjectTypeCode());
    		toReturn.setBiObjectTypeID(obj.getBiObjectTypeID());
    		toReturn.setEncrypt(obj.getEncrypt());
    		toReturn.setPath(obj.getPath());
    		toReturn.setStateCode(obj.getStateCode());
    		toReturn.setStateID(obj.getStateID());
    		toReturn.setUuid(obj.getUuid());
    		toReturn.setVisible(obj.getVisible());
    		
			List newFunctionalities = new ArrayList();
			newFunctionalities.addAll(obj.getFunctionalities());
    		toReturn.setFunctionalities(newFunctionalities);
    		
    		List newParameters = new ArrayList();
    		List parameters = obj.getBiObjectParameters();
    		if (parameters != null && parameters.size() > 0) {
    			for (int i = 0; i < parameters.size(); i++) {
    				BIObjectParameter parameter = (BIObjectParameter) parameters.get(i);
    				BIObjectParameter newParameter = new BIObjectParameter();
    				newParameter.setBiObjectID(parameter.getBiObjectID());
    				newParameter.setHasValidValues(parameter.hasValidValues());
    				newParameter.setId(parameter.getId());
    				newParameter.setLabel(parameter.getLabel());
    				newParameter.setLovResult(parameter.getLovResult());
    				newParameter.setModifiable(parameter.getModifiable());
    				newParameter.setMultivalue(parameter.getMultivalue());
    				newParameter.setParameter(parameter.getParameter());
    				newParameter.setParameterUrlName(parameter.getParameterUrlName());
    				List newParameterValues = new ArrayList();
    				if (parameter.getParameterValues() != null) {
    					newParameterValues.addAll(parameter.getParameterValues());
    				}
    				newParameter.setParameterValues(newParameterValues);
    				newParameter.setParID(parameter.getParID());
    				newParameter.setPriority(parameter.getPriority());
    				newParameter.setProg(parameter.getPriority());
    				newParameter.setRequired(parameter.getRequired());
    				newParameter.setTransientParmeters(parameter.isTransientParmeters());
    				newParameter.setVisible(parameter.getVisible());
    			}
    		}
    		toReturn.setBiObjectParameters(newParameters);
    		toReturn.setEngine(obj.getEngine());

    		return toReturn;
    	}
    	*/

		/**
	     * Gets the execution id.
	     * 
	     * @return the execution id
	     */
	    public String getExecutionId() {
			return executionId;
		}

		/**
		 * Sets the execution id.
		 * 
		 * @param executionId the new execution id
		 */
		public void setExecutionId(String executionId) {
			this.executionId = executionId;
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
		 * Sets the bI object.
		 * 
		 * @param object the new bI object
		 */
		public void setBIObject(BIObject object) {
			this.object = object;
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
		 * Gets the execution role.
		 * 
		 * @return the execution role
		 */
		public String getExecutionRole() {
			return executionRole;
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
    
}
