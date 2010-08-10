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
import it.eng.spagobi.engines.talend.exception.ContextNotFoundException;
import it.eng.spagobi.engines.talend.exception.JobExecutionException;
import it.eng.spagobi.engines.talend.exception.JobNotFoundException;
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;
import it.eng.spagobi.engines.talend.utils.EngineMessageBundle;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import sun.misc.BASE64Decoder;

public class JobRunService extends HttpServlet {
	
	private static transient Logger logger = Logger.getLogger(JobRunService.class);
	
	private Locale locale;
	
	protected AuditAccessUtils auditAccessUtils;
	
	private Locale getLocale(HttpServletRequest request) {
		Locale locale = null;
		String language;
		String country;
				
		language = request.getParameter("language");
		country = request.getParameter("country");
		logger.debug("Locale parameters received: language = [" + language + "] ; country = [" + country + "]");
				
		try {
			locale = new Locale(language, country);
		} catch (Exception e) {
			logger.debug("Error while creating Locale object from input parameters: language = [" + language + "] ; country = [" + country + "]");
			logger.debug("Creating default locale [en,US].");
			locale = new Locale("en", "US");
		}
		
		return locale;
	}
	
	private InputStream getTemplate(HttpServletRequest request) throws IOException {
		InputStream is = null;
		
		String templateBase64Coded = request.getParameter("template");
		if (templateBase64Coded == null || templateBase64Coded.equals("")) return null;
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		is = new java.io.ByteArrayInputStream(template);
		
		return is;
	}
	
	private String getLocalizedMessage(String msg) {
		if(msg == null) return "";
		return EngineMessageBundle.getMessage("template.parsing.error", locale);
	}
	
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting service method...");
		
		String engineRootDir = getServletContext().getRealPath("WEB-INF");
		SpagoBITalendEngine.getInstance().getConfig().setEngineRootDir(new File(engineRootDir));
		
		
		Enumeration paramsEnum = request.getParameterNames();
		Map parameters = new HashMap();
		while (paramsEnum.hasMoreElements()) {
			String parameterName = (String) paramsEnum.nextElement();
			if (parameterName.trim().equalsIgnoreCase("language") 
					|| parameterName.trim().equalsIgnoreCase("country")
					|| parameterName.trim().equalsIgnoreCase("template")
					|| parameterName.trim().equalsIgnoreCase("context")
					) continue;
			String[] parameterValues = request.getParameterValues(parameterName);
			if (parameterValues == null) continue;
			if (parameterValues.length == 1) parameters.put(parameterName, parameterValues[0]);
			else {
				String temp = parameterValues[0];
				for (int i = 1; i < parameterValues.length; i++) {
					temp += ", " + parameterValues[i];
				}
				parameters.put(parameterName, temp);
			}
		}
		// now the parameters map contains the biobject document parameters

		auditAccessUtils = (AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		// AUDIT UPDATE
		String auditId = (String) parameters.get("SPAGOBI_AUDIT_ID");
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
				"EXECUTION_STARTED", null, null);
		
		locale = getLocale(request);
		InputStream is = getTemplate(request);
		if(is == null) {
			logger.error("Missing document template!!");
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Missing document template", null);
			response.getOutputStream().write(getLocalizedMessage("missing.document.template").getBytes());
			return;
		}
		
		Job job = new Job();
	    
	    try {
	    	job.load(is);
		} catch (DocumentException e) {
			logger.error("Error while parsing document template:", e);
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Error while parsing document template", null);
			response.getOutputStream().write(getLocalizedMessage("template.parsing.error").getBytes());
			return;
		}
	  	  	    
	    if (job.getProject() == null || job.getProject().trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend project name in document template.");
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Missing Talend project name in document template", null);
			response.getOutputStream().write(getLocalizedMessage("missing.talend.project").getBytes());
			return;
	    }
	    
	    if (job.getName() == null || job.getName().trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend job name in document template.");
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Missing Talend job name in document template", null);
			response.getOutputStream().write(getLocalizedMessage("missing.talend.job").getBytes());
			return;
	    }
	    
	    if (job.getLanguage() == null || job.getLanguage().trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend job language in document template.");
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Missing Talend job language in document template", null);
	    	response.getOutputStream().write(getLocalizedMessage("missing.talend.lang").getBytes());
			return;
	    }
	    
	    // if the context is specified in request, it overrides the context specified in document template 
	    String requestContext = request.getParameter("context");
	    if (requestContext != null && !requestContext.trim().equals("")) {
	    	logger.debug("Context parameter with value '" + requestContext + "' found in request.");
	    	job.setContext(requestContext);
	    }
	
		RuntimeRepository runtimeRepository = SpagoBITalendEngine.getInstance().getRuntimeRepository();
		if(runtimeRepository == null || !runtimeRepository.getRootDir().exists()) {
			logger.error("Runtime-Repository not available");
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Runtime-Repository not available", null);
			response.getOutputStream().write(getLocalizedMessage("repository.not.available").getBytes());
			return;
		}
		
		String result = EngineMessageBundle.getMessage("etl.process.started", locale);
    	try {
    		runtimeRepository.runJob(job, parameters, auditAccessUtils, auditId);
    	} catch (JobNotFoundException ex) {
    		logger.error(ex.getMessage());
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Job not existing", null);
    		result = EngineMessageBundle.getMessage("job.not.existing", locale, 
					new String[]{job.getName()});
    	} catch (ContextNotFoundException ex) {
    		logger.error(ex.getMessage(), ex);
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Context script not existing", null);
    		result = EngineMessageBundle.getMessage("context.script.not.existing", locale, 
    				new String[]{job.getContext(), job.getName()});
    	} catch(JobExecutionException ex) {
    		logger.error(ex.getMessage(), ex);
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Job execution error", null);
    		result = EngineMessageBundle.getMessage("perl.exectuion.error", locale);
    	}    	
    	
    	response.getOutputStream().write(result.getBytes());
    	
    	logger.debug("Ending service method.");
		
	}
}
