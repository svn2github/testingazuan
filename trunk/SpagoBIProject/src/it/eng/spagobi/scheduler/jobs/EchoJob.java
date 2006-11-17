package it.eng.spagobi.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EchoJob implements Job {

	public void execute(JobExecutionContext jex) throws JobExecutionException {
		JobDataMap jdm = jex.getMergedJobDataMap();
	    String echo = (String)jdm.get("echo");
	    System.out.println(echo);
	}

}
