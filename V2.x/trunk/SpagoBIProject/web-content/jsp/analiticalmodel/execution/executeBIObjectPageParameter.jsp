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

<%@page import="it.eng.spagobi.commons.constants.ObjectsTreeConstants"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.commons.bo.Role"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>

<%@page import="it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance"%>
<script type="text/javascript" src="<%=linkProto%>"></script>
<script type="text/javascript" src="<%=linkProtoWin%>"></script>
<script type="text/javascript" src="<%=linkProtoEff%>"></script>
<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/>

<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />
<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "jsp/analiticalmodel/execution/box.js")%>"></script>

<%
ExecutionInstance instance = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
BIObject obj = instance.getBIObject();
Map executeUrlPars = new HashMap();
executeUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");
executeUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RUN);
executeUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
String execUrl = urlBuilder.getUrl(request, executeUrlPars);
// identity string for object of the page
String uuid = instance.getExecutionId();

String title = obj.getName();

//execution role
String executionRole = instance.getExecutionRole();
Role executionRoleObj = DAOFactory.getRoleDAO().loadByName(executionRole);

// execution modality
String modality = instance.getExecutionModality();
if (modality == null) modality = "NORMAL_EXECUTION";

%>

<div class='execution-page-title'>
	<%= title %>
</div>

<div class='errors-object-details-div'>
	<spagobi:error/>
</div>

<div class="header">
	<div class="slider_header">
		<ul>
		    <li class="arrow"><a href="javascript:void(0);" id="toggle_Parameters<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.parameters'/></a></li>
			<% if (executionRoleObj.isAbleToSeeViewpoints()) { %><li class="arrow"><a href="javascript:void(0);" id="toggle_ViewPoint<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.viewpoints'/></a></li><% } %>
			<% if (executionRoleObj.isAbleToSeeSubobjects()) { %><li class="arrow"><a href="javascript:void(0);" id="toggle_SubObject<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.subobjects'/></a></li><% } %>
			<% if (executionRoleObj.isAbleToSeeSnapshots()) { %><li class="arrow"><a href="javascript:void(0);" id="toggle_Snapshot<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.snapshots'/></a></li><% } %>
		</ul>
	</div>
	<div class="toolbar_header">
		<ul>
			<% if (!modality.equalsIgnoreCase(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY)) { %>
		    <li>
		    	<%
		    	Map backUrlPars = new HashMap();
		    	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		    	%>
				<a href='<%= urlBuilder.getUrl(request, backUrlPars) %>'>
					<img width="22px" height="22px" title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />'
						src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>'
						alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
				</a>
		    </li>
		    <% } %>
		</ul>
	</div>
</div>

<%-- Parameters --%>
<div style="display:none"><div id="parametersContentEl<%= uuid %>"><spagobi:ParametersGenerator modality="EXECUTION_MODALITY"  requestIdentity="<%=uuid%>"/></div></div>
<div id="popout_Parameters<%= uuid %>" class="popout"></div>
<script>
createToggledBox('<spagobi:message key='sbi.execution.parameters'/>:', 'parametersContentEl<%= uuid %>', 
		'popout_Parameters<%= uuid %>', 'toggle_Parameters<%= uuid %>', true);
</script>
<%-- End parameters --%>

<%-- ViewPoints --%>
<% if (executionRoleObj.isAbleToSeeViewpoints()) { %>
<div style="display:none"><div id="viewpointsContentEl<%= uuid %>"><spagobi:viewPointsList biobjectId="<%= obj.getId() %>" /></div></div>
<div id="popout_ViewPoint<%= uuid %>" class="popout"></div>
<script>
createToggledBox('<spagobi:message key='sbi.execution.viewpoints'/>:', 'viewpointsContentEl<%= uuid %>', 
		'popout_ViewPoint<%= uuid %>', 'toggle_ViewPoint<%= uuid %>', <%= pageContext.getAttribute("viewpointsBoxOpen") %>);
</script>
<% } %>
<%-- End viewPoints --%>

<%-- SubObjects --%>
<% if (executionRoleObj.isAbleToSeeSubobjects()) { %>
<div style="display:none"><div id="subobjectsContentEl<%= uuid %>"><spagobi:subObjectsList biobjectId="<%= obj.getId() %>" /></div></div>
<div id="popout_SubObject<%= uuid %>" class="popout"></div>
<script>
createToggledBox('<spagobi:message key='sbi.execution.subobjects'/>:', 'subobjectsContentEl<%= uuid %>', 
		'popout_SubObject<%= uuid %>', 'toggle_SubObject<%= uuid %>', <%= pageContext.getAttribute("subobjectsBoxOpen") %>);
</script>
<% } %>
<%-- End SubObjects --%>

<%-- Snapshots --%>
<% if (executionRoleObj.isAbleToSeeSnapshots()) { %>
<div style="display:none"><div id="snapshotsContentEl<%= uuid %>"><spagobi:snapshotsList biobjectId="<%= obj.getId() %>" /></div></div>
<div id="popout_Snapshot<%= uuid %>" class="popout"></div>
<script>
createToggledBox('<spagobi:message key='sbi.execution.snapshots'/>:', 'snapshotsContentEl<%= uuid %>', 
		'popout_Snapshot<%= uuid %>', 'toggle_Snapshot<%= uuid %>', <%= pageContext.getAttribute("snapshotsBoxOpen") %>);
</script>
<% } %>
<%-- End Snapshots --%>

<%@ include file="/jsp/commons/footer.jsp"%>
