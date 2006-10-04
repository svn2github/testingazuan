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
				it.eng.spagobi.booklets.constants.BookletsConstants,
				it.eng.spagobi.constants.SpagoBIConstants,
				it.eng.spagobi.utilities.GeneralUtilities" %>

<%
	// RETRIVE ACTIVITY KEY	
	String activityKey = (String)aServiceResponse.getAttribute("ActivityKey");
    if(activityKey==null)
    	activityKey = (String)aServiceRequest.getAttribute("ActivityKey");
	// BUILT URL TO DOWNLOAD THE DOCUMENT
	String recoverUrl = BookletServiceUtils.getBookletServiceUrl() + "?" +
						BookletsConstants.BOOKLET_SERVICE_TASK + "=" +
						BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_FINAL_DOC + "&" +
						SpagoBIConstants.ACTIVITYKEY + "=" + activityKey;
	
	// BUILT BACK URL 
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "WorkflowToDoListPage");
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   	
	// BUILT APPROVE URL 
   	PortletURL checkedDocUrl = renderResponse.createActionURL();
   	checkedDocUrl.setParameter("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
   	checkedDocUrl.setParameter("OPERATION", BookletsConstants.OPERATION_APPROVE_PRESENTATION);
   	checkedDocUrl.setParameter(SpagoBIConstants.ACTIVITYKEY, activityKey);
   	checkedDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	
   	//checkedDocUrl.setParameter("PAGE", "CompleteOrRejectActivityPage");
   	//checkedDocUrl.setParameter("CompleteActivity", "TRUE");
   	//checkedDocUrl.setParameter(SpagoBIConstants.ACTIVITYKEY, activityKey);
   	//checkedDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	
%>




<%@page import="it.eng.spagobi.booklets.utils.BookletServiceUtils"%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "book.approval"  bundle="component_booklets_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/back.png")%>' 
      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
			</a>
		</td>
	</tr>
</table>

<br/>

<form id="approveForm" action="<%=checkedDocUrl.toString() %>" method="POST"/>
<table style="width:100%">
	<tr>
		<td width="2%">&nbsp;</td>
		<td width="47%" style="padding:10px;" class="div_detail_area_forms">
		    <span class='portlet-form-field-label'>
				<spagobi:message key="book.downloadDescr"  bundle="component_booklets_messages"/>
			</span>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="<%=recoverUrl %>">
			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.download" bundle="component_booklets_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/download32.png")%>' 
	      				 alt='<spagobi:message key = "book.download"  bundle="component_booklets_messages"/>' />
	        </a>
		</td>
		<td width="2%">&nbsp;</td>
		<td width="47%" style="padding:10px;" class="div_detail_area_forms">
			
			<table>
				<tr>
					<td class='portlet-form-field-label'>
						<spagobi:message key="book.finalDocApproved"  bundle="component_booklets_messages"/>
					</td>
					<td class='portlet-form-field-label'>
						<input type="radio" name="approved" value="true" default>True<br>
					</td>
					<td class='portlet-form-field-label'>
						<input type="radio" name="approved" value="false">False<br>
					</td>
					<td class='portlet-form-field-label'>
						<input type="image" 
			       			   title='<spagobi:message key = "book.approvalBotton" bundle="component_booklets_messages" />' 
      		 	   			   src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/save32.png")%>' 
      		 	  			   alt='<spagobi:message key = "book.approvalBotton"  bundle="component_booklets_messages"/>' />
					</td>
				</tr>
			</table>
			
		</td>
		<td width="2%">&nbsp;</td>
	</tr>
</table>
</form>
	

<br/>
<br/>

	















