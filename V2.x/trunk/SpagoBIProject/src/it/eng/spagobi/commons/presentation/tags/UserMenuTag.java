/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.presentation.tags;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.util.JavaScript;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class UserMenuTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserMenuTag.class);
	
	protected HttpServletRequest httpRequest = null;
	protected IMessageBuilder msgBuilder = null;
	protected RequestContainer requestContainer = null;
	protected IEngUserProfile userProfile = null;
	
	public int doStartTag() throws JspException {
		logger.debug("IN");
		try {
			httpRequest = (HttpServletRequest) pageContext.getRequest();
			msgBuilder = MessageBuilderFactory.getMessageBuilder();
			requestContainer = RequestContainerAccess.getRequestContainer(httpRequest);
			SessionContainer spagoSession = requestContainer.getSessionContainer();
			SessionContainer spagoPermSession = spagoSession.getPermanentContainer();
			userProfile = (IEngUserProfile)spagoPermSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			StringBuffer htmlStream = new StringBuffer();
			makeMenu(htmlStream);
			pageContext.getOut().print(htmlStream);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			logger.debug("OUT");
		}
		return SKIP_BODY;
		
	}
	
	private void makeResourcesMenu(StringBuffer htmlStream) throws EMFInternalError {
		boolean canSeeEngines = userProfile.isAbleToExecuteAction(SpagoBIConstants.ENGINES_MANAGEMENT);
		boolean canSeeDatasources = userProfile.isAbleToExecuteAction(SpagoBIConstants.DATASOURCE_MANAGEMENT);
		boolean canSeeDatasets = userProfile.isAbleToExecuteAction(SpagoBIConstants.DATASET_MANAGEMENT);
		if (canSeeEngines || canSeeDatasources || canSeeDatasets) {
			htmlStream.append("\n var reourcesMenu = new Ext.menu.Menu({ ");
			htmlStream.append("\n id: 'reourcesMenu', ");
			htmlStream.append("\n items: [");
			if (canSeeEngines) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.ENGINES_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeDatasources) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.DATASOURCE_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeDatasets) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.DATASET_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (htmlStream.charAt(htmlStream.length() - 1) == ',') {
				htmlStream.deleteCharAt(htmlStream.length() - 1);
			}
			htmlStream.append("\n ]");
			htmlStream.append("\n });");
			htmlStream.append("\n tb.add(");
			htmlStream.append("\n 	new Ext.Toolbar.MenuButton({");
			htmlStream.append("\n 		text: '" + msgBuilder.getMessage("menu.Resources", httpRequest) + "',");
			htmlStream.append("\n 		cls: 'x-btn-text-icon bmenu',");
			htmlStream.append("\n 		menu: reourcesMenu");
			htmlStream.append("\n 	})");
			htmlStream.append("\n );");
//			if (canSeeEngines) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.ENGINES_MANAGEMENT);
//			}
//			if (canSeeDatasources) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.DATASOURCE_MANAGEMENT);
//			}
//			if (canSeeDatasets) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.DATASET_MANAGEMENT);
//			}
		}
	}
	
	/*
	private void makeWindowHandler(StringBuffer htmlStream, String functionality) throws EMFInternalError {
		SourceBean config = (SourceBean) ConfigSingleton.getInstance().getFilteredSourceBeanAttribute("MENU.APPLICATION", "functionality", functionality);
		String title = (String) config.getAttribute("title");
		if (title.startsWith("#")){
			title = title.substring(1);
			title = msgBuilder.getMessage(title, httpRequest);
		}
		String url = (String) config.getAttribute("link");
		url = url.replace("${SPAGOBI_CONTEXT}", httpRequest.getContextPath());
		url = url.replace("${SPAGO_ADAPTER_HTTP}", GeneralUtilities.getSpagoAdapterHttpUrl());
		if (url.indexOf("?") != -1) {
			url += "&" + LightNavigationManager.LIGHT_NAVIGATOR_ID + "=" + functionality + "&" + LightNavigationManager.LIGHT_NAVIGATOR_RESET_INSERT + "=TRUE";
		} else {
			url += "?" + LightNavigationManager.LIGHT_NAVIGATOR_ID+ "=" + functionality + "&" + LightNavigationManager.LIGHT_NAVIGATOR_RESET_INSERT + "=TRUE";
		}
		String width = (String) config.getAttribute("width");
		String height = (String) config.getAttribute("height");
		htmlStream.append("\n var " + functionality + "Window;");
		htmlStream.append("\n function " + functionality + "(){");
		htmlStream.append("\n 	if(!" + functionality + "Window) {");
		htmlStream.append("\n 		" + functionality + "Window = new Ext.Window({");
		htmlStream.append("\n 			id:'" + functionality + "Window',");
		htmlStream.append("\n 			bodyCfg: {");
		htmlStream.append("\n 				tag:'div',");
		htmlStream.append("\n 				cls:'x-panel-body',");
		htmlStream.append("\n 				children:[{");
		htmlStream.append("\n 					tag:'iframe',");
		htmlStream.append("\n       					src: '" + url + "',");
		htmlStream.append("\n       					frameBorder:0,");
		htmlStream.append("\n       					width:'100%',");
		htmlStream.append("\n       					height:'100%',");
		htmlStream.append("\n       					padding:0,");
		htmlStream.append("\n       					margin:0,");
		htmlStream.append("\n       					scrolling:'yes',");
		htmlStream.append("\n       					style: {overflow:'auto'}");
		htmlStream.append("\n  						}]");
		htmlStream.append("\n 			},");
		htmlStream.append("\n 			layout:'fit',");
		htmlStream.append("\n 			collapsible: true,");
		htmlStream.append("\n 			animCollapse: false,");
		htmlStream.append("\n 			maximizable: true,");
		htmlStream.append("\n 			width:" + width + ",");
		htmlStream.append("\n 			height:" + height + ",");
		htmlStream.append("\n 			closeAction:'hide',");
		htmlStream.append("\n 			plain: true,");
		htmlStream.append("\n 			title: '" + title + "'");
		htmlStream.append("\n 		});");
		htmlStream.append("\n 	};");
		htmlStream.append("\n 	" + functionality + "Window.show();");
		htmlStream.append("\n }");
	}
	*/
	
	private void makeAnaliticalModelMenu(StringBuffer htmlStream) throws EMFInternalError {
		boolean canSeeFolders = userProfile.isAbleToExecuteAction(SpagoBIConstants.FUNCTIONALITIES_MANAGEMENT);
		boolean canSeeDocuments = userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN) 
										|| userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV);
		if (canSeeFolders || canSeeDocuments) {
			htmlStream.append("\n var analiticalModelMenu = new Ext.menu.Menu({ ");
			htmlStream.append("\n id: 'analiticalModelMenu', ");
			htmlStream.append("\n items: [");
			if (canSeeFolders) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.FUNCTIONALITIES_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeDocuments) {
				String functionality = userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN) ? 
						SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN : SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV;
				makeSubMenuItem(htmlStream, functionality);
				htmlStream.append("\n ,");
			}
			if (htmlStream.charAt(htmlStream.length() - 1) == ',') {
				htmlStream.deleteCharAt(htmlStream.length() - 1);
			}
			htmlStream.append("\n ]");
			htmlStream.append("\n });");
			htmlStream.append("\n tb.add(");
			htmlStream.append("\n 	new Ext.Toolbar.MenuButton({");
			htmlStream.append("\n 		text: '" + msgBuilder.getMessage("menu.AnaliticalModel", httpRequest) + "',");
			htmlStream.append("\n 		cls: 'x-btn-text-icon bmenu',");
			htmlStream.append("\n 		menu: analiticalModelMenu");
			htmlStream.append("\n 	})");
			htmlStream.append("\n );");
//			if (canSeeFolders) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.FUNCTIONALITIES_MANAGEMENT);
//			}
//			if (canSeeDocuments) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN);
//			}
		}
	}
	
	private void makeBehaviouralModelMenu(StringBuffer htmlStream) throws EMFInternalError {
		boolean canSeeLovs = userProfile.isAbleToExecuteAction(SpagoBIConstants.LOVS_MANAGEMENT)
								|| userProfile.isAbleToExecuteAction(SpagoBIConstants.LOVS_VIEW);
		boolean canSeeChecks = userProfile.isAbleToExecuteAction(SpagoBIConstants.CONTSTRAINT_MANAGEMENT) 
								|| userProfile.isAbleToExecuteAction(SpagoBIConstants.CONTSTRAINT_VIEW);
		boolean canSeeParameters = userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_MANAGEMENT) 
								|| userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_VIEW);
		if (canSeeLovs || canSeeChecks || canSeeParameters) {
			htmlStream.append("\n var behaviouralModel = new Ext.menu.Menu({ ");
			htmlStream.append("\n id: 'behaviouralModel', ");
			htmlStream.append("\n items: [");
			if (canSeeLovs) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.LOVS_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeChecks) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.CONTSTRAINT_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeParameters) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.PARAMETER_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (htmlStream.charAt(htmlStream.length() - 1) == ',') {
				htmlStream.deleteCharAt(htmlStream.length() - 1);
			}
			htmlStream.append("\n ]");
			htmlStream.append("\n });");
			htmlStream.append("\n tb.add(");
			htmlStream.append("\n 	new Ext.Toolbar.MenuButton({");
			htmlStream.append("\n 		text: '" + msgBuilder.getMessage("menu.BehaviouralModelMenu", httpRequest) + "',");
			htmlStream.append("\n 		cls: 'x-btn-text-icon bmenu',");
			htmlStream.append("\n 		menu: behaviouralModel");
			htmlStream.append("\n 	})");
			htmlStream.append("\n );");
//			if (canSeeLovs) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.LOVS_MANAGEMENT);
//			}
//			if (canSeeChecks) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.CONTSTRAINT_MANAGEMENT);
//			}
//			if (canSeeParameters) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.PARAMETER_MANAGEMENT);
//			}
		}
	}
	
	private void makeToolsMenu(StringBuffer htmlStream) throws EMFInternalError {
		boolean canSeeImportExport = userProfile.isAbleToExecuteAction(SpagoBIConstants.IMPORT_EXPORT_MANAGEMENT);
		boolean canSeeScheduler = userProfile.isAbleToExecuteAction(SpagoBIConstants.SCHEDULER_MANAGEMENT);
		boolean canSeeRoles = userProfile.isAbleToExecuteAction(SpagoBIConstants.SYNCRONIZE_ROLES_MANAGEMENT);
		if (canSeeImportExport || canSeeScheduler || canSeeRoles) {
			htmlStream.append("\n var toolsMenu = new Ext.menu.Menu({ ");
			htmlStream.append("\n id: 'toolsMenu', ");
			htmlStream.append("\n items: [");
			if (canSeeImportExport) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.IMPORT_EXPORT_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeScheduler) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.SCHEDULER_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (canSeeRoles) {
				makeSubMenuItem(htmlStream, SpagoBIConstants.SYNCRONIZE_ROLES_MANAGEMENT);
				htmlStream.append("\n ,");
			}
			if (htmlStream.charAt(htmlStream.length() - 1) == ',') {
				htmlStream.deleteCharAt(htmlStream.length() - 1);
			}
			htmlStream.append("\n ]");
			htmlStream.append("\n });");
			htmlStream.append("\n tb.add(");
			htmlStream.append("\n 	new Ext.Toolbar.MenuButton({");
			htmlStream.append("\n 		text: '" + msgBuilder.getMessage("menu.ToolsMenu", httpRequest) + "',");
			htmlStream.append("\n 		cls: 'x-btn-text-icon bmenu',");
			htmlStream.append("\n 		menu: toolsMenu");
			htmlStream.append("\n 	})");
			htmlStream.append("\n );");
//			if (canSeeImportExport) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.IMPORT_EXPORT_MANAGEMENT);
//			}
//			if (canSeeScheduler) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.SCHEDULER_MANAGEMENT);
//			}
//			if (canSeeRoles) {
//				makeWindowHandler(htmlStream, SpagoBIConstants.SYNCRONIZE_ROLES_MANAGEMENT);
//			}
		}
	}
	
	private void makeMapCatalogueMenu(StringBuffer htmlStream) throws EMFInternalError {
		boolean canSeeMapCatalogue = userProfile.isAbleToExecuteAction(SpagoBIConstants.MAPCATALOGUE_MANAGEMENT);
		if (canSeeMapCatalogue) {
			htmlStream.append("\n var mapCatalogueMenu = new Ext.menu.Menu({ ");
			htmlStream.append("\n id: 'mapCatalogueMenu', ");
			htmlStream.append("\n items: [");
			makeSubMenuItem(htmlStream, SpagoBIConstants.MAPCATALOGUE_MANAGEMENT);
			htmlStream.append("\n ,");
			makeSubMenuItem(htmlStream, SpagoBIConstants.MAP_FEATURES_MANAGEMENT);
			htmlStream.append("\n ]");
			htmlStream.append("\n });");
			htmlStream.append("\n tb.add(");
			htmlStream.append("\n 	new Ext.Toolbar.MenuButton({");
			htmlStream.append("\n 		text: '" + msgBuilder.getMessage("menu.MapCatalogueMenu", httpRequest) + "',");
			htmlStream.append("\n 		cls: 'x-btn-text-icon bmenu',");
			htmlStream.append("\n 		menu: mapCatalogueMenu");
			htmlStream.append("\n 	})");
			htmlStream.append("\n );");
//			makeWindowHandler(htmlStream, SpagoBIConstants.MAPCATALOGUE_MANAGEMENT);
//			makeWindowHandler(htmlStream, SpagoBIConstants.MAP_FEATURES_MANAGEMENT);
		}
	}
	
	/*
	private void makeWorkspaceButton(StringBuffer htmlStream) throws EMFInternalError {
		htmlStream.append("\n tb.add(");
		makeMenuItem(htmlStream, msgBuilder.getMessage("menu.WorkExec", httpRequest), 
				httpRequest.getContextPath() + "/img/wapp/workspace16.png", SpagoBIConstants.WORKSPACE_MANAGEMENT);
		htmlStream.append("\n );");
		makeWindowHandler(htmlStream, SpagoBIConstants.WORKSPACE_MANAGEMENT, msgBuilder.getMessage("menu.WorkExec", httpRequest), 
				httpRequest.getContextPath() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?PAGE=ExecutionWorkspacePage&amp;WEBMODE=TRUE", "650", "450");
	}
	*/
	
	/*
	private void makeMenuConfigurationButton(StringBuffer htmlStream) throws EMFInternalError {
		boolean canSeeMenuConfiguration = userProfile.isAbleToExecuteAction(SpagoBIConstants.MENU_MANAGEMENT);
		if (canSeeMenuConfiguration) {
			htmlStream.append("\n tb.add(");
			makeMenuItem(htmlStream, msgBuilder.getMessage("menu.menuconfiguration", httpRequest), 
					httpRequest.getContextPath() + "/img/wapp/menu16.png", SpagoBIConstants.MENU_MANAGEMENT);
			htmlStream.append("\n );");
			makeWindowHandler(htmlStream, SpagoBIConstants.MENU_MANAGEMENT, msgBuilder.getMessage("menu.menuconfiguration", httpRequest), 
					httpRequest.getContextPath() + GeneralUtilities.getSpagoAdapterHttpUrl() + "?PAGE=MenuConfigurationPage", "650", "450");
		}
	}
	*/
	
	private void makeMenu(StringBuffer htmlStream) throws EMFInternalError {
		makeResourcesMenu(htmlStream);
		makeAnaliticalModelMenu(htmlStream);
		makeBehaviouralModelMenu(htmlStream);
		makeToolsMenu(htmlStream);
		makeMapCatalogueMenu(htmlStream);
		//makeMenuConfigurationButton(htmlStream);
		//makeWorkspaceButton(htmlStream);
	}
	
	private void makeSubMenuItem(StringBuffer htmlStream, String functionality) {
		SourceBean config = (SourceBean) ConfigSingleton.getInstance().getFilteredSourceBeanAttribute("MENU.APPLICATION", "functionality", functionality);
		htmlStream.append("\n new Ext.menu.Item({");
		htmlStream.append("\n 	id: '" + new Double(Math.random()).toString() + "',");
		String title = (String) config.getAttribute("title");
		if (title.startsWith("#")){
			title = title.substring(1);
			title = msgBuilder.getMessage(title, httpRequest);
		}
		String iconUrl = (String) config.getAttribute("iconUrl");
		iconUrl = iconUrl.replace("${SPAGOBI_CONTEXT}", httpRequest.getContextPath());
		String url = (String) config.getAttribute("link");
		url = url.replace("${SPAGOBI_CONTEXT}", httpRequest.getContextPath());
		url = url.replace("${SPAGO_ADAPTER_HTTP}", GeneralUtilities.getSpagoAdapterHttpUrl());
		if (url.indexOf("?") != -1) {
			url += "&" + LightNavigationManager.LIGHT_NAVIGATOR_ID + "=" + functionality + "&" + LightNavigationManager.LIGHT_NAVIGATOR_RESET_INSERT + "=TRUE";
		} else {
			url += "?" + LightNavigationManager.LIGHT_NAVIGATOR_ID+ "=" + functionality + "&" + LightNavigationManager.LIGHT_NAVIGATOR_RESET_INSERT + "=TRUE";
		}
		htmlStream.append("\n 	text: '" + title + "',");
		htmlStream.append("\n 	icon: '" + iconUrl + "', ");
		htmlStream.append("\n 	href: 'javascript:execDirectUrl(\"" + JavaScript.escape(url) + "\")'");
		//htmlStream.append("\n 	handler: " + functionality);                           
		htmlStream.append("\n })");
	}
	
	/*
	private void makeMenuItem(StringBuffer htmlStream, String text, String icon, String handler) {
		htmlStream.append("\n new Ext.Toolbar.Button({");
		htmlStream.append("\n 	text: '" + text + "',");
		htmlStream.append("\n 	icon: '" + icon + "',");
		htmlStream.append("\n 	cls: 'x-btn-text-icon bmenu',");
		htmlStream.append("\n 	handler: " + handler);
		htmlStream.append("\n })");
	}
	*/
	
}
