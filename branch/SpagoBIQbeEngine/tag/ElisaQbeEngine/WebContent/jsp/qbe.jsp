<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>

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

<%@ taglib uri="/WEB-INF/tlds/commons/qctl.tld" prefix="qbe" %>
<%@ taglib uri="/WEB-INF/tlds/jstl-1.1.2/c.tld" prefix="c" %>




<qbe:page>

	<!-- START SCRIPT FOR DOMAIN DEFINITION (MUST BE EQUAL BETWEEN SPAGOBI AND EXTERNAL ENGINES) -->
	<script type="text/javascript">
		document.domain='<%= EnginConf.getInstance().getSpagoBiDomain() %>';
	</script>
	<!-- END SCRIPT FOR DOMAIN DEFINITION -->

	<%@include file="commons/includeExtJS.jspf" %>
	
	<%
	QbeEngineInstance qbeEngineInstance;
	UserProfile profile;
	Locale locale;
	String isFromCross;
	boolean isPowerUser;
	
	qbeEngineInstance = (QbeEngineInstance)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("ENGINE_INSTANCE");
	
	profile = (UserProfile)qbeEngineInstance.getEnv().get(EngineConstants.ENV_USER_PROFILE);
	locale = (Locale)qbeEngineInstance.getEnv().get(EngineConstants.ENV_LOCALE);
	
	isFromCross = (String)qbeEngineInstance.getEnv().get("isFromCross");
	if (isFromCross == null) {
		isFromCross = "false";
	}
	isPowerUser = profile.getFunctionalities().contains(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY);
	
	//String language = (String)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("LANGUAGE");
	//UserProfile profile = (UserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	//boolean isPowerUser = profile.getFunctionalities().contains(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY);
	%>
	
	<qbe:url type="resource" var="href" ref="../css/qbe.css"/>
	<link rel="styleSheet" href ="${href}" type="text/css" />
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/parser.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/filterWizard.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/buttonColumn.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<!-- New OO GUI -->
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/overrides/overrides.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/exception/ExceptionHandler.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/service/ServiceRegistry.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/locale/LocaleUtils.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<script type="text/javascript" src='../js/spagobi/locale/<%=locale.getLanguage()%>.js'></script>
		
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/commons.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/DataStorePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/DataMartStructurePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/QueryCataloguePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/SelectGridDropTarget.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/SelectGridPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/OperandChooserWindow.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/FilterGridDropTarget.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/FilterGridPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/HavingGridPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/HavingGridDropTarget.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/SaveWindow.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/QueryBuilderPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>

	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/QbePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/FreeConditionsWindow.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/CalculatedFieldWizard.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/DocumentParametersGridPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/DocumentParametersStore.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<!-- New OO GUI -->
	
	
	<qbe:url type="resource" var="BLANK_IMAGE_URL" ref="../js/lib/ext-2.0.1/resources/images/default/s.gif"/>
	
	<script type="text/javascript">
	Ext.Ajax.timeout = 300000;
	</script>
    
    
    <script type="text/javascript">  
      <%
      //String query = (String)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("query");
      QbeEngineConfig qbeEngineConfig = QbeEngineConfig.getInstance();
      // settings for max records number limit
      Integer resultLimit = qbeEngineConfig.getResultLimit();
      boolean isMaxResultLimitBlocking = qbeEngineConfig.isMaxResultLimitBlocking();
      boolean isQueryValidationEnabled = qbeEngineConfig.isQueryValidationEnabled();
      boolean isQueryValidationBlocking = qbeEngineConfig.isQueryValidationBlocking();
      
      %>

      	Ext.BLANK_IMAGE_URL = '${BLANK_IMAGE_URL}';
      
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

	    <%
	    String spagobiServerHost = request.getParameter(SpagoBIConstants.SBI_HOST);
	    String spagobiContext = request.getParameter(SpagoBIConstants.SBI_CONTEXT);
	    String spagobiSpagoController = request.getParameter(SpagoBIConstants.SBI_SPAGO_CONTROLLER);
	    %>
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

        // javascript-side user profile object
        Ext.ns("Sbi.user");
        Sbi.user.isPowerUser = <%= isPowerUser %>;

        Ext.onReady(function(){
        	Ext.QuickTips.init();   

        	var parametersStore = new Sbi.qbe.DocumentParametersStore({});
        	var parametersInfo = <%= request.getParameter("SBI_DOCUMENT_PARAMETERS") %>;
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
	
     

	<qbe:page-content>


	</qbe:page-content>

</qbe:page>