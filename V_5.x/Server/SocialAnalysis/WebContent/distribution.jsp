<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA IMPORTS															--%>
<%-- ---------------------------------------------------------------------- --%>

<%@ page import="it.eng.spagobi.twitter.analysis.dataprocessors.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.pojos.*" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="twitter4j.JSONObject"%>


<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA CODE 																--%>
<%-- ---------------------------------------------------------------------- --%>

<%
	String searchId = request.getParameter("searchID");
	
	String summaryLink = "summary.jsp?searchID=" + searchId;
	String topicsLink = "topics.jsp?searchID=" + searchId; 
	String networkLink = "network.jsp?searchID=" + searchId; 
	String distributionLink = "distribution.jsp?searchID=" + searchId; 
	String sentimentLink = "sentiment.jsp?searchID=" + searchId; 
	String impactLink = "impact.jsp?searchID=" + searchId; 
	String roiLink = "roi.jsp?searchID=" + searchId;
	
	TwitterLocationMapDataProcessor locationMapDP = new TwitterLocationMapDataProcessor();
	JSONObject mapData = locationMapDP.locationTracker(searchId);
	 
%>

<%-- ---------------------------------------------------------------------- --%>
<%-- HTML	 																--%>
<%-- ---------------------------------------------------------------------- --%>


    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/twitter.css" />
	<link rel="stylesheet" type="text/css" href="css/jquery-jvectormap-1.2.2.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="css/socialAnalysis.css" >
	<script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script src="js/lib/others/jquery-jvectormap-1.2.2.min.js"></script>
	<script src="js/lib/others/jquery-jvectormap-world-mill-en.js"></script>

	
	<title>Twitter Analysis</title>
	
</head>
<body>

<div id="navigation">

	<ul class="navtabs tabsStyle">
	    <li class="navtabs"><a href=<%= summaryLink %>> Summary</a></li>
	    <li class="navtabs"><a href=<%= topicsLink %>>Topics</a></li>
	    <li class="navtabs"><a href=<%= networkLink %>>Network</a></li>
	    <li class="navtabs" id="activelink"><a href=<%= distributionLink %>>Distribution</a></li>
   	    <li class="navtabs"><a href=<%= sentimentLink %>>Sentiment</a></li>
	    <li class="navtabs"><a href=<%= impactLink %>>Impact</a></li>
	    <li class="navtabs"><a href=<%= roiLink %>>ROI</a></li>
	    <li class="navtabs" style="float:right;"><a href="index.jsp">Search</a></li>
	</ul>
        		
	<div id="locationbox" class="blank_box worldMapMain_box">
			
		<div class="worldMapTitle_box">
		
			<span>Location Tweets</span>
		
		</div>
		
		<br/>
	
		<div id="world-map" class="worldMap_box"></div>
				
	</div>
			
</div>        	
			
	 <script>
			    $(function()
			    {
			    	var gdpData = <%= mapData %>;
			      $('#world-map').vectorMap({
			    	  map: 'world_mill_en',
			    	  series: {
			              regions: [{
			            	  
			                values: gdpData,
			                scale: ['#ffffff', '#0071A4'],
			                normalizeFunction: 'polynomial'
			              }]
			            },
			            onRegionLabelShow: function(e, el, code)
			            {
			                el.html(el.html()+' (# tweets - '+gdpData[code]+')');
			            }
			      });
			    });
		  </script>
		  		
</body>
</html>