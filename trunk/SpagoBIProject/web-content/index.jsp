<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*,
                 it.eng.spagobi.constants.SpagoBIConstants"
%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.utilities.messages.IMessageBuilder"%>
<%@page import="it.eng.spagobi.utilities.messages.MessageBuilderFactory"%>

<%      
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	String authFailed = "";
	ResponseContainer aResponseContainer = ResponseContainerAccess.getResponseContainer(request);
	RequestContainer requestContainer = RequestContainerAccess.getRequestContainer(request); 
	if(aResponseContainer!=null) {
		SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
		if(aServiceResponse!=null) {
			SourceBean loginModuleResponse = (SourceBean)aServiceResponse.getAttribute("LoginModule");
			if(loginModuleResponse!=null) {
				String authFailedMessage = (String)loginModuleResponse.getAttribute(SpagoBIConstants.AUTHENTICATION_FAILED_MESSAGE);
				if(authFailedMessage!=null) {
					authFailed = authFailedMessage;
				}
			}
		}
	}
	
	IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
%>



<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
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
        <form action="<%=contextName%>/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE&<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>=TRUE" method="POST" >
	        <div id="content" style="width:100%;">
		        	<div style="background-color:white;width:500px;height:150px;border:1px solid gray;margin-top:130px;margin-left:50px;" >
		        		<table>
		        			<tr>
		        				<td width = "100px">
		        				   <img src="<%=contextName%>/img/wapp/loginUser64.png" />
		        				</td>
		        				<td>
		        				    <br/> 
		        				    <table>
		        				    	<tr>
		        				    		<td width="150px">
		        								<%=msgBuilder.getMessage("username", request)%>:
		        							</td>
		        							<td width="30px">&nbsp;</td>
		        							<td>
		        								<input name="userID" type="text" size="30" />
		        							</td>	
		        						</tr>
		        						<tr>
		        				    	<td width="150px">
		        								<%=msgBuilder.getMessage("password", request)%>:
		        							</td>
		        							<td width="30px">&nbsp;</td>
		        							<td>
		        								<input name="password" type="password" size="30" />
		        							</td>	
		        						</tr>
		        					</table>	
		        				</td>
		        				<td>
		        					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		        					<input type="image" 
		        					       src="<%=contextName%>/img/wapp/next32.png" 
		        					       title="<%=msgBuilder.getMessage("login", request)%>" 
		        					       alt="<%=msgBuilder.getMessage("login", request)%>"/>
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
