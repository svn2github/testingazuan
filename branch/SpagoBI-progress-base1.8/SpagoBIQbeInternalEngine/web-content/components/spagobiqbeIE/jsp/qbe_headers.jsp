

<% 
                             if(qbeMode.equalsIgnoreCase("PORTLET")){
                                 
                            	 
	                             java.util.Map params = new java.util.HashMap();
                                 
                                 params.put("ACTION_NAME", "RECOVER_CL_ACTION");
                                 
                                 String url = qbeUrl.getUrl(request, params);    			

                                 String startModifyTimeStamp =(String)sessionContainer.getAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP"); 
                                 String lastUpdTimeStamp =(String)sessionContainer.getAttribute("QBE_LAST_UPDATE_TIMESTAMP");
                               
                             %>
                             
			     
			     <%--
								<TD align="right">
									<a href="javascript:checkSavingBeforeBack('<%=url%>', '<%=aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : " " %>','<%=startModifyTimeStamp != null ? startModifyTimeStamp : " "%>', '<%=lastUpdTimeStamp != null ? lastUpdTimeStamp : " "%>' )">
									<img height="30px"
        			 	     			width="30px" 
        			 	     			src='<%=qbeUrl.conformStaticResourceLink(request,"../img/back.png")%>' 
      			             			alt='Back' 
      			            			title='Back'/>
      			            		</a>
								</TD>
							
				--%>	
				
				
				<td class='header-button-column-portlet-section'>
					<a href="javascript:checkSavingBeforeBack('<%=url%>', '<%=aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : " " %>','<%=startModifyTimeStamp != null ? startModifyTimeStamp : " "%>', '<%=lastUpdTimeStamp != null ? lastUpdTimeStamp : " "%>' )"> 
      						<img class='header-button-image-portlet-section' title='Back'
						src='<%=qbeUrl.conformStaticResourceLink(request,"../img/back.png")%>' alt='Back' />
					</a>
				</td>			
							
<% } %>
