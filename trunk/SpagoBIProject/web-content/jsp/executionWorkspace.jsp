<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>

<%@page import="it.eng.spago.security.IEngUserProfile"%>
<div class="div_no_background">

	<div style="width: 100%">
		<spagobi:treeObjects moduleName="ExecutionWorkspaceModule" attributeToRender="FIRST_LEVEL_FOLDERS"
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.TitleBarHtmlGenerator" />
	</div>

	<div style="width: 30%;float: left;clear: left;">
		<spagobi:treeObjects moduleName="ExecutionWorkspaceModule" attributeToRender="SUB_TREE"
			htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.ObjectsMenuHtmlGenerator"/>
	</div>

	<div style="width: 30%;float: left;">
		<%
	    // identity string for object of the page
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    String requestIdentity = "request" + uuid.toString();
	    // get spagobi url
	    String spagobiurl = GeneralUtilities.getSpagoBiContextAddress();
	    if (!spagobiurl.endsWith("/")) spagobiurl += "/";
	    spagobiurl += "servlet/AdapterHTTP";
	    // get module response
	    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ExecutionWorkspaceModule");
		// get the BiObject label from the response
	    String objLabel = (String) moduleResponse.getAttribute(ObjectsTreeConstants.OBJECT_LABEL);
	   	// get the user profile from session
		SessionContainer permSession = aSessionContainer.getPermanentContainer();
		IEngUserProfile userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String username = userProfile.getUserUniqueIdentifier().toString();
		if (objLabel == null) {
			%>
			Select a document
			<%
		} else {
			%>
			<center>
				<div id="divIframe<%=requestIdentity%>" style="width:700px;overflow=auto;">
					<iframe id="iframeexec<%=requestIdentity%>"
							name="iframeexec<%=requestIdentity%>"
				            src=""
				            style="width:700px;height:300px"
				            frameborder="0"></iframe>
	
					<form 	name="formexecution<%=requestIdentity%>"
							id='formexecution<%=requestIdentity%>' method="post"
							action="<%=spagobiurl%>"
							target='iframeexec<%=requestIdentity%>'>
	
						<input type="hidden" name="NEW_SESSION" value="TRUE" />
						<input type="hidden" name="PAGE" value="DirectExecutionPage" />
				        <input type="hidden" name="USERNAME" value="<%=username%>" />
				        <input type="hidden" name="DOCUMENT_LABEL" value="<%=objLabel%>" />
	
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
			</center>
			<%
		}
		%>
	</div>
</div>	