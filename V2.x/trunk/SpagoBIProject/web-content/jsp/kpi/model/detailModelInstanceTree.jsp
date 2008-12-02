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
<%@page import="it.eng.spagobi.kpi.model.bo.Model"%>
<%@page import="it.eng.spagobi.kpi.model.utils.DetailModelInstanceUtil"%>
<%
	String messageIn = (String) aServiceRequest.getAttribute("MESSAGE");
	String modelId = (String) aServiceRequest.getAttribute("MODEL_ID");
	String parentId = (String) aServiceRequest.getAttribute("ID");
	
	String modelInstanceName = "";
	String modelInstanceDescription = "";
	
	String modelName = "";
	String modelDescription = "";
	String modelCode = "";
	String typeName = "";
	String typeDescription = "";
	List attributeList = null;
	

	String title = "TITLE";
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
				.getAttribute("DetailModelInstanceTreeModule");
		ModelInstance modelInst = (ModelInstance) moduleResponse.getAttribute("MODELINSTANCE");
		modelId = modelInst.getId().toString();
		messageIn = (String) moduleResponse.getAttribute("MESSAGE");
		messageSave = DelegatedDetailService.DETAIL_UPDATE;
	}
	
	
	
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
		SourceBean moduleResponse = (SourceBean) aServiceResponse
				.getAttribute("DetailModelInstanceTreeModule");
		ModelInstance modelInstance = (ModelInstance) moduleResponse.getAttribute("MODELINSTANCE");
		if (modelInstance != null) {
			modelInstanceName = modelInstance.getName();
			modelInstanceDescription = modelInstance.getDescription();
			Model aModel = modelInstance.getModel();
			
			if (aModel != null){
				modelName = aModel.getName();
				modelDescription = aModel.getDescription();
				modelCode = aModel.getCode();
				typeName = aModel.getTypeName();
				typeDescription = aModel.getTypeDescription();
				attributeList = aModel.getModelAttributes();
			}
			
		}
	}

	Map formUrlPars = new HashMap();
	formUrlPars.put("PAGE", "ModelInstanceTreePage");
	formUrlPars.put("MODULE", "DetailModelInstanceTreeModule");
	formUrlPars.put("MESSAGE", messageSave);
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "ModelInstanceTreePage");
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


<table width="100%"
  style="margin-top: 3px; margin-left: 3px; margin-right: 3px; margin-bottom: 5px;">
  <tr height="20">
    <td class="td_tree" ><!-- Column with the navigation tree --> <%
  // drawing the tree
  
 %> <spagobi:treeObjects moduleName="DetailModelInstanceTreeModule"
		htmlGeneratorClass="it.eng.spagobi.kpi.model.presentation.ModelInstanceStructureTreeHtmlGenerator" /></td>
    <td class='td_form'>

<form method='post' action='<%=formUrl%>' id='ModelForm'
	name='ModelForm'>
	<input type="hidden" name="MODEL_ID" value="<%=modelId%>">
	<input type="hidden" name="ID" value="<%=parentId%>">
<div class="div_detail_area_forms">

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.name" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelInstanceName" size="50"
	value="<%=modelInstanceName%>" maxlength="200"> &nbsp;*</div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.description" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelInstanceDescription"
	size="50" value="<%=modelInstanceDescription%>" maxlength="200"></div>
</div>

<div class="div_detail_area_forms">
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.name" bundle="<%=messageBundle%>" /> </span></div>
<%
 	if (messageIn != null
 			&& messageIn
 					.equalsIgnoreCase(DelegatedDetailService.DETAIL_NEW)) {
 %>
 
 
<select class='portlet-form-field' name="KPI_MODEL_ID">
	<%
		List modelList = DetailModelInstanceUtil.getCandidateModelChildren(Integer.parseInt(modelId));
			Iterator itt = modelList.iterator();
			while (itt.hasNext()) {
				Model model = (Model) itt.next();
				String selected = "";
	%>
	<option value="<%=model.getId()%>"
		label="<%=model.getName()%>" <%=selected%>><%=model.getName()%>
	</option>
	<%
		}
	%>
</select>

<%
	}
%>


<%
 	if (messageIn != null
 			&& messageIn
 					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
 %>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelName" size="50"
	value="<%=modelName%>" maxlength="200" readonly></div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.description" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelDescription"
	size="50" value="<%=modelDescription%>" maxlength="200" readonly></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.code" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelCode" size="50"
	value="<%=modelCode%>" maxlength="200" readonly></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.model.typeName" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>

<input class='portlet-form-input-field' type="text" name="typeName"
	size="50" value="<%=typeName%>" maxlength="200" readonly></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.model.typeDescription" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="typeDescription"
	size="50" value="<%=typeDescription%>" maxlength="200" readonly></div>
</div>
<spagobi:message key="sbi.kpi.model.attributes"
	bundle="<%=messageBundle%>" />

<div class="div_detail_area_forms">
<%
	List modelAttributesName = new ArrayList();
		for (int i = 0; attributeList != null
				&& i < attributeList.size(); i++) {
			String attributeName = "";
			String attributeValue = "";
			Integer attributeId;
			ModelAttribute modelAttribute = (ModelAttribute) attributeList
					.get(i);
			attributeName = modelAttribute.getName();
			attributeValue = modelAttribute.getValue();
			attributeId = modelAttribute.getId();
			modelAttributesName.add(attributeId);
%>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="<%=attributeName%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text"
	name='<%="M_ATTR" + attributeId.toString()%>' size="50"
	value="<%=attributeValue%>" maxlength="200" readonly></div>
<%
	}
%>
</div>

<%
 	}
 %>

</div>


</form>
</td>
</tr>
</table>

<spagobi:error />
<%@ include file="/jsp/commons/footer.jsp"%>