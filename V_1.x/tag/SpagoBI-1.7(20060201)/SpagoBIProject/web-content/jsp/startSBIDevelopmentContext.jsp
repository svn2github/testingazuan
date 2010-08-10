<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="false"
%>

<%@ page import="it.eng.spagobi.constants.SpagoBIConstants" %>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>

<portlet:defineObjects/>

<div>

    <%--table width="100%" cellspacing="0" border="0">		
		<tr height='40'>
	    	<th align=middle><spagobi:message key = "SBIDev.titleMenu" /></th>
		</tr>
	</table--%>

	<style>
	@IMPORT url("/spagobi/css/table.css");
	</style>
	
	<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section-no-buttons'>
				<spagobi:message key = "SBIDev.titleMenu" />
			</td>
		</tr>
	</table>
	
	<!--br/--><br/>
	
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
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListLovsPage"/></portlet:actionURL>' class="portlet-menu-item" ><spagobi:message key = "SBIDev.linkPredLov" /></a>
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
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="LISTMODALITIESCHECKSPAGE"/></portlet:actionURL>' class="portlet-menu-item" ><spagobi:message key = "SBIDev.linkValConst" /></a>
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
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListParametersPage"/></portlet:actionURL>' class="portlet-menu-item" ><spagobi:message key = "SBIDev.linkParam" /></a>
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
			    <a href='<portlet:actionURL><portlet:param name="PAGE" value="TreeObjectsPage"/><portlet:param name="ACTOR" value="<%= SpagoBIConstants.DEV_ACTOR %>"/></portlet:actionURL>' class="portlet-menu-item" ><spagobi:message key = "SBIDev.linkDoc" /></a>
			</td>
		</tr>
		
		<!--  QBE 
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/objectAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
			    <a href='<portlet:actionURL>
			    			<portlet:param name="ACTION_NAME" value="PUBLISH_ACTION"/>
			    			<portlet:param name="PUBLISHER_NAME" value="INDEX_PUBLISHER"/>
			    		</portlet:actionURL>' class="portlet-menu-item">
			    		QBE Test</a>
			</td>
		</tr>
		  QBE -->
	</table>
	
	<!--br/--><br/>

	
</div>