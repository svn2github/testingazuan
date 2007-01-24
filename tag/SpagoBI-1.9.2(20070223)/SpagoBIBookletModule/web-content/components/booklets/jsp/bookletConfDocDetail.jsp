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
				java.util.Map,
				java.util.Set" %>

<%
   SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(BookletsConstants.BOOKLET_MANAGEMENT_MODULE); 
   Map parnamemap = (Map)moduleResponse.getAttribute("parnamemap");
   Map parvaluemap = (Map)moduleResponse.getAttribute("parvaluemap");
   String description = (String)moduleResponse.getAttribute("description");
   String label = (String)moduleResponse.getAttribute("label");
   String name = (String)moduleResponse.getAttribute("name");
   Integer idobj = (Integer)moduleResponse.getAttribute("idobj");
   String pathBookConf = (String)moduleResponse.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
   String logicalname = (String)moduleResponse.getAttribute("logicalname");
   if(logicalname==null)
	   logicalname = "";
   
   PortletURL backUrl = renderResponse.createActionURL();
   backUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   backUrl.setParameter(SpagoBIConstants.OPERATION, BookletsConstants.OPERATION_DETAIL_BOOKLET);
   backUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   
   PortletURL formSaveConfDocUrl = renderResponse.createActionURL();
   formSaveConfDocUrl.setParameter("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formSaveConfDocUrl.setParameter("OPERATION", BookletsConstants.OPERATION_SAVE_CONFIGURED_DOCUMENT);
   formSaveConfDocUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
%>




<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
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
			<a href="javascript:document.getElementById('saveForm').submit();"> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/save32.png")%>' 
      				 alt='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' />
			</a>
		</td>
	</tr>
</table>



	
	
<form action="<%=formSaveConfDocUrl.toString()%>" method='POST' id='saveForm' name='saveForm'>	
	
	<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="width:100%;">	
		<spagobi:message key="book.dataObject" bundle="component_booklets_messages" />
	</div>
	
	<input name="<%=BookletsConstants.PATH_BOOKLET_CONF%>" type="hidden" value="<%=pathBookConf%>"/>
	<input name="idbiobject" type="hidden" value="<%=idobj%>"/>
	
	<br/> 
	
	<div class="div_detail_area_forms" >
		<table style="margin:10px;">
			<tr>
				<td class='portlet-form-field-label' width="130px">
						<spagobi:message key="book.nameObject" bundle="component_booklets_messages" />
				</td>
				<td style="font-size:11px;"><%=name %></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label' width="130px">
					<spagobi:message key="book.descrObject" bundle="component_booklets_messages" />
				</td>
				<td style="font-size:11px;"><%=description %></td>
			</tr>
			<tr>
				<td class='portlet-form-field-label' width="130px">
					<spagobi:message key="book.labelObject" bundle="component_booklets_messages" />
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
						<spagobi:message key="book.logNameObject" bundle="component_booklets_messages" />
				</td>
				<td style="font-size:5;">
					<input type="text" size="30" name="logicalname" value="<%=logicalname%>" <%=readonlyLogicalName %> />
				</td>
			</tr>
		</table>
	</div>



    <spagobi:error/>

	
	<div style='padding-top:10px;margin-right:5px;' class='portlet-section-header' style="width:100%;">	
		<spagobi:message key="book.parametersObject" bundle="component_booklets_messages" />
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
				<td class='portlet-form-field-label' width="160px"><%=parName%>:</td>
				<td><input type="text" size="30" name="<%=urlName%>" value="<%=value%>" <%=readonly%> /></td>
			</tr>
		<% 
			}
			if(!findOutPar){
		%>
			<tr>
				<td class='portlet-form-field-label' width="160px">Output:</td>
				<td><input type="text" size="30" name="param_output_format" value="JPGBASE64" readonly /></td>
			</tr>
			<!-- 
			<input type="hidden" size="30" name="param_output_format" value="JPGBASE64" />
			-->
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















