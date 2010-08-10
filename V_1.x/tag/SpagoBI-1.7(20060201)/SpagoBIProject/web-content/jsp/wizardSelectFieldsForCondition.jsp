<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="it.eng.spago.base.*, it.eng.qbe.utility.*,it.eng.qbe.javascript.*, it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>



<%@ include file="../jsp/qbe_base.jsp" %>

<% 
   
   ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
   GenerateJavaScriptMenu jsMenu = new GenerateJavaScriptMenu(dm,request);
   
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
							<%= dm.getName() %> : <%=dm.getDescription() %> - <%= qbeMsg.getMessage(requestContainer,"QBE.Title.Conditions") %>
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

<%-- if ((aWizardObject.getEntityClasses() != null) && (aWizardObject.getEntityClasses().size() > 0)){--%> 
   		<tr>
   			<td width="100%"> <%-- INIZIO SESSIONE --%>
	   			<table width="100%">

		<% 	
			java.util.Map sParams = new java.util.HashMap();
				
	  	%>
				<tr>
		   			<td width="3%">
   						&nbsp;
   					</td>
  					<td width="30%">
   						&nbsp;
  					</td>
 					<td width="17%">
   						&nbsp;
  					</td>
  					
   					<td width="50%">
   						&nbsp;
   					</td>
	   				</tr>
	   	   			
	   			<tr>
	   			
	   			<td></td> <%-- Rientro --%>
	   			<td>
		   			<% String treeSelection = null;
		   			   String isSelectClauseEmpty = null;
		   			   	if ((aWizardObject.getSelectClause() != null) && (aWizardObject.getSelectClause().getSelectFields().size() > 0)){
		   			   	   isSelectClauseEmpty = "false";
	  						treeSelection = (String)sessionContainer.getAttribute("SELECTION_TREE");
	  						
	  						treeSelection = treeSelection == null ? "LIGHT" : treeSelection;
	  						
	  					} else {
  		   			   	    isSelectClauseEmpty = "true";	  						
	  						treeSelection = "FULL";
	  					}
		  			 %>
					 <%= jsMenu.condTree(aWizardObject,treeSelection) %>
	   			</td>
	   			
	  			<td valign="top">
	  				<table width="100%">
	  					<tbody>
	  						<tr>
	  							<td id="lightTreeLink">
									<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/removeOperator.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip") %>" onclick="javascript:vediSchermo('Light Tree','LIGHT_TREE')" />	
									<a href="javascript:vediSchermo('Light Tree','LIGHT_TREE')">
										<%=qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTree")%>
									</a>
	  							
	  							</td>
	  						</tr>
	  						<tr>
	  							<td id="fullTreeLink">
	  								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/detail.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip") %>" onclick="javascript:vediSchermo('Full Tree','FULL_TREE')"/>	
									<a href="javascript:vediSchermo('Full Tree','FULL_TREE')">
										<%=qbeMsg.getMessage(requestContainer, "QBE.Conditions.FullTree")%>
									</a>	
	  							</td>
	  						</tr>
	  							
	  					</tbody>
  					</table>

					<script type='text/javascript'>
						var temp = '';
						if ('<%=isSelectClauseEmpty%>' == 'false' ){
						
								if ('<%=treeSelection%>' == 'LIGHT'){
									
									var temp = document.getElementById("lightTreeLink");
									temp.style.display='none';
									var temp = document.getElementById("fullTreeLink");
									temp.style.display='inline';
									
									//alert("<%=treeSelection%> == LIGHT")								
										
								} else {
									var temp = document.getElementById("lightTreeLink");
									temp.style.display='inline';
									var temp = document.getElementById("fullTreeLink");
									temp.style.display='none';
									
									//alert("<%=treeSelection%> == FULL")
									
								}
  						} else {
			  						var temp = document.getElementById("lightTreeLink");
									temp.style.display='none';
									var temp = document.getElementById("fullTreeLink");
									temp.style.display='none';
									
								//alert("<%=isSelectClauseEmpty%> == true")	
  						}
  							  					 
			  		</script>
		  			
	  			</td>
		  		<td valign="top">
		  	
		  		    <table width="100%">
		  				<tr>
		  					<td>
		  						<form id="formUpdateConditions" name="formUpdateConditions" action="<%=qbeUrl.getUrl(request, null) %>"  method="POST">
		  							<input type="hidden" name="ACTION_NAME" value="UPDATE_FIELD_WHERE_ACTION"/>
		  							<input id="nextActionAfterSaveCondition" type="hidden" name="NEXT_ACTION" value=""/>
		  							<input id="nextPublisherAfterSaveCondition" type="hidden" name="NEXT_PUBLISHER" value=""/>
		  						
		  							<input id="updCondMsg" name="updCondMsg" type="hidden" value="UPD"/>
		  							
		  							
									<input id="S_COMPLETE_FIELD_NAME" type="hidden" name="S_COMPLETE_FIELD_NAME" value=""/>
									<input id="S_ALIAS_COMPLETE_FIELD_NAME" type="hidden" name="S_ALIAS_COMPLETE_FIELD_NAME" value=""/>
									
									<input id="S_CLASS_NAME" type="hidden" name="S_CLASS_NAME" value=""/>
																		
									<input id="S_HIB_TYPE" type="hidden" name="S_HIB_TYPE" value=""/>
									
									<input id="Parameter" type="hidden" name="" value=""/>
																
		  							
		  						<table widht="100%">
		  						
		  						
		  						<% 		
		    		   			String fieldName = "";
			    		   		String fieldValue = "";
			    		   		String fieldOperator = "";
			    		   		String nextBooleanFieldOperator = "";
			    		   		String urlDeleteWhere = "";
			    		   		String urlMoveDown ="#";
	    		   				String urlMoveUp ="#";
			    		   		
			    		   		String fieldId = "";
			    		   		if (aWizardObject.getWhereClause() != null){
			    		   			List l = aWizardObject.getWhereClause().getWhereFields();
			    		   			if (l != null){
			    		   				java.util.Iterator it = l.iterator();
			    		   				IWhereField aWhereField = null;
			    		   				while (it.hasNext()){
			    		   					aWhereField = (IWhereField)it.next();
			    		   					fieldId = (String)aWhereField.getId();
			    		   					fieldName = (String)aWhereField.getFieldName();
			    		   					fieldValue = (String)aWhereField.getFieldValue();
			    		   					fieldOperator = aWhereField.getFieldOperator();
			    		   					nextBooleanFieldOperator = aWhereField.getNextBooleanOperator();
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","DELETE_FIELD_FROM_WHERE_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					urlDeleteWhere = qbeUrl.getUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","MOVE_UP_WHERE_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					urlMoveUp = qbeUrl.getUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","MOVE_DOWN_WHERE_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					urlMoveDown = qbeUrl.getUrl(request, sParams);
			    		   					
			    		   				%>
			    		   				<tr>
			    		   					<td width="5%">
		    		   							<a href="<%=urlDeleteWhere %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromCondition") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromCondition") %>" /></a>
		    		   						</td>
		    		   						<td  width="5%">
				    		   					<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp") %>"/></a>
				    		   				</td>
				    		   					
				    		   				<td width="5%">
				    		   					<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown") %>"/></a>
				    		   				</td>
			    		   					<td>
			    		   						<%=Utils.getLabelForQueryField(requestContainer, dm, aWizardObject, fieldName)%>
			    		   					</td>
			    		   					<td>
			    		   						<select name="<%="OPERATOR_FOR_FIELD_"+fieldId%>"/>
													<option value="=" <%=(fieldOperator.equalsIgnoreCase("=")? "selected" : "")%>>=</option>
			    		   							<option value="<=" <%=(fieldOperator.equalsIgnoreCase("<=")? "selected" : "")%>><=</option>
			    		   							<option value=">=" <%=(fieldOperator.equalsIgnoreCase(">=")? "selected" : "")%>>>=</option>
			    		   							<option value="<=" <%=(fieldOperator.equalsIgnoreCase("<")? "selected" : "")%>><</option>
			    		   							<option value=">=" <%=(fieldOperator.equalsIgnoreCase(">")? "selected" : "")%>>></option>
			    		   							<option value="!=" <%=(fieldOperator.equalsIgnoreCase("!=")? "selected" : "")%>>!=</option>
			    		   							<option value="like" <%=(fieldOperator.equalsIgnoreCase("like")? "selected" : "")%>>like</option>
			    		   							<option value="start with" <%=(fieldOperator.equalsIgnoreCase("start with")? "selected" : "")%>>starts with</option>
			    		   							<option value="end with" <%=(fieldOperator.equalsIgnoreCase("end with")? "selected" : "")%>>end with</option>
			    		   							<option value="contains" <%=(fieldOperator.equalsIgnoreCase("contains")? "selected" : "")%>>contains</option>
			    		   							<option value="is null" <%=(fieldOperator.equalsIgnoreCase("is null")? "selected" : "")%>>is null</option>
			    		   							<option value="is not null" <%=(fieldOperator.equalsIgnoreCase("is not null")? "selected" : "")%>>is not null</option>
			    		   							<option value="in" <%=(fieldOperator.equalsIgnoreCase("in")? "selected" : "")%>>in</option>
			    		   							<option value="not in" <%=(fieldOperator.equalsIgnoreCase("not in")? "selected" : "")%>>not in</option>
			    		   							<option value="between" <%=(fieldOperator.equalsIgnoreCase("between")? "selected" : "")%>>between</option>
			    		   						</select>
			    		   					</td>
			    		   					<td>
			    		   						<input type="text" id="<%="VALUE_FOR_FIELD_"+fieldId %>" name="<%="VALUE_FOR_FIELD_"+fieldId %>" value="<%=fieldValue %>"/>
			    		   					</td>
			    		   					<td width="5%">
				    		   					<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/selectjoin.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSelectJoin") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSelectJoin") %>" onclick="openDivTreeSelectJoin('<%=fieldId%>', '<%="VALUE_FOR_FIELD_"+fieldId%>', event)"/>
				    		   				</td>
			    		   					<td>
			    		   						<select name="<%="NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId%>"/>
													<option value="AND " <%=(nextBooleanFieldOperator.equalsIgnoreCase("AND")? "selected" : "")%>>AND</option>
			    		   							<option value="OR" <%=(nextBooleanFieldOperator.equalsIgnoreCase("OR")? "selected" : "")%>>OR</option>
			    		   						</select>
			    		   					</td>
			    		   			    </tr>
			    		   			   <% }%> <%-- FINE WHILE --%>
			    		   			<% }%> <%-- FINE IF--%>
		    					<% }%><%-- FINE IF--%>
		    					
		  						</table>
		  						</form>
		  					</td>
		  			</tr>
		  		</table>
		  	</td>
		  	<tr>
		  	<tr>
	   			<td>
   					&nbsp;
   				</td>
   			</tr>
		  	
		  </table>
		 </td> <%-- INIZIO SESSIONE --%>
  </tr>
<%--} else { %>
  <tr>
   			<td width="100%">
	   			<table width="100%">
	  			<tr>
	  				<td width="100%" valign="top">
	  					<b> <%=qbeMsg.getMessage(requestContainer, "QBE.Warning.SelectFieldBeforeCondition") %></b>	
  					</td>
  				</tr>
  				</table>
  			</td>
  </tr>
<%} --%>
</table>
<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<div id="divSpanCurrent">
	<span id="currentScreen">DIV_FIELD_CONDITION</span>
</div>

<div id="divTreeSelectJoin" >
   	    <%treeSelection = (String)sessionContainer.getAttribute("SELECTION_TREE");%>
		<%= jsMenu.condTreeSelectJoin(aWizardObject,treeSelection) %>
</div>
<%@include file="../jsp/qbefooter.jsp" %>
	
	
	
 
   
    		
    	
			
		
	
			

