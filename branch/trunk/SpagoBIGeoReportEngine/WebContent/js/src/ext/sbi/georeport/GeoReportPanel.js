/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
/**
  * Object name 
  * 
  * [description]
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.georeport");

Sbi.georeport.GeoReportPanel = function(config) {
	
	var defaultSettings = {
			mapName: 'Mappa di prova'
	};
		
	if(Sbi.settings && Sbi.settings.georeport && Sbi.settings.georeport.georeportPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.georeport.georeportPanel);
	}
		
	var c = Ext.apply(defaultSettings, config || {});
		
	Ext.apply(this, c);
		
		
	this.services = this.services || new Array();	
	/*
	this.services['doThat'] = this.services['doThat'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'DO_THAT_ACTION'
		, baseParams: new Object()
	});
	*/
	
	this.addEvents('customEvents');
		
		
	this.initMap();
	this.initMapPanel();
	this.initControlPanel();
	
	c = Ext.apply(c, {
         layout   : 'border',
         items    : [this.controlPanel, this.mapPanel]
	});

	// constructor
	Sbi.georeport.GeoReportPanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.georeport.GeoReportPanel, Ext.Panel, {
    
    services: null
    , map: null
    , lon: null
    , lat: null
    , zoomLevel: null
    , layers: null
    , showPositon: true
    , showOverview: true
    , mapName: null
    , mapPanel: null
    , controlPanel: null
    
    , analysisType: null
    , targetLayerName: null
    , targetLayerLabel: null
    , targetLayer: null
    , geostatistic: null
    
    // -- public methods ------------------------------------------------------------------------
    
    , setCenter: function(center) {
		center = center || {};
		this.lon = center.lon || this.lon;
		this.lat = center.lat || this.lat;
		this.zoomLevel = center.zoomLevel || this.zoomLevel;
		
		this.centerPoint = new OpenLayers.LonLat(this.lon, this.lat);
		this.map.setCenter(this.centerPoint, this.zoomLevel);
	}

    
	 // -- private methods ------------------------------------------------------------------------
	

	, initLayers: function(c) {
		this.layers = [
		    new OpenLayers.Layer.WMS( 
		    		"OpenLayers WMS",
		    		"http://labs.metacarta.com/wms/vmap0",
		    		{layers: 'basic'},
		    		{singleTile: true}
		    ), 
		    
		    new OpenLayers.Layer.WMS( 
		    		"NASA Global Mosaic",
	                "http://t1.hypercube.telascience.org/cgi-bin/landsat7", 
	                {layers: "landsat7"}
		    ),
		    
		    new OpenLayers.Layer.WMS(
		    		"Satellite",
					"http://labs.metacarta.com/wms-c/Basic.py?", 
                    {layers: 'satellite', format: 'image/png'}
		    )
		];
	}
	
	, initAnalysis: function() {
		
		var url = '/SpagoBIGeoReportEngine/MapOl' 
			url += '?layer=' + this.targetLayerName;
			url += '&server=' + this.geojsonUrl;
			url += '&label=' + this.spagobiDataset;
			url += '&businessId=' + this.businessId;
			url += '&geoid=' + this.geoId;
			
		
		var indic = new Array(this.indicators.length);

		for(var i= 0, len = indic.length; i < len; i++){
			indic[i] = this.indicators[i];
		} 
		
		if (this.analysisType === "prop") {
			this.initProportionalSymbolsAnalysis();
			
			this.geostatistic = new mapfish.widgets.geostat.ProportionalSymbol({
	            'map': this.map,
	            'layer': this.targetLayer,
	            'indicators': indic,
	            'url': url,
	            'loadMask' : {msg: 'Proportional Symbols analysis...', msgCls: 'x-mask-loading'},
	            'legendDiv' : 'myChoroplethLegendDiv',
	            'featureSelection': false
    		});
		} else if (this.analysisType === "chorop") {
			this.initChoroplethAnalysis();
			
			this.geostatistic = new mapfish.widgets.geostat.Choropleth({
	            'map': this.map,
	            'layer': this.targetLayerName,
	            'indicators': indic,
	            'url': url,
	            'loadMask' : {msg: 'Please wait', msgCls: 'x-mask-loading'},
	            'legendDiv' : 'myChoroplethLegendDiv',
	            'featureSelection': false,
	            'listeners': {}
	        });
		} else {
			alert('error: unsupported analysis type [' + this.analysisType + ']');
		}
		
		this.map.addControl(this.analysisLayerSelectControl); 
		this.analysisLayerSelectControl.activate();
		
		
		
		
	}
	
	, initProportionalSymbolsAnalysis: function() {
		//alert('initProportionalSymbolsAnalysis: ' + this.targetLayerLabel + '; ' + this.targetLayerName);
		this.targetLayer = new OpenLayers.Layer.Vector(this.targetLayerLabel, {
				'visibility': false  ,
				'styleMap': new OpenLayers.StyleMap({
	   				'select': new OpenLayers.Style(
	       				{'strokeColor': 'red', 'cursor': 'pointer'}
	   				)
				})
		});

		this.map.addLayers([this.targetLayer]);

		/*
		var p = new Object();
		p.desc=['desc'];

		for (var i=0; i<parameters.length; i++){
			param = parameters[i][1];
            desc = parameters[i][0];

            p['desc'] = param;
        }
		                      
		var p2 = new Object();
		p2.desc=['desc'];

		for (var i=0; i< document2Parameters.length; i++){
        	param = document2Parameters[i][1];
            desc = document2Parameters[i][0];
            alert(desc + ' = ' + param);
            p2['desc'] = param;
        }
		*/
		
		
		
        this.analysisLayerSelectControl = new OpenLayers.Control.SelectFeature(this.targetLayer, {} );

        this.targetLayer.events.register("featureselected", this, function(o) { 
			//alert('select -> ' + this.getInfo(o.feature));
			this.onTargetFeatureSelect(o.feature);
		}); 
        
        this.targetLayer.events.register("featureunselected", this, function(o) { 
			//alert('unselect -> ' + this.getInfo(o.feature));
			this.onTargetFeatureUnselect(o.feature);
		}); 
		
	}
	
	, initChoroplethAnalysis: function() {
		this.targetLayer = new OpenLayers.Layer.Vector(this.targetLayerLabel, {
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
     
    
    	this.map.addLayer(this.targetLayer);
        
    	/*
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
        */                              
        var analysisLayerSelectControl = new OpenLayers.Control.SelectFeature(this.targetLayer, {});
        
        this.targetLayer.events.register("featureselected", this, function(o) { 
			//alert('select -> ' + this.getInfo(o.feature));
			this.onTargetFeatureSelect(o.feature);
		}); 
        
        this.targetLayer.events.register("featureunselected", this, function(o) { 
			//alert('unselect -> ' + this.getInfo(o.feature));
			this.onTargetFeatureUnselect(o.feature);
		}); 
	}
	
	, onTargetFeatureSelect: function(feature) {
		this.selectedFeature = feature;
        content = "<div style='font-size:.8em'>" + this.getInfo(feature);
        
        /*
        for (var i=0; i<listeners.length; i++){
            param = feature.attributes[this.listeners[i][1]];
            desc = this.listeners[i][0];
            p[desc] = param;
        }

   	    for (var i=0; i<document2Listeners.length; i++){
        	param = feature.attributes[document2Listeners[i][1]];
            desc = document2Listeners[i][0];
            p2[desc] = param;
        }
         
        spagobiContent = p;
        spagobiContent2 = p2;
		*/
        
        /* ----
         function(docLab, role, params, dispToolbar, dispSlide,frameId) {
         
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
         */

        var execDetailFn = "execDoc(";
        execDetailFn += "'" + this.document2Label + "',"; // documentLabel
        execDetailFn += "'" + this.role + "',"; // execution role
        execDetailFn += "{}" + ","; // parameters
        execDetailFn += this.dispToolbar2 + ","; // displayToolbar
        execDetailFn += this.dispSlide2 + ","; // displaySliders
        execDetailFn += "'" + this.document2Label + "'"; // frameId
        execDetailFn += ")";
        
        var link = '';
        link += '<center>'
        link += '	<font size="1" face="Verdana">'
        link += '		<a href="#" onclick="Ext.getCmp(\'' + this.mapPanel.getId() + '\').setActiveTab(\'infotable\');' 
        link += '       Ext.getCmp(\'infotable\').body.dom.innerHTML = ';
        link += '     	' + execDetailFn + '";>' 
        link += 'Dettagli</a></font></center>';

        popup = new OpenLayers.Popup.FramedCloud("chicken", 
                feature.geometry.getBounds().getCenterLonLat(),
                null,
                content + link + execDoc(this.document1Label, this.role, {}, this.dispToolbar1, this.dispSlide1, this.document1Label),
                null, 
                true, 
                function(evt) {
        			this.analysisLayerSelectControl.unselect(this.selectedFeature);    
                }.createDelegate(this, [])
        );
        
        feature.popup = popup;
        this.map.addPopup(popup);
	}
	
	, onTargetFeatureUnselect: function(feature) {
		this.map.removePopup(feature.popup);
        feature.popup.destroy();
        feature.popup = null;
        var infoPanel = Ext.getCmp('infotable');
        if(infoPanel.body){
        	infoPanel.body.dom.innerHTML = '';
        }
	}
	
	, getInfo: function(feature) {
		var info = "";
	    for(var i=0; i<this.feautreInfo.length; i++){
	    	info = info+"<b>"+ this.feautreInfo[i][0] +"</b>: " + feature.attributes[this.feautreInfo[i][1]] + "<br />";    
	    } 
	    return info;
	}
	
	
	
	
	

	, initMap: function(c) {
		this.map = new OpenLayers.Map('map');

		  
		this.initLayers(c);
	  
		this.map.addLayers(this.layers);
		this.setCenter(c);

		if(this.showPositon) {
			this.map.addControl(new OpenLayers.Control.MousePosition());
		}
		
		if(this.showOverview) {
			this.map.addControl(new OpenLayers.Control.OverviewMap());
		}
		
		this.initAnalysis();
	}
	
	, initMapPanel: function() {
		this.mapPanel = new Ext.TabPanel({
		    region    : 'center',
		    margins   : '3 3 3 0', 
		    activeTab : 0,
		    defaults  : {
				autoScroll : true
			},

	       	items: [{
	        	title: this.mapName,
	            xtype: 'mapcomponent',
	            map: this.map
	        },{
	            title    : 'Info',
	            html: '<div id="info"</div>',
	            id: 'infotable',
	            autoScroll: true
	        }]
		});
	}
	
	, initControlPanel: function() {
		
		/*
		 this.toolbar = new mapfish.widgets.toolbar.Toolbar({
		    	map: this.map, 
		        configurable: false
		 });
		 
		 this.initToolbarContent();
		 */
		
		 this.controlPanel = new Ext.Panel({
		    	title       : 'Navigazione',
		        region      : 'west',
		        split       : true,
		        width       : 300,
		        collapsible : true,
		        margins     : '3 0 3 3',
		        cmargins    : '3 3 3 3',
		        items: [
		        {
		        	title: 'Livelli',
		            collapsible: true,
		            autoHeight: true,
		            xtype: 'layertree',
		            map: this.map
		        }, /*{
		            title: 'Toolbar',
		            collapsible: true,
		            region: 'center',
		            tbar: this.toolbar                
		        },*/ {
		        	title: 'Analisi',
		            region: 'south',
		            collapsible: true,
		            items: [this.geostatistic]
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
		 
		
	}
	
	, addSeparator: function(toolbar){
          toolbar.add(new Ext.Toolbar.Spacer());
          toolbar.add(new Ext.Toolbar.Separator());
          toolbar.add(new Ext.Toolbar.Spacer());
    } 

	, initToolbarContent: function() {
			
		//alert('initToolbarContent');
		
		var vectorLayer = new OpenLayers.Layer.Vector("vector", { 
	    	displayInLayerSwitcher: false
	    });
	    this.map.addLayer(vectorLayer);
	    
	 
	   
			this.toolbar.addControl(
	              new OpenLayers.Control.ZoomToMaxExtent({
	                  map: this.map,
	                  title: 'Zoom to maximum map extent'
	              }), {
	                  iconCls: 'zoomfull', 
	                  toggleGroup: 'map'
	              }
	          );
		
			
			  this.addSeparator(toolbar);
	      
			  this.toolbar.addControl(
	              new OpenLayers.Control.ZoomBox({
	                  title: 'Zoom in: click in the map or use the left mouse button and drag to create a rectangle'
	              }), {
	                  iconCls: 'zoomin', 
	                  toggleGroup: 'map'
	              }
	          );
	      
			  this.toolbar.addControl(
	              new OpenLayers.Control.ZoomBox({
	                  out: true,
	                  title: 'Zoom out: click in the map or use the left mouse button and drag to create a rectangle'
	              }), {
	                  iconCls: 'zoomout', 
	                  toggleGroup: 'map'
	              }
	          );
	          
			  this.toolbar.addControl(
	              new OpenLayers.Control.DragPan({
	                  isDefault: true,
	                  title: 'Pan map: keep the left mouse button pressed and drag the map'
	              }), {
	                  iconCls: 'pan', 
	                  toggleGroup: 'map'
	              }
	          );
	          
			  this.addSeparator(toolbar);
	          
			  this.toolbar.addControl(
	              new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Point, {
	                  title: 'Draw a point on the map'
	              }), {
	                  iconCls: 'drawpoint', 
	                  toggleGroup: 'map'
	              }
	          );
	          
			  this.toolbar.addControl(
	              new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Path, {
	                  title: 'Draw a linestring on the map'
	              }), {
	                  iconCls: 'drawline', 
	                  toggleGroup: 'map'
	              }
	          );
	          
			  this.toolbar.addControl(
	              new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
	                  title: 'Draw a polygon on the map'
	              }), {
	                  iconCls: 'drawpolygon', 
	                  toggleGroup: 'map'
	              }
	          );
	          
			  this.addSeparator(toolbar);
	      
	          var nav = new OpenLayers.Control.NavigationHistory();
	          this.map.addControl(nav);
	          nav.activate();
	          
	          this.toolbar.add(
	              new Ext.Toolbar.Button({
	                  iconCls: 'back',
	                  tooltip: 'Previous view', 
	                  handler: nav.previous.trigger
	              })
	          );
	          
	          this.toolbar.add(
	              new Ext.Toolbar.Button({
	                  iconCls: 'next',
	                  tooltip: 'Next view', 
	                  handler: nav.next.trigger
	              })
	          );
	          
	          this.addSeparator(toolbar);
	          
	          this.toolbar.activate();
      }
});