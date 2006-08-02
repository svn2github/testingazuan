<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>
<%@ page import="it.eng.spago.base.*, it.eng.qbe.utility.*,it.eng.qbe.javascript.*, it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>




<%@ include file="../jsp/qbe_base.jsp" %>



<%   
   
   ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
   
   Map selectedFields = new HashMap();
   if(aWizardObject.getOrderByClause() != null){ 	
		List l = aWizardObject.getOrderByClause().getOrderByFields();
		if (l != null){
			java.util.Iterator it = l.iterator();
			IOrderGroupByField aOrderByField = null;
			
			while (it.hasNext()){
				aOrderByField = (IOrderGroupByField)it.next();
				selectedFields.put(aOrderByField.getFieldName(), aOrderByField);
			}			
		}
   }
   
   dm.updateCurrentClassLoader();
%>
	

	
	


<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 

		    style='vertical-align:middle;padding-left:5px;'>
			<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Ordering", bundle) %>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%@include file="../jsp/qbe_headers.jsp"%>
	</tr>
</table>


<%@include file="../jsp/testata.jsp" %>


<div class='div_background_no_img'>

<% if ((aWizardObject.getSelectClause() != null) && (aWizardObject.getSelectClause().getSelectFields().size() > 0)){%> 
		
		
		<%
		  java.util.Map params = new java.util.HashMap();
		  params.put("ACTION_NAME","SELECT_FIELD_FOR_ORDERBY_ACTION");
		  String formUrl = qbeUrl.getUrl(request, params);
		%>
		<form method='POST' action='<%=formUrl%>' id ='treeForm' name='treeForm'>
		
		
		
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
		  					if(aWizardObject.getSelectClause() != null){ 			 						
		    		  		List l = aWizardObject.getSelectClause().getSelectFields();
		    		  		if(l!= null){
		    		   			java.util.Iterator it = l.iterator();
		    		   			ISelectField aSelectField = null;
		    		   			String originalFieldName = "";
		    		   			String urlOrderBy = "";
		    		   			while (it.hasNext()){
		    		   				aSelectField = (ISelectField)it.next();
		    		   				originalFieldName = (String)aSelectField.getFieldName();
		    		   				oParams.clear();
		    		   				oParams.put("ACTION_NAME","SELECT_FIELD_FOR_ORDERBY_ACTION");
		    		   				oParams.put("COMPLETE_FIELD_NAME",originalFieldName);
		    		   				urlOrderBy = qbeUrl.getUrl(request, oParams);
									// if no operators has applied
		    		   				//if (originalFieldName.equalsIgnoreCase(aSelectField.getFieldName())){
		    		   				String checked = "";
		    		   				if(selectedFields.containsKey(originalFieldName)) checked="checked=\"checked\"";
		    		   				%>		    		   				
		    		   	<tr>
		    		   		<td colspan="2">
		    		   			<INPUT type=CHECKBOX name="field" value="<%=originalFieldName%>" <%=checked%> >		    		   						
		    		   			<a class="qbe-font-link"  href="<%=urlOrderBy %>"> 
		    		   				<%=Utils.getLabelForQueryField(requestContainer,dm,aWizardObject, originalFieldName) %>
		    		   			</a>
		    		   		</td>
		    		   	</tr>		    		   				
		    		   				<% //} %>
		    		   			<%}//endwhile%>    		   			
		    		   		<%}//endif %>
		    			 <%}//endif %>
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
						       src='<%=qbeUrl.conformStaticResourceLink(request,"../img/refresh.gif")%>' 
						       alt='<%=qbeMsg.getMessage(requestContainer,"QBE.Update", bundle) %>' 
						       title='<%=qbeMsg.getMessage(requestContainer,"QBE.Update", bundle) %>'/>
						<br/>
						<a href="javascript:document.getElementById('groupForm').submit()" class="qbe-title-link">
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
		    		<% if (aWizardObject.getOrderByClause() != null){ %>
			 						
		    		   			<% List l = aWizardObject.getOrderByClause().getOrderByFields();
		    		   			if (l != null){
		    		   				java.util.Iterator it = l.iterator();
		    		   				IOrderGroupByField aOrderByField = null;
		    		   				
		    		   				String urlDeleteOrderBy = "";
		    		   				String urlMoveUp ="";
		    		   				String urlMoveDown = "";
		    		   				while (it.hasNext()){
		    		   					aOrderByField = (IOrderGroupByField)it.next();
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","DELETE_FIELD_FOR_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlDeleteOrderBy = qbeUrl.getUrl(request, oParams);
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","MOVE_UP_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlMoveUp = qbeUrl.getUrl(request, oParams);
		    		   					
		    		   					oParams.clear();
		    		   					oParams.put("ACTION_NAME","MOVE_DOWN_ORDERBY_ACTION");
		    		   					oParams.put("FIELD_ID",aOrderByField.getId());
		    		   					urlMoveDown = qbeUrl.getUrl(request, oParams);
		    		   					
		    		   				%>
		    		   				<tr>
		    		   					<td width="5%">
		    		   						<a href="<%=urlDeleteOrderBy %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromOrdering", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromOrdering", bundle) %>"/></a>
		    		   					</td>
		    		   					<td  width="5%">
				    		   						<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>"/></a>
				    		   			</td>
				    		   					
				    		   			<td width="5%">
				    		   						<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>"/></a>
				    		   			</td>
		    		   					<td width="85%" class="qbe-font">
		    		   						 <%=Utils.getLabelForQueryField(requestContainer,dm, aWizardObject,aOrderByField.getFieldName()) %>
		    		   					</td>	
		    		   				</tr>
		    		   			<%}//endwhile%>
		    		   		<%}//endif %>
		    		 <%}//endif %>
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









 <% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
</body>
<%}%>
<div id="divSpanCurrent">
<span id="currentScreen">DIV_FIELD_ORDER_BY</span>
</div>
<%@include file="../jsp/qbefooter.jsp" %>

</div>
