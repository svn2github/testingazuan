<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.constants.SpagoBIConstants,
				java.util.List,
				java.util.Map,
				java.util.Set,
				java.util.Iterator,
				it.eng.spagobi.bo.Engine,
				it.eng.spagobi.importexport.JndiConnection,
				it.eng.spagobi.importexport.JdbcConnection" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	List expConns = (List)moduleResponse.getAttribute(SpagoBIConstants.LIST_EXPORTED_CONNECTIONS);
	Map curConns = (Map)moduleResponse.getAttribute(SpagoBIConstants.MAP_CURRENT_CONNECTIONS);
    Iterator iterExpConn = expConns.iterator();
	
    PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "ImportExportPage");
   	backUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_BACK_CONN_ASS);
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	
  	PortletURL exitUrl = renderResponse.createActionURL();
   	exitUrl.setParameter("PAGE", "ImportExportPage");
   	exitUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_EXIT);
  	exitUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
      
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.IMPEXP_CONNECTION_ASSOCIATION);
   	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
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
			<%
		    while(iterExpConn.hasNext()) {
		    	Object connObj = iterExpConn.next();
		    	String connName = "";
		    	if(connObj instanceof JdbcConnection) {
		    		JdbcConnection conn = (JdbcConnection)connObj;
		    		connName = conn.getName();	
		    %>
			<tr>
				<td>
					<span class='portlet-form-field-label'><%=conn.getName() + "  (Jdbc)"%> </span>
					<br/>
					<%=conn.getDescription()%><br/>
					<%=conn.getDriverClassName()%><br/>
					<%=conn.getConnectionString()%><br/>
				</td>
		   <% } else {
				JndiConnection conn = (JndiConnection)connObj;
				connName = conn.getName();	
		   %>
		   <tr>
				<td>
					<span class='portlet-form-field-label'><%=conn.getName() + "  (Jndi)"%></span>
					<br/>
					<%=conn.getDescription()%><br/>
					<%=conn.getJndiName()%><br/>
					<%=conn.getJndiContextName()%><br/>
				</td>
		   <% } %>
				<td>
				    <input type="hidden" name="expConn" value="<%=connName%>" />
					<select style="width:250px;margin-top:5px;" name="connAssociated<%=connName%>" >
						<option value=""></option>
						<% 
							Set curConnNames = curConns.keySet();	
							Iterator iterCurConnNames = curConnNames.iterator();
							while(iterCurConnNames.hasNext()) {
								String curConnName = (String)iterCurConnNames.next();
								String curNameDesc = (String)curConns.get(curConnName);
						%>
						<option value='<%=curConnName%>' ><%=curNameDesc%></option>
						<% } %>
					</select>
				</td>
			</tr>
			<% } %>
		</table>
	</div>
	
	<div style="float:left;">
		<input type="image" 
		       name="submit" 
		       title='<spagobi:message key="Sbi.next"/>' 
		       src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/img/next.png")%>' 
		       alt='<spagobi:message key="Sbi.next"/>' />
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>











