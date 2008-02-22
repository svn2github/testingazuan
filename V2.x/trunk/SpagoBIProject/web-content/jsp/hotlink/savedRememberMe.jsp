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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%
String normaleMsg = (String) aServiceResponse.getAttribute("NORMAL_MSG_CODE");
String errorMsg = (String) aServiceResponse.getAttribute("ERROR_MSG_CODE");
if (normaleMsg != null) {
	%>
	<div class="div_form_container" style="margin-right:10px;">
		<div class="div_form_row" >
			<div class='div_form_label_large'>
				<span class='portlet-form-field-label'>
					<spagobi:message key = "<%= normaleMsg %>" />
				</span>
			</div>
		</div>
	</div>
	<%
} else {
	%>
	<%= errorMsg %>
	<%
}
%>

<%@ include file="/jsp/commons/footer.jsp"%>