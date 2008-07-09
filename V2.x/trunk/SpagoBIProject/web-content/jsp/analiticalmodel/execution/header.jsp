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

<%@page import="it.eng.spagobi.commons.constants.ObjectsTreeConstants"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.SubObject"%>
<%@page import="java.util.Map"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.monitoring.dao.AuditManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.utilities.ParameterValuesEncoder"%>
<%@page import="it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.service.MetadataBIObjectModule"%>
<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.commons.bo.Role"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.Snapshot"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.handlers.ExecutionManager"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.handlers.ExecutionManager.ExecutionInstance"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.handlers.BIObjectNotesManager"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.service.ExecuteBIObjectModule"%>

<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />
<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "jsp/analiticalmodel/execution/box.js")%>"></script>

<%!
String getUrl(String baseUrl, Map mapPars) {
	StringBuffer buffer = new StringBuffer();
    buffer.append(baseUrl);
    buffer.append(baseUrl.indexOf("?") == -1 ? "?" : "&");
	if (mapPars != null && !mapPars.isEmpty()) {
		java.util.Set keys = mapPars.keySet();
		Iterator iterKeys = keys.iterator();
		while (iterKeys.hasNext()) {
		  	String key = iterKeys.next().toString();
		  	String value = mapPars.get(key).toString();
		  	buffer.append(key + "=" + value);
		  	if (iterKeys.hasNext()) {
		  		buffer.append("&");
		  	}
		}
	}
	System.out.println("buffer.toString()."+buffer.toString());
	return buffer.toString();
}

boolean areAllParametersTransient(List parametersList) {
	boolean toReturn = true;
	if (parametersList != null && parametersList.size() > 0) {
		for (int i = 0; i < parametersList.size(); i++) {
			BIObjectParameter parameter = (BIObjectParameter) parametersList.get(i);
			if (!parameter.isTransientParmeters()) {
				toReturn = false;
				break;
			}
		}
	}
	return toReturn;
}

// get the virtual role (a role that containes all permissions of the correct execution roles)
Role getVirtualRole(IEngUserProfile profile, BIObject obj, String baseRoleName) throws Exception {
	
	Role virtualRole = new Role(baseRoleName, "");
	virtualRole.setIsAbleToSeeSubobjects(false);
	virtualRole.setIsAbleToSeeSnapshots(false);
	virtualRole.setIsAbleToSeeViewpoints(false);
	virtualRole.setIsAbleToSeeMetadata(false);
	virtualRole.setIsAbleToSendMail(false);
	virtualRole.setIsAbleToSeeNotes(false);
	virtualRole.setIsAbleToSaveRememberMe(false);
	virtualRole.setIsAbleToSaveIntoPersonalFolder(false);
	
	List correctRoles = null;
	if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)
			|| profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_USER)
			|| profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN))
		correctRoles = DAOFactory.getBIObjectDAO()
				.getCorrectRolesForExecution(obj.getId(), profile);
	else
		correctRoles = DAOFactory.getBIObjectDAO()
				.getCorrectRolesForExecution(obj.getId());
	if (correctRoles == null || correctRoles.size() == 0) {
		throw new Exception("No correct roles for execution!!!!");
	}
	Iterator it = correctRoles.iterator();
	while (it.hasNext()) {
		String roleName = (String) it.next();
		Role anotherRole = DAOFactory.getRoleDAO().loadByName(roleName);
		if (anotherRole.isAbleToSeeViewpoints()) virtualRole.setIsAbleToSeeSubobjects(true);
		if (anotherRole.isAbleToSeeSnapshots()) virtualRole.setIsAbleToSeeSnapshots(true);
		if (anotherRole.isAbleToSeeViewpoints()) virtualRole.setIsAbleToSeeViewpoints(true);
		if (anotherRole.isAbleToSeeMetadata()) virtualRole.setIsAbleToSeeMetadata(true);
		if (anotherRole.isAbleToSendMail()) virtualRole.setIsAbleToSendMail(true);
		if (anotherRole.isAbleToSeeNotes()) virtualRole.setIsAbleToSeeNotes(true);
		if (anotherRole.isAbleToSaveRememberMe()) virtualRole.setIsAbleToSaveRememberMe(true);
		if (anotherRole.isAbleToSaveIntoPersonalFolder()) virtualRole.setIsAbleToSaveIntoPersonalFolder(true);
	}
	return virtualRole;
}
%>

<%
// get module response, biobejct, subobject, parameters map
SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
BIObject obj = (BIObject) moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
BIObjectNotesManager objectNManager = new BIObjectNotesManager();
String execIdentifier = objectNManager.getExecutionIdentifier(obj);


SubObject subObj = (SubObject) moduleResponse.getAttribute(SpagoBIConstants.SUBOBJECT);
boolean isExecutingSubObject = subObj != null;
Snapshot snapshot = (Snapshot) moduleResponse.getAttribute(SpagoBIConstants.SNAPSHOT);
boolean isExecutingSnapshot = snapshot != null;
Map documentParametersMap = (Map) moduleResponse.getAttribute(ObjectsTreeConstants.REPORT_CALL_URL);

String title = obj.getName();

Map executionParameters = new HashMap();
if (documentParametersMap != null) executionParameters.putAll(documentParametersMap);
executionParameters.put(SpagoBIConstants.SBI_CONTEXT, GeneralUtilities.getSpagoBiContext());
executionParameters.put(SpagoBIConstants.SBI_BACK_END_HOST, GeneralUtilities.getSpagoBiHostBackEnd());
executionParameters.put(SpagoBIConstants.SBI_HOST, GeneralUtilities.getSpagoBiHost());
// Auditing
AuditManager auditManager = AuditManager.getInstance();
String modality = (String) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
if (modality == null) modality = "NORMAL_EXECUTION";

// execution role
String executionRole = (String)aSessionContainer.getAttribute(SpagoBIConstants.ROLE);
Role virtualRole = getVirtualRole(userProfile, obj, executionRole);

Integer executionAuditId = auditManager.insertAudit(obj, subObj, userProfile, executionRole, modality);
// adding parameters for AUDIT updating
if (executionAuditId != null) {
	executionParameters.put(AuditManager.AUDIT_ID, executionAuditId.toString());
}
// TODO: spostare in un tag
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
// the toolbar (slider + buttons) visibility is determined by preferences
boolean toolbarIsVisible = Boolean.parseBoolean(ChannelUtilities.getPreferenceValue(aRequestContainer, "TOOLBAR_VISIBLE", "TRUE"));
// if the modality is SINGLE_OBJECT and there are no parameters or ALL parameters are transient, the slider for parameters, viewpoints, subobjects and snapshots is not displayed
List params = obj.getBiObjectParameters();
boolean sliderIsVisible = !modality.equalsIgnoreCase(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) || (params != null && params.size() > 0 && !areAllParametersTransient(params));
%>

<div class='execution-page-title'>
	<%
	ExecutionManager executionManager = (ExecutionManager) aSessionContainer.getAttribute(ObjectsTreeConstants.EXECUTION_MANAGER);
	String executionFlowId = (String) aSessionContainer.getAttribute("EXECUTION_FLOW_ID");
	if (executionFlowId != null) aSessionContainer.delAttribute("EXECUTION_FLOW_ID");
	else executionFlowId = uuid;
	if (!executionFlowId.equals(uuid) && executionManager != null) {
		List list = executionManager.getBIObjectsExecutionFlow(executionFlowId);
		for (int i = 0; i < list.size(); i++) {
			ExecutionManager.ExecutionInstance instance = (ExecutionManager.ExecutionInstance) list.get(i);
			BIObject aBIObject = instance.getBIObject();
			Map recoverExecutionParams = new HashMap();
			recoverExecutionParams.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
			recoverExecutionParams.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.RECOVER_EXECUTION_FROM_CROSS_NAVIGATION);
			recoverExecutionParams.put("EXECUTION_FLOW_ID", instance.getFlowId());
			recoverExecutionParams.put("EXECUTION_ID", instance.getExecutionId());
			recoverExecutionParams.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "TRUE");
			String recoverExecutionUrl = urlBuilder.getUrl(request, recoverExecutionParams);
			%>&nbsp;<a href='<%= recoverExecutionUrl %>' ><%= aBIObject.getName()%></a>&gt;<%
		}
	}
	%>
	<%if(toolbarIsVisible){%>
	<%= title %>
		<%}%>
</div>

<%
if (toolbarIsVisible) {
	%>
	
	<div class="header">
		<% if (sliderIsVisible) { %>
		<div class="slider_header">
			<ul>
			    <li class="arrow"><a href="javascript:void(0);" id="toggle_Parameters<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.parameters'/></a></li>
				<% if (virtualRole.isAbleToSeeViewpoints()) { %><li class="arrow"><a href="javascript:void(0);" id="toggle_ViewPoint<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.viewpoints'/></a></li><% } %>
				<% if (virtualRole.isAbleToSeeSubobjects()) { %><li class="arrow"><a href="javascript:void(0);" id="toggle_SubObject<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.subobjects'/></a></li><% } %>
				<% if (virtualRole.isAbleToSeeSnapshots()) { %><li class="arrow"><a href="javascript:void(0);" id="toggle_Snapshot<%= uuid %>" >&nbsp;<spagobi:message key='sbi.execution.snapshots'/></a></li><% } %>
			</ul>
		</div>
		<% } %>
		<div class="toolbar_header">
			<ul>
				<% if (!modality.equalsIgnoreCase(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY)) { %>
			    <li>
			    	<%
			    	 if(!sbiMode.equals("WEB")){
			    	Map backUrlPars = new HashMap();
			    	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
			    	%>
					<a href='<%= urlBuilder.getUrl(request, backUrlPars) %>'>
						<img width="22px" height="22px" title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>'
							alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
					</a>
			    </li>
			    <% } 
				}
			    %>
			    <% if (!isExecutingSnapshot) { 
			    String refreshMode="GET";
			    if(sbiMode.equals("WEB")){refreshMode="POST";}
			    	
			    %>
			    <li>		    
					<form id="refresh<%=uuid%>" action="<%= getUrl(execUrl, documentParametersMap)  %>" method="<%=refreshMode%>">
					<input id='refreshimage<%= uuid %>' type="image" width="22px" height="22px" src='<%= urlBuilder.getResourceLink(request, "/img/updateState.png")%>'
							alt='<%=msgBuilder.getMessage("SBIExecution.refresh", "messages", request)%>'
							title='<%=msgBuilder.getMessage("SBIExecution.refresh", "messages", request)%>'/>
					</form>
					</li>
				<% } %>
				<% if (virtualRole.isAbleToSendMail() && !isExecutingSnapshot  && obj.getBiObjectTypeCode().equals("REPORT")) { %>
			    <li>		    
					<a id="sendTo_button<%= uuid %>" href='javascript:void(0);'>
						<img title='<spagobi:message key = "sbi.execution.sendTo" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/mail_generic22.png")%>'
							alt='<spagobi:message key = "sbi.execution.sendTo" />' />
					</a>
				</li>
				<% } %>
				<% if (virtualRole.isAbleToSaveIntoPersonalFolder() && !isExecutingSnapshot) { %>
			    <li>
					<a href='javascript:saveIntoPersonalFolder<%= uuid %>()'>
					    <img title='<spagobi:message key = "sbi.execution.saveToPersonalFolder" />'
					         src='<%= urlBuilder.getResourceLink(request, "/img/saveIntoPersonalFolder22.png")%>'
					         alt='<spagobi:message key = "sbi.execution.saveToPersonalFolder" />' />
					</a>
				</li>
				<% } %>
				<% if (virtualRole.isAbleToSaveRememberMe() && !isExecutingSnapshot) { %>
				<li>
					<a id='saveRememberMe_button<%= uuid %>' href='javascript:void(0);'>
						<img title='<spagobi:message key = "sbi.execution.saveRememberMe" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/saveRememberMe22.png")%>'
							alt='<spagobi:message key = "sbi.execution.saveRememberMe" />' />
					</a>
				</li>
				<% } %>
				<% if (virtualRole.isAbleToSeeNotes() && !isExecutingSnapshot) { %>
				<li>
					<a id="notes_button<%= uuid %>" href='javascript:void(0);'>
						<img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/notes.jpg")%>'
							alt='<spagobi:message key = "sbi.execution.notes.opencloseeditor" />' />
					</a>
				</li>
				<% } %>
				<% if (virtualRole.isAbleToSeeMetadata()) { %>
				<li>
					<a id="metadata_button<%= uuid %>" href='javascript:void(0);'>
						<img width="22px" height="22px" title='<spagobi:message key = "SBISet.objects.captionMetadata" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/editTemplate.jpg")%>'
							alt='<spagobi:message key = "SBISet.objects.captionMetadata" />' />
					</a>
				</li>
				<li>
					<a id="rating_button<%= uuid %>" href='javascript:void(0);'>
						<img width="22px" height="22px" title='<spagobi:message key = "metadata.Rating" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/rating.png")%>'
							alt='<spagobi:message key = "metadata.Rating" />' />
					</a>
				</li>
				<% } %>
				<li>
					<a href='javascript:void(0)' onClick="print<%= uuid %>();">
						<img width="22px" height="22px" title='<spagobi:message key = "sbi.execution.print" />'
							src='<%= urlBuilder.getResourceLink(request, "/img/printer22.png")%>'
							alt='<spagobi:message key = "sbi.execution.print" />' />
					</a>
				</li>
			</ul>
		</div>
	</div>
	
	<% if (sliderIsVisible) { %>
		<%-- Parameters --%>
		<div style="display:none"><div id="parametersContentEl<%= uuid %>"><spagobi:ParametersGenerator modality="EXECUTION_MODALITY"  requestIdentity="<%=uuid%>"/></div></div>
		<div id="popout_Parameters<%= uuid %>" class="popout"></div>
		<script>
		createToggledBox('<spagobi:message key='sbi.execution.parameters'/>:', 'parametersContentEl<%= uuid %>', 'popout_Parameters<%= uuid %>', 'toggle_Parameters<%= uuid %>', false);
		</script>
		<%-- End parameters --%>
		
		<%-- ViewPoints --%>
		<% if (virtualRole.isAbleToSeeViewpoints()) { %>
		<div style="display:none"><div id="viewpointsContentEl<%= uuid %>"><spagobi:viewPointsList biobjectId="<%= obj.getId() %>" /></div></div>
		<div id="popout_ViewPoint<%= uuid %>" class="popout"></div>
		<script>
		createToggledBox('<spagobi:message key='sbi.execution.viewpoints'/>:', 'viewpointsContentEl<%= uuid %>', 'popout_ViewPoint<%= uuid %>', 'toggle_ViewPoint<%= uuid %>', false);
		</script>
		<% } %>
		<%-- End viewPoints --%>
		
		<%-- SubObjects --%>
		<% if (virtualRole.isAbleToSeeSubobjects()) { %>
		<div style="display:none"><div id="subobjectsContentEl<%= uuid %>"><spagobi:subObjectsList biobjectId="<%= obj.getId() %>" /></div></div>
		<div id="popout_SubObject<%= uuid %>" class="popout"></div>
		<script>
		createToggledBox('<spagobi:message key='sbi.execution.subobjects'/>:', 'subobjectsContentEl<%= uuid %>', 'popout_SubObject<%= uuid %>', 'toggle_SubObject<%= uuid %>', false);
		</script>
		<% } %>
		<%-- End SubObjects --%>
		
		<%-- Snapshots --%>
		<% if (virtualRole.isAbleToSeeSnapshots()) { %>
		<div style="display:none"><div id="snapshotsContentEl<%= uuid %>"><spagobi:snapshotsList biobjectId="<%= obj.getId() %>" /></div></div>
		<div id="popout_Snapshot<%= uuid %>" class="popout"></div>
		<script>
		createToggledBox('<spagobi:message key='sbi.execution.snapshots'/>:', 'snapshotsContentEl<%= uuid %>', 'popout_Snapshot<%= uuid %>', 'toggle_Snapshot<%= uuid %>', false);
		</script>
		<% } %>
		<%-- End Snapshots --%>
	<% } %>
	
	<%-- Scripts for send mail to form --%>
	<% if (virtualRole.isAbleToSendMail() && !isExecutingSnapshot  && obj.getBiObjectTypeCode().equals("REPORT")) { 

		List masterParameters = obj.getBiObjectParameters();
		String urlMasterPar = "" ;
		Iterator itMaster = masterParameters.iterator();
		while(itMaster.hasNext())
		{
			BIObjectParameter tempObjPar = (BIObjectParameter)itMaster.next();
			String UrlPar = tempObjPar.getParameterUrlName();
			List parValues = tempObjPar.getParameterValues();
			Iterator itTemp = parValues.iterator();
			String ValuePar = "";
			while(itTemp.hasNext()){
				ValuePar += (String)itTemp.next();
				if (itTemp.hasNext()) ValuePar += ";";
			}
			
			urlMasterPar += UrlPar+"="+ValuePar;
			if (itMaster.hasNext()) urlMasterPar += "&";
		}
	%>
	
	<script type="text/javascript">
	var win_sendTo_<%= uuid %>;
	Ext.get('sendTo_button<%= uuid %>').on('click', function(){
		if(!win_sendTo_<%= uuid %>) {
			win_sendTo_<%= uuid %> = new Ext.Window({
				id:'win_sendTo_<%= uuid %>',
				bodyCfg: {
					tag:'div',
					cls:'x-panel-body',
					children:[{
						tag:'iframe',
	      					src: "<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)+"&ACTION_NAME=SHOW_SEND_TO_FORM&objlabel=" + obj.getLabel()+"&"+ urlMasterPar%>",
	      					frameBorder:0,
	      					width:'100%',
	      					height:'100%',
	      					style: {overflow:'auto'}  
	 						}]
				},
				layout:'fit',
				width:650,
				height:450,
				closeAction:'hide',
				plain: true,
				title: '<spagobi:message key = "sbi.execution.sendTo" />'
			});
		};
		win_sendTo_<%= uuid %>.show();
	});
	</script>
	<% } %>
	<%-- End scripts for send mail to form --%>
	
	<%-- Scripts for Remember Me saving --%>
	<% if (virtualRole.isAbleToSaveRememberMe() && !isExecutingSnapshot) { %>
	<script>
	var saveRememberMeForm<%= uuid %>;
	var rememberMeName<%= uuid %>;
	var rememberMeDescr<%= uuid %>;
	Ext.onReady(function(){
	    Ext.QuickTips.init();
	    rememberMeName<%= uuid %> = new Ext.form.TextField({
				id:'nameRM<%= uuid %>',
				name:'nameRM',
				allowBlank:false, 
				inputType:'text',
				maxLength:50,
				width:250,
				fieldLabel:'<spagobi:message key="sbi.rememberme.name" />' 
	    });
	    rememberMeDescr<%= uuid %> = new Ext.form.HtmlEditor({
	        id:'descrRM<%= uuid %>',
	        width: 550,
	        height: 150,
			fieldLabel:'<spagobi:message key="sbi.rememberme.descr" />'  
	    });   
	    
	    Ext.form.Field.prototype.msgTarget = 'side';
	    saveRememberMeForm<%= uuid %> = new Ext.form.FormPanel({
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        items: [rememberMeName<%= uuid %>,rememberMeDescr<%= uuid %>],
	        buttons:[{text:'<spagobi:message key="sbi.rememberme.save" />',handler:function() {saveRememberMe<%= uuid %>()}}]
	    });
	});
	var win_saveRM<%= uuid %>;
	Ext.get('saveRememberMe_button<%= uuid %>').on('click', function(){
		if(!win_saveRM<%= uuid %>) {
			win_saveRM<%= uuid %> = new Ext.Window({
				id:'popup_saveRM<%= uuid %>',
				layout:'fit',
				width:700,
				height:300,
				closeAction:'hide',
				plain: true,
				title: '<spagobi:message key="sbi.execution.saveRememberMe" />',
				items: saveRememberMeForm<%= uuid %>
			});
		};
		win_saveRM<%= uuid %>.show();
		}
	);
	
	function saveRememberMe<%= uuid %>() {
		var nameRM = escape(rememberMeName<%= uuid %>.getValue());
		if (nameRM == null || nameRM == '') {
			Ext.MessageBox.show({
				msg: '<spagobi:message key="sbi.rememberme.missingName" />',
				buttons: Ext.MessageBox.OK,
				width:300,
				icon: Ext.MessageBox.WARNING
			});
			return;
		}
		win_saveRM<%= uuid %>.hide();
		var descRM = escape(rememberMeDescr<%= uuid %>.getValue());
		Ext.MessageBox.wait('Please wait...', 'Processing');
		var url="<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)%>";
		url += "&ACTION_NAME=SAVE_REMEMBER_ME&name=" + nameRM + "&description=" + descRM;
		url += "&<%=SpagoBIConstants.OBJECT_ID%>=<%=obj.getId()%>";
		<%
		String documentParametersStr = "";
		List parametersList = obj.getBiObjectParameters();
		ParameterValuesEncoder parValuesEncoder = new ParameterValuesEncoder();
		if (parametersList != null && parametersList.size() > 0) {
			for (int i = 0; i < parametersList.size(); i++) {
				BIObjectParameter parameter = (BIObjectParameter) parametersList.get(i);
				documentParametersStr += parameter.getParameterUrlName() + "%3D";
				if (parameter.getParameterValues() != null) {
					String value = parValuesEncoder.encode(parameter);
					documentParametersStr += value;
				} else 
					documentParametersStr += "NULL";
				if (i < parametersList.size() - 1) documentParametersStr += "%26";
			}
		}
		%>
		url += "&parameters=<%= documentParametersStr %>";
		<%
		if (subObj != null) {
			%>
			url += "&subobject_id=<%=subObj.getId()%>";
			<%
		}
		%>
		var pars = '';
		Ext.Ajax.request({
			url: url,
			method: 'get',
			success: function (result, request) {
				response = result.responseText || "";
				showSaveRememberMeResult<%= uuid %>(response);
			},
			params: pars,
			failure: somethingWentWrongSavingRememberMe
		});
	}
	
	function showSaveRememberMeResult<%= uuid %>(response) {
		var iconRememberMe<%= uuid %>;
		if (response=="sbi.rememberme.saveOk") {
			response = "<spagobi:message key="sbi.rememberme.saveOk" />";
			iconRememberMe<%= uuid %> = Ext.MessageBox.INFO;
		}
		if (response=="sbi.rememberme.alreadyExisting") {
			response = "<spagobi:message key="sbi.rememberme.alreadyExisting" />";
			iconRememberMe<%= uuid %> = Ext.MessageBox.WARNING;
		}
		if (response=="sbi.rememberme.errorWhileSaving") {
			response = "<spagobi:message key="sbi.rememberme.errorWhileSaving" />";
			iconRememberMe<%= uuid %> = Ext.MessageBox.ERROR;
		}
		Ext.MessageBox.show({
			title: 'Status',
			msg: response,
			buttons: Ext.MessageBox.OK,
			width:300,
			icon: iconRememberMe<%= uuid %>
		});
	}
	
	function somethingWentWrongSavingRememberMe() {
		alert('Error while saving');
	}
	</script>
	<% } %>
	<%-- End scripts for Remember Me saving --%>
	
	<%-- Scripts for save into my personal folder --%>
	<% if (virtualRole.isAbleToSaveIntoPersonalFolder() && !isExecutingSnapshot) { %>
	<script>
	function saveIntoPersonalFolder<%= uuid %>() {
		Ext.MessageBox.wait('Please wait...', 'Processing');
		var url="<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)%>";
		url+="&ACTION_NAME=SAVE_PERSONAL_FOLDER"+"&documentId=<%=obj.getId().toString()%>";
		//var pars ="&ACTION_NAME=SAVE_PERSONAL_FOLDER";
		//pars += "&documentId=<%=obj.getId().toString()%>";
		Ext.Ajax.request({
			url: url,
			method: 'get',
			success: function (result, request) {
				response = result.responseText || "";
				showSaveToPFResult<%= uuid %>(response);
			},
			//params: pars,
			failure: somethingWentWrongSavingIntoMyFolder
		});
	}
	
	function showSaveToPFResult<%= uuid %>(response) {
		var iconSaveToPF<%= uuid %>;
		if (response=="sbi.execution.stpf.ok") {
			response = "<spagobi:message key="sbi.execution.stpf.ok" />";
			iconSaveToPF<%= uuid %> = Ext.MessageBox.INFO;
		}
		if (response=="sbi.execution.stpf.alreadyPresent") {
			response = "<spagobi:message key="sbi.execution.stpf.alreadyPresent" />";
			iconSaveToPF<%= uuid %> = Ext.MessageBox.WARNING;
		}
		if (response=="sbi.execution.stpf.error") {
			response = "<spagobi:message key="sbi.execution.stpf.error" />";
			iconSaveToPF<%= uuid %> = Ext.MessageBox.ERROR;
		}
		Ext.MessageBox.show({
			title: 'Status',
			msg: response,
			buttons: Ext.MessageBox.OK,
			width:300,
			icon: iconSaveToPF<%= uuid %>
		});
	}
	
	function somethingWentWrongSavingIntoMyFolder() {
		alert('Error while saving');
	}
	</script>
	<% } %>
	<%-- End scripts for save into my personal folder --%>
	
	<%-- notes --%>
	<% if (virtualRole.isAbleToSeeNotes() && !isExecutingSnapshot) { %>
	<script>
	var win_notes_<%= uuid %>;
	Ext.get('notes_button<%= uuid %>').on('click', function(){
		if(!win_notes_<%= uuid %>) {
			win_notes_<%= uuid %> = new Ext.Window({
				id:'win_notes_<%= uuid %>',
				bodyCfg: {
					tag:'div',
					cls:'x-panel-body',
					children:[{
						tag:'iframe',
							name: 'dynamicIframeNotes', 
                    		id  : 'dynamicIframeNotes', 
	      					src: '<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)+"&ACTION_NAME=INSERT_NOTES_ACTION&execIdentifier="+execIdentifier+"&MESSAGEDET=OPEN_NOTES_EDITOR&OBJECT_ID=" + obj.getId().toString() %>',
	      					frameBorder:0,
	      					width:'100%',
	      					height:'100%',
	      					style: {overflow:'auto'}  
	 						}]
				},
				layout:'fit',
				width:700,
				height:350,
				closeAction:'hide',
				scripts: true, 
           		 buttons: [ 
         		 { text: '<spagobi:message key = "sbi.execution.notes.savenotes" />', 
    	  		 handler: function(){ 
           		dynamicIframeNotes.saveNotes(); 
             	 } } ], 
 				buttonAlign : 'left',
				plain: true,
				title: '<spagobi:message key = "sbi.execution.notes.insertNotes" />'
			});
		};
		win_notes_<%= uuid %>.show();
	});
	</script>
	<% } %>
	<%-- end notes --%>
	
	<%-- Scripts for metadata window --%>
	<% if (virtualRole.isAbleToSeeMetadata()) { %>
	<script>
	var win_metadata_<%= uuid %>;
	Ext.get('metadata_button<%= uuid %>').on('click', function(){
		if(!win_metadata_<%= uuid %>) {
			win_metadata_<%= uuid %> = new Ext.Window({
				id:'win_metadata_<%= uuid %>',
				bodyCfg: {
					tag:'div',
					cls:'x-panel-body',
					children:[{
						tag:'iframe',
	      					src: '<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)+"&PAGE=" + MetadataBIObjectModule.MODULE_PAGE + "&MESSAGEDET=" + ObjectsTreeConstants.METADATA_SELECT + "&OBJECT_ID=" + obj.getId().toString() %>',
	      					frameBorder:0,
	      					width:'100%',
	      					height:'100%',
	      					style: {overflow:'auto'}  
	 						}]
				},
				layout:'fit',
				width:700,
				height:400,
				closeAction:'hide',
				plain: true,
				title: '<spagobi:message key = "SBISet.objects.captionMetadata" />'
			});
		};
		win_metadata_<%= uuid %>.show();
	});
	</script>
	<% } %>
	<%-- End scripts for metadata window --%>
	
	<%-- Scripts for rating window --%>
	<% if (virtualRole.isAbleToSeeMetadata()) { %>
	<script>
	var win_rating_<%= uuid %>;

	Ext.get('rating_button<%= uuid %>').on('click', function(){
		if(!win_rating_<%= uuid %>){
			win_rating_<%= uuid %> = new Ext.Window({
				id:'win_rating_<%= uuid %>',
				bodyCfg: {
					tag:'div',
					cls:'x-panel-body',
					children:[{
						tag:'iframe',
						    name: 'dynamicIframe1', 
                    		id  : 'dynamicIframe1', 
	      					src: '<%=GeneralUtilities.getSpagoBIProfileBaseUrl(userId)+"&ACTION_NAME=RATING_ACTION&MESSAGEDET=GOTO_DOCUMENT_RATE&OBJECT_ID=" + obj.getId().toString() %>',
	      					frameBorder:0,
	      					width:'100%',
	      					height:'100%',
	      					style: {overflow:'auto'}  
	 						}]
				},
				layout:'fit',
				width:230,
				height:360,
				closeAction:'hide',
				scripts: true, 
           		 buttons: [ 
         		 { text: 'Vote', 
    	  		 handler: function(){ 
           		dynamicIframe1.saveDL(); 
             	 } } ], 
 				buttonAlign : 'left',
				plain: true,
				title: '<spagobi:message key = "metadata.docRating" />'
			});
		};
		
		win_rating_<%= uuid %>.show();
		
	});
	</script>
	<% } %>
	<%-- End scripts for rating window --%>
	
	<%-- Scripts for print --%>
	<%
	String engineClassName = obj.getEngine().getClassName();
	if (engineClassName != null && engineClassName.equals("it.eng.spagobi.engines.chart.SpagoBIChartInternalEngine")) {
		%>
		<link rel="stylesheet" type="text/css" href="<%=urlBuilder.getResourceLink(request, "css/printImage.css")%>" media="print">
		<script>
		function print<%= uuid %>() {
			window.print();
		}
		</script>
		<%
	} else if (engineClassName != null && engineClassName.equals("it.eng.spagobi.engines.documentcomposition.SpagoBIDocumentCompositionInternalEngine")) {
		%>
		<script>
		function print<%= uuid %>() {
			//next variable is defined into documentcomposition.js
			for(var docMaster in asUrls){
				var iframeId = "iframe_" + docMaster.substring(docMaster.indexOf('|')+1);
				var singleIframe = document.getElementById(iframeId);		
				var msg = '<spagobi:message key="sbi.execution.printDocument" />';	
				if (confirm(msg + " '" + docMaster.substring(docMaster.indexOf('|')+1) +"'?")){
					if (!isMoz()) {
						singleIframe.focus();
						singleIframe.print();
					} else {
						window.frames[iframeId].focus();
						window.frames[iframeId].print();
					} 
				}		
				/*	
				Ext.Msg.confirm("Print Document", msg + " " + docMaster.substring(docMaster.indexOf('|')+1) +"?", 
						function(btn, singleIframe, iframeId){
							if (btn == 'yes'){
								if (!isMoz()) {
									singleIframe.focus();
									singleIframe.print();
								} else {
									window.frames[iframeId].focus();
									window.frames[iframeId].print();
								} 
							}	
						});
						*/
			}
		}
		</script>
		<%
	} else { %>

		<script>
		function print<%= uuid %>() {
			if (!isMoz()) {
				document.iframeexec<%= uuid %>.focus();
				document.iframeexec<%= uuid %>.print();
			} else {
				window.frames['iframeexec<%= uuid %>'].focus();
				window.frames['iframeexec<%= uuid %>'].print();
			} 
		}
		</script>
		<%
	}
	%>
	<%-- End scripts for print --%>
	<%
}
%>

<spagobi:error/>