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
                 java.util.ArrayList,it.eng.qbe.geo.configuration.*,
                 it.eng.spagobi.geo.map.utils.*" %>
<%

	RequestContainer requestContainer = null;
	ResponseContainer responseContainer = null;
	SessionContainer sessionContainer = null;
	
	MapConfiguration mapConfiguration = null;
	
	if (requestContainer == null) {
		requestContainer = RequestContainerAccess.getRequestContainer(request);
		responseContainer = ResponseContainerAccess.getResponseContainer(request);
	}
	
	
	SourceBean serviceRequest = requestContainer.getServiceRequest();
	SourceBean serviceResponse = responseContainer.getServiceResponse();
	
	String actionUrl = "http://localhost:8080/SpagoBIGeoEngine/servlet/AdapterHTTP?";
	actionUrl += "ACTION_NAME=MAP_DRAW_ACTION";
	String template = "";
	String target_level = "0";
	List attributes = serviceResponse.getContainedAttributes();
	for(int i = 0; i < attributes.size(); i++) {
		SourceBeanAttribute attributeSB = (SourceBeanAttribute)attributes.get(i);
		String name = attributeSB.getKey();
		Object value = attributeSB.getValue();
		if(name.equalsIgnoreCase("template")) template = value.toString();
		if(name.equalsIgnoreCase("target_level")) target_level = value.toString();
		if(name.equalsIgnoreCase("configuration")) mapConfiguration = (MapConfiguration)value;
	}	
	
	String hierarchiesNameArray = "";
	String selectedHiearchy = "";
	String hierarchyLevelsArray = "";
	List list;
	
	list = mapConfiguration.getDatamartProviderConfiguration().getHierarchies();
	hierarchiesNameArray = "var hierarchies = [";
	hierarchyLevelsArray = "var hierarchyLevels = new Array();\n";
	for(int i = 0; i < list.size(); i++) {
		DatamartProviderConfiguration.Hierarchy hierarchy = (DatamartProviderConfiguration.Hierarchy)list.get(i);
		hierarchiesNameArray += ((i>0?",":"") + "\"" + hierarchy.getName()) + "\"";
		hierarchyLevelsArray += "hierarchyLevels[\"" + hierarchy.getName() + "\"] = new Array();\n";
	}
	hierarchiesNameArray += "];";
	selectedHiearchy = "var selectedHiearchy = \"" + mapConfiguration.getDatamartProviderConfiguration().getSelectedHierarchy().getName() + "\"";
	
	
	String initHierarchyLevelsArrayScript = "";
	String selectedHierarchyLevel = "";
	String mapsArray = "var maps = new Array();\n";
	List features = new ArrayList();
	list = mapConfiguration.getDatamartProviderConfiguration().getHierarchies();
	for(int j = 0; j < list.size(); j++) {
		DatamartProviderConfiguration.Hierarchy hierarchy = (DatamartProviderConfiguration.Hierarchy)list.get(j);
		List levels = hierarchy.getLevels();
		for(int i = 0; i < levels.size(); i++) {
			DatamartProviderConfiguration.Hierarchy.Level level = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
			initHierarchyLevelsArrayScript += "hierarchyLevels[\"" + hierarchy.getName() + "\"][" + i + "] = \"" + level.getName()  + "\";";
			mapsArray += "maps[\"" + level.getFeatureName() + "\"] = new Array();\n";
			features.add(level.getFeatureName());
		}
	}
	selectedHierarchyLevel = "var selectedHierarchyLevel = \"" + mapConfiguration.getDatamartProviderConfiguration().getSelectedLevel().getName()  + "\";";
	String targetFeature = "var targetFeature = \"" + mapConfiguration.getDatamartProviderConfiguration().getSelectedLevel().getFeatureName() + "\"";
	
	

	String selectedMap = "";
	String initMapsArrayScript = "";
	String layersArray = "var layers = new Array();\n";
	List mapsList = new ArrayList();
	for(int j = 0; j < features.size(); j++ ) {
		String featureName = (String)features.get(j);
		String[] maps = MapCatalogueMock.getMapNamesByFeature(featureName);
		for(int i = 0; i < maps.length; i++) {
			initMapsArrayScript += "maps[\"" + featureName + "\"][" + i + "] = \"" + maps[i]  + "\";\n"; 
			layersArray += "layers[\"" + maps[i] + "\"] = new Array();\n";
			mapsList.add(maps[i]);
		}
	}
	DatamartProviderConfiguration.Hierarchy.Level level = mapConfiguration.getDatamartProviderConfiguration().getSelectedLevel();
	String[] maps = MapCatalogueMock.getMapNamesByFeature(level.getName());
	selectedMap = "var selectedMap = \"" + maps[0] + "\";";
	
	
	
	
	String selectedLayer = "";
	String initLayersArrayScript = "";
	for(int j = 0; j < mapsList.size(); j++){
		String mapName = (String)mapsList.get(j);
		String[] layers = MapCatalogueMock.getFeatureNamesInMap(mapName);
		for(int i = 0; i < layers.length; i++) {
			initLayersArrayScript += "layers[\"" + mapName + "\"][" + i + "] = \"" + layers[i]  + "\";\n"; 
		}
		initLayersArrayScript += "layers[\"" + mapName + "\"][" + (layers.length + 0) + "] = \"grafici\";\n"; 
		initLayersArrayScript += "layers[\"" + mapName + "\"][" + (layers.length + 1) + "] = \"valori\";\n"; 
	}
	
	//selectedLayer = "var selectedLayer = \"" + layers[0] + "\";";
	
%>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="refresh();">


<script>

	<%=selectedHiearchy%>	
	<%=hierarchiesNameArray%>
	
	<%=selectedHierarchyLevel%>
	<%=hierarchyLevelsArray%> 
	<%=initHierarchyLevelsArrayScript%>
	<%=targetFeature%>
	
	<%=selectedMap%>
	<%=mapsArray%>
	<%=initMapsArrayScript%>
	
	<%=layersArray%> 
	var selectedLayers = null;
	var selectedLayersMap = null;
	<%=initLayersArrayScript%> 
	
	
	function updateHierarchySelection() {		
		var hierarchyNameOpt = document.getElementById("hierarchyName"); 
		var selectedValue = hierarchyNameOpt.options[hierarchyNameOpt.selectedIndex].value;
		selectedHiearchy = selectedValue;	
		selectedLayersMap = null;	
	}
	
	
	function updateRadioSelections(obj) {	
			
		var selectedValue;
		
		alert(obj.name)
		if(obj.name == "level") {
			selectedHierarchyLevel = obj.value;
			targetFeature = selectedHierarchyLevel;
			selectedMap = null;
			selectedLayersMap = null;
		} else if (obj.name == "map") {			
			selectedMap = obj.value;
			selectedLayersMap = null;
		}
		
		/*
		selectedValue = null;
		var hierarchyLevelRadio = document.getElementById("hierarchyLevelRadio");     
	    child = hierarchyLevelRadio.firstChild;    	      
		while(child) {
	        var nextChild = child.nextSibling;
			if(child != null && child.name == "level" && child.checked) {
				selectedValue = child.value;
			}
			
			child = nextChild;
		}    		
		
		
		selectedValue = null;
		var mapRadio = document.getElementById("mapRadio");   
	    child = mapRadio.firstChild;    	      
		while(child) {
	        var nextChild = child.nextSibling;
			if(child != null && child.name == "map" && child.checked) {
				selectedValue = child.value;
			}			
			child = nextChild;
		}    
		
		selectedMap = selectedValue;
		selectedLayersMap = null;
		*/
	}
	
	function updateRadioCheckBoxSelections() {	
			
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
	
	

 	function isIE() {
      navigatorname = navigator.appName;
      navigatorname = navigatorname.toLowerCase();
      return (navigatorname.indexOf('explorer') != -1);
    }

    function isMoz() {
      navigatorname = navigator.appName;
      navigatorname = navigatorname.toLowerCase();
      return (navigatorname.indexOf('explorer') == -1);
    }
    
    function createRadio(name, value, selected) {
      var radio;
      
      if(document.all && !window.opera && document.createElement) {
      		  if(selected) {
              	radio = document.createElement("<input type='radio' id='" + name + "' name='" + name + "' value='" + value + "' onClick='updateRadioSelections(this);refresh();' checked>");
              } else {
              	radio = document.createElement("<input type='radio' id='" + name + "' name='" + name + "' value='" + value + "' onClick='updateRadioSelections(this);refresh();' >");
              }
      } else {
              radio = document.createElement("input");
              radio.setAttribute("type", "radio");
              radio.setAttribute("id", name);
              radio.setAttribute("name", name);
              radio.setAttribute("value", value);
              radio.setAttribute("onClick","updateRadioSelections(this);refresh();");
              if(selected) radio.setAttribute("checked", "true");
      }
      return radio;
    }
    
    function createCheckBox(name, value, selected) {
      var checkBox;
      
      if(document.all && !window.opera && document.createElement) {
      		  if(selected) {
              	checkBox = document.createElement("<input type='checkbox' id='" + name + "' name='" + name + "' value='" + value + "' onClick='updateRadioCheckBoxSelections();refresh();' checked>");
              } else {
              	checkBox = document.createElement("<input type='checkbox' id='" + name + "' name='" + name + "' value='" + value + "' onClick='updateRadioCheckBoxSelections();refresh();' >");
              }
      } else {
             checkBox = document.createElement("input");
	         checkBox.setAttribute("type", "checkbox");
	         checkBox.setAttribute("name", name);
	         checkBox.setAttribute("value",value);	 
	         checkBox.setAttribute("onClick","updateRadioCheckBoxSelections();refresh();");
	         if(selected) checkBox.setAttribute("checked", "true");
      }
      return checkBox;
    }

	function refresh() {
	      
	   var child;
	    
	    // HIERARCHY NAME 
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
	        if(hierarchies[i] == selectedHiearchy) {
	        	option.setAttribute("selected", "true");
	        }
	        var label = document.createTextNode(hierarchies[i]);
	        option.appendChild(label);
	        hierarchyNameOpt.appendChild(option);
	    }      
	    
	    // HIERARCHY LEVEL 
	    var hierarchyLevelRadio = document.getElementById("hierarchyLevelRadio");     
	    child = hierarchyLevelRadio.firstChild;    	      
		while(child) {
	        var nextChild = child.nextSibling;
			hierarchyLevelRadio.removeChild(child);
			child = nextChild;
		}     
	    for(i = 0; i < hierarchyLevels[selectedHiearchy].length; i++) {
	    	var selected = false;
	    	if(hierarchyLevels[selectedHiearchy][i] == selectedHierarchyLevel) {
	    		selected = true;
	    	}
	        var radio = createRadio("level", hierarchyLevels[selectedHiearchy][i], selected);
	        var label = document.createTextNode(" " + hierarchyLevels[selectedHiearchy][i]);
	        var lf = document.createElement("br");
	        hierarchyLevelRadio.appendChild(radio);
	        hierarchyLevelRadio.appendChild(label);
	        hierarchyLevelRadio.appendChild(lf);        
	    }  
	        
	        
	    // MAP NAME 
	    var mapRadio = document.getElementById("mapRadio");      
	    child = mapRadio.firstChild;		  		      
		while(child) {
	        var nextChild = child.nextSibling;
			mapRadio.removeChild(child);
			child = nextChild;
		} 
	    for(i = 0; i < maps[targetFeature].length; i++) {
	    	var selected = false;
	    	alert(selectedMap);
	    	if(selectedMap == null && i==0) {
	    		selectedMap = maps[targetFeature][i];
	    		selected = true;
	    	} else if(maps[targetFeature][i] == selectedMap) {
	    		selected = true;
	    	}
	        var radio = createRadio("map", maps[targetFeature][i], selected);	        
	        var label = document.createTextNode(" " + maps[targetFeature][i]);
	        var lf = document.createElement("br");
	        mapRadio.appendChild(radio);
	        mapRadio.appendChild(label);
	        mapRadio.appendChild(lf);        
	    }      
	    
	  
	  	// LAYER NAME 
	    var layerCheckBox = document.getElementById("layerCheckBox");      
	    child = layerCheckBox.firstChild;			      
		while(child) {
	        var nextChild = child.nextSibling;
			layerCheckBox.removeChild(child);
			child = nextChild;
		}     
	    for(i = 0; i < layers[selectedMap].length; i++) {
	         var selected = false;
	         if(selectedLayersMap != null) {
	         	selected = selectedLayersMap[layers[selectedMap][i]];
	         } else if(layers[selectedMap][i] == targetFeature) {
	    	 	selected = true;
	    	 }
	         var checkBox = createCheckBox("layer", layers[selectedMap][i], selected);	         
	         var label = document.createTextNode(" " + layers[selectedMap][i]);
	         var lf = document.createElement("br");
	         layerCheckBox.appendChild(checkBox);
	         layerCheckBox.appendChild(label);
	         layerCheckBox.appendChild(lf);       
	    }      
	    
	}
             
</script>


<table height="100%" border="2px" valign="top">

<tr valign="top">
	<td width="85%" valign="top">
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
		      <input type="hidden" name="template" value="<%=template%>" />
		      <input type="hidden" name="target_level" value="<%=target_level%>" />
		</form>	
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
		      
			<table width="250px" height="100%" valign="top" border="1">
				<tr valign="top"><td>
					
						
				<div id="hierarchyNamePanel" 
					 style="border-width:1px; border-style:solid; padding: 10px 10px 10px 10px">
					
					<u>Hierarchies:</u>
					
					<p>
						
					<select id="hierarchyName" name="hierarchyName" onChange="updateHierarchySelection();refresh();">
						<option value="custom" selected>Custom</option>
						<option value="default">Default</option>
					</select>
				</div>
						
				<p>
					
				
				<div id="hierarchyLevelPanel"
				     style="border-width:1px; border-style:solid; padding: 10px 10px 10px 10px">
						
					<u>Hierarchy Levels:</u>
						
					<p>
					
					<div id="hierarchyLevelRadio"> </div>	
																						
				</div>
									
				<p>
					
				<div id="mapPanel" width="100%"
					 style="border-width:1px; border-style:solid; padding: 10px 10px 10px 10px">
						
					<u>Maps</u>
						
					<p>
					
					<div id="mapRadio"> </div>	
					
				</div>																
				
				<p>
				
				<div id="layerPanel" width="100%"
				     style="border-width:1px; border-style:solid; padding: 10px 10px 10px 10px">
						
					<u>Layers:</u>
						
					<p>					
						
					<div id="layerCheckBox"> </div>			
								
				</div>		
				
				<p>
				
				<div id="buttonPanel" width="100%"
				     style="border-width:1px; border-style:solid; padding: 10px 10px 10px 10px">
						
					<center>	
					<input type="submit" value="Refresh Map"/>	
					</center>	
								
				</div>															
					
				</td></tr>
				
			</table>
			</form>
			
			
		</div>
	</td>
	
</tr>
</table>
      
<script>
              var executionForm = document.getElementById('executionForm');
              executionForm.submit();
</script>

</body>

