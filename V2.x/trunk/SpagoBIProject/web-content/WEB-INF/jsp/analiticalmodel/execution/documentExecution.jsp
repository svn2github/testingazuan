<!--
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
-->

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>

<%@ page language="java" 
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="it.eng.spagobi.commons.constants.ObjectsTreeConstants"%>    
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>

	<link rel='stylesheet' type='text/css' href='<%=urlBuilder.getResourceLinkByTheme(request, "css/analiticalmodel/execution/main.css",currTheme)%>'/>
	
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/locale/" + locale.getLanguage() + ".js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/locale/LocaleUtils.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/exception/ExceptionHandler.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/service/ServiceRegistry.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/decorator/LoadMask.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/commons/Format.js")%>'></script>	
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/widgets/FilteringToolbar.js")%>'></script>  
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/widgets/PagingToolbar.js")%>'></script>      
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/widgets/LookupField.js")%>'></script>  
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/SubobjectsPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/SnapshotsPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ViewpointsPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ShortcutsPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ParametersPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/RoleSelectionPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/RoleSelectionPage.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ParametersSelectionPage.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/DocumentExecutionPage.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ExecutionWizard.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ExecutionPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/DocumentsBrowser.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/lib/ext-2.0.1/ux/miframe/miframe-min.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/lib/ext-2.0.1/ux/grid/Ext.grid.ButtonColumn.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/decorator/ComponentBuddy.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/toolbar/ExecutionToolbar.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/toolbar/SendToWindow.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/toolbar/SaveRememberMeWindow.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/toolbar/NotesWindow.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/toolbar/MetadataWindow.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/toolbar/RatingWindow.js")%>'></script>

    <script type="text/javascript">
    Ext.BLANK_IMAGE_URL = '<%=urlBuilder.getResourceLink(request, "/js/lib/ext-2.0.1/resources/images/default/s.gif")%>';
    
    Sbi.config = {};

    // the user language
    Sbi.config.language = '<%= locale.getLanguage() %>';
	// the user country
    Sbi.config.country = '<%= locale.getCountry() %>';
    // the date format localized according to user language and country
    Sbi.config.localizedDateFormat = '<%= GeneralUtilities.getLocaleDateFormatForExtJs(permanentSession) %>';
    // the date format to be used when communicating with server
    Sbi.config.clientServerDateFormat = '<%= GeneralUtilities.getServerDateFormatExtJs() %>';
    
    var url = {
    	host: '<%= request.getServerName()%>'
    	, port: '<%= request.getServerPort()%>'
    	, contextPath: '<%= request.getContextPath().startsWith("/")||request.getContextPath().startsWith("\\")?
    	   				  request.getContextPath().substring(1):
    	   				  request.getContextPath()%>'
    	    
    };

    var params = {
    	user_id:  <%= userUniqueIdentifier!=null?"'" + userUniqueIdentifier +"'": "null" %>
    	, SBI_EXECUTION_ID: <%= request.getParameter("SBI_EXECUTION_ID")!=null?"'" + request.getParameter("SBI_EXECUTION_ID") +"'": "null" %>
    	, LIGHT_NAVIGATOR_DISABLED: 'TRUE'
    };

    Sbi.config.serviceRegistry = new Sbi.service.ServiceRegistry({
    	baseUrl: url
        , baseParams: params
    });

    <%
    Integer objId = new Integer((String) aServiceResponse.getAttribute(ObjectsTreeConstants.OBJECT_ID));
    BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
    String parameters = (String) aServiceResponse.getAttribute(ObjectsTreeConstants.PARAMETERS);
    String subobjectName = (String) aServiceResponse.getAttribute(SpagoBIConstants.SUBOBJECT_NAME);
    %>
    //var menuConfig = <%= aServiceResponse.getAttribute("metaConfiguration")%>;

	var object = {id: <%= obj.getId() %>, label: '<%= obj.getLabel() %>'};

	var parameters = <%= parameters != null ? ("'" + parameters.replaceAll("'", "\'") + "'") : "undefined" %>;
	var subobject = <%= subobjectName != null ? ("'" + subobjectName.replaceAll("'", "\'") + "'") : "undefined" %>;
	
    var config = {
    	title: object.label
    	, preferences: {
			parameters: parameters
			, subobject: subobject
	    }
	};
	
	Ext.onReady(function(){
		Ext.QuickTips.init();
		var executionPanel = new Sbi.execution.ExecutionPanel(config);
		//var mainPanel = new Ext.Panel({
		//	title: object.label
	    //    , collapsible: false
		//	, items: executionPanel
		//	, layout: 'fit'
		//});
		var viewport = new Ext.Viewport({
			layout: 'border'
			, items: [
				{
					region: 'center',
					layout: 'fit',
					items: [executionPanel]
				}
			]
		});
		executionPanel.execute(object);
	});
    
    </script>
    
