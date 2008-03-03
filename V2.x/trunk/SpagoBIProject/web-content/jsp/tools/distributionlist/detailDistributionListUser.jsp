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
	<%@ include file="/jsp/commons/portlet_base.jsp"%>
	
	<%@ page         import="it.eng.spagobi.tools.distributionlist.bo.DistributionList,
							 it.eng.spagobi.tools.distributionlist.bo.Email,
	 				         it.eng.spago.navigation.LightNavigationManager,
	 				         java.util.Map,java.util.HashMap,java.util.List,
	 				         java.util.Iterator,
	 				         it.eng.spagobi.commons.bo.Domain,
	 				         it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects,
	 				         it.eng.spagobi.tools.distributionlist.service.DetailDistributionListUserModule" %>
	 				         
	<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
	
	<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailDistributionListUserModule"); 
		DistributionList dl = (DistributionList)moduleResponse.getAttribute("dlObj");
		List listDialects = (List) moduleResponse.getAttribute(DetailDistributionListUserModule.NAME_ATTR_LIST_DIALECTS);
		
		String modality = (String)moduleResponse.getAttribute("modality");
		String subMessageDet = ((String)moduleResponse.getAttribute("SUBMESSAGEDET")==null)?"":(String)moduleResponse.getAttribute("SUBMESSAGEDET");
		String msgWarningSave = msgBuilder.getMessage("8002", request);
		
		Map formUrlPars = new HashMap();
		if(ChannelUtilities.isPortletRunning()) {
			formUrlPars.put("PAGE", "DetailDistributionListUserPage");	
  			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
		}
		String formUrl = urlBuilder.getUrl(request, formUrlPars);
		
		Map backUrlPars = new HashMap();
		backUrlPars.put("PAGE", "ListDistributionListUserPage");
		backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		String backUrl = urlBuilder.getUrl(request, backUrlPars);		
	%>
	
	<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>

<form method='POST' action='<%=formUrl%>' id='dlForm' name='dlForm' >

	<% if(ChannelUtilities.isWebRunning()) { %>
		<input type='hidden' name='PAGE' value='DetailDistributionListUserPage' />
		<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />
	<% } %>

	<input type='hidden' value='<%=modality%>' name='MESSAGEDET' />	
	<input type='hidden' value='<%=subMessageDet%>' name='SUBMESSAGEDET' />
	<input type='hidden' value='<%=dl.getId()%>' name='id' />
	
	<table width="100%" cellspacing="0" border="0" class='header-table-portlet-section'>		
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "SBISet.ListDL.TitleDetail"  />
			</td>
		</tr>
	</table>
	
	<div class='div_background' style='padding-top:5px;padding-left:5px;'>
	
	<table width="100%" cellspacing="0" border="0" id = "fieldsTable" >
	<tr>
	  <td>
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDL.columnName" />
			</span>
		</div>
		<%
			  String name = dl.getName();
			   if((name==null) || (name.equalsIgnoreCase("null"))  ) {
				   name = "";
			   }
		%>
		<div class='div_detail_form'>
			<input class='portlet-form-input-field' type="text" 
				   name="NAME" size="50" value="<%=name%>" readonly maxlength="50" />
		</div>
		
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDL.columnDescr" />
			</span>
		</div>
		<div class='div_detail_form'>
		<%
			   String descr = dl.getDescr();
			   if((descr==null) || (descr.equalsIgnoreCase("null"))  ) {
			   	   descr = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="DESCR" 
				   size="50" value="<%= descr %>" readonly maxlength="160" />
		</div>
	
	</td><!-- CLOSE COLUMN WITH DATA FORM  -->
		
		
		<spagobi:error/>
	</tr>
	</table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->
	<br>
	<br>
		
	<!-- LIST OF DOCUMENTS RELATED TO A DISTRIBUTION LIST  -->
		<%
			List documents = dl.getDocuments();
			if(!documents.isEmpty()){
	%>		
	<table width="100%" cellspacing="0" border="0" id = "docTable" >
	
	<tr>
		<td>
 		<div class='div_detail_label' style='width:350px;'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDL.relatedDoc" />
			</span>
		</div>
		</td>
	</tr>
	<tr>
	  <td>
	  	 <div class='div_detail_label' style='width:250px;'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDL.columnDocName" />
			</span>
		</div>
	  	 <div class='div_detail_form'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDL.columnDocDescr" />
			</span>
		</div>
		
		<%
			Iterator it2 = documents.iterator();
			while(it2.hasNext()){
				
				BIObject bo = (BIObject)it2.next();
				String docName = bo.getName();
				String docDescr = bo.getDescription();
				if((docName==null) || (docName.equalsIgnoreCase("null"))  ) {
					docName = "";
				   }
				if((docDescr==null) || (docDescr.equalsIgnoreCase("null"))  ) {
					docDescr = "";
				   }
				
		 %>
				    	<div class='div_detail_label' style='width:250px;'><%=docName%>
				   		</div>
					
				    	<div class='div_detail_form'><%=docDescr %>
				   		</div>			
	<% } %>
	
		<spagobi:error/>
			</td>					
		</tr>
	</table> 
	<% } else {%>
		  	 <div class='div_detail_form'>
				<span class='portlet-form-field-label'>	
					<spagobi:message key = "SBISet.ListDL.noDoc" />
				</span>
			</div>
	<% } %> <!-- CLOSE LIST OF DOCUMENTS RELATED TO A DISTRIBUTION LIST  -->
	
	
	</div>  

	<script>
	
	function isDlFormChanged () {
	
	var bFormModified = 'false';
		
	var name = document.dlForm.NAME.value;
	var description = document.dlForm.DESCR.value;	
	
	if ((name != '<%=dl.getName()%>')
		|| (description != '<%=(dl.getDescr()==null)?"":dl.getDescr()%>')) {
			
		bFormModified = 'true';
	}
	
	return bFormModified;
	
	}

	
	function goBack(message, url) {
	  
	  var bFormModified = isDlFormChanged();
	  
	  if (bFormModified == 'true'){
	  	  if (confirm(message)) {
	  	      document.getElementById('saveAndGoBack').click(); 
	  	  } else {
			location.href = url;	
    	  }	         
       } else {
			location.href = url;
       }	  
	}
	
	function saveDL(type) {	
  	  	  document.dlForm.SUBMESSAGEDET.value=type;
  	  	  if (type == 'SAVE')
      		  document.getElementById('dlForm').submit();
	}
	</script>
	
	<%@ include file="/jsp/commons/footer.jsp"%>
