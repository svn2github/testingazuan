<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.exoplatform.services.portletcontainer.pci.Output"%>

<%@ page import="org.exoplatform.container.RootContainer"%>
<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page import="org.exoplatform.services.security.SecurityService"%>
<%@ page import="org.exoplatform.services.security.sso.SSOAuthenticationConfig"%>

<%@ page import="edu.yale.its.tp.cas.client.*"%>
<%@ page language="java" %>
<%@ page contentType="text/html" %>

<%
  String contextPath  =  request.getContextPath() ;
  String service = contextPath + "/faces/private/" ;
  String portalName = contextPath.substring(1, contextPath.length()) ;
  
  PortalContainer portalContainer = RootContainer.getInstance().getPortalContainer(portalName) ;
  if (portalContainer == null) {
    System.out.println("Portal (" + portalName + ") not found !") ;
    return ;
  }
  SecurityService securityService = (SecurityService) portalContainer.getComponentInstanceOfType(SecurityService.class) ;
  SSOAuthenticationConfig ssoConfig = securityService.getSSOAuthenticationConfig() ;
  String casUrl = "https://" + ssoConfig.getServerUrl() + ":" + ssoConfig.getServerPort() + "/" + ssoConfig.getApplicationPath() + "/";
  String tomcatSSLUrl = "https://" + ssoConfig.getServerUrl() + ":" + ssoConfig.getServerPort() + "/" ;
  
  boolean showForm = false ;
  String ticket = request.getParameter("ticket");
  String user = (String) session.getAttribute("edu.yale.its.tp.cas.client.filter.user");

  if (ticket == null ) {
    System.out.println("redirect to CAS login") ;
    response.sendRedirect(casUrl + "login?service=" + tomcatSSLUrl + portalName + "/cas_login.jsp") ;
  }
  else {
    if (user == null) {
      ProxyTicketValidator pv = new ProxyTicketValidator();
      pv.setCasValidateUrl(casUrl + "proxyValidate");
      pv.setService(tomcatSSLUrl + portalName + "/cas_login.jsp");
      pv.setServiceTicket(ticket);
      pv.setProxyCallbackUrl(tomcatSSLUrl + portalName + "/CasProxyServlet");
      pv.validate();
      if(pv.isAuthenticationSuccesful()) {
          System.out.println("CAS Authentication Successful") ;
          user = pv.getUser() ;
          String loginAction = service  + user ;
          String pgtIou = pv.getPgtIou() ;
					session.setAttribute(Output.LOGIN, user);
					session.setAttribute(Output.PASSWORD, pgtIou);
          response.sendRedirect(loginAction) ;
      }
      else {
        response.sendRedirect(casUrl + "logout?service=" + tomcatSSLUrl + portalName + "/cas_login.jsp") ;
      }
    }
    else response.sendRedirect(casUrl + "login?service=" + tomcatSSLUrl + portalName + "/cas_login.jsp") ;
  }
%>
