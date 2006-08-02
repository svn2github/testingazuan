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

<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.importexport.ImportExportConstants,
				java.util.List,
				java.util.Map,
				java.util.Set,
				java.util.Iterator,
				it.eng.spagobi.bo.Engine,
				it.eng.spagobi.importexport.JndiConnection,
				it.eng.spagobi.importexport.JdbcConnection" %>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	List expConns = (List)moduleResponse.getAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS);
	Map curConns = (Map)moduleResponse.getAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS);
    Iterator iterExpConn = expConns.iterator();
	
    PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "ImportExportPage");
   	backUrl.setParameter("MESSAGEDET", ImportExportConstants.IMPEXP_BACK_CONN_ASS);
  	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
  	
  	PortletURL exitUrl = renderResponse.createActionURL();
   	exitUrl.setParameter("PAGE", "ImportExportPage");
   	exitUrl.setParameter("MESSAGEDET", ImportExportConstants.IMPEXP_EXIT);
  	exitUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
      
  	PortletURL formUrl = renderResponse.createActionURL();
  	formUrl.setParameter("PAGE", "ImportExportPage");
   	formUrl.setParameter("MESSAGEDET", ImportExportConstants.IMPEXP_CONNECTION_ASSOCIATION);
   	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
%>

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "SBISet.connectionAssociation"  bundle="component_impexp_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/back.png")%>' 
      				 alt='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' />
			</a>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= exitUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "Sbi.exit"  bundle="component_impexp_messages"/>' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/stop.gif")%>' 
      				 alt='<spagobi:message key = "Sbi.exit"  bundle="component_impexp_messages"/>' />
			</a>
		</td>
	</tr>
</table>


<div class="div_background_no_img">

    <form method='POST' action='<%=formUrl.toString()%>' id='connAssForm' name='connAssForm'>
	<div style="float:left;width:60%;" class="div_detail_area_forms">
		<table style="margin:10px;" >
			<tr>
				<td class='portlet-section-header'><spagobi:message key="SBISet.impexp.exportedConn" bundle="component_impexp_messages"/></td>
				<td class='portlet-section-header'><spagobi:message key="SBISet.impexp.currentConn" bundle="component_impexp_messages"/></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%if(expConns.isEmpty()) { %>
			<tr>
				<td colspan="2" style="color:#074B88;"><spagobi:message key="SBISet.impexp.noConnExported" bundle="component_impexp_messages"/></td>
			</tr>
			<% } %>
			<%
		    while(iterExpConn.hasNext()) {
		    	Object connObj = iterExpConn.next();
		    	String connName = "";
		    	if(connObj instanceof JdbcConnection) {
		    		JdbcConnection conn = (JdbcConnection)connObj;
		    		connName = conn.getName();	
		    %>
			<tr>
				<td class="portlet-font">
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
				<td class="portlet-font">
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
						<option value="">
							<spagobi:message key="Sbi.selectcombo" bundle="component_impexp_messages"/>
						</option>
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
	
	<div style="float:left;width:29%;">
		<input type="image" 
		       name="submit" 
		       title='<spagobi:message key="Sbi.next" bundle="component_impexp_messages"/>' 
		       src='<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/components/importexport/img/next.gif")%>' 
		       alt='<spagobi:message key="Sbi.next" bundle="component_impexp_messages"/>' />
		<br/>
		<ul style="color:#074B88;">
			<li><spagobi:message key = "SBISet.impexp.connrule1"  bundle="component_impexp_messages"/></li>
		</ul>
	</div>
	</form>
	
	<div style="clear:left" />
	 &nbsp;
	</div>
	
</div>



<spagobi:error/>








