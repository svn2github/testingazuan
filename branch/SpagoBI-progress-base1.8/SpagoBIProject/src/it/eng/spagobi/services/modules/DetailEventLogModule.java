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

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.EventLogDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.SubreportDAOHibImpl;
import it.eng.spagobi.events.EventsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Gioia
 *
 */
public class DetailEventLogModule extends AbstractModule {
	public void init(SourceBean config) {
	}
	
		
	public void service(SourceBean request, SourceBean response) throws Exception {	
		
		SessionContainer permanentSession =  getRequestContainer().getSessionContainer().getPermanentContainer();
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		
		String idStr = (String)request.getAttribute("id");
		String userStr = (String)request.getAttribute("user");
		String dateStr = (String)request.getAttribute("date");
		
		EventLogDAOHibImpl eventLogDAO = new EventLogDAOHibImpl();
		EventLog firedEvent = eventLogDAO.loadEventLog(idStr, userStr, dateStr);
		response.setAttribute("firedEvent", firedEvent);
		Map eventParams = EventsManager.parseParamsStr(firedEvent.getParams());
		response.setAttribute("status", eventParams.get("operation-exit-status"));
		BIObjectDAOHibImpl biObjectDAO = new BIObjectDAOHibImpl();
		String jcrPath = (String)eventParams.get("biobj-path");
		if(jcrPath.endsWith("/template")) {
	 		int lastslash = jcrPath.lastIndexOf("/");
	 		jcrPath = jcrPath.substring(0, lastslash);
	 	}
		BIObject biObject = biObjectDAO.loadBIObjectForDetail(jcrPath);
		//BIObject biObject = null;
		response.setAttribute("biobject", biObject);
		SubreportDAOHibImpl subreportDAOHibImpl = new SubreportDAOHibImpl();
		List list = subreportDAOHibImpl.loadSubreportsByMasterRptId(biObject.getId());
		List biObjectList = new ArrayList();
		for(int i = 0; i < list.size(); i++) {
			Subreport subreport = (Subreport)list.get(i);
			BIObject biobj = biObjectDAO.loadBIObjectForDetail(subreport.getSub_rpt_id());
			biObjectList.add(biobj);
		}
		response.setAttribute("linkedBIObjects", biObjectList);
		response.setAttribute("PUBLISHER_NAME", "ExecBiobjEventLogDetailPublisher");
		
	}
}
