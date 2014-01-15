<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spagobi" tagdir="/WEB-INF/tags/spagobi" %>
<%@page import="java.util.*"%>
<%@page import="it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter"%>
<%@page import="it.eng.spagobi.sdk.documents.bo.SDKDocument"%>
<%@page import="it.eng.spagobi.sdk.config.SpagoBISDKConfig"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.net.URI"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Document execution</title>
	<style>
	body, html, p { font-family:sans-serif;font-size:11px; padding:0; margin:0; overflow:hidden; background: #f0f0f0; }
	pre { font-size:8pt; }
    iframe { border:0; height:100%; width:100%; position:absolute; }
	</style>
</head>
<body>

	<script type="text/javascript" src="js/commons.js"></script>
	<script type="text/javascript" src="js/ajax.js"></script>
	<script type="text/javascript" src="js/jsonp.js"></script>	
	<script type="text/javascript" src="js/api.js"></script>	
	<script type="text/javascript" src="js/services.js"></script>

<%
String user = (String) session.getAttribute("spagobi_user");
String password = (String) session.getAttribute("spagobi_pwd");
String spagobiserverurl = SpagoBISDKConfig.getInstance().getSpagoBIServerUrl() + "/";
if (user != null && password != null) {
	Integer documentId = (Integer) session.getAttribute("spagobi_documentId");
	SDKDocument document = null;
	SDKDocument[] documents = (SDKDocument[]) session.getAttribute("spagobi_documents");
	for (int i = 0; i < documents.length; i++) {
		SDKDocument aDocument = documents[i];
		if (aDocument.getId().equals(documentId)) {
			document = aDocument;
		}
	}
	session.setAttribute("spagobi_current_document", document);
	String role = request.getParameter("role");
	
	URI spagobiURI = new URI(SpagoBISDKConfig.getInstance().getSpagoBIServerUrl());
	%>
	<script type="text/javascript">

		Sbi.sdk.services.setBaseUrl({
	        protocol: '<%= spagobiURI.getScheme() %>'
	        , host: '<%= spagobiURI.getHost() %>'
	        , port: '<%= spagobiURI.getPort() %>'
	        , contextPath: '<%= spagobiURI.getRawPath().substring(1) %>'
	        , controllerPath: 'servlet/AdapterHTTP'
	    });
		
	    authenticationCallback = function (result, args, success) {
			if (success === true) {
				diplayDocument();
			} else {
				alert('ERROR: Wrong username or password');
			}
	    };
	    
		diplayDocument = function() {
		    Sbi.sdk.api.injectDocument({
				documentLabel: '<%= StringEscapeUtils.escapeJavaScript(document.getLabel()) %>'
				, executionRole: '<%= StringEscapeUtils.escapeJavaScript(role) %>'
				, displayToolbar: false
				, displaySliders: false
			});
		};
		
	    Sbi.sdk.api.authenticate({
			params: {
				user: '<%= StringEscapeUtils.escapeJavaScript(user) %>'
				, password: '<%= StringEscapeUtils.escapeJavaScript(password) %>'
			}
			, callback: {
				fn: authenticationCallback
				, scope: this
			}
		});
		
	</script>
	<%
} else {
	response.sendRedirect("login.jsp");
}
%>
</body>
</html>