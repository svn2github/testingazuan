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

import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.ExecutionWorkspaceModule;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.messages.IMessageBuilder;
import it.eng.spagobi.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class NestedMenuHtmlGenerator implements ITreeHtmlGenerator {

	HttpServletRequest httpRequest = null;
	private String baseFolderPath = null;
	List objectsList = new ArrayList();
	protected IUrlBuilder urlBuilder = null;
	protected IMessageBuilder msgBuilder = null;
	
	public StringBuffer makeAccessibleTree(List objectsList,
			HttpServletRequest httpRequest, String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public StringBuffer makeTree(List objectsList,
			HttpServletRequest httpRequest, String initialPath, String treename) {
		return makeTree(objectsList, httpRequest, initialPath);
	}

	
	public StringBuffer makeTree(List objsList, HttpServletRequest httpReq, String initialPath) {
		
		objectsList = objsList;
		httpRequest = httpReq;
		baseFolderPath = initialPath;
		urlBuilder = UrlBuilderFactory.getUrlBuilder();
		msgBuilder = MessageBuilderFactory.getMessageBuilder();
		StringBuffer stream = new StringBuffer();
		StringBuffer htmlStream = new StringBuffer();
		StringBuffer jsStream = new StringBuffer();
		htmlStream.append("<SCRIPT language='JavaScript' src='"+urlBuilder.getResourceLink(httpRequest, "/js/menu.js" )+"'></SCRIPT>");
		LowFunctionality root = findRoot(); 
		List childs = findChilds(root);
		if(!childs.isEmpty()) {
			generateMenu(root, childs, htmlStream, true, 0);
		}
		addJsCookiesFuncts(jsStream);
		stream.append(htmlStream);
		stream.append(jsStream);
		return stream;
	}

	
    private void addJsCookiesFuncts(StringBuffer jsStream) {
    	String script = "<script>" +
    						"try{" +
    						"  SbiJsInitializer.openmenusNM = openmenusNMFunct;" +
    						"} catch (err) {" +
    						"  	/* ignore */ " +
    						"}" +
    					"</script>";
    	jsStream.append(script);
    }
	
	
	private void generateMenu(LowFunctionality folder, List childs, StringBuffer htmlStream, boolean isroot, int level) {
		if(!isroot) {
			String cssclass = "";
			for(int i=0; i<=level; i++){
				cssclass = cssclass + "menucontainer"+i+" ";
			}
			//htmlStream.append("<div id='ul_"+folder.getId()+"' style='display:none;'>		\n");
			htmlStream.append("<div id='ul_"+folder.getId()+"' class='menuinvisiblebox' >		\n");
			htmlStream.append("		<div class='"+cssclass+"'  >		\n");
			addMenuItemHtmlCode(childs, htmlStream, level);
			htmlStream.append("		</div>		\n");
			htmlStream.append("</div>		\n");
		} else {
			htmlStream.append("<div class='menutitle'>		\n");
			htmlStream.append(	folder.getName());	
			htmlStream.append("</div>		\n");
			htmlStream.append("<div class='menucontainer"+level+"' id='ul_"+folder.getId()+"' >		\n");
			addMenuItemHtmlCode(childs, htmlStream, level);
			htmlStream.append("</div>		\n");
		}	
	}
	
	
	private void addMenuItemHtmlCode(List menuobjs, StringBuffer htmlStream, int level) {
		Iterator iterChilds = menuobjs.iterator();
		while(iterChilds.hasNext()) {
			Object obj = iterChilds.next();
			if(obj instanceof LowFunctionality) {
				LowFunctionality folderchild = (LowFunctionality)obj;
				// find child of the folder 
				String onclick = "";
				List folderchilds = findChilds(folderchild);
				if(!folderchilds.isEmpty()){
					onclick = " onclick=\"openmenuNM('ul_"+folderchild.getId()+"')\" ";
				}
				htmlStream.append("	<div class='menuitem menuitemfolder' "+onclick+" >\n");
				htmlStream.append("		<div class='menuitemleft menuitemleftfolder'></div>\n");
				htmlStream.append("		<div class='menuitemcenter'><a class='menuitemlink' href='javascript:void(0)' >"+folderchild.getName()+"</a></div>\n");
				htmlStream.append("		<div class='menuitemright'></div>\n");
				htmlStream.append("	</div>	\n");
				if(!folderchilds.isEmpty()){
					generateMenu(folderchild, folderchilds, htmlStream, false, level+1);
				}
			} else if (obj instanceof BIObject) {
				BIObject biobj = (BIObject)obj;
				String execUrl = getExecutionLink(biobj);
				String biObjType = biobj.getBiObjectTypeCode();
				//htmlStream.append("	<div class='menuitem menuitem"+biObjType+"' >\n");
				htmlStream.append("	<div class='menuitem' >\n");
				htmlStream.append("		<div class='menuitemleft menuitemleft"+biObjType+"'></div>\n");
				String cls = "menuitemlink";
				ResponseContainer responseContainer = ChannelUtilities.getResponseContainer(httpRequest);
				SourceBean moduleResponse = (SourceBean) responseContainer.getServiceResponse().getAttribute("ExecutionWorkspaceModule");
				String currentObjLabel = null;
				if (moduleResponse != null) {
					currentObjLabel = (String) moduleResponse.getAttribute(ObjectsTreeConstants.OBJECT_LABEL);
				}
				if (currentObjLabel != null && currentObjLabel.equals(biobj.getLabel())) {
					cls = "menuitemlink-selected";
				}
				htmlStream.append("		<div class='menuitemcenter'><a class='" + cls + "' href=\""+execUrl+"\">"+biobj.getName()+"</a></div>\n");
				htmlStream.append("		<div class='menuitemright'></div>\n");
				htmlStream.append("	</div>	\n");
			}	
		}
	}
	
	
	
	private String getExecutionLink(BIObject biobj) {
		Map execUrlPars = new HashMap();
		execUrlPars.put(ObjectsTreeConstants.PAGE, ExecutionWorkspaceModule.MODULE_PAGE);
		execUrlPars.put(ObjectsTreeConstants.OBJECT_LABEL, biobj.getLabel());
		execUrlPars.put(TreeObjectsModule.PATH_SUBTREE, baseFolderPath);
		if(ChannelUtilities.isWebRunning()) {
			execUrlPars.put(SpagoBIConstants.WEBMODE, "TRUE");
		}
		String execUrl = urlBuilder.getUrl(httpRequest, execUrlPars);
		return execUrl;
	}
	
	
	private List findChilds(LowFunctionality father) {
		List roots = new ArrayList();
		// add biobjects
		roots.addAll(father.getBiObjects());
		// add child folders
		Iterator iterFolder1 = objectsList.iterator();
		while(iterFolder1.hasNext()){
			LowFunctionality folder1 = (LowFunctionality)iterFolder1.next();
			Integer parId = folder1.getParentId();
			if(parId.equals(father.getId())) {
				roots.add(folder1);
			}
		}
		return roots;
	}
	
	
	private LowFunctionality findRoot() {
		LowFunctionality root = null;
		Iterator iterFolder1 = objectsList.iterator();
		while(iterFolder1.hasNext()){
			LowFunctionality folder1 = (LowFunctionality)iterFolder1.next();
			Integer parId = folder1.getParentId();
			Iterator iterFolder2 = objectsList.iterator();
			boolean hasFather = false;
			while(iterFolder2.hasNext()) {
				LowFunctionality folder2 = (LowFunctionality)iterFolder2.next();
				if(folder2.getId().equals(parId)){
					hasFather = true;
				}
			}
			if(!hasFather) {
				root = folder1;
				break;
			}
		}
		return root;
	}
	
	
	private void makeJSFunctionForMenu(StringBuffer htmlStream) {
		htmlStream.append("		function linkEmpty() {\n");
		htmlStream.append("		}\n");
	}
	
}
