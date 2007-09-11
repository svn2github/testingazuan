<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="false" 
         import="it.eng.spagobi.constants.SpagoBIConstants"
%>
<%@page import="it.eng.spagobi.utilities.urls.UrlBuilderFactory"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="it.eng.spago.base.RequestContainer"%>
<%@page import="it.eng.spagobi.utilities.urls.IUrlBuilder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>

<%
	RequestContainer reqCont = ChannelUtilities.getRequestContainer(request);
	String  sbiMode = null;
	String channelType = reqCont.getChannelType();
	if ("PORTLET".equalsIgnoreCase(channelType)) sbiMode = "PORTLET";
	else sbiMode = "WEB";
	// create url builder 
	IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder(sbiMode);

	Map listMapUrlPars = new HashMap();
	listMapUrlPars.put("PAGE", "ListMapsPage");
	String listMapUrl = urlBuilder.getUrl(request, listMapUrlPars);
	
	Map listFeatUrlPars = new HashMap();
	listFeatUrlPars.put("PAGE", "ListFeaturesPage");
	String listFeatUrl = urlBuilder.getUrl(request, listFeatUrlPars);
%>

<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "/css/spagobi_shared.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "/css/spagobi_portlet.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "/css/jsr168.css")%>' type='text/css' />
 

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section-no-buttons' 
		    style='vertical-align:middle;padding-left:5px;'>			
			<spagobi:message key="SBIMapCatalogue.titleMenu" bundle="component_mapcatalogue_messages" />
		</td>
	</tr>
</table>

	
<div class="div_background">
	<br/>
	<table>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img height="80px" width="80x" src='<%=urlBuilder.getResourceLink(request, "/components/mapcatalogue/img/mapManagement.png")%>' />
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
				<img height="80px" width="80px" src='<%=urlBuilder.getResourceLink(request, "/components/mapcatalogue/img/featureManagement.png")%>' />
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