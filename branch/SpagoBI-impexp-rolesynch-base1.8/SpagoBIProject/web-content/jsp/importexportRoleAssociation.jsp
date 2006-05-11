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
   
  	PortletURL exitUrl = renderResponse.createActionURL();
   	exitUrl.setParameter("PAGE", "ImportExportPage");
   	exitUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_EXIT);
  	exitUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_ROLE_ASSOCIATION);
   	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.roleAssociation" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= exitUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.exit" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/stop.png")%>' 
      				 alt='<spagobi:message key = "Sbi.exit" />' />
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
						<option value=""></option>
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
	
	<div style="float:left;">
		<input type="image" 
		       name="submit" 
		       title='<spagobi:message key="Sbi.next"/>' 
		       src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/next.png")%>' 
		       alt='<spagobi:message key="Sbi.next"/>' />
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>











