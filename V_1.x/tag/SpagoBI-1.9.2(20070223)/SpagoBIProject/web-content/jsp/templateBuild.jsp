<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="it.eng.spagobi.bo.BIObject"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="it.eng.spagobi.utilities.PortletUtilities"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>               
<%@page import="it.eng.spagobi.drivers.EngineURL"%>
<%@page import="java.util.Map"%>

<%
	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	UUID uuid = uuidGen.generateTimeBasedUUID();
	String requestIdentity = "request" + uuid.toString();
    // get module response
    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("DocumentTemplateBuildModule");
	// get the BiObject from the response
    BIObject obj = (BIObject) moduleResponse.getAttribute("biobject");
	// get the url of the engine
	EngineURL engineurl = (EngineURL) moduleResponse.getAttribute(ObjectsTreeConstants.CALL_URL);
    String actor = (String) moduleResponse.getAttribute(SpagoBIConstants.ACTOR);
    String operation = (String) moduleResponse.getAttribute("operation");
	
	// build the string of the title
    String title = "";
	if (operation != null && operation.equalsIgnoreCase("newDocumentTemplate")) {
		title = PortletUtilities.getMessage("SBIDev.docConf.templateBuild.newTemplateTitle", "messages");
	} else {
		title = PortletUtilities.getMessage("SBIDev.docConf.templateBuild.editTemplateTitle", "messages");
	}
    title += " : " + obj.getName();

   	// try to get from the session the heigh of the output area
   	boolean heightSetted = false;
   	String heightArea = (String) aSessionContainer.getAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA);
   	if (heightArea == null || heightArea.trim().equals("")) {
   		heightArea = "500";
   	} else {
   		heightSetted = true;
   	}
   	
   	// build the back link
   	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter(SpagoBIConstants.PAGE, "DetailBIObjectPage");
	backUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.DETAIL_SELECT);
	backUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
	backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");

%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
    	<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
           <%=title%>
       </td>
       <td class='header-empty-column-portlet-section'>&nbsp;</td>
       <td class='header-button-column-portlet-section'>
           <a href='<%= backUrl.toString() %>'>
                 <img title='<spagobi:message key = "SBIDev.docConf.templateBuild.backButton" />' 
                      class='header-button-image-portlet-section'
                      src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
                      alt='<spagobi:message key = "SBIDev.docConf.templateBuild.backButton" />' />
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

<div id="divIframe<%=requestIdentity%>" style="width:100%">
           
           <%
           		String onloadStr = " ";
           		if(!heightSetted)
           			onloadStr = " onload='adaptSize();' ";
           		String heightStr = "height:400px;";
           		if(heightSetted)
           			heightStr = "height:"+heightArea+"px;";
           %> 
             
           <iframe <%=onloadStr%> 
				   style='display:inline;<%=heightStr%>' 
				   id='iframeexec<%=requestIdentity%>' 
                   name='iframeexec<%=requestIdentity%>'  
				   src=""
                   frameborder=0  
			       width='100%' >
         	</iframe>       
                                
         	<form name="formexecution<%=requestIdentity%>" id='formexecution<%=requestIdentity%>' method="post" 
         	      action="<%=engineurl.getMainURL()%>" 
         	      target='iframeexec<%=requestIdentity%>'>
         	<%
         		Map mapPars = engineurl.getParameters();
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
       


<!-- ***************************************************************** -->
<!-- ***************************************************************** -->
<!-- **************** END BLOCK IFRAME ******************************* -->
<!-- ***************************************************************** -->
<!-- ***************************************************************** -->