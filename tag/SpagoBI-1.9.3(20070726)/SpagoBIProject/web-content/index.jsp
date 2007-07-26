<!--
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
-->


<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*,
                 it.eng.spagobi.constants.SpagoBIConstants"
%>

<%      
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	String authFailed = "";
	ResponseContainer aResponseContainer = ResponseContainerAccess.getResponseContainer(request);
	if(aResponseContainer!=null) {
		SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
		if(aServiceResponse!=null) {
			SourceBean loginModuleResponse = (SourceBean)aServiceResponse.getAttribute("LoginModule");
			if(loginModuleResponse!=null) {
				String authFailedMessage = (String)loginModuleResponse.getAttribute(SpagoBIConstants.AUTHENTICATION_FAILED_MESSAGE);
				authFailed = authFailedMessage;
			}
		}
	}
    
%>

<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<html>
  <head>
    <title>SpagoBI</title>
    <style>
      body {
	       padding: 0;
	       margin: 0;
      }
    </style> 
  </head>

  <body>
    <div id="background" style="width:100%;height:100%;background-image:url(<%=contextName%>/img/wapp/background.jpg);background-repeat:no-repeat;background-position: top left;"> 
      <div id="backgroundlogo" style="width:100%;height:100%;background-image:url(<%=contextName%>/img/wapp/backgroundlogo.jpg);background-repeat:no-repeat;background-position: bottom right;"> 
        <div id="header" style="width:100%;height:70px;">
            <div id="logotitle" style="height:57px;background-image:url(<%=contextName%>/img/wapp/titlelogo.gif);background-repeat:no-repeat;background-position: top left;"> 
            </div>
            <div id="menubar" style="width:100%;height:18px;border-top:1px solid gray;border-bottom:1px solid gray;background-image:url(<%=contextName%>/img/wapp/backgroundMenuBar.jpg);background-repeat:repeat-x;"> 
            </div>
        </div>
        <form action="<%=contextName%>/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE" method="POST" >
	        <div id="content" style="width:100%;">
		        	<div style="background-color:white;width:500px;height:150px;border:1px solid gray;margin-top:130px;margin-left:50px;" >
		        		<table>
		        			<tr>
		        				<td width = "100px">
		        				   <img src="<%=contextName%>/img/wapp/loginUser64.png" />
		        				</td>
		        				<td>
		        				    <br/>
		        					UserName:&nbsp;<input name="userID" type="text" size="30" />
		        					<br/><br/>
		        				    Password:&nbsp;&nbsp;<input name="password" type="password" size="30" />
		        				    <br/>
		        				</td>
		        				<td>
		        				    <br/>
		        					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		        					<input type="image" 
		        					       src="<%=contextName%>/img/wapp/next32.png" 
		        					       title="Login" alt="Login"/>
		        				</td>
		        			</tr>
		        			<tr>
		        				<td>&nbsp;</td>
		        				<td style='color:red;font-size:11pt;'><br/><%=authFailed%></td>
		        				<td>&nbsp;</td>
		        			</tr>
		
		        		</table>
		        	</div>
	        </div>
        </form>
        <div id="footer" style="width:100%;height:50px;">
        </div>
      </div>
    </div>
  </body>
</html>
