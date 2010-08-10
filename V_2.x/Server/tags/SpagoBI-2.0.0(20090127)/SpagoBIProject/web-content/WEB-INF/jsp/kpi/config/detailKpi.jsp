<%--
    Copyright 2008 Engineering Ingegneria Informatica S.p.A.

    This file is part of Spago4Q.

    Spago4Q is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published 
    by the Free Software Foundation; either version 3 of the License, or
    any later version.

    Spago4Q is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
--%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>

<%@ page import="java.util.Map,java.util.HashMap"%>
<%@ page	import="it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService"%>
<%@ page import="it.eng.spagobi.kpi.config.bo.Kpi"%>
<%
	String title = "";
	String id = "";
	String name = "";
	String documentLabel = "";
	String code = "";
	String metric = "";
	String description = "";
	String weight = "";
	Integer ds_id = null;
	Integer threshold_id = null;

    String messageBunle = "component_kpi_messages"; 

    ConfigSingleton configure = ConfigSingleton.getInstance();
	SourceBean moduleBean = (SourceBean) configure
			.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME",
					"DetailKpiModule");
	
	if (moduleBean.getAttribute("CONFIG.TITLE") != null)
		title = (String) moduleBean.getAttribute("CONFIG.TITLE");
	
	String messageIn = (String) aServiceRequest.getAttribute("MESSAGE");
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
		.getAttribute("DetailKpiModule");
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
				.getAttribute("DetailKpiModule");
		Kpi kpi = (Kpi) moduleResponse.getAttribute("KPI");
		if(kpi.getKpiId() != null) {
			id = kpi.getKpiId().toString();
			messageIn = (String) moduleResponse.getAttribute("MESSAGE");
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
				.getAttribute("DetailKpiModule");
		Kpi kpi = (Kpi) moduleResponse.getAttribute("KPI");
		if (kpi != null) {
			if(kpi.getKpiId()!=null)
				id = kpi.getKpiId().toString();
			if(kpi.getKpiName()!= null)
				name = kpi.getKpiName();
			if(documentLabel!= null)
				documentLabel = kpi.getDocumentLabel();
			if(code!=null)
				code = kpi.getCode();
			if(metric != null)
				metric = kpi.getMetric();
			if(description != null)
				description = kpi.getDescription();
			if (kpi.getStandardWeight() != null)
				weight = kpi.getStandardWeight().toString();
			if(kpi.getKpiDs()!=null)
				ds_id = kpi.getKpiDs().getId();
			else
				ds_id = null;
			if (kpi.getThreshold()!=null)
				threshold_id = kpi.getThreshold().getId();
			else
				threshold_id = null;	
		}
	}
	
	
	Map formUrlPars = new HashMap();
//	if(ChannelUtilities.isPortletRunning()) {
		formUrlPars.put("PAGE", "KpiPage");
		formUrlPars.put("MODULE", "DetailKpiModule");
		formUrlPars.put("MESSAGE", messageSave);
		formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
//	}
	
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	
	
	
	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "KpiPage");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>

<%@page import="it.eng.spago.navigation.LightNavigationManager"%>

<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.tools.dataset.bo.IDataSet"%> 


<%@page import="it.eng.spagobi.kpi.threshold.bo.Threshold"%><table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
			style='vertical-align: middle; padding-left: 5px;'>
			<spagobi:message key="<%=title%>" bundle="<%=messageBunle%>" /></td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'><a
			href="javascript:document.getElementById('kpiForm').submit()"> <img
			class='header-button-image-portlet-section'
			title='<spagobi:message key = "sbi.kpi.button.save.title" bundle="<%=messageBunle%>" />'
			src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>'
			alt='<spagobi:message key = "sbi.kpi.button.save.title" bundle="<%=messageBunle%>" />' /> </a></td>
		<td class='header-button-column-portlet-section'><a
			href='<%=backUrl%>'> <img
			class='header-button-image-portlet-section'
			title='<spagobi:message key = "sbi.kpi.button.back.title" bundle="<%=messageBunle%>"/>'
			src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>'
			alt='<spagobi:message key = "sbi.kpi.button.back.title" bundle="<%=messageBunle%>"/>' /> </a></td>
	</tr>
</table>

<form method='POST' action='<%=formUrl%>' id='kpiForm' name='kpiForm'>
<input type='hidden' value='<%=id%>' name='id' />

<div class="div_detail_area_forms">
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.name" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="name" size="50"
	value="<%=name%>" maxlength="200"> &nbsp;*</div>


<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.description" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form' style='height: 150px;'>
<textarea name="description" cols="40" style='height: 110px;' class='portlet-text-area-field'><%=description%></textarea></div>


<div class='div_detail_label'><span
  class='portlet-form-field-label'> <spagobi:message
  key="sbi.kpi.label.code" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'><input
  class='portlet-form-input-field' type="text" name="code" size="50"
  value="<%=code%>" maxlength="200"></div>


<div class='div_detail_label'><span
  class='portlet-form-field-label'> <spagobi:message
  key="sbi.kpi.label.metric" bundle="<%=messageBunle%>"/> </span></div>
<div style='height: 150px;' class='div_detail_form'><textarea
  style='height: 110px;' class='portlet-text-area-field' name='metric'
  cols='40'>
<%=metric%></textarea></div>


<div class='div_detail_label'><span
  class='portlet-form-field-label'> <spagobi:message
  key="sbi.kpi.label.weight" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form' style="height:40px;"><input
  class='portlet-form-input-field' type="text" name="weight" size="50"
  value="<%=weight%>" maxlength="200" ></div>
  
  <div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.thresholdName" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form' style="height:40px;">
<select class='portlet-form-field' name="threshold_id" >
<option value="" label=""></option>

<%
	List thresholds = DAOFactory.getThresholdDAO().loadThresholdList();
	Iterator thresholdsIt = thresholds.iterator();
	while (thresholdsIt.hasNext()){
		Threshold threshold = (Threshold)thresholdsIt.next();
		String selected = "";
		if (threshold_id != null && threshold_id.equals(threshold.getId())) {
			selected = "selected='selected'";
		}
		%>    			 		
		<option value="<%= threshold.getId() %>" label="<%= threshold.getThresholdName() %>" <%= selected %>>
			<%= threshold.getThresholdName() %>	
		</option>
		<%
	}
%>

</select>
</div>


<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.documentLabel" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'>
<select class='portlet-form-field' name="document_label" >
<option value="" label=""></option>
<%
	List sbiDocs = DAOFactory.getBIObjectDAO().loadAllBIObjects();
	Iterator sbiDocsIt = sbiDocs.iterator();
	while (sbiDocsIt.hasNext()){
		BIObject bio = (BIObject)sbiDocsIt.next();
		String selected = "";
		if (documentLabel!=null && documentLabel.equals(bio.getLabel())) {
			selected = "selected='selected'";										
		}	
		%>    			 		
		<option value="<%= bio.getLabel() %>" label="<%= bio.getLabel() %>" <%= selected %>>
			<%= bio.getLabel() %>	
		</option>
		<%
	}
%>
</select>
</div>


<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.dataSet" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form' style="height:40px;">
<select class='portlet-form-field' name="ds_id" >

<%
	List dataSets = DAOFactory.getDataSetDAO().loadAllDataSets();
	Iterator dataSetsIt = dataSets.iterator();
	while (dataSetsIt.hasNext()){
		IDataSet dataSet = (IDataSet)dataSetsIt.next();
		String selected = "";
		if (ds_id != null && ds_id.intValue() == dataSet.getId()) {
			selected = "selected='selected'";
		}
		%>    			 		
		<option value="<%= dataSet.getId() %>" label="<%= dataSet.getLabel() %>" <%= selected %>>
			<%= dataSet.getLabel() %>	
		</option>
		<%
	}
%>

</select>
</div>

</form>

<spagobi:error/>

<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>