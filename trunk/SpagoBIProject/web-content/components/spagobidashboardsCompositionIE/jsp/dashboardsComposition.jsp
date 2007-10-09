<!--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
-->
<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="java.net.URLEncoder,
				 java.util.Map,
                 java.util.Set,
                 java.util.Iterator,
                 it.eng.spago.navigation.LightNavigationManager,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.engines.dashboardscomposition.SpagoBIDashboardsCompositionInternalEngine,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 java.util.List,
                 org.safehaus.uuid.UUID,
                 org.safehaus.uuid.UUIDGenerator,
                 java.util.ArrayList,
                 java.util.HashMap" %>

<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<script src="<%=urlBuilder.getResourceLink(request, "/dashboards/lps/includes/vbembed.js")%>" language="JavaScript1.1" type="text/javascript"></script>
<script src="<%=urlBuilder.getResourceLink(request, "/dashboards/lps/includes/embed.js")%>" type="text/javascript"></script>

<%
	// control if the portlet act with single object modality.
	// get the modality of the portlet (single object execution, entire tree or filter tree)
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
		isSingleObjExec = true;
    // get the actor
    String actor = (String) aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
    // get the module response 
    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
    String title = (String) moduleResponse.getAttribute("title");
    List possibleStateChanges = (List) moduleResponse.getAttribute("possibleStateChanges");
    SourceBean content = (SourceBean) moduleResponse.getAttribute(SpagoBIDashboardsCompositionInternalEngine.DASHBOARDS_COMPOSITION);
    String displayTitleBar = (String) content.getAttribute("displayTitleBar");

    UUIDGenerator generator = UUIDGenerator.getInstance();
    UUID uuid = generator.generateRandomBasedUUID();
    String uuidStr = uuid.toString().replaceAll("-", "");

	//  build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
    
	// IF NOT SINGLE OBJECT MODALITY SHOW DEFAULT TITLE BAR WITH BACK BUTTON
	if(!isSingleObjExec) {
%>

<table class='header-table-portlet-section'>
			<tr class='header-row-portlet-section'>
    			<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           			<%=title%>
       			</td>
       			<td class='header-empty-column-portlet-section'>&nbsp;</td>
       			<td class='header-button-column-portlet-section'>
           			<a href='<%=backUrl%>'>
                 		<img title='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' 
                      		 class='header-button-image-portlet-section'
                      		 src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>' 
                      		 alt='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' />
           			</a>
       			</td>
		   		<% if (!possibleStateChanges.isEmpty()) {   			
		   				Map formUrlPars = new HashMap();
		   				formUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
		   				formUrlPars.put(SpagoBIConstants.ACTOR, actor);
		   				formUrlPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_CHANGE_STATE);
		   				formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
		   		    	String formUrl = urlBuilder.getUrl(request, formUrlPars);
    			%>
       			<form method='POST' action='<%= formUrl %>' id='changeStateForm'  name='changeStateForm'>
	       		<td class='header-select-column-portlet-section'>
      				<select class='portlet-form-field' name="newState">
      				<% 
      		    	Iterator iterstates = possibleStateChanges.iterator();
      		    	while(iterstates.hasNext()) {
      		    		Domain state = (Domain)iterstates.next();
      				%>
      					<option value="<%=state.getValueId() + "," + state.getValueCd()  %>"><%=state.getValueName()%></option>
      				<%}%>
      				</select>
      			</td>
      			<td class='header-select-column-portlet-section'>
      				<input type='image' class='header-button-image-portlet-section' 
      				       src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>' 
      				       title='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />' 
      				       alt='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />'/> 
      			</td>
        		</form>
       			<% } %>
   			</tr>
		</table>

<% 
	// IF SINGLE OBJECT MODALITY SHOW SLIM TITLE BAR
	} else {
%>
		<% 
		  // display the title bar only if the title has been setted
		  if(displayTitleBar.trim().equalsIgnoreCase("true")) { %>
			<table width='100%' cellspacing='0' border='0'>	
				<tr>
					<td align="center" style='vertical-align:middle;' class="portlet-section-header"  >
						<%=title%>
					</td>
				</tr>
			</table>
		<% } %>
<%  } %>















<%
 	// get refresh rate
	String refreshRate = (String) content.getAttribute(SpagoBIDashboardsCompositionInternalEngine.REFRESH_RATE);
	// get the map of couple name dashboard / lov label
	Map lovLabels_Of_dashs = new HashMap();
	String queryStr = "";
	List dashConfs = content.getAttributeAsList("DASHBOARDS_CONFIGURATION.DASHBOARD");
	Iterator iterDashConfs = dashConfs.iterator();
	while(iterDashConfs.hasNext()) {
		SourceBean dashConf = (SourceBean)iterDashConfs.next();
		String lovLabel = (String) dashConf.getAttribute(SpagoBIDashboardsCompositionInternalEngine.LOV_LABEL);
		String dashName = (String) dashConf.getAttribute("name");
		queryStr = queryStr + "LovResLogName_" + dashName + "=" + lovLabel + "&";
		lovLabels_Of_dashs.put(dashName, lovLabel);
	}
	if(queryStr.endsWith("&")) {
		queryStr = queryStr.substring(0, queryStr.length() - 1);
	}
	// get the entire result (all the lov execution combined)
	String xmldataStr = GeneralUtilities.getLovMapResult(lovLabels_Of_dashs);
	// replace all special characters into the combined lov result
	xmldataStr = xmldataStr.replaceAll("\\\\", "\\\\\\\\");
	xmldataStr = xmldataStr.replaceAll("'", "\\\\'");
	xmldataStr = xmldataStr.replaceAll("\n", "");
	xmldataStr = xmldataStr.replaceAll("\t", "");
	xmldataStr = xmldataStr.replaceAll("\r", "");	
%>


<script type="text/javascript">

	var xmldata<%=uuidStr%>='<%=xmldataStr%>';
	var t<%=uuidStr%> = setTimeout("timedCount<%=uuidStr%>()",<%=refreshRate%>);
	
	var xmlconf<%=uuidStr%>=null;
	
	function timedCount<%=uuidStr%>() {
		refreshData<%=uuidStr%>();
		t<%=uuidStr%> = setTimeout("timedCount<%=uuidStr%>()",<%=refreshRate%>);
	}
	
	function refreshData<%=uuidStr%>() {
		<%--url="<%=GeneralUtilities.getSpagoBiDashboardServlet()%>";--%>
		url= "<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>/<%=ChannelUtilities.getSpagoBIContextName(request)%>/DashboardService";
       	pars = "mode=list&<%=queryStr%>";
		new Ajax.Request(url,
          {
            method: 'post',
            parameters: pars,
            onSuccess: function(transport){
                            response = transport.responseText || "";
                            if(response!="") {
                            	xmldata<%=uuidStr%>=response;
                            }
                        },
            onFailure: somethingWentWrong<%=uuidStr%>
          }
        );
	}
	
   
	function somethingWentWrong<%=uuidStr%>(){ 
      	alert('Cannot refresh data, please contact your system administrator');
  	}
  	
</script>










<%
SourceBean layout = (SourceBean) content.getAttribute(SpagoBIDashboardsCompositionInternalEngine.LAYOUT);
if (layout == null) {
	out.write("NO LAYOUT!!!");
} else {
	
	Map linkbaseurlPars = new HashMap();
    String linkbaseurlStr = urlBuilder.getUrl(request, linkbaseurlPars);
	linkbaseurlStr = linkbaseurlStr.replaceAll("&amp;", "&");
	linkbaseurlStr = URLEncoder.encode(linkbaseurlStr);
	String layoutStr = layout.getCharacters();
	int startIndex = 0;
	// minimun delay between one dashboard request and another dashboard's one
	int mindelay = 1000;
	int delay = 0;
	int startDashboard = layoutStr.indexOf("${");
	int endDashboard = 0;
	while (startDashboard != -1) {
		// writes the html between one dashboard and another		
		out.write(layoutStr.substring(startIndex, startDashboard) + "\n");
		%>
		<script type="text/javascript">
		<%
		// finds dashboard name
		endDashboard = layoutStr.indexOf("}", startDashboard);
		String dashboardName = layoutStr.substring(startDashboard + 2, endDashboard);
		// finds dashboard name
		SourceBean dashboardConfSb = null;
		Object dashboardObj = content.getFilteredSourceBeanAttribute(
				"DASHBOARDS_CONFIGURATION.DASHBOARD", "name", dashboardName);
		if (dashboardObj == null) {
			//SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
	    	//		this.getClass().getName(),
	    	//		"execute",
	    	//		"Dashboard with name '" + dashboardName + "' has no configuration!!");
			out.write("Dashboard with name '" + dashboardName + "' has no configuration!!");
			return;
		}
		if (dashboardObj instanceof SourceBean)
			dashboardConfSb = (SourceBean) dashboardObj;
		else {
			//SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
	    	//		this.getClass().getName(),
	    	//		"execute",
	    	//		"Dashboard with name '" + dashboardName + "' has more than one configuration!!");
			out.write("Dashboard with name '" + dashboardName + "' has more than one configuration!!");
			return;
		}
		String contextPath = ChannelUtilities.getSpagoBIContextName(request);
		String moviePath = contextPath + "/dashboards";
		String relMovie = (String) dashboardConfSb.getAttribute("movie");
	    if (relMovie.startsWith("/"))
	    	moviePath = moviePath + relMovie;
	    else moviePath = moviePath + "/" + relMovie;
		String width = (String) dashboardConfSb.getAttribute("DIMENSION.width");
		String height = (String) dashboardConfSb.getAttribute("DIMENSION.height");
		
		// get all the parameters for dashboard configuration
		SourceBean confSB = (SourceBean) dashboardConfSb.getAttribute("CONFIGURATION");
		String confStr = confSB.toXML(false);
		confStr = confStr.replaceAll("'", "\\'");
		confStr = confStr.replaceAll("\n", "");
		confStr = confStr.replaceAll("\t", "");
		confStr = confStr.replaceAll("\r", "");
		// saves the dashboard configuration into a JS variable
		%>
		var <%=dashboardName + uuidStr%> = '<%=confStr%>';
		<%

		startIndex = endDashboard + 1;
		startDashboard = layoutStr.indexOf("${", endDashboard);
		%>
		lzLPSRoot = '<%=contextPath + "/dashboards"%>';
	    lzCanvasRuntimeVersion = 7 * 1;
	    if (lzCanvasRuntimeVersion == 6) {
	    	lzCanvasRuntimeVersion = 6.65;
	    }
	    if (isIE && isWin || detectFlash() >= lzCanvasRuntimeVersion) {
	    	lzEmbed({url: '<%=moviePath%>?delay=<%=delay%>&uuid=<%=uuidStr%>&logicalname=<%=dashboardName%>&linkbaseurl=<%=linkbaseurlStr%>&lzproxied=false&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('lps/includes/h.html?h='), bgcolor: '#ffffff', width: '<%=width%>', height: '<%=height%>', id: 'lzapp<%=uuidStr + dashboardName%>', accessible: 'false'}, lzCanvasRuntimeVersion);
	        lzHistEmbed(lzLPSRoot);
	    } else {
	    	document.write('This application requires Flash player ' + lzCanvasRuntimeVersion + '. <a href="http://www.macromedia.com/go/getflashplayer" target="fpupgrade">Click here</a> to upgrade.');
	    }
		</script>
	    <%
	    delay += mindelay;
	}
	if (endDashboard < layoutStr.length()) out.write("\n" + layoutStr.substring(endDashboard + 1) + "\n");
	%>
	
	
	
	
	
	
	
	
	<script type="text/javascript">
	
	function getxmldata<%=uuidStr%>(logicalName) {
	
	    // if logical name is null or empty the script returns the entire response
		if(logicalName == null || trim(logicalName) == "") {
			document.getElementById("lzapp<%=uuidStr%>" + logicalName).SetVariable("xmldata", xmldata<%=uuidStr%>);
			return;
		}
			
		var dashboardxmldata<%=uuidStr%>;
		// code for IE
		if (window.ActiveXObject) {
		  var doc=new ActiveXObject("Microsoft.XMLDOM");
		  doc.async="false";
		  doc.loadXML(xmldata<%=uuidStr%>);
		}
		// code for Mozilla, Firefox, Opera, etc.
		else {
		  var parser=new DOMParser();
		  var doc=parser.parseFromString(xmldata<%=uuidStr%>,"text/xml");
		}
		// try to get the xml envelope for the logical name
		var x = doc.documentElement;
		var	dashboardxml = x.getElementsByTagName(logicalName)[0];
		if (dashboardxml == null) {
			dashboardxml = x.getElementsByTagName(logicalName.toUpperCase())[0];
		}
		if (dashboardxml == null) {
			dashboardxml = x.getElementsByTagName(logicalName.toLowerCase())[0];
		}
		// if dashboardxml is still null means that the result doesn't contain the logical name tag
		// so the script returns the entire response
		// *****************************************
		if(dashboardxml == null) {
			dashboardxmldata<%=uuidStr%> = xmldata<%=uuidStr%>;
			document.getElementById("lzapp<%=uuidStr%>" + logicalName).SetVariable("xmldata", dashboardxmldata<%=uuidStr%>);
			return;
		}	
		// *****************************************
		// get the rows tag
		var rowsxml = dashboardxml.getElementsByTagName("rows")[0];
		if (rowsxml == null) {
			rowsxml = dashboardxml.getElementsByTagName("ROWS")[0];
		}
		// code for Mozilla
		if (window.XMLSerializer) {
			dashboardxmldata<%=uuidStr%> = (new XMLSerializer()).serializeToString(rowsxml);
		}
		// code for IE
		else {
			dashboardxmldata<%=uuidStr%> = rowsxml.xml;
		}
		document.getElementById("lzapp<%=uuidStr%>" + logicalName).SetVariable("xmldata", dashboardxmldata<%=uuidStr%>);
	}
	
	
	
	function getxmlconfig<%=uuidStr%>(logicalName) {
		document.getElementById("lzapp<%=uuidStr%>" + logicalName).SetVariable("xmlconfig", eval(logicalName + '<%=uuidStr%>'));
	}
	
	
	
	function trim(sString) {
		while (sString.substring(0,1) == ' ') {
			sString = sString.substring(1, sString.length);
		}
		while (sString.substring(sString.length-1, sString.length) == ' ') {
			sString = sString.substring(0,sString.length-1);
		}
		return sString;
	}
	
	
	</script>
	<%
}

%>