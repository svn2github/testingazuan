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
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/FolderView.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/FolderDetailPanel.js")%>'></script>
    <script type="text/javascript" src='<%=urlBuilder.getResourceLink(request, "/js/src/ext/sbi/browser/DocumentsBrowser.js")%>'></script>
    
       
    <script type="text/javascript">
    Ext.BLANK_IMAGE_URL = '<%=urlBuilder.getResourceLink(request, "/js/lib/ext-2.0.1/resources/images/default/s.gif")%>';
    
    Sbi.config = {};
    
    var url = {
		host: '<%= request.getServerName()%>',
	    port: '<%= request.getServerPort()%>',
	    contextPath: '<%= request.getContextPath().startsWith("/")||request.getContextPath().startsWith("\\")?
	        				  request.getContextPath().substring(1):
	        				  request.getContextPath()%>',
	    userId:  '<%= userUniqueIdentifier %>',
	    execId: '<%= request.getParameter("SBI_EXECUTION_ID")%>'
    };
    Sbi.config.serviceRegistry = new Sbi.service.ServiceRegistry({baseUrl: url});

	
    <% if ( "WEB".equalsIgnoreCase(sbiMode) ) { %>
    
    Ext.onReady(function(){
      Ext.QuickTips.init();              
      var browser = new Sbi.browser.DocumentsBrowser();
      var viewport = new Ext.Viewport(browser);     
    });
    
	<% } else { %>

    Ext.onReady(function(){
        Ext.QuickTips.init();    
        var browser = new Sbi.browser.DocumentsBrowser({
            renderTo: 'container'
                , height : 600}); 
     });
    
    <% } %>
    
    </script>
    
    <% if ( "PORTLET".equalsIgnoreCase(sbiMode) ) { %>
    <div id="container" width=100%></div>
    <%} %>
    
