 <%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="it.eng.spago.base.*, it.eng.spagobi.utilities.javascript.*, it.eng.qbe.utility.*,it.eng.qbe.javascript.*, it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>



<%@ include file="../jsp/qbe_base.jsp" %>

<% 
   
   ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
   QbeJsTreeBuilder qbeConditionJsTreeBuilder = new QbeConditionJsTreeBuilder(dm,aWizardObject, request);;
   qbeConditionJsTreeBuilder.setCheckable(false);
   qbeConditionJsTreeBuilder.setName("conditionTree");
   
   QbeJsTreeBuilder qbeJoinJsTreeBuilder = new QbeJoinJsTreeBuilder(dm,aWizardObject, request);;
   qbeJoinJsTreeBuilder.setCheckable(false);	
   qbeJoinJsTreeBuilder.setName("joinTree");
   
   dm.updateCurrentClassLoader();
%>
<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>
 


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 

		    style='vertical-align:middle;padding-left:5px;'>
			<%= dm.getName() %> : <%=dm.getDescription() %> - <%= qbeMsg.getMessage(requestContainer,"QBE.Title.Conditions", bundle) %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%@include file="../jsp/qbe_headers.jsp"%>
	</tr>
</table>


<%@include file="../jsp/testata.jsp" %>
 
 
<div class='div_background_no_img'>
<% if ((aWizardObject.getSelectClause() != null) && (aWizardObject.getSelectClause().getSelectFields().size() > 0)){%> 
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
	  						
	  						treeSelection = treeSelection == null ? qbeConditionJsTreeBuilder.LIGHT_MODALITY : treeSelection;
	  						
	  					} else {
  		   			   	    isSelectClauseEmpty = "true";	  						
	  						treeSelection = qbeConditionJsTreeBuilder.FULL_MODALITY;
	  					}
		   			 qbeConditionJsTreeBuilder.setModality(treeSelection);
		  			 %>
					 <%= qbeConditionJsTreeBuilder.build() %>
	   			</td>
	   			
	  			<td valign="top">
	  				<table width="100%">
	  					<tbody>
	  						<tr>
	  							<td id="lightTreeLink">
									<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/removeOperator.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" onclick="javascript:vediSchermo('Light Tree','LIGHT_TREE')" />	
									<a href="javascript:vediSchermo('Light Tree','LIGHT_TREE')"
										 class="qbe-title-link" >
										<%=qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTree", bundle)%>
									</a>
	  							
	  							</td>
	  						</tr>
	  						<tr>
	  							<td id="fullTreeLink">
	  								<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/detail.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.Conditions.LightTreeTooltip", bundle) %>" onclick="javascript:vediSchermo('Full Tree','FULL_TREE')"/>	
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
																
		  							
		  						<table widht="100%" class="qbe-font">
		  						
		  						
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
		    		   							<a href="<%=urlDeleteWhere %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromCondition", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromCondition", bundle) %>" /></a>
		    		   						</td>
		    		   						<td  width="5%">
				    		   					<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>"/></a>
				    		   				</td>
				    		   					
				    		   				<td width="5%">
				    		   					<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>"/></a>
				    		   				</td>
			    		   					<td>
			    		   						<%=Utils.getLabelForQueryField(requestContainer, dm, aWizardObject, fieldName)%>
			    		   					</td>
													<td>&nbsp;</td> 
			    		   					<td style="padding:1px;">
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
													<td>&nbsp;</td> 
			    		   					<td>
			    		   						<input type="text" id="<%="VALUE_FOR_FIELD_"+fieldId %>" name="<%="VALUE_FOR_FIELD_"+fieldId %>" value="<%=fieldValue %>"/>
			    		   					</td>
													<td>&nbsp;</td> 
			    		   					<td width="5%">
				    		   					<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/selectjoin.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSelectJoin", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSelectJoin", bundle) %>" onclick="openDivTreeSelectJoin('<%=fieldId%>', '<%="VALUE_FOR_FIELD_"+fieldId%>', event)"/>
				    		   				</td>
													<td>&nbsp;</td> 
			    		   					<td  style="padding:1px;">  
			    		   						<select name="<%="NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId%>"/>
													<option value="AND " <%=(nextBooleanFieldOperator.equalsIgnoreCase("AND")? "selected" : "")%>>AND</option>
			    		   							<option value="OR" <%=(nextBooleanFieldOperator.equalsIgnoreCase("OR")? "selected" : "")%>>OR</option>
			    		   						</select>
			    		   					</td>
													<td>&nbsp;</td>
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
		  <div id="divTreeSelectJoin" >
   	    	<%treeSelection = (String)sessionContainer.getAttribute("SELECTION_TREE");%>
			<%= qbeJoinJsTreeBuilder.build() %>
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
	  					<span class="qbeError"><%=qbeMsg.getMessage(requestContainer, "QBE.Warning.SelectFieldBeforeOrdering", bundle) %></span>
  					</td>
  				</tr>
  				<tr>
			   			<td>
    						&nbsp;
	   					</td>
	   			</tr>  
  				</table>
<%} %>



<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>

<div id="divSpanCurrent">
	<span id="currentScreen">DIV_FIELD_CONDITION</span>
</div>


<%@include file="../jsp/qbefooter.jsp" %>


</div>
