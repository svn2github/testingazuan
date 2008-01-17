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
   	
  
   	MapRendererConfiguration.Measure measure = (MapRendererConfiguration.Measure) sessionContainer.getAttribute("MEASURE");
	
	
%>



<html>

<head>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/spagobi.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/jsr168.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/js_color_picker_v2.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="../../../css/external.css" type="text/css"/>
	
	
	<script type="text/javascript" src='<%=qbeUrl.conformStaticResourceLink(request,"../js/js_color_picker_v2.js")%>'></script>
	<script type="text/javascript" src='../../../js/color_functions.js'></script>
	
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

<script>
	function updateColor(color) {
		var colorBox = document.getElementById("colorBox");
		colorBox.style.backgroundColor = color;
		
		var colorInput = document.getElementById("color");
		colorInput.value = color;
	}
	
	function updateBaseColor(color) {
		var colorBaseBox = document.getElementById("colorBaseBox");
		colorBaseBox.style.backgroundColor = color;
		
		var colorBaseInput = document.getElementById("colorBaseColor");
		colorBaseInput.value = color;
	}
	
	function updateObColor(color) {
		var obColorBox = document.getElementById("obColorBox");
		obColorBox.style.backgroundColor = color;
		
		var obColor = document.getElementById("obColor");
		obColor.value = color;
	}
	
	function updateNvColor(color) {
		var nvColorBox = document.getElementById("nvColorBox");
		nvColorBox.style.backgroundColor = color;
		
		var nvColor = document.getElementById("nvColor");
		nvColor.value = color;
	}
	
	
	
	function updateTrasholdCalculationType(obj) {
		var calculationType = obj.options[obj.selectedIndex].value;
		var staticTrasholdCalculationParams = document.getElementById("staticTrasholdCalculationParams");
		var percentageTrasholdCalculationParams = document.getElementById("percentageTrasholdCalculationParams");
		var uniformTrasholdCalculationParams = document.getElementById("uniformTrasholdCalculationParams");
		var quantileTrasholdCalculationParams = document.getElementById("quantileTrasholdCalculationParams");
		
		staticTrasholdCalculationParams.style.display = "none";
		percentageTrasholdCalculationParams.style.display = "none";
		uniformTrasholdCalculationParams.style.display = "none";
		quantileTrasholdCalculationParams.style.display = "none";
		
		
		if(calculationType == "static") {
			staticTrasholdCalculationParams.style.display = "inline";
		} else if(calculationType == "perc") {
			percentageTrasholdCalculationParams.style.display = "inline";
		} else if(calculationType == "uniform") {
			uniformTrasholdCalculationParams.style.display = "inline";
		} else if(calculationType == "quantile") {
			quantileTrasholdCalculationParams.style.display = "inline";
		}
	}
	
	function updateColorCalculationType(obj) {
		var calculationType = obj.options[obj.selectedIndex].value;
		var staticColorCalculationParams = document.getElementById("staticColorCalculationParams");
		var gradColorCalculationParams = document.getElementById("gradColorCalculationParams");
	
		staticColorCalculationParams.style.display = "none";
		gradColorCalculationParams.style.display = "none";
				
		if(calculationType == "static") {
			staticColorCalculationParams.style.display = "inline";
		} else if(calculationType == "grad") {
			gradColorCalculationParams.style.display = "inline";
		}
	}
	
	function submit(actionName) {
		//alert(actionName);
		var mainForm = document.getElementById('mainForm');
		mainForm.action = '../servlet/AdapterHTTP?ACTION_NAME=' + actionName;		
		mainForm.submit();
	}
</script>

<!-- ============================================================================================================== -->

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			Measure Details
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
					
				
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='save' value='true' class='header-button-image-portlet-section'
				   src='<%=qbeUrl.conformStaticResourceLink(request,"../img/save.png")%>'
      			   title='Save' 
      			   onclick='submit("SAVE_MEASURE_ACTION")'/> 
			
		</td>
		
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='saveAndGoBack' value='true' class='header-button-image-portlet-section'
				   src='<%=qbeUrl.conformStaticResourceLink(request,"../img/saveAndGoBack.png")%>'
      			   title='Save and return' 
      			   onclick='submit("SAVE_MEASURE_AND_EXIT_ACTION")'/> 
		</td>
		
		<td class='header-button-column-portlet-section'>
			<input type='image' name='Exit' id='Exit' value='true' class='header-button-image-portlet-section'
				   src='<%=qbeUrl.conformStaticResourceLink(request,"../img/back.png")%>'
      			   title='Exit' 
      			   onclick='submit("EXIT_MEASURE_DETAILS_ACTION")'/> 
		</td>
	</tr>
</table>

<div class='div_background_no_img'>

<form method='POST' action='' 
	  id='mainForm' name='mainForm'>

<input type="hidden" name="measureName" id="measureName" value="<%=measure.getColumnId()%>"/> 
	  
<table width="100%" cellspacing="0" border="0" id = "newMeasureBox" >
	<tr>
		<td>
			<div class="div_detail_area_forms">
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Column Id
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="name" id="name" value="<%=measure.getColumnId()%>" maxlength="20"
							readonly disabled/>
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Description
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="description" id="description" value="<%=measure.getDescription()%>" maxlength="20"/>
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Agg. Func.
					</span>
				</div>
				<div class='div_detail_form'>
					 <select id="aggFunc" name="aggFunc"/>
					    <option value="sum" <%=(measure.getAggFunc()!= null && measure.getAggFunc().equalsIgnoreCase("sum")?"selected":"")%> />Somma
					    <option value="avg" <%=(measure.getAggFunc()!= null && measure.getAggFunc().equalsIgnoreCase("avg")?"selected":"") %>/>Media
					    <option value="max" <%=(measure.getAggFunc()!= null && measure.getAggFunc().equalsIgnoreCase("max")?"selected":"")%> />Massimo
					    <option value="min" <%=(measure.getAggFunc()!= null && measure.getAggFunc().equalsIgnoreCase("min")?"selected":"")%> />Minimo
					  </select>			
				</div>
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Color
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="hidden" style='width:190px;' 
							name="color" id="color" value="" maxlength="17"/>
							
					<span class='portlet-form-input-field'
					id="colorBox"
					style="disply:block;background-color:<%=measure.getColour()%>;margin-left:1px;margin-bottom:1px;border:1px solid #000;cursor:pointer;" 
					onclick="showColorPicker(this,updateColor)">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>				
				</div>
			</div>
		</td>
	</tr>
</table>




<!-- ======================================================================================================================================= -->
<!--  TRESHOLDS																																 -->
<!-- ======================================================================================================================================= -->

<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='titlebar_level_2_text_section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;Tresholds
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
						Type
					</span>
				</div>
				<div class='div_detail_form'>
					 <select id="trasholdCalculationType" name="trasholdCalculationType"
					 onChange="updateTrasholdCalculationType(this)"/>
					    <option value="static" <%=(measure.getTresholdCalculatorType().equalsIgnoreCase("static")?"selected":"")%> />Static
					    <option value="quantile" <%=(measure.getTresholdCalculatorType().equalsIgnoreCase("quantile")?"selected":"") %> />Quantile
					    <option value="perc" <%=(measure.getTresholdCalculatorType().equalsIgnoreCase("perc")?"selected":"")%> />Percentage
					    <option value="uniform" <%=(measure.getTresholdCalculatorType().equalsIgnoreCase("uniform")?"selected":"")%> />Uniform
					  </select>
				</div>
							
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Lower Bound
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field'  type="text" style='width:230px;' 
						name="lb" id="lb" value="<%=measure.getTresholdLb() %>" maxlength="20"/>
				</div>
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Upper Bound
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field'  type="text" style='width:230px;' 
						name="ub" id="ub" value="<%=measure.getTresholdUb()%>" maxlength="20"/>
				</div>
											
			</div>
		</td>
	</tr>
</table>

<p>

<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<!--  Static TRESHOLDS Wizard																												 -->
<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->

<div id="staticTrasholdCalculationParams" style="display:'<%=(measure.getTresholdCalculatorType().equalsIgnoreCase("static")?"inline":"none")%>';">
	<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							Range
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field'  type="text" style='width:230px;' 
							name="trasholdRange" id="trasholdRange" 
							value="<%=(measure.getTresholdCalculatorParameters().getProperty("range")==null?"":measure.getTresholdCalculatorParameters().getProperty("range"))%>"/>
					</div>											
				</div>
			</td>
		</tr>
	</table>
</div>

<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<!--  Quantile TRESHOLDS Wizard																												 -->
<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<div id="quantileTrasholdCalculationParams" style="display:'<%=(measure.getTresholdCalculatorType().equalsIgnoreCase("quantile")?"inline":"none")%>';">
	<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							N.Of.Quantiles
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field'  type="text" style='width:230px;' 
							name="trasholdQuantileNo" id="trasholdQuantileNo" 
							value="<%=(measure.getTresholdCalculatorParameters().getProperty("GROUPS_NUMBER")==null?"":measure.getTresholdCalculatorParameters().getProperty("GROUPS_NUMBER"))%>" 
							maxlength="20"/>
					</div>											
				</div>
			</td>
		</tr>
	</table>
</div>

<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<!--  Percentage TRESHOLDS Wizard																												 -->
<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<div id="percentageTrasholdCalculationParams" style="display:'<%=(measure.getTresholdCalculatorType().equalsIgnoreCase("perc")?"inline":"none")%>';">
	<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							Range (%)
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field'  type="text" style='width:230px;' 
							name="trasholdPercRange" id="trasholdPercRange" 
							value="<%=(measure.getTresholdCalculatorParameters().getProperty("range")==null?"":measure.getTresholdCalculatorParameters().getProperty("range"))%>" />
					</div>											
				</div>
			</td>
		</tr>
	</table>
</div>

<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<!--  Uniform TRESHOLDS Wizard																												 -->
<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<div id="uniformTrasholdCalculationParams" style="display:'<%=(measure.getTresholdCalculatorType().equalsIgnoreCase("uniform")?"inline":"none")%>';">
	<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							N.Of Groups 
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field'  type="text" style='width:230px;' 
							name="trasholdGroupsNo" id="trasholdGroupsNo" 
							value="<%=(measure.getTresholdCalculatorParameters().getProperty("GROUPS_NUMBER")==null?"":measure.getTresholdCalculatorParameters().getProperty("GROUPS_NUMBER"))%>" 
							maxlength="20"/>
					</div>											
				</div>
			</td>
		</tr>
	</table>
</div>




<!-- ======================================================================================================================================= -->
<!--  COLOUR																																 -->
<!-- ======================================================================================================================================= -->
<table width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='titlebar_level_2_text_section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;Colours
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
						Type
					</span>
				</div>
				<div class='div_detail_form'>
					 <select id="colorCalculationType" name="colorCalculationType" 
					 onChange="updateColorCalculationType(this)"/>
					    <option value="static" <%=(measure.getColurCalculatorType().equalsIgnoreCase("static")?"selected":"")%> />Static
					    <option value="grad" <%=(measure.getColurCalculatorType().equalsIgnoreCase("grad")?"selected":"")%> />Gradient
					  </select>
				</div>
							
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						OB Colour
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="hidden" style='width:190px;' 
							name="obColor" id="obColor" value="<%=measure.getColurOutboundCol() %>" maxlength="17"/>
							
					<span class='portlet-form-input-field'
					id="obColorBox"
					style="disply:block;background-color:<%=measure.getColurOutboundCol()%>;margin-left:1px;margin-bottom:1px;border:1px solid #000;cursor:pointer;" 
					onclick="showColorPicker(this,updateObColor)">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>				
					
				</div>
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						NV Colour
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="hidden" style='width:190px;' 
							name="nvColor" id="nvColor" value="<%=measure.getColurNullCol() %>" maxlength="17"/>
							
					<span class='portlet-form-input-field'
					id="nvColorBox"
					style="disply:block;background-color:<%=measure.getColurNullCol()%>;margin-left:1px;margin-bottom:1px;border:1px solid #000;cursor:pointer;" 
					onclick="showColorPicker(this,updateNvColor)">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				</div>
											
			</div>
		</td>
	</tr>
</table>

<p>

<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<!--  Static COLOURS Wizard																												     -->
<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<div id="staticColorCalculationParams" style="display:'<%=(measure.getColurCalculatorType().equalsIgnoreCase("static")?"inline":"none")%>';">
	<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							Range
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field'  type="text" style='width:230px;' 
							name="colorRange" id="levelNameTxt" value=""<%=(measure.getColurCalculatorParameters().getProperty("range")==null?"":measure.getColurCalculatorParameters().getProperty("range")) %>" maxlength="20"/>
					</div>											
				</div>
			</td>
		</tr>
	</table>
</div>

<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<!--  Grad COLOURS Wizard																												     -->
<!-- --------------------------------------------------------------------------------------------------------------------------------------- -->
<div id="gradColorCalculationParams" style="display:'<%=(measure.getColurCalculatorType().equalsIgnoreCase("grad")?"inline":"none")%>';">
	<table width="100%" cellspacing="0" border="0px"  id = "newLevelBox" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							Base Color
						</span>
					</div>
					
					<div class='div_detail_form'>
						<input class='portlet-form-input-field' type="hidden" style='width:190px;' 
								name="colorBaseColor" id=colorBaseColor value="<%=(measure.getColurCalculatorParameters().getProperty("BASE_COLOR")==null?"":measure.getColurCalculatorParameters().getProperty("BASE_COLOR")) %>" maxlength="17"/>
								
						<span class='portlet-form-input-field'
						id="colorBaseBox"
						style="disply:block;background-color:<%=(measure.getColurCalculatorParameters().getProperty("BASE_COLOR")==null?"":measure.getColurCalculatorParameters().getProperty("BASE_COLOR"))%>;margin-left:1px;margin-bottom:1px;border:1px solid #000;cursor:pointer;" 
						onclick="showColorPicker(this,updateBaseColor)">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>				
					</div>							
				</div>
			</td>
		</tr>
	</table>
</div>

</div>

</form>


<!-- ============================================================================================================== -->





</body>

</html>
