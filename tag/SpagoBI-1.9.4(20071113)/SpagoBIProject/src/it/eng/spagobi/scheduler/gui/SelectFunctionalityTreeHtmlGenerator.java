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
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.presentation.treehtmlgenerators.ITreeHtmlGenerator;
import it.eng.spagobi.scheduler.to.SaveInfo;
import it.eng.spagobi.scheduler.to.TriggerInfo;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Contains all methods needed to generate and modify a tree object for scheduling.
 * There are methods to generate tree, configure, insert and modify elements.
 */
public class SelectFunctionalityTreeHtmlGenerator implements ITreeHtmlGenerator {

	HttpServletRequest httpRequest = null;
	RequestContainer reqCont = null;
	protected IUrlBuilder urlBuilder = null;
	protected IMessageBuilder msgBuilder = null;
	int progrJSTree = 0;
	IEngUserProfile profile = null;
	protected int dTreeRootId = -100;
	protected int dTreeObjects = -1000;
    List functIdsChecked = new ArrayList();
    protected String requestIdentity = null;


	/**
	 * Creates the Dtree configuration, in oder to inser into jsp pages cookies,
	 * images, etc.
	 * 
	 * @param htmlStream	The input String Buffer
	 */
    /*
	protected void makeConfigurationDtree(StringBuffer htmlStream) {
		
		htmlStream.append("<SCRIPT>\n");
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
		htmlStream.append("				root		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treebase.gif")+"',\n");
		htmlStream.append("				folder		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treefolder.gif")+"',\n");
		htmlStream.append("				folderOpen	: '"+urlBuilder.getResourceLink(httpRequest, "/img/treefolderopen.gif")+"',\n");
		htmlStream.append("				node		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treepage.gif")+"',\n");
		htmlStream.append("				empty		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treeempty.gif")+"',\n");
		htmlStream.append("				line		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treeline.gif")+"',\n");
		htmlStream.append("				join		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treejoin.gif")+"',\n");
		htmlStream.append("				joinBottom	: '"+urlBuilder.getResourceLink(httpRequest, "/img/treejoinbottom.gif")+"',\n");
		htmlStream.append("				plus		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treeplus.gif")+"',\n");
		htmlStream.append("				plusBottom	: '"+urlBuilder.getResourceLink(httpRequest, "/img/treeplusbottom.gif")+"',\n");
		htmlStream.append("				minus		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treeminus.gif")+"',\n");
		htmlStream.append("				minusBottom	: '"+urlBuilder.getResourceLink(httpRequest, "/img/treeminusbottom.gif")+"',\n");
		htmlStream.append("				nlPlus		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treenolines_plus.gif")+"',\n");
		htmlStream.append("				nlMinus		: '"+urlBuilder.getResourceLink(httpRequest, "/img/treenolines_minus.gif")+"'\n");
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
	*/
    
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath, String treename) {
		// identity string for object of the page
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    requestIdentity = uuid.toString();
	    requestIdentity = requestIdentity.replaceAll("-", "");
		httpRequest = httpReq;
		reqCont = ChannelUtilities.getRequestContainer(httpRequest);
		urlBuilder = UrlBuilderFactory.getUrlBuilder();
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		SessionContainer sessionContainer = reqCont.getSessionContainer();
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
		htmlStream.append("<LINK rel='StyleSheet' href='"+urlBuilder.getResourceLink(httpRequest, "/css/dtree.css" )+"' type='text/css' />");
		//makeConfigurationDtree(htmlStream);
		String nameTree = msgBuilder.getMessage("tree.objectstree.name" ,"messages", httpRequest);
		htmlStream.append("<SCRIPT language='JavaScript' src='"+urlBuilder.getResourceLink(httpRequest, "/js/dtree.js" )+"'></SCRIPT>");		
		htmlStream.append("<SCRIPT language='JavaScript' src='"+urlBuilder.getResourceLink(httpRequest, "/js/contextMenu.js" )+"'></SCRIPT>");
		htmlStream.append("<table width='100%'>");
		htmlStream.append("	<tr height='1px'>");
		htmlStream.append("		<td width='10px'>&nbsp;</td>");
		htmlStream.append("		<td>&nbsp;</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("	<tr>");
		htmlStream.append("		<td>&nbsp;</td>");
		htmlStream.append("		<td id='treeSchedulerFoldersTd" + requestIdentity + "' name='treeSchedulerFoldersTd" + requestIdentity + "'>&nbsp;</td>");
		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
	   	htmlStream.append("				var nameTree = '"+treename+"';\n");
	   	htmlStream.append("				"+treename+" = new dTree('"+treename+"', '" + httpRequest.getContextPath() + "');\n");
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
    	//htmlStream.append("				document.write("+treename+");\n");
	   	htmlStream.append("				document.getElementById('treeSchedulerFoldersTd" + requestIdentity + "').innerHTML = " + treename + ";\n");
		htmlStream.append("			</script>\n");
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
		String name = msgBuilder.getMessage(nameLabel, "messages", httpRequest);
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
				String imgFolder = urlBuilder.getResourceLink(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = urlBuilder.getResourceLink(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	"+treename+".add(" + idFolder + ", " + parentId + ",'" + name + "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + "', '', '', '"+treename+"_funct_id', '"+idFolder+"', '"+checked+"');\n");
			}
		}
	}
	
	
	
	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO code for tree with no javascript
		return null;
	}
	
	
}
