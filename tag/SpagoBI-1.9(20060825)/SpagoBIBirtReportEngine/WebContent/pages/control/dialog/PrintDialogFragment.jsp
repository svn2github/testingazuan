<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.viewer.aggregation.Fragment,
				 org.eclipse.birt.report.viewer.resource.Resources" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.viewer.aggregation.Fragment" scope="request" />

<%-----------------------------------------------------------------------------
	Print dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD><%= Resources.getString( "birt.viewer.dialog.print.options" ) %></TD>
	</TR>
	<TR>
		<TD>
			<INPUT TYPE='radio' NAME="printoption" VALUE="printoption1" CHECKED>
			<%= Resources.getString( "birt.viewer.dialog.print.printall" ) %><BR>
			<INPUT TYPE='radio' NAME="printoption" VALUE="print2option2">
			<%= Resources.getString( "birt.viewer.dialog.print.printmodified" ) %>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>