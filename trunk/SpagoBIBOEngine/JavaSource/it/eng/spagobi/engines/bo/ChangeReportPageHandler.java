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
import com.bo.rebean.wi.HTMLView;
import com.bo.rebean.wi.OutputFormatType;
import com.bo.rebean.wi.PageNavigation;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.ReportEngine;
import com.bo.rebean.wi.Reports;

public class ChangeReportPageHandler {

private transient Logger logger = Logger.getLogger(BOServlet.class);
	
	public void handle(HttpServletRequest request, HttpServletResponse response, 
			           ServletContext servletContext) throws ServletException, IOException {
		
		//get index page
		String pageIndStr = request.getParameter(BOConstants.REPORTPAGEINDEX);
		// get http session 
		HttpSession httpSession = request.getSession();
		// get report server 
		ReportEngine repEngine = (ReportEngine)httpSession.getAttribute(BOConstants.REPORTENGINE);
		// get storage token
		//String storageToken = (String)httpSession.getAttribute(BOConstants.STORAGETOKEN);
	    // load document
		//DocumentInstance document = repEngine.getDocumentFromStorageToken(storageToken);
		DocumentInstance document = (DocumentInstance)httpSession.getAttribute(BOConstants.BODOCUMENT);
	    // get current report
		int selectedReport = document.getSelectedReport();
	  	Reports reports = document.getReports();
	  	Report report = reports.getItem(selectedReport);
	  	
		
		//Report report = (Report)httpSession.getAttribute(BOConstants.BOREPORT);
		
	    PageNavigation pageNavigation = report.getPageNavigation();	
		if(pageIndStr.equalsIgnoreCase("PREV")) {
			pageNavigation.previous();
		} else if(pageIndStr.equalsIgnoreCase("FIRST")) {
			pageNavigation.first();
		} else if(pageIndStr.equalsIgnoreCase("NEXT")) {
			pageNavigation.next();
		} else if(pageIndStr.equalsIgnoreCase("LAST")) {
			pageNavigation.last();
		}
	
		//reload new report with the page changed and put it into session
		Utils.addHtmlInSession(report,httpSession);
		
		// forward to the execution jsp
		String jspexecution = "/jsp/viewDocumentHTML.jsp";	
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);
		
	}
	
}
