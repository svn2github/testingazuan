package it.eng.spagobi.scheduler.to;

import it.eng.spagobi.bo.BIObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JobInfo {

	private String jobName = "";
	private String jobGroupName = "";
	private String jobDescription = "";
	private List biobjects = new ArrayList();
	
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
	public List getBiobjects() {
		return biobjects;
	}
	public void setBiobjects(List biobjects) {
		this.biobjects = biobjects;
	}
	public List getBiobjectIds() {
		List biobjIds = new ArrayList();
		Iterator iterBiobjects = biobjects.iterator();
		while(iterBiobjects.hasNext()) {
			BIObject biobj = (BIObject)iterBiobjects.next();
			Integer id =  biobj.getId();
			biobjIds.add(id);
		}
		return biobjIds;
	}
	public String getJobGroupName() {
		return jobGroupName;
	}
	public void setJobGroupName(String jobGroupName) {
		this.jobGroupName = jobGroupName;
	}
	
}
