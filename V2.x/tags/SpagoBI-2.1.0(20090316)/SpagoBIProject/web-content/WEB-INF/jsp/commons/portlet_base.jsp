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

<%@ page language="java"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="true" 
         import="it.eng.spago.base.*,
         		 it.eng.spago.configuration.ConfigSingleton,
         		 it.eng.spagobi.commons.utilities.urls.IUrlBuilder,
         		 it.eng.spagobi.commons.utilities.messages.IMessageBuilder"
%>
<%--
The following directive catches exceptions thrown by jsps, must be commented in development environment
--%>
<%@page errorPage="/html/error.html"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.WebUrlBuilder"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.PortletUrlBuilder"%>
<%@page import="it.eng.spagobi.commons.utilities.messages.MessageBuilder"%>
<%@page import="it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Map"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="java.util.Enumeration"%>
<%@page import="it.eng.spagobi.container.CoreContextManager"%>
<%@page import="it.eng.spagobi.container.SpagoBISessionContainer"%>
<%@page import="it.eng.spagobi.container.strategy.LightNavigatorContextRetrieverStrategy"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.commons.utilities.PortletUtilities"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>
<!-- IMPORT TAG LIBRARY  -->
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>

<%!
String getUrl(String baseUrl, Map mapPars) {
	StringBuffer buffer = new StringBuffer();
    buffer.append(baseUrl);
    buffer.append(baseUrl.indexOf("?") == -1 ? "?" : "&");
	if (mapPars != null && !mapPars.isEmpty()) {
		java.util.Set keys = mapPars.keySet();
		Iterator iterKeys = keys.iterator();
		while (iterKeys.hasNext()) {
		  	String key = iterKeys.next().toString();
		  	String value = mapPars.get(key).toString();
		  	buffer.append(key + "=" + value);
		  	if (iterKeys.hasNext()) {
		  		buffer.append("&");
		  	}
		}
	}
	return buffer.toString();
}
%>

<!-- SCRIPT FOR DOMAIN DEFINITION 
<script type="text/javascript">
	document.domain='engilab.ewebpd.eng.it';
</script>
-->

<!-- GET SPAGO OBJECTS  -->
<%
	//Enumeration headers = request.getHeaderNames();
	//while (headers.hasMoreElements()) {
	//	String headerName = (String) headers.nextElement();
	//	String header = request.getHeader(headerName);
	//	System.out.println(header + ": ");
	//}

	RequestContainer aRequestContainer = null;
	ResponseContainer aResponseContainer = null;
	SessionContainer aSessionContainer = null;
	IUrlBuilder urlBuilder = null;
	IMessageBuilder msgBuilder = null;
	
	String sbiMode = null;
		
	// case of portlet mode
	aRequestContainer = RequestContainerPortletAccess.getRequestContainer(request);
	aResponseContainer = ResponseContainerPortletAccess.getResponseContainer(request);
	if (aRequestContainer == null) {
		// case of web mode
		//aRequestContainer = RequestContainerAccess.getRequestContainer(request);
		aRequestContainer = RequestContainer.getRequestContainer();
		//aResponseContainer = ResponseContainerAccess.getResponseContainer(request);
		aResponseContainer = ResponseContainer.getResponseContainer();
	}
	
	String channelType = aRequestContainer.getChannelType();
	if ("PORTLET".equalsIgnoreCase(channelType)) sbiMode = "PORTLET";
	else sbiMode = "WEB";

    // = (String)sessionContainer.getAttribute(Constants.USER_LANGUAGE);
    //country = (String)sessionContainer.getAttribute(Constants.USER_COUNTRY);
	
	// create url builder 
	urlBuilder = UrlBuilderFactory.getUrlBuilder(sbiMode);

	// create message builder
	msgBuilder = MessageBuilderFactory.getMessageBuilder();
	
	// get other spago object
	SourceBean aServiceRequest = aRequestContainer.getServiceRequest();
	SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
	aSessionContainer = aRequestContainer.getSessionContainer();
	
	
	//get session access control object
	CoreContextManager contextManager = new CoreContextManager(new SpagoBISessionContainer(aSessionContainer), 
				new LightNavigatorContextRetrieverStrategy(aServiceRequest));
	
	// urls for resources
	String linkSbijs = urlBuilder.getResourceLink(request, "/js/spagobi.js");
	String linkProto = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/prototype.js");
	String linkProtoWin = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/window.js");
	String linkProtoEff = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/effects.js");
	String linkProtoDefThem = urlBuilder.getResourceLink(request, "/js/prototype/themes/default.css");
	String linkProtoAlphaThem = urlBuilder.getResourceLink(request, "/js/prototype/themes/alphacube.css");

	SessionContainer permanentSession = aSessionContainer.getPermanentContainer();
	

	// If Language is alredy defined keep it
	
	String curr_language=(String)permanentSession.getAttribute(SpagoBIConstants.AF_LANGUAGE);
	String curr_country=(String)permanentSession.getAttribute(SpagoBIConstants.AF_COUNTRY);
	Locale locale = null;

	if(curr_language!=null && curr_country!=null && !curr_language.equals("") && !curr_country.equals("")){
		locale=new Locale(curr_language, curr_country, "");
	}
	else {	
	if (sbiMode.equals("PORTLET")) {
		locale = PortletUtilities.getLocaleForMessage();
	} else {
		locale = MessageBuilder.getBrowserLocaleFromSpago();
	}
	// updates locale information on permanent container for Spago messages mechanism
	if (locale != null) {
		permanentSession.setAttribute(Constants.USER_LANGUAGE, locale.getLanguage());
		permanentSession.setAttribute(Constants.USER_COUNTRY, locale.getCountry());
	}
	}
	
	IEngUserProfile userProfile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	
	String userUniqueIdentifier="";
	String userId="";
	//if (userProfile!=null) userId=(String)userProfile.getUserUniqueIdentifier();
	if (userProfile!=null){
		userId=(String)((UserProfile)userProfile).getUserId();
		userUniqueIdentifier=(String)userProfile.getUserUniqueIdentifier();
	}
	
%>


<!-- based on ecexution mode include initial html  -->   
<% if (sbiMode.equalsIgnoreCase("WEB")){ %> 
<html lang="<%=locale != null ? locale.getLanguage() : GeneralUtilities.getDefaultLocale().getLanguage()%>">
<head>
	<link rel="shortcut icon" href="<%=urlBuilder.getResourceLink(request, "img/favicon.ico")%>" />
</head>
<body>
<%} %>

<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/ext-base.js")%>"></script>
<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/ext-all.js")%>"></script>
<SCRIPT language='JavaScript' src='<%=linkSbijs%>'></SCRIPT>

<!-- import css  --> 
<%
	// based on mode import right css 
	if (sbiMode.equalsIgnoreCase("WEB")) {
%>
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/spagobi_wa.css")%>' 
      type='text/css' />
<%  } else {  %>
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/spagobi_portlet.css")%>' 
      type='text/css' />
<%	} %>

<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/spagobi_shared.css")%>' 
      type='text/css' />

<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/jsr168.css")%>' 
      type='text/css' />
      
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/external.css")%>' 
      type='text/css' />
      
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/menu.css")%>' 
      type='text/css' />
      
   
<LINK rel='StyleSheet' 
	  href='<%=urlBuilder.getResourceLink(request, "css/extjs/ext-all.css")%>' 
	  type='text/css' />
	  	  
<LINK rel='StyleSheet'
      href='<%=urlBuilder.getResourceLink(request, "css/extjs/xtheme-gray.css")%>'
      type='text/css' />
      	  
 <!--   for web menu
<LINK rel='StyleSheet' 
	  href='<%=urlBuilder.getResourceLink(request, "css/extjs/extSpagoBI.css")%>' 
	  type='text/css' />
  -->
<script>
	document.onselectstart = function() { return true; }
</script>


