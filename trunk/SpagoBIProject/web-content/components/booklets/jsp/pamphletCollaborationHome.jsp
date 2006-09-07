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

<%@ page import="it.eng.spago.workflow.worklist.tags.ListTag" %>

<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/css/worklist.css")%>' 
      type='text/css' />



<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section-no-buttons' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "pamp.activitylist"  bundle="component_pamphlets_messages"/>
		</td>
	</tr>
</table>

<br/>

<spagobi:worklist actionName="WORKLIST_ACTION" 
                  channel="<%=ListTag.PORTLET_CHANNEL %>" 
                  imgFolder="/components/pamphlets/img" 
                  showTitle="false" />








