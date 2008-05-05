/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.runtime;


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
		
	/**
	 * Instantiates a new job.
	 */
	public Job() {}
	
	/**
	 * Instantiates a new job.
	 * 
	 * @param name the name
	 * @param project the project
	 * @param language the language
	 */
	public Job(String name, String project, String language) {
		this(name, project, language, null);
	}
	
	/**
	 * Instantiates a new job.
	 * 
	 * @param name the name
	 * @param project the project
	 * @param language the language
	 * @param context the context
	 */
	public Job(String name, String project, String language, String context) {
		this.name = name;
		this.project = project;
		this.language = language;
		this.context = context;
	}		
	
	/**
	 * Load.
	 * 
	 * @param file the file
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws DocumentException the document exception
	 */
	public void load(File file) throws FileNotFoundException, DocumentException {
		load(new FileInputStream(file));
	}
	
	/**
	 * Load.
	 * 
	 * @param is the is
	 * 
	 * @throws DocumentException the document exception
	 */
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
		
	/**
	 * Checks if is perl job.
	 * 
	 * @return true, if is perl job
	 */
	public boolean isPerlJob() {
		return (language!= null && language.equalsIgnoreCase("perl"));
	}
	
	/**
	 * Checks if is java job.
	 * 
	 * @return true, if is java job
	 */
	public boolean isJavaJob() {
		return (language!= null && language.equalsIgnoreCase("java"));
	}
	
	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * Sets the context.
	 * 
	 * @param context the new context
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the project.
	 * 
	 * @return the project
	 */
	public String getProject() {
		return project;
	}
	
	/**
	 * Sets the project.
	 * 
	 * @param project the new project
	 */
	public void setProject(String project) {
		this.project = project;
	}
	
	/**
	 * Gets the language.
	 * 
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language.
	 * 
	 * @param language the new language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * To xml.
	 * 
	 * @return the string
	 */
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
	
	/**
	 * Load job.
	 * 
	 * @param is the is
	 * 
	 * @return the job
	 * 
	 * @throws DocumentException the document exception
	 */
	public static Job loadJob(InputStream is) throws DocumentException {
		Job job = new Job();
		job.load(is);
		return job;
	}
}
