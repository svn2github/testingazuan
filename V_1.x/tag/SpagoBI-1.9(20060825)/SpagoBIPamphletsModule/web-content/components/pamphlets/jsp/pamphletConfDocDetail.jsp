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
				java.util.Map,
				java.util.Set" %>

<%  
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(PamphletsConstants.PAMPHLET_MANAGEMENT_MODULE); 
   //List roleList = (List)moduleResponse.getAttribute(PamphletsConstants.ROLE_LIST);
   Map parnamemap = (Map)moduleResponse.getAttribute("parnamemap");
   Map parvaluemap = (Map)moduleResponse.getAttribute("parvaluemap");
   String description = (String)moduleResponse.getAttribute("description");
   String label = (String)moduleResponse.getAttribute("label");
   String name = (String)moduleResponse.getAttribute("name");
   Integer idobj = (Integer)moduleResponse.getAttribute("idobj");
   String pathPamp = (String)moduleResponse.getAttribute("PATHPAMPHLET");
   String logicalname = (String)moduleResponse.getAttribute("logicalname");
   if(logicalname==null)
	   logicalname = "";
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   backUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_DETAIL_PAMPHLET);
	 backUrl.setParameter(PamphletsConstants.PATH_PAMPHLET, pathPamp);
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
   PortletURL formSaveConfDocUrl = renderResponse.createActionURL();
   formSaveConfDocUrl.setParameter("PAGE", PamphletsConstants.PAMPHLET_MANAGEMENT_PAGE);
   formSaveConfDocUrl.setParameter("OPERATION", PamphletsConstants.OPERATION_SAVE_CONFIGURED_DOCUMENT);
   formSaveConfDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
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
			<a href="javascript:document.getElementById('saveForm').submit();"> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "pamp.save" bundle="component_pamphlets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/pamphlets/img/save32.png")%>' 
      				 alt='<spagobi:message key = "pamp.save" bundle="component_pamphlets_messages" />' />
			</a>
		</td>
	</tr>
</table>



	
	
<form action="<%=formSaveConfDocUrl.toString()%>" method='POST' id='saveForm' name='saveForm'>	
	
	<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="width:100%;">	
		<spagobi:message key="pamp.dataObject" bundle="component_pamphlets_messages" />
	</div>
	
	<input name="pathpamphlet" type="hidden" value="<%=pathPamp%>"/>
	<input name="idbiobject" type="hidden" value="<%=idobj%>"/>
	
	<br/> 
	
	<div class="div_detail_area_forms" >
		<table style="margin:10px;">
			<tr>
				<td class='portlet-form-field-label' width="130px">
						<spagobi:message key="pamp.nameObject" bundle="component_pamphlets_messages" />
				</td>
				<td style="font-size:11px;"><%=name %></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label' width="130px">
					<spagobi:message key="pamp.descrObject" bundle="component_pamphlets_messages" />
				</td>
				<td style="font-size:11px;"><%=description %></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label' width="130px">
					<spagobi:message key="pamp.labelObject" bundle="component_pamphlets_messages" />
				</td>
				<td style="font-size:11px;"><%=label %></td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<%
				String readonlyLogicalName = " ";
				if(!logicalname.trim().equals("")) {
					readonlyLogicalName = " readonly ";
				}
			%>
			<tr>
				<td class='portlet-form-field-label' width="130px">
						<spagobi:message key="pamp.logNameObject" bundle="component_pamphlets_messages" />
				</td>
				<td style="font-size:5;">
					<input type="text" size="30" name="logicalname" value="<%=logicalname%>" <%=readonlyLogicalName %> />
				</td>
			</tr>
		</table>
	</div>



  

	
	<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="width:100%;">	
		<spagobi:message key="pamp.parametersObject" bundle="component_pamphlets_messages" />
	</div>
	
	<br/>
	
	<div class="div_detail_area_forms" >
		<table style="margin:10px;">
		<%
			Set names = parnamemap.keySet();
			Iterator iterParName = names.iterator();
			boolean findOutPar = false;
			while(iterParName.hasNext()){
				String parName = (String)iterParName.next();
				String urlName = (String)parnamemap.get(parName);
				String value = (String)parvaluemap.get(urlName);
				String readonly=" ";
				if(urlName.equalsIgnoreCase("param_output_format")){
					value="JPGBASE64";
					readonly = " readonly ";
					findOutPar = true;
				}
				
		%>
			<tr>
				<td class='portlet-form-field-label' width="130px"><%=parName%>:</td>
				<td><input type="text" size="30" name="<%=urlName%>" value="<%=value%>" <%=readonly%> /></td>
			</tr>
		<% 
			}
			if(!findOutPar){
		%>
			<input type="hidden" size="30" name="param_output_format" value="JPGBASE64" />
		<%		
			}
		%>
		
		
		
		</table>
	</div>

<br/>
</br>




<%-- 

SHOW ROLES ROLE ASSIGNMENT TO DOCUMENT HAVE BEEN REPLACED BY THE WORKFLOW

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


<br/>
</form>















