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
import com.bo.rebean.wi.DrillBar;
import com.bo.rebean.wi.DrillBarObject;
import com.bo.rebean.wi.DrillElement;
import com.bo.rebean.wi.DrillElements;
import com.bo.rebean.wi.DrillFromElement;
import com.bo.rebean.wi.DrillInfo;
import com.bo.rebean.wi.DrillPath;
import com.bo.rebean.wi.HTMLView;
import com.bo.rebean.wi.Lov;
import com.bo.rebean.wi.OutputFormatType;
import com.bo.rebean.wi.PageNavigation;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.ReportEngine;
import com.bo.rebean.wi.ReportInfo;
import com.bo.rebean.wi.Reports;
import com.bo.rebean.wi.RowValue;
import com.bo.rebean.wi.Values;

public class ChangeReportModeHandler {

private transient Logger logger = Logger.getLogger(BOServlet.class);
	
	public void handle(HttpServletRequest request, HttpServletResponse response, 
			           ServletContext servletContext) throws ServletException, IOException {
		// get mode
		String reportMode = request.getParameter(BOConstants.REPORTMODE);
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
	  	
		DrillInfo drillInfo = (DrillInfo)report.getNamedInterface("DrillInfo");
	    PageNavigation pageNavigation = report.getPageNavigation();	
		if(reportMode.equalsIgnoreCase(BOConstants.REPORTMODEDRILL)) {
			drillInfo.beginDrill();
		} else if(reportMode.equalsIgnoreCase(BOConstants.REPORTMODEVIEW)) {
			drillInfo.endDrill();
		} 
	
		Utils.addHtmlInSession(report, httpSession);
		
		// reload storage token
		//String storageToken = document.getStorageToken();
		//httpSession.setAttribute(BOConstants.STORAGETOKEN, storageToken);
		// forward to the execution jsp
		String fromReportMode = "fromReportMode";
		httpSession.setAttribute("fromReportMode",fromReportMode);
		String jspexecution = "/jsp/viewDocumentHTML.jsp";	
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);
	}  
	
}
