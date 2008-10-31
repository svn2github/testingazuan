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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@page import="java.util.Map,java.util.HashMap"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page	import="it.eng.spago.dispatching.service.detail.impl.DelegatedDetailService"%>
<%
	String title = "";
	String id = "";
	String name = "";
	String description = "";
	Integer threshold_type_id = null;
	
    String messageBunle = "component_kpi_messages"; 

    ConfigSingleton configure = ConfigSingleton.getInstance();
	SourceBean moduleBean = (SourceBean) configure
			.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME",
					"DetailThresholdModule");
	
	SourceBean threshold = (SourceBean) aServiceResponse.getAttribute("DetailThresholdModule");

	String message = DelegatedDetailService.DETAIL_INSERT;

	if (moduleBean.getAttribute("CONFIG.TITLE") != null)
		title = (String) moduleBean.getAttribute("CONFIG.TITLE");
	
	if (threshold != null){
		if (threshold.getAttribute("ROW.ID") != null)
			id = String.valueOf(threshold.getAttribute("ROW.ID"));
		if (threshold.getAttribute("ROW.NAME") != null)
			name = (String) threshold.getAttribute("ROW.NAME");
		if (threshold.getAttribute("ROW.DESCRIPTION") != null)
			description = (String) threshold.getAttribute("ROW.DESCRIPTION");
		if (threshold.getAttribute("ROW.THRESHOLD_TYPE_ID") != null)
			threshold_type_id = (Integer)threshold.getAttribute("ROW.THRESHOLD_TYPE_ID");
		
		if (threshold.getAttribute(DelegatedDetailService.SERVICE_MODE) != null
			&& ((String) threshold
					.getAttribute(DelegatedDetailService.SERVICE_MODE))
					.equalsIgnoreCase(DelegatedDetailService.SERVICE_MODE_UPDATE)) {
			message = DelegatedDetailService.DETAIL_UPDATE;
		}
	}
	
	if (threshold == null){
		if (aServiceRequest.getAttribute("ID") != null)
			id = String.valueOf(aServiceRequest.getAttribute("ID"));
		if (aServiceRequest.getAttribute("NAME") != null)
			name = (String) aServiceRequest.getAttribute("NAME");
		if (aServiceRequest.getAttribute("DESCRIPTION") != null)
			description = (String) aServiceRequest.getAttribute("DESCRIPTION");
		if (threshold.getAttribute("THRESHOLD_TYPE_ID") != null)
			threshold_type_id = (Integer)threshold.getAttribute("THRESHOLD_TYPE_ID");
	}
	
	Map formUrlPars = new HashMap();
	if(ChannelUtilities.isPortletRunning()) {
		formUrlPars.put("PAGE", "ThresholdPage");
		formUrlPars.put("MODULE", "DetailThresholdModule");
		formUrlPars.put("MESSAGE", message);
		formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	}
	
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "ThresholdPage");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>


<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%><table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
			style='vertical-align: middle; padding-left: 5px;'>
			<spagobi:message key="<%=title%>" bundle="<%=messageBunle%>" /></td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'><a
			href="javascript:document.getElementById('thresholdForm').submit()"> <img
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

<form method='POST' action='<%=formUrl%>' id='thresholdForm' name='thresholdForm'>
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
<div class='div_detail_form'>
<input
  class='portlet-form-input-field' type="text" name="description" size="50"
  value="<%=description%>" maxlength="200"></div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.thresholdType" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'>

<select class='portlet-form-field' name="threshold_type_id" >
<%
	List thresholdTypes = DAOFactory.getDomainDAO().loadListDomainsByType("THRESHOLD_TYPE");
	Iterator itt = thresholdTypes.iterator();
	while (itt.hasNext()){
		Domain domain = (Domain)itt.next();
		String selected = "";
		if (threshold_type_id != null && threshold_type_id.intValue() == domain.getValueId().intValue()){
			selected = "selected='selected'";		
		}
		%>    			 		
		<option value="<%= domain.getValueId() %>" label="<%= domain.getValueName() %>" <%= selected %>>
			<%= domain.getValueName() %>	
		</option>
		<%
	}
%>
</select>

</div>

</form>

<spagobi:error/>

<%@ include file="/jsp/commons/footer.jsp"%>