/**

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

**/
package it.eng.spagobi.presentation.treehtmlgenerators;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.DetailBIObjectModule;
import it.eng.spagobi.services.modules.ExecuteBIObjectModule;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Contains all methods needed to generate and modify a tree object for Administration.
 * There are methods to generate tree, configure, insert and modify elements.
 * 
 * @author sulis
 */
public class AdminTreeHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	int progrJSTree = 0;
	//ArrayList execRoleNames = new ArrayList();
	IEngUserProfile profile = null;
	PortletRequest portReq = null;
	
	/**
	 * Builds theJavaScript object to make the tree. All code is appended into a 
	 * String Buffer, which is then returned. 
	 * 
	 * @param dataTree The tree data Source Bean
	 * @param httpReq The http Servlet Request 
	 *  
	 */
	public StringBuffer makeTree(SourceBean dataTree, HttpServletRequest httpReq) {
		
		
		httpRequest = httpReq;
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		portReq = PortletUtilities.getPortletRequest();
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+"' type='text/css' />");
		makeConfigurationDtree(htmlStream);
		String nameTree = PortletUtilities.getMessage("tree.objectstree.name" ,"messages");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/contextMenu.js" )+"'></SCRIPT>");
		htmlStream.append("<div id='divmenuFunct' class='dtreemenu' onmouseout='hideMenu(event);' >");
		htmlStream.append("		menu");
		htmlStream.append("</div>");
		htmlStream.append("<table width='100%'>");
		htmlStream.append("	<tr height='1px'>");
		htmlStream.append("		<td width='10px'>&nbsp;</td>");
		htmlStream.append("		<td>&nbsp;</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("	<tr>");
		htmlStream.append("		<td>&nbsp;</td>");
		htmlStream.append("		<td>");
		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
	   	htmlStream.append("				var nameTree = 'treeCMS';\n");
	   	htmlStream.append("				treeCMS = new dTree('treeCMS');\n");
	   	htmlStream.append("	        	treeCMS.add(0,-1,'"+nameTree+"');\n");
        addItemForJSTree(htmlStream, dataTree, 0, true);
    	htmlStream.append("				document.write(treeCMS);\n");
    	makeJSFunctionForMenu(htmlStream);	
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		return htmlStream;
	}

	/**
	 * Adds an item to the data tree, using input needed information like
	 * the parent node id and controlling if the item to add is a root.
	 *  
	 * 
	 * @param htmlStream	The input String Buffer
	 * @param dataTree	The tree Source Bean object
	 * @param pidParent	The parent's node ID
	 * @param isRoot A boolean to control if the item is a root
	 */
	private void addItemForJSTree(StringBuffer htmlStream, SourceBean dataTree, int pidParent, boolean isRoot) {
		
		List childs = dataTree.getContainedSourceBeanAttributes();
		String nameLabel = (String)dataTree.getAttribute("name");
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String path = (String)dataTree.getAttribute("path");
		String codeType = (String)dataTree.getAttribute("codeType");

		int id = ++progrJSTree;
		
		if(isRoot) {
			htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', '', '', '', '', '', 'true');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', '', '', '', '"+imgFolder+"', '"+imgFolderOp+"', '', '');\n");
				//htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '"+imgFolder+"', '"+imgFolderOp+"', '', 'menu(event, \\'\\', \\'\\')');\n");
				//execRoleNames = new ArrayList();
				//String execRolesStr = (String)dataTree.getAttribute("execRoles");
				//StringTokenizer strTok = new StringTokenizer(execRolesStr, "---");
				//while(strTok.hasMoreTokens()) {
				//	execRoleNames.add(strTok.nextToken());
				//}
			} else {
				String icon = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticon.png");
				String stateObj = (String)dataTree.getAttribute("state");
				if(ObjectsAccessVerifier.canExec(stateObj, path, profile)) {
					htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'"+createExecuteObjectLink(path)+"\\', \\'"+createDetailObjectLink(path)+"\\', \\'"+createEraseObjectLink(path)+"\\')' );\n");
				} else {
					htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'\\', \\'"+createDetailObjectLink(path)+"\\', \\'"+createEraseObjectLink(path)+"\\')' );\n");
				}
			}
			
		}
		
		Iterator iter = childs.iterator();
		while(iter.hasNext()) {
			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
			SourceBean itemSB = (SourceBean)itemSBA.getValue();
			addItemForJSTree(htmlStream, itemSB, id, false);
		}
		
	}

	/**
	 * Creates the Dtree configuration, in oder to inser into jsp pages cookies,
	 * images, etc.
	 * 
	 * @param htmlStream	The input String Buffer
	 */
	private void makeConfigurationDtree(StringBuffer htmlStream) {
		
		htmlStream.append("<SCRIPT language=JavaScript>\n");
		htmlStream.append("		function dTree(objName) {\n");
		htmlStream.append("			this.config = {\n");
		htmlStream.append("				target			: null,\n");
		htmlStream.append("				folderLinks		: true,\n");
		htmlStream.append("				useSelection	: true,\n");
		htmlStream.append("				useCookies		: true,\n");
		htmlStream.append("				useLines		: true,\n");
		htmlStream.append("				useIcons		: true,\n");
		htmlStream.append("				useStatusText	: true,\n");
		htmlStream.append("				closeSameLevel	: false,\n");
		htmlStream.append("				inOrder			: false\n");
		htmlStream.append("			}\n");
		htmlStream.append("			this.icon = {\n");
		htmlStream.append("				root		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treebase.gif")+"',\n");
		htmlStream.append("				folder		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif")+"',\n");
		htmlStream.append("				folderOpen	: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif")+"',\n");
		htmlStream.append("				node		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treepage.gif")+"',\n");
		htmlStream.append("				empty		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treeempty.gif")+"',\n");
		htmlStream.append("				line		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treeline.gif")+"',\n");
		htmlStream.append("				join		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treejoin.gif")+"',\n");
		htmlStream.append("				joinBottom	: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treejoinbottom.gif")+"',\n");
		htmlStream.append("				plus		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treeplus.gif")+"',\n");
		htmlStream.append("				plusBottom	: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treeplusbottom.gif")+"',\n");
		htmlStream.append("				minus		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treeminus.gif")+"',\n");
		htmlStream.append("				minusBottom	: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treeminusbottom.gif")+"',\n");
		htmlStream.append("				nlPlus		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treenolines_plus.gif")+"',\n");
		htmlStream.append("				nlMinus		: '"+PortletUtilities.createPortletURLForResource(httpRequest, "/img/treenolines_minus.gif")+"'\n");
		htmlStream.append("			};\n");
		htmlStream.append("			this.obj = objName;\n");
		htmlStream.append("			this.aNodes = [];\n");
		htmlStream.append("			this.aIndent = [];\n");
		htmlStream.append("			this.root = new Node(-1);\n");
		htmlStream.append("			this.selectedNode = null;\n");
		htmlStream.append("			this.selectedFound = false;\n");
		htmlStream.append("			this.completed = false;\n");
		htmlStream.append("		};\n");
		htmlStream.append("</SCRIPT>\n");
		
	}
	
	/**
	 * Creates the menu to make execution, detail, erasing for a tree element.
	 * 
	 * @param htmlStream The input String Buffer
	 */
	private void makeJSFunctionForMenu(StringBuffer htmlStream) {
		htmlStream.append("		function menu(event, urlExecution, urlDetail, urlErase) {\n");
		htmlStream.append("			divM = document.getElementById('divmenuFunct');\n");
		htmlStream.append("			divM.innerHTML = '';\n");
		String capExec = PortletUtilities.getMessage("SBISet.devObjects.captionExecute", "messages");
		htmlStream.append("			if(urlExecution!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlExecution+'\">"+capExec+"/a></div>';\n");
		String capDetail = PortletUtilities.getMessage("SBISet.devObjects.captionDetail", "messages");
		htmlStream.append("			if(urlDetail!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlDetail+'\">"+capDetail+"</a></div>';\n");
		String capErase = PortletUtilities.getMessage("SBISet.devObjects.captionErase", "messages");
        htmlStream.append("         if(urlErase!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"javascript:actionConfirm(\\'"+capErase+"\\', \\''+urlErase+'\\');\">"+capErase+"</a></div>';\n");
        htmlStream.append("				showMenu(event, divM);\n");
        //htmlStream.append("			divM.style.left = '' + (event.clientX-5) + 'px';\n");
		//htmlStream.append("			divM.style.top = '' + (event.clientY + document.documentElement.scrollTop - 5)  + 'px' ;\n");
		//htmlStream.append("			divM.style.display = 'inline' ;\n");
		htmlStream.append("		}\n");

		
		htmlStream.append("		function linkEmpty() {\n");
		htmlStream.append("		}\n");
		
		/*
		htmlStream.append("		function hideMenu(event) {\n");
		htmlStream.append("			divM = document.getElementById('divmenuFunct');\n");
		htmlStream.append("			yup = parseInt(divM.style.top) - parseInt(document.documentElement.scrollTop);\n");
		htmlStream.append("			ydown = parseInt(divM.style.top) + parseInt(divM.offsetHeight) - parseInt(document.documentElement.scrollTop);\n");
		htmlStream.append("			xleft = parseInt(divM.style.left);\n");
		htmlStream.append("			xright = parseInt(divM.style.left) + parseInt(divM.offsetWidth);\n");
		htmlStream.append("			if( (event.clientY<=(yup+2)) || (event.clientY>=ydown) || (event.clientX<=(xleft+2)) || (event.clientX>=xright) ) { ;\n");
		htmlStream.append("				divM.style.display = 'none' ;\n");
		htmlStream.append("			}\n");
		htmlStream.append("		}\n");
		*/
        
		// js function for item action confirm
        String confirmCaption = PortletUtilities.getMessage("SBISet.devObjects.confirmCaption", "messages");
        htmlStream.append("     function actionConfirm(message, url){\n");
        htmlStream.append("         if (confirm('" + confirmCaption + " ' + message + '?')){\n");
        htmlStream.append("             location.href = url;\n");
        htmlStream.append("         }\n");
        htmlStream.append("     }\n");
        
	}
	
	/**
	 * Contains Portlet URL methods to make object tree detail.
	 * 
	 * @param path	The object tree path String
	 * @return	The Portlet URL string element for detail
	 */
	private String createDetailObjectLink(String path) {
		PortletURL detUrl = renderResponse.createActionURL();
		detUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		detUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_SELECT);
		detUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		detUrl.setParameter(ObjectsTreeConstants.PATH, path);
        return detUrl.toString();
	}
	/**
	 * Contains Portlet URL methods to make object tree execution.
	 * 
	 * @param path	The object tree path String
	 * @return	The Portlet URL string element for execution
	 */
	private String createExecuteObjectLink(String path) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.PATH, path);
		execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
		return execUrl.toString();
	}
	/**
	 * Contains Portlet URL methods to make object tree erasing.
	 * 
	 * @param path	The object tree path String
	 * @return	The Portlet URL string element for erasing
	 */
	private String createEraseObjectLink(String path) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_DEL);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
        return addUrl.toString();
	}
	
	
	/**
	 * Creates an open, ad so accessible, tree. The code is directly appended into a
	 * String buffer, without using Javascripts.
	 * 
	 * @param dataTree The data Tree Source Bean
	 * @param httpRequest The http servlet request
	 * @return The output string buffer
	 * 
	 */
	public StringBuffer makeAccessibleTree(SourceBean dataTree, HttpServletRequest httpRequest) {
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("<div id='divAccessibleTree'>\n");
        addItemForAccessibleTree(htmlStream, dataTree, 0, true);
		htmlStream.append("</div>\n");
		//htmlStream.append("<br/><br/>\n");
		makeJSFunctionForHideAccessibleTree(htmlStream);
		return htmlStream;
	}
	
	/**
	 * Adds an item to the accessible tree. 
	 * 
	 * @param htmlStream The input html Stream
	 * @param dataTree The data tree Source Bean
	 * @param spaceLeft The left space to leave when an item is added
	 * @param isRoot Boolean to know is the item is a root 	
	 */
	private void addItemForAccessibleTree(StringBuffer htmlStream, SourceBean dataTree, int spaceLeft, boolean isRoot) {
		List childs = dataTree.getContainedSourceBeanAttributes();		
		String name = (String)dataTree.getAttribute("name");
		String path = (String)dataTree.getAttribute("path");
		String codeType = (String)dataTree.getAttribute("codeType");
		if(isRoot) {
			htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
			htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderopen.gif" )+"' />");
			htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
			htmlStream.append("</div>");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
				htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderopen.gif" )+"' />");
	    		htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
	    		htmlStream.append("</div>\n");
			} else {
				htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
				htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/objecticon.gif" )+"' />");
	    		htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
	    		htmlStream.append("		<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>\n");
	    		htmlStream.append("		<span>\n");
	    		String stateObj = (String)dataTree.getAttribute("state");
	    		if(ObjectsAccessVerifier.canExec(stateObj, path, profile)) {
		    		createExecuteObjectLink(path, htmlStream);
	    		} 
	    		createDetailObjectLink(path, htmlStream);
	    		createEraseObjectLink(path, htmlStream);
	    		htmlStream.append("		</span>\n");
	    		htmlStream.append("</div>\n");
			}
		}
				
		Iterator iter = childs.iterator();
		while(iter.hasNext()) {
			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
			SourceBean itemSB = (SourceBean)itemSBA.getValue();
			addItemForAccessibleTree(htmlStream, itemSB, (spaceLeft + 40), false);
		}
	}
	
	/**
	 * When Java Script mode is on, this method hides the accessible tree. 
	 * 
	 * @param htmlStream The html stream input buffer
	 */
	private void makeJSFunctionForHideAccessibleTree(StringBuffer htmlStream) {
		htmlStream.append("<SCRIPT>\n");
		htmlStream.append("		divM = document.getElementById('divAccessibleTree');\n");
		htmlStream.append("		divM.style.display='none';\n");
		htmlStream.append("</SCRIPT>\n");
	}
	
	
	/**
	 * Contains Portlet URL methods to make object tree detail for the accessible tree. It is
	 * obtained by appending directly the html code into the html Stream buffer.
	 * 
	 * @param path	The object tree path String
	 * @param htmlStream The html stream input buffer
	 */
	private void createDetailObjectLink(String path, StringBuffer htmlStream) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_SELECT);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		htmlStream.append("<a title=\"Detail Object\" class=\"linkOperation\" href=\""+addUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif" )+"' /></a>\n");
		htmlStream.append("<span>&nbsp;&nbsp;&nbsp;</span>\n");
	}
	/**
	 * Contains Portlet URL methods to make object tree execution for the accessible tree. It is
	 * obtained by appending directly the html code into the html Stream buffer.
	 * 
	 * @param path	The object tree path String
	 * @param htmlStream The html stream input buffer
	 */
	private void createExecuteObjectLink(String path, StringBuffer htmlStream) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.PATH, path);
		execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
		htmlStream.append("<a title=\"Exec Object\" class=\"linkOperation\" href=\""+execUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/execObject.gif" )+"' /></a>\n");
		htmlStream.append("<span>&nbsp;&nbsp;&nbsp;</span>\n");
	}
	/**
	 * Contains Portlet URL methods to make object tree erasing for the accessible tree. It is
	 * obtained by appending directly the html code into the html Stream buffer.
	 * 
	 * @param path	The object tree path String
	 * @param htmlStream The html stream input buffer
	 */
	private void createEraseObjectLink(String path, StringBuffer htmlStream) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_DEL);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		htmlStream.append("<a title=\"Erase Object\" class=\"linkOperation\" href=\""+addUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif" )+"' /></a>\n");
		htmlStream.append("<span>&nbsp;&nbsp;&nbsp;</span>\n");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
