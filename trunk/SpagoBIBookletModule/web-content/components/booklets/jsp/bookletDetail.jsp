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
				it.eng.spagobi.booklets.constants.BookletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.booklets.bo.ConfiguredBIDocument,
				it.eng.spagobi.constants.SpagoBIConstants,
				it.eng.spagobi.booklets.bo.WorkflowConfiguration" %>

<%
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(BookletsConstants.BOOKLET_MANAGEMENT_MODULE); 
   List confDocList = (List)moduleResponse.getAttribute(BookletsConstants.CONFIGURED_DOCUMENT_LIST);
   String pathConfNode = (String)moduleResponse.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
   String templateOOFileName = (String)moduleResponse.getAttribute(BookletsConstants.OO_TEMPLATE_FILENAME);
   String wfProcDefFileName = (String)moduleResponse.getAttribute(BookletsConstants.WF_PROCESS_DEFINTIION_FILENAME);
   //WorkflowConfiguration workConf = (WorkflowConfiguration)moduleResponse.getAttribute(BookletsConstants.WORKFLOW_CONFIGURATION);
   
    // load the biobject
   IBookletsCmsDao bookletsCmsDao = new BookletsCmsDaoImpl();
   String biObjectPath = bookletsCmsDao.getBiobjectPath(pathConfNode);
   BIObject biobject = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(biObjectPath);
   // get the biobject id
   String idBiObjStr = biobject.getId().toString();
   // get the actor from session
   String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
   
   Iterator iterDoc = confDocList.iterator();
   
   PortletURL backUrl = renderResponse.createActionURL();
   //backUrl.setParameter("LIGHT_NAVIGATOR_BACK_TO", "1");
   backUrl.setParameter("PAGE", DetailBIObjectModule.MODULE_PAGE);
   backUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.DETAIL_SELECT);
   backUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, idBiObjStr);
   backUrl.setParameter(SpagoBIConstants.ACTOR, actor);
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   
   
   PortletURL formDetailUrl = renderResponse.createActionURL();
   formDetailUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formDetailUrl.setParameter("OPERATION", BookletsConstants.OPERATION_DETAIL_CONFIGURED_DOCUMENT);
   formDetailUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfNode);
   formDetailUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL formEraseUrl = renderResponse.createActionURL();
   formEraseUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formEraseUrl.setParameter("OPERATION", BookletsConstants.OPERATION_DELETE_CONFIGURED_DOCUMENT);
   formEraseUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfNode);
   formEraseUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL saveUrl = renderResponse.createActionURL();
   saveUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   saveUrl.setParameter("OPERATION", BookletsConstants.OPERATION_SAVE_DETAIL_BOOKLET);
   saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL saveVersionUrl = renderResponse.createActionURL();
   saveVersionUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   saveVersionUrl.setParameter("OPERATION", BookletsConstants.OPERATION_SAVE_NEW_VERSION_BOOKLET);
   saveVersionUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfNode);
   saveVersionUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL formNewConfDocUrl = renderResponse.createActionURL();
   formNewConfDocUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formNewConfDocUrl.setParameter("OPERATION", BookletsConstants.OPERATION_NEW_CONFIGURED_DOCUMENT);
   formNewConfDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   //PortletURL loadTemplateUrl = renderResponse.createActionURL();
   //loadTemplateUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   //loadTemplateUrl.setParameter("OPERATION", BookletsConstants.OPERATION_LOAD_OOTEMPLATE_BOOKLET);
   //loadTemplateUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   //PortletURL saveWorkflowDataUrl = renderResponse.createActionURL();
   //saveWorkflowDataUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   //saveWorkflowDataUrl.setParameter("OPERATION", BookletsConstants.OPERATION_SAVE_WORKFLOWDATA);
   //saveWorkflowDataUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  
   
%>




	<!-- ********************* TITOLO **************************  -->

	<%@page import="it.eng.spagobi.booklets.utils.BookletServiceUtils"%>
<%@page import="it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl"%>
<%@page import="it.eng.spagobi.bo.dao.IBIObjectCMSDAO"%>
<%@page import="it.eng.spagobi.booklets.dao.IBookletsCmsDao"%>
<%@page import="it.eng.spagobi.bo.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.bo.BIObject"%>
<%@page import="it.eng.spagobi.services.modules.DetailBIObjectModule"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>
<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key="book.ConfTemp" bundle="component_booklets_messages" />
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%= backUrl.toString() %>'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/back.png")%>' 
	      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:document.getElementById('saveForm').submit()"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/save32.jpg")%>' 
	      				 alt='<spagobi:message key = "book.save"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href="<%=saveVersionUrl.toString() %>"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.saveVersion" bundle="component_booklets_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/saveVersion32.jpg")%>' 
	      				 alt='<spagobi:message key = "book.saveVersion"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
		</tr>
	</table>
	
	<br/>
	
	




	<!-- ********************* START LEFT DIV **************************  -->
	
	<div style="float:left;width:50%" class="div_detail_area_forms">
		
		
		
		
		
		
		<!-- ********************* LIST CONFIGURED DOCUMENT **************************  -->
		
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="float:left;width:100%;">	
			<spagobi:message key="book.ConfDocList" bundle="component_booklets_messages"/>
		</div>
		<div style="clear:left;margin-bottom:10px;padding-top:10px;">
			<table style="width:98%;">
			<%
				if(!iterDoc.hasNext()) {
			%>
				 <tr style="border:1px solid #eeeeee;">
					<td style="valign:middle;" class="portlet-form-field-label">
						<spagobi:message key = "book.noconfdocument" bundle="component_booklets_messages" />
					</td>
				</tr>
			<%
				}
			%>
			<%
						while(iterDoc.hasNext()) {
						ConfiguredBIDocument confDoc = (ConfiguredBIDocument)iterDoc.next();
			%>		
			  <tr style="border:1px solid #eeeeee;">
					<td style="valign:middle;" class="portlet-form-field-label">
							<%=confDoc.getLogicalName()%>&nbsp;&nbsp;&nbsp;(<%=confDoc.getName()%>)
					</td>
					<td width="20">
						<a href='<%=formDetailUrl.toString() + "&configureddocumentidentifier=" + confDoc.getLogicalName() %>' />
						<img 	title='<spagobi:message key = "book.detail" bundle="component_booklets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/detail.gif")%>' 
      				 		alt='<spagobi:message key = "book.detail"  bundle="component_booklets_messages"/>' />
      				 	</a>
					</td>
					<td  width="20">
					    <a href='<%=formEraseUrl.toString() + "&configureddocumentidentifier=" + confDoc.getLogicalName() %>' />
						<img 	title='<spagobi:message key = "book.erase" bundle="component_booklets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/erase.gif")%>' 
      				 		alt='<spagobi:message key = "book.erase"  bundle="component_booklets_messages"/>' />
      				 	</a>
					</td>
				</tr> 
			<%
 			}
 			%>
			</table>
		</div>
		
		
		
		
		
		<!-- ********************* TEMPLATE FORM **************************  -->

		<form action="<%=saveUrl.toString()%>" method='POST' id='saveForm' name='saveForm' enctype="multipart/form-data">
			<input type="hidden" name="<%=BookletsConstants.PATH_BOOKLET_CONF %>"  value="<%=pathConfNode%>"/>
		
		<br/>
				
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="float:left;width:100%;">	
			<spagobi:message key="book.template" bundle="component_booklets_messages"/>
		</div>
		<br/>
		<span style='margin-top:5px;padding-top:5px;' class="portlet-form-field-label">
			<spagobi:message key="book.currenttemplate" bundle="component_booklets_messages"/>: 
		</span>
		&nbsp;&nbsp;&nbsp;
		<% 
			if( (templateOOFileName==null) || templateOOFileName.trim().equals("")) {	
		%>
		    <span style='font:11px;font-family:verdana;'>
			     <spagobi:message key="book.templatenotloaded" bundle="component_booklets_messages"/>  
		    </span>
		<% 	} else { 
		    out.print("<span style='font:11px;font-family:verdana;'>"+templateOOFileName+"</span>");
				String downOOTemplateUrl = BookletServiceUtils.getBookletServiceUrl() + "?" + 
						                   BookletsConstants.BOOKLET_SERVICE_TASK + "=" + 
						                   BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_OOTEMPLATE + "&" +
										   BookletsConstants.PATH_BOOKLET_CONF + "=" + pathConfNode;				   
		%>
			&nbsp;&nbsp;&nbsp;
			<a href='<%=downOOTemplateUrl%>' target="iframeForDownload">
				<img title='<spagobi:message key="book.download" bundle="component_booklets_messages" />' 
					 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/download16.gif")%>' 
					 alt='<spagobi:message key="book.download"  bundle="component_booklets_messages"/>' />
			</a>
		<%
			}
		%>
		<br/>
		<br/>
			<input size="30" type="file" name="templatefile" />
 		<br/>
 		<br/>
		
		
  
  
  
  
  		<!-- ********************* WORKFLOW FORM **************************  -->
   
     	
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="float:left;width:100%;">	
			<div style='width:100%;float:left;'>
				<spagobi:message key="book.workflowData" bundle="component_booklets_messages"/>
			</div>
		</div>
		<br/>
		<span style='margin-top:5px;padding-top:5px;' class="portlet-form-field-label">
			<spagobi:message key="book.currentWFprocessDefFile" bundle="component_booklets_messages"/>: 
		</span>
		&nbsp;&nbsp;&nbsp;
		<% 
			if( (wfProcDefFileName==null) || wfProcDefFileName.trim().equals("")) {	
		%>
		    <span style='font:11px;font-family:verdana;'>
			     <spagobi:message key="book.WFprocessDefFileNotloaded" bundle="component_booklets_messages"/> 
		    </span>
    <% 	} else { 
				out.print("<span style='font:11px;font-family:verdana;'>"+wfProcDefFileName+"</span>");
				String downWorkDefUrl = BookletServiceUtils.getBookletServiceUrl() + "?" + 
            							BookletsConstants.BOOKLET_SERVICE_TASK + "=" + 
            							BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_WORKFLOW_DEFINITION + "&" +
			   							BookletsConstants.PATH_BOOKLET_CONF + "=" + pathConfNode;	
		%>
		
			&nbsp;&nbsp;&nbsp;
			<a href='<%=downWorkDefUrl%>' target="iframeForDownload">
				<img title='<spagobi:message key="book.download" bundle="component_booklets_messages" />' 
					 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/download16.gif")%>' 
				 	alt='<spagobi:message key="book.download"  bundle="component_booklets_messages"/>' />
			</a>
		
		<%
		}
		%>
		<br/>
		<br/>
			<input size="30" type="file" name="wfdefinitionfile" />
		<br/>
		<br/>
		
		</form>
		
	</div>







	<!-- ********************* START RIGHT DIV **************************  -->

	<div style="float:left;width:45%" class="div_detail_area_forms">
		<form action="<%=formNewConfDocUrl.toString()%>" method='POST' id='newForm' name='newForm'>
		<input type="hidden" value="<%=pathConfNode%>" name="<%=BookletsConstants.PATH_BOOKLET_CONF%>" />
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header'>	
				<div style='width:90%;float:left;'>
						<spagobi:message key="book.addConfDoc" bundle="component_booklets_messages"/>
				</div>
				<div style="width:8%;float:left;">
					<input style="margin-left:10px;" type="image" 
								 title='<spagobi:message key="book.addDocument" bundle="component_booklets_messages" />' 
								 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/add.gif")%>' 
								 alt='<spagobi:message key="book.addDocument"  bundle="component_booklets_messages"/>' />
				</div>
		</div>
		<div>
			<spagobi:treeObjects moduleName="<%=BookletsConstants.BOOKLET_MANAGEMENT_MODULE%>"  
								 htmlGeneratorClass="it.eng.spagobi.booklets.treegenerators.DocumentsTreeHtmlGenerator" />
		    <br/>
		    <br/>
		</div>
		</form>
	</div>


	<div style="clear:left;">
		&nbsp;
	</div>

	

<br/>

<div id="iframeForDownload" style="display:none;">
	<iframe name="iframeForDownload" src="" style="width:0px;height:0px;" /> 
</div>










