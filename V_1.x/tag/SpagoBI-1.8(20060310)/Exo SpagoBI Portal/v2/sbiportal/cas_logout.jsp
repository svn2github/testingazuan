<%@ page language="java" %>
<%@ page contentType="text/html" %>
<%@ page import="org.exoplatform.container.RootContainer"%>
<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page import="org.exoplatform.services.security.SecurityService"%>
<%@ page import="org.exoplatform.services.security.sso.SSOAuthenticationConfig"%>

<%
  String contextPath  =  request.getContextPath() ;
  String portalName = contextPath.substring(1, contextPath.length()) ;
  PortalContainer portalContainer = RootContainer.getInstance().getPortalContainer(portalName) ;
  if (portalContainer == null) {
    System.out.println("Portal (" + portalName + ") not found !") ;
    return ;
  }
  SecurityService securityService = (SecurityService) portalContainer.getComponentInstanceOfType(SecurityService.class) ;
  SSOAuthenticationConfig ssoConfig = securityService.getSSOAuthenticationConfig() ;
  String casUrl = "https://" + ssoConfig.getServerUrl() + ":" + ssoConfig.getServerPort() + "/" + ssoConfig.getApplicationPath() + "/";
  response.sendRedirect(casUrl + "logout?service=http://localhost:8080" + contextPath) ;
%>
