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
package it.eng.spagobi.engines.talend.services;

import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.SpagoBITalendEngineConfig;
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.JobDeploymentDescriptor;
import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;
import it.eng.spagobi.engines.talend.utils.ZipUtils;
import it.eng.spagobi.services.proxy.ContentServiceProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import sun.misc.BASE64Encoder;

/**
 * @author Andrea Gioia
 *
 */
public class JobUploadService extends HttpServlet {
	
	private static transient Logger logger = Logger.getLogger(JobUploadService.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting JobUpload service method...");
		HttpSession session=request.getSession();
		String engineRootDir = getServletContext().getRealPath("WEB-INF");
		SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
		config.setEngineRootDir(new File(engineRootDir));
		
		
		//  Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
		
		JobDeploymentDescriptor jobDeploymentDescriptor = getJobsDeploymetDescriptor(items);
		
		// Process the uploaded items
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();
		    if (item.isFormField()) {
		        processFormField(item);
		    } else {
		        String[] jobNames = processUploadedFile(item, jobDeploymentDescriptor);
		        if(config.isAutoPublishActive()) {
		        	if(jobNames == null) continue;
			        for(int i = 0; i < jobNames.length; i++) {
			        	publishOnSpagoBI(session,jobDeploymentDescriptor.getLanguage(), jobDeploymentDescriptor.getProject(), jobNames[i]);		 
			        }
		        }
		    }
		}
		
		response.getOutputStream().write("OK".getBytes());
	}

	private JobDeploymentDescriptor getJobsDeploymetDescriptor(List items) {
		JobDeploymentDescriptor jobDeploymentDescriptor = null;
		
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();
		    if (!item.isFormField()) {
		    	String fieldName = item.getFieldName();
		    	if(fieldName.equalsIgnoreCase("deploymentDescriptor")) {
		    		jobDeploymentDescriptor = new JobDeploymentDescriptor();
		    		try {
		    			jobDeploymentDescriptor.load(item.getInputStream());
					} catch (DocumentException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
		    	}
		    }
		}
		return jobDeploymentDescriptor;
	}
	
	
	private void processFormField(FileItem item) {
		// TODO Auto-generated method stub
		
	}

	private String[] processUploadedFile(FileItem item, JobDeploymentDescriptor jobDeploymentDescriptor) throws ZipException, IOException {
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();
	    
	    if(fieldName.equalsIgnoreCase("deploymentDescriptor")) return null;
	    
	    RuntimeRepository runtimeRepository = SpagoBITalendEngine.getInstance().getRuntimeRepository();
	    File jobsDir = new File(runtimeRepository.getRootDir(), jobDeploymentDescriptor.getLanguage().toLowerCase());
		File projectDir = new File(jobsDir, jobDeploymentDescriptor.getProject());
		File tmpDir = new File(projectDir, "tmp");
		if(!tmpDir.exists()) tmpDir.mkdirs();	   
	     File uploadedFile = new File(tmpDir, fileName);
	    
	    try {
			item.write(uploadedFile);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		String[] dirNames = ZipUtils.getDirectoryNameByLevel(new ZipFile(uploadedFile), 2);
		List dirNameList = new ArrayList();
		for(int i = 0; i < dirNames.length; i++) {
			if(!dirNames[i].equalsIgnoreCase("lib")) dirNameList.add(dirNames[i]);
		}
		String[] jobNames = (String[])dirNameList.toArray(new String[0]);
		
	    runtimeRepository.deployJob(jobDeploymentDescriptor, new ZipFile(uploadedFile));
	    uploadedFile.delete();	
	    tmpDir.delete();
	    
	    return jobNames;
	}
	
	
	public boolean isValidTemplate(File templateFile) {
		try {
		   	InputStream is = new FileInputStream(templateFile);
			Job job = new Job();
			job.load(is);
			if(job.getLanguage() == null || job.getProject() == null || job.getName() == null) return false;
		} catch (Exception e) {
			 return false;
		}
		
		return true;
	}
	
	private String getTemplate(String language, String projectName, String jobName) throws IOException {
		String template = null;
		
		RuntimeRepository runtimeRepository = SpagoBITalendEngine.getInstance().getRuntimeRepository();
		File jobsDir = new File(runtimeRepository.getRootDir(), language.toLowerCase());
		File projectDir = new File(jobsDir, projectName);
		File jobDir = new File(projectDir, jobName);
		File templateFile = new File(jobDir, "spagobi.xml");
		if(templateFile.exists() && isValidTemplate(templateFile) ) {
			BufferedReader reader = new BufferedReader(new FileReader(templateFile));
			String line = null;
			template = "";
			while((line = reader.readLine()) != null) {
				template += line + "\n";
			}
		} else {
			String contextName = "Default";
			template = "";
			template += "<etl>\n";
			template += "\t<job project=\"" + projectName + "\" ";
			template += "jobName=\"" + jobName + "\" ";
			template += "context=\"" + contextName + "\" ";
			template += "language=\"" + language + "\" />\n";
			template += "</etl>";
		}
		
		return template;
	}
	
	
	/*
	 * TODO: implementare questa funzione tramite una API  WEB Service
	 */
	private void publishOnSpagoBI(HttpSession session, String language, String projectName, String jobName) throws IOException {
		RuntimeRepository runtimeRepository = SpagoBITalendEngine.getInstance().getRuntimeRepository();
		
		String template = getTemplate(language, projectName, jobName);
		
		BASE64Encoder encoder = new BASE64Encoder();
		String templateBase64Coded = encoder.encode(template.getBytes());		
		
		SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
		
		String user = "biadmin";
		String password = "biadmin";
		Date now = new Date();
		String date = new Long (now.getTime()).toString();
		String label = jobName.toUpperCase();
		if (label.length() > 20) label = label.substring(0,19);
		String name = jobName ;
		String description = language + " job defined in " + projectName  + " project";
		String encrypt = "false";
		String visible = "true";
		String functionalitiyCode = config.getSpagobiTargetFunctionalityLabel();		
		String type = "ETL";
		String state = "DEV";
		
		 HashMap<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("TEMPLATE", templateBase64Coded);
		attributes.put("LABEL", label);
		attributes.put("NAME", name);
		attributes.put("DESCRIPTION", description);
		attributes.put("ENCRYPTED", encrypt);
		attributes.put("VISIBLE", visible);
		attributes.put("TYPE", type);
		attributes.put("FUNCTIONALITYCODE", functionalitiyCode);	
		attributes.put("STATE", state);
		attributes.put("USER", user);
		
		try {
			
			String spagobiurl = config.getSpagobiUrl();
		    session.setAttribute("BACK_END_SPAGOBI_CONTEXT", spagobiurl);
		    ContentServiceProxy contentProxy=new ContentServiceProxy(user,session);
		    contentProxy.publishTemplate(attributes);
			//PublishAccessUtils.publish(spagobiurl, user, password, label, name, description, encrypt, visible, type, state, functionalitiyCode, templateBase64Coded);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * TODO: implementare questa funzione tramite una API  WEB Service
	 */
	private void publishOnSpagoBI(FileItem item, JobDeploymentDescriptor jobDeploymentDescriptor) throws ZipException, IOException {
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();
	    
	    if(fieldName.equalsIgnoreCase("deploymentDescriptor")) return;
	    
	    
	    
	    RuntimeRepository runtimeRepository = SpagoBITalendEngine.getInstance().getRuntimeRepository();
	    
	    String projectName = jobDeploymentDescriptor.getProject();
	    String projectLanguage = jobDeploymentDescriptor.getLanguage().toLowerCase();
	    String jobName = "JOB_NAME";
	    String contextName = "Default";
	    String template = "";
	    template += "<etl>\n";
	    template += "\t<job project=\"" + projectName + "\" ";
	    template += "jobName=\"" + projectName + "\" ";
	    template += "context=\"" + projectName + "\" ";
	    template += "language=\"" + contextName + "\" />\n";
	    template += "</etl>";
	    
	    BASE64Encoder encoder = new BASE64Encoder();
	    String templateBase64Coded = encoder.encode(template.getBytes());		
	    
	    SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
	    
	    String user = "biadmin";
		String password = "biadmin";
		String label = "ETL_JOB";
		String name = "EtlJob";
		String description = "Etl Job";
		boolean encrypt = false;
		boolean visible = true;
		String functionalitiyCode = config.getSpagobiTargetFunctionalityLabel();	
		String type = "ETL";
		String state = "DEV";
	    
	    try {

	    	//PublishAccessUtils.publish(spagobiurl, user, password, label, name, description, encrypt, visible, type, state, functionalitiyCode, templateBase64Coded);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}

