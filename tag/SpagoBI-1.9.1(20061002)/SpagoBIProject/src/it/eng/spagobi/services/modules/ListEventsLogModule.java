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
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.util.StringUtils;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.services.commons.DelegatedBasicListService;
import it.eng.spagobi.utilities.GeneralUtilities;

import java.util.Iterator;
import java.util.List;

/**
 * This class shows events' notification log 
 * 
 * @author Gioia
 *
 */			 
public class ListEventsLogModule extends AbstractBasicListModule {
	
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		RequestContainer requestContainer = getRequestContainer();
		IEngUserProfile profile = (IEngUserProfile) requestContainer.getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		EventsManager eventsManager = EventsManager.getInstance();		
		List firedEventsList = eventsManager.getRegisteredEvents(profile);
        ConfigSingleton config = ConfigSingleton.getInstance();
	    SourceBean formatSB = (SourceBean) config.getAttribute("DATA-ACCESS.DATE-FORMAT");
	    String format = (String) formatSB.getAttribute("format");
	    format = format.replaceAll("D", "d");
	    format = format.replaceAll("m", "M");
	    format = format.replaceAll("Y", "y");
		PaginatorIFace paginator = new GenericPaginator();
		Iterator it = firedEventsList.iterator();
		while (it.hasNext()) {
			EventLog eventLog = (EventLog) it.next();
			String rowSBStr = "<ROW ";
			rowSBStr += "		ID=\"" + eventLog.getId() + "\"";
			String date = StringUtils.dateToString(eventLog.getDate(), format);
			rowSBStr += "		DATE=\"" + date + "\"";
			rowSBStr += "		USER=\"" + eventLog.getUser() + "\"";
			String description = eventLog.getDesc();
			if (description != null) {
				description = GeneralUtilities.replaceInternationalizedMessages(description);
				description = description.replaceAll("<br/>", " ");
				if (description.length() > 50) description = description.substring(0, 50) + "...";
				description = description.replaceAll(">", "&gt;");
				description = description.replaceAll("<", "&lt;");
				description = description.replaceAll("\"", "&quot;");
			}
			rowSBStr += "		DESCRIPTION=\"" + (description != null ? description : "") + "\"";
			rowSBStr += " 		/>";
			SourceBean rowSB = SourceBean.fromXMLString(rowSBStr);
			paginator.addRow(rowSB);
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
		
		return list;
	}
}
