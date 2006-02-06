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
import it.eng.spagobi.services.modules.ListBIObjectParametersModule;
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
 * Contains all methods needed to generate and modify a tree object for Developing.
 * There are methods to generate tree, configure, insert and modify elements.
 * 
 * @author sulis
 */
public class DevTreeHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	int progrJSTree = 0;
	IEngUserProfile profile = null;
	PortletRequest portReq = null;
	
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.
	 * AdminTreeHtmlGenerator#makeTree(it.eng.spago.base.SourceBean,javax.servlet.http.HttpServletRequest)
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
	   	htmlStream.append("				var nameTree = 'treeDevObjects';\n");
	   	htmlStream.append("				treeDevObjects = new dTree('treeDevObjects');\n");
	   	htmlStream.append("	        	treeDevObjects.add(0,-1,'"+nameTree+"');\n");
        addItemForJSTree(htmlStream, dataTree, 0, true);
    	htmlStream.append("				document.write(treeDevObjects);\n");
    	makeJSFunctionForMenu(htmlStream);	
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		return htmlStream;
	}

	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#addItemForJSTree(java.lang.StringBuffer, it.eng.spago.base.SourceBean, int, boolean)
	 */
	private void addItemForJSTree(StringBuffer htmlStream, SourceBean dataTree, int pidParent, boolean isRoot) {
		
		List childs = dataTree.getContainedSourceBeanAttributes();
		String nameLabel = (String)dataTree.getAttribute("name");
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String path = (String)dataTree.getAttribute("path");
		String codeType = (String)dataTree.getAttribute("codeType");

		int id = ++progrJSTree;
		
		if(isRoot) {
			htmlStream.append("	treeDevObjects.add("+id+", "+pidParent+",'"+name+"', '', '', '', '', '', 'true');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	treeDevObjects.add("+id+", "+pidParent+",'"+name+"', '', '', '', '"+imgFolder+"', '"+imgFolderOp+"', '', '');\n");
			} else {
				String icon = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticon.png");
				String stateObj = (String)dataTree.getAttribute("state");
				if(ObjectsAccessVerifier.canDev(stateObj, path, profile)) {
					Integer idObj = (Integer)dataTree.getAttribute("id");
					//htmlStream.append("	treeDevObjects.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'"+createExecuteObjectLink(path)+"\\', \\'"+createDetailObjectLink(path)+"\\', \\'"+createListParametersLink(path, idObj)+"\\', \\'"+createEraseObjectLink(path)+"\\')' );\n");
					htmlStream.append("	treeDevObjects.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'"+createExecuteObjectLink(path)+"\\', \\'"+createDetailObjectLink(path)+"\\', \\'\\', \\'"+createEraseObjectLink(path)+"\\')' );\n");
				} else if(ObjectsAccessVerifier.canExec(stateObj, path, profile)) {
					htmlStream.append("	treeDevObjects.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'"+createExecuteObjectLink(path)+"\\', \\'\\', \\'\\', \\'\\')' );\n");
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
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#makeConfigurationDtree(java.lang.StringBuffer)
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
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#makeJSFunctionForMenu(java.lang.StringBuffer)
	 */
	private void makeJSFunctionForMenu(StringBuffer htmlStream) {
		htmlStream.append("		function menu(event, urlExecution, urlDetail, urlList, urlErase) {\n");
		htmlStream.append("			divM = document.getElementById('divmenuFunct');\n");
		
		htmlStream.append("			divM.innerHTML = '';\n");
		String capExec = PortletUtilities.getMessage("SBISet.devObjects.treeCaptionExecute", "messages");
		htmlStream.append("			divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlExecution+'\">"+capExec+"</a></div>';\n");
		String capDetail = PortletUtilities.getMessage("SBISet.devObjects.treeCaptionDetail", "messages");
		htmlStream.append("			if(urlDetail!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlDetail+'\">"+capDetail+"</a></div>';\n");
		String capList = PortletUtilities.getMessage("SBISet.devObjects.treeCaptionListPar", "messages");
        htmlStream.append("         if(urlList!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlList+'\">"+capList+"</a></div>';\n");
		String capErase = PortletUtilities.getMessage("SBISet.devObjects.treeCaptionErase", "messages");
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
        String confirmCaption = PortletUtilities.getMessage("SBISet.devObjects.treeConfirmCaption", "messages");
        htmlStream.append("     function actionConfirm(message, url){\n");
        htmlStream.append("         if (confirm('" + confirmCaption + " ' + message + '?')){\n");
        htmlStream.append("             location.href = url;\n");
        htmlStream.append("         }\n");
        htmlStream.append("     }\n");
        
	}

	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#createExecuteObjectLink(java.lang.String)
	 */
	private String createExecuteObjectLink(String path) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.PATH, path);
		execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
		execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
		return execUrl.toString();
	}
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#createDetailObjectLink(java.lang.String)
	 */
	private String createDetailObjectLink(String path) {
		PortletURL detUrl = renderResponse.createActionURL();
		detUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		detUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_SELECT);
		detUrl.setParameter(ObjectsTreeConstants.PATH, path);
		detUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
		return detUrl.toString();
	}
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#createEraseObjectLink(java.lang.String)
	 */
	private String createEraseObjectLink(String path) {
		PortletURL delUrl = renderResponse.createActionURL();
		delUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		delUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_DEL);
		delUrl.setParameter(ObjectsTreeConstants.PATH, path);
		delUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
		return delUrl.toString();
	}
	
	private String createListParametersLink(String path, Integer id) {
		PortletURL listUrl = renderResponse.createActionURL();
		listUrl.setParameter(ObjectsTreeConstants.PAGE, ListBIObjectParametersModule.MODULE_PAGE);
		listUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, id.toString());
		listUrl.setParameter(ObjectsTreeConstants.PATH, path);
		return listUrl.toString();
	}
	
	/**
	 * Creates an open, ad so accessible, tree. The code is directly appended into a
	 * String buffer, without using Javascripts.
	 * 
	 * @param dataTree The data Tree Source Bean
	 * @param httpServletRequest The http servlet request
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
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#addItemForAccessibleTree(java.lang.StringBuffer, it.eng.spago.base.SourceBean, int, boolean)
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
	    		if(ObjectsAccessVerifier.canDev(stateObj, path, profile)) {
	    			createExecuteObjectLink(path, htmlStream);
					createDetailObjectLink(path, htmlStream);
					createEraseObjectLink(path, htmlStream);
				} else if (ObjectsAccessVerifier.canExec(stateObj, path, profile)) {
					createExecuteObjectLink(path, htmlStream);
				}
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
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#makeJSFunctionForHideAccessibleTree(java.lang.StringBuffer)
	 */
	private void makeJSFunctionForHideAccessibleTree(StringBuffer htmlStream) {
		htmlStream.append("<SCRIPT>\n");
		htmlStream.append("		divM = document.getElementById('divAccessibleTree');\n");
		htmlStream.append("		divM.style.display='none';\n");
		htmlStream.append("</SCRIPT>\n");
	}
	
	
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#createExecuteObjectLink(java.lang.String, java.lang.StringBuffer)
	 */
	private void createExecuteObjectLink(String path, StringBuffer htmlStream) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.PATH, path);
		execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
		execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
		htmlStream.append("<a title=\"Run Object\" class=\"linkOperation\" href=\""+execUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/execObject.gif" )+"' /></a>\n");
		htmlStream.append("<span>&nbsp;&nbsp;&nbsp;</span>\n");
	}
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#createDetailObjectLink(java.lang.String, java.lang.StringBuffer)
	 */
	private void createDetailObjectLink(String path, StringBuffer htmlStream) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_SELECT);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
		htmlStream.append("<a title=\"Detail Object\" class=\"linkOperation\" href=\""+addUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/detail.gif" )+"' /></a>\n");
		htmlStream.append("<span>&nbsp;&nbsp;&nbsp;</span>\n");
	}
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#createEraseObjectLink(java.lang.String, java.lang.StringBuffer)
	 */
	private void createEraseObjectLink(String path, StringBuffer htmlStream) {
		PortletURL addUrl = renderResponse.createActionURL();
		addUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		addUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_DEL);
		addUrl.setParameter(ObjectsTreeConstants.PATH, path);
		addUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.DEV_ACTOR);
		htmlStream.append("<a title=\"Erase Object\" class=\"linkOperation\" href=\""+addUrl.toString()+"\">\n");
        htmlStream.append("<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/erase.gif" )+"' /></a>\n");
		htmlStream.append("<span>&nbsp;&nbsp;&nbsp;</span>\n");
	}
	
	
	
	
	
}
