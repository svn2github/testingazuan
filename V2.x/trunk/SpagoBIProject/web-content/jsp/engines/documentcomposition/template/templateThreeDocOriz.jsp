<!--
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
--> 
<%@ include file="/jsp/commons/portlet_base.jsp"%>
<%@ page import="java.util.Map,
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="it.eng.spagobi.engines.documentcomposition.SpagoBIDocumentCompositionInternalEngine"%>
 
<%! private static transient Logger logger=Logger.getLogger(SpagoBIDocumentCompositionInternalEngine.class);%>
	
<%
	logger.debug("IN");
   	
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
    String title = (String)moduleResponse.getAttribute("title");
    
	//gets urls & co of documents
	HashMap lstUrl = (HashMap)aSessionContainer.getAttribute("docUrls");
	HashMap lstStyle = (HashMap)aSessionContainer.getAttribute("docStyle");
	HashMap lstUrlParams = (HashMap)aSessionContainer.getAttribute("docUrlParams");
	HashMap lstDocLinked = (HashMap)aSessionContainer.getAttribute("docLinked");
	HashMap lstFieldLinked = (HashMap)aSessionContainer.getAttribute("fieldLinked");
	
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
    <script type="text/javascript" src="/SpagoBI/js/documentcomposition/documentcomposition.js"></script>
 	<script type="text/javascript" src="/SpagoBI/js/extjs/ext-base.js"></script>
    <script type="text/javascript" src="/SpagoBI/js/extjs/ext-all-debug.js"></script>
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
	String styleDoc = (String)lstStyle.get("STYLE_DOC_"+i);
	String totalSbiDocLabel = (String)lstUrlParams.get("SBI_DOC_LABEL_"+(i));
	String labelDoc = totalSbiDocLabel.substring(totalSbiDocLabel.indexOf("|")+1);
%>

<div id="divIframe_<%=labelDoc%>" style="<%=styleDoc%>" >
</div> 

<%} %>
<br>

<script>
//Create associative arrays with information about configuration (dependencies, ...)   
	setUrlIframe('<%=urlIframe%>');
	//prepare associative array with url and labels values. ACTHUNG: Every array is indipendent!!!!
	var arUrl = new Object();
	var arLinkedDocs  = new Object();
	var arLinkedFields  = new Object();
 	<% //loop on documents
 	for (int i = 0; i < lstUrl.size(); i++){
 		String mainLabel = (String)lstDocLinked.get("MAIN_DOC_LABEL_"+(i+1));
 		String totalSbiDocLabel = (String)lstUrlParams.get("SBI_DOC_LABEL_"+(i+1));
 		String labelDoc = totalSbiDocLabel.substring(totalSbiDocLabel.indexOf("|")+1);
 	%>
 		arUrl['<%=totalSbiDocLabel%>'] = ['<%=(String)lstUrl.get("URL_DOC_"+(i+1))%>'];

 	<%	//loop on document linked 
 		for (int j=0; j<lstDocLinked.size(); j++){ 
 			
 			if (mainLabel != null && mainLabel.equalsIgnoreCase(labelDoc)){

		 		String fieldMaster =  (String)lstFieldLinked.get("SBI_LABEL_PAR_MASTER_"+i+"_"+(j+1));
				if (fieldMaster != null && !fieldMaster.equals("")){	
	%>
					arLinkedFields['<%="SBI_LABEL_PAR_MASTER_"+(j+1)%>'] = ['<%=fieldMaster%>'];
	<%    		}
				Integer numDocLinked = (Integer)lstFieldLinked.get("NUM_DOC_FIELD_LINKED_"+(i)+"_"+(j+1));
				if (numDocLinked != null && !numDocLinked.equals("")){
					for (int k=0; k < numDocLinked.intValue(); k++){
						String label = (String)lstDocLinked.get("DOC_LABEL_LINKED_"+(i)+"_"+(j+1)+"_"+k);
		 				String field =  (String)lstFieldLinked.get("DOC_FIELD_LINKED_"+(i)+"_"+(j+1)+"_"+k);
		 				if (field != null && !field.equals("")){
		 	%>
		 					arLinkedDocs['<%=labelDoc +"_"+(j+1)+"_"+(k)%>'] = ['<%=label%>'];
		 					arLinkedFields['<%=label%>'] = ['<%=field%>'];
	 		<%     		}
	 				}		
				}
 			} 
 		}
 	}%>
	setDocs(arUrl);
	setLinkedDocs(arLinkedDocs);
	setLinkedFields(arLinkedFields);
</script> 
   
<%	logger.debug("OUT"); %>

<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK DIV  ********************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->