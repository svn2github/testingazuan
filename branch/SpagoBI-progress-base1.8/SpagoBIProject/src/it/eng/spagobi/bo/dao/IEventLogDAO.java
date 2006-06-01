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
package it.eng.spagobi.bo.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.EventLog;

import java.sql.Timestamp;
import java.util.List;

/**
 * Defines the interfaces for all methods needed to insert, modify and deleting an
 * Event object.
 * 
 * @author Gioia
 *
 */
public interface IEventLogDAO {
		
	/**
	 * Loads  an event log
	 * 
	 * @param user The user that has registered the events
	 * @return	A <code>List</code> of <code>EventLog</code> containing all loaded information
	 * @throws	EMFUserError If an Exception occurred
	 */
	public EventLog loadEventLog(String id, String user,  String date) throws EMFUserError;
		
	
	/**
	 * Loads a list of all logs associated to events registered by the specified  <code>user</code>
	 * 
	 * @param user The user that has registered the events
	 * @return	A <code>List</code> of <code>EventLog</code> containing all loaded information
	 * @throws	EMFUserError If an Exception occurred
	 */
	public List loadEventsLogByUser(String user) throws EMFUserError;
	
	/**
	 * Register a new EventLog.
	 * 
	 * @param user The user who want to register a new event
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertEventLog(EventLog eventLog) throws EMFUserError;
	
	/**
	 * Erase an event log. 
	 *  
	 * @param the EventLog to erase
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseEventLog(EventLog eventLog) throws EMFUserError;
	
	/**
	 * Erase all event logs related to events registered by the specificated user. 
	 * 
	 * @param event The object containing all delete information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void eraseEventsLogByUser(String user) throws EMFUserError;
}
