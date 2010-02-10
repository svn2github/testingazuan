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

<%-- 
author: Antonella Giachino (antonella.giachino@eng.it)
--%>
<%@ page language="java" 
	     contentType="text/html; charset=ISO-8859-1" 
	     pageEncoding="ISO-8859-1"%>	


<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA IMPORTS															--%>
<%-- ---------------------------------------------------------------------- --%>
<%@page import="it.eng.spago.configuration.*"%>
<%@page import="it.eng.spago.base.*"%>
<%@page import="it.eng.spagobi.engines.console.ConsoleEngineConfig"%>
<%@page import="it.eng.spagobi.engines.console.ConsoleEngineInstance"%>
<%@page import="it.eng.spagobi.utilities.engines.EngineConstants"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="java.util.Locale"%>
<%@page import="it.eng.spagobi.services.common.EnginConf"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA CODE 																--%>
<%-- ---------------------------------------------------------------------- --%>
<%
	ConsoleEngineConfig consoleEngineConfig;
	ConsoleEngineInstance consoleEngineInstance;	
	UserProfile profile;
	Locale locale;	
	
	String engineContext;
	String engineServerHost;
	String enginePort;
	String executionId;
	String spagobiServerHost;
	String spagobiContext;
	String spagobiSpagoController;
	
	
	consoleEngineConfig = ConsoleEngineConfig.getInstance();
	consoleEngineInstance = (ConsoleEngineInstance)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("ENGINE_INSTANCE");
	
	profile = (UserProfile)consoleEngineInstance.getEnv().get(EngineConstants.ENV_USER_PROFILE);	
	locale = (Locale)consoleEngineInstance.getEnv().get(EngineConstants.ENV_LOCALE);
	    
	// used in remote ServiceRegistry
    spagobiServerHost = request.getParameter(SpagoBIConstants.SBI_HOST);
    spagobiContext = request.getParameter(SpagoBIConstants.SBI_CONTEXT);
    spagobiSpagoController = request.getParameter(SpagoBIConstants.SBI_SPAGO_CONTROLLER);
    
 	// used in local ServiceRegistry
 	engineServerHost = request.getServerName();
 	enginePort = "" + request.getServerPort();
    engineContext = request.getContextPath();
    if( engineContext.startsWith("/") || engineContext.startsWith("\\") ) {
    	engineContext = request.getContextPath().substring(1);
    }
    
    executionId = request.getParameter("SBI_EXECUTION_ID");
    if(executionId != null) {
    	executionId = "'" + request.getParameter("SBI_EXECUTION_ID") + "'";
    } else {
    	executionId = "null";
    }   
%>


<%-- ---------------------------------------------------------------------- --%>
<%-- HTML	 																--%>
<%-- ---------------------------------------------------------------------- --%>
<html>
	
	<head>
		<%@include file="commons/includeExtJS.jspf" %>
		<%@include file="commons/includeSbiConsoleJS.jspf"%>
		
		<%-- START SCRIPT FOR DOMAIN DEFINITION (MUST BE EQUAL BETWEEN SPAGOBI AND EXTERNAL ENGINES) --
		<script type="text/javascript">
		document.domain='<%= EnginConf.getInstance().getSpagoBiDomain() %>';
		</script>
		-- END SCRIPT FOR DOMAIN DEFINITION --%>
	
	</head>

	<body  >
		<script>

			var template = <%= consoleEngineInstance.getTemplate().toString()  %>;

			Sbi.config = {};

			var url = {
				host: '<%= engineServerHost %>'
				, port: '<%= enginePort %>'
				, contextPath: '<%= engineContext %>'
			};
		
			var params = {
				SBI_EXECUTION_ID: <%= executionId %>
			};
		
			Sbi.config.serviceRegistry = new Sbi.service.ServiceRegistry({
				baseUrl: url
			    , baseParams: params
			});

			// javascript-side user profile object
	        Ext.ns("Sbi.user");
	        Sbi.user.userId = "<%= profile.getUserId() %>";

			Ext.onReady(function() { 
				Ext.QuickTips.init();
				var consolePanel = new Sbi.console.ConsolePanel(template);
				var viewport = new Ext.Viewport(consolePanel);  
			});
		</script>
		
	</body>
 
</html>




	

	
	
	
	
	
	
	
	
	
	
	
	
    