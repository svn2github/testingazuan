<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				 it.eng.spago.navigation.LightNavigationManager" %>

<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExoProfileAttributeManagerModule"); 
	
   	PortletURL listUserUrl = renderResponse.createActionURL();
   	listUserUrl.setParameter("PAGE", "ExoProfileAttributeManagerListUserPage");
   
	PortletURL synchAttrUrl = renderResponse.createActionURL();
	synchAttrUrl.setParameter("PAGE", "ExoProfileAttributeManagerPage");
	synchAttrUrl.setParameter("MESSAGE", "SYNCH_ATTRIBUTES");
	synchAttrUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true"); 
   	
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section-no-buttons' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key="homeTitle"  bundle="it.eng.spagobi.exoaddins.component_exoprofman_messages" />
		</td>
	</tr>
</table>

<div class="div_background">
    <br/>	
	<table>
		<tr class="portlet-font">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/components/exoprofileattributemanager/img/UserProfile32.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<%=listUserUrl.toString()%>' class="link_main_menu" >
					<spagobi:message key = "changeProfSingleUser"  bundle="it.eng.spagobi.exoaddins.component_exoprofman_messages" />
				</a>
			</td>
		</tr>
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/components/exoprofileattributemanager/img/Synch32.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<%=synchAttrUrl.toString()%>' class="link_main_menu" >
					<spagobi:message key = "synchProf"  bundle="it.eng.spagobi.exoaddins.component_exoprofman_messages" />
				</a>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
</div>		
	
	




 



