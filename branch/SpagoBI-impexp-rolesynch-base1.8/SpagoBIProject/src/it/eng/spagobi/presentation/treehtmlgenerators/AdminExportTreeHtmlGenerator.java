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

import java.util.Iterator;
import java.util.List;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.PortletUtilities;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

public class AdminExportTreeHtmlGenerator extends AdminTreeHtmlGenerator {

	/**
	 * Builds theJavaScript object to make the tree. All code is appended into a 
	 * String Buffer, which is then returned. 
	 * @param dataTree The tree data Source Bean
	 * @param httpReq The http Servlet Request 
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
		htmlStream.append("<LINK rel='StyleSheet' href='"+
				renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+
				"' type='text/css' />");
		makeConfigurationDtree(htmlStream);
		String nameTree = PortletUtilities.getMessage("tree.objectstree.name" ,"messages");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+
				renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+
				renderResponse.encodeURL(renderRequest.getContextPath() + "/js/contextMenu.js" )+"'></SCRIPT>");
		htmlStream.append("<div id='divmenuFunct' style='width:150px;' class='dtreemenu' onmouseout='hideMenu(event);' >");
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
	 * @param htmlStream	The input String Buffer
	 * @param dataTree	The tree Source Bean object
	 * @param pidParent	The parent's node ID
	 * @param isRoot A boolean to control if the item is a root
	 */
	protected void addItemForJSTree(StringBuffer htmlStream, SourceBean dataTree, int pidParent, boolean isRoot) {
		List childs = dataTree.getContainedSourceBeanAttributes();
		String nameLabel = (String)dataTree.getAttribute("name");
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String path = (String)dataTree.getAttribute("path");
		String codeType = (String)dataTree.getAttribute("codeType");
		int id = ++progrJSTree;
		if(isRoot) {
			htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', 'true', 'menu(event, \\'"+path+"\\')');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '"+imgFolder+"', '"+imgFolderOp+"', '', 'menu(event, \\'"+path+"\\')');\n");
			} else {
				String icon = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticon.png");
				htmlStream.append("	treeCMS.add("+id+", "+pidParent+",'"+name+"', 'javascript:linkEmpty()', '', '', '', '', '', '', '"+SpagoBIConstants.PATH+"', '"+path+"');\n");
			}	
		}
		Iterator iter = childs.iterator();
		while(iter.hasNext()) {
			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
			SourceBean itemSB = (SourceBean)itemSBA.getValue();
			addItemForJSTree(htmlStream, itemSB, id, false);
		}
	}

	
	
	
	protected void makeJSFunctionForMenu(StringBuffer htmlStream) {
		htmlStream.append("		function menu(event, pathFather) {\n");
		htmlStream.append("			divM = document.getElementById('divmenuFunct');\n");
		htmlStream.append("			divM.innerHTML = '';\n");
		String capSelect = PortletUtilities.getMessage("SBISet.importexport.selectall", "messages");
		htmlStream.append("			divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"javascript:select(\\''+pathFather+'\\')\">"+capSelect+"</a></div>';\n");
		String capDeselect = PortletUtilities.getMessage("SBISet.importexport.deselectall", "messages");
		htmlStream.append("			divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"javascript:deselect(\\''+pathFather+'\\')\">"+capDeselect+"</a></div>';\n");
		htmlStream.append("			showMenu(event, divM);\n");
		htmlStream.append("		}\n");

		
		htmlStream.append("		function linkEmpty() {\n");
		htmlStream.append("		}\n");
		
		htmlStream.append("		function select(path) {\n");
		htmlStream.append("			var checkColl = document.getElementsByName('"+SpagoBIConstants.PATH+"');\n");
		htmlStream.append("		    for(var i=0; i< checkColl.length; i++){\n");
		htmlStream.append("             value = checkColl[i].value;\n"); 
		htmlStream.append("             if(value.indexOf(path)!= -1) {\n"); 
		htmlStream.append("		    		if(!checkColl[i].checked){\n");
		htmlStream.append("		    			checkColl[i].click();\n");
		htmlStream.append("		    		}\n");
		htmlStream.append("		    	}\n");
		htmlStream.append("		    }\n");
		htmlStream.append("		}\n");
		
		htmlStream.append("		function deselect(path) {\n");
		htmlStream.append("			var checkColl = document.getElementsByName('"+SpagoBIConstants.PATH+"');\n");
		htmlStream.append("		    for(var i=0; i< checkColl.length; i++){\n");
		htmlStream.append("             value = checkColl[i].value;\n"); 
		htmlStream.append("             if(value.indexOf(path)!= -1) {\n"); 
		htmlStream.append("		    		if(checkColl[i].checked){\n");
		htmlStream.append("		    			checkColl[i].click();\n");
		htmlStream.append("		    		}\n");
		htmlStream.append("		    	}\n");
		htmlStream.append("		    }\n");
		htmlStream.append("		}\n");
	}
	
	
	
}
