<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 java.util.List,
                 java.util.Iterator,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.constants.AdmintoolsConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spago.navigation.LightNavigationManager" %>

<% 
	// get the actor type from the session
	String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule"); 
    // get the list of possible role
    List roles = (List)moduleResponse.getAttribute("roles");
    Iterator iterroles = roles.iterator();
    // get the path of the object
	Integer id = (Integer) moduleResponse.getAttribute(ObjectsTreeConstants.OBJECT_ID);

	// build the form url	
	PortletURL formAct = renderResponse.createActionURL();
    formAct.setParameter(AdmintoolsConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
    formAct.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_PHASE_SELECTED_ROLE);
    formAct.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    
    String modality = (String) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
    
%>

<form action="<%= formAct.toString() %>" method="post" id='execRolesForm' name='execRolesForm'>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.Title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('execRolesForm').submit()"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.execButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/exec.png")%>' alt='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.execButt" />' /> 
			</a>
		</td>
		<%
			if (modality == null || !modality.equalsIgnoreCase(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY)) {
				// build the back url (the actor parameter must be passed because the module take it from the request
    			// not from the session)
    			PortletURL backUrl = renderResponse.createActionURL();
				backUrl.setParameter("PAGE", "BIObjectsPage");
				backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
				backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
				%>
				<td class='header-button-column-portlet-section'>
					<a href='<%= backUrl.toString() %>'> 
						<img class='header-button-image-portlet-section' 
							title='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.backButt" />' 
							src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
							alt='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.backButt" />' />
					</a>
				</td>
				<%
			}
		%>
	</tr>
</table>

<div class='div_background_no_img' >

		<input type="hidden" value="<%= actor %>" name="<%= SpagoBIConstants.ACTOR %>" />
		<input type="hidden" value="<%= id %>" name="<%= ObjectsTreeConstants.OBJECT_ID %>" />
		
 	<div class="div_detail_area_forms">
	 	<div class='div_detail_label'>
	 		<span class='portlet-form-field-label'>
	 			<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.roleField" />:
	 		</span>
	 	</div>
	 	<div class='div_detail_form'> 
	 	    <select class='portlet-form-field' name="role"  style="width:200px;">
	 	    <% 
	 	       while(iterroles.hasNext()) {
	 	       	String role = (String)iterroles.next(); 
	 	    %> 
	 		 	<option value="<%= role %>"><%= role %></option>	
	 	    <% 
	 	    	}
	 	    %>
	 	    </select> 
	 	</div>
	 </div>	
	 		
</div>

</form>
