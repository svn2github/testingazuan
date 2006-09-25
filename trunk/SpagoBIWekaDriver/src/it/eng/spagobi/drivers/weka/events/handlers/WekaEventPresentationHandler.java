package it.eng.spagobi.drivers.weka.events.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.SubreportDAOHibImpl;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.events.handlers.IEventPresentationHandler;

public class WekaEventPresentationHandler implements IEventPresentationHandler {

	public void loadEventInfo(EventLog event, SourceBean response) throws SourceBeanException, EMFUserError {
		response.setAttribute("firedEvent", event);
		Map eventParams = EventsManager.parseParamsStr(event.getParams());
		String startEventId = (String) eventParams.get("startEventId");
		if (startEventId != null) {
			// it's an end process event
			response.setAttribute("startEventId", startEventId);
			String result = (String) eventParams.get("operation-result");
			response.setAttribute("operation-result", result);
		} else {
			// it's an end process event, nothing more to do
		}
		BIObjectDAOHibImpl biObjectDAO = new BIObjectDAOHibImpl();
		String biobjectIdStr = (String) eventParams.get("biobjectId");
		Integer biObjectId = new Integer(biobjectIdStr);
		BIObject biObject = biObjectDAO.loadBIObjectById(biObjectId);
//		String jcrPath = (String) eventParams.get("biobj-path");
//		if (jcrPath.endsWith("/template")) {
//	 		int lastslash = jcrPath.lastIndexOf("/");
//	 		jcrPath = jcrPath.substring(0, lastslash);
//	 	}
//		BIObject biObject = biObjectDAO.loadBIObjectForDetail(jcrPath);
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
		response.setAttribute("PUBLISHER_NAME", "WekaExecutionEventLogDetailPublisher");
	}
	
}
