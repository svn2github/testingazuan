<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="java.util.Map,
                 java.util.Set,
                 java.util.Iterator,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager,
                 it.eng.spago.util.JavaScript,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.engines.dashboardscomposition.SpagoBIDashboardsCompositionInternalEngine,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 java.util.List,
                 org.safehaus.uuid.UUID,
                 org.safehaus.uuid.UUIDGenerator" %>

<%
String linkVEmbedJs = renderResponse.encodeURL(renderRequest.getContextPath() + "/dashboards/lps/includes/vbembed.js");
String linkEmbedJs = renderResponse.encodeURL(renderRequest.getContextPath() + "/dashboards/lps/includes/embed.js");
%>

<script src="<%=linkVEmbedJs%>" language="JavaScript1.1" type="text/javascript"></script>
<script src="<%=linkEmbedJs%>" type="text/javascript"></script>

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
    %>
<%--
    String movie = renderRequest.getContextPath();
    String relMovie = (String)moduleResponse.getAttribute("movie");
    if(relMovie.startsWith("/"))
    	movie = movie + relMovie;
    else movie = movie + "/" + relMovie;
	String width = (String)moduleResponse.getAttribute("width");
	String height = (String)moduleResponse.getAttribute("height");
	String dataurl = renderRequest.getContextPath();
	String dataurlRel = (String)moduleResponse.getAttribute("dataurl");
	if(dataurlRel.startsWith("/"))
		dataurl = dataurl + dataurlRel;
	else dataurl = dataurl + "/" + dataurlRel;
	Map confParameters = (Map)moduleResponse.getAttribute("confParameters");
	Map dataParameters = (Map)moduleResponse.getAttribute("dataParameters");
--%>

<%--
	// put the two dimensio parameter
	movie += "?paramHeight="+height+"&paramWidth="+width; 

	// create the dataurl string
	dataurl += "?";
	// for each data parameter append to the dataurl 
	Set dataKeys = dataParameters.keySet();
	Iterator iterDataKeys = dataKeys.iterator();
	while(iterDataKeys.hasNext()) {
		String name = (String)iterDataKeys.next();
		String value = (String)dataParameters.get(name);
	    dataurl += name + "=" + value + "&"; 
	}
    // for each conf parameter append to the data url  
	Set confKeys = confParameters.keySet();
	Iterator iterConfKeys = confKeys.iterator();
	while(iterConfKeys.hasNext()) {
		String name = (String)iterConfKeys.next();
		String value = (String)confParameters.get(name);
	    dataurl += name + "=" + value + "&"; 
	}
    // append to the calling url the dataurl	
	movie += "&dataurl=" + dataurl;
--%>
	<%
	// build the back link
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("PAGE", "BIObjectsPage");
	backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");

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
           			<a href='<%= backUrl.toString() %>'>
                 		<img title='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' 
                      		 class='header-button-image-portlet-section'
                      		 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
                      		 alt='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' />
           			</a>
       			</td>
		   		<% if (!possibleStateChanges.isEmpty()) {
    	   				PortletURL formUrl = renderResponse.createActionURL();
  		    			formUrl.setParameter("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
  		   				formUrl.setParameter(SpagoBIConstants.ACTOR,actor );
		   				formUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_CHANGE_STATE);
						formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    			%>
       			<form method='POST' action='<%= formUrl.toString() %>' id='changeStateForm'  name='changeStateForm'>
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
      				<input type='image' class='header-button-image-portlet-section' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' title='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />' alt='<spagobi:message key = "SBIDev.docConf.execBIObject.upStateButt" />'/> 
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
SourceBean data = (SourceBean) content.getAttribute(SpagoBIDashboardsCompositionInternalEngine.DATA);
String lovLabel = (String) data.getAttribute(SpagoBIDashboardsCompositionInternalEngine.LOV_LABEL);
String refreshRate = (String) data.getAttribute(SpagoBIDashboardsCompositionInternalEngine.REFRESH_RATE);
%>


<script type="text/javascript">

	var xmldata<%=uuidStr%>='<%=GeneralUtilities.getLovResult(lovLabel)%>';
	var t<%=uuidStr%> = setTimeout("timedCount<%=uuidStr%>()",0);
	
	var xmlconf<%=uuidStr%>=null;
	
	function timedCount<%=uuidStr%>() {
		refreshData<%=uuidStr%>();
		t<%=uuidStr%> = setTimeout("timedCount<%=uuidStr%>()",<%=refreshRate%>);
	}
	
	function refreshData<%=uuidStr%>() {
		xmlHttp = GetXmlHttpObject<%=uuidStr%>();
    	if (xmlHttp==null) {
  			alert ("Browser does not support HTTP Request");
  			return;
  		}
   		var url="<%=GeneralUtilities.getSpagoBiDashboardServlet()%>";
    	url=url+"?dataname=<%=lovLabel%>";
	    xmlHttp.onreadystatechange=saveResponseIntoJSVariable<%=uuidStr%>; 
	    xmlHttp.open("GET",url,true);
	    xmlHttp.send(null); 
	}
	
    function GetXmlHttpObject<%=uuidStr%>(){ 
  		var objXMLHttp=null
  		if(window.XMLHttpRequest)	{
  			objXMLHttp=new XMLHttpRequest()
  		} else if (window.ActiveXObject) {
  			objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
  		}
  		return objXMLHttp
  	}
  	
	function saveResponseIntoJSVariable<%=uuidStr%>(){ 
      	if( xmlHttp.readyState==4 || xmlHttp.readyState=="complete") { 
  			xmldata<%=uuidStr%>=xmlHttp.responseText;
  		}
  	}
  	
</script>

<%
SourceBean layout = (SourceBean) content.getAttribute(SpagoBIDashboardsCompositionInternalEngine.LAYOUT);
if (layout == null) {
	out.write("NO LAYOUT!!!");
} else {
	String layoutStr = layout.getCharacters();
	int startIndex = 0;
	int startDashboard = layoutStr.indexOf("${");
	int endDashboard = 0;
	int count = 0;
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
		String contextPath = renderRequest.getContextPath();
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
		//confStr = JavaScript.escape(confStr);
		confStr = confStr.replaceAll("'", "\'");
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
	    	lzEmbed({url: '<%=moviePath%>?uuid=<%=uuidStr%>&logicalname=<%=dashboardName%>&lzproxied=false&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('lps/includes/h.html?h='), bgcolor: '#eaeaea', width: '<%=width%>', height: '<%=height%>', id: 'lzapp<%=uuidStr + "_" + count%>', accessible: 'false'}, lzCanvasRuntimeVersion);
	        lzHistEmbed(lzLPSRoot);
	    } else {
	    	document.write('This application requires Flash player ' + lzCanvasRuntimeVersion + '. <a href="http://www.macromedia.com/go/getflashplayer" target="fpupgrade">Click here</a> to upgrade.');
	    }
		</script>
	    <%
	    count++;
	}
	%>
	<script type="text/javascript">
	
	function getxmldata<%=uuidStr%>(logicalName) {
	
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
		
		var x = doc.documentElement;
		var dashboardxml = x.getElementsByTagName(logicalName)[0].getElementsByTagName("ROWS")[0];
		
		// code for IE
		if (window.ActiveXObject) {
			dashboardxmldata<%=uuidStr%> = dashboardxml.xml;
		}
		// code for Mozilla
		else {
			dashboardxmldata<%=uuidStr%> = (new XMLSerializer()).serializeToString(dashboardxml);
		}
		lzSetCanvasAttribute("xmldata", dashboardxmldata<%=uuidStr%>, "false");
	}
	
	function getxmlconf<%=uuidStr%>(logicalName) {
		return eval(logicalName + '<%=uuidStr%>');
	}
	
	</script>
	<%
	
	if (endDashboard < layoutStr.length()) out.write("\n" + layoutStr.substring(endDashboard + 1) + "\n");
}

%>

<%--
<script type="text/javascript">
	lzLPSRoot = '/spagobi/dashboards/';
    lzCanvasRuntimeVersion = 7 * 1;
    if (lzCanvasRuntimeVersion == 6) {
    	lzCanvasRuntimeVersion = 6.65;
    }
    if (isIE && isWin || detectFlash() >= lzCanvasRuntimeVersion) {
    	lzEmbed({url: '/spagobi/dashboards/sbigrid.lzx.swf?&lzproxied=false&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('lps/includes/h.html?h='), bgcolor: '#eaeaea', width: '600', height: '600', id: 'lzapp', accessible: 'false'}, lzCanvasRuntimeVersion);
        lzHistEmbed(lzLPSRoot);
    } else {
    	document.write('This application requires Flash player ' + lzCanvasRuntimeVersion + '. <a href="http://www.macromedia.com/go/getflashplayer" target="fpupgrade">Click here</a> to upgrade.');
    }
       
	function getxmldata(logicalName) {
		lzSetCanvasAttribute("xmldata", xmldata, "false");
	}
	
	function getxmlconf(logicalName) {
		return null;
	}
</script>
--%>  
