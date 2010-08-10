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

<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.urlgenerator.*"%>
<%@ page import="it.eng.qbe.export.*"%>




<%@ include file="/jsp/qbe_base.jsp" %>

<%       
   datamartWizard.composeQuery(datamartModel);   
%>

<qbe:page>
 	<qbe:page-content>
		
		<%@include file="/jsp/commons/titlebar.jspf" %>
		<%@include file="/jsp/testata.jsp" %>	
 
 
		<div class='div_background_no_img'>


<%
	String editableStr = null;
	//Map functionalities = (Map)sessionContainer.getAttribute("FUNCTIONALITIES");
	
	if(functionalities != null) {
		Properties props = (Properties)functionalities.get("expertQuery");
		if(props != null) editableStr = props.getProperty("editable");
	}
	
	if(editableStr != null && editableStr.equalsIgnoreCase("FALSE")) {
		if (!query.isEmpty()){
%>
	<table width="100%">
	<tr>
		<td width="2%">
			&nbsp;
		</td>
		<td width="86%">
			&nbsp;
		</td>
		<td width="2%">
			&nbsp;
		</td>
	</tr>			
	
	
	
	
	<tr>  
		
		
		<%-- Left space column  --%>
		<td>&nbsp;</td> 
	 
	 
	 
	  <%-- Start Left column  --%>
		<td class="qbe-td-form">
			<table valign="top">
				<tr>
					<td>
						<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.QbeAutomaticallyComposition", bundle)%></span>
					</td>
				<tr>						
				<tr>
					<td>
						&nbsp;
					</td>
				<tr>
				<tr>
					<td>
						<table class="qbe-font" width="100%">
						<%  int rowsCounter = 1;
							if (datamartWizard.getFinalQuery() != null){ 
									 
									 if (true){ %>
									
							<tr border=2>
								<td colspan="2"> <b>Select </b> 
										<% if (query.getDistinct()) {%>
								    		<b> distinct</b>
								    	<% } %>
								</td> 			
							</tr>
									
									<%
																									   Iterator it = query.getSelectFieldsIterator();
																									   ISelectField selectF = null;
																									   while (it.hasNext()){
																										   selectF = (ISelectField)it.next();
																										   rowsCounter++;
																		%>
								    	<tr>
								    	<td>&nbsp;<%=selectF.getFieldName()%> 
								    	<%
 								    		if (selectF.getFieldAlias() != null) {
 								    	%>
								    		as <%=selectF.getFieldAlias()%>
								    	<%
								    		}
								    	%>
								    	<%
								    		if (it.hasNext()){
								    	%>
								    		,
								    	<%
								    		}
								    	%>
								    	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									
									<%
																			if (!query.isEmpty()) { 
																										rowsCounter++;
																		%>
									<tr>
										<td colspan="2"><b> From </b></td> 
										
									</tr>
									
									<%
																			
																									  Iterator it = query.getEntityClassesIterator();;
																									   EntityClass ec = null;
																									   while (it.hasNext()){
																										   ec = (EntityClass)it.next();
																										   rowsCounter++;
																		%>
								    	<tr>
								    	<td>&nbsp;<%=ec.getClassName()%> 
								    	<%
 								    		if (ec.getClassAlias() != null) {
 								    	%>
								    		as <%=ec.getClassAlias()%>
								    	<%
								    		}
								    	%>
								    	<%
								    		if (it.hasNext()){
								    	%>
								    		,
								    	<%
								    		}
								    	%>
								    	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									
									<%
																			if (query.getEntityClassesIterator().hasNext()){ 
																										rowsCounter++;
																		%>
									   <tr>
										<td colspan="2"> <b>Where</b> </td>
									   </tr>
									<%
										 
																Iterator it = query.getWhereFieldsIterator();
																   IWhereField conditionF = null;
																   while (it.hasNext()){
																	   conditionF = (IWhereField)it.next();
																	   rowsCounter++;
																	   String leftBracketsStr = "";
																	   for(int j = 0; j < conditionF.getLeftBracketsNum(); j++) leftBracketsStr += "(";
															    
																	   String rightBracketsStr = "";
																	   for(int j = 0; j < conditionF.getRightBracketsNum(); j++) rightBracketsStr += ")";
									%>
								    	<tr>
								    	<td>&nbsp;<%=leftBracketsStr + conditionF.getFieldName()%>
								    	<b><%=" " + conditionF.getFieldOperator() + " "%></b> 
								    	<%
 								    		if ((conditionF.getFieldEntityClassForRightCondition() == null)&&(conditionF.getType().endsWith("StringType"))){
 								    	%>
								    	<%=" '"+conditionF.getFieldValue()+ "' "%>
								    	<%
								    		}else{
								    	%>
								    	<%=" "+ conditionF.getFieldValue() + " "%>
								    	<%
								    		}
								    	%>
								    	<%=rightBracketsStr%>
								    	<%
								    		if (it.hasNext()) {
								    	%>
								    	&nbsp;<%=conditionF.getNextBooleanOperator()%>
								    	<%
								    		}
								    	%>
								    	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									
									
									<%
										if (query.getGroupByFieldsIterator().hasNext()){
											rowsCounter++;
									%>
									<tr>
										<td colspan="2"> <b>Group By</b> </td>
									  </tr>
									<%
																   Iterator it = query.getGroupByFieldsIterator();
																   IGroupByField groupF = null;
																   while (it.hasNext()){
																	   groupF = (IGroupByField)it.next();
																	   rowsCounter++;
									%>
								    	<tr>
								    		<td>&nbsp;<%=groupF.getFieldName()%>
								    		<%
								    			if (it.hasNext()){
								    		%>
								    		,
								    		<%
								    			}
								    		%>
								    		</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									<%
										if (query.getOrderByFieldsIterator().hasNext()){ 
																	rowsCounter++;
									%>
									<tr>
										<td colspan="2"><b> Order By</b> </td>
									   </tr>
									<%
										Iterator it = query.getOrderByFieldsIterator();
										IOrderByField orderF = null;
										while (it.hasNext()){
										   orderF = (IOrderByField)it.next();
										   rowsCounter++;
									%>
								    	<tr>
								    	<td>&nbsp;<%=orderF.getFieldName()%> 
								    	&nbsp;<%=orderF.isAscendingOrder()? "ASC": "DESC"%> 
								    	<%
 								    		if (it.hasNext()){
 								    	%>
								    		,
								    		<%
 								    		}
 								    	%>
								    	 	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
								
									<%
																		if (rowsCounter<11) rowsCounter=10;
																																								else {
																																									rowsCounter = (int)Math.floor((10 + (rowsCounter-10)*1.5));

																																								}
																	%>
							
							<%
															}
														%>	
						</table>
					</td>	
				</tr>		
			</table>
		</td>
		
		<%-- Left space column  --%>
		<td>&nbsp;</td>
	</tr>
</table>
	<%
		} else {
	%>
<table width="100%">
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
		<td valign="top">
			<span class="qbeError"><%=qbeMsg.getMessage(requestContainer, "QBE.Warning.NoFieldSelected", bundle)%></span>	
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
</table>
  
<div id="divSpanCurrent">
 <span id="currentScreen">DIV_RESUME_QUERY</span>
</div>
  
	<%
  		}
  	%>

<%
	} else if (!query.isEmpty()){
%> 
<table width="100%">
	<tr>
		<td width="2%">
			&nbsp;
		</td>
		<td width="40%">
			&nbsp;
		</td>
		<td width="16%">
			&nbsp;
		</td>
		<td width="40%">
			&nbsp;
		</td>
		<td width="2%">
			&nbsp;
		</td>
	</tr>			
	
	
	
	
	<tr>  
		
		
		<%-- Left space column  --%>
		<td>&nbsp;</td> 
	 
	 
	 
	  <%-- Start Left column  --%>
		<td class="qbe-td-form">
			<table valign="top">
				<tr>
					<td>
						<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.QbeAutomaticallyComposition", bundle)%></span>
					</td>
				<tr>						
				<tr>
					<td>
						&nbsp;
					</td>
				<tr>
				<tr>
					<td>
						<table class="qbe-font" width="100%">
						<%
							int rowsCounter = 1;
															if (datamartWizard.getFinalQuery() != null){ 
																	 
																	 if (true){
						%>
									
							<tr border=2>
								<td colspan="2"> <b>Select </b> 
										<%
 											if (query.getDistinct()) {
 										%>
								    		<b> distinct</b>
								    	<%
								    		}
								    	%>
								</td> 			
							</tr>
									
									<%
																			Iterator it = query.getSelectFieldsIterator();
																																									   ISelectField selectF = null;
																																									   while (it.hasNext()){
																																										   selectF = (ISelectField)it.next();
																																										   rowsCounter++;
																		%>
								    	<tr>
								    	<td>&nbsp;<%=selectF.getFieldName()%> 
								    	<%
 								    		if (selectF.getFieldAlias() != null) {
 								    	%>
								    		as <%=selectF.getFieldAlias()%>
								    	<%
								    		}
								    	%>
								    	<%
								    		if (it.hasNext()){
								    	%>
								    		,
								    	<%
								    		}
								    	%>
								    	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									
									<%
																			if (!query.isEmpty()) { 
																										rowsCounter++;
																		%>
									<tr>
										<td colspan="2"><b> From </b></td> 
										
									</tr>
									
									<%
																			Iterator it = query.getEntityClassesIterator();
																									   EntityClass ec = null;
																									   while (it.hasNext()){
																										   ec = (EntityClass)it.next();
																										   rowsCounter++;
																		%>
								    	<tr>
								    	<td>&nbsp;<%=ec.getClassName()%> 
								    	<%
 								    		if (ec.getClassAlias() != null) {
 								    	%>
								    		as <%=ec.getClassAlias()%>
								    	<%
								    		}
								    	%>
								    	<%
								    		if (it.hasNext()){
								    	%>
								    		,
								    	<%
								    		}
								    	%>
								    	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									
									<%
																			if (query.getEntityClassesIterator().hasNext()){ 
																										rowsCounter++;
																		%>
									   <tr>
										<td colspan="2"> <b>Where</b> </td>
									   </tr>
									<%
										java.util.Iterator it = query.getWhereFieldsIterator();
																   IWhereField conditionF = null;
																   while (it.hasNext()){
																	   conditionF = (IWhereField)it.next();
																	   rowsCounter++;
																	   String leftBracketsStr = "";
																	   for(int j = 0; j < conditionF.getLeftBracketsNum(); j++) leftBracketsStr += "(";
															    
																	   String rightBracketsStr = "";
																	   for(int j = 0; j < conditionF.getRightBracketsNum(); j++) rightBracketsStr += ")";
									%>
								    	<tr>
								    	<td>&nbsp;<%=leftBracketsStr + conditionF.getFieldName()%>
								    	<b><%=" " + conditionF.getFieldOperator() + " "%></b> 
								    	<%
 								    		if ((conditionF.getFieldEntityClassForRightCondition() == null)&&(conditionF.getType().endsWith("StringType"))){
 								    	%>
								    	<%=" '"+conditionF.getFieldValue()+ "' "%>
								    	<%
								    		}else{
								    	%>
								    	<%=" "+ conditionF.getFieldValue() + " "%>
								    	<%
								    		}
								    	%>
								    	<%=rightBracketsStr%>
								    	<%
								    		if (it.hasNext()) {
								    	%>
								    	&nbsp;<%=conditionF.getNextBooleanOperator()%>
								    	<%
								    		}
								    	%>
								    	</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									
									
									<%
										if (query.getGroupByFieldsIterator().hasNext()){
											rowsCounter++;
									%>
									<tr>
										<td colspan="2"> <b>Group By</b> </td>
									  </tr>
									<%
										
																   Iterator it = query.getGroupByFieldsIterator();
																   IGroupByField groupF = null;
																   while (it.hasNext()){
																	   groupF = (IGroupByField)it.next();
																	   rowsCounter++;
									%>
								    	<tr>
								    		<td>&nbsp;<%=groupF.getFieldName()%>
								    		<%
								    			if (it.hasNext()){
								    		%>
								    		,
								    		<%
								    			}
								    		%>
								    		</td>
								    	</tr>
									<%
										}
									%>
									<%
										}
									%>
									<%
										if (query.getOrderByFieldsIterator().hasNext()){ 
																	rowsCounter++;
									%>
									<tr>
										<td colspan="2"><b> Order By</b> </td>
									   </tr>
									<%
										
																   Iterator it = query.getOrderByFieldsIterator();
																   IOrderByField orderF = null;
																   while (it.hasNext()){
																	   orderF = (IOrderByField)it.next();
																	   rowsCounter++;
									%>
								    	<tr>
								    	<td>&nbsp;<%= orderF.getFieldName() %> 
								    	&nbsp;<%= orderF.isAscendingOrder()? "ASC": "DESC"%> 
								    	<% if (it.hasNext()){ %>
								    		,
								    		<% } %>
								    	 	</td>
								    	</tr>
									<%    } %>
									<%  } %>
								
									<% 
									
										if (rowsCounter<11) rowsCounter=10;
										else {
											rowsCounter = (int)Math.floor((10 + (rowsCounter-10)*1.5));

										}
										
									%>
							
							<%} %>	
						</table>
					</td>	
				</tr>		
			</table>
		</td>
    <%-- End Left column  --%>


  <%-- Start central column --%>
	  <td align="center" valign="middle">
	   				<img src="<%=qbeUrl.getResourceUrl(request,"../img/alignexpert.gif")%>" 
	   						 alt="<%=qbeMsg.getMessage(requestContainer,"QBE.alt.imgRresumeFromQbe", bundle) %>" 
	   						 title="<%=qbeMsg.getMessage(requestContainer,"QBE.alt.imgResumeFromQbe", bundle) %>" 
	   						 onclick="javascript:resumeFromQbe()"/>
						<br/>
						<a href="javascript:resumeFromQbe()" class="qbe-title-link" >
							 <%=qbeMsg.getMessage(requestContainer, "QBE.alt.resumeFromQbe", bundle) %>						
						</a>
	  </td>
	  <%-- End central column --%>



	  <%-- Start right column --%>
	  <td valign="top" class="qbe-td-form">
				<table valign="top" >
				<tr>
					<td colspan="3">
						<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.ExpertComposition", bundle)%></span>
					</td>
				<tr>						
				<tr>
					<td colspan="3">
						&nbsp;
					</td>
				<tr>					
				<tr>
					<td colspan="3">
 			 			<%  String strTextArea = "";
						 				if ((datamartWizard.getExpertQueryDisplayed() == null)||(datamartWizard.getExpertQueryDisplayed().trim().length() == 0)){
						 					try{
						 						strTextArea = datamartWizard.getFinalSqlQuery(datamartModel);
						 					}catch(Throwable t){
						 						strTextArea = "";
						 					}
						 					if (strTextArea == null){
						 						strTextArea = "";
						 					}
						 				}else{
						 					strTextArea = datamartWizard.getExpertQueryDisplayed();
						 				}
						%>
					 	<textarea name="expertSelectTextArea" id="expertSelectTextArea" rows="<%=rowsCounter%>" cols="50"><%=strTextArea%></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						&nbsp;
					</td>
				<tr>	
				<tr>
					<td>
						<img src="<%=qbeUrl.getResourceUrl(request,"../img/expertclose.gif")%>" 
							 alt="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgResumeLastExpert", bundle) %>"  
							 title="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgResumeLastExpert", bundle) %>" 
							 onclick="javascript:resumeLastExpert()"/> 
						<a href="javascript:submitUpdatePreview('RESUME_LAST_EXPERT_LINK')"
						   class="qbe-title-link">
							 <%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgResumeLastExpert", bundle) %>
						</a>
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						<img src="<%=qbeUrl.getResourceUrl(request,"../img/expertok.gif")%>" 
							 alt="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgExpertSave", bundle) %>"  
							 title="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgExpertSave", bundle) %>" 
				             onclick="javascript:saveExpertSelect()"/>
				        <a href="javascript:saveExpertSelect()"
				           class="qbe-title-link">
                       		 <%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgExpertSave", bundle) %>
                       	</a>
					</td>
				</tr>								
			</table>
		</td>
		<%-- Start right column --%>
		 
		  
		
		 
		<%-- Right space column --%> 
		<td>&nbsp;</td>
		
		
		
	</tr>
	

	
	
	<tr>
		<td colspan="5">
			&nbsp;
	  </td>
	</tr>
	
	
	
	
	
	<tr>
		<td></td>			
		<td>
		    <div class="qbe-td-form">
				<form id="formUpdateExpert" name="formUpdateExpert" action="<%=qbeUrl.getActionUrl(request,null) %>" method="post">
				<span class="qbeTitle">
						<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExecutionModality", bundle)%>
				</span>
				&nbsp;
				<input type="hidden" name="ACTION_NAME" value="UPDATE_PREVIEW_MODE_ACTION"/>
				<input type="hidden" id="formUpdateExpert_Source" name="SOURCE" value="RADIO_BUTTON"/>
				<input type="hidden" id="formUpdateExpert_expertTA" name="EXPERT_DISPLAYED" value=""/>												
				<% if (datamartWizard.isUseExpertedVersion()) { %>
					<input type="radio" name="previewMode" value="ComposedQuery" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')"><span class="qbe-font"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview", bundle)%></span>
					&nbsp;
					<input type="radio" name="previewMode" value="ExpertMode" checked="checked" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')"><span class="qbe-font"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview", bundle)%></span>
				<%} else {%>
					<input type="radio" name="previewMode" value="ComposedQuery" checked="checked" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')"><span class="qbe-font"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview", bundle)%></span>
					&nbsp;
					<input type="radio" name="previewMode" value="ExpertMode" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')" ><span class="qbe-font"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview", bundle)%></span>
				<%}%>
			</form>
			</div>
		</td>
    <td colspan="3">&nbsp</td>
	</tr>

	
</table> 

<div id="divSpanCurrent">
 <span id="currentScreen">DIV_RESUME_QUERY_SELECT_OK</span>
</div>


	   
<%} else { %>
<table width="100%">
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
		<td valign="top">
			<span class="qbeError"><%=qbeMsg.getMessage(requestContainer, "QBE.Warning.NoFieldSelected", bundle) %></span>	
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
</table>
  
<div id="divSpanCurrent">
 <span id="currentScreen">DIV_RESUME_QUERY</span>
</div>
  
<%}%> 


<form id="expertForm" name="expertForm" action="<%=qbeUrl.getActionUrl(request,null) %>" method="POST">
 	<input type="hidden" id="expertFormActionName" name="ACTION_NAME" value="UPDATE_EXPERT_ACTION"/>
 	<input type="hidden" id="expertSelectTA" name="expertSelectTA" value="">
</form>

<form id="formUpdateConditions" name="formUpdateConditions" action="<%=qbeUrl.getActionUrl(request,null) %>" method="POST">
 	<input type="hidden" name="ACTION_NAME" value="UPDATE_FIELD_RESUME_ACTION"/>
 	<input type="hidden" id="nextActionAfterSaveCondition"  name="NEXT_ACTION" value=""/>
	<input type="hidden" id="nextPublisherAfterSaveCondition" name="NEXT_PUBLISHER" value=""/>
	 	
 	<input type="hidden" id="expertDisplayedForUpdate" name="expertDisplayedForUpdate" value="">
</form>




		<script type="text/javascript">
			changeTabBkg();
		</script>
	
	
		</div>

	</qbe:page-content>
</qbe:page>
