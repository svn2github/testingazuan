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
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.ExecuteBIObjectModule;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Contains all methods needed to generate and modify a tree object for Execution.
 * There are methods to generate tree, configure, insert and modify elements.
 */
public class ExecTreeHtmlGenerator implements ITreeHtmlGenerator {
	
	HttpServletRequest httpRequest = null;
	RequestContainer reqCont = null;
	protected IUrlBuilder urlBuilder = null;
	protected IMessageBuilder msgBuilder = null;
	IEngUserProfile profile = null;
	int progrJSTree = 0;
	private SessionContainer sessionContainer = null;
	private boolean thereIsOneOrMoreObjectsInTestState = false;
	protected SourceBean _serviceRequest = null;
	private int dTreeRootId = -100;
	private int dTreeObjects = -1000;
	// the name of the dtree variable, default value is treeExecObj
	private String treeName = "treeExecObj";
	protected String requestIdentity = null;


	
	/**
	 * @see it.eng.spagobi.presentation.treehtmlgenerators.AdminTreeHtmlGenerator#makeJSFunctionForMenu(java.lang.StringBuffer)
	 */
	private void makeJSFunctionForMenu(StringBuffer htmlStream) {
		/* ********* start luca changes *************** */
		htmlStream.append("		function menu" + requestIdentity + "(event, urlExecution, urlEraseDoc, urlEraseFolder, urlAddFolder) {\n");
		htmlStream.append("			divM = document.getElementById('divmenu" + requestIdentity + "');\n");
		htmlStream.append("			divM.innerHTML = '';\n");
		String capExec = msgBuilder.getMessage("SBISet.devObjects.captionExecute", "messages", httpRequest);
		htmlStream.append("			if(urlExecution!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlExecution+'\">"+capExec+"</a></div>';\n");
		String capErase = msgBuilder.getMessage("SBISet.devObjects.captionErase", "messages", httpRequest);
        htmlStream.append("         if(urlEraseDoc!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"javascript:actionConfirm(\\'"+capErase+"\\', \\''+urlEraseDoc+'\\');\">"+capErase+"</a></div>';\n");
        htmlStream.append("         if(urlEraseFolder!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"javascript:actionConfirm(\\'"+capErase+"\\', \\''+urlEraseFolder+'\\');\">"+capErase+"</a></div>';\n");
        htmlStream.append("				showMenu(event, divM);\n");
        String capAddSub = "Add folder";
        htmlStream.append("			if(urlAddFolder!='') divM.innerHTML = divM.innerHTML + '<div onmouseout=\"this.style.backgroundColor=\\'white\\'\"  onmouseover=\"this.style.backgroundColor=\\'#eaf1f9\\'\" ><a class=\"dtreemenulink\" href=\"'+urlAddFolder+'\">"+capAddSub+"</a></div>';\n");
        
        
		htmlStream.append("		}\n");
		
		htmlStream.append("		function linkEmpty() {\n");
		htmlStream.append("		}\n");
		
        String confirmCaption = msgBuilder.getMessage("SBISet.devObjects.confirmCaption", "messages", httpRequest);
        htmlStream.append("     function actionConfirm(message, url){\n");
        htmlStream.append("         if (confirm('" + confirmCaption + " ' + message + '?')){\n");
        htmlStream.append("             location.href = url;\n");
        htmlStream.append("         }\n");
        htmlStream.append("     }\n");
        
        htmlStream.append("     function addSubFolder(idFP){\n");
        htmlStream.append("         alert('Not Implemented');");
        htmlStream.append("     }\n");
        
        htmlStream.append("     function eraseFolder(idF){\n");
        htmlStream.append("         alert('Not Implemented');");
        htmlStream.append("     }\n");
        
        /* ********* end luca changes *************** */
	}
		
	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpRequest, String initialPath, String treename) {
		this.treeName = treename;
		return makeTree(objectsList, httpRequest, initialPath);
	}
	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		
		// identity string for object of the page
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    requestIdentity = uuid.toString();
	    requestIdentity = requestIdentity.replaceAll("-", "");
	    // get spago containers and buildres classes
		httpRequest = httpReq;
		reqCont = ChannelUtilities.getRequestContainer(httpRequest);
		urlBuilder = UrlBuilderFactory.getUrlBuilder();
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		_serviceRequest = reqCont.getServiceRequest();
		sessionContainer = reqCont.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
		// get user profile
        profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("<LINK rel='StyleSheet' href='"+urlBuilder.getResourceLink(httpRequest, "/css/dtree.css" )+"' type='text/css' />");
		//makeConfigurationDtree(htmlStream);
		String nameTree = msgBuilder.getMessage("tree.objectstree.name" ,"messages", httpRequest);
		htmlStream.append("<SCRIPT language='JavaScript' src='"+urlBuilder.getResourceLink(httpRequest, "/js/dtree.js" )+"'></SCRIPT>");
		htmlStream.append("<div id='viewOnlyTestDocument" + requestIdentity + "' style='display:none;'>");
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
		String onlyTestObjectsViewLbl = msgBuilder.getMessage("tree.objectstree.showOnlyTestObject", "messages", httpRequest);
		if ("true".equalsIgnoreCase(onlyTestObjectsView)) checked = "checked='checked'";
		htmlStream.append("			<span class=\"dtree\">" + onlyTestObjectsViewLbl + "</span>\n");
		htmlStream.append("			<input type=\"checkbox\" " + checked + " \n");
		htmlStream.append("				onclick=\"document.getElementById('view_only_test_objects" + requestIdentity + "').checked=this.checked;document.getElementById('objectForm').submit()\" />\n");
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
		htmlStream.append("		<td id='treeExecObjTd" + requestIdentity + "' name='treeExecObjTd" + requestIdentity + "'>&nbsp;</td>");
		htmlStream.append("			<script language=\"JavaScript1.2\">\n");
	   	//htmlStream.append("				var nameTree = 'treeExecObj';\n");
	   	htmlStream.append("				" + treeName + " = new dTree('" + treeName + "', '" + httpRequest.getContextPath() + "');\n");
	   	htmlStream.append("	        	" + treeName + ".add(" + dTreeRootId + ",-1,'"+nameTree+"');\n");
	   	Iterator it = objectsList.iterator();
	   	while (it.hasNext()) {
	   		LowFunctionality folder = (LowFunctionality) it.next();
	   		/* ********* start luca changes *************** */
	   		boolean isRoot = false;
	   		boolean isUserFunct = folder.getPath().startsWith("/"+profile.getUserUniqueIdentifier());
	   		if(!isUserFunct) {
	   			if (initialPath != null) {
		   			if (initialPath.equalsIgnoreCase(folder.getPath())) 
		   				isRoot = true;
		   		} else {
		   			if (folder.getParentId() == null) 
		   				isRoot = true;
		   		}
	   		}
	   		addItemForJSTree(htmlStream, folder, isRoot, isUserFunct);
	   		/* ********* end luca changes ***************** */
	   	}
    	htmlStream.append("				document.getElementById('treeExecObjTd" + requestIdentity + "').innerHTML = " + treeName + ";\n");
    	makeJSFunctionForMenu(htmlStream);	
		htmlStream.append("			</script>\n");
		htmlStream.append("	</tr>");
		htmlStream.append("</table>");
		/* ********* start luca changes *************** */
		htmlStream.append("<SCRIPT language='JavaScript' src='"+urlBuilder.getResourceLink(httpRequest, "/js/contextMenu.js" )+"'></SCRIPT>");
		htmlStream.append("<div id='divmenu" + requestIdentity + "' class='dtreemenu' onmouseout='hideMenu(event, \"divmenu" + requestIdentity + "\");' >");
		htmlStream.append("		menu");
		htmlStream.append("</div>");
		/* ********* end luca changes *************** */
		htmlStream.append("<br/>");
		
		// if there is one or more document in test state diplay the div with id='viewOnlyTestDocument'
		if (thereIsOneOrMoreObjectsInTestState) {
    		
			Map formUrlPars = new HashMap();
			formUrlPars.put("PAGE", "LOGIN_PAGE_SBI_FUNCTIONALITY");
			formUrlPars.put(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_TREE);
			formUrlPars.put(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
			if(ChannelUtilities.isWebRunning()) {
				formUrlPars.put(SpagoBIConstants.WEBMODE, "TRUE");
			}
			String formUrl = urlBuilder.getUrl(httpRequest, formUrlPars);
		
    		String updateTree = msgBuilder.getMessage("tree.objectstree.update", "messages", httpRequest);
    		htmlStream.append("	<div style=\"display:none;\">\n");
    		htmlStream.append("	<form method='POST' action='" + formUrl + "' id ='objectForm' name='objectForm'>\n");
    		htmlStream.append("		<span>" + onlyTestObjectsViewLbl + "</span>\n");
    		htmlStream.append("		<input type=\"checkbox\" name=\"view_only_test_objects\" id=\"view_only_test_objects" + requestIdentity + "\" value=\"true\" " + checked + " />\n");
    		htmlStream.append("		<input type=\"image\" style=\"width:25px;height:25px\" title=\"" + updateTree + "\" alt\"" + updateTree + "\" \n");
    		htmlStream.append("			src=\"" + urlBuilder.getResourceLink(httpRequest, "/img/updateState.png" ) + "\" />\n");
    		htmlStream.append("	</form>\n");
    		htmlStream.append("	</div>\n");
			htmlStream.append("<script type='text/javascript'>\n");
			htmlStream.append("	document.getElementById('viewOnlyTestDocument" + requestIdentity + "').style.display='inline';\n");
			htmlStream.append("</script>\n");
		}
		return htmlStream;
	}

	private void addItemForJSTree(StringBuffer htmlStream, LowFunctionality folder, boolean isRoot, boolean isUserFunct) {
		
		String nameLabel = folder.getName();
		String name = msgBuilder.getMessage(nameLabel, "messages", httpRequest);
		Integer idFolder = folder.getId();
		Integer parentId = folder.getParentId();
		
		String imgFolder = urlBuilder.getResourceLink(httpRequest, "/img/treefolder.gif");
		String imgFolderOp = urlBuilder.getResourceLink(httpRequest, "/img/treefolderopen.gif");
				
		if (isRoot) {
			htmlStream.append(treeName + ".add(" + idFolder + ", " + dTreeRootId + ",'" + name + "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + "', 'true');\n");
		} 
		/* ********* start luca changes *************** */
		else if (isUserFunct) {
			imgFolder = urlBuilder.getResourceLink(httpRequest, "/img/treefolderuser.gif");
			imgFolderOp = urlBuilder.getResourceLink(httpRequest, "/img/treefolderopenuser.gif");
			htmlStream.append(treeName + ".add(" + idFolder + ", " + dTreeRootId + ",'" + name + "', 'javascript:linkEmpty()', '', '', '" + imgFolder + "', '" + imgFolderOp + "', 'false', 'menu" + requestIdentity + "(event, \\'\\', \\'\\', \\'javascript:eraseFolder("+idFolder+")\\', \\'javascript:addSubFolder("+idFolder+")\\')');\n");
			List objects = folder.getBiObjects();
			for (Iterator it = objects.iterator(); it.hasNext(); ) {
				BIObject obj = (BIObject) it.next();
				Integer idObj = obj.getId();
				String stateObj = obj.getStateCode();
				Integer visibleObj = obj.getVisible();
				if( !stateObj.equalsIgnoreCase("REL") || visibleObj.intValue() != 0) {
					continue;
				}
				//insert the correct image for each BI Object type
				String biObjType = obj.getBiObjectTypeCode();
				String imgUrl = "/img/objecticon_"+ biObjType+ ".png";
				String userIcon = urlBuilder.getResourceLink(httpRequest, imgUrl);
				String biObjState = obj.getStateCode();
				String stateImgUrl = "/img/stateicon_"+ biObjState+ ".png";
				String stateIcon = urlBuilder.getResourceLink(httpRequest, stateImgUrl);
				// create execution link
				Map execUrlPars = new HashMap();
				execUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
				execUrlPars.put(ObjectsTreeConstants.OBJECT_ID, idObj.toString());
				execUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
				execUrlPars.put(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
				String execUrl = urlBuilder.getUrl(httpRequest, execUrlPars);
				//htmlStream.append(treeName + ".add(" + dTreeObjects-- + ", " + idFolder + ",'<img src=\\'" + stateIcon + "\\' /> " + obj.getName() + "', '" + execUrl + "', '', '', '" + userIcon + "', '', '', '' );\n");
				htmlStream.append(treeName + ".add(" + dTreeObjects-- + ", " + idFolder + ",'<img src=\\'" + stateIcon + "\\' /> " + obj.getName() + "', 'javascript:linkEmpty()', '', '', '" + userIcon + "', '', '', 'menu" + requestIdentity + "(event, \\'"+execUrl+"\\', \\'"+createEraseDocumentLink(idObj)+"\\', \\'\\', \\'\\')' );\n");
			}
		} 
		/* ********* end luca changes ***************** */
		else {
			if (ObjectsAccessVerifier.canTest(idFolder, profile) || ObjectsAccessVerifier.canExec(idFolder, profile)) {
				htmlStream.append("	" + treeName + ".add(" + idFolder + ", " + parentId + ",'" + name + "', '', '', '', '" + imgFolder + "', '" + imgFolderOp + "', '', '');\n");
				List objects = folder.getBiObjects();
				for (Iterator it = objects.iterator(); it.hasNext(); ) {
					BIObject obj = (BIObject) it.next();
					Integer idObj = obj.getId();
					String stateObj = obj.getStateCode();
					Integer visibleObj = obj.getVisible();
					//insert the correct image for each BI Object type
					String biObjType = obj.getBiObjectTypeCode();
					String imgUrl = "/img/objecticon_"+ biObjType+ ".png";
					String userIcon = urlBuilder.getResourceLink(httpRequest, imgUrl);
					String biObjState = obj.getStateCode();
					String stateImgUrl = "/img/stateicon_"+ biObjState+ ".png";
					String stateIcon = urlBuilder.getResourceLink(httpRequest, stateImgUrl);
					String onlyTestObjectsView = (String)_serviceRequest.getAttribute("view_only_test_objects");
					
					
					Map execUrlPars = new HashMap();
					execUrlPars.put("PAGE", ExecuteBIObjectModule.MODULE_PAGE);
					execUrlPars.put(ObjectsTreeConstants.OBJECT_ID, idObj.toString());
					execUrlPars.put(SpagoBIConstants.MESSAGEDET, ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE);
					
					
					
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
							execUrlPars.put(SpagoBIConstants.ACTOR, SpagoBIConstants.TESTER_ACTOR);
							String execUrl = urlBuilder.getUrl(httpRequest, execUrlPars);
							htmlStream.append(treeName + ".add(" + dTreeObjects-- + ", " + idFolder + ",'<img src=\\'" + stateIcon + "\\' /> " + obj.getName() + "', '" + execUrl + "', '', '', '" + userIcon + "', '', '', '' );\n");
						} else if(!"true".equalsIgnoreCase(onlyTestObjectsView) && ObjectsAccessVerifier.canExec(stateObj, idFolder, profile)) {
							execUrlPars.put(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
							String execUrl = urlBuilder.getUrl(httpRequest, execUrlPars);
							htmlStream.append(treeName + ".add(" + dTreeObjects-- + ", " + idFolder + ",'<img src=\\'" + stateIcon + "\\' /> " + obj.getName() + "', '" + execUrl + "', '', '', '" + userIcon + "', '', '', '' );\n");
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
	
	private String createEraseDocumentLink(Integer iddoc) {
		HashMap execUrlParMap = new HashMap();
		execUrlParMap.put(ObjectsTreeConstants.PAGE, "MYFOLDERMANAGEMENTPAGE");
		execUrlParMap.put("TASK", "ERASE_DOCUMENT");
		execUrlParMap.put(ObjectsTreeConstants.OBJECT_ID, iddoc.toString());
		String execUrl = urlBuilder.getUrl(httpRequest, execUrlParMap);
		return execUrl;
	}
	
}
