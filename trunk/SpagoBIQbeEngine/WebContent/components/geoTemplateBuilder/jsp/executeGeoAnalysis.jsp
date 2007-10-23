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


<%@ include file="../../../jsp/qbe_base.jsp" %>




<% 
	String template = null;
	String target_level = "0";
	String actionUrl = null;
	String mapCatalogueManagerUrl = null;
	

	SourceBean serviceRequest = requestContainer.getServiceRequest();
	SourceBean serviceResponse = responseContainer.getServiceResponse();


	Object spagoBiInfo = sessionContainer.getAttribute("spagobi"); 
	ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(sessionContainer);
	DataMartModel dm = (DataMartModel) sessionContainer.getAttribute("dataMartModel");
	MapConfiguration mapConfiguration = (MapConfiguration) sessionContainer.getAttribute("MAP_CONFIGURATION");
	
	template = (String)serviceResponse.getAttribute("template");
	target_level = (String)serviceResponse.getAttribute("target_level");
	actionUrl = (String)serviceResponse.getAttribute("action_url");
	mapCatalogueManagerUrl = (String)serviceResponse.getAttribute("mapCatalogueManagerUrl"); 
	
%>



<html>

<head>
	
</head>

<body>

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


<table  height="100%" border="0px" valign="top" style="background-color:#e6e6e6;">

<tr valign="top">
	<td width="85%" valign="top">
	<div id="geoPanel" valign="top">
		<iframe id="geoIFrame"
		        name="geoIFrame"
		        src=""
		        width="100%"
		        height="100%"
		        frameborder="3">
		</iframe>
		
		<form id="geoExecutionForm"
			  name="geoExecutionForm"
		      method="post"
		      action="<%=actionUrl%>"
		      target="geoIFrame">
		      <input type="hidden" name="type" value="object" />
		      <input type="hidden" name="template" value="<%=template%>" />
		      <input type="hidden" name="target_level" value="<%=target_level%>" />
		      <input type="hidden" name="mapCatalogueManagerUrl" value="<%=mapCatalogueManagerUrl%>" />
		      <input type="hidden" name="isSaveSubObjFuncActive" value="FALSE" />
		      <input type="hidden" name="NEW_SESSION" value="TRUE" />
		      
		      
		</form>			
	</div>
	</td>
</tr>

</table>

<script>

	var geoExecutionForm = document.getElementById('geoExecutionForm');
    geoExecutionForm.submit();
     
</script>


	

<div id="divSpanCurrent">
	<span id="currentScreen">DIV_GEO_VIEWER</span>
</div>


<%@include file="../../../jsp/qbefooter.jsp" %>

</body>

</html>