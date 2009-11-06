<%@ page language="java" 
	     contentType="text/html; charset=ISO-8859-1" 
	     pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Locale"%>
<%
Locale locale = Locale.ITALIAN;
%>
<%-- ---------------------------------------------------------------------- --%>
<%-- HTML	 																--%>
<%-- ---------------------------------------------------------------------- --%>

<%-- DOCTYPE declaration: it is required in order to fix some side effects, in particular in IE --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%-- and DOCTYPE declaration --%>
    
<html>

	<head>
		<%@include file="commons/includeExtJS.jspf" %>
		<%@include file="commons/includeSbiFormViewerJS.jspf"%>
	</head>
	
	<body>
	
    	<script type="text/javascript"> 

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
	    
        Ext.onReady(function() {
        	Ext.QuickTips.init();
        	
            var viewer = new Sbi.formviewer.ViewerPanel(template, {region: 'center'});
           	var viewport = new Ext.Viewport({layout: 'border', items: [viewer]});
           	
      	});
      	
	    </script>
	
	</body>

</html>