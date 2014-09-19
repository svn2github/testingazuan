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
	 
%>

<%-- ---------------------------------------------------------------------- --%>
<%-- HTML	 																--%>
<%-- ---------------------------------------------------------------------- --%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/twitter.css" />
	<link rel="stylesheet" type="text/css" href="css/jqcloud.css" />
	<link rel="stylesheet" type="text/css" href="css/socialAnalysis.css" >
    <script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/lib/others/jqcloud-1.0.4.js"></script>

	 
	
	<title>Twitter Analysis</title>
	
</head>
<body>


<div id="navigation">

	<ul>
	    <li><a href=<%= summaryLink %>> Summary</a></li>
	    <li><a href=<%= topicsLink %>>Topics</a></li>
	    <li><a href=<%= networkLink %>>Network</a></li>
	    <li><a href=<%= distributionLink %>>Distribution</a></li>
   	    <li id="activelink"><a href=<%= sentimentLink %>>Sentiment</a></li>
	    <li><a href=<%= impactLink %>>Impact</a></li>
	    <li><a href=<%= roiLink %>>ROI</a></li>
	    <li style="float:right;"><a href="index.jsp">Search</a></li>
	</ul>
        	
    
    <div style="float:left; margin-left: 30px; margin-top: 50px; margin-bottom: 30px;">	
		<img src="img/screens/sentiment_tab.png" ></img>
	</div>	
	
			
</div>        	
			

		
</body>
</html>