<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.constants.SpagoBIConstants,
				java.util.List,
				java.util.Map,
				java.util.Set,
				java.util.Iterator,
				it.eng.spagobi.importexport.ExistingMetadata,
				it.eng.spagobi.metadata.SbiLov,
				it.eng.spagobi.metadata.SbiExtRoles,
				it.eng.spagobi.metadata.SbiObjects,
				it.eng.spagobi.metadata.SbiParameters,
				it.eng.spagobi.metadata.SbiFunctions,
				it.eng.spagobi.metadata.SbiEngines,
				it.eng.spagobi.metadata.SbiChecks,
				it.eng.spagobi.metadata.SbiParuse" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	ExistingMetadata existMD = (ExistingMetadata)moduleResponse.getAttribute(SpagoBIConstants.EXISTING_METADATA);
	
    PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "ImportExportPage");
   	backUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_BACK_METADATA_ASS);
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	
  	PortletURL exitUrl = renderResponse.createActionURL();
   	exitUrl.setParameter("PAGE", "ImportExportPage");
   	exitUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_EXIT);
  	exitUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
      
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_METADATA_ASS);
   	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.connectionAssociation" />
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

    <form method='POST' action='<%=formUrl.toString()%>' id='connAssForm' name='connAssForm'>
	<div style="float:left;width:70%;" class="div_detail_area_forms">
		<%if(!existMD.getRoleIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Roles</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported Roles</td>
				<td class='portlet-section-header'>Existing roles</td>
			</tr>
			<%
				Map rolesAss = existMD.getRoleAssociation();
				Set rolesExp = rolesAss.keySet();
			    Iterator iterExp =  rolesExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiExtRoles roleExp = (SbiExtRoles)iterExp.next();
			    	SbiExtRoles roleExist = (SbiExtRoles)rolesAss.get(roleExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=roleExp.getName()%></span><br/>
					<%=roleExp.getDescr()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=roleExist.getName()%></span><br/>
					<%=roleExist.getDescr()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getLovIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">List of values</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported List of values</td>
				<td class='portlet-section-header'>Existing List of values</td>
			</tr>
			<%
				Map lovsAss = existMD.getLovAssociation();
				Set lovsExp = lovsAss.keySet();
			    Iterator iterExp =  lovsExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiLov lovExp = (SbiLov)iterExp.next();
			    	SbiLov lovExist = (SbiLov)lovsAss.get(lovExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=lovExp.getLabel()%></span><br/>
					<%=lovExp.getName()%><br/>
					<%=lovExp.getDescr()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=lovExist.getLabel()%></span><br/>
					<%=lovExist.getName()%><br/>
					<%=lovExist.getDescr()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getFunctIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Functionalities</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported Functionalities</td>
				<td class='portlet-section-header'>Existing Functionalities</td>
			</tr>
			<%
				Map functsAss = existMD.getFunctAssociation();
				Set functsExp = functsAss.keySet();
			    Iterator iterExp =  functsExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiFunctions functExp = (SbiFunctions)iterExp.next();
			    	SbiFunctions functExist = (SbiFunctions)functsAss.get(functExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=functExp.getCode()%></span><br/>
					<%=functExp.getName()%><br/>
					<%=functExp.getDescr()%><br/>
					<%=functExp.getPath()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=functExist.getCode()%></span><br/>
					<%=functExist.getName()%><br/>
					<%=functExist.getDescr()%><br/>
					<%=functExist.getPath()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getEngineIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Engines</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported Engines</td>
				<td class='portlet-section-header'>Existing Engines</td>
			</tr>
			<%
				Map engsAss = existMD.getEngineAssociation();
				Set engsExp = engsAss.keySet();
			    Iterator iterExp =  engsExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiEngines engExp = (SbiEngines)iterExp.next();
			    	SbiEngines engExist = (SbiEngines)engsAss.get(engExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=engExp.getLabel()%></span><br/>
					<%=engExp.getName()%><br/>
					<%=engExp.getDescr()%><br/>
					<%=engExp.getMainUrl()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=engExist.getLabel()%></span><br/>
					<%=engExist.getName()%><br/>
					<%=engExist.getDescr()%><br/>
					<%=engExist.getMainUrl()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getCheckIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Checks</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported Checks</td>
				<td class='portlet-section-header'>Existing Checks</td>
			</tr>
			<%
				Map checksAss = existMD.getCheckAssociation();
				Set checksExp = checksAss.keySet();
			    Iterator iterExp =  checksExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiChecks checkExp = (SbiChecks)iterExp.next();
			    	SbiChecks checkExist = (SbiChecks)checksAss.get(checkExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=checkExp.getLabel()%></span><br/>
					<%=checkExp.getName()%><br/>
					<%=checkExp.getDescr()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=checkExist.getLabel()%></span><br/>
					<%=checkExist.getName()%><br/>
					<%=checkExist.getDescr()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getParameterIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Parameters</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported Parameters</td>
				<td class='portlet-section-header'>Existing Parameters</td>
			</tr>
			<%
				Map paramsAss = existMD.getParameterAssociation();
				Set paramsExp = paramsAss.keySet();
			    Iterator iterExp =  paramsExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiParameters paramExp = (SbiParameters)iterExp.next();
			    	SbiParameters paramExist = (SbiParameters)paramsAss.get(paramExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=paramExp.getLabel()%></span><br/>
					<%=paramExp.getName()%><br/>
					<%=paramExp.getDescr()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=paramExist.getLabel()%></span><br/>
					<%=paramExist.getName()%><br/>
					<%=paramExist.getDescr()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getParuseIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Parameter uses</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported Parameter uses</td>
				<td class='portlet-section-header'>Existing Parameter uses</td>
			</tr>
			<%
				Map parusesAss = existMD.getParuseAssociation();
				Set parusesExp = parusesAss.keySet();
			    Iterator iterExp =  parusesExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiParuse paruseExp = (SbiParuse)iterExp.next();
			    	SbiParuse paruseExist = (SbiParuse)parusesAss.get(paruseExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=paruseExp.getLabel()%></span><br/>
					<%=paruseExp.getName()%><br/>
					<%=paruseExp.getDescr()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=paruseExist.getLabel()%></span><br/>
					<%=paruseExist.getName()%><br/>
					<%=paruseExist.getDescr()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		<%if(!existMD.getBIobjIDAssociation().keySet().isEmpty()) { %>
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header' colspan="2">Objects</td>
			</tr>
			<tr>
				<td class='portlet-section-header'>Exported object</td>
				<td class='portlet-section-header'>Existing objects</td>
			</tr>
			<%
				Map biobjsAss = existMD.getBIObjAssociation();
				Set biobjsExp = biobjsAss.keySet();
			    Iterator iterExp =  biobjsExp.iterator();
			    while(iterExp.hasNext()) {
			    	SbiObjects biobjExp = (SbiObjects)iterExp.next();
			    	SbiObjects biobjExist = (SbiObjects)biobjsAss.get(biobjExp);
			%>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=biobjExp.getLabel()%></span><br/>
					<%=biobjExp.getName()%><br/>
					<%=biobjExp.getDescr()%><br/>
				</td>
				<td>
					<span class='portlet-form-field-label'><%=biobjExist.getLabel()%></span><br/>
					<%=biobjExist.getName()%><br/>
					<%=biobjExist.getDescr()%><br/>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%
			    }
			%>
		</table>
		<% } %>
		
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











