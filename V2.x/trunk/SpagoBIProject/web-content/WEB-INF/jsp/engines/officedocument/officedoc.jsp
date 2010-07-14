<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
--%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
<%@page import="it.eng.spagobi.services.common.SsoServiceInterface"%>

<% 
SourceBean sbModuleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
ExecutionInstance instanceO = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
String execContext = instanceO.getExecutionModality();

Integer executionAuditId_office = null;

if (execContext == null || !execContext.equalsIgnoreCase(SpagoBIConstants.DOCUMENT_COMPOSITION)) {
	%>
	<%@ include file="/WEB-INF/jsp/analiticalmodel/execution/header.jsp"%>
	<%
	executionAuditId_office = executionAuditId;
} else {
	ExecutionInstance instance = contextManager.getExecutionInstance(ExecutionInstance.class.getName());
	AuditManager auditManager = AuditManager.getInstance();
	executionAuditId_office = auditManager.insertAudit(instance.getBIObject(), null, userProfile, instance.getExecutionRole(), instance.getExecutionModality());
}

// identity string for object of the page
String strUuid = instanceO.getExecutionId();
//SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ExecuteBIObjectModule");
BIObject biObj = instanceO.getBIObject();

// get the url for document retrieval
String officeDocUrl = GeneralUtilities.getSpagoBIProfileBaseUrl(userUniqueIdentifier);
officeDocUrl += "&ACTION_NAME=GET_OFFICE_DOC&documentId=" + biObj.getId().toString() + "&" + LightNavigationManager.LIGHT_NAVIGATOR_DISABLED + "=TRUE";
// adding parameters for AUDIT updating
if (executionAuditId_office != null) {
	officeDocUrl += "&" + AuditManager.AUDIT_ID + "=" + executionAuditId_office.toString();
}

//tries to get the heigh of the output area
String heightArea = ChannelUtilities.getPreferenceValue(aRequestContainer, "HEIGHT_AREA", "");
String heightStr = "";
if (heightArea == null || heightArea.trim().equals("")) {
%>
	<script>
		pos<%=strUuid%> = null; 
	
		function adaptSize<%=strUuid%>Funct() {
		
			if (window != parent) {
				// case when document is executed inside main iframe in SpagoBI Web application
				heightMainIFrame = 0;
				// calculates the iframe height
				if(isIE5()) { heightMainIFrame = document.body.clientHeight; }
				else if(isIE6()) { heightMainIFrame = document.body.clientHeight; }
				else if(isIE7()) { heightMainIFrame = document.body.clientHeight; }
				else if(isMoz()) { heightMainIFrame = innerHeight; }
					// this else in case of other browser or a newer version of IE
				else { heightMainIFrame = document.body.clientHeight; }

				// minus a fixed size (header height)
				//heightExecIFrame = heightMainIFrame - 70;
				heightExecIFrame = heightMainIFrame ;
				iframeEl = document.getElementById('iframeexec<%=strUuid%>');
				iframeEl.style.height = heightExecIFrame + 'px';
				return;
			}
			
			// calculate height of the visible area
			heightVisArea = 0;

			if(isIE5()) { heightVisArea = top.document.body.clientHeight; }
			else if(isIE6()) { heightVisArea = top.document.body.clientHeight; }
			else if(isIE7()) { heightVisArea = top.document.documentElement.clientHeight }
			else if(isMoz()) { heightVisArea = top.innerHeight; }
			else { heightVisArea = top.document.documentElement.clientHeight }
	
			// get the frame div object
			diviframeobj = document.getElementById('divIframe<%=strUuid%>');
			// find the frame div position
			pos<%=strUuid%> = findPos(diviframeobj);	
						
			// calculate space below position frame div
			spaceBelowPos = heightVisArea - pos<%=strUuid%>[1];
			// set height to the frame
			iframeEl = document.getElementById('iframeexec<%=strUuid%>');
			iframeEl.style.height = spaceBelowPos + 'px';
	
	    	// to give time to the browser to update the dom (dimension of the iframe)
		  	setTimeout("adaptSize<%=strUuid%>_2Part()", 250);
		}
	
	  	function adaptSize<%=strUuid%>_2Part() {
        
        	// calculate height of the win area and height footer
			heightWinArea = 0;
  			heightFooter = 0;
  			if(isIE5()) {
  				heightWinArea = document.body.scrollHeight;
  				heightFooter = heightWinArea - heightVisArea;
  			}
  			if(isIE6()) {
  				heightWinArea = document.body.scrollHeight;
  				heightFooter = heightWinArea - heightVisArea;
  			}
  			if(isIE7()) {
  				heightWinArea = document.body.offsetHeight;
  				heightFooter = heightWinArea - heightVisArea;
  			}
  			if(isMoz()) {
  				heightWinArea = document.body.offsetHeight;
  				heightFooter = (heightWinArea - heightVisArea);
  			}	 
  	
  			// calculate height of the frame
  			heightFrame = heightVisArea - pos<%=strUuid%>[1] - heightFooter;
  			iframeEl = document.getElementById('iframeexec<%=strUuid%>');
  			if (heightFrame <= 0) heightFrame = 600;
  			iframeEl.style.height = heightFrame + 'px';
		}

		try {
			SbiJsInitializer.adaptSize<%=strUuid%> = adaptSize<%=strUuid%>Funct;
	    } catch (err) {
			alert('Cannot resize the document view area');
		}
		
		try {
			window.onload = SbiJsInitializer.initialize;
		} catch (err) {
			alert('Cannot execute javascript initialize functions');
		}
		
	</script>
<%
} else {
	heightStr = "height:"+heightArea+"px;";
}
%>

<%-- Start execution iframe --%>
<div id="divIframe<%= strUuid %>" style="width:100%;overflow=auto;border: 0;display:inline;<%= heightStr %>">
	<iframe id="iframeexec<%= strUuid %>" name="iframeexec<%= strUuid %>" src="<%= officeDocUrl %>" style="width:100%;height:300px;z-index:0;" frameborder="0" >
	</iframe>
</div>    
<%-- End execution iframe --%>

<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>