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
				it.eng.spagobi.pamphlets.constants.PamphletsConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.pamphlets.bo.Pamphlet,
				it.eng.spagobi.bo.Role,
				it.eng.spagobi.pamphlets.bo.ConfiguredBIDocument,
				it.eng.spagobi.constants.SpagoBIConstants,
				it.eng.spagobi.pamphlets.bo.WorkflowConfiguration" %>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(PamphletsConstants.PAMPHLET_MANAGEMENT_MODULE); 
   List roleList = (List)moduleResponse.getAttribute(PamphletsConstants.ROLE_LIST);
   List confDocList = (List)moduleResponse.getAttribute(PamphletsConstants.CONFIGURED_DOCUMENT_LIST);
   String pathPamphlet = (String)moduleResponse.getAttribute("PATH_PAMPHLET");
   String templateFileName = (String)moduleResponse.getAttribute("templatefilename");
   WorkflowConfiguration workConf = (WorkflowConfiguration)moduleResponse.getAttribute("workflowConfiguration");
   
   Iterator iterDoc = confDocList.iterator();
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   backUrl.setParameter("OPERATION", SpagoBIConstants.OPERATION_PAMPHLETS_VIEW_TREE);
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL formNewConfDocUrl = renderResponse.createActionURL();
   formNewConfDocUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   formNewConfDocUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_NEW_CONFIGURED_DOCUMENT);
   formNewConfDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  
   PortletURL formDetailUrl = renderResponse.createActionURL();
   formDetailUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   formDetailUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_DETAIL_CONFIGURED_DOCUMENT);
   formDetailUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pathPamphlet);
   formDetailUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL formEraseUrl = renderResponse.createActionURL();
   formEraseUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   formEraseUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_DELETE_CONFIGURED_DOCUMENT);
   formEraseUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pathPamphlet);
   formEraseUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL loadTemplateUrl = renderResponse.createActionURL();
   loadTemplateUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   loadTemplateUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_LOAD_TEMPLATE_PAMPHLET);
   loadTemplateUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL generateDocumentPartsUrl = renderResponse.createActionURL();
   generateDocumentPartsUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   generateDocumentPartsUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_GENERATE_DOCUMENT_PARTS);
   generateDocumentPartsUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pathPamphlet);
   generateDocumentPartsUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL saveWorkflowDataUrl = renderResponse.createActionURL();
   saveWorkflowDataUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   saveWorkflowDataUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_SAVE_WORKFLOWDATA);
   saveWorkflowDataUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pathPamphlet);
   saveWorkflowDataUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	 
%>




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key="SBISet.PamphletsManagement" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.back" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/back.png")%>' 
      				 alt='<spagobi:message key = "pamp.back"  bundle="component_pamphlets_messages"/>' />
			</a>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= generateDocumentPartsUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.generate" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/exec.png")%>' 
      				 alt='<spagobi:message key = "pamp.generate" bundle="component_pamphlets_messages" />' />
			</a>
		</td>
	</tr>
</table>

<br/>
	
	
	
	
	
	<div style="float:left;width:50%" class="div_detail_area_forms">
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="float:left;width:100%;">	
			<spagobi:message key="pamp.ConfDocList" bundle="component_pamphlets_messages"/>
		</div>
		<div style="clear:left;margin-bottom:10px;padding-top:10px;">
			<table style="width:98%;">
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
						<img 	title='<spagobi:message key = "pamp.detail" bundle="component_pamphlets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/detail.gif")%>' 
      				 		alt='<spagobi:message key = "pamp.detail"  bundle="component_pamphlets_messages"/>' />
      				 	</a>
					</td>
					<td  width="20">
					    <a href='<%=formEraseUrl.toString() + "&configureddocumentidentifier=" + confDoc.getLogicalName() %>' />
						<img 	title='<spagobi:message key = "pamp.erase" bundle="component_pamphlets_messages" />' 
      				 		src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/erase.gif")%>' 
      				 		alt='<spagobi:message key = "pamp.erase"  bundle="component_pamphlets_messages"/>' />
      				 	</a>
					</td>
				</tr> 
			<% 
				}
			%>
			</table>
		</div>
		
		
		
		
		
		<br/>
		
		
		
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="float:left;width:100%;">	
			<spagobi:message key="pamp.template" bundle="component_pamphlets_messages"/>
		</div>
		<br/>
		<span style='margin-top:5px;padding-top:5px;' class="portlet-form-field-label">
			<spagobi:message key="pamp.currenttemplate" bundle="component_pamphlets_messages"/>: 
		</span>
		&nbsp;&nbsp;&nbsp;
		<%=templateFileName %>
		<br/>
		<br/>
		<form action="<%=loadTemplateUrl.toString()%>" method='POST' id='loadTempForm' name='loadTempForm' enctype="multipart/form-data">
			<input type="hidden" name="<%=PamphletsConstants.PATH_PAMPHLET %>"  value="<%=pathPamphlet %>"/>
			<input size="30" type="file" name="templatefile" />
			&nbsp;&nbsp;&nbsp;
			<input type="image" 
						 title='<spagobi:message key="pamp.loadTemplate" bundle="component_pamphlets_messages" />' 
						 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/add.gif")%>' 
						 alt='<spagobi:message key="pamp.loadTemplate"  bundle="component_pamphlets_messages"/>' />
		</form>
		
		
 		<br/>
		
		
    
     		
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="float:left;width:100%;">	
			<form action="<%=saveWorkflowDataUrl.toString()%>" method='POST' id='workForm' name='workForm' >
			<div style='width:90%;float:left;'>
				<spagobi:message key="pamp.workflowData" bundle="component_pamphlets_messages"/>
			</div>
			<div style="width:8%;float:left;">
					<input style="margin-left:10px;" type="image" 
								 title='<spagobi:message key="pamp.save" bundle="component_pamphlets_messages" />' 
								 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/save22.gif")%>' 
								 alt='<spagobi:message key="pamp.save"  bundle="component_pamphlets_messages"/>' />
			</div>
		</div>
		<br/>
			<table cellspacing="10px">
				<tr>
					<td class='portlet-form-field-label' width="130px">
						<spagobi:message key="pamp.nameWorkPackage" bundle="component_pamphlets_messages" />
					</td>
					<td style="font-size:5;">
						<input size="30" type="text" name="nameWorkPackage" value="<%=workConf.getNameWorkflowPackage()%>" />
					</td>
				</tr>
				<tr style="height:10px;"><td colspan="2"><span></span></td></tr>
				<tr>
					<td class='portlet-form-field-label' width="130px">
						<spagobi:message key="pamp.nameWorkProcess" bundle="component_pamphlets_messages" />
					</td>
					<td style="font-size:5;">
						<input size="30" type="text" name="nameWorkProcess" value="<%=workConf.getNameWorkflowProcess()%>" />
					</td>
				</tr>
			</table>
		</form>

 		
		
		<br/>
		
		
	</div>









	<div style="float:left;width:45%" class="div_detail_area_forms">
		<form action="<%=formNewConfDocUrl.toString()%>" method='POST' id='newForm' name='newForm'>
		<input type="hidden" value="<%=pathPamphlet%>" name="PATHPAMPHLET" />
		<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header'>	
				<div style='width:90%;float:left;'>
						<spagobi:message key="pamp.addConfDoc" bundle="component_pamphlets_messages"/>
				</div>
				<div style="width:8%;float:left;">
					<input style="margin-left:10px;" type="image" 
								 title='<spagobi:message key="pamp.addDocument" bundle="component_pamphlets_messages" />' 
								 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/add.gif")%>' 
								 alt='<spagobi:message key="pamp.addDocument"  bundle="component_pamphlets_messages"/>' />
				</div>
		</div>
		<div>
			<spagobi:treeObjects moduleName="<%=PamphletsConstants.PAMPHLET_MANAGEMENT_MODULE%>"  
								 htmlGeneratorClass="it.eng.spagobi.pamphlets.treegenerators.DocumentsTreeHtmlGenerator" />
		    <br/>
		    <br/>
		</div>
		</form>
	</div>


	<div style="clear:left;">
		&nbsp;
	</div>

	

<br/>
</br>






<%-- 
<table width="100%" cellspacing="0" border="1" >
  	<tr height='1'>
  		<td>
  	    	<table width="100%">
  	    		<tr >
  	    			<td colspan="3" align="left" class='portlet-section-header'>
  	    				<spagobi:message key = "SBIDev.paramUse.valTab3" />
  	    			</td>
  	    		</tr>
  	    		<%   	    		    
  	    		   	int count = 1;
  	    		    int prog = 0; 
  	    		   	Iterator iterRole = roleList.iterator(); 
  	    		  	int numRoles = roleList.size();
  	    			while(iterRole.hasNext()) {     
  	    				Role role = (Role)iterRole.next();
                        if(count==1) {
                          out.print("<tr class='portlet-font'>");
                        }
  	    		 		out.print("<td class='portlet-section-body'>");
  	    		 		out.print("   <input type='checkbox' name='idExtRole' value='"+role.getId()+"'/>");
  	    		 		out.print(    role.getName());
  	    		 		out.print("</td>");
  	    		 		if((count < 3) && (prog==numRoles-1)){
  	    		 		  	int numcol = 3-count;
  	    		 		  	int num;
  	    		 		  	for (num = 0; num <numcol; num++){
  	    		 		  		out.print("<td class='portlet-section-body'>");
  	    		 		    	out.print("</td>");
  	    		 		  	}
  	    		 		  	out.print("</tr>");
  	    		 		} 
  	    		 		if( (count==3) || (prog==(numRoles-1)) ) {
  	    		 		 	out.print("</tr>");
  	    		 		 	count = 1;
  	    		 		} 
  	    		 		else {
  	    		 		 	count ++;
  	    		 		 }
  	    		  }
  	    		%>
  	    	</table> 
  		</td>
  	</tr>
</table>
--%>








