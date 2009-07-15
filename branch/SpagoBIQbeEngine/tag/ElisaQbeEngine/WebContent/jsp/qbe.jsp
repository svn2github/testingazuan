<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>

<%@page import="it.eng.spago.configuration.*"%>
<%@page import="it.eng.spago.base.*"%>
<%@page import="it.eng.spagobi.qbe.QbeEngineConfig"%>

<%@ taglib uri="/WEB-INF/tlds/commons/qctl.tld" prefix="qbe" %>
<%@ taglib uri="/WEB-INF/tlds/jstl-1.1.2/c.tld" prefix="c" %>





<qbe:page>

	<%@include file="commons/includeExtJS.jspf" %>
   	
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
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/locale/en.js"/>
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
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/FilterGridDropTarget.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/FilterGridPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/SaveWindow.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/QueryBuilderPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>

	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/QbePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/FreeConditionsWindow.js"/>
	<script type="text/javascript" src='${src}'/></script>
	<!-- New OO GUI -->
	
	
	
	
	
    
    
    <script type="text/javascript">  
      <%
      String query = (String)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("query");
      QbeEngineConfig qbeEngineConfig = QbeEngineConfig.getInstance();
      // settings for max records number limit
      Integer resultLimit = qbeEngineConfig.getResultLimit();
      boolean isMaxResultLimitBlocking = qbeEngineConfig.isMaxResultLimitBlocking();
      boolean isQueryValidationEnabled = qbeEngineConfig.isQueryValidationEnabled();
      boolean isQueryValidationBlocking = qbeEngineConfig.isQueryValidationBlocking();
      %>
      
		Sbi.config = {};

		Sbi.config.queryLimit = {};
		Sbi.config.queryLimit.maxRecords = <%= resultLimit != null ? resultLimit.intValue() : "undefined" %>;
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

      	var qbeConfig = {};
      	qbeConfig.query = <%= query %>;
      	
        Ext.onReady(function(){
        	Ext.QuickTips.init();              
        	//var qbe = new Sbi.qbe.QueryBuilderPanel() ;//Sbi.qbe.QbePanel(qbeConfig);
        	var qbe = new Sbi.qbe.QbePanel(qbeConfig);
        	var viewport = new Ext.Viewport(qbe);  
        	
        	qbe.queryEditorPanel.selectGridPanel.dropTarget = new Sbi.qbe.SelectGridDropTarget(qbe.queryEditorPanel.selectGridPanel); 
        	qbe.queryEditorPanel.filterGridPanel.dropTarget = new Sbi.qbe.FilterGridDropTarget(qbe.queryEditorPanel.filterGridPanel); 
      	});
    </script>
	
     

	<qbe:page-content>


	</qbe:page-content>

</qbe:page>