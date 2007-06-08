<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="java.io.File"%>
<%@page import="it.eng.spago.configuration.ConfigSingleton"%>



<div class="div_no_background">
	
	<%
	String styleName = ChannelUtilities.getPreferenceValue(aRequestContainer, "STYLE_NAME", "");
	styleName += ".css";
	String styleFilePath = ConfigSingleton.getRootPath() + "/css/guiComponents/" + styleName;
	File styleFile = new File(styleFilePath);
	if (styleFile.exists()) {
		%>
		<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "/css/guiComponents/" + styleName)%>' type='text/css' />
		<div class="executionWorkspace">
		<%
	}
	%>
	<div class='workspaceTopBox' >
		<spagobi:treeObjects moduleName="ExecutionWorkspaceModule" attributeToRender="FIRST_LEVEL_FOLDERS"
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.TitleBarHtmlGenerator" />
	</div>

	<div class='workspaceLeftBox' >
		<spagobi:treeObjects moduleName="ExecutionWorkspaceModule" attributeToRender="SUB_TREE"
			htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.SlideShowMenuHtmlGenerator"/>
	</div>

	<div class='workspaceRightBox' >
		<%
	    // identity string for object of the page
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    String requestIdentity = uuid.toString();
	    requestIdentity = requestIdentity.replaceAll("-", "");
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
		// urls for resources
		String linkSbijs = urlBuilder.getResourceLink(request, "/js/spagobi.js");
		%>
		<SCRIPT language='JavaScript' src='<%=linkSbijs%>'></SCRIPT>
	
		<script>
	
			function adaptSize<%=requestIdentity%>Funct() {

	          navigatorname = navigator.appName;
			  navigatorversion = navigator.appVersion;
	          navigatorname = navigatorname.toLowerCase();
	          isIE = false;
	          isIE5 = false;
	          isIE6 = false;
	          isIE7 = false;
	          isMoz = false;
	          isIE = (navigatorname.indexOf('explorer') != -1);
	          isIE5 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 5') != -1) );
	          isIE6 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 6') != -1) );
	          isIE7 = ( (navigatorname.indexOf('explorer') != -1) && (navigatorversion.indexOf('MSIE 7') != -1) );
	          isMoz = (navigatorname.indexOf('explorer') == -1);
	
			  if (window != top) {
				totalVisArea = 0;
				if(isIE5) { totalVisArea = window.document.body.clientHeight; }
				if(isIE6) { totalVisArea = window.document.body.clientHeight; }
				if(isIE7) { totalVisArea = window.document.body.clientHeight; }
				if(isMoz) { totalVisArea = window.innerHeight; }
				iframeEl = document.getElementById('workspaceiframe<%=requestIdentity%>');
				//iframeEl.scrollbars="no";
				iframeEl.style.height = totalVisArea + 'px';
				//iframeEl.style.height = 2000 + 'px';
				return;
			  }
	
	          // calculate height of the visible area
	          heightVisArea = 0;
	          //if(isIE5) { heightVisArea = window.document.body.clientHeight; }
	          //if(isIE6) { heightVisArea = window.document.body.clientHeight; }
	          //if(isIE7) { heightVisArea = window.document.documentElement.clientHeight }
	          //if(isMoz) { heightVisArea = window.innerHeight; }
	          if(isIE5) { heightVisArea = top.document.body.clientHeight; }
	          if(isIE6) { heightVisArea = top.document.body.clientHeight; }
	          if(isIE7) { heightVisArea = top.document.documentElement.clientHeight }
	          if(isMoz) { heightVisArea = top.innerHeight; }
	
	          // get the frame div object
	          diviframeobj = document.getElementById('divIframe<%=requestIdentity%>');
	          // find the frame div position
	          pos = findPos(diviframeobj);
						
	          // calculate space below position frame div
	          spaceBelowPos = heightVisArea - pos[1];
	          // set height to the frame
				    iframeEl = document.getElementById('workspaceiframe<%=requestIdentity%>');
				    iframeEl.style.height = spaceBelowPos + 'px';
	
	          // calculate height of the win area and height footer
	          heightWinArea = 0;
	          heightFooter = 0;
	          if(isIE5) {
	             //heightWinArea = window.document.body.scrollHeight;
							 heightWinArea = document.body.scrollHeight;
					     heightFooter = heightWinArea - heightVisArea;
	          }
	          if(isIE6) {
	             //heightWinArea = window.document.body.scrollHeight;
							 heightWinArea = document.body.scrollHeight;
					     heightFooter = heightWinArea - heightVisArea;
	          }
	          if(isIE7) {
					     //heightWinArea = window.document.body.offsetHeight;
							 heightWinArea = document.body.offsetHeight;
	             heightFooter = heightWinArea - heightVisArea;
	          }
	          if(isMoz) {
				   	   //heightWinArea = window.document.body.offsetHeight;
							 heightWinArea = document.body.offsetHeight;
				   	   heightFooter = (heightWinArea - heightVisArea) + 15;
				    }
	
				    // calculate height of the frame
				    heightFrame = heightVisArea - pos[1] - heightFooter;
				    // set height to the frame
				    iframeEl = document.getElementById('workspaceiframe<%=requestIdentity%>');
				    iframeEl.style.height = heightFrame + 'px';
				    //alert('iframe esterno: ' + iframeEl.style.height);
			  }
	
	
			try{
	         	SbiJsInitializer.adaptSize<%=requestIdentity%> = adaptSize<%=requestIdentity%>Funct;
	    	} catch (err) {
	       		alert('Cannot resize the document view area');
	    	}
	
		</script>
		
		<%
		if (objLabel == null) {
			%>
			<div class='noDocumentSelectedBox'>
				<br/>
				<br/>
				<br/>
				<br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<spagobi:message key = "execBIObject.selectDocument"/>
			</div>
			<%
		} else {
			%>
			<center>
				<div id="divIframe<%=requestIdentity%>" style="width:100%;overflow=auto;">
					<iframe id="workspaceiframe<%=requestIdentity%>"
							name="workspaceiframe<%=requestIdentity%>"
				            src=""
				            style="width:100%"
				            frameborder="0"></iframe>
	
					<form 	name="formexecution<%=requestIdentity%>"
							id='formexecution<%=requestIdentity%>' method="post"
							action="<%=spagobiurl%>"
							target='workspaceiframe<%=requestIdentity%>'>
	
						<input type="hidden" name="NEW_SESSION" value="TRUE" />
						<input type="hidden" name="PAGE" value="DirectExecutionPage" />
				        <input type="hidden" name="USERNAME" value="<%=username%>" />
				        <input type="hidden" name="DOCUMENT_LABEL" value="<%=objLabel%>" />
				        <input type="hidden" name="spagobi_flow_id" value="<%=requestIdentity%>" />
				        <input type="hidden" name="spagobi_execution_id" value="<%=requestIdentity%>" />
	
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
	<%
	if (styleFile.exists()) {
		%>
		</div>
		<%
	}
	%>
	
</div>	

<script>

    try{
      window.onload = SbiJsInitializer.initialize;
  	} catch (err) {
      alert('Cannot execute javascript initialize functions');
  	}

</script>