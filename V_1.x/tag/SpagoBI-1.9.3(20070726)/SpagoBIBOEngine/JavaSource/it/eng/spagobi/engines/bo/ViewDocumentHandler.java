/**
Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
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
*/
package it.eng.spagobi.engines.bo;

import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;

import sun.misc.BASE64Decoder;

import com.bo.rebean.wi.BinaryView;
import com.bo.rebean.wi.DocumentInstance;
import com.bo.rebean.wi.HTMLView;
import com.bo.rebean.wi.ImageOption;
import com.bo.rebean.wi.OutputFormatType;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.ReportEngine;
import com.bo.rebean.wi.Reports;
import com.bo.wibean.WIServer;
import com.bo.wibean.WISession;

public class ViewDocumentHandler {

	private transient Logger logger = Logger.getLogger(BOServlet.class);
	
	public void handle(HttpServletRequest request, HttpServletResponse response, 
			           ServletContext servletContext) throws ServletException, IOException{
		
		// AUDIT UPDATE
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		AuditAccessUtils auditAccessUtils = 
			(AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
				"EXECUTION_STARTED", null, null);
		
		//get template
		String template64 = (String)request.getParameter("spagobi_template");
		if(template64==null) {
 			logger.error("Engines"+ this.getClass().getName()+ 
 			 			 "service() template parameter (spagobi_template) not found");
			// AUDIT UPDATE
			if (auditAccessUtils != null) 
					auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
												"EXECUTION_FAILED", "Template not found", null);
 			return;
 		} 
 		BASE64Decoder decoder = new BASE64Decoder();
 		String reportName = null;
 		String reportID = null;
 		String repository = null;
 		String reportType = null;
 		try{
 	    	byte[] jcrContent = decoder.decodeBuffer(template64);
 			//parse template and recover parameters      
 	 		ByteArrayInputStream is = new ByteArrayInputStream(jcrContent);
 	 	    org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
 	    	Document parseDocument = reader.read(is);
            // get template parameter    
 	    	Node cube = parseDocument.selectSingleNode("//BO_DOCUMENT/REP_PROPS");
 	    	reportName = cube.valueOf("@name");
 	    	reportID = cube.valueOf("@id"); 
 	    	repository = cube.valueOf("@repository");
 	    	reportType = cube.valueOf("@type");
 	    } catch (Exception e){
 	 	  	  logger.error("Engines"+ this.getClass().getName()+ 
 	 	  	  			   "service() Error while reading template " , e);
 	 	  	  // AUDIT UPDATE
 	 	  	  if (auditAccessUtils != null) 
 	 	  		  auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
 	 	  				  					   "EXECUTION_FAILED", "Error while parsing template", null);
 	 	  	  return;
 	 	}
 	    
 	    //check parameters
	 	if((reportName==null) || (reportName.trim().equals(""))) {
	 		logger.error("Engines"+ this.getClass().getName()+ 
	 		 			"service() Cannot find REPORTNAME parameter");
	 	  	// AUDIT UPDATE
	 	  	if (auditAccessUtils != null) 
	 	  	    auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
	 	  	                                 "EXECUTION_FAILED", "Cannot find template REPORTNAME parameter", null);
	 		return;
	 	} 
		if((reportID==null) || (reportID.trim().equals(""))) {
			logger.error("Engines"+ this.getClass().getName()+ 
			 			"service() Cannot find REPORTID parameter");
	 	  	// AUDIT UPDATE
	 	  	if (auditAccessUtils != null) 
	 	  	    auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
	 	  	                                 "EXECUTION_FAILED", "Cannot find template REPORTID parameter", null);			
			return;
		}
		String outputType = request.getParameter(BOConstants.OUTPUT_TYPE);
		if((outputType==null) || (outputType.trim().equals(""))) {
			logger.info("Engines"+ this.getClass().getName()+ 
	 					 "service() Cannot find OUTPUTTYPE parameter, use default value HTML");
			outputType = BOConstants.HTML_OUTPUT_TYPE;
		}
		
		
		// get repository name and code
		int repCode = 0;
		if((repository==null) || (repository.trim().equals(""))) {
			logger.info("Engines"+ this.getClass().getName()+ 
			 			"service() Cannot find REPOSITORY parameter, use default value corporate");
			repository = BOConstants.CORPORATE_REPOSITORY;
			repCode = 0;
		} else {
			if(Utils.controlRepository(repository)) {
				repCode = Utils.getRepCodeFromName(repository);
			} else {
				logger.error("Engines"+ this.getClass().getName()+ 
	 						"service() REPOSITORY parameter has a wrong value (possible values are corporate/personal/inbox)");
		 	  	// AUDIT UPDATE
		 	  	if (auditAccessUtils != null) 
		 	  	    auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
		 	  	       "EXECUTION_FAILED", "REPOSITORY template parameter is wrong " +
		 	  	       "(possible values are corporate/personal/inbox) ", null);
				return;	
			}
		}	
		
		logger.info("Engines"+ this.getClass().getName()+ 
					"service() like report type will be used the wid value. " +
					"For this release the only possible type of reports are the ones with .wid extension"); 
		
		try {
			// get wiserver form context
			WIServer wiServer = (WIServer)servletContext.getAttribute(BOConstants.WEBISERVER);
			// check connection with the server		
			if(wiServer==null) {
				logger.error("Engines"+ this.getClass().getName()+ 
				 			 "service() No connection with the server");
				// AUDIT UPDATE
		 	  	if (auditAccessUtils != null) 
		 	  	    auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
		 	  	       "EXECUTION_FAILED", "No connection with the server", null);
			    return;
			}
			// initialize wiserver with current request and response
			wiServer.onStartPage(request, response);
			// get http session 
			HttpSession httpSession = request.getSession();
			// get wisession
			WISession wiSession = (WISession)httpSession.getAttribute(BOConstants.BOSESSION);
			if(wiSession == null) {
				logger.error("Engines"+ this.getClass().getName()+ 
				 			 "service() Session recovered is null");	
				// AUDIT UPDATE
		 	  	if (auditAccessUtils != null) 
		 	  	    auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
		 	  	       "EXECUTION_FAILED", "BO Session recovered is null", null);
				return;
			}
			// get report server 
			ReportEngine repEngine = (ReportEngine)httpSession.getAttribute(BOConstants.REPORTENGINE);
			// open document
			DocumentInstance document  = null;
			document = repEngine.openDocument(reportName, reportID, repository, reportType);
			// fills document parameters values
			Utils.fillPrompts(document, request);
			// recover storage token
			String storageToken = document.getStorageToken();
			// Set image parameters
			ImageOption cdzImageOption = document.getImageOption();
			cdzImageOption.setImageCallback("viewDocumentImages.jsp");
			cdzImageOption.setImageNameHolder("image");
			cdzImageOption.setStorageTokenHolder("entry");
			// set storage token into session
			httpSession.setAttribute(BOConstants.STORAGETOKEN, storageToken);
			// retrieve reports collection:all reports contained inside the document
			Map docReports = new HashMap();
			Reports reports = document.getReports();
			int numReports = reports.getCount();
			for(int i=0; i<numReports; i++) {
				Report tmpRep = reports.getItem(i);
				String nameRep = tmpRep.getName();
				docReports.put(new Integer(i), nameRep);
				httpSession.setAttribute(BOConstants.DOCUMENTREPORTMAP, docReports);
			}
			// select first report the first report
			document.setSelectedReport(0);
			Report report = reports.getItem(0);
			// set document in session 
			httpSession.setAttribute(BOConstants.BODOCUMENT, document);
			// generate html
			Utils.addHtmlInSession(report, httpSession);
			// forward to the execution jsp
			String jspexecution = "";
			if(outputType.trim().equalsIgnoreCase(BOConstants.HTML_OUTPUT_TYPE)){
				jspexecution="/jsp/viewDocumentHTML.jsp";
			} else {
				jspexecution="/jsp/viewDocumentPDF.jsp";	
			}		
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_PERFORMED", null, null);
			RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
			disp.forward(request, response);
 	
 	    } catch (Exception e){
 	    	  logger.error("Engines"+ this.getClass().getName()+ 
	 		  			   "service() Error while generating report output");
 	    	  // AUDIT UPDATE
		 	  	if (auditAccessUtils != null) 
		 	  	    auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
		 	  	       "EXECUTION_FAILED", "Error while generating report output: " + e.getMessage(), null);
 	    }
	}
	
	
	
	
	
	
	
	
	private void sendOutput(DocumentInstance repInstance, Report report, HttpServletResponse response, String outType) {
		if(outType.equals(BOConstants.HTML_OUTPUT_TYPE)) {
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
			} catch (IOException ioe) {
				logger.error("Engines"+ this.getClass().getName()+ 
			        "sendOutput() Cannot obtain out writer", ioe);
				return;
			}
			response.setContentType("text/html");
			HTMLView htmlView = (HTMLView)report.getView(OutputFormatType.HTML);
			String head = htmlView.getStringPart("head", false);
			writer.write(head);
			String body = htmlView.getStringPart("body", false);
			writer.write(body);
			writer.flush();
			writer.close();
		} else if(outType.equals(BOConstants.XSL_OUTPUT_TYPE)) {
			String repName = report.getName();
			response.setContentType("application/vnd.ms-excel");
			BinaryView binaryView = (BinaryView)report.getView(OutputFormatType.XLS);
			byte[] content = binaryView.getContent();
			response.setHeader("Content-disposition", "attachement;filename=\"" +  repName + ".xls\"");
			try {
				ServletOutputStream out = response.getOutputStream();
				response.setContentLength(content.length);
				out.write(content);
				out.flush();
				out.close();
			} catch (Exception e) {
					logger.error("Engines"+ this.getClass().getName()+ 
					     "sendOutput() Cannot send output", e);
			}
		} else if(outType.equals(BOConstants.PDF_OUTPUT_TYPE)) {
			response.setContentType("application/pdf");
			BinaryView binaryView = (BinaryView)report.getView(OutputFormatType.PDF);
			byte[] content = binaryView.getContent();
			String repName = report.getName();
			response.setHeader("Content-disposition", "attachement;filename=\"" +  repName + ".pdf\"");
			try {
				ServletOutputStream out = response.getOutputStream();
				response.setContentLength(content.length);
				out.write(content);
				out.flush();
				out.close();
			} catch (Exception e) {
			logger.error("Engines"+ this.getClass().getName()+ 
						     "sendOutput() Cannot send output", e);
			}
		} else {
			logger.error("Engines"+ this.getClass().getName()+ 
						"sendOutput() output type not supported");
		}
	}
	
	

	
	
}
