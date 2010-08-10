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

<%@ page import="it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.booklets.constants.BookletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.booklets.bo.ConfiguredBIDocument,
				it.eng.spagobi.constants.SpagoBIConstants,
				it.eng.spagobi.booklets.bo.WorkflowConfiguration" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(BookletsConstants.BOOKLET_COLLABORATION_MODULE); 
   String execMessage = (String)moduleResponse.getAttribute(BookletsConstants.EXECUTION_MESSAGE);
   
   Map backUrlPars = new HashMap();
   backUrlPars.put("LIGHT_NAVIGATOR_BACK_TO", "1");
   String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>




	<!-- ********************* TITOLO **************************  -->


<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key="book.Execution" bundle="component_booklets_messages" />
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%= backUrl %>'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
	      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/back.png")%>' 
	      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
		</tr>
	</table>
	
	<br/>
	<br/>
	
	<center>
		<span class="portlet-form-field-label">
			<%= execMessage %>
		</span>
	</center>
	
	
	<br/>
	<br/>
	




	








