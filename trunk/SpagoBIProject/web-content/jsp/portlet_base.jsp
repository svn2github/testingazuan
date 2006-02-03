<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="true"
%>
<%@ page import="it.eng.spago.base.*"%>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>



<%      it.eng.spago.base.ResponseContainer aResponseContainer = it.eng.spago.base.ResponseContainerPortletAccess.getResponseContainer(request);
        it.eng.spago.base.RequestContainer aRequestContainer = it.eng.spago.base.RequestContainerPortletAccess.getRequestContainer(request);
        it.eng.spago.base.SessionContainer aSessionContainer = (aRequestContainer != null ? aRequestContainer.getSessionContainer() : null);
        SourceBean aServiceRequest = aRequestContainer.getServiceRequest();
		SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
%>

<portlet:defineObjects/>



