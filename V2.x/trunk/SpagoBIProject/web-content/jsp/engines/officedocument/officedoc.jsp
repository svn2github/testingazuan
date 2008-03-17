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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ include file="/jsp/analiticalmodel/execution/header.jsp"%>

<%-- Start execution iframe --%>
<div id="divIframe<%= uuid %>" style="width:100%;overflow=auto;border: 0;display:inline;"> <!-- float:left; -->
	<iframe id="iframeexec<%= uuid %>" name="iframeexec<%= uuid %>" src="<%= GeneralUtilities.getSpagoBiContextAddress() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?ACTION_NAME=GET_OFFICE_DOC&NEW_SESSION=TRUE&userId=" + userProfile.getUserUniqueIdentifier().toString() + "&documentId=" + obj.getId().toString() %>" style="width:100%;height:300px;z-index:0;" frameborder="0" >
	</iframe>
</div>    
<%-- End execution iframe --%>

<%@ include file="/jsp/commons/footer.jsp"%>