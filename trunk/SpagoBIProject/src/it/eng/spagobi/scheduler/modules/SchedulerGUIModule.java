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
package it.eng.spagobi.scheduler.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.soap.axis.client.AdapterAxisProxy;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.to.BIObjectParamInfo;
import it.eng.spagobi.scheduler.to.ObjExecSchedulation;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SchedulerGUIModule extends AbstractModule {
    
	public static final String MODULE_PAGE = "SchedulerGUIPage";
	public static final String JOB_GROUP = "BIObjectExecutions";
	public static final String JOB_NAME_PREFIX = "Execute_";
	
	public void init(SourceBean config) {	}

	
	
	
	
	public void service(SourceBean request, SourceBean response) throws Exception { 
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
				           "service","begin of scheuling service =" +message);
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if(message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						           "service", "The message is null");
				throw userError;
			}
			if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GETOBJECTS_SCHED)) {
				getObjectForScheduling(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_OBJECT_SCHEDULATIONS)) {
				getObjectSchedulations(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_NEW_OBJECT_SCHEDULATION)) {
				newObjectSchedulationHandler(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_OBJECT_SCHEDULATION_DETAILS)) {
				getObjectSchedulationDetails(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_SCHEDULE_OBJECT)) {
				scheduleObject(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_DELETE_OBJECT_SCHEDULE)) {
				deleteObjectSchedule(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	
	}
	
	

	private void deleteObjectSchedule(SourceBean request, SourceBean response) throws EMFUserError {
		AdapterAxisProxy proxy = new AdapterAxisProxy();
		String sbiconturl = GeneralUtilities.getSpagoBiContextAddress();
		String triggerName = (String) request.getAttribute("triggerName");
		String triggerGroup = (String) request.getAttribute("triggerGroup");
		try {
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"deleteSchedulation\" ");
			message.append(" triggerName=\"" + triggerName + "\" ");
			message.append(" triggerGroup=\"" + triggerGroup + "\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
			String resp = proxy.service(message.toString());
			SourceBean respSB = SourceBean.fromXMLString(resp);
			SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
			SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
			SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
			String outcome = (String)execOutSB.getAttribute("outcome");
			if(outcome.equalsIgnoreCase("fault"))
				throw new Exception("Object schedule not deleted by the service");
			// fill spago response
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ListObjectSchedulationLoopPub");
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                                "deleteObjectSchedule","Error while deleting job schedule ", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		
	}

	
	
	
	private void getObjectSchedulationDetails(SourceBean request, SourceBean response) throws EMFUserError {
			AdapterAxisProxy proxy = new AdapterAxisProxy();
			String sbiconturl = GeneralUtilities.getSpagoBiContextAddress();
			String triggerName = (String) request.getAttribute("triggerName");
			try {
				StringBuffer message = new StringBuffer();
				message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationDefinition\" ");
				message.append(" triggerName=\"" + triggerName + "\" ");
				message.append(">");
				message.append("</SERVICE_REQUEST>");
				proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
				String resp = proxy.service(message.toString());
				SourceBean respSB = SourceBean.fromXMLString(resp);
				SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
				SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
				SourceBean triggerSB = (SourceBean) schedModRespSB.getAttribute("TRIGGER_DETAILS");
				
				// get the object id
				String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
				Integer objId = new Integer(objIdStr);
				// get the flag for object job existance
				String objJobExist = (String) request.getAttribute(SpagoBIConstants.OBJ_JOB_EXISTS);
				ObjExecSchedulation oes = new ObjExecSchedulation();
				oes.setTriggerName((String) triggerSB.getAttribute("triggerName"));
				oes.setTriggerDescription((String) triggerSB.getAttribute("triggerDescription"));
				oes.setStartDate((String) triggerSB.getAttribute("triggerStartDate"));
				oes.setStartTime((String) triggerSB.getAttribute("triggerStartTime"));
				oes.setEndDate((String) triggerSB.getAttribute("triggerEndDate"));
				oes.setEndTime((String) triggerSB.getAttribute("triggerEndTime"));
				oes.setRepeatInterval((String) triggerSB.getAttribute("triggerRepeatInterval"));
				if (objJobExist.equalsIgnoreCase("true")) {
					//load schedulation and build to
					response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "TRUE");
				} else {
					response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "FALSE");
				}
				
				// recover store output information
				String storeoutput = (String) triggerSB.getAttribute("storeoutput");
				if (storeoutput != null && storeoutput.equalsIgnoreCase("true")) {
					oes.setStoreOutput(true);
					oes.setStoreName((String) triggerSB.getAttribute("storename"));
					oes.setStoreDescription((String) triggerSB.getAttribute("storedescription"));
					oes.setHistoryLength((String) triggerSB.getAttribute("lengthhistory"));
					String storeassnapshot = (String) triggerSB.getAttribute("storeassnapshot");
					if (storeassnapshot != null && storeassnapshot.equalsIgnoreCase("true")) {
						oes.setStoreType("storeassnapshot");
					} else {
						oes.setStoreType("storeasdocument");
						oes.setPathDocument((String) triggerSB.getAttribute("pathdocument"));
					}
				} else {
					oes.setStoreOutput(false);
				}
				
				// recover the list of biobject parameters
				List biobjpars = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(objId);
				List emptyParMap = new ArrayList();
				Iterator biobjparsIter = biobjpars.iterator();
				String queryStr = (String) triggerSB.getAttribute("queryStr");
				
				while (biobjparsIter.hasNext()) {
					BIObjectParameter bop = (BIObjectParameter)biobjparsIter.next();
					String bopUrlName = bop.getParameterUrlName();
					int bopValueStartIndex = queryStr.indexOf(bopUrlName + "=") + bopUrlName.length() + 1;
					int bopValueEndIndex = queryStr.indexOf("%26", bopValueStartIndex);
					if (bopValueEndIndex < 0) bopValueEndIndex = queryStr.length();
					String bopValue = queryStr.substring(bopValueStartIndex, bopValueEndIndex);
					BIObjectParamInfo bopi = new BIObjectParamInfo(bop.getLabel(), bop.getParameterUrlName(), bopValue);
					emptyParMap.add(bopi);
				}
				oes.setObjExecParameters(emptyParMap);
				// populate response with the right values
				response.setAttribute(SpagoBIConstants.OBJ_SCHEDULE_DETAIL, oes);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "DetailObjectSchedulingPub");
				response.setAttribute(SpagoBIConstants.OBJECT_ID, objId);
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			                        "getObjectSchedulationDetails", 
			                        "Error while recovering job schedulation definition ", e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
	}





	private void getObjectForScheduling(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ObjectForSchedulingPub");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getObjectForScheduling","Error while recovering objects for scheduling", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	private void getObjectSchedulations(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			AdapterAxisProxy proxy = new AdapterAxisProxy();
			String sbiconturl = GeneralUtilities.getSpagoBiContextAddress();
			// get the object id
			String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
			Integer objId = new Integer(objIdStr);
			// load the object 
			BIObject biobj = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
			// get the label of the object
			String biobjLabel = biobj.getLabel();
			// check if the scheduler job associated to the object exists 
			boolean jobExists = false;
			try{
				StringBuffer message = new StringBuffer();
				message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"existJobDefinition\" ");
				message.append(" jobName=\""+JOB_NAME_PREFIX+biobjLabel+"\" ");
				message.append(" jobGroup=\""+JOB_GROUP+"\" ");
				message.append(">");
				message.append("</SERVICE_REQUEST>");
				proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
				String resp = proxy.service(message.toString());
				SourceBean respSB = SourceBean.fromXMLString(resp);
				SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
				SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
				SourceBean jobExistSB = (SourceBean)schedModRespSB.getAttribute("JOB_EXISTANCE");
				String existsAttr = (String)jobExistSB.getAttribute("exists");
				if(existsAttr.equalsIgnoreCase("true")){
					jobExists = true;
				}
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			                        "getObjectSchedulations","Error while recovering job definition ", e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			// if the job exist load into response its scheduling list
			SourceBean pageListSB  = new SourceBean("PAGED_LIST");
			if(jobExists) {
				try{
					StringBuffer message = new StringBuffer();
					message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationList\" ");
					message.append(" jobName=\""+JOB_NAME_PREFIX+biobjLabel+"\" ");
					message.append(" jobGroup=\""+JOB_GROUP+"\" ");
					message.append(">");
					message.append("</SERVICE_REQUEST>");
					proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
					String serviceResp = proxy.service(message.toString());
					SourceBean servRespSB = SourceBean.fromXMLString(serviceResp);
					SourceBean rowsSB = (SourceBean)servRespSB.getAttribute("SERVICE_RESPONSE.SCHEDULERMODULE.ROWS");
					pageListSB.setAttribute(rowsSB);
					response.setAttribute(pageListSB);
					response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "true");
				} catch (Exception e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
				                        "getObjectSchedulations","Error while recovering job schedulation list ", e);
					throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
				}
			} else { // else load an empty list
				SourceBean rowsSB = new SourceBean("ROWS");
				pageListSB.setAttribute(rowsSB);
				response.setAttribute(pageListSB);
				response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "false");
			}
			// populate response with the right values
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ListObjectSchedulationPub");
			response.setAttribute(SpagoBIConstants.OBJECT_ID, objId);
			
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getDetailObjectScheduling","Error while recovering ", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void newObjectSchedulationHandler(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			AdapterAxisProxy proxy = new AdapterAxisProxy();
			String sbiconturl = GeneralUtilities.getSpagoBiContextAddress();
			// get the object id
			String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
			Integer objId = new Integer(objIdStr);
			// get the flag for object job existance
			String objJobExist = (String) request.getAttribute(SpagoBIConstants.OBJ_JOB_EXISTS);
			ObjExecSchedulation oes = null;
			if(objJobExist.equalsIgnoreCase("true")) {
				//load schedulation and build to
				oes = new ObjExecSchedulation();
				response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "TRUE");
			} else {
				oes = new ObjExecSchedulation();
				response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "FALSE");
			}
			// recover the list of biobject parameters
			List biobjpars = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(objId);
			List emptyParMap = new ArrayList();
			Iterator biobjparsIter = biobjpars.iterator();
			while(biobjparsIter.hasNext()) {
				BIObjectParameter bop = (BIObjectParameter)biobjparsIter.next();
				BIObjectParamInfo bopi = new BIObjectParamInfo(bop.getLabel(), bop.getParameterUrlName(), "");
				emptyParMap.add(bopi);
			}
			oes.setObjExecParameters(emptyParMap);
			// populate response with the right values
			response.setAttribute(SpagoBIConstants.OBJ_SCHEDULE_DETAIL, oes);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "DetailObjectSchedulingPub");
			response.setAttribute(SpagoBIConstants.OBJECT_ID, objId);
			
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getDetailObjectScheduling","Error while recovering ", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	private void scheduleObject(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			AdapterAxisProxy proxy = new AdapterAxisProxy();
			String sbiconturl = GeneralUtilities.getSpagoBiContextAddress();
			proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
			// get object id
			String objIdStr = (String)request.getAttribute(SpagoBIConstants.OBJECT_ID);
			Integer objId = new Integer(objIdStr);
			// load the object 
			BIObject biobj = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
			// get the label of the object
			String biobjLabel = biobj.getLabel();
			String jobName = JOB_NAME_PREFIX + biobjLabel;
			String jobGroupName = JOB_GROUP;
			// if the object job doesn't already exist then define a new job
			String biobj_job_exist = (String)request.getAttribute(SpagoBIConstants.OBJ_JOB_EXISTS);
			if(!biobj_job_exist.equalsIgnoreCase("true")) {
				// create message to define the new job
				String jobDescr = "Execution Job of the business document " +  biobjLabel;
				StringBuffer message = new StringBuffer();
				message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"defineJob\" ");
				message.append(" jobName=\""+jobName+"\" ");
				message.append(" jobDescription=\""+jobDescr+"\" ");
				message.append(" jobGroupName=\""+jobGroupName+"\" ");
				message.append(" jobRequestRecovery=\"false\" ");
				message.append(" jobClass=\"it.eng.spagobi.scheduler.jobs.ExecuteBIDocumentJob\" ");
				message.append(">");
				message.append("   <PARAMETERS>");
				message.append("   	   <PARAMETER name=\"documentid\" value=\""+objIdStr+"\" />");
				message.append("   </PARAMETERS>");
				message.append("</SERVICE_REQUEST>");
				try{
					String servoutStr = proxy.service(message.toString());
					SourceBean servoutSB = SourceBean.fromXMLString(servoutStr);
					SourceBean servRespSB = (SourceBean)servoutSB.getAttribute("SERVICE_RESPONSE");
					SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
					SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
					String outcome = (String)execOutSB.getAttribute("outcome");
					if(outcome.equalsIgnoreCase("fault"))
						throw new Exception("Execution Job not defined by the web service");
				} catch (Exception e) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	                                    "scheduleObject","Error while defining job ", e);
					throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
				}
			}
			// put into response the existence of the job
			response.setAttribute(SpagoBIConstants.OBJ_JOB_EXISTS, "TRUE");
            // SCHEDULE THE TRIGGER
			// create the message to schedule the job	
			String triggername = (String)request.getAttribute("triggername");	
			String triggerDescription  = (String)request.getAttribute("triggerdescription");	
			String startdate  = (String)request.getAttribute("startdate");	
			String starttime = (String)request.getAttribute("starttime");	
			String enddate = (String)request.getAttribute("enddate");	
			String endtime = (String)request.getAttribute("endtime");	
			String repeatinterval = (String)request.getAttribute("repeatInterval");	
			String storout = (String) request.getAttribute("storeoutput");
			if(storout==null)
				storout = "false";
			else storout = "true";
			String storename = (String)request.getAttribute("storename");	
			String storedescr = (String)request.getAttribute("storedescription");		
			String storetype = (String)request.getAttribute("storetype");	
			String pathdoc = (String)request.getAttribute("pathdocument");	
			String lengthhist = (String)request.getAttribute("historylength");	
			String queryStr = getQueryString(request);
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"scheduleJob\" ");
			message.append(" jobName=\""+jobName+"\" ");
			message.append(" jobGroup=\""+jobGroupName+"\" ");
			message.append(" triggerName=\""+triggername+"\" ");
			message.append(" triggerDescription=\""+triggerDescription+"\" ");
			message.append(" startDate=\""+startdate+"\" ");
			message.append(" startTime=\""+starttime+"\" ");
			if(!enddate.trim().equals("")){
				message.append(" endDate=\""+enddate+"\" ");
				if(!endtime.trim().equals("")){
					message.append(" endTime=\""+endtime+"\" ");
				}
			}
			if(!repeatinterval.trim().equals("")){
				message.append(" repeatInterval=\""+repeatinterval+"\" ");
			}
			message.append(">");
			message.append("   <PARAMETERS>");
			message.append("   	   <PARAMETER name=\"parameters\" value=\""+queryStr+"\" />");
			if(storout.equals("true")) {
				message.append("   	   <PARAMETER name=\"storeoutput\" value=\"true\" />");
				message.append("   	   <PARAMETER name=\"storename\" value=\""+storename+"\" />");
				message.append("   	   <PARAMETER name=\"storedescription\" value=\""+storedescr+"\" />");
				message.append("   	   <PARAMETER name=\"lengthhistory\" value=\""+lengthhist+"\" />");
				if(storetype.equals("storesnap")) {
					message.append("   	   <PARAMETER name=\"storeassnapshot\" value=\"true\" />");
				} else {
					message.append("   	   <PARAMETER name=\"storeasdocument\" value=\"true\" />");
					message.append("   	   <PARAMETER name=\"pathdocument\" value=\""+pathdoc+"\" />");
				}
			}
			message.append("   </PARAMETERS>");
			message.append("</SERVICE_REQUEST>");
			// call the service to define the job
			try{
				String servoutStr = proxy.service(message.toString());
				SourceBean servoutSB = SourceBean.fromXMLString(servoutStr);
				SourceBean servRespSB = (SourceBean)servoutSB.getAttribute("SERVICE_RESPONSE");
				SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
				SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
				String outcome = (String)execOutSB.getAttribute("outcome");
				if(outcome.equalsIgnoreCase("fault"))
					throw new Exception("Job not scheduled by the web service");
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			                        "scheduleObject","Error while scheduling job ", e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			// put into response the name of the publisher
            response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "DetailObjectSchedulingLoopPub");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "scheduleObject","Error while scheduling object execution ", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	
	
	private String getQueryString(SourceBean request){
		String query = "";
		List attributes = request.getContainedAttributes();
		Iterator iterAttr = attributes.iterator();
		while(iterAttr.hasNext()){
			SourceBeanAttribute sba = (SourceBeanAttribute)iterAttr.next();
			String key = sba.getKey();
			if(key.startsWith("biobjpar_")){
				key = key.substring(9);
				String value = (String)sba.getValue();
				query = query + key + "=" + value + "%26";
			}
		}
		if (query.endsWith("%26")) query = query.substring(0, query.length() - 3);
		return query;
	}
	
	
}	
	
	
