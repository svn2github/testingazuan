<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.services.modules.DetailBIObjectModule,
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
   backUrl.setParameter("PUBLISHER_NAME", "LoginSBIDevelopmentContextPublisher");
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL addUrl = renderResponse.createActionURL();
   addUrl.setParameter("PAGE", DetailBIObjectModule.MODULE_PAGE);
   addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_NEW);
   addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
  
   PortletURL viewListUrl = renderResponse.createActionURL();
   viewListUrl.setParameter("PAGE", "TreeObjectsPage");
   viewListUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
   viewListUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);

   PortletURL viewTreeUrl = renderResponse.createActionURL();
   viewTreeUrl.setParameter("PAGE", "TreeObjectsPage");
   viewTreeUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
   viewTreeUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_TREE);    
%>

<%--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'>
			<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>
			   		<spagobi:message key = "SBISet.devObjects.titleList"/>
			<%   } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
			   		<spagobi:message key = "SBISet.devObjects.titleTree"/>
			<%   }
			%>
		</th>
	</tr>
</table--%>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>
				<spagobi:message key = "SBISet.devObjects.titleList" />
			<%   } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
				<spagobi:message key = "SBISet.devObjects.titleTree"/>
			<%   }   %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= addUrl.toString() %>'> 
      			<img title='<spagobi:message key = "SBISet.devObjects.newObjButt" />' width='25px' height='25px' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/new.png")%>' alt='<spagobi:message key = "SBISet.devObjects.newObjButt" />' />
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
				<a href='<%= viewListUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.devObjects.listViewButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' alt='<spagobi:message key = "SBISet.devObjects.listViewButt" />' /> 
				</a>
			<% } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) { 	 %>
				<a href='<%= viewTreeUrl.toString() %>'> 
      				<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.devObjects.treeViewButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/treeView.png")%>' alt='<spagobi:message key = "SBISet.devObjects.treeViewButt" />' /> 
				</a>
			<% } %>			
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBISet.devObjects.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBISet.devObjects.backButt" />' />
			</a>
		</td>
	</tr>
</table>

<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
		<spagobi:treeObjects moduleName="TreeObjectsModule"  htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.DevTreeHtmlGenerator" />
		<br/>
<% } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) {  %>
    	<spagobi:listObjects moduleName="TreeObjectsModule"  
        	                 htmlGeneratorClass="it.eng.spagobi.presentation.listobjectshtmlgenerators.ListObjectsHtmlGeneratorDevImpl"  
            	             listTransformerClass="it.eng.spagobi.presentation.listobjectshtmlgenerators.ListObjectsTransformerTreeImpl" 
            	             listPage="<%= listPage %>" />
		<!--br/-->
<% } %>

<%--div style="width:100%;">
	<table width="100%">
       	<tr>
       	    <td>&nbsp;</td>
       	    <td width="120px" align='center'>
			    <a href='<%= addUrl.toString() %>' class='portlet-menu-item' >
    				<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/new.png")%>' alt='Back' />
				</a>
				<br/>
				<a href='<%= addUrl.toString() %>'> 
					<spagobi:message key = "SBISet.devObjects.newObjButt"/>
				</a>
			</td>
			<td width="15px">&nbsp;</td>
			<td width="80px" align='center'>
			    <a href='<%= backUrl.toString() %>' class='portlet-menu-item' >
    				<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
				</a>
				<br/>
				<a href='<%= backUrl.toString() %>'> 
					<spagobi:message key = "SBISet.devObjects.backButt"/>
				</a>
			</td>
			
			
			<td width="15px">&nbsp;</td>
			<% if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)) { %>
		    	
		    	<td width="130px" align='center'>
			    	<a href='<%= viewListUrl.toString() %>' class='portlet-menu-item' >
    					<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/listView.png")%>' alt='Back' />
					</a> 
					<br/>
					<a href='<%= viewListUrl.toString() %>'>
						<spagobi:message key = "SBISet.devObjects.listViewButt"/>
					</a>
				</td>
				
    		<% } else if(view.equals(SpagoBIConstants.VIEW_OBJECTS_AS_LIST)) { 	 %>
    			
    			<td width="130px" align='center'>
			    	<a href='<%= viewTreeUrl.toString() %>' class='portlet-menu-item' >
    					<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/treeView.png")%>' alt='Back' />
					</a> 
					<br/>
					<a href='<%= viewTreeUrl.toString() %>'>
						<spagobi:message key = "SBISet.devObjects.treeViewButt"/>
					</a>
				</td>
				
			<% } %>
			<td>&nbsp;</td>	
       	</tr>
    </table>
</div--%>

<!--br/><br/-->







