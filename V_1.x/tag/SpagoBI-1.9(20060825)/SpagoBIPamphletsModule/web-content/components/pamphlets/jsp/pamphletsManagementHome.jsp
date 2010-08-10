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
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.pamphlets.constants.PamphletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.pamphlets.bo.Pamphlet" %>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(PamphletsConstants.PAMPHLET_MANAGEMENT_MODULE); 
   List pamphletList = (List)moduleResponse.getAttribute(PamphletsConstants.PAMPHLET_LIST);
   Iterator iterPamp = pamphletList.iterator();
   
    
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("ACTION_NAME", "START_ACTION");
   //backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBIAdministrationContextPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
  
   PortletURL formNewUrl = renderResponse.createActionURL();
   formNewUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   formNewUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_NEW_PAMPHLET);
   formNewUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
%>


<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key="SBISet.PamphletsManagement" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.back" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/back.png")%>' 
      				 alt='<spagobi:message key = "pamp.back"  bundle="component_pamphlets_messages"/>' />
			</a>
		</td>
	</tr>
</table>



<div class="div_background_no_img">
	
	
	<div style="float:left;width:50%;" class="div_detail_area_forms">
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' >	
				<spagobi:message key = "pamp.PamphletsList" bundle="component_pamphlets_messages"/>
		</div>
		
		

		<table style="width:98%;">
				<tr><td colspan="3">&nbsp;</td></tr>				
			<%
				while(iterPamp.hasNext()) {
					Pamphlet pamp = (Pamphlet)iterPamp.next();
					PortletURL formDetailUrl = renderResponse.createActionURL();
					formDetailUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
					formDetailUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_DETAIL_PAMPHLET);
					formDetailUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
					formDetailUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pamp.getPath());
			
					PortletURL eraseUrl = renderResponse.createActionURL();
					eraseUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
					eraseUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_ERASE_PAMPHLET);
					eraseUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
					eraseUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pamp.getPath());
			%>		
				<tr style="border:1px solid #eeeeee;">
					<td style="valign:middle;" class="portlet-form-field-label"><%=pamp.getName()%></td>
					<td width="20">
						<a href='<%=formDetailUrl.toString()%>' />
						<img 	title='<spagobi:message key = "pamp.detail" bundle="component_pamphlets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/detail.gif")%>' 
      				 		alt='<spagobi:message key = "pamp.detail"  bundle="component_pamphlets_messages"/>' />
      				 	</a>
					</td>
					<td  width="20">
					    <a href='<%=eraseUrl.toString()%>' />
						<img 	title='<spagobi:message key = "pamp.erase" bundle="component_pamphlets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/erase.gif")%>' 
      				 		alt='<spagobi:message key = "pamp.erase"  bundle="component_pamphlets_messages"/>' />
      				 	</a>
					</td>
				</tr>
			<% 
				}
			%>
			<tr><td colspan="3">&nbsp;</td></tr>
		</table>
	</div>









	<div style="float:left;width:45%" class="div_detail_area_forms">
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header'>	
				<spagobi:message key = "pamp.addPamphlet" bundle="component_pamphlets_messages"/>
		</div>
		<div>
		    <br/>
		    <form action="<%=formNewUrl.toString()%>" method='POST' id='newForm' name='newForm'>
			  <span class="portlet-form-field-label">
						<spagobi:message key = "pamp.NamePamphlet" bundle="component_pamphlets_messages"/>  
				</span> &nbsp;&nbsp;&nbsp;
				<input type="text" size="30" name="nameNewPamphlet"/>
				<input type="image" 
				       title='<spagobi:message key="pamp.addPamphlet" bundle="component_pamphlets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/add.gif")%>' 
      				 		alt='<spagobi:message key="pamp.addPamphlet"  bundle="component_pamphlets_messages"/>' />
		    </form>
		    <br/>
		    <br/>
		</div>
	</div>



	<div style="clear:left;">
			&nbsp;
	</div>







