<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<% String contextName = ChannelUtilities.getSpagoBIContextName(request);
    String redirectURL = contextName+"/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE&MESSAGE=START_LOGIN";
    response.sendRedirect(redirectURL);
%>
<HTML>
<HEAD>
<TITLE>Redirect...</TITLE> 

</HEAD>
<BODY>
Redirect in corso...
</BODY>