

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
	<link rel="stylesheet" type="text/css" href="css/timeline.css" >
	<link rel="stylesheet" type="text/css" href="css/jquery.qtip.css" />
	<link rel="stylesheet" type="text/css" href="css/jquery-jvectormap-1.2.2.css" media="screen"/>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script src="js/lib/others/jquery-ui.min.js"></script>
	<script type="text/javascript" src="js/lib/others/jqcloud-1.0.4.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.time.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.selection.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.navigate.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.4/d3.min.js"></script>
	<script src="js/lib/others/d3pie.min.js"></script>
	<script type="text/javascript" src="js/lib/others/freewall.js"></script>
	<script type="text/javascript" src="js/lib/others/jquery.qtip.js"></script>
	<script src="js/lib/others/jquery-jvectormap-1.2.2.min.js"></script>
	<script src="js/lib/others/jquery-jvectormap-world-mill-en.js"></script>

	
	<title>Twitter Analysis</title>
	
</head>
<body>

<%!


public String getJSONTagCloudArr(String keyword)
{
	return new TwitterTagCloudDataProcessor().tagCloudCreate(keyword).toString();
}

public List<TwitterTopTweetsPojo> getTopTweets(String keyword)
{
	return new TwitterTopTweetsDataProcessor().getTopTweetsData(keyword);
}

public List<TwitterInfluencersPojo> getMostInfluencers(String keyword)
{
	return new TwitterInfluencersDataProcessor().getMostInfluencers(keyword);
}

public JSONObject getTweetsLocationMap(String keyword)
{
	return new TwitterLocationMapDataProcessor().locationTracker(keyword);
}

public int getTotalTweets(String keyword)
{
	return new TwitterGeneralStatsDataProcessor().totalTweetsCounter(keyword);
}

public int getTotalUsers(String keyword)
{
	return new TwitterGeneralStatsDataProcessor().totalUsersCounter(keyword);
}

public TwitterPiePojo getTweetsPieChart(String keyword)
{
	return new TwitterPieDataProcessor().getTweetsPieChart(keyword);
}

public List<TwitterTimelinePojo> getDailyTimelineChartObjs(String keyword)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsDaily(keyword);
}

public List<TwitterTimelinePojo> getWeeklyTimelineChartObjs(String keyword)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsWeekly(keyword); 
}

public List<TwitterTimelinePojo> getMonthlyTimelineChartObjs(String keyword)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsMonthly(keyword); 
}
	

	/*****************/
	
	

%>

<div id="navigation">

	<% String summaryLink = "summary.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String topicsLink = "topics.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String networkLink = "network.jsp?searchID=" + request.getParameter("searchID"); %>
	<% String distributionLink = "distribution.jsp?searchID=" + request.getParameter("searchID"); %>

	<ul>
	    <li><a href=<% out.println(summaryLink); %>> Summary</a></li>
	    <li><a href=<% out.println(topicsLink); %>>Topics</a></li>
	    <li><a href=<% out.println(networkLink); %>>Network</a></li>
	    <li id="activelink"><a href=<% out.println(distributionLink); %>>Distribution</a></li>
	</ul>
        		
	<div id="locationbox" class="top-twitter box" style="display:block; width:80%; height:400px; float:left; ">
			
		<span class="title">Location Tweets</span>
		
		<br/>
	
		<div id="world-map" style="width:80%; height: 400px; margin-top: 20px;"></div>
	
		<div style="clear: both;"></div>
		<br/>
				
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