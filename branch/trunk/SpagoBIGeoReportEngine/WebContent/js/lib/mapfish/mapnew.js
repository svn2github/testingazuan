var map;
var slayer;
var layerMapnik;
var layerTah;
var center_point;
var markers;
var show_layer_control;
var popup;
var feature;
var marker;
var zoom_level


/** 
    * Method: lonLatToMercator
    * Converts a LonLat Object using the Mercator formular
    *
    * Parameters:
    * ll - {<OpenLayers.LonLat>} the coordinate object.
    * 
    * Returns:
    * <OpenLayers.LonLat> the transformed coordinates
    */
function lonLatToMercator(ll) {
	var lon = ll.lon * 20037508.34 / 180;
	var lat = Math.log(Math.tan((90 + ll.lat) * Math.PI / 360)) / (Math.PI / 180);
	lat = lat * 20037508.34 / 180;
	return new OpenLayers.LonLat(lon, lat);
}


/** 
    * Method: init_map
    * Constructs sets some inital values and calls show_map.
    *
    * Parameters:
    * lon - {Float} The longitude coordinate.
    * lat - {Float} The latitude coordinate.
    * zoom - {Integer} Zoomlevel for initial display.
    * b_layer_control - {String} 'true' to show Layer selector
    */


function start_map(lon, lat, zoom){

	center_lon = lon;
	center_lat = lat;
	//center_point = lonLatToMercator(new OpenLayers.LonLat(lon,lat));
  center_point = new OpenLayers.LonLat(lon,lat);
  zoom_level = zoom; 
	
	display_map(center_point,zoom_level);
	
	
}


/** 
    * Method: show_map
    * Sets the inital layer and displays the map.
    */
function display_map (){
	
	map = new OpenLayers.Map('map');

  
	var ol_wms = new OpenLayers.Layer.WMS( "OpenLayers WMS",
                    "http://labs.metacarta.com/wms/vmap0",
                    {layers: 'basic'},{singleTile: true} );
	var jpl_wms = new OpenLayers.Layer.WMS( "NASA Global Mosaic",
                "http://t1.hypercube.telascience.org/cgi-bin/landsat7", 
                {layers: "landsat7"});
	var sat = new OpenLayers.Layer.WMS("Satellite",
					"http://labs.metacarta.com/wms-c/Basic.py?", 
                    {layers: 'satellite', format: 'image/png'});

 
  
	map.addLayers([ol_wms, jpl_wms, sat]);

	//set center and zoomlevel of the map
	map.setCenter(center_point, zoom_level);


	show_positon();
	show_overview();
  
}

/** 
    * Method: add_marker
    * Adds a new marker - Not implemented.
    */
function add_marker(point, icon){

	
}

/** 
    * Method: delete_marker
    * Deletes a marker - Not implemented.
    */
function delete_marker(old_marker){

}

/** 
    * Method: changee_marker
    * Change the marker Icon - Not implemented.
    */
function change_marker(old_marker, new_icon){

}


/** 
    * Method: show_bubble
    * Shows a popup bubble with the html content at the passed marker
    *
    * Parameters:
    * lon - {Float} The longitude coordinate.
    * lat - {Float} The latitude coordinate.
    * icon_color - {String} Color of the marker to display - red, green, yellow - default = blue
    * record - {Array} Data to be passed with the marker
    */
function show_bubble(lonlat, html){
	if (popup != null) {
	   markers.map.removePopup(popup);
	   popup.destroy();
	   popup = null;
    }
    
	feature = new OpenLayers.Feature(markers,lonlat);
	feature.popupClass = OpenLayers.Popup.FramedCloud;
	popup = feature.createPopup(true);
	popup.setContentHTML(html);
	markers.map.addPopup(popup);	
}


/** 
    * Method: show_position
    * Shows the mouse pointer coordinates when hovering over the map
    */
function show_positon(){
	map.addControl(new OpenLayers.Control.MousePosition());
}

function show_overview(){
	map.addControl(new OpenLayers.Control.OverviewMap());
}

/** 
    * Method: show_layers
    * Add the layer control to the map
   */
function show_layers(){
	map.addControl(new OpenLayers.Control.LayerSwitcher());
}

//function is needed to get the OpenStreetMap tiles
function osm_getTileURL(bounds) {
    var res = this.map.getResolution();
    var x = Math.round((bounds.left - this.maxExtent.left) / (res * this.tileSize.w));
    var y = Math.round((this.maxExtent.top - bounds.top) / (res * this.tileSize.h));
    var z = this.map.getZoom();
    var limit = Math.pow(2, z);

    if (y < 0 || y >= limit) {
        return OpenLayers.Util.getImagesLocation() + "404.png";
    } else {
        x = ((x % limit) + limit) % limit;
        return this.url + z + "/" + x + "/" + y + "." + this.type;
    }
}


//TOOLBAR
      
      function addSeparator(toolbar){
            toolbar.add(new Ext.Toolbar.Spacer());
            toolbar.add(new Ext.Toolbar.Separator());
            toolbar.add(new Ext.Toolbar.Spacer());
        } 

        function initToolbarContent() {
        
            toolbar.addControl(
                new OpenLayers.Control.ZoomToMaxExtent({
                    map: map,
                    title: 'Zoom to maximum map extent'
                }), {
                    iconCls: 'zoomfull', 
                    toggleGroup: 'map'
                }
            );
            
            addSeparator(toolbar);
        
            toolbar.addControl(
                new OpenLayers.Control.ZoomBox({
                    title: 'Zoom in: click in the map or use the left mouse button and drag to create a rectangle'
                }), {
                    iconCls: 'zoomin', 
                    toggleGroup: 'map'
                }
            );
        
            toolbar.addControl(
                new OpenLayers.Control.ZoomBox({
                    out: true,
                    title: 'Zoom out: click in the map or use the left mouse button and drag to create a rectangle'
                }), {
                    iconCls: 'zoomout', 
                    toggleGroup: 'map'
                }
            );
            
            toolbar.addControl(
                new OpenLayers.Control.DragPan({
                    isDefault: true,
                    title: 'Pan map: keep the left mouse button pressed and drag the map'
                }), {
                    iconCls: 'pan', 
                    toggleGroup: 'map'
                }
            );
            
            addSeparator(toolbar);
            
            toolbar.addControl(
                new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Point, {
                    title: 'Draw a point on the map'
                }), {
                    iconCls: 'drawpoint', 
                    toggleGroup: 'map'
                }
            );
            
            toolbar.addControl(
                new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Path, {
                    title: 'Draw a linestring on the map'
                }), {
                    iconCls: 'drawline', 
                    toggleGroup: 'map'
                }
            );
            
            toolbar.addControl(
                new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
                    title: 'Draw a polygon on the map'
                }), {
                    iconCls: 'drawpolygon', 
                    toggleGroup: 'map'
                }
            );
            
            addSeparator(toolbar);
        
            var nav = new OpenLayers.Control.NavigationHistory();
            map.addControl(nav);
            nav.activate();
            
            toolbar.add(
                new Ext.Toolbar.Button({
                    iconCls: 'back',
                    tooltip: 'Previous view', 
                    handler: nav.previous.trigger
                })
            );
            
            toolbar.add(
                new Ext.Toolbar.Button({
                    iconCls: 'next',
                    tooltip: 'Next view', 
                    handler: nav.next.trigger
                })
            );
            
            addSeparator(toolbar);
            
            toolbar.activate();
        }
      // END TOOLBAR





