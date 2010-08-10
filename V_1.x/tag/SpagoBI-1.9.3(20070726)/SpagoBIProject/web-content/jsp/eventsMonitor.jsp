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

<spagobi:list moduleName="ListEventsLogModule" />

<%--
<%@ page import="it.eng.spagobi.constants.SpagoBIConstants,
         		it.eng.spagobi.events.EventsManager,
         		it.eng.spagobi.bo.EventLog,
         		it.eng.spago.base.SourceBean,
         		java.util.List,
         		javax.portlet.PortletURL,
         		it.eng.spago.navigation.LightNavigationManager,
         		it.eng.spagobi.constants.SpagoBIConstants,
         		it.eng.spagobi.utilities.PortletUtilities"
%>

<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("PortletEventsMonitorModule"); 
	List firedEventsList = (List) moduleResponse.getAttribute("firedEventsList");
	if(firedEventsList.size() == 0) {			
%>
	<P><H3><I>Events log is empty !!!</I></H3>
<%
		} else {
			// build the refresh button
			PortletURL refreshUrl = renderResponse.createActionURL();
			refreshUrl.setParameter("PAGE", "EVENTS_MONITOR_PAGE");			
			refreshUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
%>


<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			Events Monitor
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a style="text-decoration:none;" href='<%=refreshUrl.toString()%>'> 
				<img width="20px" height="20px"
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png")%>' 
					name='refresh' 
					alt='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' 
					title='<%=PortletUtilities.getMessage("SBIExecution.refresh", "messages")%>' /> 
			</a>
		</td>
	</tr>
</table>

<TABLE style='width:100%;margin-top:1px'>
<TR>
<TD class='portlet-section-header' valign='center' align=left >Id</TD>
<TD class='portlet-section-header' valign='center' align=left >Date</TD>
<TD class='portlet-section-header' valign='center' align=left >User</TD>
<TD class='portlet-section-header' valign='center' align=left >Description</TD>
<TD class='portlet-section-header' valign='center' align=left >&nbsp;</TD>
</TR>
<%
		
			PortletURL detailUrl = renderResponse.createActionURL();
   			detailUrl.setParameter("PAGE", "DetailEventLogPage");   	
   			detailUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "false");
			boolean alternate = false;
        	String rowClass;
			for(int i = 0; i < firedEventsList.size(); i++) {
				rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
            	alternate = !alternate;
				EventLog firedEvent = (EventLog)firedEventsList.get(i);
				detailUrl.setParameter("id", firedEvent.getId().toString());
				detailUrl.setParameter("user", firedEvent.getUser());
				long timelong = firedEvent.getDate().getTime();
				String timelongStr = Long.valueOf(timelong).toString();
				detailUrl.setParameter("date", timelongStr);
%>
	<tr>
		<td class='<%=rowClass%>' valign='top' ><%=firedEvent.getId()%></td>
		<td class='<%=rowClass%>' valign='top' ><%=firedEvent.getDate()%></td>
		<td class='<%=rowClass%>' valign='top' ><%=firedEvent.getUser()%></td>
		<td class='<%=rowClass%>' valign='top' ><%=firedEvent.getDesc()%></td>
		<td class='<%=rowClass%>' valign='top' >
			<a title="Detail" class="linkOperation" href="<%=detailUrl.toString() %>">		
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif")%>'/>
			</a>
		</td>
	</tr>
<%		
			}
%>
	</table>
<%
		}
%>
--%>
