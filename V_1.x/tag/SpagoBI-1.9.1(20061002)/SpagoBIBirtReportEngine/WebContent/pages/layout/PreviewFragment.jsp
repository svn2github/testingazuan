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
	Viewer root fragment
-----------------------------------------------------------------------------%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
	<HEAD>
		<TITLE><%= attributeBean.getReportTitle( ) %></TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; CHARSET=utf-8">
		<LINK REL="stylesheet" HREF="<%= request.getContextPath() + "/styles/iv/style.css" %>" TYPE="text/css">
		<LINK REL="stylesheet" HREF="<%= request.getContextPath() + "/styles/iv/webreporting.css" %>" MEDIA="screen" TYPE="text/css" />
		
		<script src="ajax/iv/utility/Debug.js" type="text/javascript"></script>
		<script src="ajax/lib/prototype.js" type="text/javascript"></script>
		
		<script src="ajax/iv/utility/Constants.js" type="text/javascript"></script>
		<script src="ajax/iv/utility/BirtUtilities.js" type="text/javascript"></script>
		
		<script src="ajax/iv/core/BirtEventDispatcher.js" type="text/javascript"></script>
		<script src="ajax/iv/core/BirtEvent.js" type="text/javascript"></script>
		
		<script src="ajax/iv/mh/BirtBaseResponseHandler.js" type="text/javascript"></script>
		<script src="ajax/iv/mh/BirtGetUpdatedObjectsResponseHandler.js" type="text/javascript"></script>

		<script src="ajax/iv/ui/app/BirtAppUIComponentBase.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/app/BirtNavigationBar.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/app/BirtProgressBar.js" type="text/javascript"></script>

 		<script src="ajax/iv/ui/report/BirtReportBase.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/report/BirtDocument.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/report/BirtReportTable.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/report/BirtReportChart.js" type="text/javascript"></script>

		<script src="ajax/iv/ui/app/dialog/BirtDialogBase.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/app/dialog/BirtTabedDialogBase.js" type="text/javascript"></script>
		<script src="ajax/iv/ui/app/dialog/BirtExceptionDialog.js" type="text/javascript"></script>
		
		<script src="ajax/iv/utility/BirtPosition.js" type="text/javascript"></script>

		<script src="ajax/iv/core/BirtCommunicationManager.js" type="text/javascript"></script>
		<script src="ajax/iv/core/BirtSoapRequest.js" type="text/javascript"></script>
		<script src="ajax/iv/core/BirtSoapResponse.js" type="text/javascript"></script>
		
	</HEAD>
	
	<BODY CLASS="BirtViewer_Body"  ONLOAD="javascript:init( );" LEFTMARGIN='0px' STYLE='overflow:hidden'>
		<TABLE ID='layout' CELLSPACING='0' CELLPADDING='0' STYLE='width:100%;height:100%'>
		<%
			if ( fragment != null )
			{
				fragment.callBack( request, response );
			}
		%>
		</TABLE>
	</BODY>

	<script type="text/javascript">
	// <![CDATA[
		var hasSVGSupport = false;
		var useVBMethod = false;
		if ( navigator.mimeTypes != null && navigator.mimeTypes.length > 0 )
		{
		    if ( navigator.mimeTypes["image/svg+xml"] != null )
		    {
		        hasSVGSupport = true;
		    }
		}
		else
		{
		    useVBMethod = true;
		}
	// ]]>
	</script>
	
	<script type="text/vbscript">
		On Error Resume Next
		If useVBMethod = true Then
		    hasSVGSupport = IsObject(CreateObject("Adobe.SVGCtl"))
		End If
	</script>

	<script type="text/javascript">
	// <![CDATA[
		var birtReportTable = new BirtReportTable( );
 		var birtReportChart = new BirtReportChart( );
		
		var birtReportDocument = new BirtReportDocument( "Document" );
		var birtNavigationBar = new BirtNavigationBar( 'navigationBar' );
		var birtProgressBar = new BirtProgressBar( 'progressBar' );
		var birtExceptionDialog = new BirtExceptionDialog( 'exceptionDialog' );

		function init()
		{
			birtNavigationBar.__init_page( );
		}
	
	  // ]]>
	</script>
</HTML>