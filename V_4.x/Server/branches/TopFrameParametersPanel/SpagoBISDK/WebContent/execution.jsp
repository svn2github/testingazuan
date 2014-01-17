<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>

<%
/**
This page use the SpagoBI execution tag, that displays an iframe pointing to SpagoBI context with all information about document execution 
(document identifier, role to be used, values for parameters).
*/
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spagobi" tagdir="/WEB-INF/tags/spagobi" %>
<%@page import="java.util.*"%>
<%@page import="it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter"%>
<%@page import="it.eng.spagobi.sdk.documents.bo.SDKDocument"%>
<%@page import="it.eng.spagobi.sdk.config.SpagoBISDKConfig"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Document execution</title>
	<style>
	body, p { font-family:Tahoma; font-size:10pt; padding-left:30; }
	pre { font-size:8pt; }
	</style>
</head>
<body>
<%
String user = (String) session.getAttribute("spagobi_user");
String password = (String) session.getAttribute("spagobi_pwd");
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
	%>
	<spagobi:execution 
			spagobiContext="<%= SpagoBISDKConfig.getInstance().getSpagoBIServerUrl() %>"
			userId="<%= user %>" 
			password="<%= password %>" 
	        documentId="<%= documentId.toString() %>"
	        iframeStyle="height:500px; width:100%" 
	        executionRole="<%= role %>"
	        displayToolbar="<%= Boolean.FALSE %>"
	        displaySliders="<%= Boolean.FALSE %>" 
	        target="_self" />
	<%
} else {
	response.sendRedirect("login.jsp");
}
%>
</body>
</html>