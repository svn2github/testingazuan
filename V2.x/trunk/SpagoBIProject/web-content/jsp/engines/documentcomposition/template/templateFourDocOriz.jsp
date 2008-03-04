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
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.constants.ObjectsTreeConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="it.eng.spagobi.engines.documentcomposition.SpagoBIDocumentCompositionInternalEngine"%>

<%! private static transient Logger logger=Logger.getLogger(SpagoBIDocumentCompositionInternalEngine.class);%>
	
<%
	logger.debug("IN");
	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	UUID uuid = uuidGen.generateTimeBasedUUID();
	String requestIdentity = "request" + uuid.toString(); 
	// control if the portlet act with single object modality.
	// get the modality of the portlet (single object execution, entire tree or filter tree)
	boolean isSingleObjExec = false;
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
	if( (modality!=null) && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY) )
		isSingleObjExec = true;
	
   	
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
	// get the BiObject from the response
    BIObject obj = (BIObject)moduleResponse.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);

    String title = (String)moduleResponse.getAttribute("title");
    String displayTitleBar = (String)moduleResponse.getAttribute("displayTitleBar");
    
	//gets urls & co of documents
	HashMap lstUrl = (HashMap)aSessionContainer.getAttribute("docUrls");
	HashMap lstHeight = (HashMap)aSessionContainer.getAttribute("docHeight");
	HashMap lstWidth = (HashMap)aSessionContainer.getAttribute("docWidth");
	
	String executionId = (String) aServiceRequest.getAttribute("spagobi_execution_id");
	String flowId = (String) aServiceRequest.getAttribute("spagobi_flow_id");
	    
    
	// build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>

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
<!-- **************** START BLOCK IFRAME ***************************** -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->


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

<%
	String execUrlDoc1 = (String)lstUrl.get("URL_DOC_1");
	String execUrlDoc2 = (String)lstUrl.get("URL_DOC_2");
	String execUrlDoc3 = (String)lstUrl.get("URL_DOC_3");
	String execUrlDoc4 = (String)lstUrl.get("URL_DOC_4");
	
	String heightDoc1 = (String)lstHeight.get("HEIGHT_DOC_1");
	String heightDoc2 = (String)lstHeight.get("HEIGHT_DOC_2");
	String heightDoc3 = (String)lstHeight.get("HEIGHT_DOC_3");
	String heightDoc4 = (String)lstHeight.get("HEIGHT_DOC_4");
	
	String widthDoc1 = (String)lstWidth.get("WIDTH_DOC_1");
	String widthDoc2 = (String)lstWidth.get("WIDTH_DOC_2");
	String widthDoc3 = (String)lstWidth.get("WIDTH_DOC_3");
	String widthDoc4 = (String)lstWidth.get("WIDTH_DOC_4");

	

   //URL FOR INTERNAL ENGINE
   //localhost:8080/SpagoBI/servlet/AdapterHTTP?USERNAME=....&PAGE=DirectExecutionPage=&DOCUMENT_LABEL=....&DOCUMENT_PARAMETERS=....
    
    logger.debug("execUrlDoc1: " + execUrlDoc1);
    logger.debug("execUrlDoc2: " + execUrlDoc2);
    logger.debug("execUrlDoc2: " + execUrlDoc3);
    logger.debug("execUrlDoc2: " + execUrlDoc4);
    
    
%> 

<div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">   
         
            <iframe
				   style='display:inline;height:<%=heightDoc1%>;width:<%= widthDoc1%>;' 
				   id='iframeexec_DOC_1_<%=requestIdentity%>' 
                   name='iframeexec_DOC_1_<%=requestIdentity%>'  
                   src="<%=execUrlDoc1%>"
                   frameborder=1 >
         	</iframe>  
</div>

<div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">    
         
            <iframe 
				   style='display:inline;height:<%=heightDoc2%>;width:<%= widthDoc2%>;' 
				   id='iframeexec_DOC_2_<%=requestIdentity%>' 
                   name='iframeexec_DOC_2_%=requestIdentity%>'  
                   src="<%=execUrlDoc2%>"
                   frameborder=1 >
         	</iframe>        
</div>
     
 <div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">    
         
            <iframe 
				   style='display:inline;height:<%=heightDoc3%>;width:<%= widthDoc3%>;' 
				   id='iframeexec_DOC_3_<%=requestIdentity%>' 
                   name='iframeexec_DOC_3_%=requestIdentity%>'  
                   src="<%=execUrlDoc3%>"
                   frameborder=1>
         	</iframe>        
</div>

<div id="divIframe<%=requestIdentity%>" style="width:98%;float:left;padding-left:2%;">    
         
            <iframe
				   style='display:inline;height:<%=heightDoc4%>;width:<%= widthDoc4%>;' 
				   id='iframeexec_DOC_4_<%=requestIdentity%>' 
                   name='iframeexec_DOC_4_%=requestIdentity%>'  
                   src="<%=execUrlDoc4%>"
                   frameborder=1 >
         	</iframe>        
</div>

       
<%	logger.debug("OUT"); %>

<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->