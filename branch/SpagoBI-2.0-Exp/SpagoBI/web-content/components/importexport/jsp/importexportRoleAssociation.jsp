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
				it.eng.spagobi.importexport.ImportExportConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Role" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	List curRoles = (List)moduleResponse.getAttribute(ImportExportConstants.LIST_CURRENT_ROLES);
	List expRoles = (List)moduleResponse.getAttribute(ImportExportConstants.LIST_EXPORTED_ROLES);
    Iterator iterExpRoles = expRoles.iterator();
   
  	PortletURL exitUrl = renderResponse.createActionURL();
   	exitUrl.setParameter("PAGE", "ImportExportPage");
   	exitUrl.setParameter("MESSAGEDET", ImportExportConstants.IMPEXP_EXIT);
  	exitUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", ImportExportConstants.IMPEXP_ROLE_ASSOCIATION);
   	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.roleAssociation"  bundle="component_impexp_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= exitUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.exit"  bundle="component_impexp_messages"/>' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/stop.gif")%>' 
      				 alt='<spagobi:message key = "Sbi.exit"  bundle="component_impexp_messages"/>' />
			</a>
		</td>
	</tr>
</table>






<div class="div_background_no_img">

    <form method='POST' action='<%=formUrl.toString()%>' id='roleAssForm' name='roleAssForm'>
	<div style="float:left;width:70%;" class="div_detail_area_forms">
		<table style="margin:10px;">
			<tr>
				<td class='portlet-section-header'><spagobi:message key = "SBISet.impexp.exportedRoles"  bundle="component_impexp_messages"/></td>
				<td class='portlet-section-header'><spagobi:message key = "SBISet.impexp.currentRoles"  bundle="component_impexp_messages"/></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%if(expRoles.isEmpty()) { %>
			<tr>
				<td colspan="2" style="color:#074B88;"><spagobi:message key="SBISet.impexp.noRoleExported"  bundle="component_impexp_messages"/></td>
			</tr>
			<% } %>
		    <%
		    while(iterExpRoles.hasNext()) {
		    	Role role = (Role)iterExpRoles.next();
		    %>
			<tr>
				<td class='portlet-form-field-label'>
				<%
				  String rolename = role.getName();
					if((rolename!=null) && (rolename.length() > 50)) {
					   rolename = rolename.substring(0, 50);
					   rolename += "...";
					}
				%>
            <span title="<%=role.getName()%>" alt="<%=role.getName()%>"><%=rolename%></span>
        </td>
				<td>
				    <input type="hidden" name="expRole" value="<%=role.getId()%>" />
				    <% 
						Iterator iterCurRoles = curRoles.iterator();
						boolean disabled = false;
						Integer idAssRole = null;
						while(iterCurRoles.hasNext()) {
							Role roleCur = (Role)iterCurRoles.next();
							if(roleCur.getName().equalsIgnoreCase(role.getName())){
								disabled = true;
								idAssRole = roleCur.getId();
								break;
							}
						}
					%>
					<%
						if(disabled) {
					%>
					<input type="hidden" name="roleAssociated<%=role.getId()%>" value="<%=idAssRole%>"> 
					<select style="width:250px" disabled>
					<%
						} else { 
					%>
					<select style="width:250px" name="roleAssociated<%=role.getId()%>" >
					<%
						}					
					%>	
						<option value="">
							<spagobi:message key="Sbi.selectcombo"  bundle="component_impexp_messages"/>
						</option>
						<% 
							iterCurRoles = curRoles.iterator();
							String selected = null;
							while(iterCurRoles.hasNext()) {
								selected = "";
								Role roleCur = (Role)iterCurRoles.next();
								if(roleCur.getName().equalsIgnoreCase(role.getName()))
									selected=" selected ";
						%>
						<option value='<%=roleCur.getId()%>' <%=selected%>><%=roleCur.getName()%></option>
						<% } %>
					</select>
				</td>
			</tr>
			<% } %>
		</table>
	</div>
	
	<div style="float:left;width:18%;">
		<input type="image" 
		       name="submit" 
		       title='<spagobi:message key="Sbi.next"  bundle="component_impexp_messages"/>' 
		       src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/next.gif")%>' 
		       alt='<spagobi:message key="Sbi.next"  bundle="component_impexp_messages"/>' />
		<br/>
		<ul style="color:#074B88;">
			<li><spagobi:message key = "SBISet.impexp.rolerule1"  bundle="component_impexp_messages"/></li>
			<li><spagobi:message key = "SBISet.impexp.rolerule2"  bundle="component_impexp_messages"/></li>
			<li><spagobi:message key = "SBISet.impexp.rolerule3"  bundle="component_impexp_messages"/></li>
		</ul>
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>











