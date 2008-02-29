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
<%@page import="java.util.HashMap,java.util.Iterator"%>
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
   	// get the user profile from session
	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

    String title = (String)moduleResponse.getAttribute("title");
    String displayTitleBar = (String)moduleResponse.getAttribute("displayTitleBar");
    
	//gets urls & co of documents
	HashMap lstUrl = (HashMap)aSessionContainer.getAttribute("docUrls");
	HashMap lstHeight = (HashMap)aSessionContainer.getAttribute("docHeight");
	HashMap lstWidth = (HashMap)aSessionContainer.getAttribute("docWidth");
	HashMap lstParams = (HashMap)aSessionContainer.getAttribute("docParams");
	String targetUrl = "";
	
	String executionId = (String) aServiceRequest.getAttribute("spagobi_execution_id");
	String flowId = (String) aServiceRequest.getAttribute("spagobi_flow_id");
	    
    
	// build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
    
    String urlIframe = (String)aSessionContainer.getAttribute("urlIframe");
	String execUrlDoc1 = (String)lstUrl.get("URL_DOC_1");
	String execUrlDoc2 = (String)lstUrl.get("URL_DOC_2");
	String execUrlDoc3 = (String)lstUrl.get("URL_DOC_3");
	
	String heightDoc1 = (String)lstHeight.get("HEIGHT_DOC_1");
	String heightDoc2 = (String)lstHeight.get("HEIGHT_DOC_2");
	String heightDoc3 = (String)lstHeight.get("HEIGHT_DOC_3");
	
	String widthDoc1 = (String)lstWidth.get("WIDTH_DOC_1");
	String widthDoc2 = (String)lstWidth.get("WIDTH_DOC_2");
	String widthDoc3 = (String)lstWidth.get("WIDTH_DOC_3");
	  
	logger.debug("urlIframe: " + urlIframe);
    logger.debug("execUrlDoc1: " + execUrlDoc1);
    logger.debug("execUrlDoc2: " + execUrlDoc2);
    logger.debug("execUrlDoc3: " + execUrlDoc3);
    
%> 
<!-- LIBS AJAX-->
 	<script type="text/javascript" src="/SpagoBI/js/ext/ext-base.js"></script>
    <script type="text/javascript" src="/SpagoBI/js/ext/ext-all-debug.js"></script>
    <script type="text/javascript" src="/SpagoBI/js/documentcomposition/tempThreeDocs.js"></script>
<!-- ENDLIBS -->
  
   

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

	function loadPartialIframes(){
		//aggiornare i parametri modificati (come riconoscerli ??), modificare l'url e ricaricare il frame
		//richiamre funzioni della utility
		//SIMULAZIONE: rinfrescare mappa e rpt dettMappa con cod_uu=43
			<!-- generalizzare valore del campo hidden con la label del doc su cui si è cliccato -->
			var docClicked = "mappa";
			var execForm =  document.getElementById("ValuesForm");
			var newUrl = '/SpagoBIJasperReportEngine/SpagoBIRefreshServlet?DOCUMENT_LABEL='+docClicked;
			alert(newUrl);
			execForm.action = newUrl;
			execForm.submit();
			
			/*
			 var executionForm_DOC_1 = document.getElementById('executionForm_DOC_1');
			 var newUrl = '<%=execUrlDoc2%>';
			 executionForm_DOC_1.action = newUrl;
             executionForm_DOC_1.submit();
             */
		}
		
		function execDrill(name, url){
			//test
			name = "iframe_DOC3"
			var element1 = document.getElementById(name);
			url = url.replace("SpagoBIDrillServlet","SpagoBIRefreshServlet");
			element1.src = url;
			
			name = "iframe_DOC2"
			var element2 = document.getElementById(name);
			element2.src = url;
			
			return;
		}

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
    

<div id="divIframe_DOC1" style="width:<%=widthDoc1%>;height:<%=heightDoc1%>,float:left;padding-left:2%;" >
</div> 

<div id="divIframe_DOC2" style="width:<%=widthDoc2%>;height:<%=heightDoc2%>;float:left;padding-left:2%;"> 
</div> 
     
<div id="divIframe_DOC3" style="width:<%=widthDoc3%>;height:<%=heightDoc3%>;float:left;padding-left:2%;">
</div>

<br>

<div class="panelBody">		
	<center><input type='button' name='Refresh' value='Refresh' id='refresh'/></center>	
</div>	
  

<script>
	setUrls('<%=urlIframe%>', '<%=execUrlDoc1%>', '<%=execUrlDoc2%>', '<%=execUrlDoc3%>');
	setDocsName('DOC1', 'DOC2', 'DOC3'); //GENERALIZZARE 
</script> 
       
<%	logger.debug("OUT"); %>

<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK DIV  ********************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->