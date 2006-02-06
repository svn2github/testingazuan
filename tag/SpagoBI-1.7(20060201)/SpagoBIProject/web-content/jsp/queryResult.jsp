<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="it.eng.spago.base.*, it.eng.qbe.utility.*,it.eng.qbe.javascript.*, it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>




<%@ include file="../jsp/qbe_base.jsp" %>

<% 
   ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
     
   dm.updateCurrentClassLoader(); 
     
   String msg =  (String)aServiceResponse.getAttribute("ERROR_MSG");
   boolean flagErrors = false;
   java.util.List aList = null;
   String className = null;
	String query = null;
	int currentPage = -1;
	boolean hasPreviousPage = false;
	boolean hasNextPage = false;
   SourceBean listResponse = null;
   if (msg != null){
	   flagErrors = true;
   }else{
	   listResponse = (SourceBean)sessionContainer.getAttribute(it.eng.qbe.action.ExecuteSaveQueryAction.QUERY_RESPONSE_SOURCE_BEAN);
	   aList = (java.util.List)listResponse.getAttribute("list");
	   className = (String) listResponse.getAttribute("className");
	   query =(String) listResponse.getAttribute("query");
	   currentPage = ((Integer)listResponse.getAttribute("currentPage")).intValue();
	   hasPreviousPage = ((Boolean)listResponse.getAttribute("hasPreviousPage")).booleanValue();
	   hasNextPage = ((Boolean)listResponse.getAttribute("hasNextPage")).booleanValue();
   }
  		
	String finalQueryString = null;
	if (aWizardObject.isUseExpertedVersion()){
		finalQueryString = aWizardObject.getExpertQueryDisplayed();
	}else{
		 finalQueryString = aWizardObject.getFinalQuery();
	} 
  
%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>
<table width="100%">
	<tr>
		
			<td width="100%">
				<TABLE WIDTH = "100%">
					<TR>
						<TD width="5">&nbsp;</TD>
						<TD width="90%" CLASS = "TESTATA">
							<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Result.Preview") %>
						</TD>
						<%@include file="../jsp/qbe_headers.jsp"%>
					</TR>
					<TR>
						<TD>
						</TD>
						<TD colspan="2">
							<TABLE class=LAYMENU width='100%' cellpadding='1' border='0' cellspacing='1'>
								<TR height='6'>
									<TD></TD>
								</TR>
								<%@include file="../jsp/testata.jsp" %>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>

		</tr>
		<tr>
			<td width="100%">
				<table width="100%">
				  <tbody>
				   	<tr>
				  	 <td width="3%"></td>
				  	 <td width="77%">
				  <form id="formUpdateExpertMode" name="formUpdateExpertMode" action="<%=qbeUrl.getUrl(request,null) %>" method="post">
					<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExecutionModality")%></span>
							&nbsp;
				 			<input type="hidden" name="ACTION_NAME" value="EXECUTE_QUERY_AND_SAVE_ACTION"/>
							<input type="hidden" name="SOURCE_FROM_QUERY_RESULT" value="QUERY_RESULT"/>
																																				
							<% if (aWizardObject.isUseExpertedVersion()) { %>
								<input type="radio" name="previewModeFromQueryResult" value="ComposedQuery" onclick="javascript:submitUpdatePreviewFromQueryResult()"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview")%>
								&nbsp;
								<input type="radio" name="previewModeFromQueryResult" value="ExpertMode" checked="checked" onclick="javascript:submitUpdatePreviewFromQueryResult()"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview")%>
							<%} else {%>
								<input type="radio" name="previewModeFromQueryResult" value="ComposedQuery" checked="checked" onclick="javascript:submitUpdatePreviewFromQueryResult()"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview")%>
								&nbsp;
								<input type="radio" name="previewModeFromQueryResult" value="ExpertMode" onclick="javascript:submitUpdatePreviewFromQueryResult()" > <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview")%>
							<%}%>
							</form>
						</td>	
						<td width="10%">
							&nbsp;
						</td>
						<td width="10%">
							<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertok.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Resume.ShowQueryTooltip") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.QueryResult.ShowQueryTooltip") %>" onclick="javascript:showQueryInQueryResult(event)" />	
						</td>
						</tr>	
					</tbody>
				</table>
			</td>
		</tr>
		
<% if (!flagErrors){ %>

		
		<tr>
	   		<td width="100%">
				<table width="100%" valign="top"> 
				
					<tr>
			   			<td width="3%">
						</td>
						<td width="97%">
						</td>
		   			</tr>
	   			 	<tr>
		   				<td></td> <%-- Rientro  --%>
						<td>
							<% java.util.Map sParams = new java.util.HashMap();
				  			   sParams.clear();
				  			   String urlPrev = "#";
				  			   String urlNext = "#";
				  			%>
							<% if (hasPreviousPage){ 
									sParams.clear();
			    		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
			    		   			sParams.put("query",query);
			    		   			sParams.put("pageNumber",String.valueOf(currentPage-1));
			    		   			sParams.put("ignoreJoins", "true");
			    		   			urlPrev = qbeUrl.getUrl(request, sParams);
			    		   			%>
								<a href="<%=urlPrev%>"> << </a>
							<% } %> 
							<%=qbeMsg.getMessage(requestContainer, "QBE.QueryResult.CurrentPage") %> <%=currentPage+1 %>
							<% if (hasNextPage){
								sParams.clear();
			    		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
			    		   			sParams.put("query",query);
			    		   			sParams.put("pageNumber",String.valueOf(currentPage+1));
			    		   			sParams.put("ignoreJoins", "true");
			    		   			urlNext = qbeUrl.getUrl(request, sParams);%>
								<a href="<%=urlNext%>"> >> </a>
							<% } %>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%">
				<table width="100%" valign="top">
				<tr>
					<td width="3%">
						&nbsp;						
					</td>
					<td width="97%">
						&nbsp;						
					</td>					
				
				</tr>
				<tr>
					<td></td>
					<td width="97%">
				<table  cellspacing="5" valign="top">

				<% 
					Iterator it = null;
					List headers = null;
					if (aWizardObject.isUseExpertedVersion()){%>
					<thead>
						<tr>
							<% headers = aWizardObject.extractExpertSelectFieldsList();
							   it = headers.iterator();
							   String headerName = "";
							   							   
							   while (it.hasNext()){
								   headerName = (String)it.next();
							%>
																		
							<th><%=headerName %></th>
							
							<% } %>
				
						</tr>
					</thead>	
			    <%}else{ %>  
					<thead>
						<tr>
						<% headers = aWizardObject.getSelectClause().getSelectFields(); 
						   it = headers.iterator();
						   String headerName = "";
						   ISelectField selField = null;
						   while (it.hasNext()){
							   selField = (ISelectField)it.next();
							   headerName = (selField.getFieldAlias() != null ? selField.getFieldAlias() : selField.getFieldName()); 
						%>
								<th><%=headerName %></th>
						<% } %>
						</tr>
					</thead>
  				<%}%>
				
					<tbody>
						<%
						   it = aList.iterator();
						   Object o = null;
						   Object[] row;
						   String cssClass = "odd";
						   while (it.hasNext()){%>
						   		<tr class="<%=cssClass %>">
						<% 		
								o = it.next();
							
								if (o instanceof Object[]){
									row = (Object[])o;
									// Giro le colonne
									for (int j=0; j < row.length; j++){ 
										String rowClass = null;
										if(j%2 == 0) {									
											rowClass = "portlet-section-alternate";
										} else {
											rowClass="portlet-section-body";
										}	
											%>
										<td class="<%=rowClass%>"><%=(row[j] != null ? row[j].toString() : "NULL") %></td>
									<%
									}		
								 } else { %>
									<td><%=(o != null ? o.toString() : "NULL") %></td>
							   <%}%>
									<%cssClass = (cssClass.equalsIgnoreCase("odd") ? "even" : "odd"); %>
								</tr>
						<% } %>
						<tr>
							<td width="10%">
								&nbsp;
							</td>
						</tr>
						</tbody>
				</table> 
				</td>
				</tr>
				</table>
			</td>
		</tr>
<%}else{ %>
		<tr>
	   		<td width="100%">
				<table width="100%" valign="top"> 	
	
	<% String joinMsg =  (String)aServiceResponse.getAttribute("JOIN_WARNINGS");
				
			   if (joinMsg != null){ %>
			   	<tr>
		   			<td width="3%">
					</td>
					<td width="97%">
					</td>
	   			</tr>
				<tr>
					<td></td>
	   				<td>
	   					<span class="qbeError"><%=qbeMsg.getMessage(requestContainer,msg).trim()%></span>
					</td>
				</tr>
				<tr>
					<td></td>				
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td></td>				
					<td>
						<textarea id="txtAreaMsgError" readonly="true" rows="10" cols="80"><%=joinMsg.trim() %></textarea>
					</td>
				</tr>
				
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				
				<tr>
					<td></td>				
					<td>
						<% Map eParams = new java.util.HashMap(); 
						eParams.put("ACTION_NAME", "EXECUTE_QUERY_AND_SAVE_ACTION");
						eParams.put("ignoreJoins", "true");
						%>
						<a href="<%=qbeUrl.getUrl(request, eParams) %>"><%=qbeMsg.getMessage(requestContainer, "QBE.JoinWarning.Continue") %>
					</td>
				</tr>
				<tr>
		 		<td >
						&nbsp;
					</td>
				</tr>
			<% } else {%>
				<tr>
		   			<td width="3%">
					</td>
					<td width="97%">
					</td>
					
	   			</tr>			
			
				<tr>
					<td></td>					
	   				<td>
	   					<span class="qbeError"><%=qbeMsg.getMessage(requestContainer,"QBE.Error.GenericError").trim()%></span>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td></td>					
	   				<td>
	   					<textarea id="txtAreaMsgError" readonly="true" rows="10" cols="80"><%=qbeMsg.getMessage(requestContainer,msg).trim()%></textarea>
					</td>
					
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			<% } %>
			</table>
		</td>
	</tr>
<%}%>
	</table>
		
 <% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<div id="divSpanCurrent">
<span id="currentScreen">DIV_EXEC</span>	
</div>
	
<div id="divQuery">
	<table>
	 <tr>
	  <td>
	   <textarea rows="10" cols="50" readonly="true"><%=finalQueryString%></textarea>		
	  </td>
	 </tr>
	</table>
</div>
	

<%@include file="../jsp/qbefooter.jsp" %>
