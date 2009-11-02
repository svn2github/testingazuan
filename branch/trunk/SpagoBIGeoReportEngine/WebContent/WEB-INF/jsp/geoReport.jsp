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
	</head>
	
	<body>
	
		<!-- Include template here  -->
		<script language="javascript" type="text/javascript">
			map = {
				layer: "spagobi_capoluoghi",
				layer_name: "Capoluoghi",
				analysis: "prop",
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
			    document1Label: "geobi_dial2", 
			    document2Label: "GEOREPORT_DOC_BAR",  
			    role: "spagobi/admin",
			    dispToolbar1: "false",
			    dispSlide1: "false",
			    dispToolbar2: "false",
			    dispSlide2: "false",
			    listeners: [["value","numero_watson"]],
			    document2Listeners: [["regione","gl_regione"]],
			    parameters: [],
			    document2Parameters: [],
			    feautreInfo: [["REGIONE","gl_regione"], ["CAPOLUOGO","nome"]],
				  initPosLon: 6.090,
				  initPosLat: 40.373,
				  initZoom: 5
			};

		</script>
		
		<%@include file="commons/includeSpagoBIGeoReportJS.jspf" %>
		
		<script language="javascript" type="text/javascript">
			Ext.onReady(function(){

		 
	      		start_map(lon, lat, zoom);
	        
	    		if (analysis == "prop"){ 

	    		    alert('propSymbolLayer: ' + propSymbolLayer);
	    			var propSymbolLayer = [propSymbolLayer];
	    			propSymbolLayer = new OpenLayers.Layer.Vector(layer_name, {
	       				'visibility': false  ,
	       				'styleMap': new OpenLayers.StyleMap({
	           				'select': new OpenLayers.Style(
	               				{'strokeColor': 'red', 'cursor': 'pointer'}
	           				)
	       				})
	    			});

	    			map.addLayers([propSymbolLayer]);

	    			var p = new Object();
	    			p.desc=['desc'];

	    			for (var i=0; i<parameters.length; i++){
	    				param = parameters[i][1];
                        desc = parameters[i][0];
                        alert(desc + ' = ' + param);
	                    p['desc'] = param;
                    }
	    			alert('-> p.desc =  ' + p['desc']);
	                                      
					var p2 = new Object();
	    			p2.desc=['desc'];

	    			for (var i=0; i< document2Parameters.length; i++){
	                	param = document2Parameters[i][1];
                        desc = document2Parameters[i][0];
                        alert(desc + ' = ' + param);
	                    p2['desc'] = param;
                    }
	    			alert('-> p2.desc =  ' + p2['desc']);
	                                      
	                var selectPropSymbol2 = selectControl = new OpenLayers.Control.SelectFeature(propSymbolLayer, {
	                	onSelect: (function(feature){
	                    	selectedFeature = feature;
	                        content = "<div style='font-size:.8em'>" + getInfo(feature);
	                        for (var i=0; i<listeners.length; i++){
	                            param = feature.attributes[listeners[i][1]];
	                            desc = listeners[i][0];
	                            p[desc] = param;
                            }

	                   	    for (var i=0; i<document2Listeners.length; i++){
	                        	param = feature.attributes[document2Listeners[i][1]];
	                            desc = document2Listeners[i][0];
	                            p2[desc] = param;
                            }
	                         
                            spagobiContent = p;
                            spagobiContent2 = p2;
	       

                            var link = '<center><font size="1" face="Verdana"><a href="#" onclick="DashboardsMap.infotab();' + "Ext.getCmp('infotable').body.dom.innerHTML" + ' = execDoc(document2Label, role, spagobiContent2, dispToolbar2, dispSlide2, document2Label)";>' + 'Dettagli</a></font></center>';

                            popup = new OpenLayers.Popup.FramedCloud("chicken", 
                                    feature.geometry.getBounds().getCenterLonLat(),
                                    null,
                                    content + link + execDoc(document1Label, role, spagobiContent, dispToolbar1, dispSlide1, document1Label),
                                    null, true, DashboardsMap.onPopupClose
                            );

                            feature.popup = popup;
	                        map.addPopup(popup);
	                	}), 
	                	
	                 	onUnselect: DashboardsMap.onFeatureUnselect
	              	});
	    
	 
	    			map.addControl(selectPropSymbol2); 
	    			selectPropSymbol2.activate();

	      			var geostatistic = new mapfish.widgets.geostat.ProportionalSymbol({
			            'map': map,
			            //'layer': propSymbolLayer,
			            'indicators': indic,
			            'url': '/SpagoBIGeoReportEngine/MapOl?layer=' + layername + '&server=' + server + '&label=' + label + '&businessId=' + businessId + '&geoid=' + geoid,
			            'loadMask' : {msg: 'Proportional Symbols analysis...', msgCls: 'x-mask-loading'},
			            'legendDiv' : 'myChoroplethLegendDiv',
			            'featureSelection': false
	        		});

	      			/*
	    	        alert('overrides');
	    	        mapfish.widgets.geostat.ProportionalSymbol.classify = function() {
	      				alert('classify 0');
	      		        if (!this.ready) {
	      		            if (exception) {
	      		                Ext.MessageBox.alert('Error', 'Component init not complete');
	      		            }
	      		            return;
	      		        }

						alert('classify');
	      		        
	      		        this.indicator = this.form.findField('indicator').getValue();
	      		        this.indicatorText = this.form.findField('indicator').getRawValue();
	      		        if (!this.indicator) {
	      		            Ext.MessageBox.alert('Error', 'You must choose an indicator');
	      		            return;
	      		        }
	      		        var minSize = this.form.findField('minSize').getValue();
	      		        var maxSize = this.form.findField('maxSize').getValue();

	      		        alert(this.indicator + ' - ' + this.indicatorText);
	      		      
	      		        this.coreComp.updateOptions({
	      		            'indicator': this.indicator,
	      		            'minSize': minSize,
	      		            'maxSize': maxSize
	      		        });

	      		      	alert('classify 2');
	      		      
	      		        this.coreComp.applyClassification();
	      		      	alert('classify 3');
	      		        this.classificationApplied = true;
	      		      alert('classify 4');
	      		    };*/
	    } else if (analysis == "chorop"){
	    
	    
	    	choroplethLayer = new OpenLayers.Layer.Vector(layer_name, {
	        	'visibility': false,
	          	'styleMap': new OpenLayers.StyleMap({
	            	'default': new OpenLayers.Style(
	                	OpenLayers.Util.applyDefaults(
	                      {'fillOpacity': 0.5},
	                      OpenLayers.Feature.Vector.style['default']
	                  	)
	              	),
	              	'select': new OpenLayers.Style(
	                  {'strokeColor': 'red', 'cursor': 'pointer'}
	              	)
	          	})
	      	});
	     
	    
	    	map.addLayer(choroplethLayer);
	        
	    	var p = new Object();
	    	p.desc=['desc'];
	    	for (var i=0; i<parameters.length; i++){
	        	param = parameters[i][1];
                desc = parameters[i][0];
                p[desc] = param;
            }
	                                      
		    var p2 = new Object();
		    p2.desc=['desc'];
	    	for (var i=0; i<document2Parameters.length; i++){
	        	param = document2Parameters[i][1];
                desc = document2Parameters[i][0];
                p2[desc] = param;
            }
	                                      
	        var selectPropSymbol2 = selectControl = new OpenLayers.Control.SelectFeature(choroplethLayer, {
	        	onSelect: (function(feature){
	          	  selectedFeature = feature;
	              content = "<div style='font-size:.8em'>"+getInfo(feature);
	              for (var i=0; i<listeners.length; i++){
	              	param = feature.attributes[listeners[i][1]];
                    desc = listeners[i][0];
                    p[desc] = param;
                  }

	        	  for (var i=0; i<document2Listeners.length; i++){
	              	param = feature.attributes[document2Listeners[i][1]];
                    desc = document2Listeners[i][0];
                    p2[desc] = param;
                  }
	                         
	              spagobiContent = p;
	              spagobiContent2 = p2;
	       
                  var link = '<center><font size="1" face="Verdana"><a href="#" onclick="DashboardsMap.infotab();' + "Ext.getCmp('infotable').body.dom.innerHTML" + ' = execDoc(document2Label, role, spagobiContent2, dispToolbar2, dispSlide2, document2Label)";>' + 'Dettagli</a></font></center>';

                  popup = new OpenLayers.Popup.FramedCloud("chicken", 
                      feature.geometry.getBounds().getCenterLonLat(),
                      null,
                      content + link + execDoc(document1Label, role, spagobiContent, dispToolbar1, dispSlide1, document1Label),
                      null, true, DashboardsMap.onPopupClose
                  );
                  feature.popup = popup;
	              map.addPopup(popup);
	           }), 

	           onUnselect: DashboardsMap.onFeatureUnselect}
			);
	      
	    
	    	map.addControl(selectPropSymbol2); 
	    	selectPropSymbol2.activate();
	      
	      	var geostatistic = new mapfish.widgets.geostat.Choropleth({
	            'map': map,
	            'layer': choroplethLayer,
	            'indicators': indic,
	            //'url': 'http://' + server + '/geoserver/wfs?request=GetFeature&typename=topp:'+ layername + '&outputformat=json&version=1.0.0',
	            'url': '/SpagoBIGeoReportEngine/MapOl?layer=' + layername + '&server=' + server  + '&label=' + label + '&businessId=' + businessId + '&geoid=' + geoid,
	            'loadMask' : {msg: 'Choropleths analysis...', msgCls: 'x-mask-loading'},
	            'legendDiv' : 'myChoroplethLegendDiv',
	            'featureSelection': false,
	            'listeners': {}
	        });
	  
	    } else {
		    Ext.Msg.alert( 'Status', 'No analysis category is set! Set one in template.html file!' );
		}   



	   	//TOOLBAR
	  
	    vectorLayer = new OpenLayers.Layer.Vector("vector", { 
	    	displayInLayerSwitcher: false
	    });
	    map.addLayer(vectorLayer);

	            
	            
	    toolbar = new mapfish.widgets.toolbar.Toolbar({
	    	map: map, 
	        configurable: false
	    });
	  

	  	// WINDOW
	  
	    // tabs for the center
	    var tabs = new Ext.TabPanel({
	    	region    : 'center',
	        margins   : '3 3 3 0', 
	        id: 'tab',
	        activeTab : 0,
	        defaults  : {
				autoScroll : true
			},

	       	items: [{
	        	title: mpname,
	            xtype: 'mapcomponent',
	            map: map
	        },{
	            title    : 'Info',
	            html: '<div id="info"</div>',
	            id: 'infotable',
	            autoScroll: true
	        }]
	    });

	    // Panel for the west
	    var nav = new Ext.Panel({
	    	title       : 'Navigazione',
	        region      : 'west',
	        split       : true,
	        width       : 300,
	        collapsible : true,
	        margins     : '3 0 3 3',
	        cmargins    : '3 3 3 3',
	        items: [{
	        	title: 'Livelli',
	            region: 'north',
	            collapsible: true,
	            autoHeight: true,
	            xtype: 'layertree',
	            map: map
	        },{
	            title: 'Toolbar',
	            collapsible: true,
	            region: 'center',
	            tbar: toolbar                
	        }, {
	        	title: 'Analisi',
	            region: 'south',
	            collapsible: true,
	            items: [geostatistic]
	        }, {
	           title: 'Legenda',
	           region: 'south',
	           collapsible: true,
	           height: 150,
	           html: '<center id="myChoroplethLegendDiv"></center>'
	        }, {
	           title: 'Logo',
	           region: 'south',
	           collapsible: true,
	           height: 85,
	           html: '<center><img src="/SpagoBIGeoReportEngine/js/lib/mapfish/georeport.png" alt="GeoReport"/></center>'
	        }]
	    }); 
	      
	    if (extwindow == "true") {
	    	if ((mapwidth==0) || (mapheight==0)) {
		      	var win = new Ext.Window({
		            title    : 'GeoReport',
		            closable : true,
		            width    : 500,
		            height   : 300,
		            plain    : true,
		            layout   : 'border',
		            items    : [nav, tabs]
		        });
	
		        win.show();
		      	      
		      	Ext.Msg.alert( 'Status', 'Please set Window dimension (height and width) in your template.html!' );
		    } else if ((mapwidth!=0) && (mapheight!=0)) {
		    	var win = new Ext.Window({
		            title    : 'GeoReport',
		            closable : true,
		            width    : mapwidth,
		            height   : mapheight,
		            plain    : true,
		            layout   : 'border',
		            items    : [nav, tabs]
		        });

	        	win.show();	    
	    	}
	    } else {	    
	      var viewport = new Ext.Viewport({
	            title    : 'GeoReport',
	            closable : true,
	            width    : mapwidth,
	            height   : mapheight,
	            plain    : true,
	            layout   : 'border',
	            items    : [nav, tabs]
	        });

	        this.viewport=viewport;

	   }

	   initToolbarContent();
	     
	});
	alert('ho fatto tutto');
	
	</script>
		
		
		
		
		<div style="width: 600px; height: 200px; z-index:0;">&nbsp;
	  
		<div id="buttonbar"></div>
		<div id="map"></div>
	
		</div>
		<center id="error"></center>
	 
	</body>

</html>

