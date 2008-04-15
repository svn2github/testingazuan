<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ include file="/jsp/analiticalmodel/execution/header.jsp"%>

<%
// tries to get from the session the heigh of the output area
String heightArea = (String) aSessionContainer.getAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA);
String heightStr = "";
if (heightArea == null || heightArea.trim().equals("")) {
%>
	<%-- Script for iframe automatic resize (by mitico Fisca (Luca Fiscato)) --%>
	<script>
		
	function adaptSize<%= uuid %>Funct() {
		// evaluates the iframe current height
		iframeEl = document.getElementById('iframeexec<%= uuid %>');
		offsetHeight = 0;
		clientHeight = 0;
		if(isIE5) {
			offsetHeight = iframeEl.contentWindow.document.body.scrollHeight;
			clientHeight = iframeEl.clientHeight;
		}
		if(isIE6) {
			offsetHeight = iframeEl.contentWindow.document.body.scrollHeight;
			clientHeight = iframeEl.clientHeight;
		}
		if(isIE7) {
			offsetHeight = iframeEl.contentWindow.document.body.scrollHeight;
			clientHeight = iframeEl.clientHeight;
		}
		if(isMoz) {
			offsetHeight = iframeEl.contentWindow.document.body.offsetHeight;
			clientHeight = iframeEl.clientHeight;
		}
		// adjusts current iframe height
		if (offsetHeight != clientHeight + 40) {
			heightFrame = offsetHeight + 40;
			iframeEl.style.height = heightFrame + 'px';
		}
		// saves the current iframe height into a variable
		iframeHeight<%= uuid %> = heightFrame;
	}
	
	try {
		SbiJsInitializer.adaptSize<%=uuid%> = adaptSize<%=uuid%>Funct;
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
<div id="divIframe<%= uuid %>" style="width:100%;overflow=auto;border: 0;display:inline;<%= heightStr %>">
	<iframe id="iframeexec<%= uuid %>" name="iframeexec<%= uuid %>" src="<%= getUrl(obj.getEngine().getUrl(), executionParameters) %>" style="width:100%;height:300px;z-index:0;" frameborder="0" >
	</iframe>
</div>
<%-- End execution iframe --%>

<%-- start drill script --%>
<script>
function execDrill(name, url){
	if (name == null || name == "")
		name = "iframeexec<%= uuid %>";
	var element = document.getElementById(name);
	element.src = url;
	return;
}
</script>
<%-- end drill script --%>

<%@ include file="/jsp/commons/footer.jsp"%>