<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.exoplatform.services.portletcontainer.pci.Output"%>
<%@ page language="java" %>
<%@ page contentType="text/html" %>
<%
  String userName = (String) request.getSession().getAttribute(Output.LOGIN) ;
  request.getSession().removeAttribute(Output.LOGIN);
  String password = (String) request.getSession().getAttribute(Output.PASSWORD) ;
  request.getSession().removeAttribute(Output.PASSWORD);    
  boolean showForm = false ;
  if (userName == null || userName.length() == 0 ) showForm = true ;
  if (password == null || password.length() == 0 ) showForm  = true ;
  String contextPath  =  request.getContextPath() ;
  String loginAction = contextPath  + "/j_security_check" ; 
  String portalName = contextPath.substring(1, contextPath.length()) ;

  if(!showForm) {
    password = password + "@" + portalName ;
    password = URLEncoder.encode(password) ;
    response.sendRedirect(loginAction + "?j_username=" + userName + "&j_password=" + password ) ;
  } else { 
%>
  <form name="loginForm" action="<%=loginAction%>">
    <input name="j_username" value="" />
    <input type="password" name="j_password" value="" />
    <a href="javascript:login();">Login</a>
  </form>
  <script type='text/javascript'>
    function login() {
      document.loginForm.elements['j_password'].value = 
        document.loginForm.elements['j_password'].value + "@<%=portalName%>"  ;
      document.loginForm.submit();
    }
  </script>
<%}%>
