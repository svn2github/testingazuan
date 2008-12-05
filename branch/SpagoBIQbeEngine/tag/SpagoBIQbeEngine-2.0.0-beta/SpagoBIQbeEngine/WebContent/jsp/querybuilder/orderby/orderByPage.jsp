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





<%@ include file="/jsp/qbe_base.jsp"%>



<%   
   
   Map selectedFields = new HashMap();
  
	Iterator it =  query.getOrderByFieldsIterator();
	IOrderByField aOrderByField = null;
			
	while (it.hasNext()){
		aOrderByField = (IOrderByField)it.next();
		selectedFields.put(aOrderByField.getFieldName(), aOrderByField);
	}
  
   
   //dm.updateCurrentClassLoader();
%>
	

	
	


<qbe:page>
 	<qbe:page-content>
		
		<%@include file="/jsp/commons/titlebar.jspf" %>
		<%@include file="/jsp/testata.jsp" %>	
 
 
		<div class='div_background_no_img'>
		
		
		
		
<% if ( !query.isEmpty() ){%> 
		
		
		<%
		  java.util.Map params = new java.util.HashMap();
		  params.put("ACTION_NAME","SELECT_FIELD_FOR_ORDERBY_ACTION");
		  String formUrl = qbeUrl.getActionUrl(request, params);
		  
		  it = null;
		%>
		<form method='POST' action='<%=formUrl%>' id ='orderForm' name='orderForm'>
		
		
		
		<table width="100%">
			<tr>
			  	<td width="2%">
    				&nbsp;
	   			</td>
	   			<td width="43%">
    				&nbsp;
	   			</td>
	   			<td width="10%">
    				&nbsp;
	   			</td>
				<td width="43%">
    				&nbsp;
	   			</td>
				<td width="2%">
    				&nbsp;
	   			</td>
	   		</tr>  
		   	<tr>
		   		
		   		
		   		
		   		<td>&nbsp;</td>
		  		
		  		
		  		
		  		<td valign="top" class="qbe-td-form">

		  			<!-- ============ START TABLE FIELD ON LEFT ================== -->
		  			<table width="100%">
		  				<tr>
		    		   		<td colspan="2">
		    		   		 	<span class="qbeTitle">
								     <%=qbeMsg.getMessage(requestContainer, "QBE.Ordering.ChooseFromSelectedFields", bundle) %>
								 </span>
		    		   		</td>   		   				
		    			</tr>
		    			<tr>
			   				<td colspan="2">
    							&nbsp;
	   						</td>
		   				</tr>  
		  		
		  				<% 
				    		java.util.Map oParams = new java.util.HashMap();
		  		  			oParams.clear();
		  					
		    		   			it = query.getSelectFieldsIterator();
		    		   			ISelectField aSelectField = null;
		    		   			String originalFieldName = "";
		    		   			String urlOrderBy = "";
		    		   			while (it.hasNext()){
		    		   				aSelectField = (ISelectField)it.next();
		    		   				originalFieldName = (String)aSelectField.getFieldName();
		    		   				oParams.clear();
		    		   				oParams.put("ACTION_NAME","SELECT_FIELD_FOR_ORDERBY_ACTION");
		    		   				oParams.put("COMPLETE_FIELD_NAME",originalFieldName);
		    		   				urlOrderBy = qbeUrl.getActionUrl(request, oParams);
									// if no operators has applied
		    		   				//if (originalFieldName.equalsIgnoreCase(aSelectField.getFieldName())){
		    		   				String checked = "";
		    		   				if(selectedFields.containsKey(originalFieldName)) checked="checked=\"checked\"";
		    		   				%>		    		   				
		    		   	<tr>
		    		   		<td colspan="2" ALIGN="left">
		    		   			<INPUT type=CHECKBOX name="field" value="<%=originalFieldName%>" <%=checked%> >		    		   						
		    		   			<a class="qbe-font-link"  href="<%=urlOrderBy %>"> 
		    		   				<%=JsTreeUtils.getLabelForQueryField(requestContainer,datamartModel, datamartWizard, originalFieldName) %>
		    		   			</a>
		    		   		</td>
		    		   	</tr>		    		   				
		    		   				<% //} %>
		    		   			<%}//endwhile%>    		   			
		    		   		
		    			<tr>
		    		   		<td colspan="2">
		    		   			&nbsp;
		    		   		</td>  		   				
		    			</tr>
		    		</table>
		    		<!-- ============ END TABLE FIELD ON LEFT ================== -->
		    	</td>
		    	
		    	
		    	
		    	<td align="center">
						<br/>
						<input type="image"
						       src='<%=qbeUrl.getResourceUrl(request,"../img/refresh.gif")%>' 
						       alt='<%=qbeMsg.getMessage(requestContainer,"QBE.Update", bundle) %>' 
						       title='<%=qbeMsg.getMessage(requestContainer,"QBE.Update", bundle) %>'/>
						<br/>
						<a href="javascript:document.getElementById('orderForm').submit()" class="qbe-title-link">
							<%=qbeMsg.getMessage(requestContainer,"QBE.Update", bundle) %>
						</a>
					</td>
		    	
		    	
		    	
		    	
		    	<td valign="top" class="qbe-td-form">
		    	
		    		<!-- ============ START TABLE FIELD ON RIGHT ================== -->
		    		<table width="100%"> 
		    			<tr>
		    		   		<td colspan="4">
		    		   		 	<span class="qbeTitle">
										<%=qbeMsg.getMessage(requestContainer, "QBE.Ordering.SelectedFields", bundle) %>
							    </span>
		    		   		</td>  		   				
		    			</tr>
		    			<tr>
			   				<td colspan="4">
    							&nbsp;
	   						</td>
		   				</tr>  
		    		
			 						
		    		   			<%
		    		   				it = query.getOrderByFieldsIterator();
		    		   				aOrderByField = null;
		    		   				
		    		   				String urlDeleteOrderBy = "";
		    		   				String urlMoveUp ="";
		    		   				String urlMoveDown = "";
		    		   				String urlSwitchOrder = "";
		    		   				while (it.hasNext()){
		    		   					aOrderByField = (IOrderByField)it.next();
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","DELETE_FIELD_FOR_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlDeleteOrderBy = qbeUrl.getActionUrl(request, oParams);
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","MOVE_UP_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlMoveUp = qbeUrl.getActionUrl(request, oParams);
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","MOVE_DOWN_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlMoveDown = qbeUrl.getActionUrl(request, oParams);
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","SWITCH_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlSwitchOrder = qbeUrl.getActionUrl(request, oParams);
		    		   					
		    		   				%>
		    		   				<tr>
		    		   					<td width="5%">
		    		   						<a href="<%=urlDeleteOrderBy %>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromOrdering", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromOrdering", bundle) %>"/></a>
		    		   					</td>
		    		   					<td  width="5%">
				    		   						<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>"/></a>
				    		   			</td>
				    		   					
				    		   			<td width="5%">
				    		   						<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>"/></a>
				    		   			</td>
				    		   			
				    		   		<%
				    		   			if(aOrderByField.isAscendingOrder()) {
				    		   		%>
				    		   			<td width="5%">
				    		   						<a href="<%=urlSwitchOrder%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/ascendingOrder.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSwitchToDescendingOrder", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSwitchToDescendingOrder", bundle) %>"/></a>
				    		   			</td>
				    		   		<%} else { %>
				    		   			<td width="5%">
				    		   						<a href="<%=urlSwitchOrder%>"><img src="<%=qbeUrl.getResourceUrl(request,"../img/descendingOrder.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSwitchToAscendingOrder", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgSwitchToAscendingOrder", bundle) %>"/></a>
				    		   			</td>
				    		   		<%} %>
		    		   					<td width="85%" class="qbe-font" ALIGN="left">
		    		   						 <%=JsTreeUtils.getLabelForQueryField(requestContainer,datamartModel, datamartWizard,aOrderByField.getFieldName()) %>
		    		   					</td>	
		    		   				</tr>
		    		   			<%}//endwhile%>
		    		 
		    		 	<tr>
		    		   		<td colspan="4">
		    		   			&nbsp;
		    		   		</td>  		   				
		    			</tr>
		    		</table>
		    		<!-- ============ END TABLE FIELD ON RIGHT ================== -->		    		
		  		</td>
		  		
		  		
		  		
		  		<td>&nbsp;</td>
		  		
		  		
		  		</tr>
		 	</table>
		 	</form>
		 	
		 	
		 	
		 	
		 	
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


		<div id="divSpanCurrent">
			<span id="currentScreen">DIV_FIELD_ORDER_BY</span>
		</div>

		<script type="text/javascript">
			changeTabBkg();
		</script>
	
	
		</div>

	</qbe:page-content>
</qbe:page>
