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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
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
 */
public class AdminTreeHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	int progrJSTree = 0;
	IEngUserProfile profile = null;
	PortletRequest portReq = null;
	protected int dTreeRootId = -100;
	protected int dTreeObjects = -1000;
	


	/**
	 * Creates the Dtree configuration, in oder to inser into jsp pages cookies,
	 * images, etc.
	 * 
	 * @param htmlStream	The input String Buffer
	 */
	protected void makeConfigurationDtree(StringBuffer htmlStream) {
		
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
		htmlStream.append("			if(urlExecution!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlExecution+'\">"+capExec+"</a></div>';\n");
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
	
	
	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		
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
	   	htmlStream.append("	        	treeCMS.add(" + dTreeRootId + ",-1,'"+nameTree+"');\n");
	   	Iterator it = objectsList.iterator();
	   	while (it.hasNext()) {
	   		LowFunctionality folder = (LowFunctionality) it.next();
	   		if (initialPath != null) {
	   			if (initialPath.equalsIgnoreCase(folder.getPath())) addItemForJSTree(htmlStream, folder, false, true);
	   			else addItemForJSTree(htmlStream, folder, false, false);
	   		} else {
	   			if (folder.getParentId() == null) addItemForJSTree(htmlStream, folder, true, false);
	   			else addItemForJSTree(htmlStream, folder, false, false);
	   		}
	   	}
    	htmlStream.append("				document.write(treeCMS);\n");
    	makeJSFunctionForMenu(htmlStream);	
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		htmlStream.append("<div id='divmenuFunct' class='dtreemenu' onmouseout='hideMenu(event);' >");
		htmlStream.append("		menu");
		htmlStream.append("</div>");
		return htmlStream;
	}
	
	private void addItemForJSTree(StringBuffer htmlStream, LowFunctionality folder, 
			boolean isRoot, boolean isInitialPath) {
		
		String nameLabel = folder.getName();
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String codeType = folder.getCodType();
		Integer idFolder = folder.getId();
		Integer parentId = null;
		if (isInitialPath) parentId = new Integer (dTreeRootId);
		else parentId = folder.getParentId();

		if (isRoot) {
			htmlStream.append("	treeCMS.add(" + idFolder + ", " + dTreeRootId + ",'" + name + "', '', '', '', '', '', 'true');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	treeCMS.add(" + idFolder + ", " + parentId + ",'" + name + "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + "', '', '');\n");
				List objects = folder.getBiObjects();
				for (Iterator it = objects.iterator(); it.hasNext(); ) {
					BIObject obj = (BIObject) it.next();
					Integer idObj = obj.getId();					
					String stateObj = obj.getStateCode();
					if (ObjectsAccessVerifier.canExec(stateObj, idFolder, profile)) {
						htmlStream.append("	treeCMS.add(" + dTreeObjects-- + ", " + idFolder + ",'" + obj.getName() + "', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'" + createExecuteObjectLink(idObj) + "\\', \\'" + createDetailObjectLink(idObj) + "\\', \\'" + createEraseObjectLink(idObj, idFolder) + "\\')' );\n");
					} else {
						htmlStream.append("	treeCMS.add(" + dTreeObjects-- + ", " + idFolder + ",'" + obj.getName() + "', 'javascript:linkEmpty()', '', '', '', '', '', 'menu(event, \\'\\', \\'" + createDetailObjectLink(idObj) + "\\', \\'" + createEraseObjectLink(idObj, idFolder) + "\\')' );\n");
					}
				}
			}
		}
	}
	
	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO code for tree with no javascript
		return null;
	}
	
	private String createExecuteObjectLink(Integer id) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, id.toString());
		execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
		return execUrl.toString();
	}

	private String createDetailObjectLink(Integer id) {
		PortletURL detUrl = renderResponse.createActionURL();
		detUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		detUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_SELECT);
		detUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, id.toString());
		detUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		return detUrl.toString();
	}
	
	private String createEraseObjectLink(Integer idObj, Integer idFunct) {
		PortletURL delUrl = renderResponse.createActionURL();
		delUrl.setParameter(ObjectsTreeConstants.PAGE, DetailBIObjectModule.MODULE_PAGE);
		delUrl.setParameter(ObjectsTreeConstants.MESSAGE_DETAIL, ObjectsTreeConstants.DETAIL_DEL);
		delUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, idObj.toString());
		delUrl.setParameter(ObjectsTreeConstants.FUNCT_ID, idFunct.toString());
		delUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
		return delUrl.toString();
	}
	
}
