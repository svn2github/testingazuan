package it.eng.spagobi.engines.bo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bo.rebean.wi.DocumentInstance;
import com.bo.rebean.wi.DrillActionType;
import com.bo.rebean.wi.DrillElement;
import com.bo.rebean.wi.DrillElements;
import com.bo.rebean.wi.DrillInfo;
import com.bo.rebean.wi.DrillPath;
import com.bo.rebean.wi.Report;
import com.bo.rebean.wi.Reports;

public class AddDimensionHandler {

	public void handle(HttpServletRequest request, HttpServletResponse response, 
	           ServletContext servletContext) throws ServletException, IOException {
		
		// get http session 
		HttpSession httpSession = request.getSession();
	    // load document
		DocumentInstance document = (DocumentInstance)httpSession.getAttribute(BOConstants.BODOCUMENT);
		// get current report
		int selectedReport = document.getSelectedReport();
	  	Reports reports = document.getReports();
	  	Report report = reports.getItem(selectedReport);
	  	// get from request id and value 
	  	String dimid = request.getParameter(BOConstants.DIMENSION_ID);
		// get drill info
	  	DrillInfo drillInfo = (DrillInfo)report.getNamedInterface("DrillInfo"); 
	  	// get drill path and add dimension
	  	DrillPath drillPath = drillInfo.getDrillPath();
		DrillElements drillEls = drillPath.getFrom();
		DrillElement drillEl = drillEls.add();
		drillEl.setObjectID(dimid);
		drillPath.setAction(DrillActionType.SLICE);
		// execute drill
		drillInfo.executeDrill();
	  	// set report in session
	  	Utils.addHtmlInSession(report,httpSession);  
	  	// forward to the execution jsp
		String jspexecution = "/jsp/viewDocumentHTML.jsp";	
		RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
		disp.forward(request, response);

	}
	
}
