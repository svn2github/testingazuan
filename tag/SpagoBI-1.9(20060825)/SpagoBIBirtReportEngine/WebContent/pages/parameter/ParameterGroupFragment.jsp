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
 				 org.eclipse.birt.report.viewer.utilities.ParameterAccessor,
 				 org.eclipse.birt.report.viewer.bean.ParameterGroupBean,
				 org.eclipse.birt.report.viewer.bean.ViewerAttributeBean,
 				 org.eclipse.birt.report.viewer.aggregation.Fragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragments" type="java.util.Collection" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.viewer.bean.ViewerAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Content fragment
-----------------------------------------------------------------------------%>
<%
	ParameterGroupBean parameterGroupBean = ( ParameterGroupBean ) attributeBean.getParameterBean( );
%>
<TR><TD HEIGHT="16px" COLSPAN="2"></TD></TR>
<TR>
	<TD NOWRAP>
		<IMG SRC="images/iv/parameter_group.gif" ALT="<%= parameterGroupBean.getDisplayName( ) %>" />
	</TD>
	<TD NOWRAP>
		<B><%= parameterGroupBean.getDisplayName( ) %></B>
	</TD>
</TR>
<TR>
	<TD NOWRAP>
	</TD>
	<TD NOWRAP>
		<TABLE CLASS="birtviewer_parameter_dialog_Label">
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
		%>
		</TABLE>
	</TD>
</TR>