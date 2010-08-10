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





<%@ include file="/jsp/qbe_base.jsp" %>

<qbe:url type="resource" var="pageJavaScripts" ref="../js/querybuilder/where/wherePage.js"/>
<script type="text/javascript" src='${pageJavaScripts}'/></script>

<% 
   
	QbeJsTreeBuilder qbeConditionJsTreeBuilder = null;
	QbeJsTreeBuilder qbeJoinJsTreeBuilder = null;

	qbeConditionJsTreeBuilder = new QbeConditionJsTreeBuilder(datamartModel, datamartWizard, request);;
   	qbeConditionJsTreeBuilder.setCheckable(false);
   	qbeConditionJsTreeBuilder.setName("conditionTree");
   
   	qbeJoinJsTreeBuilder = new QbeJoinJsTreeBuilder(datamartModel, datamartWizard, request);
   	qbeJoinJsTreeBuilder.setCheckable(false);	
   	qbeJoinJsTreeBuilder.setName("joinTree");
   
   QbeJsTreeBuilder qbeJoinWithParentQueryBuilder = null; 
   
   if ( query.isSubqueryModeActive() ) {
		String subQueryFieldId = (String)sessionContainer.getAttribute(QbeConstants.SUBQUERY_FIELD);
		String subQueryPrefix = query.getSubQueryIdForSubQueryOnField(subQueryFieldId);
		qbeConditionJsTreeBuilder.setClassPrefix(subQueryPrefix);
		qbeJoinJsTreeBuilder.setClassPrefix(subQueryPrefix);
		qbeJoinWithParentQueryBuilder = new QbeJoinWithFatherQueryJsTreeBuilder(datamartModel, datamartWizard, request);
		qbeJoinWithParentQueryBuilder.setCheckable(false);
		qbeJoinWithParentQueryBuilder.setName("joinTreeWithParentQuery");
		qbeJoinWithParentQueryBuilder.setClassPrefix("a");		
   }
   

%>



<qbe:page>
 	<qbe:page-content>
		
		<%@include file="/jsp/commons/titlebar.jspf" %>
		<%@include file="/jsp/testata.jsp" %>	
 
 
		<div class='div_background_no_img'>


<% if (!query.isEmpty()){%> 
	<table width="100%">

		<% 	
			Map sParams = new HashMap();
				
	  	%>
				<tr>
		   			<td width="3%">
   						&nbsp;
   					</td>
  					<td width="30%">
   						&nbsp;
  					</td>
  					<td width="30%">
   						&nbsp;
  					</td>
 					<td width="10%">
   						&nbsp;
  					</td>
  					
   					<td width="27%">
   						&nbsp;
   					</td>
	   			</tr>
	   	   			
	   			<tr>
	   			
	   			<td></td> <%-- Rientro --%>
	   			<td>
		   			<% String treeSelection = null;
		   			   String isSelectClauseEmpty = null;
		   			   	if (!query.isEmpty()){
		   			   	   isSelectClauseEmpty = "false";
	  						treeSelection = (String)sessionContainer.getAttribute("SELECTION_TREE");
	  						
	  						treeSelection = treeSelection == null ? qbeConditionJsTreeBuilder.LIGHT_MODALITY : treeSelection;
	  						
	  					} else {
  		   			   	    isSelectClauseEmpty = "true";	  						
	  						treeSelection = qbeConditionJsTreeBuilder.FULL_MODALITY;
	  					}
		   			 qbeConditionJsTreeBuilder.setModality(treeSelection);
		  			 %>
					 <%= qbeConditionJsTreeBuilder.build() %>
	   			</td>
	   			
	   			<td>
	   				<%if(treeSelection == qbeConditionJsTreeBuilder.FULL_MODALITY) {%>
	   					<qbe:newtree actionName="selectFieldForConditionCallBack" actionType="script" modality="full"/>
	   				<%} else { %>
	   					<qbe:newtree actionName="selectFieldForConditionCallBack" actionType="script" modality="light"/>
	   				<%} %>
	   			</td>
	   			
	  			<td valign="top">
	  				<table width="100%">
	  					<tbody>
	  						<tr>
	  							<td id="lightTreeLink">
									<img src="<%=qbeUrl.getResourceUrl(request,"../img/removeOperator.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" onclick="javascript:vediSchermo('Light Tree','LIGHT_TREE')" />	
									<a href="javascript:vediSchermo('Light Tree','LIGHT_TREE')"
										 class="qbe-title-link" >
										<%=qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTree", bundle)%>
									</a>
	  							
	  							</td>
	  						</tr>
	  						<tr>
	  							<td id="fullTreeLink">
	  								<img src="<%=qbeUrl.getResourceUrl(request,"../img/detail.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" onclick="javascript:vediSchermo('Full Tree','FULL_TREE')"/>	
									<a href="javascript:vediSchermo('Full Tree','FULL_TREE')" class="qbe-title-link" >
										<%=qbeMsg.getMessage(requestContainer, "QBE.Conditions.FullTree", bundle)%>
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
		  	
		  		    <table  width="100%">
		  				<tr>
		  					<td>
		  						<form id="formUpdateConditions" name="formUpdateConditions" action="<%=qbeUrl.getActionUrl(request, null) %>"  method="POST">
		  							<input id="fUpdCondJoinParent" name="fUpdCondJoinParent" value="FALSE" type="hidden" />
		  							<input name="ACTION_NAME" value="UPDATE_FIELD_WHERE_ACTION" type="hidden" />
		  							<input id="nextActionAfterSaveCondition" type="hidden" name="NEXT_ACTION" value=""/>
		  							<input id="nextPublisherAfterSaveCondition" type="hidden" name="NEXT_PUBLISHER" value=""/>		  						
		  							<input id="updCondMsg" name="updCondMsg" type="hidden" value="UPD"/>		  							
									<input id="S_COMPLETE_FIELD_NAME" type="hidden" name="S_COMPLETE_FIELD_NAME" value=""/>
									<input id="S_ALIAS_COMPLETE_FIELD_NAME" type="hidden" name="S_ALIAS_COMPLETE_FIELD_NAME" value=""/>									
									<input id="S_CLASS_NAME" type="hidden" name="S_CLASS_NAME" value=""/>																		
									<input id="S_HIB_TYPE" type="hidden" name="S_HIB_TYPE" value=""/>									
									<input id="Parameter" type="hidden" name="" value=""/>
									
									<input id="FIELD_UNIQUE_NAME" name="FIELD_UNIQUE_NAME" type="hidden"  value=""/>	
																
		  							
		  						<table widht="100%" class="qbe-font">
		  						
		  						
		  						<% 		
		    		   			String fieldName = "";
			    		   		String fieldValue = "";
			    		   		String fieldOperator = "";
			    		   		String nextBooleanFieldOperator = "";
			    		   		String urlDeleteWhere = "";
			    		   		String urlMoveDown ="#";
	    		   				String urlMoveUp ="#";
	    		   				String urlAddLeftBracket ="#";
	    		   				String urlAddRightBracket ="#";
	    		   				String urlRemoveLeftBracket = "#";
	    		   				String urlRemoveRightBracket = "#";
	    		   				
			    		   		
			    		   		String fieldId = "";
			    		   		
			    		   		Iterator it = query.getWhereFieldsIterator();
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
			    		   					urlDeleteWhere = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","MOVE_UP_WHERE_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					urlMoveUp = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","MOVE_DOWN_WHERE_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					urlMoveDown = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","UPDATE_BRACKETS_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					sParams.put("SIDE", "RIGHT");
			    		   					sParams.put("ACTION", "ADD");
			    		   					urlAddRightBracket = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","UPDATE_BRACKETS_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					sParams.put("SIDE", "RIGHT");
			    		   					sParams.put("ACTION", "REMOVE");
			    		   					urlRemoveRightBracket = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","UPDATE_BRACKETS_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					sParams.put("SIDE", "LEFT");
			    		   					sParams.put("ACTION", "ADD");
			    		   					urlAddLeftBracket = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					
			    		   					sParams.clear();
			    		   					sParams.put("ACTION_NAME","UPDATE_BRACKETS_ACTION");
			    		   					sParams.put("FIELD_ID",fieldId);
			    		   					sParams.put("SIDE", "LEFT");
			    		   					sParams.put("ACTION", "REMOVE");
			    		   					urlRemoveLeftBracket = qbeUrl.getActionUrl(request, sParams);
			    		   					
			    		   					String leftBracketsStr = "";
			    		   					if(aWhereField.getLeftBracketsNum() == 0) {
			    		   						leftBracketsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			    		   					} else {
			    		   						for(int i = 0; i < aWhereField.getLeftBracketsNum(); i++) {
			    		   							leftBracketsStr += "(";
			    		   						}
			    		   					}
			    		   					
			    		   						
			    		   					String rightBracketsStr = "";
			    		   					if(aWhereField.getRightBracketsNum() == 0) {
			    		   						rightBracketsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			    		   					} else {
			    		   						for(int i = 0; i < aWhereField.getRightBracketsNum(); i++) {
				    		   						rightBracketsStr += ")";
			    		   						}
			    		   					}
			    		   					
			    		   				%>
			    		   				<tr>
			    		   					<td width="5%">
		    		   							<a href="<%=urlDeleteWhere %>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromCondition", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromCondition", bundle) %>" /></a>
		    		   						</td>
		    		   						<td  width="5%">
				    		   					<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>"/></a>
				    		   				</td>
				    		   					
				    		   				<td width="5%">
				    		   					<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>"/></a>
				    		   				</td>
				    		   				<td>&nbsp;</td> 
				    		   				<td width="5%">
				    		   					<a href="<%=urlAddLeftBracket%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/addLeft.png")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.addLeftBraket", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.addLeftBraket", bundle) %>"/></a>
				    		   				</td>
				    		   				<td width="5%">
				    		   					<a href="<%=urlRemoveLeftBracket%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/removeLeft.png")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.removeLeftBraket", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.removeLeftBraket", bundle) %>"/></a>
				    		   				</td>
				    		   				<td>&nbsp;</td> 
				    		   				<td width="5%">
				    		   					<div style="border:1px; border-color:black">
				    		   						<%=leftBracketsStr%>
				    		   					</div>
			    		   					</td>
				    		   				<td>&nbsp;</td> 
				    		   				
			    		   					<td>
			    		   						
			    		   						<% String label = JsTreeUtils.getLabelForQueryField(requestContainer, datamartModel, datamartWizard, fieldName);
			    		   						label = label.replaceAll("\\.", ". ");				    		   				
			    		   						%>
			    		   						<%=label %>
			    		   					</td>
											<td>&nbsp;</td> 
			    		   					<td style="padding:1px;">
			    		   						<select name="<%="OPERATOR_FOR_FIELD_"+fieldId%>" class="qbe-font"/>
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
													<td>&nbsp;</td> 
			    		   					<td>
			    		   						<%
			    		   							
			    		   							  if(query.getSubqueryErrMsg(fieldId) != null) {
			    		   						%>
			    		   							<input type="text" id="<%="VALUE_FOR_FIELD_"+fieldId %>" 
			    		   								   name="<%="VALUE_FOR_FIELD_"+fieldId %>" value="<%=fieldValue %>" 
			    		   								   style="background-color:#FF6666;"
			    		   								   title="<%=query.getSubqueryErrMsg(fieldId)%>"/>
			    		   						<% 
			    		   						} else {
			    		   						%>
			    		   						<input type="text" id="<%="VALUE_FOR_FIELD_"+fieldId %>" name="<%="VALUE_FOR_FIELD_"+fieldId %>" 
			    		   							style="background-color:#FFFFFF;"
			    		   							value="<%=fieldValue %>"/>
			    		   						<%
			    		   						}
			    		   						%>
			    		   					</td>
											<td>&nbsp;</td> 
											<td width="5%">
				    		   					<div style="border:1px; border-color:black">
				    		   						<%=rightBracketsStr%>
				    		   					</div>
				    		   				</td>	
				    		   				<td>&nbsp;</td> 	
											<td width="5%">
				    		   					<a href="<%=urlAddRightBracket%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/addRight.png")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.addRightBraket", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.addRightBraket", bundle) %>"/></a>
				    		   				</td>
				    		   				<td width="5%">
				    		   					<a href="<%=urlRemoveRightBracket%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/removeRight.png")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.removeRightBraket", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.removeRightBraket", bundle) %>"/></a>
				    		   				</td>
				    		   					
													
													
			    		   					<td width="5%">
				    		   					<img src="<%=qbeUrl.getResourceUrl(request,"../img/selectjoin.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSelectJoin", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSelectJoin", bundle) %>" onclick="openDivTreeSelectJoin('<%=fieldId%>', '<%="VALUE_FOR_FIELD_"+fieldId%>', event)"/>
				    		   				</td>
				    		   				<% if ( QbeEngineConf.getInstance().isWebModalityActive() ){ %>
				    		   				<%    if (query.isSubqueryModeActive()){ %> 
				    		   						<td width="5%">
										    			<img src="<%=qbeUrl.getResourceUrl(request,"../img/joinparentquery.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgJoinParentQuery", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgJoinParentQuery", bundle) %>" onclick="openDivTreeSelectJoinParentQuery('<%=fieldId%>', '<%="VALUE_FOR_FIELD_"+fieldId%>', event)"/>
										    		</td>
				    		   				<%	  }else{ %>
										    		<td width="5%">
										    			<img src="<%=qbeUrl.getResourceUrl(request,"../img/subquery.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSubquery", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSubquery", bundle) %>" onclick="showSubqueryWin('<%=fieldId%>')"/>
										    		</td>
										    	<%} %>
										     <%} %>
			    		   					<td  style="padding:1px;">  
			    		   						<select name="<%="NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId%>" class="qbe-font"/>
													<option value="AND " <%=(nextBooleanFieldOperator.equalsIgnoreCase("AND")? "selected" : "")%>>AND</option>
			    		   							<option value="OR" <%=(nextBooleanFieldOperator.equalsIgnoreCase("OR")? "selected" : "")%>>OR</option>
			    		   						</select>
			    		   					</td>
													<td>&nbsp;</td>
			    		   			    </tr><tr><td>&nbsp;</td></tr>
			    		   			   <% }%> <%-- FINE WHILE --%>
		    					
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
		  <div id="divTreeSelectJoin" >
   	    	<%treeSelection = (String)sessionContainer.getAttribute("SELECTION_TREE");%>
			<%//=qbeJoinJsTreeBuilder.build() %>
			
			<qbe:newtree actionName="selectFieldForJoinCallBack" actionType="script" modality="full"/>
			
		  </div>
		  <% if (qbeJoinWithParentQueryBuilder != null){ 
		  		System.out.println(qbeJoinWithParentQueryBuilder.build());
		  %>
		  	
		  	<div id="divTreeSelectJoinParentQuery" >	
		  		<%//=qbeJoinWithParentQueryBuilder.build() %>
		  		
		  		<qbe:newtree actionName="selectFieldForJoinWithParentCallBack" actionType="script" modality="full"/>
		  		
		  </div>
		  <%} %>
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
	  					<span class="qbeError"><%=qbeMsg.getMessage(requestContainer, "QBE.Warning.SelectFieldBeforeFiltering", bundle) %></span>
  					</td>
  				</tr>
  				<tr>
			   			<td>
    						&nbsp;
	   					</td>
	   			</tr>  
  				</table>
<%} %>




		<div id="divSpanCurrent">
			<span id="currentScreen">DIV_FIELD_CONDITION</span>
		</div>

	
		<script type="text/javascript">
			changeTabBkg();
		</script>
	
	
		</div>

	</qbe:page-content>
</qbe:page>
