<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager" %>

<%  
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("ACTION_NAME", "START_ACTION");
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
%>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBISet.treeFunct.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.treeFunct.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.treeFunct.backButt" />' />
			</a>
		</td>
	</tr>
</table>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>
<portlet:defineObjects/>

<spagobi:treeObjects moduleName="TreeObjectsModule"  htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.FunctionalitiesTreeHtmlGenerator" />

<br/>

<%--table width="100%">
	<tr>
		<td align="center">
			<a href= '<%= backUrl.toString() %>' class='portlet-menu-item' >
      			<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
			</a>
			<br>
			<a href='<%= backUrl.toString() %>' >
				<spagobi:message key = "SBISet.treeFunct.backButt" />
			</a> 
		</td>
	</tr>
</table--%>







