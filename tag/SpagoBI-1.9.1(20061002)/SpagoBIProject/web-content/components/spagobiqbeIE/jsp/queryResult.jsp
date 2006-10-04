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
	Integer pagesNumber = null;
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
	   pagesNumber = (Integer)listResponse.getAttribute("pagesNumber");
	   hasPreviousPage = ((Boolean)listResponse.getAttribute("hasPreviousPage")).booleanValue();
	   hasNextPage = ((Boolean)listResponse.getAttribute("hasNextPage")).booleanValue();
   }
   	String qbeQuery = null;
	String qbeSqlQuery = null;
	String expertQuery = null;
   	try{
  		 qbeQuery = aWizardObject.getFinalQuery();
  		 qbeSqlQuery = aWizardObject.getFinalSqlQuery(dm);
  		 expertQuery = aWizardObject.getExpertQueryDisplayed();
   	}catch(Throwable t){
   		t.printStackTrace();
   		qbeQuery = null;
   	}
	String finalQueryString = null;
	String queryLang = null;
	if (aWizardObject.isUseExpertedVersion()){
		finalQueryString = expertQuery;
		queryLang = "sql";
	}else{
		finalQueryString = qbeQuery;
		queryLang = "hql";
	} 
	
  	String jarFilePath = dm.getJarFile().toString();
  	String exportFormUrl = Utils.getReportServletContextAddress() + "/ReportServlet";
  	
  	
  
						
%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 

		    style='vertical-align:middle;padding-left:5px;'>
			<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Result.Preview", bundle) %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%@include file="../jsp/qbe_headers.jsp"%>
	</tr>
</table>


<%@include file="../jsp/testata.jsp" %>


<div class='div_background_no_img'>


<table width="100%">
		<tr>
			<td width="3%">
			</td>
  	 		<td width="64%">
				<form id="formUpdateExpertMode" name="formUpdateExpertMode" action="<%=qbeUrl.getUrl(request,null) %>" method="post">
				<table> <tr>				
					<td rowspan="2" width="30%">
					<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExecutionModality", bundle)%></span>
				 			<input type="hidden" name="ACTION_NAME" value="EXECUTE_QUERY_AND_SAVE_ACTION"/>
							<input type="hidden" name="SOURCE_FROM_QUERY_RESULT" value="QUERY_RESULT"/>
					</td>																																			
							<% if (aWizardObject.isUseExpertedVersion()) { %>
								<td width="20%">	
								<input type="radio" name="previewModeFromQueryResult" value="ComposedQuery" onclick="javascript:submitUpdatePreviewFromQueryResult()" title="<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview.Tooltip", bundle)%>"> 
									<span class="qbe-font">
									<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview", bundle)%>
									</span>
								</input>
								</td>	
								
								<td>
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertok.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Resume.ShowQueryTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.QueryResult.ShowQueryTooltip", bundle) %>" onclick="javascript:showQueryInQueryResult(event, 'divQbeQuery')" />	
								</td>
								
								<td width="50%">&nbsp;</td>								
								</tr>	
								
								<tr>
								<td width="20%">	
								<input type="radio" name="previewModeFromQueryResult" value="ExpertMode" checked="checked" onclick="javascript:submitUpdatePreviewFromQueryResult()" title="<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview.Tooltip", bundle)%>"> 
									<span class="qbe-font">
										<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview", bundle)%>
									</span>
								</input>
								</td>	
								<td>
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertok.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Resume.ShowQueryTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.QueryResult.ShowQueryTooltip", bundle) %>" onclick="javascript:showQueryInQueryResult(event, 'divExpertQuery')" />	
								</td>
								</tr>
							<%} else {%>
								<td width="20%px">	
								<input type="radio" name="previewModeFromQueryResult" value="ComposedQuery" checked="checked" onclick="javascript:submitUpdatePreviewFromQueryResult()" title="<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview.Tooltip", bundle)%>"> 
									<span class="qbe-font">
										<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview", bundle)%>
									</span>
								</input>
								</td>	
								
								<td>
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertok.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Resume.ShowQueryTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.QueryResult.ShowQueryTooltip", bundle) %>" onclick="javascript:showQueryInQueryResult(event, 'divQbeQuery')" />	
								</td>
								<td width="50%">&nbsp;</td>
								</tr>
								
								<tr>
								<td width="20%">	
								<input type="radio" name="previewModeFromQueryResult" value="ExpertMode" onclick="javascript:submitUpdatePreviewFromQueryResult()" title="<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview.Tooltip", bundle)%>"> 
									<span class="qbe-font">
										<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview", bundle)%>
									</span>
								</input>
								</td>	
								<td>
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertok.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Resume.ShowQueryTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.QueryResult.ShowQueryTooltip", bundle) %>" onclick="javascript:showQueryInQueryResult(event, 'divExpertQuery')" />	
								</td>
								</tr>
							<%}%>
				</table>
				</form>
			</td>	
			<td width="30%">
				<% if (finalQueryString != null){ %>
				<form 	id="formExport" 
						name="formExport" 
						action="<%=exportFormUrl%>" 
						method="post">	
				
				<input type="hidden" id="jarfilepath" name="jarfilepath" value="<%=jarFilePath%>"/>
  				<input type="hidden" id="query" name="query" value="<%=finalQueryString%>"/>
 				<input type="hidden" id="lang" name="lang" value="<%=queryLang%>"/>
 				
 				<%if(dm.getJndiDataSourceName() != null) {%>
  				<input type="hidden" id="jndiDataSourceName" name="jndiDataSourceName" value="<%=dm.getJndiDataSourceName()%>"/>
  				<%}%>
  				
  				<input type="hidden" id="dialect" name="dialect" value="<%=dm.getDialect()%>"/>
  				
  				<% if(aWizardObject.getQueryId() != null) {%>
  					<input type="hidden" id="queryName" name="queryName" value="<%=aWizardObject.getQueryId()%>"/>;
  				<% } %>
  				
				<table>
				<td width="40%"><span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExportFormatTitle", bundle)%></span></td>
				
				<td width="40%">
				<select name="format" >
				<option value="application/pdf" selected="selected">PDF</option>   				
   				<option value="application/vnd.ms-excel">XLS</option>
   				<option value="application/rtf">RTF</option>
   				<option value="text/html">HTML</option>
   				<option value="text/xml">XML</option>
   				<option value="text/plain">TXT</option>
   				<option value="text/csv">CSV</option>
  				</select>
				</td>
				
				<td width="20%">					
					<input 	type="image" src="<%=qbeUrl.conformStaticResourceLink(request,"../img/exec22.png")%>" value="submit" 
							alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Export", bundle) %>" 
							title="<%= qbeMsg.getMessage(requestContainer, "QBE.ExportTooltip", bundle) %>"/>	
				</td>
				</table>
				</form>
				<% } %>
			</td>
			
			<td width="3%"></td>			
		</tr>	
</table>
		
<% if (!flagErrors){ %>

<table width="100%" valign="top"> 
	<tr>
		<td width="3%">
		</td>
		<td width="94%">
		</td>
		<td width="3%">
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
				<a class="qbe-title-link" href="<%=urlPrev%>"> << </a>
			<% } %> 
			<span class="qbe-font">
			<%=qbeMsg.getMessage(requestContainer, "QBE.QueryResult.CurrentPage", bundle) %> <%=currentPage+1 %>
			<% if (pagesNumber != null){%>
				/<%=pagesNumber.intValue()%> 
			<% } %>
			</span>
			<% if (hasNextPage){
				sParams.clear();
   		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
   		   			sParams.put("query",query);
   		   			sParams.put("pageNumber",String.valueOf(currentPage+1));
   		   			sParams.put("ignoreJoins", "true");
   		   			urlNext = qbeUrl.getUrl(request, sParams);%>
				<a class="qbe-title-link" href="<%=urlNext%>"> >> </a>
			<% } %>
		</td>
		<td></td>
	</tr>
</table>


<table width="100%" valign="top">
	<tr>
		<td width="3%">
			&nbsp;						
		</td>
		<td width="94%">
			&nbsp;						
		</td>
		<td width="3%">
		</td>					
	</tr>
	<tr>
		<td></td>
		<td width="94%">
			<table>

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
																		
							<td class='portlet-section-header' style='vertical-align:middle;'><%=headerName %></td>
							
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
							   headerName = selField.getFieldName();
							   if(headerName.equalsIgnoreCase(selField.getFieldNameWithoutOperators()))
							   	headerName = (selField.getFieldAlias() != null ? selField.getFieldAlias() : headerName); 
							   else
								   if(selField.getFieldAlias() != null)
								   	headerName = headerName.replaceAll(selField.getFieldNameWithoutOperators(), selField.getFieldAlias());
						%>
								<td class='portlet-section-header' style='vertical-align:middle;'><%=headerName %></td>
						<% } %>
						</tr>
					</thead>
  				<%}%>
				
					<tbody>
						<%
						   it = aList.iterator();
						   Object o = null;
						   Object[] row;
						   String rowClass;
						   boolean alternate = false;
						   while (it.hasNext()){
					            rowClass = (alternate) ? "portlet-section-alternate" : "portlet-section-body";
					            alternate = !alternate;  
						   		%>
						   		<tr class='portlet-font'>
								<% 		
								o = it.next();
							
								if (o instanceof Object[]){
									row = (Object[])o;
									// Giro le colonne
									for (int j=0; j < row.length; j++){ 
										%>
										<td class="<%=rowClass%>"><%=(row[j] != null ? row[j].toString() : "NULL") %></td>
										<%
									}		
								 } else { %>
									<td class="<%=rowClass%>"><%=(o != null ? o.toString() : "NULL") %></td>
							   <%}%>
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
		<td></td>
	</tr>
</table>
				
<%}else{ %>

<table width="100%" valign="top"> 	
	
<% String joinMsg =  (String)aServiceResponse.getAttribute("JOIN_WARNINGS");
	
   if (joinMsg != null){ %>
	<tr>
		<td width="3%">
		</td>
		<td width="94%">
		</td>
		<td width="3%">
		</td>
 	</tr>
	<tr>
		<td></td>
 		<td>
 			<span class="qbeError"><%=qbeMsg.getMessage(requestContainer,msg, bundle).trim()%></span>
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>				
		<td>
			&nbsp;
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>				
		<td>
			<textarea id="txtAreaMsgError" readonly="true" rows="10" cols="80"><%=joinMsg.trim() %></textarea>
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td>
			&nbsp;
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>				
		<td>
			<% Map eParams = new java.util.HashMap(); 
			eParams.put("ACTION_NAME", "EXECUTE_QUERY_AND_SAVE_ACTION");
			eParams.put("ignoreJoins", "true");
			%>
			<a href="<%=qbeUrl.getUrl(request, eParams) %>"><%=qbeMsg.getMessage(requestContainer, "QBE.JoinWarning.Continue", bundle) %>
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td>
			&nbsp;
		</td>
		<td></td>
	</tr>
<% } else {%>
	<tr>
  		<td width="3%">
		</td>
		<td width="94%">
		</td>
		<td width="3%">
		</td>
	</tr>			
	<tr>
		<td></td>					
 		<td>
 			<span class="qbeError"><%=qbeMsg.getMessage(requestContainer,"QBE.Error.GenericError", bundle).trim()%></span>
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td>
			&nbsp;
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>					
 		<td>
 			<textarea id="txtAreaMsgError" readonly="true" rows="10" cols="80"><%=qbeMsg.getMessage(requestContainer,msg, bundle).trim()%></textarea>
		</td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td>
			&nbsp;
		</td>
		<td></td>
	</tr>
<% } %>
</table>
<%}%>

		
<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<div id="divSpanCurrent">
<span id="currentScreen">DIV_EXEC</span>	
</div>
	
<div id="divQbeQuery" style="position:absolute; padding:5px;display:none; z-index:1000">
	<table>
	 <tr>
	  <td>
	   <textarea rows="10" cols="50" readonly="true"><%=qbeSqlQuery%></textarea>		
	  </td>
	 </tr>
	</table>
</div>

<div id="divExpertQuery" style="position:absolute; padding:5px;display:none; z-index:1000">
	<table>
	 <tr>
	  <td>
	   <textarea rows="10" cols="50" readonly="true"><%=expertQuery%></textarea>		
	  </td>
	 </tr>
	</table>
</div>


<%@include file="../jsp/qbefooter.jsp" %>

</div>
