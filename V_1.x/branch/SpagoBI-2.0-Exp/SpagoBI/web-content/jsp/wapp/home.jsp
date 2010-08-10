<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*"
%>
<%@ page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.utilities.messages.MessageBuilderFactory"%>
<%@page import="it.eng.spagobi.utilities.messages.IMessageBuilder"%>

<%@ taglib uri="/WEB-INF/tlds/spagobiwa.tld" prefix="spagobiwa" %>

<%      
	IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();	
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	RequestContainer aRequestContainer = RequestContainerAccess.getRequestContainer(request); 
%>


<html>
  <head>
    <title>SpagoBI Home</title>
    
    <script type="text/javascript" src="<%=contextName%>/js/prototype/javascripts/prototype.js"></script>
    <script type="text/javascript" src="<%=contextName%>/js/prototype/javascripts/window.js"></script>
    <script type="text/javascript" src="<%=contextName%>/js/prototype/javascripts/effects.js"></script>
    <link href="<%=contextName%>/js/prototype/themes/alphacube.css" rel="stylesheet" type="text/css"/> 
    <link href="<%=contextName%>/js/prototype/themes/default.css" rel="stylesheet" type="text/css"/> 
    
    <script type="text/javascript">
	     var djConfig = {isDebug: false, debugAtAllCosts: false};
    </script>
    <script type="text/javascript" src="<%=contextName%>/js/dojo/dojo.js"></script>
    <script language="JavaScript" type="text/javascript">
    	dojo.require("dojo.widget.FisheyeList");
    	dojo.hostenv.writeIncludes();
    </script>

    
    <style>
      body {
	       font-family: Arial, Helvetica, sans-serif;
	       padding: 0;
	       margin: 0;
      }
      img {
      	padding: 0px; 
      	margin: 0px; 
      	border: none;		
      }
      .dojoHtmlFisheyeListBar {
      	margin: 0 auto;
      	text-align: left;
      }
      .outerbar {
      	text-align: left;
      	position: absolute;
      	left: 50px;
      	bottom: 0px;
      	width: 50%;
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
            	<div style="position:absolute;right:15px;">
                    <a href="<%=contextName%>/servlet/AdapterHTTP?PAGE=LogoutPage">
                        <img src="<%=contextName%>/img/wapp/exit16.png" 
                             title="<%=msgBuilder.getMessage(aRequestContainer, "logout")%>" 
                             alt="<%=msgBuilder.getMessage(aRequestContainer, "logout")%>" />
                    </a>
                </div>
            </div>
        </div>
        <div id="content" style="width:100%;">
        </div>
        <div id="footer" style="width:100%;height:50px;">
        	<spagobiwa:FisheyeMenu />
        </div>
  </body>
  
</html>







