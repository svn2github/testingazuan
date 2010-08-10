<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
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

<%--
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>
<portlet:defineObjects/>
--%>

<!-- GET SPAGO OBJECTS  -->
<%
	RequestContainer aRequestContainer = null;
	ResponseContainer aResponseContainer = null;
	SessionContainer aSessionContainer = null;
	IUrlBuilder urlBuilder = null;
	IMessageBuilder msgBuilder = null;
	
	// get configuration
	ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
	// get mode of execution
	String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
	
	// based on mode get spago object 
	if (sbiMode.equalsIgnoreCase("WEB")) {
		aRequestContainer = RequestContainerAccess.getRequestContainer(request);
		aResponseContainer = ResponseContainerAccess.getResponseContainer(request);	
	} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
		aRequestContainer = RequestContainerPortletAccess.getRequestContainer(request);
		aResponseContainer = ResponseContainerPortletAccess.getResponseContainer(request);
	}
	
	// create url builder 
	urlBuilder = UrlBuilderFactory.getUrlBuilder();

	// create message builder
	msgBuilder = MessageBuilderFactory.getMessageBuilder();
	
	// get other spago object
	SourceBean aServiceRequest = aRequestContainer.getServiceRequest();
	SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
	aSessionContainer = aRequestContainer.getSessionContainer();
%>
 
 
   
<!-- based on ecexution mode include initial html  -->   
<% if (sbiMode.equalsIgnoreCase("WEB")){ %> 
<html>
<body>
<%} %>


<!-- import css  --> 
<%
	// based on mode import right css 
	if (sbiMode.equalsIgnoreCase("WEB")) {
%>
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/wa/spagobi.css")%>' 
      type='text/css' />
<%  } else {  %>
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/spagobi.css")%>' 
      type='text/css' />
<%	} %>

<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/jsr168.css")%>' 
      type='text/css' />
      
<LINK rel='StyleSheet' 
      href='<%=urlBuilder.getResourceLink(request, "css/external.css")%>' 
      type='text/css' />

<script>
	document.onselectstart = function() { return true; }
</script>

