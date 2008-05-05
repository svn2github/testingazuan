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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ include file="/jsp/analiticalmodel/execution/header.jsp"%>

<%
// built the url for the content recovering
String url = GeneralUtilities.getSpagoBIProfileBaseUrl(userId) + "&ACTION_NAME=GET_SNAPSHOT_CONTENT&" 
		+ SpagoBIConstants.SNAPSHOT_ID + "=" + snapshot.getId();

//tries to get from the session the heigh of the output area
String heightArea = (String) aSessionContainer.getAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA);
String heightStr = "";
if (heightArea == null || heightArea.trim().equals("")) {
%>
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
		alert(err.description + ' Cannot resize the document view area');
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

<div id="divIframe<%=uuid%>" style="width:98%;float:left;padding-left:2%;<%= heightStr %>">
	<iframe src="<%=url%>"
		style='display:inline;' 
		id='iframeexec<%=uuid%>' 
		name='iframeexec<%=uuid%>'
		frameborder=0  
		width='100%' >
	</iframe> 
</div>

<%@ include file="/jsp/commons/footer.jsp"%>

<%--
<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
                 
<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the recover url
	String url = (String) moduleResponse.getAttribute(SpagoBIConstants.URL);
	url = ChannelUtilities.getSpagoBIContextName(request) + GeneralUtilities.getSpagoAdapterHttpUrl() + url;
	
	// generate an unique identity
	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
    UUID uuid = uuidGen.generateTimeBasedUUID();
    String requestIdentity = "request" + uuid.toString();  
	
	// build the back link
	Map backUrlPars = new HashMap();
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>


<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           &nbsp;&nbsp;&nbsp;<spagobi:message key = "execBIObject.ExecSnapshot" />
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
                      class='header-button-image-portlet-section'
                      src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>' 
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>
   </tr>
</table>


<script>
		function adaptSize() {
			iframe = window.frames['iframeexec<%=requestIdentity%>'];
			navigatorname = navigator.appName;
			height = 0;
			navigatorname = navigatorname.toLowerCase();
			if(navigatorname.indexOf('explorer')) {
				height = iframe.document.body.offsetHeight;
			} else {
				height = iframe.innerHeight;
			}
			iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
			height = height + 100;
			if(height < 300){
				height = 300;
			}
			iframeEl.style.height = height + 100 + 'px';
		}
		
</script>



<div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">
    <%
  		String onloadStr = " onload='adaptSize();' ";
    %> 
            
        <iframe <%=onloadStr%> 
				style='display:inline;' 
				id='iframeexec<%=requestIdentity%>' 
                name='iframeexec<%=requestIdentity%>'  
				src=""
                frameborder=0  
			    width='100%' >
         </iframe>       
                                
         <form name="formexecution<%=requestIdentity%>" id='formexecution<%=requestIdentity%>' method="post" 
               action="<%=url%>" 
               target='iframeexec<%=requestIdentity%>'>
         	<center>
         		<input id="button<%=requestIdentity%>" type="submit" value="View Output"  style='display:inline;'/>
			</center>
		</form>
         
        <script>
              button = document.getElementById('button<%=requestIdentity%>');
              button.style.display='none';
              button.click();               
        </script>
            
</div>
--%>