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
import="
    java.io.PrintWriter,
    java.io.StringWriter,
    java.lang.Exception"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
isErrorPage="true"
session="true"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>Errore</TITLE>
</HEAD>
<BODY>
<%
if (exception == null)
    exception = (Exception) session.getAttribute("_EXCEPTION_");
StringWriter exStringWriter = new StringWriter();
PrintWriter exPrintWriter = new PrintWriter(exStringWriter);
exception.printStackTrace(exPrintWriter);
String message = exStringWriter.toString();
%>
<TABLE align="center" cols=1 width="90%">
	<TR>
		<TD><BR>
		</TD>
	</TR>
	<TR>
		<TD align="center"><B>Siamo spiacenti, si è verificato un errore
		interno all'applicativo.</B><BR>
		</TD>
	</TR>
	<TR>
		<TD><BR>
		</TD>
	</TR>
	<TR>
		<TD align="center"><TEXTAREA rows="22" name="error_message" cols="80"><%=message%></TEXTAREA>
		</TD>
	</TR>
</TABLE>
</BODY>
</HTML>
