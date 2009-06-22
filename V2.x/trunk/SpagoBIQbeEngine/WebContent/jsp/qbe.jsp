<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>

<%@page import="it.eng.spago.configuration.*"%>
<%@page import="it.eng.spago.base.*"%>

<%@ taglib uri="/WEB-INF/tlds/commons/qctl.tld" prefix="qbe" %>
<%@ taglib uri="/WEB-INF/tlds/jstl-1.1.2/c.tld" prefix="c" %>




<qbe:page>

	<%@include file="commons/includeExtJS.jspf" %>
   	
   	
	<qbe:url type="resource" var="href" ref="../css/spagobi.css"/>
	<link rel="styleSheet" href ="${href}" type="text/css" />
	
	<qbe:url type="resource" var="href" ref="../css/qbe.css"/>
	<link rel="styleSheet" href ="${href}" type="text/css" />
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/exceptionHandler.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/serviceRegistry.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<script type="text/javascript">   
    	var url = {
			host: '<%= request.getServerName()%>',
	        port: '<%= request.getServerPort()%>',
	        contextPath: '<%= request.getContextPath().startsWith("/")||request.getContextPath().startsWith("\\")?
	        				  request.getContextPath().substring(1):
	        				  request.getContextPath()%>',
	        execId: '<%= request.getParameter("SBI_EXECUTION_ID")%>'
    	};
    	it.eng.spagobi.engines.qbe.serviceregistry.module.init();
    	it.eng.spagobi.engines.qbe.serviceregistry.module.setBaseUrl(url);
    </script>
    
    <qbe:url type="resource" var="src" ref="../js/spagobi/qbe/overrides/overrides.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/commons.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	
	<!-- New OO GUI -->
	<qbe:url type="resource" var="src" ref="../js/spagobi/service/ServiceRegistry.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbex/DataStorePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	
	<script>
		Sbi.config = {};
	
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
	</script>
	<!-- New OO GUI -->
	
	
	
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/parser.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/filterWizard.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/buttonColumn.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/selectGrid.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/filterGrid.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/treePanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/queryResultsPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/queryBuilderPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	
	
	<!-- A Localization Script File comes here -->
    <qbe:url type="resource" var="src" ref="../js/spagobi/commons/locale.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/qbe/qbe.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
    
    
    <script type="text/javascript">  
      <%
      	String query = (String)ResponseContainerAccess.getResponseContainer(request).getServiceResponse().getAttribute("query");
      %>
      
      it.eng.spagobi.engines.qbe.app.setQuery( <%=query%> );
      Ext.onReady(it.eng.spagobi.engines.qbe.app.init, it.eng.spagobi.engines.qbe.app);        
    </script>
	
     

	<qbe:page-content>

	<div id="tabs"></div>
	<div id="menuTreePane1"></div>
	<div id="menuTreePane2"></div>
	<form id="form" 
		  method="post"
		  action=""
		  target=""></form>

	</qbe:page-content>

</qbe:page>