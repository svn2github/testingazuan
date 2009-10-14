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

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%>
<%@page import="it.eng.spagobi.kpi.model.bo.Model"%>
<%@page import="it.eng.spagobi.kpi.config.bo.Kpi"%>
<%@page import="it.eng.spagobi.kpi.model.bo.ModelAttribute"%>
<%@page import="java.util.ArrayList"%>
<%  
	String messageIn = (String) aServiceRequest.getAttribute("MESSAGE");
	String modelId = (String) aServiceRequest.getAttribute("MODEL_ID");
	String parentId = (String) aServiceRequest.getAttribute("ID");
	String modelName = "";
	String modelCode = "";
	String modelDescription = "";
	Integer kpiId = null;

	String typeName = "";
	String typeDescription = "";
	List attributeList = null;

	String title = "";
	
    ConfigSingleton configure = ConfigSingleton.getInstance();
	SourceBean moduleBean = (SourceBean) configure
			.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME",
					"DetailModelTreeModule");
	
	if (moduleBean.getAttribute("CONFIG.TITLE") != null)
		title = (String) moduleBean.getAttribute("CONFIG.TITLE");
	
	String messageSave = "";

	// DETAIL_SELECT
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
		messageSave = DelegatedDetailService.DETAIL_UPDATE;
	}
	// DETAIL_UPDATE
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_UPDATE)) {
		SourceBean moduleResponse = (SourceBean) aServiceResponse
		.getAttribute("DetailModelTreeModule");
		messageIn = (String) moduleResponse.getAttribute("MESSAGE");
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
		.getAttribute("DetailModelTreeModule");
		Model model = (Model) moduleResponse.getAttribute("MODEL");
		if(model.getId() != null){
			modelId = model.getId().toString();
			messageIn = DelegatedDetailService.DETAIL_SELECT;
			messageSave = DelegatedDetailService.DETAIL_UPDATE;
		} else {
			messageIn = DelegatedDetailService.DETAIL_SELECT;
			messageSave = DelegatedDetailService.DETAIL_INSERT;
		}
	}

	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
		SourceBean moduleResponse = (SourceBean) aServiceResponse
		.getAttribute("DetailModelTreeModule");
		Model model = (Model) moduleResponse.getAttribute("MODEL");

		if (model != null) {
			modelName = model.getName();
			modelCode = model.getCode();
			modelDescription = model.getDescription();
			typeName = model.getTypeName();
			typeDescription = model.getTypeDescription();
			attributeList = model.getModelAttributes();
			kpiId = model.getKpiId();
		}
		// if messageSave is INSERT set the message insert to NEW		
		if(messageSave.equals(DelegatedDetailService.DETAIL_INSERT))
			messageIn = DelegatedDetailService.DETAIL_NEW;
		// 
	}

	Map formUrlPars = new HashMap();
	formUrlPars.put("PAGE", "ModelTreePage");
	formUrlPars.put("MODULE", "DetailModelTreeModule");
	formUrlPars.put("MESSAGE", messageSave);
	formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "ModelTreePage");
	if(messageSave.equals(DelegatedDetailService.DETAIL_UPDATE)){
		backUrlPars.put("ID", parentId);
	}
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);


	String messageBundle = "component_kpi_messages";
%>


<table class='header-table-portlet-section'>
  <tr class='header-row-portlet-section'>
    <td class='header-title-column-portlet-section'
      style='vertical-align: middle; padding-left: 5px;'><spagobi:message
      key="<%=title%>" bundle="<%=messageBundle%>" /></td>
    <td class='header-empty-column-portlet-section'>&nbsp;</td>
    <td class='header-button-column-portlet-section'><a
      href="javascript:document.getElementById('ModelTreeForm').submit()">
    <img class='header-button-image-portlet-section'
      title='<spagobi:message key = "sbi.kpi.button.save.title"  bundle="<%=messageBundle%>"/>'
      src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>'
      alt='<spagobi:message key = "sbi.kpi.button.save.title"  bundle="<%=messageBundle%>"/>' /> </a></td>
    <td class='header-button-column-portlet-section'><a
      href='<%=backUrl%>'> <img
      class='header-button-image-portlet-section'
      title='<spagobi:message key = "sbi.kpi.button.back.title" bundle="<%=messageBundle%>"/>'
      src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>'
      alt='<spagobi:message key = "sbi.kpi.button.back.title" bundle="<%=messageBundle%>"/>' /> </a></td>
  </tr>
</table>


<table width="100%"
  style="margin-top: 3px; margin-left: 3px; margin-right: 3px; margin-bottom: 5px;">
  <tr height="20">
    <td class="td_tree" ><!-- Column with the navigation tree --> <%
  // drawing the tree
  
 %> <spagobi:treeObjects moduleName="DetailModelTreeModule"
		htmlGeneratorClass="it.eng.spagobi.kpi.model.presentation.ModelStructureTreeHtmlGenerator" /></td>
    <td class='td_form'>
    
    <form method='post' action='<%=formUrl%>' id='ModelTreeForm'
	name='ModelForm'>
	<input type="hidden" name="MODEL_ID" value="<%=modelId%>">
	<input type="hidden" name="ID" value="<%=parentId%>">

<fieldset  style="width:500px;">
<legend>
<spagobi:message
	key="sbi.kpi.modelDefinition.label" bundle="<%=messageBundle%>" />
</legend>
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
<div class='div_detail_form' style='height: 150px;'>
	<textarea name="modelDescription" cols="40" style='height: 110px;' class='portlet-text-area-field'><%=modelDescription%></textarea>
</div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.code" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelCode" size="50"
	value="<%=modelCode%>" maxlength="200"></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.model.typeName" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
<%
	if (messageIn != null
			&& messageIn
					.equalsIgnoreCase(DelegatedDetailService.DETAIL_SELECT)) {
%>
<input class='portlet-form-input-field' type="text" name="typeName"
	size="50" value="<%=typeName%>" maxlength="200" disabled="disabled"></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.model.typeDescription" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form' style='height: 150px;'>
	<textarea name="typeDescription" cols="40" style='height: 110px;' class='portlet-text-area-field' disabled="disabled"><%=typeDescription%></textarea>
</div>

</div>


</fieldset>

<fieldset  style="width:500px;">
<legend>
<spagobi:message key="sbi.kpi.model.attributes"
	bundle="<%=messageBundle%>" />
</legend>


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
	value="<%=attributeValue%>" maxlength="200"></div>
<%
	}
%>
</div>
</fieldset>

<input type="hidden" name="MODELATTRIBUTESNAME"
	value="<%=modelAttributesName.toString()%>"> <%
 	}
 %> <%
 	if (messageIn != null
 			&& messageIn
 					.equalsIgnoreCase(DelegatedDetailService.DETAIL_NEW)) {
 %>
<select class='portlet-form-field' name="modelTypeId">
	<%
		List severityLevels = DAOFactory.getDomainDAO()
					.loadListDomainsByType("MODEL_NODE");
			Iterator itt = severityLevels.iterator();
			while (itt.hasNext()) {
				Domain domain = (Domain) itt.next();
				String selected = "";
	%>
	<option value="<%=domain.getValueId()%>"
		label="<%=domain.getValueName()%>" <%=selected%>><%=domain.getValueName()%>
	</option>
	<%
		}
	%>
</select>
</div>
<%
	}
%>
<fieldset  style="width:500px;">
<legend>
<spagobi:message
	key="sbi.kpi.kpiDefinition.label" bundle="<%=messageBundle%>" />
</legend>
<div class="div_detail_area_forms">
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.kpi.name" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
<select class='portlet-form-field' name="kpiId">
<%
	String selected = "";

	if(kpiId == null) {
		selected = "selected";
	}
	else {
		selected = "";
	}
%>
	<option value="-1"
		label="" <%=selected%>>
	</option>

	<%
	List kpiList = DAOFactory.getKpiDAO().loadKpiList();
	for (java.util.Iterator iterator = kpiList.iterator(); iterator
			.hasNext();) {
		Kpi kpi = (Kpi) iterator.next();
		if(kpi.getKpiId().equals(kpiId)){
			selected = "selected";
		}
		else {
			selected = "";
		}
	%>
	<option value="<%=kpi.getKpiId()%>"
		label="<%=kpi.getKpiName()%>" <%=selected%>><%=kpi.getKpiName()%>
	</option>
	<%
	}
	%>

</select>

</div>
</div>
</fieldset>

</form>
    </td>
  </tr>
</table>
      
<spagobi:error />
<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>