<%--
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
--%>

<%@page import="com.bo.rebean.wi.Report,
                it.eng.spagobi.engines.bo.BOConstants,
                it.eng.spagobi.engines.bo.Utils,
                com.bo.rebean.wi.HTMLView,
                com.bo.rebean.wi.OutputFormatType,
                com.bo.wibean.WIServerImpl,
                java.util.Map,
                java.util.Set,
                java.util.Iterator,
                java.util.Enumeration,
                com.bo.rebean.wi.PaginationMode,
                com.bo.rebean.wi.PageNavigation,
                com.bo.rebean.wi.ReportEngine,
                com.bo.rebean.wi.DocumentInstance,
                com.bo.rebean.wi.Reports,
                com.bo.rebean.wi.ReportMode,
                com.bo.rebean.wi.ReportInfo,
                com.bo.rebean.wi.Lov,
                com.bo.rebean.wi.RowValue,
                com.bo.rebean.wi.Values,
                com.bo.rebean.wi.DrillInfo,
                com.bo.rebean.wi.DrillBar,
                com.bo.rebean.wi.DrillBarObject,
                com.bo.rebean.wi.DrillOption,
                com.bo.rebean.wi.DrillPath,
                com.bo.rebean.wi.DrillElements,
                com.bo.rebean.wi.DrillFromElement,
                com.bo.rebean.wi.DrillToElement,
                com.bo.rebean.wi.DrillActionType,
                javax.servlet.ServletContext,
                javax.servlet.RequestDispatcher"%>
                
  
 <!-- Called fromreport drill down link when a drill action down is
       required -->
       
  <% // get http session 
	HttpSession httpSession = request.getSession();
  // load document
	DocumentInstance document = (DocumentInstance)httpSession.getAttribute(BOConstants.BODOCUMENT);
	 // get current report
	int selectedReport = document.getSelectedReport();
	Reports reports = document.getReports();
	Report report = reports.getItem(selectedReport);
	DrillInfo drillInfo = (DrillInfo)report.getNamedInterface("DrillInfo"); 
	DrillOption drillOpt = drillInfo.getDrillOption();
	
	//get from request all drill important parameters
	//the storage token entry
	String sEntry = request.getParameter("sEntry");
	//the value id to which the drill down is executed
	String[] to = request.getParameterValues("to");
	//the hierarchy id interested from the drill 
	String[] from = request.getParameterValues("from");
	String[] hier = request.getParameterValues("hier");
	//the block value
	String block = request.getParameter("block");
	//the filter value
	String[] filter = (String[])request.getParameterValues("filter");
	//the action made by the user 
	String action = request.getParameter("action");
	
	//get the drill path for drill
	DrillPath drillPath = drillInfo.getDrillPath();
	
	//set the drill action 
	if(action.equals("by")){
		drillPath.setAction(DrillActionType.BY);
		}
	else if (action.equals("up")){
		drillPath.setAction(DrillActionType.UP);
	}
	else if (action.equals("down")){
		drillPath.setAction(DrillActionType.DOWN);
	}
	else if (action.equals("slice")){
		drillPath.setAction(DrillActionType.SLICE);
	}
	
	//set the block 
	drillPath.setBlockID(block);
	
	//set the TO drill elements
	if(to.length > 0){
		DrillElements toElements = drillPath.getTo();
		for(int j=0; j<to.length; j++){
			DrillToElement toElement = (DrillToElement)toElements.add();
			toElement.setObjectID(to[j]);
			toElement.setHierarchyID(hier[0]);
		}
	}
	
	//set the FROM drill elements
	if(from.length > 0){
		DrillElements fromElements = drillPath.getFrom();
		for(int k=0; k<from.length; k++){
			DrillFromElement fromElement = (DrillFromElement)fromElements.add();
			fromElement.setObjectID(from[k]);
			//set filters if there are (for the TO filter is not executed)
			//if((filter != null)&&(filter.length > 0)){
				//for(int m=0; m < filter.length; m++) {
					//fromElement.setFilter(filter[m]);
			//	}
			//}
		}
	}
	
	//execute the drill
	
	drillInfo.executeDrill();
	//display the drilled report
    Utils.addHtmlInSession(report,httpSession);

	// forward the execution to the servlet 
	%>
	<script>
		function reloadFromDrill(){
			document.getElementById('execReloadFromDrillForm').submit();
		}
	</script>
	<%
		String urlReloadFromDrillForm = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.RELOADFROMDRILL;
	%>
	<form id="execReloadFromDrillForm" method="post" action="<%=urlReloadFromDrillForm%>" target="_parent">
    </form>
	
	
	
	<%
	//String jspexecution = "/jsp/viewDocumentHTML.jsp";
	//String fromDrillUp = "fromDrillUp";
	//jspexecution = jspexecution + "?fromDrillUp="+fromDrillUp;
	//ServletContext servletContext = httpSession.getServletContext();
	//RequestDispatcher disp = servletContext.getRequestDispatcher(jspexecution);
	//disp.forward(request, response);
   %>
   <head>
   <body onload="reloadFromDrill()">
   </body>
