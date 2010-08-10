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

<%@ page import="it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.booklets.constants.BookletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.booklets.bo.ConfiguredBIDocument,
				it.eng.spagobi.constants.SpagoBIConstants,
				it.eng.spagobi.booklets.bo.WorkflowConfiguration" %>
<%@page import="it.eng.spagobi.booklets.utils.BookletServiceUtils"%>
<%@page import="it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl"%>
<%@page import="it.eng.spagobi.bo.dao.IBIObjectCMSDAO"%>
<%@page import="it.eng.spagobi.booklets.dao.IBookletsCmsDao"%>
<%@page import="it.eng.spagobi.bo.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.bo.BIObject"%>
<%@page import="it.eng.spagobi.services.modules.DetailBIObjectModule"%>
<%@page import="it.eng.spagobi.constants.ObjectsTreeConstants"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page import="it.eng.spagobi.utilities.ChannelUtilities"%>

<%
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(BookletsConstants.BOOKLET_MANAGEMENT_MODULE); 
   List confDocList = (List)moduleResponse.getAttribute(BookletsConstants.CONFIGURED_DOCUMENT_LIST);
   String pathConfNode = (String)moduleResponse.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
   String templateOOFileName = (String)moduleResponse.getAttribute(BookletsConstants.OO_TEMPLATE_FILENAME);
   String wfProcDefFileName = (String)moduleResponse.getAttribute(BookletsConstants.WF_PROCESS_DEFINTIION_FILENAME);
   
    // load the biobject
   IBookletsCmsDao bookletsCmsDao = new BookletsCmsDaoImpl();
   String biObjectPath = bookletsCmsDao.getBiobjectPath(pathConfNode);
   BIObject biobject = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(biObjectPath);
   // get the biobject id
   String idBiObjStr = biobject.getId().toString();
   // get the actor from session
   String actor = (String)aSessionContainer.getAttribute(SpagoBIConstants.ACTOR);
   
   Iterator iterDoc = confDocList.iterator();
   
   Map backUrlPars = new HashMap();
   backUrlPars.put("PAGE", DetailBIObjectModule.MODULE_PAGE);
   backUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.DETAIL_SELECT);
   backUrlPars.put(ObjectsTreeConstants.OBJECT_ID, idBiObjStr);
   backUrlPars.put(SpagoBIConstants.ACTOR, actor);
   backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
   String backUrl = urlBuilder.getUrl(request, backUrlPars);
   
   Map formDetailUrlPars = new HashMap();
   formDetailUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formDetailUrlPars.put("OPERATION", BookletsConstants.OPERATION_DETAIL_CONFIGURED_DOCUMENT);
   formDetailUrlPars.put(BookletsConstants.PATH_BOOKLET_CONF, pathConfNode);
   formDetailUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String formDetailUrl = urlBuilder.getUrl(request, formDetailUrlPars);
   
   Map formEraseUrlPars = new HashMap();
   formEraseUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formEraseUrlPars.put("OPERATION", BookletsConstants.OPERATION_DELETE_CONFIGURED_DOCUMENT);
   formEraseUrlPars.put(BookletsConstants.PATH_BOOKLET_CONF, pathConfNode);
   formEraseUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String formEraseUrl = urlBuilder.getUrl(request, formEraseUrlPars);
   
   Map saveUrlPars = new HashMap();
   if(ChannelUtilities.isPortletRunning()) {
		saveUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
    saveUrlPars.put("OPERATION", BookletsConstants.OPERATION_SAVE_DETAIL_BOOKLET);
    saveUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	}
   //saveUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   //saveUrlPars.put("OPERATION", BookletsConstants.OPERATION_SAVE_DETAIL_BOOKLET);
   //saveUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String saveUrl = urlBuilder.getUrl(request, saveUrlPars);
   
   Map saveVersionUrlPars = new HashMap();
   saveVersionUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   saveVersionUrlPars.put("OPERATION", BookletsConstants.OPERATION_SAVE_NEW_VERSION_BOOKLET);
   saveVersionUrlPars.put(BookletsConstants.PATH_BOOKLET_CONF, pathConfNode);
   saveVersionUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String saveVersionUrl = urlBuilder.getUrl(request, saveVersionUrlPars);
   
   Map formNewConfDocUrlPars = new HashMap();
   formNewConfDocUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formNewConfDocUrlPars.put("OPERATION", BookletsConstants.OPERATION_NEW_CONFIGURED_DOCUMENT);
   formNewConfDocUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String formNewConfDocUrl = urlBuilder.getUrl(request, formNewConfDocUrlPars);
   
   
%>




	<!-- ********************* TITOLO **************************  -->


<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key="book.ConfTemp" bundle="component_booklets_messages" />
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%= backUrl %>'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
	      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/back.png")%>' 
	      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:document.getElementById('saveForm').submit()"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
	      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/save32.jpg")%>' 
	      				 alt='<spagobi:message key = "book.save"  bundle="component_booklets_messages"/>' />
				</a>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href="<%=saveVersionUrl %>"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "book.saveVersion" bundle="component_booklets_messages" />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/components/booklets/img/saveVersion32.jpg")%>' 
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
						<a href='<%=formDetailUrl + "&configureddocumentidentifier=" + confDoc.getLogicalName() %>' >
						<img 	title='<spagobi:message key = "book.detail" bundle="component_booklets_messages" />' 
      				 		src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/detail.gif")%>' 
      				 		alt='<spagobi:message key = "book.detail"  bundle="component_booklets_messages"/>' />
      				 	</a>
					</td>
					<td  width="20">
					    <a href='<%=formEraseUrl + "&configureddocumentidentifier=" + confDoc.getLogicalName() %>' >
						<img 	title='<spagobi:message key = "book.erase" bundle="component_booklets_messages" />' 
      				 		src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/erase.gif")%>' 
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

		<form action="<%=saveUrl%>" method='POST' id='saveForm' name='saveForm' enctype="multipart/form-data">
			<input type="hidden" name="<%=BookletsConstants.PATH_BOOKLET_CONF %>"  value="<%=pathConfNode%>"/>
			<input type="hidden" name="OPERATION"  value="<%=BookletsConstants.OPERATION_SAVE_DETAIL_BOOKLET%>"/>

	<% if(ChannelUtilities.isWebRunning()) { %>
		<input type='hidden' name='PAGE' value='<%=BookletsConstants.BOOKLET_MANAGEMENT_PAGE%>' />
		<input type='hidden' name='OPERATION' value='<%=BookletsConstants.OPERATION_SAVE_DETAIL_BOOKLET%>' />
		<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />
	<% } %>
		
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
		    <span style='font-size:11px;font-family:verdana;'>
			     <spagobi:message key="book.templatenotloaded" bundle="component_booklets_messages"/>  
		    </span>
		<% 	} else { 
		    out.print("<span style='font-size:11px;font-family:verdana;'>"+templateOOFileName+"</span>");
				String downOOTemplateUrl = BookletServiceUtils.getBookletServiceUrl(request) + "?" + 
						                   BookletsConstants.BOOKLET_SERVICE_TASK + "=" + 
						                   BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_OOTEMPLATE + "&" +
										   BookletsConstants.PATH_BOOKLET_CONF + "=" + pathConfNode;				   
		%>
			&nbsp;&nbsp;&nbsp;
			<a style='text-decoration:none;' href='<%=downOOTemplateUrl%>' target="iframeForDownload">
				<img title='<spagobi:message key="book.download" bundle="component_booklets_messages" />' 
					 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/download16.gif")%>' 
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
		    <span style='font-size:11px;font-family:verdana;'>
			     <spagobi:message key="book.WFprocessDefFileNotloaded" bundle="component_booklets_messages"/> 
		    </span>
    <% 	} else { 
				out.print("<span style='font-size:11px;font-family:verdana;'>"+wfProcDefFileName+"</span>");
				String downWorkDefUrl = BookletServiceUtils.getBookletServiceUrl(request) + "?" + 
            							BookletsConstants.BOOKLET_SERVICE_TASK + "=" + 
            							BookletsConstants.BOOKLET_SERVICE_TASK_DOWN_WORKFLOW_DEFINITION + "&" +
			   							BookletsConstants.PATH_BOOKLET_CONF + "=" + pathConfNode;	
		%>
		
			&nbsp;&nbsp;&nbsp;
			<a style='text-decoration:none;' href='<%=downWorkDefUrl%>' target="iframeForDownload">
				<img title='<spagobi:message key="book.download" bundle="component_booklets_messages" />' 
					 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/download16.gif")%>' 
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
		<form action="<%=formNewConfDocUrl%>" method='POST' id='newForm' name='newForm'>
		<input type="hidden" value="<%=pathConfNode%>" name="<%=BookletsConstants.PATH_BOOKLET_CONF%>" />
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header'>	
				<div style='width:90%;float:left;'>
						<spagobi:message key="book.addConfDoc" bundle="component_booklets_messages"/>
				</div>
				<div style="width:8%;float:left;">
					<input style="margin-left:10px;" type="image" 
								 title='<spagobi:message key="book.addDocument" bundle="component_booklets_messages" />' 
								 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/add.gif")%>' 
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
	<iframe name="iframeForDownload" src="" style="width:0px;height:0px;" ></iframe> 
</div>










