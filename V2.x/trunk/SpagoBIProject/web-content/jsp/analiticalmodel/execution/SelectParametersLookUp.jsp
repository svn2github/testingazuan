<%@page import="java.util.ArrayList"%>
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

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<spagobi:list moduleName="SelectParameterPageModule" />

<%--

<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.commons.utilities.PortletUtilities"%>
<%@page import="it.eng.spagobi.commons.constants.SpagoBIConstants"%>



<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/windows.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/table.css")%>' type='text/css' />

<%


	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("SelectParameterPageModule");
    // get message info 

    String msgInfo = (String)moduleResponse.getAttribute(SpagoBIConstants.MESSAGE_INFO); 

    // if there's a message info show only it
    if(msgInfo!=null) {
%>

	<br/><br/>
	<center>
		<span class='portlet-form-field-label'><%=msgInfo%></span>
	</center>
	<br/><br/>

<%
    } else {
	    // get parameter field name
	    String parameterFieldName = (String)moduleResponse.getAttribute(SpagoBIConstants.PARAMETER_FIELD_NAME); 
	    // get value column name
	    String valColName = (String)moduleResponse.getAttribute(SpagoBIConstants.VALUE_COLUMN_NAME); 
	    String returnParam = (String)moduleResponse.getAttribute("returnParam"); 
	    
	    // get rows
		List rows = moduleResponse.getAttributeAsList("PAGED_LIST.ROWS.ROW");
		SourceBean listConfig = (SourceBean)moduleResponse.getAttribute("CONFIG");
		
		// get visible columns
		List columns = new ArrayList();
		List configuredColumns = listConfig.getAttributeAsList("COLUMNS.COLUMN");
		for (int i = 0; i < configuredColumns.size(); i++) {
			String hidden = (String)((SourceBean) configuredColumns.get(i)).getAttribute("HIDDEN");
			if (hidden == null || hidden.trim().equalsIgnoreCase("FALSE"))
				columns.add((SourceBean) configuredColumns.get(i));
		}
%>

	<table id="ToSort">
		<thead>
			<tr>
					
				<!-- for each column design header -->
				<%
				for(int i = 0; i < columns.size(); i++) {
					String nameColumn = (String) ((SourceBean) columns.get(i)).getAttribute("NAME");
					String labelColumnCode = (String) ((SourceBean) columns.get(i)).getAttribute("LABEL");
					String labelColumn = "";
					if (labelColumnCode != null) 
						labelColumn = PortletUtilities.getMessage(labelColumnCode, "messages");
					else labelColumn = nameColumn;
					// if an horizontal-align is specified it is considered, otherwise the defualt is align='left'
					String align = (String) ((SourceBean) columns.get(i)).getAttribute("horizontal-align");
					if (align == null || align.trim().equals("")) 
						align = "left";
				%>
				<th><%=labelColumn%></th>
				<%
				} 	
				%>
				<TH>&nbsp;</TH>		
			</tr>
		</thead>
		<tbody>
			<!-- for each row design table row -->
			<%
			String rowvalue = "";
			for(int i = 0; i < rows.size(); i++) {
				rowvalue = "";
				SourceBean row = (SourceBean) rows.get(i);
				String rowClass = "";    
            %>
            <tr>
          
            <%
				for (int j = 0; j < columns.size(); j++) {
					String nameColumn = (String) ((SourceBean) columns.get(j)).getAttribute("NAME");
					Object fieldObject = row.getAttribute(nameColumn);
					String field = null;
					if (fieldObject != null) {
						field = fieldObject.toString();
						// set value of the row
						if(nameColumn.equalsIgnoreCase(valColName)) {
							rowvalue = field;
						}
					}
					else { field = "&nbsp;"; }
					// if an horizontal-align is specified it is considered, otherwise the defualt is align='left'
					String aligncell = (String) ((SourceBean) columns.get(j)).getAttribute("horizontal-align");
					if (aligncell == null || aligncell.trim().equals("")) 
						aligncell = "left";
			%>
				<td><%=field%></td>
				
			<%
				}   
            %>
 				<td width='20' class='<%=rowClass%>'>
					<input id='rowcheck_<%=i%>' name='rowcheck' value='<%=rowvalue%>' type='image' src='<%=urlBuilder.getResourceLink(request, "img/button_ok.gif")%>' />
					<script type="text/javascript">
					    var rowCheckSel_<%=i%>=Ext.get('rowcheck_<%=i%>');
						rowCheckSel_<%=i%>.on('click', function(){
							//alert(Ext.get('rowcheck_<%=i%>').dom.value);
							//alert(Ext.get('in_<%=returnParam%>').dom.value);
							
							Ext.get('<%=returnParam%>').dom.value='<%=rowvalue%>';
							Ext.get('in_<%=returnParam%>').dom.value='<%=rowvalue%>';
							Ext.get('spagobi_popup').hide();
						});
					</script>
					
				</td>             
            <%
			}
			%>
			</tr>
		</tbody>
	</table>
<%
	} 
%>
--%>





