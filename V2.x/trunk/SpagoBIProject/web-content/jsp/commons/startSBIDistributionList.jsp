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
         import="it.eng.spagobi.commons.constants.SpagoBIConstants,
         		 it.eng.spago.configuration.ConfigSingleton,
                 it.eng.spago.base.SourceBean,
                 it.eng.spago.security.IEngUserProfile,
                 java.util.Collection,
                 java.util.Iterator" %>
<%@page import="it.eng.spago.base.RequestContainer"%>
<%@page import="it.eng.spago.base.SessionContainer"%>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>

<%@ include file="/jsp/commons/portlet_base.jsp"%>


<portlet:defineObjects/>
<% 

	
	Collection c = userProfile.getRoles();
	Iterator i = c.iterator();
	int j = 0;
	while (i.hasNext()){
		String roles = (String)i.next();
		aSessionContainer.setAttribute("role"+j,roles);
		j++ ;
	}
		while (j<6){
			String s= "/";
			aSessionContainer.setAttribute("role"+j,s);
			j++ ;
		}
%>

<div class="div_background">
    <br/>	
	<table>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DISTRIBUTIONLIST_USER)) {%>
			<tr class="portlet-font" vAlign="middle">
				<td width="100" align="center">
					<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/tools/distributionlist/distributionlistuser.png")%>' />
				</td>
				<td width="20">
					&nbsp;
				</td>
				<td vAlign="middle">
					    <br/> 
						<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListDistributionListUserPage"/>
								<portlet:param name="PAGE" value="ListDistributionListUserPage"/></portlet:actionURL>' 
							class="link_main_menu" >
						 	<spagobi:message key="SBISet.linkDLUConf" />
						</a>
					</td>
			</tr>
		<%} %>
		<% if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DISTRIBUTIONLIST_MANAGEMENT)) {%>		
			<tr class="portlet-font">
					<td width="100" align="center">
						<img src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/tools/distributionlist/distributionlist.png")%>' />
					</td>
					<td width="20">
						&nbsp;
					</td>
					<td vAlign="middle">
					    <br/> 
						<a href='<portlet:actionURL><portlet:param name="PAGE" value="ListDistributionListPage"/></portlet:actionURL>' 
							class="link_main_menu" >
						 	<spagobi:message key="SBISet.linkDLConf" />
						</a>
					</td>
			</tr>
		<%} %>
	</table>
	<br/>
</div>
<%@ include file="/jsp/commons/footer.jsp"%>
<iframe id='iframeLogin'
                 name='iframeLogin'
                 src='<%=srcIFrame%>'
                 height='0'
                 width='0'
                 frameborder='0' >
</iframe>
