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
