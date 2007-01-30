package it.eng.spagobi.engines.talend;

import it.eng.spagobi.engines.talend.messages.TalendMessageBundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

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
			String msg = TalendMessageBundle.getMessage("missing.document.template", locale);
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
			String msg = TalendMessageBundle.getMessage("template.parsing.error", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
		}
	    
	    Node job = document.selectSingleNode("//etl/job");
	    if (job == null) {
			logger.error("Missing node //etl/job in document template.");
			String msg = TalendMessageBundle.getMessage("template.parsing.error", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
	    }
	    String project = job.valueOf("@project");
	    String jobName = job.valueOf("@jobName");
	    String context = job.valueOf("@context");
	    
	    if (project == null || project.trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend project name in document template.");
			String msg = TalendMessageBundle.getMessage("missing.talend.project", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
	    }
	    
	    if (jobName == null || jobName.trim().equalsIgnoreCase("")) {
	    	logger.error("Missing Talend job name in document template.");
			String msg = TalendMessageBundle.getMessage("missing.talend.job", locale);
			response.getOutputStream().write(msg.getBytes());
			return;
	    }
	    
	    if (context == null || context.trim().equalsIgnoreCase("")) {
	    	logger.debug("Missing Talend context name in document template. Considering \"Default\" as default context value.");
	    	context = "Default";
	    }
		
		String scriptsBaseDir = this.getServletContext().getRealPath("/jobs");
    	RunProcess aRunProcess = new RunProcess();
    	String result = aRunProcess.run(scriptsBaseDir, project, jobName, context, locale);
    	
    	response.getOutputStream().write(result.getBytes());
    	
    	logger.debug("Ending service method.");
		
	}
}
