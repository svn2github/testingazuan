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
                 java.util.List,
                 java.util.ArrayList"
%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.commons.services.LoginModule"%>
<%@page import="it.eng.spagobi.wapp.bo.Menu"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.wapp.services.DetailMenuModule"%>
<%@page import="it.eng.spagobi.wapp.util.MenuAccessVerifier"%>
<%@page import="it.eng.spagobi.commons.utilities.GeneralUtilities"%>
<%@page import="it.eng.spago.util.JavaScript"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>

<%@ include file="/jsp/commons/portlet_base.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/spagobiwa.tld" prefix="spagobiwa" %>

<%      
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("LoginModule"); 
	List lstMenu = new ArrayList();
	if (moduleResponse.getAttribute(LoginModule.LIST_MENU) != null)
		lstMenu = (List)moduleResponse.getAttribute(LoginModule.LIST_MENU);

	
	String menuMode = (String)moduleResponse.getAttribute(LoginModule.MENU_MODE); 
	String menuExtra = (String)moduleResponse.getAttribute(LoginModule.MENU_EXTRA);
	boolean first=true;
%>

<%-- Javascript object useful for session expired management (see also sessionExpired.jsp) --%>
<script>
sessionExpiredSpagoBIJS = 'sessionExpiredSpagoBIJS';
</script>
<%-- End javascript object useful for session expired management (see also sessionExpired.jsp) --%>


<script type="text/javascript" src="<%=linkSbijs%>"></script>
	   <script type="text/javascript" src="<%=linkProto%>"></script>
		<script type="text/javascript" src="<%=linkProtoWin%>"></script>
		<script type="text/javascript" src="<%=linkProtoEff%>"></script>
		<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
		<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/>
   
  <script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/menu.js")%>"></script>
  <script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/wapp/menuTree.js")%>"></script>
    
    <link href="<%=contextName%>/css/extjs/ext-all-SpagoBI-web.css" rel="stylesheet" type="text/css"/>
    
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
      
      iframe {
		  background-color: transparent;
	  }
    </style> 

  </head>


  <body>

	<%
	String displayBannerAndFooterParam = (String) aServiceRequest.getAttribute("displayBannerAndFooter");
	boolean displayBannerAndFooter = !(displayBannerAndFooterParam != null && displayBannerAndFooterParam.equalsIgnoreCase("false"));
	if (displayBannerAndFooter) {
	%>
		<%@include file="/html/banner.html" %>
	<% } %>

	<%-- contains the menu --%>
	<div id="menubar" style="width:100%;background:#EEEFF3;"> 
	</div>
	
	<% if (menuMode.equalsIgnoreCase(LoginModule.LAYOUT_ALL_TOP)){ %>
	<div id="content" style="margin:2;">
		<iframe id='iframeDoc'  name='iframeDoc' src='' width='100%' height='74%' frameborder='0' Style='background-color: white' >
		</iframe>
	</div>
	<% } %>
	
	<%--if(menuMode.equalsIgnoreCase(LoginModule.LAYOUT_ALL_LEFT)) {%>
	  <div id="leftMenu" style='float:left;background-color: transparent'></div>
	  <div id="content" style="float:right;top:90px;left:300px;width:90%;height:90%;border-top:1px solid gray;border-bottom:1px solid gray;background-image:url(<%=contextName%>/img/wapp/backgroundMenuBar.jpg);background-repeat:repeat-x;">
        <iframe id='iframeDoc'  name='iframeDoc' src='' width='85%' height='90%' frameborder='0' Style='background-color: white'>
		</iframe>
       </div>
	<%}--%>
	
	<script type="text/javascript">
	if (isMoz()) {
		document.getElementById('iframeDoc').height='75%';
	} else {
		document.getElementById('iframeDoc').height='72%';
	}
	</script>
  
	<%
	if (displayBannerAndFooter) {
	%>
	<%@include file="/html/footer.html" %>
	<% } %>
	
  <script>
  	<%-- Ext overriding methods for mouseout and mouseexit from menu --%>
	Ext.override(Ext.menu.Menu, {
	  render : function(){
	    if(this.el){
	      return;
	    }
	    var el = this.el = this.createEl();
	    
	    if(!this.keyNav){
	      this.keyNav = new Ext.menu.MenuNav(this);
	    }
	    if(this.plain){
	      el.addClass("x-menu-plain");
	    }
	    if(this.cls){
	      el.addClass(this.cls);
	    }
	    // generic focus element
	    this.focusEl = el.createChild({
	      tag: "a", cls: "x-menu-focus", href: "#", onclick: "return false;", tabIndex:"-1"
	    });
	    var ul = el.createChild({tag: "ul", cls: "x-menu-list"});
	    ul.on("click", this.onClick, this);
	    ul.on("mouseover", this.onMouseOver, this);
	    ul.on("mouseout", this.onMouseOut, this);
	    if (!this.topmenu) {
	      this.addEvents("mouseenter", "mouseexit");
	      this.mouseout = null;
	    }
	    el.on("mouseover", function(e, t){
	      if(this.topmenu){
	        clearTimeout(this.topmenu.mouseout);
	        this.topmenu.mouseout=null;
	      }else if (this.mouseout == null) this.fireEvent("mouseenter", this, e, t);
	      else {
	        clearTimeout(this.mouseout);
	        this.mouseout = null;
	      }
	    }, this);
	    el.on("mouseout", function(e, t){
	      if (this.topmenu) {
	        this.topmenu.mouseout = (function(){
	          this.topmenu.mouseout = null;
	          this.topmenu.fireEvent("mouseexit", this.topmenu, e, t);
	        }).defer(100, this);
	      } else {
	        this.mouseout = (function(){
	          this.mouseout = null;
	          this.fireEvent("mouseexit", this, e, t);
	        }).defer(100, this);
	      }
	    }, this);
	    el.on("mouseup", function(e, t){
	      e.stopEvent();
	    })
	    this.items.each(function(item){
	      var li = document.createElement("li");
	      li.className = "x-menu-list-item";
	      ul.dom.appendChild(li);
	      if(item.menu)item.menu.topmenu=this.topmenu||this;
	      item.render(li, this);
	    }, this);
	    this.ul = ul;
	    this.autoWidth();
	  }
	});
  	
  	
  	
	<%//loading menu on TOP (max 4 levels)
	if (menuMode.equalsIgnoreCase(LoginModule.LAYOUT_ALL_TOP)) {
	%>
	Ext.onReady(function(){  	
		Ext.QuickTips.init();
		//MENU MANAGEMENT
		var tb = new Ext.Toolbar();			
		tb.render('menubar');
			
		<%
		//if the user is a final user, the menu is created and putted into the response with other informations like the type of layout,
		//otherwise don't, administrators, developers, testers, behavioral model administrators have they own pre-configured menu
		if (!userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)  // for administrators
				&& !userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)  // for developers
				&& !userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_TEST)  // for testers
				&& !userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_MANAGEMENT)) {  // for behavioral model administrators
		Integer currParentId = new Integer("-1");
		for (int i=0; i<lstMenu.size(); i++){
			Menu menuElem = (Menu)lstMenu.get(i);
			if (menuElem.getParentId() != null && menuElem.getParentId().intValue() > 0 && 
					menuElem.getParentId().compareTo(currParentId) != 0)
				currParentId = menuElem.getParentId();
			else currParentId = menuElem.getMenuId();
			if (menuElem.getLevel().intValue() == 1){
				%>
				var menu<%=i%> = new Ext.menu.Menu({
				id: 'basicMenu_<%=i%>',
				items: [
				<%
				if (menuElem.getHasChildren()){			    		
					List lstChildrenLev2 = menuElem.getLstChildren();
					for (int j=0; j<lstChildrenLev2.size(); j++){ //LEVEL 2
						Menu childElemLev2 = (Menu)lstChildrenLev2.get(j);	
						boolean canView2=MenuAccessVerifier.canView(childElemLev2,userProfile);
						if(canView2){
							if (childElemLev2.getHasChildren()){
							%>
								{id: '<%new Double(Math.random()).toString();%>',
				    			     text: "<%=JavaScript.escapeText(msgBuilder.getMessage(childElemLev2.getName(), "menu", request))%>",
				    				 		<% if(childElemLev2.getObjId()!=null){%>
					                       		href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=childElemLev2.getMenuId()%>')"                   
					                        <%} else if(childElemLev2.getStaticPage()!=null) {%>
						                         href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=childElemLev2.getMenuId()%>')"
						                    <%} else if(childElemLev2.getFunctionality()!=null) {%>
						                         href: "javascript:execDirectUrl('<%=DetailMenuModule.findFunctionalityUrl(childElemLev2, contextName)%>')"
						                    <%} else {%>
						                         href: ''     
						                    <%}%>,		// comma
											<%String icon2=DetailMenuModule.assignImage(childElemLev2);
						                         if(childElemLev2.isViewIcons() && !icon2.equalsIgnoreCase("")){%>
						                         	icon: '<%=contextName%><%=icon2%>',
						                         <%}%>					    			     
				                	 menu: {        // <-- submenu 
				                     items: [
			    			    <%	 List lstChildrenLev3 = childElemLev2.getLstChildren();
						    		 for (int k=0; k<lstChildrenLev3.size(); k++){ //LEVEL 3
						    			 Menu childElemLev3 = (Menu)lstChildrenLev3.get(k);	
									    boolean canView3=MenuAccessVerifier.canView(childElemLev3,userProfile);
									    if(canView3){
			    			     %>						    			    
					    			 <%if (childElemLev3.getHasChildren()){%>
					    			    {id: '<%new Double(Math.random()).toString();%>',
					    			    text: "<%=JavaScript.escapeText(msgBuilder.getMessage(childElemLev3.getName(), "menu", request))%>",
				    				 		<% if(childElemLev3.getObjId()!=null){%>
					                       		href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=childElemLev3.getMenuId()%>')"                   
					                        <%} else if(childElemLev3.getStaticPage()!=null) {%>
						                         href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=childElemLev3.getMenuId()%>')"
						                    <%} else if(childElemLev3.getFunctionality()!=null) {%>
						                         href: "javascript:execDirectUrl('<%=DetailMenuModule.findFunctionalityUrl(childElemLev3, contextName)%>')"
						                    <%} else {%>
						                         href: ''     
						                    <%}%>,		// comma
											<%String icon3=DetailMenuModule.assignImage(childElemLev3);
						                         if(childElemLev3.isViewIcons() && !icon3.equalsIgnoreCase("")){%>
						                         	icon: '<%=contextName%><%=icon3%>',
						                         <%}%>							                            
					                	menu: {        // <-- submenu 
					                    items: [
					    			    <%	 List lstChildrenLev4 = childElemLev3.getLstChildren();
								    		 for (int x=0; x<lstChildrenLev4.size(); x++){ //LEVEL 4
								    			 Menu childElemLev4 = (Menu)lstChildrenLev4.get(x);	
												    boolean canView4=MenuAccessVerifier.canView(childElemLev4,userProfile);
												    if(canView4){
					    			    %>
							    			    new Ext.menu.Item({
						                            id: '<%new Double(Math.random()).toString();%>',
						                            text: "<%=JavaScript.escapeText(msgBuilder.getMessage(childElemLev4.getName(), "menu", request))%>",
						                            group: 'group_4', 
						                            <%String icon=DetailMenuModule.assignImage(childElemLev4);
						                            if( childElemLev4.isViewIcons() && !icon.equalsIgnoreCase("")){%>
						                            icon: '<%=contextName%><%=icon%>',
						                            <%}%>
						    				 		<% if(childElemLev4.getObjId()!=null){%>
							                       		href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=childElemLev4.getMenuId()%>')"                   
							                        <%} else if(childElemLev4.getStaticPage()!=null) {%>
								                         href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=childElemLev4.getMenuId()%>')"
								                    <%} else if(childElemLev4.getFunctionality()!=null) {%>
								                         href: "javascript:execDirectUrl('<%=DetailMenuModule.findFunctionalityUrl(childElemLev4, contextName)%>')"
								                    <%} else {%>
								                         href: ''     
								                    <%}%>,		// comma
						                        })
												<%if(x < lstChildrenLev4.size()-1){%>
												, // comma level 4
												<%}%>	                  	                       				                   		
					            			<%
												    } //can View 4
												}//for LEVEL 4
								    		%>
					                        ]}}
					            		<%}
					    			    else{ %>
					                        new Ext.menu.Item({
					                            id: '<%new Double(Math.random()).toString();%>',
					                            text: "<%=JavaScript.escapeText(msgBuilder.getMessage(childElemLev3.getName(), "menu", request))%>",
					                            group: 'group_3',
												<%String icon=DetailMenuModule.assignImage(childElemLev3);
						                          if(childElemLev3.isViewIcons() && !icon.equalsIgnoreCase("")){%>
						                          icon: '<%=contextName%><%=icon%>',
						                          <%}%>					                             
					                            <% if(childElemLev3.getObjId()!=null){%>
						                       		href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=childElemLev3.getMenuId()%>')"                   
						                        <%} else if(childElemLev3.getStaticPage()!=null) {%>
							                         href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=childElemLev3.getMenuId()%>')"
							                    <%} else if(childElemLev3.getFunctionality()!=null) {%>
							                         href: "javascript:execDirectUrl('<%=DetailMenuModule.findFunctionalityUrl(childElemLev3, contextName)%>')"
							                    <%} else {%>
							                         href: ''     
							                    <%}%>
					                        })
					                        
					                   <%}%>					                      
					                    <%if(k < lstChildrenLev3.size()-1){%>
					                            ,
					                    <%}                             
									    } //can View 3
									    } //for LEVEL 3
					    		%>
		                        ]}} 
			    			<%}
		    			    else{ %>
		                        new Ext.menu.Item({
		                            id: '<%new Double(Math.random()).toString();%>',
		                            text: "<%=JavaScript.escapeText(msgBuilder.getMessage(childElemLev2.getName(), "menu", request))%>",
		                            group: 'group_2',
									<%String icon=DetailMenuModule.assignImage(childElemLev2);
									   if(childElemLev2.isViewIcons() && !icon.equalsIgnoreCase("")){%>
										icon: '<%=contextName%><%=icon%>',
											<%}%>		                             
		                            <% if(childElemLev2.getObjId()!=null){%>
			                       		href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=childElemLev2.getMenuId()%>')"                   
			                        <%} else if(childElemLev2.getStaticPage()!=null) {%>
				                         href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=childElemLev2.getMenuId()%>')"
				                    <%} else if(childElemLev2.getFunctionality()!=null) {%>
				                         href: "javascript:execDirectUrl('<%=DetailMenuModule.findFunctionalityUrl(childElemLev2, contextName)%>')"
				                    <%} else {%>
				                         href: ''     
				                    <%}%>
		                        })	                       
		                    <%}%>		                      
		                    <%if(j < lstChildrenLev2.size()-1){%>
		                            ,
		                    <%}
						    	} //can view level 2
						    	} //for LEVEL 2			    		  
				    	}else{%>
				    	 	new Ext.menu.Item({
		                            id: '<%new Double(Math.random()).toString();%>',
		                            text: "<%=JavaScript.escapeText(msgBuilder.getMessage(menuElem.getName(), "menu", request))%>",
		                            group: 'group_1',		                             
	                            <% if(menuElem.getObjId()!=null){%>
		                       		href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=menuElem.getMenuId()%>')"                   
		                        <%} else if(menuElem.getStaticPage()!=null) {%>
			                         href: "javascript:execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=menuElem.getMenuId()%>')"
			                    <%} else if(menuElem.getFunctionality()!=null) {%>
			                         href: "javascript:execDirectUrl('<%=DetailMenuModule.findFunctionalityUrl(menuElem, contextName)%>')"
			                    <%} else {%>
			                         href: ''     
			                    <%}%>
		                            
		                        })	     			    						     
						   <%}%>      	 
				         ]
					});		
					menu<%=i%>.addListener('mouseexit', function() {menu<%=i%>.hide();});
				<%
				if(first==true){
				first=false;
				}
				else{
				%>
					tb.addSeparator();
				<%
				}
				
						%>
					
					
					<%if(menuElem.getHasChildren()){%>
						
					tb.add(
						new Ext.Toolbar.MenuButton({
							id:'<%=menuElem.getMenuId()%>',
				            text: "<%=JavaScript.escapeText(msgBuilder.getMessage(menuElem.getName(), "menu", request))%>",
							<%String icon=DetailMenuModule.assignImage(menuElem);
							if(menuElem.isViewIcons() && !icon.equalsIgnoreCase("")){%>
								icon: '<%=contextName%><%=icon%>',
							<%}%>					            
				            <% if(menuElem.getObjId()!=null) { %>
				            	handler: execDirectDoc,
							<% } else if(menuElem.getStaticPage()!=null) { %>
				            	handler: readHtmlFile,
							<% } else if(menuElem.getFunctionality()!=null) { %>
								url: "<%=DetailMenuModule.findFunctionalityUrl(menuElem, contextName)%>",
				            	handler: getFunctionality,
							<% } %>
							<%if (menuElem.getHasChildren()) { %>
				            	menu: menu<%=i%>,
				            <% } %>
				            cls: 'x-btn-menubutton x-btn-text-icon bmenu '
				        })					    				        				
					);				
				<%}
					else{%>
			    		tb.add(
						new Ext.Toolbar.Button({
							id:'<%=menuElem.getMenuId()%>',
				            text: "<%=JavaScript.escapeText(msgBuilder.getMessage(menuElem.getName(), "menu", request))%>",
							<%String icon=DetailMenuModule.assignImage(menuElem);
							if(menuElem.isViewIcons() && !icon.equalsIgnoreCase("")){%>
								icon: '<%=contextName%><%=icon%>',
							<%}%>					            
				            <% if(menuElem.getObjId()!=null) { %>
				            	handler: execDirectDoc,
							<% } else if(menuElem.getStaticPage()!=null) { %>
				            	handler: readHtmlFile,
							<% } else if(menuElem.getFunctionality()!=null) { %>
								url: "<%=DetailMenuModule.findFunctionalityUrl(menuElem, contextName)%>",
				            	handler: getFunctionality,
							<% } %>
							<%if (menuElem.getHasChildren()) { %>
				            	menu: menu<%=i%>, 	  
				            <% } %>
				            cls: 'x-btn-menubutton x-btn-text-icon bmenu'
				        })					    				        				
					);	
			    	
						
		<%				
					} // else has no children
			    	
			    	}
			    	} //for
			%>
		
		<% } else { %>
		<spagobiwa:userMenu/>
		<% } %>
		
		tb.addFill();
			
		tb.add(
			new Ext.Toolbar.TextItem({
				text: '<spagobi:message key="menu.welcome" />: <b><%= ((UserProfile)userProfile).getUserName().toString() %></b>&nbsp;&nbsp;&nbsp;'
			})	
		);
		    
		//adds exit menu
		tb.addSeparator();

		tb.add(
			new Ext.Toolbar.Button({
	            id: '<%new Double(Math.random()).toString();%>',
	            text: '<spagobi:message key="menu.logout" />',
	            icon: '<%=contextName%>/img/wapp/exit16.png',
	            cls: 'x-btn-logout x-btn-text-icon bmenu',
	            handler: logout	  
	        })	
	    );
		
	});
	
	
	function execDirectDoc(btn){
		var url = "";
		var idMenu = btn.id;
		if (idMenu != null && idMenu != 'null'){
			url =  "<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID="+idMenu;
			document.getElementById('iframeDoc').src = url;
		}
		return;
	 }

	function getFunctionality(btn){
		var url = btn.url;
		execDirectUrl(url);
		return;
	 }
	 
	function readHtmlFile(btn){
		var url = "";
	 	var idMenu = btn.id;
	 	 if (idMenu != null && idMenu != 'null'){
			url =  "<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID="+idMenu;
			document.getElementById('iframeDoc').src = url;
		}
		return;
	}
	 	 
	function execDirectUrl(url){
		document.getElementById('iframeDoc').src = url;
		return;
	}
	
	function logout() {
		window.location = "<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=LOGOUT_ACTION&<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>=TRUE";
	}
	
	<% } %>
	
<%--} else if (menuMode.equalsIgnoreCase(LoginModule.LAYOUT_ALL_LEFT)){ %>

    Ext.onReady(function(){
	    //Logout
		var tb = new Ext.Toolbar();			
		tb.render('menubar');
		tb.add(	
			new Ext.Toolbar.MenuButton({
	            id: '<%new Double(Math.random()).toString();%>',
	            text: 'Logout',
	            icon: '<%=contextName%>/img/wapp/exit16.png',
	            cls: 'x-btn-text-icon bmenu',
	           // tooltip: {text:'Exit', title:'Exit', autoHide:true},
	            handler: logout	  
	        })	
	    ); 
	   var treeMenu = getMenuTreePanel();
	  
       var viewport = new Ext.Viewport({
            layout:'border',
            items:[{
                region:'west',
                id:'west-panel',
                baseCls:'left-menu-item',
                collapseMode:'mini',
                animCollapse:false,
                split:true,
                useSplitTips:true,
                width: 200,
                minSize: 100,
                maxSize: 500,
                collapsible: true,
                margins:'90 0 0 0',
                layout:'accordion',
                border:false,
                layoutConfig:{
                    animate:true,
                    fill:false,
                    hideCollapseTool:true
                },
                items:[treeMenu]                              
            },{
                region:'center',
                id:'center-panel',
                margins:'0 0 0 0',
                x:300,
                //layout:'column',
                autoScroll:true,
                //renderTo: 'content'                
                items:[{
                    columnWidth:1,
                    baseCls:'x-plain',
                    bodyStyle:'padding:5px 5px 5px 5px',
                    items:[{                    
                        html: '<iframe id="iframeDoc2"  name="iframeDoc2"  src="" width="50%" height="85%" frameborder="1"> </iframe>'
                    }]  
                 }]    
         	}]
        });  
     
     
		
});  

var getMenuTreePanel = function() {
    // root node
    var rootNode=new Ext.tree.TreeNode({text:'Choose a Dcoument...', icon:'<%=contextName%>/img/rememberMe22.png',expanded:true, id:'0'});
    var  nodeLev1 = new Ext.tree.TreeNode({icon:'<%=contextName%>/img/attach.png',expanded:false});
   	var  nodeLev2 = new Ext.tree.TreeNode({icon:'<%=contextName%>/img/attach.png',expanded:false});
   	var  nodeLev3 = new Ext.tree.TreeNode({icon:'<%=contextName%>/img/attach.png',expanded:false});
   	var  nodeLev4 = new Ext.tree.TreeNode({icon:'<%=contextName%>/img/attach.png',expanded:false});
 
   	<%for (int i=0; i< lstMenu.size(); i++){
   		Menu menuLev1 = (Menu)lstMenu.get(i);
   		if (menuLev1.getLevel().intValue() == 1){   			 
   	       if (menuLev1.getHasChildren()){ //first node
   	 %>   	   
   	    	nodeLev1 = new Ext.tree.TreeNode({text:'<%=menuLev1.getName()%>', icon:'<%=contextName%>/img/attach.png', id:'-<%=i%>'}); 
   <%		List lstChildrenLev2 = menuLev1.getLstChildren();
   				for (int j=0; j < lstChildrenLev2.size(); j++){
   					Menu menuLev2 = (Menu)lstChildrenLev2.get(j);   
   				    if (menuLev2.getHasChildren()){ //seconde node
    %>
    		nodeLev2 = new Ext.tree.TreeNode({text:'<%=menuLev2.getName()%>', icon:'<%=contextName%>/img/attach.png', id:'-<%=i%><%=j%>'});   
   	<%	   				List lstChildrenLev3 = menuLev2.getLstChildren();	 
   		   				for (int k=0; k < lstChildrenLev3.size(); k++){
   		   					Menu menuLev3 = (Menu)lstChildrenLev3.get(k);     	
   		   					if (menuLev3.getHasChildren()){ //third node	
   	%>
   			nodeLev3 = new Ext.tree.TreeNode({text:'<%=menuLev3.getName()%>', icon:'<%=contextName%>/img/attach.png', id:'-<%=i%><%=j%><%=k%>'});  
	<%   	   		   				List lstChildrenLev4 = menuLev3.getLstChildren();	
			   	   		   		for (int x=0; x < lstChildrenLev4.size(); x++){
		   		   					Menu menuLev4 = (Menu)lstChildrenLev4.get(x);  //fourth node (lief)	
	%>
			nodeLev4 = new Ext.tree.TreeNode({text:'<%=menuLev4.getName()%>', icon:'<%=contextName%>/img/treepage.gif', id:'<%=menuLev4.getObjId()%>'});
			nodeLev3.appendChild(nodeLev4);
	<%	   		   								   	   		   			
		   	   		   			}//for x
    %>
			nodeLev2.appendChild(nodeLev3); 
	<%   	   		   			}else{
	%>
			nodeLev2.appendChild([new Ext.tree.TreeNode({text:'<%=menuLev3.getName()%>', icon:'<%=contextName%>/img/treepage.gif', id:'<%=menuLev3.getObjId()%>'})]);  //(lief)
	<%   	   		   			}
   		   				}//for k   		   				
    %>
   		   	nodeLev1.appendChild(nodeLev2); 
    <%   			  }else{
    %>
	   		nodeLev1.appendChild([new Ext.tree.TreeNode({text:'<%=menuLev2.getName()%>', icon:'<%=contextName%>/img/treepage.gif', id:'<%=menuLev2.getObjId()%>'})]); //(lief)	
    <%				  }
   				} // for j 
    	    }else{
   %>
   			nodeLev1 = new Ext.tree.TreeNode({text:'<%=menuLev1.getName()%>', icon:'<%=contextName%>/img/treepage.gif', id:'<%=menuLev1.getObjId()%>'}); //lief
   <%		}
   %>
   			rootNode.appendChild(nodeLev1); 
   <%	 } //if (menuLev1.getLevel().intValue() == 1)   		
   	  }//for i
   	%>
   	
   	
    var menuTree=new Ext.tree.TreePanel({
      root:rootNode,
      //enableDD:true,
      expandable:true,
      collapsible:true,
      autoHeight:true ,
      bodyBorder:false ,
      width:300,
      leaf:false,
      lines:true,
      animate:true
    });
    
    menuTree.addListener('click', this.selectNode);
  
    return menuTree;
};

var selectNode = function(node, e) {
	if (node.id > 0){
		document.getElementById('iframeDoc').src = "<%=contextName%>/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&MESSAGEDET=EXEC_PHASE_CREATE_PAGE&OBJECT_ID="+node.id;		
	}
	return;
};  

<%}--%>
    
  </script>
  
  
	<!-- I want to execute if there is an homepage, only for user!-->
	<%
	if (lstMenu.size() > 0) {
		Menu menuElem = (Menu) lstMenu.get(0);
		Integer objId=menuElem.getObjId();
		if (objId!=null) {
			%> 					
			<script type="text/javascript">
			execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID=<%=menuElem.getMenuId()%>'); 
			</script>  					
			<%
		} else if(menuElem.getStaticPage()!=null) {
			%> 					
			<script type="text/javascript">
			execDirectUrl('<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID=<%=menuElem.getMenuId()%>'); 
			</script>  					
			<%
		} else if(menuElem.getFunctionality()!=null && !menuElem.getFunctionality().trim().equals("")) {
			String url = DetailMenuModule.findFunctionalityUrl(menuElem, contextName);
			%> 					
			<script type="text/javascript">
			execDirectUrl('<%=url%>');
			</script>  					
			<%
		}
	}
  	%>
  
<%@ include file="/jsp/commons/footer.jsp"%>