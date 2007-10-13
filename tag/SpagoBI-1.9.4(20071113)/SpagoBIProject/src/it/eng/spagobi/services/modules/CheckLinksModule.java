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
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.AbstractHibernateConnectionCheckListModule;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;

/**
 * @author Gioia
 *
 */
public class CheckLinksModule extends AbstractHibernateConnectionCheckListModule {
	
	protected IEngUserProfile profile = null;
	protected String initialPath = null;
	
	public void save() throws Exception {
		super.save();
		
		String subjectIdName = (String)((SourceBean) config.getAttribute("KEYS.SUBJECT")).getAttribute("key");
		String masterReportIdStr = (String)getAttribute(subjectIdName, _request);
		Integer masterReportId = new Integer(masterReportIdStr);		
		SourceBean checkedObjects = getCheckedObjects();
		List checkedObjectsList = checkedObjects.getAttributeAsList(OBJECT);
				
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			subrptdao.eraseSubreportByMasterRptId(masterReportId);
			for(int i = 0; i < checkedObjectsList.size(); i++) {
				SourceBean subreport = (SourceBean)checkedObjectsList.get(i);
				String key = getObjectKey(subreport);
				Integer subReportId = new Integer(key);
				subrptdao.insertSubreport(new Subreport(masterReportId, subReportId));
			}
			
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","saveSubreportsConfiguration","Cannot erase/insert subreports from/into db", e);
		}
	}
	
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		
		RequestContainer requestContainer = this.getRequestContainer();	
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		SessionContainer permanentSession = sessionContainer.getPermanentContainer();
		profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String actor = (String) sessionContainer.getAttribute(SpagoBIConstants.ACTOR);
        
		String modality = ChannelUtilities.getPreferenceValue(requestContainer, BIObjectsModule.MODALITY, BIObjectsModule.ENTIRE_TREE);
		if (modality != null && modality.equalsIgnoreCase(BIObjectsModule.FILTER_TREE)) {
			initialPath = (String)ChannelUtilities.getPreferenceValue(requestContainer, TreeObjectsModule.PATH_SUBTREE, "");
		}
        String objIdStr = (String) sessionContainer.getAttribute("SUBJECT_ID");
        Integer objId = null;
        if (objIdStr != null) objId = new Integer (objIdStr);
		
		PaginatorIFace paginator = new GenericPaginator();		
		IBIObjectDAO objDAO = DAOFactory.getBIObjectDAO();
		List objectsList = null;
		if (initialPath != null && !initialPath.trim().equals("")) {
			objectsList = objDAO.loadAllBIObjectsFromInitialPath(initialPath);
		} else {
			objectsList = objDAO.loadAllBIObjects();
		}
		
		String checked = (String)request.getAttribute("checked");
		if(checked==null){
			checked = "true";
		}
		if(checked.equals("true")){
		//if the request is to show only checked objects (it is settled by default when page is loaded at the first time
			for (Iterator it = objectsList.iterator(); it.hasNext(); ) {
				BIObject obj = (BIObject) it.next();
				if (objId != null && obj.getId().equals(objId)) continue;
				//boolean bool = isCheckedObject(obj.getId().toString());
				if(isCheckedObject(obj.getId().toString())){
					SourceBean rowSB = null;
					if (SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor)) {
						rowSB = makeAdminListRow(obj);
					} else if (SpagoBIConstants.DEV_ACTOR.equalsIgnoreCase(actor)) {
						rowSB = makeDevListRow(obj);
					}
					if (rowSB != null) paginator.addRow(rowSB);
				}
			}
		}
		//else if it is false, show all objects
		else if (checked.equals("false")){
			for (Iterator it = objectsList.iterator(); it.hasNext(); ) {
				BIObject obj = (BIObject) it.next();
				if (objId != null && obj.getId().equals(objId)) continue;
				//boolean bool = isCheckedObject(obj.getId().toString());
				SourceBean rowSB = null;
					if (SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor)) {
						rowSB = makeAdminListRow(obj);
					} else if (SpagoBIConstants.DEV_ACTOR.equalsIgnoreCase(actor)) {
						rowSB = makeDevListRow(obj);
					}
					if (rowSB != null) paginator.addRow(rowSB);
				
			}
		}
		
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		return list;
	}
	
	private SourceBean makeDevListRow(BIObject obj) throws Exception {
		String rowSBStr = "<ROW ";
		rowSBStr += "		OBJ_ID=\"" + obj.getId() + "\"";
		rowSBStr += "		LABEL=\"" + obj.getLabel() + "\"";
		rowSBStr += "		NAME=\"" + obj.getName() + "\"";
		rowSBStr += "		DESCRIPTION=\"" + obj.getDescription() + "\"";
		
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
		rowSBStr += " 		/>";
		SourceBean rowSB = SourceBean.fromXMLString(rowSBStr);
		return rowSB;
	}
	
	private SourceBean makeAdminListRow(BIObject obj) throws Exception {
		
		if (initialPath != null && !initialPath.trim().equals("")) {
			boolean isVisible = false;
			List functionalitiesId = obj.getFunctionalities();
			Iterator it = functionalitiesId.iterator();
			while (it.hasNext()) {
				Integer id = (Integer) it.next();
				LowFunctionality folder = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(id, false);
				String folderPath = folder.getPath();
				if (folderPath.equalsIgnoreCase(initialPath) || folderPath.startsWith(initialPath + "/")) {
					isVisible = true;
					break;
				}
			}
			if (!isVisible) return null;
		}

		String rowSBStr = "<ROW ";
		rowSBStr += "		OBJ_ID=\"" + obj.getId() + "\"";
		rowSBStr += "		LABEL=\"" + obj.getLabel() + "\"";
		rowSBStr += "		NAME=\"" + obj.getName() + "\"";
		rowSBStr += "		DESCRIPTION=\"" + obj.getDescription() + "\"";
		rowSBStr += " 		/>";
		SourceBean rowSB = SourceBean.fromXMLString(rowSBStr);
		return rowSB;
	}
	public boolean isCheckedObject(String objectID) throws Exception{
		boolean isChecked = false;
		SourceBean checkedObjects = getCheckedObjects();
		List checkedObjectsList = checkedObjects.getAttributeAsList("OBJECT");
		Iterator i = checkedObjectsList.iterator();
		while (i.hasNext()){
			SourceBean source = (SourceBean)i.next();
			String objID = (String)source.getAttribute("OBJ_ID");
			if(objID.equals(objectID)){
				isChecked = true;
			}
		}
		
		return isChecked;
	}
}
