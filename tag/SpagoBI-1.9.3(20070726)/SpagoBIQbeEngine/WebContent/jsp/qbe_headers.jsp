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
							

