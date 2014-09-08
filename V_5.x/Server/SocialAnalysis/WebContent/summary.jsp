

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

public TwitterPiePojo getTweetsPieChart(String searchID)
{
	return new TwitterPieDataProcessor().getTweetsPieChart(searchID);
}

public List<TwitterTimelinePojo> getDailyTimelineChartObjs(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsDaily(searchID);
}

public List<TwitterTimelinePojo> getWeeklyTimelineChartObjs(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsWeekly(searchID); 
}

public List<TwitterTimelinePojo> getMonthlyTimelineChartObjs(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsMonthly(searchID); 
}

public String getMinSearchDate(String searchID)
{
	return new TwitterTimelineDataProcessor().getMinDateSearch(searchID);
}

public String getMaxSearchDate(String searchID)
{
	return new TwitterTimelineDataProcessor().getMaxDateSearch(searchID);
}

public List<TwitterTimelinePojo> getRangeTimelineChartObjs(String searchID)
{
	return new TwitterTimelineDataProcessor().getTimelineObjsRangeTime(searchID); 
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
							<li><a id="monthly" style="cursor:pointer;">Months</a>							
				          	<li><a id="weekly" style="cursor:pointer;">Days</a></li>
				          	<li><a id="daily" style="cursor:pointer;">Hours</a></li></li>
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
		
	<div id="piebox" class="blank_box pieChart_box"  ">
		
		<div id="pieChart"></div>
		
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
					List<TwitterTimelinePojo> monthlyTimelineChartObjs = getMonthlyTimelineChartObjs(request.getParameter("searchID"));
					List<TwitterTimelinePojo> weeklyTimelineChartObjs = getWeeklyTimelineChartObjs(request.getParameter("searchID"));
					List<TwitterTimelinePojo> dailyTimelineChartObjs = getDailyTimelineChartObjs(request.getParameter("searchID"));
					List<TwitterTimelinePojo> rangeTimelineChartObjs = getRangeTimelineChartObjs(request.getParameter("searchID"));

				%>	
				
				
				var rangedata = 
					[ 
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%   for(TwitterTimelinePojo timelineObj : rangeTimelineChartObjs) { %>	
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
		               			<% 	for(TwitterTimelinePojo timelineObj : rangeTimelineChartObjs) {  %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
		               			],
		               			<% } %>
		                    ], 
		                    label: "# of RTs" }
		            ];
				
				var rangedataOverview = 
					[ 
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%   for(TwitterTimelinePojo timelineObj : rangeTimelineChartObjs) { %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnTweets() %>
		               			],	               						
		               			<% } %>
		                     ]
		                },
						{ 
		                	data: 
		                	[
		               			<% 	for(TwitterTimelinePojo timelineObj : rangeTimelineChartObjs) {  %>	
		               			[
		               				<%= timelineObj.getPostTimeMills() + (60 * 60 * 2000) %>, <%= timelineObj.getnRTs() %>
		               			],
		               			<% } %>
		                    ]
		                }
		            ];
				
				var monthlydata = 
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
				
				var weeklydata = 
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
				
				var dailydata = 
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
				
				var rangeOptions = 
				{
					xaxis: 
					{
						mode: "time",
						minTickSize: [1, "day"],
						min: <%= rangeTimelineChartObjs.get(0).getLowerBound() + (60 * 60 * 2000)  %>,
						max: <%= rangeTimelineChartObjs.get(0).getUpperBound() + (60 * 60 * 2000)  %>
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
				
				var monthlyOptions = 
				{
					xaxis: 
					{
						mode: "time",
						minTickSize: [1, "day"],
						min: <%= monthlyTimelineChartObjs.get(0).getLowerBound() + (60 * 60 * 2000)  %>,
						max: <%= monthlyTimelineChartObjs.get(0).getUpperBound() + (60 * 60 * 2000)  %>
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
				
				var weeklyOptions = 
				{
					xaxis: 
					{
						mode: "time",
						minTickSize: [1, "day"],
						min: <%= weeklyTimelineChartObjs.get(0).getLowerBound() + (60 * 60 * 2000)  %>,
						max: <%= weeklyTimelineChartObjs.get(0).getUpperBound() + (60 * 60 * 2000)  %>
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
				
				var dailyOptions = 
				{
					xaxis: 
					{
						mode: "time",
						minTickSize: [1, "hour"],
						min: <%= dailyTimelineChartObjs.get(0).getLowerBound() + (60 * 60 * 2000)  %>,
						max: <%= dailyTimelineChartObjs.get(0).getUpperBound() + (60 * 60 * 2000)  %>
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
				
				
				

// 				var plot = $.plot("#placeholder", monthlydata, monthlyOptions);

				var plot = $.plot("#placeholder", rangedata, rangeOptions);
				
				var overview = $.plot("#overview", rangedataOverview, {
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
				});
				
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
						var someData = monthlydata;
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
					
					
				$("#daily").click(function () 
				{					
					plot.getAxes().xaxis.options.minTickSize = [1, "hour"];
					plot.setupGrid();
					plot.draw();					 
							
				});	
					
					$("#weekly").click(function () 
					{						
						plot.getAxes().xaxis.options.minTickSize = [1, "day"];
						plot.setupGrid();
						plot.draw();								
					});	
					
					$("#monthly").click(function () 
					{
						plot.getAxes().xaxis.options.minTickSize = [1, "month"];
						plot.setupGrid();
						plot.draw();		
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
			
			

		<!-- 
		<div id="pie-tooltip" class="hidden">
        <p><strong>Important Label Heading</strong></p>
        <p><span id="value">100</span>%</p>
		</div> -->

		 <script>
			 <% TwitterPiePojo pieChartObj = getTweetsPieChart(request.getParameter("searchID")); %>
		 
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
					"canvasWidth": 300,
					"canvasHeight": 300
				},
				"data": {
					"sortOrder": "value-desc",
					"content": [
					{
						"label": 'Tweets',
						"value": <%= pieChartObj.getTweets() %>,
						"color": "#29c03c"
					},
					{
						"label": "RTs",
						"value": <%= pieChartObj.getRTs() %>,
						"color": "#ff1000"
					},
					{
						"label": "Replies",
						"value": <%= pieChartObj.getReplies() %>,
						"color": "#633be2"
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
		</script>
		
</body>
</html>