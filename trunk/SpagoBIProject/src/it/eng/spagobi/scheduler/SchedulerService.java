package it.eng.spagobi.scheduler;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class SchedulerService {

	public ScheduledJobDetail[][] getScheduledJobList() {
		ScheduledJobDetail[][] list = new ScheduledJobDetail[1][3];
		ScheduledJobDetail detail1 = new ScheduledJobDetail();
		detail1.setJobClass("classe");
		detail1.setDate(new GregorianCalendar());
		list[0][0] = detail1;
		list[0][1] = detail1;
		list[0][2] = detail1;
		return list;
	}
	
	public ScheduledJobDetail getScheduledJobDetail(String jobId) {
		ScheduledJobDetail detail1 = new ScheduledJobDetail();
		detail1.setJobClass("classe");
		detail1.setDate(new GregorianCalendar());
		return detail1;
	}
	
	
	public String scheduleJob(String jobId, String jobClass, String startDate) {
		System.out.println("jobid = " + jobId);
		System.out.println("jobclass = " + jobClass);
		System.out.println("start date = " + startDate);
		if(jobClass.equalsIgnoreCase("schedule")) {
			return "scheduled correctly";
		} else {
			return "error during scheduling";
		}
	}
	
}
