<!--
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
-->


<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.pamphlets.constants.PamphletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.pamphlets.bo.Pamphlet,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.pamphlets.bo.ConfiguredBIDocument,
				java.util.Map,
				java.util.Set,
				it.eng.spago.base.SessionContainer,
				it.eng.spago.base.ApplicationContainer,
				it.eng.spago.security.IEngUserProfile,
				it.eng.spago.workflow.api.IWorkflowEngine,
				it.eng.spago.workflow.api.IWorkflowConnection,
				it.eng.spago.workflow.api.IWorkflowAssignment,
				it.eng.spago.configuration.ConfigSingleton,
				it.eng.spago.base.SourceBean,
				java.io.File,
				it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao,
				it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl,
				java.util.HashMap,
				java.io.FileOutputStream" %>

<%  
   	// RETRIVE ACTIVITY KEY	
	String activityKey = (String)aServiceResponse.getAttribute("ActivityKey");   
	// BUILT URL TO DOWNLOAD THE DOCUMENT
	String contextAddress = it.eng.spagobi.utilities.GeneralUtilities.getSpagoBiContextAddress();
	String recoverUrl = contextAddress + "/PamphletsImageService?" +
			            "task=downloadFinalDocument&ActivityKey=" + activityKey;

	// BUILT BACK URL 
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_COLLABORATION_PAGE);
   	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   	// BUILT APPROVE URL 
   	PortletURL checkedDocUrl = renderResponse.createActionURL();
   	checkedDocUrl.setParameter("ACTION_NAME", "COMPLETE_OR_REJECT_ACTIVITY_ACTION");
   	checkedDocUrl.setParameter("CompleteActivity", "TRUE");
   	checkedDocUrl.setParameter("ActivityKey", activityKey);
   	checkedDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
%>




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "pamp.approval"  bundle="component_pamphlets_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.back" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/back.png")%>' 
      				 alt='<spagobi:message key = "pamp.back"  bundle="component_pamphlets_messages"/>' />
			</a>
		</td>
	</tr>
</table>

<br/>
<br/>


<script>
		function approve() {
			checkObj = document.getElementById("checkapproved");
			if(checkObj.checked) {
				formObj = document.getElementById("approveForm");
				formObj.submit();
			} else {
			 	alert('<spagobi:message key="pamp.selectCheckApprove"  bundle="component_pamphlets_messages"/>');
			}
		}
	</script>


<center>
<table style="width:300px">
	<tr>
		<td class='portlet-form-field-label'>
			<spagobi:message key="pamp.downloadDescr"  bundle="component_pamphlets_messages"/>
		</td>
		<td>
			<a href="<%=recoverUrl %>">
			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "pamp.download" bundle="component_pamphlets_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/download32.png")%>' 
	      				 alt='<spagobi:message key = "pamp.download"  bundle="component_pamphlets_messages"/>' />
	        </a>
		</td>
	<tr height="30px"><td colspan="2">&nbsp;</td></tr>
	<form id="approveForm" action="<%=checkedDocUrl.toString() %>" method="POST"/>
	<tr>
		<td class='portlet-form-field-label'>
			<spagobi:message key="pamp.finalDocApproved"  bundle="component_pamphlets_messages"/>
			<input id="checkapproved" type="checkbox" name="approved"/>
		</td>
		<td>
			<img title='<spagobi:message key = "pamp.approvalBotton" bundle="component_pamphlets_messages" />' 
      		 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/save32.png")%>' 
      		 alt='<spagobi:message key = "pamp.approvalBotton"  bundle="component_pamphlets_messages"/>' 
      		 onclick="javascript:approve();" />
		</td>
	</tr>	
	</form>
</table>
</center>


<br/>
<br/>

	















