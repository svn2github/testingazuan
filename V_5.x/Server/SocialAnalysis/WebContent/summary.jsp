

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

public List<TwitterTopTweetsPojo> getTopTweets(String keyword)
{
	return new TwitterTopTweetsDataProcessor().getTopTweetsData(keyword);
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
	    <li id="activelink"><a href=<% out.println(summaryLink); %>> Summary</a></li>
	    <li><a href=<% out.println(topicsLink); %>>Topics</a></li>
	    <li><a href=<% out.println(networkLink); %>>Network</a></li>
	    <li><a href=<% out.println(distributionLink); %>>Distribution</a></li>
	</ul>
        		
	<div class="generalinfo">

		<div style="float:left; width: 100%;">
	
			<div class="inforules" style="float:left;">
				<span class="num" ><%= getTotalTweets(request.getParameter("searchID")) %></span>
				<br>
				<span style="font-size: 24px; ">tweets</span>
			</div>
		
			<div class="inforules" style="float:left;">
				<span class="num" ><%= getTotalUsers(request.getParameter("searchID")) %></span>
				<br>
				<span style="font-size: 24px; ">users</span>
			</div>

		</div>
		
	</div>
		
	<div id="timeline" class="top-twitter box" style="display:block; float:left; width: 90%; overflow:hidden; padding: 4px;">
		
		<span class="title" >Timeline</span>
		
		
		<div class="demo-container" style="width: 100%; height: 385px;">
			<div id="timelinebuttons" style="float:right;">
				<a id="monthly" style="cursor:pointer;">Monthly</a>
				<a id="weekly" style="cursor:pointer;">Weekly</a>
				<a id="daily" style="cursor:pointer;">Daily</a>
			</div>
			<div id="main-graph" style="vertical-align: middle !important;"></div>
			<div id="placeholder" class="demo-placeholder"></div>
		</div>

		<!-- <div class="demo-container" style="width: 550px; height: 125px;">
			<div id="overview" class="demo-placeholder-o"></div>
		</div>		
		 -->
	</div>
		
	<div id="piebox" class="top-twitter box" style="display:block; width:555px; height:500px; float:left; ">
			
		<span class="title">Tweets Summary</span>				
		<br/>
		
		<div id="pieChart"></div>
		
	</div>
		
	<div id="toptweets" class="top-twitter box" style="display:block; float: left; width:475px; height:600px; padding-left:90px;">
				
		<span class="title">Top Tweets</span>
				
		<div class="toptweets">
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
			
</div>

        		
		
		
		<script type="text/javascript">
		
			$(function() 
			{
				
				<% 
					List<TwitterTimelinePojo> monthlyTimelineChartObjs = getMonthlyTimelineChartObjs(request.getParameter("searchID"));
					List<TwitterTimelinePojo> weeklyTimelineChartObjs = getWeeklyTimelineChartObjs(request.getParameter("searchID"));
					List<TwitterTimelinePojo> dailyTimelineChartObjs = getDailyTimelineChartObjs(request.getParameter("searchID"));
				%>	
				
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
				
				
				

				var plot = $.plot("#placeholder", monthlydata, monthlyOptions);
								
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
					
					$.plot("#placeholder", dailydata, dailyOptions);
					
					 $("#placeholder").bind("plothover", function (event, pos, item) 
								{
								 	if (item) 
								 	{
								 		var someData = dailydata;
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
					
					$("#weekly").click(function () 
					{
						
						var weekplot = $.plot("#placeholder", weeklydata, weeklyOptions);
						
						 $("#placeholder").bind("plothover", function (event, pos, item) 
									{
									 	if (item) 
									 	{
									 		var someData = weeklydata;
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
					
					$("#monthly").click(function () 
					{
						$.plot("#placeholder", monthlydata, monthlyOptions);
						
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
					"canvasWidth": 590
				},
				"data": {
					"sortOrder": "value-desc",
					"content": [
					{
						"label": "Original Tweets",
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
						"pieDistance": 11
					},
					"mainLabel": {
						"fontSize": 11
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
						"enabled": true,
						"style": "straight",
						"color": "#ffffff"
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
				},
				"callbacks": 
				{					
					//  onMouseoverSegment: function(d) 
					//{
						//Show the tooltip
					//	d3.select("#pie-tooltip")
					//		.classed("hidden", false)
					//		.style("left", d3.getBBox().pageX + "px")
    				//		.style("top", d3.getBBox().pageY + "px")
					//	  	.select("#value")
					//	  	.text(d.data.value);						
				//	},
				//	onMouseoutSegment: function(info) 
				//	{
				//		d3.select("#pie-tooltip").classed("hidden", true);
				//	} -->
				}
			});
		</script>
		
</body>
</html>