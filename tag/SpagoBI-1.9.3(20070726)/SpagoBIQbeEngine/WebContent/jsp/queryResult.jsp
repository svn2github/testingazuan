<%-- 

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

--%>

<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>

<%@ page import="it.eng.spago.base.*"%>
<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.urlgenerator.*"%>
<%@ page import="it.eng.qbe.wizard.*"%>
<%@ page import="it.eng.qbe.export.*"%>
<%@ page import="it.eng.qbe.utilities.*"%>
<%@ page import="java.util.*"%>




<%@ include file="../jsp/qbe_base.jsp" %>

<% 
   Object spagoBiInfo = sessionContainer.getAttribute("spagobi"); 

	String paddingStyle = "padding-left:4px;padding-right:4px";


   ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(sessionContainer);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
     

     
   String msg =  (String)aServiceResponse.getAttribute("ERROR_MSG");
   boolean flagErrors = false;
   java.util.List aList = null;
   String className = null;
	String query = null;
	int currentPage = -1;
	Integer pagesNumber = null;
	boolean hasPreviousPage = false;
	boolean hasNextPage = false;
	boolean overflow = false;
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
	   Boolean b = (Boolean)listResponse.getAttribute("overflow");
	   overflow = (b == null)? false: b.booleanValue();
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
  	
  
	List calculateFields = new ArrayList();
	if (aWizardObject.getSelectClause() != null){
		calculateFields.addAll(aWizardObject.getSelectClause().getCalcuatedFields());
		calculateFields.addAll(Utils.getCalculatedFields(aWizardObject,dm));
	}
  	try{
		for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
  			((CalculatedField)itCalcFields.next()).calculateMappings(aWizardObject);
  		}
  	}catch(Throwable t){
  		t.printStackTrace();
  	}
  	
  	GroovyScriptEngine gse = GroovyEngine.getGroovyEngine().getGroovyScriptEngine();
  	Binding binding = new Binding();
  	binding.setVariable("qbe_mode", "exec");
  	String calcFldHdr = null;
  	
  	String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
  	String calculateFieldPosition = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.calculateFieldPosition");
%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<%@page import="groovy.util.GroovyScriptEngine"%>
<%@page import="groovy.lang.Binding"%>
<%@page import="it.eng.spago.configuration.ConfigSingleton"%>
<body>

<script>
function askConfirmation (message) {

	if (confirm(message))
	{
		ajxPersistTemporaryQueryAction();
	}
	else
	{
		
	}	
}
</script>

<%}%>

<%
	if(spagoBiInfo == null) {
%>
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
<%
	}
%>

<%@include file="../jsp/testata.jsp" %>


<div class='div_background_no_img'>


<table width="100%">
		<tr>
			<td width="3%">
			</td>
  	 		<td width="64%">
				<form id="formUpdateExpertMode" name="formUpdateExpertMode" action="<%=qbeUrl.getUrl(request,null) %>" method="post">
				<input type="hidden" name="ACTION_NAME" value="EXECUTE_QUERY_AND_SAVE_ACTION"/>
				<input type="hidden" name="SOURCE_FROM_QUERY_RESULT" value="QUERY_RESULT"/>
						
				<table> 																																							
					<% 
					String editableStr = null;
					Map functionalities = (Map)sessionContainer.getAttribute("FUNCTIONALITIES");
					
					if(functionalities != null) {
						Properties props = (Properties)functionalities.get("expertQuery");
						if(props != null) editableStr = props.getProperty("editable");
					}
					
					if(editableStr != null && editableStr.equalsIgnoreCase("FALSE")) { %>
					
					
					
					<%} else { %>
						<tr>				
						<td rowspan="2" width="30%">
							<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExecutionModality", bundle)%></span>
					 	</td>	
					 <%	if (aWizardObject.isUseExpertedVersion()) { %>
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
								<%}
							}%>
				</table>
				</form>
			</td>	
			<td width="30%">
				<% if (finalQueryString != null){ %>
				<form 	id="formExport" 
						name="formExport" 
						action="<%=exportFormUrl%>" 
						method="post">	
				<input type="hidden" id="_savedObjectId" name="_savedObjectId" value=""/>
				<input type="hidden" id="jarfilepath" name="jarfilepath" value="<%=jarFilePath%>"/>
  				<input type="hidden" id="query" name="query" value="<%=finalQueryString%>"/>
 				<input type="hidden" id="lang" name="lang" value="<%=queryLang%>"/>
  				<input type="hidden" id="jndiDataSourceName" name="jndiDataSourceName" value="<%=dm.getJndiDataSourceName()%>"/>
  				<input type="hidden" id="dialect" name="dialect" value="<%=dm.getDialect()%>"/>
  				<input type="hidden" id="orderedFldList" name="orderedFldList" value="<%=Utils.getOrderedFieldList(aWizardObject)%>"/>
  				<input type="hidden" id="extractedEntitiesList" name="extractedEntitiesList" value="<%=Utils.getSelectedEntitiesAsString(aWizardObject)%>"/>
  				
  				<% if(aWizardObject.getQueryId() != null) {%>
  					<input type="hidden" id="queryName" name="queryName" value="<%=aWizardObject.getQueryId()%>"/>
  				<% } %>
  				
				<table>
					<tr>
						
						<td width="40%">
							<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExportFormatTitle", bundle)%></span>
						</td>
				
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
							
							<% if (overflow){ %>
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/exec22.png")%>"  
									alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Export", bundle) %>" 
									title="<%= qbeMsg.getMessage(requestContainer, "QBE.ExportTooltip", bundle) %>"
									onclick='askConfirmation("<%=qbeMsg.getMessage(requestContainer, "QBE.Exportation.warning", bundle)%>")'/>
							<%} else {%> 
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/exec22.png")%>"  
									alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Export", bundle) %>" 
									title="<%= qbeMsg.getMessage(requestContainer, "QBE.ExportTooltip", bundle) %>"
									onclick='ajxPersistTemporaryQueryAction();'/>
							<% }%>
								
									
						</td>
					</tr>
					<tr>
						<td width="40%">
							<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.MaterializeView", bundle)%></span>
						</td>
						<td width="60%" align="left">
							
							
								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/mview2.gif")%>"
									 alt="<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.MaterializeView", bundle)%>"
									 title="<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.MaterializeView", bundle)%>"
									 onclick="ajxCreateViewFromCurrentQuery();"/>
									 
							
						</td>
					</tr>
				</table>
				</form>
				<% } %>
			</td>
			
			<td width="3%"></td>			
		</tr>	
</table>
		
				
<% if (queryLang.equalsIgnoreCase("sql") && overflow){ %>
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
			&nbsp;
		</td>
		<td></td> <%-- Rientro  --%>
	</tr>
 	<tr>
 		<td></td> <%-- Rientro  --%>
		<td>
			<span class="qbeError">Warning !!!</span>
		</td>
		<td></td> <%-- Rientro  --%>
	</tr>
	<tr>
 		<td></td> <%-- Rientro  --%>
		<td>
			&nbsp;
		</td>
		<td></td> <%-- Rientro  --%>
	</tr>
 	<tr>
 		<td></td> <%-- Rientro  --%>
		<td>
			<textarea id="txtAreaMsgError" readonly="true" rows="3" cols="80"><%=qbeMsg.getMessage(requestContainer, "QBE.Execution.warning", bundle) %></textarea>
		</td>
		<td></td> <%-- Rientro  --%>
	</tr>
	<tr>
 		<td></td> <%-- Rientro  --%>
		<td>
			&nbsp;
		</td>
		<td></td> <%-- Rientro  --%>
	</tr>
</table>
<%}%>
		
<% if (!flagErrors){ %>

<p>

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
  				int[] pages = PageNavigatorUtils.getPageWindow(currentPage+1, pagesNumber.intValue(), 6);
  			%>
  			<% if (hasPreviousPage){ 
					sParams.clear();
   		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
   		   			sParams.put("query",query);
   		   			sParams.put("pageNumber",String.valueOf(0));
   		   			sParams.put("ignoreJoins", "true");
   		   			urlPrev = qbeUrl.getUrl(request, sParams);
   		   			%>
				<a class="qbe-title-link" href="<%=urlPrev%>"> << </a>
			<% } %> 
			<% if (hasPreviousPage){ 
					sParams.clear();
   		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
   		   			sParams.put("query",query);
   		   			sParams.put("pageNumber",String.valueOf(currentPage-1));
   		   			sParams.put("ignoreJoins", "true");
   		   			urlPrev = qbeUrl.getUrl(request, sParams);
   		   			%>
				<a class="qbe-title-link" href="<%=urlPrev%>"> < </a>
			<% } %> 
			<span class="qbe-font">
			<%
				for(int y = 0; y < pages.length; y++) {
					if((pages[y]-1) == currentPage) {
			%>
			&nbsp;[<%=pages[y]%>]&nbsp;
			<%
					} else {
						sParams.clear();
	   		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
	   		   			sParams.put("query",query);
	   		   			sParams.put("pageNumber",String.valueOf(pages[y]-1));
	   		   			sParams.put("ignoreJoins", "true");
	   		   			urlNext = qbeUrl.getUrl(request, sParams);
	   		%>
			&nbsp;<a class="qbe-title-link" href="<%=urlNext%>"> <%=pages[y]%> </a>&nbsp;
			<% 		}
				}
			%>
			</span>
			<% if (hasNextPage){
				sParams.clear();
   		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
   		   			sParams.put("query",query);
   		   			sParams.put("pageNumber",String.valueOf(currentPage+1));
   		   			sParams.put("ignoreJoins", "true");
   		   			urlNext = qbeUrl.getUrl(request, sParams);%>
				<a class="qbe-title-link" href="<%=urlNext%>"> > </a>
			<% } %>
			<% if (hasNextPage){
				sParams.clear();
   		   			sParams.put("ACTION_NAME","EXECUTE_QUERY_AND_SAVE_ACTION");
   		   			sParams.put("query",query);
   		   			sParams.put("pageNumber",String.valueOf(pagesNumber.intValue()-1));
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
							<% if (calculateFieldPosition != null && calculateFieldPosition.equalsIgnoreCase("BEFORE_COLUMNS")){ 
								 for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
								  		calcFldHdr =  ((CalculatedField)itCalcFields.next()).getFldLabel();
								  		%>
										<td class='portlet-section-header' style='vertical-align:middle;<%=paddingStyle%>'><%=calcFldHdr %></td>
										<%
								 }
							 	} %>
							<% headers = aWizardObject.extractExpertSelectFieldsList();
							   it = headers.iterator();
							   String headerName = "";
							   							   
							   while (it.hasNext()){
								   headerName = (String)it.next();
							%>
																		
								<td class='portlet-section-header' style='vertical-align:middle;<%=paddingStyle%>'><%=headerName %></td>
							
							<% } %>
							<% if (calculateFieldPosition != null && calculateFieldPosition.equalsIgnoreCase("AFTER_COLUMNS")){ 
								 for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
								  		calcFldHdr =  ((CalculatedField)itCalcFields.next()).getFldLabel();
								  		%>
										<td class='portlet-section-header' style='vertical-align:middle;<%=paddingStyle%>'><%=calcFldHdr %></td>
										<%
								 }
							 	} %>
							
						</tr>
					</thead>	
			    <%}else{ %>  
					<thead>
						<tr>
						<% if (calculateFieldPosition != null && calculateFieldPosition.equalsIgnoreCase("BEFORE_COLUMNS")){ 
								 for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
								  		calcFldHdr =  ((CalculatedField)itCalcFields.next()).getFldLabel();
								  		%>
										<td class='portlet-section-header' style='vertical-align:middle;<%=paddingStyle%>'><%=calcFldHdr %></td>
										<%
								 }
							 	} %>
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
								<td class='portlet-section-header' style='vertical-align:middle;<%=paddingStyle%>'><%=headerName %></td>
						<% } %>
						<% if (calculateFieldPosition != null && calculateFieldPosition.equalsIgnoreCase("AFTER_COLUMNS")){ 
								 for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
								  		calcFldHdr =  ((CalculatedField)itCalcFields.next()).getFldLabel();
								  		%>
										<td class='portlet-section-header' style='vertical-align:middle;<%=paddingStyle%>'><%=calcFldHdr %></td>
										<%
								 }
							 	} %>
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
								
							    if (!(o instanceof Object[])){
							    	row = new Object[1];
							    	row[0] = o.toString();
							    }else{
							    	row = (Object[])o;
							    }
								
								
								String calcFld = "";
								if (calculateFieldPosition != null && calculateFieldPosition.equalsIgnoreCase("BEFORE_COLUMNS")){
										for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
								  			calcFld =  ((CalculatedField)itCalcFields.next()).calculate(gse, row, binding ).toString();
								  			%>
											<td class="<%=rowClass%>" style='<%=paddingStyle%>'><%=calcFld %></td>
											<%
								  		}
								}
									// Giro le colonne
								for (int j=0; j < row.length; j++){ 
										%>
										<td class="<%=rowClass%>" style='<%=paddingStyle%>'><%=(row[j] != null ? row[j].toString() : "NULL") %></td>
										<%
								}
									
								if (calculateFieldPosition != null && calculateFieldPosition.equalsIgnoreCase("AFTER_COLUMNS")){
										for (Iterator itCalcFields = calculateFields.iterator(); itCalcFields.hasNext(); ){
								  			calcFld =  ((CalculatedField)itCalcFields.next()).calculate(gse, row, binding ).toString();
								  			%>
											<td class="<%=rowClass%>" style='<%=paddingStyle%>'><%=calcFld %></td>
											<%
								  		}
								}%>
							</tr>	
							<%} %>
									
								
					
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
 			<span class="qbeError"><%=qbeMsg.getMessage(requestContainer,msg, bundle)%></span>
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
 			<span class="qbeError"><%=qbeMsg.getMessage(requestContainer,"QBE.Error.GenericError", bundle)%></span>
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
 			<textarea id="txtAreaMsgError" readonly="true" rows="10" cols="80"><%=msg%></textarea>
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
