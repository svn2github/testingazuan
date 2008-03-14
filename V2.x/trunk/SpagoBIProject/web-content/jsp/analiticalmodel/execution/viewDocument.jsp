<%--
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
--%>

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@page import="it.eng.spagobi.commons.constants.ObjectsTreeConstants"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />

<%
// get module response
BIObject obj = (BIObject) aSessionContainer.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
Map executeUrlPars = new HashMap();
executeUrlPars.put("PAGE", "ValidateExecuteBIObjectPage");
executeUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_RUN);
executeUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
String execUrl = urlBuilder.getUrl(request, executeUrlPars);
// identity string for object of the page
UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
UUID uuidObj = uuidGen.generateTimeBasedUUID();
String uuid = uuidObj.toString();
uuid = uuid.replaceAll("-", "");

%>
<div class='errors-object-details-div'>
	<spagobi:error/>
</div>

<div class="header">
	<div class="slider_header">
		<ul>
		    <li class="arrow"><a href="javascript:void(0);" id="toggle_Parameters<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.parameters'/></a></li>
			<li class="arrow"><a href="javascript:void(0);" id="toggle_ViewPoint<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.viewpoints'/></a></li>
			<li class="arrow"><a href="javascript:void(0);" id="toggle_SubObject<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.subobjects'/></a></li>
			<li class="arrow"><a href="javascript:void(0);" id="toggle_Snapshot<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.snapshots'/></a></li>
		</ul>
	</div>
	<div class="toolbar_header">
		<ul>
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
		</ul>
	</div>
</div>

<!-- Parameters -->
<div id="popout_Parameters<%= uuid %>" class="popout">
	<div class="popout_selector">
		<h4 class="popout_label"><spagobi:message key='sbi.execution.parameters'/>:</h4> 
		<spagobi:ParametersGenerator modality="EXECUTION_MODALITY"  requestIdentity="<%=uuid%>"/>
	</div>
</div>

<!-- ViewPoints -->
<div id="popout_ViewPoint<%= uuid %>" class="popout">
	<div class="popout_selector">
		<h4 class="popout_label"><spagobi:message key='sbi.execution.viewpoints'/></h4>
		<spagobi:viewPointsList biobjectId="<%= obj.getId() %>" />
	</div>
</div>
<div class="clear"></div>
<!-- SubObjects -->
<div id="popout_SubObject<%= uuid %>" class="popout">
	<div class="popout_selector">
		<h4 class="popout_label"><spagobi:message key='sbi.execution.subobjects'/></h4>
		<spagobi:subObjectsList biobjectId="<%= obj.getId() %>" />
	</div>
</div>
<div class="clear"></div>
<!-- Snapshots -->
<div id="popout_Snapshot<%= uuid %>" class="popout">
	<div class="popout_selector">
		<h4 class="popout_label"><spagobi:message key='sbi.execution.snapshots'/></h4>
		<spagobi:snapshotsList biobjectId="<%= obj.getId() %>" />
	</div>
</div>

<script>
toggle('popout_Parameters<%= uuid %>', 'toggle_Parameters<%= uuid %>', true);
toggle('popout_ViewPoint<%= uuid %>', 'toggle_ViewPoint<%= uuid %>', false);
toggle('popout_SubObject<%= uuid %>', 'toggle_SubObject<%= uuid %>', false);
toggle('popout_Snapshot<%= uuid %>', 'toggle_Snapshot<%= uuid %>', false);
</script>

<%@ include file="/jsp/commons/footer.jsp"%>