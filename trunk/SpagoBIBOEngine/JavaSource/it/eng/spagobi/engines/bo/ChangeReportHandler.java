package it.eng.spagobi.engines.bo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bo.rebean.wi.DocumentInstance;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.ReportEngine;
import com.bo.rebean.wi.Reports;

public class ChangeReportHandler {

	private transient Logger logger = Logger.getLogger(BOServlet.class);
	
	public void handle(HttpServletRequest request, HttpServletResponse response, 
	           ServletContext servletContext) throws ServletException, IOException {
		
		// get http session 
		HttpSession httpSession = request.getSession();
		// get report index
		String repInd = request.getParameter(BOConstants.REPORTINDEX);
		// get report server 
		ReportEngine repEngine = (ReportEngine)httpSession.getAttribute(BOConstants.REPORTENGINE);
		// get storage token
		//String storageToken = (String)httpSession.getAttribute(BOConstants.STORAGETOKEN);
	    // load document
		//DocumentInstance document = repEngine.getDocumentFromStorageToken(storageToken);
		DocumentInstance document = (DocumentInstance)httpSession.getAttribute(BOConstants.BODOCUMENT);
		document.setSelectedReport(new Integer(repInd).intValue());
		
		Reports reports = document.getReports();
	    Report report = reports.getItem(new Integer(repInd).intValue());
		
		Utils.addHtmlInSession(report, httpSession);
		
		// reload storage token
		//storageToken = document.getStorageToken();
		//httpSession.setAttribute(BOConstants.STORAGETOKEN, storageToken);
	    
		// forward to the execution jsp
		String jspexecution = "/jsp/viewDocumentHTML.jsp";	
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);
	}

}
