package it.eng.spagobi.engines.bo;

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
		
		//get template string
		//String template = (String)request.getParameter("templatePath");
 		//String spagobibase = (String)request.getParameter("spagobiurl");
 		//byte[] jcrContent = new SpagoBIAccessUtils().getContent(spagobibase, template);
		String template64 = (String)request.getParameter("spagobi_template");
 		BASE64Decoder decoder = new BASE64Decoder();
 		byte[] jcrContent = decoder.decodeBuffer(template64);
 		
		//parse template and recover parameters      
 		   ByteArrayInputStream is = new ByteArrayInputStream(jcrContent);
 	       org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
 	       try{
 	       Document parseDocument = reader.read(is);
 	       /*<BO_DOCUMENT>
 	        <REP_NAME>EtaMedia_2<REP_NAME>
 	        <REP_ID>24</REP_ID>
 	        <REP_TYPE>wid</REP_TYPE>
 	        <REPOSITORY>corporate</REPOSITORY>
 	        </BO_DOCUMENT>
 	        * 
 	        * */

 	                     // get cube properties    
 	       Node cube = parseDocument.selectSingleNode("//BO_DOCUMENT/REP_PROPS");
 	       String reportName = cube.valueOf("@name");
 	       String reportID = cube.valueOf("@id"); 
 	       String repository = cube.valueOf("@repository");
 	       String reportType = cube.valueOf("@type");
 	     
 	    //control parameters
 	    if((reportName==null) || (reportName.trim().equals(""))) {
 				logger.error("Engines"+ this.getClass().getName()+ 
 				 			"service() Cannot find REPORTNAME parameter");
 				return;
 			} 
 	       
		if((reportID==null) || (reportID.trim().equals(""))) {
			logger.error("Engines"+ this.getClass().getName()+ 
			 			"service() Cannot find REPORTID parameter");
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
				return;	
			}
		}	
		// set the type of the report
		logger.info("Engines"+ this.getClass().getName()+ 
					"service() like report type will be used the wid value. " +
					"For this release the only possible type of reports are the ones with .wid extension"); 
		
		// get wiserver form context
		WIServer wiServer = (WIServer)servletContext.getAttribute(BOConstants.WEBISERVER);
		// control connection with the server		
		if(wiServer==null) {
			logger.error("Engines"+ this.getClass().getName()+ 
			 			 "service() No connection with the server");
		    return;
		}
		// initialize wiserver with current request and response
		wiServer.onStartPage(request, response);

		// get http session 
		HttpSession httpSession = request.getSession();
		// get wisession
		WISession wiSession = (WISession)httpSession.getAttribute(BOConstants.BOSESSION);
		if(wiSession == null)
			return;
		
		// get report server 
		ReportEngine repEngine = (ReportEngine)httpSession.getAttribute(BOConstants.REPORTENGINE);

		// open document
		DocumentInstance document  = null;
		document = repEngine.openDocument(reportName, reportID, repository, reportType);
		// fills document parameters values
		Utils.fillPrompts(document, request);
		
		String storageToken = document.getStorageToken();

		// Set image parameters
		ImageOption cdzImageOption = document.getImageOption();
	
		
		cdzImageOption.setImageCallback("viewDocumentImages.jsp");
		cdzImageOption.setImageNameHolder("image");
		cdzImageOption.setStorageTokenHolder("entry");
		
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
		
		// reload storage token
		//storageToken = document.getStorageToken();
		//httpSession.setAttribute(BOConstants.STORAGETOKEN, storageToken);
	
		httpSession.setAttribute(BOConstants.BODOCUMENT, document);
		
		Utils.addHtmlInSession(report, httpSession);
		
		// forward to the execution jsp
		String jspexecution = "";
		if(outputType.trim().equalsIgnoreCase(BOConstants.HTML_OUTPUT_TYPE)){
			jspexecution="/jsp/viewDocumentHTML.jsp";
		} else {
			jspexecution="/jsp/viewDocumentPDF.jsp";	
		}		
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);
 	    
 	       
 	    }
 	       catch (Exception e){
 	    	  logger.error("Engines"+ this.getClass().getName()+ 
	 		  "service() Cannot parse XML document");
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
