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
package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.DelegatedBasicListService;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ListBIObjectsModule extends AbstractBasicListModule {
	
	protected IEngUserProfile profile = null; 
	protected String initialPath = null;
	
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		
		RequestContainer requestContainer = this.getRequestContainer();	
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
		profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
        initialPath = (String) request.getAttribute(TreeObjectsModule.PATH_SUBTREE);
		
		SourceBean moduleConfig = null;
		if (SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor)) {
			moduleConfig = makeAdminListConfiguration();
		} else if (SpagoBIConstants.DEV_ACTOR.equalsIgnoreCase(actor)) {
			moduleConfig = makeDevListConfiguration();
		} else if (SpagoBIConstants.USER_ACTOR.equalsIgnoreCase(actor)) {
			moduleConfig = makeUserListConfiguration();
		}
		response.setAttribute(moduleConfig);
		
		PaginatorIFace paginator = new GenericPaginator();		
		IBIObjectDAO objDAO = DAOFactory.getBIObjectDAO();
		List objectsList = null;
		if (initialPath != null && !initialPath.trim().equals("")) {
			objectsList = objDAO.loadAllBIObjectsFromInitialPath(initialPath);
		} else {
			objectsList = objDAO.loadAllBIObjects();
		}
		
		for (Iterator it = objectsList.iterator(); it.hasNext(); ) {
			BIObject obj = (BIObject) it.next();
			SourceBean rowSB = null;
			if (SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor)) {
				rowSB = makeAdminListRow(obj);
			} else if (SpagoBIConstants.DEV_ACTOR.equalsIgnoreCase(actor)) {
				rowSB = makeDevListRow(obj);
			} else if (SpagoBIConstants.USER_ACTOR.equalsIgnoreCase(actor)) {
				rowSB = makeUserListRow(obj);
			}
			if (rowSB != null) paginator.addRow(rowSB);
		}
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		// filter the list 
		String valuefilter = (String) request.getAttribute(SpagoBIConstants.VALUE_FILTER);
		if (valuefilter != null) {
			String columnfilter = (String) request
					.getAttribute(SpagoBIConstants.COLUMN_FILTER);
			String typeFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_FILTER);
			String typeValueFilter = (String) request
					.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
			list = DelegatedBasicListService.filterList(list, valuefilter, typeValueFilter, 
					columnfilter, typeFilter, getResponseContainer().getErrorHandler());
		}
		
		HashMap parametersMap = new HashMap();
		parametersMap.put(SpagoBIConstants.ACTOR, actor);
		parametersMap.put(SpagoBIConstants.OBJECTS_VIEW, SpagoBIConstants.VIEW_OBJECTS_AS_LIST);
		response.setAttribute("PARAMETERS_MAP", parametersMap);
		
		return list;
	}

	private SourceBean makeUserListRow(BIObject obj) throws Exception {
		Integer visible = obj.getVisible();
		String state = obj.getStateCode();
		if (!"REL".equalsIgnoreCase(state) && !"TEST".equalsIgnoreCase(state)) {
			// the document in development state are excluded
			return null;
		}
		if (visible != null && visible.intValue() == 0) {
			// the document is not visible
			return null;
		}
		
		List functionalities = obj.getFunctionalities();
		int visibleInstances = 0;
		for (Iterator funcIt = functionalities.iterator(); funcIt.hasNext(); ) {
			Integer funcId = (Integer) funcIt.next();
			if (ObjectsAccessVerifier.canTest(obj.getStateCode(), funcId, profile) 
					|| ObjectsAccessVerifier.canExec(obj.getStateCode(), funcId, profile)) {
				visibleInstances++;
			}
		}
		
		if (visibleInstances == 0) {
			// the document does not belong to any folder where the profile has the rigth permissions
			// (i.e.: the document is in REL state but belongs to folders where the profile cannot execute it
			// OR the document is in TEST state but belongs to folders where the profile cannot test it)
			return null;
		}
		
		String actor = null;
		// at this point the document is in REL or TEST state and there is one or more visible instances
		if (obj.getStateCode().equalsIgnoreCase("REL")) actor = SpagoBIConstants.USER_ACTOR;
		else actor = SpagoBIConstants.TESTER_ACTOR;
		
		String rowSBStr = "<ROW ";
		rowSBStr += "		OBJECT_ID=\"" + obj.getId() + "\"";
		rowSBStr += "		LABEL=\"" + obj.getLabel() + "\"";
		rowSBStr += "		NAME=\"" + obj.getName() + "\"";
		rowSBStr += "		DESCRIPTION=\"" + (obj.getDescription() != null ? obj.getDescription() : "") + "\"";
		rowSBStr += "		TYPE=\"" + obj.getBiObjectTypeCode() + "\"";
		rowSBStr += "		STATE=\"" + obj.getStateCode() + "\"";
		rowSBStr += "		ACTOR=\"" + actor + "\"";
		rowSBStr += "		INSTANCES=\"" + visibleInstances + "\"";
		rowSBStr += " 		/>";
		SourceBean rowSB = SourceBean.fromXMLString(rowSBStr);
		return rowSB;
	}

	private SourceBean makeDevListRow(BIObject obj) throws Exception {
		String rowSBStr = "<ROW ";
		rowSBStr += "		OBJECT_ID=\"" + obj.getId() + "\"";
		rowSBStr += "		LABEL=\"" + obj.getLabel() + "\"";
		rowSBStr += "		NAME=\"" + obj.getName() + "\"";
		rowSBStr += "		DESCRIPTION=\"" + (obj.getDescription() != null ? obj.getDescription() : "") + "\"";
		rowSBStr += "		TYPE=\"" + obj.getBiObjectTypeCode() + "\"";
		rowSBStr += "		STATE=\"" + obj.getStateCode() + "\"";
		
		int visibleInstances = 0;
		List functionalities = obj.getFunctionalities();
		for (Iterator funcIt = functionalities.iterator(); funcIt.hasNext(); ) {
			Integer funcId = (Integer) funcIt.next();
			if (ObjectsAccessVerifier.canDev(obj.getStateCode(), funcId, profile)
					|| ObjectsAccessVerifier.canExec(obj.getStateCode(), funcId, profile)) {
				visibleInstances++;
			}
		}
		
		if (visibleInstances == 0) {
			// the document does not belong to any folder where the profile has the rigth permissions
			// (i.e.: the document is in REL state but belongs to folders where the profile cannot execute it
			// OR the document is in DEV state but belongs to folders where the profile cannot develope it)
			return null;
		}
		
		// at this point the document is in DEV or REL state and there is one or more visible instances
		boolean canDev = true;
		if (obj.getStateCode().equalsIgnoreCase("REL")) canDev = false;
		
		rowSBStr += "		INSTANCES=\"" + visibleInstances + "\"";
		rowSBStr += "		canDev=\"" + canDev + "\"";
		rowSBStr += " 		/>";
		SourceBean rowSB = SourceBean.fromXMLString(rowSBStr);
		return rowSB;
	}

	private SourceBean makeAdminListRow(BIObject obj) throws Exception {
		String rowSBStr = "<ROW ";
		rowSBStr += "		OBJECT_ID=\"" + obj.getId() + "\"";
		rowSBStr += "		LABEL=\"" + obj.getLabel() + "\"";
		rowSBStr += "		NAME=\"" + obj.getName() + "\"";
		rowSBStr += "		DESCRIPTION=\"" + (obj.getDescription() != null ? obj.getDescription() : "") + "\"";
		rowSBStr += "		TYPE=\"" + obj.getBiObjectTypeCode() + "\"";
		rowSBStr += "		STATE=\"" + obj.getStateCode() + "\"";
		
		List functionalities = obj.getFunctionalities();
		int visibleInstances = 0;
		if (initialPath != null && !initialPath.trim().equals("")) {
			// in case of local administrator, he can admin only a part of the instances of the document
			for (Iterator funcIt = functionalities.iterator(); funcIt.hasNext(); ) {
				Integer funcId = (Integer) funcIt.next();
				LowFunctionality folder = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(funcId, false);
				String folderPath = folder.getPath();
				if (folderPath.equalsIgnoreCase(initialPath) || folderPath.startsWith(initialPath + "/")) {
					visibleInstances++;
				}
			}
			if (visibleInstances == 0) return null;
		} else {
			// in case of global administrator, he can admin all the instances of the document
			visibleInstances = functionalities.size();
		}
		rowSBStr += "		INSTANCES=\"" + visibleInstances + "\"";
		
		boolean canExec = false;
		for (Iterator funcIt = functionalities.iterator(); funcIt.hasNext(); ) {
			Integer funcId = (Integer) funcIt.next();
			if (ObjectsAccessVerifier.canExec(obj.getStateCode(), funcId, profile)) {
				canExec = true;
				break;
			}
		}
		rowSBStr += "		canExec=\"" + canExec + "\"";
		rowSBStr += " 		/>";
		SourceBean rowSB = SourceBean.fromXMLString(rowSBStr);
		return rowSB;
	}

	private SourceBean makeUserListConfiguration() throws SourceBeanException {
		String moduleConfigStr = "";
		moduleConfigStr += "<CONFIG title=\"SBISet.exeObjects.titleList\">";
		moduleConfigStr += "	<QUERIES/>";
		moduleConfigStr += "	<COLUMNS>";
		moduleConfigStr += "		<COLUMN label=\"ID\" name=\"OBJECT_ID\" hidden=\"true\" />";
		moduleConfigStr += "		<COLUMN label=\"ACTOR\" name=\"ACTOR\" hidden=\"true\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnLabel\" name=\"LABEL\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnName\" name=\"NAME\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnDescr\" name=\"DESCRIPTION\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnType\" name=\"TYPE\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnState\" name=\"STATE\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.instancesNumber\" name=\"INSTANCES\" horizontal-align=\"center\"/>";
		moduleConfigStr += "	</COLUMNS>";
		moduleConfigStr += "	<CAPTIONS>";
		moduleConfigStr += "	<EXEC_CAPTION  confirm=\"FALSE\" image=\"/img/execObject.gif\" label=\"SBISet.objects.captionExecute\">" +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ExecuteBIObjectModule.MODULE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.MESSAGEDET + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"ACTOR\"/> " +
		   				   "	</EXEC_CAPTION>";
		moduleConfigStr += "	</CAPTIONS>";
		moduleConfigStr += "	<BUTTONS>";
		moduleConfigStr += "		<CHANGE_VIEW_BUTTON confirm=\"FALSE\" image=\"/img/treeView.png\" label=\"SBISet.objects.treeViewButt\"> " +
		   				   "			<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"BIObjectsPage\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.USER_ACTOR + "\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.OBJECTS_VIEW + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.VIEW_OBJECTS_AS_TREE + "\"/> " +
		   				   "		</CHANGE_VIEW_BUTTON>";	
      	moduleConfigStr += "	</BUTTONS>";
		moduleConfigStr += "</CONFIG>";
		SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
		return moduleConfig;
	}

	private SourceBean makeDevListConfiguration() throws SourceBeanException {
		String moduleConfigStr = "";
		moduleConfigStr += "<CONFIG title=\"SBISet.devObjects.titleList\">";
		moduleConfigStr += "	<QUERIES/>";
		moduleConfigStr += "	<COLUMNS>";
		moduleConfigStr += "		<COLUMN label=\"ID\" name=\"OBJECT_ID\" hidden=\"true\" />";
		moduleConfigStr += "		<COLUMN label=\"canDev\" name=\"canDev\" hidden=\"true\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnLabel\" name=\"LABEL\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnName\" name=\"NAME\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnDescr\" name=\"DESCRIPTION\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnType\" name=\"TYPE\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnState\" name=\"STATE\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.instancesNumber\" name=\"INSTANCES\" horizontal-align=\"center\"/>";
		moduleConfigStr += "	</COLUMNS>";
		moduleConfigStr += "	<CAPTIONS>";
		moduleConfigStr += "	<EXEC_CAPTION  confirm=\"FALSE\" image=\"/img/execObject.gif\" label=\"SBISet.objects.captionExecute\">" +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ExecuteBIObjectModule.MODULE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.MESSAGEDET + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.DEV_ACTOR + "\"/> " +
		   				   "	</EXEC_CAPTION>";
		moduleConfigStr += "	<DETAIL_CAPTION  confirm=\"FALSE\" image=\"/img/detail.gif\" label=\"SBISet.objects.captionDetail\">" +
		   				   "		<CONDITIONS>" +
		   				   "			<PARAMETER name=\"canDev\" scope=\"LOCAL\"  value=\"true\"/> " +
		   				   "		</CONDITIONS> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + DetailBIObjectModule.MODULE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.MESSAGE_DETAIL + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.DETAIL_SELECT + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.DEV_ACTOR + "\"/> " +
		   				   "	</DETAIL_CAPTION>";
		moduleConfigStr += "	<DELETE_CAPTION  confirm=\"TRUE\" image=\"/img/erase.gif\" label=\"SBISet.objects.captionErase\">" +
		   				   "		<CONDITIONS>" +
		   				   "			<PARAMETER name=\"canDev\" scope=\"LOCAL\"  value=\"true\"/> " +
		   				   "		</CONDITIONS> " +		
						   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + DetailBIObjectModule.MODULE_PAGE + "\"/> " +
						   "		<PARAMETER name=\"" + ObjectsTreeConstants.MESSAGE_DETAIL + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.DETAIL_DEL + "\"/> " +
						   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
						   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.DEV_ACTOR + "\"/> " +
						   "	</DELETE_CAPTION>";
		moduleConfigStr += "	</CAPTIONS>";
		moduleConfigStr += "	<BUTTONS>";
		moduleConfigStr += "		<INSERT_BUTTON confirm=\"FALSE\" image=\"/img/new.png\" label=\"SBISet.devObjects.newObjButt\"> " +
		   				   "			<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + DetailBIObjectModule.MODULE_PAGE + "\"/> " +
		   				   "			<PARAMETER name=\"" + ObjectsTreeConstants.MESSAGE_DETAIL + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.DETAIL_NEW + "\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.DEV_ACTOR + "\"/> " +
		   				   "		</INSERT_BUTTON>";	
		moduleConfigStr += "		<CHANGE_VIEW_BUTTON confirm=\"FALSE\" image=\"/img/treeView.png\" label=\"SBISet.objects.treeViewButt\"> " +
		   				   "			<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"BIObjectsPage\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.DEV_ACTOR + "\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.OBJECTS_VIEW + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.VIEW_OBJECTS_AS_TREE + "\"/> " +
		   				   "		</CHANGE_VIEW_BUTTON>";	
		moduleConfigStr += "		<BACK_BUTTON confirm=\"FALSE\" image=\"/img/back.png\" label=\"SBISet.objects.backButt\" onlyPortletRunning=\"true\"> " +
        				   "			<PARAMETER name=\"ACTION_NAME\" scope=\"\" type=\"ABSOLUTE\" value=\"START_ACTION\"/> " +
        				   "			<PARAMETER name=\"PUBLISHER_NAME\" scope=\"\" type=\"ABSOLUTE\" value=\"LoginSBIDevelopmentContextPublisher\"/> " +
        				   "			<PARAMETER name=\"" + LightNavigationManager.LIGHT_NAVIGATOR_RESET + "\" scope=\"\" type=\"ABSOLUTE\" value=\"true\"/> " +
        				   "		</BACK_BUTTON>";
      	moduleConfigStr += "	</BUTTONS>";
		moduleConfigStr += "</CONFIG>";
		SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
		return moduleConfig;
	}

	private SourceBean makeAdminListConfiguration() throws SourceBeanException {
		String moduleConfigStr = "";
		moduleConfigStr += "<CONFIG title=\"SBISet.objects.titleList\">";
		moduleConfigStr += "	<QUERIES/>";
		moduleConfigStr += "	<COLUMNS>";
		moduleConfigStr += "		<COLUMN label=\"ID\" name=\"OBJECT_ID\" hidden=\"true\" />";
		moduleConfigStr += "		<COLUMN label=\"canExec\" name=\"canExec\" hidden=\"true\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnLabel\" name=\"LABEL\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnName\" name=\"NAME\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnDescr\" name=\"DESCRIPTION\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnType\" name=\"TYPE\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.columnState\" name=\"STATE\" />";
		moduleConfigStr += "		<COLUMN label=\"SBISet.objects.instancesNumber\" name=\"INSTANCES\" horizontal-align=\"center\"/>";
		moduleConfigStr += "	</COLUMNS>";
		moduleConfigStr += "	<CAPTIONS>";
		moduleConfigStr += "	<EXEC_CAPTION  confirm=\"FALSE\" image=\"/img/execObject.gif\" label=\"SBISet.objects.captionExecute\">" +
		//				   "		<CONDITIONS>" +
		//				   "			<PARAMETER name=\"canExec\" scope=\"LOCAL\"  value=\"true\"/> " +
		//				   "		</CONDITIONS> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ExecuteBIObjectModule.MODULE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.MESSAGEDET + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.ADMIN_ACTOR + "\"/> " +
		   				   "	</EXEC_CAPTION>";
		moduleConfigStr += "	<DETAIL_CAPTION  confirm=\"FALSE\" image=\"/img/detail.gif\" label=\"SBISet.objects.captionDetail\">" +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + DetailBIObjectModule.MODULE_PAGE + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.MESSAGE_DETAIL + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.DETAIL_SELECT + "\"/> " +
		   				   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
		   				   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.ADMIN_ACTOR + "\"/> " +
		   				   "	</DETAIL_CAPTION>";
		moduleConfigStr += "	<DELETE_CAPTION  confirm=\"TRUE\" image=\"/img/erase.gif\" label=\"SBISet.objects.captionErase\">" +
						   "		<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + DetailBIObjectModule.MODULE_PAGE + "\"/> " +
						   "		<PARAMETER name=\"" + ObjectsTreeConstants.MESSAGE_DETAIL + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.DETAIL_DEL + "\"/> " +
						   "		<PARAMETER name=\"" + ObjectsTreeConstants.OBJECT_ID + "\" scope=\"LOCAL\" type=\"RELATIVE\" value=\"OBJECT_ID\"/> " +
						   "		<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.ADMIN_ACTOR + "\"/> " +
						   "	</DELETE_CAPTION>";
		moduleConfigStr += "	</CAPTIONS>";
		moduleConfigStr += "	<BUTTONS>";
		
		if(ChannelUtilities.isWebRunning()) {
			moduleConfigStr += "		<INSERT_BUTTON confirm=\"FALSE\" image=\"/img/new.png\" label=\"SBISet.devObjects.newObjButt\"> " +
			   				   "			<PARAMETER name=\"" + ObjectsTreeConstants.PAGE + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + DetailBIObjectModule.MODULE_PAGE + "\"/> " +
			   				   "			<PARAMETER name=\"" + ObjectsTreeConstants.MESSAGE_DETAIL + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + ObjectsTreeConstants.DETAIL_NEW + "\"/> " +
			   				   "			<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.ADMIN_ACTOR + "\"/> " +
			   				   "		</INSERT_BUTTON>";		
		}
		
		moduleConfigStr += "		<CHANGE_VIEW_BUTTON confirm=\"FALSE\" image=\"/img/treeView.png\" label=\"SBISet.objects.treeViewButt\"> " +
		   				   "			<PARAMETER name=\"PAGE\" scope=\"\" type=\"ABSOLUTE\" value=\"BIObjectsPage\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.ACTOR + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.ADMIN_ACTOR + "\"/> " +
		   				   "			<PARAMETER name=\"" + SpagoBIConstants.OBJECTS_VIEW + "\" scope=\"\" type=\"ABSOLUTE\" value=\"" + SpagoBIConstants.VIEW_OBJECTS_AS_TREE + "\"/> " +
		   				   "		</CHANGE_VIEW_BUTTON>";	
		moduleConfigStr += "		<BACK_BUTTON confirm=\"FALSE\" image=\"/img/back.png\" label=\"SBISet.objects.backButt\"  onlyPortletRunning=\"true\"> " +
        				   "			<PARAMETER name=\"ACTION_NAME\" scope=\"\" type=\"ABSOLUTE\" value=\"START_ACTION\"/> " +
        				   "			<PARAMETER name=\"PUBLISHER_NAME\" scope=\"\" type=\"ABSOLUTE\" value=\"LoginSBIAdministrationContextPublisher\"/> " +
        				   "			<PARAMETER name=\"" + LightNavigationManager.LIGHT_NAVIGATOR_RESET + "\" scope=\"\" type=\"ABSOLUTE\" value=\"true\"/> " +
        				   "		</BACK_BUTTON>";
      	moduleConfigStr += "	</BUTTONS>";
		moduleConfigStr += "</CONFIG>";
		SourceBean moduleConfig = SourceBean.fromXMLString(moduleConfigStr);
		return moduleConfig;
	}
	
}
