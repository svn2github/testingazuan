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