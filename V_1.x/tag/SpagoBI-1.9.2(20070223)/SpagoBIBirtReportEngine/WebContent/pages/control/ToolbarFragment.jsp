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
				 org.eclipse.birt.report.viewer.resource.Resources,
				 org.eclipse.birt.report.viewer.utilities.ParameterAccessor" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.viewer.aggregation.Fragment" scope="request" />

<%
	String pdfUrl = request.getContextPath( ) + "/run?"
		+ ParameterAccessor.getEncodedQueryString( request, ParameterAccessor.PARAM_FORMAT,	ParameterAccessor.PARAM_FORMAT_PDF );
%>

<%-----------------------------------------------------------------------------
	Toolbar fragment
-----------------------------------------------------------------------------%>
<TR HEIGHT="20px">
	<TD COLSPAN='2'>
		<DIV ID="toolbar">
			<TABLE CELLSPACING="1px" CELLPADDING="1px" WIDTH="100%" CLASS="birtviewer_toolbar">
				<TR><TD></TD></TR>
				<TR>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <IMG NAME='toc' SRC="images/iv/Toc.gif"
					   		TITLE="<%= Resources.getString( "birt.viewer.toolbar.toc" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <IMG NAME='parameter' SRC="images/iv/ChangeParameter.gif"
					   		TITLE="<%= Resources.getString( "birt.viewer.toolbar.parameter" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <IMG NAME='export' SRC="images/iv/ExportData.gif"
					   		TITLE="<%= Resources.getString( "birt.viewer.toolbar.export" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD ALIGN='right'>
					</TD>
				</TR>
			</TABLE>
		</DIV>
	</TD>
</TR>
