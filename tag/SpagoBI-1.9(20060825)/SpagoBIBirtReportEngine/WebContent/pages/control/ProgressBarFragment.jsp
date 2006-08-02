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
	Progress bar fragment
-----------------------------------------------------------------------------%>
<DIV ID="progressBar" STYLE="display:none;position:absolute;z-index:310">
	<TABLE WIDTH="250px" CLASS="birtviewer_progressbar" CELLSPACING="10px">
		<TR>
			<TD ALIGN="center">
				<B><%= Resources.getString( "birt.viewer.progressbar.prompt" )%></B>
			</TD>
		</TR>
		<TR>
			<TD ALIGN="center">
				<IMG SRC="images/iv/Loading.gif" />
			</TD>
		</TR>
	</TABLE>
</DIV>