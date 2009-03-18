<%--
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
--%>

<%
/**
This page use the SpagoBI execution tag, that displays an iframe pointing to SpagoBI context with all information about document execution 
(document identifier, role to be used, values for parameters).
*/
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spagobi" tagdir="/WEB-INF/tags/spagobi" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="it.eng.spagobi.services.session.bo.DocumentParameter"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Document execution</title>
</head>
<body>
<%
Integer documentId = (Integer) session.getAttribute("spagobi_documentId");
String userId = (String) session.getAttribute("spagobi_user");
String role = (String) session.getAttribute("spagobi_role");
DocumentParameter[] parameters = (DocumentParameter[]) session.getAttribute("spagobi_document_parameters"); 
Map parameterValues = new HashMap();
if (parameters != null && parameters.length > 0) {
	for (int i = 0; i < parameters.length; i++) {
		DocumentParameter aParameter = parameters[i];
		String value = request.getParameter(aParameter.getUrlName());
		if (value != null) {
			parameterValues.put(aParameter.getUrlName(), value);
		}
	}
}
%>
<spagobi:execution 
		spagobiContext="http://localhost:8080/SpagoBI/"
		userId="<%= userId %>" 
        documentId="<%= documentId.toString() %>"
        iframeStyle="height:500px; width:100%" 
        executionRole="<%= role %>"
        parametersMap="<%= parameterValues %>"
        displayToolbar="<%= Boolean.TRUE %>"
        displaySliders="<%= Boolean.TRUE %>" />

<a href="documentsList.jsp">Back to documents list</a>

</body>
</html>