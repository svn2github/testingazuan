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





<%@ include file="../../../jsp/qbe_base.jsp" %>

<% 
   	Object spagoBiInfo = sessionContainer.getAttribute("spagobi"); 
	ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(sessionContainer);
   	DataMartModel dm = (DataMartModel) sessionContainer.getAttribute("dataMartModel");
   	MapConfiguration mapConfiguration = (MapConfiguration) sessionContainer.getAttribute("MAP_CONFIGURATION");
   	
   	String qbeSqlQuery = (String)aServiceResponse.getAttribute("QBE_QUERY");
   	List qbeQueryColumns = (List)aServiceResponse.getAttribute("QBE_QUERY_COLUMNS");
   	String qbeQueryColumnsInit = "";
   	for(int i = 0; i < qbeQueryColumns.size(); i++) {
   		String columnName = (String)qbeQueryColumns.get(i);
   		qbeQueryColumnsInit += "qbeQueryColumns[" + i + "] = '" + columnName + "';\n";
   	}
   	
   	String expertSqlQuery = (String)aServiceResponse.getAttribute("EXPERT_QUERY");
   	List expertQueryColumns = (List)aServiceResponse.getAttribute("EXPERT_QUERY_COLUMNS");
   	String expertQueryColumnsInit = "";
   	for(int i = 0; i < expertQueryColumns.size(); i++) {
   		String columnName = (String)expertQueryColumns.get(i);
   		expertQueryColumnsInit += "expertQueryColumns[" + i + "] = '" + columnName + "';\n";
   	}
   	
   	DatamartProviderConfiguration datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();   	
   	List hierachies = datamartProviderConfiguration.getHierarchies();
   	DatamartProviderConfiguration.Hierarchy hierarchy;
   	String hierachiesInit = "";
   	String standardHierarchyName = "";
   	for(int i = 0; i < hierachies.size(); i++) {
   		hierarchy = (DatamartProviderConfiguration.Hierarchy)hierachies.get(i);
   		if(hierarchy.isDefaultHierarchy()) standardHierarchyName = hierarchy.getName();
   		hierachiesInit += "hierachies[" + i + "] = '" + hierarchy.getName() + "';\n";
   	}
%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>
 
<%
	if(spagoBiInfo == null) {
%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 

		    style='vertical-align:middle;padding-left:5px;'>
			<%= dm.getName() %> : <%=dm.getDescription() %> - <%= qbeMsg.getMessage(requestContainer,"QBE.Title.Conditions", bundle) %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%@include file="/../../../jsp/qbe_headers.jsp"%>
	</tr>
</table>


<%
	}
%>

<%@include file="../../../jsp/testata.jsp" %>
 
 
<div class='div_background_no_img'>

<!-- ============================================================================================================== -->

<center>
<a href="#" onClick="moveToPreviewStep()"> &lt;&lt; </a>&nbsp;&nbsp;&nbsp; 
<a href="#" onClick="moveToStep(1)"> 1 </a> - 
<a href="#" onClick="moveToStep(2)"> 2 </a> - 
<a href="#" onClick="moveToStep(3)"> 3 </a> - 
<a href="#" onClick="moveToStep(4)"> 4 </a> - 
<a href="#" onClick="moveToStep(5)"> 5 </a> - 
<a href="#" onClick="moveToStep(6)"> 6 </a> - 
<a href="#" onClick="moveToStep(7)"> 7 </a>&nbsp;&nbsp;&nbsp;
<a href="#" onClick="moveToNextStep()"> &gt;&gt; </a>
</center>

<p>

<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td id="confStepTitle" class='titlebar_configuration_step' 
		style='font-family: Verdana,Geneva,Arial,Helvetica,sans-serif;
		font-size: 18px;
		font-weight: bold;
		border-bottom: 1px solid #bbb;
		background-color: rgb(255, 255, 255);
		color: #074B88;
		height: 35px;
		border-top: 1px solid #bbb;
		vertical-align:middle;
		text-align: center'>
			STEP1 - Select query
		</td>
	</tr>
</table>

<p>

<div id='queryDiv' style="display:none;width:100%;padding:10px 10px 10px 10px;margin:10px 0px 0px 0px;border:solid;border-width:0px">
<center>
<input type="radio" id="qbeQueryRadio" name="selectedQuery" value="qbeQuery" onClick="updateIsQbeQuerySelectedValue(this)"/>Qbe Query
<input type="radio" id="expertQueryRadio" name="selectedQuery" value="expertQuery" onClick="updateIsQbeQuerySelectedValue(this)"/>Expert Query<br>
<textarea
      accesskey="q"
      class="queryText"
      cols="100"
      dir="ltr"
      id="queryText"
      lang="sql"
      name="queryText"
      rows="6"
      title="Query">
      ?Undefined?
  </textarea>
</center>
</div>

 <center>

<div id='fieldsDiv' style="display:none;width:95%;margin:10px 0px 0px 0px;border:solid; border-width:0px"></div>

<div id='hierarchyDiv' style="display:none;width:98%;margin:10px 0px 0px 0px;border:solid; border-width:0px"></div>

<div id='selectedHierarchyDiv' style="display:none;width:98%;margin:10px 0px 0px 0px;border:solid; border-width:0px"></div>

<div id='mapDiv' style="display:none;width:98%;margin:10px 0px 0px 0px;border:solid; border-width:0px"></div>

<div id='layerDiv' style="display:none;width:98%;margin:10px 0px 0px 0px;border:solid; border-width:0px"></div>


<div id='submitDiv' style="display:none;width:98%;margin:10px 0px 0px 0px;border:solid; border-width:0px">
	

	<table  width="100%" height="100%" border="0" valign="top" style="background-color:white">
		
		<tr valign="top" height="50px">
			<td width="50%" align="right" valign="top">
	  			<input type="submit" name="refresh" value="Get templete" onClick="getTempalte()"/>
			</td>
			<td width="50%" align="left" valign="top">
				<input type="submit" name="preview" value="See preview" onClick="showPreview()"/>
			</td>
		</tr>
		
		<tr valign="top" height="80%">
			<td width="100%" valign="top" colspan="2">
			
			<div width="100%" id="previewPanel" valign="top" style="display:none">
				<center>
				<iframe id="previewIFrame"
				        name="previewIFrame"
				        src=""
				        width="100%"
				        height="300px"
				        frameborder="3">
				</iframe>	
				</center>				
			</div>
			
			</td>
		</tr>
		
	</table>	
</div>

</center>

<script type="text/javascript">

function initIframeDimensions() {
 var winW = "100%";
 var winH = "300px";
 
 if (navigator.appName=="Netscape") {
  winW = window.innerWidth;
  winH = window.innerHeight;
 }
 
 if (navigator.appName.indexOf("Microsoft")!=-1) {
  winW = document.body.offsetWidth;
  winH = document.body.offsetHeight;
 }
 
 var previewIFrame = document.getElementById('previewIFrame');
 previewIFrame.width = Math.round(winW*0.8);
 previewIFrame.height = Math.round(winH*0.8);
 }
 
 initIframeDimensions();

</script>

<form method='POST' action='../servlet/AdapterHTTP?ACTION_NAME=GET_TEMPLETE_ACTION' 
	  id='mainForm' name='mainForm'
	  target="self">
	  
	  <input type="hidden" id="par_query" name="query" value=""/>
	  <input type="hidden" id="par_geoIdColumn" name="geoIdColumn" value=""/>
	  <input type="hidden" id="par_measureColumns" name="measureColumns" value=""/>
	  <input type="hidden" id="par_selectedHierarchy" name="selectedHierarchy" value=""/>
	  <input type="hidden" id="par_baseLevel" name="baseLevel" value=""/>
	  <input type="hidden" id="par_aggregationLevel" name="aggregationLevel" value=""/>
	  
	  <input type="hidden" id="par_includedHierarchies" name="includedHierarchies" value=""/>
	  <input type="hidden" id="par_selectedMap" name="selectedMap" value=""/>
	  <input type="hidden" id="par_selectedLayers" name="selectedLayers" value=""/>
	  
</form>







<script type='text/javascript'>

  var step;
  var stepDivs = new Array();
  var stepDescr = new Array();
  
  var qbeQueryTxt = '<%=qbeSqlQuery%>';
  var qbeQueryColumns = new Array();
   <%=qbeQueryColumnsInit%>
  
  var expertQueryTxt = '<%=expertSqlQuery%>';
  var expertQueryColumns = new Array;;
  <%=expertQueryColumnsInit%>
  
  var isQbeQuerySelected = true;
  
  var geoIdColumn = null;
  var measureColumns = new Array();
  
  var hierachies = new Array();
  <%=hierachiesInit%>  
  var standardHierarchy = '<%=standardHierarchyName%>';
  var includedHierarchies = new Array();
  var selectedHierarchy = null;
  
  var selectedHierarchyLevels = new Array();
  var hierarchyLevelToFeatureMap = new Array();
  var aggregationLevel = null;
  var baseLevel = null;
  
  var maps = new Array();
  var selectedMap = null;
  
  var layers = new Array();
  var selectedLayers = new Array();

  function windowCloseHandler(eventName, win){
  	if(win.getId() == 'hierarchyDetailsWin') {
  		refreshHierarchyBlock();
  	}  	
  }

  function init() {   
    
    step = 1;
    stepDivs[1] = document.getElementById('queryDiv');
  	stepDivs[2] = document.getElementById('fieldsDiv');
   	stepDivs[3] = document.getElementById('hierarchyDiv');
    stepDivs[4] = document.getElementById('selectedHierarchyDiv');
    stepDivs[5] = document.getElementById('mapDiv');
    stepDivs[6] = document.getElementById('layerDiv');
    stepDivs[7] = document.getElementById('submitDiv');
    
    stepDescr[1] = "Step1 - Select query";
  	stepDescr[2] = "Step2 - Select columns";
   	stepDescr[3] = "Step3 - Select hierachy";
    stepDescr[4] = "Step4 - Select level";
    stepDescr[5] = "Step5 - Select map";
    stepDescr[6] = "Step6 - Select layers";
    stepDescr[7] = "Step7 - Get template";
  
  	
    
    var confStepTitle = document.getElementById('confStepTitle');
  
    
    var alertOnClose = { onClose: function(eventName, win) { windowCloseHandler(eventName, win);} }
	Windows.addObserver(alertOnClose);
    
  }
  
  function moveToNextStep() {
  	moveToStep(step+1);  
  }
  
  function moveToPreviewStep() {
  	moveToStep(step-1);  	
  }
  
  function moveToStep(stepNo) {
  	step = (stepNo<1)?1:stepNo;
  	step = (stepNo>7)?7:stepNo;
  	displayPage();
  }
  
  function displayPage() {
  	stepDivs[1].style.display = 'none';
  	stepDivs[2].style.display = 'none';
   	stepDivs[3].style.display = 'none';
    stepDivs[4].style.display = 'none';
    stepDivs[5].style.display = 'none';
    stepDivs[6].style.display = 'none';
    stepDivs[7].style.display = 'none';
    
    if(step == 7) {
    	var previewPanel = document.getElementById('previewPanel');
    	previewPanel.style.display = 'none';
    	//alert("hide");
    }
    
    stepDivs[step].style.display = 'inline';
    confStepTitle.innerHTML = stepDescr[step];
    
    
  }
  
  
  function setUpInputParams() {
  	var queryPar = document.getElementById('par_query');
  	if(queryPar !=null) {
	  	if(isQbeQuerySelected) {
	      queryPar.value = qbeQueryTxt;
	    } else {
	      queryPar.value = expertQueryTxt;
	    }
    }
  
	var geoIdColumnPar = document.getElementById('par_geoIdColumn');
	geoIdColumnPar.value = geoIdColumn;
	
	var measureColumnsPar = document.getElementById('par_measureColumns');
	var measureColumnsStr = "";
	for(i = 0; i < measureColumns.length; i++) {
		measureColumnsStr += measureColumns[i] + ";";
	}
	measureColumnsPar.value = measureColumnsStr;
	
	var selectedHierarchyPar = document.getElementById('par_selectedHierarchy');
	selectedHierarchyPar.value = selectedHierarchy;
	
	var baseLevelPar = document.getElementById('par_baseLevel');
	baseLevelPar.value = baseLevel;
	
	var aggregationLevelPar = document.getElementById('par_aggregationLevel');
	aggregationLevelPar.value = aggregationLevel;
	
	var includedHierarchiesPar = document.getElementById('par_includedHierarchies');
	var includedHierarchiesStr = "";
	for(i = 0; i < includedHierarchies.length; i++) {
		includedHierarchiesStr += includedHierarchies[i] + ";";
	}
	includedHierarchiesPar.value = includedHierarchiesStr;
	
	var selectedMapPar = document.getElementById('par_selectedMap');
	selectedMapPar.value = selectedMap;
	
	var selectedLayersPar = document.getElementById('par_selectedLayers');
	var selectedLayersStr = "";
	for(i = 0; i < selectedLayers.length; i++) {
		selectedLayersStr += selectedLayers[i] + ";";
	}
	selectedLayersPar.value = selectedLayersStr;
  }
  
  
  function getTempalte() {
  
  	setUpInputParams();
	
  	var mainForm = document.getElementById('mainForm');
  	mainForm.action = "../servlet/AdapterHTTP?ACTION_NAME=GET_TEMPLETE_ACTION";
  	mainForm.target = "_self";
  	mainForm.submit();
  }
  
  function showPreview() {
  	setUpInputParams();
  	
  	var mainForm = document.getElementById('mainForm');
  	mainForm.action = "../servlet/AdapterHTTP?ACTION_NAME=PREVIEW_ON_MAP";
  	mainForm.target = "previewIFrame";
  	mainForm.submit();
  	
  	var previewPanel = document.getElementById('previewPanel');
    previewPanel.style.display = 'inline';
  }
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // QUERY BLOCK
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  
  function updateIsQbeQuerySelectedValue(obj) {
    //alert('updateIsQbeQuerySelectedValue');
    if(obj.value == 'qbeQuery') {
      isQbeQuerySelected = true;
    } else {
      isQbeQuerySelected = false;
    }
    
    displayQuery();
    displayQueryFields();
  }
  
  function displayQuery() { 
    var queryText =  document.getElementById('queryText');
    var qbeQueryRadio =  document.getElementById('qbeQueryRadio');
    var expertQueryRadio =  document.getElementById('expertQueryRadio');
    if(isQbeQuerySelected) {
      queryText.value = qbeQueryTxt; 
      qbeQueryRadio.checked = true;
      expertQueryRadio.checked = false;
    } else {
      queryText.value = expertQueryTxt; 
      qbeQueryRadio.checked = false;
      expertQueryRadio.checked = true; 
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // COLUMNS BLOCK
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  
   function modifyMeasureDetails(measureName) {
  	var url = "http://www.spagobi.org";
  	url = "../servlet/AdapterHTTP?ACTION_NAME=MODIFY_MEASURE_DETAILS_ACTION&measureName=" + measureName;
    var measureDetailsWin = new Window("measureDetailsWin", {className: "dialog", title: "Measure details", 
                                              top:70, left:100, width:850, height:400, 
                                              resizable: true, url: url })
	measureDetailsWin.setDestroyOnClose();
	measureDetailsWin.show(true); 						
  }
  
  function updateIdColumnValue(obj) {
    //alert('updateIdColumnValue');
    var kpi;
    if(geoIdColumn != null){
      kpi = document.getElementById('kpi_' + geoIdColumn);
      kpi.disabled = false;
    }
    
    geoIdColumn = obj.value;
    var kpi = document.getElementById('kpi_' + geoIdColumn);
    kpi.disabled = true;
  }
  
  function updateMeasureColumnsValue(obj) {
    //alert('updateMeasureColumnsValue: ' + obj.checked);
    if(obj.checked) {
      measureColumns[measureColumns.length] = obj.value;
      var idCol = document.getElementById('id_' + obj.value);
      idCol.disabled = true;
      var idImg = document.getElementById('kpi_img_' + obj.value);
       idImg.style.display  = 'inline';      
    } else {
      var newMesureColumns = new Array();
      for(i = 0, j = 0; i < measureColumns.length; i++) {
        if(measureColumns[i] != obj.value) {
          newMesureColumns[j] = measureColumns[i];
          j++;
        } else  {
          var idCol = document.getElementById('id_' + obj.value);
          idCol.disabled = false;
        }
      }
      measureColumns = newMesureColumns;  
      var idImg = document.getElementById('kpi_img_' + obj.value);
      idImg.style.display  = 'none'; 
         
    }
  }
  
  function displayQueryFields() {  
    var strHTML ='';
    
    var columns;
    if(isQbeQuerySelected) columns = qbeQueryColumns;
    else columns = expertQueryColumns;
    
    strHTML += '<table class="object-details-table" style="margin:5px;width:100%;">';
    strHTML += '<tr>';
    strHTML += '<td class="portlet-section-header">' + 'Query Columns' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'GeoId Column' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Measure Columns' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Measure Details' + '</td>';
    strHTML += '</tr>';
    
    var rowClass;
    var alternate = false;
    
    for (i = 0; i < columns.length; i++) {
      if (alternate) rowClass = "portlet-section-alternate";
      else rowClass = "portlet-section-body";      
      alternate = !alternate;
      
      strHTML += '<tr class="portlet-font">';
      strHTML += '<td class="' + rowClass + '">' + columns[i] + '</td>';      
      
     
      var isGeoIdColumns = false;
      var isMeasureColumns = false;
      
      if (columns[i] == geoIdColumn) isGeoIdColumns = true;
      for (j = 0; j < measureColumns.length; j++) {
        if (columns[i] == measureColumns[j]) {
          isMeasureColumns = true;
          break;
        }
      }      
      
      var checked;
      var disabled; 
      
      if (isGeoIdColumns) checked = 'checked="checked"';
      else checked = '';
      
      if(isMeasureColumns) disabled = 'disabled="true"';
      else disabled = '';
      
      strHTML += '<td class="' + rowClass + '" align="center"> <input type="radio" id="id_' + columns[i] + '" name="idColumn"  value="' + columns[i] + '" onClick="updateIdColumnValue(this)"  ' + checked + ' ' + disabled + '> </td>';
      
      
      if(isMeasureColumns) checked = 'checked="checked"';
      else checked = '';
      
      if (isGeoIdColumns) disabled = 'disabled="true"';
      else disabled = '';
  
      strHTML += '<td class="' + rowClass + '" align="center"><input type="checkbox"  id="kpi_' + columns[i] + '" name="measureColumns" value="' + columns[i] + '" onClick="updateMeasureColumnsValue(this)" ' + checked + ' ' + disabled + '></td>';
      
      var style = '';      
      if(isMeasureColumns) style = 'style="display:inline;"';
      else style = 'style="display:none;"';
      
      strHTML += '<td class="' + rowClass + '" align="center">';
      strHTML += '<img id="kpi_img_' + columns[i] + '" ' + style +  ' src="<%=qbeUrl.conformStaticResourceLink(request,"../img/detail.gif")%>" onClick="modifyMeasureDetails(\'' + columns[i] + '\')"/>';
      strHTML += '</td>';
      
      
      
      strHTML += '</tr>';
    }
    
    strHTML += '</table>';
    
    document.getElementById('fieldsDiv').innerHTML = strHTML;
  }
  
  
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // HIERARCHIES BLOCK
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  
  function createHierachy() {
  	var url = "http://www.spagobi.org";
  	url = "../servlet/AdapterHTTP?ACTION_NAME=CREATE_HIERARCHY_ACTION";
    var hierarchyWin = new Window("hierarchyDetailsWin", {className: "dialog", title: "Create hierarchy", 
                                              top:70, left:100, width:850, height:400, 
                                              resizable: true, url: url })
	hierarchyWin.setDestroyOnClose();
	hierarchyWin.show(true); 						
  }
  
  function modifyHierachy(hierarchyName) {
  	var url = "http://www.spagobi.org";
  	url = "../servlet/AdapterHTTP?ACTION_NAME=MODIFY_HIERARCHY_ACTION&hierarchyName=" + hierarchyName;
    var hierarchyWin = new Window("hierarchyDetailsWin", {className: "dialog", title: "Modify hierarchy", 
                                              top:70, left:100, width:850, height:400, 
                                              resizable: true, url: url })
	hierarchyWin.setDestroyOnClose();
	hierarchyWin.show(true); 						
  }
  
  function deleteHierarchy(hierarchyName) {
  	var url = '../servlet/AdapterHTTP';
	var pars = 'ACTION_NAME=DELETE_HIERACHIES_ACTION&hierarchyName=' + hierarchyName;
	
	var ajaxRequest = new Ajax.Request(url, 
								       { method: 'post', 
										 parameters: pars, 
										 onComplete: refreshHierarchyBlockCallback });
  }
  
  function refreshHierarchyBlock() {
  	var url = '../servlet/AdapterHTTP';
	var pars = 'ACTION_NAME=GET_HIERACHIES_ACTION';
	
	var ajaxRequest = new Ajax.Request(url, 
								       { method: 'post', 
										 parameters: pars, 
										 onComplete: refreshHierarchyBlockCallback });
  }
  
  function refreshHierarchyBlockCallback(req) {
  	var newHierachies = new Array();
  	var newIncludedHierarchies = new Array();
  	var newSelectedHierarchy = null;
  	var nextHiearchyIndex = 0;
  	var nextIncludedHiearchyIndex = 0;
  		
  	var childs = req.responseXML.getElementsByTagName("HIERARCHIES")[0].childNodes;
  	for (i = 0; i< childs.length; i++) {  		
  		var value = childs[i].childNodes[0].nodeValue;
  		if(value == null) continue;
  		  		
  		newHierachies[nextHiearchyIndex++] = value;
  		//alert(newHierachies[nextHiearchyIndex-1] + " #" + includedHierarchies); 
  		if(includedHierarchies != null) {
  			for(j = 0; j < includedHierarchies.length; j++) {
  				//alert("Comapring with ... " + includedHierarchies[j]); 
  				if(includedHierarchies[j] == newHierachies[nextHiearchyIndex-1]) {
  					newIncludedHierarchies[nextIncludedHiearchyIndex++] = newHierachies[nextHiearchyIndex-1];
  					//alert("MATCH num " + nextIncludedHiearchyIndex);
  				}  				
  			}  			
  		}
  		
  		//alert("selectedHierarchy: " + selectedHierarchy + "; hierchies: " + newHierachies[nextHiearchyIndex-1]);
  		if(selectedHierarchy!= null && (selectedHierarchy == newHierachies[nextHiearchyIndex-1]) ) {
  			newSelectedHierarchy = newHierachies[nextHiearchyIndex-1];
  		} 		
    }
  	
  	hierachies = newHierachies;
  	includedHierarchies = newIncludedHierarchies;
  	selectedHierarchy = newSelectedHierarchy;
  	
  	displayHierachies();
  }
  
  function updateSelectedHierarchyValue(obj) { 
    selectedHierarchy = obj.value;
   	var url = '../servlet/AdapterHTTP';
	var pars = 'ACTION_NAME=GET_HIERARCHY_LEVELS_ACTION&hierarchyName=' + selectedHierarchy;
	
	var ajaxRequest = new Ajax.Request(url, 
								       { method: 'post', 
										 parameters: pars, 
										 onComplete: updateSelectedHierarchyValueCallback });
    
  }	
  
  function updateSelectedHierarchyValueCallback(req) {
  	var levelChilds;
  	var featureChilds;
  	
  	levelChilds = req.responseXML.getElementsByTagName("LEVELS")[0].childNodes;
  	featureChilds = req.responseXML.getElementsByTagName("FEATURES")[0].childNodes;
  	selectedHierarchyLevels = new Array();
  	hierarchyLevelToFeatureMap = new Array();
  	for (i = 0; i< levelChilds.length; i++) {  		
  		var v1 = levelChilds[i].childNodes[0].nodeValue;
  		var v2 = featureChilds[i].childNodes[0].nodeValue;
  		selectedHierarchyLevels[i] = v1;
  		hierarchyLevelToFeatureMap[v1] = v2;
  	}
  	
  	aggregationLevel = null;
  	baseLevel = null;
  	displaySelectedHierachy();
  	refreshMapsBlock();
  }
   
  function updateIncludedHierarchiesValue(obj) {
  	if(obj.checked) {
      includedHierarchies[includedHierarchies.length] = obj.value;
    } else {
      var newIncludedHierarchies = new Array();
      for(i = 0, j = 0; i < includedHierarchies.length; i++) {
        if(includedHierarchies[i] != obj.value) {
          newIncludedHierarchies[j++] = includedHierarchies[i];
        } 
      }
      includedHierarchies = newIncludedHierarchies;     
    }
  }
  
  
  function displayHierachies() {  
  	var strHTML ='';
    
    strHTML += '<table class="object-details-table" style="margin:5px;width:100%;">';
    strHTML += '<tr>';
    strHTML += '<td class="portlet-section-header">' + 'Hierarchy Name' + '</td>';
    strHTML += '<td class="portlet-section-header">' + 'Type' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Select' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Include' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Details' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Delete' + '</td>';
    strHTML += '</tr>';
    
    var rowClass;
    var alternate = false;
    
    for (i = 0; i < hierachies.length; i++) {
          	
      if (alternate) rowClass = "portlet-section-alternate";
      else rowClass = "portlet-section-body";      
      alternate = !alternate;
      
      strHTML += '<tr class="portlet-font">';
      strHTML += '<td class="' + rowClass + '">' + hierachies[i] + '</td>';
      if(hierachies[i] == standardHierarchy)  strHTML += '<td class="' + rowClass + '">Standard</td>';
      else strHTML += '<td class="' + rowClass + '">Custom</td>';
      
      var checked = '';
      var disabled = '';
      
      //alert("[" + selectedHierarchy + "] - [" + hierachies[i] + "]");
      if(hierachies[i] == selectedHierarchy) {
      	checked = checked = 'checked="checked"';
      } else {
      	checked = '';
      }
      
      strHTML += '<td class="' + rowClass + '" align="center"> <input type="radio" id="sel_hier_' + hierachies[i] + '" name="selectedHierachy"  value="' + hierachies[i] + '" onClick="updateSelectedHierarchyValue(this)"  ' + checked + ' ' + disabled + '> </td>';
     
	  checked = '';		
      if(includedHierarchies != null) {
      	for(j = 0; j < includedHierarchies.length; j++) {
      		if(includedHierarchies[j] == hierachies[i]){
      			checked = 'checked="checked"';
      			break;
      		}
      	}      	
      } 
      
      strHTML += '<td class="' + rowClass + '" align="center"><input type="checkbox"  id="inc_hier_' + hierachies[i] + '" name="includedHierachies" value="' + hierachies[i] + '" onClick="updateIncludedHierarchiesValue(this)" ' + checked + ' ' + disabled + '></td>';
      
      strHTML += '<td class="' + rowClass + '" align="center">';
      strHTML += '<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/detail.gif")%>" onClick="modifyHierachy(\'' + hierachies[i] + '\')"/>';
      strHTML += '</td>';
      
      strHTML += '<td class="' + rowClass + '" align="center">';
      strHTML += '<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/erase.gif")%>" onClick="deleteHierarchy(\'' + hierachies[i] + '\')"/>';
      strHTML += '</td>';
      
      
      strHTML += '</tr>';
    }
    
    if (alternate) rowClass = "portlet-section-alternate";
    else rowClass = "portlet-section-body";     
    strHTML += '<tr class="portlet-font">';
    strHTML += '<td class="' + rowClass + '"><a href="#" onClick="createHierachy()">New...</a></td>';
    strHTML += '<td class="' + rowClass + '">&nbsp</td>';
    strHTML += '<td class="' + rowClass + '">&nbsp</td>';
    strHTML += '<td class="' + rowClass + '">&nbsp</td>';
    strHTML += '<td class="' + rowClass + '">&nbsp</td>';
    strHTML += '<td class="' + rowClass + '">&nbsp</td>';
    strHTML += '</tr>';
    strHTML += '</table>';
      
    
    document.getElementById('hierarchyDiv').innerHTML = strHTML;    
  }
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // SELECTED HIERARCHY BLOCK
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  
  function updateAggregationLevel(obj) {
  	aggregationLevel = obj.options[obj.selectedIndex].value;
  	refreshMapsBlock();
  }
  
   function updateBaseLevel(obj) {
  	baseLevel = obj.options[obj.selectedIndex].value;
  }
  
  
  
  
  function displaySelectedHierachy() {  
    var strHTML ='';
    strHTML += '<table class="object-details-table" style="margin:5px;width:100%;">';
    strHTML += '<tr>';
    strHTML += '<td class="portlet-section-header">';
    strHTML += 'Selected Hierarchy: '
    if(selectedHierarchy == null) strHTML += 'not selected'
    else strHTML +=  selectedHierarchy;
    strHTML += '</td>';
    
    strHTML += '<td class="portlet-section-header">';
    strHTML += 'Base Level: ';
    strHTML += '<select NAME="baseLevel onChange="updateBaseLevel(this)">';
    for(i = 0; i < selectedHierarchyLevels.length; i++) {
       var selected = '';
       if(baseLevel == null && i == 0) {
       	baseLevel = selectedHierarchyLevels[i];
       	selected = 'selected="true"';
       }
       strHTML += '<option VALUE="' + selectedHierarchyLevels[i] + '" ' + selected + '>' + selectedHierarchyLevels[i];
    }               
    strHTML += '</select>';
    strHTML += '</td>';
    
    strHTML += '<td class="portlet-section-header">';
    strHTML += 'Aggregation Level: ';
    strHTML += '<select NAME="aggregationLevel" onChange="updateAggregationLevel(this)">';
     
    for(i = 0; i < selectedHierarchyLevels.length; i++) {
       var selected = '';
       if(aggregationLevel == null && i == 0)  {
       	aggregationLevel = selectedHierarchyLevels[i];
       	selected = 'selected="true"';
       }
       strHTML += '<option VALUE="' + selectedHierarchyLevels[i] + '" ' + selected + '>' + selectedHierarchyLevels[i];
    }               
    strHTML += '</select>';
    strHTML += '</td>';
    
    
    strHTML += '</tr>';
    strHTML += '</table>';
    
    document.getElementById('selectedHierarchyDiv').innerHTML = strHTML;   
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // MAP BLOCK
  ////////////////////////////////////////////////////////////////////////////////////////////////////// 
  
  function refreshMapsBlock() {
  	//alert(aggregationLevel + " 2--> " + hierarchyLevelToFeatureMap[aggregationLevel]);
  	var featureName = hierarchyLevelToFeatureMap[aggregationLevel];
  	var url = '../servlet/AdapterHTTP';
	var pars = 'ACTION_NAME=GET_MAPS_ACTION&featureName=' + featureName;
	
	var ajaxRequest = new Ajax.Request(url, 
								       { method: 'post', 
										 parameters: pars, 
										 onComplete: refreshMapsBlockCallback });
  }
  
  function refreshMapsBlockCallback(req) {
  	maps = new Array();
  	var childs = req.responseXML.getElementsByTagName("MAPS")[0].childNodes;
  	for (i = 0; i< childs.length; i++) {  		
  		var value = childs[i].childNodes[0].nodeValue;
  		maps[i] = value;
  	}
  	
  	displayMap();
  	layers = new Array();
  	selectedLayers = new Array();
  	displayLayer();
  }
  
  function updateSelectedMapValue(obj) {
    selectedMap = obj.value;
    refreshLayersBlock();
  }
  
  function displayMap() {  
    var strHTML ='';
    
    strHTML += '<table class="object-details-table" style="margin:5px;width:100%;">';
    strHTML += '<tr>';
    strHTML += '<td class="portlet-section-header">' + 'Map Name' + '</td>';
    strHTML += '<td class="portlet-section-header">' + 'Description' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;">' + 'Format' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;">' + 'Layers' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Selected' + '</td>';
    strHTML += '</tr>';
    
    var rowClass;
    var alternate = false;
    
    for (i = 0; i < maps.length; i++) {
      if (alternate) rowClass = "portlet-section-alternate";
      else rowClass = "portlet-section-body";      
      alternate = !alternate;
      
      strHTML += '<tr class="portlet-font">';
      strHTML += '<td class="' + rowClass + '">' + maps[i] + '</td>';
      strHTML += '<td class="' + rowClass + '">' + maps[i] + '</td>';
      strHTML += '<td class="' + rowClass + '" style="text-align:center;">' + 'SVG' + '</td>';
      strHTML += '<td class="' + rowClass + '" style="text-align:center;">' + '?' + '</td>';
      
      var checked = '';
      var disabled = '';
      
      
      strHTML += '<td class="' + rowClass + '" align="center"> <input type="radio" id="sel_map_' + hierachies[i] + '" name="selectedMap"  value="' + maps[i] + '" onClick="updateSelectedMapValue(this)"  ' + checked + ' ' + disabled + '> </td>';
     
      
      strHTML += '</tr>';
    }
    
    strHTML += '</table>';
      
    
    document.getElementById('mapDiv').innerHTML = strHTML;    
  }
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // LAYER BLOCK
  ////////////////////////////////////////////////////////////////////////////////////////////////////// 
  
  function refreshLayersBlock() {
  	var url = '../servlet/AdapterHTTP';
	var pars = 'ACTION_NAME=GET_LAYERS_ACTION&mapName=' + selectedMap;
	
	var ajaxRequest = new Ajax.Request(url, 
								       { method: 'post', 
										 parameters: pars, 
										 onComplete: refreshLayersBlockCallback });
  }
  
  function refreshLayersBlockCallback(req) {
  	layers = new Array();
  	var childs = req.responseXML.getElementsByTagName("LAYERS")[0].childNodes;
  	for (i = 0; i< childs.length; i++) {  		
  		var value = childs[i].childNodes[0].nodeValue;
  		layers[i] = value;
  	}
  	
  	displayLayer();
  }
  
  function updateSelectedLayersValue(obj) {
  	if(obj.checked) {
      selectedLayers[selectedLayers.length] = obj.value;
    } else {
      var newSelectedLayers = new Array();
      for(i = 0, j = 0; i < selectedLayers.length; i++) {
        if(selectedLayers[i] != obj.value) {
          newSelectedLayers[j] = selectedLayers[i];
          j++;
        } 
      }
      selectedLayers = newSelectedLayers;     
    }
  }
  
  function displayLayer() {  
    var strHTML ='';
    
    strHTML += '<table class="object-details-table" style="margin:5px;width:100%;">';
    strHTML += '<tr>';
    strHTML += '<td class="portlet-section-header">' + 'Layer Name' + '</td>';
    strHTML += '<td class="portlet-section-header">' + 'Description' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;">' + 'Type' + '</td>';
    strHTML += '<td class="portlet-section-header" style="text-align:center;width:100px;">' + 'Selected' + '</td>';
    strHTML += '</tr>';
    
    var rowClass;
    var alternate = false;
    
    for (i = 0; i < layers.length; i++) {
      if (alternate) rowClass = "portlet-section-alternate";
      else rowClass = "portlet-section-body";      
      alternate = !alternate;
      
      strHTML += '<tr class="portlet-font">';
      strHTML += '<td class="' + rowClass + '">' + layers[i] + '</td>';
      strHTML += '<td class="' + rowClass + '">' + layers[i] + '</td>';
      strHTML += '<td class="' + rowClass + '" style="text-align:center;">' + '?' + '</td>';
      
      var checked = '';
      var disabled = '';
      
      
      strHTML += '<td class="' + rowClass + '" align="center"> <input type="checkbox" id="sel_layer_' + layers[i] + '" name="selectedLayer"  value="' + layers[i] + '" onClick="updateSelectedLayersValue(this)"  ' + checked + ' ' + disabled + '> </td>';
     
      
      strHTML += '</tr>';
    }
    
    strHTML += '</table>';
      
    
    document.getElementById('layerDiv').innerHTML = strHTML;    
  }
  
</script>


<script>
init();
displayQuery();
displayQueryFields();
displayHierachies();
displaySelectedHierachy();
displayMap();
displayLayer();

displayPage();
</script>

<!-- ============================================================================================================== -->

</div>



<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>

<div id="divSpanCurrent">
	<span id="currentScreen">DIV_GEO</span>
</div>


<%@include file="../../../jsp/qbefooter.jsp" %>


</div>
