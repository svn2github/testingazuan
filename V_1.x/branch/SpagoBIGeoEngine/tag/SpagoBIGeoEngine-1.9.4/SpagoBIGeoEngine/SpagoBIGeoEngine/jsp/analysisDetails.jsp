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
<%@ page import="it.eng.spagobi.geo.action.*"%>







<% 
	RequestContainer requestContainer = null;
	ResponseContainer responseContainer = null;
	SessionContainer sessionContainer = null;   
	
	requestContainer = RequestContainerAccess.getRequestContainer(request);
	responseContainer = ResponseContainerAccess.getResponseContainer(request);
	sessionContainer = requestContainer.getSessionContainer();
	
	GeoAction.SubObjectDetails subObjectDetails = (GeoAction.SubObjectDetails)sessionContainer.getAttribute("SUBOBJECT");
	
	
	
	
%>



<html>

<head>
	<link rel="stylesheet" href ="../css/spagobi.css" type="text/css"/>
	<link rel="stylesheet" href ="../css/jsr168.css" type="text/css"/>
	<link rel="stylesheet" href ="../../../css/external.css" type="text/css"/>
</head>

<body>

<script>	
	
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
			Analysis Details
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
					
				
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='save' value='true' class='header-button-image-portlet-section'
				   src='../img/save.png'
      			   title='Save' 
      			   onclick='submit("SAVE_ANALYSIS_ACTION")'/> 
			
		</td>
		
		<td class='header-button-column-portlet-section'>
			<input type='image' name='saveAndGoBack' id='saveAndGoBack' value='true' class='header-button-image-portlet-section'
				   src='../img/saveAndGoBack.png'
      			   title='Save and return' 
      			   onclick='submit("SAVE_ANALYSIS_AND_EXIT_ACTION")'/> 
		</td>
		
		<td class='header-button-column-portlet-section'>
			<input type='image' name='Exit' id='Exit' value='true' class='header-button-image-portlet-section'
				   src='../img/back.png'
      			   title='Exit' 
      			   onclick='submit("EXIT_ANALYSIS_DETAILS_ACTION")'/> 
		</td>
	</tr>
</table>

<div class='div_background_no_img'>

<form method='POST' action='' 
	  id='mainForm' name='mainForm'>

	  
<table width="100%" cellspacing="0" border="0" id = "newMeasureBox" >
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
							name="name" id="name" value="<%=subObjectDetails.getName()%>" maxlength="20"/>
				</div>
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Description
					</span>
				</div>
				<div class='div_detail_form'>
					<input class='portlet-form-input-field' type="text" style='width:230px;' 
							name="description" id="description" value="<%=subObjectDetails.getDescription()%>" maxlength="20"/>
				</div>
				
				<div class='div_detail_label'>
					<span class='portlet-form-field-label'>
						Scope
					</span>
				</div>
				<div class='div_detail_form'>
					 <select id="scope" name="scope"/>
					    <option value="Public" <%=subObjectDetails.getScope().equalsIgnoreCase("public")?"selected":""%> />Public
					    <option value="Private" <%=subObjectDetails.getScope().equalsIgnoreCase("private")?"selected":""%> />Private
					  </select>
				</div>
				
				
			</div>
		</td>
	</tr>
</table>




<!-- ============================================================================================================== -->





</body>

</html>
