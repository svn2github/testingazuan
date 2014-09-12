

<%@page import="com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter"%>
<%@page import="twitter4j.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.dataprocessors.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.pojos.*" %>


    
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

<%!

public JSONObject getTweetsLocationMap(String keyword)
{
	return new TwitterLocationMapDataProcessor().locationTracker(keyword);
}

	/*****************/
	
	

%>

<div id="navigation">

	<% String summaryLink = "summary.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String topicsLink = "topics.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String networkLink = "network.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String distributionLink = "distribution.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String sentimentLink = "sentiment.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String impactLink = "impact.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String roiLink = "roi.jsp?searchID=" + request.getParameter("searchID"); %>

	<ul>
	    <li><a href=<% out.println(summaryLink); %>> Summary</a></li>
	    <li><a href=<% out.println(topicsLink); %>>Topics</a></li>
	    <li><a href=<% out.println(networkLink); %>>Network</a></li>
	    <li id="activelink"><a href=<% out.println(distributionLink); %>>Distribution</a></li>
   	    <li><a href=<% out.println(sentimentLink); %>>Sentiment</a></li>
	    <li><a href=<% out.println(impactLink); %>>Impact</a></li>
	    <li><a href=<% out.println(roiLink); %>>ROI</a></li>
	    <li style="float:right;"><a href="index.jsp">Search</a></li>
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
			    	var gdpData = <%= getTweetsLocationMap(request.getParameter("searchID")) %>;
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