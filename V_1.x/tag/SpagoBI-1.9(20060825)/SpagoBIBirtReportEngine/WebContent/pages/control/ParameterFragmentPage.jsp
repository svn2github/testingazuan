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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Parameter Selection Page</title>

<style type="text/css">

body
{
	font: 11px/16px verdana, tahoma, arial, sans-serif;
}

select
{
	font: 11px/16px verdana, tahoma, arial, sans-serif;
}

.baseBackground
{
	background-color: #ebeae6;
	padding-left: 10px;
}

.dropDown
{
	/*padding-left: 10px;*/
	
}

.labelStyle
{
	padding-right: 4px;
}

.selectionArea
{
	position: relative;
	/*width: 800px;*/
	height: 450px;
	background-color: #ffffff;
	border-style: inset;
	overflow: auto;	
}

.thumbnail
{
	/*border: solid;*/
	position: absolute;
	border-style: solid;
	border-width: 1px;
	border-color: #ffffff;
	background-color: #ffffff;
	text-align: center;
	align: center;
	padding: 6px;
}

.lowerRule
{
	position: relative;
	top: 16px;
	height: 3px;
}

.buttons
{
	position: relative;
	top: 20px;
	align: center;
	text-align: center;
}

.button
{
	background-color: #ffffff;  
	border-size: 2px; 
	border-style: inset;
	border-color: #ababa8;
	padding-left: 8px;
	padding-right: 8px;
	padding-top: 2px;
	padding-bottom: 2px;
	font: 11px/16px verdana, tahoma, arial, sans-serif;
}


</style>
</head>
<body class="baseBackground">
<%-----------------------------------------------------------------------------
	Content fragment
-----------------------------------------------------------------------------%>
<!-- parameter dialog -->
<div id="parameterDialog">
	<TABLE	CELLSPACING="0" CELLPADDING="0"	style="width:500px">
		<tr>
			<td>
				<TABLE CELLSPACING="0" CELLPADDING="0" style='width:100%;font-size:8pt'>
					<tr style='width:100%;height:5px'>
						<td colspan=3></td>
					</tr>
					<tr style='width:100%'>
						<td style='width:5px'></td>
						<td>
							<div style='height:100%;overflow:auto'>
								<TABLE CELLSPACING="2" CELLPADDING="2"
									style='background-color:#dbe4ee;
										   border-width:1px;
										   border-style:inset;
										   width:100%;
										   height:100%;
										   font-size:8pt'>
									<tr style='height:5px'><td></td></tr>
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
									<tr style='height:5px'><td></td></tr>
								</table>
							</div>
						</td>
						<td style='width:5px'></td>
					</tr>
				</TABLE>
			</td>
		</tr>
	</TABLE>
	<div><hr class="lowerRule"></div>
	<div class="buttons">
	<button id="ok" name="ok" value="ok" type="push" class="button">OK</button>
	<button id="cancel" name="cancel" value="cancel" type="push" class="button" style="position: relative; left: 4px">Cancel</button>
</div>
</div>
</body>
</html>