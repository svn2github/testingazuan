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
	BIObject biobj = (BIObject)aSessionContainer.getAttribute("JR_IE_OBJ_TO_EXEC");
	
	// get title
    String title = biobj.getName();
    // get SpagoBI context name   	
    String contextName = ChannelUtilities.getSpagoBIContextName(request);
    // build exec url
    String execUrl = contextName + "/servlet/AdapterHTTP?PAGE=ExecuteJasperReportPage&TASK=EXEC_REPORT";
    if(execId!=null) {
    	execUrl += "&EXECUTION_IDENTIFIER="+execId;
    }
    
	//  build the back link
	Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "BIObjectsPage");
    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
    
	// build the refresh link
    Map refreshUrlPars = new HashMap();
    refreshUrlPars.put("PAGE", "DirectExecutionPage");
    refreshUrlPars.put("REFRESH", "TRUE");
    if(execId!=null) {
    	refreshUrlPars.put("EXECUTION_IDENTIFIER", execId);
    }
    String refreshUrl = urlBuilder.getUrl(request, refreshUrlPars);
    
    
    
    String downPdfUrl = execUrl;
    downPdfUrl = downPdfUrl.trim();
  	if(downPdfUrl.endsWith("&")) {
   		downPdfUrl += "DOWNLOAD_PDF=TRUE";
   	} else {
   		downPdfUrl += "&DOWNLOAD_PDF=TRUE";
   	}
    
%>


<% if(!isDirectExec) { %>


<table heigth='40px' width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;<%=title%>
		</td>
       	<td class='header-button-column-portlet-section' 
       	    style='border-top: 1px solid #bbb;border-bottom: 1px solid #bbb;' >
        	<a href='<%=backUrl%>'>
           		<img title='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' 
               		 class='header-button-image-portlet-section'
               		 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
               		 alt='<spagobi:message key = "SBIDev.docConf.execBIObject.backButt" />' />
        	</a>
       	</td>
       	<td class='header-button-column-portlet-section'
       	    style='padding-top:3px;border-top: 1px solid #bbb;border-bottom: 1px solid #bbb;'>
        	<a href='<%=downPdfUrl%>'>
           		<img height="25px" title='Download Pdf' 
               		 class='header-button-image-portlet-section'
               		 src='<%=urlBuilder.getResourceLink(request, "/img/wapp/pdf32.png")%>' 
               		 alt='Download Pdf' />
        	</a>
       	</td>
	</tr>
</table>

<% } %>


<% if(isDirectExec) { %>

<table heigth='12px' width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' 
		    style='vertical-align:middle;height:12px;font-size:10px;'>
		    &nbsp;
		</td>
		<td class='header-button-column-portlet-section'
		    style='color: #074B88;vertical-align:middle;height:12px;width:130px;font-size:10px;border-top: 1px solid #bbb;border-bottom: 1px solid #bbb;' >
        	&nbsp;&nbsp;&nbsp;<span><a style="text-decoration:none;" href="<%=refreshUrl%>">Refresh</a></span>
			&nbsp;&nbsp;&nbsp;<span><a style="text-decoration:none;" href="<%=downPdfUrl%>">Download Pdf</a></span>
       	</td>
	</tr>
</table>
<br/>

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



