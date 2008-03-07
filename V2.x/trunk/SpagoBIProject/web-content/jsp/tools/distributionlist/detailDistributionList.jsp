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
	 				         it.eng.spagobi.tools.distributionlist.service.DetailDistributionListModule" %>
	 				         
	<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
	<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>
	
	<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailDistributionListModule"); 
		DistributionList dl = (DistributionList)moduleResponse.getAttribute("dlObj");
		List listDialects = (List) moduleResponse.getAttribute(DetailDistributionListModule.NAME_ATTR_LIST_DIALECTS);
		
		String modality = (String)moduleResponse.getAttribute("modality");
		String subMessageDet = ((String)moduleResponse.getAttribute("SUBMESSAGEDET")==null)?"":(String)moduleResponse.getAttribute("SUBMESSAGEDET");
		String msgWarningSave = msgBuilder.getMessage("8002", request);
		
		Map formUrlPars = new HashMap();
			formUrlPars.put("PAGE", "DetailDistributionListPage");	
  			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
		String formUrl = urlBuilder.getUrl(request, formUrlPars);
		
		Map backUrlPars = new HashMap();
		backUrlPars.put("PAGE", "ListDistributionListPage");
		backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		String backUrl = urlBuilder.getUrl(request, backUrlPars);		
	%>
	
	

<form method='POST' action='<%=formUrl%>' id='dlForm' name='dlForm' >

	<!-- 
		<input type='hidden' name='PAGE' value='DetailDistributionListPage' />
		<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />  -->

	<input type='hidden' value='<%=modality%>' name='modality' />	
	<input type='hidden' value='<%=modality%>' name='MESSAGEDET' />	
	<input type='hidden' value='<%=subMessageDet%>' name='SUBMESSAGEDET' />
	<input type='hidden' value='<%=dl.getId()%>' name='id' />
	
	<table width="100%" cellspacing="0" border="0" class='header-table-portlet-section'>		
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' 
			    style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "SBISet.ListDL.TitleDetail"  />
			</td>
			<td class='header-button-column-portlet-section'>
				<a href="javascript:saveDL('SAVE')"> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.ListDL.saveButton" />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.ListDL.saveButton"/>' 
	      			/> 
				</a>
			</td>		 
			<td class='header-button-column-portlet-section'>
				<input type='image' name='saveAndGoBack' id='saveAndGoBack' onClick="javascript:saveDL('SAVEBACK')" class='header-button-image-portlet-section'
				       src='<%=urlBuilder.getResourceLink(request, "/img/saveAndGoBack.png")%>' 
      				   title='<spagobi:message key = "SBISet.ListDL.saveBackButton" />'  
                       alt='<spagobi:message key = "SBISet.ListDL.saveBackButton" />' 
			   />
			</td>
			<td class='header-button-column-portlet-section'>
				<a href='javascript:goBack("<%=msgWarningSave%>", "<%=backUrl%>")'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "SBISet.ListDL.backButton"  />' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/img/back.png")%>' 
	      				 alt='<spagobi:message key = "SBISet.ListDL.backButton" />' 
	      			/>
				</a>
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
				   name="NAME" size="50" value="<%=name%>" maxlength="50" />
			&nbsp;*
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
				   size="50" value="<%= descr %>" maxlength="160" />
		</div>
	
	</td><!-- CLOSE COLUMN WITH DATA FORM  -->
		
		
		<spagobi:error/>
	</tr>
	</table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->
	<BR>
	
<% if (modality!= null && modality.equalsIgnoreCase("DETAIL_MOD")){ %>
<table style='width:80%;vertical-align:middle;margin-top:1px' >
<tr>
	<td>
	<table style='width:70%;vertical-align:middle;margin-top:1px' id ="userTable" >
		<tr class='header-row-portlet-section'>	
			<td class='header-title-column-portlet-section-nogrey' style='text-align:center;vertical-align:middle'>
					<spagobi:message key = "SBISet.ListDL.relatedUsers" />
			</td>
		</tr>
	</table>
		<table style='width:70%;margin-top:1px' id ="usersTable" >
	<tr>	
	  <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.columnUser" />
	  </td>			

	  <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.columnEmail" />		
	  </td>
	</tr>
	<!-- LIST OF USERS AND RESPECTIVE E_MAILS FOR A DISTRIBUTION LIST  -->
	<%
			List users = dl.getEmails();
			if(users!= null && !users.isEmpty()){
	%>		

	
		<%
			Iterator it = users.iterator();
			while(it.hasNext()){
				
				Email user=(Email)it.next();
				String userName = user.getUserId();
				if((userName==null) || (userName.equalsIgnoreCase("null"))  ) {
					   userName = "";
				   }
				String userEmail = user.getEmail();
				if((userEmail==null) || (userEmail.equalsIgnoreCase("null"))  ) {
					   userEmail = "";
				   }				
		 %>
				
		<tr class='portlet-font'>
		 	<td class='portlet-section-body' style='vertical-align:left;text-align:left;'><%=userName %>	 			
			</td>	
			<td class='portlet-section-body' style='vertical-align:left;text-align:left;'><%=userEmail %>
			</td>
	    </tr>
										
		<% } %>

	<% } %>
  <!-- CLOSE LIST OF USERS AND RESPECTIVE E_MAILS FOR A DISTRIBUTION LIST  -->
	</table>
</td>
	
<td>	
	<table style='width:90%;vertical-align:middle;margin-top:1px' id ="userTable" >
		<tr class='header-row-portlet-section'>			
			<td class='header-title-column-portlet-section-nogrey' style='text-align:center;vertical-align:middle'>
					<spagobi:message key = "SBISet.ListDL.relatedDoc" />
			</td>
		</tr>
	</table>	
	
	<table style='width:90%;margin-top:1px' id ="documentsTable" >
	<!-- LIST OF DOCUMENTS RELATED TO A DISTRIBUTION LIST  -->
	<tr>	
	  <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.columnDocName" />
	  </td>			

	  <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.columnDocDescr" />		
	  </td>
	</tr>	
	
	<%
			List documents = dl.getDocuments();
			if(documents!=null && !documents.isEmpty()){
	%>	
	
	
		
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
		 <tr class='portlet-font'>
		 	<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>		 	
			<%=docName%>
			</td>	
			<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>
			<%=docDescr %>
			</td>
	    </tr>
				    		
	<% } %>
	
								
	<% } %>
<!-- CLOSE LIST OF DOCUMENTS RELATED TO A DISTRIBUTION LIST  -->
<spagobi:error/>
	
	</table> 
	</td>
</tr>	
</table>
	<% } %>	
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
	</form>
	<%@ include file="/jsp/commons/footer.jsp"%>
	