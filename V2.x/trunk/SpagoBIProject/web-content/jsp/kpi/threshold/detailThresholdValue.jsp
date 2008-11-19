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
	String minValue = "";
	String maxValue = "";
	String label = "";
	String colour = "";
	String position = "";
	
	String threshold_id = "";
	Integer severity_id = null;
	
    String messageBunle = "component_kpi_messages"; 
    String moduleName = "DetailThresholdValueModule";

    ConfigSingleton configure = ConfigSingleton.getInstance();
	SourceBean moduleBean = (SourceBean) configure
			.getFilteredSourceBeanAttribute("MODULES.MODULE", "NAME",
					moduleName);
	
	SourceBean threshold = (SourceBean) aServiceResponse.getAttribute(moduleName);

	String message = DelegatedDetailService.DETAIL_INSERT;

	if (moduleBean.getAttribute("CONFIG.TITLE") != null)
		title = (String) moduleBean.getAttribute("CONFIG.TITLE");
	
	if (aServiceRequest.getAttribute("THRESHOLD_ID") != null)
		threshold_id = (String)aServiceRequest.getAttribute("THRESHOLD_ID");
	
	if (threshold != null){
		if (threshold.getAttribute("ROW.ID") != null)
			id = String.valueOf(threshold.getAttribute("ROW.ID"));
		if (threshold.getAttribute("ROW.MIN_VALUE") != null)
			minValue = String.valueOf(threshold.getAttribute("ROW.MIN_VALUE"));
		if (threshold.getAttribute("ROW.MAX_VALUE") != null)
			maxValue = String.valueOf(threshold.getAttribute("ROW.MAX_VALUE"));
		if (threshold.getAttribute("ROW.LABEL") != null)
			label = (String) threshold.getAttribute("ROW.LABEL");
		if (threshold.getAttribute("ROW.COLOUR") != null)
			colour = (String) threshold.getAttribute("ROW.COLOUR");
		if (threshold.getAttribute("ROW.POSITION") != null)
			position = String.valueOf(threshold.getAttribute("ROW.POSITION"));
		
		if (threshold.getAttribute("ROW.SEVERITY_ID") != null)
			severity_id = (Integer)threshold.getAttribute("ROW.SEVERITY_ID");
		
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
		if (aServiceRequest.getAttribute("MIN_VALUE") != null)
			minValue = String.valueOf(aServiceRequest.getAttribute("MIN_VALUE"));
		if (aServiceRequest.getAttribute("MAX_VALUE") != null)
			maxValue = String.valueOf(aServiceRequest.getAttribute("MAX_VALUE"));
		if (aServiceRequest.getAttribute("LABEL") != null)
			label = (String) aServiceRequest.getAttribute("LABEL");
		if (aServiceRequest.getAttribute("COLOUR") != null)
			colour = (String) aServiceRequest.getAttribute("COLOUR");
		if (aServiceRequest.getAttribute("POSITION") != null)
			position = String.valueOf(aServiceRequest.getAttribute("POSITION"));
		
		if (aServiceRequest.getAttribute("SEVERITY_ID") != null)
			severity_id = (Integer)aServiceRequest.getAttribute("SEVERITY_ID");
		
	}
	
	Map formUrlPars = new HashMap();
	if(ChannelUtilities.isPortletRunning()) {
		formUrlPars.put("PAGE", "ThresholdValuePage");
		formUrlPars.put("MODULE", "DetailThresholdValueModule");
		formUrlPars.put("MESSAGE", message);
		formUrlPars.put("IDT", threshold_id);
		formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	}
	
	String formUrl = urlBuilder.getUrl(request, formUrlPars);

	Map backUrlPars = new HashMap();
	backUrlPars.put("PAGE", "ThresholdValuePage");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>

<%
String urlColorPicker=urlBuilder.getResourceLink(request,"/js/kpi/colorPicker.js");
%>
<script type="text/javascript" src="<%=urlColorPicker%>"></script>

<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.commons.bo.Domain"%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
			style='vertical-align: middle; padding-left: 5px;'>
			<spagobi:message key="<%=title%>" bundle="<%=messageBunle%>" /></td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'><a
			href="javascript:document.getElementById('thresholdValueForm').submit()"> <img
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

<form method='POST' action='<%=formUrl%>' id='thresholdValueForm' name='thresholdForm'>
<input type='hidden' value='<%=id%>' name='id' />
<input type='hidden' value='<%=threshold_id%>' name='threshold_id' />
<div class="div_detail_area_forms">

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.position" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="position" size="50"
	value="<%=position%>" maxlength="3"></div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.label" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'><input
	class='portlet-form-input-field' type="text" name="label" size="50"
	value="<%=label%>" maxlength="20"></div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.minValue" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'>
<input
  class='portlet-form-input-field' type="text" name="min_Value" size="50"
  value="<%=minValue%>" maxlength="200"></div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.maxValue" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'>
<input
  class='portlet-form-input-field' type="text" name="max_Value" size="50"
  value="<%=maxValue%>" maxlength="200"></div>

<script language="JavaScript">
var cp = new ColorPicker('window'); // Popup window
</script>
<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.colour" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'>
<input style="background-color:<%=colour%>"
  class='portlet-form-input-field' type="text" name="colour" id="colour" size="50"
  value="<%=colour%>" maxlength="20" >
<a fref="#" onClick="javascript:cp.select(document.forms[0].colour,'pick');return false;" name="pick" id="pick">Select</a>
<script language="JavaScript">
cp.writeDiv()
</script>
</div>

<div class='div_detail_label'><span
	class='portlet-form-field-label'> <spagobi:message
	key="sbi.kpi.label.severity" bundle="<%=messageBunle%>"/> </span></div>
<div class='div_detail_form'>
<select class='portlet-form-field' name="severity_id" >
<%
	List severityLevels = DAOFactory.getDomainDAO().loadListDomainsByType("SEVERITY");
	Iterator itt = severityLevels.iterator();
	while (itt.hasNext()){
		Domain domain = (Domain)itt.next();
		String selected = "";
		if (severity_id != null && severity_id.intValue() == domain.getValueId().intValue()){
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

</div>
</form>

<spagobi:error/>

<%@ include file="/jsp/commons/footer.jsp"%>