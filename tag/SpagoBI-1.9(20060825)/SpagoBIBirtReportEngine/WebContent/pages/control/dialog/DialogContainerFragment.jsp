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
				 org.eclipse.birt.report.viewer.bean.ViewerAttributeBean,
				 org.eclipse.birt.report.viewer.resource.Resources" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.viewer.aggregation.Fragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.viewer.bean.ViewerAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Dialog container fragment, shared by all standard dialogs.
-----------------------------------------------------------------------------%>
<DIV ID="<%= fragment.getClientId( ) %>" STYLE="display:none;position:absolute;z-index:220">
	<TABLE CELLSPACING="0" CELLPADDING="0" CLASS="birtviewer_dialog" WIDTH="450px">
		<TR>
			<TD>
				<TABLE CLASS="birtviewer_dialog_caption">
					<TR>
						<TD><B><%= fragment.getClientName( ) %></B></TD>
						<TD ALIGN='right'>
							<IMG NAME='close' SRC="images/iv/Close.gif" CLASS="birtviewer_clickable">	
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
					<TR HEIGHT="5px">
						<TD COLSPAN="3"></TD>
					</TR>
					<TR>
						<TD WIDTH="5px"></TD>
						<TD>
						<%
							if ( fragment != null )
							{
								fragment.callBack( request, response );
							}
						%>
						</TD>
						<TD WIDTH="5px"></TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR HEIGHT="5px"><TD></TD></TR>
		<TR>
			<TD>
				<CENTER>
					<TABLE>
						<TR>
							<TD>
							<%
								if ( "parameter".equalsIgnoreCase( fragment.getClientName( ) ) )
								{
							%>
								<INPUT TYPE='button' NAME='ok'
									VALUE="<%= Resources.getString( "birt.viewer.dialog.run" )%>" CLASS="birtviewer_dialog_button">
							<%
								}
								else
								{
							%>
								<INPUT TYPE='button' NAME='ok'
									VALUE="<%= Resources.getString( "birt.viewer.dialog.ok" )%>" CLASS="birtviewer_dialog_button">
							<%
								}
							%>
								<INPUT TYPE='button' NAME='cancel'
									VALUE="<%= Resources.getString( "birt.viewer.dialog.cancel" )%>" CLASS="birtviewer_dialog_button">
							</TD>
						</TR>
					</TABLE>
				</CENTER>
			</TD>
		</TR>
		<TR HEIGHT="5px"><TD></TD></TR>
	</TABLE>
</DIV>