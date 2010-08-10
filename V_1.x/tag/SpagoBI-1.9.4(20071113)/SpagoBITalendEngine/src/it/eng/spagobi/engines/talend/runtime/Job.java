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
package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.SpagoBITalendEngineConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @author Andrea Gioia
 *
 */
public class Job {
	String name;
	String project;
	String language;
	String context;
		
	public Job() {}
	
	public Job(String name, String project, String language) {
		this(name, project, language, null);
	}
	
	public Job(String name, String project, String language, String context) {
		this.name = name;
		this.project = project;
		this.language = language;
		this.context = context;
	}		
	
	public void load(File file) throws FileNotFoundException, DocumentException {
		load(new FileInputStream(file));
	}
	
	public void load(InputStream is) throws DocumentException{
		SAXReader reader = new org.dom4j.io.SAXReader();
	    Document document = null;
	    
	   	document = reader.read(is);		
	    
	    Node job = document.selectSingleNode("//etl/job");
	    if (job != null) {
	    	this.name = job.valueOf("@jobName");
	    	this.project = job.valueOf("@project");
	    	this.language = job.valueOf("@language");
	 	    this.context = job.valueOf("@context");
	    }
	}
		
	public boolean isPerlJob() {
		return (language!= null && language.equalsIgnoreCase("perl"));
	}
	
	public boolean isJavaJob() {
		return (language!= null && language.equalsIgnoreCase("java"));
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String toXml() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<ETL>");
		buffer.append("<JOB");
		if(name != null && !name.trim().equalsIgnoreCase("")) buffer.append(" jobName=" + name);
		if(project != null && !project.trim().equalsIgnoreCase("")) buffer.append(" project=" + project);
		if(language != null && !language.trim().equalsIgnoreCase("")) buffer.append(" language=" + language);
		if(context != null && !context.trim().equalsIgnoreCase("")) buffer.append(" context=" + context);
		buffer.append("/>");
		buffer.append("</ETL>");
		
		return buffer.toString();
	}
	
	public static Job loadJob(InputStream is) throws DocumentException {
		Job job = new Job();
		job.load(is);
		return job;
	}
}
