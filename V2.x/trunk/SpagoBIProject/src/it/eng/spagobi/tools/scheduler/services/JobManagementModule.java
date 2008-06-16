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
package it.eng.spagobi.tools.scheduler.services;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorCategory;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.services.scheduler.service.SchedulerServiceSupplier;
import it.eng.spagobi.tools.scheduler.to.JobInfo;
import it.eng.spagobi.tools.scheduler.utils.SchedulerUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class JobManagementModule extends AbstractModule {
	static private Logger logger = Logger.getLogger(JobManagementModule.class);
	
	public static final String MODULE_PAGE = "SchedulerGUIPage";
	public static final String JOB_GROUP = "BIObjectExecutions";
	public static final String JOB_NAME_PREFIX = "Execute_";
	
	private RequestContainer reqCont = null;
	private SessionContainer sessCont = null;
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.module.AbstractModule#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {	
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception { 
		String message = (String) request.getAttribute("MESSAGEDET");
		logger.debug("begin of scheuling service =" +message);
		reqCont = getRequestContainer();
		sessCont = reqCont.getSessionContainer();
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if(message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				logger.error("The message is null");
				throw userError;
			}
			if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_ALL_JOBS) ||
			   message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_ORDER_LIST)) {
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
			logger.error("Error while executing schedule service", ex);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
		logger.debug("end of scheuling service =" +message);
	}
	
	
	
	
	private void getAllJobs(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			// create the sourcebean of the list
			SourceBean pageListSB  = new SourceBean("PAGED_LIST");
			SchedulerServiceSupplier schedulerService=new SchedulerServiceSupplier();
			String xmlList = schedulerService.getJobList();
			//SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(wsresp);
			SourceBean rowsSB = SourceBean.fromXMLString(xmlList);
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
				String xmlSchedList = schedulerService.getJobSchedulationList(jobname, jobgroupname);
				int numSchedulation = 0;
				SourceBean rowsSB_JSL = SourceBean.fromXMLString(xmlSchedList);
				if(rowsSB_JSL!=null) {
					List schedulations = rowsSB_JSL.getAttributeAsList("ROW");
					if(schedulations!=null){
						numSchedulation = schedulations.size();
					}
				}
				jobSB.setAttribute("numSchedule", new Integer(numSchedulation));
			}
			// fill the list sourcebean
			pageListSB.setAttribute(rowsSB);

			//ordering of list
			String typeOrder = (request.getAttribute("TYPE_ORDER")==null)?" ASC":(String)request.getAttribute("TYPE_ORDER");
			String fieldOrder = (request.getAttribute("FIELD_ORDER")==null)?" jobDescription":(String)request.getAttribute("FIELD_ORDER");
			pageListSB = orderJobList(pageListSB, typeOrder, fieldOrder);

			// populate response with the right values			
			response.setAttribute(pageListSB);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ListJobs");
		} catch (Exception ex) {
			logger.error("Error while recovering all job definition", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1000", "component_scheduler_messages");
		}
	}
	
	
	private void newJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			JobInfo jobInfo = new JobInfo();
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			logger.error("Error while recovering objects for scheduling", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1001", "component_scheduler_messages");
		}
	}
	
	
	
	private void deleteJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
		        SchedulerServiceSupplier schedulerService=new SchedulerServiceSupplier();
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			String xmlSchedList = schedulerService.getJobSchedulationList(jobName, jobGroupName);
			SourceBean rowsSB_JSL = SourceBean.fromXMLString(xmlSchedList);
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
			   	DAOFactory.getDistributionListDAO().eraseAllRelatedDistributionListObjects(triggerName);
			   	String delResp = schedulerService.deleteSchedulation(triggerName, triggerGroup);
				SourceBean schedModRespSB_DS = SchedulerUtilities.getSBFromWebServiceResponse(delResp);
				if(schedModRespSB_DS==null) {
					throw new Exception("Imcomplete response returned by the Web service " +
							            "during schedule "+triggerName+" deletion");
				}	
				if(!SchedulerUtilities.checkResultOfWSCall(schedModRespSB_DS)){
					throw new Exception("Schedule "+triggerName+" not deleted by the Web Service");
				}
			}			
			// delete job	
			String resp_DJ = schedulerService.deleteJob(jobName, jobGroupName);
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
			logger.error("Error while deleting job", ex);
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
			if (sel_biobj_ids_str.equals(""))
				biobj_sel_now = new ArrayList();
			else{
				String[] sel_biobj_ids_arr = sel_biobj_ids_str.split(",");
				List biobjIdsFromRequest = Arrays.asList(sel_biobj_ids_arr);
				// update the job information
				Iterator iterBiobjIdsFromRequest = biobjIdsFromRequest.iterator();		
				while(iterBiobjIdsFromRequest.hasNext()) {					
					String biobjidStr = (String)iterBiobjIdsFromRequest.next();
					Integer biobjInt = Integer.valueOf(biobjidStr.substring(0, biobjidStr.lastIndexOf("__")));
					//adds new documents
					if(!biobjIds.contains(biobjInt)) {
						Integer biobjid = new Integer(biobjidStr.substring(0, biobjidStr.lastIndexOf("__")));
						IBIObjectDAO biobjectDAO = DAOFactory.getBIObjectDAO();
						IBIObjectParameterDAO ibiobjpardao = DAOFactory.getBIObjectParameterDAO();
						BIObject biobj = biobjectDAO.loadBIObjectById(biobjid);
						List bipars = ibiobjpardao.loadBIObjectParametersById(biobjid);
						biobj.setBiObjectParameters(bipars);
						biobj_sel_now.add(biobj);
					} else {
						Iterator iter_prev_biobj = biobjects.iterator();
						int index = 0;
						boolean flgExists = false;
						//preserves documents already existing
						while(iter_prev_biobj.hasNext()){
							index ++;
							BIObject biobj = (BIObject)iter_prev_biobj.next();
							String tmpID = biobj.getId().toString()+"__"+index;
							if(tmpID.equals(biobjidStr)) {
								biobj_sel_now.add(biobj);
								flgExists = true;
								continue;
							}
						}
						//adds new copy of document already existing
						if (!flgExists){
							Integer biobjid = new Integer(biobjidStr.substring(0, biobjidStr.lastIndexOf("__")));
							IBIObjectDAO biobjectDAO = DAOFactory.getBIObjectDAO();
							IBIObjectParameterDAO ibiobjpardao = DAOFactory.getBIObjectParameterDAO();
							BIObject biobj = biobjectDAO.loadBIObjectById(biobjid);
							List bipars = ibiobjpardao.loadBIObjectParametersById(biobjid);
							biobj.setBiObjectParameters(bipars);
							biobj_sel_now.add(biobj);
						}							
					}
				}
			}
			jobInfo.setBiobjects(biobj_sel_now);
			sessCont.setAttribute(SpagoBIConstants.JOB_INFO, jobInfo);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "JobDetail");
		} catch (Exception ex) {
			logger.error("Error while selecting documents", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1006", "component_scheduler_messages");
		}
	}
	
	
	
	
	private void saveJob(SourceBean request, SourceBean response) throws EMFUserError {
		try {
		    SchedulerServiceSupplier schedulerService=new SchedulerServiceSupplier();
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
			message.append("<SERVICE_REQUEST ");
			message.append(" jobName=\""+jobInfo.getJobName()+"\" ");
			message.append(" jobDescription=\""+jobInfo.getJobDescription()+"\" ");
			message.append(" jobGroupName=\""+jobGroupName+"\" ");
			message.append(" jobRequestRecovery=\"false\" ");
			message.append(" jobClass=\"it.eng.spagobi.tools.scheduler.jobs.ExecuteBIDocumentJob\" ");
			message.append(">");
			message.append("   <PARAMETERS>");
			List biobjs = jobInfo.getBiobjects();
			Iterator iterbiobj = biobjs.iterator();
			String doclabels = "";
			int index = 0;
			while(iterbiobj.hasNext()) {
				index ++;
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
				message.append("<PARAMETER name=\""+biobj.getLabel()+"__"+index+"\" value=\""+queryString+"\" />");
				doclabels += biobj.getLabel() +"__"+index+ ",";
			}
			if(doclabels.length()>0) {
				doclabels = doclabels.substring(0, doclabels.length()-1);
			}
			message.append("   	   <PARAMETER name=\"documentLabels\" value=\""+doclabels+"\" />");
			message.append("   </PARAMETERS>");
			message.append("</SERVICE_REQUEST>");
			// call the web service
			String servoutStr = schedulerService.defineJob(message.toString());
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
			logger.error("Error while saving job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1004", "component_scheduler_messages");
		}
	}

		

	
	private void getJobDetail(SourceBean request, SourceBean response) throws EMFUserError {
		try {
		    SchedulerServiceSupplier schedulerService=new SchedulerServiceSupplier();
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
	        // call we service
			String respStr = schedulerService.getJobDefinition(jobName, jobGroupName);
            SourceBean jobDetailSB = SchedulerUtilities.getSBFromWebServiceResponse(respStr);
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
			logger.error("Error while getting detail of the job", ex);
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
		List newBiObjects = new ArrayList();
		// iter over biobjects
		Iterator iterbiobjs = biobjects.iterator();
		int index = 0;
		while(iterbiobjs.hasNext()) {
			index ++;
			BIObject biobj = (BIObject)iterbiobjs.next();
			BIObject newBiObj = new BIObject();
			newBiObj = biobj;
			List biobjpars = biobj.getBiObjectParameters();
			List newBiobjpars = new ArrayList();
			// iter over parameters
			Iterator iterbiobjpars = biobjpars.iterator();
			while(iterbiobjpars.hasNext()) {
				BIObjectParameter biobjpar = (BIObjectParameter)iterbiobjpars.next();
				String nameParInRequest = "par_" + biobj.getId() +"_" + index + "_" + biobjpar.getParameterUrlName();
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
					newBiobjpars.add(biobjpar);
				}				
			}
			newBiObj.setBiObjectParameters(null);
			newBiObj.setBiObjectParameters(newBiobjpars);
			newBiObjects.add(newBiObj);
		}
		jobInfo.setBiobjects(newBiObjects);
	}
	
	
	private SourceBean orderJobList(SourceBean pageListSB, String typeOrder, String fieldOrder) throws EMFUserError {
		try {
			List tmpAllList = pageListSB.getAttributeAsList("ROWS.ROW");
			List tmpFieldList = new ArrayList();
			
			if (tmpAllList != null){
				for (int i=0; i < tmpAllList.size(); i++){
					SourceBean tmpSB = (SourceBean)tmpAllList.get(i);
					tmpFieldList.add(tmpSB.getAttribute(fieldOrder.trim()));
				}
			}
			Object[] orderList = tmpFieldList.toArray();
			Arrays.sort(orderList);
			//create a source bean with the list ordered
			SourceBean orderedPageListSB  = new SourceBean("PAGED_LIST");
			SourceBean rows = new SourceBean("ROWS");
			int i = 0;
			if (typeOrder.trim().equals("DESC"))				 
					i = tmpFieldList.size()-1;
			
			while (tmpFieldList != null && tmpFieldList.size() > 0){	
					SourceBean newSB = (SourceBean)tmpAllList.get(tmpFieldList.indexOf(orderList[i]));					
					rows.setAttribute(newSB);
					//remove elements from temporary lists
					tmpAllList.remove(tmpFieldList.indexOf(orderList[i]));
					tmpFieldList.remove(tmpFieldList.indexOf(orderList[i]));
					if (typeOrder.trim().equals("DESC"))
						i--;
					else
						i++;
			}
			orderedPageListSB.setAttribute(rows);
			return orderedPageListSB;
		} catch (Exception ex) {
			logger.error("Error while recovering all job definition", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "errors.1000", "component_scheduler_messages");
		}
	}
	
}	
	
	
