<!--
/**
 *	LICENSE: see COPYING file
**/
-->

<%@ page language="java"
		 import="it.eng.spago.error.*,java.util.*"
		 extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPage"
		 contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"
		 session="true"
		 errorPage="/jsp/error.jsp"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
	EMFErrorHandler errorHandler = getErrorHandler(request);
%>

<HTML>
	<HEAD>
		<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<TITLE>Service Error</TITLE>
	</HEAD>
	<BODY>
		
<%
	Iterator it = errorHandler.getErrors().iterator();
	while(it.hasNext()) {
		EMFInternalError error = (EMFInternalError)it.next();	
		Exception exception = error.getNativeException();
		Throwable targetException = exception;
		while( targetException != null) {	
%>
			<br/>
			<span style="font-size:13pt;">
				Class: <%=targetException.getClass().getName()%>
			</span>
			<br/>
			<span style="font-size:13pt;">
				Message: <%=targetException.getMessage()%>
			</span>
			<br/>
<%
			targetException = targetException.getCause();
		}
%>
		<br/><br/>
		<TEXTAREA rows="22" name="error_message" cols="100"><%=error.toString()%></TEXTAREA>
		<br/><br/><hr/>
<%
	}
%>
	
	</BODY>
</HTML>
