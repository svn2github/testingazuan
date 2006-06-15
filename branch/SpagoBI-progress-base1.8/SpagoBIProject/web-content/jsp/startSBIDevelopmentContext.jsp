<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="false" 
         import="it.eng.spagobi.constants.SpagoBIConstants"
%>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>
<portlet:defineObjects/>

<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/spagobi.css")%>' 
      type='text/css' />

<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/jsr168.css")%>' 
      type='text/css' />
      
<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/external.css")%>' 
      type='text/css' />




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section-no-buttons' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.titleMenu" />
		</td>
	</tr>
</table>

	
<div class="div_background">
	<br/>
	<table>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/valueModalityAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListLovsPage"/></portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBIDev.linkPredLov" /></a>
			</td>
		</tr>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/modalityCheckAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="LISTMODALITIESCHECKSPAGE"/></portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBIDev.linkValConst" /></a>
			</td>
		</tr>
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/domainAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListParametersPage"/></portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBIDev.linkParam" /></a>
			</td>
		</tr>
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/objectAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
			    <a href='<portlet:actionURL><portlet:param name="PAGE" value="BIObjectsPage"/><portlet:param name="ACTOR" value="<%= SpagoBIConstants.DEV_ACTOR %>"/></portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBIDev.linkDoc" /></a>
			</td>
		</tr>
	</table>
	<br/>
</div>