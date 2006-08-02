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
package it.eng.spagobi.pamphlets.treegenerators;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.pamphlets.constants.PamphletsConstants;
import it.eng.spagobi.presentation.treehtmlgenerators.ITreeHtmlGenerator;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

public class DocumentsTreeHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	int progrJSTree = 0;
	IEngUserProfile profile = null;
	PortletRequest portReq = null;
	protected int dTreeRootId = -100;
	protected int dTreeObjects = -1000;
	
	/**
	 * Builds theJavaScript object to make the tree. All code is appended into a 
	 * String Buffer, which is then returned. 
	 * @param dataTree The tree data Source Bean
	 * @param httpReq The http Servlet Request  
	 */
//	public StringBuffer makeTree(SourceBean dataTree, HttpServletRequest httpReq) {
//		httpRequest = httpReq;
//		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
//		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");
//		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
//		portReq = PortletUtilities.getPortletRequest();
//		SessionContainer sessionContainer = requestContainer.getSessionContainer();
//		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
//        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
//		StringBuffer htmlStream = new StringBuffer();
//		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+"' type='text/css' />");
//		makeConfigurationDtree(htmlStream);
//		String nameTree = PortletUtilities.getMessage("tree.objectstree.name" ,"messages");
//		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
//		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/contextMenu.js" )+"'></SCRIPT>");
//		htmlStream.append("<div id='divmenuFunct' class='dtreemenu' onmouseout='hideMenu(event);' >");
//		htmlStream.append("		menu");
//		htmlStream.append("</div>");
//		htmlStream.append("<table width='100%'>");
//		htmlStream.append("	<tr height='1px'>");
//		htmlStream.append("		<td width='10px'>&nbsp;</td>");
//		htmlStream.append("		<td>&nbsp;</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("	<tr>");
//		htmlStream.append("		<td>&nbsp;</td>");
//		htmlStream.append("		<td>");
//		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
//	   	htmlStream.append("				var nameTree = 'treeCMS';\n");
//	   	htmlStream.append("				treeCMS = new dTree('treeCMS');\n");
//	   	htmlStream.append("	        	treeCMS.add(0,-1,'"+nameTree+"');\n");
//        addItemForJSTree(htmlStream, dataTree, 0, true);
//    	htmlStream.append("				document.write(treeCMS);\n");
//		htmlStream.append("			</script>\n");
//		htmlStream.append("		</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("</table>");
//		return htmlStream;
//	}

	/**
	 * Adds an item to the data tree, using input needed information like
	 * the parent node id and controlling if the item to add is a root.
	 * @param htmlStream	The input String Buffer
	 * @param dataTree	The tree Source Bean object
	 * @param pidParent	The parent's node ID
	 * @param isRoot A boolean to control if the item is a root
	 */
//	private void addItemForJSTree(StringBuffer htmlStream, SourceBean dataTree, int pidParent, boolean isRoot) {
//		List childs = dataTree.getContainedSourceBeanAttributes();
//		String nameLabel = (String)dataTree.getAttribute("name");
//		String name = PortletUtilities.getMessage(nameLabel, "messages");
//		String path = (String)dataTree.getAttribute("path");
//		String codeType = (String)dataTree.getAttribute("codeType");
//		int id = ++progrJSTree;
//		if(isRoot) {
//			htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', '', '', '', '', '', 'true');\n");
//		} else {
//			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
//				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
//				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
//				htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', '', '', '', '"+imgFolder+"', '"+imgFolderOp+"', '', '');\n");
//			} else if(codeType.equalsIgnoreCase(SpagoBIConstants.REPORT_TYPE_CODE)) {
//				String icon = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticon.png");
//				htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', '', '"+PamphletsConstants.PATH_OBJECT+"', '"+path+"' );\n");
//			}
//		}
//		Iterator iter = childs.iterator();
//		while(iter.hasNext()) {
//			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
//			SourceBean itemSB = (SourceBean)itemSBA.getValue();
//			addItemForJSTree(htmlStream, itemSB, id, false);
//		}
//		
//	}

	/**
	 * Creates the Dtree configuration, in oder to inser into jsp pages cookies,
	 * images, etc.
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

	public StringBuffer makeAccessibleTree(SourceBean dataTree, HttpServletRequest httpRequest) {
		StringBuffer htmlStream = new StringBuffer();
		return htmlStream;
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
	   	htmlStream.append("	        	treeCMS.add(" + dTreeRootId + ",-1,'"+nameTree+"');\n");
	   	Iterator it = objectsList.iterator();
	   	while (it.hasNext()) {
	   		LowFunctionality folder = (LowFunctionality) it.next();
	   		if (initialPath != null) {
	   			if (initialPath.equalsIgnoreCase(folder.getPath())) addItemForJSTree(htmlStream, folder, true);
	   			else addItemForJSTree(htmlStream, folder, false);
	   		} else {
	   			if (folder.getParentId() == null) addItemForJSTree(htmlStream, folder, true);
	   			else addItemForJSTree(htmlStream, folder, false);
	   		}
	   	}
    	htmlStream.append("				document.write(treeCMS);\n");
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		return htmlStream;
	}

	private void addItemForJSTree(StringBuffer htmlStream, LowFunctionality folder, boolean isRoot) {

		String nameLabel = folder.getName();
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String codeType = folder.getCodType();
		Integer idFolder = folder.getId();
		Integer parentId = folder.getParentId();

		if (isRoot) {
			htmlStream.append("	treeCMS.add(" + idFolder + ", " + dTreeRootId + ",'" + name + "', '', '', '', '', '', 'true');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	treeCMS.add(" + idFolder + ", " + parentId + ",'" + name + "', '', '', '', '" + imgFolder+"', '" + imgFolderOp + "', '', '');\n");
				
				List objects = folder.getBiObjects();
				for (Iterator it = objects.iterator(); it.hasNext(); ) {
					BIObject obj = (BIObject) it.next();
					Engine engine = obj.getEngine();
					String objTypeCode = obj.getBiObjectTypeCode();
					ConfigSingleton config = ConfigSingleton.getInstance();
					SourceBean technologyFilterSB = (SourceBean) config.getAttribute("PAMPHLETS.TECHNOLOGY_DRIVER_FILTER");
					String technologyFilter = (String) technologyFilterSB.getAttribute("match");
					if (objTypeCode.equalsIgnoreCase(SpagoBIConstants.REPORT_TYPE_CODE) && engine.getDriverName().toLowerCase().contains(technologyFilter)) {
						htmlStream.append("	treeCMS.add(" + dTreeObjects-- + ", " + idFolder + ",'" + obj.getName() + "', 'javascript:linkEmpty()', '', '', '', '', '', '', '" + PamphletsConstants.PATH_OBJECT + "', '" + obj.getPath() + "' );\n");
					}
				}
			}
		}
	}
	
	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
