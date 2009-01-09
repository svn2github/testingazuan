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
package it.eng.spagobi.engines.kpi.service;

import java.util.Date;

import org.quartz.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class KpiEngineJob implements Job {

        static private Logger logger = Logger.getLogger(KpiEngineJob.class);	
	
        static public final String MODEL_INSTANCE_ID="MODEL_INSTANCE_ID";
    	
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
	    logger.debug("IN");
	    
	    try{
		      String instName = context.getJobDetail().getName();
		      String instGroup = context.getJobDetail().getGroup();
		      JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		      logger.debug("context.isRecovering()="+context.isRecovering());
		      // use this variable for running the KPI Engine
		      String modelInstanceId = dataMap.getString(MODEL_INSTANCE_ID);
		      logger.debug("modelInstanceId="+modelInstanceId);
		      
	      
		      Date data=context.getFireTime();
		      logger.debug("data="+data.toString());
		      
		      // invocare il motore KPi passando i parametri:
		      // - data di riferimento
		      // - ID del nodo 
		
	     } catch (Throwable e) {
        	    logger.error("Error while executiong KpiEngineJob", e);
	     } finally {

        	 logger.debug("OUT");
	    }
	}
	

}
