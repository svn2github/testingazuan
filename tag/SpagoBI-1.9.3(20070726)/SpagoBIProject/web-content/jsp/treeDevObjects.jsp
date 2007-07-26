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


<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.DetailBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager" %>

<% 
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("TreeObjectsModule"); 
  
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("ACTION_NAME", "START_ACTION");
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBIDevelopmentContextPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL addUrl = renderResponse.createActionURL();
   addUrl.setParameter("PAGE", DetailBIObjectModule.MODULE_PAGE);
   addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_NEW);
   addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
  
   PortletURL viewListUrl = renderResponse.createActionURL();
   viewListUrl.setParameter("PAGE", it.eng.spagobi.services.modules.BIObjectsModule.MODULE_PAGE);
   viewListUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
   viewListUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);

%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.devObjects.titleTree"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= addUrl.toString() %>'> 
      			<img title='<spagobi:message key = "SBISet.devObjects.newObjButt" />' width='25px' height='25px' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/new.png")%>' alt='<spagobi:message key = "SBISet.devObjects.newObjButt" />' />
			</a>
		</td>
		<td class='header-button-column-portlet-section'>			
			<a href='<%= viewListUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.devObjects.listViewButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' alt='<spagobi:message key = "SBISet.devObjects.listViewButt" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.devObjects.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.devObjects.backButt" />' />
			</a>
		</td>
	</tr>
</table>


<div class="div_background">
	<spagobi:treeObjects moduleName="TreeObjectsModule"  
	                     htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.DevTreeHtmlGenerator" />
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
</div>	










