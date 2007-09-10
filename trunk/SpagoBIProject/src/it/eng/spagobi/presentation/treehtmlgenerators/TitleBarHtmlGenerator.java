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
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.modules.ExecutionWorkspaceModule;
import it.eng.spagobi.services.modules.TreeObjectsModule;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.urls.IUrlBuilder;
import it.eng.spagobi.utilities.urls.UrlBuilderFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TitleBarHtmlGenerator implements ITreeHtmlGenerator {

	protected HttpServletRequest httpRequest = null;	
	
	public StringBuffer makeAccessibleTree(List objectsList, HttpServletRequest httpRequest, String initialPath) {
		// TODO Auto-generated method stub
		return null;
	}

	public StringBuffer makeTree(List objectsList, HttpServletRequest httpReq, String initialPath) {
		httpRequest = httpReq;	
		IUrlBuilder urlBuilder = UrlBuilderFactory.getUrlBuilder();
		StringBuffer htmlStream = new StringBuffer();
		htmlStream.append("				<div class='UITabs'>\n");
		htmlStream.append("					<div class='first-tab-level' >\n");
		Iterator it = objectsList.iterator();
		while (it.hasNext()) {
			LowFunctionality folder = (LowFunctionality) it.next();
			String linkClass = "tab";
			if (folder.getPath().equals(initialPath)) linkClass = "tab selected";
			htmlStream.append("						<div class='" + linkClass + "'>\n");
			Map changeFolderUrlPars = new HashMap();
			changeFolderUrlPars.put(ObjectsTreeConstants.PAGE, ExecutionWorkspaceModule.MODULE_PAGE);
			changeFolderUrlPars.put(TreeObjectsModule.PATH_SUBTREE, folder.getPath());
			if(ChannelUtilities.isWebRunning()) {
				changeFolderUrlPars.put(SpagoBIConstants.WEBMODE, "TRUE");
			}
			String changeFolderUrl = urlBuilder.getUrl(httpRequest, changeFolderUrlPars);
			htmlStream.append("							<a href='" + changeFolderUrl + "'>\n");
			htmlStream.append("								" + folder.getName() + "\n");
			htmlStream.append("							</a>\n");
			htmlStream.append("						</div>\n");
		}
		htmlStream.append("");
		htmlStream.append("					</div>");
		htmlStream.append("				</div>");
		return htmlStream;
	}

	public StringBuffer makeTree(List objectsList, HttpServletRequest httpRequest, String initialPath, String treename) {
		return makeTree(objectsList, httpRequest, initialPath);
	}

}
