<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spago.navigation.LightNavigationManager" %>

<% 
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("TreeObjectsModule"); 
   Object viewObj = moduleResponse.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
   String view = SpagoBIConstants.VIEW_OBJECTS_AS_LIST;
   if(viewObj!=null) {
   		view = (String)viewObj;
   }
   
   Object listPageObj = moduleResponse.getAttribute(SpagoBIConstants.LIST_PAGE);
   String listPage = "1";
   if(listPageObj!=null) {
   		listPage = (String)listPageObj;
   }
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("ACTION_NAME", "START_ACTION");
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL viewListUrl = renderResponse.createActionURL();
   viewListUrl.setParameter("PAGE", "TreeObjectsPage");
   viewListUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
   viewListUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);

   PortletURL viewTreeUrl = renderResponse.createActionURL();
   viewTreeUrl.setParameter("PAGE", "TreeObjectsPage");
   viewTreeUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
   viewTreeUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_TREE);

%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>
				<spagobi:message key = "SBISet.objects.titleList" />
			<%   } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
				<spagobi:message key = "SBISet.objects.titleTree"/>
			<%   }   %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
				<a href='<%= viewListUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.objects.listViewButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' alt='<spagobi:message key = "SBISet.objects.listViewButt" />' /> 
				</a>
			<% } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) { 	 %>
				<a href='<%= viewTreeUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.objects.treeViewButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/treeView.png")%>' alt='<spagobi:message key = "SBISet.objects.treeViewButt" />' /> 
				</a>
			<% } %>			
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.objects.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.objects.backButt" />' />
			</a>
		</td>
	</tr>
</table>


    <% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
	<div class="div_background">
		<spagobi:treeObjects moduleName="TreeObjectsModule"  htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator" />
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
	</div>	
    <% } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>
    	<spagobi:listObjects moduleName="TreeObjectsModule"  
        	                 htmlGeneratorClass="it.eng.spagobi.presentation.listobjectshtmlgenerators.ListObjectsHtmlGeneratorAdminImpl"  
            	             listTransformerClass="it.eng.spagobi.presentation.listobjectshtmlgenerators.ListObjectsTransformerTreeImpl" 
            	             listPage="<%= listPage %>" />
		<!-- br/-->
	<% } %>












