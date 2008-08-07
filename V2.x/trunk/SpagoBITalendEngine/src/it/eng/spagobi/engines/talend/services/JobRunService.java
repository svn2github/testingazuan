/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
**/
package it.eng.spagobi.engines.talend.services;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.exception.ContextNotFoundException;
import it.eng.spagobi.engines.talend.exception.JobExecutionException;
import it.eng.spagobi.engines.talend.exception.JobNotFoundException;
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;
import it.eng.spagobi.engines.talend.utils.EngineMessageBundle;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import sun.misc.BASE64Decoder;

public class JobRunService extends HttpServlet {

	private static transient Logger logger = Logger.getLogger(JobRunService.class);
	public static final String JS_FILE_ZIP = "JS_File";
	public static final String JS_EXT_ZIP = ".zip";	
	private Locale locale;
	private String documentId=null;
	private String userId=null;	

	protected AuditAccessUtils auditAccessUtils;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.debug("Initializing SpagoBI Talend Engine...");
	}


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



	private String getLocalizedMessage(String msg) {
		if(msg == null) return "";
		return EngineMessageBundle.getMessage("template.parsing.error", locale);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.debug("Start processing a new request...");
		HttpSession session=request.getSession();
		logger.debug("documentId IN Session:"+(String)session.getAttribute("document"));

		String engineRootDir = getServletContext().getRealPath("WEB-INF");
		SpagoBITalendEngine.getInstance().getConfig().setEngineRootDir(new File(engineRootDir));
		// USER PROFILE
		documentId = (String) request.getParameter("document");
		if (documentId==null){
			documentId=(String)session.getAttribute("document");
			logger.debug("documentId From Session:"+documentId);
		}

		logger.debug("documentId:"+documentId);
		IEngUserProfile profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		userId=(String)profile.getUserUniqueIdentifier();
		Map params = new HashMap();
		Enumeration enumer = request.getParameterNames();
		params.put("userId", userId);	
		
		String parName = null;
		String parValue = null;
		logger.debug("Reading request parameters...");

		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();

			if (parName.trim().equalsIgnoreCase("language") 
					|| parName.trim().equalsIgnoreCase("country")
					|| parName.trim().equalsIgnoreCase("template")
					|| parName.trim().equalsIgnoreCase("context")
			) continue;

			parValue = request.getParameter(parName);

			addParToParMap(params, parName, parValue);
			logger.debug("Read parameter [" + parName + "] with value ["+ parValue + "] from request");
		}

		logger.debug("Request parameters read sucesfully" + params);


		// AUDIT UPDATE
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		AuditAccessUtils auditAccessUtils = (AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils != null)
			auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, new Long(System
					.currentTimeMillis()), null, "EXECUTION_STARTED", null, null);


		locale = getLocale(request);


		ContentServiceProxy contentProxy=new ContentServiceProxy(userId,session);

		try {								
			Content template=contentProxy.readTemplate(documentId);
			logger.debug("Read the template."+template.getFileName());
			InputStream is = null;		
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			byte[] templateContent = bASE64Decoder.decodeBuffer(template.getContent());
			is = new java.io.ByteArrayInputStream(templateContent);

	

			Job job = new Job();

			try {
				job.load(is);
			} 
			catch (DocumentException e) {
				logger.error("Error while parsing document template:", e);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Error while parsing document template", null);
				response.getOutputStream().write(getLocalizedMessage("template.parsing.error").getBytes());
				return;
			}

			if (job.getProject() == null || job.getProject().trim().equalsIgnoreCase("")) {
				logger.error("Missing Talend project name in document template.");
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Missing Talend project name in document template", null);
				response.getOutputStream().write(getLocalizedMessage("missing.talend.project").getBytes());
				return;
			}

			if (job.getName() == null || job.getName().trim().equalsIgnoreCase("")) {
				logger.error("Missing Talend job name in document template.");
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Missing Talend job name in document template", null);
				response.getOutputStream().write(getLocalizedMessage("missing.talend.job").getBytes());
				return;
			}

			if (job.getLanguage() == null || job.getLanguage().trim().equalsIgnoreCase("")) {
				logger.error("Missing Talend job language in document template.");
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
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
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Runtime-Repository not available", null);
				response.getOutputStream().write(getLocalizedMessage("repository.not.available").getBytes());
				return;
			}

			String result = EngineMessageBundle.getMessage("etl.process.started", locale);
			try {
				runtimeRepository.runJob(job, params, auditAccessUtils, auditId,session);
			} catch (JobNotFoundException ex) {
				logger.error(ex.getMessage());
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Job not existing", null);
				result = EngineMessageBundle.getMessage("job.not.existing", locale, new String[]{job.getName()});
			} catch (ContextNotFoundException ex) {
				logger.error(ex.getMessage(), ex);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Context script not existing", null);
				result = EngineMessageBundle.getMessage("context.script.not.existing", locale, new String[]{job.getContext(), job.getName()});
			} catch(JobExecutionException ex) {
				logger.error(ex.getMessage(), ex);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Job execution error", null);
				result = EngineMessageBundle.getMessage("perl.exectuion.error", locale);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(session,(String) profile.getUserUniqueIdentifier(), auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Job execution error", null);
				result = EngineMessageBundle.getMessage("perl.exectuion.error", locale);
			}

			response.getOutputStream().write(result.getBytes());

			logger.debug("Ending service method.");

		}finally{}
	}


		/**
		 * @param params
		 * @param parName
		 * @param parValue
		 */
		private void addParToParMap(Map params, String parName, String parValue) {
			logger.debug("IN.parName:"+parName+" /parValue:"+parValue);
			String newParValue;

			ParametersDecoder decoder = new ParametersDecoder();
			if (decoder.isMultiValues(parValue)) {
				List values = decoder.decode(parValue);
				newParValue = "";
				for (int i = 0; i < values.size(); i++) {
					newParValue += (i > 0 ? "," : "");
					newParValue += values.get(i);
				}
			} else {
				newParValue = parValue;
			}
			params.put(parName, newParValue);
		logger.debug("OUT");
		}



	}
