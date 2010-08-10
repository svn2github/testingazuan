<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.DetailBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager" %>


<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("TreeObjectsModule");
	
	String pageName = (String) aServiceRequest.getAttribute("PAGE");
	  
	PortletURL viewListUrl = renderResponse.createActionURL();
	viewListUrl.setParameter("PAGE", pageName);
	viewListUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
	viewListUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);
	
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.exeObjects.titleTree" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
				<td class='header-button-column-portlet-section'>
			<a href='<%= viewListUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
				src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' 
				title='<spagobi:message key = "SBISet.exeObjects.listViewButt" />' 
				alt='<spagobi:message key = "SBISet.exeObjects.listViewButt" />' /> 
			</a>		
		</td>
	</tr>
</table>


<div class="div_background">
	<spagobi:treeObjects moduleName="TreeObjectsModule"  
	    				 htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.ExecTreeHtmlGenerator" />
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
</div>








