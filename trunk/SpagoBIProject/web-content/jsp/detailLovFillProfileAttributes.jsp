<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
			it.eng.spagobi.constants.SpagoBIConstants,
			it.eng.spagobi.bo.ModalitiesValue,
			it.eng.spagobi.bo.lov.ScriptDetail,
			it.eng.spagobi.bo.lov.JavaClassDetail,
			it.eng.spagobi.bo.lov.FixedListDetail,
			it.eng.spagobi.bo.ParameterUse,
			it.eng.spagobi.bo.dao.DAOFactory,
			it.eng.spago.navigation.LightNavigationManager,
			java.util.List,
			java.util.ArrayList,
			java.util.Iterator"%>
<%@page import="it.eng.spagobi.bo.lov.LovDetailFactory"%>
<%@page import="it.eng.spagobi.bo.lov.ILovDetail"%>
<%@page import="it.eng.spagobi.utilities.GeneralUtilities"%>

<%

	SourceBean detailMR = (SourceBean) aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
	List profAttrToFill = (List)detailMR.getAttribute(SpagoBIConstants.PROFILE_ATTRIBUTES_TO_FILL);

    String modality = null;
	if (detailMR != null) modality = (String) detailMR.getAttribute("modality");
	if (modality == null) modality = (String) aSessionContainer.getAttribute(SpagoBIConstants.MODALITY);
  	String messagedet = "";
  	if (modality.equals(SpagoBIConstants.DETAIL_INS))
		messagedet = SpagoBIConstants.DETAIL_INS;
	else messagedet = SpagoBIConstants.DETAIL_MOD;
		
  	PortletURL backUrl = renderResponse.createActionURL();
  	backUrl.setParameter("PAGE", "DetailModalitiesValuePage");
  	backUrl.setParameter(SpagoBIConstants.MESSAGEDET, messagedet);
  	backUrl.setParameter("modality", modality);
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	backUrl.setParameter("RETURN_FROM_TEST_MSG", "DO_NOT_SAVE");
  
    PortletURL testUrl = renderResponse.createActionURL();	
    testUrl.setParameter("PAGE", "detailModalitiesValuePage");
    testUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    testUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.MESSAGE_TEST_AFTER_ATTRIBUTES_FILLING);
%>


 





<form id="formTest" method="post" action="<%=testUrl.toString()%>" >



<!-- TITLE -->

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'
		    style='vertical-align:middle;padding-left:5px;'>
		    <%--
			<spagobi:message key = "SBIDev.predLov.testPageTitle" />
			--%>
			Profile  attribute to fill
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		
		<td class='header-button-column-portlet-section' id='testButton'>
			<input type='image' class='header-button-image-portlet-section' id='testButtonImage'
					name="testLovBeforeSave" value="testLovBeforeSave" 
					src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/test.png")%>' 
					title='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />'  
					alt='<spagobi:message key = "SBIDev.predLov.TestBeforeSaveLbl" />' 
			/>
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		
		<td class='header-button-column-portlet-section'>
			<a href="<%=backUrl.toString()%>"> 
      				<img class='header-button-image-portlet-section' 
      				     title='<spagobi:message key = "SBISet.Funct.backButt" />' 
      				     src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
      				     alt='<spagobi:message key = "SBISet.Funct.backButt" />' />
			</a>
		</td>
	</tr>
</table>




<!-- BODY -->


<div style="padding-left:10px;" class='div_background_no_img' >

	
		


		
		<div class="div_detail_area_forms" >
		
		  <div class='portlet-form-field-label' >
		     <spagobi:message key = "SBIDev.lov.needProfAttr" /> 
			 <br/>
			 <spagobi:message key = "SBIDev.lov.profNotContProfAttr" /> 
			 <br/>
			 <spagobi:message key = "SBIDev.lov.assignValToProfAttr" /> 			
		  </div> 
		  
		  <br/>
		  <br/>
		
		<%
			Iterator iterProfAttr = profAttrToFill.iterator();
		    while(iterProfAttr.hasNext()) {
		    	String profAttrName = (String)iterProfAttr.next();
		%>
			<div class='div_detail_label' style="width:200px;">
				<span class='portlet-form-field-label'>
					<%= profAttrName %>
				</span>
			</div>
			<div class='div_detail_form'>
				<input class='portlet-form-input-field' type="text" name="<%=profAttrName%>" size="35" >
	    		&nbsp;*
			</div>
		<%    	
		    }
		%>
		</div>
		
		<br/>

</div>
				
</form>


