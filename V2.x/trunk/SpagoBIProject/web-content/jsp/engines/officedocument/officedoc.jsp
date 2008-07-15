<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<% 
SourceBean sbModuleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
ExecutionInstance instanceO = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
String execContext = instanceO.getExecutionModality();
if (execContext == null || !execContext.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)) {
	%>
	<%@ include file="/jsp/analiticalmodel/execution/header.jsp"%>
	<%
}

// identity string for object of the page
String strUuid = instanceO.getExecutionId();
//SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
BIObject biObj = instanceO.getBIObject();
%>


<%-- Start execution iframe --%>
<div id="divIframe<%= strUuid %>" style="width:100%;overflow=auto;border: 0;display:inline;"> <!-- float:left; -->
	<iframe id="iframeexec<%= strUuid %>" name="iframeexec<%= strUuid %>" src="<%= GeneralUtilities.getSpagoBiHost()+GeneralUtilities.getSpagoBiContext() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?ACTION_NAME=GET_OFFICE_DOC&NEW_SESSION=TRUE&userId=" + userProfile.getUserUniqueIdentifier().toString() + "&documentId=" + biObj.getId().toString() %>" style="width:100%;height:300px;z-index:0;" frameborder="0" >
	</iframe>
</div>    
<%-- End execution iframe --%>

<%@ include file="/jsp/commons/footer.jsp"%>