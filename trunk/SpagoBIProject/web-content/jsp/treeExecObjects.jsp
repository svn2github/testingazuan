<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.DetailBIObjectModule,
                 it.eng.spago.navigation.LightNavigationManager" %>


<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("TreeObjectsModule");
	Object viewObj = moduleResponse.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
	String view = SpagoBIConstants.VIEW_OBJECTS_AS_TREE;
	if (viewObj != null) {
   		view = (String) viewObj;
	}
   
	Object listPageObj = moduleResponse.getAttribute(SpagoBIConstants.LIST_PAGE);
	String listPage = "1";
	if (listPageObj != null) {
   		listPage = (String) listPageObj;
	}
	
	String pageName = (String) aServiceRequest.getAttribute("PAGE");
	  
	PortletURL viewListUrl = renderResponse.createActionURL();
	viewListUrl.setParameter("PAGE", pageName);
	viewListUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
	viewListUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);

	PortletURL viewTreeUrl = renderResponse.createActionURL();
	viewTreeUrl.setParameter("PAGE", pageName);
	viewTreeUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
	viewTreeUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_TREE);  
	
	
%>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<% if (view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>
				<spagobi:message key = "SBISet.exeObjects.titleList" />
			<%   } else if (view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
				<spagobi:message key = "SBISet.exeObjects.titleTree" />
			<%   }   %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
				<td class='header-button-column-portlet-section'>
			<% if (view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
				<a href='<%= viewListUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' 
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' 
					title='<spagobi:message key = "SBISet.exeObjects.listViewButt" />' 
					alt='<spagobi:message key = "SBISet.exeObjects.listViewButt" />' /> 
				</a>
			<% } else if (view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) { 	 %>
				<a href='<%= viewTreeUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' 
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/treeView.png")%>'
					title='<spagobi:message key = "SBISet.exeObjects.treeViewButt" />'  
					alt='<spagobi:message key = "SBISet.exeObjects.treeViewButt" />' /> 
				</a>
			<% } %>			
		</td>
	</tr>
</table>


<% if (view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>

		<spagobi:treeObjects moduleName="TreeObjectsModule"  
			htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.ExecTreeHtmlGenerator" />
			
<% } else if (view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>

    	<spagobi:listObjects moduleName="TreeObjectsModule"  
		htmlGeneratorClass="it.eng.spagobi.presentation.listobjectshtmlgenerators.ListObjectsHtmlGeneratorExecImpl"  
		listTransformerClass="it.eng.spagobi.presentation.listobjectshtmlgenerators.ListObjectsTransformerTreeImpl" 
		listPage="<%= listPage %>" />
		
<% } %>








