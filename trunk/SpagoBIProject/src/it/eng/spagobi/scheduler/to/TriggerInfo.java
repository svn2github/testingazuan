package it.eng.spagobi.scheduler.to;

import java.util.Map;


public class TriggerInfo {


	private String triggerName = "";
	private String triggerDescription = "";
	private String startDate = "";
	private String startTime = "";
	private String chronString = "";
	private String endDate = "";
	private String endTime = "";
	private String repeatInterval = "";
	private JobInfo jobInfo = null;
	private Map saveOptions = null;
    
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(String repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getTriggerDescription() {
		return triggerDescription;
	}
	public void setTriggerDescription(String triggerDescription) {
		this.triggerDescription = triggerDescription;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public JobInfo getJobInfo() {
		return jobInfo;
	}
	public void setJobInfo(JobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}
	public Map getSaveOptions() {
		return saveOptions;
	}
	public void setSaveOptions(Map saveOptions) {
		this.saveOptions = saveOptions;
	}
	public String getChronString() {
		return chronString;
	}
	public void setChronString(String chronString) {
		this.chronString = chronString;
	}
	
}
