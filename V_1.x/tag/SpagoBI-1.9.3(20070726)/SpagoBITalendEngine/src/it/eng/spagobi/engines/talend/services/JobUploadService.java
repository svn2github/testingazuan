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
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.JobDeploymentDescriptor;
import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

/**
 * @author Andrea Gioia
 *
 */
public class JobUploadService extends HttpServlet {
	
	private static transient Logger logger = Logger.getLogger(JobUploadService.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting JobUpload service method...");
	
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
		        processUploadedFile(item, jobDeploymentDescriptor);
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

	private void processUploadedFile(FileItem item, JobDeploymentDescriptor jobDeploymentDescriptor) throws ZipException, IOException {
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();
	    
	    if(fieldName.equalsIgnoreCase("deploymentDescriptor")) return;
	    
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
		
	    runtimeRepository.deployJob(jobDeploymentDescriptor, new ZipFile(uploadedFile));
	    uploadedFile.delete();	
	    tmpDir.delete();
	}

}

