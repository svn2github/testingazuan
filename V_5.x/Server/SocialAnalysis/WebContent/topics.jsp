

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
	<link rel="stylesheet" type="text/css" href="css/jqcloud.css" />
	<link rel="stylesheet" type="text/css" href="css/socialAnalysis.css" >
    <script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/lib/others/jqcloud-1.0.4.js"></script>

	 
	
	<title>Twitter Analysis</title>
	
</head>
<body>

<%!


public String getJSONTagCloudArr(String keyword)
{
	return new TwitterTagCloudDataProcessor().tagCloudCreate(keyword).toString();
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
	    <li id="activelink"><a href=<% out.println(topicsLink); %>>Topics</a></li>
	    <li><a href=<% out.println(networkLink); %>>Network</a></li>
	    <li><a href=<% out.println(distributionLink); %>>Distribution</a></li>
  	    <li><a href=<% out.println(sentimentLink); %>>Sentiment</a></li>
	    <li><a href=<% out.println(impactLink); %>>Impact</a></li>
	    <li><a href=<% out.println(roiLink); %>>ROI</a></li>
	</ul>
        		
	<div id="tagcloud" class="blank_box tagCloudMain_box">
			
		<div class="tagCloudTitle_box">
			
			<span>Hashtags Cloud</span>
		
		</div>
			
		<br/>
			
		<div id="my_cloud" class="tagCloud_box"></div>
		
	</div>
			
</div>        	
			
	<script type="text/javascript">
					var word_list = <% out.print(getJSONTagCloudArr(request.getParameter("searchID"))); %>
					$(function() {
	 						$("#my_cloud").jQCloud(word_list);
					});
	</script>
			
			

		
</body>
</html>