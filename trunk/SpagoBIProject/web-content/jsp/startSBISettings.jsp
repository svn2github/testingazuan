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

<div class="div_background">

<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/table.css")%>' 
      type='text/css' />
<LINK rel='StyleSheet' 
      href='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/css/spagobi.css")%>' 
      type='text/css' />
	
	<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section-no-buttons'>
				<spagobi:message key = "SBISet.titleMenu" />
			</td>
		</tr>
	</table>

    <br/>
	
	<table>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/engineAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListEnginesPage"/></portlet:actionURL>' class="portlet-menu-item" >
				 	<spagobi:message key="SBISet.linkEngConf" />
				</a>
			</td>
		</tr>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="TreeObjectsPage"/><portlet:param name="ACTOR" value="<%= SpagoBIConstants.ADMIN_ACTOR %>"/><portlet:param name="OPERATION" value="<%= SpagoBIConstants.FUNCTIONALITIES_OPERATION %>"/></portlet:actionURL>' class="portlet-menu-item" >
					<spagobi:message key = "SBISet.linkFunctMan" />
				</a>
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
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="TreeObjectsPage"/><portlet:param name="ACTOR" value="<%= SpagoBIConstants.ADMIN_ACTOR %>"/><portlet:param name="OBJECTS_VIEW" value="<%= SpagoBIConstants.VIEW_OBJECTS_AS_LIST %>"/><portlet:param name="OBJECTS_VIEW" value="<%= SpagoBIConstants.VIEW_OBJECTS_AS_LIST %>"/></portlet:actionURL>' class="portlet-menu-item" >
					<spagobi:message key = "SBISet.linkDocMan" />
				</a>
			</td>
		</tr>
	</table>
	
	<br/>
	
</div>