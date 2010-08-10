package it.eng.spagobi.scheduler.to;

import java.util.ArrayList;
import java.util.List;

public class ObjExecSchedulation {

	private String jobName = "";
	private String jobDescription = "";
	private boolean jobRecoverabilty = false;
	private String triggerName = "";
	private String triggerDescription = "";
	private String startDate = "";
	private String startTime = "";
	private String endDate = "";
	private String endTime = "";
	private String repeatInterval = "";
	private boolean storeOutput = false;
	private String storeName = "";
	private String storeDescription = "";
	private String storeType = "";
	private String pathDocument = "";
	private String historyLength = "";
    private List objExecParameters = new ArrayList();
    
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
	public String getHistoryLength() {
		return historyLength;
	}
	public void setHistoryLength(String historyLength) {
		this.historyLength = historyLength;
	}
	public List getObjExecParameters() {
		return objExecParameters;
	}
	public void setObjExecParameters(List objExecParameters) {
		this.objExecParameters = objExecParameters;
	}
	public String getPathDocument() {
		return pathDocument;
	}
	public void setPathDocument(String pathDocument) {
		this.pathDocument = pathDocument;
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
	public String getStoreDescription() {
		return storeDescription;
	}
	public void setStoreDescription(String storeDescription) {
		this.storeDescription = storeDescription;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public boolean isStoreOutput() {
		return storeOutput;
	}
	public void setStoreOutput(boolean storeOutput) {
		this.storeOutput = storeOutput;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
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
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public boolean isJobRecoverabilty() {
		return jobRecoverabilty;
	}
	public void setJobRecoverabilty(boolean jobRecoverabilty) {
		this.jobRecoverabilty = jobRecoverabilty;
	}
	
	
}
