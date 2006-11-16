package it.eng.spagobi.scheduler;

import java.util.Calendar;

public class ScheduledJobDetail {

	private String jobClass = null;
	private Calendar date = null;
	
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	
}
