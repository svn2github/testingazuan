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

<%@ page language="java" 
		 contentType="text/html; charset=ISO-8859-1"
    	 pageEncoding="ISO-8859-1"
    	 import="it.eng.spago.base.*,
    	 		 it.eng.spagobi.chiron.ChironStartAction"%>
    
<%
	
	ResponseContainer responseContainer = null;
	responseContainer = ResponseContainerAccess.getResponseContainer(request);	
	SourceBean serviceResponse = responseContainer.getServiceResponse();
	String mode = (String)serviceResponse.getAttribute(ChironStartAction.MODE);
	String rootDir = ChironStartAction.DEBUG_MODE.equalsIgnoreCase(mode)
						? "source"
						: "build";
%>

<%@page import="it.eng.spagobi.chiron.ChironStartAction;"%>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    	<title>CHIRON DEMO - v0.0</title>    	
    		<script type="text/javascript" src='../js/spagobi/<%=rootDir%>/script/spagobi.test.js'></script>
    	
	</head>
	
	<body>

	</body>

</html>

