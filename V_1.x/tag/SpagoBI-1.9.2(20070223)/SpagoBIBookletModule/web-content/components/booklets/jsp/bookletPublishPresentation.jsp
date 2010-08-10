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
				it.eng.spagobi.booklets.constants.BookletsConstants" %>
<%@page import="it.eng.spago.base.SourceBean"%>
		
		
<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("BookletsCollaborationModule"); 
	String pathBookConf = (String)moduleResponse.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
	String presVerName = (String)moduleResponse.getAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME);

	String label = (String)moduleResponse.getAttribute("label");
	if(label==null) label="";
	String name = (String)moduleResponse.getAttribute("name");
	if(name==null) name="";
	String description = (String)moduleResponse.getAttribute("description");
	if(description==null) description="";
	String publishMessage = (String)moduleResponse.getAttribute("PublishMessage");
	
	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("LIGHT_NAVIGATOR_BACK_TO", "1");
	
   	PortletURL saveUrl = renderResponse.createActionURL();
   	saveUrl.setParameter("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
   	saveUrl.setParameter("OPERATION", BookletsConstants.OPERATION_PUBLISH_PRESENTATION);
   	saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   	saveUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
   	saveUrl.setParameter(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME, presVerName);
	
%>		
		
				

<form method='POST' action='<%=saveUrl.toString()%>' id='publishForm' name='publishForm' >


<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key="book.Execution" bundle="component_booklets_messages" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>		
		<td class='header-button-column-portlet-section'>
			<input type='image' class='header-button-image-portlet-section' 
      			   title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
      			   src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/save32.png")%>' 
      			   alt='<spagobi:message key = "book.save"  bundle="component_booklets_messages"/>' />
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
	</tr>
</table>



<div class='div_background_no_img' >


	
	<table width="100%" cellspacing="0" border="0" id = "fieldsTable" >
		<tr>
			<td>
				<div class="div_detail_area_forms">
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							<spagobi:message key = "SBIDev.docConf.docDet.labelField" />
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field' type="text" style='width:230px;' 
							   name="label" id="label" value="<%=label%>" maxlength="20">
						&nbsp;*
					</div>
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							<spagobi:message key = "SBIDev.docConf.docDet.nameField" />
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field' type="text" style='width:230px;' 
								name="name" id="name" value="<%=name%>" maxlength="40">
						&nbsp;*
					</div>
					<div class='div_detail_label'>
						<span class='portlet-form-field-label'>
							<spagobi:message key ="SBIDev.docConf.docDet.descriptionField" />
						</span>
					</div>
					<div class='div_detail_form'>
						<input class='portlet-form-input-field' style='width:230px;' type="text" 
 								name="description" id="description" value="<%=description%>" maxlength="160">
					</div>
				</div> 
				
				<%
					if((publishMessage!=null) && !publishMessage.trim().equals("") ){
				%>
				
					<div class="div_detail_area_forms">
						<span class='portlet-form-field-label'>
							<%=publishMessage%>
						</span>
					</div>
				
				<%
					}
				%>
				
				<spagobi:error/>
				
			</td>
			<!-- OPEN COLUMN WITH TREE FUNCTIONALITIES   -->	     
			<td width="60%">
				<div style='display:inline;' id='folderTree'>
					<spagobi:treeObjects moduleName="BookletsCollaborationModule"  
	 						 htmlGeneratorClass="it.eng.spagobi.presentation.treehtmlgenerators.FunctionalitiesTreeInsertObjectHtmlGenerator" />    	
				</div>
			</td>
      	</tr>
   </table>  

</div>

   <br/>
   <br/>
    	
</form>
    	
    	
    	
    	