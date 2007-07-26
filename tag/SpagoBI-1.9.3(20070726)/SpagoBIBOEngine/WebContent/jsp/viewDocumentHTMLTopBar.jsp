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
                com.bo.rebean.wi.HTMLView,
                com.bo.rebean.wi.OutputFormatType,
                com.bo.wibean.WIServerImpl,
                java.util.Map,
                java.util.HashMap,
                java.util.Set,
                java.util.Iterator,
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
                com.bo.rebean.wi.DrillBarObject"%>
        


	<html>
	
		<LINK rel='StyleSheet' href='../css/tab.css' type='text/css' />
	
	
	
	
	<table width="100%" style="background-color:#f8f8f8;border:solid 1px #b8c1ca;">
	   <tr>
	       <td width="70%">
	       		<div style='visibility:visible;' class='UITabs' >
			         <div class="first-tab-level" style="background-color:#f8f8f8">
				          <div style="overflow: hidden; width:  100%">

<%
	WIServerImpl wiServer =  (WIServerImpl)application.getAttribute(BOConstants.WEBISERVER);
	wiServer.onStartPage(request, response);  	
	ReportEngine reportEngine = (ReportEngine)session.getAttribute(BOConstants.REPORTENGINE);
  	//String storageToken = (String)session.getAttribute(BOConstants.STORAGETOKEN);
  	//DocumentInstance document = reportEngine.getDocumentFromStorageToken(storageToken);
  	DocumentInstance document = (DocumentInstance)session.getAttribute(BOConstants.BODOCUMENT);
  	int selectedReport = document.getSelectedReport();
  	Reports reports = document.getReports();
  	Report report = reports.getItem(selectedReport);
  	//Report report = (Report)session.getAttribute(BOConstants.BOREPORT);
  	Map docReports = (Map)session.getAttribute(BOConstants.DOCUMENTREPORTMAP); 
	Set keySet = docReports.keySet();
	Iterator iterKey = keySet.iterator();
	while(iterKey.hasNext()){
		String className = "tab";
		String url = "javascript:void(0);";
		Integer index = (Integer)iterKey.next();
		String nameReport = (String)docReports.get(index);
		if(nameReport.equals(report.getName())) {
			className = "tab selected";
		} else {
			url = "../BOServlet?"+ BOConstants.OPERATION + "=" + BOConstants.OPERATION_CHANGEREPORT + 
				   "&" + BOConstants.REPORTINDEX + "=" + index;
		}
%>	
        					   <div class='<%=className%>'>
      						      <a href='<%=url%>' style="color:black;" target="_parent"> 
      							       <%=nameReport%>
      						      </a>
      					     </div>
<%
	}
%>	

				          </div>
			         </div>
		        </div>
		     </td>		
<%

	boolean isPaginated = false;
	Integer currPage = null;
	Integer lastPage = null;
	String currPageStr = "";
	String lastPageStr = "";
	boolean isLast = false;
	boolean isFirst = false;
	PaginationMode paginationMode = report.getPaginationMode();
	int pagModeVal = paginationMode.value();
	if(pagModeVal == PaginationMode._Page) {
		isPaginated = true;
		PageNavigation pageNavigation = report.getPageNavigation();		
		currPage = new Integer(pageNavigation.getCurrent());
		currPageStr = currPage.toString();
		isLast = pageNavigation.isLast();
		isFirst =  pageNavigation.isFirst(); 
		//get the report pages number:move to the last page and then get its index
		pageNavigation.last();
		lastPage = new Integer(pageNavigation.getCurrent());
		lastPageStr = lastPage.toString();
		//then return to the current page
		pageNavigation.setTo(currPage.intValue());
	}

	
	boolean isDrill = false;
	ReportMode reportMode = report.getReportMode();
	int repModVal = reportMode.value();
	String repID = report.getID();  
	String endDrill = null;
	String beginDrill = null;
	if(repModVal == ReportMode._Analysis){
		isDrill = true;	
		endDrill = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.OPERATION_CHANGEMODE + 
        "&" + BOConstants.REPORTMODE + "=" + BOConstants.REPORTMODEVIEW;
	} else {
		isDrill = false;
		beginDrill = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.OPERATION_CHANGEMODE + 
        "&" + BOConstants.REPORTMODE + "=" + BOConstants.REPORTMODEDRILL;
	}
	
	
	
	
	
	
	if(isPaginated) {
		String baseurl = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.OPERATION_CHANGEPAGE +
				         "&" + BOConstants.REPORTPAGEINDEX + "=";	
%>		

    			<td width="20px" align="center">
    			   <%if(!isFirst) { %>  
    			   <a target="_parent" href="<%=baseurl + "FIRST"%>">
    			   		<img width="20px" height="20px" border="0" 
                     src="../images/first.jpg"  alt="draft" title="draft"/>
    			   </a>
    			   <% } %>
    			</td>
    			<td width="20px" align="center">
    			   <%if(!isFirst) { %> 
    			   <a target="_parent"  href="<%=baseurl + "PREV"%>">
    			   <img width="20px" height="20px" border="0" src="../images/prev.jpg"  alt="draft" title="draft"/>
    			   </a>
    			   <% } %>
    			</td>
    			<td>&nbsp;</td>
    			<td width="80px" align="center" style="font-family:arial;font-size:10px;">
    			    Page <b><%=currPageStr %></b> of <b><%=lastPageStr %></b>
    			</td>
    			<td>&nbsp;</td>
    			<td width="20px" height="20px" width="20px" align="center">
    			  <%if(!isLast) { %> 
    			  <a target="_parent"  href="<%=baseurl + "NEXT"%>">
    			   <img width="20px" height="20px" border="0" src="../images/next.jpg"  alt="draft" title="draft"/>
    			  </a> 
    			  <% } %>
    			</td>
    			<td width="20px" align="center">
    			   <%if(!isLast) { %> 
    			   <a target="_parent"  href="<%=baseurl + "LAST"%>">
    			       <img width="20px" height="20px" border="0" src="../images/last.jpg"  alt="draft" title="draft"/>
    			   </a>
    			    <% } %>
    			</td>
    			<td width="20px" align="center">
    			  <%String draftModeUrl = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.OPERATION_CHANGEPAGEMODE +
    				         "&" + BOConstants.PAGEMODE + "=" + BOConstants.DRAFT;%>
    	         		<a href='<%=draftModeUrl%>' style="color:black;" target="_parent">
    	         			<img width="20px" height="20px"  border="0" src="../images/singlePage.jpg"  alt="draft" title="draft"/>
    	         		</a>	
    			</td>
    			
    			<td width="20px">
    			    <% if(isDrill) { %>
			   		<a target="_parent" href="<%=endDrill%>" alt="view">
            			<img border="0" src="../images/viewmode.gif" title="view" alt="view" />	
          			</a>  
          			<% } else { %>
          			<a target="_parent" href="<%=beginDrill%>" alt="drill">
          				<img border="0" src="../images/drillmode.gif" title="drill" alt="drill" /></a> 
          			<% } %>
			  	</td>
	
<%
	} else  {
%>

          	  <td>&nbsp;</td>
		      <td align="right">
		          <%String pageModeUrl = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.OPERATION_CHANGEPAGEMODE +
				        "&" + BOConstants.PAGEMODE + "=" + BOConstants.PAGE;%>
	            <a href='<%=pageModeUrl%>' style="color:black;" target="_parent">
		            <img width="20px" height="20px" border="0" src="../images/multiPage.jpg"  alt="draft" title="draft"/>
	            </a>	         
		      </td>
		      <td width="20px">
    			    <% if(isDrill) { %>
			   		<a target="_parent" href="<%=endDrill%>" alt="view">
            			<img border="0" src="../images/viewmode.gif" title="view" alt="view" />	
          			</a>  
          			<% } else { %>
          			<a target="_parent" href="<%=beginDrill%>" alt="drill">
          				<img border="0" src="../images/drillmode.gif" title="drill" alt="drill" />
          			</a> 
          			<% } %>
			  	</td>

<%

	}
%>

		  </tr>
	</table>





<%-- ***************************** DRILL BAR ****************************************  --%>

<%
	if(isDrill){
%>		
	<table width="100%" style="background-color:#f8f8f8;border:solid 1px #b8c1ca;">
    <tr>
				<td width="20px"> 
					<a href="javascript:showAddMenu();" />
						<img border="0" src="../images/attach.gif" title="Add Dimension" alt="Add Dimension" />
					</a> 
				</td>	
 
<%				
		//retrieve all drill objects for drill
		DrillInfo drillInfo = (DrillInfo)report.getNamedInterface("DrillInfo");
		drillInfo.beginDrill();
		RowValue rowVal = null;
		DrillBar drillBar = drillInfo.getDrillBar();
		int numDrillObj = drillBar.getCount();
        
		HashMap hash = (HashMap)session.getAttribute(BOConstants.ISFIRSTDRILL);
		String fromReportMode = (String)session.getAttribute("fromReportMode");
		if(fromReportMode == null) {
			fromReportMode = "notFromReportMode";
		}
		if (hash == null){//the hash is null so it is the first time
			hash = new HashMap();
			hash.put(repID,"first");
			session.setAttribute(BOConstants.ISFIRSTDRILL, hash);
		}
		else if(hash.get(repID)== null)	{//it is the first time
			hash.put(repID, "first");
			session.setAttribute(BOConstants.ISFIRSTDRILL, hash);
		}else{//it is not the first time
			hash.remove(repID);
			hash.put(repID,"notfirst");
		}
		for(int i=0; i<numDrillObj; i++) {
			DrillBarObject drillBarObject = drillBar.getItem(i);
			String nameDrillObj = drillBarObject.getName();
			String idDrillObj = drillBarObject.getID();
			String allValues = "All"+" "+nameDrillObj;   
			String filter = drillBarObject.getFilter();
			hash = (HashMap)session.getAttribute(BOConstants.ISFIRSTDRILL);
			String firstValue = "";
			if (hash != null){
				firstValue = (String)hash.get(repID);
				
			}
			if((hash.get(repID) == null) || !filter.equals("") || firstValue.equals("first") || !fromReportMode.equals("fromReportMode")){
			Lov lovDrillObj = drillBarObject.getLOV();
			Values values = lovDrillObj.getAllValues(); 
%>
			<td style="padding-left:10px;padding-right:10px;" >			
				<span style="font-size:11px;font-family:arial;color:darkblue;"><%=nameDrillObj%></span>
				<br/>
        <select onchange="execDrill('<%=idDrillObj%>', this.value)" name="<%=idDrillObj %>" >
					<option val="">All</option>
				<%
					int valNum = values.getCount();
					for(int j=0; j<valNum; j++) {
			 			String val = values.getValue(j);
			 			String selected = " ";
			 			if(filter.equals(val))
			 				selected = " selected ";
			 	%>
			 		<option value="<%=val%>" <%=selected%> ><%=val%></option>
			 	<%
			 		}
				%>
					<option value="remove">Remove</option>
				</select>
			</td>
			
			
	<%			}//end if
	else {
		 String value = nameDrillObj+" "+"(No Values)";
		%>
			<td style="padding-left:10px;padding-right:10px;">
			<select onchange="execDrill('<%=idDrillObj%>', this.value)" name="<%=idDrillObj %>" >
			<option value="no values" selected="selected"><%=value%></option>
			<option value="remove">Remove</option>
			</select>
			</td>
		
		
	<% 
	}
	
		}// end for
		//at the end of loading toolbar, remove the attribute from session
		if(fromReportMode != null){
		session.removeAttribute("fromReportMode"); 
		}
	%>
        <td width="100%">&nbsp;</td>
	   </tr>
	</table>

	
	<script>
		function execDrill(id, value) {
			document.getElementById('idDrillObj').value=id;
			document.getElementById('valueDrillObj').value=value;
			document.getElementById('execDrillForm').submit();
		}
		
		function showAddMenu() {
        parent.frames.content.document.getElementById('addMenu').style.display='inline';
    }
		
	</script>
	<%
		String urlDrillForm = "../BOServlet?" + BOConstants.OPERATION + "=" + BOConstants.OPERATION_DRILL;
	%>
	<form id="execDrillForm" method="post" action="<%=urlDrillForm%>" target="_parent">
		<input id="idDrillObj" name="idDrillObj" type="hidden" value="" />
	    <input id="valueDrillObj" name="valueDrillObj" type="hidden" value="" />
	</form>
	
	<% } %>
	
	

		

	</html>	
	
<%
	//storageToken = document.getStorageToken();
	//session.setAttribute(BOConstants.STORAGETOKEN, storageToken);
	wiServer.onEndPage();
%>
