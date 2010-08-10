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
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.to.JobInfo;
import it.eng.spagobi.scheduler.to.SaveInfo;
import it.eng.spagobi.scheduler.to.TriggerInfo;
import it.eng.spagobi.scheduler.utils.SchedulerUtilities;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TriggerManagementModule extends AbstractModule {
    	
	private RequestContainer reqCont = null;
	private SessionContainer sessCont = null;
	private AdapterAxisProxy proxy = null;
	private String sbiconturl = null; 
	
	public void init(SourceBean config) {	
		
	}
	
	public void service(SourceBean request, SourceBean response) throws Exception { 
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
				           "service","begin of trigger management service =" +message);
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
			if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_JOB_SCHEDULES)) {
				getTriggersForJob(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_NEW_SCHEDULE)) {
				newScheduleForJob(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_SAVE_SCHEDULE)) {
				saveScheduleForJob(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_DELETE_SCHEDULE)) {
				deleteSchedule(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_GET_SCHEDULE_DETAIL)) {
				getSchedule(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.MESSAGE_RUN_SCHEDULE)) {
				runSchedule(request, response);
			} 
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
			           			"service", "Error while executing trigger management service", ex);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		           			"service", "end of trigger management service =" +message);
	}
	
	
	
	private void runSchedule(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			getSchedule(request, response);
			TriggerInfo tInfo = (TriggerInfo)sessCont.getAttribute(SpagoBIConstants.TRIGGER_INFO);
			StringBuffer message = createMessageSaveSchedulation(tInfo, true);
			// call the web service to create the schedule
			String servoutStr = proxy.service(message.toString());
			SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(servoutStr);
			SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
			if(execOutSB!=null) {
				String outcome = (String)execOutSB.getAttribute("outcome");
				if(outcome.equalsIgnoreCase("fault"))
					throw new Exception("Immediate Trigger not created by the web service");
			}
			// fill spago response
			response.updAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToTriggerList");
			response.setAttribute(SpagoBIConstants.JOB_GROUP_NAME, jobGroupName);
			response.setAttribute(SpagoBIConstants.JOB_NAME, jobName);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                    			"runSchedule","Error while create immediate trigger ", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	private void deleteSchedule(SourceBean request, SourceBean response) throws EMFUserError {
		String jobName = (String)request.getAttribute("jobName");
		String jobGroupName = (String)request.getAttribute("jobGroupName");
		String triggerName = (String) request.getAttribute("triggerName");
		String triggerGroup = (String) request.getAttribute("triggerGroup");
		try {
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"deleteSchedulation\" ");
			message.append(" triggerName=\"" + triggerName + "\" ");
			message.append(" triggerGroup=\"" + triggerGroup + "\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String resp = proxy.service(message.toString());
			SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(resp);
			SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
			if(execOutSB!=null) {
				String outcome = (String)execOutSB.getAttribute("outcome");
				if(outcome.equalsIgnoreCase("fault"))
					throw new Exception("Trigger not deleted by the service");
			}
			// fill spago response
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToTriggerList");
			response.setAttribute(SpagoBIConstants.JOB_GROUP_NAME, jobGroupName);
			response.setAttribute(SpagoBIConstants.JOB_NAME, jobName);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                                "deleteSchedule","Error while deleting schedule (trigger) ", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	private void getSchedule(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			String jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			String triggerName = (String) request.getAttribute("triggerName");
			String triggerGroup = (String) request.getAttribute("triggerGroup");
			
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationDefinition\" ");
			message.append(" triggerName=\"" + triggerName + "\" ");
			message.append(" triggerGroup=\"" + triggerGroup + "\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String respStr_gt = proxy.service(message.toString());
	        SourceBean respSB_gt = SchedulerUtilities.getSBFromWebServiceResponse(respStr_gt);			
			SourceBean triggerDetailSB = (SourceBean)respSB_gt.getAttribute("TRIGGER_DETAILS");
			
			message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobDefinition\" ");
			message.append(" jobName=\""+jobName+"\" ");
			message.append(" jobGroup=\""+jobGroupName+"\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String respStr_gj = proxy.service(message.toString());
            SourceBean respSB_gj = SchedulerUtilities.getSBFromWebServiceResponse(respStr_gj);			
			SourceBean jobDetailSB = (SourceBean)respSB_gj.getAttribute("JOB_DETAIL");
			
			
			if(triggerDetailSB!=null) {
				if(jobDetailSB!=null){
					TriggerInfo tInfo = SchedulerUtilities.getTriggerInfoFromTriggerSourceBean(triggerDetailSB, jobDetailSB);
					sessCont.setAttribute(SpagoBIConstants.TRIGGER_INFO, tInfo);
				} else {
					throw new Exception("Detail not recovered for job " + jobName + 
							            "associated to trigger " + triggerName);
				}
			} else {
				throw new Exception("Detail not recovered for trigger " + triggerName);
			}
			
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "TriggerDetail");
			
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getSchedule","Error while getting detail of the schedule(trigger)", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	} 
	
	
	private void saveScheduleForJob(SourceBean request, SourceBean response) throws EMFUserError {
		try{
			TriggerInfo triggerInfo = (TriggerInfo)sessCont.getAttribute(SpagoBIConstants.TRIGGER_INFO);
			JobInfo jobInfo = triggerInfo.getJobInfo();
			String jobName = jobInfo.getJobName();
			String jobGroupName = jobInfo.getJobGroupName();
			
			String triggername = (String)request.getAttribute("triggername");	
			String triggerDescription  = (String)request.getAttribute("triggerdescription");	
			String startdate  = (String)request.getAttribute("startdate");	
			String starttime = (String)request.getAttribute("starttime");	
			String chronstr = (String)request.getAttribute("chronstring");
			String enddate = (String)request.getAttribute("enddate");	
			String endtime = (String)request.getAttribute("endtime");	
			String repeatinterval = (String)request.getAttribute("repeatInterval");
			triggerInfo.setEndDate(enddate);
			triggerInfo.setEndTime(endtime);
			triggerInfo.setRepeatInterval(repeatinterval);
			triggerInfo.setStartDate(startdate);
			triggerInfo.setStartTime(starttime);
			triggerInfo.setChronString(chronstr);
			triggerInfo.setTriggerDescription(triggerDescription);
			triggerInfo.setTriggerName(triggername);
			
			// check for input validation errors 
			if(!this.getErrorHandler().isOKByCategory(EMFErrorCategory.VALIDATION_ERROR)) {
				List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
				response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "TriggerDetail");
				return;
			}
			
			Map saveOptions = new HashMap();
			List biobjIds = jobInfo.getBiobjectIds();
			Iterator iterBiobjIds = biobjIds.iterator();
			while(iterBiobjIds.hasNext()){
				SaveInfo sInfo = new SaveInfo();
				Integer biobId = (Integer)iterBiobjIds.next();
				String saveassnap = (String)request.getAttribute("saveassnapshot_"+biobId);	
				if(saveassnap!=null) {
					sInfo.setSaveAsSnapshot(true);
					String snapname = (String)request.getAttribute("snapshotname_"+biobId);	
					sInfo.setSnapshotName(snapname);
					String snapdescr = (String)request.getAttribute("snapshotdescription_"+biobId);
					sInfo.setSnapshotDescription(snapdescr);
					String snaphistlength = (String)request.getAttribute("snapshothistorylength_"+biobId);
					sInfo.setSnapshotHistoryLength(snaphistlength);
				}
				String saveasdoc = (String)request.getAttribute("saveasdocument_"+biobId);	
				if(saveasdoc!=null) {
					sInfo.setSaveAsDocument(true);
					String docname = (String)request.getAttribute("documentname_"+biobId);	
					sInfo.setDocumentName(docname);
					String docdescr = (String)request.getAttribute("documentdescription_"+biobId);	
					sInfo.setDocumentDescription(docdescr);
					String functIdsConcat = "";
					List functIds = request.getAttributeAsList("tree_"+biobId+"_funct_id");	
					Iterator iterFunctIds = functIds.iterator();
					while(iterFunctIds.hasNext()) {
						String idFunct = (String)iterFunctIds.next();
						functIdsConcat += idFunct;
						if(iterFunctIds.hasNext()){
							functIdsConcat += ",";
						}
					}
					sInfo.setFunctionalityIds(functIdsConcat);
				}
				String sendmail = (String)request.getAttribute("sendmail_"+biobId);	
				if(sendmail!=null) {
					sInfo.setSendMail(true);
					String mailtos = (String)request.getAttribute("mailtos_"+biobId);	
					sInfo.setMailTos(mailtos);
				}
				saveOptions.put(biobId, sInfo);
			}
			
			triggerInfo.setSaveOptions(saveOptions);
			
			StringBuffer message = createMessageSaveSchedulation(triggerInfo, false);
			
			// call the web service to create the schedule
			String servoutStr = proxy.service(message.toString());
			SourceBean schedModRespSB = SchedulerUtilities.getSBFromWebServiceResponse(servoutStr);
			SourceBean execOutSB = (SourceBean)schedModRespSB.getAttribute("EXECUTION_OUTCOME");
			if(execOutSB!=null) {
				String outcome = (String)execOutSB.getAttribute("outcome");
				if(outcome.equalsIgnoreCase("fault"))
					throw new Exception("Trigger "+triggername+" not created by the web service");
			}
			
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ReturnToTriggerList");
			response.setAttribute(SpagoBIConstants.JOB_GROUP_NAME, jobGroupName);
			response.setAttribute(SpagoBIConstants.JOB_NAME, jobName);
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "saveScheduleForJob", "Error while saving schedule for job", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void newScheduleForJob(SourceBean request, SourceBean response) throws EMFUserError {
		String jobName = "";
		try{
			jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			TriggerInfo ti = new TriggerInfo();
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
				ti.setJobInfo(jobInfo);
				Map saveOptions = new HashMap();
				List biobjids = jobInfo.getBiobjectIds();
				Iterator iterbiobjids = biobjids.iterator();
				while(iterbiobjids.hasNext()) {
					Integer idobj = (Integer)iterbiobjids.next();
					saveOptions.put(idobj, new SaveInfo());
				}
				ti.setSaveOptions(saveOptions);
			} else {
				throw new Exception("Cannot recover job " + jobName);
			}		
			
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			
			sessCont.setAttribute(SpagoBIConstants.TRIGGER_INFO, ti);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "TriggerDetail");
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "newScheduleForJob", "Error while creating a new schedule for job " + jobName, ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	private void getTriggersForJob(SourceBean request, SourceBean response) throws EMFUserError {
		String jobName = "";
		try{
			// create the sourcebean of the list
			SourceBean pageListSB  = new SourceBean("PAGED_LIST");
			jobName = (String)request.getAttribute("jobName");
			String jobGroupName = (String)request.getAttribute("jobGroupName");
			StringBuffer message = new StringBuffer();
			message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"getJobSchedulationList\" ");
			message.append(" jobName=\""+jobName+"\" ");
			message.append(" jobGroup=\""+jobGroupName+"\" ");
			message.append(">");
			message.append("</SERVICE_REQUEST>");
			String serviceResp = proxy.service(message.toString());
			SourceBean respSB = SourceBean.fromXMLString(serviceResp);
			SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
			SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
			SourceBean rowsSB = (SourceBean)schedModRespSB.getAttribute("ROWS");
			if(rowsSB==null) {
				rowsSB = new SourceBean("ROWS");
			}
			// fill the list sourcebean
			pageListSB.setAttribute(rowsSB);
			// populate response with the right values
			response.setAttribute(pageListSB);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ListTriggers");	
		} catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getTriggersForJob","Error while recovering triggers of the job " + jobName, ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	
	private StringBuffer createMessageSaveSchedulation(TriggerInfo tInfo, boolean runImmediately) {
		StringBuffer message = new StringBuffer();
		JobInfo jInfo = tInfo.getJobInfo();
		Map saveOptions = tInfo.getSaveOptions();
		Set biobjids_so =  saveOptions.keySet();
		Iterator iterbiobjids_s = biobjids_so.iterator();
		
		message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"scheduleJob\" ");
		message.append(" jobName=\""+jInfo.getJobName()+"\" ");
		message.append(" jobGroup=\""+jInfo.getJobGroupName()+"\" ");
		if(runImmediately) {
			message.append(" runImmediately=\"true\" ");
		} else {
			message.append(" triggerName=\""+tInfo.getTriggerName()+"\" ");
			message.append(" triggerDescription=\""+tInfo.getTriggerDescription()+"\" ");
			message.append(" startDate=\""+tInfo.getStartDate()+"\" ");
			message.append(" startTime=\""+tInfo.getStartTime()+"\" ");
			message.append(" chronString=\""+tInfo.getChronString()+"\" ");
			String enddate = tInfo.getEndDate();
			String endtime = tInfo.getEndTime();
			if(!enddate.trim().equals("")){
				message.append(" endDate=\""+enddate+"\" ");
				if(!endtime.trim().equals("")){
					message.append(" endTime=\""+endtime+"\" ");
				}
			}
		}
		String repeatinterval = tInfo.getRepeatInterval();
		if(!repeatinterval.trim().equals("")){
			message.append(" repeatInterval=\""+repeatinterval+"\" ");
		}	
		message.append(">");
		
		
		message.append("   <PARAMETERS>");
		while(iterbiobjids_s.hasNext()) {
			Integer biobjid_so =  (Integer)iterbiobjids_s.next();
			SaveInfo sInfo = (SaveInfo)saveOptions.get(biobjid_so);
			String saveOptString = "";
			if(sInfo.isSaveAsSnapshot()) {
				saveOptString += "saveassnapshot=true%26";
				if( (sInfo.getSnapshotName()!=null) && !sInfo.getSnapshotName().trim().equals("") ) {
					saveOptString += "snapshotname="+sInfo.getSnapshotName()+"%26";
				}
				if( (sInfo.getSnapshotDescription()!=null) && !sInfo.getSnapshotDescription().trim().equals("") ) {
					saveOptString += "snapshotdescription="+sInfo.getSnapshotDescription()+"%26";
				}
				if( (sInfo.getSnapshotHistoryLength()!=null) && !sInfo.getSnapshotHistoryLength().trim().equals("") ) {
					saveOptString += "snapshothistorylength="+sInfo.getSnapshotHistoryLength()+"%26";
				}
			}
			if(sInfo.isSaveAsDocument()) {
				saveOptString += "saveasdocument=true%26";
				if( (sInfo.getDocumentName()!=null) && !sInfo.getDocumentName().trim().equals("") ) {
					saveOptString += "documentname="+sInfo.getDocumentName()+"%26";
				}
				if( (sInfo.getDocumentDescription()!=null) && !sInfo.getDocumentDescription().trim().equals("") ) {
					saveOptString += "documentdescription="+sInfo.getDocumentDescription()+"%26";
				}
				if( (sInfo.getDocumentHistoryLength()!=null) && !sInfo.getDocumentHistoryLength().trim().equals("") ) {
					saveOptString += "documenthistorylength="+sInfo.getDocumentHistoryLength()+"%26";
				}
				if( (sInfo.getFunctionalityIds()!=null) && !sInfo.getFunctionalityIds().trim().equals("") ) {
					saveOptString += "functionalityids="+sInfo.getFunctionalityIds()+"%26";
				}
			}
			if(sInfo.isSendMail()) {
				saveOptString += "sendmail=true%26";
				if( (sInfo.getMailTos()!=null) && !sInfo.getMailTos().trim().equals("") ) {
					saveOptString += "mailtos="+sInfo.getMailTos()+"%26";
				}
			}
			message.append("   	   <PARAMETER name=\"biobject_id_"+biobjid_so+"\" value=\""+saveOptString+"\" />");
		}
		
		message.append("   </PARAMETERS>");
		message.append("</SERVICE_REQUEST>");
		
		return message;
	}
	
	
	
	
	
}	
	
	
