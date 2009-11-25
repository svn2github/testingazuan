<%-- 
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
--%>

<%-- 
author: Andrea Gioia (andrea.gioia@eng.it)
--%>
<%@ page language="java" 
	     contentType="text/html; charset=ISO-8859-1" 
	     pageEncoding="ISO-8859-1"%>	


<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA IMPORTS															--%>
<%-- ---------------------------------------------------------------------- --%>
<% // no imports %>


<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA CODE 																--%>
<%-- ---------------------------------------------------------------------- --%>
<% // lucky for us no code yet %>

<%-- ---------------------------------------------------------------------- --%>
<%-- HTML	 																--%>
<%-- ---------------------------------------------------------------------- --%>

<html>

	<head>
		<title>SpagoBIGeoReportEngine</title>
		
		<%@include file="commons/includeExtJS.jspf" %>
		<%@include file="commons/includeMapFishJS.jspf" %>
		<%@include file="commons/includeSpagoBIGeoReportJS.jspf" %>
	</head>
	
	<body>
	
		<!-- Include template here  -->
		<script language="javascript" type="text/javascript">
			map = {
				targetLayerName: "spagobi_capoluoghi",
				targetLayerLabel: "Capoluoghi",
				analysisType: "prop",
				indicators: [["numero_watson", "XNUMERO"],["valore_watson", "XVALORE"]],


			    geojsonUrl: "localhost:8080",
			    


			    //IF YOU WANT TO DISPLAY MAP IN EXT WINDOW (default is Ext ViewPort)
			    window: "false",
			    businessId: "id_capoluog", //it links to alphanumeric data into spagobi dataset
			    geoId: "id_capoluog", //it links to geometires 
			    
			    mapname: "WATSONs",
			    width: 0, //ONLY FOR EXT WINDOW
			    height: 600, //ONLY FOR EXT WINDOW

			    spagobiDataset: 'mapdata', //spagobi dataset label from which you can retreive alphanumeric data

			    role: "spagobi/admin",
			    document2Label: "DepartmentList",  
			    dispToolbar2: "false",
			    dispSlide2: "false",
			    document2Listeners: [["regione", "gl_regione"]],
			    document2Parameters: [],
			    
			    document1Label: "DIALCHART_simpledial", 			   
			    dispToolbar1: "false",
			    dispSlide1: "false",			   
			    listeners: [["value","numero_watson"]],
			    parameters: [],
			    
			    feautreInfo: [["REGIONE","gl_regione"], ["CAPOLUOGO","nome"]],
				  lon: 6.090,
				  lat: 40.373,
				  zoomLevel: 5
			};


			execDoc = function(docLab, role, params, dispToolbar, dispSlide,frameId) {
				
				var html = Sbi.sdk.api.getDocumentHtml({
					documentLabel: docLab
					, executionRole: "/" + role
					, parameters: params 
			      	, displayToolbar: dispToolbar
					, displaySliders: dispSlide
					, iframe: {
			        	id: frameId,
			          	height: '100%'
				    	, width: '100%'
						, style: 'border: 0px;'
					}
				});
				
				//var html = '<h1>Prova provata ' + docLab + ' </h1>'
			    return html;
			};

		</script>
		
		
		
		<script language="javascript" type="text/javascript">
			Ext.onReady(function(){

				Ext.QuickTips.init();   
				var geoReportPanel = new Sbi.georeport.GeoReportPanel(map);	    
	      		var viewport = new Ext.Viewport({
		      		layout: 'fit',
		            items: [geoReportPanel]
		        });
			});
	
	
	</script>
		
		
		
		
		<div style="width: 600px; height: 200px; z-index:0;">&nbsp;
	  
		<div id="buttonbar"></div>
		<div id="map"></div>
	
		</div>
		<center id="error"></center>
		
	 
	</body>

</html>

