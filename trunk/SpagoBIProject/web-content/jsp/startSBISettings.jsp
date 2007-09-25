<!--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
-->


<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="it.eng.spagobi.constants.SpagoBIConstants,
         		 it.eng.spago.configuration.ConfigSingleton,
                 it.eng.spago.base.SourceBean" %>

<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<%@ include file="/jsp/portlet_base.jsp"%>

<%@ include file="/jsp/shareProfile.jsp"%>

<portlet:defineObjects/>

	
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
