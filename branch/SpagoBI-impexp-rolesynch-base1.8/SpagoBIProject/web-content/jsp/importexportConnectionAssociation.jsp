<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.constants.SpagoBIConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Engine" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	//List curEngines = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_CURRENT_ENGINES);
	//List expEngines = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_EXPORTED_ENGINES);
    //Iterator iterExpEngines = expEngines.iterator();
    
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("ACTION_NAME", "START_ACTION");
   	backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_CONNECTION_ASSOCIATION);
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.connectionAssociation" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back" />' />
			</a>
		</td>
	</tr>
</table>






<div class="div_background_no_img">

    <form method='POST' action='<%=formUrl.toString()%>' id='connAssForm' name='connAssForm'>
	<div style="float:left;width:70%;" class="div_detail_area_forms">
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header'>Exported Connections</td>
				<td class='portlet-section-header'>System Connection Associations</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
		</table>
	</div>
	
	<div style="float:left;">
		<input type="submit" name="submit" value="Next" />
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>











