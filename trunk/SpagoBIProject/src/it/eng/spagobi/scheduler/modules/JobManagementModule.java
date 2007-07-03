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
import it.eng.spago.error.EMFErrorCategory;
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
	
	
	
	
	private void getAllJobs(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			// create the sourcebean of the list
			SourceBean pageListSB  = new SourceBean("PAGED_LIST");
			// get the job list form the web sevice
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobList\" >");
			message.append("</SERVICE_REQUEST>");
			proxy.setEndpoint(sbiconturl + "/services/AdapterAxis");
			String wsresp = proxy.service(message.toString());
			SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(wsresp);
			if(schedModRespSB==null) {
				throw new Exception("Web service response incomplete");
			}
			SourceBean rowsSB = (SourceBean)schedModRespSB.getAttribute("ROWS");
			if(rowsSB==null){
				throw new Exception("Web service response incomplete");
			}
			// recover all jobs
			List jobSBs = rowsSB.getAttributeAsList("ROW");
			Iterator jobSBiter = jobSBs.iterator();
			while(jobSBiter.hasNext()) {
				SourceBean jobSB = (SourceBean)jobSBiter.next();
				String jobname = (String)jobSB.getAttribute("jobName");
				String jobgroupname = (String)jobSB.getAttribute("jobGroupName");
				message = new StringBuffer();
				message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationList\" ");
				message.append(" jobName=\""+jobname+"\" ");
				message.append(" jobGroup=\""+jobgroupname+"\" ");
				message.append(">");
				message.append("</SERVICE_REQUEST>");
				String servResp_JSL = proxy.service(message.toString());
				int numSchedulation = 0;
				SourceBean schesModSB_JSL = SchedulerUtilities.getSBFromWebServiceResponse(servResp_JSL);
				if(schesModSB_JSL!=null) {
					SourceBean rowsSB_JSL = (SourceBean)schesModSB_JSL.getAttribute("ROWS");
					if(rowsSB_JSL!=null) {
						List schedulations = rowsSB_JSL.getAttributeAsList("ROW");
						if(schedulations!=null){
							numSchedulation = schedulations.size();
						}
					}
				}
				jobSB.setAttribute("numSchedule", new Integer(numSchedulation));
			}
			// fill the list sourcebean
			pageListSB.setAttribute(rowsSB);
			// populate response with the right values
			response.setAttribute(pageListSB);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ListJobs");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getAllJobs","Error while recovering all job definition", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1000", "component_scheduler_messages");
		}
	}
	
	
	private void newJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			JobInfo jobInfo = new JobInfo();
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			//response.setAttribute(SpagoBIConstants.MODALITY, "SELECT_DOCUMENTS");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "newJob","Error while recovering objects for scheduling", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1001", "component_scheduler_messages");
		}
	}
	
	
	
	private void deleteJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			StringBuffer message = null;
			// delete all job schedules
			message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationList\" ");
			message.append(" jobName=\""+jobName+"\" ");
			message.append(" jobGroup=\""+jobGroupName+"\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String servResp_JSL = proxy.service(message.toString());
			SourceBean schedModSB_JSL = SchedulerUtilities.getSBFromWebServiceResponse(servResp_JSL);
			if(schedModSB_JSL==null) {
				throw new Exception("List of job triggers not returned by Web service ");
			}
			SourceBean rowsSB_JSL = (SourceBean)schedModSB_JSL.getAttribute("ROWS");
			if(rowsSB_JSL==null) {
				throw new Exception("List of job triggers not returned by Web service ");
			}
			// delete each schedulation
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
				String resp_DS = proxy.service(message.toString());
				SourceBean schedModRespSB_DS = SchedulerUtilities.getSBFromWebServiceResponse(resp_DS);
				if(schedModRespSB_DS==null) {
					throw new Exception("Imcomplete response returned by the Web service " +
							            "during schedule "+triggerName+" deletion");
				}	
				if(!SchedulerUtilities.checkResultOfWSCall(schedModRespSB_DS)){
					throw new Exception("Schedule "+triggerName+" not deleted by the Web Service");
				}
			}			
			// delete job	
			message = new StringBuffer();
	    	message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"deleteJob\" ");
			message.append(" jobName=\"" + jobName + "\" ");
			message.append(" jobGroupName=\"" + jobGroupName + "\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String resp_DJ = proxy.service(message.toString());
			SourceBean schedModRespSB_DJ = SchedulerUtilities.getSBFromWebServiceResponse(resp_DJ);
			if(schedModRespSB_DJ==null) {
				throw new Exception("Imcomplete response returned by the Web service " +
						            "during job "+jobName+" deletion");
			}	
			if(!SchedulerUtilities.checkResultOfWSCall(schedModRespSB_DJ)){
				throw new Exception("JOb "+jobName+" not deleted by the Web Service");
			}
			// fill response
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToJobList");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "deleteJob","Error while deleting job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1002", "component_scheduler_messages");
		}
	}
	
	
	
	private void documentSelected(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			// get hob information from session
			JobInfo jobInfo = (JobInfo)sessCont.getAttribute(SpagoBIConstants.JOB_INFO);
			// recover generic data
			getJobGenericDataFromRequest(request, jobInfo);
			// recover parameter values
			getDocParValuesFromRequest(request, jobInfo);
			// get the list of biobject previously setted
			List biobjects = jobInfo.getBiobjects();
			// get the list of biobject id previously setted
			List biobjIds = jobInfo.getBiobjectIds();
			// create the list of new biobject selected
			List biobj_sel_now = new ArrayList();
		    // get the list of biobject id from the request
			String sel_biobj_ids_str = (String)request.getAttribute("selected_biobject_ids");
			String[] sel_biobj_ids_arr = sel_biobj_ids_str.split(",");
			List biobjIdsFromRequest = Arrays.asList(sel_biobj_ids_arr);
			//List biobjIdsFromRequest = request.getAttributeAsList("biobject");
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
			jobInfo.setBiobjects(biobj_sel_now);
			sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "documentSelected","Error while selecting documents", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1006", "component_scheduler_messages");
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
			// check for input validation errors 
			//ValidationCoordinator.validate("PAGE", "JobManagementPage", this);
			if(!this.getErrorHandler().isOKByCategory(EMFErrorCategory.VALIDATION_ERROR)) {
				List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
				response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
				return;
			}
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
				}
				message.append("<PARAMETER name=\""+biobj.getLabel()+"\" value=\""+queryString+"\" />");
				doclabels += biobj.getLabel() + ",";
			}
			if(doclabels.length()>0) {
				doclabels = doclabels.substring(0, doclabels.length()-1);
			}
			message.append("   	   <PARAMETER name=\"documentLabels\" value=\""+doclabels+"\" />");
			message.append("   </PARAMETERS>");
			message.append("</SERVICE_REQUEST>");
			// call the web service
			String servoutStr = proxy.service(message.toString());
			SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(servoutStr);
			if(schedModRespSB==null) {
				throw new Exception("Imcomplete response returned by the Web service " +
						            "during job "+jobInfo.getJobName()+" creation");
			}	
			if(!SchedulerUtilities.checkResultOfWSCall(schedModRespSB)){
				throw new Exception("Job "+jobInfo.getJobName()+" not created by the web service");
			}
			// fil response
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToJobList");	
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "saveJob","Error while saving job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1004", "component_scheduler_messages");
		}
	}

		

	
	private void getJobDetail(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
	        // call we service
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobDefinition\" ");
			message.append(" jobName=\""+jobName+"\" ");
			message.append(" jobGroup=\""+jobGroupName+"\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String respStr = proxy.service(message.toString());
            SourceBean respSB = SchedulerUtilities.getSBFromWebServiceResponse(respStr);
            if(respSB==null) {
				throw new Exception("Imcomplete response returned by the Web service " +
						            "during job "+jobName+" recover");
			}	
			SourceBean jobDetailSB = (SourceBean)respSB.getAttribute("JOB_DETAIL");
			if(jobDetailSB!=null) {
				JobInfo jobInfo = SchedulerUtilities.getJobInfoFromJobSourceBean(jobDetailSB);
				sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			} else {
				throw new Exception("Detail not recovered for job " + jobName);
			}
			// fill response
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getJobDetail","Error while getting detail of the job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1005", "component_scheduler_messages");
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
				if(valueParConcat!=null){
					if(valueParConcat.trim().equals("")) {
						biobjpar.setParameterValues(new ArrayList());
						continue;
					} else {
						String[] valueParArr = valueParConcat.split(splitter);
						List valuePar = Arrays.asList(valueParArr);
						biobjpar.setParameterValues(valuePar);
					}
				}
			}
		}
	}
	
	
	
}	
	
	
