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

import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.services.modules.ExecutionWorkspaceModule;
import it.eng.spagobi.services.modules.TreeObjectsModule;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

public class TitleBarHtmlGenerator implements ITreeHtmlGenerator {

	protected RenderResponse renderResponse = null;
	protected RenderRequest renderRequest = null;
	protected HttpServletRequest httpRequest = null;	
	
	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		httpRequest = httpReq;
		renderResponse =(RenderResponse)httpRequest.getAttribute("javax.portlet.response");
		renderRequest = (RenderRequest)httpRequest.getAttribute("javax.portlet.request");	
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("				<div class='UITabs'>\n");
		htmlStream.append("					<div class='first-tab-level' >\n");
		//htmlStream.append("						<div style='overflow: hidden;'>\n");
		Iterator it = objectsList.iterator();
		while (it.hasNext()) {
			LowFunctionality folder = (LowFunctionality) it.next();
			String linkClass = "tab";
			if (folder.getPath().equals(initialPath)) linkClass = "tab selected";
			htmlStream.append("						<div class='" + linkClass + "'>\n");
			PortletURL changeFolderUrl = renderResponse.createActionURL();
			changeFolderUrl.setParameter(ObjectsTreeConstants.PAGE, ExecutionWorkspaceModule.MODULE_PAGE);
			changeFolderUrl.setParameter(TreeObjectsModule.PATH_SUBTREE, folder.getPath());
			htmlStream.append("							<a href='" + changeFolderUrl.toString() + "'>\n");
			htmlStream.append("								" + folder.getName() + "\n");
			htmlStream.append("							</a>\n");
			htmlStream.append("						</div>\n");
		}
		htmlStream.append("");
		//htmlStream.append("						</div>");
		htmlStream.append("					</div>");
		htmlStream.append("				</div>");
		return htmlStream;
	}

	public StringBuffer makeTree(List objectsList, HttpServletRequest httpRequest, String initialPath, String treename) {
		return makeTree(objectsList, httpRequest, initialPath);
	}

}
