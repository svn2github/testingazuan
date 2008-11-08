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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>

<head>

<%@ include file="../jsp/geo_base.jspf"%>
<script type="text/javascript" src='../js/service/geo.js'></script>
<link type="text/css" rel="stylesheet" href ="../css/geo.css"/>

</head>


<%	
	System.out.println("Andiamo avincere");
	boolean isDrillPanelVisible = true;
	String isSaveSubObjFuncActive = null;
	String actionUrl = null;
	String isDocumentCompositionModeActive = null;
	
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
	
	
	try {
	
		
	
	
	
	isSaveSubObjFuncActive = (String)serviceResponse.getAttribute("isSaveSubObjFuncActive");
	isDocumentCompositionModeActive = (String)serviceResponse.getAttribute("isDocumentCompositionModeActive");
	if(isDocumentCompositionModeActive != null && isDocumentCompositionModeActive.equalsIgnoreCase("TRUE")) {
		isDrillPanelVisible = false;
	}

	actionUrl = baseUrl + "/servlet/AdapterHTTP?ACTION_NAME=MAP_DRAW_ACTION";
	
	
	String[] hierachieNames = (String[])datamartProvider.getHierarchyNames().toArray(new String[0]);
	
	Hierarchy selectedHierarchy = datamartProvider.getSelectedHierarchy();
	List selectedHierarchyLevels =  selectedHierarchy.getLevels();
	Hierarchy.Level selectedHierarchyAggregationLevel  = datamartProvider.getSelectedLevel();
	
	
	
	
	// --- HIERACHY ----------------------------------------------------------------------------------------------
	hierarchiesInitScript = "hierarchies = [";
	for(int i = 0; i < hierachieNames.length; i++) {
		hierarchiesInitScript += ((i>0?",":"") + "\"" + hierachieNames[i]) + "\"";
	}
	hierarchiesInitScript += "];";
	selectedHierarchyInitScript = "selectedHierarchy = \"" + selectedHierarchy.getName() + "\";";
	
	// --- HIERACHY LEVELS----------------------------------------------------------------------------------------------
	for(int j = 0; j < hierachieNames.length; j++) {
		Hierarchy hierarchy = datamartProvider.getHierarchy(hierachieNames[j]);
		hierarchyLevelsInitScript += "hierarchyLevels[\"" + hierarchy.getName() + "\"] = new Array();\n";
		hierarchyFeaturesInitScript += "hierarchyFeatures[\"" + hierarchy.getName() + "\"] = new Array();\n";
		List levels = hierarchy.getLevels();
		for(int i = 0; i < levels.size(); i++) {
			Hierarchy.Level level = (Hierarchy.Level)levels.get(i);
			hierarchyLevelsInitScript += "hierarchyLevels[\"" + hierarchy.getName() + "\"][" + i + "] = \"" + level.getName()  + "\";\n";
			hierarchyFeaturesInitScript += "hierarchyFeatures[\"" + hierarchy.getName() + "\"][" + i + "] = \"" + level.getFeatureName()  + "\";\n";
		}
	}
	selectedLevelInitScript = "selectedLevel = \"" + selectedHierarchyAggregationLevel.getName() + "\";";
	selectedFeatureInitScript = "selectedFeature = \"" + selectedHierarchyAggregationLevel.getFeatureName() + "\";";
	
	//	 --- MAPS ----------------------------------------------------------------------------------------------
	
	List maps = null;
	maps = mapProvider.getMapNamesByFeature(selectedHierarchyAggregationLevel.getFeatureName());
	
	mapsInitScript = "maps = [";
	for(int i = 0; i < maps.size(); i++) {
		String map = (String)maps.get(i);
		mapsInitScript += ((i>0?",":"") + "\"" + map + "\"");
	}
	mapsInitScript += "];";
	String mapName = mapProvider.getSelectedMapName();
	if(mapName == null) mapName = (String)maps.get(0);
	selectedMapInitScript = "selectedMap = \"" + mapName + "\";";
	
	//	 --- LAYERS ----------------------------------------------------------------------------------------------
	
	List layers = mapProvider.getFeatureNamesInMap(mapName);
	layers.add("grafici");
	layers.add("valori");
	layersInitScript = "layers = [";
	selectedLayersInitScript = "selectedLayers = \"";
	int selectedLayersNum = 0;
	for(int i = 0; i < layers.size(); i++) {
		String layerName = (String)layers.get(i);
		layersInitScript += ((i>0?",":"") + "\"" + layerName + "\"");
		
		Layer layer = mapRenderer.getLayer(layerName);
		if(layer != null && layer.isSelected()) {
			selectedLayersMapInitScript +=  "selectedLayersMap[\""+ layerName +"\"] = " + "true" + ";\n";
			selectedLayersInitScript += ((i>0?";":"") + "" + layerName + "");
		} else {
			selectedLayersMapInitScript +=  "selectedLayersMap[\""+ layerName +"\"] = " + "false" + ";\n";
		}
	}
	layersInitScript += "];";
	selectedLayersInitScript += "\";";
	} catch (Exception e) {
		e.printStackTrace();
	}
%>

	
  	
  	
  	

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init()">



<script>

<%="actionUrl = '" + actionUrl + "';"%>

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
</script>







<table  height="100%" border="0px" valign="top" style="background-color:#e6e6e6;">

<tr valign="top">
	
	<td width="<%=isDrillPanelVisible?"85%":"100%"%>" valign="top">
		<div id="mapPanel" valign="top" height="100%">
			<iframe id="mapIFrame"
			        name="mapIFrame"
			        src=""
			        width="100%"
			        height="100%"
			        frameborder="3"
			        style="background-color:white;">
			</iframe>
			
			
			<form id="executionForm"
				  name="executionForm"
			      method="post"
			      action="<%=actionUrl%>"
			      target="mapIFrame">
			      <input type="hidden" name="type" value="object" />
			      <input type="hidden" name="target_level" value="" />
			</form>	
			
				
		</div>
	</td>

	<%if(isDrillPanelVisible) {%>
	
	<td width="15%">
		<%@include file="../jsp/controlPanel.jspf"%>
	</td>
	<%} %>	
	
</tr>
</table>

	
      
<script>
		<%if(isDrillPanelVisible) {%>
			draw();	
		<%} %>	
		
		
		var clientWidth = document.getElementById("mapPanel").clientWidth;		
		var offsetHeight = document.getElementById("mapPanel").offsetHeight;
		var offsetHeightParent = document.getElementById("mapPanel").parentNode.offsetHeight;
		//alert(offsetHeightParent);
		
		var iframe = document.getElementById('mapIFrame');
		iframe.height = '615';		
		
		var executionForm = document.getElementById('executionForm');
        executionForm.submit();
</script>

</body>

