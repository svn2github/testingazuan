<!--

    Copyright 2004 Engineering Ingegneria Informatica S.p.A. & Sinapsi S.p.A.

    This file is part of Spago.

    Spago is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    any later version.

    Spago is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Spago; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

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

