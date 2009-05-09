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
   
   	
   
   	 
    <link rel='stylesheet' type='text/css' href='<%=urlBuilder.getResourceLinkByTheme(request, "css/analiticalmodel/browser/main.css", currTheme)%>'/>
    <link rel='stylesheet' type='text/css' href='<%=urlBuilder.getResourceLinkByTheme(request, "css/analiticalmodel/browser/listview.css", currTheme)%>'/>
    <link rel='stylesheet' type='text/css' href='<%=urlBuilder.getResourceLinkByTheme(request, "css/analiticalmodel/browser/groupview.css",currTheme)%>'/>
   	<link rel='stylesheet' type='text/css' href='<%=urlBuilder.getResourceLinkByTheme(request, "css/commons/loadmask.css", currTheme)%>' />
	
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/locale/" + locale.getLanguage() + ".js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/locale/LocaleUtils.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/exception/ExceptionHandler.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/service/ServiceRegistry.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/decorator/LoadMask.js")%>'></script>
	<script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/SampleData.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/SearchField.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/Toolbar.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/DocumentsTree.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/FilterPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/SearchPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/FolderViewTemplate.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/FolderView.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/FolderDetailPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/RoleSelectionPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ParametersSelectionPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/DocumentViewPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/execution/ExecutionWizardPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/DocumentsBrowser.js")%>'></script>
    
       
    <script type="text/javascript">
    Ext.BLANK_IMAGE_URL = '<%=urlBuilder.getResourceLink(request, "/js/lib/ext-2.0.1/resources/images/default/s.gif")%>';
    
    Sbi.config = {};
    
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



	
    var browserConfig = <%= aServiceResponse.getAttribute("metaConfiguration")%>;
	//var lang = '<%=locale.getLanguage()%>';
    //alert('language: ' + lang);
    	       
   
    browserConfig = Ext.apply(browserConfig, {
        //rootFolderId: 3
        /*
        metaDocument: [
   			  {id:"label", 				groupable:true, maxChars:20, visible:true, showLabel:false, sortable:false, searchable:true}
   			, {id:"name", 				groupable:true, maxChars:20, visible:true, showLabel:false, sortable:true, searchable:true}
   			, {id:"extendedDescription",groupable:false, maxChars:20, visible:true, showLabel:true, sortable:false}
   			, {id:"description", 		groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"engine", 			groupable:false, maxChars:5, visible:false, showLabel:true, sortable:true, searchable:true}
   			, {id:"creationUser", 		groupable:true, maxChars:20, visible:true, showLabel:true, sortable:false}
   			, {id:"creationDate",		groupable:true, maxChars:20, visible:true, showLabel:false, sortable:true, searchable:true}
   			, {id:"typeCode", 			groupable:false, maxChars:20, visible:true, showLabel:true, sortable:false}
   			, {id:"encrypt", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"profiledVisibility", groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"datasource", 		groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"dataset", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"uuid",				groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"relname", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"stateCode",			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"stateId", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"functionalities", 	groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"language", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"objectve", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"keywords", 			groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   			, {id:"refreshSeconds",		groupable:false, maxChars:20, visible:false, showLabel:true, sortable:false}
   		]
   		, metaFolder: [
   		     {maxChars:20, visible:true, showLabel:false, id:"id"}
   		   , {maxChars:20, visible:true, showLabel:false, id:"name"}
   		   , {maxChars:20, visible:true, showLabel:false, id:"description"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"code"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"codType"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"path"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"parentId"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"devRoles"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"testRoles"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"execRoles"}
   		   , {maxChars:20, visible:false, showLabel:false, id:"biObjects"}
   		]
   		*/
   	});
    
    Ext.onReady(function(){
      Ext.QuickTips.init();              
      var browser = new Sbi.browser.DocumentsBrowser(browserConfig);
      var viewport = new Ext.Viewport(browser);  
         
    });
    
    </script>
    
    
    
    
    
