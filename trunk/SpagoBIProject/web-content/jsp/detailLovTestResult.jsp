<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
			it.eng.spagobi.constants.SpagoBIConstants,
			it.eng.spagobi.bo.ModalitiesValue,
			it.eng.spagobi.bo.lov.ScriptDetail,
			it.eng.spagobi.bo.lov.JavaClassDetail,
			it.eng.spagobi.bo.lov.FixedListDetail,
			it.eng.spagobi.bo.ParameterUse,
			it.eng.spagobi.bo.dao.DAOFactory,
			it.eng.spago.navigation.LightNavigationManager,
			java.util.List,
			java.util.ArrayList,
			java.util.Iterator"%>
<%@page import="it.eng.spagobi.bo.lov.LovDetailFactory"%>
<%@page import="it.eng.spagobi.bo.lov.ILovDetail"%>
<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.managers.LovManager"%>
<%@page import="it.eng.spagobi.utilities.PortletUtilities"%>

<%

	SourceBean detailMR = (SourceBean) aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
	SourceBean listLovMR = (SourceBean) aServiceResponse.getAttribute("ListTestLovModule"); 

	String lovProviderModified = (String)aSessionContainer.getAttribute(SpagoBIConstants.LOV_MODIFIED);
	if (lovProviderModified == null) 
		lovProviderModified = "false";
	
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
	if (!lovProviderModified.trim().equals(""))
		backUrl.setParameter("lovProviderModified", lovProviderModified);
  	
  	ModalitiesValue modVal = (ModalitiesValue) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
  	String lovProv = modVal.getLovProvider();
  	ILovDetail lovDet = LovDetailFactory.getLovFromXML(lovProv);
  	
%>


 

<!--  SCRIPTS  -->





<script type="text/javascript">

<%
	// get the labels of all documents related to the lov
	List docLabels = LovManager.getLabelsOfDocumentsWhichUseLov(modVal);
	String confirmMessage = null;
	boolean askConfirm = false;
	if(docLabels.size() > 0) {
		askConfirm = true;
		String documentsStr = docLabels.toString();
		confirmMessage += PortletUtilities.getMessage("SBIDev.predLov.savePreamble", "messages");
		confirmMessage += " ";
		confirmMessage += documentsStr;
		confirmMessage += ". ";
		confirmMessage += "\\n\\n";
		confirmMessage += PortletUtilities.getMessage("SBIDev.predLov.saveConfirm", "messages");
	}	
	
%>

function askForConfirmIfNecessary() {
<%
	if(askConfirm && lovProviderModified.equalsIgnoreCase("true")) {
		String documentsStr = docLabels.toString();
%>
		if (confirm('<spagobi:message key = "SBIDev.predLov.savePreamble" />' + ' ' + '<%=documentsStr%>' + '. ' + '<spagobi:message key = "SBIDev.predLov.saveConfirm" />')) {
			document.getElementById('formTest').submit();
		}
<%
	} else {
%>
		document.getElementById('formTest').submit();
<%
	}
%>
}
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





<!-- TITLE -->

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.predLov.testPageTitle" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href= 'javascript:askForConfirmIfNecessary();' >
				<img class='header-button-image-portlet-section'
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' 
					title='<spagobi:message key = "SBIDev.predLov.saveButt" />'  
					alt='<spagobi:message key = "SBIDev.predLov.saveButt" />' 
				/>
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href="<%=backUrl.toString()%>"> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.Funct.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.Funct.backButt" />' />
			</a>
		</td>
	</tr>
</table>




<form id="formTest" method="post" action="<%=saveUrl.toString()%>" >

<!-- BODY -->


<div class='div_background_no_img' >


   <!-- ERROR TAG --> 
	<spagobi:error/>



<%
	String errorMessage = (String) listLovMR.getAttribute("errorMessage");	
	String stack = (String) listLovMR.getAttribute("stacktrace");
	if (errorMessage != null) {				  
%>
		<br/>
		<div style="left:10%;width:80%" class='portlet-form-field-label' >
			<spagobi:message key = "SBIDev.predLov.testExecNotCorrect" />
		</div>	
<%
		if (!errorMessage.trim().equals("")) { 
%>					  
			<br/>
		 	<div style="left:10%;width:80%" class='portlet-form-field-label' >
		 		<spagobi:message key = "SBIDev.predLov.testErrorMessage" />
		 	</div>
			<% if (errorMessage.equalsIgnoreCase("Invalid_XML_Output"))  { %>
				<div style="left:10%;width:70%" class='portlet-section-alternate'>
					<spagobi:message key = "SBIDev.predLov.testScriptInvalidXMLOutput" />
				</div>
			<% } else { %>
				<div style="left:10%;width:70%" class='portlet-section-alternate'>
					<%= errorMessage %>
				</div>
			<% } %>
			<br/>
<% 
		}

		if (stack != null) { 
%>
			<div id='errorDescriptionJS' style='display:inline;'>
			  	<br/>
			  	<div style="left:10%;width:80%;display:inline;" class='portlet-form-field-label' id='showStacktraceDiv'>
			  		<spagobi:message key = "SBIDev.predLov.testErrorShowStacktrace1" />
			  		<a href='javascript:showStacktrace()'>
			 	 		<spagobi:message key = "SBIDev.predLov.testErrorShowStacktrace2" />
					</a>
			 		.
			 	</div>
			    <div style="left:10%;width:80%;display:none;" class='portlet-form-field-label' id='hideStacktraceDiv'>
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
<%
		}		
		String result = (String) listLovMR.getAttribute("result");
		if(result != null) { 				  
	  		result = result.replaceAll(">", "&gt;");
	  		result = result.replaceAll("<", "&lt;");
	  		result = result.replaceAll("\"", "&quot;");					  
%>			  
			<div width="100%">
				<br/>
				<div style="position:relative;left:10%;width:80%" class='portlet-form-field-label' >
					<spagobi:message key = "SBIDev.predLov.testScriptNonCorrectResult" />
				</div>
				<br/>	
				<div style="position:relative;left:10%;width:70%" class='portlet-section-alternate'>
					<%= result %>
				</div>
			</div>
			<br/>					  
<% 
		} 
	}else {
%>
		
		<div width="100%">
			<spagobi:LovColumnsSelector moduleName="ListTestLovModule" 
			                            visibleColumns="<%=GeneralUtilities.fromListToString(lovDet.getVisibleColumnNames(), ",")%>" 
			                            valueColumn="<%=lovDet.getValueColumnName()%>" 
			                            descriptionColumn="<%=lovDet.getDescriptionColumnName()%>" 
			                            invisibleColumns="<%=GeneralUtilities.fromListToString(lovDet.getInvisibleColumnNames(), ",")%>" />
		</div>
		
		<br/>
		
		<div width="100%">
			<spagobi:list moduleName="ListTestLovModule"/>
		</div>
<%
	}
%>
				 

</div>

				
</form>


