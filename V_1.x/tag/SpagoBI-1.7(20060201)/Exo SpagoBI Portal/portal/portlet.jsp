<%
/**
 * Copyright 2001-2003 The EXO Development Team All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Created by the Exo Development team.
 * @author: Tuan Nguyen
 * @version: $Id: portlet.jsp,v 1.1.1.1 2004/07/24 21:11:20 benjmestrallet Exp $
 * @email: tuan08@yahoo.com
 **/
%>
<%@ page contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="exo" prefix="exo" %>

<html>
<head id="head">
  <meta name="keywords" lang="en" content="portlet, portal, jsr-168, jsr 168, websphere api, jetspeed, jakarta  vietnam, vietnamese, jboss, velocity, JSF, java server face">
  <meta name="keywords" lang="fr" content="portlet, portal, jsr-168, jsr 168, websphere api, jetspeed, jakarta  vietnam, vietnamese, jboss, velocity, JSF, java server face">
  <!--
  <meta http-equiv="pragma" content="no-cache"/>
  -->
  <link rel="stylesheet" type='text/css' href="/portal/skin/default-skin.css">
</head>

  <body width="100%" height="100%">
    <f:view>
      <exo:portlet id="portlet-page"/>
    </f:view>
    <div align='center'><a href="/portal/bookmark.jsp">Go to the Bookmark<a/></div>
  </body>  
</html>
