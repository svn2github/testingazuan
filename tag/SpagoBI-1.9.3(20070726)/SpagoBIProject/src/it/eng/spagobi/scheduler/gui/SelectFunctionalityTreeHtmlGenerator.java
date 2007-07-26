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
package it.eng.spagobi.scheduler.gui;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.presentation.treehtmlgenerators.ITreeHtmlGenerator;
import it.eng.spagobi.scheduler.to.JobInfo;
import it.eng.spagobi.scheduler.to.SaveInfo;
import it.eng.spagobi.scheduler.to.TriggerInfo;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Contains all methods needed to generate and modify a tree object for scheduling.
 * There are methods to generate tree, configure, insert and modify elements.
 */
public class SelectFunctionalityTreeHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	int progrJSTree = 0;
	IEngUserProfile profile = null;
	PortletRequest portReq = null;
	protected int dTreeRootId = -100;
	protected int dTreeObjects = -1000;
    List functIdsChecked = new ArrayList();


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
		
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath, String treename) {
		httpRequest = httpReq;
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		TriggerInfo tInfo = (TriggerInfo)sessionContainer.getAttribute(SpagoBIConstants.TRIGGER_INFO);
		String idObjStr = treename.substring(5);
		Integer idBiobj = Integer.valueOf(idObjStr);
		Map saveOpts = tInfo.getSaveOptions();
		SaveInfo sInfo = (SaveInfo)saveOpts.get(idBiobj);
		String functIdsConcat = sInfo.getFunctionalityIds();
		String[] functIds =  functIdsConcat.split(",");
		for(int i=0; i<functIds.length; i++) {
			String functIdStr = functIds[i];
			if(functIdStr.trim().equals(""))
				continue;
			Integer functId = Integer.valueOf(functIdStr);
			functIdsChecked.add(functId);
		}
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
	   	htmlStream.append("				var nameTree = '"+treename+"';\n");
	   	htmlStream.append("				"+treename+" = new dTree('"+treename+"');\n");
	   	htmlStream.append("	        	"+treename+".add(" + dTreeRootId + ",-1,'"+nameTree+"');\n");
	   	Iterator it = objectsList.iterator();
	   	while (it.hasNext()) {
	   		LowFunctionality folder = (LowFunctionality) it.next();
	   		if (initialPath != null) {
	   			if (initialPath.equalsIgnoreCase(folder.getPath())) addItemForJSTree(htmlStream, folder, false, true, treename);
	   			else addItemForJSTree(htmlStream, folder, false, false, treename);
	   		} else {
	   			if (folder.getParentId() == null) addItemForJSTree(htmlStream, folder, true, false, treename);
	   			else addItemForJSTree(htmlStream, folder, false, false, treename);
	   		}
	   	}
    	htmlStream.append("				document.write("+treename+");\n");
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		return htmlStream;
	}
	
	
	
	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		return makeTree(objectsList, httpRequest, initialPath, "Tree");
	}
	
	
	
	
	private void addItemForJSTree(StringBuffer htmlStream, LowFunctionality folder, 
			boolean isRoot, boolean isInitialPath, String treename) {
		String nameLabel = folder.getName();
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		String codeType = folder.getCodType();
		Integer idFolder = folder.getId();
		Integer parentId = null;
		if (isInitialPath) parentId = new Integer (dTreeRootId);
		else parentId = folder.getParentId();

		if (isRoot) {
			htmlStream.append("	"+treename+".add(" + idFolder + ", " + dTreeRootId + ",'" + name + "', '', '', '', '', '', 'true');\n");
		} else {
			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
				String checked = "";
				if(functIdsChecked.contains(idFolder)){
					checked = "true";
				}
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	"+treename+".add(" + idFolder + ", " + parentId + ",'" + name + "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + "', '', '', '"+treename+"_funct_id', '"+idFolder+"', '"+checked+"');\n");
			}
		}
	}
	
	
	
	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO code for tree with no javascript
		return null;
	}
	
	
}
