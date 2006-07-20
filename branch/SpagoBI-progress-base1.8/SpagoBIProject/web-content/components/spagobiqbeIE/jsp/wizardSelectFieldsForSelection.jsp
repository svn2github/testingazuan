 <%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="it.eng.spago.base.*, it.eng.spagobi.utilities.javascript.*, it.eng.qbe.utility.*,it.eng.qbe.javascript.*, it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>





<%@ include file="../jsp/qbe_base.jsp" %>


<%   
   ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
   QbeJsTreeBuilder qbeJsBuilder = new QbeSelectJsTreeBuilder(dm,aWizardObject, request);;
	   
   dm.updateCurrentClassLoader();
   
%>


<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Selection", bundle) %>
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
    						&nbsp;
	   					</td>
	   					<td width="47%">
    						&nbsp;
	   					</td>
	   					<td width="50%">
    						&nbsp;
	   					</td>
	   				</tr>
				   	<tr>
				   		<td></td>
				  		<td valign="top">
				  		
				  					<%=qbeJsBuilder.build()%>
				  		</td>
				  		<td width="47%" valign="top">
				  		<% java.util.Map sParams = new java.util.HashMap();
				  		   sParams.clear(); 
		   					
				  		%>
				  		<form id="formUpdateSelect" name="formUpdateSelect" action="<%=qbeUrl.getUrl(request, null) %>" method="post">	
				  			<input type="hidden" name="ACTION_NAME" value="UPDATE_FIELD_SELECT_ACTION"/>
				  		<table class="qbe-font">
				  			
				  		<% if (aWizardObject.getSelectClause() != null){ %>
					 										 									 					
					 					
				    		   			<% List l = aWizardObject.getSelectClause().getSelectFields();
				    		   			if (l != null){
				    		   				java.util.Iterator it = l.iterator();
				    		   				ISelectField aSelectField = null;
				    		   				String fieldId = "";
				    		   				String fieldName = "";
				    		   				String fieldAlias = "";
				    		   				String urlDeleteSelect ="#";
				    		   				String urlMoveDown ="#";
				    		   				String urlMoveUp ="#";
				    		   				
				    		   				String idInputFieldName = "";
				    		   				String idSpanInputFieldName = "";
				    		   				
				    		   				String idInputAliasFieldName = "";
				    		   				String idSpanAliasInputFieldName = "";
				    		   				
				    		   				while (it.hasNext()){
				    		   					aSelectField = (ISelectField)it.next();
				    		   					fieldId = (String)aSelectField.getId();
				    		   					fieldName = (String)aSelectField.getFieldName();
				    		   					fieldAlias = (String)aSelectField.getFieldAlias();
				    		   					
				    		   					sParams.clear();
				    		   					sParams.put("ACTION_NAME","DELETE_FIELD_FROM_SELECT_ACTION");
				    		   					sParams.put("FIELD_ID",fieldId);
				    		   					urlDeleteSelect = qbeUrl.getUrl(request, sParams);
				    		   					
				    		   					sParams.clear();
				    		   					sParams.put("ACTION_NAME","MOVE_UP_ACTION");
				    		   					sParams.put("FIELD_ID",fieldId);
				    		   					urlMoveUp = qbeUrl.getUrl(request, sParams);
				    		   					
				    		   					sParams.clear();
				    		   					sParams.put("ACTION_NAME","MOVE_DOWN_ACTION");
				    		   					sParams.put("FIELD_ID",fieldId);
				    		   					urlMoveDown = qbeUrl.getUrl(request, sParams);
				    		   					
				    		   					
				    		   					idInputFieldName = "NEW_FIELD_"+fieldId;
				    		   					idSpanInputFieldName =idInputFieldName+ "_span";
				    		   					
				    		   					idInputAliasFieldName = "ALIAS_FOR_"+fieldId;
				        		   				idSpanAliasInputFieldName = idInputAliasFieldName + "_span";
				        		   				
				    		   				%>
				    		   				<tr>
				    		   					<td width="5%">
				    		   						<a href="<%=urlDeleteSelect %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromSelection", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromSelection", bundle) %>"/></a>
				    		   					</td>
				    		   				
				    		   					<td  width="5%">
				    		   						<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>"/></a>
				    		   					</td>
				    		   					
				    		   					<td width="5%">
				    		   						<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>"/></a>
				    		   					</td>
				    		  
				    		  					<td width="5%">
				    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/clear.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgCleanOperator", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgCleanOperator", bundle) %>" onclick="javascript:removeOperator('<%=idInputFieldName%>',event)"/>
				    		   					</td>
				    		   					
				    		   					<td width="5%">
				    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/operator.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgApplyOperator", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgApplyOperator", bundle) %>" onclick="javascript:openDivOperatoriSelect('<%=idInputFieldName%>',event)"/>
				    		   					</td>
				    		   					
				    		   					
				    		   					<td width="35%" align="left">
				    		   						<input type="hidden" name="<%=idInputFieldName %>" id="<%=idInputFieldName%>"  value="<%=fieldName%>" readonly="readonly"/>
				    		   						<span id="<%=idSpanInputFieldName%>"><%=Utils.getLabelForQueryField(requestContainer, dm, aWizardObject,fieldName)%></span>
				    		   					</td>
				    		   					
				    		   					<td width="5%">
				    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/as.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgAs", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgAs", bundle) %>" onclick="javascript:inputAS('<%=idInputFieldName%>','<%=idInputAliasFieldName%>',event)"/>
				    		   					</td>
				    		   					<td width="35%" align="left">
				    		   						<input type="hidden" name="<%=idInputAliasFieldName %>" id="<%=idInputAliasFieldName %>"  value="" readonly="readonly"/>
				    		   						<span id="<%=idSpanAliasInputFieldName%>">
				    		   						<%=(fieldAlias != null ? fieldAlias : "&nbsp;")%>
				    		   						</span>
				    		   					</td>
				    		   					
				    		   					
				    		   					
				    		   				</tr>
				    		   				
				    		   			<%}//endwhile%>
				    		   			<tr>
				    		   					<td colspan="8">
													<% if (aWizardObject.getDistinct()){ %>				    		   						
				    		   							<input type="checkbox" id="checkboxDistinct" checked="checked" onclick="handleDistinct()"> distinct
				    		   						<%}else{ %>
				    		   							<input type="checkbox" id="checkboxDistinct" onclick="handleDistinct()"> distinct
				    		   						<%} %>
				    		   						<input type="hidden" id="selectDistinct" name="selectDistinct" value="<%=aWizardObject.getDistinct()%>"/>
				    		   					</td>
				    		   			</tr>
				    		   		<%}//endif %>
				    		   	
				    		 <%}//endif %>
				    		 <% if ((aWizardObject.getSelectClause() != null) && (aWizardObject.getSelectClause().getSelectFields().size() > 0)){ %>
				    		 <%-- <tr>
				    		 	<td colspan="7">
				    		 		
				    		 		<input type="submit" value="Save"/>
				    		 	</td>
				    		 </tr>
				    		 --%>
				    		 <%} %>
				    		 </table>
				    		 </form>
				  		</td>
				  		</tr>
				  		<tr>
			   				<td>
    							&nbsp;
	   						</td>
	   					</tr>
				  	</table>




<%--
	<table width="100%">
		<tr>
			<td width="100%">
				<TABLE WIDTH = "100%">
					
					<TR>
						<TD width="5">&nbsp;</TD>
						<TD width="90%" CLASS = "TESTATA">
							<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Selection") %>
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
			<td width="100%">
				
				<table width="100%">  
					<tr>
			   			<td width="3%">
    						&nbsp;
	   					</td>
	   					<td width="47%">
    						&nbsp;
	   					</td>
	   					<td width="50%">
    						&nbsp;
	   					</td>
	   				</tr>
				   	<tr>
				   		<td></td>
				  		<td valign="top">
				  		
				  					<%=jsMenu.selTree()%>
				  			
				  		</td>
				  		<td width="47%" valign="top">
				  		<% java.util.Map sParams = new java.util.HashMap();
				  		   sParams.clear(); 
		   					
				  		%>
				  		<form id="formUpdateSelect" name="formUpdateSelect" action="<%=qbeUrl.getUrl(request, null) %>" method="post">	
				  			<input type="hidden" name="ACTION_NAME" value="UPDATE_FIELD_SELECT_ACTION"/>
				  		<table>
				  			
				  		<% if (aWizardObject.getSelectClause() != null){ %>
					 										 									 					
					 					
				    		   			<% List l = aWizardObject.getSelectClause().getSelectFields();
				    		   			if (l != null){
				    		   				java.util.Iterator it = l.iterator();
				    		   				ISelectField aSelectField = null;
				    		   				String fieldId = "";
				    		   				String fieldName = "";
				    		   				String fieldAlias = "";
				    		   				String urlDeleteSelect ="#";
				    		   				String urlMoveDown ="#";
				    		   				String urlMoveUp ="#";
				    		   				
				    		   				String idInputFieldName = "";
				    		   				String idSpanInputFieldName = "";
				    		   				
				    		   				String idInputAliasFieldName = "";
				    		   				String idSpanAliasInputFieldName = "";
				    		   				
				    		   				while (it.hasNext()){
				    		   					aSelectField = (ISelectField)it.next();
				    		   					fieldId = (String)aSelectField.getId();
				    		   					fieldName = (String)aSelectField.getFieldName();
				    		   					fieldAlias = (String)aSelectField.getFieldAlias();
				    		   					
				    		   					sParams.clear();
				    		   					sParams.put("ACTION_NAME","DELETE_FIELD_FROM_SELECT_ACTION");
				    		   					sParams.put("FIELD_ID",fieldId);
				    		   					urlDeleteSelect = qbeUrl.getUrl(request, sParams);
				    		   					
				    		   					sParams.clear();
				    		   					sParams.put("ACTION_NAME","MOVE_UP_ACTION");
				    		   					sParams.put("FIELD_ID",fieldId);
				    		   					urlMoveUp = qbeUrl.getUrl(request, sParams);
				    		   					
				    		   					sParams.clear();
				    		   					sParams.put("ACTION_NAME","MOVE_DOWN_ACTION");
				    		   					sParams.put("FIELD_ID",fieldId);
				    		   					urlMoveDown = qbeUrl.getUrl(request, sParams);
				    		   					
				    		   					
				    		   					idInputFieldName = "NEW_FIELD_"+fieldId;
				    		   					idSpanInputFieldName =idInputFieldName+ "_span";
				    		   					
				    		   					idInputAliasFieldName = "ALIAS_FOR_"+fieldId;
				        		   				idSpanAliasInputFieldName = idInputAliasFieldName + "_span";
				        		   				
				    		   				%>
				    		   				<tr>
				    		   					<td width="5%">
				    		   						<a href="<%=urlDeleteSelect %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromSelection") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromSelection") %>"/></a>
				    		   					</td>
				    		   				
				    		   					<td  width="5%">
				    		   						<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp") %>"/></a>
				    		   					</td>
				    		   					
				    		   					<td width="5%">
				    		   						<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown") %>"/></a>
				    		   					</td>
				    		  
				    		  					<td width="5%">
				    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/clear.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgCleanOperator") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgCleanOperator") %>" onclick="javascript:removeOperator('<%=idInputFieldName%>',event)"/>
				    		   					</td>
				    		   					
				    		   					<td width="5%">
				    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/operator.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgApplyOperator") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgApplyOperator") %>" onclick="javascript:openDivOperatoriSelect('<%=idInputFieldName%>',event)"/>
				    		   					</td>
				    		   					
				    		   					
				    		   					<td width="35%" align="left">
				    		   						<input type="hidden" name="<%=idInputFieldName %>" id="<%=idInputFieldName%>"  value="<%=fieldName%>" readonly="readonly"/>
				    		   						<span id="<%=idSpanInputFieldName%>"><%=Utils.getLabelForQueryField(requestContainer, dm, aWizardObject,fieldName)%></span>
				    		   					</td>
				    		   					
				    		   					<td width="5%">
				    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/as.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgAs") %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgAs") %>" onclick="javascript:inputAS('<%=idInputFieldName%>','<%=idInputAliasFieldName%>',event)"/>
				    		   					</td>
				    		   					<td width="35%" align="left">
				    		   						<input type="hidden" name="<%=idInputAliasFieldName %>" id="<%=idInputAliasFieldName %>"  value="" readonly="readonly"/>
				    		   						<span id="<%=idSpanAliasInputFieldName%>">
				    		   						<%=(fieldAlias != null ? fieldAlias : "&nbsp;")%>
				    		   						</span>
				    		   					</td>
				    		   					
				    		   					
				    		   					
				    		   				</tr>
				    		   				
				    		   			<%}//endwhile%>
				    		   			<tr>
				    		   					<td colspan="8">
													<% if (aWizardObject.getDistinct()){ %>				    		   						
				    		   							<input type="checkbox" id="checkboxDistinct" checked="checked" onclick="handleDistinct()"> distinct
				    		   						<%}else{ %>
				    		   							<input type="checkbox" id="checkboxDistinct" onclick="handleDistinct()"> distinct
				    		   						<%} %>
				    		   						<input type="hidden" id="selectDistinct" name="selectDistinct" value="<%=aWizardObject.getDistinct()%>"/>
				    		   					</td>
				    		   			</tr>
				    		   		<%}//endif %>
				    		   	
				    		 <%}//endif %>
				    		 <% if ((aWizardObject.getSelectClause() != null) && (aWizardObject.getSelectClause().getSelectFields().size() > 0)){ %>
				    		 <%} %>
				    		 </table>
				    		 </form>
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
	</table>
	
--%>




<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>

<div id="divOperatoriSelect">
 	<table>
 	
 		<tr>
 			<td class="even">
 				<a href="javascript:applyOperator('sum')"/>sum()</a>
 			</td>
 		</tr>
 		
 	 	<tr>
 			<td>
 				<a href="javascript:applyOperator('avg')"/>avg()</a>
 			</td>
 		</tr>
 		
 		<tr>
 			<td class="even">
 				<a href="javascript:applyOperator('min')"/>min()</a>
 			</td>
 		</tr>
 		<tr>
 			<td>
 				<a href="javascript:applyOperator('max')"/>max()</a>
 			</td>
 		</tr>
 		
 		<tr>
 			<td class="even">
 				<a href="javascript:applyOperator('count')"/>count()</a>
 			</td>
 		</tr>
 		
 		<tr>
 			<td>
 				<a href="javascript:closeDiv()"/><%=qbeMsg.getMessage(requestContainer, "QBE.Selection.close", bundle) %></a>
 			</td>
 		</tr>
	</table>
 </div>
 
 <div id="divSpanCurrent">
   <span id="currentScreen">DIV_FIELD_SELECTION</span>
 </div>	
 
 
<%@include file="../jsp/qbefooter.jsp" %>

</div>
