package it.eng.spagobi.engines.talend;

import it.eng.spagobi.utilities.messages.EngineMessageBundle;

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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Decoder;

public class TalendServlet extends HttpServlet {
	
	private static transient Logger logger = Logger.getLogger(TalendServlet.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting service method...");
		String language = request.getParameter("language");
		String country = request.getParameter("country");
		logger.debug("Locale parameters received: language = [" + language + "] ; country = [" + country + "]");
		
		Locale locale = null;
		
		try {
			locale = new Locale(language, country);
		} catch (Exception e) {
			logger.debug("Error while creating Locale object from input parameters: language = [" + language + "] ; country = [" + country + "]");
			logger.debug("Creating default locale [en,US].");
			locale = new Locale("en", "US");
		}
		
		//request.getSession().setAttribute("userLcoale", locale);
		
		String templateBase64Coded = request.getParameter("template");
		if (templateBase64Coded == null || templateBase64Coded.equals("")) {
			logger.error("Missing document template!!");
			String msg = EngineMessageBundle.getMessage("missing.document.template", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
		}
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		InputStream is = new java.io.ByteArrayInputStream(template);
		SAXReader reader = new org.dom4j.io.SAXReader();
	    Document document = null;
	    
	    try {
			document = reader.read(is);
		} catch (DocumentException e) {
			logger.error("Error while parsing document template:", e);
			String msg = EngineMessageBundle.getMessage("template.parsing.error", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
		}
	    
	    Node job = document.selectSingleNode("//etl/job");
	    if (job == null) {
			logger.error("Missing node //etl/job in document template.");
			String msg = EngineMessageBundle.getMessage("template.parsing.error", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
	    }
	    String project = job.valueOf("@project");
	    String jobName = job.valueOf("@jobName");
	    String context = job.valueOf("@context");
	    
	    if (project == null || project.trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend project name in document template.");
			String msg = EngineMessageBundle.getMessage("missing.talend.project", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
	    }
	    
	    if (jobName == null || jobName.trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend job name in document template.");
			String msg = EngineMessageBundle.getMessage("missing.talend.job", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
	    }
	    
	    // if the context is specified in request, it overrides the context specified in document template 
	    String requestContext = request.getParameter("context");
	    if (requestContext != null && !requestContext.trim().equals("")) {
	    	logger.debug("Context parameter with value '" + requestContext + "' found in request.");
	    	context = requestContext;
	    }
	    
//	    if (context == null || context.trim().equalsIgnoreCase("")) {
//	    	logger.debug("Missing Talend context name in document template.");
//	    	if (requestContext != null && !requestContext.trim().equals("")) {
//	    	} else {
//	    		context = DEFAULT_CONTEXT;
//	    		logger.debug("Considering '" + DEFAULT_CONTEXT + "' as default context value.");
//	    	}
//	    }
		
		String scriptsBaseDir = this.getServletContext().getRealPath("/jobs");
		
		Enumeration e = request.getParameterNames();
		Map parameters = new HashMap();
		while (e.hasMoreElements()) {
			String parameterName = (String) e.nextElement();
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
		
    	RunProcess aRunProcess = new RunProcess();
    	String result = aRunProcess.run(scriptsBaseDir, project, jobName, context, locale, parameters);
    	
    	response.getOutputStream().write(result.getBytes());
    	
    	logger.debug("Ending service method.");
		
	}
}
