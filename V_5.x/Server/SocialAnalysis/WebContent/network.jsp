

<%@page import="com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter"%>
<%@page import="twitter4j.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.dataprocessors.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.pojos.*" %>
<%@ page import="twitter4j.JSONObject" %>

    
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

<%!

public List<TwitterInfluencersPojo> getMostInfluencers(String searchID)
{
	return new TwitterInfluencersDataProcessor().getMostInfluencers(searchID);
}

public List<JSONObject> getMostInfluencersJSON(String searchID)
{
	return new TwitterInfluencersDataProcessor().getMostInfluencersJSON(searchID);
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
	    <li id="activelink"><a href=<% out.println(networkLink); %>>Network</a></li>
	    <li><a href=<% out.println(distributionLink); %>>Distribution</a></li>
   	    <li><a href=<% out.println(sentimentLink); %>>Sentiment</a></li>
	    <li><a href=<% out.println(impactLink); %>>Impact</a></li>
	    <li><a href=<% out.println(roiLink); %>>ROI</a></li>
	    
	</ul>
        		
	<div id="influencers" class="blank_box topInfluencersMain_box" >
			
		<div class="topInfluencersTitle_box">
			
			<span>Top Influencers</span>
		
		</div>
		
		<br/>
		
		<div id="freewall" class="free-wall" style="margin-top: 20px;"></div>
			
	</div> 
			
</div>        	
			
	<script type="text/javascript">
								
				var temp = "<img class='cell' width='{width}px;' height='{height}px;' src='{lImg}' text='{lText}' data-title='{lTitle}'></img>";
				var w = 61, h = 61, html = '', limitItem = 32;
				
				<%				
					for (JSONObject tempObj : getMostInfluencersJSON(request.getParameter("searchID"))) 
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