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
		String dlid = (String)moduleResponse.getAttribute("DL_ID");
		DistributionList dl = (DistributionList)moduleResponse.getAttribute("dlObj");
		String modality = "DETAIL_SUBSC" ;
		String subMessageDet = (((String)moduleResponse.getAttribute("SUBMESSAGEDET")==null)?"":(String)moduleResponse.getAttribute("SUBMESSAGEDET"));
		String msgWarningSave = msgBuilder.getMessage("8002", request);
		String email = (String)moduleResponse.getAttribute("EMAIL");
		
		request.setAttribute("dlObj", dl);
		request.setAttribute("DL_ID", dlid);
		request.setAttribute("modality", modality);
		request.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "insertEmailPubJ");
		request.setAttribute("SUBMESSAGEDET",subMessageDet);
		
		
		Map backUrlPars = new HashMap();
		backUrlPars.put("PAGE", "ListDistributionListUserPage");
		backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		String backUrl = urlBuilder.getUrl(request, backUrlPars);	
	
		Map formUrlPars = new HashMap();
		String formUrl = urlBuilder.getUrl(request, formUrlPars);		
	%>
	
	
	<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>

<form method='POST' action='<%=formUrl%>' id='emailForm' name='emailForm' >

		<input type='hidden' name='PAGE' value='InsertEmailPage' />
		<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />


	<input type='hidden' value='<%=modality%>' name='MESSAGEDET' />	
	<input type='hidden' value='<%=subMessageDet%>' name='SUBMESSAGEDET' />
	<input type='hidden' value='<%=dlid%>' name='DL_ID' />
	
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
	

	<div id='emailinsert' class='div_background' style='padding-top:5px;padding-left:5px;'>
		<br>
		<%
			  
			  String name = dl.getName();
			   if((name==null) || (name.equalsIgnoreCase("null"))  ) {
				   
				   name = "";
			   }
		%>
	<div class='div_detail_form'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDL.columnNameInsertMail" /> &nbsp; <i style="color:#6495ED"> <%=name%> </i> 
			</span>
		</div>
		
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDL.emailInsert" />
			</span>
		</div>
		<%
			   if((email==null) || (email.equalsIgnoreCase("null"))  ) {
				   
				   email = "";
			   }
		%>
		<div class='div_detail_form'>
			<input class='portlet-form-input-field' type="text" 
				   name="EMAIL" size="50" value="<%=email%>" maxlength="50" />
			&nbsp;*
		</div>
	</div>	
	
	<script>
	function isDlFormChanged () {
	
	var bFormModified = 'false';
		
	var email1 = document.emailForm.EMAIL.value;	
	
	if ((email1 != '<%=email%>')
		|| (email1 != '<%=((email)==null ?"":email)%>')) {
			
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
  	  	  document.emailForm.SUBMESSAGEDET.value=type;
  	  	  if (type == 'SAVE') document.getElementById('emailForm').submit();
	}
	</script>	
	
	<%@ include file="/jsp/commons/footer.jsp"%>
	