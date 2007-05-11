<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="java.util.UUID"%>

<div class="div_no_background">

	<div style="width: 100%">
		<spagobi:treeObjects moduleName="ExecutionWorkspaceModule" attributeToRender="FIRST_LEVEL_FOLDERS"
				htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.TitleBarHtmlGenerator" />
	</div>

	<div style="width: 30%;float: left;clear: left;">
		<spagobi:treeObjects moduleName="ExecutionWorkspaceModule" attributeToRender="SUB_TREE"
			htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.ObjectsMenuHtmlGenerator"/>
	</div>

	<div style="width: 30%;float: left;clear: left;">
		<%
	    // identity string for object of the page
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    String requestIdentity = "request" + uuid.toString();
		%>
		<center>
			<div id="divIframe<%=requestIdentity%>" style="width:100%;overflow=auto;">
			            <iframe id="iframeexec<%=requestIdentity%>"
			                    name="iframeexec<%=requestIdentity%>"
			                    src=""
			                    style="width:100%;height:100%"
			                    frameborder="0"></iframe>

			         	<form name="formexecution<%=requestIdentity%>"
			                id='formexecution<%=requestIdentity%>' method="post"
			         	      action="<%=engineurl%>"
			         	      target='iframeexec<%=requestIdentity%>'>
			         	<%
			         		 java.util.Set keys = mapPars.keySet();
			         	   Iterator iterKeys = keys.iterator();
			         	   while(iterKeys.hasNext()) {
			         	    	String key = iterKeys.next().toString();
			         	    	String value = mapPars.get(key).toString();
			         	%>
			         		<input type="hidden" name="<%=key%>" value="<%=value%>" />
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
	</div>
</div>	