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
	
	TwitterInfluencersDataProcessor influencersDP = new TwitterInfluencersDataProcessor();
	List<JSONObject> mostrInfluencersJSON = influencersDP.getMostInfluencersJSON(searchId);
	 
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
	<link rel="stylesheet" type="text/css" href="css/jquery.qtip.css" />
	<link rel="stylesheet" type="text/css" href="css/socialAnalysis.css" >
	<script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/lib/others/freewall.js"></script>
	<script type="text/javascript" src="js/lib/others/jquery.qtip.js"></script>

	
	
	<title>Twitter Analysis</title>
	
</head>
<body>

<div id="navigation">

	<ul>
	    <li><a href=<%= summaryLink %>> Summary</a></li>
	    <li><a href=<%= topicsLink %>>Topics</a></li>
	    <li id="activelink"><a href=<%= networkLink %>>Network</a></li>
	    <li><a href=<%= distributionLink %>>Distribution</a></li>
   	    <li><a href=<%= sentimentLink %>>Sentiment</a></li>
	    <li><a href=<%= impactLink %>>Impact</a></li>
	    <li><a href=<%= roiLink %>>ROI</a></li>
	    <li style="float:right;"><a href="index.jsp">Search</a></li>
	    
	</ul>
        		
	<div id="influencers" class="blank_box topInfluencersMain_box" >
			
		<div class="topInfluencersTitle_box">
			
			<span>Top Influencers</span>
		
		</div>
		
		<br/>
		
		<div id="freewall" class="free-wall" style="margin-top: 20px;"></div>
			
	</div> 
	
	<div style="float:left; margin-left: 30px; margin-top: 50px;">	
		<img src="img/screens/user_mentions.png" ></img>
	</div>
	
	
	<div style="float:left; margin-left: 30px; margin-top: 50px; clear: both; margin-bottom: 30px;">	
		<img src="img/screens/influencers_links.png" ></img>
	</div>
	
	<div style="float:left; margin-left: 30px; margin-top: 50px;">	
		<img src="img/screens/influencers_web.png" ></img>
	</div>
			
</div>        	
			
	<script type="text/javascript">
								
				var temp = "<img class='cell' width='{width}px;' height='{height}px;' src='{lImg}' text='{lText}' data-title='{lTitle}'></img>";
				var w = 61, h = 61, html = '', limitItem = 32;
				
				<%				
					for (JSONObject tempObj : mostrInfluencersJSON) 
					{
						
				%>
						var userInfo = "<%= tempObj.get("followers") %> followers <br/> <%= tempObj.get("description") %>";
						
				//for (var i = 0; i < limitItem; ++i) {
						html += temp.replace(/\{height\}/g, h).replace(/\{width\}/g, w)
							.replace("{lImg}", "<%= tempObj.get("profileImg") %>" )
							.replace("{lText}", userInfo)
							.replace("{lTitle}", "@<%= tempObj.get("username") %>");
				//}
				<%
					}
				%>
								
				$("#freewall").html(html);
				
				var wall = new freewall("#freewall");
				wall.reset({
					selector: '.cell',
					animate: true,
					cellW: 58,
					cellH: 58,
					onResize: function() {
						wall.refresh();
					}
				});
				wall.fitWidth();
				// for scroll bar appear;
				$(window).trigger("resize");
			</script>
								
			<script type="text/javascript">						
				$('.cell').each(function() 
					{
						var t = $(this).attr("text")
						var iTitle = $(this).attr("data-title")
					    $(this).qtip(
					   	{
					    	content: 
					    	{ 
					    		text: t, 
					    		title: '<div><img style="float:left; vertical-align:middle;" src="img/twitter.png" width="20px;" height="20px;" /><div style="vertical-align:middle;margin-left:25px;">' + iTitle + '</div>'
					   		}, 
					   		position: 
					   		{ 
					   			corner: 
					   			{ 
					   				target: 'rightMiddle', 
					   				tooltip: 'leftMiddle' 
					   			}
					   		},
					   		show: 
					   		{ 
					   			solo: true, delay: 1 
					   		}, 
					   		hide: 
					   		{ 
					   			delay: 10 
					   		}, 
					   		style: 
					   		{ 
					   			classes: 'qtip-tipped',
					   			tip: true, 
					   			border: 
					   			{ 
					   				width: 0, 
					   				radius: 4 
					   			}, 
					   			name: 'blue', 
					   			width: 420 
					   		} 
					    }); 
					 });
					
			</script>
			
			

		
</body>
</html>