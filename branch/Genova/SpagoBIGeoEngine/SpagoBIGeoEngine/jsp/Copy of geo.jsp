<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPage"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*,
 				 it.eng.spago.configuration.ConfigSingleton,
                 java.util.Map,
                 java.util.HashMap,
                 java.util.List,
                 java.util.Properties,
                 java.util.Enumeration,
                 java.util.ArrayList,
                 it.eng.spagobi.geo.configuration.*,
                 it.eng.spagobi.geo.map.utils.*,
                 it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils,
                 it.eng.spago.tracing.TracerSingleton,
                 it.eng.spagobi.geo.action.*" %>
<%

	RequestContainer requestContainer = null;
	ResponseContainer responseContainer = null;
	SessionContainer sessionContainer = null;	
	
	requestContainer = RequestContainerAccess.getRequestContainer(request);
	responseContainer = ResponseContainerAccess.getResponseContainer(request);	
	
	SourceBean serviceRequest = requestContainer.getServiceRequest();
	SourceBean serviceResponse = responseContainer.getServiceResponse();
	
	MapConfiguration mapConfiguration = null;
	MapCatalogueAccessUtils mapCatalogueAccessUtils = null;
	String template = null;
	String target_level = "0";
	String[] selectedLayers = null;
	String baseUrl = null;
	String actionUrl = null;
	String isSubObject = null;
	
	
	
	template = (String)serviceResponse.getAttribute("template");
	target_level = (String)serviceResponse.getAttribute("target_level");
	mapConfiguration = (MapConfiguration)serviceResponse.getAttribute(GeoAction.OUTPUT_PAR_MAP_CONFIGURATION);
	selectedLayers = (String[])serviceResponse.getAttribute(GeoAction.OUTPUT_PAR_SELECTED_LAYERS);
	baseUrl = (String)serviceResponse.getAttribute(GeoAction.OUTPUT_PAR_BASE_URL);
	isSubObject = (String)serviceResponse.getAttribute(GeoAction.OUTPUT_PAR_SO_FLAG);
	mapCatalogueAccessUtils = MapConfiguration.getMapCatalogueAccessUtils();	
	actionUrl = baseUrl + "/servlet/AdapterHTTP?ACTION_NAME=MAP_DRAW_ACTION";
	
	
	List hierachies = mapConfiguration.getDatamartProviderConfiguration().getHierarchies();
	DatamartProviderConfiguration.Hierarchy selectedHierarchy = mapConfiguration.getDatamartProviderConfiguration().getSelectedHierarchy();
	List selectedHierarchyLevels =  selectedHierarchy.getLevels();
	DatamartProviderConfiguration.Hierarchy.Level selectedHierarchyAggregationLevel  = mapConfiguration.getDatamartProviderConfiguration().getSelectedLevel();
	
	
	String hierarchiesInitScript = "";
	String selectedHierarchyInitScript = "";
	
	String hierarchyLevelsInitScript = "";
	String hierarchyFeaturesInitScript = "";
	
	String selectedLevelInitScript = "";	
	String selectedFeatureInitScript = "";
	
	String mapsInitScript = "";
	String selectedMapInitScript = "";
	
	String layersInitScript = "";
	String selectedLayersMapInitScript = "";
	String selectedLayersInitScript = "";
	
	// --- HIERACHY ----------------------------------------------------------------------------------------------
	hierarchiesInitScript = "hierarchies = [";
	for(int i = 0; i < hierachies.size(); i++) {
		DatamartProviderConfiguration.Hierarchy hierarchy = (DatamartProviderConfiguration.Hierarchy)hierachies.get(i);
		hierarchiesInitScript += ((i>0?",":"") + "\"" + hierarchy.getName()) + "\"";
	}
	hierarchiesInitScript += "];";
	selectedHierarchyInitScript = "selectedHierarchy = \"" + selectedHierarchy.getName() + "\";";
	
	// --- HIERACHY LEVELS----------------------------------------------------------------------------------------------
	for(int j = 0; j < hierachies.size(); j++) {
		DatamartProviderConfiguration.Hierarchy hierarchy = (DatamartProviderConfiguration.Hierarchy)hierachies.get(j);
		hierarchyLevelsInitScript += "hierarchyLevels[\"" + hierarchy.getName() + "\"] = new Array();\n";
		hierarchyFeaturesInitScript += "hierarchyFeatures[\"" + hierarchy.getName() + "\"] = new Array();\n";
		List levels = hierarchy.getLevels();
		for(int i = 0; i < levels.size(); i++) {
			DatamartProviderConfiguration.Hierarchy.Level level = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
			hierarchyLevelsInitScript += "hierarchyLevels[\"" + hierarchy.getName() + "\"][" + i + "] = \"" + level.getName()  + "\";\n";
			hierarchyFeaturesInitScript += "hierarchyFeatures[\"" + hierarchy.getName() + "\"][" + i + "] = \"" + level.getFeatureName()  + "\";\n";
		}
	}
	selectedLevelInitScript = "selectedLevel = \"" + selectedHierarchyAggregationLevel.getName() + "\";";
	selectedFeatureInitScript = "selectedFeature = \"" + selectedHierarchyAggregationLevel.getFeatureName() + "\";";
	
	//	 --- MAPS ----------------------------------------------------------------------------------------------
	
	MapCatalogueAccessUtils mapCatalogueClient = MapConfiguration.getMapCatalogueAccessUtils();
	List maps = mapCatalogueClient.getMapNamesByFeature(selectedHierarchyAggregationLevel.getFeatureName());
	mapsInitScript = "maps = [";
	for(int i = 0; i < maps.size(); i++) {
		String map = (String)maps.get(i);
		mapsInitScript += ((i>0?",":"") + "\"" + map + "\"");
	}
	mapsInitScript += "];";
	String mapName = mapConfiguration.getMapProviderConfiguration().getMapName();
	if(mapName == null) mapName = (String)maps.get(0);
	selectedMapInitScript = "selectedMap = \"" + mapName + "\";";
	
	//	 --- LAYERS ----------------------------------------------------------------------------------------------
	
	List layers = mapCatalogueClient.getFeatureNamesInMap(mapName);
	layers.add("grafici");
	layers.add("valori");
	layersInitScript = "layers = [";
	selectedLayersInitScript = "selectedLayers = \"";
	for(int i = 0; i < layers.size(); i++) {
		String layerName = (String)layers.get(i);
		layersInitScript += ((i>0?",":"") + "\"" + layerName + "\"");
		
		MapRendererConfiguration.Layer layer = mapConfiguration.getMapRendererConfiguration().getLayer(layerName);
		if(layer != null && layer.isSelected()) {
			selectedLayersMapInitScript +=  "selectedLayersMap[\""+ layerName +"\"] = " + "true" + ";\n";
			selectedLayersInitScript += ((i>0?";":"") + "" + layerName + "");
		} else {
			selectedLayersMapInitScript +=  "selectedLayersMap[\""+ layerName +"\"] = " + "false" + ";\n";
		}
	}
	layersInitScript += "];";
	selectedLayersInitScript += "\";";
	
	
	System.out.println(hierarchiesInitScript);
	System.out.println(selectedHierarchyInitScript);
	System.out.println(hierarchyLevelsInitScript);
	System.out.println(hierarchyFeaturesInitScript);
	System.out.println(selectedLevelInitScript);
	System.out.println(selectedFeatureInitScript);	
	System.out.println(mapsInitScript);	
	System.out.println(selectedMapInitScript);	
	
	System.out.println(layersInitScript);	
	System.out.println(selectedLayersMapInitScript);	
	System.out.println(selectedLayersInitScript);	
%>

<head>
	<%-- Necessary for window Javascript library --%>
	<script type="text/javascript" src='../js/ajax/prototype.js'></script>
  	<script type="text/javascript" src='../js/ajax/effects.js'></script>
  	<script type="text/javascript" src='../js/ajax/window.js'></script>
  	<script type="text/javascript" src='../js/ajax/debug.js'></script>
  	<script type="text/javascript" src='../js/ajax/application.js"'/></script>
  	
  	<link rel="stylesheet" href ="../themes/default.css" type="text/css"/>	 
  	<link rel="stylesheet" href ="../themes/alert.css" type="text/css"/>	 
  	<link rel="stylesheet" href ="../themes/alert_lite.css" type="text/css" />
  	<link rel="stylesheet" href ="../themes/mac_os_x.css" type="text/css"/>	
  	<link rel="stylesheet" href ="../themes/debug.css" type="text/css"/>
  	
  	<style type="text/css"> 
	div.panel {
		background: #fffce6;
		
		border-color:dimgray;
		border-style:solid;
		border-width:1px;
	}
	
	div.panelTitle {
		background:steelblue;
		color:white;
		
		border-color:dimgray;
		border-style:solid;
		border-width:1px;
		
		font-family:Arial,Helvetica;
		font-size:14px;
		font-style: bold;		
			
		padding: 1px 3px 1px 3px
	}
	
	div.panelBody {
		font-size:13px;
		font-style: normal;	
		
		padding: 5px 5px 3px 5px
	}
	
	</style>
  	
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init();">





<script>

var hierarchies = new Array();
var selectedHierarchy = null;

var hierarchyLevels = new Array();
var hierarchyFeatures = new Array();

var selectedLevel = null;
var selectedFeature = null;

var maps = new Array();
var selectedMap = null;

var layers = new Array();
var selectedLayersMap = new Array();
var selectedLayers = "";


<%=hierarchiesInitScript%>
<%=selectedHierarchyInitScript%>	

<%=hierarchyLevelsInitScript%>
<%=hierarchyFeaturesInitScript%>

<%=selectedLevelInitScript%>
<%=selectedFeatureInitScript%>

<%=mapsInitScript%>
<%=selectedMapInitScript%>

<%=layersInitScript%>
<%=selectedLayersMapInitScript%>
<%=selectedLayersInitScript%>





// ==== BASE FUNCS ========================================================================================= 

	function init() {
		var alertOnClose = { onClose: function(eventName, win) { windowCloseHandler(eventName, win);} }
		Windows.addObserver(alertOnClose);
	}
	
	function draw() {
		drawHierarchiesBlock();
		drawHierarchyLevelsBlock();
		drawMapsBlock();
		drawLayersBlock();
	}

	function createRadio(name, value, selected, callback_script) {
    	var radio;
      
      	if(document.all && !window.opera && document.createElement) {
      		if(selected) {
            	radio = document.createElement("<input type='radio' id='" + name + "' name='" + name + "' value='" + value + "' onClick='" + callback_script + "' checked>");
            } else {
            	radio = document.createElement("<input type='radio' id='" + name + "' name='" + name + "' value='" + value + "' onClick='" + callback_script + "'>");
            }
      	} else {
        	radio = document.createElement("input");
            radio.setAttribute("type", "radio");
            radio.setAttribute("id", name);
            radio.setAttribute("name", name);
            radio.setAttribute("value", value);
            radio.setAttribute("onClick",callback_script);
            if(selected) radio.setAttribute("checked", "true");
      	}
      	
      	
      	return radio;
    }
    
    function createCheckBox(name, value, selected, callback_script) {
      var checkBox;
      
      if(document.all && !window.opera && document.createElement) {
      		  if(selected) {
              	checkBox = document.createElement("<input type='checkbox' id='" + name + "' name='" + name + "' value='" + value + "' onClick='" + callback_script +"' checked>");
              } else {
              	checkBox = document.createElement("<input type='checkbox' id='" + name + "' name='" + name + "' value='" + value + "' onClick='" + callback_script +"' >");
              }
      } else {
             checkBox = document.createElement("input");
	         checkBox.setAttribute("type", "checkbox");
	         checkBox.setAttribute("name", name);
	         checkBox.setAttribute("value",value);	 
	         checkBox.setAttribute("onClick", callback_script);
	         if(selected) checkBox.setAttribute("checked", "true");
      }
      return checkBox;
    }
    
// ==== AJAX FUNCS ========================================================================================== 

	var pendingAjaxCall = false;
	
	function getMapsByFeature(featureName) {
	  	var url = '../servlet/AdapterHTTP';
		var pars = 'ACTION_NAME=GET_MAPS_ACTION&featureName=' + featureName;
		
		var ajaxRequest = new Ajax.Request(url, 
									       { method: 'post', 
											 parameters: pars, 
											 onComplete: getMapsByFeatureCallback });
		pendingAjaxCall = true;
  	}
  
	function getMapsByFeatureCallback(req) {
		maps = new Array();
  		var childs = req.responseXML.getElementsByTagName("MAPS")[0].childNodes;
  		for (i = 0; i< childs.length; i++) {  		
	  		var value = childs[i].childNodes[0].nodeValue;
	  		maps[i] = value;
  		}
  		
  		// set also base map using a simple heuristic
		var preservePreviousMap = false;
		for(i = 0; i < maps.length; i++) {
			if(maps[i] == selectedMap) {
				preservePreviousMap = true;
				break;
			}
		}		
		if(!preservePreviousMap) selectedMap = maps[0];
		
		updateMaps();
  		  		
  		pendingAjaxCall = false;
  		draw();
  	}
  	
  	function getLayersInMap(mapName) {
  		var url = '../servlet/AdapterHTTP';
		var pars = 'ACTION_NAME=GET_LAYERS_ACTION&mapName=' + mapName;
		
		var ajaxRequest = new Ajax.Request(url, 
									       { method: 'post', 
											 parameters: pars, 
											 onComplete: getLayersInMapCallback });
		pendingAjaxCall = true;
  	}
  	
  	function getLayersInMapCallback(req) {
  		layers = new Array();
  		var newSelectedLayersMap = new Array();
  		
  		var childs = req.responseXML.getElementsByTagName("LAYERS")[0].childNodes;
  		for (i = 0; i< childs.length; i++) {  		
  			var value = childs[i].childNodes[0].nodeValue;
  			layers[i] = value;
  			newSelectedLayersMap[layers[i]] = selectedLayersMap[layers[i]];
  			if(layers[i] == selectedLevel) newSelectedLayersMap[layers[i]] = true;
  		}
  		
  		selectedLayersMap = newSelectedLayersMap;
  		
  		pendingAjaxCall = false;
  		draw();
  	}

	
	function windowCloseHandler(eventName, win){
	  	if(win.getId() == 'analysisDetailsWin') {
	  		var mapIFrame = document.getElementById("mapIFrame");
			mapIFrame.style.display = 'inline';
	  	}  	
  	}
	
	function saveAnalysis() {
		var mapIFrame = document.getElementById("mapIFrame");
		mapIFrame.style.display = 'none';
	  	var url = "http://www.spagobi.org";
	  	url = "../servlet/AdapterHTTP?ACTION_NAME=SHOW_ANALYSIS_DETAILS_ACTION";
	  	url += "&selected_hierachy=" + selectedHierarchy;
	  	url += "&selected_hierarchy_level=" + selectedLevel;
	  	url += "&selected_map=" + selectedMap;
	  	url += "&selected_layers=" + selectedLayers;
	  	
	    var hierarchyWin = new Window("analysisDetailsWin", {className: "dialog", title: "Save Analysis", 
	                                              top:70, left:100, width:850, height:400, 
	                                              resizable: true, url: url })
		hierarchyWin.setDestroyOnClose();
		hierarchyWin.show(true); 						
  	}
// ==== HIERARCHIES ========================================================================================= 

	function drawHierarchiesBlock() {
		
		var hierarchyNameOpt = document.getElementById("hierarchyName");       
	    child = hierarchyNameOpt.firstChild;				      
		while(child) {
	        var nextChild = child.nextSibling;
			hierarchyNameOpt.removeChild(child);
			child = nextChild;
		}
	     
	    for(i = 0; i < hierarchies.length; i++) {
	    	var option = document.createElement("option");
	        option.setAttribute("id",hierarchies[i]);
	        option.setAttribute("value",hierarchies[i]);
	        if(hierarchies[i] == selectedHierarchy) {
	        	option.setAttribute("selected", "true");
	        }
	        var label = document.createTextNode(hierarchies[i]);
	        option.appendChild(label);
	        hierarchyNameOpt.appendChild(option);
	    }   
	}
	
	function updateHierarchySelection() {		
		var hierarchyNameOpt = document.getElementById("hierarchyName"); 
		var selectedValue = hierarchyNameOpt.options[hierarchyNameOpt.selectedIndex].value;
		selectedHierarchy = selectedValue;	
		
		updateHierarchy();
	}
	
	function updateHierarchy() {
			
		// set also base hierarchy level using a simple heuristic
		var preservePreviousLevel = false;
		for(i = 0; i < hierarchyLevels[selectedHierarchy].length; i++) {
			if(hierarchyLevels[selectedHierarchy][i] == selectedLevel) {
				preservePreviousLevel = true;
				break;
			}
		}		
		if(!preservePreviousLevel) {
			selectedLevel = hierarchyLevels[selectedHierarchy][0];
		}
		
		updateHierarchyLevel();
	}


// ==== HIERARCHY LEVELS ========================================================================================= 

	function drawHierarchyLevelsBlock() {
	
		var hierarchyLevelRadio = document.getElementById("hierarchyLevelRadio");     
		child = hierarchyLevelRadio.firstChild;    	      
		while(child) {
			var nextChild = child.nextSibling;
			hierarchyLevelRadio.removeChild(child);
			child = nextChild;
		}     
		
		
		for(i = 0; i < hierarchyLevels[selectedHierarchy].length; i++) {
			var selected = false;
		    if(hierarchyLevels[selectedHierarchy][i] == selectedLevel) {
		    	selected = true;
		    }
		    var radio = createRadio("level", hierarchyLevels[selectedHierarchy][i], selected, "updateHierarchyLevelSelection(this);draw();");
		    var label = document.createTextNode(" " + hierarchyLevels[selectedHierarchy][i]);
		    var lf = document.createElement("br");
		    hierarchyLevelRadio.appendChild(radio);
		    hierarchyLevelRadio.appendChild(label);
		    hierarchyLevelRadio.appendChild(lf);        
		}  
	}
	
	function updateHierarchyLevelSelection(obj) {				

		selectedLevel = obj.value;
		updateHierarchyLevel();
	}
	
	function updateHierarchyLevel() {				
		
		selectedHierarchyLevelIndex = -1;
		for(i = 0; i < hierarchyLevels[selectedHierarchy].length; i++) {
			if(hierarchyLevels[selectedHierarchy][i] == selectedLevel) selectedHierarchyLevelIndex = i;
		}
		if(selectedHierarchyLevelIndex == -1) alert("Error: " + selectedLevel + "is not a valid hierarchy level name !")
		
		selectedFeature = hierarchyFeatures[selectedHierarchy][selectedHierarchyLevelIndex];
		
		getMapsByFeature(selectedFeature);
	}
	
	
// ==== MAPS ========================================================================================= 

	function drawMapsBlock() {
	    var mapRadio = document.getElementById("mapRadio");      
	    child = mapRadio.firstChild;		  		      
		while(child) {
	        var nextChild = child.nextSibling;
			mapRadio.removeChild(child);
			child = nextChild;
		} 
	    for(i = 0; i < maps.length; i++) {
	    	var selected = false;
	    	if(selectedMap != null && maps[i] == selectedMap) {
	    		selected = true;
	    	}
	        var radio = createRadio("map", maps[i], selected, "updateMapsSelection(this);draw();");	        
	        var label = document.createTextNode(" " + maps[i]);
	        var lf = document.createElement("br");
	        mapRadio.appendChild(radio);
	        mapRadio.appendChild(label);
	        mapRadio.appendChild(lf);        
	    }      
	}
	
	function updateMapsSelection(obj) {
		selectedMap = obj.value;
		updateMaps();
	}
	
	function updateMaps() {
		getLayersInMap(selectedMap);
	}
	
	
	
// ==== MAPS ========================================================================================= 

	function drawLayersBlock() {
		var layerCheckBox = document.getElementById("layerCheckBox");      
	    child = layerCheckBox.firstChild;			      
		while(child) {
	        var nextChild = child.nextSibling;
			layerCheckBox.removeChild(child);
			child = nextChild;
		}     
		
	    for(i = 0; i < layers.length; i++) {
	         var selected = false;
	         if(selectedLayersMap != null) {
	         	selected = selectedLayersMap[layers[i]];
	         } else if(layers[i] == selectedFeature) {
	    	 	selected = true;
	    	 }
	    	 
	         var checkBox = createCheckBox("layer", layers[i], selected, "updateLayersSelection();draw();");	         
	         var label = document.createTextNode(" " + layers[i]);
	         var lf = document.createElement("br");
	         layerCheckBox.appendChild(checkBox);
	         layerCheckBox.appendChild(label);
	         layerCheckBox.appendChild(lf);       
	    }      
	}
	
	function updateLayersSelection(obj) {
		var selectedValue = "";
		selectedLayersMap = new Array();
		
		var layerCheckBox = document.getElementById("layerCheckBox");     
	    child = layerCheckBox.firstChild;    	      
		while(child) {
	        var nextChild = child.nextSibling;
			if(child != null && child.name == "layer" && child.checked) {
				selectedValue += child.value + ";";
				selectedLayersMap[child.value] = true;
			} else {
				selectedLayersMap[child.value] = false;
			}
			
			child = nextChild;
		}    		
		selectedLayers = selectedValue;
	}

             
</script>






<!--  
<table height="100%" border="0px" valign="top" style="background-color:#e6e6e6;">
-->
<table height="100%" border="0px" valign="top" style="background-color:yellow;">

	

<tr valign="top">
	<td width="85%" valign="top"  style="background-color:green">
	<div id="mapPanel" valign="top">
		<iframe id="mapIFrame"
		        name="mapIFrame"
		        src=""
		        width="100%"
		        height="760px"
		        frameborder="3">
		</iframe>
		
		<form id="executionForm"
			  name="executionForm"
		      method="post"
		      action="<%=actionUrl%>"
		      target="mapIFrame">
		      <input type="hidden" name="type" value="object" />
		      <input type="hidden" name="template" value="<%=template%>" />
		      <input type="hidden" name="target_level" value="<%=target_level%>" />
		</form>	
		
		<%if(isSubObject != null){ %>
		
		<form id="subObjectExecutionForm"
			  name="subObjectExecutionForm"
		      method="post"
		      action="<%=actionUrl%>"
		      target="mapIFrame">
		      <input type="hidden" name="type" value="subobject" />
		      <input type="hidden" name="template" value="<%=template%>" />
		      <input type="hidden" name="target_level" value="<%=target_level%>" />
		      <input type="hidden" name="soHierarchyName" value="<%=mapConfiguration.getDatamartProviderConfiguration().getSelectedHierarchy().getName()%>" />
		      <input type="hidden" name="soLevel" value="<%=mapConfiguration.getDatamartProviderConfiguration().getSelectedLevel().getName()%>" />
		      <input type="hidden" name="soMap" value="<%=mapConfiguration.getMapName()%>" />
		      <%for(int i = 0; i < selectedLayers.length; i++) { %>
		      	<input type="hidden" name="soLayer" value="<%=selectedLayers[i]%>" />
		      <%} %>
		</form>	
		
		<%} %>
		
		
		
	</div>
	</td>

	<td width="15%">
		<div id="controlPanel">
			
			<form id="optionForm" 
			  name="optionForm"
		      method="post"
		      action="<%=actionUrl%>"
		      target="mapIFrame">
		      
		      <input type="hidden" name="template" value="<%=template%>" />
		      <input type="hidden" name="target_level" value="<%=target_level%>" />
		      
			<table width="250px" height="100%" valign="top" border="0px">
				<tr valign="top"><td>
					
						
				<div class="panel" id="hierarchyNamePanel">
					
					<div class="panelTitle">Hierarchies</div>					
					
					<div class="panelBody">	
						<center>
						<select id="hierarchyName" name="hierarchyName" onChange="updateHierarchySelection();draw();">
							<option value="custom" selected>Custom</option>
							<option value="default">Default</option>
						</select>
						</center>
					</div>
					
				</div>
						
				<p>
					
				
				<div class="panel" id="hierarchyLevelPanel">
						
					<div class="panelTitle">Hierarchy Levels</div>
						
					<div class="panelBody">						
						<div id="hierarchyLevelRadio"> </div>	
					</div>
																						
				</div>
									
				<p>
					
				<div class="panel" id="mapPanel">
						
					<div class="panelTitle">Maps</div>
						
					<div class="panelBody">					
						<div id="mapRadio"> </div>	
					</div>
					
				</div>																
				
				<p>
				
				<div class="panel" id="layerPanel">
						
					<div class="panelTitle">Layers</div>
						
					<div class="panelBody">							
						<div id="layerCheckBox"> </div>	
					</div>		
								
				</div>		
				
				<p>
				
				<div class="panel" id="buttonPanel">
						
					<div class="panelTitle">Controls</div>	
					
					<div class="panelBody">		
						<center>	
						<input type="submit" value="Refresh Map"/>	
						</center>	
						<p>
						<center>	
						<input type="button" value="Save Map" onClick="saveAnalysis()"/>	
						</center>	
					</div>	
								
				</div>															
					
				</td></tr>
				
			</table>
			</form>
			
			
			
			
			
		</div>
	</td>
	
</tr>
</table>

	
      
<script>

		draw();
		
		<%if(isSubObject != null){ %>		
	
			var subObjectExecutionForm = document.getElementById('subObjectExecutionForm');
            subObjectExecutionForm.submit();
		
		
		<%} else { %>
              var executionForm = document.getElementById('executionForm');
             executionForm.submit();
        <%} %>
</script>

</body>

