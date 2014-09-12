

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
	<link rel="stylesheet" type="text/css" href="css/timeline.css" >
	<link rel="stylesheet" type="text/css" href="css/socialAnalysis.css" >
    <script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.time.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.selection.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.navigate.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/d3.min.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/d3pie.min.js"></script>
	<script src="js/lib/others/d3pie.min.js"></script>
	
	<title>Twitter Analysis</title>
	
</head>
<body>

<%!

public List<TwitterTopTweetsPojo> getTopTweets(String searchID)
{
	return new TwitterTopTweetsDataProcessor().getTopTweetsData(searchID, 30);
}

public List<TwitterTopTweetsPojo> getTopRecentTweets(String searchID)
{
	return new TwitterTopTweetsDataProcessor().getTopRecentTweetsData(searchID, 30);
}

public int getTotalTweets(String searchID)
{
	return new TwitterGeneralStatsDataProcessor().totalTweetsCounter(searchID);
}

public int getTotalUsers(String searchID)
{
	return new TwitterGeneralStatsDataProcessor().totalUsersCounter(searchID);
}

/*************** START PIE CHARTS *****************************************************/

public TwitterPiePojo getTweetsPieChart(String searchID)
{
	return new TwitterPieDataProcessor().getTweetsPieChart(searchID);
}

public List<TwitterPieSourcePojo> getTweetsPieChartDevice(String searchID)
{
	return new TwitterPieDataProcessor().getTweetsPieSourceChart(searchID);
}


/*************** END PIE CHARTS *****************************************************/

/*************** START TIMELINE *****************************************************/

public List<TwitterTimelinePojo> getTimelineObjsHourRounded(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjs(searchID, "hours");
}

public List<TwitterTimelinePojo> getTimelineObjsDayRounded(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjs(searchID, "days");
}

public List<TwitterTimelinePojo> getTimelineObjsWeekRounded(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjs(searchID, "weeks"); 
}

public List<TwitterTimelinePojo> getTimelineObjsMonthRounded(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjs(searchID, "months"); 
}

public List<TwitterTimelinePojo> getRangeTimelineChartObjs(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsRangeTime(searchID); 
}

/*************** END TIMELINE *****************************************************/

public String getMinSearchDate(String searchID)
{
	return new TwitterTimelineDataProcessor().getMinDateSearch(searchID);
}

public String getMaxSearchDate(String searchID)
{
	return new TwitterTimelineDataProcessor().getMaxDateSearch(searchID);
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
	    <li id="activelink"><a href=<% out.println(summaryLink); %>> Summary</a></li>
	    <li><a href=<% out.println(topicsLink); %>>Topics</a></li>
	    <li><a href=<% out.println(networkLink); %>>Network</a></li>
	    <li><a href=<% out.println(distributionLink); %>>Distribution</a></li>
	    <li><a href=<% out.println(sentimentLink); %>>Sentiment</a></li>
	    <li><a href=<% out.println(impactLink); %>>Impact</a></li>
	    <li><a href=<% out.println(roiLink); %>>ROI</a></li>
	    <li style="float:right;"><a href="index.jsp">Search</a></li>
	</ul>
        		
	<div class="generalinfo">

		<div class="generalInfo_main">
	
			<div class="generalInfo_box">
				<span class="generalInfo_infos" ><%= getTotalTweets(request.getParameter("searchID")) %></span>
				<br>
				<span class="generalInfo_label" >tweets</span>
			</div>
		
			<div class="generalInfo_box">
				<span class="generalInfo_infos" ><%= getTotalUsers(request.getParameter("searchID")) %></span>
				<br>
				<span class="generalInfo_label">users</span>
			</div>
			
			<div class="blank_box dateRange_box" style="float:left;">
				<span class="dateRange_dates"><%= getMinSearchDate(request.getParameter("searchID")) %></span>
				<br />
				<span class="dateRange_dates"><%= getMaxSearchDate(request.getParameter("searchID")) %></span>
				<br />
				<span class="dateRange_label">Date range</span>
			</div>

		</div>
		
	</div>
		
	<div id="timeline" class="timeline_main">		
		
		<div class="demo-container" style="width: 100%; height: 60%;">
			<div id="hormenu">
				<ul> 
					<li><span>View</span>
						<ul>
							<li><a id="months" style="cursor:pointer;">Months</a>							
				          	<li><a id="weeks" style="cursor:pointer;">Weeks</a></li>
				          	<li><a id="days" style="cursor:pointer;">Days</a></li>
				          	<li><a id="hours" style="cursor:pointer;">Hours</a></li>
				     	</ul>
				 	</li>
			</div>
			<div id="main-graph" style="vertical-align: middle !important;"></div>
			<div id="placeholder" class="demo-placeholder"></div>
		</div>
		<div class="demo-container" style="width: 100%; height: 40%">
			<div id="overview" class="demo-placeholder-o"></div>
		</div>	
	</div>

		<!-- <div class="demo-container" style="width: 550px; height: 125px;">
			<div id="overview" class="demo-placeholder-o"></div>
		</div>		
		 -->
	</div>
		
	<div id="pieBoxMain" style="float:left; width:400px">
		
		<div class="blank_box pieChart_box"  ">
			<span style="font-weight: bold; font-size: 20px; margin-left: 30%;">Tweets Summary</span>	
			<div id="pieChart" style="float:left"></div>
			
		</div>
		
		<div class="blank_box pieChart_box" style="margin-top: 20px; margin-bottom: 30px;">
			<span style="font-weight: bold; font-size: 20px; margin-left: 30%;">Tweets Sources</span>
			<div id="pieChartDevice" style="float:left;"></div>
			
		</div>
	</div>
		
	<div id="toptweets" class="blank_box twitterTopWidget_box">
			
		<div class="twitterTopTitle_box">	
			<span>Top Tweets</span>
		</div>
				
		<div class="twitterTopRT_box">
			<ol>
					<%						
						for (TwitterTopTweetsPojo topObj : getTopTweets(request.getParameter("searchID"))) 
						{ 
							
					%>			
					
					<li>
						<div class="tweetData">

							<img class="tweetprofileimg" src="<%= topObj.getProfileImgSrcFromDB() %>" />

							<div class="tweettext">					
									<div>
										<a href="https://twitter.com/<%= topObj.getUsernameFromDb() %>" ><%= topObj.getUsernameFromDb() %></a>
										<span style="float:right;"><%= topObj.getCreateDateFromDb() %></span>
										<span class="retweetClass"><%= topObj.getCounterRTs() %></span>	
										<img style="float:right;" src="img/retweet.png" />																					
									</div>
								<p>
									<%= topObj.getTweetText() %> 
								</p>
								<p>
									<% 
										for (String hashtag : topObj.getHashtags()) 
									{  
									
									%> 
									<a style="color: #87C2ED" href="https://twitter.com/hashtag/<%= hashtag %>" ><%= hashtag %></a>
									<% } %> 
								</p>
							</div>
						</div> 
					</li>
				<% } %> 
				</ol>
			</div>

	</div>
	
	<div id="toptweets" class="blank_box twitterTopWidget_box">
			
		<div class="twitterTopTitle_box">	
			<span>Recent Tweets</span>
		</div>
				
		<div class="twitterTopRT_box">
			<ol>
					<%						
						for (TwitterTopTweetsPojo topObj : getTopRecentTweets(request.getParameter("searchID"))) 
						{ 
							
					%>			
					
					<li>
						<div class="tweetData">

							<img class="tweetprofileimg" src="<%= topObj.getProfileImgSrcFromDB() %>" />

							<div class="tweettext">					
									<div>
										<a href="https://twitter.com/<%= topObj.getUsernameFromDb() %>" ><%= topObj.getUsernameFromDb() %></a>
										<span style="float:right;"><%= topObj.getCreateDateFromDb() %></span>
										<span class="retweetClass"><%= topObj.getCounterRTs() %></span>	
										<img style="float:right;" src="img/retweet.png" />																					
									</div>
								<p>
									<%= topObj.getTweetText() %> 
								</p>
								<p>
									<% 
										for (String hashtag : topObj.getHashtags()) 
									{  
									
									%> 
									<a style="color: #87C2ED" href="https://twitter.com/hashtag/<%= hashtag %>" ><%= hashtag %></a>
									<% } %> 
								</p>
							</div>
						</div> 
					</li>
				<% } %> 
				</ol>
			</div>

	</div>
			
</div>

        		
		
		
		<script type="text/javascript">
		
			$(function() 
			{
				
				<% 
					List<TwitterTimelinePojo>  monthlyTimelineChartObjs = getTimelineObjsMonthRounded(request.getParameter("searchID"));
					List<TwitterTimelinePojo> weeklyTimelineChartObjs = getTimelineObjsWeekRounded(request.getParameter("searchID"));
					List<TwitterTimelinePojo> dailyTimelineChartObjs = getTimelineObjsDayRounded(request.getParameter("searchID"));
					List<TwitterTimelinePojo> hourlyTimelineChartObjs = getTimelineObjsHourRounded(request.getParameter("searchID"));

				%>	
				
				
				var tWeeklyData = 
					[
					 	
						<%   for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) { %>	
							[
								<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
							],	               						
						<% } %>
					 
	                ]; 
				
				var ticks = [];
			
				
				for (var i = 0; i < tWeeklyData.length; i++) 
				{
				 	ticks.push(tWeeklyData[i][0]);
				}
			
				
				var monthlyData = 
					[ 
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%   for(TwitterTimelinePojo timelineObj : monthlyTimelineChartObjs) { %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
		               			],	               						
		               			<% } %>
		                     ], 
		            	     label: "# of tweets" 
		                },
						{ 
		                	data: 
		                	[
		               			<% 	for(TwitterTimelinePojo timelineObj : monthlyTimelineChartObjs) {  %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
		               			],
		               			<% } %>
		                    ], 
		                    label: "# of RTs" }
		            ];
					
					var weeklyData = 
					[ 
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%   for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) { %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
		               			],	               						
		               			<% } %>
		                     ], 
		            	     label: "# of tweets" 
		                },
						{ 
		                	data: 
		                	[
		               			<% 	for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) {  %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
		               			],
		               			<% } %>
		                    ], 
		                    label: "# of RTs" }
		            ];
					
					var dailyData = 
					[ 
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%   for(TwitterTimelinePojo timelineObj : dailyTimelineChartObjs) { %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
		               			],	               						
		               			<% } %>
		                     ], 
		            	     label: "# of tweets" 
		                },
						{ 
		                	data: 
		                	[
		               			<% 	for(TwitterTimelinePojo timelineObj : dailyTimelineChartObjs) {  %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
		               			],
		               			<% } %>
		                    ], 
		                    label: "# of RTs" }
		            ];
					
					var hourlyData = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : hourlyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ], 
			            	     label: "# of tweets" 
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : hourlyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ], 
			                    label: "# of RTs" }
			            ];
					
					
					var monthlyDataOverview = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : monthlyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ]
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : monthlyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ] }
			            ];
						
						var weeklyDataOverview = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ]
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ] }
			            ];
						
						var dailyDataOverview = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : dailyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ] 
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : dailyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ] }
			            ];
						
						var hourlyDataOverview = 
							[ 
				                { 
				           	    	data: 
				           	    	[	               					   
				               			<%   for(TwitterTimelinePojo timelineObj : hourlyTimelineChartObjs) { %>	
				               			[
				               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
				               			],	               						
				               			<% } %>
				                     ]
				                },
								{ 
				                	data: 
				                	[
				               			<% 	for(TwitterTimelinePojo timelineObj : hourlyTimelineChartObjs) {  %>	
				               			[
				               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
				               			],
				               			<% } %>
				                    ] }
				            ];
					
					// helper for returning the weekends in a period

					function weekendAreas(axes) {

						var markings = [],
							d = new Date(axes.xaxis.min);

						// go to the first Saturday

						d.setUTCDate(d.getUTCDate() - ((d.getUTCDay() + 1) % 7))
						d.setUTCSeconds(0);
						d.setUTCMinutes(0);
						d.setUTCHours(0);

						var i = d.getTime();

						// when we don't set yaxis, the rectangle automatically
						// extends to infinity upwards and downwards

						do {
							markings.push({ xaxis: { from: i, to: i + 2 * 24 * 60 * 60 * 1000 } });
							i += 7 * 24 * 60 * 60 * 1000;
						} while (i < axes.xaxis.max);

						return markings;
					}
					
					var monthlyOptions = 
					{
						xaxis: 
						{
							mode: "time",
							minTickSize: [1, "month"],
							timeformat: "%b %Y"
						},
						yaxis: 
						{
							tickDecimals: 0
						},
						series: 
						{
							lines:
							{
								show: true,
								fill: true
							}
						},
						grid: {
							hoverable: true,
							clickable: true
						},
						legend:
						{
							container: $("#main-graph"),
							noColumns:2,
							margin: '5px',
						},
						colors: ["#ff0000", "#0000ff"]
					};
					
					var weeklyOptions = 
					{
						xaxis: 
						{
							mode: "time",
							ticks: ticks,
							tickFormatter: function (val, axis) 
							{
								var month = new Array(12);
								month[0] = "Jan";
								month[1] = "Feb";
								month[2] = "Mar";
								month[3] = "Apr";
								month[4] = "May";
								month[5] = "Jun";
								month[6] = "Jul";
								month[7] = "Aug";
								month[8] = "Sep";
								month[9] = "Oct";
								month[10] = "Nov";
								month[11] = "Dec";
								
							    var firstDayWeek = new Date(val);
							    firstDayWeek.setUTCSeconds(0);
							    firstDayWeek.setUTCMinutes(0);
							    firstDayWeek.setUTCHours(0);
							    
							    var lastDayWeek = new Date(firstDayWeek.getTime() + 6 * 24 * 60 * 60 * 1000);
							    
							    return ('0' + firstDayWeek.getUTCDate()).slice(-2) + " - " + ('0' + lastDayWeek.getUTCDate()).slice(-2) + " " + month[firstDayWeek.getUTCMonth()];
							},
						},
						yaxis: 
						{
							tickDecimals: 0
						},
						series: 
						{
							lines:
							{
								show: true,
								fill: true
							}
						},
						grid: 
						{
							hoverable: true,
							clickable: true
						},
						legend:
						{
							container: $("#main-graph"),
							noColumns:2,
							margin: '5px',
						},
						colors: ["#ff0000", "#0000ff"]
					};
					
					var dailyOptions = 
					{
						xaxis: 
						{
							mode: "time",
							minTickSize: [1, "day"],
							timeformat: "%d %b"
						},
						yaxis: 
						{
							tickDecimals: 0
						},
						series: 
						{
							lines:
							{
								show: true,
								fill: true
							}
						},
						grid: 
						{
							markings: weekendAreas,
							hoverable: true,
							clickable: true
						},
						legend:
						{
							container: $("#main-graph"),
							noColumns:2,
							margin: '5px',
						},
						colors: ["#ff0000", "#0000ff"]
					};
					
					var hourlyOptions = 
					{
						xaxis: 
						{
							mode: "time",
							minTickSize: [1, "hour"],
							timeformat: "%d %b  %H:%M"
						},
						yaxis: 
						{
							tickDecimals: 0
						},
						series: 
						{
							lines:
							{
								show: true,
								fill: true
							}
						},
						grid: 
						{
							markings: weekendAreas,
							hoverable: true,
							clickable: true
						},
						legend:
						{
							container: $("#main-graph"),
							noColumns:2,
							margin: '5px',
						},
						colors: ["#ff0000", "#0000ff"]
					};
					
					overviewOptionsMonth =
						{
							series: {
								lines: {
									show: true,
									lineWidth: 1
								},							
								shadowSize: 0
							},
							xaxis: {
								mode: "time",
								minTickSize: [1, "month"],
								timeformat: "%b %Y"
							},
							yaxis: {
								ticks: [],
								min: 0,
								autoscaleMargin: 0.1
							},
							selection: {
								mode: "x"
							},
							colors: ["#ff0000", "#0000ff"]
						};
					
					overviewOptionsWeek =
					{
						series: {
							lines: {
								show: true,
								lineWidth: 1
							},							
							shadowSize: 0
						},
						xaxis: {
							mode: "time",
							ticks: ticks,
							tickFormatter: function (val, axis) 
							{
								var month = new Array(12);
								month[0] = "Jan";
								month[1] = "Feb";
								month[2] = "Mar";
								month[3] = "Apr";
								month[4] = "May";
								month[5] = "Jun";
								month[6] = "Jul";
								month[7] = "Aug";
								month[8] = "Sep";
								month[9] = "Oct";
								month[10] = "Nov";
								month[11] = "Dec";
								
							    var firstDayWeek = new Date(val);
							    firstDayWeek.setUTCSeconds(0);
							    firstDayWeek.setUTCMinutes(0);
							    firstDayWeek.setUTCHours(0);
							    
							    var lastDayWeek = new Date(firstDayWeek.getTime() + 6 * 24 * 60 * 60 * 1000);
							    
							    return ('0' + firstDayWeek.getUTCDate()).slice(-2) + " - " + ('0' + lastDayWeek.getUTCDate()).slice(-2) + " " + month[firstDayWeek.getUTCMonth()];
							},							
						},
						yaxis: {
							ticks: [],
							min: 0,
							autoscaleMargin: 0.1
						},
						selection: {
							mode: "x"
						},
						colors: ["#ff0000", "#0000ff"]
					};
				
					overviewOptionsDay =
					{
						series: {
							lines: {
								show: true,
								lineWidth: 1
							},							
							shadowSize: 0
						},
						xaxis: {
							mode: "time",
							minTickSize: [1, "day"],
							timeformat: "%d %b"
						},
						yaxis: {
							ticks: [],
							min: 0,
							autoscaleMargin: 0.1
						},
						selection: {
							mode: "x"
						},
						colors: ["#ff0000", "#0000ff"]
					};
					
					overviewOptionsHour =
					{
						series: {
							lines: {
								show: true,
								lineWidth: 1
							},							
							shadowSize: 0
						},
						xaxis: {
							mode: "time",
							minTickSize: [1, "hour"],
							timeformat: "%d %b",
						},
						yaxis: {
							ticks: [],
							min: 0,
							autoscaleMargin: 0.1
						},
						selection: {
							mode: "x"
						},
						colors: ["#ff0000", "#0000ff"]
					};
					
				
					var monthlyDataOverview = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : monthlyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ]
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : monthlyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ] }
			            ];
						
						var weeklyDataOverview = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ]
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : weeklyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ] }
			            ];
						
						var dailyDataOverview = 
						[ 
			                { 
			           	    	data: 
			           	    	[	               					   
			               			<%   for(TwitterTimelinePojo timelineObj : dailyTimelineChartObjs) { %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
			               			],	               						
			               			<% } %>
			                     ] 
			                },
							{ 
			                	data: 
			                	[
			               			<% 	for(TwitterTimelinePojo timelineObj : dailyTimelineChartObjs) {  %>	
			               			[
			               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
			               			],
			               			<% } %>
			                    ] }
			            ];
						
						var hourlyDataOverview = 
							[ 
				                { 
				           	    	data: 
				           	    	[	               					   
				               			<%   for(TwitterTimelinePojo timelineObj : hourlyTimelineChartObjs) { %>	
				               			[
				               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
				               			],	               						
				               			<% } %>
				                     ]
				                },
								{ 
				                	data: 
				                	[
				               			<% 	for(TwitterTimelinePojo timelineObj : hourlyTimelineChartObjs) {  %>	
				               			[
				               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
				               			],
				               			<% } %>
				                    ] }
				            ];
				
				
				

// 				var plot = $.plot("#placeholder", monthlydata, monthlyOptions);

				var plot = $.plot("#placeholder", weeklyData, weeklyOptions);
				
				var overview = $.plot("#overview", weeklyDataOverview, overviewOptionsWeek);
				
				$("#placeholder").bind("plotselected", function (event, ranges) {

					// do the zooming
					$.each(plot.getXAxes(), function(_, axis) {
						var opts = axis.options;
						opts.min = ranges.xaxis.from;
						opts.max = ranges.xaxis.to;
					});
					plot.setupGrid();
					plot.draw();
					plot.clearSelection();

					// don't fire event on the overview to prevent eternal loop

					overview.setSelection(ranges, true);
				});

				$("#overview").bind("plotselected", function (event, ranges) {
					plot.setSelection(ranges);
					
				});
								
				$("#placeholder").bind("plothover", function (event, pos, item) 
				{
					if (item) 
					{
						var someData = weeklyData;
						var content = item.series.label + " = " + item.datapoint[1];
						            
						for (var i = 0; i < someData.length; i++)
			            {
			                if (someData[i].label == item.series.label)
			                {					                	
			                    continue;   
			                }
			                
			                for (var j=0; j < someData[i].data.length; j++)
			                {
			                    if (someData[i].data[j][0] == item.datapoint[0] && someData[i].data[j][1] == item.datapoint[1])
			                  	{
			                          content += '<br/>' + someData[i].label + " = " + item.datapoint[1]; 
			                    }
			                }                
			            }					            
			            
			            showTooltip(item.pageX, item.pageY, content);
			        }
			        else 
			        {
			            $("#tooltip").css('display','none');       
			        }
				});	
				
				
				$("#hours").click(function () 
						{
							plot = $.plot("#placeholder", hourlyData, hourlyOptions);
							
							 overview = $.plot("#overview", hourlyDataOverview, overviewOptionsHour);
							
							$("#placeholder").bind("plothover", function (event, pos, item) 
							{
							 	if (item) 
							 	{
							 		var someData = hourlyData;
						            var content = item.series.label + " = " + item.datapoint[1];
						            
						            for (var i = 0; i < someData.length; i++)
						            {
						                if (someData[i].label == item.series.label)
						                {					                	
						                    continue;   
						                }
						                
						                for (var j=0; j < someData[i].data.length; j++)
						                {
						                    if (someData[i].data[j][0] == item.datapoint[0] && someData[i].data[j][1] == item.datapoint[1])
						                  	{
						                          content += '<br/>' + someData[i].label + " = " + item.datapoint[1]; 
						                    }
						                }                
						            }					            
						            
						            showTooltip(item.pageX, item.pageY, content);
						        }
						        else 
						        {
						            $("#tooltip").css('display','none');       
						        }
							});					
						});
					
					
				$("#days").click(function () 
				{					
					
					plot = $.plot("#placeholder", dailyData, dailyOptions);
					
					overview = $.plot("#overview", dailyDataOverview, overviewOptionsDay);
					
					 $("#placeholder").bind("plothover", function (event, pos, item) 
								{
								 	if (item) 
								 	{
								 		var someData = dailyData;
							            var content = item.series.label + " = " + item.datapoint[1];
							            
							            for (var i = 0; i < someData.length; i++)
							            {
							                if (someData[i].label == item.series.label)
							                {					                	
							                    continue;   
							                }
							                
							                for (var j=0; j < someData[i].data.length; j++)
							                {
							                    if (someData[i].data[j][0] == item.datapoint[0] && someData[i].data[j][1] == item.datapoint[1])
							                  	{
							                          content += '<br/>' + someData[i].label + " = " + item.datapoint[1]; 
							                    }
							                }                
							            }					            
							            
							            showTooltip(item.pageX, item.pageY, content);
							        }
							        else 
							        {
							            $("#tooltip").css('display','none');       
							        }
						});	
							
					});	
					
					$("#weeks").click(function () 
					{
						
						plot = $.plot("#placeholder", weeklyData, weeklyOptions);
						
						overview = $.plot("#overview", weeklyDataOverview, overviewOptionsWeek);
						
						 $("#placeholder").bind("plothover", function (event, pos, item) 
									{
									 	if (item) 
									 	{
									 		var someData = weeklyData;
								            var content = item.series.label + " = " + item.datapoint[1];
								            
								            for (var i = 0; i < someData.length; i++)
								            {
								                if (someData[i].label == item.series.label)
								                {					                	
								                    continue;   
								                }
								                
								                for (var j=0; j < someData[i].data.length; j++)
								                {
								                    if (someData[i].data[j][0] == item.datapoint[0] && someData[i].data[j][1] == item.datapoint[1])
								                  	{
								                          content += '<br/>' + someData[i].label + " = " + item.datapoint[1]; 
								                    }
								                }                
								            }					            
								            
								            showTooltip(item.pageX, item.pageY, content);
								        }
								        else 
								        {
								            $("#tooltip").css('display','none');       
								        }
							});	
								
						});	
					
					$("#months").click(function () 
					{
						plot = $.plot("#placeholder", monthlyData, monthlyOptions);
						
						overview = $.plot("#overview", monthlyDataOverview, overviewOptionsMonth);
						
						$("#placeholder").bind("plothover", function (event, pos, item) 
						{
						 	if (item) 
						 	{
						 		var someData = monthlyData;
					            var content = item.series.label + " = " + item.datapoint[1];
					            
					            for (var i = 0; i < someData.length; i++)
					            {
					                if (someData[i].label == item.series.label)
					                {					                	
					                    continue;   
					                }
					                
					                for (var j=0; j < someData[i].data.length; j++)
					                {
					                    if (someData[i].data[j][0] == item.datapoint[0] && someData[i].data[j][1] == item.datapoint[1])
					                  	{
					                          content += '<br/>' + someData[i].label + " = " + item.datapoint[1]; 
					                    }
					                }                
					            }					            
					            
					            showTooltip(item.pageX, item.pageY, content);
					        }
					        else 
					        {
					            $("#tooltip").css('display','none');       
					        }
						});					
					});
					
				
					$("<div id='tooltip'></div>").css({
						position: "absolute",
						display: "none",
						border: "1px solid #fdd",
						padding: "2px",
						"background-color": "#fee",
						opacity: 0.80
					}).appendTo("body");
					
					function showTooltip(x, y, contents) 
					{
				        $('#tooltip').html(contents);
				        $('#tooltip').css({
				            top: y + 5,
				            left: x + 5,
				            display: 'block'});
				    }				 
			});
		
			</script>
			
		

		 <script>
			 <% 
			 
			 	TwitterPiePojo pieChartObj = getTweetsPieChart(request.getParameter("searchID")); 
			 	List<TwitterPieSourcePojo> sources = getTweetsPieChartDevice(request.getParameter("searchID"));
			 
			 %>
		 
			var pie = new d3pie("pieChart", {
				"header": {
					"title": {
						"fontSize": 24,
						"font": "open sans"
					},
					"subtitle": {
						"color": "#999999",
						"fontSize": 12,
						"font": "open sans"
					},
					"titleSubtitlePadding": 9
				},
				"footer": {
					"color": "#999999",
					"fontSize": 10,
					"font": "open sans",
					"location": "bottom-left"
				},
				"size": {
					"canvasWidth": 350,
					"canvasHeight": 300
				},
				"data": {
					"sortOrder": "value-desc",
					"content": [
					{
						"label": 'Tweets',
						"value": <%= pieChartObj.getTweets() %>,
					},
					{
						"label": "RTs",
						"value": <%= pieChartObj.getRTs() %>,
					},
					{
						"label": "Replies",
						"value": <%= pieChartObj.getReplies() %>,
					}
					]
				},
				"labels": {
					"outer": {
						"pieDistance": 1
					},
					"mainLabel": {
						"fontSize": 12
					},
					"percentage": {
						"color": "#ffffff",
						"decimalPlaces": 0
					},
					"value": {
						"color": "#adadad",
						"fontSize": 11
					},
					"lines": {
						"enabled": false
					}
				},
				"effects": {
					"pullOutSegmentOnClick": {
						"effect": "linear",
						"speed": 400,
						"size": 8
					}
				},
				"misc": {
					"gradient": {
						"enabled": true,
						"percentage": 100
					}
				}
			});
			
			
			var pieDevice = new d3pie("pieChartDevice", {
				"header": {
					"title": {
						"fontSize": 24,
						"font": "open sans"
					},
					"subtitle": {
						"color": "#999999",
						"fontSize": 12,
						"font": "open sans"
					},
					"titleSubtitlePadding": 9
				},
				"footer": {
					"color": "#999999",
					"fontSize": 10,
					"font": "open sans",
					"location": "bottom-left"
				},
				"size": {
					"canvasWidth": 350,
					"canvasHeight": 300
				},
				"data": {
					"sortOrder": "value-desc",
					"content": [
					            <% 
					            	for(TwitterPieSourcePojo sourceObj : sources)
					            	{
					            							            	
					            %>
							{
								"label": '<%= sourceObj.getSource()%> ',
								"value": <%= sourceObj.getValue() %>,
		
							},
							
							<%
					            	}
							%>

					]
				},
				"labels": {
					"outer": {
						"pieDistance": 8
					},
					"inner": {
						"hideWhenLessThanPercentage": 4
					},
					"mainLabel": {
						"fontSize": 12
					},
					"percentage": {
						"color": "#ffffff",
						"decimalPlaces": 0
					},
					"value": {
						"color": "#adadad",
						"fontSize": 11
					},
					"lines": {
						"enabled": true
					}
				},
				"effects": {
					"pullOutSegmentOnClick": {
						"effect": "linear",
						"speed": 400,
						"size": 8
					}
				},
				"misc": {
					"gradient": {
						"enabled": true,
						"percentage": 100
					}
				}
			});
		</script>
		
</body>
</html>