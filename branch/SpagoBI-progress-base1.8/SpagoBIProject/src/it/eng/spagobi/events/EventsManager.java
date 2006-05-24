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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Gioia
 *
 */
public class EventsManager {
	
	private Map events  = new HashMap();	
	private Map firedEvents  = new HashMap();
	
	private long nextFreeId = 0;
	
	public static class Event {
		public String id;
		public String user;
	}
	
	public static class FiredEvent {
		public String id;
		public String user;
		public Date date;
		public String desc; 
		public String params;		
	}
	
	private static EventsManager instance = null;
	
	public static EventsManager getInstance(){
		if(instance == null) instance = new EventsManager();
		return instance;
	}
	
	private EventsManager(){}
	
	public String registerEvent(String user) {
		Event event = new Event();
		event.id = "" + nextFreeId;
		event.user = user;
		events.put("" + nextFreeId, event);
		nextFreeId++;
		return event.id;
	}
	
	public void registerHandler(long eventId, Object handler) {
		
	}
	
	public void fireEvent(String eventId, String user, String desc, Map params) {
		Event event = (Event)events.get(eventId);
		FiredEvent firedEvent = new FiredEvent();
		firedEvent.id = event.id;
		firedEvent.user = event.user;
		firedEvent.date = new Date();
		firedEvent.desc = desc;
		firedEvents.put(eventId, firedEvent);
	}
	
	public List getFiredEvents(String user) {
		List firedEventsList = new ArrayList();
		Iterator it = firedEvents.keySet().iterator();
		while(it.hasNext()){
			FiredEvent firedEvent = (FiredEvent)firedEvents.get(it.next());
			if(firedEvent.user.equalsIgnoreCase(user))
				firedEventsList.add(firedEvent);
		}
		return firedEventsList;
	}
	
	public List getFiredEventsOrdered(String user) {
		List firedEventsList = new ArrayList();
		Iterator it = firedEvents.keySet().iterator();
		TreeMap tree = new TreeMap();
		while(it.hasNext()){
			FiredEvent firedEvent = (FiredEvent)firedEvents.get(it.next());
			if(firedEvent.user.equalsIgnoreCase(user))
				tree.put(firedEvent.date, firedEvent);
		}
		
		it = tree.keySet().iterator();
		while(it.hasNext()){
			FiredEvent firedEvent = (FiredEvent)tree.get(it.next());
			firedEventsList.add(firedEvent);
		}
		
		return firedEventsList;
	}
	
	
}
