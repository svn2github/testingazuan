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
	Exception dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD>
		<TABLE CELLSPACING="0" CELLPADDING="0" style="font:verdana;font-size:8pt">
			<TR>
				<TD VALIGN="top"><IMG SRC="images/iv/Error.gif" /></TD>
				<TD WIDTH="20px"></TD>
				<TD>
					<B>
						Exception in:<br>
					</B>
					<SPAN ID='faultcode'></SPAN><br><br>
					<FONT STYLE='color:red'><SPAN ID='faultstring'></SPAN></FONT>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>