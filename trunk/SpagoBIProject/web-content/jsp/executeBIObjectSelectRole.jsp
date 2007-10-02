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

<%@ include file="/jsp/shareProfile.jsp"%>

<%@ page import="java.util.List,
                 java.util.Iterator,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.services.modules.ExecuteBIObjectModule,
                 it.eng.spagobi.constants.AdmintoolsConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>

<% 
	// identity string for object of the page
	UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	UUID uuid = uuidGen.generateTimeBasedUUID();
	String requestIdentity = "request" + uuid.toString();
	requestIdentity = requestIdentity.replaceAll("-", "");
	// get the actor type from the session
	String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
    // get module response
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ExecuteBIObjectModule"); 
    // get the list of possible role
    List roles = (List)moduleResponse.getAttribute("roles");
    Iterator iterroles = roles.iterator();
    // get the path of the object
	Integer id = (Integer) moduleResponse.getAttribute(ObjectsTreeConstants.OBJECT_ID);

	// build the url for the parameters form
   	Map formActPars = new HashMap();
   	formActPars.put(AdmintoolsConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
   	formActPars.put(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.EXEC_PHASE_SELECTED_ROLE);
   	formActPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String formAct = urlBuilder.getUrl(request, formActPars);
    
    String modality = (String) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY); 
    
    String executionId = (String) aServiceRequest.getAttribute("spagobi_execution_id");
    String flowId = (String) aServiceRequest.getAttribute("spagobi_flow_id");
    
%>


<form action="<%= formAct.toString() %>" method="post" id='execRolesForm<%=requestIdentity%>' name='execRolesForm'>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.Title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('execRolesForm<%=requestIdentity%>').submit()"> 
      			<img class='header-button-image-portlet-section' 
      			     title='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.execButt" />' 
      			     src='<%=urlBuilder.getResourceLink(request, "/img/exec.png")%>' 
      			     alt='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.execButt" />' /> 
			</a>
		</td>
		<%
			if (modality == null || !modality.equalsIgnoreCase(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY)) {
				// build the back url (the actor parameter must be passed because the module take it from the request
    			// not from the session)
				
				Map backUrlPars = new HashMap();
			    backUrlPars.put("PAGE", "BIObjectsPage");
			    backUrlPars.put(SpagoBIConstants.ACTOR, actor);
			    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
			    String backUrl = urlBuilder.getUrl(request, backUrlPars);

				%>
				<td class='header-button-column-portlet-section'>
					<a href='<%= backUrl %>'> 
						<img class='header-button-image-portlet-section' 
							title='<spagobi:message key = "SBIDev.docConf.execBIObject.selRoles.backButt" />' 
							src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
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
		<%
		if (executionId != null && flowId != null) {
			%>
			<input type="hidden" name="spagobi_execution_id" value="<%= executionId %>" />
			<input type="hidden" name="spagobi_flow_id" value="<%= flowId %>" />
			<%
		}
		%>
		
		
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
