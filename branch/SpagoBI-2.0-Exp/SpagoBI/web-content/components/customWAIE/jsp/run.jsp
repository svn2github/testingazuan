<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.BIObject,
                 java.util.List,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 java.util.Iterator,
                 it.eng.spagobi.bo.Engine,
                 it.eng.spagobi.bo.Domain,
                 it.eng.spagobi.bo.BIObjectParameter,
                 it.eng.spagobi.bo.dao.IDomainDAO,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.BIObjectsModule,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager,
                 java.util.Map,
                 it.eng.spagobi.utilities.GeneralUtilities,
                 it.eng.spago.base.SessionContainer,
                 it.eng.spago.security.IEngUserProfile" %>
<%@ page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@ page import="java.util.HashMap"%>
                 

<%
    
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
    // get the wa publisher name
    String wapub = (String)moduleResponse.getAttribute("WA_PUBLISHER_NAME");
	// get title
	String title = (String)moduleResponse.getAttribute("TITLE");
    // get execution id
	String execId = (String)moduleResponse.getAttribute("EXECUTION_IDENTIFIER");
	// get the relative (to this execution) session container
	if(execId!=null) {
		aSessionContainer = (SessionContainer)aSessionContainer.getAttribute(execId);
	}
	// get the actor 
	String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
	// get the execution modality
	String modality = (String)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
	// check if is direct execution
	boolean isDirectExec = false;
	if( (modality!=null) && modality.equalsIgnoreCase(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY))  {
		isDirectExec = true;
	}
    // get SpagoBI context name   	
    String contextName = ChannelUtilities.getSpagoBIContextName(request);
    // build exec url
    String execUrl = contextName + "/servlet/AdapterHTTP?PAGE=ExecuteCustomWAPage&WA_PUBLISHER_NAME=" + wapub;
    if(execId!=null) {
    	execUrl += "&EXECUTION_IDENTIFIER="+execId;
    }
    
	//  build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
    
%>


<% if(!isDirectExec) { %>


<table heigth='40px' width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;<%=title%>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
       	<td class='header-button-column-portlet-section'>
        	<a href='<%=backUrl%>'>
           		<img title='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' 
               		 class='header-button-image-portlet-section'
               		 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
               		 alt='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' />
        	</a>
       	</td>
	</tr>
</table>

<% } %>


<iframe id='frameexecution' name='frameexecution' 
        src="<%=execUrl%>" 
        frameborder="0"  width='100%' >
</iframe>

<script>
	function resizeContent(width, height) {
		var frame = document.getElementById('frameexecution');	
    	frame.style.height = (parseInt(height) - 40 )  + 'px';
	}
</script>

<script>
    
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
          
    // calculate height of the visible area
    var heightVisArea = 0;
    if(isIE5) { heightVisArea = window.document.body.clientHeight; }
    if(isIE6) { heightVisArea = window.document.body.clientHeight; }
    if(isIE7) { heightVisArea = window.document.body.offsetHeight }
    if(isMoz) { heightVisArea = window.innerHeight; }
    
    var frame = document.getElementById('frameexecution');	
    frame.style.height = (heightVisArea - 40 )  + 'px';
	
</script>



