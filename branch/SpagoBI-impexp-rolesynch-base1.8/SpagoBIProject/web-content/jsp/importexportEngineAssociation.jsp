<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.constants.SpagoBIConstants,
				java.util.List,
				java.util.Iterator,
				it.eng.spagobi.bo.Engine" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	List curEngines = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_CURRENT_ENGINES);
	List expEngines = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_EXPORTED_ENGINES);
    Iterator iterExpEngines = expEngines.iterator();
    
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("ACTION_NAME", "START_ACTION");
   	backUrl.setParameter("PUBLISHER_NAME", "LoginSBISettingsPublisher");
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
   
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_ENGINE_ASSOCIATION);
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.engineAssociation" />
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

    <form method='POST' action='<%=formUrl.toString()%>' id='engineAssForm' name='engineAssForm'>
	<div style="float:left;width:70%;" class="div_detail_area_forms">
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header'>Exported Engines</td>
				<td class='portlet-section-header'>System Engine Associations</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
		    <%
		    while(iterExpEngines.hasNext()) {
		    	Engine engine = (Engine)iterExpEngines.next();
		    %>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=engine.getName()%></span>
					<br/>
					<span><%=engine.getDescription()%></span>
					<span><%=engine.getUrl()%></span>
					<span><%=engine.getDriverName()%></span>
				</td>
				<td>
				    <input type="hidden" name="expEngine" value="<%=engine.getId()%>" />
					<select style="width:250px" name="engineAssociated<%=engine.getId()%>" >
						<option value=""></option>
						<% 
							Iterator iterCurEngines = curEngines.iterator();
							while(iterCurEngines.hasNext()) {
								Engine engineCur = (Engine)iterCurEngines.next();
						%>
						<option value='<%=engineCur.getId()%>' ><%=engineCur.getName()%></option>
						<% } %>
					</select>
				</td>
			</tr>
			<% } %>
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











