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
                com.bo.rebean.wi.PageNavigation,
                com.bo.rebean.wi.ReportEngine,
                com.bo.rebean.wi.DocumentInstance,
                com.bo.rebean.wi.Reports"%>
<%@page import="com.bo.rebean.wi.DrillInfo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.bo.rebean.wi.DrillDimension"%>
<%@page import="it.eng.spagobi.engines.bo.Utils"%>


<%
	//ReportEngine reportEngine = (ReportEngine)session.getAttribute(BOConstants.REPORTENGINE);
	//String storageToken = (String)session.ge tAttribute(BOConstants.STORAGETOKEN);
	//DocumentInstance document = reportEngine.getDocumentFromStorageToken(storageToken);
	//HTMLView reportHtmlView = (HTMLView) report.getView(OutputFormatType.HTML);


	WIServerImpl wiServer =  (WIServerImpl)application.getAttribute(BOConstants.WEBISERVER);
	wiServer.onStartPage(request, response); 
  	DocumentInstance document = (DocumentInstance)session.getAttribute(BOConstants.BODOCUMENT);
  	int selectedReport = document.getSelectedReport();
  	Reports reports = document.getReports();
  	Report report = reports.getItem(selectedReport);
  	DrillInfo drillInfo = (DrillInfo)report.getNamedInterface("DrillInfo");
  	Map drillHier = new HashMap();
  	if(report.getReportMode().value() == report.getReportMode().Analysis.value()){
  		try {
  			drillHier = Utils.getDrillDimensions(drillInfo);
  		} catch (Exception e) {
  			out.write("Error while getting drill dimensions. Try restarting document execution.");
  			return;
  		}
  	}
  	Set drilHierNames = drillHier.keySet();
  	Iterator iterDrillHier = drilHierNames.iterator();
  	String head = (String)session.getAttribute(BOConstants.REPORTHEADPART);
  	String body = (String)session.getAttribute(BOConstants.REPORTBODYPART);
%>
	
  
  
  <!-- ***************DISPLAY THE HTML REPORT************************************* -->
	<style>

	   .addMenu {
        display:none;
        position:absolute;
        top:0px;
        left:10px;
        background-color:#f8f8f8;
        border: 2px solid gray;
     }
     
     .addMenuList{
        padding-right: 30px;
     }
     
     .addMenuClose{
        background-color:white;
        margin:0px;
        padding-right:5px;
        font-weight:bold; 
        text-align:right;
        border-bottom:1px solid gray;
     }
     
     .list {
        list-style-type:none;
        margin-left:20px;
     }
     
     .listFirstLevel {
        font-size:11px;
        font-family:arial;
        font-weight:bold;
        color:darkblue;
     }
     
     .listSecondLevel {
        font-size:11px;
        font-family:arial;
        color:darkblue;
        font-weight:normal;
        text-decoration:none;
     }
     
	</style>
	
	
	<script>
	   function closeAddMenu() {
        document.getElementById('addMenu').style.display="none";
     }
	
	</script>
	
	
  

<html>
	 
	
		<div>
					<%-- 
					<%=reportHtmlView.getStringPart("head", false) %>
					<%=reportHtmlView.getStringPart("body", false)%>
					--%>
					<%=head%>
					<%=body%>
			</div>
			
		 <div id="addMenu" class="addMenu">
		     <div class="addMenuClose">
		          <a style="text-decoration:none;color:red;" href="javascript:closeAddMenu();" />
                  x
              </a>
		     </div>
         <div class="addMenuList"> 
          	 <ul class="list">
             <% while(iterDrillHier.hasNext()) {
          		 	String drillHierName = (String)iterDrillHier.next();
             %>
                <li class="listFirstLevel"> 
          	 		<%=drillHierName%>
          	 		<ul class="list">
          	 		<%
          	 			List dims = (List)drillHier.get(drillHierName);
          	    		Iterator iterDims = dims.iterator();
          	    		while(iterDims.hasNext()) {
          	    			DrillDimension drilldim = (DrillDimension)iterDims.next();
          	 				String drillName = drilldim.getName();
          	 				String iddim = drilldim.getID();
          	 				String url = "../BOServlet?"+ BOConstants.OPERATION + "=" + 
          	 						     BOConstants.OPERATION_ADD_DIM + 
         				   				 "&" + BOConstants.DIMENSION_ID + "=" + iddim;
          	 		%>
          	 			<li>
          	 				<a class="listSecondLevel" 
          	 				   href="<%=url%>"
          	 				   target="_parent"><%=drillName%></a>
          	 			</li>
          	 		<%
          	    		}
          	 		%>
          	 		</ul>
          	 	</li>
          	 <% } %>
             </ul>	  
          </div>
	  	 </div>	
			
	</html>	
	
<%
    //String storageToken = document.getStorageToken();
	//session.setAttribute(BOConstants.STORAGETOKEN, storageToken);
	wiServer.onEndPage();
%>