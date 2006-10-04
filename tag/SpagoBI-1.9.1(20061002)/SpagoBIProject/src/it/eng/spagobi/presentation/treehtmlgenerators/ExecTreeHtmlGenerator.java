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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.ExecuteBIObjectModule;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
/**
 * Contains all methods needed to generate and modify a tree object for Execution.
 * There are methods to generate tree, configure, insert and modify elements.
 * 
 * @author sulis
 */
public class ExecTreeHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	IEngUserProfile profile = null;
	int progrJSTree = 0;
	private SessionContainer sessionContainer = null;
	private boolean thereIsOneOrMoreObjectsInTestState = false;
	protected SourceBean _serviceRequest = null;
	private int dTreeRootId = -100;
	private int dTreeObjects = -1000;
	//ArrayList testRoleNames = new ArrayList();
	//ArrayList execRoleNames = new ArrayList();
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
//		_serviceRequest = requestContainer.getServiceRequest();
//		sessionContainer = requestContainer.getSessionContainer();
//		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
//        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
//		StringBuffer htmlStream = new StringBuffer();
//		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+"' type='text/css' />");
//		makeConfigurationDtree(htmlStream);
//		String nameTree = PortletUtilities.getMessage("tree.objectstree.name" ,"messages");
//		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
//		htmlStream.append("<div id='divmenu' style='position:absolute;left:0;top:0;display:none;width:80px;height:120px;background-color:#FFFFCC;border-color:black;border-style:solid;border-weight:1;' onmouseout='hideMenu();' >");
//		htmlStream.append("		menu");
//		htmlStream.append("</div>");
//		
//		htmlStream.append("<div id='viewOnlyTestDocument' style='display:none;'>");
//		htmlStream.append("<table width='100%'>");
//		htmlStream.append("	<tr height='1px'>");
//		htmlStream.append("		<td width='10px'>&nbsp;</td>");
//		htmlStream.append("		<td>&nbsp;</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("	<tr>");
//		htmlStream.append("		<td>&nbsp;</td>");
//		htmlStream.append("		<td>");
//		String checked = "";
//		String onlyTestObjectsView = httpRequest.getParameter("view_only_test_objects");
//		String onlyTestObjectsViewLbl = PortletUtilities.getMessage("tree.objectstree.showOnlyTestObject", "messages");
//		if ("true".equalsIgnoreCase(onlyTestObjectsView)) checked = "checked='checked'";
//		htmlStream.append("			<span class=\"dtree\">" + onlyTestObjectsViewLbl + "</span>\n");
//		htmlStream.append("			<input type=\"checkbox\" " + checked + " \n");
//		htmlStream.append("				onclick=\"document.getElementById('view_only_test_objects').checked=this.checked;document.getElementById('objectForm').submit()\" />\n");
//		htmlStream.append("		</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("</table>");
//		htmlStream.append("</div>");
//		
//		htmlStream.append("<table width='100%'>");
//		htmlStream.append("	<tr height='1px'>");
//		htmlStream.append("		<td width='10px'>&nbsp;</td>");
//		htmlStream.append("		<td>&nbsp;</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("	<tr>");
//		htmlStream.append("		<td>&nbsp;</td>");
//		htmlStream.append("		<td>");
//		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
//	   	htmlStream.append("				var nameTree = 'treeExecObj';\n");
//	   	htmlStream.append("				treeExecObj = new dTree('treeExecObj');\n");
//	   	htmlStream.append("	        	treeExecObj.add(0,-1,'"+nameTree+"');\n");
//        addItemForJSTree(htmlStream, dataTree, 0, true);
//    	htmlStream.append("				document.write(treeExecObj);\n");
//    	makeJSFunctionForMenu(htmlStream);	
//		htmlStream.append("			</script>\n");
//		htmlStream.append("		</td>");
//		htmlStream.append("	</tr>");
//		htmlStream.append("</table>");
//		htmlStream.append("<br/>");
//		
//		// if there is one or more document in test state diplay the div with id='viewOnlyTestDocument'
//		if (thereIsOneOrMoreObjectsInTestState) {
//			htmlStream.append("<script type='text/javascript'>\n");
//			htmlStream.append("	document.getElementById('viewOnlyTestDocument').style.display='inline';\n");
//			htmlStream.append("</script>\n");
//		}
//		
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
//		boolean addItemFlag = false;
//
//		int id = ++progrJSTree;
//		
//		if(isRoot) {
//			htmlStream.append("	treeExecObj.add("+id+", "+pidParent+",'"+name+"', '', '', '', '', '', 'true');\n");
//		} else {
//			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
//				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
//				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
//				if(ObjectsAccessVerifier.canTest(path,profile)||ObjectsAccessVerifier.canExec(path,profile)){
//					addItemFlag = true;
//				htmlStream.append("	treeExecObj.add("+id+", "+pidParent+",'"+name+"', '', '', '', '"+imgFolder+"', '"+imgFolderOp+"', '', '');\n");
//				}
//			} else {
//				String userIcon = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticon.gif");
//				String userIconTest = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticontest.gif");
//				String stateObj = (String)dataTree.getAttribute("state");
//				Integer visibleObj = (Integer)dataTree.getAttribute("visible");
//				String onlyTestObjectsView = (String)_serviceRequest.getAttribute("view_only_test_objects");
//				PortletURL execUrl = renderResponse.createActionURL();
//				execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
//				execUrl.setParameter(ObjectsTreeConstants.PATH, path);
//				execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
//				
//				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
//			            "addItemForJSTree ", nameLabel+": "+ stateObj +" - "+ visibleObj);
//				if( visibleObj != null && visibleObj.intValue() == 0 && (stateObj.equalsIgnoreCase("REL") || stateObj.equalsIgnoreCase("TEST"))) {
//					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
//				            "addItemForJSTree ", "NOT visible " + nameLabel);
//				}
//				else {	
//					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
//				            "addItemForJSTree ", "VISIBLE " + nameLabel);
//					if(ObjectsAccessVerifier.canTest(stateObj, path, profile)) {
//						//sessionContainer.setAttribute(SpagoBIConstants.ACTOR, SpagoBIConstants.TESTER_ACTOR);
//						thereIsOneOrMoreObjectsInTestState = true;
//						execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.TESTER_ACTOR);
//						htmlStream.append("	treeExecObj.add("+id+", "+pidParent+",'"+name+"', '"+execUrl.toString()+"', '', '', '"+userIconTest+"', '', '', '' );\n");
//					} else if(!"true".equalsIgnoreCase(onlyTestObjectsView) && ObjectsAccessVerifier.canExec(stateObj, path, profile)) {
//						//sessionContainer.setAttribute(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
//						execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
//						htmlStream.append("	treeExecObj.add("+id+", "+pidParent+",'"+name+"', '"+execUrl.toString()+"', '', '', '"+userIcon+"', '', '', '' );\n");
//					}
//				}
//			}
//		}
//		
//		Iterator iter = childs.iterator();
//		while(iter.hasNext()) {
//			SourceBeanAttribute itemSBA = (SourceBeanAttribute)iter.next();
//			SourceBean itemSB = (SourceBean)itemSBA.getValue();
//			if(addItemFlag || isRoot){
//			addItemForJSTree(htmlStream, itemSB, id, false);}
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
		htmlStream.append("		function linkEmpty() {\n");
		htmlStream.append("		}\n");
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
//		htmlStream.append("<div id='divAccessibleTree'>\n");
//        addItemForAccessibleTree(htmlStream, dataTree, 0, true);
//		//htmlStream.append("<br/><br/>\n");
//        if (thereIsOneOrMoreObjectsInTestState) {
//    		PortletURL formUrl = renderResponse.createActionURL();
//    		formUrl.setParameter("PAGE", "LOGIN_PAGE_SBI_FUNCTIONALITY");
//    		formUrl.setParameter("ACTOR", "USER_ACTOR");
//    		formUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_TREE);
//    		String checked = "";
//    		String onlyTestObjectsView = httpRequest.getParameter("view_only_test_objects");
//    		String onlyTestObjectsViewLbl = PortletUtilities.getMessage("tree.objectstree.showOnlyTestObject", "messages");
//    		String updateTree = PortletUtilities.getMessage("tree.objectstree.update", "messages");
//    		if ("true".equalsIgnoreCase(onlyTestObjectsView)) checked = "checked='checked'";
//    		htmlStream.append("	<form method='POST' action='" + formUrl.toString() + "' id ='objectForm' name='objectForm'>\n");
//    		htmlStream.append("		<span>" + onlyTestObjectsViewLbl + "</span>\n");
//    		htmlStream.append("		<input type=\"checkbox\" name=\"view_only_test_objects\" id=\"view_only_test_objects\" value=\"true\" " + checked + " />\n");
//    		htmlStream.append("		<input type=\"image\" style=\"width:25px;height:25px\" title=\"" + updateTree + "\" alt\"" + updateTree + "\" \n");
//    		htmlStream.append("			src=\"" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png" ) + "\" />\n");
//    		htmlStream.append("	</form>\n");
//        }
//		htmlStream.append("</div>\n");
//		makeJSFunctionForHideAccessibleTree(htmlStream);
//		return htmlStream;
//	}
	
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#addItemForAccessibleTree(java.lang.StringBuffer, it.eng.spago.base.SourceBean, int, boolean)
	 */
//	private void addItemForAccessibleTree(StringBuffer htmlStream, SourceBean dataTree, int spaceLeft, boolean isRoot) {
//		List childs = dataTree.getContainedSourceBeanAttributes();		
//		String nameLabel = (String)dataTree.getAttribute("name");
//		String name = PortletUtilities.getMessage(nameLabel, "messages");
//		String path = (String)dataTree.getAttribute("path");
//		String codeType = (String)dataTree.getAttribute("codeType");
//		if(isRoot) {
//			htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
//			htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderopen.gif" )+"' />");
//			htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
//			htmlStream.append("</div>");
//		} else {
//			if(codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
//				htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
//				htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/folderopen.gif" )+"' />");
//	    		htmlStream.append("		<span class=\"portlet-item-menu\">"+name+"</span>\n");
//	    		htmlStream.append("</div>\n");
//			} else {
//				String stateObj = (String)dataTree.getAttribute("state");
//				Integer visibleObj = (Integer)dataTree.getAttribute("visible");
//				String onlyTestObjectsView = httpRequest.getParameter("view_only_test_objects");
//				PortletURL execUrl = renderResponse.createActionURL();
//				execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
//				execUrl.setParameter(ObjectsTreeConstants.PATH, path);
//				execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
//				
//				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
//			            "addItemForJSTree ", nameLabel+": "+ stateObj +" - "+ visibleObj);
//				if( visibleObj != null && visibleObj.intValue() == 0 && (stateObj.equalsIgnoreCase("REL") || stateObj.equalsIgnoreCase("TEST"))) {
//					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
//				            "addItemForJSTree ", "NOT VISIBLE " + nameLabel);
//				}
//				else {	
//					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
//				            "addItemForJSTree ", "VISIBLE " + nameLabel);
//					if(ObjectsAccessVerifier.canTest(stateObj, path, profile)) {
//						execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.TESTER_ACTOR);
//						thereIsOneOrMoreObjectsInTestState = true;
//						htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
//						htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/objecticontest.gif" )+"' />");
//			    		htmlStream.append("		<span class=\"portlet-item-menu\"><a href='"+execUrl.toString()+"'>"+name+"</a></span>\n");
//			    		htmlStream.append("</div>\n");
//					} else if(!"true".equalsIgnoreCase(onlyTestObjectsView) && ObjectsAccessVerifier.canExec(stateObj, path, profile)) {
//						execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
//						htmlStream.append("<div style=\"padding-left:"+spaceLeft+"px;\">\n");
//						htmlStream.append("		<img src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/img/objecticon.gif" )+"' />");
//			    		htmlStream.append("		<span class=\"portlet-item-menu\"><a href='"+execUrl.toString()+"'>"+name+"</a></span>\n");
//			    		htmlStream.append("</div>\n");
//					}
//				}
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
//		htmlStream.append("		divM = document.getElementById('divAccessibleTree');\n");
//		htmlStream.append("		divM.style.display='none';\n");
//		htmlStream.append("</SCRIPT>\n");
//	}
		
	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		
		httpRequest = httpReq;
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");	
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		_serviceRequest = requestContainer.getServiceRequest();
		sessionContainer = requestContainer.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/css/dtree.css" )+"' type='text/css' />");
		makeConfigurationDtree(htmlStream);
		String nameTree = PortletUtilities.getMessage("tree.objectstree.name" ,"messages");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dtree.js" )+"'></SCRIPT>");
		htmlStream.append("<div id='divmenu' style='position:absolute;left:0;top:0;display:none;width:80px;height:120px;background-color:#FFFFCC;border-color:black;border-style:solid;border-weight:1;' onmouseout='hideMenu();' >");
		htmlStream.append("		menu");
		htmlStream.append("</div>");
		htmlStream.append("<div id='viewOnlyTestDocument' style='display:none;'>");
		htmlStream.append("<table width='100%'>");
		htmlStream.append("	<tr height='1px'>");
		htmlStream.append("		<td width='10px'>&nbsp;</td>");
		htmlStream.append("		<td>&nbsp;</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("	<tr>");
		htmlStream.append("		<td>&nbsp;</td>");
		htmlStream.append("		<td>");
		String checked = "";
		String onlyTestObjectsView = httpRequest.getParameter("view_only_test_objects");
		String onlyTestObjectsViewLbl = PortletUtilities.getMessage("tree.objectstree.showOnlyTestObject", "messages");
		if ("true".equalsIgnoreCase(onlyTestObjectsView)) checked = "checked='checked'";
		htmlStream.append("			<span class=\"dtree\">" + onlyTestObjectsViewLbl + "</span>\n");
		htmlStream.append("			<input type=\"checkbox\" " + checked + " \n");
		htmlStream.append("				onclick=\"document.getElementById('view_only_test_objects').checked=this.checked;document.getElementById('objectForm').submit()\" />\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
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
	   	htmlStream.append("				var nameTree = 'treeExecObj';\n");
	   	htmlStream.append("				treeExecObj = new dTree('treeExecObj');\n");
	   	htmlStream.append("	        	treeExecObj.add(" + dTreeRootId + ",-1,'"+nameTree+"');\n");
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
    	htmlStream.append("				document.write(treeExecObj);\n");
    	makeJSFunctionForMenu(htmlStream);	
		htmlStream.append("			</script>\n");
		htmlStream.append("		</td>");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		htmlStream.append("<br/>");
		
		// if there is one or more document in test state diplay the div with id='viewOnlyTestDocument'
		if (thereIsOneOrMoreObjectsInTestState) {
    		PortletURL formUrl = renderResponse.createActionURL();
    		formUrl.setParameter("PAGE", "LOGIN_PAGE_SBI_FUNCTIONALITY");
    		formUrl.setParameter("ACTOR", "USER_ACTOR");
    		formUrl.setParameter(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_TREE);
    		String updateTree = PortletUtilities.getMessage("tree.objectstree.update", "messages");
    		htmlStream.append("	<div style=\"display:none;\">\n");
    		htmlStream.append("	<form method='POST' action='" + formUrl.toString() + "' id ='objectForm' name='objectForm'>\n");
    		htmlStream.append("		<span>" + onlyTestObjectsViewLbl + "</span>\n");
    		htmlStream.append("		<input type=\"checkbox\" name=\"view_only_test_objects\" id=\"view_only_test_objects\" value=\"true\" " + checked + " />\n");
    		htmlStream.append("		<input type=\"image\" style=\"width:25px;height:25px\" title=\"" + updateTree + "\" alt\"" + updateTree + "\" \n");
    		htmlStream.append("			src=\"" + renderResponse.encodeURL(renderRequest.getContextPath() + "/img/updateState.png" ) + "\" />\n");
    		htmlStream.append("	</form>\n");
    		htmlStream.append("	</div>\n");
			htmlStream.append("<script type='text/javascript'>\n");
			htmlStream.append("	document.getElementById('viewOnlyTestDocument').style.display='inline';\n");
			htmlStream.append("</script>\n");
		}
		return htmlStream;
	}

	private void addItemForJSTree(StringBuffer htmlStream, LowFunctionality folder, boolean isRoot) {
		
		String nameLabel = folder.getName();
		String name = PortletUtilities.getMessage(nameLabel, "messages");
		Integer idFolder = folder.getId();
		Integer parentId = folder.getParentId();
		
		if (isRoot) {
			htmlStream.append("	treeExecObj.add(" + idFolder + ", " + dTreeRootId + ",'" + name + "', '', '', '', '', '', 'true');\n");
		} else {
			if (ObjectsAccessVerifier.canTest(idFolder, profile) || ObjectsAccessVerifier.canExec(idFolder, profile)) {
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				String imgFolderOp = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolderopen.gif");
				htmlStream.append("	treeExecObj.add(" + idFolder + ", " + parentId + ",'" + name + "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + "', '', '');\n");
				List objects = folder.getBiObjects();
				for (Iterator it = objects.iterator(); it.hasNext(); ) {
					BIObject obj = (BIObject) it.next();
					Integer idObj = obj.getId();
					String stateObj = obj.getStateCode();
					Integer visibleObj = obj.getVisible();
					//insert the correct image for each BI Object type
					String biObjType = obj.getBiObjectTypeCode();
					String imgUrl = "/img/objecticon_"+ biObjType+ ".png";
					String userIcon = PortletUtilities.createPortletURLForResource(httpRequest, imgUrl);
					String biObjState = obj.getStateCode();
					String stateImgUrl = "/img/stateicon_"+ biObjState+ ".png";
					String stateIcon = PortletUtilities.createPortletURLForResource(httpRequest, stateImgUrl);
					//String userIconTest = PortletUtilities.createPortletURLForResource(httpRequest, "/img/objecticontest.gif");
					String onlyTestObjectsView = (String)_serviceRequest.getAttribute("view_only_test_objects");
					PortletURL execUrl = renderResponse.createActionURL();
					execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecuteBIObjectModule.MODULE_PAGE);
					execUrl.setParameter(ObjectsTreeConstants.OBJECT_ID, idObj.toString());
					execUrl.setParameter(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
				            "addItemForJSTree ", obj.getName() + ": " + stateObj + " - " + visibleObj);
					if (visibleObj != null && visibleObj.intValue() == 0 && (stateObj.equalsIgnoreCase("REL") || stateObj.equalsIgnoreCase("TEST"))) {
						SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
					            "addItemForJSTree ", "NOT visible " + obj.getName());
					} else {
						SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "ExecTreeHtmlGenerator", 
					            "addItemForJSTree ", "VISIBLE " + obj.getName());
						if (ObjectsAccessVerifier.canTest(stateObj, idFolder, profile)) {
							thereIsOneOrMoreObjectsInTestState = true;
							execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.TESTER_ACTOR);
							htmlStream.append("	treeExecObj.add(" + dTreeObjects-- + ", " + idFolder + ",'<img src=\\'" + stateIcon + "\\' /> " + obj.getName() + "', '" + execUrl.toString() + "', '', '', '" + userIcon + "', '', '', '' );\n");
						} else if(!"true".equalsIgnoreCase(onlyTestObjectsView) && ObjectsAccessVerifier.canExec(stateObj, idFolder, profile)) {
							execUrl.setParameter(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
							htmlStream.append("	treeExecObj.add(" + dTreeObjects-- + ", " + idFolder + ",'<img src=\\'" + stateIcon + "\\' /> " + obj.getName() + "', '" + execUrl.toString() + "', '', '', '" + userIcon + "', '', '', '' );\n");
						}
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
