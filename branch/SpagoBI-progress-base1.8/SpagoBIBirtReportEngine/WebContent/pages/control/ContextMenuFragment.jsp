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
<%@ page import="org.eclipse.birt.report.viewer.aggregation.Fragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.viewer.aggregation.Fragment" scope="request" />

<%-----------------------------------------------------------------------------
	Context menu fragment
-----------------------------------------------------------------------------%>
<DIV ID="contextMenu" STYLE="display:none;position:absolute;z-index:210">
	<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" CLASS="birtviewer_contextmenu">
		<TR>
			<TD WIDTH="2px"></TD>
			<TD></TD>
			<TD WIDTH="2px"></TD>
		</TR>
	</TABLE>	   
</DIV>
