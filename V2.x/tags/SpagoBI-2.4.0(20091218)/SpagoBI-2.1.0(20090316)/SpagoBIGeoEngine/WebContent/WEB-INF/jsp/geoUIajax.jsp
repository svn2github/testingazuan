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

<%-- 
author: Andrea Gioia (andrea.gioia@eng.it)
--%>

<%@ page language="java" 
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/WEB-INF/tlds/commons/qctl.tld" prefix="qbe" %>
<%@ taglib uri="/WEB-INF/tlds/jstl-1.1.2/c.tld" prefix="c" %>

<qbe:page>

	<%@include file="commons/includeExtJS.jspf" %>
   	
	<qbe:url type="resource" var="href" ref="../../css/geo.css"/>
	<link rel="styleSheet" href ="${href}" type="text/css" />
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/commons.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/ComponentBuddy.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/ExceptionHandler.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/ServiceRegistry.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/commons/ServiceSequence.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	
	
	
	
    
    <qbe:url type="resource" var="src" ref="../js/spagobi/geo/overrides/overrides.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/geo/Geo.js"/>
	<script type="text/javascript" src='${src}'/></script>	
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/geo/SaveAnalysisWindow.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/geo/DrillControlPanel.js"/>
	<script type="text/javascript" src='${src}'/></script>
	
	<qbe:url type="resource" var="src" ref="../js/spagobi/geo/GeoApp.js"/>
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
    	
    	
    	Sbi.geo.app.serviceRegistry = new Sbi.commons.ServiceRegistry({baseUrl: url});
    </script>
	
    
    
    <script type="text/javascript">  
      Ext.onReady(Sbi.geo.app.init, Sbi.geo.app.app);        
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


