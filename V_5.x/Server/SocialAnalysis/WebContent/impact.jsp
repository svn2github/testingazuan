

<%@page import="com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter"%>
<%@page import="twitter4j.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.util.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.dataprocessors.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.pojos.*" %>
<%@ page import="it.eng.spagobi.bitly.analysis.utilities.*" %>
<%@ page import="it.eng.spagobi.twitter.analysis.utilities.*" %>
<%@ page import="twitter4j.JSONArray" %>
<%@ page import="twitter4j.JSONObject" %>


    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/twitter.css" />
	<link rel="stylesheet" type="text/css" href="css/socialAnalysis.css" >
	<link rel="stylesheet" type="text/css" href="css/timeline.css" >
	<script src="js/lib/others/jquery-2.1.1.min.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.time.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.selection.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/jquery.flot.navigate.js"></script>

	
	<title>Twitter Analysis</title>
	
</head>
<body>

<%!




public JSONObject getBitlyClicksTimelineObjs(String searchID)
{
	return new TwitterTimelineDataProcessor().getBitlyClicksTimelineObjs(searchID); 
}

/*************** START TIMELINE ACCOUNT *****************************************************/

public JSONObject getFollowersTimelineHour(String searchID)
{
	return new TwitterResourcesTimelineDataProcessor().getFollowers(searchID, "hours"); 
}

public JSONObject getFollowersTimelineDay(String searchID)
{
	return new TwitterResourcesTimelineDataProcessor().getFollowers(searchID, "days"); 
}

public JSONObject getFollowersTimelineWeek(String searchID)
{
	return new TwitterResourcesTimelineDataProcessor().getFollowers(searchID, "weeks"); 
}

public JSONObject getFollowersTimelineMonth(String searchID)
{
	return new TwitterResourcesTimelineDataProcessor().getFollowers(searchID, "months"); 
}

/*************** END TIMELINE ACCOUNT *****************************************************/


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
	    <li><a href=<% out.println(distributionLink); %>>Distribution</a></li>
   	    <li><a href=<% out.println(sentimentLink); %>>Sentiment</a></li>
	    <li id="activelink"><a href=<% out.println(impactLink); %>>Impact</a></li>
	    <li><a href=<% out.println(roiLink); %>>ROI</a></li>
	</ul>		
		
		<div id="timeline" class="timeline_main">	
		
			<div class="demo-container" style="width: 100%; height: 60%;">
				<div id="hormenu-a">
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
				<div id="main-graph-account" style="vertical-align: middle !important;"></div>
				<div id="placeholder-account" class="demo-placeholder"></div>
			</div>
			<div class="demo-container" style="width: 100%; height: 40%">
				<div id="overview-account" class="demo-placeholder-o"></div>
			</div>	
	
			<div id="monitor_link" class="timeline_main">		
				
				<div class="demo-container" style="width: 100%; height: 385px;">
					<div id="main-graph-link" style="vertical-align: middle !important;"></div>
					<div id="placeholder-link" class="demo-placeholder"></div>
				</div>
				<div class="demo-container" style="width: 100%; height: 40%">
					<div id="overview-link" class="demo-placeholder-o"></div>
				</div>	
			</div>
		</div>
			
</div>   

	<script type="text/javascript">
		
			$(function() 
			{
				
				<% 
				JSONObject accountsJSONHour = getFollowersTimelineHour(request.getParameter("searchID"));
				JSONObject accountsJSONDay = getFollowersTimelineDay(request.getParameter("searchID"));
				JSONObject accountsJSONWeek = getFollowersTimelineWeek(request.getParameter("searchID"));
				JSONObject accountsJSONMonth = getFollowersTimelineMonth(request.getParameter("searchID"));
				
				JSONArray hourResults = accountsJSONHour.getJSONArray("results");
				JSONArray dayResults = accountsJSONDay.getJSONArray("results");
				JSONArray weekResults = accountsJSONWeek.getJSONArray("results");
				JSONArray monthResults = accountsJSONMonth.getJSONArray("results");

				%>
				
				
				var hourData = 
					[ 
					 	<%  if(hourResults != null && hourResults.length() > 0)
					 		{
					 			
					 			for(int i = 0; i < hourResults.length(); i++)
					 			{
					 				JSONObject element = hourResults.getJSONObject(i);
					 				String label = element.getString("label");
					 				JSONArray data = element.getJSONArray("data");
					 	%>
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%  if(data != null && data.length() > 0)
		               				{
		               					for(int j = 0; j < data.length(); j++) 
		               					{ 
		               						
		               						JSONObject dataElement = data.getJSONObject(j);
		               			%>	
		               			
		               			[
		               				<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
		               			],	               						
		               			<%
		               					}
		               				}
		               			
		               			%>
		                     ], 
		            	     label: "<%= label %>" 
		                },
		                
		                <%
               					}
               				}
               			
               			%>
		            ];
				
				var hourDataOverview = 
					[ 
						<%  if(hourResults != null && hourResults.length() > 0)
						{
							
							for(int i = 0; i < hourResults.length(); i++)
							{
								JSONObject element = hourResults.getJSONObject(i);
								String label = element.getString("label");
								JSONArray data = element.getJSONArray("data");
						%>
						{ 
							data: 
							[	               					   
								<%  if(data != null && data.length() > 0)
								{
									for(int j = 0; j < data.length(); j++) 
									{ 
										
										JSONObject dataElement = data.getJSONObject(j);
							%>	
							
							[
								<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
							],	               						
							<%
									}
								}
							
							%>
						], 
						},
						
						<%
							}
						}
						
						%>
		            ];
				
				
				var dayData = 
					[ 
					 	<%  if(dayResults != null && dayResults.length() > 0)
					 		{
					 			
					 			for(int i = 0; i < dayResults.length(); i++)
					 			{
					 				JSONObject element = dayResults.getJSONObject(i);
					 				String label = element.getString("label");
					 				JSONArray data = element.getJSONArray("data");
					 	%>
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%  if(data != null && data.length() > 0)
		               				{
		               					for(int j = 0; j < data.length(); j++) 
		               					{ 
		               						
		               						JSONObject dataElement = data.getJSONObject(j);
		               			%>	
		               			
		               			[
		               				<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
		               			],	               						
		               			<%
		               					}
		               				}
		               			
		               			%>
		                     ], 
		            	     label: "<%= label %>" 
		                },
		                
		                <%
               					}
               				}
               			
               			%>
		            ];
				
				var dayDataOverview = 
					[ 
						<%  if(dayResults != null && dayResults.length() > 0)
						{
							
							for(int i = 0; i < dayResults.length(); i++)
							{
								JSONObject element = dayResults.getJSONObject(i);
								String label = element.getString("label");
								JSONArray data = element.getJSONArray("data");
						%>
						{ 
							data: 
							[	               					   
								<%  if(data != null && data.length() > 0)
								{
									for(int j = 0; j < data.length(); j++) 
									{ 
										
										JSONObject dataElement = data.getJSONObject(j);
							%>	
							
							[
								<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
							],	               						
							<%
									}
								}
							
							%>
						], 
						},
						
						<%
							}
						}
						
						%>
		            ];
				
				var weekData = 
					[ 
					 	<%  if(weekResults != null && weekResults.length() > 0)
					 		{
					 			
					 			for(int i = 0; i < weekResults.length(); i++)
					 			{
					 				JSONObject element = weekResults.getJSONObject(i);
					 				String label = element.getString("label");
					 				JSONArray data = element.getJSONArray("data");
					 	%>
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%  if(data != null && data.length() > 0)
		               				{
		               					for(int j = 0; j < data.length(); j++) 
		               					{ 
		               						
		               						JSONObject dataElement = data.getJSONObject(j);
		               			%>	
		               			
		               			[
		               				<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
		               			],	               						
		               			<%
		               					}
		               				}
		               			
		               			%>
		                     ], 
		            	     label: "<%= label %>" 
		                },
		                
		                <%
               					}
               				}
               			
               			%>
		            ];
				
				var weekDataOverview = 
					[ 
						<%  if(weekResults != null && weekResults.length() > 0)
						{
							
							for(int i = 0; i < weekResults.length(); i++)
							{
								JSONObject element = weekResults.getJSONObject(i);
								String label = element.getString("label");
								JSONArray data = element.getJSONArray("data");
						%>
						{ 
							data: 
							[	               					   
								<%  if(data != null && data.length() > 0)
								{
									for(int j = 0; j < data.length(); j++) 
									{ 
										
										JSONObject dataElement = data.getJSONObject(j);
							%>	
							
							[
								<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
							],	               						
							<%
									}
								}
							
							%>
						], 
						},
						
						<%
							}
						}
						
						%>
		            ];
				
				var monthData = 
					[ 
					 	<%  if(monthResults != null && monthResults.length() > 0)
					 		{
					 			
					 			for(int i = 0; i < monthResults.length(); i++)
					 			{
					 				JSONObject element = monthResults.getJSONObject(i);
					 				String label = element.getString("label");
					 				JSONArray data = element.getJSONArray("data");
					 	%>
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%  if(data != null && data.length() > 0)
		               				{
		               					for(int j = 0; j < data.length(); j++) 
		               					{ 
		               						
		               						JSONObject dataElement = data.getJSONObject(j);
		               			%>	
		               			
		               			[
		               				<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
		               			],	               						
		               			<%
		               					}
		               				}
		               			
		               			%>
		                     ], 
		            	     label: "<%= label %>" 
		                },
		                
		                <%
               					}
               				}
               			
               			%>
		            ];
				
				var monthDataOverview = 
					[ 
						<%  if(monthResults != null && monthResults.length() > 0)
						{
							
							for(int i = 0; i < monthResults.length(); i++)
							{
								JSONObject element = monthResults.getJSONObject(i);
								String label = element.getString("label");
								JSONArray data = element.getJSONArray("data");
						%>
						{ 
							data: 
							[	               					   
								<%  if(data != null && data.length() > 0)
								{
									for(int j = 0; j < data.length(); j++) 
									{ 
										
										JSONObject dataElement = data.getJSONObject(j);
							%>	
							
							[
								<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("followers") %>
							],	               						
							<%
									}
								}
							
							%>
						], 
						},
						
						<%
							}
						}
						
						%>
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
				
				var hourOptions = 
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
							lineWidth: 2
						}
					},
					grid: {
						markings: weekendAreas,
						hoverable: true,
						clickable: true
					},
					legend:
					{
						container: $("#main-graph-account"),
						noColumns:1,
						margin: '5px',
					},
				};
				
				
				var dayOptions = 
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
							lineWidth: 2
						}
					},
					grid: {
						markings: weekendAreas,
						hoverable: true,
						clickable: true
					},
					legend:
					{
						container: $("#main-graph-account"),
						noColumns:1,
						margin: '5px',
					},
				};
				
				var weekOptions = 
				{
					xaxis: 
					{
						mode: "time",
						minTickSize: [1, "day"]
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
							lineWidth: 2
						}
					},
					grid: {
						markings: weekendAreas,
						hoverable: true,
						clickable: true
					},
					legend:
					{
						container: $("#main-graph-account"),
						noColumns:1,
						margin: '5px',
					},
				};
				
				
				var monthOptions = 
				{
					xaxis: 
					{
						mode: "time",
						minTickSize: [1, "month"]
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
							lineWidth: 2
						}
					},
					grid: {
						markings: weekendAreas,
						hoverable: true,
						clickable: true
					},
					legend:
					{
						container: $("#main-graph-account"),
						noColumns:1,
						margin: '5px',
					},
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
			};
			
				

			var plot = $.plot("#placeholder-account", weekData, weekOptions);
			
			var overview = $.plot("#overview-account", weekDataOverview, overviewOptionsWeek);
			
			$("#placeholder-account").bind("plotselected", function (event, ranges) {

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

			$("#overview-account").bind("plotselected", function (event, ranges) {
				plot.setSelection(ranges);
				
			});
							
			$("#placeholder-account").bind("plothover", function (event, pos, item) 
			{
				if (item) 
				{
					var someData = weekData;
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
						plot = $.plot("#placeholder-account", hourData, hourOptions);
						
						 overview = $.plot("#overview-account", hourDataOverview, overviewOptionsHour);
						
						$("#placeholder-account").bind("plothover", function (event, pos, item) 
						{
						 	if (item) 
						 	{
						 		var someData = hourData;
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
				
				plot = $.plot("#placeholder-account", dayData, dayOptions);
				
				overview = $.plot("#overview-account", dayDataOverview, overviewOptionsDay);
				
				 $("#placeholder-account").bind("plothover", function (event, pos, item) 
							{
							 	if (item) 
							 	{
							 		var someData = dayData;
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
					
					plot = $.plot("#placeholder-account", weekData, weekOptions);
					
					overview = $.plot("#overview-account", weekDataOverview, overviewOptionsWeek);
					
					 $("#placeholder-account").bind("plothover", function (event, pos, item) 
								{
								 	if (item) 
								 	{
								 		var someData = weekData;
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
					plot = $.plot("#placeholder-account", monthData, monthOptions);
					
					overview = $.plot("#overview-account", monthDataOverview, overviewOptionsMonth);
					
					$("#placeholder-account").bind("plothover", function (event, pos, item) 
					{
					 	if (item) 
					 	{
					 		var someData = monthData;
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
			
	
		<script type="text/javascript">
		
			$(function() 
			{
				
				<% 
					JSONObject linksJSON = getBitlyClicksTimelineObjs(request.getParameter("searchID"));
					JSONArray lResults = linksJSON.getJSONArray("results");

				%>
				
				
				var rangedata = 
					[ 
					 	<%  if(lResults != null && lResults.length() > 0)
					 		{
					 			
					 			for(int i = 0; i < lResults.length(); i++)
					 			{
					 				JSONObject element = lResults.getJSONObject(i);
					 				String label = element.getString("label");
					 				JSONArray data = element.getJSONArray("data");
					 	%>
		                { 
		           	    	data: 
		           	    	[	               					   
		               			<%  if(data != null && data.length() > 0)
		               				{
		               					for(int j = 0; j < data.length(); j++) 
		               					{ 
		               						
		               						JSONObject dataElement = data.getJSONObject(j);
		               			%>	
		               			
		               			[
		               				<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("clicks") %>
		               			],	               						
		               			<%
		               					}
		               				}
		               			
		               			%>
		                     ], 
		            	     label: "<%= label %>" 
		                },
		                
		                <%
               					}
               				}
               			
               			%>
		            ];
				
				var rangedataOverview = 
					[ 
						<%  if(lResults != null && lResults.length() > 0)
						{
							
							for(int i = 0; i < lResults.length(); i++)
							{
								JSONObject element = lResults.getJSONObject(i);
								String label = element.getString("label");
								JSONArray data = element.getJSONArray("data");
						%>
						{ 
							data: 
							[	               					   
								<%  if(data != null && data.length() > 0)
								{
									for(int j = 0; j < data.length(); j++) 
									{ 
										
										JSONObject dataElement = data.getJSONObject(j);
							%>	
							
							[
								<%= dataElement.getLong("mills") + (60 * 60 * 2000) %>, <%= dataElement.getInt("clicks") %>
							],	               						
							<%
									}
								}
							
							%>
						], 
						},
						
						<%
							}
						}
						
						%>
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
						minTickSize: [1, "<%= linksJSON.getString("type") %>"],
						min: <%= linksJSON.getLong("lowerBound") + (60 * 60 * 2000)  %>,
						max: <%= linksJSON.getLong("upperBound") + (60 * 60 * 2000)  %>
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
							lineWidth: 2
						}
					},
					grid: {
						markings: weekendAreas,
						hoverable: true,
						clickable: true
					},
					legend:
					{
						container: $("#main-graph-link"),
						noColumns:1,
						margin: '5px',
					},
// 					colors: ["#ff0000", "#0000ff"]
				};
				

				var plot = $.plot("#placeholder-link", rangedata, rangeOptions);
				
				var overview = $.plot("#overview-link", rangedataOverview, {
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
					}
				});
				
				$("#placeholder-link").bind("plotselected", function (event, ranges) {

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

				$("#overview-link").bind("plotselected", function (event, ranges) {
					plot.setSelection(ranges);
					
				});
								
				$("#placeholder-link").bind("plothover", function (event, pos, item) 
				{
					if (item) 
					{
						var someData = rangedata;
						var content = item.series.label + " #clicks = " + item.datapoint[1];
						            
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
			                          content += '<br/>' + someData[i].label + " #clicks = " + item.datapoint[1]; 
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
</body>
</html>