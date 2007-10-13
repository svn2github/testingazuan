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
         import="it.eng.spagobi.constants.SpagoBIConstants"
%>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<%@ include file="/jsp/portlet_base.jsp"%>

<%@ include file="/jsp/shareProfile.jsp"%>

<portlet:defineObjects/>

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