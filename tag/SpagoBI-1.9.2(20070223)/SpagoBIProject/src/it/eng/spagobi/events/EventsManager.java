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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.EventLog;
import it.eng.spagobi.bo.dao.hibernate.EventLogDAOHibImpl;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

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
	
	public static final String DEFAULT_EVENT_PRESENTAION_HANDLER_CLASS_NAME = "it.eng.spagobi.events.handlers.DefaultEventPresentationHandler";
	
	//private EventDAOHibImpl eventDAO = new EventDAOHibImpl();
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
//	public Integer registerEvent(String user) {
//		Integer id = null;
//		try {
//			id = eventDAO.registerEvent(user);
//		} catch (EMFUserError e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		
//		return id;
//	}
	
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
//	public void fireEvent(String eventId, String user, String desc, String params) {	
//		
//		EventLog eventLog = new EventLog();
//		eventLog.setId(new Integer(eventId));
//		eventLog.setUser(user);
//		eventLog.setDesc(desc);
//		eventLog.setDate(new Timestamp(System.currentTimeMillis()));
//		eventLog.setParams(params);
//		
//		try {
//			eventLogDAO.insertEventLog(eventLog);
//		} catch (EMFUserError e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	public Integer registerEvent(String user, String desc, String params, List roles) {
		return registerEvent(user, desc, params, roles, EventsManager.DEFAULT_EVENT_PRESENTAION_HANDLER_CLASS_NAME);
	}
	
	/**
	 * Register an event given its user name, description and parameters
	 * 
	 * @param user The user who generated the event
	 * @param desc a description provided by the agent
	 * @param params parameters provided by the agent (usefull for the handlers configuration)
	 * @param handler The presentation class (implementing <code>it.eng.spagobi.events.handlers.IEventPresentationHandler</code>) for the event
	 */
	public Integer registerEvent(String user, String desc, String params, List roles, String handler) {	
		Integer id = null;
		EventLog eventLog = new EventLog();
		eventLog.setUser(user);
		eventLog.setDesc(desc);
		eventLog.setDate(new Timestamp(System.currentTimeMillis()));
		eventLog.setParams(params);
		eventLog.setHandler(handler);
		eventLog.setRoles(roles);
		try {
			id = eventLogDAO.insertEventLog(eventLog);
		} catch (EMFUserError e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "registerEvent", "Error while registering event generated by user '" + user + "' at " + eventLog.getDate().toString() + " with decription " + desc, e);
		}
		return id;
	}
	
	/**
	 * Fire a registered event
	 * 
	 * @param id the event unique id
	 * @param user the user that have registered the event
	 * @param desc a description provided by the agent that fired the event
	 * @param params parameters provided by the agent that fired the event (usefull for the handlers configuration)
	 */
//	public void fireEvent(String eventId, String user, String desc, Map params) {	
//		fireEvent(eventId, user, desc, getParamsStr(params));
//	}
	
	/**
	 * Register an event given its user name, description and parameters
	 * 
	 * @param user The user who generated the event
	 * @param desc a description provided by the agent
	 * @param params parameters provided by the agent (usefull for the handlers configuration)
	 * @param roles The list of event correlated roles
	 */
//	public void registerEvent(String user, String desc, Map params, List roles) {	
//		registerEvent(user, desc, getParamsStr(params), roles);
//	}
	
	/**
	 * Get a list of all fired events registered by the given user and 
	 * ordered by date
	 * @param user 
	 * @return
	 */
//	public List getFiredEvents(String user) {
//		
//		List firedEventsList = null;
//				
//		try {
//			firedEventsList = eventLogDAO.loadEventsLogByUser(user);
//		} catch (EMFUserError e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}				
//		
//		Iterator it = firedEventsList.iterator();
//		while(it.hasNext()){
//			EventLog eventLog = (EventLog)it.next();
//		}
//		
//		return firedEventsList;
//	}
	
	/**
	 * Get registered event with the id specified at input
	 * 
	 * @param id The id of the registered event
	 * @return The EventLog object with the id specified at input
	 */
	public EventLog getRegisteredEvent(Integer id) {
		EventLog event = null;
		try {
			event = eventLogDAO.loadEventLogById(id);
		} catch (EMFUserError e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getRegisteredEvent", "The event with id = " + id + " was not found.", e);
		}
		return event;
	}
	
	/**
	 * Get the list of registered events (generated by the user at input) ordered by date
	 * 
	 * @param user The name of the user who generated the events
	 * @return The list of events generated by the user
	 */
	public List getRegisteredEvents(IEngUserProfile profile) {
		List registeredEventsList = null;
		try {
			registeredEventsList = eventLogDAO.loadEventsLogByUser(profile);
		} catch (EMFUserError e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getRegisteredEvents", "Error while loading events list for the user '" + profile.getUserUniqueIdentifier().toString(), e);
		}
		return registeredEventsList;
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
