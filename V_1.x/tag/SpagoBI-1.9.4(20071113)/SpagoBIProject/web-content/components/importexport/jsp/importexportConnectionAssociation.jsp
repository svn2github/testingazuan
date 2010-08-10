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

<%@ page import="it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.importexport.ImportExportConstants,
				java.util.List,
				java.util.Map,
				java.util.Set,
				java.util.Iterator,
				it.eng.spagobi.bo.Engine,
				it.eng.spagobi.importexport.JndiConnection,
				it.eng.spagobi.importexport.JdbcConnection" %>
<%@page import="java.util.HashMap"%>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ImportExportModule"); 
	List expConns = (List)moduleResponse.getAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS);
	Map curConns = (Map)moduleResponse.getAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS);
    Iterator iterExpConn = expConns.iterator();
	
    Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "ImportExportPage");
    backUrlPars.put("MESSAGEDET", ImportExportConstants.IMPEXP_BACK_CONN_ASS);
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String backUrl = urlBuilder.getUrl(request, backUrlPars);
  
    Map exitUrlPars = new HashMap();
    exitUrlPars.put("PAGE", "ImportExportPage");
    exitUrlPars.put("MESSAGEDET", ImportExportConstants.IMPEXP_EXIT);
    exitUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String exitUrl = urlBuilder.getUrl(request, exitUrlPars);

    Map formUrlPars = new HashMap();
    formUrlPars.put("PAGE", "ImportExportPage");
    formUrlPars.put("MESSAGEDET", ImportExportConstants.IMPEXP_CONNECTION_ASSOCIATION);
    formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
    String formUrl = urlBuilder.getUrl(request, formUrlPars);
   
%>



<script>

	var infopanelopen = false;
	var winInfo = null;
	
	function opencloseInfoPanel() {
		if(!infopanelopen){
			infopanelopen = true;
		 	openInfo();
		 }
	}
	
	function openInfo(){
		if(winInfo==null) {
		 	winInfo = new Window('winInfo', {className: "alphacube", title:"<spagobi:message key="help"  bundle="messages"/>", width:680, height:150, destroyOnClose: false});
		 	winInfo.setContent('infodiv', false, false);
		 	winInfo.showCenter(true);
		 } else {
			winInfo.showCenter(true);
		 }
	}
	
	observerWInfo = { onClose: function(eventName, win) {
			if (win == winInfo) {
			 	infopanelopen = false;
			 }
		}
	}
	
	Windows.addObserver(observerWInfo);

</script>



<div id='infodiv' style='display:none;'>
	<ul style="color:#074B88;">
			<li><spagobi:message key = "SBISet.impexp.connrule1"  bundle="component_impexp_messages"/></li>
	</ul>
</div>	



<form method='POST' action='<%=formUrl%>' id='connAssForm' name='connAssForm'>

	<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "SBISet.connectionAssociation"  bundle="component_impexp_messages"/>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='javascript:opencloseInfoPanel()'> 
			    	<img class='header-button-image-portlet-section' 
			    		title='<spagobi:message key="help"  bundle="messages"/>' 
			    		src='<%=urlBuilder.getResourceLink(request, "/img/info22.jpg")%>' 
			    		alt='<spagobi:message key="help"  bundle="messages"/>' />
				</a>
			</td>		
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='javascript:document.getElementById("connAssForm").submit()'> 
			    	<img class='header-button-image-portlet-section' 
			    		title='<spagobi:message key="Sbi.next"  bundle="component_impexp_messages"/>' 
			    		src='<%=urlBuilder.getResourceLink(request, "/components/importexport/img/next.gif")%>' 
			    		alt='<spagobi:message key="Sbi.next"  bundle="component_impexp_messages"/>' />
				</a>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%=backUrl%>'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/components/importexport/img/back.png")%>' 
	      				 alt='<spagobi:message key = "Sbi.back"  bundle="component_impexp_messages"/>' />
				</a>
			</td>
			<td class='header-empty-column-portlet-section'>&nbsp;</td>
			<td class='header-button-column-portlet-section'>
				<a href='<%=exitUrl %>'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "Sbi.exit"  bundle="component_impexp_messages"/>' 
	      				 src='<%=urlBuilder.getResourceLink(request, "/components/importexport/img/stop.gif")%>' 
	      				 alt='<spagobi:message key = "Sbi.exit"  bundle="component_impexp_messages"/>' />
				</a>
			</td>
		</tr>
	</table>


	<div class="div_background_no_img">
		<div class="box padding5" >
			<table>
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
	</div>
</form>

<spagobi:error/>
	
