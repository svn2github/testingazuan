package it.eng.spagobi.engines.bo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bo.rebean.wi.DocumentInstance;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.ReportEngine;
import com.bo.rebean.wi.Reports;
import com.bo.rebean.wi.PaginationMode;

public class ChangePageModeHandler {
	public void handle(HttpServletRequest request, HttpServletResponse response, 
	           ServletContext servletContext) throws ServletException, IOException {
		String pageMode = request.getParameter(BOConstants.PAGEMODE);
		HttpSession httpSession = request.getSession();
		// get report server 
		ReportEngine repEngine = (ReportEngine)httpSession.getAttribute(BOConstants.REPORTENGINE);
		DocumentInstance document = (DocumentInstance)httpSession.getAttribute(BOConstants.BODOCUMENT);
		// get current report
		int selectedReport = document.getSelectedReport();
	  	Reports reports = document.getReports();
	  	Report report = reports.getItem(selectedReport);
	  	if(report.getPaginationMode().equals(PaginationMode.Listing)){    
	  		report.setPaginationMode(PaginationMode.Page);
	  	}
	  		else{
	  			report.setPaginationMode(PaginationMode.Listing);   
	  			}
//	  	reload new report with the page changed and put it into session
		Utils.addHtmlInSession(report,httpSession);
		
		// forward to the execution jsp
		String fromButton = "fromButton";
		String jspexecution = "/jsp/viewDocumentHTML.jsp";	
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);	
	  	
	}
}
