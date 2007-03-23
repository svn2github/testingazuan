<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>

<%  
	Map backUrlPars = new HashMap();
	backUrlPars.put("ACTION_NAME", "START_ACTION");
	backUrlPars.put("PUBLISHER_NAME", "LoginSBIAdministrationContextPublisher");
	backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
%>


<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.treeFunct.title" />
		</td>
		<%
			if(ChannelUtilities.isPortletRunning()) {
		%>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key = "SBISet.treeFunct.backButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
      			     alt='<spagobi:message key = "SBISet.treeFunct.backButt" />' />
			</a>
		</td>
	    <%
			}
	    %>
	</tr>
</table>

<div class="div_background">

<spagobi:treeObjects moduleName="TreeObjectsModule"  htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.FunctionalitiesTreeHtmlGenerator" />

<br/>
<br/>
<br/>
<br/>
<br/>

</div>








