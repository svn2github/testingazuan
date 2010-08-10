

 <% 
                                 
                            	 
	                             java.util.Map paramsBack = new java.util.HashMap();
                                 
 								 paramsBack.put("ACTION_NAME", "RECOVER_CL_ACTION");
                                 
                                 String url = qbeUrl.getUrl(request, paramsBack);    			

                                 String startModifyTimeStamp =(String)sessionContainer.getAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP"); 
                                 String lastUpdTimeStamp =(String)sessionContainer.getAttribute("QBE_LAST_UPDATE_TIMESTAMP");
                               
%>
                             
			     
<% if (!Utils.isSubQueryModeActive(sessionContainer)){ %>
				
				<td class='header-button-column-portlet-section'>
					<a href="javascript:checkSavingBeforeBack('<%=url%>', '<%=aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : " " %>','<%=startModifyTimeStamp != null ? startModifyTimeStamp : " "%>', '<%=lastUpdTimeStamp != null ? lastUpdTimeStamp : " "%>' )"> 
      						<img class='header-button-image-portlet-section' title='Back'
						src='<%=qbeUrl.conformStaticResourceLink(request,"../img/back.gif")%>' alt='Back' />
					</a>
				</td>			
<% } %>
							

