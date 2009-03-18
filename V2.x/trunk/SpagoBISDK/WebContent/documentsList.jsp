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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="it.eng.spagobi.services.proxy.SessionServiceProxy"%>
<%@page import="it.eng.spagobi.services.session.exceptions.AuthenticationException"%>
<%@page import="it.eng.spagobi.services.session.bo.Document"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Choose document</title>
</head>
<body>
<%
String user = request.getParameter("user");
String password = request.getParameter("password");
if (user != null && password != null) {
	session.setAttribute("spagobi_user", user);
	SessionServiceProxy proxy = new SessionServiceProxy();
	proxy.setEndpoint("http://localhost:8080/SpagoBI/services/WSSessionService");
	try {
		// session opening
		proxy.openSession(user, password);
		session.setAttribute("spagobi_proxy", proxy);
	} catch (AuthenticationException e) {
		%>
		User not authenticated
		<%
	}
}

SessionServiceProxy proxy = (SessionServiceProxy) session.getAttribute("spagobi_proxy");

if (proxy.isValidSession()) {
%>
<span><b>Choose a document</b></span>
<form action="chooseRole.jsp" method="post">
	Document: 
	<select name="documentId">
	<%
	// gets all visible documents list
	Document[] documents = proxy.getDocuments(null, null, null);
	for (int i = 0; i < documents.length; i++) {
		Document aDoc = documents[i];
		%>
		<option value="<%= aDoc.getId() %>"><%= aDoc.getName() %></option>
		<%
	}
	%>
	</select>
	<input type="submit" value="Go on" />
</form>
<%
}
%>
</body>
</html>