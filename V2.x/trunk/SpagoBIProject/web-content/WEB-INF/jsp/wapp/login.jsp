<%--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"
         session="true" 
         import="it.eng.spago.base.*,
                 it.eng.spagobi.commons.constants.SpagoBIConstants"
%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.commons.utilities.messages.IMessageBuilder"%>
<%@page import="it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.UrlBuilderFactory"%>
<%@page import="it.eng.spagobi.commons.utilities.urls.IUrlBuilder"%>
<%@page import="it.eng.spago.configuration.ConfigSingleton"%>
<%@page import="it.eng.spago.base.SourceBean"%>

<%      
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	String authFailed = "";
	String startUrl = "";
	ResponseContainer aResponseContainer = ResponseContainerAccess.getResponseContainer(request);
	//RequestContainer requestContainer = RequestContainerAccess.getRequestContainer(request); 
	
	ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	SourceBean securitySB = (SourceBean) serverConfig.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS");
	
	String strInternalSecurity = (String) securitySB.getAttribute("className");
	boolean isInternalSecurity = (strInternalSecurity.indexOf("InternalSecurity")>0)?true:false;
	
	RequestContainer requestContainer = RequestContainer.getRequestContainer();
	
	String currTheme=ThemesManager.getCurrentTheme(requestContainer);
	if(currTheme==null)currTheme=ThemesManager.getDefaultTheme();
	 
	if(aResponseContainer!=null) {
		SourceBean aServiceResponse = aResponseContainer.getServiceResponse();
		if(aServiceResponse!=null) {
			SourceBean loginModuleResponse = (SourceBean)aServiceResponse.getAttribute("LoginModule");
			if(loginModuleResponse!=null) {
				String authFailedMessage = (String)loginModuleResponse.getAttribute(SpagoBIConstants.AUTHENTICATION_FAILED_MESSAGE);
				startUrl = (loginModuleResponse.getAttribute("start_url")==null)?"":(String)loginModuleResponse.getAttribute("start_url");
				if(authFailedMessage!=null) {
					authFailed = authFailedMessage;
				}				
			}
		}
	}

	
	IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
	
	String sbiMode = "WEB";
	IUrlBuilder urlBuilder = null;
	urlBuilder = UrlBuilderFactory.getUrlBuilder(sbiMode);
%>



<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.utilities.themes.ThemesManager"%>
<html>
  <head>
	<link rel="shortcut icon" href="<%=urlBuilder.getResourceLink(request, "img/favicon.ico")%>" />
    <title>SpagoBI</title>
    <style>
      body {
	       padding: 0;
	       margin: 0;
      }
    </style> 
  </head>

  <body>
<% 
String url="";
if(ThemesManager.resourceExists(currTheme,"/html/banner.html")){
	url = "/themes/"+currTheme+"/html/banner.html";	
}
else {
	url = "/themes/sbi_default/html/banner.html";	
}

%>	<jsp:include page='<%=url%>' />
        <form action="<%=contextName%>/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE" method="POST" >
        	<input type="hidden" id="isInternalSecurity" name="isInternalSecurity" value="<%=isInternalSecurity %>" />
	        <div id="content" style="width:100%;">
		        	<div style="background-color:white;width:500px;height:150px;border:1px solid gray;margin-top:130px;margin-left:50px;" >
		        		<table>
		        			<tr>
		        				<td width = "100px">
		        				   <img src="<%=urlBuilder.getResourceLinkByTheme(request, "/img/wapp/loginUser64.png", currTheme)%>"/>
		        				</td>
		        				<td>
		        				    <br/> 
		        				    <table>
		        				    	<tr>
		        				    		<td width="150px">
		        								<%=msgBuilder.getMessage("username")%>:
		        							</td>
		        							<td width="30px">&nbsp;</td>
		        							<td>
		        								<input name="userID" type="text" size="30" />
		        							</td>	
		        						</tr>
		        						<tr>
		        				    	<td width="150px">
		        								<%=msgBuilder.getMessage("password")%>:
		        							</td>
		        							<td width="30px">&nbsp;</td>
		        							<td>
		        								<input name="password" type="password" size="30" />
		        							</td>	
		        						</tr>
		        						<% if (isInternalSecurity) {%>
			        						<tr><td>&nbsp;</td></tr>
			        						<tr>
			        							<td>&nbsp;</td>
			        							<td>&nbsp;</td>
			        							<td width="150px">
	  		        							  	<a href="<%=contextName %>/ChangePwdServlet?start_url=<%=startUrl%>">
						             						<%=msgBuilder.getMessage("changePwd")%>
						             				</a>
	  		        							</td>
			        						</tr>
			        					<% } %>
		        					</table>	
		        				</td>
		        				<td>
		        					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		        					<input type="image" 
		        					       src="<%=urlBuilder.getResourceLinkByTheme(request, "/img/wapp/next32.png", currTheme)%>" 
		        					       title="<%=msgBuilder.getMessage("login")%>" 
		        					       alt="<%=msgBuilder.getMessage("login")%>"/>
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
        <spagobi:error/>
        <div id="footer" style="width:100%;height:50px;">
        </div>

      </div>
    </div>
    
    
    <% 
String url2="";
if(ThemesManager.resourceExists(currTheme,"/html/footer.html")){
	url2 = "/themes/"+currTheme+"/html/footer.html";	
}
else {
	url2 = "/themes/sbi_default/html/footer.html";	
}
%>
	<jsp:include page='<%=url2%>' />

  </body>
</html>
