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

<%@ page
	import="java.util.Map,java.util.HashMap,java.util.List,java.util.ArrayList"%>
<%@page
	import="it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.kpi.model.bo.ModelInstance"%>
<%@page import="it.eng.spagobi.kpi.model.bo.Model"%>
<%@page import="it.eng.spagobi.kpi.config.bo.Kpi"%>
<%@page import="it.eng.spagobi.kpi.threshold.bo.Threshold"%>
<%@page import="it.eng.spagobi.kpi.config.bo.KpiInstance"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%>
<%@page import="it.eng.spagobi.kpi.config.bo.Periodicity"%>
<%
	String messageIn = (String) aServiceRequest.getAttribute("MESSAGE");
	String id = (String) aServiceRequest.getAttribute("ID");
	
	String modelInstanceName = "";
	String modelInstanceDescription = "";
	
	String modelName = "";
	String modelDescription = "";
	String modelCode = "";
	String typeName = "";
	String typeDescription = "";
	List attributeList = null;
	
	Integer kpiId = null;
	Integer thresholdId = null;
	Integer chartTypeId = null;
	Integer periodicityId = null;
	String weight = "";
	

	String title = "";
	
    ConfigSingleton configure = ConfigSingleton.getInstance();
	SourceBean moduleBean = (SourceBean) configure
			.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME",
					"DetailModelInstanceModule");
	
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
		.getAttribute("DetailModelInstanceModule");
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
			
			KpiInstance aKpiInstance = modelInstance.getKpiInstance();
			
			if (aKpiInstance != null){
				kpiId = aKpiInstance.getKpi();
				thresholdId = aKpiInstance.getThresholdId();
				chartTypeId = aKpiInstance.getChartTypeId();
				periodicityId = aKpiInstance.getPeriodicityId();
				Double aWeight = aKpiInstance.getWeight();
				if (aWeight != null)
					weight = aWeight.toString(); 
			}
		}
	}

	Map formUrlPars = new HashMap();
	formUrlPars.put("PAGE", "ModelInstancePage");
	formUrlPars.put("MODULE", "DetailModelInstanceModule");
	formUrlPars.put("MESSAGE", messageSave);
	formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "ModelInstancePage");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
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
			key="<%=title%>" bundle="<%=messageBundle%>" /></td>
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
	<fieldset style="width:500px;">
	<legend><spagobi:message
	key="sbi.kpi.modelInstanceDefinition.label" bundle="<%=messageBundle%>" /></legend>
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
<div class='div_detail_form' style='height: 150px;'>
	<textarea name="modelInstanceDescription" cols="40" style='height: 110px;' class='portlet-text-area-field'><%=modelInstanceDescription%></textarea>
</div>
</div>

</fieldset>
<%
 	if (messageIn != null
 			&& messageIn
 					.equalsIgnoreCase(DelegatedDetailService.DETAIL_NEW)) {
 %>
 <fieldset style="width:500px;">
 <legend>
 <spagobi:message
	key="sbi.kpi.modelDefinition.label" bundle="<%=messageBundle%>" /></legend>
<div class="div_detail_area_forms">
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.name" bundle="<%=messageBundle%>" /> </span></div>
	 
<select class='portlet-form-field' name="KPI_MODEL_ID">
	<%
		List severityLevels = DAOFactory.getModelDAO().loadModelsRoot();
			Iterator itt = severityLevels.iterator();
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
 <fieldset style="width:500px;">
	<legend><spagobi:message
	key="sbi.kpi.modelDefinition.label" bundle="<%=messageBundle%>" /></legend>
<div class="div_detail_area_forms">
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.name" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelName" size="50"
	value="<%=modelName%>" maxlength="200" readonly disabled="disabled"></div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.description" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form' style='height: 150px;'>
	<textarea name="modelDescription" cols="40" style='height: 110px;' class='portlet-text-area-field' disabled="disabled"><%=modelDescription%></textarea>
</div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.code" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="modelCode" size="50"
	value="<%=modelCode%>" maxlength="200" readonly disabled="disabled"></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.model.typeName" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
<input class='portlet-form-input-field' type="text" name="typeName"
	size="50" value="<%=typeName%>" maxlength="200" readonly disabled="disabled"></div>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.model.typeDescription" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form' style='height: 150px;'>
	<textarea name="typeDescription" cols="40" style='height: 110px;' class='portlet-text-area-field' disabled="disabled"><%=typeDescription%></textarea>
</div>
</div>
</fieldset>

<fieldset style="width:500px;">
<legend><spagobi:message key="sbi.kpi.model.attributes"
	bundle="<%=messageBundle%>" /></legend>
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
	value="<%=attributeValue%>" maxlength="200" readonly disabled="disabled"></div>
<%
	}
%>
</div>
</fieldset>

<fieldset style="width:500px;">
<legend>
<spagobi:message key="sbi.kpi.label.kpiInstance"
	bundle="<%=messageBundle%>" />
</legend>

<div class="div_detail_area_forms">

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.kpi.name" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
<select class='portlet-form-field' name="KPI_ID">
<%
 
	if(kpiId == null) { %>
		<option value="-1"
			label="" selected>
		</option>
<%	}
	else {  %>
		<option value="-1"
			label="" selected>
		</option>
<%	} %>

	<%
	List kpiList = DAOFactory.getKpiDAO().loadKpiList();
	for (java.util.Iterator iterator = kpiList.iterator(); iterator
			.hasNext();) {
		String selected = "";
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

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.restoreKpiValue" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
<input type="checkbox" name="RESTORE_DEFAULT" >
</div>

<%if (kpiId != null) {%>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.thresholdName" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
<select class='portlet-form-field' name="THRESHOLD_ID">

<%
	if(thresholdId == null) { %>
		<option value="-1"
			label="" selected>
		</option>
	<%	}
	else {  %>
		<option value="-1"
			label="" selected>
		</option>
<%	} %>

<%
	List thresholdList = DAOFactory.getThresholdDAO().loadThresholdList();
	for (java.util.Iterator iterator = thresholdList.iterator(); iterator
			.hasNext();) {
		Threshold threshold = (Threshold) iterator.next();
		String selected = "";
		if(threshold.getId().equals(thresholdId)){
			selected = "selected";
		}
		else {
			selected = "";
	}
	%>
	<option value="<%=threshold.getId()%>"
		label="<%=threshold.getThresholdName()%>" <%=selected%>><%=threshold.getThresholdName()%>
	</option>
	<%
	}
	%>

</select>

</div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.weight" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="weight"
	size="10" value="<%=weight%>" maxlength="200" >&nbsp;*</div>

<hr>


<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.chartType" bundle="<%=messageBundle%>" /> </span></div>

<div class='div_detail_form'>	
<select  class='portlet-form-field' name="CHART_TYPE_ID">

<%
	if(chartTypeId == null) { %>
		<option value="-1"
			label="" selected>
		</option>
<%	}
	else {  %>
		<option value="-1"
			label="" selected>
		</option>
<%	} %>

	<%
		List ChartType = DAOFactory.getDomainDAO()
					.loadListDomainsByType("KPI_CHART");
	for (Iterator iterator = ChartType.iterator(); iterator.hasNext();) {
		Domain domain = (Domain) iterator.next();
		String selected = "";
		if(domain.getValueId().equals(chartTypeId)){
			selected = "selected";
		}
		else {
			selected = "";
		}
	%>
	<option value="<%=domain.getValueId()%>"
		label="<%=domain.getValueName()%>" <%=selected%>><%=domain.getValueName()%>
	</option>
	<%
		}
	%>
</select>
</div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.periodicity" bundle="<%=messageBundle%>" /> </span></div>
<div class='div_detail_form'>
	<select class='portlet-form-field' name="ID_KPI_PERIODICITY">
	<%
	if(periodicityId == null) { %>
		<option value="-1"
			label="" selected>
		</option>
<%	}
	else {  %>
		<option value="-1"
			label="" selected>
		</option>
<%	} %>
	
	<%
		List periodicityList = DAOFactory.getPeriodicityDAO().loadPeriodicityList();
		for (Iterator iterator = periodicityList.iterator(); iterator.hasNext();) {
		Periodicity periodicity = (Periodicity) iterator.next();
		String selected = "";
		if(periodicity.getIdKpiPeriodicity().equals(periodicityId)){
			selected = "selected";
		}
		else {
			selected = "";
		}
	%>
	<option value="<%=periodicity.getIdKpiPeriodicity()%>"
		label="<%=periodicity.getName()%>" <%=selected%>><%=periodicity.getName()%>
	</option>
	<%
		}
	%>
</select>
</div>

<%} %>
</div>
</fieldset>

<%
 	}
 %>

</form>

<spagobi:error />
<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>