
<%@ page language="java" 
	     contentType="text/html; charset=ISO-8859-1" 
	     pageEncoding="ISO-8859-1"%>	

<%@page import="it.eng.spago.configuration.*"%>
<%@page import="it.eng.spago.base.*"%>
<%@page import="it.eng.spagobi.qbe.QbeEngineConfig"%>
<%@page import="it.eng.spagobi.qbe.QbeEngineInstance"%>
<%@page import="it.eng.spagobi.utilities.engines.EngineConstants"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="java.util.Locale"%>
<%@page import="it.eng.spagobi.services.common.EnginConf"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<%
	QbeEngineInstance qbeEngineInstance;
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
	
	qbeEngineInstance = (QbeEngineInstance)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("ENGINE_INSTANCE");
	List datamartNames = qbeEngineInstance.getDatamartModel().getDataSource().getDatamartNames();
	profile = (UserProfile)qbeEngineInstance.getEnv().get(EngineConstants.ENV_USER_PROFILE);
	locale = (Locale)qbeEngineInstance.getEnv().get(EngineConstants.ENV_LOCALE);
	
	isFromCross = (String)qbeEngineInstance.getEnv().get("isFromCross");
	if (isFromCross == null) {
		isFromCross = "false";
	}
	isPowerUser = profile.getFunctionalities().contains(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY);
	
	QbeEngineConfig qbeEngineConfig = QbeEngineConfig.getInstance();
    
    // settings for max records number limit
    resultLimit = qbeEngineConfig.getResultLimit();
    isMaxResultLimitBlocking = qbeEngineConfig.isMaxResultLimitBlocking();
    isQueryValidationEnabled = qbeEngineConfig.isQueryValidationEnabled();
    isQueryValidationBlocking = qbeEngineConfig.isQueryValidationBlocking();
    
    spagobiServerHost = request.getParameter(SpagoBIConstants.SBI_HOST);
    spagobiContext = request.getParameter(SpagoBIConstants.SBI_CONTEXT);
    spagobiSpagoController = request.getParameter(SpagoBIConstants.SBI_SPAGO_CONTROLLER);
%>


<html>
	
	<head>
		<%@include file="commons/includeExtJS.jspf" %>
		<%@include file="commons/includeSbiQbeJS.jspf"%>
		
		<!-- START SCRIPT FOR DOMAIN DEFINITION (MUST BE EQUAL BETWEEN SPAGOBI AND EXTERNAL ENGINES) -->
		<script type="text/javascript">
		document.domain='<%= EnginConf.getInstance().getSpagoBiDomain() %>';
		</script>
		<!-- END SCRIPT FOR DOMAIN DEFINITION -->
	
	</head>
	
	<body>
	
    	<script type="text/javascript">  
			Sbi.config = {};
	
			Sbi.config.queryLimit = {};
			Sbi.config.queryLimit.maxRecords = <%= resultLimit != null ? "" + resultLimit.intValue() : "undefined" %>;
			Sbi.config.queryLimit.isBlocking = <%= isMaxResultLimitBlocking %>;
			Sbi.config.queryValidation = {};
			Sbi.config.queryValidation.isEnabled = <%= isQueryValidationEnabled %>;
			Sbi.config.queryValidation.isBlocking = <%= isQueryValidationBlocking %>;
	  	
			var url = {
		    	host: '<%= request.getServerName()%>'
		    	, port: '<%= request.getServerPort()%>'
		    	, contextPath: '<%= request.getContextPath().startsWith("/")||request.getContextPath().startsWith("\\")?
		    	   				  request.getContextPath().substring(1):
		    	   				  request.getContextPath()%>'
		    	    
		    };
	
		    var params = {
		    	SBI_EXECUTION_ID: <%= request.getParameter("SBI_EXECUTION_ID")!=null?"'" + request.getParameter("SBI_EXECUTION_ID") +"'": "null" %>
		    };
	
		    Sbi.config.serviceRegistry = new Sbi.service.ServiceRegistry({
		    	baseUrl: url
		        , baseParams: params
		    });
	
			var remoteUrl = {
				completeUrl: '<%= spagobiServerHost + spagobiContext + spagobiSpagoController %>'
			};
		    
		    Sbi.config.remoteServiceRegistry = new Sbi.service.ServiceRegistry({
		    	baseUrl: remoteUrl
		        , baseParams: params
		        , defaultAbsolute: true
		    });
	
	      	var qbeConfig = {};
	      	qbeConfig.isFromCross = <%= isFromCross %>;
	      	<%
	      	StringBuffer datamartNamesBuffer = new StringBuffer("[");
	      	Iterator it = datamartNames.iterator();
	      	while (it.hasNext()) {
	      		String datamartName = (String) it.next();
	      		datamartNamesBuffer.append("'" + datamartName + "'");
	      		if (it.hasNext()) {
	      			datamartNamesBuffer.append(",");
	      		}
	      	}
	      	datamartNamesBuffer.append("]");
	      	%>
	      	qbeConfig.westConfig = {};
	      	qbeConfig.westConfig.datamartsName = <%= datamartNamesBuffer.toString() %>;
	
	        // javascript-side user profile object
	        Ext.ns("Sbi.user");
	        Sbi.user.isPowerUser = <%= isPowerUser %>;
	
	        Ext.onReady(function(){
	        	Ext.QuickTips.init();   
	
	        	var parametersStore = new Sbi.qbe.DocumentParametersStore({});
	        	var parametersInfo = <%= request.getParameter("SBI_DOCUMENT_PARAMETERS") != null ? request.getParameter("SBI_DOCUMENT_PARAMETERS") : "[{}]" %>;
	        	parametersStore.loadData(parametersInfo);
	        	
	       		qbeConfig.documentParametersStore = parametersStore;
	           	var qbe = new Sbi.qbe.QbePanel(qbeConfig);
	           	var viewport = new Ext.Viewport(qbe);  
	           	<%if (isPowerUser && isFromCross.equalsIgnoreCase("false")) {%>
	           		qbe.queryEditorPanel.selectGridPanel.dropTarget = new Sbi.qbe.SelectGridDropTarget(qbe.queryEditorPanel.selectGridPanel); 
	           		qbe.queryEditorPanel.filterGridPanel.dropTarget = new Sbi.qbe.FilterGridDropTarget(qbe.queryEditorPanel.filterGridPanel);
	           		qbe.queryEditorPanel.havingGridPanel.dropTarget = new Sbi.qbe.HavingGridDropTarget(qbe.queryEditorPanel.havingGridPanel);
	           	<%}%>
	           	
	      	});
	    </script>
	
	</body>

</html>




	

	
	
	
	
	
	
	
	
	
	
	
	
    