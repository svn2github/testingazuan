<%@ include file="/jsp/portlet_base.jsp"%>
<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="false"
%>

<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>
<portlet:defineObjects/>
<%@ page import="it.eng.spagobi.constants.AdmintoolsConstants,
				 it.eng.spagobi.constants.SpagoBIConstants,
				 it.eng.spagobi.bo.ModalitiesValue,
				 it.eng.spagobi.bo.LovDetail,
				 it.eng.spagobi.bo.LovDetailList,
                 it.eng.spagobi.bo.dao.jdbc.DomainDAOImpl,
                 it.eng.spagobi.bo.dao.jdbc.ModalitiesValueDAOImpl,
                 it.eng.spago.base.RequestContainer,
				 it.eng.spago.base.ResponseContainer,
				 it.eng.spago.base.SessionContainer,
                 it.eng.spagobi.services.modules.DetailModalitiesValueModule,
                 it.eng.spago.navigation.LightNavigationManager,
                 java.util.ArrayList,
                 java.util.List,
                 javax.portlet.PortletURL,
                 java.util.Iterator"
                 %>

<%
 
  SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailModalitiesValueModule");
  String modality = (String)moduleResponse.getAttribute(SpagoBIConstants.MODALITY); 
  List lovs = new ArrayList();
  ModalitiesValue modVal = (ModalitiesValue)aSessionContainer.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
  String lovProvider = null;
  if(modVal!= null){
  	lovProvider = modVal.getLovProvider();
  }
  if(lovProvider!= null && !lovProvider.equals("")) {
  	lovs = LovDetailList.fromXML(lovProvider).getLovs();
  } 
  
%>

<script>
// js function for item action confirm
function actionConfirm(message, url){
	if (confirm('<spagobi:message key = "SBIDev.lovWiz.confirmCaption" /> ' + message + '?')){
		location.href = url;
	}
}
</script>

<%String lovLabel = modVal.getLabel();
  String lovName = modVal.getName();
  String lovDescription = modVal.getDescription();
  String lovType = modVal.getITypeCd(); %>
  

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


<% 
   
   PortletURL formUrl = renderResponse.createActionURL();
   formUrl.setParameter("PAGE", "detailModalitiesValuePage");
   formUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_ADD_WIZARD_LOV);
   formUrl.setParameter(SpagoBIConstants.MODALITY, modality);
   formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   PortletURL saveUrl = renderResponse.createActionURL();
   saveUrl.setParameter("PAGE", "detailModalitiesValuePage");
   if(modality.equals(SpagoBIConstants.DETAIL_INS)) {
   		saveUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_INS_WIZARD_FIX_LOV);
   } else {
   		saveUrl.setParameter(SpagoBIConstants.MESSAGEDET, SpagoBIConstants.DETAIL_MOD_WIZARD_FIX_LOV);
   }
   saveUrl.setParameter(SpagoBIConstants.MODALITY, modality);
   saveUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
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
		
<form method='POST' action='<%= formUrl.toString() %>' id ='lovSelectionWizardForm' name='lovSelectionWizardForm'>

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<input type='hidden' value='' name='id' />

<%--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'><spagobi:message key = "SBIDev.lovWiz.title" /></th>
	</tr>
</table--%>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.lovWiz.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= saveUrl.toString() %>' > 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.lovWiz.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBIDev.lovWiz.saveButt" />'/>
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href= '<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.lovWiz.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.lovWiz.backButt" />'/>
			</a>
		</td>
</table>

<table width="100%" cellspacing="0" border="0" >
  	<tr height='1px'>
  		<td width="1px"><span>&nbsp;</span></td>
  		<td width="50px"><span>&nbsp;</span></td>
  		<td width="20px"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.lovWiz.lovNameField" /></td>
      	<td>&nbsp;</td>
      	<td><input class='portlet-form-input-field' type="text" name="lovName" size="50" value="" maxlength="100">&nbsp;*
    	&nbsp;&nbsp;&nbsp;
    	<input type='image' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif")%>' name='add' alt='add a value'/>  
    	&nbsp;
    	<a href="javascript:document.getElementById('lovSelectionWizardForm').submit()" > 
      	     <spagobi:message key = "SBIDev.lovWiz.addButt" />
        </a> 
   	</td>
    </tr>
    <tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label'><spagobi:message key = "SBIDev.lovWiz.lovDescriptionField" /></td>
      	<td>&nbsp;</td>
      	
      	<td ><input class='portlet-form-input-field' type="text" name="lovDescription" size="50" value="" maxlength="100">&nbsp;*</td>
    </tr>
</table>
   
<br/>

<table width="100%" cellspacing="0" border="0">
  	<!--tr>
  		<td width="30px">&nbsp;</td>
  		<td>
  			<table width="100%"-->	
  				<tr >
  					<td colspan="1" class='portlet-section-header'>
  						<spagobi:message key = "SBIDev.lovWiz.tableCol1" />
  					</td>
  					<td colspan="1" class='portlet-section-header'>
  						<spagobi:message key = "SBIDev.lovWiz.tableCol2" />
  					</td>
  					<td colspan="1" width='20' class='portlet-section-header'>&nbsp;
  					</td>
  				</tr>
  				<%	if (lovs != null) {
  						Iterator iter = lovs.iterator();
  						boolean alternate = false;
				        String rowClass;
  						while(iter.hasNext()) {
  							LovDetail lovDet = (LovDetail)iter.next(); 
  							String name = lovDet.getName();
  							String description = lovDet.getDescription(); 
  							char quote = '"';
  							char space = ' ';
  							
  							rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
				            alternate = !alternate;
  							out.print("<tr class='portlet-font'>");
  							
  							out.print("<td class='" + rowClass + "'>" + name + "</td>");
  							out.print("<td class='" + rowClass + "'>" + description + "</td>");
  							out.print("<td class='" + rowClass + "'>");
  				%>
  					<a href = "javascript:actionConfirm('<spagobi:message key = "SBIDev.lovWiz.deleteCaption" />', '<portlet:actionURL>
  								<portlet:param name = "PAGE" value = "detailModalitiesValuePage"/>
  								<portlet:param name = "MESSAGEDET" value = "<%=SpagoBIConstants.DETAIL_DEL_WIZARD_LOV%>" />
  				    			<portlet:param name = "LovName" value = "<%=name %>"/>
  				    			<portlet:param name = "lovDescription" value = "<%=description%>"/> 
  				    			<portlet:param name = "MODALITY" value = "<%=modality%>"/> 
  				    			<portlet:param name = '<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>' value = "true"/>
  							   </portlet:actionURL>');"
  							   class ="portlet-menu-item" >
  						<img src= '<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif")%>' />
  					</a>
  				<%
  							out.print("</td>");
  							out.print("</tr>");
  						}
  					}
  				 %>
  			<%--/table>
  		</td>
  	</tr--%>
</table> 
      
</form>

<!--br/-->
    
<%--table width="100%" cellspacing="0" border="0" >
   	<tr>
   	 	<td>&nbsp;</td>
   	 	<td width='80px'>
			<a href='<%= saveUrl.toString() %>' class ="portlet-menu-item" >		
      				<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='save values' />
			</a> 
      	</td>
      	<td width='30px'>&nbsp;</td>
      	<td width='80px'>
			<a href= '<%= backUrl.toString() %>' class='portlet-menu-item' >
      			<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
			</a> 
		</td>
      	<td>&nbsp;</td>
     </tr>
     <tr>
     	<td>&nbsp;</td>
      	<td width='80px'>
		   <a href='<%= saveUrl.toString() %>'>
			 <spagobi:message key = "SBIDev.lovWiz.saveButt" />
		  </a>
		</td>
		<td width='30px'>&nbsp;</td>
     <td width='80px'>
		   <a href='<%= backUrl.toString() %>'>
			 <spagobi:message key = "SBIDev.lovWiz.backButt" /> 
		  </a>
		</td>
     </tr>
 </table--%>


<!--br/-->
 


