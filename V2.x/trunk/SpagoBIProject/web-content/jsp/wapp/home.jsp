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
         import="it.eng.spago.base.*"
%>

<%@ include file="/jsp/commons/portlet_base.jsp"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="java.util.List"%>
<%@page import="it.eng.spagobi.wapp.bo.Menu"%>

<%@ taglib uri="/WEB-INF/tlds/spagobiwa.tld" prefix="spagobiwa" %>

<%      
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("LoginModule"); 
	List lstMenu = (List)moduleResponse.getAttribute("LIST_MENU");	
%>


<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<html>
  <head>
    <title>SpagoBI Home</title>
    
    <script type="text/javascript" src="<%=contextName%>/js/spagobi.js"></script>
    <script type="text/javascript" src="<%=contextName%>/js/prototype/javascripts/prototype.js"></script>
    <script type="text/javascript" src="<%=contextName%>/js/prototype/javascripts/window.js"></script>
    <script type="text/javascript" src="<%=contextName%>/js/prototype/javascripts/effects.js"></script>
    <script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/menu.js")%>"></script> 
    <link href="<%=contextName%>/css/extjs/extSpagoBI.css" rel="stylesheet" type="text/css"/> 
   
    
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
  <!--  
  	<frameset cols="120,*">
		<frame src="menupage.htm" name="menu">
		<frameset rows="*,50">
			<frame src="welcomepage.htm" name="main">
			<frameset rows="*,50">
				<frame src="welcomepage.htm" name="main">
			</frameset>
		</frameset>
	</frameset>
  -->
	<div id="background" style="width:100%;height:100%;background-image:url(<%=contextName%>/img/wapp/background.jpg);background-repeat:no-repeat;background-position: top left;"> 
    	<div id="backgroundlogo" style="width:100%;height:100%;background-image:url(<%=contextName%>/img/wapp/backgroundlogo.jpg);background-repeat:no-repeat;background-position: bottom right;">   
        <div id="header" style="width:100%;height:70px;">
            <div id="logotitle" style="height:57px;background-image:url(<%=contextName%>/img/wapp/titlelogo.gif);background-repeat:no-repeat;background-position: top left;"> 
            </div>                        	
             <div id="menubar" style="float:left;width:100%;height:18px;border-top:1px solid gray;border-bottom:1px solid gray;background-image:url(<%=contextName%>/img/wapp/backgroundMenuBar.jpg);background-repeat:repeat-x;"> 
            </div>
        </div>
        <div id="content" style="top:80px;width:100%;height:80%;border-top:1px solid gray;border-bottom:1px solid gray;background-image:url(<%=contextName%>/img/wapp/backgroundMenuBar.jpg);background-repeat:repeat-x;">
	        <iframe id='iframeDoc'  name='iframeDoc'  src='' width='100%' height='85%' frameborder='0' >
			</iframe>
        </div>
        <div id="footer" style="width:100%;height:50px;">
        	<spagobiwa:FisheyeMenu />
        </div>
  </body>
  <script>
  	setContextName("<%=contextName%>");

  	Ext.onReady(function(){
  	
  	 		Ext.QuickTips.init();
	 		//MENU MANAGEMENT
			var tb = new Ext.Toolbar();
			//if(isMoz()) {
				tb.render('menubar');
			//}
		
	    <%  Integer currParentId = new Integer("-1");
	    	for (int i=0; i<lstMenu.size(); i++){
		    	Menu menuElem = (Menu)lstMenu.get(i);		    	
		    	if (menuElem.getParentId() != null && !menuElem.getParentId().equals("") && 
		    		menuElem.getParentId().compareTo(currParentId) != 0)
		    		currParentId = menuElem.getParentId();
		    	else currParentId = menuElem.getMenuId();
		    	if (menuElem.getLevel().intValue() == 1){
		  %>
			    var menu<%=i%> = new Ext.menu.Menu({
			    id: 'basicMenu_<%=i%>',
			    items: [
			    	<%if (menuElem.getHasChildren()){			    		
			    		  List lstChildrenLev2 = menuElem.getLstChildren();
			    		  for (int j=0; j<lstChildrenLev2.size(); j++){ //LEVEL 2
			    			Menu childElemLev2 = (Menu)lstChildrenLev2.get(j);				    			
			    			if (childElemLev2.getHasChildren()){%>
			    			     {text: '<%=childElemLev2.getDescr()%>',
			                	 menu: {        // <-- submenu 
			                     items: [
		    			    <%	 List lstChildrenLev3 = childElemLev2.getLstChildren();
					    		 for (int k=0; k<lstChildrenLev3.size(); k++){ //LEVEL 3
					    			 Menu childElemLev3 = (Menu)lstChildrenLev3.get(k);	
		    			     %>						    			    
				    			 <%if (childElemLev3.getHasChildren()){%>
				    			    {text: '<%=childElemLev3.getDescr()%>',
				                	menu: {        // <-- submenu 
				                    items: [
				    			    <%	 List lstChildrenLev4 = childElemLev3.getLstChildren();
							    		 for (int x=0; x<lstChildrenLev4.size(); x++){ //LEVEL 4
							    			 Menu childElemLev4 = (Menu)lstChildrenLev4.get(x);	
				    			    %>
						    			    new Ext.menu.CheckItem({
					                            text: '<%=childElemLev4.getDescr()%>',
					                            group: 'group_4', 
					                            href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID=<%=childElemLev4.getObjId().toString()%>')"                           
					                            
					                        })
					                        <%if(x < lstChildrenLev4.size()-1){%>
					                            ,
					                         <%}%>	                  	                       
				                   		
				            			<%}//for LEVEL 4
							    		 if(k < lstChildrenLev3.size()-1){%>
				                            ,
				                        <%}%>
				                        ]}}
				            		<%}
				    			    else{ %>
				                        new Ext.menu.CheckItem({
				                            text: '<%=childElemLev3.getDescr()%>',
				                            group: 'group_3', 
				                            href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID=<%=childElemLev3.getObjId().toString()%>')"                   
				                        })
				                        
				                   <%}%>	
				                      
				                    <%if(k < lstChildrenLev3.size()-1){%>
				                            ,
				                    <%}                             
			    		    } //for LEVEL 3
				    		/*if(j < lstChildrenLev2.size()-1){%>
	                            ,
	                        <%}*/%>
	                        ]}} 
		    			<%}
	    			    else{ %>
	                        new Ext.menu.CheckItem({
	                            text: '<%=childElemLev2.getDescr()%>',
	                            group: 'group_2', 
	                            href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID=<%=childElemLev2.getObjId().toString()%>')"                           
	                        })	                       
	                    <%}%>	
	                      
	                    <%if(j < lstChildrenLev2.size()-1){%>
	                            ,
	                    <%}
			    	  } //for LEVEL 2			    		  
			    	}else{%>
			    	 	new Ext.menu.CheckItem({
	                            text: '<%=menuElem.getDescr()%>',
	                            group: 'group_2', 
	                            href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID=<%=menuElem.getObjId().toString()%>')"                           
	                        })	     			    						     
					   <%}%>      	 
			         ]
				});				
				tb.add(
					new Ext.Toolbar.MenuButton({
						id:'<%=menuElem.getDescr()%>_<%=menuElem.getObjId()%>',
			            text: '<%=menuElem.getDescr()%>',
			            //tooltip: {text:'<%=menuElem.getDescr()%>', title:'<%=menuElem.getDescr()%>', autoHide:true},
			            cls: 'x-btn-text-icon bmenu',			           
			            handler: execDirectDoc <%if (menuElem.getHasChildren()){%>,		            	
			            menu: menu<%=i%>  	  
			            <%}%>
			        })					    				        				
				);
				
			<%}
		} //for%>	
		
		//adds exit menu		
		tb.add(
			
			new Ext.Toolbar.MenuButton({
	            text: 'Logout',
	            cls: 'x-btn-text-icon bmenu',
	           // tooltip: {text:'Exit', title:'Exit', autoHide:true},
	            handler: logout	  
	        })	
	    );
	});

 function logout(){
 	window.location = "<%=contextName%>/servlet/AdapterHTTP?PAGE=LogoutPage&<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>=TRUE";
 }
 
 
 function execDirectDoc(btn){
 	var url = "";
 	var idDoc = btn.id;
 	idDoc = idDoc.substring(idDoc.indexOf("_")+1);
 	
 	if (idDoc != null && idDoc != 'null'){
 		url =  "<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID="+idDoc;
		//	open_win_DocumentMenu("<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID="+idDoc);
		document.getElementById('iframeDoc').src = url;
	}
	return;
 }
 
 function execDirectUrl(url){ 	
	document.getElementById('iframeDoc').src = url;
	return;
 }


  </script>
</html>
