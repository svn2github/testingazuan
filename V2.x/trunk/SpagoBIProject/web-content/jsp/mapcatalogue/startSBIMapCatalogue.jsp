<%@page language="java"
        contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" %>
         
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory"%>
<%@page import="it.eng.spago.base.RequestContainer"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%
	Map backUrlPars = new HashMap();
	backUrlPars.put("ACTION_NAME", "START_ACTION");
	backUrlPars.put("PUBLISHER_NAME", "LoginSBICataloguePublisher");
	//backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);

	Map listMapUrlPars = new HashMap();
	listMapUrlPars.put("PAGE", "ListMapsPage");
	String listMapUrl = urlBuilder.getUrl(request, listMapUrlPars);
	
	Map listFeatUrlPars = new HashMap();
	listFeatUrlPars.put("PAGE", "ListFeaturesPage");
	String listFeatUrl = urlBuilder.getUrl(request, listFeatUrlPars);
%>
<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>			
			<spagobi:message key="SBIMapCatalogue.titleMenu" bundle="component_mapcatalogue_messages" />
		</td>
		<%if(ChannelUtilities.isPortletRunning()) { %>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "SBIMapCatalogue.backButton" bundle="component_mapcatalogue_messages" />' 
      				 src='<%=urlBuilder.getResourceLink(request, "/img/mapcatalogue/back.png")%>' 
      				 alt='<spagobi:message key = "SBIMapCatalogue.backButton"  bundle="component_mapcatalogue_messages"/>' />
			</a>
		</td>
		<% } %>
	</tr>
</table>

	
<div class="div_background">
	<br/>
	<table>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img height="80px" width="80x" src='<%=urlBuilder.getResourceLink(request, "/img/mapcatalogue/mapManagement.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<%=listMapUrl%>' 
					class="link_main_menu" >
					<spagobi:message key="SBIMapCatalogue.linkMaps" bundle="component_mapcatalogue_messages" /></a>
			</td>
		</tr>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img height="80px" width="80px" src='<%=urlBuilder.getResourceLink(request, "/img/mapcatalogue/featureManagement.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<%=listFeatUrl%>' 
					class="link_main_menu" >
					<spagobi:message key="SBIMapCatalogue.linkFeatures" bundle="component_mapcatalogue_messages" /></a>
			</td>
		</tr>				
	</table>
	<br/>
</div>