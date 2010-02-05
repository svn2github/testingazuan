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
author: Andrea Gioia (andrea.gioia@eng.it)
--%>
<%@ page language="java" 
	     contentType="text/html; charset=ISO-8859-1" 
	     pageEncoding="ISO-8859-1"%>	


<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA IMPORTS															--%>
<%-- ---------------------------------------------------------------------- --%>
<%@page import="it.eng.spago.configuration.*"%>
<%@page import="it.eng.spago.base.*"%>
<%@page import="it.eng.spagobi.utilities.engines.EngineConstants"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="it.eng.spagobi.engines.commonj.*"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<%-- ---------------------------------------------------------------------- --%>
<%-- JAVA CODE 																--%>
<%-- ---------------------------------------------------------------------- --%>
<%
	//QbeEngineInstance qbeEngineInstance;
	UserProfile profile;
	Locale locale;
	String isFromCross;
	boolean isPowerUser;
	Integer resultLimit;
	boolean isMaxResultLimitBlocking;
	boolean isQueryValidationEnabled;
	boolean isQueryValidationBlocking;
	String spagobiServerHost;
	String spagobiContext;
	String spagobiSpagoController;
	String docId;
	
	/*qbeEngineInstance = (QbeEngineInstance)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("ENGINE_INSTANCE");
	List datamartNames = qbeEngineInstance.getDatamartModel().getDataSource().getDatamartNames();
	profile = (UserProfile)qbeEngineInstance.getEnv().get(EngineConstants.ENV_USER_PROFILE);
	locale = (Locale)qbeEngineInstance.getEnv().get(EngineConstants.ENV_LOCALE);
	
	isFromCross = (String)qbeEngineInstance.getEnv().get("isFromCross");
	if (isFromCross == null) {
		isFromCross = "false";
	}
	isPowerUser = profile.getFunctionalities().contains(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY);*/
	
	CommonjEngineConfig qcommonJEngineConfig = CommonjEngineConfig.getInstance();

    spagobiServerHost = request.getParameter(SpagoBIConstants.SBI_HOST);
    spagobiContext = request.getParameter(SpagoBIConstants.SBI_CONTEXT);
    spagobiSpagoController = request.getParameter(SpagoBIConstants.SBI_SPAGO_CONTROLLER);
    docId = request.getParameter("document");
%>


<%-- ---------------------------------------------------------------------- --%>
<%-- HTML	 																--%>
<%-- ---------------------------------------------------------------------- --%>
<html>
	
	<head>
		<%@include file="commons/includeExtJS.jspf" %>
	    <%@include file="commons/includeSrcJS.jspf" %>
	</head>
	
	<body>
	
    	<script type="text/javascript">  
	     
	     var generalPanel=null;
	        
	        Ext.onReady(function(){
	        	Ext.QuickTips.init();   
	        	
	        	Sbi.config = {};
	        	
	        	var url = {
			    	host: '<%= request.getServerName()%>'
			    	, port: '<%= request.getServerPort()%>'
			    	, contextPath: '<%= request.getContextPath().startsWith("/")||request.getContextPath().startsWith("\\")?
			    	   				  request.getContextPath().substring(1):
			    	   				  request.getContextPath()%>'
			    	    
		    	};
		
			    Sbi.config.serviceRegistry = new Sbi.commons.ServiceRegistry({
			    	baseUrl: url
			    });
	        	
	        	generalPanel= new Sbi.commons.ExecutionPanel({document_id:<%=docId%>});
	        	//generalPanel.monitorStatus();
		        
		        var viewport = new Ext.Viewport({
	           		items: [generalPanel]
	           	});  
	           	
	           //	setTimeout("timer()", 5000);
	           	timer();
	           	
	      	});
	      	
	      	
	     function timer(){
			//alert(generalPanel);	
			generalPanel.statusProcess(<%=docId%>);
			setTimeout("timer()", 10000);	
		}
	
		
	      	
	      	
	      	
	    </script>
	
	</body>

</html>