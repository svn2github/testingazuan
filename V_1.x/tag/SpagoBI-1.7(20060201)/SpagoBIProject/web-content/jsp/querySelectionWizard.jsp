<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.constants.AdmintoolsConstants,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.bo.ModalitiesValue,
				 it.eng.spagobi.bo.Domain,
				 it.eng.spagobi.bo.QueryDetail,
                 it.eng.spagobi.bo.dao.jdbc.DomainDAOImpl,
                 it.eng.spagobi.bo.dao.jdbc.ModalitiesValueDAOImpl,
                 it.eng.spago.navigation.LightNavigationManager,
                 java.util.ArrayList,
                 it.eng.spagobi.services.modules.DetailModalitiesValueModule,
                 javax.portlet.PortletURL"
                 %>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>
<portlet:defineObjects/>
<%
  SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailModalitiesValueModule"); 
  String modality = (String)moduleResponse.getAttribute(SpagoBIConstants.MODALITY);
  QueryDetail query = new QueryDetail();
  ModalitiesValue modVal = (ModalitiesValue)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
  if(modVal != null){
  		String lovProvider = modVal.getLovProvider();
  		if (lovProvider != null  &&  !lovProvider.equals("")){
  			query = QueryDetail.fromXML(lovProvider);
  		}
  }
%>

<%String lovLabel = modVal.getLabel();
  String lovName = modVal.getName();
  String lovDescription = modVal.getDescription();
  String lovType = modVal.getITypeCd(); %>

<% 
   PortletURL formUrl = renderResponse.createActionURL();
   formUrl.setParameter("PAGE", "detailModalitiesValuePage");
   if(modality.equals(SpagoBIConstants.DETAIL_INS)) {
   		formUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_INS_WIZARD_QUERY);
   } else {
   		formUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_MOD_WIZARD_QUERY);
   }
   formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL backUrl = renderResponse.createActionURL();
   if(modality.equals(SpagoBIConstants.DETAIL_INS)) {
   		backUrl.setParameter("PAGE", "ListLovsPage");
   } else {
   		backUrl.setParameter("PAGE", "DetailModalitiesValuePage");
   		backUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_SELECT);
   		backUrl.setParameter("id", modVal.getId().toString());
   }
   backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
%>

<table width="100%"  style="margin-top:3px; margin-left:3px; margin-right:3px; margin-bottom:5px;">
  	<tr height='1'>
  		<td width="25%"></td>
  		<td style="width:3px;"></td>
  		<td width="12%"></td>
  		<td width="15%"></td>
  		<td width="15%"></td>
  		<td width="33%"></td>
  	</tr>
  	<tr height = "20">
  		<td class='portlet-section-subheader' style='text-align:center;vertical-align:bottom;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet1"/>
  		</td>
  		<td style="width:3px;"></td>
  		<td class='portlet-section-body' style='border-top: 1px solid #CCCCCC;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.label"/>: 
  		</td>
  		<td class='portlet-section-alternate' style='border-top: 1px solid #CCCCCC;'>
  			<%=lovLabel %>
  		</td>
  		<td class='portlet-section-body' style='border-top: 1px solid #CCCCCC;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.name"/>: 
  		</td>
  		<td class='portlet-section-alternate' style='border-top: 1px solid #CCCCCC;'>
  			<%=lovName %>
  		</td>
  	</tr>
  	<tr height = "20">
  		<td class='portlet-section-subheader' style='text-align:center;vertical-align:top;'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet2" />
  		</td>
  		<td style="width:3px;"></td>
  		<td class='portlet-section-body' >
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.type"/>: 
  		</td>
  		<td class = 'portlet-section-alternate'>
  			<%=lovType %>
  		</td>
  		<td class='portlet-section-body'>
  			<spagobi:message key = "SBIDev.lovWiz.valueDet.description"/>: 
  		</td>
  		<td class = 'portlet-section-alternate'>
  			<%=lovDescription %>
  		</td>
  	</tr>
</table>

<form method='POST' action='<%= formUrl.toString() %>' id ='querySelectionWizardForm' name='querySelectionWizardForm'>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.queryWiz.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('querySelectionWizardForm').submit()" > 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.queryWiz.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBIDev.queryWiz.saveButt" />'/>
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href= '<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.queryWiz.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.queryWiz.backButt" />'/>
			</a>
		</td>
</table>

<!--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIDev.queryWiz.title" /></th>
	</tr>
</table-->

<input type='hidden' value='' name='id' />

<table width="100%" style="margin-top:3px; margin-left:3px; margin-right:3px; margin-bottom:5px;" cellspacing="0" border="0">
  	<tr height='1'>
  		<td width="1px"><span>&nbsp;</span></td>
  		<td width="13%"><span>&nbsp;</span></td>
  		<td width="20px"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.queryWiz.connNameField" /></td>
      	<td>&nbsp;</td>
      	<td><input class='portlet-form-input-field' type="text" name="connName" size="50" value="<%= query.getConnectionName()!= null ? query.getConnectionName().toString() : "" %>" maxlength="100">&nbsp;*</td>
    </tr>
    <tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.queryWiz.visColumnsField" /></td>
      	<td>&nbsp;</td>
      	
      	<td ><input class='portlet-form-input-field' type="text" name="visColumns" size="50" value="<%= query.getVisibleColumns()!= null ? query.getVisibleColumns().toString() : "" %>" maxlength="100">&nbsp;*</td>
    </tr>
   
      <tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.queryWiz.valueColumnsField" /></td>
      	<td>&nbsp;</td>
      	<td><input class='portlet-form-input-field' type="text" name="valueColumns" size="50" value="<%= query.getValueColumns() != null ? query.getValueColumns().toString() : "" %>" maxlength="100">&nbsp;*</td>
    </tr> 
   
    
    <tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.queryWiz.queryDefField" /></td>
      	<td>&nbsp;</td>

      	<td > <%-- <input class='portlet-text-area-field' type="text" name="lovProvider" size="50" value="<%=dataDef%>" maxlength="1000"> --%>
      		<!-- input type='hidden' value='<%=query.getQueryDefinition()!= null ? query.getQueryDefinition().toString() : "" %>' name='queryDef' /-->
      		<TEXTAREA class='portlet-text-area-field' name="queryDef" rows="5" cols="50"><%=query.getQueryDefinition()!= null ? query.getQueryDefinition().toString() : ""%></TEXTAREA>
      	</td>
    </tr>
    
    <%--tr height='25'>
      	<td colspan="4">&nbsp;</td>
    </tr>
    <tr height='40'>
      	<td>&nbsp;</td>
      	<td align='right'>&nbsp;</td>
      	<td>&nbsp;</td>
      	<td>
      	    <table>
      	    	<tr>
      	    	 	<td>
      	    	 	   	
      	    	 	    <input type='image' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' name='save' alt='save'/> 
					</td>
					</form>
					<td width='30'>&nbsp;</td>
					<td>
					    <a href= '<%= backUrl.toString() %>' class='portlet-menu-item' >
      						<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
						</a> 
					</td>
      	    	</tr>
      	    	<tr>
      	    		<td>
      	    	 	    <a href="javascript:document.getElementById('querySelectionWizardForm').submit()" > 
      	    	 	        <spagobi:message key = "SBIDev.queryWiz.saveButt" />
      	    	 	    </a> 
					</td>
					<td width='30'>&nbsp;</td>
					<td>
						<a href='<%= backUrl.toString() %>'>
							<spagobi:message key = "SBIDev.queryWiz.backButt" />
						</a>
					</td>
      	    	</tr>
      	    </table>
      	</td>
    </tr--%>
</table>

</form>

<!-- br/-->
 


