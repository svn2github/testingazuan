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

import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.services.modules.ExecutionWorkspaceModule;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

public class YuiSlideShowMenuHtmlGenerator implements ITreeHtmlGenerator {

	RenderResponse renderResponse = null;
	RenderRequest renderRequest = null;
	HttpServletRequest httpRequest = null;
	private String baseFolderPath = null;
	
	public StringBuffer makeAccessibleTree(List objectsList,
			HttpServletRequest httpRequest, String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public StringBuffer makeTree(List objectsList,
			HttpServletRequest httpRequest, String initialPath, String treename) {
		return makeTree(objectsList, httpRequest, initialPath);
	}

	
	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		
		httpRequest = httpReq;
		baseFolderPath = initialPath;
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");	
		StringBuffer htmlStream = new StringBuffer();
		
		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/reset-fonts-grids/reset-fonts-grids.css" )+"' type='text/css' />");
		htmlStream.append("<LINK rel='StyleSheet' href='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/menu/assets/menu.css" )+"' type='text/css' />");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/yahoo/yahoo.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/event/event.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/dom/dom.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/animation/animation.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/container/container_core.js" )+"'></SCRIPT>");
		htmlStream.append("<SCRIPT language='JavaScript' src='"+renderResponse.encodeURL(renderRequest.getContextPath() + "/js/yui/build/menu/menu.js" )+"'></SCRIPT>");
		
		htmlStream.append("<script type='text/javascript'>										\n");
		htmlStream.append("		onMenuReady = function() { 										\n");
		htmlStream.append("    		var oMenu = new YAHOO.widget.Menu( 							\n");
		htmlStream.append("         	\"biobjectsmenu\", 										\n"); 
		htmlStream.append("           	{ 														\n");
		htmlStream.append("            		position:\"static\",								\n");
		htmlStream.append("             	hidedelay:750,										\n");
		htmlStream.append("             	lazyload:true,										\n");
		htmlStream.append("             	effect:{  		     								\n");
		htmlStream.append("           			effect:YAHOO.widget.ContainerEffect.FADE,		\n");
		htmlStream.append("            			duration:0.25 									\n");
		htmlStream.append("           		}					 								\n");
		htmlStream.append("           	} 														\n");
		htmlStream.append("     	);							 								\n");
		htmlStream.append("     	oMenu.render();												\n");
		htmlStream.append("     };							 									\n");
		htmlStream.append("     YAHOO.util.Event.onContentReady(\"biobjectsmenu\", onMenuReady); \n");
		htmlStream.append("</script>															\n");
		
		
		
		htmlStream.append("<div style=\"width:200px;\" id=\"biobjectsmenu\" class=\"yuimenu\">	\n");
		htmlStream.append("		<div class=\"bd\">	\n");
		htmlStream.append("			<ul class=\"first-of-type\">	\n");
		
		LowFunctionality root = findRoot(objectsList); 
		List roots = findChilds(objectsList, root);
		addMenuItemHtmlCode(roots, objectsList, htmlStream);
		htmlStream.append("			</ul>							\n");
		htmlStream.append("		</div>	\n");
		htmlStream.append("</div>	\n");
		return htmlStream;
	}

	
	private void generateSubMenu(LowFunctionality folder, List objectsList, StringBuffer htmlStream) {
		List childs = findChilds(objectsList, folder);
		if(childs.isEmpty()) {
			return;
		}
		htmlStream.append("<div id=\""+folder.getId()+"\" class=\"yuimenu\">	\n");
		htmlStream.append("		<div class=\"bd\">	\n");
		htmlStream.append("			<ul class=\"first-of-type\">	\n");
		addMenuItemHtmlCode(childs, objectsList, htmlStream);
		htmlStream.append("			</ul>							\n");
		htmlStream.append("		</div>	\n");
		htmlStream.append("</div>	\n");           
		
	}
	
	
	private void addMenuItemHtmlCode(List menuobjs, List objectsList, StringBuffer htmlStream) {
		Iterator iterChilds = menuobjs.iterator();
		while(iterChilds.hasNext()) {
			Object obj = iterChilds.next();
			if(obj instanceof LowFunctionality) {
				LowFunctionality folderchild = (LowFunctionality)obj;
				String imgFolder = PortletUtilities.createPortletURLForResource(httpRequest, "/img/treefolder.gif");
				htmlStream.append("			<li class=\"yuimenuitem\">	\n");
				htmlStream.append("				<img style='clear:left;border:0px;margin:0px;' width='15' height='15' src=\""+imgFolder+"\" /> \n");
				htmlStream.append("				"+folderchild.getName()+"\n");
				generateSubMenu(folderchild, objectsList, htmlStream);
				htmlStream.append("			</li>	\n");
			} else if (obj instanceof BIObject) {
				BIObject biobj = (BIObject)obj;
				String execUrl = getExecutionLink(biobj);
				htmlStream.append("			<li class=\"yuimenuitem\">	\n");
				String biObjType = biobj.getBiObjectTypeCode();
				String imgUrl = "/img/objecticon_"+ biObjType+ ".png";
				String userIcon = PortletUtilities.createPortletURLForResource(httpRequest, imgUrl);
				//htmlStream.append("				<img width='15' height='15' src=\""+userIcon+"\" /> \n");
				htmlStream.append("				<a href=\""+execUrl+"\"><img width='15' height='15' src=\""+userIcon+"\" />"+biobj.getName()+"</a>	\n");
				htmlStream.append("			</li>	\n");
			}	
		}
	}
	
	
	
	private String getExecutionLink(BIObject biobj) {
		PortletURL execUrl = renderResponse.createActionURL();
		execUrl.setParameter(ObjectsTreeConstants.PAGE, ExecutionWorkspaceModule.MODULE_PAGE);
		execUrl.setParameter(ObjectsTreeConstants.OBJECT_LABEL, biobj.getLabel());
		execUrl.setParameter(TreeObjectsModule.PATH_SUBTREE, baseFolderPath);
		return execUrl.toString();
	}
	
	
	private List findChilds(List objectsList, LowFunctionality father) {
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
	
	
	private LowFunctionality findRoot(List objectsList) {
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
