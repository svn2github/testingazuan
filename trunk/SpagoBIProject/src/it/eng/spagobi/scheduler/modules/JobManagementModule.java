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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.soap.axis.client.AdapterAxisProxy;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.to.JobInfo;
import it.eng.spagobi.scheduler.utils.SchedulerUtilities;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JobManagementModule extends AbstractModule {
    
	
	public static final String MODULE_PAGE = "SchedulerGUIPage";
	public static final String JOB_GROUP = "BIObjectExecutions";
	public static final String JOB_NAME_PREFIX = "Execute_";
	
	private RequestContainer reqCont = null;
	private SessionContainer sessCont = null;
	private AdapterAxisProxy proxy = null;
	private String sbiconturl = null; 
	
	public void init(SourceBean config) {	
		
	}
	
	public void service(SourceBean request, SourceBean response) throws Exception { 
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
				           "service","begin of scheuling service =" +message);
		reqCont = getRequestContainer();
		sessCont = reqCont.getSessionContainer();
		proxy = new AdapterAxisProxy();
		sbiconturl = GeneralUtilities.getSpagoBiContextAddress();
		proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
		
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if(message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						           "service", "The message is null");
				throw userError;
			}
			if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_ALL_JOBS)) {
				getAllJobs(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_NEW_JOB)) {
				newJob(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_DOCUMENTS_SELECTED)) {
				documentSelected(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_FILL_PARAMETERS)) {
				fillParameters(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_SAVE_JOB)) {
				saveJob(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_DELETE_JOB)) {
				deleteJob(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_JOB_DETAIL)) {
				getJobDetail(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			           			"service", "Error while executing schedule service", ex);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		           			"service", "end of scheuling service =" +message);
	}
	
	
	
	private void getJobDetail(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobDefinition\" ");
			message.append(" jobName=\""+jobName+"\" ");
			message.append(" jobGroup=\""+jobGroupName+"\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String respStr = proxy.service(message.toString());
            SourceBean respSB = SchedulerUtilities.getSBFromWebServiceResponse(respStr);			
			SourceBean jobDetailSB = (SourceBean)respSB.getAttribute("JOB_DETAIL");
			if(jobDetailSB!=null) {
				JobInfo jobInfo = SchedulerUtilities.getJobInfoFromJobSourceBean(jobDetailSB);
				sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			} else {
				throw new Exception("Detail not recovered for job " + jobName);
			}
			
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.MODALITY, "SELECT_DOCUMENTS");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
			
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getJobDetail","Error while getting detail of the job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void deleteJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			StringBuffer message = null;
			// delete all job schedules
			try{
				message = new StringBuffer();
				message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationList\" >");
				message.append(" jobName=\""+jobName+"\" ");
				message.append(" jobGroup=\""+jobGroupName+"\" ");
				message.append(">");
				message.append("</SERVICE_REQUEST>");
				String servResp_JSL = proxy.service(message.toString());
				SourceBean servRespSB_JSL = SourceBean.fromXMLString(servResp_JSL);
				SourceBean rowsSB_JSL = (SourceBean)servRespSB_JSL.getAttribute("SERVICE_RESPONSE.SCHEDULERMODULE.ROWS");
				if(rowsSB_JSL!=null) {
					List schedules = rowsSB_JSL.getAttributeAsList("ROW");
				    Iterator iterSchedules = schedules.iterator();
				    while(iterSchedules.hasNext()) {
				    	SourceBean scheduleSB = (SourceBean)iterSchedules.next();
				    	String triggerName = (String)scheduleSB.getAttribute("triggerName");
				    	String triggerGroup = (String)scheduleSB.getAttribute("triggerGroup");
				    	message = new StringBuffer();
				    	message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"deleteSchedulation\" ");
						message.append(" triggerName=\"" + triggerName + "\" ");
						message.append(" triggerGroup=\"" + triggerGroup + "\" ");
						message.append(">");
						message.append("</SERVICE_REQUEST>");
						String resp = proxy.service(message.toString());
						SourceBean respSB = SourceBean.fromXMLString(resp);
						SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
						SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
						SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
						if(execOutSB!=null) {
							String outcome = (String)execOutSB.getAttribute("outcome");
							if(outcome.equalsIgnoreCase("fault"))
								throw new Exception("Schedule "+triggerName+" not deleted by the service");
						}
				    }
				}
			} catch (Exception e) {
				throw new Exception("Error while deleting schedules of the job", e);
			}
			
			// delete job	
			message = new StringBuffer();
	    	message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"deleteJob\" ");
			message.append(" jobName=\"" + jobName + "\" ");
			message.append(" jobGroupName=\"" + jobGroupName + "\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String resp_DJ = proxy.service(message.toString());
			SourceBean respSB_DJ = SourceBean.fromXMLString(resp_DJ);
			SourceBean servRespSB_DJ = (SourceBean)respSB_DJ.getAttribute("SERVICE_RESPONSE");
			SourceBean schedModRespSB_DJ = (SourceBean)servRespSB_DJ.getAttribute("SCHEDULERMODULE");
			SourceBean execOutSB_DJ = (SourceBean)schedModRespSB_DJ.getAttribute("EXECUTION_OUTCOME");
			if(execOutSB_DJ!=null) {
				String outcome_DJ = (String)execOutSB_DJ.getAttribute("outcome");
				if(outcome_DJ.equalsIgnoreCase("fault"))
					throw new Exception("Job "+jobName+" not deleted by the web service");
			}
			
			// fill response
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToJobList");
			
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "deleteJob","Error while deleting job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void saveJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			// get job information from session
			JobInfo jobInfo = (JobInfo)sessCont.getAttribute(SpagoBIConstants.JOB_INFO);
			// recover generic data
			getJobGenericDataFromRequest(request, jobInfo);
			// recover parameter values
			getDocParValuesFromRequest(request, jobInfo);
			// create message to define the new job (for the web service)
			String jobGroupName = JOB_GROUP;
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"defineJob\" ");
			message.append(" jobName=\""+jobInfo.getJobName()+"\" ");
			message.append(" jobDescription=\""+jobInfo.getJobDescription()+"\" ");
			message.append(" jobGroupName=\""+jobGroupName+"\" ");
			message.append(" jobRequestRecovery=\"false\" ");
			message.append(" jobClass=\"it.eng.spagobi.scheduler.jobs.ExecuteBIDocumentJob\" ");
			message.append(">");
			message.append("   <PARAMETERS>");
			
			List biobjs = jobInfo.getBiobjects();
			Iterator iterbiobj = biobjs.iterator();
			String doclabels = "";
			while(iterbiobj.hasNext()) {
				BIObject biobj = (BIObject)iterbiobj.next();
				List pars = biobj.getBiObjectParameters();
				Iterator iterPars = pars.iterator();
				String queryString= "";
				while(iterPars.hasNext()) {
					BIObjectParameter biobjpar = (BIObjectParameter)iterPars.next();
					String concatenatedValue = "";
					List values = biobjpar.getParameterValues();
					if(values!=null) {
						Iterator itervalues = values.iterator();
						while(itervalues.hasNext()) {
							String value = (String)itervalues.next();
							concatenatedValue += value + ",";
						}
						if(concatenatedValue.length()>0) {
							concatenatedValue = concatenatedValue.substring(0, concatenatedValue.length() - 1);
							queryString += biobjpar.getParameterUrlName() + "=" + concatenatedValue + "%26";
						}
					}
				}
				if(queryString.length()>0) {
					queryString = queryString.substring(0, queryString.length()-3);
					message.append("<PARAMETER name=\""+biobj.getLabel()+"\" value=\""+queryString+"\" />");
				}
				doclabels += biobj.getLabel() + ",";
			}
			
			
			if(doclabels.length()>0) {
				doclabels = doclabels.substring(0, doclabels.length()-1);
			}
			message.append("   	   <PARAMETER name=\"documentLabels\" value=\""+doclabels+"\" />");
			message.append("   </PARAMETERS>");
			message.append("</SERVICE_REQUEST>");
			
			// call the web service
			try{
				String servoutStr = proxy.service(message.toString());
				SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(servoutStr);
				SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
				String outcome = (String)execOutSB.getAttribute("outcome");
				if(outcome.equalsIgnoreCase("fault"))
					throw new Exception("Job "+jobInfo.getJobName()+" not created by the web service");
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	                                "saveJob","Error while creating job ", e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToJobList");
			
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "saveJob","Error while saving job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}

	
	
	
	private void fillParameters(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			// get list of functionalities
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			// get job information from session
			JobInfo jobInfo = (JobInfo)sessCont.getAttribute(SpagoBIConstants.JOB_INFO);
			// recover generic data
			getJobGenericDataFromRequest(request, jobInfo);
			// recover parameter values
			getDocParValuesFromRequest(request, jobInfo);
			// fill the response
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.MODALITY, "SELECT_DOCUMENTS");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");		
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "fillParameters","Error while filling parameters", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	
	private void documentSelected(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			// get hob information from session
			JobInfo jobInfo = (JobInfo)sessCont.getAttribute(SpagoBIConstants.JOB_INFO);
			// recover generic data
			getJobGenericDataFromRequest(request, jobInfo);
			// get the list of biobject previously setted
			List biobjects = jobInfo.getBiobjects();
			// get the list of biobject id previously setted
			List biobjIds = jobInfo.getBiobjectIds();
			// create the list of new biobject selected
			List biobj_sel_now = new ArrayList();
			
		    // get the list of biobject id from the request
			List biobjIdsFromRequest = request.getAttributeAsList("biobject");
			// update the job information
			Iterator iterBiobjIdsFromRequest = biobjIdsFromRequest.iterator();
			while(iterBiobjIdsFromRequest.hasNext()) {
				String biobjidStr = (String)iterBiobjIdsFromRequest.next();
				Integer biobjInt = Integer.valueOf(biobjidStr);
				if(!biobjIds.contains(biobjInt)) {
					Integer biobjid = new Integer(biobjidStr);
					IBIObjectDAO biobjectDAO = DAOFactory.getBIObjectDAO();
					IBIObjectParameterDAO ibiobjpardao = DAOFactory.getBIObjectParameterDAO();
					BIObject biobj = biobjectDAO.loadBIObjectById(biobjid);
					List bipars = ibiobjpardao.loadBIObjectParametersById(biobjid);
					biobj.setBiObjectParameters(bipars);
					biobj_sel_now.add(biobj);
					//biobjects.add(biobj);
				} else {
					Iterator iter_prev_biobj = biobjects.iterator();
					while(iter_prev_biobj.hasNext()){
						BIObject biobj = (BIObject)iter_prev_biobj.next();
						if(biobj.getId().equals(biobjInt)) {
							biobj_sel_now.add(biobj);
							continue;
						}
					}
				}
			}
			//jobInfo.setBiobjects(biobjects);
			jobInfo.setBiobjects(biobj_sel_now);
			sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.MODALITY, "FILL_PARAMETERS");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "documentSelected","Error while selecting documents", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	private void newJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			JobInfo jobInfo = new JobInfo();
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			response.setAttribute(SpagoBIConstants.MODALITY, "SELECT_DOCUMENTS");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "newJob","Error while recovering objects for scheduling", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void getAllJobs(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			try{
				// create the sourcebean of the list
				SourceBean pageListSB  = new SourceBean("PAGED_LIST");
				// get the job list form the web sevice
				StringBuffer message = new StringBuffer();
				message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobList\" >");
				message.append("</SERVICE_REQUEST>");
				proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
				String resp = proxy.service(message.toString());
				SourceBean respSB = SourceBean.fromXMLString(resp);
				SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
				SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
				SourceBean rowsSB = (SourceBean)schedModRespSB.getAttribute("ROWS");
				if(rowsSB!=null) {
					List jobSBs = rowsSB.getAttributeAsList("ROW");
					Iterator jobSBiter = jobSBs.iterator();
					while(jobSBiter.hasNext()) {
						SourceBean jobSB = (SourceBean)jobSBiter.next();
						String jobname = (String)jobSB.getAttribute("jobName");
						String jobgroupname = (String)jobSB.getAttribute("jobGroupName");
						message = new StringBuffer();
						message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationList\" >");
						message.append(" jobName=\""+jobname+"\" ");
						message.append(" jobGroup=\""+jobgroupname+"\" ");
						message.append(">");
						message.append("</SERVICE_REQUEST>");
						String servResp_JSL = proxy.service(message.toString());
						SourceBean servRespSB_JSL = SourceBean.fromXMLString(servResp_JSL);
						SourceBean rowsSB_JSL = (SourceBean)servRespSB_JSL.getAttribute("SERVICE_RESPONSE.SCHEDULERMODULE.ROWS");
						int numSchedulation = 0;
						if(rowsSB_JSL!=null) {
							List schedulations = rowsSB_JSL.getAttributeAsList("ROW");
							if(schedulations!=null){
								numSchedulation = schedulations.size();
							}
						}
						jobSB.setAttribute("numSchedule", new Integer(numSchedulation));
					}
				} else {
					rowsSB = new SourceBean("ROWS");
				}
				// fill the list sourcebean
				pageListSB.setAttribute(rowsSB);
				// populate response with the right values
				response.setAttribute(pageListSB);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ListJobs");
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			                        "getAllJobs","Error while recovering job definition ", e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getAllJobs","Error while recovering all schedulations", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void getJobGenericDataFromRequest(SourceBean request, JobInfo ji) {
		String jobname = (String)request.getAttribute("jobname");
		String jobdescription = (String)request.getAttribute("jobdescription");
		if(jobname!=null) {
			ji.setJobName(jobname);
		}
		if(jobdescription!=null) {
			ji.setJobDescription(jobdescription);
		}
	}
	
	
	private void getDocParValuesFromRequest(SourceBean request, JobInfo jobInfo) {
		// get the splitter character
		String splitter = (String)request.getAttribute("splitter");
		// get the list of biobject previously setted
		List biobjects = jobInfo.getBiobjects();
		// iter over biobjects
		Iterator iterbiobjs = biobjects.iterator();
		while(iterbiobjs.hasNext()) {
			BIObject biobj = (BIObject)iterbiobjs.next();
			List biobjpars = biobj.getBiObjectParameters();
			// iter over parameters
			Iterator iterbiobjpars = biobjpars.iterator();
			while(iterbiobjpars.hasNext()) {
				BIObjectParameter biobjpar = (BIObjectParameter)iterbiobjpars.next();
				String nameParInRequest = "par_" + biobj.getId() + "_" + biobjpar.getParameterUrlName();
				String valueParConcat = (String)request.getAttribute(nameParInRequest);
				if(valueParConcat!=null) {
					String[] valueParArr = valueParConcat.split(splitter);
					List valuePar = Arrays.asList(valueParArr);
					biobjpar.setParameterValues(valuePar);
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*
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
	*/
	
}	
	
	
