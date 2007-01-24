<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="false" 
         import="it.eng.spagobi.constants.SpagoBIConstants,
         		 it.eng.spago.configuration.ConfigSingleton,
                 it.eng.spago.base.SourceBean"
%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>


<portlet:defineObjects/>


<%
	PortletURL scheduleUrl = renderResponse.createActionURL();
	scheduleUrl.setParameter("PAGE", "SchedulerGUIPage");
	scheduleUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.MESSAGE_GETOBJECTS_SCHED);

%>

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
			<spagobi:message key = "SBIAdmin.titleMenu" />
		</td>
	</tr>
</table>



<div class="div_background">
    <br/>	
	<table>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderAdministrationIcon.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="BIObjectsPage"/><portlet:param name="ACTOR" value="<%= SpagoBIConstants.ADMIN_ACTOR %>"/><portlet:param name="OPERATION" value="<%= SpagoBIConstants.FUNCTIONALITIES_OPERATION %>"/></portlet:actionURL>' 
					class="link_main_menu" >
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
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="BIObjectsPage"/><portlet:param name="ACTOR" value="<%= SpagoBIConstants.ADMIN_ACTOR %>"/></portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBISet.linkDocMan" />
				</a>
			</td>
		</tr>
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/scheduleIcon64.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<%=scheduleUrl.toString()%>' 
					class="link_main_menu" >
					<spagobi:message key = "scheduler.Schedule" />
				</a>
			</td>
		</tr>
	
	</table>
	<br/>
</div>
