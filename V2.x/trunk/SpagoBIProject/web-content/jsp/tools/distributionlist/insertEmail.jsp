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

		String modality = "DETAIL_SUBSC" ;
		String subMessageDet = ((String)moduleResponse.getAttribute("SUBMESSAGEDET")==null)?"":(String)moduleResponse.getAttribute("SUBMESSAGEDET");
		String msgWarningSave = msgBuilder.getMessage("8002", request);
		
		Map formUrlPars = new HashMap();
		if(ChannelUtilities.isPortletRunning()) {
			formUrlPars.put("PAGE", "InsertEmailPage");	
  			formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
		}
		String formUrl = urlBuilder.getUrl(request, formUrlPars);
		
		Map backUrlPars = new HashMap();
		backUrlPars.put("PAGE", "ListDistributionListUserPage");
		backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
		String backUrl = urlBuilder.getUrl(request, backUrlPars);		
	%>
	
	
	<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>

<form method='POST' action='<%=formUrl%>' id='emailForm' name='emailForm' >

	<% if(ChannelUtilities.isWebRunning()) { %>
		<input type='hidden' name='PAGE' value='InsertEmailPage' />
		<input type='hidden' name='<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value='true' />
	<% } %>

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
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDL.emailInsert" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input class='portlet-form-input-field' type="text" 
				   name="EMAIL" size="50" value="" maxlength="50" />
			&nbsp;*
		</div>
	</div>	
	
	<script>
	function isDlFormChanged () {
	
	var bFormModified = 'false';
		
	var email = document.dlForm.EMAIL.value;
	
	if (email != 'prova') {
			bFormModified = 'true';
	}
	
	return bFormModified;
	
	}
	
	function goBack(message, url) {
	  
	  //var bFormModified = isDlFormChanged();
	  location.href = url;	  
	}
	
	function saveDL(type) {	
  	  	  document.emailForm.SUBMESSAGEDET.value=type;
  	  	  if (type == 'SAVE') document.getElementById('emailForm').submit();
	}
	</script>	
	
	<%@ include file="/jsp/commons/footer.jsp"%>
	