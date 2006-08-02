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
package it.eng.spagobi.events;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.bo.dao.hibernate.EventDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.EventLogDAOHibImpl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class menage SpagoBI Event System
 * 
 * @author Gioia
 * 
 * TODO add logging
 */
public class EventsManager {
	
	private EventDAOHibImpl eventDAO = new EventDAOHibImpl();
	private EventLogDAOHibImpl eventLogDAO = new EventLogDAOHibImpl();
	
	
	/**
	 *  Singleton design pattern
	 */
	private static EventsManager instance = null;
	
	public static EventsManager getInstance(){
		if(instance == null) instance = new EventsManager();
		return instance;
	}
		
	private EventsManager(){}
	
	
	
	/**
	 * Register a new event for the given user
	 * 
	 * @param user User who want to register the new event
	 * @return The unique id of the newly generated event
	 */
	public Integer registerEvent(String user) {
		Integer id = null;
		try {
			id = eventDAO.registerEvent(user);
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return id;
	}
	
	/**
	 * Associate an handler to the given event. Every time an event is fired all the handlers 
	 * associteted to it are executed by the EventManager.
	 * 
	 * To be implemented .... 
	 * 
	 * @param id the event unique id to which the handler is associated
	 * @param handler the handler to execute when the event is fired
	 * 
	 * TODO decide EventHandler interface
	 * TODO decide EventHandler execution order policy
	 * TODO implement some default EventHandler (i.e. NotificationHandler, HousekeepingHandler, ecc...)
	 */
	public void registerHandler(long id, Object handler) {
		
	}
	
	/**
	 * Fire a registered event
	 * 
	 * @param id the event unique id
	 * @param user the user that have registered the event
	 * @param desc a description provided by the agent that fired the event
	 * @param params parameters provided by the agent that fired the event (usefull for the handlers configuration)
	 */
	public void fireEvent(String eventId, String user, String desc, String params) {	
		
		EventLog eventLog = new EventLog();
		eventLog.setId(new Integer(eventId));
		eventLog.setUser(user);
		eventLog.setDesc(desc);
		eventLog.setDate(new Timestamp(System.currentTimeMillis()));
		eventLog.setParams(params);
		
		try {
			eventLogDAO.insertEventLog(eventLog);
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Fire a registered event
	 * 
	 * @param id the event unique id
	 * @param user the user that have registered the event
	 * @param desc a description provided by the agent that fired the event
	 * @param params parameters provided by the agent that fired the event (usefull for the handlers configuration)
	 */
	public void fireEvent(String eventId, String user, String desc, Map params) {	
		fireEvent(eventId, user, desc, getParamsStr(params));
	}
	
	/**
	 * Get a list of all fired events registered by the given user and 
	 * ordered by date
	 * @param user 
	 * @return
	 */
	public List getFiredEvents(String user) {
		
		List firedEventsList = null;
				
		try {
			firedEventsList = eventLogDAO.loadEventsLogByUser(user);
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		Iterator it = firedEventsList.iterator();
		while(it.hasNext()){
			EventLog eventLog = (EventLog)it.next();
		}
		
		return firedEventsList;
	}
	
	
	public static String getParamsStr(Map params) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = params.keySet().iterator();
		boolean isFirstParameter = true;
		while(it.hasNext()) {
			String pname = (String)it.next();
			String pvalue = (String)params.get(pname);
			if(!isFirstParameter) buffer.append("&");
			else isFirstParameter = false;
			buffer.append(pname + "=" + pvalue);
		}
		return buffer.toString();
	}
	
	public static Map parseParamsStr(String str) {
		Map params = new HashMap();
		String[] parameterPairs = str.split("&");
		for(int i = 0; i < parameterPairs.length; i++) {
			String[] chunks = parameterPairs[i].split("=");
			params.put(chunks[0], chunks[1]);
		}
		return params;
	}
}
