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
<%@ page import="java.util.Iterator,
				 java.util.Collection,
 				 org.eclipse.birt.report.viewer.resource.Resources,
 				 org.eclipse.birt.report.viewer.aggregation.Fragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragments" type="java.util.Collection" scope="request" />

<%-----------------------------------------------------------------------------
	Parameter dialog fragment
-----------------------------------------------------------------------------%>
<DIV CLASS="birtviewer_parameter_dialog">
	<TABLE CELLSPACING="2" CELLPADDING="2" ID="parameter_table" CLASS="birtviewer_dialog_body">
		<TR VALIGN="top">
			<TD>
				<TABLE STYLE="font-size:8pt">
					<TR HEIGHT="5px"><TD></TD></TR>
					<%
					if ( fragments.size( ) <= 0 )
					{
					%>
						<TR><TD><%= Resources.getString( "birt.viewer.error.noparameter" ) %></TD></TR>
					<%
					}
					else
					{
					%>
						<TR><TD COLSPAN="2"><%= Resources.getString( "birt.viewer.required" ) %></TD></TR>
					<%
						if ( fragments != null )
						{
							Iterator childIterator = fragments.iterator( );
							while ( childIterator.hasNext( ) )
							{
							    Fragment subfragment = ( Fragment ) childIterator.next( );
								if ( subfragment != null )
								{
									subfragment.service( request, response );
								}
							}
						}
					}
					%>
					<TR HEIGHT="5px"><TD></TD></TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>	
</DIV>