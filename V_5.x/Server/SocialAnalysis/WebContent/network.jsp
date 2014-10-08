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
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="it.eng.spagobi.twitter.analysis.entities.TwitterUser"%>
<%@page import="twitter4j.JSONArray"%>
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
	List<TwitterUser> mostrInfluencers = influencersDP.getMostInfluencers(searchId);
	
	TwitterMentionsCloudDataProcessor mentionsDP = new TwitterMentionsCloudDataProcessor();
	String mentionsCloud = mentionsDP.mentionsCloudCreate(searchId).toString();
	
	UsersNetworkGraphDataProcessor usersGraphDP = new UsersNetworkGraphDataProcessor();
	usersGraphDP.initializeUsersNetworkGraph(searchId);
	
	JSONObject profiles = usersGraphDP.getProfiles();
	JSONArray links = usersGraphDP.getLinks();
	
	UsersNetworkLinkMapDataProcessor usersMapDP = new UsersNetworkLinkMapDataProcessor();
	usersMapDP.initializeUsersNetworkLinkMap(searchId);
	
	JSONArray connections = usersMapDP.getLinks();
	JSONArray codes = usersMapDP.getContriesCodes();
	
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
	<script type="text/javascript" src="js/lib/others/jqcloud-1.0.4.js"></script>
	<script language="javascript" type="text/javascript" src="js/lib/others/d3.min.js"></script>
	<script src="http://d3js.org/d3.geo.projection.v0.min.js"></script>
	<script src="http://d3js.org/topojson.v1.min.js"></script>
    <style type="text/css">

		circle {
		  stroke-width: 1.5px;
		}
		
		line {
		  stroke: rgba(32,32,32,.3);
		}

    </style>
	
	
	<title>Twitter Analysis</title>
	
</head>
<body>

<div id="navigation">

	<ul class="navtabs tabsStyle">
	    <li class="navtabs"><a href=<%= summaryLink %>> Summary</a></li>
	    <li class="navtabs"><a href=<%= topicsLink %>>Topics</a></li>
	    <li class="navtabs" id="activelink"><a href=<%= networkLink %>>Network</a></li>
	    <li class="navtabs"><a href=<%= distributionLink %>>Distribution</a></li>
   	    <li class="navtabs"><a href=<%= sentimentLink %>>Sentiment</a></li>
	    <li class="navtabs"><a href=<%= impactLink %>>Impact</a></li>
	    <li class="navtabs"><a href=<%= roiLink %>>ROI</a></li>
	    <li class="navtabs" style="float:right;"><a href="index.jsp">Search</a></li>
	    
	</ul>
        		
	<div id="influencers" class="blank_box topInfluencersMain_box" >
			
		<div class="topInfluencersTitle_box">
			
			<span>Top Influencers</span>
		
		</div>
		
		<br/>
		
		<div id="freewall" class="free-wall" style="margin-top: 20px;"></div>
			
	</div> 
	
	<div id="mentionscloud" class="blank_box mentionsCloudMain_box">
			
		<div class="mentionsCloudTitle_box">
			
			<span>Users Mentions</span>
		
		</div>
			
		<br/>
			
		<div id="my_cloud" class="mentionsCloud_box"></div>
		
	</div>
		
	<div id="usersMainGraph" class="blank_box usersGraphMain_box">
		
		<div id="usersGraphTitle" class="usersGraphTitle_box">
			
			<span>Users Graph</span>
		
		</div>
	
		<div id="usersGraph" ></div>
	
	</div>
	
	
	<div id="usersTweetLinkMapMain" class="blank_box usersTweetLinkMapMain_box">
		
		<div id="usersTweetLinkMapTitle" class="usersTweetLinkMapTitle_box">
			
			<span>Users Tweets Map</span>
		
		</div>
	
		<div id="usersTweetLinkMap" ></div>
	
	</div>
	
	<div style="float:left; margin-left: 30px; margin-top: 50px;">	
		<img src="img/screens/influencers_web.png" ></img>
	</div>
			
</div>        	
			
	<script type="text/javascript">
								
				var temp = "<img class='cell' width='{width}px;' height='{height}px;' src='{lImg}' text='{lText}' data-title='{lTitle}'></img>";
				var w = 61, h = 61, html = '', limitItem = 32;
				
				<%				
					for (TwitterUser tempObj : mostrInfluencers) 
					{
						
				%>
						var userInfo = "<%= tempObj.getFollowersCount() %> followers <br/> <%= tempObj.getDescription() %>";
						
				//for (var i = 0; i < limitItem; ++i) {
						html += temp.replace(/\{height\}/g, h).replace(/\{width\}/g, w)
							.replace("{lImg}", "<%= tempObj.getProfileImgSrc() %>" )
							.replace("{lText}", userInfo)
							.replace("{lTitle}", "@<%= tempObj.getUsername() %>");
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
			
			<script type="text/javascript">
					var word_list = <%= mentionsCloud %>
					$(function() {
	 						$("#my_cloud").jQCloud(word_list);
					});
			</script>
			
			
			<script>
			
			var links = <%= links%>
			var profiles = <%= profiles%>
			
			var nodes = {}
			
			// Compute the distinct nodes from the links.
			links.forEach(function(link) {
				  link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
				  link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
				});			

			var width =  $('#usersMainGraph').width();
			var height = $('#usersMainGraph').height()-$('#usersGraphTitle').innerHeight();
			var r = 25;

			var force = d3.layout.force()
			    .nodes(d3.values(nodes))
			    .links(links)
			    .size([width, height])
			    .linkDistance(40)
			    .charge(-800)
				.on("tick", tick)
			    .start();
			
			var svg = d3.select("#usersGraph").append("svg")
		    .attr("width", width)
		    .attr("height", height);
			
			svg.append("svg:rect")
		    .attr("width", width)
		    .attr("height", height)
		    .style("stroke", "#000");
			
			var link = svg.selectAll(".link")
		    .data(force.links())
		  .enter().append("line")
		    .attr("class", "link");

			var node = svg.selectAll(".node")
			    .data(force.nodes())
			  .enter().append("g")
			    .attr("class", "node")
			    .call(force.drag);

			node.append("svg:defs")
				.append("svg:pattern")
				.attr("id", function(d, i) {
					return "image"+i;	
				})
				.attr("x", "25")
				.attr("y", "25")
				.attr("width", "50")
				.attr("height", "50")
				.attr("patternUnits", "userSpaceOnUse")				
				.append("svg:image")
				.attr("xlink:href", function(d, i) {
			        // d is the node data, i is the index of the node
						return profiles[d.name];
			    })
				.attr("x", "0")
				.attr("y", "0")
// 				.attr("id", "fillImage");
				.attr("width", "50")
				.attr("height", "50");			
			
		 var circle = node.append("circle")
		    .attr("r", 25)
			.attr("fill",function(d, i) {
				return "url(#image"+i+")";	
			});
		 
		 function tick() {
			 
			 node.attr("cx", function(d) { return d.x = Math.max(15, Math.min(width - 15, d.x)); })
			    .attr("cy", function(d) { return d.y = Math.max(15, Math.min(height - 15, d.y)); });

			link.attr("x1", function(d) { return d.source.x; })
			    .attr("y1", function(d) { return d.source.y; })
			    .attr("x2", function(d) { return d.target.x; })
			    .attr("y2", function(d) { return d.target.y; });

			node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
			}


	</script>
	
	<script>
	
	var connections = <%= connections %>
	var codes = <%= codes %>
	
	var connectionsMap = [];
	var linksByOrigin = {};
	
	var w =  $('#usersTweetLinkMapMain').width();
	var h = $('#usersTweetLinkMapMain').height()-$('#usersTweetLinkMapTitle').innerHeight();
	var centered;

	
// 	connections.forEach(function(connection) {
//         var origin = connection.source, destination = connection.target, connectionsMap = linksByOrigin[origin] || (linksByOrigin[origin] = []);
        
//         countByAirport[origin] = (countByAirport[origin] || 0) + 1;
//         countByAirport[destination] = (countByAirport[destination] || 0) + 1;
//     });

	var projection = d3.geo.kavrayskiy7(),
	    color = d3.scale.category20(),
	    graticule = d3.geo.graticule();
	
	var path = d3.geo.path()
	    .projection(projection);
	
	var svg = d3.select("#usersTweetLinkMap").append("svg")
	    .attr("width", w)
	    .attr("height", h)
	    
// 	var g = svg.append("g");
	
	
	
// 	svg.append("path")
// 	    .datum(graticule)
// 	    .attr("class", "graticule")
// 	    .attr("d", path);
	
// 	svg.append("path")
// 	    .datum(graticule.outline)
// 	    .attr("class", "graticule outline")
// 	    .attr("d", path);

	d3.json("json/world-110m.json", function(error, world) {
		
		
	  var countries = topojson.feature(world, world.objects.countries).features,
	      neighbors = topojson.neighbors(world.objects.countries.geometries);
	
	  svg.selectAll(".country")
	      .data(countries)
	    .enter().insert("path", ".graticule")
	      .attr("class", "country")
	      .attr("d", path);
// 	      .style("fill", function(d, i) { return color(d.color = d3.max(neighbors[i], function(n) { return countries[n].color; }) + 1 | 0); });
	  
	  d3.csv("json/countries.csv", function(error, data) {// read in and plot the circles
		  
		  var filterData = [];
	  	  var pointData = [];
	  
		  for(var i = 0; i < codes.length; i++)
		  {
			  for(var j = 0; j < data.length; j++)
			  {
	// 			  console.log("Codes[i]: " + codes[i]);
	// 			  console.log("Data[i].code: " + data[i].code)
			  		if(codes[i] == data[j].code)
		  			{
		  				filterData[data[j].code] = data[j];
		  				pointData.push(data[j]);
		  				break;
		  			}
			  }
						  
		  }
// 		  console.log(data);
// 	  	console.log(connections);
// 	  	console.log(codes);
		console.log(filterData);
	  	
	  	
	  	
	  	
		  
	        svg.selectAll("circle").data(pointData).enter().append("circle").attr("class", "circle").attr("cx", function(d) {
	            return projection([d.lon, d.lat])[0];
	        }).attr("cy", function(d) {
	            return projection([d.lon, d.lat])[1];
	        }).attr("r", 5)
	        .style("fill", "red");
	  
	  
	        var lineTransition = function lineTransition(path) {
	            path.transition()

	                .duration(5500)
	                .attrTween("stroke-dasharray", tweenDash)
	                .each("end", function(d,i) { 

	                });
	        };
	        var tweenDash = function tweenDash() {

	            var len = this.getTotalLength(),
	                interpolate = d3.interpolateString("0," + len, len + "," + len);

	            return function(t) { return interpolate(t); };
	        };

// 	        var links = [
// 	            {
// 	                type: "LineString",
// 	                    coordinates: [
// 	                        [ data[0].lon, data[0].lat ],
// 	                        [ data[1].lon, data[1].lat ]
// 	                    ]
// 	            }
// 	        ];


	        var connectionsData = [];
	        
	        for(var i = 0; i < connections.length; i++)
        	{
        		var originCode = connections[i][0];
        		var destinationCode = connections[i][1];
        				
        		connectionsData.push({
        	                type: "LineString",
        	                coordinates: [
        	                    [ filterData[originCode].lon, filterData[originCode].lat ],
        	                    [ filterData[destinationCode].lon, filterData[destinationCode].lat ]
        	                ]
        	    });
       		}
 
	        


	        // Standard enter / update 
	        var pathArcs = svg.selectAll(".arc")
	            .data(connectionsData);

	        //enter
	        pathArcs.enter()
	            .append("path").attr({
	                'class': 'arc'
	            }).style({ 
	                fill: 'none',
	            });

	        //update
	        pathArcs.attr({
	                //d is the points attribute for this path, we'll draw
	                //  an arc between the points using the arc function
	                d: path
	            })
	            .style({
	                stroke: '#0000ff',
	                'stroke-width': '2px'
	            })
	            // Uncomment this line to remove the transition
	            .call(lineTransition); 

	        //exit
	        pathArcs.exit().remove();

	    });

	  
	    });	
	
	
// 	for(var i = 0; i < connections.length; i++)
// 	{
// 		var connection = connections[i];
		
// 		d3.geo.greatArc().source(connection["source"]).target(connection["target"]);
// 	}
	
	</script>

		
</body>
</html>