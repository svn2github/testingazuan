<!--
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
-->


<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="java.util.List" %>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
                 
<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the recover url
	String url = (String) moduleResponse.getAttribute(SpagoBIConstants.URL);
	
	// generate an unique identity
	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
    UUID uuid = uuidGen.generateTimeBasedUUID();
    String requestIdentity = "request" + uuid.toString();  
	
	// build the back link
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");

%>




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
           &nbsp;&nbsp;&nbsp;<spagobi:message key = "execBIObject.ExecSnapshot" />
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl.toString() %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' 
                      class='header-button-image-portlet-section'
                      src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
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