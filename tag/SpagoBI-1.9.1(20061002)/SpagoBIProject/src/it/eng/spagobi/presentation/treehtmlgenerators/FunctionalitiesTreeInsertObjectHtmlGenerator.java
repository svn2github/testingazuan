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
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.DetailBIObjectModule;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.Iterator;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
/**
 * Contains all methods needed to generate and modify a tree object for Functionalities
 * and an object insertion.
 * There are methods to generate tree, configure, insert and modify elements.
 * 
 * @author sulis
 */
public class FunctionalitiesTreeInsertObjectHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	int progrJSTree = 0;
	private IEngUserProfile profile = null;
	private int dTreeRootId = -100;
	private String actor = null;
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.
	 * AdminTreeHtmlGenerator#makeTree(it.eng.spago.base.SourceBean,javax.servlet.http.HttpServletRequest)
	 */
//	public StringBuffer makeTree(SourceBean dataTree, HttpServletRequest httpReq) {
//		
//		httpRequest = httpReq;
//		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
//		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");
//		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
//		SessionContainer sessionContainer = requestContainer.getSessionContainer();
//		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
//        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
//        
//		StringBuffer htmlStream = new StringBuffer();
//		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+"' type='text/css' />");
//		makeConfigurationDtree(htmlStream);
//		String nameTree = PortletUtilities.getMessage("tree.functtree.name" ,"messages");
//		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
//		htmlStream.append("<div id='divmenuFunctIns' class='dtreemenu' onmouseout='hideMenu(event);' >");
//		htmlStream.append("		menu");
//		htmlStream.append("</div>");
//		htmlStream.append("<table width='100%'>");
//		htmlStream.append("	<tr >");
//		htmlStream.append("		<td width='40px'>&nbsp;</td>");
//		htmlStream.append("		<td>");
//		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
//	   	htmlStream.append("				var nameTree = 'treeFunctIns';\n");
//	   	htmlStream.append("				treeFunctIns = new dTree('treeFunctIns');\n");
//	   	htmlStream.append("	        	treeFunctIns.add(0,-1,'"+nameTree+"');\n");
//        addItemForJSTree(htmlStream, dataTree, 0, true);
//    	htmlStream.append("				document.write(treeFunctIns);\n");
//		htmlStream.append("			</script>\n");
//		htmlStream.append("		</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("</table>");
//		return htmlStream;
//	}

	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#addItemForJSTree(java.lang.StringBuffer, it.eng.spago.base.SourceBean, int, boolean)
	 */
//	private void addItemForJSTree(StringBuffer htmlStream, SourceBean dataTree, int pidParent, boolean isRoot) {
//		
//		List childs = dataTree.getContainedSourceBeanAttributes();
//		String nameLabel = (String)dataTree.getAttribute("name");
//		String name = PortletUtilities.getMessage(nameLabel, "messages");
//		String path = (String)dataTree.getAttribute("path");
//		String codeType = (String)dataTree.getAttribute("codeType");
//
//		int id = ++progrJSTree;
//		
//		if(isRoot) {
//			htmlStream.append("	treeFunctIns.add("+id+", "+pidParent+",'"+name+"', '', '', '', '', '', 'true');\n");
//		} else {
//			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
//				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
//				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
//				if(ObjectsAccessVerifier.canDev(path, profile)) {
//					htmlStream.append("	treeFunctIns.add("+id+", "+pidParent+",'"+name+
//						          "', '', '', '', '"+imgFolder+"', '"+imgFolderOp+
//						          "', '', '', '"+ObjectsTreeConstants.PATH_PARENT+"', '"+path+"');\n");
//				} else {
//					htmlStream.append("	treeFunctIns.add("+id+", "+pidParent+",'"+name+
//					          "', '', '', '', '"+imgFolder+"', '"+imgFolderOp+
//					          "', '', '', '', '');\n");
//				}
//			} 
//		}
//		
//		Iterator iter = childs.iterator();
//		while(iter.hasNext()) {
//			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
//			SourceBean itemSB = (SourceBean)itemSBA.getValue();
//			addItemForJSTree(htmlStream, itemSB, id, false);
//		}
//		
//	}

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
		htmlStream.append("				inOrder			: false};\n");
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
	 * Creates an open, ad so accessible, tree. The code is directly appended into a
	 * String buffer, without using Javascripts.
	 * 
	 * @param dataTree The data Tree Source Bean
	 * @param httpServletRequest The http servlet request
	 * @return The output string buffer
	 * 
	 */
//	public StringBuffer makeAccessibleTree(SourceBean dataTree, HttpServletRequest httpRequest) {
//		StringBuffer htmlStream = new StringBuffer();
//		htmlStream.append("<div id='divAccessibleTreeFunctIns'>\n");
//        addItemForAccessibleTree(htmlStream, dataTree, 20, true);
//		htmlStream.append("</div>\n");
//		htmlStream.append("<br/><br/>\n");
//		makeJSFunctionForHideAccessibleTree(htmlStream);
//		return htmlStream;
//	}
	
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#addItemForAccessibleTree(java.lang.StringBuffer, it.eng.spago.base.SourceBean, int, boolean)
	 */
//	private void addItemForAccessibleTree(StringBuffer htmlStream, SourceBean dataTree, int spaceLeft, boolean isRoot) {
//		List childs = dataTree.getContainedSourceBeanAttributes();		
//		String name = (String)dataTree.getAttribute("name");
//		//String path = (String)dataTree.getAttribute("path");
//		String codeType = (String)dataTree.getAttribute("codeType");
//		if(isRoot) {
//			htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
//			htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderopen.gif" )+"' />");
//			htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
//			htmlStream.append("</div>");
//		} else {
//			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
//				htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
//				htmlStream.append("		<input type='checkbox' name='pathParent' />\n");
//				htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderopen.gif" )+"' />");
//	    		htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
//	    		htmlStream.append("</div>\n");
//			} 
//		}
//				
//		Iterator iter = childs.iterator();
//		while(iter.hasNext()) {
//			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
//			SourceBean itemSB = (SourceBean)itemSBA.getValue();
//			addItemForAccessibleTree(htmlStream, itemSB, (spaceLeft + 40), false);
//		}
//	}
	
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#makeJSFunctionForHideAccessibleTree(java.lang.StringBuffer)
	 */
//	private void makeJSFunctionForHideAccessibleTree(StringBuffer htmlStream) {
//		htmlStream.append("<SCRIPT>\n");
//		htmlStream.append("		divM = document.getElementById('divAccessibleTreeFunctIns');\n");
//		htmlStream.append("		divM.style.display='none';\n");
//		htmlStream.append("</SCRIPT>\n");
//	}
	
	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		
		httpRequest = httpReq;
		renderResponse = (RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		SourceBean serviceRequest = requestContainer.getServiceRequest();
		actor = (String) serviceRequest.getAttribute(SpagoBIConstants.ACTOR);
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		ResponseContainer responseContainer = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		SourceBean serviceResponse = responseContainer.getServiceResponse();
		BIObject obj = (BIObject) serviceResponse.getAttribute("DetailBIObjectModule." + DetailBIObjectModule.NAME_ATTR_OBJECT);
        
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+"' type='text/css' />");
		makeConfigurationDtree(htmlStream);
		String nameTree = PortletUtilities.getMessage("tree.functtree.name" ,"messages");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
		htmlStream.append("<div id='divmenuFunctIns' class='dtreemenu' onmouseout='hideMenu(event);' >");
		htmlStream.append("		menu");
		htmlStream.append("</div>");
		htmlStream.append("<table width='100%'>");
		htmlStream.append("	<tr >");
		htmlStream.append("		<td width='40px'>&nbsp;</td>");
		htmlStream.append("		<td>");
		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
	   	htmlStream.append("				var nameTree = 'treeFunctIns';\n");
	   	htmlStream.append("				treeFunctIns = new dTree('treeFunctIns');\n");
	   	htmlStream.append("	        	treeFunctIns.add(" + dTreeRootId + ",-1,'"+nameTree+"');\n");
	   	Iterator it = objectsList.iterator();
	   	while (it.hasNext()) {
	   		LowFunctionality folder = (LowFunctionality) it.next();
	   		if (initialPath != null) {
	   			if (initialPath.equalsIgnoreCase(folder.getPath())) addItemForJSTree(htmlStream, folder, obj, false, true);
	   			else addItemForJSTree(htmlStream, folder, obj, false, false);
	   		} else {
	   			if (folder.getParentId() == null) addItemForJSTree(htmlStream, folder, obj, true, false);
	   			else addItemForJSTree(htmlStream, folder, obj, false, false);
	   		}
	   	}
	   	htmlStream.append("				document.write(treeFunctIns);\n");
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		return htmlStream;
		
	}

	private void addItemForJSTree(StringBuffer htmlStream, LowFunctionality folder, BIObject obj, 
			boolean isRoot, boolean isInitialPath) {
		
		String nameLabel = folder.getName();
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String codeType = folder.getCodType();
		Integer id = folder.getId();
		Integer parentId = null;
		if (isInitialPath) parentId = new Integer (dTreeRootId);
		else parentId = folder.getParentId();

		if (isRoot) {
			htmlStream.append("	treeFunctIns.add(" + id + ", " + dTreeRootId + ",'" + name + "', '', '', '', '', '', 'true');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				if (SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor) || ObjectsAccessVerifier.canDev(id, profile)) {
					boolean checked = false;
					if (obj != null) {
						List funcs = obj.getFunctionalities();
						if (funcs.contains(id)) checked = true;
					}
					htmlStream.append("	treeFunctIns.add(" + id + ", " + parentId + ",'" + name + 
						          "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + 
						          "', '', '', '" + ObjectsTreeConstants.FUNCT_ID + "', '" + id + "'," + checked + ");\n");
				} else if (ObjectsAccessVerifier.canExec(id, profile)) {
					htmlStream.append("	treeFunctIns.add(" + id + ", " + parentId + ",'" + name + 
					          "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + 
					          "', '', '', '', '',false);\n");
				}
			} 
		}
	}

	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
