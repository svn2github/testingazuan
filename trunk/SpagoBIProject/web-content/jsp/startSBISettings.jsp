<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="false" 
         import="it.eng.spagobi.constants.SpagoBIConstants,
         		 it.eng.spago.configuration.ConfigSingleton,
                 it.eng.spago.base.SourceBean"
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
			<spagobi:message key = "SBISet.titleMenu" />
		</td>
	</tr>
</table>



<div class="div_background">
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
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListEnginesPage"/></portlet:actionURL>' 
					class="link_main_menu" >
				 	<spagobi:message key="SBISet.linkEngConf" />
				</a>
			</td>
		</tr>
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/rolesynch64.jpg")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListRolesPage"/></portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBISet.linkRolesSynch" />
				</a>
			</td>
		</tr>
		<%
			boolean impexpInst = false;
			ConfigSingleton spagoConfig = ConfigSingleton.getInstance();
			SourceBean moduleSB = (SourceBean)spagoConfig.getFilteredSourceBeanAttribute("SPAGOBI_COMPONENTS.SPAGOBI_COMPONENT", "name", "importexport");
			if(moduleSB!=null){
				String inst = (String)moduleSB.getAttribute("installed");
				if((inst!=null) && inst.equalsIgnoreCase("true")) {
					impexpInst = true;
				}
			}
			if(impexpInst) {
		%>
		<tr class="portlet-font" vAlign="middle">
			<td width="100" align="center">
				<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/importexport64.png")%>' />
			</td>
			<td width="20">
				&nbsp;
			</td>
			<td vAlign="middle">
			    <br/> 
				<a href='<portlet:actionURL>
				                <portlet:param name="ACTOR" value="<%= SpagoBIConstants.ADMIN_ACTOR %>"/>
								<portlet:param name="PAGE" value="BIObjectsPage"/>
								<portlet:param name="OPERATION" value="<%=SpagoBIConstants.IMPORTEXPORT_OPERATION %>"/>
								<portlet:param name="OBJECTS_VIEW" value="<%=SpagoBIConstants.VIEW_OBJECTS_AS_TREE%>"/>
						</portlet:actionURL>' 
					class="link_main_menu" >
					<spagobi:message key = "SBISet.importexport" />
				</a>
			</td>
		</tr>
		<%
			}
		%>
	
	</table>
	<br/>
</div>
