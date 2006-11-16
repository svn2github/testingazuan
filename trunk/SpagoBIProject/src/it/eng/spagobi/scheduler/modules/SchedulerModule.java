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
import it.eng.spago.dispatching.module.AbstractModule;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;


public class SchedulerModule extends AbstractModule {
    
	private Scheduler scheduler = null;
	
	public void init(SourceBean config) {	}

	public void service(SourceBean request, SourceBean response) throws Exception { 
	
		scheduler = StdSchedulerFactory.getDefaultScheduler(); 
		String task = (String)request.getAttribute("task");
		if(task.equalsIgnoreCase("getJobList")) {
			getJobList(request, response);
		} else if(task.equalsIgnoreCase("getJobDefinition")){
			getJobDefinition(request, response);
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
	}
	
	
	private void getJobList(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getJobDefinition(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getJobSchedulationList(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getJobSchedulationDefinition(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void defineJob(SourceBean request, SourceBean response){
		try{
			String jobName = (String)request.getAttribute("jobName");
			String jobgroupName = (String)request.getAttribute("jobGroupName");
			String jobDescription = (String)request.getAttribute("jobDescription");
			String jobClassName = (String)request.getAttribute("jobClass");
			String jobDurability = (String)request.getAttribute("jobDurability");
			String jobRequestRecovery = (String)request.getAttribute("jobRequestRecovery");
			String jobVolatility = (String)request.getAttribute("jobVolatility");
			SourceBean jobParameters = (SourceBean)request.getAttribute("jobParameters");
			
			JobDetail jobDetail = new JobDetail();
			jobDetail.setName(jobName);
			if(jobgroupName!=null)
				jobDetail.setGroup(jobgroupName);
			else jobDetail.setGroup(Scheduler.DEFAULT_GROUP);
			if(jobDescription==null)
				jobDescription = "";
			jobDetail.setDescription(jobDescription);
			if((jobDurability!=null) && (jobDurability.trim().equalsIgnoreCase("true")))
				jobDetail.setDurability(true);
			else jobDetail.setDurability(false);
			if((jobRequestRecovery!=null) && (jobRequestRecovery.trim().equalsIgnoreCase("true")))
				jobDetail.setRequestsRecovery(true);
			else jobDetail.setRequestsRecovery(false);
			if((jobVolatility!=null) && (jobVolatility.trim().equalsIgnoreCase("true")))
				jobDetail.setVolatility(true);
			else jobDetail.setVolatility(false);
			
			// transform parameters sourcebean into JobDataMap structure and set it into the jobDetail
			
			Class jobClass = Class.forName(jobClassName);
			jobDetail.setJobClass(jobClass);
			
			response.setAttribute("result", "");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleJob(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteJob(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteSchedulation(SourceBean request, SourceBean response){
		try{
			response.setAttribute("result", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}	
	
	
