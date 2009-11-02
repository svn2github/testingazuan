var viewport;

var DashboardsMap = {

	onPopupClose: function(evt) {
        selectControl.unselect(selectedFeature);    
    },         

	onFeatureUnselect: function (feature) {
        map.removePopup(feature.popup);
        feature.popup.destroy();
        feature.popup = null;
        var infoPanel = Ext.getCmp('infotable');
        if(infoPanel.body){
        	infoPanel.body.dom.innerHTML = '';
        }
    },
  
 	infotab: function() { 
 		Ext.getCmp('tab').setActiveTab('infotable');
 	}   
};    

var indic = new Array(this.map.indicators.length);

for(var i= 0, len = indic.length; i < len; i++){
	indic[i] = this.map.indicators[i];
} 

var url = this.map.url;
var analysis = this.map.analysis;
var layername = this.map.layer;
var mpname = this.map.mapname;
var server = this.map.geojsonUrl;
var document1Label = this.map.document1Label;
var listeners = this.map.listeners;
var parameters = this.map.parameters;
var mapwidth = this.map.width;
var mapheight = this.map.height;

var feautreInfo = this.map.feautreInfo;

var getInfo = function(feature) {
	var info = "";
    for(var i=0; i<feautreInfo.length; i++){
    	info = info+"<b>"+ feautreInfo[i][0] +"</b>: " + feature.attributes[feautreInfo[i][1]] + "<br />";    
    } 
    return info;
};

var document2Label = this.map.document2Label;
var document2Listeners = this.map.document2Listeners;
var document2Parameters = this.map.document2Parameters;
var lon = this.map.initPosLon;
var lat = this.map.initPosLat;
var zoom = this.map.initZoom;
var layer_name = this.map.layer_name;
var extwindow = this.map.window;
var businessId = this.map.businessId;
var label = this.map.spagobiDataset;
var geoid = this.map.geoId;
var role = this.map.role;
var dispToolbar1 = this.map.dispToolbar1;
var dispSlide1 = this.map.dispSlide1;
var dispToolbar2 = this.map.dispToolbar2;
var dispSlide2 = this.map.dispSlide2;

execDoc = function(docLab, role, params, dispToolbar, dispSlide,frameId) {
	/*
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
	var html = '<h1>Prova provata ' + docLab + ' </h1>'
    return html;
};
