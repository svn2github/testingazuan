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
