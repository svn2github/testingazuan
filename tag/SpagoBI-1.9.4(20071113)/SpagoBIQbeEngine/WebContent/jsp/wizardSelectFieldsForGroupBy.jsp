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

<%@ page import="java.util.*"%>
<%@ page import="it.eng.spago.base.*"%>
<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.urlgenerator.*"%>
<%@ page import="it.eng.qbe.wizard.*"%>




<%@ include file="../jsp/qbe_base.jsp" %>


<%   
   Object spagoBiInfo = sessionContainer.getAttribute("spagobi"); 

   ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(sessionContainer);
   it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel)sessionContainer.getAttribute("dataMartModel"); 
   
   Map selectedFields = new HashMap();
   if(aWizardObject.getGroupByClause() != null){ 	
		List l = aWizardObject.getGroupByClause().getGroupByFields();
		if (l != null){
			java.util.Iterator it = l.iterator();
			IOrderGroupByField aGroupByField = null;
			
			while (it.hasNext()){
				aGroupByField = (IOrderGroupByField)it.next();
				selectedFields.put(aGroupByField.getFieldName(), aGroupByField);
			}			
		}
   }
   
   //dm.updateCurrentClassLoader();
%>

<% if (qbeMode.equalsIgnoreCase("WEB")){ %> 
<body>
<%}%>

<%
	if(spagoBiInfo == null) {
%>
<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 

		    style='vertical-align:middle;padding-left:5px;'>
			<%= dm.getName() %> : <%=dm.getDescription() %> - <%=qbeMsg.getMessage(requestContainer, "QBE.Title.Grouping", bundle) %>
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

<% if ((aWizardObject.getSelectClause() != null) && (aWizardObject.getSelectClause().getSelectFields().size() > 0)){ %>
		
		
		<% 
		  java.util.Map params = new java.util.HashMap();
		  params.put("ACTION_NAME","SELECT_FIELD_FOR_GROUPBY_ACTION");
		  String formUrl = qbeUrl.getUrl(request, params);
		%>
		<form method='POST' action='<%=formUrl%>' id ='groupForm' name='groupForm'>
		
		
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
		  			
		  			<table width="100%">
		  				<tr>
		    		   		<td colspan="2">
		    		   		 	<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer,"QBE.Grouping.ChooseFromSelectedFields", bundle) %></span>
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
			  			 if (aWizardObject.getSelectClause() != null){ 
			    		   			List l = aWizardObject.getSelectClause().getSelectFields();
			    		   			if (l != null){
			    		   				java.util.Iterator it = l.iterator();
			    		   				ISelectField aSelectField = null;
			    		   				String originalFieldName = "";
			    		   				String urlOrderBy = ""; 
			    		   				while (it.hasNext()){
			    		   					aSelectField = (ISelectField)it.next();
			    		   					originalFieldName = (String)aSelectField.getFieldNameWithoutOperators();
			    		   					oParams.clear();
			    		   					oParams.put("ACTION_NAME","SELECT_FIELD_FOR_GROUPBY_ACTION");
			    		   					oParams.put("COMPLETE_FIELD_NAME",originalFieldName);
			    		   					urlOrderBy = qbeUrl.getUrl(request, oParams);
			    		   					//if no operators has applied
			    		   					if (originalFieldName.equalsIgnoreCase(aSelectField.getFieldName())){
			    		   						String checked = "";
			    		   						if(selectedFields.containsKey(originalFieldName)) checked="checked=\"checked\"";
			    		   				%>
			    		<tr>
			    		   	<td colspan="2" ALIGN="left">
			    		   		<INPUT type=CHECKBOX name="field" value="<%=originalFieldName%>" <%=checked%> >
			    		   		<a href="<%=urlOrderBy %>" class="qbe-font-link"> 
			    		   			<%=Utils.getLabelForQueryField(requestContainer,dm, aWizardObject,originalFieldName) %>
			    		   		</a>
			    		   	</td>				
			    		</tr>
			    		   			<% } %>
			    		   			<%}//endwhile%>		    		   				
			    		   		<%}//endif %>
			    		 <%}//endif %>
			    		 
			    		 <tr>
			    		   <td colspan="2">
			    		   		 &nbsp;
			    		   </td>   		   				
			    		</tr>
			    	</table>
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
		  			<table width="100%">
			    		 <tr>
			    		   <td colspan="4">
			    		   		<span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer,"QBE.Grouping.SelectedFields", bundle) %> </span>
			    		   </td>  		   				
			    		</tr>
			    		<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
			    		<% if (aWizardObject.getGroupByClause() != null){ %>
				 						
			    		   			<% List l = aWizardObject.getGroupByClause().getGroupByFields();
			    		   			if (l != null){
			    		   				java.util.Iterator it = l.iterator();
			    		   				IOrderGroupByField aOrderByField = null;
			    		   				String originalFieldName = "";
			    		   				String urlDeleteOrderBy = "";
			    		   				String urlMoveUp ="";
			    		   				String urlMoveDown = "";
			    		   				while (it.hasNext()){
			    		   					aOrderByField = (IOrderGroupByField)it.next();
			    		   					originalFieldName = (String)aOrderByField.getFieldName();
			    		   					oParams.clear();
			    		   					oParams.put("ACTION_NAME","DELETE_FIELD_FOR_GROUPBY_ACTION");
			    		   					oParams.put("FIELD_ID",aOrderByField.getId());
			    		   					
			    		   					urlDeleteOrderBy = qbeUrl.getUrl(request, oParams);
			    		   					
			    		   					oParams.clear();
			    		   					oParams.put("ACTION_NAME","MOVE_UP_GROUPBY_ACTION");
			    		   					oParams.put("FIELD_ID",aOrderByField.getId());
			    		   					urlMoveUp = qbeUrl.getUrl(request, oParams);
			    		   					
			    		   					oParams.clear();
			    		   					oParams.put("ACTION_NAME","MOVE_DOWN_GROUPBY_ACTION");
			    		   					oParams.put("FIELD_ID",aOrderByField.getId());
			    		   					urlMoveDown = qbeUrl.getUrl(request, oParams);
			    		   					
			    		   				%>
			    		   				<tr>
			    		   					<td width="5%">
			    		   						<a href="<%=urlDeleteOrderBy %>">
			    		   						<img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/delete.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromGrouping", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgDeleteFromGrouping", bundle) %>" /></a>
			    		   					</td>
			    		   					<td  width="5%">
				    		   						<a href="<%=urlMoveUp %>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowUp.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveUp", bundle) %>"/></a>
				    		   				</td>
				    		   					
				    		   				<td width="5%">
				    		   						<a href="<%=urlMoveDown%>"><img src="<%=qbeUrl.conformStaticResourceLink(request,"../img/ArrowDown.gif")%>" alt="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>" title="<%= qbeMsg.getMessage(requestContainer, "QBE.alt.imgMoveDown", bundle) %>"/></a>
				    		   				</td>
			    		   					<td width="85%" class="qbe-font" ALIGN="left">
			    		   						 <%--<%=originalFieldName %> --%>
			    		   						<%=Utils.getLabelForQueryField(requestContainer,dm,aWizardObject, originalFieldName) %>
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
	  					<span class="qbeError"> <%=qbeMsg.getMessage(requestContainer, "QBE.Warning.SelectFieldBeforeGrouping", bundle) %></span>	
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
	<span id="currentScreen">DIV_FIELD_GROUP_BY</span>
</div>
<%@include file="../jsp/qbefooter.jsp" %>


</div>
