<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.constants.SpagoBIConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Role" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	List curRoles = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_CURRENT_ROLES);
	List expRoles = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_EXPORTED_ROLES);
    Iterator iterExpRoles = expRoles.iterator();
    
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("ACTION_NAME", "START_ACTION");
   	backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_ROLE_ASSOCIATION);
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.roleAssociation" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back" />' />
			</a>
		</td>
	</tr>
</table>






<div class="div_background_no_img">

    <form method='POST' action='<%=formUrl.toString()%>' id='roleAssForm' name='roleAssForm'>
	<div style="float:left;width:50%;" class="div_detail_area_forms">
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header'>Exported Roles</td>
				<td class='portlet-section-header'>System Role Associations</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
		    <%
		    while(iterExpRoles.hasNext()) {
		    	Role role = (Role)iterExpRoles.next();
		    %>
			<tr>
				<td class='portlet-form-field-label'><%=role.getName()%></td>
				<td>
				    <input type="hidden" name="expRole" value="<%=role.getId()%>" />
					<select style="width:250px" name="roleAssociated<%=role.getId()%>" >
						<option value=""></option>
						<% 
							Iterator iterCurRoles = curRoles.iterator();
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
	
	<div style="float:left;">
		<input type="submit" name="submit" value="Next" />
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>











