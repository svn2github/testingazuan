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
				java.util.Map,
				java.util.Set" %>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="java.util.HashMap"%>

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
   
   Map backUrlPars = new HashMap();
   backUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   backUrlPars.put(SpagoBIConstants.OPERATION, BookletsConstants.OPERATION_DETAIL_BOOKLET);
   backUrlPars.put(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
   backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String backUrl = urlBuilder.getUrl(request, backUrlPars);
   
   Map formSaveConfDocUrlPars = new HashMap();
   formSaveConfDocUrlPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
   formSaveConfDocUrlPars.put("OPERATION", BookletsConstants.OPERATION_SAVE_CONFIGURED_DOCUMENT);
   formSaveConfDocUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   String formSaveConfDocUrl = urlBuilder.getUrl(request, formSaveConfDocUrlPars);
   
%>


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
			<a href="javascript:document.getElementById('saveForm').submit();"> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/save32.png")%>' 
      				 alt='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' />
			</a>
		</td>
	</tr>
</table>



	
	
<form action="<%=formSaveConfDocUrl%>" method='POST' id='saveForm' name='saveForm'>	
	
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

		<%		
			}
		%>
		
		
		
		</table>
	</div>

<br/>
</br>


<br/>
</form>















