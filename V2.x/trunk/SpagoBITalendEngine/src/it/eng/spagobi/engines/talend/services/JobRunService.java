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
import it.eng.spagobi.commons.bo.UserProfile;
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
import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.strings.StringUtils;

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

public class JobRunService extends AbstractEngineStartServlet {

	private static transient Logger logger = Logger.getLogger(JobRunService.class);
	public static final String JS_FILE_ZIP = "JS_File";
	public static final String JS_EXT_ZIP = ".zip";	
	
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.debug("Initializing SpagoBI Talend Engine...");
	}


	



	private String getLocalizedMessage(String msg) {
		if(msg == null) return "";
		return EngineMessageBundle.getMessage(msg, getLocale());
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String responseMessage;
		RuntimeRepository runtimeRepository;
		Job job;

		logger.debug("IN");
		
		try {		
			
			super.service(request, response);
		
		
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Template: " + getTemplate());
		
			
			
			
			Map params = new HashMap();
			Enumeration enumer = request.getParameterNames();
			params.put("userId", getUserId());	
			
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
	
	
			auditServiceStartEvent();
		
		
			
			job = new Job(getTemplate());	
			validateJob(job);
			
			runtimeRepository = SpagoBITalendEngine.getInstance().getRuntimeRepository();
			validateRuntimeRepository(runtimeRepository);
			
			
			
			
			
			try {
				runtimeRepository.runJob(job, getEnv(), getHttpSession());
			} catch (JobNotFoundException ex) {
				logger.error(ex.getMessage());

				throw new SpagoBIEngineException("Job not found",
						getLocalizedMessage("job.not.existing"));
		
			} catch (ContextNotFoundException ex) {
				logger.error(ex.getMessage(), ex);
				
				throw new SpagoBIEngineException("Context script not found",
						getLocalizedMessage("context.script.not.existing"));
			
			} catch(JobExecutionException ex) {
				logger.error(ex.getMessage(), ex);
				
				throw new SpagoBIEngineException("Job execution error",
						getLocalizedMessage("job.exectuion.error"));
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				
				throw new SpagoBIEngineException("Job execution error",
						getLocalizedMessage("job.exectuion.error"));
			}

			
			responseMessage = getLocalizedMessage("etl.process.started");
			response.getOutputStream().write(responseMessage.getBytes());
		} catch(SpagoBIEngineException error) {
			logger.error(error.getMessage());
			auditServiceErrorEvent(error.getMessage());			
			
			response.getOutputStream().write(error.getErrorDescription().getBytes());
		} finally {
			logger.debug("OUT");
		}
	}

	/**
	 * 
	 * @TODO move validation in the template parsing step
	 */
	public void validateJob(Job job) throws SpagoBIEngineException {
		if(this.requestContainsParameter("context")) {
			logger.debug("Context parameter with value '" + getParameterAsString("context") + "' found in request.");
			job.setContext( getParameterAsString("context") );
		}

		if ( StringUtils.isEmpty( job.getProject() ) ) {			
			throw new SpagoBIEngineException("Missing Talend project name in document template",
					getLocalizedMessage("missing.talend.project"));
		}

		if ( StringUtils.isEmpty( job.getName() ) ) {
			throw new SpagoBIEngineException("Missing Talend job name in document template",
					getLocalizedMessage("missing.talend.job"));
		}

		if ( StringUtils.isEmpty( job.getLanguage() ) ) {
			throw new SpagoBIEngineException("Missing Talend job language in document template",
					getLocalizedMessage("missing.talend.lang"));
		}			
	}
	
	/**
	 * 
	 * @TODO move validation in the configuration parsing step
	 */
	public void validateRuntimeRepository(RuntimeRepository runtimeRepository) throws SpagoBIEngineException {
		if(runtimeRepository == null || !runtimeRepository.getRootDir().exists()) {
			throw new SpagoBIEngineException("Runtime-Repository not available",
					getLocalizedMessage("repository.not.available"));
		}
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
