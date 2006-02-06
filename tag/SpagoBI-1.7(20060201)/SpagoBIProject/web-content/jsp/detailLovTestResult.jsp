<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.bo.ModalitiesValue,
				 it.eng.spagobi.bo.ScriptDetail,
				 it.eng.spago.navigation.LightNavigationManager"%>

<%

	SourceBean detailMR = (SourceBean) aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
	SourceBean listQueryMR = (SourceBean) aServiceResponse.getAttribute("ListTestQueryModule"); 
	SourceBean listScriptMR = (SourceBean) aServiceResponse.getAttribute("ListTestScriptModule"); 

	String modality = null;
	if (detailMR != null) modality = (String) detailMR.getAttribute("modality");
	if (modality == null) modality = (String) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
		
  	String messagedet = "";
  	if (modality.equals(SpagoBIConstants.DETAIL_INS))
		messagedet = SpagoBIConstants.DETAIL_INS;
	else messagedet = SpagoBIConstants.DETAIL_MOD;
		
  	PortletURL saveUrl = renderResponse.createActionURL();
  	saveUrl.setParameter("PAGE", "DetailModalitiesValuePage");
  	saveUrl.setParameter(SpagoBIConstants.MESSAGEDET, messagedet);
  	saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	saveUrl.setParameter("RETURN_FROM_TEST_MSG","SAVE");
	
  	PortletURL backUrl = renderResponse.createActionURL();
  	backUrl.setParameter("PAGE", "DetailModalitiesValuePage");
  	backUrl.setParameter(SpagoBIConstants.MESSAGEDET, messagedet);
  	backUrl.setParameter("modality", modality);
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	backUrl.setParameter("RETURN_FROM_TEST_MSG", "DO_NOT_SAVE");
  	
  	ModalitiesValue modVal = (ModalitiesValue) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
  	
%>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.predLov.testPageTitle" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>			
			<a href="<%=saveUrl.toString()%>"> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBISet.Funct.saveButt" />' /> 
			</a>	
		</td>
		<td class='header-button-column-portlet-section'>
			<a href="<%=backUrl.toString()%>"> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.Funct.backButt" />' />
			</a>
		</td>
	</tr>
</table>

<% 	if (modVal.getITypeCd().equalsIgnoreCase("SCRIPT")) {
	
		String lovProvider = modVal.getLovProvider();
		ScriptDetail scriptDetail = ScriptDetail.fromXML(lovProvider);
		
		if (scriptDetail.isSingleValue()) {
			
			String result = (String) detailMR.getAttribute("result");
	  		String stack = (String) detailMR.getAttribute("stacktrace");
		
			if (result != null) { 
			
				  result = result.replaceAll(">", "&gt;");
				  result = result.replaceAll("<", "&lt;");
				  result = result.replaceAll("\"", "&quot;");
			
			%>
	    
	   	 		<div width="100%">
					<br/>
					<div style="position:relative;left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.predLov.testExecCorrect" /></div>
					<br/>	
					<div style="position:relative;left:10%;width:70%" class='portlet-section-alternate'><%= result %></div>
	 			</div>
				<br/>
			<% } else { %>
		
		 	 	<br/>
		 	 	<div style="left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.predLov.testExecNotCorrect" /></div>
			 	<br/>	
			 	<div style="left:10%;width:70%" class='portlet-section-alternate'><%= stack %></div>
		
			<% } 
			
		} else {
			
			  String errorMessage = (String) listScriptMR.getAttribute("errorMessage");
			  
			  if (errorMessage != null) { 
				  
				  %>
				  
				  <br/>
				  <div style="left:10%;width:80%" class='portlet-form-field-label' >
				  	<spagobi:message key = "SBIDev.predLov.testExecNotCorrect" />
				  </div>
				  
				  <%
				  
				  if (!errorMessage.trim().equals("")) { %>
					  
					<br/>
				 	<div style="left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.predLov.testErrorMessage" /></div>
					<% if (errorMessage.equalsIgnoreCase("Invalid_XML_Output"))  { %>
					<div style="left:10%;width:70%" class='portlet-section-alternate'><spagobi:message key = "SBIDev.predLov.testScriptInvalidXMLOutput" /></div>
					<% } else { %>
					<div style="left:10%;width:70%" class='portlet-section-alternate'><%= errorMessage %></div>
					<% } %>
					<br/>
				  <% }
					 
			  } else { 
				  
				  String result = (String) listScriptMR.getAttribute("result");
				  
				  if (result != null) { 
				  
					  result = result.replaceAll(">", "&gt;");
					  result = result.replaceAll("<", "&lt;");
					  result = result.replaceAll("\"", "&quot;");
					  
				  %>
					  
				   	<div width="100%">
					<br/>
					<div style="position:relative;left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.predLov.testScriptNonCorrectResult" /></div>
					<br/>	
					<div style="position:relative;left:10%;width:70%" class='portlet-section-alternate'><%= result %></div>
			 		</div>
					<br/>
					  
					  
				  <% } else { %>
				  
				    <div width="100%">
						<spagobi:list moduleName="ListTestScriptModule"/>
				  	</div>
				  	
			  <% }
		  	
			  }
			
			}

		
		} else if (modVal.getITypeCd().equalsIgnoreCase("QUERY")) { 
	  
		  String stack = (String) listQueryMR.getAttribute("stacktrace");
		  String errorMessage = (String) listQueryMR.getAttribute("errorMessage");
		  
		  if (stack != null) { 
			  
			  %>
			  
			  <br/>
			  <div style="left:10%;width:80%" class='portlet-form-field-label' >
			  	<spagobi:message key = "SBIDev.predLov.testExecNotCorrect" />
			  </div>
			  
			  <%
			  
			  if (errorMessage != null && !errorMessage.trim().equals("")) { %>
				  
				 <br/>
			 	 <div style="left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.predLov.testErrorMessage" /></div>
				 <div style="left:10%;width:70%" class='portlet-section-alternate'><%= errorMessage %></div>
				  
			  <% } %>	
			  	
			 	 <div id='errorDescription' style='display:inline;'>
			 	 	<br/>
			 	 	<div style="left:10%;width:80%" class='portlet-form-field-label' >
			 	 		<spagobi:message key = "SBIDev.predLov.testErrorDescription" />
			 	 	</div>
				 	<br/>	
				 	<div style="left:10%;width:70%" class='portlet-section-alternate'><%= stack %></div>
				 </div>
				 
				 <div id='errorDescriptionJS' style='display:none;'>
			 	 	<br/>
			 	 	<div style="left:10%;width:80%display:inline;" class='portlet-form-field-label' id='showStacktraceDiv'>
			 	 		<spagobi:message key = "SBIDev.predLov.testErrorShowStacktrace1" />
			 	 		<a href='javascript:showStacktrace()'>
				 	 		<spagobi:message key = "SBIDev.predLov.testErrorShowStacktrace2" />
			 	 		</a>
			 	 		.
			 	 	</div>
			 	 	<div style="left:10%;width:80%display:none;" class='portlet-form-field-label' id='hideStacktraceDiv'>
			 	 		<spagobi:message key = "SBIDev.predLov.testErrorHideStacktrace1" />
			 	 		<a href='javascript:hideStacktrace()'>
				 	 		<spagobi:message key = "SBIDev.predLov.testErrorHideStacktrace2" />
			 	 		</a>
			 	 		.
			 	 	</div>
				 	<br/>	
				 	<div id='stacktrace' style="left:10%;width:70%;display:none;" class='portlet-section-alternate'>
				 		<%= stack %>
				 	</div>
				 </div>
				 
				<script type="text/javascript">
					document.getElementById('errorDescriptionJS').style.display = 'inline';
					document.getElementById('errorDescription').style.display = 'none';
					document.getElementById("stacktrace").style.display = 'none';
					document.getElementById("showStacktraceDiv").style.display = 'inline';
					document.getElementById("hideStacktraceDiv").style.display = 'none';
				</script>
				
				<script type="text/javascript">
					function showStacktrace(){
						document.getElementById("stacktrace").style.display = 'inline';
						document.getElementById("showStacktraceDiv").style.display = 'none';
						document.getElementById("hideStacktraceDiv").style.display = 'inline';
					}
					
					function hideStacktrace(){
						document.getElementById("stacktrace").style.display = 'none';
						document.getElementById("showStacktraceDiv").style.display = 'inline';
						document.getElementById("hideStacktraceDiv").style.display = 'none';
					}
				</script>
				
			
		  <% } else { %>
		  
			    <div width="100%">
					<spagobi:list moduleName="ListTestQueryModule"/>
			  	</div>
			  	
		  <% }
	  
	} else { %>
	
	<br/>
	<div style="position:relative;left:10%;width:80%" class='portlet-form-field-label' ><spagobi:message key = "SBIDev.predLov.testNotImplemented" /></div>
	<br/>
	
<% } %>

