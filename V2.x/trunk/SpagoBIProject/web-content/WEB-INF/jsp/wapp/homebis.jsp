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

<%@page language="java" 
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
		import="it.eng.spago.base.*,
                 java.util.List,
                 java.util.ArrayList"%>
<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
<%@page import="it.eng.spagobi.commons.services.LoginModule"%>
<%@page import="it.eng.spagobi.wapp.util.MenuUtilities"%>
<%@page import="it.eng.spagobi.chiron.serializer.MenuListJSONSerializer"%>
<%@page import="it.eng.spagobi.wapp.services.DetailMenuModule"%>
<%@page import="it.eng.spagobi.wapp.bo.Menu"%>
<%@page import="org.json.JSONObject"%>

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>
 	<%@ include file="/WEB-INF/jsp/commons/importSbiJS.jspf"%>

<%-- START CHECK USER PROFILE EXISTENCE
	This Ajax call is usefull to find out if a user profile object is in session, i.e. if a user has logged in.
	In case the user profile object is not found, the browser is redirected to the login page.
	
	N.B.  TODO con il CAS da problemi perchè non valida il ticket ...
	--%>
<%@page import="it.eng.spagobi.chiron.serializer.MenuListJSONSerializer;"%>
<script type="text/javascript" src="/SpagoBI/js/src/ext/sbi/overrides/overrides.js"></script>
	
<%  
	//TODO usare curr_language curr_country locale e ricordarsi di passare il current theme

	String contextName = ChannelUtilities.getSpagoBIContextName(request);
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("LoginModule"); 

	if(moduleResponse==null) moduleResponse=aServiceResponse;
	
	List lstMenu = new ArrayList();
	
	if (moduleResponse.getAttribute(MenuUtilities.LIST_MENU) != null){
		lstMenu = (List)moduleResponse.getAttribute(MenuUtilities.LIST_MENU);
	}
	List filteredMenuList = MenuUtilities.filterListForUser(lstMenu, userProfile);
	MenuListJSONSerializer m = new MenuListJSONSerializer();
	JSONObject jsonMenuList = (JSONObject)m.serialize(filteredMenuList,locale);
	
	ConfigSingleton spagoconfig = ConfigSingleton.getInstance(); 
	// get mode of execution
	String viewTrack = (String)spagoconfig.getAttribute("SPAGOBI.MENU.pathTracked");   
	boolean viewTrackPath=false;	
	if(viewTrack!=null && viewTrack.equalsIgnoreCase("TRUE")){
	viewTrackPath=true;	
	}
%>

<!-- I want to execute if there is an homepage, only for user!-->
<%
    String firstUrlToCall = "";
	if (filteredMenuList.size() > 0) {
		//DAO method returns menu ordered by parentId, but null values are higher or lower on different database:
		//PostgreSQL - Nulls are considered HIGHER than non-nulls.
		//DB2 - Higher
		//MSSQL - Lower
		//MySQL - Lower
		//Oracle - Higher
		//Ingres - Higher
		// so we must look for the first menu item with null parentId
		Menu firtsItem = null;
		Iterator it = filteredMenuList.iterator();
		while (it.hasNext()) {
			Menu aMenuElement = (Menu) it.next();
			if (aMenuElement.getParentId() == null) {
				firtsItem = aMenuElement;
				break;
			}
		}
		String pathInit=MenuUtilities.getMenuPath(firtsItem);
		Integer objId=firtsItem.getObjId();
		
		if(objId!=null){
			firstUrlToCall = "'"+contextName+"/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID="+firtsItem.getMenuId()+"'";
		}else if(firtsItem.getStaticPage()!=null){
			firstUrlToCall = "'"+contextName+"/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID="+firtsItem.getMenuId()+"'";
		}else if(firtsItem.getFunctionality()!=null){
			firstUrlToCall = "'"+DetailMenuModule.findFunctionalityUrl(firtsItem, contextName)+"'";
		}else{
			firstUrlToCall = "''";
		}
	}
  	%>

<script type="text/javascript">
    
    var northFrame;
    var centerFrame;
    var southFrame;
    
   function execCrossNavigation(windowName, label, parameters) {
		centerFrame.getFrame().contentWindow.execCrossNavigation(windowName, label, parameters);
	}
     	
    function execDirectDoc(btn){
		var url = "";
		var idMenu = btn.id;
		var path=btn.path;
		var viewTrackPath='<%=viewTrackPath%>';
		if( viewTrackPath=='true' && path!=null)
		{document.getElementById('trackPath').innerHTML=path;}
		
		if (idMenu != null && idMenu != 'null'){
			url =   "'<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=MENU_BEFORE_EXEC&MENU_ID="+idMenu+"'" ;
			centerFrame.getFrame().setSrc(url);
		}
		return;
	 }

	function getFunctionality(btn){
		var url = btn.url;
		var path=btn.path;
		execDirectUrl(url, path);
		return;
	 }
	 
	function readHtmlFile(btn){
		var url = "";
	 	var idMenu = btn.id;
	 	var path = btn.path;
		var viewTrackPath='<%=viewTrackPath%>';
		if( viewTrackPath=='true' && path!=null)
		{document.getElementById('trackPath').innerHTML=path;}

	 	 if (idMenu != null && idMenu != 'null'){
			url =  "'<%=contextName%>/servlet/AdapterHTTP?ACTION_NAME=READ_HTML_FILE&MENU_ID="+idMenu+"'";
			centerFrame.getFrame().setSrc(url);
		}
		return;
	}
     	 
	function execDirectUrl(url, path){
		centerFrame.getFrame().setSrc(url);
		return;
	}
	
	function execUrl(url){
		document.location.href=url;
		return;
	}
	

	Ext.onReady(function(){
      Ext.QuickTips.init();              
      var menuList = <%=jsonMenuList%>;
      var firstUrl =  <%=firstUrlToCall%>;
      northFrame = new Sbi.home.Banner({bannerMenu: menuList});
      centerFrame = new  Ext.ux.ManagedIframePanel({
      					region: 'center'
      					,xtype: 'panel'
						,frameConfig:{autoCreate:{id: 'iframeDoc', name:'iframeDoc'},
	        					disableMessaging :false}
		                ,defaultSrc : firstUrl
		                ,border		: false 
		                ,height : 500
						,collapseMode: 'mini'
						,scrolling  : 'auto'	
						, disableMessaging :false
						,listeners: {'message:collapse2':  {
				        		fn: function(srcFrame, message) {
					        		
					        		if(northFrame.collapsed && southFrame.collapsed){
					        			
					        			northFrame.expand(false);
					        			southFrame.expand(false);
					        		}else{
					        			
					        			northFrame.collapse(false);
					        			southFrame.collapse(false);
					        		}
						        }
	        					, scope: this}}
						
	        			});

      southFrame = new Sbi.home.Footer({});
      var viewport = new Ext.Viewport({
	    layout: 'border',
	    items: [northFrame, centerFrame, southFrame]
	    });	  
	  viewport.render();
    });
    

    
    </script>
<%-- END CHECK USER PROFILE EXISTENCE --%>




