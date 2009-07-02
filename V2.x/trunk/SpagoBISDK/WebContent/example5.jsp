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
Login page: user must fill form with his credentials.
Authentication works properly only if 
it.eng.spagobi.services.security.service.ISecurityServiceSupplier.checkAuthentication(String userId, String psw) 
method is implemented, therefore it should work if SpagoBI is installed as a web application.
If SpagoBI is installed into a portal environment, the above method should be implemented for the portal in use (eXo, Liferay, ...).
The form points to documentsList.jsp.
*/
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<script type="text/javascript" src="js/sbisdk-all-production.js"></script>
	<!--  script type="text/javascript" src="http://localhost:8080/SpagoBI/js/src/sdk/sbisdk-all-production.js"></script -->

	<script type="text/javascript">

		Sbi.sdk.services.setBaseUrl({
	        protocol: 'http'     
	        , host: 'localhost'
	        , port: '8080'
	        , contextPath: 'SpagoBI'
	        , controllerPath: 'servlet/AdapterHTTP'  
	    });
		
		execTest5 = function() {
		    Sbi.sdk.api.injectDocument({
				documentLabel: 'RPT_WAREHOUSE_PROF'
				, executionRole: '/spagobi/user'
				, parameters: {warehouse_id: 19}
				, displayToolbar: false
				, displaySliders: false
				, target: 'targetDiv'
				, height: '500px'
				, width: '800px'
				, iframe: {
					style: 'border: 0px;'
				}
			});
		}
	</script>
</head>


<body>
<h2>Example 5 : injectDocument into non-existing div</h2>
<hr>
<b>Description:</b> Use <i>injectDocument</i> function to inject into a div an html string that contains the definition of an iframe 
pointing to the execution service. In this example the specified target div does not exist so it is created on the fly by the function
	
<p>
<b>Code: </b>
<p>
<BLOCKQUOTE>
<PRE>
example5Function = function() {
	Sbi.sdk.api.injectDocument({
		documentLabel: 'RPT_WAREHOUSE_PROF'
		, executionRole: '/spagobi/user'
		, parameters: {warehouse_id: 19}
		, displayToolbar: false
		, displaySliders: false
		, target: 'targetDiv'
		, height: '500px'
		, width: '800px'
		, iframe: {
			style: 'border: 0px;'
		}
	});
}
</PRE>
</BLOCKQUOTE>
<hr>

<script type="text/javascript">
	execTest5();
</script>
</body>
</html>