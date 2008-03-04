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
<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ page import="java.util.Map,
                 it.eng.spago.navigation.LightNavigationManager,
                 org.safehaus.uuid.UUIDGenerator,
                 org.safehaus.uuid.UUID" %>
<%@page import="java.util.HashMap,java.util.Iterator, java.util.List"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.constants.ObjectsTreeConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="it.eng.spagobi.engines.documentcomposition.SpagoBIDocumentCompositionInternalEngine"%>


<%! private static transient Logger logger=Logger.getLogger(SpagoBIDocumentCompositionInternalEngine.class);%>
	
<%
	logger.debug("IN");
   	
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the BiObject from the response
    BIObject obj = (BIObject)moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
   	// get the user profile from session
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

    String title = (String)moduleResponse.getAttribute("title");
    String displayTitleBar = (String)moduleResponse.getAttribute("displayTitleBar");
    
	//gets urls & co of documents
	HashMap lstUrl = (HashMap)aSessionContainer.getAttribute("docUrls");
	HashMap lstHeight = (HashMap)aSessionContainer.getAttribute("docHeight");
	HashMap lstWidth = (HashMap)aSessionContainer.getAttribute("docWidth");
	HashMap lstUrlParams = (HashMap)aSessionContainer.getAttribute("docUrlParams");
	HashMap lstDocLinked = (HashMap)aSessionContainer.getAttribute("docLinked");
	String targetUrl = "";
	
	String executionId = (String) aServiceRequest.getAttribute("spagobi_execution_id");
	String flowId = (String) aServiceRequest.getAttribute("spagobi_flow_id");
	 
	    
	// build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);

    String urlIframe = (String)aSessionContainer.getAttribute("urlIframe");
	logger.debug("urlIframe: " + urlIframe);

%> 
<!-- LIBS AJAX-->
 	<script type="text/javascript" src="/SpagoBI/js/ext/ext-base.js"></script>
    <script type="text/javascript" src="/SpagoBI/js/ext/ext-all-debug.js"></script>
  <!--   <script type="text/javascript" src="/SpagoBI/js/documentcomposition/tempThreeDocs.js"></script>-->
    <script type="text/javascript" src="/SpagoBI/js/documentcomposition/documentcomposition.js"></script>
<!-- ENDLIBS -->
  
   

<script>

</script>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;'>
        <div id="navigationBar<%=executionId%>">
           &nbsp;&nbsp;&nbsp;<%=title%>
        </div>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%=backUrl%>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />'
                      class='header-button-image-portlet-section'
                      src='<%= urlBuilder.getResourceLink(request, "/img/back.png")%>'
                      alt='<spagobi:message key = "SBIDev.docConf.execBIObjectParams.backButt" />' />
           </a>
       </td>
   </tr>
</table>
   
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** START BLOCK DIV ******************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<%
for (int i=1; i<=lstUrl.size(); i++){
	String heightDoc = (String)lstHeight.get("HEIGHT_DOC_"+i);
	String widthDoc = (String)lstWidth.get("WIDTH_DOC_"+i);
	String labelDoc = (String)lstUrlParams.get("DOC_LABEL_"+i);
%>

<div id="divIframe_<%=labelDoc%>" style="width:<%=widthDoc%>;height:<%=heightDoc%>,float:left;padding-left:2%;" >
</div> 

<%} %>
<br>
  
<script>
	setUrlIframe('<%=urlIframe%>');
	//prepare associative array with url and labels values
	var arUrl = new Object();
	var arLinkedDocs  = new Object();
	
 	<% for (int i = 1; i <= lstUrl.size(); i++){ %>
 		arUrl['<%=(String)lstUrlParams.get("DOC_LABEL_"+i)%>'] = ['<%=(String)lstUrl.get("URL_DOC_"+i)%>'];
 	<%	List linkedDoc = (List)lstDocLinked.get("DOC_LABEL_LINKED_"+i);
 		for (int j=0; j<linkedDoc.size(); j++){ 
 			if (linkedDoc.get(j) != null && !linkedDoc.get(j).equals(""))	{
 				 String key = lstUrlParams.get("DOC_LABEL_"+i) + "_"+(j+1);
 	%>
 				arLinkedDocs['<%=key%>'] = ['<%=(String)linkedDoc.get(j)%>'];
 	<%     		}			
 		}
 	}%>
	
	
	setDocs(arUrl);
	setLinkedDocs(arLinkedDocs);
</script> 
   
<%	logger.debug("OUT"); %>

<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK DIV  ********************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->