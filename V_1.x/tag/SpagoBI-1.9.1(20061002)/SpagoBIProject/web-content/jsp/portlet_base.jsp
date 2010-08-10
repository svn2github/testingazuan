<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="true" 
         import="it.eng.spago.base.*"
%>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>
<%      
it.eng.spago.base.ResponseContainer aResponseContainer = it.eng.spago.base.ResponseContainerPortletAccess.getResponseContainer(request);
        it.eng.spago.base.RequestContainer aRequestContainer = it.eng.spago.base.RequestContainerPortletAccess.getRequestContainer(request);
        it.eng.spago.base.SessionContainer aSessionContainer = (aRequestContainer != null ? aRequestContainer.getSessionContainer() : null);
        SourceBean aServiceRequest = aRequestContainer.getServiceRequest();
		SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
%>
<portlet:defineObjects/>

<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/spagobi.css")%>' 
      type='text/css' />

<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/jsr168.css")%>' 
      type='text/css' />
      
<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/external.css")%>' 
      type='text/css' />

<script>
	document.onselectstart = function() { return true; }
</script>

