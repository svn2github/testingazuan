<%-- SpagoBI, the Open Source Business Intelligence suite

Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. --%>
 
<%@ page language="java"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" 
%>
<%@page session="false" %>

<%@page import="java.util.Locale"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory"%>
<%@page import="it.eng.spagobi.commons.utilities.SpagoBIUtilities"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.IUrlBuilder"%>
<%@page import="it.eng.spagobi.utilities.themes.ThemesManager"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%
String header = request.getHeader("Powered-By");
if (header != null && header.equals("Ext")) {
	response.setStatus(500);
	JSONObject sessionExpiredError = new JSONObject();
	sessionExpiredError.put("message", "session-expired");
	JSONArray array = new JSONArray();
	array.put(sessionExpiredError);
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("errors", array);
	out.clear();
	out.write(jsonObject.toString());
	out.flush();
} else {
	
	IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder("WEB"); // it can only be web, i.e. in case of portlet request, the session expiration event 
																	 // is managed by the portlet container
	 // get the current ext theme
	String currTheme = ThemesManager.getDefaultTheme();
	String extJSTheme = ThemesManager.getTheExtTheme(currTheme);
	Locale locale = GeneralUtilities.getDefaultLocale();
	
%>

<html lang="<%=locale.getLanguage()%>">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<link rel="shortcut icon" href="<%=urlBuilder.getResourceLinkByTheme(request, "img/favicon.ico", currTheme)%>" />
</head>
<body>

<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/lib/ext-3.1.1/adapter/ext/ext-base.js")%>"></script>
<%-- Ext lib for release --%>
<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/lib/ext-3.1.1/ext-all.js")%>"></script>
<%-- Ext js overrides --%>
<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/lib/ext-3.1.1/overrides/overrides.js")%>"></script>

<LINK rel='StyleSheet'  href='<%=urlBuilder.getResourceLink(request, "js/lib/ext-3.1.1/resources/css/ext-all.css")%>' type='text/css' />
<%-- Ext css overrides --%>
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "js/src/ext/sbi/overrides/overrides.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "js/lib/ext-3.1.1/overrides/resources/css/overrides.css")%>' type='text/css' />
	  	  
<LINK rel='StyleSheet' href='<%= urlBuilder.getResourceLink(request, "js/lib/ext-3.1.1/resources/css/" + extJSTheme) %>' type='text/css' />

<%@ include file="/WEB-INF/jsp/commons/importSbiJS311.jspf"%>

<script type="text/javascript">
    Ext.BLANK_IMAGE_URL = '<%= urlBuilder.getResourceLink(request, "/js/lib/ext-3.1.1/resources/images/default/s.gif") %>';
    
	Ext.onReady(function(){
    	Ext.MessageBox.show({
       		title: LN('sbi.general.sessionexpired.title')
       		, msg: LN('sbi.general.sessionexpired.message')
       		, buttons: Ext.MessageBox.OK     
       		, icon: Ext.MessageBox.WARNING
       		, modal: true
   		});
	});
</script>

</body>
</html>
<%
}
%>