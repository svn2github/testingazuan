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
                 

<%
    
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule");
    // get title
    String title = (String) moduleResponse.getAttribute("title");
    // get SpagoBI context name   	
    String contextName = ChannelUtilities.getSpagoBIContextName(request);
    
   	// build the back link
   	//PortletURL backUrl = renderResponse.createActionURL();
	//backUrl.setParameter("PAGE", "BIObjectsPage");
	//backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	//backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	// build the refresh button
	//PortletURL refreshUrl = renderResponse.createActionURL();
	//refreshUrl.setParameter("PAGE", BIObjectsModule.MODULE_PAGE);
	//refreshUrl.setParameter(SpagoBIConstants.ACTOR, actor);
	//refreshUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
%>

<%      
	
%>

<table heigth='40px' width='100%' cellspacing='0' border='0'>
	<tr>
		<td class='header-title-column-single-object-execution-portlet-section' style='vertical-align:middle;'>
			&nbsp;&nbsp;&nbsp;Titolo
		</td>
	</tr>
</table>


<iframe id='frameexecution' name='frameexecution' 
        src="<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteJasperReportPage&TASK=EXEC_REPORT" 
        frameborder=0  width='100%' >
</iframe>


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
    if(isIE7) { heightVisArea = window.document.documentElement.clientHeight }
    if(isMoz) { heightVisArea = window.innerHeight; }
    
    var frame = document.getElementById('frameexecution');	
    frame.style.height = (heightVisArea - 40 )  + 'px';
	
</script>



