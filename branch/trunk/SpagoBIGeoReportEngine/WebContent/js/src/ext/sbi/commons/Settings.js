Ext.ns("Sbi.settings");

Sbi.settings.georeport = {
		georeportPanel: {
			
	
			/**
			 	configurations of layers that must be loaded at startup
			 	
			 	layer configuration is an object composed by the following attributes ...
			 	 - name {String} A name for the layer (can be localized)
				 - url {String} Base url for the WMS (e.g.  http://wms.jpl.nasa.gov/wms.cgi) 
				 - params {Object} An object with key/value pairs representing the GetMap query string parameters and parameter values. 
				 - options {Ojbect} Hashtable of extra options to tag onto the layer 
			 	
			 	for more informations see:
			 		http://dev.openlayers.org/releases/OpenLayers-2.7/doc/apidocs/files/OpenLayers/Layer/WMS-js.html#OpenLayers.Layer.WMS.OpenLayers.Layer.WMS
			 */ 
			baseLayersConf: [
			   {
				   name: "OpenLayers WMS",
				   url: "http://labs.metacarta.com/wms/vmap0",
				   params: {layers: 'basic'},
				   options: {singleTile: true}
			   }, /*{
				   name: "NASA Global Mosaic",
				   url: "http://t1.hypercube.telascience.org/cgi-bin/landsat7",
				   params: {layers: "landsat7"}
			   }, */ {
				   name: "Satellite",
				   url: "http://labs.metacarta.com/wms-c/Basic.py?",
				   params: {layers: 'satellite', format: 'image/png'}
			   }
			]
			
			// controls
			, showPositon: true
			, showOverview: true
			
		}


};
