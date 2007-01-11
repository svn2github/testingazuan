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
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;


public class SchedulerModule extends AbstractModule {
    
	private Scheduler scheduler = null;
	
	private EMFErrorHandler errorHandler;
	
	public void init(SourceBean config) {	}

	public void service(SourceBean request, SourceBean response) throws Exception { 
		try {
			errorHandler = getErrorHandler();
			scheduler = StdSchedulerFactory.getDefaultScheduler(); 
			String task = (String)request.getAttribute("task");
			if(task.equalsIgnoreCase("getJobList")) {
				getJobList(request, response);
			} else if(task.equalsIgnoreCase("getJobDefinition")){
				getJobDefinition(request, response);
			} else if(task.equalsIgnoreCase("existJobDefinition")){
				existJobDefinition(request, response);
			} else if(task.equalsIgnoreCase("getJobSchedulationList")){
				getJobSchedulationList(request, response);
			} else if(task.equalsIgnoreCase("getJobSchedulationDefinition")){
				getJobSchedulationDefinition(request, response);
			} else if(task.equalsIgnoreCase("defineJob")){
				defineJob(request, response);
			} else if(task.equalsIgnoreCase("scheduleJob")){
				scheduleJob(request, response);
			} else if(task.equalsIgnoreCase("deleteJob")){
				deleteJob(request, response);
			} else if(task.equalsIgnoreCase("deleteSchedulation")){
				deleteSchedulation(request, response);
			}
		} catch (EMFUserError e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
								"service", "EMFUserError captured", e);
			//errorHandler.addError(e);
		} catch (Exception e) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
								  "service", "Generic error", e);
			//errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, "100"));
		}
	}
	
	private void getJobList(SourceBean request, SourceBean response) throws EMFUserError, Exception {
		List toReturn = new ArrayList();
		String[] groups = scheduler.getJobGroupNames();
		if (groups == null || groups.length == 0) {
			SpagoBITracer.warning("SCHEDULER", this.getClass().getName(), "getJobList", 
					"No job groups defined!");
		} else {
			for (int i = 0; i < groups.length; i++) {
				String group = groups[i];
				String[] jobNames = scheduler.getJobNames(group);
				if (jobNames == null || jobNames.length == 0) {
					SpagoBITracer.warning("SCHEDULER", this.getClass().getName(), 
							"getJobList", "No job defined for group " + group + "!");
				} else {
					for (int j = 0; j < jobNames.length; j++) {
						JobDetail aJob = scheduler.getJobDetail(jobNames[j], group);
						toReturn.add(aJob);
					}
				}
			}
		}
		loadJobListIntoResponse(response, toReturn);
	}
	
	private void loadJobListIntoResponse(SourceBean response, List toReturn) throws SourceBeanException {
		StringBuffer buffer = new StringBuffer("<ROWS>");
		Iterator it = toReturn.iterator();
		while (it.hasNext()) {
			JobDetail job = (JobDetail) it.next();
			String jobName = job.getName();
			String jobGroupName = job.getGroup();
			String jobDescription = job.getDescription();
			String jobClassName = job.getJobClass().getName();
			String jobDurability = job.isDurable() ? "true" : "false";
			String jobRequestRecovery = job.requestsRecovery() ? "true" : "false";
			String jobVolatility = job.isVolatile() ? "true" : "false";
			buffer.append("<ROW ");
			buffer.append(" jobName=\"" + jobName != null ? jobName : "" + "\"");
			buffer.append(" jobGroupName=\"" + jobGroupName != null ? jobGroupName : "" + "\"");
			buffer.append(" jobDescription=\"" + jobDescription != null ? jobDescription : "" + "\"");
			buffer.append(" jobClass=\"" + jobClassName != null ? jobClassName : "" + "\"");
			buffer.append(" jobDurability=\"" + jobDurability);
			buffer.append(" jobRequestRecovery=\"" + jobRequestRecovery);
			buffer.append(" jobVolatility=\"" + jobVolatility);
			buffer.append(" />");
		}
		buffer.append("</ROWS>");
		SourceBean listSourceBean = SourceBean.fromXMLString(buffer.toString());
		response.setAttribute(listSourceBean);
	}

	private void getJobDefinition(SourceBean request, SourceBean response) throws EMFUserError, Exception {
		String jobName = (String) request.getAttribute("jobName");
		String jobGroup = (String) request.getAttribute("jobGroup");
		if (jobName == null || jobName.trim().equals("")) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "getJobDefinition", 
				"Missing job name request parameter!");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001");
		}
		if (jobGroup == null || jobGroup.trim().equals("")) {
			SpagoBITracer.major("SCHEDULER", this.getClass().getName(), "getJobDefinition", 
				"Missing job group name! Using default group...");
			jobGroup = Scheduler.DEFAULT_GROUP;
		}
		JobDetail aJob = scheduler.getJobDetail(jobName, jobGroup);
		if (aJob == null) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "getJobDefinition", 
				"Job with name '" + jobName + "' not found in group '" + jobGroup + "'!");
			Vector v = new Vector();
			v.add(jobName);
			v.add(jobGroup);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1002", v);
		}
		loadJobDetailIntoResponse(response, aJob);
	}
	
	private void loadJobDetailIntoResponse(SourceBean response, JobDetail job) throws SourceBeanException {
		StringBuffer buffer = new StringBuffer("<JOB_DETAIL ");
		String jobName = job.getName();
		String jobGroupName = job.getGroup();
		String jobDescription = job.getDescription();
		String jobClassName = job.getJobClass().getName();
		String jobDurability = job.isDurable() ? "true" : "false";
		String jobRequestRecovery = job.requestsRecovery() ? "true" : "false";
		String jobVolatility = job.isVolatile() ? "true" : "false";
		JobDataMap jobDataMap = job.getJobDataMap();
		buffer.append(" jobName=\"" + (jobName != null ? jobName : "") + "\"");
		buffer.append(" jobGroupName=\"" + (jobGroupName != null ? jobGroupName : "") + "\"");
		buffer.append(" jobDescription=\"" + (jobDescription != null ? jobDescription : "") + "\"");
		buffer.append(" jobClass=\"" + (jobClassName != null ? jobClassName : "") + "\"");
		buffer.append(" jobDurability=\"" + jobDurability + "\"");
		buffer.append(" jobRequestRecovery=\"" + jobRequestRecovery + "\"");
		buffer.append(" jobVolatility=\"" + jobVolatility + "\"");
		buffer.append(" >");
		buffer.append("<JOB_PARAMETERS>");
		if (jobDataMap != null && !jobDataMap.isEmpty()) {
			String[] keys = jobDataMap.getKeys();
			if (keys != null && keys.length > 0) {
				for (int i = 0; i < keys.length; i++) {
					buffer.append("<JOB_PARAMETER ");
					String key = keys[i];
					String value = jobDataMap.getString(key);
					if (value == null) {
						SpagoBITracer.warning("SCHEDULER", this.getClass().getName(), "loadJobDetailIntoResponse", 
						"Job parameter '" + key + "' has no String value!!");	
					}
					buffer.append(" name=\"" + key + "\"");
					buffer.append(" value=\"" + value + "\"");
					buffer.append(" />");
				}
			}
		}
		buffer.append("</JOB_PARAMETERS>");
		buffer.append("</JOB_DETAIL>");
		SourceBean detailSourceBean = SourceBean.fromXMLString(buffer.toString());
		response.setAttribute(detailSourceBean);
	}

	private void existJobDefinition(SourceBean request, SourceBean response) throws EMFUserError, Exception {
		String jobName = (String) request.getAttribute("jobName");
		String jobGroup = (String) request.getAttribute("jobGroup");
		if (jobName == null || jobName.trim().equals("")) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "existJobDefinition", 
				"Missing job name request parameter!");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001");
		}
		if (jobGroup == null || jobGroup.trim().equals("")) {
			SpagoBITracer.major("SCHEDULER", this.getClass().getName(), "existJobDefinition", 
				"Missing job group name! Using default group...");
			jobGroup = Scheduler.DEFAULT_GROUP;
		}
		JobDetail aJob = scheduler.getJobDetail(jobName, jobGroup);
		StringBuffer buffer = new StringBuffer("<JOB_EXISTANCE  ");
		if (aJob == null) {
			buffer.append(" exists=\"false\" />");
		} else {
			buffer.append(" exists=\"true\" />");
		}
		SourceBean existSourceBean = SourceBean.fromXMLString(buffer.toString());
		response.setAttribute(existSourceBean);
	}
	
	
	private void getJobSchedulationList(SourceBean request, SourceBean response) 
							throws EMFUserError, SchedulerException, SourceBeanException{
		String jobName = (String) request.getAttribute("jobName");
		String jobGroup = (String) request.getAttribute("jobGroup");
		if (jobName == null || jobName.trim().equals("")) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "getJobDefinition", 
				"Missing job name request parameter!");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001");
		}
		if (jobGroup == null || jobGroup.trim().equals("")) {
			SpagoBITracer.major("SCHEDULER", this.getClass().getName(), "getJobDefinition", 
				"Missing job group name! Using default group...");
			jobGroup = Scheduler.DEFAULT_GROUP;
		}
		Trigger[] triggers = scheduler.getTriggersOfJob(jobName, jobGroup);
		loadTriggersIntoResponse(response, triggers);
	}
	
	private void loadTriggersIntoResponse(SourceBean response, Trigger[] triggers) throws SourceBeanException {
		StringBuffer buffer = new StringBuffer("<ROWS>");
		if (triggers != null && triggers.length > 0) {
			for (int i = 0; i < triggers.length; i++) {
				Trigger trigger = triggers[i];
				buffer.append("<ROW ");
				String triggerName = trigger.getName();
				String triggerGroup = trigger.getGroup();
				String triggerDescription = trigger.getDescription();
				String triggerCalendarName = trigger.getCalendarName();
				Date triggerStartTime = trigger.getStartTime();
				String triggerStartTimeStr = triggerStartTime != null ? triggerStartTime.toString(): "";
				Date triggerEndTime = trigger.getEndTime();
				String triggerEndTimeStr = triggerEndTime != null ? triggerEndTime.toString(): "";
				buffer.append(" triggerName=\"" + (triggerName != null ? triggerName : "") + "\"");
				buffer.append(" triggerGroup=\"" + (triggerGroup != null ? triggerGroup : "") + "\"");
				buffer.append(" triggerDescription=\"" + (triggerDescription != null ? triggerDescription : "") + "\"");
				buffer.append(" triggerCalendarName=\"" + (triggerCalendarName != null ? triggerCalendarName : "") + "\"");
				buffer.append(" triggerStartTime=\"" + triggerStartTimeStr + "\"");
				buffer.append(" triggerEndTime=\"" + triggerEndTimeStr + "\"");
				buffer.append(" />");
			}
		}
		buffer.append("</ROWS>");
		SourceBean listSourceBean = SourceBean.fromXMLString(buffer.toString());
		response.setAttribute(listSourceBean);
	}

	private void getJobSchedulationDefinition(SourceBean request, SourceBean response) 
									throws EMFUserError, SchedulerException, SourceBeanException{
		String triggerName = (String) request.getAttribute("triggerName");
		String triggerGroup = (String) request.getAttribute("triggerGroup");
		if (triggerName == null || triggerName.trim().equals("")) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "getJobSchedulationDefinition", 
				"Missing trigger name request parameter!");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1003");
		}
		if (triggerGroup == null || triggerGroup.trim().equals("")) {
			SpagoBITracer.major("SCHEDULER", this.getClass().getName(), "getJobSchedulationDefinition", 
				"Missing trigger group name! Using default group...");
			triggerGroup = Scheduler.DEFAULT_GROUP;
		}
		SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerName, triggerGroup);
		if (trigger == null) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "getJobSchedulationDefinition", 
				"Trigger with name '" + triggerName + "' not found in group '" + triggerGroup + "'!");
			Vector v = new Vector();
			v.add(triggerName);
			v.add(triggerGroup);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1004", v);
		}
		loadTriggerIntoResponse(response, trigger);
	}

	private void loadTriggerIntoResponse(SourceBean response, SimpleTrigger trigger) throws SourceBeanException {
		StringBuffer buffer = new StringBuffer("<TRIGGER_DETAILS ");
		buffer.append(" ");
		String triggerName = trigger.getName();
		String triggerDescription = trigger.getDescription();
		//String triggerCalendarName = trigger.getCalendarName();
		
		Date triggerStartTime = trigger.getStartTime();
		String triggerStartDateStr = "";
		String triggerStartTimeStr = "";
		if (triggerStartTime != null) {
			Calendar startCal = new GregorianCalendar();
			startCal.setTime(triggerStartTime);
			// date format: dd/mm/yyyy
			int day = startCal.get(Calendar.DAY_OF_MONTH);
			int month = startCal.get(Calendar.MONTH);
			int year = startCal.get(Calendar.YEAR);
			triggerStartDateStr = ((day < 10) ? "0" : "") + day + 
						"/" + 
						((month + 1 < 10) ? "0" : "") + (month + 1) + 
						"/" + 
						year;
			int hour = startCal.get(Calendar.HOUR_OF_DAY);
			int minute = startCal.get(Calendar.MINUTE);
			// hour format: hh:mm
			triggerStartTimeStr = ((hour < 10) ? "0" : "") + hour + ":" + ((minute < 10) ? "0" : "") + minute;
		}
		
		Date triggerEndTime = trigger.getEndTime();
		String triggerEndDateStr = "";
		String triggerEndTimeStr = "";
		if (triggerEndTime != null) {
			Calendar endCal = new GregorianCalendar();
			endCal.setTime(triggerEndTime);
			// date format: dd/mm/yyyy
			int day = endCal.get(Calendar.DAY_OF_MONTH);
			int month = endCal.get(Calendar.MONTH);
			int year = endCal.get(Calendar.YEAR);
			triggerEndDateStr = ((day < 10) ? "0" : "") + day + 
						"/" + 
						((month + 1 < 10) ? "0" : "") + (month + 1) + 
						"/" + 
						year;
			int hour = endCal.get(Calendar.HOUR_OF_DAY);
			int minute = endCal.get(Calendar.MINUTE);
			// hour format: hh:mm
			triggerEndTimeStr = ((hour < 10) ? "0" : "") + hour + ":" + ((minute < 10) ? "0" : "") + minute;
		}
		
		String triggerRepeatInterval = new Long(trigger.getRepeatInterval()).toString();
		buffer.append(" triggerName=\"" + (triggerName != null ? triggerName : "") + "\"");
		buffer.append(" triggerDescription=\"" + (triggerDescription != null ? triggerDescription : "") + "\"");
		//buffer.append(" triggerCalendarName=\"" + (triggerCalendarName != null ? triggerCalendarName : "") + "\"");
		buffer.append(" triggerStartDate=\"" + triggerStartDateStr + "\"");
		buffer.append(" triggerStartTime=\"" + triggerStartTimeStr + "\"");
		buffer.append(" triggerEndDate=\"" + triggerEndDateStr + "\"");
		buffer.append(" triggerEndTime=\"" + triggerEndTimeStr + "\"");
		buffer.append(" triggerRepeatInterval=\"" + triggerRepeatInterval + "\"");
		
		JobDataMap jdm = trigger.getJobDataMap();
		String queryStr = jdm.getString("parameters");
		buffer.append(" queryStr=\"" + queryStr + "\"");
		
		String storeoutput = jdm.getString("storeoutput");
		if (storeoutput != null && storeoutput.equalsIgnoreCase("true")) {
			buffer.append(" storeoutput=\"true\"");
			buffer.append(" storename=\"" + jdm.getString("storename") + "\"");
			buffer.append(" storedescription=\"" + jdm.getString("storedescription") + "\"");
			buffer.append(" lengthhistory=\"" + jdm.getString("lengthhistory") + "\"");
			String storeassnapshot = jdm.getString("storeassnapshot");
			if (storeassnapshot != null && storeassnapshot.equalsIgnoreCase("true")) {
				buffer.append(" storeassnapshot=\"true\"");
			} else {
				buffer.append(" storeasdocument=\"true\"");
				buffer.append(" pathdocument=\"" + jdm.getString("pathdocument") + "\"");
			}
		} else {
			buffer.append(" storeoutput=\"false\"");
		}
		buffer.append(" />");
		SourceBean triggerSB = SourceBean.fromXMLString(buffer.toString());
		response.setAttribute(triggerSB);
	}

	
	
	
	
	
	
	private void defineJob(SourceBean request, SourceBean response) {
		StringBuffer servreponse = new StringBuffer();
		try{
			servreponse.append("<EXECUTION_OUTCOME ");
			// READ REQUEST
			String jobName = (String)request.getAttribute("jobName");
			String jobgroupName = (String)request.getAttribute("jobGroupName");
			if(jobgroupName==null)
				jobgroupName = Scheduler.DEFAULT_GROUP;
			String jobDescription = (String)request.getAttribute("jobDescription");
			if(jobDescription==null)
				jobDescription = "";
			String jobRequestRecoveryStr = (String)request.getAttribute("jobRequestRecovery");
			boolean jobRequestRecovery = false;
			if((jobRequestRecoveryStr!=null) && (jobRequestRecoveryStr.trim().equalsIgnoreCase("true")))
				jobRequestRecovery = true;
			SourceBean jobParameters = (SourceBean)request.getAttribute("PARAMETERS");
			// transform parameters sourcebean into JobDataMap structure and set it into the jobDetail
			JobDataMap jdm = getJobDataMap(jobParameters);
			// get the job class
			String jobClassName = (String)request.getAttribute("jobClass");
			Class jobClass = null;
			try {
				jobClass = Class.forName(jobClassName);
			} catch (ClassNotFoundException e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
						               "defineJob", "Class '" + jobClassName + "' not found for job with name '" + jobName + "' of group '" + jobgroupName + "'!");
				throw e;
			}
			// CREATE JOB DETAIL 
			JobDetail jobDetail = new JobDetail();
			jobDetail.setName(jobName);
			jobDetail.setGroup(jobgroupName);
			jobDetail.setDescription(jobDescription);
			jobDetail.setDurability(true);
			jobDetail.setVolatility(false);
			jobDetail.setRequestsRecovery(jobRequestRecovery);
			jobDetail.setJobDataMap(jdm);
			jobDetail.setJobClass(jobClass);
			// ADD JOB
			try {
				scheduler.addJob(jobDetail, false);
			} catch (SchedulerException e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
						               "defineJob", "Error while adding job to the scheduler", e);
				throw e;
			}
			servreponse.append("outcome=\"perform\"/>");
		} catch (Exception e) {
			servreponse.append("outcome=\"fault\"/>");
		}
		// put into response the outcome of the service
		try{
			SourceBean outcomeSB = SourceBean.fromXMLString(servreponse.toString());
			response.setAttribute(outcomeSB);
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
		                        "defineJob", "Error while filling response with the service outcome", e);
		}
	}

	
	
	
	
	
	
	
	private void scheduleJob(SourceBean request, SourceBean response) {
		StringBuffer servreponse = new StringBuffer();
		try{
			servreponse.append("<EXECUTION_OUTCOME ");
			String triggerName = (String) request.getAttribute("triggerName");
			String triggerDescription = (String) request.getAttribute("triggerDescription");
			String triggerGroup = (String) request.getAttribute("triggerGroup");
			if(triggerGroup==null)
				triggerGroup = Scheduler.DEFAULT_GROUP;
			String jobName = (String) request.getAttribute("jobName");
			String jobGroup = (String) request.getAttribute("jobGroup");
			if(jobGroup==null)
				jobGroup = Scheduler.DEFAULT_GROUP;
			// recover scheduling parameters
			SourceBean jobParameters = (SourceBean)request.getAttribute("PARAMETERS");
			// transform parameters sourcebean into JobDataMap structure and set it into the jobDetail
			JobDataMap jdm = getJobDataMap(jobParameters);
			// recover and transform dates
			// get the start date param (format gg/mm/yyyy) and start time (format hh:mm)
			String startDateStr = (String)request.getAttribute("startDate");
			String startTimeStr = (String)request.getAttribute("startTime");
			String startDay = startDateStr.substring(0, 2);
			String startMonth = startDateStr.substring(3, 5);
			String startYear = startDateStr.substring(6);
			Calendar startCal = new GregorianCalendar(new Integer(startYear).intValue(), 
					                                  new Integer(startMonth).intValue()-1, 
					                                  new Integer(startDay).intValue());
			if(startTimeStr!=null) {
				String startHour = startTimeStr.substring(0, 2);
				String startMinute = startTimeStr.substring(3);
				startCal.set(startCal.HOUR_OF_DAY, new Integer(startHour).intValue());
				startCal.set(startCal.MINUTE, new Integer(startMinute).intValue());
			}
			Date startDate = startCal.getTime();
			// get the end date param (format gg/mm/yyyy) and end time (format hh:mm)
			Date endDate = null;
			String endDateStr = (String)request.getAttribute("endDate");
			if(endDateStr!=null){
				String endDay = endDateStr.substring(0, 2);
				String endMonth = endDateStr.substring(3, 5);
				String endYear = endDateStr.substring(6);
				Calendar endCal = new GregorianCalendar(new Integer(endYear).intValue(), 
					                           new Integer(endMonth).intValue()-1, 
					                           new Integer(endDay).intValue());
				String endTimeStr = (String)request.getAttribute("endTime");
				if(endTimeStr!=null) {
					String endHour = endTimeStr.substring(0, 2);
					String endMinute = endTimeStr.substring(3);
					endCal.set(endCal.HOUR_OF_DAY, new Integer(endHour).intValue());
					endCal.set(endCal.MINUTE, new Integer(endMinute).intValue());
				}
				endDate = endCal.getTime();
			}
			// get the repeat interval
			long repeatInterval = -1;
			String repeatIntervalStr = (String) request.getAttribute("repeatInterval");
			if( (repeatIntervalStr!=null) && !repeatIntervalStr.trim().equals("") ) {
				repeatInterval = Long.parseLong(repeatIntervalStr);
			}
			// get the repeat count 
			int repeatCount = 0;
			String repeatCountStr = (String) request.getAttribute("repeatCount");
			if( (repeatCountStr!=null) && !repeatCountStr.trim().equals("") ) {
				repeatCount = Integer.parseInt(repeatCountStr);
			} else {
				long startTimeL = startDate.getTime();
				if(endDate!=null) {
					long endTimeL = endDate.getTime();
					if(repeatInterval!=-1) {
						repeatCount = new Long((endTimeL - startTimeL) / repeatInterval).intValue();
					}
				}
				
			}
			// create trigger
			SimpleTrigger trigger = new SimpleTrigger();
			trigger.setName(triggerName);
			trigger.setDescription(triggerDescription);
			trigger.setGroup(triggerGroup);
			trigger.setStartTime(startDate);
			if(endDate!=null) {
				trigger.setEndTime(endDate);
			}
			if(repeatInterval!=-1) {
				trigger.setRepeatInterval(repeatInterval);
			}
			if(repeatCount!=0) {
				trigger.setRepeatCount(repeatCount);
			}
			trigger.setJobName(jobName);
		    trigger.setJobGroup(jobGroup);
		    trigger.setJobDataMap(jdm);
		    trigger.setVolatility(false);
			// check if the trigger already exists 
		    boolean exists = false;
		    Trigger[] jobTrgs = scheduler.getTriggersOfJob(jobName, jobGroup);
            for(int ind=0; ind<jobTrgs.length; ind++) {
            	Trigger trg = jobTrgs[ind];
            	if(trg.getName().equals(triggerName)) {
            		exists = true;
            		break;
            	}
            }
	        // schedule trigger 
			try{
				if(!exists) {
					scheduler.scheduleJob(trigger);
				} else {
					scheduler.rescheduleJob(triggerName, triggerGroup, trigger);
				}
			} catch (SchedulerException e) {
				SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "scheduleJob", 
						"Error while scheduling job ", e);
				throw e;
			}			
		} catch (Exception e) {
			servreponse.append("outcome=\"fault\"/>");
		}
		servreponse.append("outcome=\"perform\"/>");
		// put into response the outcome of the service
		try{
			SourceBean outcomeSB = SourceBean.fromXMLString(servreponse.toString());
			response.setAttribute(outcomeSB);
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
		                        "scheduleJob", "Error while filling response with the service outcome", e);
		}
	}

	
	
	
	private void deleteJob(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException{
		
		String jobName = (String)request.getAttribute("jobName");
		String jobgroupName = (String)request.getAttribute("jobGroupName");
		try {
			scheduler.deleteJob(jobName, jobgroupName);
		} catch (SchedulerException e) {
			SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "deleteJob", 
					"Error while deleting job", e);
			Vector v = new Vector();
			v.add(e.getMessage());
			throw new EMFUserError(EMFErrorSeverity.ERROR, "101", v);
		}
	}

	
	
	
	private void deleteSchedulation(SourceBean request, SourceBean response) throws EMFUserError{
		StringBuffer servreponse = new StringBuffer();
		try{
			servreponse.append("<EXECUTION_OUTCOME ");
			String triggerName = (String) request.getAttribute("triggerName");
			String triggerGroup = (String) request.getAttribute("triggerGroup");
			try {
				scheduler.unscheduleJob(triggerName, triggerGroup);
			} catch (SchedulerException e) {
				SpagoBITracer.critical("SCHEDULER", this.getClass().getName(), "deleteSchedulation", 
										"Error while deleting trigger", e);
				throw e;
			}
		} catch (Exception e) {
			servreponse.append("outcome=\"fault\"/>");
		}
		servreponse.append("outcome=\"perform\"/>");
		// put into response the outcome of the service
		try{
			SourceBean outcomeSB = SourceBean.fromXMLString(servreponse.toString());
			response.setAttribute(outcomeSB);
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE+"(SCHEDULER)", this.getClass().getName(), 
		                        "deleteSchedulation", "Error while filling response with the service outcome", e);
		}
	}
	
	
	
	
	
	private JobDataMap getJobDataMap(SourceBean jobParameters) {
		JobDataMap jdm = new JobDataMap();
		jdm.put("empty", "empty");
		if(jobParameters!=null) {
			List paramsSB = jobParameters.getContainedAttributes();
			Iterator iterParSb = paramsSB.iterator();
			while(iterParSb.hasNext()) {
					SourceBeanAttribute paramSBA = (SourceBeanAttribute)iterParSb.next();
					String nameAttr = (String)paramSBA.getKey();
					if(nameAttr.equalsIgnoreCase("PARAMETER")) {
						SourceBean paramSB = (SourceBean)paramSBA.getValue();
						String name = (String)paramSB.getAttribute("name");
						String value = (String)paramSB.getAttribute("value");
						jdm.put(name, value);
					}
			}
		}
		return jdm;
	}

	
}	
	
	
