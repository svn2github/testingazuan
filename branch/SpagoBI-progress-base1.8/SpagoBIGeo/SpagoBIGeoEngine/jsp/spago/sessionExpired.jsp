<!--
/**
 *
 *	LICENSE: see COPYING file
 *
**/
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
import="it.eng.spago.error.EMFErrorHandler"
extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPage"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
session="true"
errorPage="/jsp/spago/jspError.jsp"
%>
<%
EMFErrorHandler errorHandler = getErrorHandler(request);
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>Session Expired</TITLE>
</HEAD>
<BODY>
<P align="center"><B><FONT size="4" color="#000080" face="Courier New">Session
Expired</FONT></B></P>
</BODY>
</HTML>

