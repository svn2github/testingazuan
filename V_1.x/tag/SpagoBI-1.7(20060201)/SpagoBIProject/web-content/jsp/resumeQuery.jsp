<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="it.eng.spago.base.*, it.eng.qbe.utility.*,it.eng.qbe.javascript.*, it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>



<%@ include file="../jsp/qbe_base.jsp" %>

<% 
   
   ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
   
   aWizardObject.composeQuery();
   dm.updateCurrentClassLoader();
   
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
							<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Resume.Query") %>
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
		
<% if ((aWizardObject.getEntityClasses() != null) && (aWizardObject.getEntityClasses().size() > 0)){%> 
	<tr>
	   		<td width="100%">
	   		<table width="100%">
	   		
	   			<tr>
					<td width="3%">
						&nbsp;
					</td>
					<td width="42%">
						&nbsp;
					</td>
					<td width="10%">
						&nbsp;
					</td>
					<td width="45%">
						&nbsp;
					</td>
				</tr>
				
	   			<tr>  
	   			<td></td> <%-- Rientro  --%>
	   			<%-- Colonna di SX  --%>
	   			<td>
					<table valign="top">
						<tr>
							<td>
		   						<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.QbeAutomaticallyComposition")%></span>
							</td>
						<tr>						
						<tr>
							<td>
								&nbsp;
							</td>
						<tr>
						<tr border=5>
							<td>
								<table width="100%">
									<% int rowsCounter = 1;
										if (aWizardObject.getSelectClause() != null){ %>
									
									<tr border=2>
										<td colspan="2"> <b>Select </b> 
										<% if (aWizardObject.getDistinct()) {%>
								    		<b> distinct</b>
								    	<% } %>
										</td> 
										
									</tr>
									
									<% List selectedFields = aWizardObject.getSelectClause().getSelectFields(); 
									   java.util.Iterator it = selectedFields.iterator();
									   ISelectField selectF = null;
									   while (it.hasNext()){
										   selectF = (ISelectField)it.next();
										   rowsCounter++;
								    %>
								    	<tr>
								    	<td>&nbsp;<%= selectF.getFieldName() %> 
								    	<% if (selectF.getFieldAlias() != null) {%>
								    		as <%=selectF.getFieldAlias() %>
								    	<% } %>
								    	<% if (it.hasNext()){ %>
								    		,
								    	<% } %>
								    	</td>
								    	</tr>
									<%    } %>
									<%  } %>
									
									<% if ((aWizardObject.getEntityClasses() != null) && (aWizardObject.getEntityClasses().size() > 0)) { 
										rowsCounter++;
									%>
									<tr>
										<td colspan="2"><b> From </b></td> 
										
									</tr>
									<% List enityClasses = aWizardObject.getEntityClasses();
									   java.util.Iterator it = enityClasses.iterator();
									   EntityClass ec = null;
									   while (it.hasNext()){
										   ec = (EntityClass)it.next();
										   rowsCounter++;
								    %>
								    	<tr>
								    	<td>&nbsp;<%= ec.getClassName() %> 
								    	<% if (ec.getClassAlias() != null) {%>
								    		as <%=ec.getClassAlias() %>
								    	<% } %>
								    	<% if (it.hasNext()){ %>
								    		,
								    	<% } %>
								    	</td>
								    	</tr>
									<%    } %>
									<%  } %>
									
									<% if (aWizardObject.getWhereClause() != null){ 
										rowsCounter++;
									%>
									   <tr>
										<td colspan="2"> <b>Where</b> </td>
									   </tr>
									<% List conditionFields = aWizardObject.getWhereClause().getWhereFields(); 
										java.util.Iterator it = conditionFields.iterator();
									   IWhereField conditionF = null;
									   while (it.hasNext()){
										   conditionF = (IWhereField)it.next();
										   rowsCounter++;
								    %>
								    	<tr>
								    	<td>&nbsp;<%=conditionF.getFieldName() %>
								    	<b><%= " " + conditionF.getFieldOperator() + " "%></b> 
								    	<% if ((conditionF.getFieldEntityClassForRightCondition() == null)&&(conditionF.getHibernateType().endsWith("StringType"))){ %>
								    	<%=" '"+conditionF.getFieldValue()+ "' "%>
								    	<% }else{ %>
								    	<%=" "+ conditionF.getFieldValue() + " "%>
								    	<% } %>
								    	<% if (it.hasNext()) {%>
								    	&nbsp;<%=conditionF.getNextBooleanOperator() %>
								    	<% } %>
								    	</td>
								    	</tr>
									<%    } %>
									<% } %>
									
									
									<% if (aWizardObject.getGroupByClause() != null){
										rowsCounter++;
									%>
									<tr>
										<td colspan="2"> <b>Group By</b> </td>
									  </tr>
									<% List groupByFields = aWizardObject.getGroupByClause().getGroupByFields(); 
									   java.util.Iterator it = groupByFields.iterator();
									   IOrderGroupByField groupF = null;
									   while (it.hasNext()){
										   groupF = (IOrderGroupByField)it.next();
										   rowsCounter++;
								    %>
								    	<tr>
								    		<td>&nbsp;<%= groupF.getFieldName() %>
								    		<% if (it.hasNext()){ %>
								    		,
								    		<% } %>
								    		</td>
								    	</tr>
									<%    } %>
									<%  } %>
									<% if (aWizardObject.getOrderByClause() != null){ 
										rowsCounter++;
									%>
									<tr>
										<td colspan="2"><b> Order By</b> </td>
									   </tr>
									<% List orderByFields = aWizardObject.getOrderByClause().getOrderByFields(); 
									   java.util.Iterator it = orderByFields.iterator();
									   IOrderGroupByField orderF = null;
									   while (it.hasNext()){
										   orderF = (IOrderGroupByField)it.next();
										   rowsCounter++;
								    %>
								    	<tr>
								    	<td>&nbsp;<%= orderF.getFieldName() %> 
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
								</table>

							</td>	
						</tr>
						
					</table>

	   			<%-- Colonna di Centro --%>
	   			</td>
	   			
	   			<td valign="top">
	   				<table valign="top">
	   					
	   					<tr>
	   						<td>
	   							&nbsp;
	   						</td>
	   					</tr>
	   					
	   					<tr>
	   						<td>
	   							&nbsp;
	   						</td>
	   					</tr>
	   					
	   					<tr>
	   						<td>
	   							&nbsp;
	   						</td>
	   					</tr>
	   					
	   					<tr>
	   						<td>
	   							<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/alignexpert.gif")%>" 
	   								 alt="<%=qbeMsg.getMessage(requestContainer,"QBE.alt.imgRresumeFromQbe") %>" 
	   								 title="<%=qbeMsg.getMessage(requestContainer,"QBE.alt.imgResumeFromQbe") %>" 
	   								 onclick="javascript:resumeFromQbe()"/>
								<br/>
								<a href="javascript:resumeFromQbe()">
									 <%=qbeMsg.getMessage(requestContainer, "QBE.alt.resumeFromQbe") %>
											
								</a>


	   						</td>
	   					</tr>
	   					
	   					<tr>
	   						<td>
	   							&nbsp;
	   						</td>
	   					</tr>
	   					
	   					<tr>
	   						<td>
	   							&nbsp;
	   						</td>
	   					</tr>
	   						
	   					<tr>
	   						<td>
	   							&nbsp;
	   						</td>
	   					</tr>
	   				</table>
	   			</td>

	   			<%-- Colonna di DX  --%>
	   			
	   			<td valign="top">
	   				<table valign="top">
	
						<tr border=5>
							<td>
		   						<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.ExpertComposition")%></span>
							</td>
						<tr>						
						<tr border=5>
							<td>
								&nbsp;
							</td>
						<tr>		
						
						 	<tr>
						 		<td colspan="3">
 		
						 			<%  String strTextArea = "";

						 				if ((aWizardObject.getExpertQueryDisplayed() == null)||(aWizardObject.getExpertQueryDisplayed().trim().length() == 0)){
						 					
						 					strTextArea = aWizardObject.getFinalQuery();
						 					
						 				}else{
						 					
						 					strTextArea = aWizardObject.getExpertQueryDisplayed();
				 					
						 				}
						 				
						 			%>
						 									 			
					 				<textarea name="expertSelectTextArea" id="expertSelectTextArea" rows="<%=rowsCounter%>" cols="50"><%=strTextArea%></textarea>
								</td>
						 	</tr>
						 	<tr>
						 		<td>
									<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertclose.gif")%>" 
										 alt="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgResumeLastExpert") %>"  
										 title="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgResumeLastExpert") %>" 
										 onclick="javascript:resumeLastExpert()"/> 
									 <a href="javascript:submitUpdatePreview('RESUME_LAST_EXPERT_LINK')">
										 <%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgResumeLastExpert") %>
									 </a>
								</td>
								<td>
									&nbsp;
								</td>
								<td>
									<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/expertok.gif")%>" 
										 alt="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgExpertSave") %>"  
										 title="<%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgExpertSave") %>" 
				                         onclick="javascript:saveExpertSelect()"/>
				                         <a href="javascript:saveExpertSelect()">
                       						 <%=qbeMsg.getMessage(requestContainer, "QBE.alt.imgExpertSave") %>
                       					 </a>
		                     	</td>
						</tr>
												
				</table>
			</td>
				
	   			
			<%-- FORM  --%>			
			<tr>
	   			<td>
	   				&nbsp;
	   			</td>
	   		</tr>
			<tr>
				<td></td>			
				<td colspan="3" >
					<form id="formUpdateExpert" name="formUpdateExpert" action="<%=qbeUrl.getUrl(request,null) %>" method="post">
												<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer, "QBE.Resume.ExecutionModality")%></span>
												&nbsp;
												<input type="hidden" name="ACTION_NAME" value="UPDATE_PREVIEW_MODE_ACTION"/>
												<input type="hidden" id="formUpdateExpert_Source" name="SOURCE" value="RADIO_BUTTON"/>
												<input type="hidden" id="formUpdateExpert_expertTA" name="EXPERT_DISPLAYED" value=""/>												
												
												
												
												<% if (aWizardObject.isUseExpertedVersion()) { %>
													<input type="radio" name="previewMode" value="ComposedQuery" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview")%>
													&nbsp;
													<input type="radio" name="previewMode" value="ExpertMode" checked="checked" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview")%>
												<%} else {%>
													<input type="radio" name="previewMode" value="ComposedQuery" checked="checked" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')"> <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseQbeQueryInPreview")%>
													&nbsp;
													<input type="radio" name="previewMode" value="ExpertMode" onclick="javascript:submitUpdatePreview('RADIO_BUTTON')" > <%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.RadioUseExpertQueryInPreview")%>
												<%}%>
					</form>
				</td>		
			</tr>
			<%-- FORM per ??? --%>			
				
			
			
		</table> 
	</td> 
</tr> 
<div id="divSpanCurrent">
 <span id="currentScreen">DIV_RESUME_QUERY_SELECT_OK</span>
</div>


	   
<%} else { %>
  <tr>
   			<td width="100%">
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
	  					<span class="qbeError"><%=qbeMsg.getMessage(requestContainer, "QBE.Warning.NoFieldSelected") %></span>	
  					</td>
  				</tr>
	   			<tr>
		   			<td>
						&nbsp;
					</td>
	   			</tr>
  				
  				</table>
  			</td>
  </tr>
  
<div id="divSpanCurrent">
 <span id="currentScreen">DIV_RESUME_QUERY</span>
</div>
  
<%} %>
</table>
<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>

<form id="expertForm" name="expertForm" action="<%=qbeUrl.getUrl(request,null) %>" method="POST">
 	<input type="hidden" id="expertFormActionName" name="ACTION_NAME" value="UPDATE_EXPERT_ACTION"/>
 	<input type="hidden" id="expertSelectTA" name="expertSelectTA" value="">
</form>

<form id="formUpdateConditions" name="formUpdateConditions" action="<%=qbeUrl.getUrl(request,null) %>" method="POST">
 	<input type="hidden" name="ACTION_NAME" value="UPDATE_FIELD_RESUME_ACTION"/>
 	<input type="hidden" id="nextActionAfterSaveCondition"  name="NEXT_ACTION" value=""/>
	<input type="hidden" id="nextPublisherAfterSaveCondition" name="NEXT_PUBLISHER" value=""/>
	 	
 	<input type="hidden" id="expertDisplayedForUpdate" name="expertDisplayedForUpdate" value="">
</form>


 	



<%@include file="../jsp/qbefooter.jsp" %>
