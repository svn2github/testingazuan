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
   	backUrl.setParameter("PAGE", "ImportExportPage");
   	backUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_BACK_ENGINE_ASS);
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	
  	PortletURL exitUrl = renderResponse.createActionURL();
   	exitUrl.setParameter("PAGE", "ImportExportPage");
   	exitUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_EXIT);
  	exitUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_ENGINE_ASSOCIATION);
   	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
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
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= exitUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.exit" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/stop.png")%>' 
      				 alt='<spagobi:message key = "Sbi.exit" />' />
			</a>
		</td>
	</tr>
</table>






<div class="div_background_no_img">

    <form method='POST' action='<%=formUrl.toString()%>' id='engineAssForm' name='engineAssForm'>
	<div style="float:left;width:69%;" class="div_detail_area_forms">
		<table style="margin:10px;" cellspacing="5px">
			<tr>
				<td class='portlet-section-header'><spagobi:message key = "SBISet.impexp.exportedEngines" /></td>
				<td class='portlet-section-header'><spagobi:message key = "SBISet.impexp.currentEngines" /></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%if(expEngines.isEmpty()) { %>
			<tr>
				<td colspan="2" style="color:#074B88;"><spagobi:message key="SBISet.impexp.noEngineExported"/></td>
			</tr>
			<% } %>
		    <%
		    while(iterExpEngines.hasNext()) {
		    	Engine engine = (Engine)iterExpEngines.next();
		    %>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=engine.getName()%></span>
					<br/>
					<%=engine.getDescription()%><br/>
					<%=engine.getUrl()%><br/>
					<%=engine.getDriverName()%><br/>
				</td>
				<td>
				    <input type="hidden" name="expEngine" value="<%=engine.getId()%>" />
					<select style="width:250px;margin-top:5px;" name="engineAssociated<%=engine.getId()%>" >
						<option value="">
							<spagobi:message key="Sbi.selectcombo"/>
						</option>
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
	
	<div style="float:left;width:29%;">
		<input type="image" 
		       name="submit" 
		       title='<spagobi:message key="Sbi.next"/>' 
		       src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/next.png")%>' 
		       alt='<spagobi:message key="Sbi.next"/>' />
		<br/>
		<ul style="color:#074B88;">
			<li><spagobi:message key = "SBISet.impexp.enginerule1" /></li>
		</ul>
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>











