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

<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="java.io.File"%>
<%@page import="it.eng.spago.configuration.ConfigSingleton"%>
<%@page import="it.eng.spagobi.managers.ExecutionManager"%>


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
			htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.NestedMenuHtmlGenerator"/>
	</div>
	
	
	<div class='workspaceRightBox' >
		<%
	    // get spagobi url
	    String spagobiurl = GeneralUtilities.getSpagoBiContextAddress();
	    if (!spagobiurl.endsWith("/")) spagobiurl += "/";
	    spagobiurl += "servlet/AdapterHTTP";
	    // get module response
	    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ExecutionWorkspaceModule");
		  // get the BiObject label from the response
	    String objLabel = (String) moduleResponse.getAttribute(ObjectsTreeConstants.OBJECT_LABEL);		
	    String requestIdentity = (String) moduleResponse.getAttribute("spagobi_execution_id");
		%>

		<script>
	
			pos<%=requestIdentity%> = null; 
	
			function adaptSize<%=requestIdentity%>Funct() {
				// calculate height of the visible area
				heightVisArea = 0;
				if(isIE5()) { heightVisArea = top.document.body.clientHeight; }
				if(isIE6()) { heightVisArea = top.document.body.clientHeight; }
				if(isIE7()) { heightVisArea = top.document.documentElement.clientHeight }
				if(isMoz()) { heightVisArea = top.innerHeight; }
				// calculate the total area of the window
				heightWinArea = 0;
				if(isIE5()) { heightWinArea = document.body.scrollHeight; }
				if(isIE6()) { heightWinArea = document.body.scrollHeight; }
				if(isIE7()) { heightWinArea = document.body.offsetHeight; }
				if(isMoz()) { heightWinArea = document.body.offsetHeight; }
				// check if the page has scrollbar
				hasScroll = false;
				heightScroll = heightWinArea - heightVisArea;
				if(heightScroll>0) {
					hasScroll = true;
				}
				// get the frame div object
				diviframeobj = document.getElementById('divIframe<%=requestIdentity%>');
				// find the frame div position
				pos<%=requestIdentity%> = findPos(diviframeobj);	
				// calculate space below position frame div
				spaceBelowPos = heightVisArea - pos<%=requestIdentity%>[1];
				// set height to the frame
				iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
				if(hasScroll) {
					iframeEl.style.height = spaceBelowPos + heightScroll + 'px';
					//try {
					//	if(isIE5) { heightScroll = iframeEl.contentWindow.document.body.scrollHeight; }
					//	if(isIE6) { heightScroll = iframeEl.contentWindow.document.body.scrollHeight; }
					//	if(isIE7) { heightScroll = iframeEl.contentWindow.document.body.scrollHeight; }
					//	if(isMoz) { heightScroll = iframeEl.contentWindow.document.body.offsetHeight; }
					//	iframeEl.style.height = heightScroll + 'px';
					//} catch (err) {
					//	iframeEl.style.height = spaceBelowPos + heightScroll + 'px';
					//}
		  		} else {
		  			iframeEl.style.height = spaceBelowPos + 'px';
		    		// to give time to the browser to update the dom (dimension of the iframe)
		  			setTimeout("adaptSize<%=requestIdentity%>_2Part()", 250);
		  		}
			}
	
		  	function adaptSize<%=requestIdentity%>_2Part() {
               	// calculate height of the win area and height footer
				heightWinArea = 0;
  				heightFooter = 0;
  				heightScroll = 0;
  				if(isIE5()) {
  					heightWinArea = document.body.scrollHeight;
  					heightFooter = heightWinArea - heightVisArea;
  				}
  				if(isIE6()) {
  					heightWinArea = document.body.scrollHeight;
  					heightFooter = heightWinArea - heightVisArea;
  				}
  				if(isIE7()) {
  					heightWinArea = document.body.offsetHeight;
  					heightFooter = heightWinArea - heightVisArea;
  				}
  				if(isMoz()) {
  					heightWinArea = document.body.offsetHeight;
  					heightFooter = (heightWinArea - heightVisArea);
  				}	 
  	  			// calculate height of the frame
  				heightFrame = heightVisArea - pos<%=requestIdentity%>[1] - heightFooter;
  				iframeEl = document.getElementById('iframeexec<%=requestIdentity%>');
  				iframeEl.style.height = heightFrame + 'px';
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
			
			ExecutionManager executionManager = ExecutionManager.getInstance();
			ExecutionManager.ExecutionInstance instance = executionManager.getLastExecutionInstance(requestIdentity);
			boolean isRefreshRequest = false;
			if (instance != null) {
				isRefreshRequest = true;
			}
			
			%>

			
			<div class='navBarContainerEW'>
				<div id='navigationBarContainer<%=requestIdentity%>' class='navBarTextContainerEW'>
					<div id="navigationBar<%=requestIdentity%>" class='documentNameNavBarEW'>
						&nbsp;
						<%-- this div we be filled by js code --%>
					</div>
				</div>
			<!-- ***************************************************************** -->
			<!-- ***************************************************************** -->
			<!-- **************** START MAXIMIZE ********************************* -->
			<!-- ***************************************************************** -->
			<!-- ***************************************************************** -->
				<div class='navBarBottonContainerEW'>	
					<div class='documentMaximizeNavBarEW'>
						<div id='maximizeDiv<%=requestIdentity%>' style='visibility:visible;'>
							<a class='documentMaximizeLinkNavBarEW' href='javascript:maximize<%=requestIdentity%>();'>
								<img class='documentMaximizeIconNavBarEW'
									src='<%= urlBuilder.getResourceLink(request, "/img/maximize32.jpg")%>'
									name='maximize'
									alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIExecution.maximize", "messages")%>'
									title='<%=msgBuilder.getMessage(aRequestContainer, "SBIExecution.maximize", "messages")%>' />
							</a>
						</div>
					</div>
				</div>
			</div>
			<script>
				if(!isIE7() && !isMoz()) {
	        		$('maximizeDiv<%=requestIdentity%>').style.visibility	= 'hidden';
	        	}   
			</script>
			<div id='maximizebackground<%=requestIdentity%>' class='maximizeContainer'>
				<table class='maximizeTitleTable'>
					<tr>
						<td>
					        <div id='maximizeNavigationBarContainer<%=requestIdentity%>'>
							</div>
						</td>
						<td align='center'>
							<a class='documentMaximizeLinkNavBarEW' href='javascript:minimize<%=requestIdentity%>()' >
								<img class='closeMaximizeIcon'
									src='<%= urlBuilder.getResourceLink(request, "/img/erase32.png")%>'
									name='close'
									alt='<%=msgBuilder.getMessage(aRequestContainer, "SBIExecution.close", "messages")%>'
									title='<%=msgBuilder.getMessage(aRequestContainer, "SBIExecution.close", "messages")%>' />
							</a>
						</td>
					</tr>
				</table>
			</div> 
			<script>
			      dimensionHolder<%=requestIdentity%> = new functDimensionHolder<%=requestIdentity%>();
			    
			      function functDimensionHolder<%=requestIdentity%>() {
			        this.width = 0;
			        this.height = 0;
			      }
				
			      function maximize<%=requestIdentity%>() {      
			        if(!isIE7() && !isMoz()) {
			        	alert('Works only with Explorer 7 or Firefox ...');
			        	return;
			        }   
			        divbg = document.getElementById('maximizebackground<%=requestIdentity%>');
			        divbg.style.display='inline';
			        ifram = document.getElementById('iframeexec<%=requestIdentity%>');
			        clientHeight = ifram.clientHeight;
					clientWidth = ifram.clientWidth;						
			        dimensionHolder<%=requestIdentity%>.width = clientWidth;
			        dimensionHolder<%=requestIdentity%>.height = clientHeight;
			        ifram.style.position='absolute';
			        ifram.style.left='2%';
			        ifram.style.top='7%';
			        ifram.style.width='96%';
			        ifram.style.height='91%';
			        // get the nav bar container element
			        navbarContEl = $('navigationBarContainer<%=requestIdentity%>');
			        // get the html contained
			        navbarContElinnHTML = navbarContEl.innerHTML;
			        // get the navigation bar 
			        navbarEl = $('navigationBar<%=requestIdentity%>');
			        // remove navigation bar
			        navbarEl.remove();
			        // insert html into maximize navigation bar cotainer
			        new Insertion.Top('maximizeNavigationBarContainer<%=requestIdentity%>', navbarContElinnHTML);
				  }
			      
			      
			      function minimize<%=requestIdentity%>() {
			          divbg = document.getElementById('maximizebackground<%=requestIdentity%>');
			          divbg.style.display='none';
			          ifram = document.getElementById('iframeexec<%=requestIdentity%>');
			          ifram.style.top='0px';
			          ifram.style.left='0px';
			          ifram.style.paddingLeft='0px';
			          ifram.style.marginLeft='0px';
			          ifram.style.width='100%';
			          ifram.style.position='relative';
			          ifram.style.height=dimensionHolder<%=requestIdentity%>.height + 'px';
			          // get the nav bar container element
			          navbarContEl = $('maximizeNavigationBarContainer<%=requestIdentity%>');
			          // get the html contained
			          navbarContElinnHTML = navbarContEl.innerHTML;
			          // get the navigation bar 
			          navbarEl = $('navigationBar<%=requestIdentity%>');
			          // remove navigation bar
			          navbarEl.remove();
			          // insert html into navigation bar container
			          new Insertion.Top('navigationBarContainer<%=requestIdentity%>', navbarContElinnHTML);		          
			      }
			      
			</script>
			<!-- ***************************************************************** -->
			<!-- ***************************************************************** -->
			<!-- **************** END MAXIMIZE *********************************** -->
			<!-- ***************************************************************** -->
			<!-- ***************************************************************** -->
			
			
			<center>
				<div id="divIframe<%=requestIdentity%>" style="width:100%;overflow=auto;">
					<iframe id="iframeexec<%=requestIdentity%>"
							name="iframeexec<%=requestIdentity%>"
				            src=""
				            style="width:100%;"
				            frameborder="0"></iframe>
	
					<form 	name="formexecution<%=requestIdentity%>"
							id='formexecution<%=requestIdentity%>' method="post"
							action="<%=spagobiurl%>"
							target='iframeexec<%=requestIdentity%>'>
						
						<%
						if (!isRefreshRequest) {
						   	// get the user profile from session
							SessionContainer permSession = aSessionContainer.getPermanentContainer();
							IEngUserProfile userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
							String username = userProfile.getUserUniqueIdentifier().toString();
							%>
							<input type="hidden" name="NEW_SESSION" value="TRUE" />
							<input type="hidden" name="PAGE" value="DirectExecutionPage" />
					        <input type="hidden" name="USERNAME" value="<%=username%>" />
					        <input type="hidden" name="DOCUMENT_LABEL" value="<%=objLabel%>" />
					        <input type="hidden" name="spagobi_flow_id" value="<%=requestIdentity%>" />
					        <input type="hidden" name="spagobi_execution_id" value="<%=requestIdentity%>" />
							<%
						} else {
							%>
							<input type="hidden" name="NEW_SESSION" value="TRUE" />
							<input type="hidden" name="PAGE" value="DirectExecutionPage" />
							<input type="hidden" name="OPERATION" value="RECOVER_EXECUTION_FROM_DRILL_FLOW" />
							<input type="hidden" name="spagobi_flow_id" value="<%=requestIdentity%>" />
							<input type="hidden" name="spagobi_execution_id" value="<%=instance.getExecutionId()%>" />
							<%
						}
						%>						
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
