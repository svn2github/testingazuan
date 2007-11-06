<!--
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
-->


<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*,
         		 it.eng.spago.configuration.ConfigSingleton,
         		 it.eng.spagobi.utilities.urls.IUrlBuilder,
         		 it.eng.spagobi.utilities.messages.IMessageBuilder"
%>
<%@page import="it.eng.spagobi.utilities.urls.WebUrlBuilder"%>
<%@page import="it.eng.spagobi.utilities.urls.PortletUrlBuilder"%>
<%@page import="it.eng.spagobi.utilities.messages.MessageBuilder"%>
<%@page import="it.eng.spagobi.utilities.messages.MessageBuilderFactory"%>
<%@page import="it.eng.spagobi.utilities.urls.UrlBuilderFactory"%>

<!-- IMPORT TAG LIBRARY  -->
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>

<!-- GET SPAGO OBJECTS  -->
<%
	RequestContainer aRequestContainer = null;
	ResponseContainer aResponseContainer = null;
	SessionContainer aSessionContainer = null;
	IUrlBuilder urlBuilder = null;
	IMessageBuilder msgBuilder = null;
	
	// get configuration
	ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
	
	// check if the sbi path is already setted into configuration otherwise add it
	//if(spagoconfig.getAttribute(SpagoBIConstants.SBICONTEXTURL)==null) {
	//	// calculate spagobi context address
	//	String  sbipath = request.getScheme() + "://"+request.getServerName()+ ":"+request.getServerPort() + request.getContextPath();
	//	spagoconfig.setAttribute(SpagoBIConstants.SBICONTEXTURL, sbipath); 
	//}
	
	
	// get mode of execution
	//String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
	String sbiMode = null;
		
	// case of portlet mode
	aRequestContainer = RequestContainerPortletAccess.getRequestContainer(request);
	aResponseContainer = ResponseContainerPortletAccess.getResponseContainer(request);
	if (aRequestContainer == null) {
		// case of web mode
		aRequestContainer = RequestContainerAccess.getRequestContainer(request);
		aResponseContainer = ResponseContainerAccess.getResponseContainer(request);
	}
	
	String channelType = aRequestContainer.getChannelType();
	if ("PORTLET".equalsIgnoreCase(channelType)) sbiMode = "PORTLET";
	else sbiMode = "WEB";
	
	// create url builder 
	urlBuilder = UrlBuilderFactory.getUrlBuilder(sbiMode);

	// create message builder
	msgBuilder = MessageBuilderFactory.getMessageBuilder();
	
	// get other spago object
	SourceBean aServiceRequest = aRequestContainer.getServiceRequest();
	SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
	aSessionContainer = aRequestContainer.getSessionContainer();
	
	// urls for resources
	String linkSbijs = urlBuilder.getResourceLink(request, "/js/spagobi.js");
	String linkProto = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/prototype.js");
	String linkProtoWin = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/window.js");
	String linkProtoEff = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/effects.js");
	String linkProtoDefThem = urlBuilder.getResourceLink(request, "/js/prototype/themes/default.css");
	String linkProtoAlphaThem = urlBuilder.getResourceLink(request, "/js/prototype/themes/alphacube.css");
	
%>
 
<SCRIPT language='JavaScript' src='<%=linkSbijs%>'></SCRIPT>
<script type="text/javascript" src="<%=linkProto%>"></script>
<script type="text/javascript" src="<%=linkProtoWin%>"></script>
<script type="text/javascript" src="<%=linkProtoEff%>"></script>
<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/>

   
<!-- based on ecexution mode include initial html  -->   
<% if (sbiMode.equalsIgnoreCase("WEB")){ %> 
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<html>
<body>
<%} %>


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

<script>
	document.onselectstart = function() { return true; }
</script>

