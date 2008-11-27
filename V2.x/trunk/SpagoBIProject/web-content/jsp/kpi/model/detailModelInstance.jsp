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

<%@ page
	import="java.util.Map,java.util.HashMap,java.util.List,java.util.ArrayList"%>
<%@page
	import="it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.kpi.model.bo.ModelInstance"%>
<%
	String messageIn = (String) aServiceRequest.getAttribute("MESSAGE");
	String id = (String) aServiceRequest.getAttribute("ID");
	
	String modelName = "";
	String modelDescription = "";

	String title = "";
	String messageSave = "";

	// DETAIL_SELECT
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
		messageSave = DelegatedDetailService.DETAIL_UPDATE;
	}
	//DETAIL_NEW
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_NEW)) {
		messageSave = DelegatedDetailService.DETAIL_INSERT;
	}
	//DETAIL_INSERT
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_INSERT)) {
		SourceBean moduleResponse = (SourceBean) aServiceResponse
				.getAttribute("DetailModelInstanceModule");
		ModelInstance modelInst = (ModelInstance) moduleResponse.getAttribute("MODELINSTANCE");
		id = modelInst.getId().toString();
		messageIn = (String) moduleResponse.getAttribute("MESSAGE");
		messageSave = DelegatedDetailService.DETAIL_UPDATE;
	}
	
	
	
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
		SourceBean moduleResponse = (SourceBean) aServiceResponse
				.getAttribute("DetailModelInstanceModule");
		ModelInstance model = (ModelInstance) moduleResponse.getAttribute("MODELINSTANCE");
		if (model != null) {
			modelName = model.getName();
			modelDescription = model.getDescription();
		}
	}

	Map formUrlPars = new HashMap();
	formUrlPars.put("PAGE", "ModelInstancePage");
	formUrlPars.put("MODULE", "DetailModelInstanceModule");
	formUrlPars.put("MESSAGE", messageSave);
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "ModelInstancePage");
	backUrlPars
			.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);

	String messageBundle = "component_kpi_messages";
%>



<%@page import="it.eng.spago.navigation.LightNavigationManager"%>

<%@page import="java.util.ArrayList"%>
<%@page import="it.eng.spagobi.kpi.model.bo.ModelAttribute"%><table
	class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
			style='vertical-align: middle; padding-left: 5px;'><spagobi:message
			key="<%=title%>" /></td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'><a
			href="javascript:document.getElementById('ModelForm').submit()">
		<img class='header-button-image-portlet-section'
			title='<spagobi:message key = "sbi.kpi.button.save.title" bundle="<%=messageBundle%>" />'
			src='<%=urlBuilder.getResourceLink(request,
									"/img/save.png")%>'
			alt='<spagobi:message key = "sbi.kpi.button.save.title" bundle="<%=messageBundle%>"/>' />
		</a></td>
		<td class='header-button-column-portlet-section'><a
			href='<%=backUrl%>'> <img
			class='header-button-image-portlet-section'
			title='<spagobi:message key = "sbi.kpi.button.back.title" bundle="<%=messageBundle%>" />'
			src='<%=urlBuilder.getResourceLink(request,
									"/img/back.png")%>'
			alt='<spagobi:message key = "sbi.kpi.button.back.title" bundle="<%=messageBundle%>"/>' />
		</a></td>
	</tr>
</table>

<form method='post' action='<%=formUrl%>' id='ModelForm'
	name='ModelForm'><input type="hidden" name="ID" value="<%=id%>">
<div class="div_detail_area_forms">

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.name" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelName" size="50"
	value="<%=modelName%>" maxlength="200"> &nbsp;*</div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.description" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelDescription"
	size="50" value="<%=modelDescription%>" maxlength="200"></div>
</div>

</form>

<spagobi:error />
<%@ include file="/jsp/commons/footer.jsp"%>