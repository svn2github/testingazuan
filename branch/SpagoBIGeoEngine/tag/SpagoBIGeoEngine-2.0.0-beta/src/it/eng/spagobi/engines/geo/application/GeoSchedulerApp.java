/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.application;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class GeoSchedulerApp.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GeoSchedulerApp {
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * 
	 * @throws SchedulerException the scheduler exception
	 */
	public static void main(String[] args) throws SchedulerException {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		Scheduler sched = schedFact.getScheduler();

		sched.start();

		JobDetail jobDetail = new JobDetail("myJob",
											sched.DEFAULT_GROUP,
											GeoSchedulerApp.DumbJob.class);

		Trigger trigger = TriggerUtils.makeMinutelyTrigger(5);
		trigger.setStartTime(new Date());  // start now
		trigger.setName("myTrigger");

		sched.scheduleJob(jobDetail, trigger);

	}
	
	/**
	 * The Class DumbJob.
	 */
	public static class DumbJob implements Job {

	    /**
    	 * Instantiates a new dumb job.
    	 */
    	public DumbJob() { }

	    /* (non-Javadoc)
    	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
    	 */
    	public void execute(JobExecutionContext context) throws JobExecutionException {
	      System.err.println("DumbJob is executing.");
	    }
	  }

}
