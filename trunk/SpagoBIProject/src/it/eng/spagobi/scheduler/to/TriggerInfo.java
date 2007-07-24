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
	public String getStartDateRFC3339() {
		String startDRFC = "";
		String startD = this.getStartDate();
		if( (startD!=null) && !startD.trim().equals("") ) {
			String[] dateParts = startD.split("/");
			startDRFC = dateParts[2] + "-" + dateParts[1] + "-" +  dateParts[0];
		}
		return startDRFC;
	}
	public String getEndDateRFC3339() {
		String endDRFC = "";
		String endD = this.getEndDate();
		if( (endD!=null) && !endD.trim().equals("") ) {
			String[] dateParts = endD.split("/");
			endDRFC = dateParts[2] + "-" + dateParts[1] + "-" +  dateParts[0];
		}
		return endDRFC;
	}
	
}
