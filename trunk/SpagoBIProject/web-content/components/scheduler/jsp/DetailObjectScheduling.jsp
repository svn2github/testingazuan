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

<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.bo.BIObjectParameter"%>
<%@page import="it.eng.spagobi.scheduler.modules.SchedulerGUIModule"%>
<%@page import="it.eng.spagobi.scheduler.to.ObjExecSchedulation"%>
<%@page import="it.eng.spagobi.scheduler.to.BIObjectParamInfo"%>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("SchedulerGUIModule"); 
   Integer objid = (Integer)moduleResponse.getAttribute(SpagoBIConstants.OBJECT_ID);
   String jobExist = (String)moduleResponse.getAttribute(SpagoBIConstants.OBJ_JOB_EXISTS);
   ObjExecSchedulation oes = (ObjExecSchedulation)moduleResponse.getAttribute(SpagoBIConstants.OBJ_SCHEDULE_DETAIL);
   List opis = oes.getObjExecParameters();
   Iterator opisIter = opis.iterator();
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   
   PortletURL saveUrl = renderResponse.createActionURL();
   saveUrl.setParameter("PAGE", SchedulerGUIModule.MODULE_PAGE);
   saveUrl.setParameter("MESSAGEDET", SpagoBIConstants.MESSAGE_SCHEDULE_OBJECT);
   saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "TRUE");
   
%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "scheduler.scheduledetail"  bundle="component_scheduler_messages"/>			
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:schedulerDetailFormSubmit()"> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key="scheduler.save" bundle="component_scheduler_messages" />' 
      			     src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/scheduler/img/save.png")%>' 
      			     alt='<spagobi:message key="scheduler.save" bundle="component_scheduler_messages" />' /> 
			</a>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "scheduler.back" bundle="component_scheduler_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/scheduler/img/back.png")%>' 
      				 alt='<spagobi:message key = "scheduler.back" bundle="component_scheduler_messages" />' />
			</a>
		</td>
	</tr>
</table>

<br/>

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dojo/dojo.js" )%>"></script>
<script type="text/javascript">
       dojo.require("dojo.widget.DropdownDatePicker");
</script>

<script>
	function storeOutClickHandler() {
		soc = document.getElementById('storeoutcheck');
		socCheck = soc.checked;
		sod = document.getElementById('storeoutdiv');
		if(socCheck){
			sod.style.display="inline";
		} else {
			sod.style.display="none";
		}
	}
	
	function radioClickHandler() {
		sadr = document.getElementById('storeAsDocRadio');
		sadrCheck = sadr.checked;
		sandd = document.getElementById('storeasnewdocdiv');
		if(sadrCheck){
			sandd.style.display="inline";
		} else {
			sandd.style.display="none";
		}
	}
</script>

<form id="scheduleDetailForm" name="scheduleDetailForm" method="post" action="<%=saveUrl.toString()%>" >
    <input type="hidden" name="<%=SpagoBIConstants.OBJECT_ID%>" value="<%=objid%>" />
	<input type="hidden" name="<%=SpagoBIConstants.OBJ_JOB_EXISTS%>" value="<%=jobExist%>" />
	
	
	<div class="div_detail_area_forms_scheduler" >
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.schedname" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="triggername" value="<%=oes.getTriggerName() != null ? oes.getTriggerName() : ""%>" size="35"/>
		    &nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.scheddescription" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="triggerdescription" value="<%=oes.getTriggerDescription() != null ? oes.getTriggerDescription() : ""%>" size="35"/>
			&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.startdate" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<%--
			<input type="text" name="startdate" value="<%=oes.getStartDate() != null ? oes.getStartDate() : ""%>" size="35"/>
			--%>
			<input type="hidden" id="startdate" name="startdate" value="<%=oes.getStartDate() != null ? oes.getStartDate() : ""%>" />
		 	<div dojoType="dropdowndatepicker" widgetId="startDateWidget" ></div>&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.starttime" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="starttime" value="<%=oes.getStartTime() != null ? oes.getStartTime() : ""%>" size="35"/>
			&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.enddate" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="enddate" value="<%=oes.getEndDate() != null ? oes.getEndDate() : ""%>" size="35"/>
			&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.endtime" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="endtime" value="<%=oes.getEndTime() != null ? oes.getEndTime() : ""%>" size="35"/>
			&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.repeatinterval" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="repeatInterval" value="<%=oes.getRepeatInterval() != null ? oes.getRepeatInterval() : ""%>" size="35"/>
			&nbsp;*&nbsp;ms
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.storeout" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<% String storeOutputChecked = oes.isStoreOutput() ? "checked=\"checked\"" : ""; %>
		 	<input id="storeoutcheck" type="checkbox" name="storeoutput" <%=storeOutputChecked%> onclick="storeOutClickHandler()"/>
			&nbsp;
		</div>
		<% String styleStoreOutDiv = oes.isStoreOutput() ? "display:inline;" : "display:none;"; %>
		<div id="storeoutdiv" style="<%=styleStoreOutDiv%>">
			<div class='div_detail_label_scheduler'>
				<span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.storename" bundle="component_scheduler_messages" />
				</span>
			</div>
			<div class='div_detail_form'>
				<input type="text" name="storename" value="<%=oes.getStoreName() != null ? oes.getStoreName() : ""%>" size="35"/>
				&nbsp;*
			</div>
			<div class='div_detail_label_scheduler'>
				<span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.storedescr" bundle="component_scheduler_messages" />
				</span>
			</div>
			<div class='div_detail_form'>
				<input type="text" name="storedescription" value="<%=oes.getStoreDescription() != null ? oes.getStoreDescription() : ""%>" size="35"/>
				&nbsp;*
			</div>
			<div class='div_detail_label_scheduler'>
				<span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.historylength" bundle="component_scheduler_messages" />
				</span>
			</div>
			<div class='div_detail_form'>
				<input type="text" name="historylength" value="<%=oes.getHistoryLength() != null ? oes.getHistoryLength() : ""%>" size="35"/>
				&nbsp;*
			</div>
			<div class='div_detail_label_scheduler'>
				<span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.storeas" bundle="component_scheduler_messages" />
				</span>
			</div>
			<div class='div_detail_form' style="height:50px;">
				<%
				String storedocChecked =  (oes.getStoreType()!= null && oes.getStoreType().equalsIgnoreCase("storeasnewdoc")) ? "checked=\"checked\"" : "";
				String storesnapChecked =  storedocChecked.equalsIgnoreCase("")? "checked=\"checked\"" : "";
			 	//String storesnapChecked = (oes.getStoreType() != null && oes.getStoreType().equalsIgnoreCase("storeassnapshot")) ? "checked=\"checked\"" : "";
			 	//String storedocChecked =  storesnapChecked.equalsIgnoreCase("")? "checked=\"checked\"" : "";
			 	%>
				<input type="radio" name="storetype" value="storesnap" <%=storesnapChecked%> onclick="radioClickHandler()" /> 
				<spagobi:message key="storeassnapshot" bundle="component_scheduler_messages" />
				<br/>
				<input id="storeAsDocRadio" type="radio" name="storetype" value="storedoc" <%=storedocChecked%> onclick="radioClickHandler()"/> 
				<spagobi:message key="storeasnewdoc" bundle="component_scheduler_messages" /> 
			</div>
			<% String styleStoreAsNewDocDiv = (oes.getStoreType() != null && oes.getStoreType().equalsIgnoreCase("storeasnewdoc")) ? "display:inline;" : "display:none;"; %>
			<div id="storeasnewdocdiv" style="<%=styleStoreAsNewDocDiv%>">
				<div class='div_detail_label_scheduler'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.newdoclabel" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_detail_form'>
					<input type="text" name="documentlabel" value="" size="35"/>
					&nbsp;*
				</div>
				<div class='div_detail_label_scheduler'>
					<span class='portlet-form-field-label'>
						&nbsp;
					</span>
				</div>
				<div class='div_detail_form'>
					Qui ci va il tree
				</div>
			</div>
		</div>
	</div>



	<div class='portlet-section-header' >
		<spagobi:message key="scheduler.biobjectParameters" bundle="component_scheduler_messages" />
	</div>
	
	<br/>
	
	<div class="div_detail_area_forms_scheduler" >
	<%
		while(opisIter.hasNext()) {
			BIObjectParamInfo bipo = (BIObjectParamInfo)opisIter.next();
	%>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<%=bipo.getLabel()%>:
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" name="biobjpar_<%=bipo.getUrlname()%>" value="<%=bipo.getValue()%>"/>
		    &nbsp;*
		</div>
	<%
		}
	%>
	</div>

</form>

<script type="text/javascript">
	function schedulerDetailFormSubmit() {
		var datePicker = dojo.widget.byId("startDateWidget");
		var startdate = datePicker.getDate();
		var startDateStr = startdate.getDate() + "/" + (startdate.getMonth() + 1) + "/" + startdate.getFullYear();
		document.getElementById("startdate").value = startDateStr;
		document.getElementById("scheduleDetailForm").submit();
	}
</script>
<%
if (oes.getStartDate() != null) {
	// date format dd/mm/yyyy
	String[] splittedDate = oes.getStartDate().split("/");
	%>
	<script type="text/javascript">
		setTimeout("updateDate()",1);
		function updateDate () {
			var datePicker = dojo.widget.byId("startDateWidget");
			var startdate = new Date();
			startdate.setDate(<%=splittedDate[0]%>);
			startdate.setMonth(<%=new Integer(splittedDate[1]).intValue() - 1%>);
			startdate.setFullYear(<%=splittedDate[2]%>);
			datePicker.setDate(startdate);
		}
	</script>
	<%
}
%>