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

<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>

<%@ page import="java.util.*"%>
<%@ page import="it.eng.spago.base.*"%>
<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.urlgenerator.*"%>
<%@ page import="it.eng.qbe.wizard.*"%>
<%@ page import="it.eng.qbe.model.*"%>
<%@ page import="it.eng.qbe.geo.configuration.*"%>
<%@ page import="it.eng.qbe.utility.*"%>
<%@page  import="it.eng.qbe.conf.*"%>







<% 
	RequestContainer requestContainer = null;
	ResponseContainer responseContainer = null;
	SessionContainer sessionContainer = null;   
	
	requestContainer = RequestContainerAccess.getRequestContainer(request);
	responseContainer = ResponseContainerAccess.getResponseContainer(request);
	sessionContainer = requestContainer.getSessionContainer();
	
	IQbeUrlGenerator qbeUrl = new WebQbeUrlGenerator();	

	Object spagoBiInfo = sessionContainer.getAttribute("spagobi"); 
	ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(sessionContainer);
   	DataMartModel dm = (DataMartModel) sessionContainer.getAttribute("dataMartModel");
   	MapConfiguration mapConfiguration = (MapConfiguration) sessionContainer.getAttribute("MAP_CONFIGURATION");
   	
   	DatamartProviderConfiguration.Hierarchy hierarchy;
   	hierarchy =	(DatamartProviderConfiguration.Hierarchy)sessionContainer.getAttribute("HIERARCHY");
   	String hierarchyName = hierarchy.getName();
   	
	List queryColumns = (List)requestContainer.getAttribute("QUERY_COLUMNS");
	String initQueryColumns = "";
	if(queryColumns == null) {
		initQueryColumns += "queryColumns = (parent.isQbeQuerySelected? parent.qbeQueryColumns: parent.expertQueryColumns);";
	} else {
		for(int i = 0; i < queryColumns.size(); i++) {
			String columnName = (String)queryColumns.get(i);
			initQueryColumns += "queryColumns[" + i + "] = '" + columnName + "';" ;
		}		
	}
	
	List levels = hierarchy.getLevels();
	DatamartProviderConfiguration.Hierarchy.Level level;
	String initLevelNames = "";
	String initSelectedLevelColumns = "";
	String initSelectedFeatureNames = "";
	if(levels == null || levels.size() == 0) {
		
	} else {
		for(int i = 0; i < levels.size(); i++){
			level = (DatamartProviderConfiguration.Hierarchy.Level)levels.get(i);
			
			String levelName = level.getName();
			initLevelNames += "levelNames[" + i + "] = '" + levelName + "';" ;
			
			String levelColumn = level.getColumnId();
			initSelectedLevelColumns += "selectedLevelColumns[" + i + "] = '" + levelColumn + "';" ;
			
			String featureName = level.getFeatureName();
			initSelectedFeatureNames += "selectedFeatureNames[" + i + "] = '" + featureName + "';" ;
		}
	}
	
	List features = (List)responseContainer.getServiceResponse().getAttribute("FEATURES");
	String initFeatureNames = "";
	if(features == null || features.size() == 0) {
		
	} else {
		for(int i = 0; i < features.size(); i++){
			String fetureName = (String)features.get(i);
			initSelectedFeatureNames += "featureNames[" + i + "] = '" + fetureName + "';" ;
		}
	}
	
	
%>



<html>

<head>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/spagobi.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/jsr168.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="../../../css/external.css" type="text/css"/>
	
	<%-- Necessary for window Javascript library --%>
	<script type="text/javascript" src='../../../js/prototype.js'></script>
  	<script type="text/javascript" src='../../../js/effects.js'></script>
  	<script type="text/javascript" src='../../../js/window.js'></script>
  	<script type="text/javascript" src='../../../js/debug.js'></script>
  	<script type="text/javascript" src='../../../js/application.js'/></script>
  	
   
  	<link rel="stylesheet" href ="../../../themes/default.css" type="text/css"/>	 
  	<link rel="stylesheet" href ="../../../themes/alert.css" type="text/css"/>	 
  	<link rel="stylesheet" href ="../../../themes/alert_lite.css" type="text/css" />
  	<link rel="stylesheet" href ="../../../themes/mac_os_x.css" type="text/css"/>	
  	<link rel="stylesheet" href ="../../../themes/debug.css" type="text/css"/>
</head>

<body>



<!-- ============================================================================================================== -->

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			Hierarchy Details
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
					
				
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='save' value='true' class='header-button-image-portlet-section'
				   src='<%=qbeUrl.conformStaticResourceLink(request,"../img/save.png")%>'
      			   title='Save' 
      			   onclick='submit("SAVE_HIERARCHY_ACTION")'/> 
			
		</td>
		
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='saveAndGoBack' value='true' class='header-button-image-portlet-section'
				   src='<%=qbeUrl.conformStaticResourceLink(request,"../img/saveAndGoBack.png")%>'
      			   title='Save and return' 
      			   onclick='submit("SAVE_HIERARCHY_AND_EXIT_ACTION")'/> 
		</td>
		
		<td class='header-button-column-portlet-section'>
			<input type='image' name='Exit' id='Exit' value='true' class='header-button-image-portlet-section'
				   src='<%=qbeUrl.conformStaticResourceLink(request,"../img/back.png")%>'
      			   title='Exit' 
      			   onclick='submit("EXIT_HIERARCHY_DETAILS_ACTION")'/> 
		</td>
	</tr>
</table>

<div class='div_background_no_img'>

<form method='POST' action='' 
	  id='mainForm' name='mainForm'>

<input type="hidden" id="levelNames" name="levelNames" value="A"/>
<input type="hidden" id="columnNames" name="columnNames" value="B">
<input type="hidden" id="featureNames" name="featureNames" value="C">
	  
<table width="100%" cellspacing="0" border="0" id = "newHierarchyBox" >
	<tr>
		<td>
			<div class="div_detail_area_forms">
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Name
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="hierarchyName" id="hierarchyName" value="<%=hierarchyName%>" maxlength="20"/>
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Type
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="type" id="type" value="Custom" maxlength="20" readonly disabled/>
				</div>
			</div>
		</td>
	</tr>
</table>

<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='titlebar_level_2_text_section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;Levels
		</td>
	</tr>
</table>

<p>

<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
	<tr>
		<td>
			<div class="div_detail_area_forms">
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Name
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field'  type="text" style='width:230px;' 
						name="name" id="levelNameTxt" value="" maxlength="20"/>
				</div>
							
				<div class='div_detail_label_lov'>
					&nbsp;
				</div>
				<div class='div_detail_form'>
					<a href="#" onClick='addLevelButtonClicked()' class='portlet-form-field-label' style='text-decoration:none;'>
						<img src='/spagobi/img/attach.gif' title='Add' alt='Add'/>
						Add
					</a>
				</div>
											
			</div>
		</td>
	</tr>
</table>

<div id='levelDiv' style="width:750px;margin:10px 0px 0px 0px;border:solid; border-width:0px"></div>

</form>

<script type="text/javascript">
	
	var queryColumns = new Array();
	<%=initQueryColumns%>
	
	var levelNames = new Array();
	<%=initLevelNames%>
	
	//var levelColumns = new Array();
	var featureNames = new Array();
	<%=initFeatureNames%>
	
	var selectedLevelColumns = new Array();
	<%=initSelectedLevelColumns%>
	
	var selectedFeatureNames = new Array();
	<%=initSelectedFeatureNames%>
	
	function init() {
		//levelNames[0] = 'Level1';
		//levelNames[1] = 'Level2';
		
		//levelColumns[0] = 'coSezione';
		//levelColumns[1] = 'codUU';
		
		//featureNames[0] = 'Sezione';
		//featureNames[1] = 'UU';
		
		//selectedLevelColumns[0] = '';
		//selectedLevelColumns[1] = '';
		
		//selectedFeatureNames[0] = '';
		//selectedFeatureNames[1] = '';
		
		
	}
	
	function addLevelButtonClicked() {		
		var levelNameTxt = document.getElementById("levelNameTxt");
		var levelName = levelNameTxt.value;
		levelNameTxt.value = "";
		addLevel(levelName);
	}
	
	function addLevel(levelName) {
		levelNames[levelNames.length] = levelName;
		selectedLevelColumns[selectedLevelColumns.length] = '';
	    selectedFeatureNames[selectedFeatureNames.length] = '';
	    displayLevels();
	}
	
	function deleteLevel(levelName) {	
		var newLevelNames = new Array();
		var newSelectedLevelColumns = new Array();
		var newSelectedFeatureNames = new Array();
		
		for(i = 0, j = 0; i < levelNames.length; i++) {
  			if(levelNames[i] != levelName) {
  				newLevelNames[j] = levelNames[i];
				newSelectedLevelColumns[j] = queryColumns[i];
				newSelectedFeatureNames[j] = selectedFeatureNames[i];
				j++;
  			}
  		}
  		
  		levelNames = newLevelNames;
		selectedLevelColumns = newSelectedLevelColumns;	
		selectedFeatureNames = newSelectedFeatureNames;
		
		displayLevels();
	}
  	
  	function moveUpLevel(levelName) {
  		for(i = 0; i < levelNames.length; i++) {
  			if(levelNames[i] == levelName) {
  				var tmp;
  				
  				tmp = levelNames[i];
  				levelNames[i] = levelNames[i-1];
  				levelNames[i-1] = tmp;
  				
  				tmp = selectedLevelColumns[i];
  				selectedLevelColumns[i] = selectedLevelColumns[i-1];
  				selectedLevelColumns[i-1] = tmp;
  				
  				tmp = selectedFeatureNames[i];
  				selectedFeatureNames[i] = selectedFeatureNames[i-1];
  				selectedFeatureNames[i-1] = tmp;
  				
  				break;
  			}
  		}
  		
  		displayLevels();
  	}
  	
  	function moveDownLevel(levelName) {
  		for(i = 0; i < levelNames.length; i++) {
  			if(levelNames[i] == levelName) {
  				var tmp;
  				
  				tmp = levelNames[i];
  				levelNames[i] = levelNames[i+1];
  				levelNames[i+1] = tmp;
  				
  				tmp = selectedLevelColumns[i];
  				selectedLevelColumns[i] = selectedLevelColumns[i+1];
  				selectedLevelColumns[i+1] = tmp;
  				
  				tmp = selectedFeatureNames[i];
  				selectedFeatureNames[i] = selectedFeatureNames[i+1];
  				selectedFeatureNames[i+1] = tmp;
  				
  				break;
  			}
  		}
  		displayLevels();
  	}
  	
  	function updateSelectedLevelColumns(obj, levelNum) {
  		var selectedColumn = obj.options[obj.selectedIndex].value;
		selectedLevelColumns[levelNum] = selectedColumn;
  	}
  	
  	function updateSelectedFeatureNames(obj, levelNum) {
  		var selectedFeature = obj.options[obj.selectedIndex].value;
		selectedFeatureNames[levelNum] = selectedFeature;
  	}
  	
  	
  	
  	function displayLevels() { 
		 
	    var strHTML ='';
	    
	        
	    
	    strHTML += '<table class="object-details-table" style="margin:5px;width:100%;">';
	    strHTML += '<tr>';
	    strHTML += '<td class="portlet-section-header" style="text-align:left;width:200px;">' + 'Name' + '</td>';
	    strHTML += '<td class="portlet-section-header" style="text-align:left;width:200px;">' + 'Column' + '</td>';
	    strHTML += '<td class="portlet-section-header" style="text-align:left;width:200px;">' + 'Target Feature' + '</td>';
	    strHTML += '<td class="portlet-section-header" style="text-align:center;width:50px;">&nbsp;</td>';
	    strHTML += '<td class="portlet-section-header" style="text-align:center;width:50px;">&nbsp;</td>';
	    strHTML += '<td class="portlet-section-header" style="text-align:center;width:50px;">&nbsp;</td>';
	    strHTML += '</tr>';
	    
	    var rowClass;
	    var alternate = false;
	    
	    for (i = 0; i < levelNames.length; i++) {
	      if (alternate) rowClass = "portlet-section-alternate";
	      else rowClass = "portlet-section-body";      
	      alternate = !alternate;
	     
	      
	      strHTML += '<tr class="portlet-font">';
	      strHTML += '<td class="' + rowClass + '">' + levelNames[i] + '</td>';   
	      
	      
	      strHTML += '<td class="' + rowClass + '">';
	      strHTML += '<select name="column" onChange="updateSelectedLevelColumns(this, ' + i + ')">';	      
	      for(j = 0; j < queryColumns.length; j++) {
	      	var selected = '';
	      	if(selectedLevelColumns[i] == '' && j == 0) {
	      		selectedLevelColumns[i] = queryColumns[j];
	      	}
	      	if(selectedLevelColumns[i] == queryColumns[j]) {
	      		selected = 'selected="true"';
	      	}
	      	strHTML += '<option value="' + queryColumns[j]+'" ' + selected +  ' >' + queryColumns[j];
	      }
	      strHTML += '</select>';
	      strHTML += '</td>'; 
	      
	      strHTML += '<td class="' + rowClass + '">';
	      strHTML += '<select name="feature" onChange="updateSelectedFeatureNames(this, ' + i + ')">';	      
	      for(j = 0; j < featureNames.length; j++) {
	      	var selected = '';
	      	if(selectedFeatureNames[i] == '' && j == 0) {
	      		selectedFeatureNames[i] = featureNames[j];
	      	}
	      	if(selectedFeatureNames[i] == featureNames[j] ) {
	      		selected = 'selected="true"';
	      	}
	      	strHTML += '<option value="' + featureNames[j]+'" ' + selected +  ' >' + featureNames[j];
	      }
	      strHTML += '</select>';
	      strHTML += '</td>'; 
	     
	    
	      strHTML += '<td class="' + rowClass + '" align="center">';
	      strHTML += '<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/erase.gif")%>" onClick="deleteLevel(\'' + levelNames[i] + '\')"/>';
	      strHTML += '</td>';
	      
	      strHTML += '<td class="' + rowClass + '" align="center">';
	      if( (i+1) != levelNames.length ) {
	      	strHTML += '<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/down16.gif")%>" onClick="moveDownLevel(\'' + levelNames[i] + '\')"/>';
	      } else {
	      	strHTML += '&nbsp;';
	      }
	      strHTML += '</td>';
	      
	      strHTML += '<td class="' + rowClass + '" align="center">';
	      if( i != 0 ) {
	      	strHTML += '<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/up16.gif")%>" onClick="moveUpLevel(\'' + levelNames[i] +'\')"/>';
	      } else {
	      	strHTML += '&nbsp;';
	      }
	      strHTML += '</td>';
	      	      
	      strHTML += '</tr>';
	    }
	    
	    strHTML += '</table>';
	    
	    document.getElementById('levelDiv').innerHTML = strHTML;
  	}
	
	function submit(actionName) {
		//alert(actionName);
		var mainForm = document.getElementById('mainForm');
		mainForm.action = '../servlet/AdapterHTTP?ACTION_NAME=' + actionName;
		
		var levels = document.getElementById('levelNames');
		var levelNamesValue = '';
		for(i = 0; i < levelNames.length; i++) {
			levelNamesValue += levelNames[i] + ';';
		}
		levels.value = levelNamesValue;
		
		
		var columnNames = document.getElementById('columnNames');
		var columnNamesValue = '';
		for(i = 0; i < selectedLevelColumns.length; i++) {
			columnNamesValue += selectedLevelColumns[i] + ';';
		}
		columnNames.value = columnNamesValue;
		
		
		var featureNames = document.getElementById('featureNames');
		var featureNamesValue = '';
		for(i = 0; i < selectedFeatureNames.length; i++) {
			featureNamesValue += selectedFeatureNames[i] + ';';
		}
		featureNames.value = featureNamesValue;
		
		mainForm.submit();
	}
	
	init();
	displayLevels();
	
</script>

</div>


<!-- ============================================================================================================== -->





</body>

</html>
